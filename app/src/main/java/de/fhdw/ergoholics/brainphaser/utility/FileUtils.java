package de.fhdw.ergoholics.brainphaser.utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by thomasstuckel on 10/03/2016.
 */
public class FileUtils {

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
