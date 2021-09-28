package com.google.android.systemui.columbus.sensors;

import android.content.Context;
import android.hardware.google.pixel.vendor.PixelAtoms$DoubleTapNanoappEventReported$Type;
import android.hardware.location.ContextHubClient;
import android.hardware.location.ContextHubInfo;
import android.hardware.location.ContextHubManager;
import android.hardware.location.NanoAppMessage;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.util.StatsEvent;
import android.util.StatsLog;
import androidx.constraintlayout.widget.R$styleable;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.util.RingBuffer;
import com.android.systemui.Dumpable;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.google.android.systemui.columbus.ColumbusEvent;
import com.google.android.systemui.columbus.proto.nano.ColumbusProto$GestureDetected;
import com.google.android.systemui.columbus.proto.nano.ColumbusProto$NanoappEvent;
import com.google.android.systemui.columbus.proto.nano.ColumbusProto$NanoappEvents;
import com.google.android.systemui.columbus.proto.nano.ColumbusProto$RecognizerStart;
import com.google.android.systemui.columbus.proto.nano.ColumbusProto$ScreenStateUpdate;
import com.google.android.systemui.columbus.proto.nano.ColumbusProto$SensitivityUpdate;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import com.google.android.systemui.columbus.sensors.config.GestureConfiguration;
import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import kotlin.Unit;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: CHREGestureSensor.kt */
/* loaded from: classes2.dex */
public final class CHREGestureSensor extends GestureSensor implements Dumpable {
    public static final Companion Companion = new Companion(null);
    private final Handler bgHandler;
    private final Context context;
    private ContextHubClient contextHubClient;
    private final GestureConfiguration gestureConfiguration;
    private boolean isAwake;
    private boolean isDozing;
    private boolean isListening;
    private boolean screenOn;
    private boolean screenStateUpdated;
    private final CHREGestureSensor$statusBarStateListener$1 statusBarStateListener;
    private final UiEventLogger uiEventLogger;
    private final CHREGestureSensor$wakefulnessLifecycleObserver$1 wakefulnessLifecycleObserver;
    private final FeatureVectorDumper featureVectorDumper = new FeatureVectorDumper();
    private final CHREGestureSensor$contextHubClientCallback$1 contextHubClientCallback = new CHREGestureSensor$contextHubClientCallback$1(this);

    private final int protoGestureTypeToGesture(int i) {
        if (i != 1) {
            return i != 2 ? 0 : 2;
        }
        return 1;
    }

