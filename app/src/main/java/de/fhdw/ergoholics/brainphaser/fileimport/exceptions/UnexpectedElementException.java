package de.fhdw.ergoholics.brainphaser.fileimport.exceptions;

/**
 * Created by Daniel Hoogen on 19/02/2016.
 *
 * This exception is thrown when an unexpected element is found in a xml based file
 */
public class UnexpectedElementException extends Exception {
    //Constructor
    public UnexpectedElementException(String elementName)
    {
        super("Unexpected element: " + elementName + "!");
    }
}
