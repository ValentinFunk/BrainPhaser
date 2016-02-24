package de.fhdw.ergoholics.brainphaser.fileimport;

/**
 * Created by Daniel on 19/02/2016.
 */
public class FileFormatException extends Exception {
    public FileFormatException(String expectedType)
    {
        super("The given file does not match the expected " + expectedType + " file structure!");
    }
}
