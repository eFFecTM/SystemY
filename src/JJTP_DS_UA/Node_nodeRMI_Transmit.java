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
        try
        {
            String location = ("//"+ ipAddr + "/NodeSet"); //  voorbeeld: "//192.168.1.1/FileServer"
            NodeRMIReceive = (Node_nodeRMI_ReceiveInterface) Naming.lookup(location);
        } catch(Exception e)
        {
            System.err.println("NodeSet exception: "+ e.getMessage());
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
