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
    int ownerID; //degene die nu eigenaar is (naar waar het voor het laatst gerepliceerd is)
    int creator; //degene die het bestand origineel in het netwerk bracht
    ArrayList<Integer> downloadList; // bevat respectievelijk de nodehashes van de nodes die het bestand lokaal hebben staan en die het gedownload hebben
    // lokaal = enkel creator!
    // downloaded = handmatig gedownload hebben

    public FileMarker(String fileName, int fileNameHash, int creator)
    {
        this.fileName = fileName;
        this.fileNameHash = fileNameHash;
        this.creator = creator;
        downloadList = new ArrayList<>();
    }
}
