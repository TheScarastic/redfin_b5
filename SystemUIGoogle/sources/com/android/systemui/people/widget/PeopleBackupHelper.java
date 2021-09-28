package com.android.systemui.people.widget;

import android.app.backup.BackupDataInputStream;
import android.app.backup.BackupDataOutput;
import android.app.backup.SharedPreferencesBackupHelper;
import android.app.people.IPeopleManager;
import android.appwidget.AppWidgetManager;
import android.compat.annotation.UnsupportedAppUsage;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.people.PeopleBackupFollowUpJob;
import com.android.systemui.people.SharedPreferencesHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
/* loaded from: classes.dex */
public class PeopleBackupHelper extends SharedPreferencesBackupHelper {
    private final AppWidgetManager mAppWidgetManager;
    private final Context mContext;
    private final IPeopleManager mIPeopleManager;
    private final PackageManager mPackageManager;
    private final UserHandle mUserHandle;

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum SharedFileEntryType {
        UNKNOWN,
        WIDGET_ID,
        PEOPLE_TILE_KEY,
        CONTACT_URI
    }

    @Override // android.app.backup.SharedPreferencesBackupHelper, android.app.backup.BackupHelper
    @UnsupportedAppUsage
    public /* bridge */ /* synthetic */ void writeNewStateDescription(ParcelFileDescriptor parcelFileDescriptor) {
        super.writeNewStateDescription(parcelFileDescriptor);
    }

    public static List<String> getFilesToBackup() {
        return Collections.singletonList("shared_backup");
    }

    public PeopleBackupHelper(Context context, UserHandle userHandle, String[] strArr) {
        super(context, strArr);
        this.mContext = context;
        this.mUserHandle = userHandle;
        this.mPackageManager = context.getPackageManager();
        this.mIPeopleManager = IPeopleManager.Stub.asInterface(ServiceManager.getService("people"));
        this.mAppWidgetManager = AppWidgetManager.getInstance(context);
    }

    @VisibleForTesting
    public PeopleBackupHelper(Context context, UserHandle userHandle, String[] strArr, PackageManager packageManager, IPeopleManager iPeopleManager) {
        super(context, strArr);
        this.mContext = context;
        this.mUserHandle = userHandle;
        this.mPackageManager = packageManager;
        this.mIPeopleManager = iPeopleManager;
        this.mAppWidgetManager = AppWidgetManager.getInstance(context);
    }

