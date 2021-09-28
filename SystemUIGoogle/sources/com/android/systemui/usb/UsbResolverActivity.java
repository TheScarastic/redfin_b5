package com.android.systemui.usb;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.hardware.usb.IUsbManager;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.util.Log;
import android.widget.CheckBox;
import com.android.internal.app.IntentForwarderActivity;
import com.android.internal.app.ResolverActivity;
import com.android.internal.app.chooser.TargetInfo;
import com.android.systemui.R$string;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes2.dex */
public class UsbResolverActivity extends ResolverActivity {
    private UsbAccessory mAccessory;
    private UsbDevice mDevice;
    private UsbDisconnectedReceiver mDisconnectedReceiver;
    private ResolveInfo mForwardResolveInfo;
    private Intent mOtherProfileIntent;

    protected boolean shouldShowTabs() {
        return false;
    }

    /* JADX DEBUG: Multi-variable search result rejected for r11v0, resolved type: com.android.systemui.usb.UsbResolverActivity */
    /* JADX WARN: Multi-variable type inference failed */
    protected void onCreate(Bundle bundle) {
        boolean z;
        Intent intent = getIntent();
        Parcelable parcelableExtra = intent.getParcelableExtra("android.intent.extra.INTENT");
        if (!(parcelableExtra instanceof Intent)) {
            Log.w("UsbResolverActivity", "Target is not an intent: " + parcelableExtra);
            finish();
            return;
        }
        Intent intent2 = (Intent) parcelableExtra;
        ArrayList arrayList = new ArrayList(intent.getParcelableArrayListExtra("rlist"));
        ArrayList<? extends Parcelable> arrayList2 = new ArrayList<>();
        this.mForwardResolveInfo = null;
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ResolveInfo resolveInfo = (ResolveInfo) it.next();
            if (resolveInfo.getComponentInfo().name.equals(IntentForwarderActivity.FORWARD_INTENT_TO_MANAGED_PROFILE)) {
                this.mForwardResolveInfo = resolveInfo;
            } else if (UserHandle.getUserId(resolveInfo.activityInfo.applicationInfo.uid) != UserHandle.myUserId()) {
                it.remove();
                arrayList2.add(resolveInfo);
            }
        }
        UsbDevice usbDevice = (UsbDevice) intent2.getParcelableExtra("device");
        this.mDevice = usbDevice;
        if (usbDevice != null) {
            this.mDisconnectedReceiver = new UsbDisconnectedReceiver((Activity) this, this.mDevice);
            z = this.mDevice.getHasAudioCapture();
        } else {
            UsbAccessory usbAccessory = (UsbAccessory) intent2.getParcelableExtra("accessory");
            this.mAccessory = usbAccessory;
            if (usbAccessory == null) {
                Log.e("UsbResolverActivity", "no device or accessory");
                finish();
                return;
            }
            this.mDisconnectedReceiver = new UsbDisconnectedReceiver((Activity) this, this.mAccessory);
            z = false;
        }
        if (this.mForwardResolveInfo != null) {
            if (arrayList2.size() > 1) {
                Intent intent3 = new Intent(intent);
                this.mOtherProfileIntent = intent3;
                intent3.putParcelableArrayListExtra("rlist", arrayList2);
            } else {
                Intent intent4 = new Intent((Context) this, (Class<?>) UsbConfirmActivity.class);
                this.mOtherProfileIntent = intent4;
                intent4.putExtra("rinfo", (Parcelable) arrayList2.get(0));
                UsbDevice usbDevice2 = this.mDevice;
                if (usbDevice2 != null) {
                    this.mOtherProfileIntent.putExtra("device", usbDevice2);
                }
                UsbAccessory usbAccessory2 = this.mAccessory;
                if (usbAccessory2 != null) {
                    this.mOtherProfileIntent.putExtra("accessory", usbAccessory2);
                }
            }
        }
        getIntent().putExtra("is_audio_capture_device", z);
        UsbResolverActivity.super.onCreate(bundle, intent2, getResources().getText(17039838), (Intent[]) null, arrayList, true);
        CheckBox checkBox = (CheckBox) findViewById(16908753);
        if (checkBox == null) {
            return;
        }
        if (this.mDevice == null) {
            checkBox.setText(R$string.always_use_accessory);
        } else {
            checkBox.setText(R$string.always_use_device);
        }
    }

    protected void onDestroy() {
        UsbDisconnectedReceiver usbDisconnectedReceiver = this.mDisconnectedReceiver;
        if (usbDisconnectedReceiver != null) {
            unregisterReceiver(usbDisconnectedReceiver);
        }
        UsbResolverActivity.super.onDestroy();
    }

    /* JADX DEBUG: Multi-variable search result rejected for r8v0, resolved type: com.android.systemui.usb.UsbResolverActivity */
    /* JADX WARN: Multi-variable type inference failed */
    protected boolean onTargetSelected(TargetInfo targetInfo, boolean z) {
        ResolveInfo resolveInfo = targetInfo.getResolveInfo();
        ResolveInfo resolveInfo2 = this.mForwardResolveInfo;
        if (resolveInfo == resolveInfo2) {
            startActivityAsUser(this.mOtherProfileIntent, (Bundle) null, UserHandle.of(resolveInfo2.targetUserId));
            return true;
        }
        try {
            IUsbManager asInterface = IUsbManager.Stub.asInterface(ServiceManager.getService("usb"));
            int i = resolveInfo.activityInfo.applicationInfo.uid;
            int myUserId = UserHandle.myUserId();
            UsbDevice usbDevice = this.mDevice;
            if (usbDevice != null) {
                asInterface.grantDevicePermission(usbDevice, i);
                if (z) {
                    asInterface.setDevicePackage(this.mDevice, resolveInfo.activityInfo.packageName, myUserId);
                } else {
                    asInterface.setDevicePackage(this.mDevice, (String) null, myUserId);
                }
            } else {
                UsbAccessory usbAccessory = this.mAccessory;
                if (usbAccessory != null) {
                    asInterface.grantAccessoryPermission(usbAccessory, i);
                    if (z) {
                        asInterface.setAccessoryPackage(this.mAccessory, resolveInfo.activityInfo.packageName, myUserId);
                    } else {
                        asInterface.setAccessoryPackage(this.mAccessory, (String) null, myUserId);
                    }
                }
            }
            try {
                targetInfo.startAsUser(this, (Bundle) null, UserHandle.of(myUserId));
            } catch (ActivityNotFoundException e) {
                Log.e("UsbResolverActivity", "startActivity failed", e);
            }
        } catch (RemoteException e2) {
            Log.e("UsbResolverActivity", "onIntentSelected failed", e2);
        }
        return true;
    }
}
