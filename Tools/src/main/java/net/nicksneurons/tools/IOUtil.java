package net.nicksneurons.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtil {

    /**
     * TODO Document
     * @param stream
     * @return
     * @throws IOException
     */
    public static byte[] readStream(InputStream stream) throws IOException {
        return readStream(stream, 8192); //Arbitrary buffer size of 8192.
    }

    /**
     * TODO Document
     * @param stream
     * @param bufferSize
     * @return
     * @throws IOException
     */
    public static byte[] readStream(InputStream stream, int bufferSize) throws IOException {
        //Create ByteArrayOutputStream to store the contents of the input stream.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //Stores the number of bytes read (equals -1 at the end of the input stream).
        int bytesRead = 0;
        byte[] buffer = new byte[bufferSize];
        while((bytesRead = stream.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead); //Only write what was read.
        }

        return baos.toByteArray();
    }
}
