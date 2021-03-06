package okio;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
/* compiled from: BufferedSource.kt */
/* loaded from: classes2.dex */
public interface BufferedSource extends Source, ReadableByteChannel {
    Buffer getBuffer();

    long indexOf(ByteString byteString) throws IOException;

    long indexOfElement(ByteString byteString) throws IOException;

    boolean request(long j) throws IOException;

    int select(Options options) throws IOException;
}