    public CHREGestureSensor(Context context, UiEventLogger uiEventLogger, GestureConfiguration gestureConfiguration, StatusBarStateController statusBarStateController, WakefulnessLifecycle wakefulnessLifecycle, Handler handler) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(uiEventLogger, "uiEventLogger");
        Intrinsics.checkNotNullParameter(gestureConfiguration, "gestureConfiguration");
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(wakefulnessLifecycle, "wakefulnessLifecycle");
        Intrinsics.checkNotNullParameter(handler, "bgHandler");
        this.context = context;
        this.uiEventLogger = uiEventLogger;
        this.gestureConfiguration = gestureConfiguration;
        this.bgHandler = handler;
        CHREGestureSensor$statusBarStateListener$1 cHREGestureSensor$statusBarStateListener$1 = new StatusBarStateController.StateListener(this) { // from class: com.google.android.systemui.columbus.sensors.CHREGestureSensor$statusBarStateListener$1
            final /* synthetic */ CHREGestureSensor this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public void onDozingChanged(boolean z) {
                this.this$0.handleDozingChanged(z);
            }
        };
        this.statusBarStateListener = cHREGestureSensor$statusBarStateListener$1;
        CHREGestureSensor$wakefulnessLifecycleObserver$1 cHREGestureSensor$wakefulnessLifecycleObserver$1 = new WakefulnessLifecycle.Observer(this) { // from class: com.google.android.systemui.columbus.sensors.CHREGestureSensor$wakefulnessLifecycleObserver$1
            final /* synthetic */ CHREGestureSensor this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public void onStartedWakingUp() {
                this.this$0.handleWakefullnessChanged(false);
            }

            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public void onFinishedWakingUp() {
                this.this$0.handleWakefullnessChanged(true);
            }

            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public void onStartedGoingToSleep() {
                this.this$0.handleWakefullnessChanged(false);
            }

            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public void onFinishedGoingToSleep() {
                this.this$0.handleWakefullnessChanged(false);
            }
        };
        this.wakefulnessLifecycleObserver = cHREGestureSensor$wakefulnessLifecycleObserver$1;
        this.isDozing = statusBarStateController.isDozing();
        boolean z = false;
        boolean z2 = wakefulnessLifecycle.getWakefulness() == 2;
        this.isAwake = z2;
        if (z2 && !this.isDozing) {
            z = true;
        }
        this.screenOn = z;
        this.screenStateUpdated = true;
        gestureConfiguration.setListener(new GestureConfiguration.Listener(this) { // from class: com.google.android.systemui.columbus.sensors.CHREGestureSensor.1
            final /* synthetic */ CHREGestureSensor this$0;

            {
                this.this$0 = r1;
            }

            @Override // com.google.android.systemui.columbus.sensors.config.GestureConfiguration.Listener
            public void onGestureConfigurationChanged(GestureConfiguration gestureConfiguration2) {
                Intrinsics.checkNotNullParameter(gestureConfiguration2, "configuration");
                this.this$0.updateSensitivity(gestureConfiguration2);
            }
        });
        statusBarStateController.addCallback(cHREGestureSensor$statusBarStateListener$1);
        wakefulnessLifecycle.addObserver(cHREGestureSensor$wakefulnessLifecycleObserver$1);
        initializeContextHubClientIfNull();
    }

    @Override // com.google.android.systemui.columbus.sensors.Sensor
    public boolean isListening() {
        return this.isListening;
    }

    public void setListening(boolean z) {
        this.isListening = z;
    }

    @Override // com.google.android.systemui.columbus.sensors.Sensor
    public void startListening() {
        setListening(true);
        startRecognizer();
        sendScreenState();
    }

    @Override // com.google.android.systemui.columbus.sensors.Sensor
    public void stopListening() {
        sendMessageToNanoApp$default(this, R$styleable.Constraint_layout_goneMarginRight, new byte[0], new Function0<Unit>(this) { // from class: com.google.android.systemui.columbus.sensors.CHREGestureSensor$stopListening$1
            final /* synthetic */ CHREGestureSensor this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // kotlin.jvm.functions.Function0
            public final void invoke() {
                this.this$0.uiEventLogger.log(ColumbusEvent.COLUMBUS_MODE_INACTIVE);
            }
        }, null, 8, null);
        setListening(false);
    }

    /* access modifiers changed from: private */
    public final void startRecognizer() {
        ColumbusProto$RecognizerStart columbusProto$RecognizerStart = new MessageNano() { // from class: com.google.android.systemui.columbus.proto.nano.ColumbusProto$RecognizerStart
            public float sensitivity;

            {
                clear();
            }

            public ColumbusProto$RecognizerStart clear() {
                this.sensitivity = 0.0f;
                this.cachedSize = -1;
                return this;
            }

            @Override // com.google.protobuf.nano.MessageNano
            public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                if (Float.floatToIntBits(this.sensitivity) != Float.floatToIntBits(0.0f)) {
                    codedOutputByteBufferNano.writeFloat(1, this.sensitivity);
                }
                super.writeTo(codedOutputByteBufferNano);
            }

            /* access modifiers changed from: protected */
            @Override // com.google.protobuf.nano.MessageNano
            public int computeSerializedSize() {
                int computeSerializedSize = super.computeSerializedSize();
                return Float.floatToIntBits(this.sensitivity) != Float.floatToIntBits(0.0f) ? computeSerializedSize + CodedOutputByteBufferNano.computeFloatSize(1, this.sensitivity) : computeSerializedSize;
            }

            @Override // com.google.protobuf.nano.MessageNano
            public ColumbusProto$RecognizerStart mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                while (true) {
                    int readTag = codedInputByteBufferNano.readTag();
                    if (readTag == 0) {
                        return this;
                    }
                    if (readTag == 13) {
                        this.sensitivity = codedInputByteBufferNano.readFloat();
                    } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                        return this;
                    }
                }
            }
        };
        columbusProto$RecognizerStart.sensitivity = this.gestureConfiguration.getSensitivity();
        byte[] byteArray = MessageNano.toByteArray(columbusProto$RecognizerStart);
        Intrinsics.checkNotNullExpressionValue(byteArray, "toByteArray(recognizerStart)");
        sendMessageToNanoApp$default(this, 100, byteArray, new Function0<Unit>(this) { // from class: com.google.android.systemui.columbus.sensors.CHREGestureSensor$startRecognizer$1
            final /* synthetic */ CHREGestureSensor this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // kotlin.jvm.functions.Function0
            public final void invoke() {
                this.this$0.uiEventLogger.log(ColumbusEvent.COLUMBUS_MODE_LOW_POWER_ACTIVE);
            }
        }, null, 8, null);
    }

    /* JADX DEBUG: Multi-variable search result rejected for r1v0, resolved type: com.google.android.systemui.columbus.sensors.CHREGestureSensor */
    /* JADX WARN: Multi-variable type inference failed */
    static /* synthetic */ void sendMessageToNanoApp$default(CHREGestureSensor cHREGestureSensor, int i, byte[] bArr, Function0 function0, Function0 function02, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            function0 = null;
        }
        if ((i2 & 8) != 0) {
            function02 = null;
        }
        cHREGestureSensor.sendMessageToNanoApp(i, bArr, function0, function02);
    }

    private final void sendMessageToNanoApp(int i, byte[] bArr, Function0<Unit> function0, Function0<Unit> function02) {
        initializeContextHubClientIfNull();
        if (this.contextHubClient == null) {
            Log.e("Columbus/GestureSensor", "ContextHubClient null");
        } else {
            this.bgHandler.post(new Runnable(i, bArr, this, function02, function0) { // from class: com.google.android.systemui.columbus.sensors.CHREGestureSensor$sendMessageToNanoApp$1
                final /* synthetic */ byte[] $bytes;
                final /* synthetic */ int $messageType;
                final /* synthetic */ Function0<Unit> $onFail;
                final /* synthetic */ Function0<Unit> $onSuccess;
                final /* synthetic */ CHREGestureSensor this$0;

                /* access modifiers changed from: package-private */
                {
                    this.$messageType = r1;
                    this.$bytes = r2;
                    this.this$0 = r3;
                    this.$onFail = r4;
                    this.$onSuccess = r5;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    NanoAppMessage createMessageToNanoApp = NanoAppMessage.createMessageToNanoApp(5147455389092024345L, this.$messageType, this.$bytes);
                    ContextHubClient contextHubClient = this.this$0.contextHubClient;
                    Integer valueOf = contextHubClient == null ? null : Integer.valueOf(contextHubClient.sendMessageToNanoApp(createMessageToNanoApp));
                    if (valueOf != null && valueOf.intValue() == 0) {
                        Function0<Unit> function03 = this.$onSuccess;
                        if (function03 != null) {
                            function03.invoke();
                            return;
                        }
                        return;
                    }
                    Log.e("Columbus/GestureSensor", "Unable to send message " + this.$messageType + " to nanoapp, error code " + valueOf);
                    Function0<Unit> function04 = this.$onFail;
                    if (function04 != null) {
                        function04.invoke();
                    }
                }
            });
        }
    }

    private final void initializeContextHubClientIfNull() {
        List list;
        int i;
        if (this.contextHubClient == null) {
            ContextHubManager contextHubManager = (ContextHubManager) this.context.getSystemService("contexthub");
            if (contextHubManager == null) {
                list = null;
            } else {
                list = contextHubManager.getContextHubs();
            }
            if (list == null) {
                i = 0;
            } else {
                i = list.size();
            }
            if (i == 0) {
                Log.e("Columbus/GestureSensor", "No context hubs found");
            } else if (list != null) {
                this.contextHubClient = contextHubManager.createClient((ContextHubInfo) list.get(0), this.contextHubClientCallback);
            }
        }
    }

    /* access modifiers changed from: private */
    public final void updateSensitivity(GestureConfiguration gestureConfiguration) {
        ColumbusProto$SensitivityUpdate columbusProto$SensitivityUpdate = new MessageNano() { // from class: com.google.android.systemui.columbus.proto.nano.ColumbusProto$SensitivityUpdate
            public float sensitivity;

            {
                clear();
            }

            public ColumbusProto$SensitivityUpdate clear() {
                this.sensitivity = 0.0f;
                this.cachedSize = -1;
                return this;
            }

            @Override // com.google.protobuf.nano.MessageNano
            public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                if (Float.floatToIntBits(this.sensitivity) != Float.floatToIntBits(0.0f)) {
                    codedOutputByteBufferNano.writeFloat(1, this.sensitivity);
                }
                super.writeTo(codedOutputByteBufferNano);
            }

            /* access modifiers changed from: protected */
            @Override // com.google.protobuf.nano.MessageNano
            public int computeSerializedSize() {
                int computeSerializedSize = super.computeSerializedSize();
                return Float.floatToIntBits(this.sensitivity) != Float.floatToIntBits(0.0f) ? computeSerializedSize + CodedOutputByteBufferNano.computeFloatSize(1, this.sensitivity) : computeSerializedSize;
            }

            @Override // com.google.protobuf.nano.MessageNano
            public ColumbusProto$SensitivityUpdate mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                while (true) {
                    int readTag = codedInputByteBufferNano.readTag();
                    if (readTag == 0) {
                        return this;
                    }
                    if (readTag == 13) {
                        this.sensitivity = codedInputByteBufferNano.readFloat();
                    } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                        return this;
                    }
                }
            }
        };
        columbusProto$SensitivityUpdate.sensitivity = gestureConfiguration.getSensitivity();
        byte[] byteArray = MessageNano.toByteArray(columbusProto$SensitivityUpdate);
        Intrinsics.checkNotNullExpressionValue(byteArray, "toByteArray(sensitivityUpdate)");
        sendMessageToNanoApp$default(this, 200, byteArray, null, null, 12, null);
    }

    /* access modifiers changed from: private */
    public final void handleGestureDetection(ColumbusProto$GestureDetected columbusProto$GestureDetected) {
        int i = columbusProto$GestureDetected.gestureType;
        reportGestureDetected(protoGestureTypeToGesture(i), new GestureSensor.DetectionProperties(i == 2));
        this.featureVectorDumper.onGestureDetected(columbusProto$GestureDetected);
    }

    /* access modifiers changed from: private */
    public final void handleNanoappEvents(ColumbusProto$NanoappEvents columbusProto$NanoappEvents) {
        ColumbusProto$NanoappEvent[] columbusProto$NanoappEventArr = columbusProto$NanoappEvents.batchedEvents;
        Intrinsics.checkNotNullExpressionValue(columbusProto$NanoappEventArr, "nanoappEvents.batchedEvents");
        for (ColumbusProto$NanoappEvent columbusProto$NanoappEvent : columbusProto$NanoappEventArr) {
            StatsLog.write(StatsEvent.newBuilder().setAtomId(100051).writeLong(columbusProto$NanoappEvent.timestamp).writeInt(toWestWorldEventType(columbusProto$NanoappEvent.type)).build());
        }
    }

    private final int toWestWorldEventType(int i) {
        switch (i) {
            case 1:
                return PixelAtoms$DoubleTapNanoappEventReported$Type.GATE_START.getNumber();
            case 2:
                return PixelAtoms$DoubleTapNanoappEventReported$Type.GATE_STOP.getNumber();
            case 3:
                return PixelAtoms$DoubleTapNanoappEventReported$Type.HIGH_IMU_ODR_START.getNumber();
            case 4:
                return PixelAtoms$DoubleTapNanoappEventReported$Type.HIGH_IMU_ODR_STOP.getNumber();
            case 5:
                return PixelAtoms$DoubleTapNanoappEventReported$Type.ML_PREDICTION_START.getNumber();
            case 6:
                return PixelAtoms$DoubleTapNanoappEventReported$Type.ML_PREDICTION_STOP.getNumber();
            case 7:
                return PixelAtoms$DoubleTapNanoappEventReported$Type.SINGLE_TAP.getNumber();
            case 8:
                return PixelAtoms$DoubleTapNanoappEventReported$Type.DOUBLE_TAP.getNumber();
            default:
                return PixelAtoms$DoubleTapNanoappEventReported$Type.UNKNOWN.getNumber();
        }
    }

    /* access modifiers changed from: private */
    public final void handleDozingChanged(boolean z) {
        if (this.isDozing != z) {
            this.isDozing = z;
            updateScreenState();
        }
    }

    /* access modifiers changed from: private */
    public final void handleWakefullnessChanged(boolean z) {
        if (this.isAwake != z) {
            this.isAwake = z;
            updateScreenState();
        }
    }

    /* access modifiers changed from: private */
    public final void updateScreenState() {
        boolean z = this.isAwake && !this.isDozing;
        if (this.screenOn != z || !this.screenStateUpdated) {
            this.screenOn = z;
            if (isListening()) {
                sendScreenState();
            }
        }
    }

    private final void sendScreenState() {
        ColumbusProto$ScreenStateUpdate columbusProto$ScreenStateUpdate = new MessageNano() { // from class: com.google.android.systemui.columbus.proto.nano.ColumbusProto$ScreenStateUpdate
            public int screenState;

            {
                clear();
            }

            public ColumbusProto$ScreenStateUpdate clear() {
                this.screenState = 0;
                this.cachedSize = -1;
                return this;
            }

            @Override // com.google.protobuf.nano.MessageNano
            public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                int i = this.screenState;
                if (i != 0) {
                    codedOutputByteBufferNano.writeInt32(1, i);
                }
                super.writeTo(codedOutputByteBufferNano);
            }

            /* access modifiers changed from: protected */
            @Override // com.google.protobuf.nano.MessageNano
            public int computeSerializedSize() {
                int computeSerializedSize = super.computeSerializedSize();
                int i = this.screenState;
                return i != 0 ? computeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(1, i) : computeSerializedSize;
            }

            @Override // com.google.protobuf.nano.MessageNano
            public ColumbusProto$ScreenStateUpdate mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                while (true) {
                    int readTag = codedInputByteBufferNano.readTag();
                    if (readTag == 0) {
                        return this;
                    }
                    if (readTag == 8) {
                        int readInt32 = codedInputByteBufferNano.readInt32();
                        if (readInt32 == 0 || readInt32 == 1 || readInt32 == 2) {
                            this.screenState = readInt32;
                        }
                    } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                        return this;
                    }
                }
            }
        };
        columbusProto$ScreenStateUpdate.screenState = this.screenOn ? 1 : 2;
        byte[] byteArray = MessageNano.toByteArray(columbusProto$ScreenStateUpdate);
        Intrinsics.checkNotNullExpressionValue(byteArray, "toByteArray(screenStateUpdate)");
        sendMessageToNanoApp(400, byteArray, new Function0<Unit>(this) { // from class: com.google.android.systemui.columbus.sensors.CHREGestureSensor$sendScreenState$1
            final /* synthetic */ CHREGestureSensor this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // kotlin.jvm.functions.Function0
            public final void invoke() {
                this.this$0.screenStateUpdated = true;
            }
        }, new Function0<Unit>(this) { // from class: com.google.android.systemui.columbus.sensors.CHREGestureSensor$sendScreenState$2
            final /* synthetic */ CHREGestureSensor this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // kotlin.jvm.functions.Function0
            public final void invoke() {
                this.this$0.screenStateUpdated = false;
            }
        });
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(fileDescriptor, "fd");
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        this.featureVectorDumper.dump(fileDescriptor, printWriter, strArr);
    }

    /* compiled from: CHREGestureSensor.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* compiled from: CHREGestureSensor.kt */
    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class FeatureVector implements Dumpable {
        private final int gesture;
        private final long timestamp = SystemClock.elapsedRealtime();
        private final float[] vector;

        public FeatureVector(ColumbusProto$GestureDetected columbusProto$GestureDetected) {
            Intrinsics.checkNotNullParameter(columbusProto$GestureDetected, "gestureDetected");
            this.vector = columbusProto$GestureDetected.featureVector;
            this.gesture = columbusProto$GestureDetected.gestureType;
        }

        @Override // com.android.systemui.Dumpable
        public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            Intrinsics.checkNotNullParameter(fileDescriptor, "fd");
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            Intrinsics.checkNotNullParameter(strArr, "args");
            printWriter.println("      Gesture: " + this.gesture + " Time: " + (this.timestamp - SystemClock.elapsedRealtime()));
            float[] fArr = this.vector;
            Intrinsics.checkNotNullExpressionValue(fArr, "vector");
            printWriter.println(Intrinsics.stringPlus("      ", ArraysKt___ArraysKt.joinToString$default(fArr, ", ", "[ ", " ]", 0, null, null, 56, null)));
        }
    }

    /* compiled from: CHREGestureSensor.kt */
    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class FeatureVectorDumper implements Dumpable {
        private final RingBuffer<FeatureVector> featureVectors = new RingBuffer<>(FeatureVector.class, 10);
        private FeatureVector lastSingleTapFeatureVector;
        private FeatureVector secondToLastSingleTapFeatureVector;

        public final void onGestureDetected(ColumbusProto$GestureDetected columbusProto$GestureDetected) {
            Intrinsics.checkNotNullParameter(columbusProto$GestureDetected, "gestureDetected");
            int i = columbusProto$GestureDetected.gestureType;
            if (i == 1) {
                FeatureVector featureVector = this.secondToLastSingleTapFeatureVector;
                FeatureVector featureVector2 = this.lastSingleTapFeatureVector;
                this.secondToLastSingleTapFeatureVector = null;
                this.lastSingleTapFeatureVector = null;
                if (featureVector2 == null || featureVector == null) {
                    Log.w("Columbus/GestureSensor", "Received double tap without single taps, event will not appear in sysdump");
                    return;
                }
                this.featureVectors.append(featureVector);
                this.featureVectors.append(featureVector2);
            } else if (i == 2) {
                this.secondToLastSingleTapFeatureVector = this.lastSingleTapFeatureVector;
                this.lastSingleTapFeatureVector = new FeatureVector(columbusProto$GestureDetected);
            }
        }

        @Override // com.android.systemui.Dumpable
        public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            Intrinsics.checkNotNullParameter(fileDescriptor, "fd");
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            Intrinsics.checkNotNullParameter(strArr, "args");
            printWriter.println("    Feature Vectors:");
            Object[] array = this.featureVectors.toArray();
            Intrinsics.checkNotNullExpressionValue(array, "featureVectors.toArray()");
            for (Object obj : array) {
                ((FeatureVector) obj).dump(fileDescriptor, printWriter, strArr);
            }
        }
    }
}
