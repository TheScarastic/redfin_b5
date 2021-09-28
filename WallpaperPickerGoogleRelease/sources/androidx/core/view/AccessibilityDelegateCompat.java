package androidx.core.view;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import com.android.systemui.shared.R;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class AccessibilityDelegateCompat {
    public static final View.AccessibilityDelegate DEFAULT_DELEGATE = new View.AccessibilityDelegate();
    public final View.AccessibilityDelegate mBridge;
    public final View.AccessibilityDelegate mOriginalDelegate;

    /* loaded from: classes.dex */
    public static final class AccessibilityDelegateAdapter extends View.AccessibilityDelegate {
        public final AccessibilityDelegateCompat mCompat;

        public AccessibilityDelegateAdapter(AccessibilityDelegateCompat accessibilityDelegateCompat) {
            this.mCompat = accessibilityDelegateCompat;
        }

        @Override // android.view.View.AccessibilityDelegate
        public boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            return this.mCompat.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
        }

        @Override // android.view.View.AccessibilityDelegate
        public AccessibilityNodeProvider getAccessibilityNodeProvider(View view) {
            AccessibilityNodeProviderCompat accessibilityNodeProvider = this.mCompat.getAccessibilityNodeProvider(view);
            if (accessibilityNodeProvider != null) {
                return (AccessibilityNodeProvider) accessibilityNodeProvider.mProvider;
            }
            return null;
        }

        @Override // android.view.View.AccessibilityDelegate
        public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            this.mCompat.onInitializeAccessibilityEvent(view, accessibilityEvent);
        }

        @Override // android.view.View.AccessibilityDelegate
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
            boolean z;
            boolean z2;
            AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = new AccessibilityNodeInfoCompat(accessibilityNodeInfo);
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            Boolean valueOf = Boolean.valueOf(view.isScreenReaderFocusable());
            if (valueOf == null) {
                z = false;
            } else {
                z = valueOf.booleanValue();
            }
            accessibilityNodeInfo.setScreenReaderFocusable(z);
            Boolean valueOf2 = Boolean.valueOf(view.isAccessibilityHeading());
            if (valueOf2 == null) {
                z2 = false;
            } else {
                z2 = valueOf2.booleanValue();
            }
            accessibilityNodeInfo.setHeading(z2);
            accessibilityNodeInfo.setPaneTitle(ViewCompat.getAccessibilityPaneTitle(view));
            accessibilityNodeInfo.setStateDescription(view.getStateDescription());
            this.mCompat.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            accessibilityNodeInfo.getText();
            List list = (List) view.getTag(R.id.tag_accessibility_actions);
            if (list == null) {
                list = Collections.emptyList();
            }
            for (int i = 0; i < list.size(); i++) {
                accessibilityNodeInfoCompat.addAction((AccessibilityNodeInfoCompat.AccessibilityActionCompat) list.get(i));
            }
        }

        @Override // android.view.View.AccessibilityDelegate
        public void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            this.mCompat.onPopulateAccessibilityEvent(view, accessibilityEvent);
        }

        @Override // android.view.View.AccessibilityDelegate
        public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            return this.mCompat.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
        }

        @Override // android.view.View.AccessibilityDelegate
        public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
            return this.mCompat.performAccessibilityAction(view, i, bundle);
        }

        @Override // android.view.View.AccessibilityDelegate
        public void sendAccessibilityEvent(View view, int i) {
            this.mCompat.sendAccessibilityEvent(view, i);
        }

        @Override // android.view.View.AccessibilityDelegate
        public void sendAccessibilityEventUnchecked(View view, AccessibilityEvent accessibilityEvent) {
            this.mCompat.sendAccessibilityEventUnchecked(view, accessibilityEvent);
        }
    }

    public AccessibilityDelegateCompat() {
        this.mOriginalDelegate = DEFAULT_DELEGATE;
        this.mBridge = new AccessibilityDelegateAdapter(this);
    }

    public boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        return this.mOriginalDelegate.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
    }

    public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View view) {
        AccessibilityNodeProvider accessibilityNodeProvider = this.mOriginalDelegate.getAccessibilityNodeProvider(view);
        if (accessibilityNodeProvider != null) {
            return new AccessibilityNodeProviderCompat(accessibilityNodeProvider);
        }
        return null;
    }

    public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        this.mOriginalDelegate.onInitializeAccessibilityEvent(view, accessibilityEvent);
    }

    public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        this.mOriginalDelegate.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat.mInfo);
    }

    public void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        this.mOriginalDelegate.onPopulateAccessibilityEvent(view, accessibilityEvent);
    }

    public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
        return this.mOriginalDelegate.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0073  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean performAccessibilityAction(android.view.View r9, int r10, android.os.Bundle r11) {
        /*
            r8 = this;
            r0 = 2131362348(0x7f0a022c, float:1.8344474E38)
            java.lang.Object r0 = r9.getTag(r0)
            java.util.List r0 = (java.util.List) r0
            if (r0 != 0) goto L_0x000f
            java.util.List r0 = java.util.Collections.emptyList()
        L_0x000f:
            r1 = 0
            r2 = r1
        L_0x0011:
            int r3 = r0.size()
            if (r2 >= r3) goto L_0x0070
            java.lang.Object r3 = r0.get(r2)
            androidx.core.view.accessibility.AccessibilityNodeInfoCompat$AccessibilityActionCompat r3 = (androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat) r3
            int r4 = r3.getId()
            if (r4 != r10) goto L_0x006d
            androidx.core.view.accessibility.AccessibilityViewCommand r0 = r3.mCommand
            if (r0 == 0) goto L_0x0070
            r0 = 0
            java.lang.Class<? extends androidx.core.view.accessibility.AccessibilityViewCommand$CommandArguments> r2 = r3.mViewCommandArgumentClass
            if (r2 == 0) goto L_0x0066
            java.lang.Class[] r4 = new java.lang.Class[r1]     // Catch: Exception -> 0x0040
            java.lang.reflect.Constructor r2 = r2.getDeclaredConstructor(r4)     // Catch: Exception -> 0x0040
            java.lang.Object[] r4 = new java.lang.Object[r1]     // Catch: Exception -> 0x0040
            java.lang.Object r2 = r2.newInstance(r4)     // Catch: Exception -> 0x0040
            androidx.core.view.accessibility.AccessibilityViewCommand$CommandArguments r2 = (androidx.core.view.accessibility.AccessibilityViewCommand.CommandArguments) r2     // Catch: Exception -> 0x0040
            java.util.Objects.requireNonNull(r2)     // Catch: Exception -> 0x003e
            goto L_0x0065
        L_0x003e:
            r0 = move-exception
            goto L_0x0044
        L_0x0040:
            r2 = move-exception
            r7 = r2
            r2 = r0
            r0 = r7
        L_0x0044:
            java.lang.Class<? extends androidx.core.view.accessibility.AccessibilityViewCommand$CommandArguments> r4 = r3.mViewCommandArgumentClass
            if (r4 != 0) goto L_0x004b
            java.lang.String r4 = "null"
            goto L_0x004f
        L_0x004b:
            java.lang.String r4 = r4.getName()
        L_0x004f:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "Failed to execute command with argument class ViewCommandArgument: "
            r5.append(r6)
            r5.append(r4)
            java.lang.String r4 = r5.toString()
            java.lang.String r5 = "A11yActionCompat"
            android.util.Log.e(r5, r4, r0)
        L_0x0065:
            r0 = r2
        L_0x0066:
            androidx.core.view.accessibility.AccessibilityViewCommand r2 = r3.mCommand
            boolean r0 = r2.perform(r9, r0)
            goto L_0x0071
        L_0x006d:
            int r2 = r2 + 1
            goto L_0x0011
        L_0x0070:
            r0 = r1
        L_0x0071:
            if (r0 != 0) goto L_0x0079
            android.view.View$AccessibilityDelegate r8 = r8.mOriginalDelegate
            boolean r0 = r8.performAccessibilityAction(r9, r10, r11)
        L_0x0079:
            if (r0 != 0) goto L_0x00ca
            r8 = 2131361808(0x7f0a0010, float:1.8343379E38)
            if (r10 != r8) goto L_0x00ca
            r8 = -1
            java.lang.String r10 = "ACCESSIBILITY_CLICKABLE_SPAN_ID"
            int r8 = r11.getInt(r10, r8)
            r10 = 2131362349(0x7f0a022d, float:1.8344476E38)
            java.lang.Object r10 = r9.getTag(r10)
            android.util.SparseArray r10 = (android.util.SparseArray) r10
            r11 = 1
            if (r10 == 0) goto L_0x00c9
            java.lang.Object r8 = r10.get(r8)
            java.lang.ref.WeakReference r8 = (java.lang.ref.WeakReference) r8
            if (r8 == 0) goto L_0x00c9
            java.lang.Object r8 = r8.get()
            android.text.style.ClickableSpan r8 = (android.text.style.ClickableSpan) r8
            if (r8 == 0) goto L_0x00c2
            android.view.accessibility.AccessibilityNodeInfo r10 = r9.createAccessibilityNodeInfo()
            java.lang.CharSequence r10 = r10.getText()
            android.text.style.ClickableSpan[] r10 = androidx.core.view.accessibility.AccessibilityNodeInfoCompat.getClickableSpans(r10)
            r0 = r1
        L_0x00b0:
            if (r10 == 0) goto L_0x00c2
            int r2 = r10.length
            if (r0 >= r2) goto L_0x00c2
            r2 = r10[r0]
            boolean r2 = r8.equals(r2)
            if (r2 == 0) goto L_0x00bf
            r10 = r11
            goto L_0x00c3
        L_0x00bf:
            int r0 = r0 + 1
            goto L_0x00b0
        L_0x00c2:
            r10 = r1
        L_0x00c3:
            if (r10 == 0) goto L_0x00c9
            r8.onClick(r9)
            r1 = r11
        L_0x00c9:
            r0 = r1
        L_0x00ca:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.view.AccessibilityDelegateCompat.performAccessibilityAction(android.view.View, int, android.os.Bundle):boolean");
    }

    public void sendAccessibilityEvent(View view, int i) {
        this.mOriginalDelegate.sendAccessibilityEvent(view, i);
    }

    public void sendAccessibilityEventUnchecked(View view, AccessibilityEvent accessibilityEvent) {
        this.mOriginalDelegate.sendAccessibilityEventUnchecked(view, accessibilityEvent);
    }

    public AccessibilityDelegateCompat(View.AccessibilityDelegate accessibilityDelegate) {
        this.mOriginalDelegate = accessibilityDelegate;
        this.mBridge = new AccessibilityDelegateAdapter(this);
    }
}
