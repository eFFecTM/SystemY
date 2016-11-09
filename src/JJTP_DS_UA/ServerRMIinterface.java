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
    public int checkAmountOfNodes() throws RemoteException;
    public boolean checkIfLowEdge(int nameHash) throws RemoteException;
    public boolean checkIfHighEdge(int nameHash) throws RemoteException;
    public boolean checkIfNameExists(String name) throws RemoteException;
    public String getIP(int hash)throws RemoteException;
    public void deleteNode(int hash) throws RemoteException;
}
