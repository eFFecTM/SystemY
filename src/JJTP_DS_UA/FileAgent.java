package JJTP_DS_UA;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jonas on 7-12-2016.
 */

public class FileAgent implements Runnable,Serializable
{
    Map<String, Integer> systemYfiles; //<K = Filename.ext, V = De node ID die een lock heeft (=null als er geen lock is>)
    ConcurrentHashMap<String, Boolean> nodeSystemYfiles;
    ConcurrentHashMap<String, FileMarker> nodeFileMarkerMap;
    ArrayList<String> nodeRemovedFiles;

    public FileAgent()
    {
        systemYfiles = new HashMap<String, Integer>();
    }

    @Override
    public void run()
    {
        updateSystemYfiles();
        removeFiles();
        updateNodeSystemYfiles();
        //checkLocks();
    }


    public void updateSystemYfiles()
    {
        Set<String> keyset = nodeFileMarkerMap.keySet(); //maak een set van keys van de map van de node van de bestanden waar hij eigenaar van is
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
            if(!nodeSystemYfiles.containsKey(key))
                nodeSystemYfiles.put(key, false);
        }

        Set<String> keyset2 = nodeSystemYfiles.keySet();
        for(String key2 : keyset2)
        {
            if(!systemYfiles.containsKey(key2))
                nodeSystemYfiles.remove(key2);
        }
    }

    public void removeFiles()
    {
        for(int i=0;i<nodeRemovedFiles.size();i++)
        {
            if(systemYfiles.containsKey(nodeRemovedFiles.get(i)))
            {
                systemYfiles.remove(nodeRemovedFiles.get(i));
                nodeRemovedFiles.remove(i);
            }

        }
    }

    public void checkLocks() //@TODO bij het voltooien van download, de locks juist uitwerken
    {
        Set<String> keyset = nodeSystemYfiles.keySet();
        for(String key : keyset)
        {
            if(nodeSystemYfiles.get(key) == true) //de node heeft een lock
            {
                if(systemYfiles.get(key) == null) //er is nog geen lock
                {
                    // do something like give permission to download and release their lock
                    //systemYfiles.put(key, currentNodeHash); //voeg de lock toe in de lijst van de node
                }
            }
        }
    }

    public void setCurrentNodeMaps(ConcurrentHashMap<String, Boolean> nodeSystemYfiles, ConcurrentHashMap<String, FileMarker> nodeFileMarkerMap, ArrayList<String> nodeRemovedFiles)
    {
        this.nodeSystemYfiles = nodeSystemYfiles;
        this.nodeFileMarkerMap = nodeFileMarkerMap;
        this.nodeRemovedFiles = nodeRemovedFiles;
    }
}
