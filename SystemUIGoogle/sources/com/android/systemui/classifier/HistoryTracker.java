package com.android.systemui.classifier;

import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.classifier.HistoryTracker;
import com.android.systemui.util.time.SystemClock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
/* loaded from: classes.dex */
public class HistoryTracker {
    private static final double HISTORY_DECAY = Math.pow(10.0d, (Math.log10(0.1d) / 10000.0d) * 100.0d);
    private final SystemClock mSystemClock;
    DelayQueue<CombinedResult> mResults = new DelayQueue<>();
    private final List<BeliefListener> mBeliefListeners = new ArrayList();

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface BeliefListener {
        void onBeliefChanged(double d);
    }

    /* access modifiers changed from: package-private */
    public HistoryTracker(SystemClock systemClock) {
        this.mSystemClock = systemClock;
    }

    /* access modifiers changed from: package-private */
    public double falseBelief() {
        do {
        } while (this.mResults.poll() != null);
        if (this.mResults.isEmpty()) {
            return 0.5d;
        }
        return ((Double) this.mResults.stream().map(new Function(this.mSystemClock.uptimeMillis()) { // from class: com.android.systemui.classifier.HistoryTracker$$ExternalSyntheticLambda4
            public final /* synthetic */ long f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return HistoryTracker.lambda$falseBelief$0(this.f$0, (HistoryTracker.CombinedResult) obj);
            }
        }).reduce(Double.valueOf(0.5d), HistoryTracker$$ExternalSyntheticLambda0.INSTANCE)).doubleValue();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ Double lambda$falseBelief$0(long j, CombinedResult combinedResult) {
        return Double.valueOf(combinedResult.getDecayedScore(j));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ Double lambda$falseBelief$1(Double d, Double d2) {
        return Double.valueOf((d.doubleValue() * d2.doubleValue()) / ((d.doubleValue() * d2.doubleValue()) + ((1.0d - d.doubleValue()) * (1.0d - d2.doubleValue()))));
    }

    /* access modifiers changed from: package-private */
    public double falseConfidence() {
        do {
        } while (this.mResults.poll() != null);
        if (this.mResults.isEmpty()) {
            return 0.0d;
        }
        return 1.0d - Math.sqrt(((Double) this.mResults.stream().map(new Function(((Double) this.mResults.stream().map(HistoryTracker$$ExternalSyntheticLambda5.INSTANCE).reduce(Double.valueOf(0.0d), HistoryTracker$$ExternalSyntheticLambda1.INSTANCE)).doubleValue() / ((double) this.mResults.size())) { // from class: com.android.systemui.classifier.HistoryTracker$$ExternalSyntheticLambda3
            public final /* synthetic */ double f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return HistoryTracker.lambda$falseConfidence$2(this.f$0, (HistoryTracker.CombinedResult) obj);
            }
        }).reduce(Double.valueOf(0.0d), HistoryTracker$$ExternalSyntheticLambda1.INSTANCE)).doubleValue() / ((double) this.mResults.size()));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ Double lambda$falseConfidence$2(double d, CombinedResult combinedResult) {
        return Double.valueOf(Math.pow(combinedResult.getScore() - d, 2.0d));
    }

    /* access modifiers changed from: package-private */
    public void addResults(Collection<FalsingClassifier.Result> collection, long j) {
        double d = 0.0d;
        for (FalsingClassifier.Result result : collection) {
            d += ((result.isFalse() ? 0.5d : -0.5d) * result.getConfidence()) + 0.5d;
        }
        double size = d / ((double) collection.size());
        if (size == 1.0d) {
            size = 0.99999d;
        } else if (size == 0.0d) {
            size = 1.0E-5d;
        }
        do {
        } while (this.mResults.poll() != null);
        this.mResults.add((DelayQueue<CombinedResult>) new CombinedResult(j, size));
        this.mBeliefListeners.forEach(new Consumer() { // from class: com.android.systemui.classifier.HistoryTracker$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                HistoryTracker.this.lambda$addResults$3((HistoryTracker.BeliefListener) obj);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$addResults$3(BeliefListener beliefListener) {
        beliefListener.onBeliefChanged(falseBelief());
    }

    /* access modifiers changed from: package-private */
    public void addBeliefListener(BeliefListener beliefListener) {
        this.mBeliefListeners.add(beliefListener);
    }

    /* access modifiers changed from: package-private */
    public void removeBeliefListener(BeliefListener beliefListener) {
        this.mBeliefListeners.remove(beliefListener);
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class CombinedResult implements Delayed {
        private final long mExpiryMs;
        private final double mScore;

        CombinedResult(long j, double d) {
            this.mExpiryMs = j + 10000;
            this.mScore = d;
        }

        double getDecayedScore(long j) {
            return ((this.mScore - 0.5d) * Math.pow(HistoryTracker.HISTORY_DECAY, ((double) (10000 - (this.mExpiryMs - j))) / 100.0d)) + 0.5d;
        }

        /* access modifiers changed from: package-private */
        public double getScore() {
            return this.mScore;
        }

        @Override // java.util.concurrent.Delayed
        public long getDelay(TimeUnit timeUnit) {
            return timeUnit.convert(this.mExpiryMs - HistoryTracker.this.mSystemClock.uptimeMillis(), TimeUnit.MILLISECONDS);
        }

        public int compareTo(Delayed delayed) {
            TimeUnit timeUnit = TimeUnit.MILLISECONDS;
            return Long.compare(getDelay(timeUnit), delayed.getDelay(timeUnit));
        }
    }
}
