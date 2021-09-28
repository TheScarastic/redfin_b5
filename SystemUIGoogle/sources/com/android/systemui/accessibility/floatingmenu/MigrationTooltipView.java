package com.android.systemui.accessibility.floatingmenu;

import android.content.Context;
import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.view.View;
import com.android.internal.accessibility.AccessibilityShortcutController;
import com.android.systemui.R$string;
import com.android.systemui.accessibility.floatingmenu.AnnotationLinkSpan;
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class MigrationTooltipView extends BaseTooltipView {
    /* access modifiers changed from: package-private */
    public MigrationTooltipView(Context context, AccessibilityFloatingMenuView accessibilityFloatingMenuView) {
        super(context, accessibilityFloatingMenuView);
        Intent intent = new Intent("android.settings.ACCESSIBILITY_DETAILS_SETTINGS");
        intent.addFlags(268435456);
        intent.putExtra("android.intent.extra.COMPONENT_NAME", AccessibilityShortcutController.ACCESSIBILITY_BUTTON_COMPONENT_NAME.flattenToShortString());
        setDescription(AnnotationLinkSpan.linkify(getContext().getText(R$string.accessibility_floating_button_migration_tooltip), new AnnotationLinkSpan.LinkInfo("link", new View.OnClickListener(intent) { // from class: com.android.systemui.accessibility.floatingmenu.MigrationTooltipView$$ExternalSyntheticLambda0
            public final /* synthetic */ Intent f$1;

            {
                this.f$1 = r2;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MigrationTooltipView.this.lambda$new$0(this.f$1, view);
            }
        })));
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Intent intent, View view) {
        getContext().startActivity(intent);
        hide();
    }
}
