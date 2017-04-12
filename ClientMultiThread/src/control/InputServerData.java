/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.util.Scanner;

/**
 *
 * @author chasma
 */
public final class InputServerData {
    
    public static int SERVER_IP_PORT;
    public static int SERVER_UDP_PORT;
    public static String SERVER_ADRESS = "";
    public static boolean IS_VALID_SERVER_DATA;

    public InputServerData(String[] args) {
        checkInputOnStartUp(args);
    }

    
    public void checkInputOnStartUp(String[] args)
    {
         IS_VALID_SERVER_DATA = false;

        if (args.length == 3) {
            SERVER_ADRESS = args[0];
            SERVER_IP_PORT = Integer.parseInt(args[1]);
            SERVER_UDP_PORT = Integer.parseInt(args[2]);

            System.out.println("SERVER ADRESS: " + SERVER_ADRESS);
            System.out.println("SERVER IP PORT: " + SERVER_IP_PORT);
            System.out.println("SERVER UDP PORT: " + SERVER_UDP_PORT);

            IS_VALID_SERVER_DATA = true;
        } else {
            System.out.println("Please enter SERVER and PORT");

            Scanner reader = new Scanner(System.in);  // Reading from System.in
            System.out.print("Enter Adress: ");
            SERVER_ADRESS = reader.next();
            System.out.print("Enter IP Port: ");
            SERVER_IP_PORT = reader.nextInt();
            System.out.print("Enter UDP Port: ");
            SERVER_UDP_PORT = reader.nextInt();

            IS_VALID_SERVER_DATA = true;
        }        
   
    }
    
    public String getAdress()
    {
        return SERVER_ADRESS;
    }
    
    public int getIpPort()
    {
        return SERVER_IP_PORT;
    }
    
    public int getUdpPort()
    {
        return SERVER_UDP_PORT;
    }
    
    public boolean isValidServerData()
    {
        return IS_VALID_SERVER_DATA;
    }
    
    
    
  
    
}
