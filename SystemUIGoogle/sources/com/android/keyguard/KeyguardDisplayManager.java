package com.android.keyguard;

import android.app.Presentation;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.media.MediaRouter;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.DisplayInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.dagger.KeyguardStatusViewComponent;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$style;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationBarView;
import dagger.Lazy;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class KeyguardDisplayManager {
    private static boolean DEBUG = false;
    private final Context mContext;
    private final DisplayManager.DisplayListener mDisplayListener;
    private final DisplayManager mDisplayService;
    private final KeyguardStatusViewComponent.Factory mKeyguardStatusViewComponentFactory;
    private final Lazy<NavigationBarController> mNavigationBarControllerLazy;
    private boolean mShowing;
    private MediaRouter mMediaRouter = null;
    private final DisplayInfo mTmpDisplayInfo = new DisplayInfo();
    private final SparseArray<Presentation> mPresentations = new SparseArray<>();
    private final MediaRouter.SimpleCallback mMediaRouterCallback = new MediaRouter.SimpleCallback() { // from class: com.android.keyguard.KeyguardDisplayManager.2
        @Override // android.media.MediaRouter.SimpleCallback, android.media.MediaRouter.Callback
        public void onRouteSelected(MediaRouter mediaRouter, int i, MediaRouter.RouteInfo routeInfo) {
            if (KeyguardDisplayManager.DEBUG) {
                Log.d("KeyguardDisplayManager", "onRouteSelected: type=" + i + ", info=" + routeInfo);
            }
            KeyguardDisplayManager keyguardDisplayManager = KeyguardDisplayManager.this;
            keyguardDisplayManager.updateDisplays(keyguardDisplayManager.mShowing);
        }

        @Override // android.media.MediaRouter.SimpleCallback, android.media.MediaRouter.Callback
        public void onRouteUnselected(MediaRouter mediaRouter, int i, MediaRouter.RouteInfo routeInfo) {
            if (KeyguardDisplayManager.DEBUG) {
                Log.d("KeyguardDisplayManager", "onRouteUnselected: type=" + i + ", info=" + routeInfo);
            }
            KeyguardDisplayManager keyguardDisplayManager = KeyguardDisplayManager.this;
            keyguardDisplayManager.updateDisplays(keyguardDisplayManager.mShowing);
        }

        @Override // android.media.MediaRouter.Callback
        public void onRoutePresentationDisplayChanged(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            if (KeyguardDisplayManager.DEBUG) {
                Log.d("KeyguardDisplayManager", "onRoutePresentationDisplayChanged: info=" + routeInfo);
            }
            KeyguardDisplayManager keyguardDisplayManager = KeyguardDisplayManager.this;
            keyguardDisplayManager.updateDisplays(keyguardDisplayManager.mShowing);
        }
    };

    public KeyguardDisplayManager(Context context, Lazy<NavigationBarController> lazy, KeyguardStatusViewComponent.Factory factory, Executor executor) {
        AnonymousClass1 r1 = new DisplayManager.DisplayListener() { // from class: com.android.keyguard.KeyguardDisplayManager.1
            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayChanged(int i) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayAdded(int i) {
                Display display = KeyguardDisplayManager.this.mDisplayService.getDisplay(i);
                if (KeyguardDisplayManager.this.mShowing) {
                    KeyguardDisplayManager.this.updateNavigationBarVisibility(i, false);
                    KeyguardDisplayManager.this.showPresentation(display);
                }
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayRemoved(int i) {
                KeyguardDisplayManager.this.hidePresentation(i);
            }
        };
        this.mDisplayListener = r1;
        this.mContext = context;
        this.mNavigationBarControllerLazy = lazy;
        this.mKeyguardStatusViewComponentFactory = factory;
        executor.execute(new Runnable() { // from class: com.android.keyguard.KeyguardDisplayManager$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                KeyguardDisplayManager.this.lambda$new$0();
            }
        });
        DisplayManager displayManager = (DisplayManager) context.getSystemService(DisplayManager.class);
        this.mDisplayService = displayManager;
        displayManager.registerDisplayListener(r1, null);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mMediaRouter = (MediaRouter) this.mContext.getSystemService(MediaRouter.class);
    }

    private boolean isKeyguardShowable(Display display) {
        if (display == null) {
            if (DEBUG) {
                Log.i("KeyguardDisplayManager", "Cannot show Keyguard on null display");
            }
            return false;
        } else if (display.getDisplayId() == 0) {
            if (DEBUG) {
                Log.i("KeyguardDisplayManager", "Do not show KeyguardPresentation on the default display");
            }
            return false;
        } else {
            display.getDisplayInfo(this.mTmpDisplayInfo);
            DisplayInfo displayInfo = this.mTmpDisplayInfo;
            if ((displayInfo.flags & 4) != 0) {
                if (DEBUG) {
                    Log.i("KeyguardDisplayManager", "Do not show KeyguardPresentation on a private display");
                }
                return false;
            } else if (displayInfo.displayGroupId == 0) {
                return true;
            } else {
                if (DEBUG) {
                    Log.i("KeyguardDisplayManager", "Do not show KeyguardPresentation on a non-default group display");
                }
                return false;
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean showPresentation(Display display) {
        if (!isKeyguardShowable(display)) {
            return false;
        }
        if (DEBUG) {
            Log.i("KeyguardDisplayManager", "Keyguard enabled on display: " + display);
        }
        int displayId = display.getDisplayId();
        if (this.mPresentations.get(displayId) == null) {
            KeyguardPresentation createPresentation = createPresentation(display);
            createPresentation.setOnDismissListener(new DialogInterface.OnDismissListener(createPresentation, displayId) { // from class: com.android.keyguard.KeyguardDisplayManager$$ExternalSyntheticLambda0
                public final /* synthetic */ Presentation f$1;
                public final /* synthetic */ int f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    KeyguardDisplayManager.this.lambda$showPresentation$1(this.f$1, this.f$2, dialogInterface);
                }
            });
            try {
                createPresentation.show();
            } catch (WindowManager.InvalidDisplayException e) {
                Log.w("KeyguardDisplayManager", "Invalid display:", e);
                createPresentation = null;
            }
            if (createPresentation != null) {
                this.mPresentations.append(displayId, createPresentation);
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showPresentation$1(Presentation presentation, int i, DialogInterface dialogInterface) {
        if (presentation.equals(this.mPresentations.get(i))) {
            this.mPresentations.remove(i);
        }
    }

    KeyguardPresentation createPresentation(Display display) {
        return new KeyguardPresentation(this.mContext, display, this.mKeyguardStatusViewComponentFactory);
    }

    /* access modifiers changed from: private */
    public void hidePresentation(int i) {
        Presentation presentation = this.mPresentations.get(i);
        if (presentation != null) {
            presentation.dismiss();
            this.mPresentations.remove(i);
        }
    }

    public void show() {
        if (!this.mShowing) {
            if (DEBUG) {
                Log.v("KeyguardDisplayManager", "show");
            }
            MediaRouter mediaRouter = this.mMediaRouter;
            if (mediaRouter != null) {
                mediaRouter.addCallback(4, this.mMediaRouterCallback, 8);
            } else {
                Log.w("KeyguardDisplayManager", "MediaRouter not yet initialized");
            }
            updateDisplays(true);
        }
        this.mShowing = true;
    }

    public void hide() {
        if (this.mShowing) {
            if (DEBUG) {
                Log.v("KeyguardDisplayManager", "hide");
            }
            MediaRouter mediaRouter = this.mMediaRouter;
            if (mediaRouter != null) {
                mediaRouter.removeCallback(this.mMediaRouterCallback);
            }
            updateDisplays(false);
        }
        this.mShowing = false;
    }

    protected boolean updateDisplays(boolean z) {
        boolean z2 = false;
        if (z) {
            Display[] displays = this.mDisplayService.getDisplays();
            boolean z3 = false;
            for (Display display : displays) {
                updateNavigationBarVisibility(display.getDisplayId(), false);
                z3 |= showPresentation(display);
            }
            return z3;
        }
        if (this.mPresentations.size() > 0) {
            z2 = true;
        }
        for (int size = this.mPresentations.size() - 1; size >= 0; size--) {
            updateNavigationBarVisibility(this.mPresentations.keyAt(size), true);
            this.mPresentations.valueAt(size).dismiss();
        }
        this.mPresentations.clear();
        return z2;
    }

    /* access modifiers changed from: private */
    public void updateNavigationBarVisibility(int i, boolean z) {
        NavigationBarView navigationBarView;
        if (i != 0 && (navigationBarView = this.mNavigationBarControllerLazy.get().getNavigationBarView(i)) != null) {
            if (z) {
                navigationBarView.getRootView().setVisibility(0);
            } else {
                navigationBarView.getRootView().setVisibility(8);
            }
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: classes.dex */
    public static final class KeyguardPresentation extends Presentation {
        private View mClock;
        private final Context mContext;
        private KeyguardClockSwitchController mKeyguardClockSwitchController;
        private final KeyguardStatusViewComponent.Factory mKeyguardStatusViewComponentFactory;
        private int mMarginLeft;
        private int mMarginTop;
        Runnable mMoveTextRunnable = new Runnable() { // from class: com.android.keyguard.KeyguardDisplayManager.KeyguardPresentation.1
            @Override // java.lang.Runnable
            public void run() {
                int random = KeyguardPresentation.this.mMarginLeft + ((int) (Math.random() * ((double) (KeyguardPresentation.this.mUsableWidth - KeyguardPresentation.this.mClock.getWidth()))));
                int random2 = KeyguardPresentation.this.mMarginTop + ((int) (Math.random() * ((double) (KeyguardPresentation.this.mUsableHeight - KeyguardPresentation.this.mClock.getHeight()))));
                KeyguardPresentation.this.mClock.setTranslationX((float) random);
                KeyguardPresentation.this.mClock.setTranslationY((float) random2);
                KeyguardPresentation.this.mClock.postDelayed(KeyguardPresentation.this.mMoveTextRunnable, 10000);
            }
        };
        private int mUsableHeight;
        private int mUsableWidth;

        @Override // android.app.Dialog, android.content.DialogInterface
        public void cancel() {
        }

        KeyguardPresentation(Context context, Display display, KeyguardStatusViewComponent.Factory factory) {
            super(context, display, R$style.Theme_SystemUI_KeyguardPresentation, 2009);
            this.mKeyguardStatusViewComponentFactory = factory;
            setCancelable(false);
            this.mContext = context;
        }

        @Override // android.app.Dialog, android.view.Window.Callback
        public void onDetachedFromWindow() {
            this.mClock.removeCallbacks(this.mMoveTextRunnable);
        }

        @Override // android.app.Presentation
        public void onDisplayChanged() {
            updateBounds();
            getWindow().getDecorView().requestLayout();
        }

        @Override // android.app.Dialog
        protected void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            updateBounds();
            setContentView(LayoutInflater.from(this.mContext).inflate(R$layout.keyguard_presentation, (ViewGroup) null));
            getWindow().getDecorView().setSystemUiVisibility(1792);
            getWindow().getAttributes().setFitInsetsTypes(0);
            getWindow().setNavigationBarContrastEnforced(false);
            getWindow().setNavigationBarColor(0);
            int i = R$id.clock;
            View findViewById = findViewById(i);
            this.mClock = findViewById;
            findViewById.post(this.mMoveTextRunnable);
            KeyguardClockSwitchController keyguardClockSwitchController = this.mKeyguardStatusViewComponentFactory.build((KeyguardStatusView) findViewById(i)).getKeyguardClockSwitchController();
            this.mKeyguardClockSwitchController = keyguardClockSwitchController;
            keyguardClockSwitchController.setOnlyClock(true);
            this.mKeyguardClockSwitchController.init();
        }

        private void updateBounds() {
            Rect bounds = getWindow().getWindowManager().getMaximumWindowMetrics().getBounds();
            this.mUsableWidth = (bounds.width() * 80) / 100;
            this.mUsableHeight = (bounds.height() * 80) / 100;
            this.mMarginLeft = (bounds.width() * 20) / 200;
            this.mMarginTop = (bounds.height() * 20) / 200;
        }
    }
}
