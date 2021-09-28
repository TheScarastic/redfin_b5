package com.android.systemui.toast;

import android.animation.Animator;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.launcher3.icons.IconFactory;
import com.android.settingslib.applications.ApplicationsState;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.plugins.ToastPlugin;
/* loaded from: classes2.dex */
public class SystemUIToast implements ToastPlugin.Toast {
    final Context mContext;
    private int mDefaultGravity;
    final int mDefaultHorizontalMargin;
    final int mDefaultVerticalMargin;
    final int mDefaultX;
    private int mDefaultY;
    private final Animator mInAnimator;
    private final LayoutInflater mLayoutInflater;
    private final Animator mOutAnimator;
    private final String mPackageName;
    final ToastPlugin.Toast mPluginToast;
    final CharSequence mText;
    private final View mToastView;
    private final int mUserId;

    private static boolean hasFlag(int i, int i2) {
        return (i & i2) != 0;
    }

    /* access modifiers changed from: package-private */
    public SystemUIToast(LayoutInflater layoutInflater, Context context, CharSequence charSequence, String str, int i, int i2) {
        this(layoutInflater, context, charSequence, null, str, i, i2);
    }

    /* access modifiers changed from: package-private */
    public SystemUIToast(LayoutInflater layoutInflater, Context context, CharSequence charSequence, ToastPlugin.Toast toast, String str, int i, int i2) {
        this.mDefaultX = 0;
        this.mDefaultHorizontalMargin = 0;
        this.mDefaultVerticalMargin = 0;
        this.mLayoutInflater = layoutInflater;
        this.mContext = context;
        this.mText = charSequence;
        this.mPluginToast = toast;
        this.mPackageName = str;
        this.mUserId = i;
        this.mToastView = inflateToastView();
        this.mInAnimator = createInAnimator();
        this.mOutAnimator = createOutAnimator();
        onOrientationChange(i2);
    }

    @Override // com.android.systemui.plugins.ToastPlugin.Toast
    public Integer getGravity() {
        if (!isPluginToast() || this.mPluginToast.getGravity() == null) {
            return Integer.valueOf(this.mDefaultGravity);
        }
        return this.mPluginToast.getGravity();
    }

    @Override // com.android.systemui.plugins.ToastPlugin.Toast
    public Integer getXOffset() {
        if (!isPluginToast() || this.mPluginToast.getXOffset() == null) {
            return 0;
        }
        return this.mPluginToast.getXOffset();
    }

    @Override // com.android.systemui.plugins.ToastPlugin.Toast
    public Integer getYOffset() {
        if (!isPluginToast() || this.mPluginToast.getYOffset() == null) {
            return Integer.valueOf(this.mDefaultY);
        }
        return this.mPluginToast.getYOffset();
    }

    @Override // com.android.systemui.plugins.ToastPlugin.Toast
    public Integer getHorizontalMargin() {
        if (!isPluginToast() || this.mPluginToast.getHorizontalMargin() == null) {
            return 0;
        }
        return this.mPluginToast.getHorizontalMargin();
    }

    @Override // com.android.systemui.plugins.ToastPlugin.Toast
    public Integer getVerticalMargin() {
        if (!isPluginToast() || this.mPluginToast.getVerticalMargin() == null) {
            return 0;
        }
        return this.mPluginToast.getVerticalMargin();
    }

    @Override // com.android.systemui.plugins.ToastPlugin.Toast
    public View getView() {
        return this.mToastView;
    }

    @Override // com.android.systemui.plugins.ToastPlugin.Toast
    public Animator getInAnimation() {
        return this.mInAnimator;
    }

    @Override // com.android.systemui.plugins.ToastPlugin.Toast
    public Animator getOutAnimation() {
        return this.mOutAnimator;
    }

    public boolean hasCustomAnimation() {
        return (getInAnimation() == null && getOutAnimation() == null) ? false : true;
    }

    private boolean isPluginToast() {
        return this.mPluginToast != null;
    }

