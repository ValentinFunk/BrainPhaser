package de.fhdw.ergoholics.brainphaser.fileimport;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Daniel Hoogen on 19/02/2016.
 */
public class BPCImport {

    public static boolean importBPC(File bpcFile) throws FileFormatException
    {
        //Create required objects
        Document document = null;

        try
        {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(bpcFile);
        }
        catch (ParserConfigurationException | SAXException | IOException e)
        {
            /*
            TODO: Uncomment next line, delete System.out.println(...)
            Log.d("Unhandled exception in BPCImport(): \n", e.getMessage());
            */

            System.out.println(e);
        }

        document.getDocumentElement().normalize();

        NodeList childrenRoot = document.getChildNodes();
        Node categoriesNode;

        if (childrenRoot.getLength() < 1 || childrenRoot.getLength() < 1 ||
                !childrenRoot.item(0).getNodeName().equals("categories"))
            throw new FileFormatException("BPC");
        else
        {
            categoriesNode = childrenRoot.item(0);
        }

        System.out.println(document.getDocumentURI());
        System.out.println(document.getLocalName());
        System.out.println(document.getInputEncoding());
        System.out.println(document);

        NodeList nl = document.getChildNodes();

        for (int i = 0; i < nl.getLength(); i++)
        {
            System.out.println(nl.item(i));
        }

        /*//Read all
        Node root = document.get
        NodeList nList = document.getElementsByTagName("student");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            System.out.println("\nCurrent Element :"
                    + nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
            }
        }*/

        return false;
    }
}