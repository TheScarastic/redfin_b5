package com.bumptech.glide.gifdecoder;

import android.util.Log;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Objects;
/* loaded from: classes.dex */
public class GifHeaderParser {
    public final byte[] block = new byte[256];
    public int blockSize = 0;
    public GifHeader header;
    public ByteBuffer rawData;

    public final boolean err() {
        return this.header.status != 0;
    }

    public GifHeader parseHeader() {
        if (this.rawData == null) {
            throw new IllegalStateException("You must call setData() before parseHeader()");
        } else if (err()) {
            return this.header;
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                sb.append((char) read());
            }
            if (!sb.toString().startsWith("GIF")) {
                this.header.status = 1;
            } else {
                this.header.width = readShort();
                this.header.height = readShort();
                int read = read();
                GifHeader gifHeader = this.header;
                gifHeader.gctFlag = (read & 128) != 0;
                gifHeader.gctSize = (int) Math.pow(2.0d, (double) ((read & 7) + 1));
                this.header.bgIndex = read();
                GifHeader gifHeader2 = this.header;
                read();
                Objects.requireNonNull(gifHeader2);
                if (this.header.gctFlag && !err()) {
                    GifHeader gifHeader3 = this.header;
                    gifHeader3.gct = readColorTable(gifHeader3.gctSize);
                    GifHeader gifHeader4 = this.header;
                    gifHeader4.bgColor = gifHeader4.gct[gifHeader4.bgIndex];
                }
            }
            if (!err()) {
                boolean z = false;
                while (!z && !err() && this.header.frameCount <= Integer.MAX_VALUE) {
                    int read2 = read();
                    if (read2 == 33) {
                        int read3 = read();
                        if (read3 == 1) {
                            skip();
                        } else if (read3 == 249) {
                            this.header.currentFrame = new GifFrame();
                            read();
                            int read4 = read();
                            GifFrame gifFrame = this.header.currentFrame;
                            int i2 = (read4 & 28) >> 2;
                            gifFrame.dispose = i2;
                            if (i2 == 0) {
                                gifFrame.dispose = 1;
                            }
                            gifFrame.transparency = (read4 & 1) != 0;
                            int readShort = readShort();
                            if (readShort < 2) {
                                readShort = 10;
                            }
                            GifFrame gifFrame2 = this.header.currentFrame;
                            gifFrame2.delay = readShort * 10;
                            gifFrame2.transIndex = read();
                            read();
                        } else if (read3 == 254) {
                            skip();
                        } else if (read3 != 255) {
                            skip();
                        } else {
                            readBlock();
                            StringBuilder sb2 = new StringBuilder();
                            for (int i3 = 0; i3 < 11; i3++) {
                                sb2.append((char) this.block[i3]);
                            }
                            if (sb2.toString().equals("NETSCAPE2.0")) {
                                do {
                                    readBlock();
                                    byte[] bArr = this.block;
                                    if (bArr[0] == 1) {
                                        byte b = bArr[1];
                                        byte b2 = bArr[2];
                                        Objects.requireNonNull(this.header);
                                    }
                                    if (this.blockSize > 0) {
                                    }
                                } while (!err());
                            } else {
                                skip();
                            }
                        }
                    } else if (read2 == 44) {
                        GifHeader gifHeader5 = this.header;
                        if (gifHeader5.currentFrame == null) {
                            gifHeader5.currentFrame = new GifFrame();
                        }
                        gifHeader5.currentFrame.ix = readShort();
                        this.header.currentFrame.iy = readShort();
                        this.header.currentFrame.iw = readShort();
                        this.header.currentFrame.ih = readShort();
                        int read5 = read();
                        boolean z2 = (read5 & 128) != 0;
                        int pow = (int) Math.pow(2.0d, (double) ((read5 & 7) + 1));
                        GifFrame gifFrame3 = this.header.currentFrame;
                        gifFrame3.interlace = (read5 & 64) != 0;
                        if (z2) {
                            gifFrame3.lct = readColorTable(pow);
                        } else {
                            gifFrame3.lct = null;
                        }
                        this.header.currentFrame.bufferFrameStart = this.rawData.position();
                        read();
                        skip();
                        if (!err()) {
                            GifHeader gifHeader6 = this.header;
                            gifHeader6.frameCount++;
                            gifHeader6.frames.add(gifHeader6.currentFrame);
                        }
                    } else if (read2 != 59) {
                        this.header.status = 1;
                    } else {
                        z = true;
                    }
                }
                GifHeader gifHeader7 = this.header;
                if (gifHeader7.frameCount < 0) {
                    gifHeader7.status = 1;
                }
            }
            return this.header;
        }
    }

    public final int read() {
        try {
            return this.rawData.get() & 255;
        } catch (Exception unused) {
            this.header.status = 1;
            return 0;
        }
    }

    public final void readBlock() {
        int read = read();
        this.blockSize = read;
        if (read > 0) {
            int i = 0;
            int i2 = 0;
            while (true) {
                try {
                    i2 = this.blockSize;
                    if (i < i2) {
                        i2 -= i;
                        this.rawData.get(this.block, i, i2);
                        i += i2;
                    } else {
                        return;
                    }
                } catch (Exception e) {
                    if (Log.isLoggable("GifHeaderParser", 3)) {
                        int i3 = this.blockSize;
                        StringBuilder m = GifHeaderParser$$ExternalSyntheticOutline0.m(76, "Error Reading Block n: ", i, " count: ", i2);
                        m.append(" blockSize: ");
                        m.append(i3);
                        Log.d("GifHeaderParser", m.toString(), e);
                    }
                    this.header.status = 1;
                    return;
                }
            }
        }
    }

    public final int[] readColorTable(int i) {
        byte[] bArr = new byte[i * 3];
        int[] iArr = null;
        try {
            this.rawData.get(bArr);
            iArr = new int[256];
            int i2 = 0;
            int i3 = 0;
            while (i2 < i) {
                int i4 = i3 + 1;
                int i5 = i4 + 1;
                int i6 = i5 + 1;
                int i7 = i2 + 1;
                iArr[i2] = ((bArr[i3] & 255) << 16) | -16777216 | ((bArr[i4] & 255) << 8) | (bArr[i5] & 255);
                i3 = i6;
                i2 = i7;
            }
        } catch (BufferUnderflowException e) {
            if (Log.isLoggable("GifHeaderParser", 3)) {
                Log.d("GifHeaderParser", "Format Error Reading Color Table", e);
            }
            this.header.status = 1;
        }
        return iArr;
    }

    public final int readShort() {
        return this.rawData.getShort();
    }

    public final void skip() {
        int read;
        do {
            read = read();
            this.rawData.position(Math.min(this.rawData.position() + read, this.rawData.limit()));
        } while (read > 0);
    }
}
