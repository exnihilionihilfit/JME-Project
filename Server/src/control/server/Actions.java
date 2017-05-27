/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.server;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author novo
 */
public class Actions implements Runnable {

    private static ServerState SERVER_STATE = ServerState.IS_RUNNING;
    private final String helpInformation = "\n Orders:  \n \t start \t starts the server"
            + " \n \t pause \t paused the server until start is typed"
            + " \n \t restart \t restart the server"
            + " \n \t shutdown \t shutdown the server";

    private boolean isRunning = true;

    public boolean isPaused() {
        return SERVER_STATE.equals(ServerState.IS_PAUSED);
    }

    public boolean isRunning() {
        return SERVER_STATE.equals(ServerState.IS_RUNNING);
    }

    public boolean isShutdown() {
        return SERVER_STATE.equals(ServerState.IS_SHUTDOWN);
    }

    public boolean isRestarting() {
        return SERVER_STATE.equals(ServerState.IS_RESTARTING);
    }

    public void stop() {
        this.isRunning = false;
    }

    @Override
    public void run() {

        while (isRunning) {
            try {
                checkForInput();
                Thread.sleep(150);
            } catch (InterruptedException ex) {
                Logger.getLogger(Actions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void checkForInput() {

        if (this.isRunning) {
            Scanner reader = new Scanner(System.in);  // Reading from System.in

            String input = reader.next();

            String answer = "";

            switch (input) {
                case "start":
                    if (SERVER_STATE.equals(ServerState.IS_RUNNING)) {
                        answer = "server is allready running";
                    } else {
                        SERVER_STATE = ServerState.IS_RUNNING;
                        answer = "starting server";
                    }

                    break;
                case "pause":
                    if (SERVER_STATE.equals(ServerState.IS_PAUSED)) {
                        answer = "server is allready paused";
                    } else {
                        SERVER_STATE = ServerState.IS_PAUSED;
                        answer = "starting server";
                    }
                    break;
                case "restart":
                    if (SERVER_STATE.equals(ServerState.IS_RESTARTING)) {
                        answer = "server is allready restarting";
                    } else {
                        SERVER_STATE = ServerState.IS_RESTARTING;
                        answer = "restarting server";
                    }
                    break;
                case "shutdown":
                    if (SERVER_STATE.equals(ServerState.IS_SHUTDOWN)) {
                        answer = "server is allready shutting down";
                        stop();
                    } else {
                        SERVER_STATE = ServerState.IS_SHUTDOWN;
                        answer = "shutdown server";
                        stop();
                    }
                    break;
                case "help":
                    answer = this.helpInformation;
                    break;

            }

            System.out.println("server: " + answer);
        }
    }

}
