package com.android.systemui.screenshot;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.FileUtils;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.os.Trace;
import android.provider.MediaStore;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.exifinterface.media.ExifInterface;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.screenshot.ImageExporter;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.util.UUID;
import java.util.concurrent.Executor;
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ImageExporter {
    private Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.PNG;
    private int mQuality = 100;
    private final ContentResolver mResolver;
    private static final String TAG = LogConfig.logTag(ImageExporter.class);
    static final Duration PENDING_ENTRY_TTL = Duration.ofHours(24);
    private static final String SCREENSHOTS_PATH = Environment.DIRECTORY_PICTURES + File.separator + Environment.DIRECTORY_SCREENSHOTS;

    /* access modifiers changed from: package-private */
    public ImageExporter(ContentResolver contentResolver) {
        this.mResolver = contentResolver;
    }

    /* access modifiers changed from: package-private */
    public ListenableFuture<File> exportToRawFile(Executor executor, Bitmap bitmap, File file) {
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver(executor, file, bitmap) { // from class: com.android.systemui.screenshot.ImageExporter$$ExternalSyntheticLambda0
            public final /* synthetic */ Executor f$1;
            public final /* synthetic */ File f$2;
            public final /* synthetic */ Bitmap f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                return ImageExporter.$r8$lambda$yjrfcN9tIQcZd4RAekYYOMRhu14(ImageExporter.this, this.f$1, this.f$2, this.f$3, completer);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Object lambda$exportToRawFile$1(Executor executor, File file, Bitmap bitmap, CallbackToFutureAdapter.Completer completer) throws Exception {
        executor.execute(new Runnable(file, bitmap, completer) { // from class: com.android.systemui.screenshot.ImageExporter$$ExternalSyntheticLambda3
            public final /* synthetic */ File f$1;
            public final /* synthetic */ Bitmap f$2;
            public final /* synthetic */ CallbackToFutureAdapter.Completer f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // java.lang.Runnable
            public final void run() {
                ImageExporter.$r8$lambda$OnqINtOL69BvGyNJ0WTEtPAkevk(ImageExporter.this, this.f$1, this.f$2, this.f$3);
            }
        });
        return "Bitmap#compress";
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$exportToRawFile$0(File file, Bitmap bitmap, CallbackToFutureAdapter.Completer completer) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(this.mCompressFormat, this.mQuality, fileOutputStream);
            completer.set(file);
            fileOutputStream.close();
        } catch (IOException e) {
            if (file.exists()) {
                file.delete();
            }
            completer.setException(e);
        }
    }

    /* access modifiers changed from: package-private */
    public ListenableFuture<Result> export(Executor executor, UUID uuid, Bitmap bitmap) {
        return export(executor, uuid, bitmap, ZonedDateTime.now());
    }

    /* access modifiers changed from: package-private */
    public ListenableFuture<Result> export(Executor executor, UUID uuid, Bitmap bitmap, ZonedDateTime zonedDateTime) {
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver(executor, new Task(this.mResolver, uuid, bitmap, zonedDateTime, this.mCompressFormat, this.mQuality, true)) { // from class: com.android.systemui.screenshot.ImageExporter$$ExternalSyntheticLambda1
            public final /* synthetic */ Executor f$0;
            public final /* synthetic */ ImageExporter.Task f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                return ImageExporter.m216$r8$lambda$qma6MDVusfuK9lzVXtmOkUzGk(this.f$0, this.f$1, completer);
            }
        });
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ Object lambda$export$3(Executor executor, Task task, CallbackToFutureAdapter.Completer completer) throws Exception {
        executor.execute(new Runnable(task) { // from class: com.android.systemui.screenshot.ImageExporter$$ExternalSyntheticLambda2
            public final /* synthetic */ ImageExporter.Task f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                ImageExporter.$r8$lambda$KIet6qHM6e0ch093NbaNOXoCeoI(CallbackToFutureAdapter.Completer.this, this.f$1);
            }
        });
        return task;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$export$2(CallbackToFutureAdapter.Completer completer, Task task) {
        try {
            completer.set(task.execute());
        } catch (ImageExportException | InterruptedException e) {
            completer.setException(e);
        }
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Result {
        boolean deleted;
        String fileName;
        Bitmap.CompressFormat format;
        boolean published;
        UUID requestId;
        long timestamp;
        Uri uri;

        Result() {
        }

        public String toString() {
            return "Result{uri=" + this.uri + ", requestId=" + this.requestId + ", fileName='" + this.fileName + "', timestamp=" + this.timestamp + ", format=" + this.format + ", published=" + this.published + ", deleted=" + this.deleted + '}';
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Task {
        private final Bitmap mBitmap;
        private final ZonedDateTime mCaptureTime;
        private final String mFileName;
        private final Bitmap.CompressFormat mFormat;
        private final boolean mPublish;
        private final int mQuality;
        private final UUID mRequestId;
        private final ContentResolver mResolver;

        Task(ContentResolver contentResolver, UUID uuid, Bitmap bitmap, ZonedDateTime zonedDateTime, Bitmap.CompressFormat compressFormat, int i, boolean z) {
            this.mResolver = contentResolver;
            this.mRequestId = uuid;
            this.mBitmap = bitmap;
            this.mCaptureTime = zonedDateTime;
            this.mFormat = compressFormat;
            this.mQuality = i;
            this.mFileName = ImageExporter.createFilename(zonedDateTime, compressFormat);
            this.mPublish = z;
        }

        public Result execute() throws ImageExportException, InterruptedException {
            Uri uri;
            ImageExportException e;
            Trace.beginSection("ImageExporter_execute");
            Result result = new Result();
            try {
                try {
                    uri = ImageExporter.createEntry(this.mResolver, this.mFormat, this.mCaptureTime, this.mFileName);
                } catch (ImageExportException e2) {
                    e = e2;
                    uri = null;
                }
                try {
                    ImageExporter.throwIfInterrupted();
                    ImageExporter.writeImage(this.mResolver, this.mBitmap, this.mFormat, this.mQuality, uri);
                    ImageExporter.throwIfInterrupted();
                    ImageExporter.writeExif(this.mResolver, uri, this.mRequestId, this.mBitmap.getWidth(), this.mBitmap.getHeight(), this.mCaptureTime);
                    ImageExporter.throwIfInterrupted();
                    if (this.mPublish) {
                        ImageExporter.publishEntry(this.mResolver, uri);
                        result.published = true;
                    }
                    result.timestamp = this.mCaptureTime.toInstant().toEpochMilli();
                    result.requestId = this.mRequestId;
                    result.uri = uri;
                    result.fileName = this.mFileName;
                    result.format = this.mFormat;
                    return result;
                } catch (ImageExportException e3) {
                    e = e3;
                    if (uri != null) {
                        this.mResolver.delete(uri, null);
                    }
                    throw e;
                }
            } finally {
                Trace.endSection();
            }
        }

        public String toString() {
            return "export [" + this.mBitmap + "] to [" + this.mFormat + "] at quality " + this.mQuality;
        }
    }

    /* access modifiers changed from: private */
    public static Uri createEntry(ContentResolver contentResolver, Bitmap.CompressFormat compressFormat, ZonedDateTime zonedDateTime, String str) throws ImageExportException {
        Trace.beginSection("ImageExporter_createEntry");
        try {
            Uri insert = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, createMetadata(zonedDateTime, compressFormat, str));
            if (insert != null) {
                return insert;
            }
            throw new ImageExportException("ContentResolver#insert returned null.");
        } finally {
            Trace.endSection();
        }
    }

    /* access modifiers changed from: private */
    public static void writeImage(ContentResolver contentResolver, Bitmap bitmap, Bitmap.CompressFormat compressFormat, int i, Uri uri) throws ImageExportException {
        try {
            Trace.beginSection("ImageExporter_writeImage");
            try {
                OutputStream openOutputStream = contentResolver.openOutputStream(uri);
                try {
                    SystemClock.elapsedRealtime();
                    if (bitmap.compress(compressFormat, i, openOutputStream)) {
                        if (openOutputStream != null) {
                            openOutputStream.close();
                        }
                        return;
                    }
                    throw new ImageExportException("Bitmap.compress returned false. (Failure unknown)");
                } catch (Throwable th) {
                    if (openOutputStream != null) {
                        try {
                            openOutputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (IOException e) {
                throw new ImageExportException("ContentResolver#openOutputStream threw an exception.", e);
            }
        } finally {
            Trace.endSection();
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: android.content.ContentResolver */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [android.os.CancellationSignal, java.lang.AutoCloseable] */
    /* access modifiers changed from: private */
    public static void writeExif(ContentResolver contentResolver, Uri uri, UUID uuid, int i, int i2, ZonedDateTime zonedDateTime) throws ImageExportException {
        ParcelFileDescriptor parcelFileDescriptor;
        try {
            Trace.beginSection("ImageExporter_writeExif");
            parcelFileDescriptor = 0;
            try {
                parcelFileDescriptor = contentResolver.openFile(uri, "rw", parcelFileDescriptor);
                if (parcelFileDescriptor != null) {
                    try {
                        ExifInterface exifInterface = new ExifInterface(parcelFileDescriptor.getFileDescriptor());
                        updateExifAttributes(exifInterface, uuid, i, i2, zonedDateTime);
                        try {
                            exifInterface.saveAttributes();
                        } catch (IOException e) {
                            throw new ImageExportException("ExifInterface threw an exception writing to the file descriptor.", e);
                        }
                    } catch (IOException e2) {
                        throw new ImageExportException("ExifInterface threw an exception reading from the file descriptor.", e2);
                    }
                } else {
                    throw new ImageExportException("ContentResolver#openFile returned null.");
                }
            } catch (FileNotFoundException e3) {
                throw new ImageExportException("ContentResolver#openFile threw an exception.", e3);
            }
        } finally {
            FileUtils.closeQuietly((AutoCloseable) parcelFileDescriptor);
            Trace.endSection();
        }
    }

    /* access modifiers changed from: private */
    public static void publishEntry(ContentResolver contentResolver, Uri uri) throws ImageExportException {
        Trace.beginSection("ImageExporter_publishEntry");
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("is_pending", (Integer) 0);
            contentValues.putNull("date_expires");
            if (contentResolver.update(uri, contentValues, null) < 1) {
                throw new ImageExportException("Failed to publish entry. ContentResolver#update reported no rows updated.");
            }
        } finally {
            Trace.endSection();
        }
    }

    @VisibleForTesting
    static String createFilename(ZonedDateTime zonedDateTime, Bitmap.CompressFormat compressFormat) {
        return String.format("Screenshot_%1$tY%<tm%<td-%<tH%<tM%<tS.%2$s", zonedDateTime, fileExtension(compressFormat));
    }

    static ContentValues createMetadata(ZonedDateTime zonedDateTime, Bitmap.CompressFormat compressFormat, String str) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("relative_path", SCREENSHOTS_PATH);
        contentValues.put("_display_name", str);
        contentValues.put("mime_type", getMimeType(compressFormat));
        contentValues.put("date_added", Long.valueOf(zonedDateTime.toEpochSecond()));
        contentValues.put("date_modified", Long.valueOf(zonedDateTime.toEpochSecond()));
        contentValues.put("date_expires", Long.valueOf(zonedDateTime.plus((TemporalAmount) PENDING_ENTRY_TTL).toEpochSecond()));
        contentValues.put("is_pending", (Integer) 1);
        return contentValues;
    }

    static void updateExifAttributes(ExifInterface exifInterface, UUID uuid, int i, int i2, ZonedDateTime zonedDateTime) {
        exifInterface.setAttribute("ImageUniqueID", uuid.toString());
        exifInterface.setAttribute("Software", "Android " + Build.DISPLAY);
        exifInterface.setAttribute("ImageWidth", Integer.toString(i));
        exifInterface.setAttribute("ImageLength", Integer.toString(i2));
        String format = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss").format(zonedDateTime);
        String format2 = DateTimeFormatter.ofPattern("SSS").format(zonedDateTime);
        String format3 = DateTimeFormatter.ofPattern("xxx").format(zonedDateTime);
        exifInterface.setAttribute("DateTimeOriginal", format);
        exifInterface.setAttribute("SubSecTimeOriginal", format2);
        exifInterface.setAttribute("OffsetTimeOriginal", format3);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.screenshot.ImageExporter$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$CompressFormat;

        static {
            int[] iArr = new int[Bitmap.CompressFormat.values().length];
            $SwitchMap$android$graphics$Bitmap$CompressFormat = iArr;
            try {
                iArr[Bitmap.CompressFormat.JPEG.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$CompressFormat[Bitmap.CompressFormat.PNG.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$CompressFormat[Bitmap.CompressFormat.WEBP.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$CompressFormat[Bitmap.CompressFormat.WEBP_LOSSLESS.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$CompressFormat[Bitmap.CompressFormat.WEBP_LOSSY.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    static String getMimeType(Bitmap.CompressFormat compressFormat) {
        int i = AnonymousClass1.$SwitchMap$android$graphics$Bitmap$CompressFormat[compressFormat.ordinal()];
        if (i == 1) {
            return "image/jpeg";
        }
        if (i == 2) {
            return "image/png";
        }
        if (i == 3 || i == 4 || i == 5) {
            return "image/webp";
        }
        throw new IllegalArgumentException("Unknown CompressFormat!");
    }

    static String fileExtension(Bitmap.CompressFormat compressFormat) {
        int i = AnonymousClass1.$SwitchMap$android$graphics$Bitmap$CompressFormat[compressFormat.ordinal()];
        if (i == 1) {
            return "jpg";
        }
        if (i == 2) {
            return "png";
        }
        if (i == 3 || i == 4 || i == 5) {
            return "webp";
        }
        throw new IllegalArgumentException("Unknown CompressFormat!");
    }

    /* access modifiers changed from: private */
    public static void throwIfInterrupted() throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class ImageExportException extends IOException {
        ImageExportException(String str) {
            super(str);
        }

        ImageExportException(String str, Throwable th) {
            super(str, th);
        }
    }
}
