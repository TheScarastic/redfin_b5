package com.android.systemui.camera;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.android.systemui.R$string;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: CameraIntents.kt */
/* loaded from: classes.dex */
public final class CameraIntents {
    public static final Companion Companion = new Companion(null);
    private static final String DEFAULT_SECURE_CAMERA_INTENT_ACTION = "android.media.action.STILL_IMAGE_CAMERA_SECURE";
    private static final String DEFAULT_INSECURE_CAMERA_INTENT_ACTION = "android.media.action.STILL_IMAGE_CAMERA";

    public static final Intent getInsecureCameraIntent(Context context) {
        return Companion.getInsecureCameraIntent(context);
    }

    public static final String getOverrideCameraPackage(Context context) {
        return Companion.getOverrideCameraPackage(context);
    }

    public static final Intent getSecureCameraIntent(Context context) {
        return Companion.getSecureCameraIntent(context);
    }

    public static final boolean isInsecureCameraIntent(Intent intent) {
        return Companion.isInsecureCameraIntent(intent);
    }

    public static final boolean isSecureCameraIntent(Intent intent) {
        return Companion.isSecureCameraIntent(intent);
    }

    /* compiled from: CameraIntents.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final String getDEFAULT_SECURE_CAMERA_INTENT_ACTION() {
            return CameraIntents.DEFAULT_SECURE_CAMERA_INTENT_ACTION;
        }

        public final String getDEFAULT_INSECURE_CAMERA_INTENT_ACTION() {
            return CameraIntents.DEFAULT_INSECURE_CAMERA_INTENT_ACTION;
        }

        public final String getOverrideCameraPackage(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            String string = context.getResources().getString(R$string.config_cameraGesturePackage);
            if (string != null && !TextUtils.isEmpty(string)) {
                return string;
            }
            return null;
        }

        public final Intent getInsecureCameraIntent(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            Intent intent = new Intent(getDEFAULT_INSECURE_CAMERA_INTENT_ACTION());
            String overrideCameraPackage = getOverrideCameraPackage(context);
            if (overrideCameraPackage != null) {
                intent.setPackage(overrideCameraPackage);
            }
            return intent;
        }

        public final Intent getSecureCameraIntent(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            Intent intent = new Intent(getDEFAULT_SECURE_CAMERA_INTENT_ACTION());
            String overrideCameraPackage = getOverrideCameraPackage(context);
            if (overrideCameraPackage != null) {
                intent.setPackage(overrideCameraPackage);
            }
            Intent addFlags = intent.addFlags(8388608);
            Intrinsics.checkNotNullExpressionValue(addFlags, "intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)");
            return addFlags;
        }

        public final boolean isSecureCameraIntent(Intent intent) {
            String action = intent == null ? null : intent.getAction();
            if (action == null) {
                return false;
            }
            return action.equals(getDEFAULT_SECURE_CAMERA_INTENT_ACTION());
        }

        public final boolean isInsecureCameraIntent(Intent intent) {
            String action = intent == null ? null : intent.getAction();
            if (action == null) {
                return false;
            }
            return action.equals(getDEFAULT_INSECURE_CAMERA_INTENT_ACTION());
        }
    }
}
