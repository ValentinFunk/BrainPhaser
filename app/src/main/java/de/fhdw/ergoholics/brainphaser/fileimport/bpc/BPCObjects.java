package de.fhdw.ergoholics.brainphaser.fileimport.bpc;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.List;

import de.fhdw.ergoholics.brainphaser.fileimport.exceptions.UnexpectedElementException;
import de.fhdw.ergoholics.brainphaser.model.Answer;
import de.fhdw.ergoholics.brainphaser.model.Category;
import de.fhdw.ergoholics.brainphaser.model.Challenge;

/**
 * Created by Daniel Hoogen on 25/02/2016.
 */
public class BPCObjects {
    public static long readCategory(Node categoryNode, long categoryId,
                                     long challengeId, List<Category> categoryList,
                                     List<Challenge> challengeList, List<Answer> answerList)
            throws UnexpectedElementException {
        //Check if the node is the correct element type
        if (!categoryNode.getNodeName().equals("category"))
        {
            throw new UnexpectedElementException(categoryNode.getNodeName());
        }

        //Read the attributes
        NamedNodeMap attributes = categoryNode.getAttributes();

        Node titleNode =attributes.getNamedItem("title");
        Node descriptionNode = attributes.getNamedItem("description");
        Node imageNode = attributes.getNamedItem("image");

        String title = titleNode.getNodeValue();
        String description = descriptionNode.getNodeValue();
        String image = imageNode.getNodeValue();

        //Add category to the list
        categoryList.add(new Category(categoryId, title, description, image));

        Node childCategory = categoryNode.getFirstChild();

        while (childCategory != null)
        {
            if (childCategory.getNodeType()==Node.ELEMENT_NODE) {
                readChallenge(childCategory, categoryId, challengeId,
                        challengeList, answerList);
                challengeId++;
            }

            childCategory = childCategory.getNextSibling();
        }

        //Return next free challengeId
        return challengeId;
    }

    private static void readChallenge(Node challengeNode, long categoryId, long challengeId,
                                      List<Challenge> challengeList, List<Answer> answerList)
            throws UnexpectedElementException {
        //Check if the node is the correct element type
        if (!challengeNode.getNodeName().equals("challenge")) {
            throw new UnexpectedElementException(challengeNode.getNodeName());
        }

        //Read the attributes
        NamedNodeMap attributes = challengeNode.getAttributes();

        Node typeNode = attributes.getNamedItem("type");
        Node questionNode = attributes.getNamedItem("question");

        int type = Integer.parseInt(typeNode.getNodeValue());
        String question = questionNode.getNodeValue();

        //Add challenge to the list
        challengeList.add(new Challenge(challengeId, type, question, categoryId));

        Node childChallenge = challengeNode.getFirstChild();

        while (childChallenge != null) {
            if (childChallenge.getNodeType()==Node.ELEMENT_NODE) {
                readAnswer(childChallenge, challengeId, answerList);
            }

            childChallenge = childChallenge.getNextSibling();
        }
    }

    private static void readAnswer(Node answerNode, long challengeId,
                                   List<Answer> answerList) throws UnexpectedElementException {
        //Check if the node is the correct element type
        if (!answerNode.getNodeName().equals("answer")) {
            throw new UnexpectedElementException(answerNode.getNodeName());
        }

        //Read the attributes
        NamedNodeMap attributes = answerNode.getAttributes();

        Node textNode = attributes.getNamedItem("text");
        Node answerCorrectNode = attributes.getNamedItem("correct");

        String text = textNode.getNodeValue();
        String answerCorrectString = answerCorrectNode.getNodeValue();
        boolean answerCorrect;
        if (answerCorrectString.equals("true")) {
            answerCorrect = true;
        }
        else if (answerCorrectString.equals("false")) {
            answerCorrect = false;
        }
        else {
            throw new UnexpectedElementException(answerCorrectString);
        }

        //Add answer to the list
        answerList.add(new Answer(null, text, answerCorrect, challengeId));
    }
}
