package com.bumptech.glide.load.data;

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes.dex */
public class StreamLocalUriFetcher extends LocalUriFetcher<InputStream> {
    public static final UriMatcher URI_MATCHER;

    static {
        UriMatcher uriMatcher = new UriMatcher(-1);
        URI_MATCHER = uriMatcher;
        uriMatcher.addURI("com.android.contacts", "contacts/lookup/*/#", 1);
        uriMatcher.addURI("com.android.contacts", "contacts/lookup/*", 1);
        uriMatcher.addURI("com.android.contacts", "contacts/#/photo", 2);
        uriMatcher.addURI("com.android.contacts", "contacts/#", 3);
        uriMatcher.addURI("com.android.contacts", "contacts/#/display_photo", 4);
        uriMatcher.addURI("com.android.contacts", "phone_lookup/*", 5);
    }

    public StreamLocalUriFetcher(ContentResolver contentResolver, Uri uri) {
        super(contentResolver, uri);
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
    @Override // com.bumptech.glide.load.data.LocalUriFetcher
    public void close(InputStream inputStream) throws IOException {
        inputStream.close();
    }

    @Override // com.bumptech.glide.load.data.DataFetcher
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    /* Return type fixed from 'java.lang.Object' to match base method */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0025 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0026  */
    @Override // com.bumptech.glide.load.data.LocalUriFetcher
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.io.InputStream loadResource(android.net.Uri r3, android.content.ContentResolver r4) throws java.io.FileNotFoundException {
        /*
            r2 = this;
            android.content.UriMatcher r2 = com.bumptech.glide.load.data.StreamLocalUriFetcher.URI_MATCHER
            int r2 = r2.match(r3)
            r0 = 1
            if (r2 == r0) goto L_0x0019
            r1 = 3
            if (r2 == r1) goto L_0x0014
            r1 = 5
            if (r2 == r1) goto L_0x0019
            java.io.InputStream r2 = r4.openInputStream(r3)
            goto L_0x0023
        L_0x0014:
            java.io.InputStream r2 = android.provider.ContactsContract.Contacts.openContactPhotoInputStream(r4, r3, r0)
            goto L_0x0023
        L_0x0019:
            android.net.Uri r2 = android.provider.ContactsContract.Contacts.lookupContact(r4, r3)
            if (r2 == 0) goto L_0x003c
            java.io.InputStream r2 = android.provider.ContactsContract.Contacts.openContactPhotoInputStream(r4, r2, r0)
        L_0x0023:
            if (r2 == 0) goto L_0x0026
            return r2
        L_0x0026:
            java.io.FileNotFoundException r2 = new java.io.FileNotFoundException
            java.lang.String r3 = java.lang.String.valueOf(r3)
            int r4 = r3.length()
            int r4 = r4 + 24
            java.lang.String r0 = "InputStream is null for "
            java.lang.String r3 = com.bumptech.glide.Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline0.m(r4, r0, r3)
            r2.<init>(r3)
            throw r2
        L_0x003c:
            java.io.FileNotFoundException r2 = new java.io.FileNotFoundException
            java.lang.String r3 = "Contact cannot be found"
            r2.<init>(r3)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.data.StreamLocalUriFetcher.loadResource(android.net.Uri, android.content.ContentResolver):java.lang.Object");
    }
}
