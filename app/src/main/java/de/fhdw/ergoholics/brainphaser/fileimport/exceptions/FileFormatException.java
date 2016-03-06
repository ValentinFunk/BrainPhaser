package de.fhdw.ergoholics.brainphaser.fileimport.exceptions;

/**
 * Created by Daniel Hoogen on 19/02/2016.
 *
 * This exception can be thrown when a file to be loaded does not have the expected file format
 */
public class FileFormatException extends Exception {
    //Constructor
    public FileFormatException(String expectedType)
    {
        super("The given file does not match the expected " + expectedType + " file structure!");
    }
}

