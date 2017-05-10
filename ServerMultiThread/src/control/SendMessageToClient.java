/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import control.network.NetworkMessages;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import model.Entities;
import model.EntityContainer;
import model.Player;
import model.Players;

/**
 *
 * @author novo
 */
public class SendMessageToClient implements Runnable {

    private final ArrayList<EntityContainer> filteredEntityContainers = new ArrayList<>();

    private final Queue<EntityContainer> CLIENT_MESSAGES = new LinkedList<>();

    private final int maxMessageSize = 20;
    
    private Player player;
    private boolean toAll;
    
    
    public SendMessageToClient(boolean toAll, Player player)
    {
        this.player = player;
        this.toAll = toAll;
    }

    private void sendMessagesToAll() {

        prepareMessages();

        /**
         * move each entity and only the entities which are moved will be send
         * to reduce traffic. After all any change should set a flag to send it
         * for now only movement will do so Also all other playerId's should set
         * to -1 so nobody could mess with all id's around ;) PROBLEM:
         * java.util.ConcurrentModificationException ^^ should be realy
         * multy-threaded ;) AND: A reconnected player would get the whole list
         * of entities !
         */
        // should be filtedEntities but we need to set proper flags and send client at start all entities once !
        NetworkMessages.EntitiesListMessage entitiesListMessage = new NetworkMessages.EntitiesListMessage(filteredEntityContainers);

        synchronized (Players.getPlayerList()) {
            for (Player player_ : Players.getPlayerList()) {
                if (player_.getConnection() != null) {
                    player_.getConnection().send(entitiesListMessage);
                }
            }
        }
    }
    
    private void sendMessagesToPlayer(Player player)
    {
        prepareMessages();
          NetworkMessages.EntitiesListMessage entitiesListMessage = new NetworkMessages.EntitiesListMessage(filteredEntityContainers);

           if (player.getConnection() != null) {
                    player.getConnection().send(entitiesListMessage);
                }
    }

    @Override
    public void run() {
        
        if(toAll){
        sendMessagesToAll();
        }
        else
        {
            sendMessagesToPlayer(player);
        }

    }

    private void prepareMessages() {
       //SimpleCollision.resetCollided(Entities.ENTITY_CONTAINER);
        synchronized (Entities.ENTITY_CONTAINER) {
            for (EntityContainer entityContainer : Entities.ENTITY_CONTAINER) {

                EntityAction.moveEntityToPosition(entityContainer);

                SimpleCollision.checkCollision(entityContainer, Entities.ENTITY_CONTAINER);

                if (entityContainer.collided || entityContainer.moveToPositon || entityContainer.isNewCreated) {
                    CLIENT_MESSAGES.add(entityContainer);
                    entityContainer.isNewCreated = false;
                    entityContainer.collided = false;

                }
            }
        }

        int i = 0;
        while (i < maxMessageSize) {
            EntityContainer container = CLIENT_MESSAGES.poll();

            if (container != null) {
                filteredEntityContainers.add(container);
            } else {
                break;
            }
            i++;
        }
    }

}