    @Override // android.app.backup.SharedPreferencesBackupHelper, android.app.backup.BackupHelper
    public void performBackup(ParcelFileDescriptor parcelFileDescriptor, BackupDataOutput backupDataOutput, ParcelFileDescriptor parcelFileDescriptor2) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        if (!defaultSharedPreferences.getAll().isEmpty()) {
            SharedPreferences.Editor edit = this.mContext.getSharedPreferences("shared_backup", 0).edit();
            edit.clear();
            List<String> existingWidgetsForUser = getExistingWidgetsForUser(this.mUserHandle.getIdentifier());
            if (!existingWidgetsForUser.isEmpty()) {
                defaultSharedPreferences.getAll().entrySet().forEach(new Consumer(edit, existingWidgetsForUser) { // from class: com.android.systemui.people.widget.PeopleBackupHelper$$ExternalSyntheticLambda0
                    public final /* synthetic */ SharedPreferences.Editor f$1;
                    public final /* synthetic */ List f$2;

                    {
                        this.f$1 = r2;
                        this.f$2 = r3;
                    }

                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        PeopleBackupHelper.this.lambda$performBackup$0(this.f$1, this.f$2, (Map.Entry) obj);
                    }
                });
                edit.apply();
                super.performBackup(parcelFileDescriptor, backupDataOutput, parcelFileDescriptor2);
            }
        }
    }

    @Override // android.app.backup.SharedPreferencesBackupHelper, android.app.backup.BackupHelper
    public void restoreEntity(BackupDataInputStream backupDataInputStream) {
        super.restoreEntity(backupDataInputStream);
        boolean z = false;
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("shared_backup", 0);
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
        SharedPreferences.Editor edit2 = this.mContext.getSharedPreferences("shared_follow_up", 0).edit();
        for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
            if (!restoreKey(entry, edit, edit2, sharedPreferences)) {
                z = true;
            }
        }
        edit.apply();
        edit2.apply();
        SharedPreferencesHelper.clear(sharedPreferences);
        if (z) {
            PeopleBackupFollowUpJob.scheduleJob(this.mContext);
        }
        updateWidgets(this.mContext);
    }

    /* renamed from: backupKey */
    public void lambda$performBackup$0(Map.Entry<String, ?> entry, SharedPreferences.Editor editor, List<String> list) {
        String key = entry.getKey();
        if (!TextUtils.isEmpty(key)) {
            int i = AnonymousClass1.$SwitchMap$com$android$systemui$people$widget$PeopleBackupHelper$SharedFileEntryType[getEntryType(entry).ordinal()];
            if (i == 1) {
                backupWidgetIdKey(key, String.valueOf(entry.getValue()), editor, list);
            } else if (i == 2) {
                backupPeopleTileKey(key, (Set) entry.getValue(), editor, list);
            } else if (i != 3) {
                Log.w("PeopleBackupHelper", "Key not identified, skipping: " + key);
            } else {
                backupContactUriKey(key, (Set) entry.getValue(), editor);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.people.widget.PeopleBackupHelper$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$systemui$people$widget$PeopleBackupHelper$SharedFileEntryType;

        static {
            int[] iArr = new int[SharedFileEntryType.values().length];
            $SwitchMap$com$android$systemui$people$widget$PeopleBackupHelper$SharedFileEntryType = iArr;
            try {
                iArr[SharedFileEntryType.WIDGET_ID.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$systemui$people$widget$PeopleBackupHelper$SharedFileEntryType[SharedFileEntryType.PEOPLE_TILE_KEY.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$systemui$people$widget$PeopleBackupHelper$SharedFileEntryType[SharedFileEntryType.CONTACT_URI.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$systemui$people$widget$PeopleBackupHelper$SharedFileEntryType[SharedFileEntryType.UNKNOWN.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    boolean restoreKey(Map.Entry<String, ?> entry, SharedPreferences.Editor editor, SharedPreferences.Editor editor2, SharedPreferences sharedPreferences) {
        String key = entry.getKey();
        SharedFileEntryType entryType = getEntryType(entry);
        int i = sharedPreferences.getInt("add_user_id_to_uri_" + key, -1);
        int i2 = AnonymousClass1.$SwitchMap$com$android$systemui$people$widget$PeopleBackupHelper$SharedFileEntryType[entryType.ordinal()];
        if (i2 == 1) {
            restoreWidgetIdKey(key, String.valueOf(entry.getValue()), editor, i);
            return true;
        } else if (i2 == 2) {
            return restorePeopleTileKeyAndCorrespondingWidgetFile(key, (Set) entry.getValue(), editor, editor2);
        } else {
            if (i2 != 3) {
                Log.e("PeopleBackupHelper", "Key not identified, skipping:" + key);
                return true;
            }
            restoreContactUriKey(key, (Set) entry.getValue(), editor, i);
            return true;
        }
    }

    private void backupWidgetIdKey(String str, String str2, SharedPreferences.Editor editor, List<String> list) {
        if (list.contains(str)) {
            Uri parse = Uri.parse(str2);
            if (ContentProvider.uriHasUserId(parse)) {
                int userIdFromUri = ContentProvider.getUserIdFromUri(parse);
                editor.putInt("add_user_id_to_uri_" + str, userIdFromUri);
                parse = ContentProvider.getUriWithoutUserId(parse);
            }
            editor.putString(str, parse.toString());
        }
    }

    private void restoreWidgetIdKey(String str, String str2, SharedPreferences.Editor editor, int i) {
        Uri parse = Uri.parse(str2);
        if (i != -1) {
            parse = ContentProvider.createContentUriForUser(parse, UserHandle.of(i));
        }
        editor.putString(str, parse.toString());
    }

    private void backupPeopleTileKey(String str, Set<String> set, SharedPreferences.Editor editor, List<String> list) {
        PeopleTileKey fromString = PeopleTileKey.fromString(str);
        if (fromString.getUserId() == this.mUserHandle.getIdentifier()) {
            Set<String> set2 = (Set) set.stream().filter(new Predicate(list) { // from class: com.android.systemui.people.widget.PeopleBackupHelper$$ExternalSyntheticLambda1
                public final /* synthetic */ List f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return this.f$0.contains((String) obj);
                }
            }).collect(Collectors.toSet());
            if (!set2.isEmpty()) {
                fromString.setUserId(-1);
                editor.putStringSet(fromString.toString(), set2);
            }
        }
    }

    private boolean restorePeopleTileKeyAndCorrespondingWidgetFile(String str, Set<String> set, SharedPreferences.Editor editor, SharedPreferences.Editor editor2) {
        PeopleTileKey fromString = PeopleTileKey.fromString(str);
        if (fromString == null) {
            return true;
        }
        fromString.setUserId(this.mUserHandle.getIdentifier());
        if (!PeopleTileKey.isValid(fromString)) {
            return true;
        }
        boolean isReadyForRestore = isReadyForRestore(this.mIPeopleManager, this.mPackageManager, fromString);
        if (!isReadyForRestore) {
            editor2.putStringSet(fromString.toString(), set);
        }
        editor.putStringSet(fromString.toString(), set);
        restoreWidgetIdFiles(this.mContext, set, fromString);
        return isReadyForRestore;
    }

    private void backupContactUriKey(String str, Set<String> set, SharedPreferences.Editor editor) {
        Uri parse = Uri.parse(String.valueOf(str));
        if (ContentProvider.uriHasUserId(parse)) {
            int userIdFromUri = ContentProvider.getUserIdFromUri(parse);
            if (userIdFromUri == this.mUserHandle.getIdentifier()) {
                Uri uriWithoutUserId = ContentProvider.getUriWithoutUserId(parse);
                editor.putInt("add_user_id_to_uri_" + uriWithoutUserId.toString(), userIdFromUri);
                editor.putStringSet(uriWithoutUserId.toString(), set);
            }
        } else if (this.mUserHandle.isSystem()) {
            editor.putStringSet(parse.toString(), set);
        }
    }

    private void restoreContactUriKey(String str, Set<String> set, SharedPreferences.Editor editor, int i) {
        Uri parse = Uri.parse(str);
        if (i != -1) {
            parse = ContentProvider.createContentUriForUser(parse, UserHandle.of(i));
        }
        editor.putStringSet(parse.toString(), set);
    }

    public static void restoreWidgetIdFiles(Context context, Set<String> set, PeopleTileKey peopleTileKey) {
        for (String str : set) {
            SharedPreferencesHelper.setPeopleTileKey(context.getSharedPreferences(str, 0), peopleTileKey);
        }
    }

    private List<String> getExistingWidgetsForUser(int i) {
        ArrayList arrayList = new ArrayList();
        for (int i2 : this.mAppWidgetManager.getAppWidgetIds(new ComponentName(this.mContext, PeopleSpaceWidgetProvider.class))) {
            String valueOf = String.valueOf(i2);
            if (this.mContext.getSharedPreferences(valueOf, 0).getInt("user_id", -1) == i) {
                arrayList.add(valueOf);
            }
        }
        return arrayList;
    }

    public static boolean isReadyForRestore(IPeopleManager iPeopleManager, PackageManager packageManager, PeopleTileKey peopleTileKey) {
        if (!PeopleTileKey.isValid(peopleTileKey)) {
            return true;
        }
        try {
            packageManager.getPackageInfoAsUser(peopleTileKey.getPackageName(), 0, peopleTileKey.getUserId());
            return iPeopleManager.isConversation(peopleTileKey.getPackageName(), peopleTileKey.getUserId(), peopleTileKey.getShortcutId());
        } catch (PackageManager.NameNotFoundException | Exception unused) {
            return false;
        }
    }

    public static SharedFileEntryType getEntryType(Map.Entry<String, ?> entry) {
        String key = entry.getKey();
        if (key == null) {
            return SharedFileEntryType.UNKNOWN;
        }
        try {
            Integer.parseInt(key);
            try {
                try {
                    String str = (String) entry.getValue();
                    return SharedFileEntryType.WIDGET_ID;
                } catch (Exception unused) {
                    Log.w("PeopleBackupHelper", "Malformed value, skipping:" + entry.getValue());
                    return SharedFileEntryType.UNKNOWN;
                }
            } catch (Exception unused2) {
                Log.w("PeopleBackupHelper", "Malformed value, skipping:" + entry.getValue());
                return SharedFileEntryType.UNKNOWN;
            }
        } catch (NumberFormatException unused3) {
            Set set = (Set) entry.getValue();
            if (PeopleTileKey.fromString(key) != null) {
                return SharedFileEntryType.PEOPLE_TILE_KEY;
            }
            try {
                Uri.parse(key);
                return SharedFileEntryType.CONTACT_URI;
            } catch (Exception unused4) {
                return SharedFileEntryType.UNKNOWN;
            }
        }
    }

    public static void updateWidgets(Context context) {
        int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, PeopleSpaceWidgetProvider.class));
        if (appWidgetIds != null && appWidgetIds.length != 0) {
            Intent intent = new Intent(context, PeopleSpaceWidgetProvider.class);
            intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
            intent.putExtra("appWidgetIds", appWidgetIds);
            context.sendBroadcast(intent);
        }
    }
}
