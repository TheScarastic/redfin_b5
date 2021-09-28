package com.bumptech.glide.module;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
/* loaded from: classes.dex */
public abstract class AppGlideModule implements AppliesOptions, RegistersComponents {
    @Override // com.bumptech.glide.module.AppliesOptions
    public void applyOptions(Context context, GlideBuilder glideBuilder) {
    }

    @Override // com.bumptech.glide.module.RegistersComponents
    public /* bridge */ /* synthetic */ void registerComponents(Context context, Glide glide, Registry registry) {
    }
}
