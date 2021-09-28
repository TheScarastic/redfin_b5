package com.android.wm.shell.bubbles;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.util.Log;
import android.util.PathParser;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.android.internal.graphics.ColorUtils;
import com.android.launcher3.icons.BitmapInfo;
import com.android.wm.shell.R;
import com.android.wm.shell.bubbles.Bubble;
import com.android.wm.shell.bubbles.BubbleViewInfoTask;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
public class BubbleViewInfoTask extends AsyncTask<Void, Void, BubbleViewInfo> {
    private Bubble mBubble;
    private Callback mCallback;
    private WeakReference<Context> mContext;
    private WeakReference<BubbleController> mController;
    private BubbleIconFactory mIconFactory;
    private Executor mMainExecutor;
    private boolean mSkipInflation;
    private WeakReference<BubbleStackView> mStackView;

    /* loaded from: classes2.dex */
    public interface Callback {
        void onBubbleViewsReady(Bubble bubble);
    }

    /* access modifiers changed from: package-private */
    public BubbleViewInfoTask(Bubble bubble, Context context, BubbleController bubbleController, BubbleStackView bubbleStackView, BubbleIconFactory bubbleIconFactory, boolean z, Callback callback, Executor executor) {
        this.mBubble = bubble;
        this.mContext = new WeakReference<>(context);
        this.mController = new WeakReference<>(bubbleController);
        this.mStackView = new WeakReference<>(bubbleStackView);
        this.mIconFactory = bubbleIconFactory;
        this.mSkipInflation = z;
        this.mCallback = callback;
        this.mMainExecutor = executor;
    }

    /* access modifiers changed from: protected */
    public BubbleViewInfo doInBackground(Void... voidArr) {
        return BubbleViewInfo.populate(this.mContext.get(), this.mController.get(), this.mStackView.get(), this.mIconFactory, this.mBubble, this.mSkipInflation);
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(BubbleViewInfo bubbleViewInfo) {
        if (!isCancelled() && bubbleViewInfo != null) {
            this.mMainExecutor.execute(new Runnable(bubbleViewInfo) { // from class: com.android.wm.shell.bubbles.BubbleViewInfoTask$$ExternalSyntheticLambda0
                public final /* synthetic */ BubbleViewInfoTask.BubbleViewInfo f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    BubbleViewInfoTask.this.lambda$onPostExecute$0(this.f$1);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onPostExecute$0(BubbleViewInfo bubbleViewInfo) {
        this.mBubble.setViewInfo(bubbleViewInfo);
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onBubbleViewsReady(this.mBubble);
        }
    }

    /* loaded from: classes2.dex */
    public static class BubbleViewInfo {
        String appName;
        Bitmap badgeBitmap;
        Bitmap bubbleBitmap;
        int dotColor;
        Path dotPath;
        BubbleExpandedView expandedView;
        Bubble.FlyoutMessage flyoutMessage;
        BadgedImageView imageView;
        ShortcutInfo shortcutInfo;

        public static BubbleViewInfo populate(Context context, BubbleController bubbleController, BubbleStackView bubbleStackView, BubbleIconFactory bubbleIconFactory, Bubble bubble, boolean z) {
            BubbleViewInfo bubbleViewInfo = new BubbleViewInfo();
            if (!z && !bubble.isInflated()) {
                LayoutInflater from = LayoutInflater.from(context);
                BadgedImageView badgedImageView = (BadgedImageView) from.inflate(R.layout.bubble_view, (ViewGroup) bubbleStackView, false);
                bubbleViewInfo.imageView = badgedImageView;
                badgedImageView.initialize(bubbleController.getPositioner());
                BubbleExpandedView bubbleExpandedView = (BubbleExpandedView) from.inflate(R.layout.bubble_expanded_view, (ViewGroup) bubbleStackView, false);
                bubbleViewInfo.expandedView = bubbleExpandedView;
                bubbleExpandedView.initialize(bubbleController, bubbleStackView, false);
            }
            if (bubble.getShortcutInfo() != null) {
                bubbleViewInfo.shortcutInfo = bubble.getShortcutInfo();
            }
            PackageManager packageManagerForUser = BubbleController.getPackageManagerForUser(context, bubble.getUser().getIdentifier());
            try {
                ApplicationInfo applicationInfo = packageManagerForUser.getApplicationInfo(bubble.getPackageName(), 795136);
                if (applicationInfo != null) {
                    bubbleViewInfo.appName = String.valueOf(packageManagerForUser.getApplicationLabel(applicationInfo));
                }
                Drawable applicationIcon = packageManagerForUser.getApplicationIcon(bubble.getPackageName());
                Drawable userBadgedIcon = packageManagerForUser.getUserBadgedIcon(applicationIcon, bubble.getUser());
                Drawable bubbleDrawable = bubbleIconFactory.getBubbleDrawable(context, bubbleViewInfo.shortcutInfo, bubble.getIcon());
                if (bubbleDrawable != null) {
                    applicationIcon = bubbleDrawable;
                }
                BitmapInfo badgeBitmap = bubbleIconFactory.getBadgeBitmap(userBadgedIcon, bubble.isImportantConversation());
                bubbleViewInfo.badgeBitmap = badgeBitmap.icon;
                bubbleViewInfo.bubbleBitmap = bubbleIconFactory.createBadgedIconBitmap(applicationIcon, null, true).icon;
                Path createPathFromPathData = PathParser.createPathFromPathData(context.getResources().getString(17039948));
                Matrix matrix = new Matrix();
                float scale = bubbleIconFactory.getNormalizer().getScale(applicationIcon, null, null, null);
                matrix.setScale(scale, scale, 50.0f, 50.0f);
                createPathFromPathData.transform(matrix);
                bubbleViewInfo.dotPath = createPathFromPathData;
                bubbleViewInfo.dotColor = ColorUtils.blendARGB(badgeBitmap.color, -1, 0.54f);
                Bubble.FlyoutMessage flyoutMessage = bubble.getFlyoutMessage();
                bubbleViewInfo.flyoutMessage = flyoutMessage;
                if (flyoutMessage != null) {
                    flyoutMessage.senderAvatar = BubbleViewInfoTask.loadSenderAvatar(context, flyoutMessage.senderIcon);
                }
                return bubbleViewInfo;
            } catch (PackageManager.NameNotFoundException unused) {
                Log.w("Bubbles", "Unable to find package: " + bubble.getPackageName());
                return null;
            }
        }
    }

    static Drawable loadSenderAvatar(Context context, Icon icon) {
        Objects.requireNonNull(context);
        if (icon == null) {
            return null;
        }
        try {
            if (icon.getType() == 4 || icon.getType() == 6) {
                context.grantUriPermission(context.getPackageName(), icon.getUri(), 1);
            }
            return icon.loadDrawable(context);
        } catch (Exception e) {
            Log.w("Bubbles", "loadSenderAvatar failed: " + e.getMessage());
            return null;
        }
    }
}
