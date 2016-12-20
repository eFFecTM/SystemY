/**
 * Created by JJTP on 31-10-2016.
 * This is the class that is used by the nameserver to communicate with a node using RMI.
 */
package JJTP_DS_UA;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.Objects;

// Boven: NameServer
// Implementeert Interface ServerRMIinterface
public class ServerRMI extends UnicastRemoteObject implements ServerRMIinterface
{
    NameServer ns;

    // ServerRMI Constructor
    public ServerRMI(NameServer ns) throws RemoteException
    {
        super();
        this.ns = ns;
    }

    /**
     * When searching for a file, the filename is hashed. Then the algorithm looks for the nearest hash value
     * in the TreeMap. This value is linked to the IP-address of the computer that has the file.
     **/

    @Override
    public Inet4Address findFile (String fileName) throws RemoteException //@fixme als hash = nodeid, geef dat ip terug
    {
        int hash = ns.calcHash(fileName);
        if(ns.nodeMap.lowerKey(hash) == null) // returnt key < dan de meegegeven parameter of null als die niet bestaat
            return ns.nodeMap.get(ns.nodeMap.lastKey()); // returnt de grootste key uit de map (als er geen kleiner bestaat)
        else
            return ns.nodeMap.get(ns.nodeMap.lowerKey(hash));
    }

    // Geeft het aantal nodes in de cirkel terug
    @Override
    public int checkAmountOfNodes()
    {
        return ns.nodeMap.keySet().size();
    }

    // Node wilt weten of het op de laagste rand zit
    @Override
    public boolean checkIfLowEdge(int nameHash)
    {
        if(nameHash <= ns.nodeMap.firstKey())
            return true;
        else
            return false;
    }

    // Node wilt weten of het op de hoogste rand zit
    @Override
    public boolean checkIfHighEdge(int nameHash)
    {
        if(nameHash >= ns.nodeMap.lastKey())
            return true;
        else
            return false;
    }

    // Kijkt na of de naam al bestaat
    @Override
    public boolean checkIfNameExists(String name)
    {
        int hash = ns.calcHash(name);
        return ns.nodeMap.containsKey(hash);
    }

    @Override
    public String getIP(int nodeHash)
    {
        return ns.nodeMap.get(nodeHash).toString().substring(1); //returned : /192.168.1.xxx , substring verwijderd eerste char
    }

    @Override
    public int[] getIDs(String ipAddr) throws RemoteException //itereert over de map tot de ID gevonden is (kan beter bv binair zoeken, of google API gebruiken)
    {
        int ID = -1;
        int[] neighbours = new int[2];
        for(Map.Entry<Integer, Inet4Address> entry : ns.nodeMap.entrySet())
        {
            try
            {
                if(entry.getValue().equals(Inet4Address.getByName(ipAddr)))
                    ID = entry.getKey();
            } catch (UnknownHostException e)
            {
                e.printStackTrace();
            }
        }
        if(ID != -1)
        {
            if(ns.nodeMap.higherKey(ID) == null)
                neighbours[1] = ns.nodeMap.firstKey();//is er geen rechtse (node was grootste, return dan kleinste = rechtse buur)
            else
                neighbours[1] = ns.nodeMap.higherKey(ID);//rechtse buur (groter)

            if(ns.nodeMap.lowerKey(ID) == null)
                neighbours[0] = ns.nodeMap.lastKey();//is er geen linkse (node was kleinste), return de grootse = linkse buur
            else
                neighbours[0] = ns.nodeMap.lowerKey(ID); //linkse buur (kleiner)
        }
        return neighbours;
    }

    @Override
    public int getID(String ipAddr) throws RemoteException //@fixme /'en in de adresse nakijken (volgens jan ok)
    {
        int ID = -1;
        try
        {
            Inet4Address ip = (Inet4Address) Inet4Address.getByName(ipAddr);
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        for(Map.Entry<Integer, Inet4Address> entry : ns.nodeMap.entrySet())
        {
            if(entry.getValue().equals(ipAddr))
                ID = entry.getKey();
        }
        return ID;
    }

    // Verwijderen van een node
    @Override
    public void deleteNode(int hash)
    {
        if(ns.nodeMap.containsKey(hash))
            ns.nodeMap.remove(hash);
        else
            System.err.println("No such Node.");
    }

    public int getNodeFromFilename(int fileHash)
    {
        if (ns.nodeMap.lowerKey(fileHash) == null)
        {
            return ns.nodeMap.lastKey();
        }
        else
        {
            return ns.nodeMap.lowerKey(fileHash);
        }
    }

    @Override
    public int getMapsize() throws RemoteException
    {
        return ns.nodeMap.size();
    }

    @Override
    public String getLastNodeIP() throws RemoteException
    {
        return ns.nodeMap.get(ns.nodeMap.firstKey()).toString().substring(1); //verwijdert de / van het ip adres.
    }
}
