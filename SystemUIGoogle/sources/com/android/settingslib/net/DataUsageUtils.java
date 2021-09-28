package com.android.settingslib.net;

import android.content.Context;
import android.net.NetworkTemplate;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.util.ArrayUtils;
import java.util.List;
/* loaded from: classes.dex */
public class DataUsageUtils {
    public static NetworkTemplate getMobileTemplate(Context context, int i) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        int subscriptionId = telephonyManager.getSubscriptionId();
        List<SubscriptionInfo> availableSubscriptionInfoList = ((SubscriptionManager) context.getSystemService(SubscriptionManager.class)).getAvailableSubscriptionInfoList();
        if (availableSubscriptionInfoList == null) {
            Log.i("DataUsageUtils", "Subscription is not inited: " + i);
            return getMobileTemplateForSubId(telephonyManager, subscriptionId);
        }
        for (SubscriptionInfo subscriptionInfo : availableSubscriptionInfoList) {
            if (subscriptionInfo != null && subscriptionInfo.getSubscriptionId() == i) {
                return getNormalizedMobileTemplate(telephonyManager, i);
            }
        }
        Log.i("DataUsageUtils", "Subscription is not active: " + i);
        return getMobileTemplateForSubId(telephonyManager, subscriptionId);
    }

    private static NetworkTemplate getNormalizedMobileTemplate(TelephonyManager telephonyManager, int i) {
        NetworkTemplate mobileTemplateForSubId = getMobileTemplateForSubId(telephonyManager, i);
        String[] mergedImsisFromGroup = telephonyManager.createForSubscriptionId(i).getMergedImsisFromGroup();
        if (!ArrayUtils.isEmpty(mergedImsisFromGroup)) {
            return NetworkTemplate.normalize(mobileTemplateForSubId, mergedImsisFromGroup);
        }
        Log.i("DataUsageUtils", "mergedSubscriberIds is null.");
        return mobileTemplateForSubId;
    }

    private static NetworkTemplate getMobileTemplateForSubId(TelephonyManager telephonyManager, int i) {
        String subscriberId = telephonyManager.getSubscriberId(i);
        if (subscriberId != null) {
            return NetworkTemplate.buildTemplateCarrierMetered(subscriberId);
        }
        return NetworkTemplate.buildTemplateMobileAll(subscriberId);
    }
}
