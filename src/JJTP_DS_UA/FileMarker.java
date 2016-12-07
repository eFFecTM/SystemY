package JJTP_DS_UA;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jonas on 19-11-2016.
 */
public class FileMarker implements Serializable
{
    String fileName;
    int fileNameHash;
    public int ownerID; //degene die nu eigenaar is (naar waar het voor het laatst gerepliceerd is)
    int creator; //degene die het bestand origineel in het netwerk bracht
    ArrayList<Integer> localList, downloadList; // bevat respectievelijk de nodehashes van de nodes die het bestand lokaal hebben staan en die het gedownload hebben
    // lokaal = creator + replica
    // downloaded = handmatig gedownload hebben

    public FileMarker(String fileName, int fileNameHash, int creator)
    {
        this.fileName = fileName;
        this.fileNameHash = fileNameHash;
        this.creator = creator;
        localList = new ArrayList<>();
        localList.add(creator);
        downloadList = new ArrayList<>();
    }

    public void setOwnerID(int ownerID)
    {
        this.ownerID = ownerID;
    }

    public void addLocalList(int nodeHash)
    {
        localList.add(nodeHash);
    }

    public void addDownloadList(int nodeHash)
    {
        downloadList.add(nodeHash);
    }

    public void removeLocalList(int nodeHash)
    {
        localList.remove(nodeHash);
    }

    public void removeDownloadList(int nodeHash)
    {
        downloadList.remove(nodeHash);
    }

}
