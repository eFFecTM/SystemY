package JJTP_DS_UA;

import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.TreeMap;

/**
 * Created by JJTP on 25/10/2016.
 */
public class NameServer
{
    TreeMap<Integer,Inet4Address> nodeMap;


    public NameServer() throws RemoteException
    {
        nodeMap = new TreeMap<>(); //Treemap met <(hash)nodeNaam,ip>
        serverRMI RMIclass = new serverRMI(this); //RMIclass maken + referentie naar zichzelf doorgeven (voor lookup)
        String bindLocation = "//localhost/FileServer"; //@TODO juiste locatie instellen

        try
        {
            LocateRegistry.createRegistry(1099);
            Naming.bind(bindLocation, RMIclass);
            System.out.println("FileServer Server is ready at:" + bindLocation);
            System.out.println("java RMI registry created.");
        } catch (MalformedURLException | AlreadyBoundException e) {
            e.printStackTrace();
            System.out.println("java RMI registry already exists.");
        }
    }

    public void addName(String name, Inet4Address IP)
    {
        Integer hashCode = name.hashCode();
        Integer hash = (int) Integer.toUnsignedLong(hashCode) % 32768;
        nodeMap.put(hash,IP);
    }

    public Inet4Address lookup(String fileName)
    {
        Integer fileNameHash = (int) (Integer.toUnsignedLong(fileName.hashCode()) % 32768);
        if(nodeMap.lowerKey(fileNameHash)==null) // returnt key < dan de meegegeven paramater of null als die niet bestaat
            return nodeMap.get(nodeMap.lastKey()); //returnt de grootste key uit de map
        else
            return nodeMap.get(nodeMap.lowerKey(fileNameHash));
    }

}
