package JJTP_DS_UA;

import java.net.Inet4Address;
import java.rmi.*;

/**
 * Created by JJTP on 31/10/2016.
 */
public class Node_NameServerRMI
{
    ServerRMIinterface NSI;

    public Node_NameServerRMI()
    {
        try
        {
            String location = "//192.168.1.1/FileServer"; //@TODO juiste locatie ingeven
            NSI = (ServerRMIinterface) Naming.lookup(location);
        } catch(Exception e)
        {
            System.err.println("FileServer exception: "+ e.getMessage());
            e.printStackTrace();
        }
    }

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

    public int checkAmountOfNodes()
    {
        return NSI.checkAmountOfNodes();
    }

    public boolean checkIfLeftEdge(int nameHash)
    {
        return NSI.checkIfLeftEdge(nameHash);
    }

    public boolean checkIfRightEdge(int nameHash)
    {
        return NSI.checkIfRightEdge(nameHash);
    }
}
