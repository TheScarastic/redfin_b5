package androidx.core.widget;
/* loaded from: classes.dex */
public final /* synthetic */ class ContentLoadingProgressBar$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ ContentLoadingProgressBar f$0;

    public /* synthetic */ ContentLoadingProgressBar$$ExternalSyntheticLambda0(ContentLoadingProgressBar contentLoadingProgressBar, int i) {
        this.$r8$classId = i;
        if (i == 1 || i == 2 || i != 3) {
        }
        this.f$0 = contentLoadingProgressBar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                ContentLoadingProgressBar contentLoadingProgressBar = this.f$0;
                contentLoadingProgressBar.mPostedHide = false;
                contentLoadingProgressBar.mStartTime = -1;
                contentLoadingProgressBar.setVisibility(8);
                return;
            case 1:
                ContentLoadingProgressBar contentLoadingProgressBar2 = this.f$0;
                contentLoadingProgressBar2.mPostedShow = false;
                if (!contentLoadingProgressBar2.mDismissed) {
                    contentLoadingProgressBar2.mStartTime = System.currentTimeMillis();
                    contentLoadingProgressBar2.setVisibility(0);
                    return;
                }
                return;
            case 2:
                ContentLoadingProgressBar contentLoadingProgressBar3 = this.f$0;
                contentLoadingProgressBar3.mStartTime = -1;
                contentLoadingProgressBar3.mDismissed = false;
                contentLoadingProgressBar3.removeCallbacks(contentLoadingProgressBar3.mDelayedHide);
                contentLoadingProgressBar3.mPostedHide = false;
                if (!contentLoadingProgressBar3.mPostedShow) {
                    contentLoadingProgressBar3.postDelayed(contentLoadingProgressBar3.mDelayedShow, 500);
                    contentLoadingProgressBar3.mPostedShow = true;
                    return;
                }
                return;
            case 3:
                ContentLoadingProgressBar contentLoadingProgressBar4 = this.f$0;
                contentLoadingProgressBar4.mDismissed = true;
                contentLoadingProgressBar4.removeCallbacks(contentLoadingProgressBar4.mDelayedShow);
                contentLoadingProgressBar4.mPostedShow = false;
                long currentTimeMillis = System.currentTimeMillis();
                long j = contentLoadingProgressBar4.mStartTime;
                long j2 = currentTimeMillis - j;
                if (j2 >= 500 || j == -1) {
                    contentLoadingProgressBar4.setVisibility(8);
                    return;
                } else if (!contentLoadingProgressBar4.mPostedHide) {
                    contentLoadingProgressBar4.postDelayed(contentLoadingProgressBar4.mDelayedHide, 500 - j2);
                    contentLoadingProgressBar4.mPostedHide = true;
                    return;
                } else {
                    return;
                }
            case 4:
                this.f$0.show();
                return;
            default:
                this.f$0.hide();
                return;
        }
    }
}
