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
        updateSystemYfiles();
        updateNodeSystemYfiles();
        //checkLocks();
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
            if(!currentNode.systemYfiles.containsKey(key))
                currentNode.systemYfiles.put(key, false);
        }
    }
    public void checkLocks() //@TODO bij het voltooien van download, de locks juist uitwerken
    {
        Set<String> keyset = currentNode.systemYfiles.keySet();
        for(String key : keyset)
        {
            if(currentNode.systemYfiles.get(key) == true) //de node heeft een lock
            {
                if(systemYfiles.get(key) == null) //er is nog geen lock
                {
                    // do something like give permission to download and release their lock
                    systemYfiles.put(key, currentNode.ownHash); //voeg de lock toe in de lijst van de node
                }
            }
        }
    }

    public void setCurrentNode(Node node)
    {
        this.currentNode = node;
    }
}
