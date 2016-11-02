package JJTP_DS_UA;

import java.rmi.*;

/**
 * Created by JJTP on 31/10/2016.
 */
public class Node_NameServerCommunication //@TODO vraag: op welke computer staat deze klasse?
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

    public String searchFile(String fileName)
    {
        try
        {
            String ipAddress = NSI.findFile(fileName);
            System.out.println("File is located at: "+ipAddress);
            return ipAddress;
        } catch(Exception e)
        {
            System.err.println("FileServerexception: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
