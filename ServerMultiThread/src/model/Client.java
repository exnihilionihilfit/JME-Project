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
public class Client {
    
    private long credits;
    private final List<Spaceship> shipList;
    
    public Client(long credits)
    {
        this.credits = credits;
        this.shipList = new ArrayList<>();
    }
    
    public long getCredits()
    {
        return this.credits;
    }
    public void addCredits(long income)
    {
        this.credits += income;
    }
    
    public void removeCredits(long outcome)
    {        
        this.credits -= outcome;
    }
    
    public void addShipToList(Spaceship spaceship)
    {
        if(spaceship != null)
        {
              this.shipList.add(spaceship);
        }      
    }
    public List<Spaceship> getShipList()
    {
        return this.shipList;
    }

    public void addHostedConnection(HostedConnection source) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
