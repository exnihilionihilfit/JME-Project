/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.network;

import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import model.Entity;
import model.EntityTypes;
import model.Player;

/**
 *
 * @author novo
 */
public class SendNetworkMessage {

    Client client;

    public SendNetworkMessage(Client client) {
        this.client = client;
    }

    public void sendEntityPositionMessage(Entity entity, Vector3f targetPosition) {
        NetworkMessages.EntityPositionMessage message = new NetworkMessages.EntityPositionMessage(Player.getPlayerId(), entity.getID(), targetPosition, entity.getDirection(), entity.getEntityContainer().getActiveOrder());
        client.send(message);

    }

    public void sendPingMessage(String message) {
        long time = System.currentTimeMillis();
        NetworkMessages.PingMessage pingMessage = new NetworkMessages.PingMessage(Player.getPlayerId(), message, time);
        client.send(pingMessage);
    }

    public void sendCreateEntityMessage(String name, EntityTypes type, Vector3f position) {
        NetworkMessages.CreateEntityMessage createEntityMessage = new NetworkMessages.CreateEntityMessage(Player.getPlayerId(), true, name, type,position);

        client.send(createEntityMessage);
        System.out.println("send create entity message");
    }

}
