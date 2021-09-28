package com.android.systemui.statusbar.tv.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
/* loaded from: classes.dex */
public class TvNotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private SparseArray<StatusBarNotification> mNotifications;

    public TvNotificationAdapter() {
        setHasStableIds(true);
    }

    /* Return type fixed from 'com.android.systemui.statusbar.tv.notifications.TvNotificationAdapter$TvNotificationViewHolder' to match base method */
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new TvNotificationViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.tv_notification_item, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        SparseArray<StatusBarNotification> sparseArray = this.mNotifications;
        if (sparseArray == null) {
            Log.e("TvNotificationAdapter", "Could not bind view holder because the notification is missing");
            return;
        }
        TvNotificationViewHolder tvNotificationViewHolder = (TvNotificationViewHolder) viewHolder;
        Notification notification = sparseArray.valueAt(i).getNotification();
        tvNotificationViewHolder.mTitle.setText(notification.extras.getString("android.title"));
        tvNotificationViewHolder.mDetails.setText(notification.extras.getString("android.text"));
        tvNotificationViewHolder.mPendingIntent = notification.contentIntent;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        SparseArray<StatusBarNotification> sparseArray = this.mNotifications;
        if (sparseArray == null) {
            return 0;
        }
        return sparseArray.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return (long) this.mNotifications.keyAt(i);
    }

    public void setNotifications(SparseArray<StatusBarNotification> sparseArray) {
        this.mNotifications = sparseArray;
        notifyDataSetChanged();
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class TvNotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView mDetails;
        PendingIntent mPendingIntent;
        final TextView mTitle;

        protected TvNotificationViewHolder(View view) {
            super(view);
            this.mTitle = (TextView) view.findViewById(R$id.tv_notification_title);
            this.mDetails = (TextView) view.findViewById(R$id.tv_notification_details);
            view.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            try {
                PendingIntent pendingIntent = this.mPendingIntent;
                if (pendingIntent != null) {
                    pendingIntent.send();
                }
            } catch (PendingIntent.CanceledException unused) {
                Log.d("TvNotificationAdapter", "Pending intent canceled for : " + this.mPendingIntent);
            }
        }
    }
}
