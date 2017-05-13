/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.network;

import com.jme3.math.Vector3f;
import com.jme3.network.HostedConnection;
import control.GameOptions;
import java.util.UUID;
import model.Entities;
import model.EntityContainer;
import model.Player;
import model.Players;
import control.PropertiesHandler;
import control.SendMessageToClient;

/**
 *
 * @author novo
 */
public class NetworkMessageHandling {

    /*
    take each call one message and handle it
    returns false if the deque is empty and no messages are there
    returns true if a massage is handeld
     */
    public static boolean handlePingMessage() {
        NetworkMessages.PingMessage pingMessage = NetworkMessageListener.PING_MESSAGES.poll();

        if (pingMessage != null) {
            System.out.println(pingMessage.hello);

            return true;
        } else {
            return false;
        }
    }

    /*
    take each call one message and handle it
    returns false if the deque is empty and no messages are there
    returns true if a massage is handeld
     */
    public static void handleEntityPositionMessage() {
        NetworkMessages.EntityPositionMessage entityPositionMessage = NetworkMessageListener.ENTITY_POSITION_MESSAGE.poll();

        if (entityPositionMessage != null) {

            // just send the position back without check ... for now ...
            Vector3f newPositonVector = entityPositionMessage.position;
            Vector3f newDirection = entityPositionMessage.direction;
            int entityID = entityPositionMessage.entityID;
            long playerId = entityPositionMessage.payerId;

            System.out.println("Client [" + entityID + "] Validate position: " + newPositonVector);

            NetworkMessages.EntityPositionMessage newPositionMessage = new NetworkMessages.EntityPositionMessage(playerId, entityID, newPositonVector, newDirection);

            // Player player = Players.checkListOfPlayersContains(playerId);
            EntityContainer entityContainer = Entities.getEntityById(entityPositionMessage.entityID);

            entityContainer.destination = newPositionMessage.position;
            entityContainer.lastMoveUpdate = System.currentTimeMillis();
            entityContainer.moveToPositon = true;
            entityContainer.playerId = entityPositionMessage.payerId;

        }
    }

    /**
     * If the client send a registration message with an 0 playerId a new one
     * will be created and a (server side) player will also created if the
     * playerId exsists it will be assumed that the client has to reconnect and
     * the connection will be updated to the new soccet connection
     *
     * @param connection is a client server connection
     * @param registerOnServerMessage
     */
    public static void addPlayer(HostedConnection connection, NetworkMessages.RegisterOnServerMessage registerOnServerMessage) {

        Player player = null;

        // a new player
        if (registerOnServerMessage.playerId == 0L) {
            // create id 
            UUID uuid = UUID.randomUUID();
            registerOnServerMessage.playerId = uuid.getLeastSignificantBits();
            player = new Player(GameOptions.clientStartCredits, connection, registerOnServerMessage.playerId);

            synchronized (Players.getPlayerList()) {
                Players.getPlayerList().add(player);
            }

        } else {
            player = Players.checkListOfPlayersContains(registerOnServerMessage.playerId);

            if (player != null) {
                player.setConnection(connection);
            } else {
                System.out.println("WARNING: client send playerId that didn't exists!");
            }
        }

        if (player != null) {
            registerOnServerMessage.playerId = player.getPlayerId();
            connection.send(registerOnServerMessage);
                    

            System.out.println(" registered client \n name: " + registerOnServerMessage.clienUserName + " id: " + registerOnServerMessage.playerId);
        } else {
            System.out.println(" Player registration failed ");
        }
    }

    public static void handleCreateEntityMessage() {

        NetworkMessages.CreateEntityMessage createEntityMessage = NetworkMessageListener.CREATE_ENTITY_MESSAGES.poll();

        if (createEntityMessage != null) {
            long playerId = createEntityMessage.playerId;

            Player player = Players.checkListOfPlayersContains(playerId);

            if (player != null) {
                int tmp = Entities.getNewEntityId();
                
                // should check new position first !
                EntityContainer entity = new EntityContainer(playerId, tmp, createEntityMessage.name, createEntityMessage.type, createEntityMessage.position);
                
                PropertiesHandler.setProperties(entity);
                                
                
                Entities.ENTITY_CONTAINER.add(entity);
                System.out.println(" new entity created ");
                //player.getEntityList().add(ship);
            } else {
                System.out.println("unknown player tries to create ship ");
            }
        }
    }

}
