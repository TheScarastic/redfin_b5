package com.android.volley.toolbox;

import android.os.SystemClock;
import android.support.media.ExifInterface$$ExternalSyntheticOutline0;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.text.TextUtils;
import com.android.volley.Cache;
import com.android.volley.Header;
import com.android.volley.VolleyLog;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
/* loaded from: classes.dex */
public class DiskBasedCache implements Cache {
    public static final float HYSTERESIS_FACTOR = 0.9f;
    public final FileSupplier mRootDirectorySupplier;
    public final Map<String, CacheHeader> mEntries = new LinkedHashMap(16, 0.75f, true);
    public long mTotalSize = 0;
    public final int mMaxCacheSizeInBytes = 5242880;

    /* loaded from: classes.dex */
    public interface FileSupplier {
    }

    public DiskBasedCache(FileSupplier fileSupplier) {
        this.mRootDirectorySupplier = fileSupplier;
    }

    public static int read(InputStream inputStream) throws IOException {
        int read = inputStream.read();
        if (read != -1) {
            return read;
        }
        throw new EOFException();
    }

    public static int readInt(InputStream inputStream) throws IOException {
        return (read(inputStream) << 24) | (read(inputStream) << 0) | 0 | (read(inputStream) << 8) | (read(inputStream) << 16);
    }

    public static long readLong(InputStream inputStream) throws IOException {
        return ((((long) read(inputStream)) & 255) << 0) | 0 | ((((long) read(inputStream)) & 255) << 8) | ((((long) read(inputStream)) & 255) << 16) | ((((long) read(inputStream)) & 255) << 24) | ((((long) read(inputStream)) & 255) << 32) | ((((long) read(inputStream)) & 255) << 40) | ((((long) read(inputStream)) & 255) << 48) | ((255 & ((long) read(inputStream))) << 56);
    }

    public static String readString(CountingInputStream countingInputStream) throws IOException {
        return new String(streamToBytes(countingInputStream, readLong(countingInputStream)), "UTF-8");
    }

    public static byte[] streamToBytes(CountingInputStream countingInputStream, long j) throws IOException {
        long j2 = countingInputStream.length - countingInputStream.bytesRead;
        if (j >= 0 && j <= j2) {
            int i = (int) j;
            if (((long) i) == j) {
                byte[] bArr = new byte[i];
                new DataInputStream(countingInputStream).readFully(bArr);
                return bArr;
            }
        }
        throw new IOException("streamToBytes length=" + j + ", maxLength=" + j2);
    }

    public static void writeInt(OutputStream outputStream, int i) throws IOException {
        outputStream.write((i >> 0) & 255);
        outputStream.write((i >> 8) & 255);
        outputStream.write((i >> 16) & 255);
        outputStream.write((i >> 24) & 255);
    }

    public static void writeLong(OutputStream outputStream, long j) throws IOException {
        outputStream.write((byte) ((int) (j >>> 0)));
        outputStream.write((byte) ((int) (j >>> 8)));
        outputStream.write((byte) ((int) (j >>> 16)));
        outputStream.write((byte) ((int) (j >>> 24)));
        outputStream.write((byte) ((int) (j >>> 32)));
        outputStream.write((byte) ((int) (j >>> 40)));
        outputStream.write((byte) ((int) (j >>> 48)));
        outputStream.write((byte) ((int) (j >>> 56)));
    }

    public static void writeString(OutputStream outputStream, String str) throws IOException {
        byte[] bytes = str.getBytes("UTF-8");
        writeLong(outputStream, (long) bytes.length);
        outputStream.write(bytes, 0, bytes.length);
    }

    public InputStream createInputStream(File file) throws FileNotFoundException {
        return new FileInputStream(file);
    }

    public OutputStream createOutputStream(File file) throws FileNotFoundException {
        return new FileOutputStream(file);
    }

