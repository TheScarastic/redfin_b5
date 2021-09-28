package com.bumptech.glide.load;

import java.io.IOException;
/* loaded from: classes.dex */
public final class HttpException extends IOException {
    private static final long serialVersionUID = 1;
    private final int statusCode;

    public HttpException(String str) {
        super(str, null);
        this.statusCode = -1;
    }

    public HttpException(String str, int i) {
        super(str, null);
        this.statusCode = i;
    }
}
