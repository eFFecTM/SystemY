/**
 * Created by jonas on 5-11-2016.
 */
package JJTP_DS_UA;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class Node_nodeRMI_Transmit
{
    Node node;
    Node_nodeRMI_ReceiveInterface nodeRMIReceive;

    // Node_nodeRMI_Transmit constructor
    public Node_nodeRMI_Transmit(String ipAddr, Node node)
    {
        this.node = node;
        try
        {
            String location = ("//"+ ipAddr + "/NodeSet"); //  voorbeeld: "//192.168.1.1/FileServer"
            nodeRMIReceive = (Node_nodeRMI_ReceiveInterface) Naming.lookup(location);
        } catch(Exception e)
        {
            node.failureOtherNode(ipAddr);
            System.err.println("NodeSet exception: "+ e.getMessage());
            e.printStackTrace();
        }
    }

    // Buren van Node instellen
    public void setNeighbours(int prevHash,int nextHash)
    {
        try
        {
            nodeRMIReceive.setNeighbours(prevHash,nextHash);
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    public void updateRightNeighbour(int hash)
    {
        try
        {
            nodeRMIReceive.updateLeftNeighbour(hash);
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    public void updateLeftNeighbour(int hash)
    {
        try
        {
            nodeRMIReceive.updateRightNeighbour(hash); //update de rechterbuur van de linker waarmee de shutdown node geconnecteerd is
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    public void updateOnlyNode()
    {
        try
        {
            nodeRMIReceive.updateOnlyNode();
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }
}
