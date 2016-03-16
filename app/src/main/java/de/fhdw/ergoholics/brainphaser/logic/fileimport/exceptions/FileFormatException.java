package de.fhdw.ergoholics.brainphaser.logic.fileimport.exceptions;

/**
 * Created by Daniel Hoogen on 19/02/2016.
 *
 * This exception is thrown when a file does not have the expected format
 */
public class FileFormatException extends Exception {
    //Constructor
    public FileFormatException(String expectedType)
    {
        super("The given file does not match the expected " + expectedType + " file structure!");
    }
}

