package com.android.systemui.privacy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import com.android.settingslib.Utils;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.R$style;
import com.android.systemui.privacy.PrivacyDialog;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: PrivacyDialog.kt */
/* loaded from: classes.dex */
public final class PrivacyDialog extends SystemUIDialog {
    private final View.OnClickListener clickListener;
    private final String enterpriseText;
    private final List<PrivacyElement> list;
    private final String phonecall;
    private ViewGroup rootView;
    private final List<WeakReference<OnDialogDismissed>> dismissListeners = new ArrayList();
    private final AtomicBoolean dismissed = new AtomicBoolean(false);
    private final int iconColorSolid = Utils.getColorAttrDefaultColor(getContext(), 16843827);

    /* compiled from: PrivacyDialog.kt */
    /* loaded from: classes.dex */
    public interface OnDialogDismissed {
        void onDialogDismissed();
    }

    /* compiled from: PrivacyDialog.kt */
    /* loaded from: classes.dex */
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[PrivacyType.values().length];
            iArr[PrivacyType.TYPE_LOCATION.ordinal()] = 1;
            iArr[PrivacyType.TYPE_CAMERA.ordinal()] = 2;
            iArr[PrivacyType.TYPE_MICROPHONE.ordinal()] = 3;
            $EnumSwitchMapping$0 = iArr;
        }
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public PrivacyDialog(Context context, List<PrivacyElement> list, Function2<? super String, ? super Integer, Unit> function2) {
        super(context, R$style.PrivacyDialog);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(list, "list");
        Intrinsics.checkNotNullParameter(function2, "activityStarter");
        this.list = list;
        this.enterpriseText = Intrinsics.stringPlus(" ", context.getString(R$string.ongoing_privacy_dialog_enterprise));
        this.phonecall = context.getString(R$string.ongoing_privacy_dialog_phonecall);
        this.clickListener = new View.OnClickListener(function2) { // from class: com.android.systemui.privacy.PrivacyDialog$clickListener$1
            final /* synthetic */ Function2<String, Integer, Unit> $activityStarter;

            /* JADX DEBUG: Multi-variable search result rejected for r1v0, resolved type: kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.Integer, kotlin.Unit> */
            /* JADX WARN: Multi-variable type inference failed */
            /* access modifiers changed from: package-private */
            {
                this.$activityStarter = r1;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                Object tag = view.getTag();
                if (tag != null) {
                    PrivacyDialog.PrivacyElement privacyElement = (PrivacyDialog.PrivacyElement) tag;
                    this.$activityStarter.invoke(privacyElement.getPackageName(), Integer.valueOf(privacyElement.getUserId()));
                }
            }
        };
    }

    @Override // android.app.AlertDialog, android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        if (window != null) {
            window.getAttributes().setFitInsetsTypes(window.getAttributes().getFitInsetsTypes() | WindowInsets.Type.statusBars());
            window.getAttributes().receiveInsetsIgnoringZOrder = true;
            window.setLayout(window.getContext().getResources().getDimensionPixelSize(R$dimen.qs_panel_width), -2);
            window.setGravity(49);
        }
        setContentView(R$layout.privacy_dialog);
        View requireViewById = requireViewById(R$id.root);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById<ViewGroup>(R.id.root)");
        this.rootView = (ViewGroup) requireViewById;
        for (PrivacyElement privacyElement : this.list) {
            ViewGroup viewGroup = this.rootView;
            if (viewGroup != null) {
                viewGroup.addView(createView(privacyElement));
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("rootView");
                throw null;
            }
        }
    }

    public final void addOnDismissListener(OnDialogDismissed onDialogDismissed) {
        Intrinsics.checkNotNullParameter(onDialogDismissed, "listener");
        if (this.dismissed.get()) {
            onDialogDismissed.onDialogDismissed();
        } else {
            this.dismissListeners.add(new WeakReference<>(onDialogDismissed));
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.phone.SystemUIDialog, android.app.Dialog
    public void onStop() {
        super.onStop();
        this.dismissed.set(true);
        Iterator<WeakReference<OnDialogDismissed>> it = this.dismissListeners.iterator();
        while (it.hasNext()) {
            it.remove();
            OnDialogDismissed onDialogDismissed = it.next().get();
            if (onDialogDismissed != null) {
                onDialogDismissed.onDialogDismissed();
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v13, types: [java.lang.CharSequence] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final android.view.View createView(com.android.systemui.privacy.PrivacyDialog.PrivacyElement r10) {
        /*
            r9 = this;
            android.content.Context r0 = r9.getContext()
            android.view.LayoutInflater r0 = android.view.LayoutInflater.from(r0)
            int r1 = com.android.systemui.R$layout.privacy_dialog_item
            android.view.ViewGroup r2 = r9.rootView
            if (r2 == 0) goto L_0x00d2
            r3 = 0
            android.view.View r0 = r0.inflate(r1, r2, r3)
            java.lang.String r1 = "null cannot be cast to non-null type android.view.ViewGroup"
            java.util.Objects.requireNonNull(r0, r1)
            android.view.ViewGroup r0 = (android.view.ViewGroup) r0
            com.android.systemui.privacy.PrivacyType r1 = r10.getType()
            android.graphics.drawable.LayerDrawable r1 = r9.getDrawableForType(r1)
            int r2 = com.android.systemui.R$id.icon
            android.graphics.drawable.Drawable r4 = r1.findDrawableByLayerId(r2)
            int r5 = r9.iconColorSolid
            r4.setTint(r5)
            android.view.View r2 = r0.requireViewById(r2)
            android.widget.ImageView r2 = (android.widget.ImageView) r2
            r2.setImageDrawable(r1)
            com.android.systemui.privacy.PrivacyType r1 = r10.getType()
            android.content.Context r4 = r2.getContext()
            java.lang.String r5 = "context"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r4, r5)
            java.lang.String r1 = r1.getName(r4)
            r2.setContentDescription(r1)
            boolean r1 = r10.getActive()
            int r1 = r9.getStringIdForState(r1)
            boolean r2 = r10.getPhoneCall()
            if (r2 == 0) goto L_0x005b
            java.lang.String r2 = r9.phonecall
            goto L_0x005f
        L_0x005b:
            java.lang.CharSequence r2 = r10.getApplicationName()
        L_0x005f:
            boolean r4 = r10.getEnterprise()
            r5 = 2
            r6 = 1
            if (r4 == 0) goto L_0x0073
            java.lang.CharSequence[] r4 = new java.lang.CharSequence[r5]
            r4[r3] = r2
            java.lang.String r2 = r9.enterpriseText
            r4[r6] = r2
            java.lang.CharSequence r2 = android.text.TextUtils.concat(r4)
        L_0x0073:
            android.content.Context r4 = r9.getContext()
            java.lang.Object[] r7 = new java.lang.Object[r6]
            r7[r3] = r2
            java.lang.String r1 = r4.getString(r1, r7)
            java.lang.CharSequence r2 = r10.getAttribution()
            if (r2 != 0) goto L_0x0086
            goto L_0x00a7
        L_0x0086:
            r4 = 3
            java.lang.CharSequence[] r4 = new java.lang.CharSequence[r4]
            r4[r3] = r1
            java.lang.String r7 = " "
            r4[r6] = r7
            android.content.Context r7 = r9.getContext()
            int r8 = com.android.systemui.R$string.ongoing_privacy_dialog_attribution_text
            java.lang.Object[] r6 = new java.lang.Object[r6]
            r6[r3] = r2
            java.lang.String r2 = r7.getString(r8, r6)
            r4[r5] = r2
            java.lang.CharSequence r2 = android.text.TextUtils.concat(r4)
            if (r2 != 0) goto L_0x00a6
            goto L_0x00a7
        L_0x00a6:
            r1 = r2
        L_0x00a7:
            int r2 = com.android.systemui.R$id.text
            android.view.View r2 = r0.requireViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r2.setText(r1)
            boolean r1 = r10.getPhoneCall()
            if (r1 == 0) goto L_0x00c3
            int r1 = com.android.systemui.R$id.chevron
            android.view.View r1 = r0.requireViewById(r1)
            r2 = 8
            r1.setVisibility(r2)
        L_0x00c3:
            r0.setTag(r10)
            boolean r10 = r10.getPhoneCall()
            if (r10 != 0) goto L_0x00d1
            android.view.View$OnClickListener r9 = r9.clickListener
            r0.setOnClickListener(r9)
        L_0x00d1:
            return r0
        L_0x00d2:
            java.lang.String r9 = "rootView"
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r9)
            r9 = 0
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.privacy.PrivacyDialog.createView(com.android.systemui.privacy.PrivacyDialog$PrivacyElement):android.view.View");
    }

    private final int getStringIdForState(boolean z) {
        if (z) {
            return R$string.ongoing_privacy_dialog_using_op;
        }
        return R$string.ongoing_privacy_dialog_recent_op;
    }

    private final LayerDrawable getDrawableForType(PrivacyType privacyType) {
        int i;
        Context context = getContext();
        int i2 = WhenMappings.$EnumSwitchMapping$0[privacyType.ordinal()];
        if (i2 == 1) {
            i = R$drawable.privacy_item_circle_location;
        } else if (i2 == 2) {
            i = R$drawable.privacy_item_circle_camera;
        } else if (i2 == 3) {
            i = R$drawable.privacy_item_circle_microphone;
        } else {
            throw new NoWhenBranchMatchedException();
        }
        Drawable drawable = context.getDrawable(i);
        Objects.requireNonNull(drawable, "null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
        return (LayerDrawable) drawable;
    }

    /* compiled from: PrivacyDialog.kt */
    /* loaded from: classes.dex */
    public static final class PrivacyElement {
        private final boolean active;
        private final CharSequence applicationName;
        private final CharSequence attribution;
        private final StringBuilder builder;
        private final boolean enterprise;
        private final long lastActiveTimestamp;
        private final String packageName;
        private final boolean phoneCall;
        private final PrivacyType type;
        private final int userId;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof PrivacyElement)) {
                return false;
            }
            PrivacyElement privacyElement = (PrivacyElement) obj;
            return this.type == privacyElement.type && Intrinsics.areEqual(this.packageName, privacyElement.packageName) && this.userId == privacyElement.userId && Intrinsics.areEqual(this.applicationName, privacyElement.applicationName) && Intrinsics.areEqual(this.attribution, privacyElement.attribution) && this.lastActiveTimestamp == privacyElement.lastActiveTimestamp && this.active == privacyElement.active && this.enterprise == privacyElement.enterprise && this.phoneCall == privacyElement.phoneCall;
        }

        public int hashCode() {
            int hashCode = ((((((this.type.hashCode() * 31) + this.packageName.hashCode()) * 31) + Integer.hashCode(this.userId)) * 31) + this.applicationName.hashCode()) * 31;
            CharSequence charSequence = this.attribution;
            int hashCode2 = (((hashCode + (charSequence == null ? 0 : charSequence.hashCode())) * 31) + Long.hashCode(this.lastActiveTimestamp)) * 31;
            boolean z = this.active;
            int i = 1;
            if (z) {
                z = true;
            }
            int i2 = z ? 1 : 0;
            int i3 = z ? 1 : 0;
            int i4 = z ? 1 : 0;
            int i5 = (hashCode2 + i2) * 31;
            boolean z2 = this.enterprise;
            if (z2) {
                z2 = true;
            }
            int i6 = z2 ? 1 : 0;
            int i7 = z2 ? 1 : 0;
            int i8 = z2 ? 1 : 0;
            int i9 = (i5 + i6) * 31;
            boolean z3 = this.phoneCall;
            if (!z3) {
                i = z3 ? 1 : 0;
            }
            return i9 + i;
        }

        public PrivacyElement(PrivacyType privacyType, String str, int i, CharSequence charSequence, CharSequence charSequence2, long j, boolean z, boolean z2, boolean z3) {
            Intrinsics.checkNotNullParameter(privacyType, "type");
            Intrinsics.checkNotNullParameter(str, "packageName");
            Intrinsics.checkNotNullParameter(charSequence, "applicationName");
            this.type = privacyType;
            this.packageName = str;
            this.userId = i;
            this.applicationName = charSequence;
            this.attribution = charSequence2;
            this.lastActiveTimestamp = j;
            this.active = z;
            this.enterprise = z2;
            this.phoneCall = z3;
            StringBuilder sb = new StringBuilder("PrivacyElement(");
            this.builder = sb;
            sb.append(Intrinsics.stringPlus("type=", privacyType.getLogName()));
            sb.append(Intrinsics.stringPlus(", packageName=", str));
            sb.append(Intrinsics.stringPlus(", userId=", Integer.valueOf(i)));
            sb.append(Intrinsics.stringPlus(", appName=", charSequence));
            if (charSequence2 != null) {
                sb.append(Intrinsics.stringPlus(", attribution=", charSequence2));
            }
            sb.append(Intrinsics.stringPlus(", lastActive=", Long.valueOf(j)));
            if (z) {
                sb.append(", active");
            }
            if (z2) {
                sb.append(", enterprise");
            }
            if (z3) {
                sb.append(", phoneCall");
            }
            sb.append(")");
        }

        public final PrivacyType getType() {
            return this.type;
        }

        public final String getPackageName() {
            return this.packageName;
        }

        public final int getUserId() {
            return this.userId;
        }

        public final CharSequence getApplicationName() {
            return this.applicationName;
        }

        public final CharSequence getAttribution() {
            return this.attribution;
        }

        public final long getLastActiveTimestamp() {
            return this.lastActiveTimestamp;
        }

        public final boolean getActive() {
            return this.active;
        }

        public final boolean getEnterprise() {
            return this.enterprise;
        }

        public final boolean getPhoneCall() {
            return this.phoneCall;
        }

        public String toString() {
            String sb = this.builder.toString();
            Intrinsics.checkNotNullExpressionValue(sb, "builder.toString()");
            return sb;
        }
    }
}
