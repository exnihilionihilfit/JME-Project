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
public enum EntityTypes {

    NOT_DEFINED("NOTDEFINED"),
    BATTLESHIP("BATTLESHIP"),
    FREIGHTER("FREIGHTER"),
    DRONE("DRONE"),
    SKIFF("SKIFF"),
    EXCHANGE_STATION("EXCHANGE_STATION"),    
    SENSOR_STATION("SENSOR_STATION"),
    ASTEROID("ASTEROID");

    private final String name;

    EntityTypes(String name) {
        this.name = name;
    }

}
