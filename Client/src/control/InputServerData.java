/**
 * This is for console input for server data.
 * default adress is 'localhost' port udp: 6142 tcp:6143
 * after starting the client you can hit 'n' to use default
 * or you would like to change things then hit 'y'
 */
package control;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * @author chasma
 */
public final class InputServerData {

    public static int SERVER_IP_PORT = 6143;
    public static int SERVER_UDP_PORT = 6142;
    public static String SERVER_ADRESS = "localhost";
    public static boolean IS_VALID_SERVER_DATA = false;

    public static void checkInputOnStartUp(String[] args) {
        IS_VALID_SERVER_DATA = false;

        if (args.length == 3) {
            SERVER_ADRESS = args[0];
            SERVER_IP_PORT = Integer.parseInt(args[1]);
            SERVER_UDP_PORT = Integer.parseInt(args[2]);

            System.out.println("SERVER ADRESS: " + SERVER_ADRESS);
            System.out.println("SERVER IP PORT: " + SERVER_IP_PORT);
            System.out.println("SERVER UDP PORT: " + SERVER_UDP_PORT);

            IS_VALID_SERVER_DATA = true;
        } else if (args.length == 1) {

            if (args[0].equals("-c")) {
                System.out.println("Please enter SERVER and PORT");

                Scanner reader = new Scanner(System.in);  // Reading from System.in

                System.out.println("SERVER ADRESS: " + SERVER_ADRESS);
                System.out.println("SERVER IP PORT: " + SERVER_IP_PORT);
                System.out.println("SERVER UDP PORT: " + SERVER_UDP_PORT);
                System.out.println("Change settings ? Type [y] or [n]");

                String answer = reader.next();

                if (answer.equals("y")) {
                    System.out.print("Enter Adress: ");
                    SERVER_ADRESS = reader.next();
                    System.out.print("Enter IP Port: ");
                    SERVER_IP_PORT = reader.nextInt();
                    System.out.print("Enter UDP Port: ");
                    SERVER_UDP_PORT = reader.nextInt();
                } else if (answer.equals("n")) {
                    System.out.println("default settings in use");
                }

                IS_VALID_SERVER_DATA = true;
            } else if (args[0].equals("--help")) {
                System.out.println("Help (called with --help)");
                System.out.println(" add -c to enter server data via console");

            }
        }

    }

    public static boolean checkServerAdress(String adress) {
        
        adress = adress.trim();
        
        if (adress.equals("localhost")) {
            return true;
        } else {
            String[] parts = adress.split(Pattern.quote("."));

            if (parts.length == 4) {
                return true;
            }
            else
            {
                System.out.println(" server adress "+adress+" not valid");
            }
        }
        return false;
    }

}
