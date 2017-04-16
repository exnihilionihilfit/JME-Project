/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author novo
 */
public class Player {
    
   
    private static long clientID = 0L;
    
    public static void setClientID(long clientID)
    {
        Player.clientID = clientID;
    }
    
    public static long getClientID()
    {
        return Player.clientID;
    }
}
