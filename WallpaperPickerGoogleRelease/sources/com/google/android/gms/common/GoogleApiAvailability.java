package com.google.android.gms.common;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import androidx.collection.SimpleArrayMap;
import androidx.core.app.NotificationCompat$Action;
import androidx.core.app.NotificationCompat$BigTextStyle;
import androidx.core.app.NotificationCompat$Builder;
import androidx.core.app.NotificationCompat$Style;
import androidx.core.app.NotificationCompatJellybean;
import androidx.core.app.Person;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.preference.R$layout;
import com.android.systemui.shared.R;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.internal.zzg;
import com.google.android.gms.common.internal.zzh;
import com.google.android.gms.common.util.zzi;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public class GoogleApiAvailability extends GoogleApiAvailabilityLight {
    public static final Object zza = new Object();
    public static final GoogleApiAvailability zzb = new GoogleApiAvailability();
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE = GoogleApiAvailabilityLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;

    @SuppressLint({"HandlerLeak"})
    /* loaded from: classes.dex */
    public class zza extends Handler {
        public final Context zza;

        public zza(Context context) {
            super(Looper.myLooper() == null ? Looper.getMainLooper() : Looper.myLooper());
            this.zza = context.getApplicationContext();
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            int i = message.what;
            if (i != 1) {
                StringBuilder sb = new StringBuilder(50);
                sb.append("Don't know how to handle this message: ");
                sb.append(i);
                Log.w("GoogleApiAvailability", sb.toString());
                return;
            }
            int isGooglePlayServicesAvailable = GoogleApiAvailability.this.isGooglePlayServicesAvailable(this.zza);
            if (GoogleApiAvailability.this.isUserResolvableError(isGooglePlayServicesAvailable)) {
                GoogleApiAvailability.this.showErrorNotification(this.zza, isGooglePlayServicesAvailable);
            }
        }
    }

    private final String zza() {
        synchronized (zza) {
        }
        return null;
    }

    @Override // com.google.android.gms.common.GoogleApiAvailabilityLight
    public Intent getErrorResolutionIntent(Context context, int i, String str) {
        return super.getErrorResolutionIntent(context, i, str);
    }

    @Override // com.google.android.gms.common.GoogleApiAvailabilityLight
    public PendingIntent getErrorResolutionPendingIntent(Context context, int i, int i2, String str) {
        return super.getErrorResolutionPendingIntent(context, i, i2, null);
    }

    public int isGooglePlayServicesAvailable(Context context) {
        return isGooglePlayServicesAvailable(context, GoogleApiAvailabilityLight.GOOGLE_PLAY_SERVICES_VERSION_CODE);
    }

    @Override // com.google.android.gms.common.GoogleApiAvailabilityLight
    public final boolean isUserResolvableError(int i) {
        return super.isUserResolvableError(i);
    }

    public boolean showErrorDialogFragment(Activity activity, int i, int i2, DialogInterface.OnCancelListener onCancelListener) {
        Dialog zza2 = zza(activity, i, new zzh(super.getErrorResolutionIntent(activity, i, "d"), activity, i2), onCancelListener);
        if (zza2 == null) {
            return false;
        }
        zza(activity, zza2, "GooglePlayServicesErrorDialog", onCancelListener);
        return true;
    }

    public void showErrorNotification(Context context, int i) {
        zza(context, i, super.getErrorResolutionPendingIntent(context, i, 0, "n"));
    }

    public static Dialog zza(Context context, int i, zzg zzg, DialogInterface.OnCancelListener onCancelListener) {
        String str;
        AlertDialog.Builder builder = null;
        if (i == 0) {
            return null;
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(16843529, typedValue, true);
        if ("Theme.Dialog.Alert".equals(context.getResources().getResourceEntryName(typedValue.resourceId))) {
            builder = new AlertDialog.Builder(context, 5);
        }
        if (builder == null) {
            builder = new AlertDialog.Builder(context);
        }
        builder.setMessage(zzf.zzc(context, i));
        if (onCancelListener != null) {
            builder.setOnCancelListener(onCancelListener);
        }
        Resources resources = context.getResources();
        if (i == 1) {
            str = resources.getString(R.string.common_google_play_services_install_button);
        } else if (i == 2) {
            str = resources.getString(R.string.common_google_play_services_update_button);
        } else if (i != 3) {
            str = resources.getString(17039370);
        } else {
            str = resources.getString(R.string.common_google_play_services_enable_button);
        }
        if (str != null) {
            builder.setPositiveButton(str, zzg);
        }
        String zza2 = zzf.zza(context, i);
        if (zza2 != null) {
            builder.setTitle(zza2);
        }
        return builder.create();
    }

    public static void zza(Activity activity, Dialog dialog, String str, DialogInterface.OnCancelListener onCancelListener) {
        if (activity instanceof FragmentActivity) {
            FragmentManager supportFragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
            SupportErrorDialogFragment supportErrorDialogFragment = new SupportErrorDialogFragment();
            R$layout.zza(dialog, "Cannot display null dialog");
            dialog.setOnCancelListener(null);
            dialog.setOnDismissListener(null);
            supportErrorDialogFragment.zza = dialog;
            if (onCancelListener != null) {
                supportErrorDialogFragment.zzb = onCancelListener;
            }
            supportErrorDialogFragment.show(supportFragmentManager, str);
            return;
        }
        android.app.FragmentManager fragmentManager = activity.getFragmentManager();
        ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
        R$layout.zza(dialog, "Cannot display null dialog");
        dialog.setOnCancelListener(null);
        dialog.setOnDismissListener(null);
        errorDialogFragment.zza = dialog;
        if (onCancelListener != null) {
            errorDialogFragment.zzb = onCancelListener;
        }
        errorDialogFragment.show(fragmentManager, str);
    }

    @TargetApi(26)
    public final String zza(Context context, NotificationManager notificationManager) {
        String zza2 = zza();
        if (zza2 == null) {
            zza2 = "com.google.android.gms.availability";
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(zza2);
            SimpleArrayMap<String, String> simpleArrayMap = zzf.zza;
            String string = context.getResources().getString(R.string.common_google_play_services_notification_channel_name);
            if (notificationChannel == null) {
                notificationManager.createNotificationChannel(new NotificationChannel(zza2, string, 4));
            } else if (!string.equals(notificationChannel.getName())) {
                notificationChannel.setName(string);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        return zza2;
    }

    @TargetApi(20)
    public final void zza(Context context, int i, PendingIntent pendingIntent) {
        String str;
        String str2;
        Notification notification;
        int i2;
        Bundle bundle;
        if (i == 18) {
            new zza(context).sendEmptyMessageDelayed(1, 120000);
        } else if (pendingIntent != null) {
            if (i == 6) {
                str = zzf.zza(context, "common_google_play_services_resolution_required_title");
            } else {
                str = zzf.zza(context, i);
            }
            if (str == null) {
                str = context.getResources().getString(R.string.common_google_play_services_notification_ticker);
            }
            if (i == 6) {
                str2 = zzf.zza(context, "common_google_play_services_resolution_required_text", zzf.zzb(context));
            } else {
                str2 = zzf.zzc(context, i);
            }
            Resources resources = context.getResources();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            boolean z = false;
            if (zzi.zzb(context)) {
                Notification.Builder addAction = new Notification.Builder(context).setSmallIcon(context.getApplicationInfo().icon).setPriority(2).setAutoCancel(true).setContentTitle(str).setStyle(new Notification.BigTextStyle().bigText(str2)).addAction(R.drawable.common_full_open_on_phone, resources.getString(R.string.common_open_on_phone), pendingIntent);
                addAction.setChannelId(zza(context, notificationManager));
                notification = addAction.build();
            } else {
                NotificationCompat$Builder notificationCompat$Builder = new NotificationCompat$Builder(context);
                notificationCompat$Builder.mNotification.icon = 17301642;
                notificationCompat$Builder.mNotification.tickerText = NotificationCompat$Builder.limitCharSequenceLength(resources.getString(R.string.common_google_play_services_notification_ticker));
                long currentTimeMillis = System.currentTimeMillis();
                Notification notification2 = notificationCompat$Builder.mNotification;
                notification2.when = currentTimeMillis;
                notification2.flags |= 16;
                notificationCompat$Builder.mContentIntent = pendingIntent;
                notificationCompat$Builder.mContentTitle = NotificationCompat$Builder.limitCharSequenceLength(str);
                notificationCompat$Builder.mContentText = NotificationCompat$Builder.limitCharSequenceLength(str2);
                notificationCompat$Builder.mLocalOnly = true;
                NotificationCompat$BigTextStyle notificationCompat$BigTextStyle = new NotificationCompat$Style() { // from class: androidx.core.app.NotificationCompat$BigTextStyle
                    public CharSequence mBigText;
                };
                notificationCompat$BigTextStyle.mBigText = NotificationCompat$Builder.limitCharSequenceLength(str2);
                if (notificationCompat$Builder.mStyle != notificationCompat$BigTextStyle) {
                    notificationCompat$Builder.mStyle = notificationCompat$BigTextStyle;
                    if (notificationCompat$BigTextStyle.mBuilder != notificationCompat$Builder) {
                        notificationCompat$BigTextStyle.mBuilder = notificationCompat$Builder;
                        notificationCompat$Builder.setStyle(notificationCompat$BigTextStyle);
                    }
                }
                notificationCompat$Builder.mChannelId = zza(context, notificationManager);
                new ArrayList();
                Bundle bundle2 = new Bundle();
                Notification.Builder builder = new Notification.Builder(notificationCompat$Builder.mContext, notificationCompat$Builder.mChannelId);
                Notification notification3 = notificationCompat$Builder.mNotification;
                builder.setWhen(notification3.when).setSmallIcon(notification3.icon, notification3.iconLevel).setContent(notification3.contentView).setTicker(notification3.tickerText, null).setVibrate(notification3.vibrate).setLights(notification3.ledARGB, notification3.ledOnMS, notification3.ledOffMS).setOngoing((notification3.flags & 2) != 0).setOnlyAlertOnce((notification3.flags & 8) != 0).setAutoCancel((notification3.flags & 16) != 0).setDefaults(notification3.defaults).setContentTitle(notificationCompat$Builder.mContentTitle).setContentText(notificationCompat$Builder.mContentText).setContentInfo(null).setContentIntent(notificationCompat$Builder.mContentIntent).setDeleteIntent(notification3.deleteIntent).setFullScreenIntent(null, (notification3.flags & 128) != 0).setLargeIcon((Bitmap) null).setNumber(0).setProgress(0, 0, false);
                builder.setSubText(null).setUsesChronometer(false).setPriority(0);
                Iterator<NotificationCompat$Action> it = notificationCompat$Builder.mActions.iterator();
                while (it.hasNext()) {
                    Objects.requireNonNull(it.next());
                    Notification.Action.Builder builder2 = new Notification.Action.Builder((Icon) null, (CharSequence) null, (PendingIntent) null);
                    Bundle bundle3 = new Bundle();
                    bundle3.putBoolean("android.support.allowGeneratedReplies", false);
                    builder2.setAllowGeneratedReplies(false);
                    bundle3.putInt("android.support.action.semanticAction", 0);
                    builder2.setSemanticAction(0);
                    builder2.setContextual(false);
                    bundle3.putBoolean("android.support.action.showsUserInterface", false);
                    builder2.addExtras(bundle3);
                    builder.addAction(builder2.build());
                }
                Bundle bundle4 = notificationCompat$Builder.mExtras;
                if (bundle4 != null) {
                    bundle2.putAll(bundle4);
                }
                builder.setShowWhen(notificationCompat$Builder.mShowWhen);
                builder.setLocalOnly(notificationCompat$Builder.mLocalOnly).setGroup(null).setGroupSummary(false).setSortKey(null);
                builder.setCategory(null).setColor(0).setVisibility(0).setPublicVersion(null).setSound(notification3.sound, notification3.audioAttributes);
                ArrayList<String> arrayList = notificationCompat$Builder.mPeople;
                if (arrayList != null && !arrayList.isEmpty()) {
                    for (String str3 : arrayList) {
                        builder.addPerson(str3);
                    }
                }
                if (notificationCompat$Builder.mInvisibleActions.size() > 0) {
                    if (notificationCompat$Builder.mExtras == null) {
                        notificationCompat$Builder.mExtras = new Bundle();
                    }
                    Bundle bundle5 = notificationCompat$Builder.mExtras.getBundle("android.car.EXTENSIONS");
                    if (bundle5 == null) {
                        bundle5 = new Bundle();
                    }
                    Bundle bundle6 = new Bundle(bundle5);
                    Bundle bundle7 = new Bundle();
                    int i3 = 0;
                    while (i3 < notificationCompat$Builder.mInvisibleActions.size()) {
                        String num = Integer.toString(i3);
                        Object obj = NotificationCompatJellybean.sExtrasLock;
                        Bundle bundle8 = new Bundle();
                        Objects.requireNonNull(notificationCompat$Builder.mInvisibleActions.get(i3));
                        int i4 = z ? 1 : 0;
                        int i5 = z ? 1 : 0;
                        int i6 = z ? 1 : 0;
                        bundle8.putInt("icon", i4);
                        bundle8.putCharSequence("title", null);
                        bundle8.putParcelable("actionIntent", null);
                        Bundle bundle9 = new Bundle();
                        bundle9.putBoolean("android.support.allowGeneratedReplies", z);
                        bundle8.putBundle("extras", bundle9);
                        bundle8.putParcelableArray("remoteInputs", NotificationCompatJellybean.toBundleArray(null));
                        bundle8.putBoolean("showsUserInterface", false);
                        bundle8.putInt("semanticAction", 0);
                        bundle7.putBundle(num, bundle8);
                        i3++;
                        z = false;
                    }
                    bundle5.putBundle("invisible_actions", bundle7);
                    bundle6.putBundle("invisible_actions", bundle7);
                    if (notificationCompat$Builder.mExtras == null) {
                        notificationCompat$Builder.mExtras = new Bundle();
                    }
                    notificationCompat$Builder.mExtras.putBundle("android.car.EXTENSIONS", bundle5);
                    bundle2.putBundle("android.car.EXTENSIONS", bundle6);
                }
                builder.setExtras(notificationCompat$Builder.mExtras).setRemoteInputHistory(null);
                builder.setBadgeIconType(0).setSettingsText(null).setShortcutId(null).setTimeoutAfter(0).setGroupAlertBehavior(0);
                if (!TextUtils.isEmpty(notificationCompat$Builder.mChannelId)) {
                    builder.setSound(null).setDefaults(0).setLights(0, 0, 0).setVibrate(null);
                }
                Iterator<Person> it2 = notificationCompat$Builder.mPersonList.iterator();
                while (it2.hasNext()) {
                    Objects.requireNonNull(it2.next());
                    builder.addPerson(new Person.Builder().setName(null).setIcon(null).setUri(null).setKey(null).setBot(false).setImportant(false).build());
                }
                builder.setAllowSystemGeneratedContextualActions(notificationCompat$Builder.mAllowSystemGeneratedContextualActions);
                builder.setBubbleMetadata(null);
                NotificationCompat$Style notificationCompat$Style = notificationCompat$Builder.mStyle;
                if (notificationCompat$Style != null) {
                    new Notification.BigTextStyle(builder).setBigContentTitle(null).bigText(((NotificationCompat$BigTextStyle) notificationCompat$Style).mBigText);
                }
                Notification build = builder.build();
                if (notificationCompat$Style != null) {
                    Objects.requireNonNull(notificationCompat$Builder.mStyle);
                }
                if (!(notificationCompat$Style == null || (bundle = build.extras) == null)) {
                    bundle.putString("androidx.core.app.extra.COMPAT_TEMPLATE", "androidx.core.app.NotificationCompat$BigTextStyle");
                }
                notification = build;
            }
            if (i == 1 || i == 2 || i == 3) {
                i2 = 10436;
                GooglePlayServicesUtilLight.zza.set(false);
            } else {
                i2 = 39789;
            }
            notificationManager.notify(i2, notification);
        } else if (i == 6) {
            Log.w("GoogleApiAvailability", "Missing resolution for ConnectionResult.RESOLUTION_REQUIRED. Call GoogleApiAvailability#showErrorNotification(Context, ConnectionResult) instead.");
        }
    }
}
