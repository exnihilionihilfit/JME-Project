/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.entity;

import control.SimpleCollision;
import control.network.NetworkMessages;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Entities;
import model.EntityContainer;
import model.Player;
import model.Players;

/**
 *
 * @author novo
 */
public class HandleEntityCondition implements Runnable {

    private final ArrayList<EntityContainer> filteredEntityContainers = new ArrayList<>();

    private final Deque<EntityContainer> CLIENT_MESSAGES = new ArrayDeque<>();

    private final int maxMessageSize = 20;
    
    private boolean isRunning = true;

 
    public HandleEntityCondition() {

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

                    if (player_.isNew) {
                        sendToSinglePlayer(player_);
                    } else {
                        if(player_.getConnection().getServer().isRunning())
                        {
                           player_.getConnection().send(entitiesListMessage);  
                        }
                       
                    }

                }
            }
        }
        filteredEntityContainers.clear();
    }

    private void sendToSinglePlayer(Player player) {

        NetworkMessages.EntitiesListMessage entitiesListMessageUnfilterd;

        int entitiesListSize = Entities.ENTITY_CONTAINER.size();
        ArrayList<EntityContainer> chunkedList = new ArrayList<>();
        int counter = 0;
        System.out.println("send client gamestate start");
        for (int i = 0; i < entitiesListSize; i++) {
            chunkedList.add(Entities.ENTITY_CONTAINER.get(i));

            if (i % maxMessageSize == 0 || i + 1 == entitiesListSize) {
                entitiesListMessageUnfilterd = new NetworkMessages.EntitiesListMessage(chunkedList);
                player.getConnection().send(entitiesListMessageUnfilterd);
                chunkedList.clear();
            }
           
        }
        System.out.println("send client gamestate finished");
        player.isNew = false;
    }

    @Override
    public void run() {

        while (isRunning) {
            sendMessagesToAll();
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(HandleEntityCondition.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void prepareMessages() {

   

        synchronized (Entities.ENTITY_CONTAINER) {
            
            ArrayList<EntityContainer> cloned = (ArrayList<EntityContainer>) Entities.ENTITY_CONTAINER.clone();
           
            for (EntityContainer entityContainer : cloned) {

                EntityAction.moveEntityToPosition(entityContainer);

                SimpleCollision.checkCollision(entityContainer, cloned);

               if (entityContainer.isChanged()) {
                    CLIENT_MESSAGES.add(entityContainer);
                    entityContainer.confirmeChanges();

               }
            }
        }

        int i = 0;
        
        while (i < maxMessageSize) {
            EntityContainer container = CLIENT_MESSAGES.pollLast();

            if (container != null) {
                filteredEntityContainers.add(container);
            } else {
                break;
            }
            i++;
        }

    }

    public void stop() {
        this.isRunning = false;
    }

}