    public synchronized Cache.Entry get(String str) {
        CacheHeader cacheHeader = this.mEntries.get(str);
        if (cacheHeader == null) {
            return null;
        }
        File fileForKey = getFileForKey(str);
        try {
            CountingInputStream countingInputStream = new CountingInputStream(new BufferedInputStream(createInputStream(fileForKey)), fileForKey.length());
            try {
                CacheHeader readHeader = CacheHeader.readHeader(countingInputStream);
                if (!TextUtils.equals(str, readHeader.key)) {
                    VolleyLog.d("%s: key=%s, found=%s", fileForKey.getAbsolutePath(), str, readHeader.key);
                    CacheHeader remove = this.mEntries.remove(str);
                    if (remove != null) {
                        this.mTotalSize -= remove.size;
                    }
                    return null;
                }
                return cacheHeader.toCacheEntry(streamToBytes(countingInputStream, countingInputStream.length - countingInputStream.bytesRead));
            } finally {
                countingInputStream.close();
            }
        } catch (IOException e) {
            VolleyLog.d("%s: %s", fileForKey.getAbsolutePath(), e.toString());
            remove(str);
            return null;
        }
    }

    public File getFileForKey(String str) {
        return new File(((Volley$1) this.mRootDirectorySupplier).get(), getFilenameForKey(str));
    }

    public final String getFilenameForKey(String str) {
        int length = str.length() / 2;
        StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m(String.valueOf(str.substring(0, length).hashCode()));
        m.append(String.valueOf(str.substring(length).hashCode()));
        return m.toString();
    }

