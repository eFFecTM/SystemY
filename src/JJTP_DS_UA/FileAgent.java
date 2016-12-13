package JJTP_DS_UA;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by jonas on 7-12-2016.
 */

public class FileAgent implements Runnable,Serializable
{
    Map<String, Integer> systemYfiles; //<K = Filename.ext, V = De node ID die een lock heeft (=null als er geen lock is>)
    Node currentNode;

    public FileAgent()
    {
        systemYfiles = new HashMap<String, Integer>();
    }

    @Override
    public void run()
    {


    }

    public void updateSystemYfiles()
    {
        Set<String> keyset = currentNode.fileMarkerMap.keySet(); //maak een set van keys van de map van de node van de bestanden waar hij eigenaar van is
        for(String key : keyset) //ga de map af (van de files waar de node eigenaar van is) en put alles in de systemYfiles
        {
            if(!systemYfiles.containsKey(key))
                systemYfiles.put(key, null);
        }
    }

    public void updateNodeSystemYfiles()
    {
        Set<String> keyset = systemYfiles.keySet();
        for(String key : keyset)
        {

        }
    }
    public void checkLocks()
    {

    }

    public void setCurrentNode(Node node)
    {
        this.currentNode = node;
    }
}