    private View inflateToastView() {
        if (isPluginToast() && this.mPluginToast.getView() != null) {
            return this.mPluginToast.getView();
        }
        ApplicationInfo applicationInfo = null;
        View inflate = this.mLayoutInflater.inflate(R$layout.text_toast, (ViewGroup) null);
        TextView textView = (TextView) inflate.findViewById(R$id.text);
        ImageView imageView = (ImageView) inflate.findViewById(R$id.icon);
        textView.setText(this.mText);
        try {
            applicationInfo = this.mContext.getPackageManager().getApplicationInfoAsUser(this.mPackageName, 0, this.mUserId);
        } catch (PackageManager.NameNotFoundException unused) {
            Log.e("SystemUIToast", "Package name not found package=" + this.mPackageName + " user=" + this.mUserId);
        }
        if (applicationInfo == null || applicationInfo.targetSdkVersion >= 31) {
            Drawable badgedIcon = getBadgedIcon(this.mContext, this.mPackageName, this.mUserId);
            if (badgedIcon == null) {
                imageView.setVisibility(8);
            } else {
                imageView.setImageDrawable(badgedIcon);
                if (applicationInfo.labelRes != 0) {
                    try {
                        imageView.setContentDescription(this.mContext.getPackageManager().getResourcesForApplication(applicationInfo, new Configuration(this.mContext.getResources().getConfiguration())).getString(applicationInfo.labelRes));
                    } catch (PackageManager.NameNotFoundException unused2) {
                        Log.d("SystemUIToast", "Cannot find application resources for icon label.");
                    }
                }
            }
        } else {
            textView.setMaxLines(Integer.MAX_VALUE);
            inflate.findViewById(R$id.icon).setVisibility(8);
        }
        return inflate;
    }

    @Override // com.android.systemui.plugins.ToastPlugin.Toast
    public void onOrientationChange(int i) {
        ToastPlugin.Toast toast = this.mPluginToast;
        if (toast != null) {
            toast.onOrientationChange(i);
        }
        this.mDefaultY = this.mContext.getResources().getDimensionPixelSize(17105584);
        this.mDefaultGravity = this.mContext.getResources().getInteger(17694929);
    }

    private Animator createInAnimator() {
        if (!isPluginToast() || this.mPluginToast.getInAnimation() == null) {
            return ToastDefaultAnimation.Companion.toastIn(getView());
        }
        return this.mPluginToast.getInAnimation();
    }

    private Animator createOutAnimator() {
        if (!isPluginToast() || this.mPluginToast.getOutAnimation() == null) {
            return ToastDefaultAnimation.Companion.toastOut(getView());
        }
        return this.mPluginToast.getOutAnimation();
    }

    public static Drawable getBadgedIcon(Context context, String str, int i) {
        ApplicationInfo applicationInfo;
        if (!(context.getApplicationContext() instanceof Application)) {
            return null;
        }
        try {
            Context createPackageContextAsUser = context.createPackageContextAsUser("android", 0, new UserHandle(i));
            ApplicationsState instance = ApplicationsState.getInstance((Application) context.getApplicationContext());
            if (!instance.isUserAdded(i)) {
                Log.d("SystemUIToast", "user hasn't been fully initialized, not showing an app icon for packageName=" + str);
                return null;
            }
            PackageManager packageManager = createPackageContextAsUser.getPackageManager();
            ApplicationsState.AppEntry entry = instance.getEntry(str, i);
            if (entry == null || (applicationInfo = entry.info) == null || !showApplicationIcon(applicationInfo, packageManager)) {
                return null;
            }
            ApplicationInfo applicationInfo2 = entry.info;
            return new BitmapDrawable(context.getResources(), IconFactory.obtain(context).createBadgedIconBitmap(applicationInfo2.loadUnbadgedIcon(packageManager), UserHandle.getUserHandleForUid(applicationInfo2.uid), true).icon);
        } catch (PackageManager.NameNotFoundException unused) {
            Log.e("SystemUIToast", "Could not create user package context");
            return null;
        }
    }

    private static boolean showApplicationIcon(ApplicationInfo applicationInfo, PackageManager packageManager) {
        if (!hasFlag(applicationInfo.flags, 128)) {
            return !hasFlag(applicationInfo.flags, 1);
        }
        if (packageManager.getLaunchIntentForPackage(applicationInfo.packageName) != null) {
            return true;
        }
        return false;
    }
}
