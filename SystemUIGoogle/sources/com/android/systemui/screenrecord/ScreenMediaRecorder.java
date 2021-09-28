package com.android.systemui.screenrecord;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.display.VirtualDisplay;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.media.projection.IMediaProjection;
import android.media.projection.IMediaProjectionManager;
import android.media.projection.MediaProjection;
import android.net.Uri;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.WindowManager;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
/* loaded from: classes.dex */
public class ScreenMediaRecorder {
    private ScreenInternalAudioRecorder mAudio;
    private ScreenRecordingAudioSource mAudioSource;
    private Context mContext;
    private Surface mInputSurface;
    MediaRecorder.OnInfoListener mListener;
    private MediaProjection mMediaProjection;
    private MediaRecorder mMediaRecorder;
    private ScreenRecordingMuxer mMuxer;
    private File mTempAudioFile;
    private File mTempVideoFile;
    private int mUser;
    private VirtualDisplay mVirtualDisplay;

    public ScreenMediaRecorder(Context context, int i, ScreenRecordingAudioSource screenRecordingAudioSource, MediaRecorder.OnInfoListener onInfoListener) {
        this.mContext = context;
        this.mUser = i;
        this.mListener = onInfoListener;
        this.mAudioSource = screenRecordingAudioSource;
    }

