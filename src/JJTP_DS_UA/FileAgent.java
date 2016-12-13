package JJTP_DS_UA;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jonas on 7-12-2016.
 */

public class FileAgent implements Runnable,Serializable
{
    Map<String, Integer> SystemYfiles; //<K = Filename.ext, V = De node ID die een lock heeft (=null als er geen lock is>)
    Node currentNode;

    public FileAgent()
    {
        SystemYfiles = new HashMap<String, Integer>();
    }

    @Override
    public void run()
    {

    }

    public void setCurrentNode(Node node)
    {
        this.currentNode = node;
    }
}
