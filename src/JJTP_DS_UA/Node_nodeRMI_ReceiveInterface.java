/**
 * Created by JJTP on 04/11/16.
 * This interface is used to communicate between 2 nodes using RMI.
 */
package JJTP_DS_UA;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Node_nodeRMI_ReceiveInterface extends Remote
{
    void setNeighbours(int prevHash, int nextHash) throws RemoteException;
    void updateRightNeighbour(int hash) throws RemoteException;
    void updateLeftNeighbour(int hash) throws RemoteException;
    void updateOnlyNode() throws RemoteException;
    void updateFileMarkers(FileMarker fm) throws RemoteException;
    void notifyOwner(String fileName) throws RemoteException;
    void removeFile(String fileName) throws RemoteException;
    void receiveFileAgent(FileAgent agent) throws RemoteException;
    int negotiatePort(String filename, Boolean askFile, String ipDest, int port) throws RemoteException;
}