    public synchronized void initialize() {
        long length;
        CountingInputStream countingInputStream;
        File file = ((Volley$1) this.mRootDirectorySupplier).get();
        if (!file.exists()) {
            if (!file.mkdirs()) {
                VolleyLog.e("Unable to create cache dir %s", file.getAbsolutePath());
            }
            return;
        }
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                try {
                    length = file2.length();
                    countingInputStream = new CountingInputStream(new BufferedInputStream(createInputStream(file2)), length);
                } catch (IOException unused) {
                    file2.delete();
                }
                try {
                    CacheHeader readHeader = CacheHeader.readHeader(countingInputStream);
                    readHeader.size = length;
                    putEntry(readHeader.key, readHeader);
                    countingInputStream.close();
                } catch (Throwable th) {
                    countingInputStream.close();
                    throw th;
                    break;
                }
            }
        }
    }

    public final void pruneIfNeeded() {
        if (this.mTotalSize >= ((long) this.mMaxCacheSizeInBytes)) {
            if (VolleyLog.DEBUG) {
                VolleyLog.v("Pruning old cache entries.", new Object[0]);
            }
            long j = this.mTotalSize;
            long elapsedRealtime = SystemClock.elapsedRealtime();
            Iterator<Map.Entry<String, CacheHeader>> it = this.mEntries.entrySet().iterator();
            int i = 0;
            while (it.hasNext()) {
                CacheHeader value = it.next().getValue();
                if (getFileForKey(value.key).delete()) {
                    this.mTotalSize -= value.size;
                } else {
                    String str = value.key;
                    VolleyLog.d("Could not delete cache entry for key=%s, filename=%s", str, getFilenameForKey(str));
                }
                it.remove();
                i++;
                if (((float) this.mTotalSize) < ((float) this.mMaxCacheSizeInBytes) * 0.9f) {
                    break;
                }
            }
            if (VolleyLog.DEBUG) {
                VolleyLog.v("pruned %d files, %d bytes, %d ms", Integer.valueOf(i), Long.valueOf(this.mTotalSize - j), Long.valueOf(SystemClock.elapsedRealtime() - elapsedRealtime));
            }
        }
    }

    public synchronized void put(String str, Cache.Entry entry) {
        BufferedOutputStream bufferedOutputStream;
        CacheHeader cacheHeader;
        long j = this.mTotalSize;
        byte[] bArr = entry.data;
        long length = j + ((long) bArr.length);
        int i = this.mMaxCacheSizeInBytes;
        if (length <= ((long) i) || ((float) bArr.length) <= ((float) i) * 0.9f) {
            File fileForKey = getFileForKey(str);
            try {
                bufferedOutputStream = new BufferedOutputStream(createOutputStream(fileForKey));
                cacheHeader = new CacheHeader(str, entry);
            } catch (IOException unused) {
                if (!fileForKey.delete()) {
                    VolleyLog.d("Could not clean up file %s", fileForKey.getAbsolutePath());
                }
                if (!((Volley$1) this.mRootDirectorySupplier).get().exists()) {
                    VolleyLog.d("Re-initializing cache after external clearing.", new Object[0]);
                    this.mEntries.clear();
                    this.mTotalSize = 0;
                    initialize();
                }
            }
            if (cacheHeader.writeHeader(bufferedOutputStream)) {
                bufferedOutputStream.write(entry.data);
                bufferedOutputStream.close();
                cacheHeader.size = fileForKey.length();
                putEntry(str, cacheHeader);
                pruneIfNeeded();
                return;
            }
            bufferedOutputStream.close();
            VolleyLog.d("Failed to write header for %s", fileForKey.getAbsolutePath());
            throw new IOException();
        }
    }

    public final void putEntry(String str, CacheHeader cacheHeader) {
        if (!this.mEntries.containsKey(str)) {
            this.mTotalSize += cacheHeader.size;
        } else {
            this.mTotalSize = (cacheHeader.size - this.mEntries.get(str).size) + this.mTotalSize;
        }
        this.mEntries.put(str, cacheHeader);
    }

    public synchronized void remove(String str) {
        boolean delete = getFileForKey(str).delete();
        CacheHeader remove = this.mEntries.remove(str);
        if (remove != null) {
            this.mTotalSize -= remove.size;
        }
        if (!delete) {
            VolleyLog.d("Could not delete cache entry for key=%s, filename=%s", str, getFilenameForKey(str));
        }
    }

    /* loaded from: classes.dex */
    public static class CountingInputStream extends FilterInputStream {
        public long bytesRead;
        public final long length;

        public CountingInputStream(InputStream inputStream, long j) {
            super(inputStream);
            this.length = j;
        }

        public long bytesRead() {
            return this.bytesRead;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read() throws IOException {
            int read = super.read();
            if (read != -1) {
                this.bytesRead++;
            }
            return read;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read(byte[] bArr, int i, int i2) throws IOException {
            int read = super.read(bArr, i, i2);
            if (read != -1) {
                this.bytesRead += (long) read;
            }
            return read;
        }
    }

    /* loaded from: classes.dex */
    public static class CacheHeader {
        public final List<Header> allResponseHeaders;
        public final String etag;
        public final String key;
        public final long lastModified;
        public final long serverDate;
        public long size;
        public final long softTtl;
        public final long ttl;

        public CacheHeader(String str, String str2, long j, long j2, long j3, long j4, List<Header> list) {
            this.key = str;
            this.etag = "".equals(str2) ? null : str2;
            this.serverDate = j;
            this.lastModified = j2;
            this.ttl = j3;
            this.softTtl = j4;
            this.allResponseHeaders = list;
        }

        public static CacheHeader readHeader(CountingInputStream countingInputStream) throws IOException {
            if (DiskBasedCache.readInt(countingInputStream) == 538247942) {
                String readString = DiskBasedCache.readString(countingInputStream);
                String readString2 = DiskBasedCache.readString(countingInputStream);
                long readLong = DiskBasedCache.readLong(countingInputStream);
                long readLong2 = DiskBasedCache.readLong(countingInputStream);
                long readLong3 = DiskBasedCache.readLong(countingInputStream);
                long readLong4 = DiskBasedCache.readLong(countingInputStream);
                int readInt = DiskBasedCache.readInt(countingInputStream);
                if (readInt >= 0) {
                    List emptyList = readInt == 0 ? Collections.emptyList() : new ArrayList();
                    for (int i = 0; i < readInt; i++) {
                        emptyList.add(new Header(DiskBasedCache.readString(countingInputStream).intern(), DiskBasedCache.readString(countingInputStream).intern()));
                    }
                    return new CacheHeader(readString, readString2, readLong, readLong2, readLong3, readLong4, emptyList);
                }
                throw new IOException(ExifInterface$$ExternalSyntheticOutline0.m("readHeaderList size=", readInt));
            }
            throw new IOException();
        }

        public Cache.Entry toCacheEntry(byte[] bArr) {
            Cache.Entry entry = new Cache.Entry();
            entry.data = bArr;
            entry.etag = this.etag;
            entry.serverDate = this.serverDate;
            entry.lastModified = this.lastModified;
            entry.ttl = this.ttl;
            entry.softTtl = this.softTtl;
            List<Header> list = this.allResponseHeaders;
            TreeMap treeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
            for (Header header : list) {
                treeMap.put(header.mName, header.mValue);
            }
            entry.responseHeaders = treeMap;
            entry.allResponseHeaders = Collections.unmodifiableList(this.allResponseHeaders);
            return entry;
        }

        public boolean writeHeader(OutputStream outputStream) {
            try {
                DiskBasedCache.writeInt(outputStream, 538247942);
                DiskBasedCache.writeString(outputStream, this.key);
                String str = this.etag;
                if (str == null) {
                    str = "";
                }
                DiskBasedCache.writeString(outputStream, str);
                DiskBasedCache.writeLong(outputStream, this.serverDate);
                DiskBasedCache.writeLong(outputStream, this.lastModified);
                DiskBasedCache.writeLong(outputStream, this.ttl);
                DiskBasedCache.writeLong(outputStream, this.softTtl);
                List<Header> list = this.allResponseHeaders;
                if (list != null) {
                    DiskBasedCache.writeInt(outputStream, list.size());
                    for (Header header : list) {
                        DiskBasedCache.writeString(outputStream, header.mName);
                        DiskBasedCache.writeString(outputStream, header.mValue);
                    }
                } else {
                    DiskBasedCache.writeInt(outputStream, 0);
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                VolleyLog.d("%s", e.toString());
                return false;
            }
        }

        /* JADX DEBUG: Failed to insert an additional move for type inference into block B:11:0x000e */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v0, types: [java.util.List<com.android.volley.Header>] */
        /* JADX WARN: Type inference failed for: r0v1, types: [java.util.ArrayList] */
        /* JADX WARN: Type inference failed for: r0v2, types: [java.util.List] */
        /* JADX WARNING: Illegal instructions before constructor call */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public CacheHeader(java.lang.String r14, com.android.volley.Cache.Entry r15) {
            /*
                r13 = this;
                java.lang.String r2 = r15.etag
                long r3 = r15.serverDate
                long r5 = r15.lastModified
                long r7 = r15.ttl
                long r9 = r15.softTtl
                java.util.List<com.android.volley.Header> r0 = r15.allResponseHeaders
                if (r0 == 0) goto L_0x0010
            L_0x000e:
                r11 = r0
                goto L_0x0044
            L_0x0010:
                java.util.Map<java.lang.String, java.lang.String> r15 = r15.responseHeaders
                java.util.ArrayList r0 = new java.util.ArrayList
                int r1 = r15.size()
                r0.<init>(r1)
                java.util.Set r15 = r15.entrySet()
                java.util.Iterator r15 = r15.iterator()
            L_0x0023:
                boolean r1 = r15.hasNext()
                if (r1 == 0) goto L_0x000e
                java.lang.Object r1 = r15.next()
                java.util.Map$Entry r1 = (java.util.Map.Entry) r1
                com.android.volley.Header r11 = new com.android.volley.Header
                java.lang.Object r12 = r1.getKey()
                java.lang.String r12 = (java.lang.String) r12
                java.lang.Object r1 = r1.getValue()
                java.lang.String r1 = (java.lang.String) r1
                r11.<init>(r12, r1)
                r0.add(r11)
                goto L_0x0023
            L_0x0044:
                r0 = r13
                r1 = r14
                r0.<init>(r1, r2, r3, r5, r7, r9, r11)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.volley.toolbox.DiskBasedCache.CacheHeader.<init>(java.lang.String, com.android.volley.Cache$Entry):void");
        }
    }
}
