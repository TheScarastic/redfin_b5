package com.android.systemui.media;

import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.SeekBar;
import androidx.core.view.GestureDetectorCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.android.systemui.media.SeekBarViewModel;
import com.android.systemui.util.concurrency.RepeatableExecutor;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SeekBarViewModel.kt */
/* loaded from: classes.dex */
public final class SeekBarViewModel {
    private final MutableLiveData<Progress> _progress;
    private final RepeatableExecutor bgExecutor;
    private Runnable cancel;
    private MediaController controller;
    private boolean isFalseSeek;
    public Function0<Unit> logSmartspaceClick;
    private PlaybackState playbackState;
    private boolean scrubbing;
    private Progress _data = new Progress(false, false, null, 0);
    private SeekBarViewModel$callback$1 callback = new SeekBarViewModel$callback$1(this);
    private boolean listening = true;

    public SeekBarViewModel(RepeatableExecutor repeatableExecutor) {
        Intrinsics.checkNotNullParameter(repeatableExecutor, "bgExecutor");
        this.bgExecutor = repeatableExecutor;
        MutableLiveData<Progress> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.postValue(this._data);
        Unit unit = Unit.INSTANCE;
        this._progress = mutableLiveData;
    }

    public final void set_data(Progress progress) {
        this._data = progress;
        this._progress.postValue(progress);
    }

    public final LiveData<Progress> getProgress() {
        return this._progress;
    }

    public final void setController(MediaController mediaController) {
        MediaController mediaController2 = this.controller;
        MediaSession.Token token = null;
        MediaSession.Token sessionToken = mediaController2 == null ? null : mediaController2.getSessionToken();
        if (mediaController != null) {
            token = mediaController.getSessionToken();
        }
        if (!Intrinsics.areEqual(sessionToken, token)) {
            MediaController mediaController3 = this.controller;
            if (mediaController3 != null) {
                mediaController3.unregisterCallback(this.callback);
            }
            if (mediaController != null) {
                mediaController.registerCallback(this.callback);
            }
            this.controller = mediaController;
        }
    }

