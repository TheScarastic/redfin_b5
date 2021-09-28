package com.bumptech.glide.load.data;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import androidx.appcompat.R$dimen$$ExternalSyntheticOutline0;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.HttpException;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.ContentLengthInputStream;
import com.bumptech.glide.util.LogTime;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
public class HttpUrlFetcher implements DataFetcher<InputStream> {
    public static final HttpUrlConnectionFactory DEFAULT_CONNECTION_FACTORY = new DefaultHttpUrlConnectionFactory();
    public final HttpUrlConnectionFactory connectionFactory;
    public final GlideUrl glideUrl;
    public volatile boolean isCancelled;
    public InputStream stream;
    public final int timeout;
    public HttpURLConnection urlConnection;

    /* loaded from: classes.dex */
    public static class DefaultHttpUrlConnectionFactory implements HttpUrlConnectionFactory {
    }

    /* loaded from: classes.dex */
    public interface HttpUrlConnectionFactory {
    }

    public HttpUrlFetcher(GlideUrl glideUrl, int i, HttpUrlConnectionFactory httpUrlConnectionFactory) {
        this.glideUrl = glideUrl;
        this.timeout = i;
        this.connectionFactory = httpUrlConnectionFactory;
    }

    @Override // com.bumptech.glide.load.data.DataFetcher
    public void cancel() {
        this.isCancelled = true;
    }

    @Override // com.bumptech.glide.load.data.DataFetcher
    public void cleanup() {
        InputStream inputStream = this.stream;
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException unused) {
            }
        }
        HttpURLConnection httpURLConnection = this.urlConnection;
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
        this.urlConnection = null;
    }

    @Override // com.bumptech.glide.load.data.DataFetcher
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @Override // com.bumptech.glide.load.data.DataFetcher
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:23:0x0000 */
    /* JADX DEBUG: Multi-variable search result rejected for r10v0, resolved type: com.bumptech.glide.Priority */
    /* JADX DEBUG: Multi-variable search result rejected for r10v1, resolved type: java.lang.String */
    /* JADX DEBUG: Multi-variable search result rejected for r9v2, resolved type: java.lang.StringBuilder */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v3, types: [java.lang.String] */
    @Override // com.bumptech.glide.load.data.DataFetcher
    public void loadData(Priority priority, DataFetcher.DataCallback<? super InputStream> dataCallback) {
        long elapsedRealtimeNanos;
        StringBuilder sb;
        double d;
        try {
            priority = "Finished http url fetcher fetch in ";
            int i = LogTime.$r8$clinit;
            elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
            try {
                dataCallback.onDataReady(loadDataWithRedirects(this.glideUrl.toURL(), 0, null, this.glideUrl.headers.getHeaders()));
            } catch (IOException e) {
                if (Log.isLoggable("HttpUrlFetcher", 3)) {
                    Log.d("HttpUrlFetcher", "Failed to load data for url", e);
                }
                dataCallback.onLoadFailed(e);
                if (Log.isLoggable("HttpUrlFetcher", 2)) {
                    d = LogTime.getElapsedMillis(elapsedRealtimeNanos);
                    sb = new StringBuilder(59);
                } else {
                    return;
                }
            }
            if (Log.isLoggable("HttpUrlFetcher", 2)) {
                d = LogTime.getElapsedMillis(elapsedRealtimeNanos);
                sb = new StringBuilder(59);
                sb.append((String) priority);
                sb.append(d);
                Log.v("HttpUrlFetcher", sb.toString());
            }
        } catch (Throwable th) {
            if (Log.isLoggable("HttpUrlFetcher", 2)) {
                double elapsedMillis = LogTime.getElapsedMillis(elapsedRealtimeNanos);
                StringBuilder sb2 = new StringBuilder(59);
                sb2.append(priority);
                sb2.append(elapsedMillis);
                Log.v("HttpUrlFetcher", sb2.toString());
            }
            throw th;
        }
    }

    public final InputStream loadDataWithRedirects(URL url, int i, URL url2, Map<String, String> map) throws IOException {
        if (i < 5) {
            if (url2 != null) {
                try {
                    if (url.toURI().equals(url2.toURI())) {
                        throw new HttpException("In re-direct loop");
                    }
                } catch (URISyntaxException unused) {
                }
            }
            Objects.requireNonNull((DefaultHttpUrlConnectionFactory) this.connectionFactory);
            this.urlConnection = (HttpURLConnection) url.openConnection();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                this.urlConnection.addRequestProperty(entry.getKey(), entry.getValue());
            }
            this.urlConnection.setConnectTimeout(this.timeout);
            this.urlConnection.setReadTimeout(this.timeout);
            boolean z = false;
            this.urlConnection.setUseCaches(false);
            this.urlConnection.setDoInput(true);
            this.urlConnection.setInstanceFollowRedirects(false);
            this.urlConnection.connect();
            this.stream = this.urlConnection.getInputStream();
            if (this.isCancelled) {
                return null;
            }
            int responseCode = this.urlConnection.getResponseCode();
            int i2 = responseCode / 100;
            if (i2 == 2) {
                HttpURLConnection httpURLConnection = this.urlConnection;
                if (TextUtils.isEmpty(httpURLConnection.getContentEncoding())) {
                    this.stream = new ContentLengthInputStream(httpURLConnection.getInputStream(), (long) httpURLConnection.getContentLength());
                } else {
                    if (Log.isLoggable("HttpUrlFetcher", 3)) {
                        String valueOf = String.valueOf(httpURLConnection.getContentEncoding());
                        Log.d("HttpUrlFetcher", valueOf.length() != 0 ? "Got non empty content encoding: ".concat(valueOf) : new String("Got non empty content encoding: "));
                    }
                    this.stream = httpURLConnection.getInputStream();
                }
                return this.stream;
            }
            if (i2 == 3) {
                z = true;
            }
            if (z) {
                String headerField = this.urlConnection.getHeaderField("Location");
                if (!TextUtils.isEmpty(headerField)) {
                    URL url3 = new URL(url, headerField);
                    cleanup();
                    return loadDataWithRedirects(url3, i + 1, url, map);
                }
                throw new HttpException("Received empty or null redirect url");
            } else if (responseCode == -1) {
                throw new HttpException(R$dimen$$ExternalSyntheticOutline0.m(49, "Http request failed with status code: ", responseCode), responseCode);
            } else {
                throw new HttpException(this.urlConnection.getResponseMessage(), responseCode);
            }
        } else {
            throw new HttpException("Too many (> 5) redirects!");
        }
    }
}
