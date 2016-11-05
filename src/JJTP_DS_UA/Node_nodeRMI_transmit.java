package JJTP_DS_UA;

import java.rmi.Naming;

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
        NodeRMIReceive.setNeighbours(leftHash,rightHash);
    }
}
