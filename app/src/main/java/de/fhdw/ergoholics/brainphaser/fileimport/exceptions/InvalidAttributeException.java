package de.fhdw.ergoholics.brainphaser.fileimport.exceptions;

/**
 * Created by Daniel Hoogen on 15/03/2016.
 *
 * This exception is thrown when an invalid attribute value was found in a file
 */
public class InvalidAttributeException extends Exception {
    //Attributes
    private String mAttribute;
    private String mValue;

    //Constructor
    public InvalidAttributeException(String attributeName, String attributeValue)
    {
        super("The attribute " + attributeName + " has an invalid value: " + attributeValue + "!");

        mAttribute = attributeName;
        mValue = attributeValue;
    }

    /**
     * Returns the name of the attribute that was given when the exception was created
     * @return the name of the attribute
     */
    public String getAttribute() {
        return mAttribute;
    }

    /**
     * Returns the value of the attribute that was given when the exception was created
     * @return the value of the attribute
     */
    public String getValue() {
        return mValue;
    }
}
