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
public class EntityHandling implements Runnable {

    private final ArrayList<EntityContainer> filteredEntityContainers = new ArrayList<>();

    private final Queue<EntityContainer> CLIENT_MESSAGES = new LinkedList<>();

    private final int maxMessageSize = 20;

    private boolean sendMessages = true;

    public void setSendMessages(boolean value) {
        this.sendMessages = value;
    }

    public EntityHandling() {

    }

    private void sendMessagesToAll() {

        prepareMessages();

        // should be filtedEntities but we need to set proper flags and send client at start all entities once !
        NetworkMessages.EntitiesListMessage entitiesListMessage = new NetworkMessages.EntitiesListMessage(filteredEntityContainers);

        synchronized (Players.getPlayerList()) {
            for (Player player_ : Players.getPlayerList()) {
                if (player_.getConnection() != null) {

                    if (player_.isNew) {
                        sendToSinglePlayer(player_);
                    } else {
                        
                        player_.getConnection().send(entitiesListMessage);
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

        while (sendMessages) {
            sendMessagesToAll();
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(EntityHandling.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void prepareMessages() {

        synchronized (Entities.ENTITY_CONTAINER) {
            
            ArrayList<EntityContainer> cloned = (ArrayList<EntityContainer>) Entities.ENTITY_CONTAINER.clone();
           
            for (EntityContainer entityContainer : cloned) {

                EntityAction.moveEntityToPosition(entityContainer);

                SimpleCollision.checkCollision(entityContainer, cloned);

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
