package com.bumptech.glide.disklrucache;

import androidx.preference.R$string$$ExternalSyntheticOutline0;
import com.adobe.xmp.XMPPathFactory$$ExternalSyntheticOutline0;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public final class DiskLruCache implements Closeable {
    public final int appVersion;
    public final File directory;
    public final File journalFile;
    public final File journalFileBackup;
    public final File journalFileTmp;
    public Writer journalWriter;
    public long maxSize;
    public int redundantOpCount;
    public final int valueCount;
    public long size = 0;
    public final LinkedHashMap<String, Entry> lruEntries = new LinkedHashMap<>(0, 0.75f, true);
    public long nextSequenceNumber = 0;
    public final ThreadPoolExecutor executorService = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(), new DiskLruCacheThreadFactory(null));
    public final Callable<Void> cleanupCallable = new Callable<Void>() { // from class: com.bumptech.glide.disklrucache.DiskLruCache.1
        /* Return type fixed from 'java.lang.Object' to match base method */
        @Override // java.util.concurrent.Callable
        public Void call() throws Exception {
            synchronized (DiskLruCache.this) {
                DiskLruCache diskLruCache = DiskLruCache.this;
                if (diskLruCache.journalWriter == null) {
                    return null;
                }
                diskLruCache.trimToSize();
                if (DiskLruCache.this.journalRebuildRequired()) {
                    DiskLruCache.this.rebuildJournal();
                    DiskLruCache.this.redundantOpCount = 0;
                }
                return null;
            }
        }
    };

    /* loaded from: classes.dex */
    public static final class DiskLruCacheThreadFactory implements ThreadFactory {
        public DiskLruCacheThreadFactory(AnonymousClass1 r1) {
        }

        @Override // java.util.concurrent.ThreadFactory
        public synchronized Thread newThread(Runnable runnable) {
            Thread thread;
            thread = new Thread(runnable, "glide-disk-lru-cache-thread");
            thread.setPriority(1);
            return thread;
        }
    }

    /* loaded from: classes.dex */
    public final class Editor {
        public boolean committed;
        public final Entry entry;
        public final boolean[] written;

        public Editor(Entry entry, AnonymousClass1 r3) {
            boolean[] zArr;
            this.entry = entry;
            if (entry.readable) {
                zArr = null;
            } else {
                zArr = new boolean[DiskLruCache.this.valueCount];
            }
            this.written = zArr;
        }

        public void abort() throws IOException {
            DiskLruCache.access$2100(DiskLruCache.this, this, false);
        }

        public File getFile(int i) throws IOException {
            File file;
            synchronized (DiskLruCache.this) {
                Entry entry = this.entry;
                if (entry.currentEditor == this) {
                    if (!entry.readable) {
                        this.written[i] = true;
                    }
                    file = entry.dirtyFiles[i];
                    if (!DiskLruCache.this.directory.exists()) {
                        DiskLruCache.this.directory.mkdirs();
                    }
                } else {
                    throw new IllegalStateException();
                }
            }
            return file;
        }
    }

    /* loaded from: classes.dex */
    public final class Entry {
        public File[] cleanFiles;
        public Editor currentEditor;
        public File[] dirtyFiles;
        public final String key;
        public final long[] lengths;
        public boolean readable;
        public long sequenceNumber;

        public Entry(String str, AnonymousClass1 r8) {
            this.key = str;
            int i = DiskLruCache.this.valueCount;
            this.lengths = new long[i];
            this.cleanFiles = new File[i];
            this.dirtyFiles = new File[i];
            StringBuilder sb = new StringBuilder(str);
            sb.append('.');
            int length = sb.length();
            for (int i2 = 0; i2 < DiskLruCache.this.valueCount; i2++) {
                sb.append(i2);
                this.cleanFiles[i2] = new File(DiskLruCache.this.directory, sb.toString());
                sb.append(".tmp");
                this.dirtyFiles[i2] = new File(DiskLruCache.this.directory, sb.toString());
                sb.setLength(length);
            }
        }

        public String getLengths() throws IOException {
            StringBuilder sb = new StringBuilder();
            long[] jArr = this.lengths;
            for (long j : jArr) {
                sb.append(' ');
                sb.append(j);
            }
            return sb.toString();
        }

        public final IOException invalidLengths(String[] strArr) throws IOException {
            String valueOf = String.valueOf(Arrays.toString(strArr));
            throw new IOException(valueOf.length() != 0 ? "unexpected journal line: ".concat(valueOf) : new String("unexpected journal line: "));
        }
    }

    /* loaded from: classes.dex */
    public final class Value {
        public final File[] files;

        public Value(DiskLruCache diskLruCache, String str, long j, File[] fileArr, long[] jArr, AnonymousClass1 r7) {
            this.files = fileArr;
        }
    }

    public DiskLruCache(File file, int i, int i2, long j) {
        this.directory = file;
        this.appVersion = i;
        this.journalFile = new File(file, "journal");
        this.journalFileTmp = new File(file, "journal.tmp");
        this.journalFileBackup = new File(file, "journal.bkp");
        this.valueCount = i2;
        this.maxSize = j;
    }

    public static void access$2100(DiskLruCache diskLruCache, Editor editor, boolean z) throws IOException {
        synchronized (diskLruCache) {
            Entry entry = editor.entry;
            if (entry.currentEditor == editor) {
                if (z && !entry.readable) {
                    for (int i = 0; i < diskLruCache.valueCount; i++) {
                        if (!editor.written[i]) {
                            editor.abort();
                            StringBuilder sb = new StringBuilder(61);
                            sb.append("Newly created entry didn't create value for index ");
                            sb.append(i);
                            throw new IllegalStateException(sb.toString());
                        } else if (!entry.dirtyFiles[i].exists()) {
                            editor.abort();
                            return;
                        }
                    }
                }
                for (int i2 = 0; i2 < diskLruCache.valueCount; i2++) {
                    File file = entry.dirtyFiles[i2];
                    if (!z) {
                        deleteIfExists(file);
                    } else if (file.exists()) {
                        File file2 = entry.cleanFiles[i2];
                        file.renameTo(file2);
                        long j = entry.lengths[i2];
                        long length = file2.length();
                        entry.lengths[i2] = length;
                        diskLruCache.size = (diskLruCache.size - j) + length;
                    }
                }
                diskLruCache.redundantOpCount++;
                entry.currentEditor = null;
                if (entry.readable || z) {
                    entry.readable = true;
                    diskLruCache.journalWriter.append((CharSequence) "CLEAN");
                    diskLruCache.journalWriter.append(' ');
                    diskLruCache.journalWriter.append((CharSequence) entry.key);
                    diskLruCache.journalWriter.append((CharSequence) entry.getLengths());
                    diskLruCache.journalWriter.append('\n');
                    if (z) {
                        long j2 = diskLruCache.nextSequenceNumber;
                        diskLruCache.nextSequenceNumber = 1 + j2;
                        entry.sequenceNumber = j2;
                    }
                } else {
                    diskLruCache.lruEntries.remove(entry.key);
                    diskLruCache.journalWriter.append((CharSequence) "REMOVE");
                    diskLruCache.journalWriter.append(' ');
                    diskLruCache.journalWriter.append((CharSequence) entry.key);
                    diskLruCache.journalWriter.append('\n');
                }
                diskLruCache.journalWriter.flush();
                if (diskLruCache.size > diskLruCache.maxSize || diskLruCache.journalRebuildRequired()) {
                    diskLruCache.executorService.submit(diskLruCache.cleanupCallable);
                }
                return;
            }
            throw new IllegalStateException();
        }
    }

    public static void deleteIfExists(File file) throws IOException {
        if (file.exists() && !file.delete()) {
            throw new IOException();
        }
    }

    public static DiskLruCache open(File file, int i, int i2, long j) throws IOException {
        if (j <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        } else if (i2 > 0) {
            File file2 = new File(file, "journal.bkp");
            if (file2.exists()) {
                File file3 = new File(file, "journal");
                if (file3.exists()) {
                    file2.delete();
                } else {
                    renameTo(file2, file3, false);
                }
            }
            DiskLruCache diskLruCache = new DiskLruCache(file, i, i2, j);
            if (diskLruCache.journalFile.exists()) {
                try {
                    diskLruCache.readJournal();
                    diskLruCache.processJournal();
                    return diskLruCache;
                } catch (IOException e) {
                    PrintStream printStream = System.out;
                    String valueOf = String.valueOf(file);
                    String message = e.getMessage();
                    StringBuilder m = R$string$$ExternalSyntheticOutline0.m(XMPPathFactory$$ExternalSyntheticOutline0.m(message, valueOf.length() + 36), "DiskLruCache ", valueOf, " is corrupt: ", message);
                    m.append(", removing");
                    printStream.println(m.toString());
                    diskLruCache.close();
                    Util.deleteContents(diskLruCache.directory);
                }
            }
            file.mkdirs();
            DiskLruCache diskLruCache2 = new DiskLruCache(file, i, i2, j);
            diskLruCache2.rebuildJournal();
            return diskLruCache2;
        } else {
            throw new IllegalArgumentException("valueCount <= 0");
        }
    }

    public static void renameTo(File file, File file2, boolean z) throws IOException {
        if (z) {
            deleteIfExists(file2);
        }
        if (!file.renameTo(file2)) {
            throw new IOException();
        }
    }

    public final void checkNotClosed() {
        if (this.journalWriter == null) {
            throw new IllegalStateException("cache is closed");
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        if (this.journalWriter != null) {
            Iterator it = new ArrayList(this.lruEntries.values()).iterator();
            while (it.hasNext()) {
                Editor editor = ((Entry) it.next()).currentEditor;
                if (editor != null) {
                    editor.abort();
                }
            }
            trimToSize();
            this.journalWriter.close();
            this.journalWriter = null;
        }
    }

    public Editor edit(String str) throws IOException {
        synchronized (this) {
            checkNotClosed();
            Entry entry = this.lruEntries.get(str);
            if (entry == null) {
                entry = new Entry(str, null);
                this.lruEntries.put(str, entry);
            } else if (entry.currentEditor != null) {
                return null;
            }
            Editor editor = new Editor(entry, null);
            entry.currentEditor = editor;
            this.journalWriter.append((CharSequence) "DIRTY");
            this.journalWriter.append(' ');
            this.journalWriter.append((CharSequence) str);
            this.journalWriter.append('\n');
            this.journalWriter.flush();
            return editor;
        }
    }

    public synchronized Value get(String str) throws IOException {
        checkNotClosed();
        Entry entry = this.lruEntries.get(str);
        if (entry == null) {
            return null;
        }
        if (!entry.readable) {
            return null;
        }
        for (File file : entry.cleanFiles) {
            if (!file.exists()) {
                return null;
            }
        }
        this.redundantOpCount++;
        this.journalWriter.append((CharSequence) "READ");
        this.journalWriter.append(' ');
        this.journalWriter.append((CharSequence) str);
        this.journalWriter.append('\n');
        if (journalRebuildRequired()) {
            this.executorService.submit(this.cleanupCallable);
        }
        return new Value(this, str, entry.sequenceNumber, entry.cleanFiles, entry.lengths, null);
    }

    public final boolean journalRebuildRequired() {
        int i = this.redundantOpCount;
        return i >= 2000 && i >= this.lruEntries.size();
    }

    public final void processJournal() throws IOException {
        deleteIfExists(this.journalFileTmp);
        Iterator<Entry> it = this.lruEntries.values().iterator();
        while (it.hasNext()) {
            Entry next = it.next();
            int i = 0;
            if (next.currentEditor == null) {
                while (i < this.valueCount) {
                    this.size += next.lengths[i];
                    i++;
                }
            } else {
                next.currentEditor = null;
                while (i < this.valueCount) {
                    deleteIfExists(next.cleanFiles[i]);
                    deleteIfExists(next.dirtyFiles[i]);
                    i++;
                }
                it.remove();
            }
        }
    }

    public final void readJournal() throws IOException {
        StrictLineReader strictLineReader = new StrictLineReader(new FileInputStream(this.journalFile), Util.US_ASCII);
        try {
            String readLine = strictLineReader.readLine();
            String readLine2 = strictLineReader.readLine();
            String readLine3 = strictLineReader.readLine();
            String readLine4 = strictLineReader.readLine();
            String readLine5 = strictLineReader.readLine();
            if (!"libcore.io.DiskLruCache".equals(readLine) || !"1".equals(readLine2) || !Integer.toString(this.appVersion).equals(readLine3) || !Integer.toString(this.valueCount).equals(readLine4) || !"".equals(readLine5)) {
                StringBuilder sb = new StringBuilder(String.valueOf(readLine).length() + 35 + String.valueOf(readLine2).length() + String.valueOf(readLine4).length() + String.valueOf(readLine5).length());
                sb.append("unexpected journal header: [");
                sb.append(readLine);
                sb.append(", ");
                sb.append(readLine2);
                sb.append(", ");
                sb.append(readLine4);
                sb.append(", ");
                sb.append(readLine5);
                sb.append("]");
                throw new IOException(sb.toString());
            }
            boolean z = false;
            int i = 0;
            while (true) {
                try {
                    readJournalLine(strictLineReader.readLine());
                    i++;
                } catch (EOFException unused) {
                    this.redundantOpCount = i - this.lruEntries.size();
                    if (strictLineReader.end == -1) {
                        z = true;
                    }
                    if (z) {
                        rebuildJournal();
                    } else {
                        this.journalWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFile, true), Util.US_ASCII));
                    }
                    try {
                        strictLineReader.close();
                        return;
                    } catch (RuntimeException e) {
                        throw e;
                    } catch (Exception unused2) {
                        return;
                    }
                }
            }
        } catch (Throwable th) {
            try {
                strictLineReader.close();
            } catch (RuntimeException e2) {
                throw e2;
            } catch (Exception unused3) {
            }
            throw th;
        }
    }

    public final void readJournalLine(String str) throws IOException {
        String str2;
        int indexOf = str.indexOf(32);
        if (indexOf == -1) {
            throw new IOException(str.length() != 0 ? "unexpected journal line: ".concat(str) : new String("unexpected journal line: "));
        }
        int i = indexOf + 1;
        int indexOf2 = str.indexOf(32, i);
        if (indexOf2 == -1) {
            str2 = str.substring(i);
            if (indexOf == 6 && str.startsWith("REMOVE")) {
                this.lruEntries.remove(str2);
                return;
            }
        } else {
            str2 = str.substring(i, indexOf2);
        }
        Entry entry = this.lruEntries.get(str2);
        if (entry == null) {
            entry = new Entry(str2, null);
            this.lruEntries.put(str2, entry);
        }
        if (indexOf2 != -1 && indexOf == 5 && str.startsWith("CLEAN")) {
            String[] split = str.substring(indexOf2 + 1).split(" ");
            entry.readable = true;
            entry.currentEditor = null;
            if (split.length == DiskLruCache.this.valueCount) {
                for (int i2 = 0; i2 < split.length; i2++) {
                    try {
                        entry.lengths[i2] = Long.parseLong(split[i2]);
                    } catch (NumberFormatException unused) {
                        entry.invalidLengths(split);
                        throw null;
                    }
                }
                return;
            }
            entry.invalidLengths(split);
            throw null;
        } else if (indexOf2 == -1 && indexOf == 5 && str.startsWith("DIRTY")) {
            entry.currentEditor = new Editor(entry, null);
        } else if (indexOf2 != -1 || indexOf != 4 || !str.startsWith("READ")) {
            throw new IOException(str.length() != 0 ? "unexpected journal line: ".concat(str) : new String("unexpected journal line: "));
        }
    }

    public final synchronized void rebuildJournal() throws IOException {
        Writer writer = this.journalWriter;
        if (writer != null) {
            writer.close();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFileTmp), Util.US_ASCII));
        bufferedWriter.write("libcore.io.DiskLruCache");
        bufferedWriter.write("\n");
        bufferedWriter.write("1");
        bufferedWriter.write("\n");
        bufferedWriter.write(Integer.toString(this.appVersion));
        bufferedWriter.write("\n");
        bufferedWriter.write(Integer.toString(this.valueCount));
        bufferedWriter.write("\n");
        bufferedWriter.write("\n");
        for (Entry entry : this.lruEntries.values()) {
            if (entry.currentEditor != null) {
                String str = entry.key;
                StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 7);
                sb.append("DIRTY ");
                sb.append(str);
                sb.append('\n');
                bufferedWriter.write(sb.toString());
            } else {
                String str2 = entry.key;
                String lengths = entry.getLengths();
                StringBuilder sb2 = new StringBuilder(String.valueOf(str2).length() + 7 + String.valueOf(lengths).length());
                sb2.append("CLEAN ");
                sb2.append(str2);
                sb2.append(lengths);
                sb2.append('\n');
                bufferedWriter.write(sb2.toString());
            }
        }
        bufferedWriter.close();
        if (this.journalFile.exists()) {
            renameTo(this.journalFile, this.journalFileBackup, true);
        }
        renameTo(this.journalFileTmp, this.journalFile, false);
        this.journalFileBackup.delete();
        this.journalWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFile, true), Util.US_ASCII));
    }

    public final void trimToSize() throws IOException {
        while (this.size > this.maxSize) {
            String key = this.lruEntries.entrySet().iterator().next().getKey();
            synchronized (this) {
                checkNotClosed();
                Entry entry = this.lruEntries.get(key);
                if (entry != null && entry.currentEditor == null) {
                    for (int i = 0; i < this.valueCount; i++) {
                        File file = entry.cleanFiles[i];
                        if (file.exists() && !file.delete()) {
                            String valueOf = String.valueOf(file);
                            StringBuilder sb = new StringBuilder(valueOf.length() + 17);
                            sb.append("failed to delete ");
                            sb.append(valueOf);
                            throw new IOException(sb.toString());
                        }
                        long j = this.size;
                        long[] jArr = entry.lengths;
                        this.size = j - jArr[i];
                        jArr[i] = 0;
                    }
                    this.redundantOpCount++;
                    this.journalWriter.append((CharSequence) "REMOVE");
                    this.journalWriter.append(' ');
                    this.journalWriter.append((CharSequence) key);
                    this.journalWriter.append('\n');
                    this.lruEntries.remove(key);
                    if (journalRebuildRequired()) {
                        this.executorService.submit(this.cleanupCallable);
                    }
                }
            }
        }
    }
}
