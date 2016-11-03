package JJTP_DS_UA;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.TreeMap;

import static sun.management.snmp.jvminstr.JvmThreadInstanceEntryImpl.ThreadStateMap.Byte0.runnable;

/**
 * Created by JJTP on 25/10/2016.
 */
public class NameServer
{
    TreeMap<Integer,Inet4Address> nodeMap;
    serverRMI RMIclass;


    public NameServer() throws RemoteException
    {
        nodeMap = new TreeMap<>(); //Treemap met <(hash)nodeNaam,ip>
        bindRMIclass();
        listenMC();
    }

    public void addNode(String name, Inet4Address IP)
    {
        Integer hashCode = name.hashCode();
        Integer hash = (int) Integer.toUnsignedLong(hashCode) % 32768;
        if(nodeMap.containsKey(hash))
            System.err.println("Node not added, name already taken.");
        else
            nodeMap.put(hash,IP);
    }

    public void deleteNode(String name)
    {
        Integer hashCode = name.hashCode();
        Integer hash = (int) Integer.toUnsignedLong(hashCode) % 32768;
        if(nodeMap.containsKey(hash))
            nodeMap.remove(hash);
        else
            System.err.println("No such Node.");

    }

    public Inet4Address lookup(String fileName)
    {
        Integer fileNameHash = (int) (Integer.toUnsignedLong(fileName.hashCode()) % 32768);
        if(nodeMap.lowerKey(fileNameHash)==null) // returnt key < dan de meegegeven paramater of null als die niet bestaat
            return nodeMap.get(nodeMap.lastKey()); //returnt de grootste key uit de map
        else
            return nodeMap.get(nodeMap.lowerKey(fileNameHash));
    }

    public void saveNodeMapXML()
    {
        try
        {
            File file = new File("NodeMap.xml");
            //Marshalling
            JAXBContext context = JAXBContext.newInstance(TreeMap.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(nodeMap,file);
            System.out.println("Converted to XML.");
        }
        catch (JAXBException e)
        {
            e.printStackTrace();
        }
    }

    public void listenMC()
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    int portMC = 12345;
                    Inet4Address IPMC = (Inet4Address) Inet4Address.getByName("230.0.0.0");
                    MulticastSocket mcSocket;
                    mcSocket = new MulticastSocket(portMC);
                    mcSocket.joinGroup(IPMC);
                    DatagramPacket packet;

                    while(true)
                    {
                        packet = new DatagramPacket(new byte[1024], 1024);
                        System.out.println("Waiting for a  multicast message...");
                        mcSocket.receive(packet);
                        String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());;
                        String[] info = msg.split(" "); // het ontvangen bericht splitsen in woorden gescheiden door een spatie
                        System.out.println("Naam: " + info[0]);
                        System.out.println("IP: " + info[1]);
                    }
                } catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void bindRMIclass()
    {
        try
        {
            RMIclass = new serverRMI(this); //RMIclass maken + referentie naar zichzelf doorgeven (voor lookup)
            String bindLocation = "//localhost/FileServer";
            LocateRegistry.createRegistry(1099);
            Naming.bind(bindLocation, RMIclass);
            System.out.println("FileServer Server is ready at:" + bindLocation);
            System.out.println("java RMI registry created.");
        } catch (MalformedURLException | AlreadyBoundException | RemoteException e) {
            e.printStackTrace();
            System.err.println("java RMI registry already exists.");
        }
    }

}
