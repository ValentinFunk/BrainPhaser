package de.fhdw.ergoholics.brainphaser.logic.fileimport.exceptions;

/**
 * Created by Daniel Hoogen on 19/02/2016.
 * <p/>
 * This exception is thrown when an unexpected element is found in a xml based file
 */
public class UnexpectedElementException extends Exception {
    /**
     * Constructor which creates an error string from the given string parameters
     *
     * @param elementName the name of the unexpected element
     */
    public UnexpectedElementException(String elementName) {
        super("Unexpected element: " + elementName + "!");
    }
}