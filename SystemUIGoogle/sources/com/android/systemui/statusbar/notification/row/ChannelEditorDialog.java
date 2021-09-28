package com.android.systemui.statusbar.notification.row;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ChannelEditorDialogController.kt */
/* loaded from: classes.dex */
public final class ChannelEditorDialog extends Dialog {
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public ChannelEditorDialog(Context context) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public final void updateDoneButtonText(boolean z) {
        int i;
        TextView textView = (TextView) findViewById(R$id.done_button);
        if (textView != null) {
            if (z) {
                i = R$string.inline_ok_button;
            } else {
                i = R$string.inline_done_button;
            }
            textView.setText(i);
        }
    }

    /* compiled from: ChannelEditorDialogController.kt */
    /* loaded from: classes.dex */
    public static final class Builder {
        private Context context;

        public final Builder setContext(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            this.context = context;
            return this;
        }

        public final ChannelEditorDialog build() {
            Context context = this.context;
            if (context != null) {
                return new ChannelEditorDialog(context);
            }
            Intrinsics.throwUninitializedPropertyAccessException("context");
            throw null;
        }
    }
}
