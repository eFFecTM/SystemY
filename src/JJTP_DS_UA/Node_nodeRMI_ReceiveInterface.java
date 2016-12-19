/**
 * Created by JJTP on 04/11/16.
 * This interface is used to communicate between 2 nodes using RMI.
 */
package JJTP_DS_UA;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Node_nodeRMI_ReceiveInterface extends Remote
{
    public void setNeighbours(int prevHash, int nextHash) throws RemoteException;
    public void updateRightNeighbour(int hash) throws RemoteException;
    public void updateLeftNeighbour(int hash) throws RemoteException;
    public void updateOnlyNode() throws RemoteException;
    public void updateFileMarkers(FileMarker fm) throws RemoteException;
    public boolean notifyOwner(String fileName, int ownHash) throws RemoteException;
    public void receiveFileAgent(FileAgent agent) throws RemoteException;
    public int negotiatePort(String filename, Boolean askFile, String ipDest) throws RemoteException;
}
