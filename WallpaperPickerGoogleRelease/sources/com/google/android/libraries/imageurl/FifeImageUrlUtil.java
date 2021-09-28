package com.google.android.libraries.imageurl;

import android.net.Uri;
import com.google.common.base.Preconditions;
import com.google.photos.base.BaseImageUrlUtil;
import com.google.photos.base.ImageUrlOptionsStringBuilder;
import com.google.photos.base.ThinBaseImageUrlUtil;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
public class FifeImageUrlUtil extends BaseImageUrlUtil<Uri> {

    /* loaded from: classes.dex */
    public static class AndroidUriWrapper implements BaseImageUrlUtil.UriWrapper<Uri> {
        public final Uri uri;

        public AndroidUriWrapper(Uri uri) {
            this.uri = uri;
        }

        public String getPath() {
            return this.uri.getPath();
        }

        public String toString() {
            return this.uri.toString();
        }
    }

    /* loaded from: classes.dex */
    public static class InvalidUrlException extends Exception {
        public InvalidUrlException(BaseImageUrlUtil.InvalidUrlException invalidUrlException, AnonymousClass1 r2) {
            super(invalidUrlException);
        }
    }

    /* loaded from: classes.dex */
    public static class Options extends ImageUrlOptionsStringBuilder {
    }

    public boolean isFifeHostedUri(Uri uri) {
        boolean z = true;
        Preconditions.checkArgument(true);
        String uri2 = uri.toString();
        Pattern pattern = ThinBaseImageUrlUtil.FIFE_HOSTED_IMAGE_URL_RE;
        if (uri2 == null) {
            z = false;
        }
        if (z) {
            return ThinBaseImageUrlUtil.FIFE_HOSTED_IMAGE_URL_RE.matcher(uri2).find();
        }
        throw new IllegalArgumentException();
    }

    public Uri mergeOptions(Options options, Uri uri) throws InvalidUrlException {
        try {
            return changeImageUrlOptions(options, new AndroidUriWrapper(uri), false, true);
        } catch (BaseImageUrlUtil.InvalidUrlException e) {
            throw new InvalidUrlException(e, null);
        }
    }
}
