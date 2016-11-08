package JJTP_DS_UA;

import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * Created by jonas on 5-11-2016.
 */
public class Node_nodeRMI_Transmit
{
    Node_nodeRMI_ReceiveInterface nodeRMIReceive;

    // Node_nodeRMI_Transmit constructor
    public Node_nodeRMI_Transmit(String ipAddr)
    {
        try
        {
            String location = ("//"+ ipAddr + "/NodeSet"); //  voorbeeld: "//192.168.1.1/FileServer"
            nodeRMIReceive = (Node_nodeRMI_ReceiveInterface) Naming.lookup(location);
        } catch(Exception e)
        {
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
}
