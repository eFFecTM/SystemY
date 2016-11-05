package JJTP_DS_UA;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Jonas on 04/11/16.
 */
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
