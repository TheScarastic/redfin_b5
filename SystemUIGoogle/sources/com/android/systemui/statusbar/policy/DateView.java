package com.android.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.icu.text.DateFormat;
import android.icu.text.DisplayContext;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;
import com.android.systemui.Dependency;
import com.android.systemui.R$string;
import com.android.systemui.R$styleable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import java.util.Date;
import java.util.Locale;
/* loaded from: classes.dex */
public class DateView extends TextView {
    private final BroadcastDispatcher mBroadcastDispatcher;
    private DateFormat mDateFormat;
    private String mDatePattern;
    private String mLastText;
    private final Date mCurrentTime = new Date();
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.policy.DateView.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Handler handler = DateView.this.getHandler();
            if (handler != null) {
                String action = intent.getAction();
                if ("android.intent.action.TIME_TICK".equals(action) || "android.intent.action.TIME_SET".equals(action) || "android.intent.action.TIMEZONE_CHANGED".equals(action) || "android.intent.action.LOCALE_CHANGED".equals(action)) {
                    if ("android.intent.action.LOCALE_CHANGED".equals(action) || "android.intent.action.TIMEZONE_CHANGED".equals(action)) {
                        handler.post(new DateView$1$$ExternalSyntheticLambda0(this));
                    }
                    handler.post(new DateView$1$$ExternalSyntheticLambda1(this));
                }
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0() {
            DateView.this.mDateFormat = null;
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$1() {
            DateView.this.updateClock();
        }
    };

    /* JADX INFO: finally extract failed */
    public DateView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R$styleable.DateView, 0, 0);
        try {
            this.mDatePattern = obtainStyledAttributes.getString(R$styleable.DateView_datePattern);
            obtainStyledAttributes.recycle();
            if (this.mDatePattern == null) {
                this.mDatePattern = getContext().getString(R$string.system_ui_date_pattern);
            }
            this.mBroadcastDispatcher = (BroadcastDispatcher) Dependency.get(BroadcastDispatcher.class);
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.TIME_TICK");
        intentFilter.addAction("android.intent.action.TIME_SET");
        intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
        intentFilter.addAction("android.intent.action.LOCALE_CHANGED");
        this.mBroadcastDispatcher.registerReceiverWithHandler(this.mIntentReceiver, intentFilter, (Handler) Dependency.get(Dependency.TIME_TICK_HANDLER));
        updateClock();
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mDateFormat = null;
        this.mBroadcastDispatcher.unregisterReceiver(this.mIntentReceiver);
    }

    protected void updateClock() {
        if (this.mDateFormat == null) {
            DateFormat instanceForSkeleton = DateFormat.getInstanceForSkeleton(this.mDatePattern, Locale.getDefault());
            instanceForSkeleton.setContext(DisplayContext.CAPITALIZATION_FOR_STANDALONE);
            this.mDateFormat = instanceForSkeleton;
        }
        this.mCurrentTime.setTime(System.currentTimeMillis());
        String format = this.mDateFormat.format(this.mCurrentTime);
        if (!format.equals(this.mLastText)) {
            setText(format);
            this.mLastText = format;
        }
    }
}
