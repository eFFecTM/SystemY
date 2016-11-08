/**
 * Created by JJTP on 31-10-2016.
 * This is the class that is used by a node to communicate with the nameserver using RMI.
 */
package JJTP_DS_UA;

import java.net.Inet4Address;
import java.rmi.*;

public class Node_NameServerRMI
{
    ServerRMIinterface NSI;

    public Node_NameServerRMI()
    {
        try
        {
            String location = "//192.168.1.1/FileServer";
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
        try
        {
            return NSI.checkAmountOfNodes();
        } catch(RemoteException e)
        {
            e.printStackTrace();
            return -1;
        }

    }

    public boolean checkIfLeftEdge(int nameHash)
    {
        try
        {
            return NSI.checkIfLeftEdge(nameHash);
        } catch (RemoteException e)
        {
            e.printStackTrace();
            return false;  //@TODO nakijken/vragen over return statement
        }
    }

    public boolean checkIfRightEdge(int nameHash)
    {
        try
        {
            return NSI.checkIfRightEdge(nameHash);
        } catch (RemoteException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkIfNameExists(String name)
    {
        try
        {
            return NSI.checkIfNameExists(name);
        } catch (RemoteException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}