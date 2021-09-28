package com.google.android.systemui.smartspace;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import com.android.systemui.R$dimen;
import com.android.systemui.R$plurals;
import com.android.systemui.R$string;
import com.android.systemui.smartspace.nano.SmartspaceProto$CardWrapper;
import com.android.systemui.smartspace.nano.SmartspaceProto$SmartspaceUpdate;
/* loaded from: classes2.dex */
public class SmartSpaceCard {
    private static int sRequestCode;
    private final SmartspaceProto$SmartspaceUpdate.SmartspaceCard mCard;
    private final Context mContext;
    private Bitmap mIcon;
    private boolean mIconProcessed;
    private final Intent mIntent;
    private final boolean mIsIconGrayscale;
    private final boolean mIsWeather;
    private final long mPublishTime;
    private int mRequestCode;

    public SmartSpaceCard(Context context, SmartspaceProto$SmartspaceUpdate.SmartspaceCard smartspaceCard, Intent intent, boolean z, Bitmap bitmap, boolean z2, long j) {
        this.mContext = context.getApplicationContext();
        this.mCard = smartspaceCard;
        this.mIsWeather = z;
        this.mIntent = intent;
        this.mIcon = bitmap;
        this.mPublishTime = j;
        this.mIsIconGrayscale = z2;
        int i = sRequestCode + 1;
        sRequestCode = i;
        if (i > 2147483646) {
            sRequestCode = 0;
        }
        this.mRequestCode = sRequestCode;
    }

    public boolean isSensitive() {
        return this.mCard.isSensitive;
    }

    public boolean isWorkProfile() {
        return this.mCard.isWorkProfile;
    }

    public Intent getIntent() {
        return this.mIntent;
    }

    public Bitmap getIcon() {
        return this.mIcon;
    }

    public void setIcon(Bitmap bitmap) {
        this.mIcon = bitmap;
    }

    public void setIconProcessed(boolean z) {
        this.mIconProcessed = z;
    }

    public boolean isIconProcessed() {
        return this.mIconProcessed;
    }

    public String getTitle() {
        return substitute(true);
    }

    public CharSequence getFormattedTitle() {
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText formattedText;
        String str;
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText.FormatParam[] formatParamArr;
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message message = getMessage();
        if (message == null || (formattedText = message.title) == null || (str = formattedText.text) == null) {
            return "";
        }
        if (!hasParams(formattedText)) {
            return str;
        }
        String str2 = null;
        String str3 = null;
        int i = 0;
        while (true) {
            formatParamArr = formattedText.formatParam;
            if (i >= formatParamArr.length) {
                break;
            }
            SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText.FormatParam formatParam = formatParamArr[i];
            if (formatParam != null) {
                int i2 = formatParam.formatParamArgs;
                if (i2 == 1 || i2 == 2) {
                    str3 = getDurationText(formatParam);
                } else if (i2 == 3) {
                    str2 = formatParam.text;
                }
            }
            i++;
        }
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard smartspaceCard = this.mCard;
        if (smartspaceCard.cardType == 3 && formatParamArr.length == 2) {
            str3 = formatParamArr[0].text;
            str2 = formatParamArr[1].text;
        }
        if (str2 == null) {
            return "";
        }
        if (str3 == null) {
            if (message != smartspaceCard.duringEvent) {
                return str;
            }
            str3 = this.mContext.getString(R$string.smartspace_now);
        }
        return this.mContext.getString(R$string.smartspace_pill_text_format, str3, str2);
    }

    public String getSubtitle() {
        return substitute(false);
    }

    private SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message getMessage() {
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message message;
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message message2;
        long currentTimeMillis = System.currentTimeMillis();
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard smartspaceCard = this.mCard;
        long j = smartspaceCard.eventTimeMillis;
        long j2 = smartspaceCard.eventDurationMillis + j;
        if (currentTimeMillis < j && (message2 = smartspaceCard.preEvent) != null) {
            return message2;
        }
        if (currentTimeMillis > j2 && (message = smartspaceCard.postEvent) != null) {
            return message;
        }
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message message3 = smartspaceCard.duringEvent;
        if (message3 != null) {
            return message3;
        }
        return null;
    }

    private SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText getFormattedText(boolean z) {
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message message = getMessage();
        if (message != null) {
            return z ? message.title : message.subtitle;
        }
        return null;
    }

    private boolean hasParams(SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText formattedText) {
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText.FormatParam[] formatParamArr;
        return (formattedText == null || formattedText.text == null || (formatParamArr = formattedText.formatParam) == null || formatParamArr.length <= 0) ? false : true;
    }

    long getMillisToEvent(SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText.FormatParam formatParam) {
        long j;
        if (formatParam.formatParamArgs == 2) {
            SmartspaceProto$SmartspaceUpdate.SmartspaceCard smartspaceCard = this.mCard;
            j = smartspaceCard.eventTimeMillis + smartspaceCard.eventDurationMillis;
        } else {
            j = this.mCard.eventTimeMillis;
        }
        return Math.abs(System.currentTimeMillis() - j);
    }

