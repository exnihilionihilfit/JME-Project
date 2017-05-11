/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.jme3.network.HostedConnection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chasma
 */
public class Player {

    private long credits;
    private final List<EntityContainer> shipList;
    private HostedConnection connection;
    private long playerId;
    public boolean isNew = true;

    public Player(long credits, HostedConnection connection, long playerId) {
        this.credits = credits;
        this.shipList = new ArrayList<>();
        this.connection = connection;
        this.playerId = playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;

    }

    public long getPlayerId() {
        return this.playerId;
    }

    public HostedConnection getConnection() {
        return this.connection;
    }

    public void setConnection(HostedConnection hostedConnection) {
        this.connection = hostedConnection;
    }

    public long getCredits() {
        return this.credits;
    }

    public void addCredits(long income) {
        this.credits += income;
    }

    public void removeCredits(long outcome) {
        this.credits -= outcome;
    }

    public List<EntityContainer> getEntityList() {
        return this.shipList;
    }

    public void addHostedConnection(HostedConnection source) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
