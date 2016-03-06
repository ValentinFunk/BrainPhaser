package de.fhdw.ergoholics.brainphaser.fileimport.exceptions;

/**
 * Created by Daniel Hoogen on 19/02/2016.
 *
 * This exception can be thrown when an element in an xml based file is not expected in a given context
 */
public class UnexpectedElementException extends Exception {

    //Constructor
    public UnexpectedElementException(String elementName)
    {
        super("Unexpected element: " + elementName + "!");
    }
}
