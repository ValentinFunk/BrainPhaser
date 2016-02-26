package de.fhdw.ergoholics.brainphaser.fileimport.exceptions;

/**
 * Created by Daniel Hoogen on 19/02/2016.
 */
public class FileFormatException extends Exception {
    public FileFormatException(String expectedType)
    {
        super("The given file does not match the expected " + expectedType + " file structure!");
    }
}

