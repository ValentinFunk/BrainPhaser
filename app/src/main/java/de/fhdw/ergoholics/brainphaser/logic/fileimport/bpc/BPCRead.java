package de.fhdw.ergoholics.brainphaser.logic.fileimport.bpc;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import de.fhdw.ergoholics.brainphaser.logic.fileimport.exceptions.FileFormatException;
import de.fhdw.ergoholics.brainphaser.logic.fileimport.exceptions.UnexpectedElementException;

/**
 * Created by Daniel Hoogen on 25/02/2016.
 * <p/>
 * Contains the logic for reading the root element of an .bpc file
 */
public class BPCRead {
    /**
     * Reads the categories node from an input stream of a .bpc file and returns it
     *
     * @param is the input stream of the .bpc file
     * @return the categories node of the .bpc file
     * @throws FileFormatException        if file is no xml file
     * @throws UnexpectedElementException if an unexpected element was fond in the file
     */
    public static Node getCategoriesNode(InputStream is)
            throws FileFormatException, UnexpectedElementException {
        //Read document
        Document document;

        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(is);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            Log.d("FileImport Exception", e.getMessage());
            throw new FileFormatException("BPC");
        }

        document.getDocumentElement().normalize();

        //Get Nodes from document
        Node childRoot = document.getFirstChild();

        /**
         * By the definition of the file format it is expected that the root  element is a
         * <category> element. If this requirement is not met, an exception is caused. Otherwise
         * the method returns the correct root element.
         */
        if (!childRoot.getNodeName().equals("categories"))
            throw new UnexpectedElementException("BPC");
        else {
            return childRoot;
        }
    }
}
