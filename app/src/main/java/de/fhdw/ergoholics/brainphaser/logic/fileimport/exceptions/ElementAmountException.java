package de.fhdw.ergoholics.brainphaser.logic.fileimport.exceptions;

/**
 * Created by Daniel Hoogen on 19/02/2016.
 * <p/>
 * This exception is thrown when an element does not occur as often as it is expected in a given context
 */
public class ElementAmountException extends Exception {
    private String mElement;
    private String mExpectedAmount;
    private String mAmount;

    /**
     * Constructor which creates an error string from the given string parameters and saves them as
     * member attributes.
     *
     * @param element        the element that did not occur as often as expected
     * @param amountExpected the expected amount of occurences of the element
     * @param amount         the amount of occurences of the element
     */
    public ElementAmountException(String element, String amountExpected, String amount) {
        super("The element " + element + " is expected " + amountExpected + " times but was found " + amount + " times!");

        mElement = element;
        mExpectedAmount = amountExpected;
        mAmount = amount;
    }

    /**
     * Returns the name of the element that was given when the exception was created
     *
     * @return the name of the element
     */
    public String getElement() {
        return mElement;
    }

    /**
     * Returns the expected amount of the element that was given when the exception was created
     *
     * @return the expected amount of the alement
     */
    public String getExpectedAmount() {
        return mExpectedAmount;
    }

    /**
     * Returns the amount of the element that was given when the exception was created
     *
     * @return the amount of the alement
     */
    public String getAmount() {
        return mAmount;
    }
}