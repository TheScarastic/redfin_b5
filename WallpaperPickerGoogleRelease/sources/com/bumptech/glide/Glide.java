package com.bumptech.glide;

import android.app.Activity;
import android.content.ComponentCallbacks2;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.data.InputStreamRewinder;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPoolAdapter;
import com.bumptech.glide.load.engine.bitmap_recycle.LruArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.load.model.AssetUriLoader;
import com.bumptech.glide.load.model.ByteArrayLoader;
import com.bumptech.glide.load.model.ByteBufferEncoder;
import com.bumptech.glide.load.model.ByteBufferFileLoader;
import com.bumptech.glide.load.model.DataUrlLoader;
import com.bumptech.glide.load.model.FileLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.MediaStoreFileLoader;
import com.bumptech.glide.load.model.ResourceLoader;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.model.StringLoader;
import com.bumptech.glide.load.model.UnitModelLoader;
import com.bumptech.glide.load.model.UriLoader;
import com.bumptech.glide.load.model.UrlUriLoader;
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader;
import com.bumptech.glide.load.model.stream.HttpUriLoader;
import com.bumptech.glide.load.model.stream.MediaStoreImageThumbLoader;
import com.bumptech.glide.load.model.stream.MediaStoreVideoThumbLoader;
import com.bumptech.glide.load.model.stream.UrlLoader;
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableDecoder;
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableEncoder;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.bumptech.glide.load.resource.bitmap.ByteBufferBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.DefaultImageHeaderParser;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.bitmap.ExifInterfaceImageHeaderParser;
import com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.UnitBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.VideoDecoder;
import com.bumptech.glide.load.resource.bytes.ByteBufferRewinder;
import com.bumptech.glide.load.resource.drawable.ResourceDrawableDecoder;
import com.bumptech.glide.load.resource.drawable.UnitDrawableDecoder;
import com.bumptech.glide.load.resource.file.FileDecoder;
import com.bumptech.glide.load.resource.gif.ByteBufferGifDecoder;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawableEncoder;
import com.bumptech.glide.load.resource.gif.GifFrameResourceDecoder;
import com.bumptech.glide.load.resource.gif.StreamGifDecoder;
import com.bumptech.glide.load.resource.transcode.BitmapBytesTranscoder;
import com.bumptech.glide.load.resource.transcode.BitmapDrawableTranscoder;
import com.bumptech.glide.load.resource.transcode.DrawableBytesTranscoder;
import com.bumptech.glide.load.resource.transcode.GifDrawableBytesTranscoder;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.bumptech.glide.manager.DefaultConnectivityMonitorFactory;
import com.bumptech.glide.manager.RequestManagerRetriever;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.module.ManifestParser;
import com.bumptech.glide.provider.ImageHeaderParserRegistry;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.bumptech.glide.util.LruCache;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public class Glide implements ComponentCallbacks2 {
    public static volatile Glide glide;
    public static volatile boolean isInitializing;
    public final ArrayPool arrayPool;
    public final BitmapPool bitmapPool;
    public final ConnectivityMonitorFactory connectivityMonitorFactory;
    public final Engine engine;
    public final GlideContext glideContext;
    public final MemoryCache memoryCache;
    public final Registry registry;
    public final RequestManagerRetriever requestManagerRetriever;
    public final List<RequestManager> managers = new ArrayList();
    public MemoryCategory memoryCategory = MemoryCategory.NORMAL;

    public Glide(Context context, Engine engine, MemoryCache memoryCache, BitmapPool bitmapPool, ArrayPool arrayPool, RequestManagerRetriever requestManagerRetriever, ConnectivityMonitorFactory connectivityMonitorFactory, int i, RequestOptions requestOptions, Map<Class<?>, TransitionOptions<?, ?>> map, List<RequestListener<Object>> list) {
        this.engine = engine;
        this.bitmapPool = bitmapPool;
        this.arrayPool = arrayPool;
        this.memoryCache = memoryCache;
        this.requestManagerRetriever = requestManagerRetriever;
        this.connectivityMonitorFactory = connectivityMonitorFactory;
        DecodeFormat decodeFormat = (DecodeFormat) requestOptions.options.get(Downsampler.DECODE_FORMAT);
        new Handler(Looper.getMainLooper());
        Resources resources = context.getResources();
        Registry registry = new Registry();
        this.registry = registry;
        ExifInterfaceImageHeaderParser exifInterfaceImageHeaderParser = new ExifInterfaceImageHeaderParser();
        ImageHeaderParserRegistry imageHeaderParserRegistry = registry.imageHeaderParserRegistry;
        synchronized (imageHeaderParserRegistry) {
            imageHeaderParserRegistry.parsers.add(exifInterfaceImageHeaderParser);
        }
        DefaultImageHeaderParser defaultImageHeaderParser = new DefaultImageHeaderParser();
        ImageHeaderParserRegistry imageHeaderParserRegistry2 = registry.imageHeaderParserRegistry;
        synchronized (imageHeaderParserRegistry2) {
            imageHeaderParserRegistry2.parsers.add(defaultImageHeaderParser);
        }
        List<ImageHeaderParser> imageHeaderParsers = registry.getImageHeaderParsers();
        Downsampler downsampler = new Downsampler(imageHeaderParsers, resources.getDisplayMetrics(), bitmapPool, arrayPool);
        ByteBufferGifDecoder byteBufferGifDecoder = new ByteBufferGifDecoder(context, imageHeaderParsers, bitmapPool, arrayPool, ByteBufferGifDecoder.PARSER_POOL, ByteBufferGifDecoder.GIF_DECODER_FACTORY);
        VideoDecoder.ParcelFileDescriptorInitializer parcelFileDescriptorInitializer = new VideoDecoder.ParcelFileDescriptorInitializer();
        VideoDecoder.MediaMetadataRetrieverFactory mediaMetadataRetrieverFactory = VideoDecoder.DEFAULT_FACTORY;
        VideoDecoder videoDecoder = new VideoDecoder(bitmapPool, parcelFileDescriptorInitializer, mediaMetadataRetrieverFactory);
        ByteBufferBitmapDecoder byteBufferBitmapDecoder = new ByteBufferBitmapDecoder(downsampler);
        StreamBitmapDecoder streamBitmapDecoder = new StreamBitmapDecoder(downsampler, arrayPool);
        ResourceDrawableDecoder resourceDrawableDecoder = new ResourceDrawableDecoder(context);
        ResourceLoader.StreamFactory streamFactory = new ResourceLoader.StreamFactory(resources);
        ResourceLoader.UriFactory uriFactory = new ResourceLoader.UriFactory(resources);
        ResourceLoader.FileDescriptorFactory fileDescriptorFactory = new ResourceLoader.FileDescriptorFactory(resources);
        ResourceLoader.AssetFileDescriptorFactory assetFileDescriptorFactory = new ResourceLoader.AssetFileDescriptorFactory(resources);
        BitmapEncoder bitmapEncoder = new BitmapEncoder(arrayPool);
        BitmapBytesTranscoder bitmapBytesTranscoder = new BitmapBytesTranscoder();
        GifDrawableBytesTranscoder gifDrawableBytesTranscoder = new GifDrawableBytesTranscoder();
        ContentResolver contentResolver = context.getContentResolver();
        registry.append(ByteBuffer.class, new ByteBufferEncoder());
        registry.append(InputStream.class, new StreamEncoder(arrayPool));
        registry.append("Bitmap", ByteBuffer.class, Bitmap.class, byteBufferBitmapDecoder);
        registry.append("Bitmap", InputStream.class, Bitmap.class, streamBitmapDecoder);
        registry.append("Bitmap", ParcelFileDescriptor.class, Bitmap.class, videoDecoder);
        registry.append("Bitmap", AssetFileDescriptor.class, Bitmap.class, new VideoDecoder(bitmapPool, new VideoDecoder.AssetFileDescriptorInitializer(null), mediaMetadataRetrieverFactory));
        UnitModelLoader.Factory<?> factory = UnitModelLoader.Factory.FACTORY;
        registry.append(Bitmap.class, Bitmap.class, factory);
        registry.append("Bitmap", Bitmap.class, Bitmap.class, new UnitBitmapDecoder());
        registry.append(Bitmap.class, (ResourceEncoder) bitmapEncoder);
        registry.append("BitmapDrawable", ByteBuffer.class, BitmapDrawable.class, new BitmapDrawableDecoder(resources, byteBufferBitmapDecoder));
        registry.append("BitmapDrawable", InputStream.class, BitmapDrawable.class, new BitmapDrawableDecoder(resources, streamBitmapDecoder));
        registry.append("BitmapDrawable", ParcelFileDescriptor.class, BitmapDrawable.class, new BitmapDrawableDecoder(resources, videoDecoder));
        registry.append(BitmapDrawable.class, (ResourceEncoder) new BitmapDrawableEncoder(bitmapPool, bitmapEncoder));
        registry.append("Gif", InputStream.class, GifDrawable.class, new StreamGifDecoder(imageHeaderParsers, byteBufferGifDecoder, arrayPool));
        registry.append("Gif", ByteBuffer.class, GifDrawable.class, byteBufferGifDecoder);
        registry.append(GifDrawable.class, (ResourceEncoder) new GifDrawableEncoder());
        registry.append(GifDecoder.class, GifDecoder.class, factory);
        registry.append("Bitmap", GifDecoder.class, Bitmap.class, new GifFrameResourceDecoder(bitmapPool));
        registry.append("legacy_append", Uri.class, Drawable.class, resourceDrawableDecoder);
        registry.append("legacy_append", Uri.class, Bitmap.class, new BitmapDrawableDecoder(resourceDrawableDecoder, bitmapPool));
        registry.register(new ByteBufferRewinder.Factory());
        registry.append(File.class, ByteBuffer.class, new ByteBufferFileLoader.Factory());
        registry.append(File.class, InputStream.class, new FileLoader.StreamFactory());
        registry.append("legacy_append", File.class, File.class, new FileDecoder());
        registry.append(File.class, ParcelFileDescriptor.class, new FileLoader.FileDescriptorFactory());
        registry.append(File.class, File.class, factory);
        registry.register(new InputStreamRewinder.Factory(arrayPool));
        Class cls = Integer.TYPE;
        registry.append(cls, InputStream.class, streamFactory);
        registry.append(cls, ParcelFileDescriptor.class, fileDescriptorFactory);
        registry.append(Integer.class, InputStream.class, streamFactory);
        registry.append(Integer.class, ParcelFileDescriptor.class, fileDescriptorFactory);
        registry.append(Integer.class, Uri.class, uriFactory);
        registry.append(cls, AssetFileDescriptor.class, assetFileDescriptorFactory);
        registry.append(Integer.class, AssetFileDescriptor.class, assetFileDescriptorFactory);
        registry.append(cls, Uri.class, uriFactory);
        registry.append(String.class, InputStream.class, new DataUrlLoader.StreamFactory());
        registry.append(Uri.class, InputStream.class, new DataUrlLoader.StreamFactory());
        registry.append(String.class, InputStream.class, new StringLoader.StreamFactory());
        registry.append(String.class, ParcelFileDescriptor.class, new StringLoader.FileDescriptorFactory());
        registry.append(String.class, AssetFileDescriptor.class, new StringLoader.AssetFileDescriptorFactory());
        registry.append(Uri.class, InputStream.class, new HttpUriLoader.Factory());
        registry.append(Uri.class, InputStream.class, new AssetUriLoader.StreamFactory(context.getAssets()));
        registry.append(Uri.class, ParcelFileDescriptor.class, new AssetUriLoader.FileDescriptorFactory(context.getAssets()));
        registry.append(Uri.class, InputStream.class, new MediaStoreImageThumbLoader.Factory(context));
        registry.append(Uri.class, InputStream.class, new MediaStoreVideoThumbLoader.Factory(context));
        registry.append(Uri.class, InputStream.class, new UriLoader.StreamFactory(contentResolver));
        registry.append(Uri.class, ParcelFileDescriptor.class, new UriLoader.FileDescriptorFactory(contentResolver));
        registry.append(Uri.class, AssetFileDescriptor.class, new UriLoader.AssetFileDescriptorFactory(contentResolver));
        registry.append(Uri.class, InputStream.class, new UrlUriLoader.StreamFactory());
        registry.append(URL.class, InputStream.class, new UrlLoader.StreamFactory());
        registry.append(Uri.class, File.class, new MediaStoreFileLoader.Factory(context));
        registry.append(GlideUrl.class, InputStream.class, new HttpGlideUrlLoader.Factory());
        registry.append(byte[].class, ByteBuffer.class, new ByteArrayLoader.ByteBufferFactory());
        registry.append(byte[].class, InputStream.class, new ByteArrayLoader.StreamFactory());
        registry.append(Uri.class, Uri.class, factory);
        registry.append(Drawable.class, Drawable.class, factory);
        registry.append("legacy_append", Drawable.class, Drawable.class, new UnitDrawableDecoder());
        registry.register(Bitmap.class, BitmapDrawable.class, new BitmapDrawableTranscoder(resources));
        registry.register(Bitmap.class, byte[].class, bitmapBytesTranscoder);
        registry.register(Drawable.class, byte[].class, new DrawableBytesTranscoder(bitmapPool, bitmapBytesTranscoder, gifDrawableBytesTranscoder));
        registry.register(GifDrawable.class, byte[].class, gifDrawableBytesTranscoder);
        this.glideContext = new GlideContext(context, arrayPool, registry, new ImageViewTargetFactory(), requestOptions, map, list, engine, i);
    }

    public static Glide get(Context context) {
        if (glide == null) {
            synchronized (Glide.class) {
                if (glide == null) {
                    if (!isInitializing) {
                        isInitializing = true;
                        initializeGlide(context, new GlideBuilder());
                        isInitializing = false;
                    } else {
                        throw new IllegalStateException("You cannot call Glide.get() in registerComponents(), use the provided Glide instance instead");
                    }
                }
            }
        }
        return glide;
    }

    @Deprecated
    public static synchronized void init(Glide glide2) {
        synchronized (Glide.class) {
            if (glide != null) {
                tearDown();
            }
            glide = glide2;
        }
    }

    public static void initializeGlide(Context context, GlideBuilder glideBuilder) {
        GeneratedAppGlideModule generatedAppGlideModule;
        Context applicationContext = context.getApplicationContext();
        try {
            generatedAppGlideModule = (GeneratedAppGlideModule) Class.forName("com.bumptech.glide.GeneratedAppGlideModuleImpl").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (ClassNotFoundException unused) {
            if (Log.isLoggable("Glide", 5)) {
                Log.w("Glide", "Failed to find GeneratedAppGlideModule. You should include an annotationProcessor compile dependency on com.github.bumptech.glide:compiler in your application and a @GlideModule annotated AppGlideModule implementation or LibraryGlideModules will be silently ignored");
            }
            generatedAppGlideModule = null;
        } catch (IllegalAccessException e) {
            throwIncorrectGlideModule(e);
            throw null;
        } catch (InstantiationException e2) {
            throwIncorrectGlideModule(e2);
            throw null;
        } catch (NoSuchMethodException e3) {
            throwIncorrectGlideModule(e3);
            throw null;
        } catch (InvocationTargetException e4) {
            throwIncorrectGlideModule(e4);
            throw null;
        }
        Collections.emptyList();
        if (Log.isLoggable("ManifestParser", 3)) {
            Log.d("ManifestParser", "Loading Glide modules");
        }
        ArrayList<GlideModule> arrayList = new ArrayList();
        try {
            ApplicationInfo applicationInfo = applicationContext.getPackageManager().getApplicationInfo(applicationContext.getPackageName(), 128);
            if (applicationInfo.metaData != null) {
                if (Log.isLoggable("ManifestParser", 2)) {
                    String valueOf = String.valueOf(applicationInfo.metaData);
                    StringBuilder sb = new StringBuilder(valueOf.length() + 23);
                    sb.append("Got app info metadata: ");
                    sb.append(valueOf);
                    Log.v("ManifestParser", sb.toString());
                }
                for (String str : applicationInfo.metaData.keySet()) {
                    if ("GlideModule".equals(applicationInfo.metaData.get(str))) {
                        arrayList.add(ManifestParser.parseModule(str));
                        if (Log.isLoggable("ManifestParser", 3)) {
                            String valueOf2 = String.valueOf(str);
                            Log.d("ManifestParser", valueOf2.length() != 0 ? "Loaded Glide module: ".concat(valueOf2) : new String("Loaded Glide module: "));
                        }
                    }
                }
                if (Log.isLoggable("ManifestParser", 3)) {
                    Log.d("ManifestParser", "Finished loading Glide modules");
                }
            } else if (Log.isLoggable("ManifestParser", 3)) {
                Log.d("ManifestParser", "Got null app info metadata");
            }
            if (generatedAppGlideModule != null && !generatedAppGlideModule.getExcludedModuleClasses().isEmpty()) {
                Set<Class<?>> excludedModuleClasses = generatedAppGlideModule.getExcludedModuleClasses();
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    GlideModule glideModule = (GlideModule) it.next();
                    if (excludedModuleClasses.contains(glideModule.getClass())) {
                        if (Log.isLoggable("Glide", 3)) {
                            String valueOf3 = String.valueOf(glideModule);
                            StringBuilder sb2 = new StringBuilder(valueOf3.length() + 46);
                            sb2.append("AppGlideModule excludes manifest GlideModule: ");
                            sb2.append(valueOf3);
                            Log.d("Glide", sb2.toString());
                        }
                        it.remove();
                    }
                }
            }
            if (Log.isLoggable("Glide", 3)) {
                for (GlideModule glideModule2 : arrayList) {
                    String valueOf4 = String.valueOf(glideModule2.getClass());
                    StringBuilder sb3 = new StringBuilder(valueOf4.length() + 38);
                    sb3.append("Discovered GlideModule from manifest: ");
                    sb3.append(valueOf4);
                    Log.d("Glide", sb3.toString());
                }
            }
            glideBuilder.requestManagerFactory = null;
            for (GlideModule glideModule3 : arrayList) {
                glideModule3.applyOptions(applicationContext, glideBuilder);
            }
            if (glideBuilder.sourceExecutor == null) {
                int calculateBestThreadCount = GlideExecutor.calculateBestThreadCount();
                glideBuilder.sourceExecutor = new GlideExecutor(new ThreadPoolExecutor(calculateBestThreadCount, calculateBestThreadCount, 0, TimeUnit.MILLISECONDS, new PriorityBlockingQueue(), new GlideExecutor.DefaultThreadFactory("source", GlideExecutor.UncaughtThrowableStrategy.DEFAULT, false)));
            }
            if (glideBuilder.diskCacheExecutor == null) {
                glideBuilder.diskCacheExecutor = new GlideExecutor(new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new PriorityBlockingQueue(), new GlideExecutor.DefaultThreadFactory("disk-cache", GlideExecutor.UncaughtThrowableStrategy.DEFAULT, true)));
            }
            if (glideBuilder.animationExecutor == null) {
                glideBuilder.animationExecutor = GlideExecutor.newAnimationExecutor();
            }
            if (glideBuilder.memorySizeCalculator == null) {
                glideBuilder.memorySizeCalculator = new MemorySizeCalculator(new MemorySizeCalculator.Builder(applicationContext));
            }
            if (glideBuilder.connectivityMonitorFactory == null) {
                glideBuilder.connectivityMonitorFactory = new DefaultConnectivityMonitorFactory();
            }
            if (glideBuilder.bitmapPool == null) {
                int i = glideBuilder.memorySizeCalculator.bitmapPoolSize;
                if (i > 0) {
                    glideBuilder.bitmapPool = new LruBitmapPool((long) i);
                } else {
                    glideBuilder.bitmapPool = new BitmapPoolAdapter();
                }
            }
            if (glideBuilder.arrayPool == null) {
                glideBuilder.arrayPool = new LruArrayPool(glideBuilder.memorySizeCalculator.arrayPoolSize);
            }
            if (glideBuilder.memoryCache == null) {
                glideBuilder.memoryCache = new LruResourceCache((long) glideBuilder.memorySizeCalculator.memoryCacheSize);
            }
            if (glideBuilder.diskCacheFactory == null) {
                glideBuilder.diskCacheFactory = new InternalCacheDiskCacheFactory(applicationContext);
            }
            if (glideBuilder.engine == null) {
                glideBuilder.engine = new Engine(glideBuilder.memoryCache, glideBuilder.diskCacheFactory, glideBuilder.diskCacheExecutor, glideBuilder.sourceExecutor, new GlideExecutor(new ThreadPoolExecutor(0, Integer.MAX_VALUE, GlideExecutor.KEEP_ALIVE_TIME_MS, TimeUnit.MILLISECONDS, new SynchronousQueue(), new GlideExecutor.DefaultThreadFactory("source-unlimited", GlideExecutor.UncaughtThrowableStrategy.DEFAULT, false))), GlideExecutor.newAnimationExecutor(), null, null, null, null, null, null, false);
            }
            List<RequestListener<Object>> list = glideBuilder.defaultRequestListeners;
            if (list == null) {
                glideBuilder.defaultRequestListeners = Collections.emptyList();
            } else {
                glideBuilder.defaultRequestListeners = Collections.unmodifiableList(list);
            }
            RequestManagerRetriever requestManagerRetriever = new RequestManagerRetriever(glideBuilder.requestManagerFactory);
            Engine engine = glideBuilder.engine;
            MemoryCache memoryCache = glideBuilder.memoryCache;
            BitmapPool bitmapPool = glideBuilder.bitmapPool;
            ArrayPool arrayPool = glideBuilder.arrayPool;
            ConnectivityMonitorFactory connectivityMonitorFactory = glideBuilder.connectivityMonitorFactory;
            RequestOptions requestOptions = glideBuilder.defaultRequestOptions;
            requestOptions.isLocked = true;
            Glide glide2 = new Glide(applicationContext, engine, memoryCache, bitmapPool, arrayPool, requestManagerRetriever, connectivityMonitorFactory, 4, requestOptions, glideBuilder.defaultTransitionOptions, glideBuilder.defaultRequestListeners);
            for (GlideModule glideModule4 : arrayList) {
                glideModule4.registerComponents(applicationContext, glide2, glide2.registry);
            }
            applicationContext.registerComponentCallbacks(glide2);
            glide = glide2;
        } catch (PackageManager.NameNotFoundException e5) {
            throw new RuntimeException("Unable to find metadata to parse GlideModules", e5);
        }
    }

    public static synchronized void tearDown() {
        synchronized (Glide.class) {
            if (glide != null) {
                glide.getContext().getApplicationContext().unregisterComponentCallbacks(glide);
                glide.engine.shutdown();
            }
            glide = null;
        }
    }

    public static void throwIncorrectGlideModule(Exception exc) {
        throw new IllegalStateException("GeneratedAppGlideModuleImpl is implemented incorrectly. If you've manually implemented this class, remove your implementation. The Annotation processor will generate a correct implementation.", exc);
    }

    public static RequestManager with(Activity activity) {
        Objects.requireNonNull(activity, "You cannot start a load on a not yet attached View or a Fragment where getActivity() returns null (which usually occurs when getActivity() is called before the Fragment is attached or after the Fragment is destroyed).");
        return get(activity).requestManagerRetriever.get(activity);
    }

    public void clearMemory() {
        Util.assertMainThread();
        ((LruCache) this.memoryCache).trimToSize(0);
        this.bitmapPool.clearMemory();
        this.arrayPool.clearMemory();
    }

    public Context getContext() {
        return this.glideContext.getBaseContext();
    }

    @Override // android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
    }

    @Override // android.content.ComponentCallbacks
    public void onLowMemory() {
        clearMemory();
    }

    @Override // android.content.ComponentCallbacks2
    public void onTrimMemory(int i) {
        long j;
        Util.assertMainThread();
        LruResourceCache lruResourceCache = (LruResourceCache) this.memoryCache;
        Objects.requireNonNull(lruResourceCache);
        if (i >= 40) {
            lruResourceCache.trimToSize(0);
        } else if (i >= 20 || i == 15) {
            synchronized (lruResourceCache) {
                j = lruResourceCache.maxSize;
            }
            lruResourceCache.trimToSize(j / 2);
        }
        this.bitmapPool.trimMemory(i);
        this.arrayPool.trimMemory(i);
    }

    public MemoryCategory setMemoryCategory(MemoryCategory memoryCategory) {
        Util.assertMainThread();
        MemoryCache memoryCache = this.memoryCache;
        float multiplier = memoryCategory.getMultiplier();
        LruCache lruCache = (LruCache) memoryCache;
        synchronized (lruCache) {
            if (multiplier >= 0.0f) {
                long round = (long) Math.round(((float) lruCache.initialMaxSize) * multiplier);
                lruCache.maxSize = round;
                lruCache.trimToSize(round);
            } else {
                throw new IllegalArgumentException("Multiplier must be >= 0");
            }
        }
        this.bitmapPool.setSizeMultiplier(memoryCategory.getMultiplier());
        MemoryCategory memoryCategory2 = this.memoryCategory;
        this.memoryCategory = memoryCategory;
        return memoryCategory2;
    }

    public static synchronized void init(Context context, GlideBuilder glideBuilder) {
        synchronized (Glide.class) {
            if (glide != null) {
                tearDown();
            }
            initializeGlide(context, glideBuilder);
        }
    }

    public static RequestManager with(Context context) {
        Objects.requireNonNull(context, "You cannot start a load on a not yet attached View or a Fragment where getActivity() returns null (which usually occurs when getActivity() is called before the Fragment is attached or after the Fragment is destroyed).");
        return get(context).requestManagerRetriever.get(context);
    }
}
