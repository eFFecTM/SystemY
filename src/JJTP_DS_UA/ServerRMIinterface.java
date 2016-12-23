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
    Inet4Address findFile(String fileName) throws RemoteException;
    int checkAmountOfNodes() throws RemoteException;
    boolean checkIfLowEdge(int nameHash) throws RemoteException;
    boolean checkIfHighEdge(int nameHash) throws RemoteException;
    boolean checkIfNameExists(String name) throws RemoteException;
    String getIP(int hash)throws RemoteException;
    int[] getIDs(String ipAddr) throws RemoteException;
    int getID(String ipAddr) throws RemoteException;
    void deleteNode(int hash) throws RemoteException;
    int getNodeFromFilename(int fileHash) throws RemoteException;
    int getMapsize()throws RemoteException;
    String getLastNodeIP()throws RemoteException;
}
