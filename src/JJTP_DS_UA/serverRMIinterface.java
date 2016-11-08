/**
 * Created by JJTP on 25-10-2016.
 * This is the RMI interface between the nameserver and a node.
 */
package JJTP_DS_UA;

import java.net.Inet4Address;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRMIinterface extends Remote
{
    public Inet4Address findFile(String fileName) throws RemoteException;
    public int checkAmountOfNodes();
    public boolean checkIfLeftEdge(int nameHash);
    public boolean checkIfRightEdge(int nameHash);
    public boolean checkIfNameExists(String name);
}
