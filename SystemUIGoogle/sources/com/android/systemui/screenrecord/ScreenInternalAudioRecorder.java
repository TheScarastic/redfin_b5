package com.android.systemui.screenrecord;

import android.media.AudioFormat;
import android.media.AudioPlaybackCaptureConfiguration;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.projection.MediaProjection;
import android.util.Log;
import android.view.Surface;
import java.io.IOException;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class ScreenInternalAudioRecorder {
    private static String TAG = "ScreenAudioRecorder";
    private AudioRecord mAudioRecord;
    private AudioRecord mAudioRecordMic;
    private MediaCodec mCodec;
    private MediaProjection mMediaProjection;
    private boolean mMic;
    private MediaMuxer mMuxer;
    private long mPresentationTime;
    private boolean mStarted;
    private Thread mThread;
    private long mTotalBytes;
    private Config mConfig = new Config();
    private int mTrackId = -1;

    public ScreenInternalAudioRecorder(String str, MediaProjection mediaProjection, boolean z) throws IOException {
        this.mMic = z;
        this.mMuxer = new MediaMuxer(str, 0);
        this.mMediaProjection = mediaProjection;
        String str2 = TAG;
        Log.d(str2, "creating audio file " + str);
        setupSimple();
    }

    /* loaded from: classes.dex */
    public static class Config {
        public int channelOutMask = 4;
        public int channelInMask = 16;
        public int encoding = 2;
        public int sampleRate = 44100;
        public int bitRate = 196000;
        public int bufferSizeBytes = 131072;
        public boolean privileged = true;
        public boolean legacy_app_looback = false;

        public String toString() {
            return "channelMask=" + this.channelOutMask + "\n   encoding=" + this.encoding + "\n sampleRate=" + this.sampleRate + "\n bufferSize=" + this.bufferSizeBytes + "\n privileged=" + this.privileged + "\n legacy app looback=" + this.legacy_app_looback;
        }
    }

    private void setupSimple() throws IOException {
        Config config = this.mConfig;
        int minBufferSize = AudioRecord.getMinBufferSize(config.sampleRate, config.channelInMask, config.encoding) * 2;
        String str = TAG;
        Log.d(str, "audio buffer size: " + minBufferSize);
        AudioFormat build = new AudioFormat.Builder().setEncoding(this.mConfig.encoding).setSampleRate(this.mConfig.sampleRate).setChannelMask(this.mConfig.channelOutMask).build();
        this.mAudioRecord = new AudioRecord.Builder().setAudioFormat(build).setAudioPlaybackCaptureConfig(new AudioPlaybackCaptureConfiguration.Builder(this.mMediaProjection).addMatchingUsage(1).addMatchingUsage(0).addMatchingUsage(14).build()).build();
        if (this.mMic) {
            Config config2 = this.mConfig;
            this.mAudioRecordMic = new AudioRecord(7, config2.sampleRate, 16, config2.encoding, minBufferSize);
        }
        this.mCodec = MediaCodec.createEncoderByType("audio/mp4a-latm");
        MediaFormat createAudioFormat = MediaFormat.createAudioFormat("audio/mp4a-latm", this.mConfig.sampleRate, 1);
        createAudioFormat.setInteger("aac-profile", 2);
        createAudioFormat.setInteger("bitrate", this.mConfig.bitRate);
        createAudioFormat.setInteger("pcm-encoding", this.mConfig.encoding);
        this.mCodec.configure(createAudioFormat, (Surface) null, (MediaCrypto) null, 1);
        this.mThread = new Thread(new Runnable(minBufferSize) { // from class: com.android.systemui.screenrecord.ScreenInternalAudioRecorder$$ExternalSyntheticLambda0
            public final /* synthetic */ int f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                ScreenInternalAudioRecorder.m212$r8$lambda$giSe2V7T3s4zBoy2_2AQ5tkcd8(ScreenInternalAudioRecorder.this, this.f$1);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setupSimple$0(int i) {
        short[] sArr;
        short[] sArr2;
        int i2;
        int i3;
        byte[] bArr = null;
        if (this.mMic) {
            int i4 = i / 2;
            sArr2 = new short[i4];
            sArr = new short[i4];
        } else {
            bArr = new byte[i];
            sArr2 = null;
            sArr = null;
        }
        while (true) {
            int i5 = 0;
            if (this.mMic) {
                int read = this.mAudioRecord.read(sArr2, 0, sArr2.length);
                int read2 = this.mAudioRecordMic.read(sArr, 0, sArr.length);
                sArr = scaleValues(sArr, read2, 1.4f);
                i3 = Math.min(read, read2) * 2;
                bArr = addAndConvertBuffers(sArr2, read, sArr, read2);
                i5 = read;
                i2 = read2;
            } else {
                i3 = this.mAudioRecord.read(bArr, 0, bArr.length);
                i2 = 0;
            }
            if (i3 < 0) {
                Log.e(TAG, "read error " + i3 + ", shorts internal: " + i5 + ", shorts mic: " + i2);
                endStream();
                return;
            }
            encode(bArr, i3);
        }
    }

    private short[] scaleValues(short[] sArr, int i, float f) {
        for (int i2 = 0; i2 < i; i2++) {
            short s = sArr[i2];
            int i3 = (int) (((float) sArr[i2]) * f);
            if (i3 > 32767) {
                i3 = 32767;
            } else if (i3 < -32768) {
                i3 = -32768;
            }
            sArr[i2] = (short) i3;
        }
        return sArr;
    }

    private byte[] addAndConvertBuffers(short[] sArr, int i, short[] sArr2, int i2) {
        int i3;
        int max = Math.max(i, i2);
        if (max < 0) {
            return new byte[0];
        }
        byte[] bArr = new byte[max * 2];
        for (int i4 = 0; i4 < max; i4++) {
            if (i4 > i) {
                i3 = sArr2[i4];
            } else if (i4 > i2) {
                i3 = sArr[i4];
            } else {
                i3 = sArr[i4] + sArr2[i4];
            }
            if (i3 > 32767) {
                i3 = 32767;
            }
            if (i3 < -32768) {
                i3 = -32768;
            }
            int i5 = i4 * 2;
            bArr[i5] = (byte) (i3 & 255);
            bArr[i5 + 1] = (byte) ((i3 >> 8) & 255);
        }
        return bArr;
    }

    private void encode(byte[] bArr, int i) {
        int i2 = 0;
        while (i > 0) {
            int dequeueInputBuffer = this.mCodec.dequeueInputBuffer(500);
            if (dequeueInputBuffer < 0) {
                writeOutput();
                return;
            }
            ByteBuffer inputBuffer = this.mCodec.getInputBuffer(dequeueInputBuffer);
            inputBuffer.clear();
            int capacity = inputBuffer.capacity();
            int i3 = i > capacity ? capacity : i;
            i -= i3;
            inputBuffer.put(bArr, i2, i3);
            i2 += i3;
            this.mCodec.queueInputBuffer(dequeueInputBuffer, 0, i3, this.mPresentationTime, 0);
            long j = this.mTotalBytes + ((long) (i3 + 0));
            this.mTotalBytes = j;
            this.mPresentationTime = ((j / 2) * 1000000) / ((long) this.mConfig.sampleRate);
            writeOutput();
        }
    }

    private void endStream() {
        this.mCodec.queueInputBuffer(this.mCodec.dequeueInputBuffer(500), 0, 0, this.mPresentationTime, 4);
        writeOutput();
    }

    private void writeOutput() {
        while (true) {
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int dequeueOutputBuffer = this.mCodec.dequeueOutputBuffer(bufferInfo, 500);
            if (dequeueOutputBuffer == -2) {
                this.mTrackId = this.mMuxer.addTrack(this.mCodec.getOutputFormat());
                this.mMuxer.start();
            } else if (dequeueOutputBuffer != -1 && this.mTrackId >= 0) {
                ByteBuffer outputBuffer = this.mCodec.getOutputBuffer(dequeueOutputBuffer);
                if ((bufferInfo.flags & 2) == 0 || bufferInfo.size == 0) {
                    this.mMuxer.writeSampleData(this.mTrackId, outputBuffer, bufferInfo);
                }
                this.mCodec.releaseOutputBuffer(dequeueOutputBuffer, false);
            } else {
                return;
            }
        }
    }

    public synchronized void start() throws IllegalStateException {
        if (!this.mStarted) {
            this.mStarted = true;
            this.mAudioRecord.startRecording();
            if (this.mMic) {
                this.mAudioRecordMic.startRecording();
            }
            String str = TAG;
            Log.d(str, "channel count " + this.mAudioRecord.getChannelCount());
            this.mCodec.start();
            if (this.mAudioRecord.getRecordingState() == 3) {
                this.mThread.start();
            } else {
                throw new IllegalStateException("Audio recording failed to start");
            }
        } else if (this.mThread == null) {
            throw new IllegalStateException("Recording stopped and can't restart (single use)");
        } else {
            throw new IllegalStateException("Recording already started");
        }
    }

    public void end() {
        this.mAudioRecord.stop();
        if (this.mMic) {
            this.mAudioRecordMic.stop();
        }
        this.mAudioRecord.release();
        if (this.mMic) {
            this.mAudioRecordMic.release();
        }
        try {
            this.mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.mCodec.stop();
        this.mCodec.release();
        this.mMuxer.stop();
        this.mMuxer.release();
        this.mThread = null;
    }
}
