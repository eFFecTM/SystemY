package JJTP_DS_UA;

import java.rmi.*;
import java.util.*;
/**
 * Created by JJTP on 31/10/2016.
 */
public class Node_NameServerComm
{
    public static void SearchFile()
    {
        try {
            String name = "//192.168.1.1/FileServer";
            serverRMIinterface_temp NSI = (serverRMIinterface_temp) Naming.lookup(name);
            System.out.println("Give a filename you like to search: ");
            Scanner scan = new Scanner(System.in);
            String fileName = scan.nextLine();
            String ipAddress = NSI.getOwner(fileName);
            System.out.println("File is at: "+ipAddress);

        } catch(Exception e) {
            System.err.println("FileServerexception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
