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

    private static long playerId = 0L;

    public static void setPlayerId(long clientID) {
        Player.playerId = clientID;
    }

    public static long getPlayerId() {
        return Player.playerId;
    }
}