    public final void setListening(boolean z) {
        this.bgExecutor.execute(new Runnable(this, z) { // from class: com.android.systemui.media.SeekBarViewModel$listening$1
            final /* synthetic */ boolean $value;
            final /* synthetic */ SeekBarViewModel this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$value = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                boolean z2 = this.this$0.listening;
                boolean z3 = this.$value;
                if (z2 != z3) {
                    this.this$0.listening = z3;
                    this.this$0.checkIfPollingNeeded();
                }
            }
        });
    }

    public final void setScrubbing(boolean z) {
        if (this.scrubbing != z) {
            this.scrubbing = z;
            checkIfPollingNeeded();
        }
    }

    public final Function0<Unit> getLogSmartspaceClick() {
        Function0<Unit> function0 = this.logSmartspaceClick;
        if (function0 != null) {
            return function0;
        }
        Intrinsics.throwUninitializedPropertyAccessException("logSmartspaceClick");
        throw null;
    }

    public final void setLogSmartspaceClick(Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(function0, "<set-?>");
        this.logSmartspaceClick = function0;
    }

    public final void onSeekStarting() {
        this.bgExecutor.execute(new Runnable(this) { // from class: com.android.systemui.media.SeekBarViewModel$onSeekStarting$1
            final /* synthetic */ SeekBarViewModel this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.this$0.setScrubbing(true);
                this.this$0.isFalseSeek = false;
            }
        });
    }

    public final void onSeekProgress(long j) {
        this.bgExecutor.execute(new Runnable(this, j) { // from class: com.android.systemui.media.SeekBarViewModel$onSeekProgress$1
            final /* synthetic */ long $position;
            final /* synthetic */ SeekBarViewModel this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$position = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                if (this.this$0.scrubbing) {
                    SeekBarViewModel seekBarViewModel = this.this$0;
                    seekBarViewModel.set_data(SeekBarViewModel.Progress.copy$default(seekBarViewModel._data, false, false, Integer.valueOf((int) this.$position), 0, 11, null));
                }
            }
        });
    }

    public final void onSeekFalse() {
        this.bgExecutor.execute(new Runnable(this) { // from class: com.android.systemui.media.SeekBarViewModel$onSeekFalse$1
            final /* synthetic */ SeekBarViewModel this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // java.lang.Runnable
            public final void run() {
                if (this.this$0.scrubbing) {
                    this.this$0.isFalseSeek = true;
                }
            }
        });
    }

    public final void onSeek(long j) {
        this.bgExecutor.execute(new Runnable(this, j) { // from class: com.android.systemui.media.SeekBarViewModel$onSeek$1
            final /* synthetic */ long $position;
            final /* synthetic */ SeekBarViewModel this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$position = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                MediaController.TransportControls transportControls;
                if (this.this$0.isFalseSeek) {
                    this.this$0.setScrubbing(false);
                    this.this$0.checkPlaybackPosition();
                    return;
                }
                this.this$0.getLogSmartspaceClick().invoke();
                MediaController mediaController = this.this$0.controller;
                if (!(mediaController == null || (transportControls = mediaController.getTransportControls()) == null)) {
                    transportControls.seekTo(this.$position);
                }
                this.this$0.playbackState = null;
                this.this$0.setScrubbing(false);
            }
        });
    }

    /* JADX WARNING: Code restructure failed: missing block: B:34:0x006e, code lost:
        if (r0.intValue() != 0) goto L_0x0070;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0070, code lost:
        if (r9 > 0) goto L_0x0073;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void updateController(android.media.session.MediaController r9) {
        /*
            r8 = this;
            r8.setController(r9)
            android.media.session.MediaController r9 = r8.controller
            r0 = 0
            if (r9 != 0) goto L_0x000a
            r9 = r0
            goto L_0x000e
        L_0x000a:
            android.media.session.PlaybackState r9 = r9.getPlaybackState()
        L_0x000e:
            r8.playbackState = r9
            android.media.session.MediaController r9 = r8.controller
            if (r9 != 0) goto L_0x0016
            r9 = r0
            goto L_0x001a
        L_0x0016:
            android.media.MediaMetadata r9 = r9.getMetadata()
        L_0x001a:
            android.media.session.PlaybackState r1 = r8.playbackState
            r2 = 0
            if (r1 != 0) goto L_0x0022
            r4 = r2
            goto L_0x0026
        L_0x0022:
            long r4 = r1.getActions()
        L_0x0026:
            r6 = 256(0x100, double:1.265E-321)
            long r4 = r4 & r6
            int r1 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            r2 = 1
            r3 = 0
            if (r1 == 0) goto L_0x0031
            r1 = r2
            goto L_0x0032
        L_0x0031:
            r1 = r3
        L_0x0032:
            android.media.session.PlaybackState r4 = r8.playbackState
            if (r4 != 0) goto L_0x0038
            r4 = r0
            goto L_0x0041
        L_0x0038:
            long r4 = r4.getPosition()
            int r4 = (int) r4
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
        L_0x0041:
            if (r9 != 0) goto L_0x0045
            r9 = r0
            goto L_0x004f
        L_0x0045:
            java.lang.String r5 = "android.media.metadata.DURATION"
            long r5 = r9.getLong(r5)
            java.lang.Long r9 = java.lang.Long.valueOf(r5)
        L_0x004f:
            if (r9 != 0) goto L_0x0053
            r9 = r3
            goto L_0x0058
        L_0x0053:
            long r5 = r9.longValue()
            int r9 = (int) r5
        L_0x0058:
            android.media.session.PlaybackState r5 = r8.playbackState
            if (r5 == 0) goto L_0x0072
            if (r5 != 0) goto L_0x005f
            goto L_0x0067
        L_0x005f:
            int r0 = r5.getState()
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
        L_0x0067:
            if (r0 != 0) goto L_0x006a
            goto L_0x0070
        L_0x006a:
            int r0 = r0.intValue()
            if (r0 == 0) goto L_0x0072
        L_0x0070:
            if (r9 > 0) goto L_0x0073
        L_0x0072:
            r2 = r3
        L_0x0073:
            com.android.systemui.media.SeekBarViewModel$Progress r0 = new com.android.systemui.media.SeekBarViewModel$Progress
            r0.<init>(r2, r1, r4, r9)
            r8.set_data(r0)
            r8.checkIfPollingNeeded()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.SeekBarViewModel.updateController(android.media.session.MediaController):void");
    }

    public final void clearController() {
        this.bgExecutor.execute(new Runnable(this) { // from class: com.android.systemui.media.SeekBarViewModel$clearController$1
            final /* synthetic */ SeekBarViewModel this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // java.lang.Runnable
            public final void run() {
                SeekBarViewModel.access$setController(this.this$0, null);
                SeekBarViewModel.access$setPlaybackState$p(this.this$0, null);
                Runnable access$getCancel$p = SeekBarViewModel.access$getCancel$p(this.this$0);
                if (access$getCancel$p != null) {
                    access$getCancel$p.run();
                }
                SeekBarViewModel.access$setCancel$p(this.this$0, null);
                SeekBarViewModel seekBarViewModel = this.this$0;
                SeekBarViewModel.access$set_data(seekBarViewModel, SeekBarViewModel.Progress.copy$default(SeekBarViewModel.access$get_data$p(seekBarViewModel), false, false, null, 0, 14, null));
            }
        });
    }

    public final void onDestroy() {
        this.bgExecutor.execute(new Runnable(this) { // from class: com.android.systemui.media.SeekBarViewModel$onDestroy$1
            final /* synthetic */ SeekBarViewModel this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.this$0.setController(null);
                this.this$0.playbackState = null;
                Runnable runnable = this.this$0.cancel;
                if (runnable != null) {
                    runnable.run();
                }
                this.this$0.cancel = null;
            }
        });
    }

    public final void checkPlaybackPosition() {
        int duration = this._data.getDuration();
        PlaybackState playbackState = this.playbackState;
        Integer valueOf = playbackState == null ? null : Integer.valueOf((int) SeekBarViewModelKt.computePosition(playbackState, (long) duration));
        if (valueOf != null && !Intrinsics.areEqual(this._data.getElapsedTime(), valueOf)) {
            set_data(Progress.copy$default(this._data, false, false, valueOf, 0, 11, null));
        }
    }

    public final void checkIfPollingNeeded() {
        boolean z = false;
        if (this.listening && !this.scrubbing) {
            PlaybackState playbackState = this.playbackState;
            if (playbackState == null ? false : SeekBarViewModelKt.isInMotion(playbackState)) {
                z = true;
            }
        }
        if (!z) {
            Runnable runnable = this.cancel;
            if (runnable != null) {
                runnable.run();
            }
            this.cancel = null;
        } else if (this.cancel == null) {
            this.cancel = this.bgExecutor.executeRepeatedly(new Runnable() { // from class: com.android.systemui.media.SeekBarViewModel$checkIfPollingNeeded$1
                @Override // java.lang.Runnable
                public final void run() {
                    SeekBarViewModel.access$checkPlaybackPosition(SeekBarViewModel.this);
                }
            }, 0, 100);
        }
    }

    public final SeekBar.OnSeekBarChangeListener getSeekBarListener() {
        return new SeekBarChangeListener(this);
    }

    public final void attachTouchHandlers(SeekBar seekBar) {
        Intrinsics.checkNotNullParameter(seekBar, "bar");
        seekBar.setOnSeekBarChangeListener(getSeekBarListener());
        seekBar.setOnTouchListener(new SeekBarTouchListener(this, seekBar));
    }

    /* compiled from: SeekBarViewModel.kt */
    /* loaded from: classes.dex */
    public static final class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        private final SeekBarViewModel viewModel;

        public SeekBarChangeListener(SeekBarViewModel seekBarViewModel) {
            Intrinsics.checkNotNullParameter(seekBarViewModel, "viewModel");
            this.viewModel = seekBarViewModel;
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            Intrinsics.checkNotNullParameter(seekBar, "bar");
            if (z) {
                this.viewModel.onSeekProgress((long) i);
            }
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStartTrackingTouch(SeekBar seekBar) {
            Intrinsics.checkNotNullParameter(seekBar, "bar");
            this.viewModel.onSeekStarting();
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStopTrackingTouch(SeekBar seekBar) {
            Intrinsics.checkNotNullParameter(seekBar, "bar");
            this.viewModel.onSeek((long) seekBar.getProgress());
        }
    }

    /* compiled from: SeekBarViewModel.kt */
    /* loaded from: classes.dex */
    public static final class SeekBarTouchListener implements View.OnTouchListener, GestureDetector.OnGestureListener {
        private final SeekBar bar;
        private final GestureDetectorCompat detector;
        private final int flingVelocity;
        private boolean shouldGoToSeekBar;
        private final SeekBarViewModel viewModel;

        @Override // android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
            Intrinsics.checkNotNullParameter(motionEvent, "event");
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public void onShowPress(MotionEvent motionEvent) {
            Intrinsics.checkNotNullParameter(motionEvent, "event");
        }

        public SeekBarTouchListener(SeekBarViewModel seekBarViewModel, SeekBar seekBar) {
            Intrinsics.checkNotNullParameter(seekBarViewModel, "viewModel");
            Intrinsics.checkNotNullParameter(seekBar, "bar");
            this.viewModel = seekBarViewModel;
            this.bar = seekBar;
            this.detector = new GestureDetectorCompat(seekBar.getContext(), this);
            this.flingVelocity = ViewConfiguration.get(seekBar.getContext()).getScaledMinimumFlingVelocity() * 10;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Intrinsics.checkNotNullParameter(view, "view");
            Intrinsics.checkNotNullParameter(motionEvent, "event");
            if (!Intrinsics.areEqual(view, this.bar)) {
                return false;
            }
            this.detector.onTouchEvent(motionEvent);
            return !this.shouldGoToSeekBar;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent motionEvent) {
            double d;
            double d2;
            ViewParent parent;
            Intrinsics.checkNotNullParameter(motionEvent, "event");
            int paddingLeft = this.bar.getPaddingLeft();
            int paddingRight = this.bar.getPaddingRight();
            int progress = this.bar.getProgress();
            int max = this.bar.getMax() - this.bar.getMin();
            double min = max > 0 ? ((double) (progress - this.bar.getMin())) / ((double) max) : 0.0d;
            int width = (this.bar.getWidth() - paddingLeft) - paddingRight;
            if (this.bar.isLayoutRtl()) {
                d2 = (double) paddingLeft;
                d = ((double) width) * (((double) 1) - min);
            } else {
                d2 = (double) paddingLeft;
                d = ((double) width) * min;
            }
            double d3 = d2 + d;
            long height = (long) (this.bar.getHeight() / 2);
            int round = (int) (Math.round(d3) - height);
            int round2 = (int) (Math.round(d3) + height);
            int round3 = Math.round(motionEvent.getX());
            boolean z = round3 >= round && round3 <= round2;
            this.shouldGoToSeekBar = z;
            if (z && (parent = this.bar.getParent()) != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
            return this.shouldGoToSeekBar;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            Intrinsics.checkNotNullParameter(motionEvent, "event");
            this.shouldGoToSeekBar = true;
            return true;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            Intrinsics.checkNotNullParameter(motionEvent, "eventStart");
            Intrinsics.checkNotNullParameter(motionEvent2, "event");
            return this.shouldGoToSeekBar;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            Intrinsics.checkNotNullParameter(motionEvent, "eventStart");
            Intrinsics.checkNotNullParameter(motionEvent2, "event");
            if (Math.abs(f) > ((float) this.flingVelocity) || Math.abs(f2) > ((float) this.flingVelocity)) {
                this.viewModel.onSeekFalse();
            }
            return this.shouldGoToSeekBar;
        }
    }

    /* compiled from: SeekBarViewModel.kt */
    /* loaded from: classes.dex */
    public static final class Progress {
        private final int duration;
        private final Integer elapsedTime;
        private final boolean enabled;
        private final boolean seekAvailable;

        public static /* synthetic */ Progress copy$default(Progress progress, boolean z, boolean z2, Integer num, int i, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                z = progress.enabled;
            }
            if ((i2 & 2) != 0) {
                z2 = progress.seekAvailable;
            }
            if ((i2 & 4) != 0) {
                num = progress.elapsedTime;
            }
            if ((i2 & 8) != 0) {
                i = progress.duration;
            }
            return progress.copy(z, z2, num, i);
        }

        public final Progress copy(boolean z, boolean z2, Integer num, int i) {
            return new Progress(z, z2, num, i);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Progress)) {
                return false;
            }
            Progress progress = (Progress) obj;
            return this.enabled == progress.enabled && this.seekAvailable == progress.seekAvailable && Intrinsics.areEqual(this.elapsedTime, progress.elapsedTime) && this.duration == progress.duration;
        }

        public int hashCode() {
            boolean z = this.enabled;
            int i = 1;
            if (z) {
                z = true;
            }
            int i2 = z ? 1 : 0;
            int i3 = z ? 1 : 0;
            int i4 = z ? 1 : 0;
            int i5 = i2 * 31;
            boolean z2 = this.seekAvailable;
            if (!z2) {
                i = z2 ? 1 : 0;
            }
            int i6 = (i5 + i) * 31;
            Integer num = this.elapsedTime;
            return ((i6 + (num == null ? 0 : num.hashCode())) * 31) + Integer.hashCode(this.duration);
        }

        public String toString() {
            return "Progress(enabled=" + this.enabled + ", seekAvailable=" + this.seekAvailable + ", elapsedTime=" + this.elapsedTime + ", duration=" + this.duration + ')';
        }

        public Progress(boolean z, boolean z2, Integer num, int i) {
            this.enabled = z;
            this.seekAvailable = z2;
            this.elapsedTime = num;
            this.duration = i;
        }

        public final boolean getEnabled() {
            return this.enabled;
        }

        public final boolean getSeekAvailable() {
            return this.seekAvailable;
        }

        public final Integer getElapsedTime() {
            return this.elapsedTime;
        }

        public final int getDuration() {
            return this.duration;
        }
    }
}
