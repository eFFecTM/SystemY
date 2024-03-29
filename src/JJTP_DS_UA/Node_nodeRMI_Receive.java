/**
 * Created by JJTP on 04/11/16.
 * This class is used to communicate between 2 nodes using RMI. This is the receiving info side.
 */
package JJTP_DS_UA;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Node_nodeRMI_Receive extends UnicastRemoteObject implements Node_nodeRMI_ReceiveInterface
{
    Node myNode;

    // Node_nodeRMI_Receive constructor
    public Node_nodeRMI_Receive(Node n) throws RemoteException
        {
            super();
            myNode = n;
        }

    // Buren van Node instellen
    @Override
    public void setNeighbours(int prevHash, int nextHash)
    {
        myNode.prevHash=prevHash;
        myNode.nextHash=nextHash;
        myNode.prevNodeIP = myNode.NScommunication.getIP(prevHash);
        myNode.nextNodeIP = myNode.NScommunication.getIP(nextHash);
    }

    @Override
    public void updateLeftNeighbour(int hash)
    {
        myNode.prevHash=hash;
        myNode.prevNodeIP = myNode.NScommunication.getIP(hash);
    }

    @Override
    public void updateRightNeighbour(int hash) throws RemoteException
    {
        myNode.nextHash = hash; //rechterbuur van de node wordt geupdate door de huidige rechterbuur die weggaat
        myNode.nextNodeIP = myNode.NScommunication.getIP(hash);
    }

    @Override
    public void updateOnlyNode()
    {
        myNode.onlyNode = true;
    }

    @Override
    public void updateFileMarkers(FileMarker fm) throws RemoteException
    {
        myNode.updateFileMarker(fm);
    }

    @Override
    public void notifyOwner(String fileName) throws RemoteException
    {
        myNode.notifyOwner(fileName);
    }

    @Override
    public void removeFile(String fileName) throws RemoteException
    {
        myNode.currentFileList.remove(fileName);
        for(File file : myNode.currentFileList)
        {
            if(file.getName().equals(fileName))
                file.delete();
        }
    }

    @Override
    public int negotiatePort(String filename, Boolean askFile, String ipDest, int port) throws RemoteException
    {
        return myNode.negotiatePort(filename, askFile, ipDest, port);
    }

    @Override
    public void receiveFileAgent(FileAgent agent) throws RemoteException
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                agent.setCurrentNodeMaps(myNode.systemYfiles, myNode.fileMarkerMap, myNode.removedFiles);
                agent.run();
                myNode.transferFileAgent(agent);
            }
        }).start();
    }







}
