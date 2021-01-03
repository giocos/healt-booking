package buffer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class BufferFactory {

    protected BufferFactory() {}

    public static StringBuffer getStringBuffer() {
        return new StringBuffer();
    }

    public static BufferedReader getBufferReader(final InputStream input) {
        return new BufferedReader(new InputStreamReader(input));
    }
}
