package JJTP_DS_UA;


import java.io.Serializable;
import java.util.Set;


/**
 * Created by JJTP on 7-12-2016.
 */
//  TODO zorgt voor het updaten van de filemarkers bij het falen van een node
public class FailureAgent implements Runnable,Serializable
{
    int failedNode_ID;
    int failedNode_prevID;
    Node startedNode;

    public FailureAgent(int failedNode_ID, Node startedNode,int failedNode_prevID )
    {
        this.failedNode_ID = failedNode_ID;
        this.startedNode = startedNode;
        this.failedNode_prevID = failedNode_prevID;
    }

    @Override
    public void run()
    {

    }

    public void checkFiles()
    {
        Set<String> keyset = startedNode.systemYfiles.keySet();
        for(String key : keyset)
        {
            if(startedNode.NScommunication.getNodeFromFilename(startedNode.calcHash(key)) == failedNode_ID) //een bestand verwijst als eigenaar naar de gefaalde node
            {
                // bestand naar nieuwe eigenaar zenden + fm updaten
                // probleem: filemarkers zijn weg, stonden bij de gefaalde node ->download lijst ook weg
            }
        }
    }
}
