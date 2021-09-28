package com.google.android.systemui.columbus;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ContentResolverWrapper.kt */
/* loaded from: classes2.dex */
public class ContentResolverWrapper {
    private final ContentResolver contentResolver;

    public ContentResolverWrapper(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.contentResolver = context.getContentResolver();
    }

    public void registerContentObserver(Uri uri, boolean z, ContentObserver contentObserver, int i) {
        Intrinsics.checkNotNullParameter(uri, "uri");
        Intrinsics.checkNotNullParameter(contentObserver, "observer");
        this.contentResolver.registerContentObserver(uri, z, contentObserver, i);
    }

    public void unregisterContentObserver(ContentObserver contentObserver) {
        Intrinsics.checkNotNullParameter(contentObserver, "observer");
        this.contentResolver.unregisterContentObserver(contentObserver);
    }
}
