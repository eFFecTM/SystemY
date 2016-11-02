package JJTP_DS_UA;

import java.net.Inet4Address;
import java.rmi.*;

/**
 * Created by JJTP on 31/10/2016.
 */
public class Node_NameServerCommunication
{
    serverRMIinterface NSI;

    public Node_NameServerCommunication()
    {
        try
        {
            String location = "//localhost/FileServer"; //@TODO juiste locatie ingeven
            NSI = (serverRMIinterface) Naming.lookup(location);
        } catch(Exception e)
        {
            System.err.println("FileServer exception: "+ e.getMessage());
            e.printStackTrace();
        }
    }

    //@TODO: In de main oproepen?
    public Inet4Address searchFile(String fileName)
    {
        try
        {
            Inet4Address IP = NSI.findFile(fileName);
            System.out.println("File is located at: "+IP);
            return IP;
        } catch(Exception e)
        {
            System.err.println("FileServerexception: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
