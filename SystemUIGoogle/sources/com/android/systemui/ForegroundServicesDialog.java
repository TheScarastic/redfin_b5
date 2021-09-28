package com.android.systemui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.IconDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.internal.app.AlertActivity;
import com.android.internal.app.AlertController;
import com.android.internal.logging.MetricsLogger;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class ForegroundServicesDialog extends AlertActivity implements AdapterView.OnItemSelectedListener, DialogInterface.OnClickListener, AlertController.AlertParams.OnPrepareListViewListener {
    private PackageItemAdapter mAdapter;
    private DialogInterface.OnClickListener mAppClickListener = new DialogInterface.OnClickListener() { // from class: com.android.systemui.ForegroundServicesDialog.1
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            String str = ForegroundServicesDialog.this.mAdapter.getItem(i).packageName;
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", str, null));
            ForegroundServicesDialog.this.startActivity(intent);
            ForegroundServicesDialog.this.finish();
        }
    };
    LayoutInflater mInflater;
    private MetricsLogger mMetricsLogger;
    private String[] mPackages;

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onItemSelected(AdapterView adapterView, View view, int i, long j) {
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onNothingSelected(AdapterView adapterView) {
    }

    public void onPrepareListView(ListView listView) {
    }

    /* JADX DEBUG: Multi-variable search result rejected for r3v0, resolved type: com.android.systemui.ForegroundServicesDialog */
    /* JADX WARN: Multi-variable type inference failed */
    protected void onCreate(Bundle bundle) {
        ForegroundServicesDialog.super.onCreate(bundle);
        this.mMetricsLogger = (MetricsLogger) Dependency.get(MetricsLogger.class);
        this.mInflater = LayoutInflater.from(this);
        PackageItemAdapter packageItemAdapter = new PackageItemAdapter(this);
        this.mAdapter = packageItemAdapter;
        AlertController.AlertParams alertParams = ((AlertActivity) this).mAlertParams;
        alertParams.mAdapter = packageItemAdapter;
        alertParams.mOnClickListener = this.mAppClickListener;
        alertParams.mCustomTitleView = this.mInflater.inflate(R$layout.foreground_service_title, (ViewGroup) null);
        alertParams.mIsSingleChoice = true;
        alertParams.mOnItemSelectedListener = this;
        alertParams.mPositiveButtonText = getString(17040115);
        alertParams.mPositiveButtonListener = this;
        alertParams.mOnPrepareListViewListener = this;
        updateApps(getIntent());
        if (this.mPackages == null) {
            Log.w("ForegroundServicesDialog", "No packages supplied");
            finish();
            return;
        }
        setupAlert();
    }

    protected void onResume() {
        ForegroundServicesDialog.super.onResume();
        this.mMetricsLogger.visible(944);
    }

    protected void onPause() {
        ForegroundServicesDialog.super.onPause();
        this.mMetricsLogger.hidden(944);
    }

    protected void onNewIntent(Intent intent) {
        ForegroundServicesDialog.super.onNewIntent(intent);
        updateApps(intent);
    }

    protected void onStop() {
        ForegroundServicesDialog.super.onStop();
        if (!isChangingConfigurations()) {
            finish();
        }
    }

    void updateApps(Intent intent) {
        String[] stringArrayExtra = intent.getStringArrayExtra("packages");
        this.mPackages = stringArrayExtra;
        if (stringArrayExtra != null) {
            this.mAdapter.setPackages(stringArrayExtra);
        }
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i) {
        finish();
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class PackageItemAdapter extends ArrayAdapter<ApplicationInfo> {
        final IconDrawableFactory mIconDrawableFactory;
        final LayoutInflater mInflater;
        final PackageManager mPm;

        public PackageItemAdapter(Context context) {
            super(context, R$layout.foreground_service_item);
            this.mPm = context.getPackageManager();
            this.mInflater = LayoutInflater.from(context);
            this.mIconDrawableFactory = IconDrawableFactory.newInstance(context, true);
        }

        public void setPackages(String[] strArr) {
            clear();
            ArrayList arrayList = new ArrayList();
            for (String str : strArr) {
                try {
                    arrayList.add(this.mPm.getApplicationInfo(str, 4202496));
                } catch (PackageManager.NameNotFoundException unused) {
                }
            }
            arrayList.sort(new ApplicationInfo.DisplayNameComparator(this.mPm));
            addAll(arrayList);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = this.mInflater.inflate(R$layout.foreground_service_item, viewGroup, false);
            }
            ((ImageView) view.findViewById(R$id.app_icon)).setImageDrawable(this.mIconDrawableFactory.getBadgedIcon(getItem(i)));
            ((TextView) view.findViewById(R$id.app_name)).setText(getItem(i).loadLabel(this.mPm));
            return view;
        }
    }
}
