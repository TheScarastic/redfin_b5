package com.android.systemui.tuner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.TypedValue;
import android.widget.EditText;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import com.android.systemui.Dependency;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.R$xml;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.tuner.TunerService;
import java.util.ArrayList;
@Deprecated
/* loaded from: classes2.dex */
public class NavBarTuner extends TunerPreferenceFragment {
    private static final int[][] ICONS = {new int[]{R$drawable.ic_qs_circle, R$string.tuner_circle}, new int[]{R$drawable.ic_add, R$string.tuner_plus}, new int[]{R$drawable.ic_remove, R$string.tuner_minus}, new int[]{R$drawable.ic_left, R$string.tuner_left}, new int[]{R$drawable.ic_right, R$string.tuner_right}, new int[]{R$drawable.ic_menu, R$string.tuner_menu}};
    private Handler mHandler;
    private final ArrayList<TunerService.Tunable> mTunables = new ArrayList<>();

    @Override // androidx.preference.PreferenceFragment, android.app.Fragment
    public void onCreate(Bundle bundle) {
        this.mHandler = new Handler();
        super.onCreate(bundle);
    }

    @Override // android.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override // androidx.preference.PreferenceFragment
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R$xml.nav_bar_tuner);
        bindLayout((ListPreference) findPreference("layout"));
        bindButton("sysui_nav_bar_left", "space", "left");
        bindButton("sysui_nav_bar_right", "menu_ime", "right");
    }

    @Override // android.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.mTunables.forEach(NavBarTuner$$ExternalSyntheticLambda9.INSTANCE);
    }

    public static /* synthetic */ void lambda$onDestroy$0(TunerService.Tunable tunable) {
        ((TunerService) Dependency.get(TunerService.class)).removeTunable(tunable);
    }

    private void addTunable(TunerService.Tunable tunable, String... strArr) {
        this.mTunables.add(tunable);
        ((TunerService) Dependency.get(TunerService.class)).addTunable(tunable, strArr);
    }

    private void bindLayout(ListPreference listPreference) {
        addTunable(new TunerService.Tunable(listPreference) { // from class: com.android.systemui.tuner.NavBarTuner$$ExternalSyntheticLambda4
            public final /* synthetic */ ListPreference f$1;

            {
                this.f$1 = r2;
            }

            @Override // com.android.systemui.tuner.TunerService.Tunable
            public final void onTuningChanged(String str, String str2) {
                NavBarTuner.this.lambda$bindLayout$2(this.f$1, str, str2);
            }
        }, "sysui_nav_bar");
        listPreference.setOnPreferenceChangeListener(NavBarTuner$$ExternalSyntheticLambda2.INSTANCE);
    }

    public /* synthetic */ void lambda$bindLayout$2(ListPreference listPreference, String str, String str2) {
        this.mHandler.post(new Runnable(str2, listPreference) { // from class: com.android.systemui.tuner.NavBarTuner$$ExternalSyntheticLambda8
            public final /* synthetic */ String f$0;
            public final /* synthetic */ ListPreference f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                NavBarTuner.lambda$bindLayout$1(this.f$0, this.f$1);
            }
        });
    }

    public static /* synthetic */ void lambda$bindLayout$1(String str, ListPreference listPreference) {
        if (str == null) {
            str = "default";
        }
        listPreference.setValue(str);
    }

    public static /* synthetic */ boolean lambda$bindLayout$3(Preference preference, Object obj) {
        String str = (String) obj;
        if ("default".equals(str)) {
            str = null;
        }
        ((TunerService) Dependency.get(TunerService.class)).setValue("sysui_nav_bar", str);
        return true;
    }

    private void bindButton(String str, String str2, String str3) {
        ListPreference listPreference = (ListPreference) findPreference("type_" + str3);
        Preference findPreference = findPreference("keycode_" + str3);
        ListPreference listPreference2 = (ListPreference) findPreference("icon_" + str3);
        setupIcons(listPreference2);
        addTunable(new TunerService.Tunable(str2, listPreference, listPreference2, findPreference) { // from class: com.android.systemui.tuner.NavBarTuner$$ExternalSyntheticLambda5
            public final /* synthetic */ String f$1;
            public final /* synthetic */ ListPreference f$2;
            public final /* synthetic */ ListPreference f$3;
            public final /* synthetic */ Preference f$4;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
            }

            @Override // com.android.systemui.tuner.TunerService.Tunable
            public final void onTuningChanged(String str4, String str5) {
                NavBarTuner.this.lambda$bindButton$5(this.f$1, this.f$2, this.f$3, this.f$4, str4, str5);
            }
        }, str);
        NavBarTuner$$ExternalSyntheticLambda1 navBarTuner$$ExternalSyntheticLambda1 = new Preference.OnPreferenceChangeListener(str, listPreference, findPreference, listPreference2) { // from class: com.android.systemui.tuner.NavBarTuner$$ExternalSyntheticLambda1
            public final /* synthetic */ String f$1;
            public final /* synthetic */ ListPreference f$2;
            public final /* synthetic */ Preference f$3;
            public final /* synthetic */ ListPreference f$4;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
            }

            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public final boolean onPreferenceChange(Preference preference, Object obj) {
                return NavBarTuner.$r8$lambda$G5CIyZLizH9qjkmLxTjh8OXaZc4(NavBarTuner.this, this.f$1, this.f$2, this.f$3, this.f$4, preference, obj);
            }
        };
        listPreference.setOnPreferenceChangeListener(navBarTuner$$ExternalSyntheticLambda1);
        listPreference2.setOnPreferenceChangeListener(navBarTuner$$ExternalSyntheticLambda1);
        findPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(findPreference, str, listPreference, listPreference2) { // from class: com.android.systemui.tuner.NavBarTuner$$ExternalSyntheticLambda3
            public final /* synthetic */ Preference f$1;
            public final /* synthetic */ String f$2;
            public final /* synthetic */ ListPreference f$3;
            public final /* synthetic */ ListPreference f$4;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
            }

            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public final boolean onPreferenceClick(Preference preference) {
                return NavBarTuner.m391$r8$lambda$FoCVNhUUpQFaJdtzR8TOEAw6BE(NavBarTuner.this, this.f$1, this.f$2, this.f$3, this.f$4, preference);
            }
        });
    }

    public /* synthetic */ void lambda$bindButton$5(String str, ListPreference listPreference, ListPreference listPreference2, Preference preference, String str2, String str3) {
        this.mHandler.post(new Runnable(str3, str, listPreference, listPreference2, preference) { // from class: com.android.systemui.tuner.NavBarTuner$$ExternalSyntheticLambda7
            public final /* synthetic */ String f$1;
            public final /* synthetic */ String f$2;
            public final /* synthetic */ ListPreference f$3;
            public final /* synthetic */ ListPreference f$4;
            public final /* synthetic */ Preference f$5;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
                this.f$5 = r6;
            }

            @Override // java.lang.Runnable
            public final void run() {
                NavBarTuner.this.lambda$bindButton$4(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
            }
        });
    }

    public /* synthetic */ void lambda$bindButton$4(String str, String str2, ListPreference listPreference, ListPreference listPreference2, Preference preference) {
        if (str == null) {
            str = str2;
        }
        String extractButton = NavigationBarInflaterView.extractButton(str);
        if (extractButton.startsWith("key")) {
            listPreference.setValue("key");
            String extractImage = NavigationBarInflaterView.extractImage(extractButton);
            int extractKeycode = NavigationBarInflaterView.extractKeycode(extractButton);
            listPreference2.setValue(extractImage);
            updateSummary(listPreference2);
            preference.setSummary(extractKeycode + "");
            preference.setVisible(true);
            listPreference2.setVisible(true);
            return;
        }
        listPreference.setValue(extractButton);
        preference.setVisible(false);
        listPreference2.setVisible(false);
    }

    public /* synthetic */ boolean lambda$bindButton$7(String str, ListPreference listPreference, Preference preference, ListPreference listPreference2, Preference preference2, Object obj) {
        this.mHandler.post(new Runnable(str, listPreference, preference, listPreference2) { // from class: com.android.systemui.tuner.NavBarTuner$$ExternalSyntheticLambda6
            public final /* synthetic */ String f$1;
            public final /* synthetic */ ListPreference f$2;
            public final /* synthetic */ Preference f$3;
            public final /* synthetic */ ListPreference f$4;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
            }

            @Override // java.lang.Runnable
            public final void run() {
                NavBarTuner.this.lambda$bindButton$6(this.f$1, this.f$2, this.f$3, this.f$4);
            }
        });
        return true;
    }

    public /* synthetic */ void lambda$bindButton$6(String str, ListPreference listPreference, Preference preference, ListPreference listPreference2) {
        setValue(str, listPreference, preference, listPreference2);
        updateSummary(listPreference2);
    }

    public /* synthetic */ boolean lambda$bindButton$9(Preference preference, String str, ListPreference listPreference, ListPreference listPreference2, Preference preference2) {
        EditText editText = new EditText(getContext());
        new AlertDialog.Builder(getContext()).setTitle(preference2.getTitle()).setView(editText).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).setPositiveButton(17039370, new DialogInterface.OnClickListener(editText, preference, str, listPreference, listPreference2) { // from class: com.android.systemui.tuner.NavBarTuner$$ExternalSyntheticLambda0
            public final /* synthetic */ EditText f$1;
            public final /* synthetic */ Preference f$2;
            public final /* synthetic */ String f$3;
            public final /* synthetic */ ListPreference f$4;
            public final /* synthetic */ ListPreference f$5;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
                this.f$5 = r6;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                NavBarTuner.$r8$lambda$h5ybktTfwBxA5KL8hLYKl1l8m1Y(NavBarTuner.this, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, dialogInterface, i);
            }
        }).show();
        return true;
    }

    public /* synthetic */ void lambda$bindButton$8(EditText editText, Preference preference, String str, ListPreference listPreference, ListPreference listPreference2, DialogInterface dialogInterface, int i) {
        int i2;
        try {
            i2 = Integer.parseInt(editText.getText().toString());
        } catch (Exception unused) {
            i2 = 66;
        }
        preference.setSummary(i2 + "");
        setValue(str, listPreference, preference, listPreference2);
    }

    private void updateSummary(ListPreference listPreference) {
        try {
            int applyDimension = (int) TypedValue.applyDimension(1, 14.0f, getContext().getResources().getDisplayMetrics());
            String str = listPreference.getValue().split("/")[0];
            int parseInt = Integer.parseInt(listPreference.getValue().split("/")[1]);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            Drawable loadDrawable = Icon.createWithResource(str, parseInt).loadDrawable(getContext());
            loadDrawable.setTint(-16777216);
            loadDrawable.setBounds(0, 0, applyDimension, applyDimension);
            spannableStringBuilder.append("  ", new ImageSpan(loadDrawable, 1), 0);
            spannableStringBuilder.append((CharSequence) " ");
            int i = 0;
            while (true) {
                int[][] iArr = ICONS;
                if (i < iArr.length) {
                    if (iArr[i][0] == parseInt) {
                        spannableStringBuilder.append((CharSequence) getString(iArr[i][1]));
                    }
                    i++;
                } else {
                    listPreference.setSummary(spannableStringBuilder);
                    return;
                }
            }
        } catch (Exception e) {
            Log.d("NavButton", "Problem with summary", e);
            listPreference.setSummary((CharSequence) null);
        }
    }

    private void setValue(String str, ListPreference listPreference, Preference preference, ListPreference listPreference2) {
        String value = listPreference.getValue();
        if ("key".equals(value)) {
            String value2 = listPreference2.getValue();
            int i = 66;
            try {
                i = Integer.parseInt(preference.getSummary().toString());
            } catch (Exception unused) {
            }
            value = value + "(" + i + ":" + value2 + ")";
        }
        ((TunerService) Dependency.get(TunerService.class)).setValue(str, value);
    }

    private void setupIcons(ListPreference listPreference) {
        int[][] iArr = ICONS;
        CharSequence[] charSequenceArr = new CharSequence[iArr.length];
        CharSequence[] charSequenceArr2 = new CharSequence[iArr.length];
        int applyDimension = (int) TypedValue.applyDimension(1, 14.0f, getContext().getResources().getDisplayMetrics());
        int i = 0;
        while (true) {
            int[][] iArr2 = ICONS;
            if (i < iArr2.length) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                Drawable loadDrawable = Icon.createWithResource(getContext().getPackageName(), iArr2[i][0]).loadDrawable(getContext());
                loadDrawable.setTint(-16777216);
                loadDrawable.setBounds(0, 0, applyDimension, applyDimension);
                spannableStringBuilder.append("  ", new ImageSpan(loadDrawable, 1), 0);
                spannableStringBuilder.append((CharSequence) " ");
                spannableStringBuilder.append((CharSequence) getString(iArr2[i][1]));
                charSequenceArr[i] = spannableStringBuilder;
                charSequenceArr2[i] = getContext().getPackageName() + "/" + iArr2[i][0];
                i++;
            } else {
                listPreference.setEntries(charSequenceArr);
                listPreference.setEntryValues(charSequenceArr2);
                return;
            }
        }
    }
}
