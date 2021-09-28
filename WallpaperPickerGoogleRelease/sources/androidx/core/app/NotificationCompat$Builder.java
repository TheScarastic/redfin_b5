package androidx.core.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class NotificationCompat$Builder {
    public PendingIntent mContentIntent;
    public CharSequence mContentText;
    public CharSequence mContentTitle;
    public Context mContext;
    public Bundle mExtras;
    public Notification mNotification;
    public NotificationCompat$Style mStyle;
    public ArrayList<NotificationCompat$Action> mActions = new ArrayList<>();
    public ArrayList<Person> mPersonList = new ArrayList<>();
    public ArrayList<NotificationCompat$Action> mInvisibleActions = new ArrayList<>();
    public boolean mShowWhen = true;
    public boolean mLocalOnly = false;
    public String mChannelId = null;
    @Deprecated
    public ArrayList<String> mPeople = new ArrayList<>();
    public boolean mAllowSystemGeneratedContextualActions = true;

    @Deprecated
    public NotificationCompat$Builder(Context context) {
        Notification notification = new Notification();
        this.mNotification = notification;
        this.mContext = context;
        notification.when = System.currentTimeMillis();
        this.mNotification.audioStreamType = -1;
    }

    public static CharSequence limitCharSequenceLength(CharSequence charSequence) {
        if (charSequence == null) {
            return charSequence;
        }
        return charSequence.length() > 5120 ? charSequence.subSequence(0, 5120) : charSequence;
    }

    public NotificationCompat$Builder setStyle(NotificationCompat$Style notificationCompat$Style) {
        if (this.mStyle != notificationCompat$Style) {
            this.mStyle = notificationCompat$Style;
            if (notificationCompat$Style.mBuilder != this) {
                notificationCompat$Style.mBuilder = this;
                setStyle(notificationCompat$Style);
            }
        }
        return this;
    }
}
