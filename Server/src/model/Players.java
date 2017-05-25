/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author novo
 */
public class Players {

    private static final ArrayList<Player> LIST_OF_PLAYERS = new ArrayList<>();

    public static ArrayList<Player> getPlayerList() {
        return Players.LIST_OF_PLAYERS;
    }

    public static Player checkListOfPlayersContains(long playerId) {
        /**
         *
         * check if player with specific id is allready there and update
         * connection
         */
        synchronized(Players.getPlayerList())
        {
        for (Player player : Players.getPlayerList()) {
            if (player.getPlayerId() == playerId) {
                return player;
            }
        }
        }
        return null;
    }

}
