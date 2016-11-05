package JJTP_DS_UA;

import java.rmi.Remote;

/**
 * Created by Jonas on 04/11/16.
 */
public interface Node_nodeRMI_ReceiveInterface extends Remote
{
    public void setNeighbours(int leftHash, int rightHash);

}
