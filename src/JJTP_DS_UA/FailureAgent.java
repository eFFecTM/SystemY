package JJTP_DS_UA;


import java.io.Serializable;

/**
 * Created by jonas on 7-12-2016.
 */
public class FailureAgent implements Runnable,Serializable
{
    int failedNode_ID;
    int startedNode_ID;
    public FailureAgent(int failedNode_ID, int startedNode_ID)
    {
        this.failedNode_ID = failedNode_ID;
        this.startedNode_ID = startedNode_ID;
    }

    @Override
    public void run()
    {

    }
}