    private int getMinutesToEvent(SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText.FormatParam formatParam) {
        return (int) Math.ceil(((double) getMillisToEvent(formatParam)) / 60000.0d);
    }

    private String substitute(boolean z) {
        return substitute(z, null);
    }

    private String[] getTextArgs(SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText.FormatParam[] formatParamArr, String str) {
        int length = formatParamArr.length;
        String[] strArr = new String[length];
        for (int i = 0; i < length; i++) {
            int i2 = formatParamArr[i].formatParamArgs;
            if (i2 == 1 || i2 == 2) {
                strArr[i] = getDurationText(formatParamArr[i]);
            } else {
                String str2 = "";
                if (i2 != 3) {
                    strArr[i] = str2;
                } else if (str == null || formatParamArr[i].truncateLocation == 0) {
                    if (formatParamArr[i].text != null) {
                        str2 = formatParamArr[i].text;
                    }
                    strArr[i] = str2;
                } else {
                    strArr[i] = str;
                }
            }
        }
        return strArr;
    }

    private String getDurationText(SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText.FormatParam formatParam) {
        int minutesToEvent = getMinutesToEvent(formatParam);
        if (minutesToEvent < 60) {
            return this.mContext.getResources().getQuantityString(R$plurals.smartspace_minutes, minutesToEvent, Integer.valueOf(minutesToEvent));
        }
        int i = minutesToEvent / 60;
        int i2 = minutesToEvent % 60;
        String quantityString = this.mContext.getResources().getQuantityString(R$plurals.smartspace_hours, i, Integer.valueOf(i));
        if (i2 <= 0) {
            return quantityString;
        }
        return this.mContext.getString(R$string.smartspace_hours_mins, quantityString, this.mContext.getResources().getQuantityString(R$plurals.smartspace_minutes, i2, Integer.valueOf(i2)));
    }

    private String substitute(boolean z, String str) {
        String str2;
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText formattedText = getFormattedText(z);
        if (formattedText == null || (str2 = formattedText.text) == null) {
            return "";
        }
        return hasParams(formattedText) ? String.format(str2, getTextArgs(formattedText.formatParam, str)) : str2;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > getExpiration();
    }

    public long getExpiration() {
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.ExpiryCriteria expiryCriteria;
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard smartspaceCard = this.mCard;
        if (smartspaceCard == null || (expiryCriteria = smartspaceCard.expiryCriteria) == null) {
            return 0;
        }
        return expiryCriteria.expirationTimeMillis;
    }

    public String toString() {
        return "title:" + getTitle() + " subtitle:" + getSubtitle() + " expires:" + getExpiration() + " published:" + this.mPublishTime;
    }

    /* access modifiers changed from: package-private */
    public static SmartSpaceCard fromWrapper(Context context, SmartspaceProto$CardWrapper smartspaceProto$CardWrapper, boolean z) {
        if (smartspaceProto$CardWrapper == null) {
            return null;
        }
        try {
            SmartspaceProto$SmartspaceUpdate.SmartspaceCard.TapAction tapAction = smartspaceProto$CardWrapper.card.tapAction;
            Intent parseUri = (tapAction == null || TextUtils.isEmpty(tapAction.intent)) ? null : Intent.parseUri(smartspaceProto$CardWrapper.card.tapAction.intent, 0);
            byte[] bArr = smartspaceProto$CardWrapper.icon;
            Bitmap decodeByteArray = bArr != null ? BitmapFactory.decodeByteArray(bArr, 0, bArr.length, null) : null;
            int dimensionPixelSize = context.getResources().getDimensionPixelSize(R$dimen.header_icon_size);
            if (decodeByteArray != null && decodeByteArray.getHeight() > dimensionPixelSize) {
                decodeByteArray = Bitmap.createScaledBitmap(decodeByteArray, (int) (((float) decodeByteArray.getWidth()) * (((float) dimensionPixelSize) / ((float) decodeByteArray.getHeight()))), dimensionPixelSize, true);
            }
            return new SmartSpaceCard(context, smartspaceProto$CardWrapper.card, parseUri, z, decodeByteArray, smartspaceProto$CardWrapper.isIconGrayscale, smartspaceProto$CardWrapper.publishTime);
        } catch (Exception e) {
            Log.e("SmartspaceCard", "from proto", e);
            return null;
        }
    }

    public PendingIntent getPendingIntent() {
        if (this.mCard.tapAction == null) {
            return null;
        }
        Intent intent = new Intent(getIntent());
        int i = this.mCard.tapAction.actionType;
        if (i == 1) {
            intent.addFlags(268435456);
            intent.setPackage("com.google.android.googlequicksearchbox");
            return PendingIntent.getBroadcast(this.mContext, this.mRequestCode, intent, 0);
        } else if (i != 2) {
            return null;
        } else {
            return PendingIntent.getActivity(this.mContext, this.mRequestCode, intent, 67108864);
        }
    }
}
