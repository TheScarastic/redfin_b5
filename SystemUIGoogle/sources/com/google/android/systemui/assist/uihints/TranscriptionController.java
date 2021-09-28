package com.google.android.systemui.assist.uihints;

import android.app.PendingIntent;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.R$id;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import com.google.android.systemui.assist.uihints.input.TouchActionRegion;
import com.google.android.systemui.assist.uihints.input.TouchInsideRegion;
import com.google.common.base.Function;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
/* loaded from: classes2.dex */
public class TranscriptionController implements NgaMessageHandler.CardInfoListener, NgaMessageHandler.TranscriptionInfoListener, NgaMessageHandler.GreetingInfoListener, NgaMessageHandler.ChipsInfoListener, NgaMessageHandler.ClearListener, ConfigurationController.ConfigurationListener, TouchActionRegion, TouchInsideRegion {
    private State mCurrentState;
    private final TouchInsideHandler mDefaultOnTap;
    private final FlingVelocityWrapper mFlingVelocity;
    private ListenableFuture<Void> mHideFuture;
    private TranscriptionSpaceListener mListener;
    private PendingIntent mOnGreetingTap;
    private PendingIntent mOnTranscriptionTap;
    private final ViewGroup mParent;
    private Runnable mQueuedCompletion;
    private State mQueuedState;
    private Map<State, TranscriptionSpaceView> mViewMap = new HashMap();
    private boolean mHasAccurateBackground = false;
    private boolean mQueuedStateAnimates = false;

    /* loaded from: classes2.dex */
    public enum State {
        TRANSCRIPTION,
        GREETING,
        CHIPS,
        NONE
    }

    /* loaded from: classes2.dex */
    public interface TranscriptionSpaceListener {
        void onStateChanged(State state, State state2);
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public interface TranscriptionSpaceView {
        void getHitRect(Rect rect);

        ListenableFuture<Void> hide(boolean z);

        void onFontSizeChanged();

        default void setCardVisible(boolean z) {
        }

        void setHasDarkBackground(boolean z);
    }

