package com.android.systemui.classifier;

import android.view.MotionEvent;
import com.android.systemui.classifier.FalsingClassifier;
import java.util.List;
/* loaded from: classes.dex */
public class DoubleTapClassifier extends FalsingClassifier {
    private final float mDoubleTapSlop;
    private final long mDoubleTapTimeMs;
    private final SingleTapClassifier mSingleTapClassifier;

    /* access modifiers changed from: package-private */
    public DoubleTapClassifier(FalsingDataProvider falsingDataProvider, SingleTapClassifier singleTapClassifier, float f, long j) {
        super(falsingDataProvider);
        this.mSingleTapClassifier = singleTapClassifier;
        this.mDoubleTapSlop = f;
        this.mDoubleTapTimeMs = j;
    }

    @Override // com.android.systemui.classifier.FalsingClassifier
    FalsingClassifier.Result calculateFalsingResult(int i, double d, double d2) {
        List<MotionEvent> recentMotionEvents = getRecentMotionEvents();
        List<MotionEvent> priorMotionEvents = getPriorMotionEvents();
        StringBuilder sb = new StringBuilder();
        if (priorMotionEvents == null) {
            return falsed(0.0d, "Only one gesture recorded");
        }
        return !isDoubleTap(priorMotionEvents, recentMotionEvents, sb) ? falsed(0.5d, sb.toString()) : FalsingClassifier.Result.passed(0.5d);
    }

    public boolean isDoubleTap(List<MotionEvent> list, List<MotionEvent> list2, StringBuilder sb) {
        FalsingClassifier.Result isTap = this.mSingleTapClassifier.isTap(list, 0.5d);
        if (isTap.isFalse()) {
            sb.append("First gesture is not a tap. ");
            sb.append(isTap.getReason());
            return false;
        }
        FalsingClassifier.Result isTap2 = this.mSingleTapClassifier.isTap(list2, 0.5d);
        if (isTap2.isFalse()) {
            sb.append("Second gesture is not a tap. ");
            sb.append(isTap2.getReason());
            return false;
        }
        MotionEvent motionEvent = list.get(list.size() - 1);
        MotionEvent motionEvent2 = list2.get(list2.size() - 1);
        long eventTime = motionEvent2.getEventTime() - motionEvent.getEventTime();
        if (eventTime > this.mDoubleTapTimeMs) {
            sb.append("Time between taps too large: ");
            sb.append(eventTime);
            sb.append("ms");
            return false;
        } else if (Math.abs(motionEvent.getX() - motionEvent2.getX()) >= this.mDoubleTapSlop) {
            sb.append("Delta X between taps too large:");
            sb.append(Math.abs(motionEvent.getX() - motionEvent2.getX()));
            sb.append(" vs ");
            sb.append(this.mDoubleTapSlop);
            return false;
        } else if (Math.abs(motionEvent.getY() - motionEvent2.getY()) < this.mDoubleTapSlop) {
            return true;
        } else {
            sb.append("Delta Y between taps too large:");
            sb.append(Math.abs(motionEvent.getY() - motionEvent2.getY()));
            sb.append(" vs ");
            sb.append(this.mDoubleTapSlop);
            return false;
        }
    }
}
