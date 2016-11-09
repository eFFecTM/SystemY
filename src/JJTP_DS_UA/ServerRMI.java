/**
 * Created by JJTP on 31-10-2016.
 * This is the class that is used by the nameserver to communicate with a node using RMI.
 */
package JJTP_DS_UA;

import java.net.Inet4Address;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

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
    public Inet4Address findFile (String fileName) throws RemoteException
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
        return ns.nodeMap.get(nodeHash).toString(); //returned : /192.168.1.xxx
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
}
