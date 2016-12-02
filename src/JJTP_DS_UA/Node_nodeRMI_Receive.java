/**
 * Created by JJTP on 04/11/16.
 * This class is used to communicate between 2 nodes using RMI. This is the receiving info side.
 */
package JJTP_DS_UA;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Node_nodeRMI_Receive extends UnicastRemoteObject implements Node_nodeRMI_ReceiveInterface
{
    Node myNode;

    // Node_nodeRMI_Receive constructor
    public Node_nodeRMI_Receive(Node n) throws RemoteException
        {
            super();
            myNode = n;
        }

    // Buren van Node instellen
    @Override
    public void setNeighbours(int prevHash, int nextHash)
    {
        myNode.prevHash=prevHash;
        myNode.nextHash=nextHash;
    }

    @Override
    public void updateLeftNeighbour(int hash)
    {
        myNode.prevHash=hash;
    }

    @Override
    public void updateRightNeighbour(int hash) throws RemoteException
    {
        myNode.nextHash=hash; //rechterbuur van de node wordt geupdate door de huidige rechterbuur die weggaat
    }

    @Override
    public void updateOnlyNode()
    {
        myNode.onlyNode = true;
    }

    @Override
    public void updateFileMarkers(FileMarker fm) throws RemoteException
    {
        myNode.updateFileMarker(fm);
    }
}
