package de.fhdw.ergoholics.brainphaser.utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by thomasstuckel on 10/03/2016.
 *
 * This class contains all file utilitiy methods.
 */
public class FileUtils {
    /**
     * Resource: Pro Android by Syes Y. Hashimi and Satya Komatineni (2009) p.59
     * this funnctions reads all bytes from a stream and converts into a String
     * @param is input stream
     * @return a string
     * @throws IOException
     */
    public static String convertStreamToString(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = is.read();
        while (i != -1) {
            baos.write(i);
            i = is.read();
        }
        return baos.toString();
    }
}
