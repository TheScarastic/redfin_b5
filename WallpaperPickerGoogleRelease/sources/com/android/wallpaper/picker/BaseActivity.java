package com.android.wallpaper.picker;

import androidx.appcompat.app.AppCompatActivity;
/* loaded from: classes.dex */
public class BaseActivity extends AppCompatActivity implements FragmentTransactionChecker {
    public boolean mIsSafeToCommitFragmentTransaction;

    @Override // com.android.wallpaper.picker.FragmentTransactionChecker
    public boolean isSafeToCommitFragmentTransaction() {
        return this.mIsSafeToCommitFragmentTransaction;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        this.mIsSafeToCommitFragmentTransaction = false;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.mIsSafeToCommitFragmentTransaction = true;
    }
}
