package de.fhdw.ergoholics.brainphaser.logic.fileimport.exceptions;

/**
 * Created by Daniel Hoogen on 19/02/2016.
 */
public class FileFormatException extends Exception {
    //Constructor
    public FileFormatException(String expectedType)
    {
        super("The given file does not match the expected " + expectedType + " file structure!");
    }
}

