package JJTP_DS_UA;

import java.math.BigDecimal;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by jonas on 5-11-2016.
 */
public class Node_nodeRMI_Transmit
{
    Node_nodeRMI_ReceiveInterface NodeRMIReceive;

    public Node_nodeRMI_Transmit(String ipAddr)
    {
        try {
            String name = "NodeSet";
            Registry registry = LocateRegistry.getRegistry();
            NodeRMIReceive = (Node_nodeRMI_ReceiveInterface) registry.lookup(name);
        } catch (Exception e) {
            System.err.println("ComputePi exception:");
            e.printStackTrace();
        }
    }

    public void updateNewNodeNeighbours(int leftHash,int rightHash)
    {
        try
        {
            NodeRMIReceive.setNeighbours(leftHash,rightHash);
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }
}
