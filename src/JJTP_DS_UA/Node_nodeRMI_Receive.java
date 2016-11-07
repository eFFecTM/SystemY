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

    public Node_nodeRMI_Receive(Node n) throws RemoteException
        {
            super();
            myNode = n;
        }

    @Override
    public void setNeighbours(int leftHash, int rightHash)
    {
        myNode.prevHash=leftHash;
        myNode.nextHash=rightHash;
    }

}
