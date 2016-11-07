/**
 * Created by JJTP on 04/11/16.
 * This interface is used to communicate between 2 nodes using RMI.
 */
package JJTP_DS_UA;

import java.rmi.Remote;

public interface Node_nodeRMI_ReceiveInterface extends Remote
{
    public void setNeighbours(int leftHash, int rightHash);

}
