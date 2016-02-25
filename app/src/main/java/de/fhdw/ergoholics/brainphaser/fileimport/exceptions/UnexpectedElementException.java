package de.fhdw.ergoholics.brainphaser.fileimport.exceptions;

/**
 * Created by Daniel Hoogen on 19/02/2016.
 */
public class UnexpectedElementException extends Exception {
    public UnexpectedElementException(String elementName)
    {
        super("Unexpected element: " + elementName + "!");
    }
}
