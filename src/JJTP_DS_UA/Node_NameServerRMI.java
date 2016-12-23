/**
 * Created by JJTP on 31-10-2016.
 * This is the class that is used by a node to communicate with the nameserver using RMI.
 */
package JJTP_DS_UA;

import java.net.Inet4Address;
import java.rmi.Naming;
import java.rmi.RemoteException;

// Boven: Node
// Implementeert Interface ServerRMI
public class Node_NameServerRMI
{
    ServerRMIinterface NSI;

    //Node_NameServerRMI Constructor
    public Node_NameServerRMI()
    {
        try
        {
            String location = "//192.168.1.1/FileServer"; // @TODO aangepast naar 32 voor thuisgebruik via wifi (jonas)
            NSI = (ServerRMIinterface) Naming.lookup(location);
        }
        catch(Exception e)
        {
            System.err.println("FileServer exception: "+ e.getMessage());
        }
    }

    // Zoek op welke node deze file staat -> Wordt later in gebruik gesteld
    public Inet4Address findFile(String fileName)
    {
        try
        {
            Inet4Address IP = NSI.findFile(fileName);
            System.out.println("File is located at: "+IP);
            return IP;
        }
        catch(Exception e)
        {
            System.err.println("FileServerexception: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Geeft het aantal nodes in de cirkel terug
    public int checkAmountOfNodes()
    {
        try
        {
            return NSI.checkAmountOfNodes();
        }
        catch(RemoteException e)
        {
            e.printStackTrace();
            return -1;
        }

    }

    // Node wilt weten of het op de laagste rand zit
    public boolean checkIfLowEdge(int nameHash)
    {
        try
        {
            return NSI.checkIfLowEdge(nameHash);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
            return false; // nagevraagd, was in orde
        }
    }

    // Node wilt weten of het op de hoogste rand zit
    public boolean checkIfHighEdge(int nameHash)
    {
        try
        {
            return NSI.checkIfHighEdge(nameHash);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    // Kijkt na of de naam al bestaat
    public boolean checkIfNameExists(String name)
    {
        try
        {
            return NSI.checkIfNameExists(name);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public String getIP(int hash)
    {
        try
        {
            return NSI.getIP(hash);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public int[] getIDs(String ipAddr)
    {
        try
        {
            return NSI.getIDs(ipAddr);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public int getID(String ipAddr)
    {
        try
        {
            return NSI.getID(ipAddr);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public void deleteNode(int hash)
    {
        try
        {
            NSI.deleteNode(hash);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    public int getMapsize()
    {
        try
        {
            return NSI.getMapsize();
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public String getLastNodeIP()
    {
        try
        {
            return NSI.getLastNodeIP();
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public int getNodeFromFilename(int fileHash)
    {
        try
        {
            return NSI.getNodeFromFilename(fileHash);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

}