    /* access modifiers changed from: package-private */
    public TranscriptionController(ViewGroup viewGroup, TouchInsideHandler touchInsideHandler, FlingVelocityWrapper flingVelocityWrapper, ConfigurationController configurationController) {
        State state = State.NONE;
        this.mCurrentState = state;
        this.mQueuedState = state;
        this.mParent = viewGroup;
        this.mDefaultOnTap = touchInsideHandler;
        this.mFlingVelocity = flingVelocityWrapper;
        setUpViews();
        configurationController.addCallback(this);
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.CardInfoListener
    public void onCardInfo(boolean z, int i, boolean z2, boolean z3) {
        setCardVisible(z);
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.TranscriptionInfoListener
    public void onTranscriptionInfo(String str, PendingIntent pendingIntent, int i) {
        setTranscription(str, pendingIntent);
        setTranscriptionColor(i);
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.GreetingInfoListener
    public void onGreetingInfo(String str, PendingIntent pendingIntent) {
        if (!TextUtils.isEmpty(str)) {
            this.mOnGreetingTap = pendingIntent;
            Optional<Float> consumeVelocity = this.mFlingVelocity.consumeVelocity();
            if (this.mCurrentState != State.NONE || !consumeVelocity.isPresent()) {
                setQueuedState(State.GREETING, false, new Runnable(str) { // from class: com.google.android.systemui.assist.uihints.TranscriptionController$$ExternalSyntheticLambda3
                    public final /* synthetic */ String f$1;

                    {
                        this.f$1 = r2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        TranscriptionController.this.lambda$onGreetingInfo$1(this.f$1);
                    }
                });
            } else {
                setQueuedState(State.GREETING, false, new Runnable(str, consumeVelocity) { // from class: com.google.android.systemui.assist.uihints.TranscriptionController$$ExternalSyntheticLambda5
                    public final /* synthetic */ String f$1;
                    public final /* synthetic */ Optional f$2;

                    {
                        this.f$1 = r2;
                        this.f$2 = r3;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        TranscriptionController.this.lambda$onGreetingInfo$0(this.f$1, this.f$2);
                    }
                });
            }
            maybeSetState();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onGreetingInfo$0(String str, Optional optional) {
        ((GreetingView) this.mViewMap.get(State.GREETING)).setGreetingAnimated(str, ((Float) optional.get()).floatValue());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onGreetingInfo$1(String str) {
        ((GreetingView) this.mViewMap.get(State.GREETING)).setGreeting(str);
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.ChipsInfoListener
    public void onChipsInfo(List<Bundle> list) {
        if (list == null || list.size() == 0) {
            Log.e("TranscriptionController", "Null or empty chip list received; clearing transcription space");
            onClear(false);
            return;
        }
        Optional<Float> consumeVelocity = this.mFlingVelocity.consumeVelocity();
        if (this.mCurrentState != State.NONE || !consumeVelocity.isPresent()) {
            State state = this.mCurrentState;
            if (state == State.GREETING || state == State.TRANSCRIPTION) {
                setQueuedState(State.CHIPS, false, new Runnable(list) { // from class: com.google.android.systemui.assist.uihints.TranscriptionController$$ExternalSyntheticLambda6
                    public final /* synthetic */ List f$1;

                    {
                        this.f$1 = r2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        TranscriptionController.this.lambda$onChipsInfo$3(this.f$1);
                    }
                });
            } else {
                setQueuedState(State.CHIPS, false, new Runnable(list) { // from class: com.google.android.systemui.assist.uihints.TranscriptionController$$ExternalSyntheticLambda7
                    public final /* synthetic */ List f$1;

                    {
                        this.f$1 = r2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        TranscriptionController.this.lambda$onChipsInfo$4(this.f$1);
                    }
                });
            }
        } else {
            setQueuedState(State.CHIPS, false, new Runnable(list, consumeVelocity) { // from class: com.google.android.systemui.assist.uihints.TranscriptionController$$ExternalSyntheticLambda8
                public final /* synthetic */ List f$1;
                public final /* synthetic */ Optional f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    TranscriptionController.this.lambda$onChipsInfo$2(this.f$1, this.f$2);
                }
            });
        }
        maybeSetState();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onChipsInfo$2(List list, Optional optional) {
        ((ChipsContainer) this.mViewMap.get(State.CHIPS)).setChipsAnimatedBounce(list, ((Float) optional.get()).floatValue());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onChipsInfo$3(List list) {
        ((ChipsContainer) this.mViewMap.get(State.CHIPS)).setChipsAnimatedZoom(list);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onChipsInfo$4(List list) {
        ((ChipsContainer) this.mViewMap.get(State.CHIPS)).setChips(list);
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.ClearListener
    public void onClear(boolean z) {
        setQueuedState(State.NONE, z, null);
        maybeSetState();
    }

    public void setListener(TranscriptionSpaceListener transcriptionSpaceListener) {
        this.mListener = transcriptionSpaceListener;
        if (transcriptionSpaceListener != null) {
            transcriptionSpaceListener.onStateChanged(null, this.mCurrentState);
        }
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onDensityOrFontScaleChanged() {
        for (TranscriptionSpaceView transcriptionSpaceView : this.mViewMap.values()) {
            transcriptionSpaceView.onFontSizeChanged();
        }
    }

    public void setHasDarkBackground(boolean z) {
        for (TranscriptionSpaceView transcriptionSpaceView : this.mViewMap.values()) {
            transcriptionSpaceView.setHasDarkBackground(z);
        }
    }

    public void setCardVisible(boolean z) {
        for (TranscriptionSpaceView transcriptionSpaceView : this.mViewMap.values()) {
            transcriptionSpaceView.setCardVisible(z);
        }
    }

    public void setTranscription(String str, PendingIntent pendingIntent) {
        this.mOnTranscriptionTap = pendingIntent;
        setQueuedState(State.TRANSCRIPTION, false, new Runnable(str) { // from class: com.google.android.systemui.assist.uihints.TranscriptionController$$ExternalSyntheticLambda4
            public final /* synthetic */ String f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                TranscriptionController.this.lambda$setTranscription$5(this.f$1);
            }
        });
        maybeSetState();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setTranscription$5(String str) {
        ((TranscriptionView) this.mViewMap.get(State.TRANSCRIPTION)).setTranscription(str);
    }

    public void setTranscriptionColor(int i) {
        ((TranscriptionView) this.mViewMap.get(State.TRANSCRIPTION)).setTranscriptionColor(i);
    }

    public void setHasAccurateBackground(boolean z) {
        if (this.mHasAccurateBackground != z) {
            this.mHasAccurateBackground = z;
            if (z) {
                maybeSetState();
            }
        }
    }

    @Override // com.google.android.systemui.assist.uihints.input.TouchInsideRegion
    public Optional<Region> getTouchInsideRegion() {
        return hasTapAction() ? Optional.empty() : getTouchRegion();
    }

    @Override // com.google.android.systemui.assist.uihints.input.TouchActionRegion
    public Optional<Region> getTouchActionRegion() {
        return hasTapAction() ? getTouchRegion() : Optional.empty();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: com.google.android.systemui.assist.uihints.TranscriptionController$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$google$android$systemui$assist$uihints$TranscriptionController$State;

        static {
            int[] iArr = new int[State.values().length];
            $SwitchMap$com$google$android$systemui$assist$uihints$TranscriptionController$State = iArr;
            try {
                iArr[State.TRANSCRIPTION.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$google$android$systemui$assist$uihints$TranscriptionController$State[State.GREETING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$google$android$systemui$assist$uihints$TranscriptionController$State[State.CHIPS.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$google$android$systemui$assist$uihints$TranscriptionController$State[State.NONE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    private boolean hasTapAction() {
        int i = AnonymousClass1.$SwitchMap$com$google$android$systemui$assist$uihints$TranscriptionController$State[this.mCurrentState.ordinal()];
        return i != 1 ? i != 2 ? i == 3 : this.mOnGreetingTap != null : this.mOnTranscriptionTap != null;
    }

    private Optional<Region> getTouchRegion() {
        TranscriptionSpaceView transcriptionSpaceView = this.mViewMap.get(this.mCurrentState);
        if (transcriptionSpaceView == null) {
            return Optional.empty();
        }
        Rect rect = new Rect();
        transcriptionSpaceView.getHitRect(rect);
        return Optional.of(new Region(rect));
    }

    private void setQueuedState(State state, boolean z, Runnable runnable) {
        this.mQueuedState = state;
        this.mQueuedStateAnimates = z;
        this.mQueuedCompletion = runnable;
    }

    private void maybeSetState() {
        State state = this.mCurrentState;
        State state2 = this.mQueuedState;
        if (state == state2) {
            Runnable runnable = this.mQueuedCompletion;
            if (runnable != null) {
                runnable.run();
            }
        } else if (this.mHasAccurateBackground || state2 == State.NONE) {
            ListenableFuture<Void> listenableFuture = this.mHideFuture;
            if (listenableFuture == null || listenableFuture.isDone()) {
                updateListener(this.mCurrentState, this.mQueuedState);
                if (State.NONE.equals(this.mCurrentState)) {
                    this.mCurrentState = this.mQueuedState;
                    Runnable runnable2 = this.mQueuedCompletion;
                    if (runnable2 != null) {
                        runnable2.run();
                        return;
                    }
                    return;
                }
                ListenableFuture<Void> hide = this.mViewMap.get(this.mCurrentState).hide(this.mQueuedStateAnimates);
                this.mHideFuture = hide;
                Futures.transform(hide, new Function() { // from class: com.google.android.systemui.assist.uihints.TranscriptionController$$ExternalSyntheticLambda2
                    @Override // com.google.common.base.Function
                    public final Object apply(Object obj) {
                        return TranscriptionController.this.lambda$maybeSetState$6((Void) obj);
                    }
                }, MoreExecutors.directExecutor());
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Object lambda$maybeSetState$6(Void r1) {
        this.mCurrentState = this.mQueuedState;
        Runnable runnable = this.mQueuedCompletion;
        if (runnable == null) {
            return null;
        }
        runnable.run();
        return null;
    }

    private void updateListener(State state, State state2) {
        TranscriptionSpaceListener transcriptionSpaceListener = this.mListener;
        if (transcriptionSpaceListener != null) {
            transcriptionSpaceListener.onStateChanged(state, state2);
        }
    }

    private void setUpViews() {
        this.mViewMap = new HashMap();
        TranscriptionView transcriptionView = (TranscriptionView) this.mParent.findViewById(R$id.transcription);
        transcriptionView.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.systemui.assist.uihints.TranscriptionController$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TranscriptionController.this.lambda$setUpViews$7(view);
            }
        });
        transcriptionView.setOnTouchListener(this.mDefaultOnTap);
        this.mViewMap.put(State.TRANSCRIPTION, transcriptionView);
        GreetingView greetingView = (GreetingView) this.mParent.findViewById(R$id.greeting);
        greetingView.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.systemui.assist.uihints.TranscriptionController$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TranscriptionController.this.lambda$setUpViews$8(view);
            }
        });
        greetingView.setOnTouchListener(this.mDefaultOnTap);
        this.mViewMap.put(State.GREETING, greetingView);
        this.mViewMap.put(State.CHIPS, (ChipsContainer) this.mParent.findViewById(R$id.chips));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setUpViews$7(View view) {
        PendingIntent pendingIntent = this.mOnTranscriptionTap;
        if (pendingIntent == null) {
            this.mDefaultOnTap.onTouchInside();
            return;
        }
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException unused) {
            Log.e("TranscriptionController", "Transcription tap PendingIntent cancelled");
            this.mDefaultOnTap.onTouchInside();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setUpViews$8(View view) {
        PendingIntent pendingIntent = this.mOnGreetingTap;
        if (pendingIntent == null) {
            this.mDefaultOnTap.onTouchInside();
            return;
        }
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException unused) {
            Log.e("TranscriptionController", "Greeting tap PendingIntent cancelled");
            this.mDefaultOnTap.onTouchInside();
        }
    }
}
