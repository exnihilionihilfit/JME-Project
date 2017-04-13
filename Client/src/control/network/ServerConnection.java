/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.network;

import main.Main;
import com.jme3.network.Network;
import control.InputServerData;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author novo
 */
public class ServerConnection {

    private Main main;

    public ServerConnection() {

    }

    public ServerConnection(Main main) {
        this.main = main;
    }

    public void connectToServer() {

        if (InputServerData.SERVER_ADRESS != null) {
            if (InputServerData.SERVER_IP_PORT > 0) {

                if (InputServerData.SERVER_UDP_PORT > 0) {
                    try {
                        main.myClient = Network.connectToServer(InputServerData.SERVER_ADRESS, InputServerData.SERVER_IP_PORT, InputServerData.SERVER_UDP_PORT);

                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        }
    }

}