    private void prepare() throws IOException, RemoteException, RuntimeException {
        boolean z = false;
        this.mMediaProjection = new MediaProjection(this.mContext, IMediaProjection.Stub.asInterface(IMediaProjectionManager.Stub.asInterface(ServiceManager.getService("media_projection")).createProjection(this.mUser, this.mContext.getPackageName(), 0, false).asBinder()));
        File cacheDir = this.mContext.getCacheDir();
        cacheDir.mkdirs();
        this.mTempVideoFile = File.createTempFile("temp", ".mp4", cacheDir);
        MediaRecorder mediaRecorder = new MediaRecorder();
        this.mMediaRecorder = mediaRecorder;
        ScreenRecordingAudioSource screenRecordingAudioSource = this.mAudioSource;
        ScreenRecordingAudioSource screenRecordingAudioSource2 = ScreenRecordingAudioSource.MIC;
        if (screenRecordingAudioSource == screenRecordingAudioSource2) {
            mediaRecorder.setAudioSource(0);
        }
        this.mMediaRecorder.setVideoSource(2);
        this.mMediaRecorder.setOutputFormat(2);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) this.mContext.getSystemService("window");
        windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
        int[] supportedSize = getSupportedSize(displayMetrics.widthPixels, displayMetrics.heightPixels, (int) windowManager.getDefaultDisplay().getRefreshRate());
        int i = supportedSize[0];
        int i2 = supportedSize[1];
        int i3 = supportedSize[2];
        this.mMediaRecorder.setVideoEncoder(2);
        this.mMediaRecorder.setVideoEncodingProfileLevel(8, 256);
        this.mMediaRecorder.setVideoSize(i, i2);
        this.mMediaRecorder.setVideoFrameRate(i3);
        this.mMediaRecorder.setVideoEncodingBitRate((((i * i2) * i3) / 30) * 6);
        this.mMediaRecorder.setMaxDuration(3600000);
        this.mMediaRecorder.setMaxFileSize(5000000000L);
        if (this.mAudioSource == screenRecordingAudioSource2) {
            this.mMediaRecorder.setAudioEncoder(4);
            this.mMediaRecorder.setAudioChannels(1);
            this.mMediaRecorder.setAudioEncodingBitRate(196000);
            this.mMediaRecorder.setAudioSamplingRate(44100);
        }
        this.mMediaRecorder.setOutputFile(this.mTempVideoFile);
        this.mMediaRecorder.prepare();
        Surface surface = this.mMediaRecorder.getSurface();
        this.mInputSurface = surface;
        this.mVirtualDisplay = this.mMediaProjection.createVirtualDisplay("Recording Display", i, i2, displayMetrics.densityDpi, 16, surface, null, null);
        this.mMediaRecorder.setOnInfoListener(this.mListener);
        ScreenRecordingAudioSource screenRecordingAudioSource3 = this.mAudioSource;
        if (screenRecordingAudioSource3 == ScreenRecordingAudioSource.INTERNAL || screenRecordingAudioSource3 == ScreenRecordingAudioSource.MIC_AND_INTERNAL) {
            this.mTempAudioFile = File.createTempFile("temp", ".aac", this.mContext.getCacheDir());
            String absolutePath = this.mTempAudioFile.getAbsolutePath();
            MediaProjection mediaProjection = this.mMediaProjection;
            if (this.mAudioSource == ScreenRecordingAudioSource.MIC_AND_INTERNAL) {
                z = true;
            }
            this.mAudio = new ScreenInternalAudioRecorder(absolutePath, mediaProjection, z);
        }
    }

    private int[] getSupportedSize(int i, int i2, int i3) {
        int i4;
        String[] strArr;
        int i5;
        MediaCodecInfo[] mediaCodecInfoArr;
        MediaCodecInfo mediaCodecInfo;
        MediaCodecInfo.VideoCapabilities videoCapabilities;
        MediaCodecInfo.CodecCapabilities capabilitiesForType;
        int i6;
        int i7;
        int i8 = i3;
        int i9 = 0;
        MediaCodecInfo[] codecInfos = new MediaCodecList(0).getCodecInfos();
        int length = codecInfos.length;
        double d = 0.0d;
        MediaCodecInfo.VideoCapabilities videoCapabilities2 = null;
        int i10 = 0;
        while (i10 < length) {
            MediaCodecInfo mediaCodecInfo2 = codecInfos[i10];
            String[] supportedTypes = mediaCodecInfo2.getSupportedTypes();
            int length2 = supportedTypes.length;
            while (i9 < length2) {
                if (!supportedTypes[i9].equalsIgnoreCase("video/avc") || (capabilitiesForType = mediaCodecInfo2.getCapabilitiesForType("video/avc")) == null || capabilitiesForType.getVideoCapabilities() == null) {
                    mediaCodecInfoArr = codecInfos;
                    i5 = length;
                    videoCapabilities = videoCapabilities2;
                    strArr = supportedTypes;
                    i4 = length2;
                    mediaCodecInfo = mediaCodecInfo2;
                } else {
                    MediaCodecInfo.VideoCapabilities videoCapabilities3 = capabilitiesForType.getVideoCapabilities();
                    mediaCodecInfoArr = codecInfos;
                    int intValue = videoCapabilities3.getSupportedWidths().getUpper().intValue();
                    i5 = length;
                    int intValue2 = videoCapabilities3.getSupportedHeights().getUpper().intValue();
                    if (i % videoCapabilities3.getWidthAlignment() != 0) {
                        i6 = i - (i % videoCapabilities3.getWidthAlignment());
                        videoCapabilities = videoCapabilities2;
                    } else {
                        videoCapabilities = videoCapabilities2;
                        i6 = i;
                    }
                    if (i2 % videoCapabilities3.getHeightAlignment() != 0) {
                        i7 = i2 - (i2 % videoCapabilities3.getHeightAlignment());
                        strArr = supportedTypes;
                    } else {
                        strArr = supportedTypes;
                        i7 = i2;
                    }
                    if (intValue < i6 || intValue2 < i7 || !videoCapabilities3.isSizeSupported(i6, i7)) {
                        videoCapabilities2 = videoCapabilities3;
                        i4 = length2;
                        double d2 = (double) intValue2;
                        mediaCodecInfo = mediaCodecInfo2;
                        double min = Math.min(((double) intValue) / ((double) i), d2 / ((double) i2));
                        if (min > d) {
                            d = Math.min(1.0d, min);
                            i9++;
                            i8 = i3;
                            mediaCodecInfo2 = mediaCodecInfo;
                            codecInfos = mediaCodecInfoArr;
                            length = i5;
                            supportedTypes = strArr;
                            length2 = i4;
                        }
                    } else {
                        int intValue3 = videoCapabilities3.getSupportedFrameRatesFor(i6, i7).getUpper().intValue();
                        if (intValue3 >= i8) {
                            intValue3 = i8;
                        }
                        Log.d("ScreenMediaRecorder", "Screen size supported at rate " + intValue3);
                        return new int[]{i6, i7, intValue3};
                    }
                }
                videoCapabilities2 = videoCapabilities;
                i9++;
                i8 = i3;
                mediaCodecInfo2 = mediaCodecInfo;
                codecInfos = mediaCodecInfoArr;
                length = i5;
                supportedTypes = strArr;
                length2 = i4;
            }
            i10++;
            i8 = i3;
            i9 = 0;
        }
        int i11 = (int) (((double) i) * d);
        int i12 = (int) (((double) i2) * d);
        if (i11 % videoCapabilities2.getWidthAlignment() != 0) {
            i11 -= i11 % videoCapabilities2.getWidthAlignment();
        }
        if (i12 % videoCapabilities2.getHeightAlignment() != 0) {
            i12 -= i12 % videoCapabilities2.getHeightAlignment();
        }
        int intValue4 = videoCapabilities2.getSupportedFrameRatesFor(i11, i12).getUpper().intValue();
        if (intValue4 >= i3) {
            intValue4 = i3;
        }
        Log.d("ScreenMediaRecorder", "Resized by " + d + ": " + i11 + ", " + i12 + ", " + intValue4);
        return new int[]{i11, i12, intValue4};
    }

    /* access modifiers changed from: package-private */
    public void start() throws IOException, RemoteException, RuntimeException {
        Log.d("ScreenMediaRecorder", "start recording");
        prepare();
        this.mMediaRecorder.start();
        recordInternalAudio();
    }

    /* access modifiers changed from: package-private */
    public void end() {
        this.mMediaRecorder.stop();
        this.mMediaRecorder.release();
        this.mInputSurface.release();
        this.mVirtualDisplay.release();
        this.mMediaProjection.stop();
        this.mMediaRecorder = null;
        this.mMediaProjection = null;
        stopInternalAudioRecording();
        Log.d("ScreenMediaRecorder", "end recording");
    }

    private void stopInternalAudioRecording() {
        ScreenRecordingAudioSource screenRecordingAudioSource = this.mAudioSource;
        if (screenRecordingAudioSource == ScreenRecordingAudioSource.INTERNAL || screenRecordingAudioSource == ScreenRecordingAudioSource.MIC_AND_INTERNAL) {
            this.mAudio.end();
            this.mAudio = null;
        }
    }

    private void recordInternalAudio() throws IllegalStateException {
        ScreenRecordingAudioSource screenRecordingAudioSource = this.mAudioSource;
        if (screenRecordingAudioSource == ScreenRecordingAudioSource.INTERNAL || screenRecordingAudioSource == ScreenRecordingAudioSource.MIC_AND_INTERNAL) {
            this.mAudio.start();
        }
    }

    /* access modifiers changed from: protected */
    public SavedRecording save() throws IOException {
        String format = new SimpleDateFormat("'screen-'yyyyMMdd-HHmmss'.mp4'").format(new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put("_display_name", format);
        contentValues.put("mime_type", "video/mp4");
        contentValues.put("date_added", Long.valueOf(System.currentTimeMillis()));
        contentValues.put("datetaken", Long.valueOf(System.currentTimeMillis()));
        ContentResolver contentResolver = this.mContext.getContentResolver();
        Uri insert = contentResolver.insert(MediaStore.Video.Media.getContentUri("external_primary"), contentValues);
        Log.d("ScreenMediaRecorder", insert.toString());
        ScreenRecordingAudioSource screenRecordingAudioSource = this.mAudioSource;
        if (screenRecordingAudioSource == ScreenRecordingAudioSource.MIC_AND_INTERNAL || screenRecordingAudioSource == ScreenRecordingAudioSource.INTERNAL) {
            try {
                Log.d("ScreenMediaRecorder", "muxing recording");
                File createTempFile = File.createTempFile("temp", ".mp4", this.mContext.getCacheDir());
                ScreenRecordingMuxer screenRecordingMuxer = new ScreenRecordingMuxer(0, createTempFile.getAbsolutePath(), this.mTempVideoFile.getAbsolutePath(), this.mTempAudioFile.getAbsolutePath());
                this.mMuxer = screenRecordingMuxer;
                screenRecordingMuxer.mux();
                this.mTempVideoFile.delete();
                this.mTempVideoFile = createTempFile;
            } catch (IOException e) {
                Log.e("ScreenMediaRecorder", "muxing recording " + e.getMessage());
                e.printStackTrace();
            }
        }
        OutputStream openOutputStream = contentResolver.openOutputStream(insert, "w");
        Files.copy(this.mTempVideoFile.toPath(), openOutputStream);
        openOutputStream.close();
        File file = this.mTempAudioFile;
        if (file != null) {
            file.delete();
        }
        DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
        SavedRecording savedRecording = new SavedRecording(insert, this.mTempVideoFile, new Size(displayMetrics.widthPixels, displayMetrics.heightPixels));
        this.mTempVideoFile.delete();
        return savedRecording;
    }

    /* loaded from: classes.dex */
    public class SavedRecording {
        private Bitmap mThumbnailBitmap;
        private Uri mUri;

        protected SavedRecording(Uri uri, File file, Size size) {
            this.mUri = uri;
            try {
                this.mThumbnailBitmap = ThumbnailUtils.createVideoThumbnail(file, size, null);
            } catch (IOException e) {
                Log.e("ScreenMediaRecorder", "Error creating thumbnail", e);
            }
        }

        public Uri getUri() {
            return this.mUri;
        }

        public Bitmap getThumbnail() {
            return this.mThumbnailBitmap;
        }
    }
}
