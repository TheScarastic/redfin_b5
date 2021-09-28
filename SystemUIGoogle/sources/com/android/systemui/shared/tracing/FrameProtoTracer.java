package com.android.systemui.shared.tracing;

import android.os.Trace;
import android.util.Log;
import android.view.Choreographer;
import com.android.internal.util.TraceBuffer;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class FrameProtoTracer<P, S extends P, T extends P, R> implements Choreographer.FrameCallback {
    private final TraceBuffer<P, S, T> mBuffer;
    private Choreographer mChoreographer;
    private volatile boolean mEnabled;
    private boolean mFrameScheduled;
    private final ProtoTraceParams<P, S, T, R> mParams;
    private final TraceBuffer.ProtoProvider<P, S, T> mProvider;
    private final File mTraceFile;
    private final Object mLock = new Object();
    private final Queue<T> mPool = new LinkedList();
    private final ArrayList<ProtoTraceable<R>> mTraceables = new ArrayList<>();
    private final ArrayList<ProtoTraceable<R>> mTmpTraceables = new ArrayList<>();

    /* loaded from: classes.dex */
    public interface ProtoTraceParams<P, S, T, R> {
        S getEncapsulatingTraceProto();

        byte[] getProtoBytes(P p);

        int getProtoSize(P p);

        File getTraceFile();

        byte[] serializeEncapsulatingProto(S s, Queue<T> queue);

        T updateBufferProto(T t, ArrayList<ProtoTraceable<R>> arrayList);
    }

    public FrameProtoTracer(ProtoTraceParams<P, S, T, R> protoTraceParams) {
        AnonymousClass1 r0 = new TraceBuffer.ProtoProvider<?, ?, ?>() { // from class: com.android.systemui.shared.tracing.FrameProtoTracer.1
            public int getItemSize(Object obj) {
                return FrameProtoTracer.this.mParams.getProtoSize(obj);
            }

            public byte[] getBytes(Object obj) {
                return FrameProtoTracer.this.mParams.getProtoBytes(obj);
            }

            public void write(Object obj, Queue<?> queue, OutputStream outputStream) throws IOException {
                outputStream.write(FrameProtoTracer.this.mParams.serializeEncapsulatingProto(obj, queue));
            }
        };
        this.mProvider = r0;
        this.mParams = protoTraceParams;
        this.mBuffer = new TraceBuffer<>(1048576, r0, new Consumer<T>() { // from class: com.android.systemui.shared.tracing.FrameProtoTracer.2
            @Override // java.util.function.Consumer
            public void accept(T t) {
                FrameProtoTracer.this.onProtoDequeued(t);
            }
        });
        this.mTraceFile = protoTraceParams.getTraceFile();
    }

    public void start() {
        synchronized (this.mLock) {
            if (!this.mEnabled) {
                this.mBuffer.resetBuffer();
                this.mEnabled = true;
                logState();
            }
        }
    }

    public void stop() {
        synchronized (this.mLock) {
            if (this.mEnabled) {
                this.mEnabled = false;
                writeToFile();
            }
        }
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public void add(ProtoTraceable<R> protoTraceable) {
        synchronized (this.mLock) {
            this.mTraceables.add(protoTraceable);
        }
    }

    public void remove(ProtoTraceable<R> protoTraceable) {
        synchronized (this.mLock) {
            this.mTraceables.remove(protoTraceable);
        }
    }

    public void scheduleFrameUpdate() {
        if (this.mEnabled && !this.mFrameScheduled) {
            if (this.mChoreographer == null) {
                this.mChoreographer = Choreographer.getMainThreadInstance();
            }
            this.mChoreographer.postFrameCallback(this);
            this.mFrameScheduled = true;
        }
    }

    public float getBufferUsagePct() {
        return ((float) this.mBuffer.getBufferSize()) / 1048576.0f;
    }

    @Override // android.view.Choreographer.FrameCallback
    public void doFrame(long j) {
        logState();
    }

    /* access modifiers changed from: private */
    public void onProtoDequeued(T t) {
        this.mPool.add(t);
    }

    private void logState() {
        synchronized (this.mLock) {
            this.mTmpTraceables.addAll(this.mTraceables);
        }
        this.mBuffer.add(this.mParams.updateBufferProto(this.mPool.poll(), this.mTmpTraceables));
        this.mTmpTraceables.clear();
        this.mFrameScheduled = false;
    }

    private void writeToFile() {
        try {
            try {
                Trace.beginSection("ProtoTracer.writeToFile");
                this.mBuffer.writeTraceToFile(this.mTraceFile, this.mParams.getEncapsulatingTraceProto());
            } catch (IOException e) {
                Log.e("FrameProtoTracer", "Unable to write buffer to file", e);
            }
        } finally {
            Trace.endSection();
        }
    }
}
