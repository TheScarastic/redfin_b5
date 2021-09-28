package androidx.slice.core;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.SliceItem;
/* loaded from: classes.dex */
public class SliceActionImpl implements SliceAction {
    private PendingIntent mAction;
    private SliceItem mActionItem;
    private String mActionKey;
    private ActionType mActionType;
    private CharSequence mContentDescription;
    private long mDateTimeMillis;
    private IconCompat mIcon;
    private int mImageMode;
    private boolean mIsActivity;
    private boolean mIsChecked;
    private int mPriority;
    private SliceItem mSliceItem;
    private CharSequence mTitle;

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum ActionType {
        DEFAULT,
        TOGGLE,
        DATE_PICKER,
        TIME_PICKER
    }

    public SliceActionImpl(PendingIntent pendingIntent, IconCompat iconCompat, int i, CharSequence charSequence) {
        this.mImageMode = 5;
        this.mActionType = ActionType.DEFAULT;
        this.mPriority = -1;
        this.mDateTimeMillis = -1;
        this.mAction = pendingIntent;
        this.mIcon = iconCompat;
        this.mTitle = charSequence;
        this.mImageMode = i;
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    @SuppressLint({"InlinedApi"})
    public SliceActionImpl(SliceItem sliceItem) {
        char c;
        this.mImageMode = 5;
        ActionType actionType = ActionType.DEFAULT;
        this.mActionType = actionType;
        int i = -1;
        this.mPriority = -1;
        this.mDateTimeMillis = -1;
        this.mSliceItem = sliceItem;
        SliceItem find = SliceQuery.find(sliceItem, "action");
        if (find != null) {
            this.mActionItem = find;
            this.mAction = find.getAction();
            SliceItem find2 = SliceQuery.find(find.getSlice(), "image");
            if (find2 != null) {
                this.mIcon = find2.getIcon();
                this.mImageMode = parseImageMode(find2);
            }
            SliceItem find3 = SliceQuery.find(find.getSlice(), "text", "title", (String) null);
            if (find3 != null) {
                this.mTitle = find3.getSanitizedText();
            }
            SliceItem findSubtype = SliceQuery.findSubtype(find.getSlice(), "text", "content_description");
            if (findSubtype != null) {
                this.mContentDescription = findSubtype.getText();
            }
            if (find.getSubType() != null) {
                String subType = find.getSubType();
                subType.hashCode();
                switch (subType.hashCode()) {
                    case -868304044:
                        if (subType.equals("toggle")) {
                            c = 0;
                            break;
                        }
                        c = 65535;
                        break;
                    case 759128640:
                        if (subType.equals("time_picker")) {
                            c = 1;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1250407999:
                        if (subType.equals("date_picker")) {
                            c = 2;
                            break;
                        }
                        c = 65535;
                        break;
                    default:
                        c = 65535;
                        break;
                }
                switch (c) {
                    case 0:
                        this.mActionType = ActionType.TOGGLE;
                        this.mIsChecked = find.hasHint("selected");
                        break;
                    case 1:
                        this.mActionType = ActionType.TIME_PICKER;
                        SliceItem findSubtype2 = SliceQuery.findSubtype(find, "long", "millis");
                        if (findSubtype2 != null) {
                            this.mDateTimeMillis = findSubtype2.getLong();
                            break;
                        }
                        break;
                    case 2:
                        this.mActionType = ActionType.DATE_PICKER;
                        SliceItem findSubtype3 = SliceQuery.findSubtype(find, "long", "millis");
                        if (findSubtype3 != null) {
                            this.mDateTimeMillis = findSubtype3.getLong();
                            break;
                        }
                        break;
                    default:
                        this.mActionType = actionType;
                        break;
                }
            } else {
                this.mActionType = actionType;
            }
            this.mIsActivity = this.mSliceItem.hasHint("activity");
            SliceItem findSubtype4 = SliceQuery.findSubtype(find.getSlice(), "int", "priority");
            this.mPriority = findSubtype4 != null ? findSubtype4.getInt() : i;
            SliceItem findSubtype5 = SliceQuery.findSubtype(find.getSlice(), "text", "action_key");
            if (findSubtype5 != null) {
                this.mActionKey = findSubtype5.getText().toString();
            }
        }
    }

    public PendingIntent getAction() {
        PendingIntent pendingIntent = this.mAction;
        return pendingIntent != null ? pendingIntent : this.mActionItem.getAction();
    }

    public SliceItem getActionItem() {
        return this.mActionItem;
    }

    @Override // androidx.slice.core.SliceAction
    public IconCompat getIcon() {
        return this.mIcon;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public CharSequence getContentDescription() {
        return this.mContentDescription;
    }

    @Override // androidx.slice.core.SliceAction
    public int getPriority() {
        return this.mPriority;
    }

    @Override // androidx.slice.core.SliceAction
    public boolean isToggle() {
        return this.mActionType == ActionType.TOGGLE;
    }

    public boolean isChecked() {
        return this.mIsChecked;
    }

    @Override // androidx.slice.core.SliceAction
    public int getImageMode() {
        return this.mImageMode;
    }

    public boolean isDefaultToggle() {
        return this.mActionType == ActionType.TOGGLE && this.mIcon == null;
    }

    public SliceItem getSliceItem() {
        return this.mSliceItem;
    }

    public Slice buildSlice(Slice.Builder builder) {
        return builder.addHints("shortcut").addAction(this.mAction, buildSliceContent(builder).build(), getSubtype()).build();
    }

    public Slice buildPrimaryActionSlice(Slice.Builder builder) {
        return buildSliceContent(builder).addHints("shortcut", "title").build();
    }

    private Slice.Builder buildSliceContent(Slice.Builder builder) {
        Slice.Builder builder2 = new Slice.Builder(builder);
        IconCompat iconCompat = this.mIcon;
        if (iconCompat != null) {
            int i = this.mImageMode;
            builder2.addIcon(iconCompat, (String) null, i == 6 ? new String[]{"show_label"} : i == 0 ? new String[0] : new String[]{"no_tint"});
        }
        CharSequence charSequence = this.mTitle;
        if (charSequence != null) {
            builder2.addText(charSequence, (String) null, "title");
        }
        CharSequence charSequence2 = this.mContentDescription;
        if (charSequence2 != null) {
            builder2.addText(charSequence2, "content_description", new String[0]);
        }
        long j = this.mDateTimeMillis;
        if (j != -1) {
            builder2.addLong(j, "millis", new String[0]);
        }
        if (this.mActionType == ActionType.TOGGLE && this.mIsChecked) {
            builder2.addHints("selected");
        }
        int i2 = this.mPriority;
        if (i2 != -1) {
            builder2.addInt(i2, "priority", new String[0]);
        }
        String str = this.mActionKey;
        if (str != null) {
            builder2.addText(str, "action_key", new String[0]);
        }
        if (this.mIsActivity) {
            builder.addHints("activity");
        }
        return builder2;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: androidx.slice.core.SliceActionImpl$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$androidx$slice$core$SliceActionImpl$ActionType;

        static {
            int[] iArr = new int[ActionType.values().length];
            $SwitchMap$androidx$slice$core$SliceActionImpl$ActionType = iArr;
            try {
                iArr[ActionType.TOGGLE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$androidx$slice$core$SliceActionImpl$ActionType[ActionType.DATE_PICKER.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$androidx$slice$core$SliceActionImpl$ActionType[ActionType.TIME_PICKER.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public String getSubtype() {
        int i = AnonymousClass1.$SwitchMap$androidx$slice$core$SliceActionImpl$ActionType[this.mActionType.ordinal()];
        if (i == 1) {
            return "toggle";
        }
        if (i == 2) {
            return "date_picker";
        }
        if (i != 3) {
            return null;
        }
        return "time_picker";
    }

    public void setActivity(boolean z) {
        this.mIsActivity = z;
    }

    public static int parseImageMode(SliceItem sliceItem) {
        if (sliceItem.hasHint("show_label")) {
            return 6;
        }
        if (!sliceItem.hasHint("no_tint")) {
            return 0;
        }
        return sliceItem.hasHint("raw") ? sliceItem.hasHint("large") ? 4 : 3 : sliceItem.hasHint("large") ? 2 : 1;
    }
}
