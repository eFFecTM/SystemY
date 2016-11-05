package JJTP_DS_UA;

import java.net.Inet4Address;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by jonas on 31-10-2016.
 */
public class ServerRMI extends UnicastRemoteObject implements ServerRMIinterface
{
    NameServer ns;


    public ServerRMI(NameServer ns) throws RemoteException{
        super();
        this.ns = ns;
    }

    public Inet4Address findFile (String fileName) throws RemoteException
    {
        return ns.lookup(fileName);
    }

    public int checkAmountOfNodes()
    {
        return ns.nodeMap.keySet().size();
    }

    public boolean checkIfLeftEdge(int nameHash)
    {
        if(nameHash<ns.nodeMap.firstKey())
            return true;
        else
            return false;
    }

    public boolean checkIfRightEdge(int nameHash)
    {
        if(nameHash>ns.nodeMap.lastKey())
            return true;
        else
            return false;
    }

}
