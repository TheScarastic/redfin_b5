package com.google.android.gms.gcm;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import androidx.viewpager2.widget.FakeDrag;
import com.android.wallpaper.module.DefaultWallpaperRefresher;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.util.DiskBasedLogger;
import com.google.android.apps.wallpaper.backdrop.BackdropRotationTask;
import com.google.android.apps.wallpaper.backdrop.DefaultGcmNetworkManagerWrapper;
import com.google.android.gms.gcm.INetworkTaskCallback;
import com.google.android.gms.internal.zzbmj;
import com.google.android.gms.internal.zzbmk;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;
/* loaded from: classes.dex */
public abstract class GcmTaskService extends Service {
    public final Object zza = new Object();
    public int zzb;
    public ExecutorService zzc;
    public Messenger zzd;
    public ComponentName zze;
    public FakeDrag zzf;

    @TargetApi(21)
    /* loaded from: classes.dex */
    public class zza extends Handler {
        public zza(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            boolean z;
            Messenger messenger;
            GcmTaskService gcmTaskService = GcmTaskService.this;
            int i = message.sendingUid;
            zzbmj zza = zzbmk.zza(gcmTaskService);
            Objects.requireNonNull(zza);
            try {
                ((AppOpsManager) zza.zza.getSystemService("appops")).checkPackage(i, "com.google.android.gms");
                z = true;
            } catch (SecurityException unused) {
                z = false;
            }
            if (!z) {
                Log.e("GcmTaskService", "unable to verify presence of Google Play Services");
                return;
            }
            int i2 = message.what;
            if (i2 == 1) {
                Bundle data = message.getData();
                if (!data.isEmpty() && (messenger = message.replyTo) != null) {
                    String string = data.getString("tag");
                    ArrayList parcelableArrayList = data.getParcelableArrayList("triggered_uris");
                    if (!GcmTaskService.this.zza(string)) {
                        Bundle bundle = data.getBundle("extras");
                        GcmTaskService gcmTaskService2 = GcmTaskService.this;
                        zzb zzb = new zzb(string, messenger, bundle, parcelableArrayList);
                        Objects.requireNonNull(gcmTaskService2);
                        try {
                            gcmTaskService2.zzc.execute(zzb);
                        } catch (RejectedExecutionException e) {
                            Log.e("GcmTaskService", "Executor is shutdown. onDestroy was called but main looper had an unprocessed start task message. The task will be retried with backoff delay.", e);
                            zzb.zza(1);
                        }
                    }
                }
            } else if (i2 != 2) {
                if (i2 != 4) {
                    String valueOf = String.valueOf(message);
                    StringBuilder sb = new StringBuilder(valueOf.length() + 31);
                    sb.append("Unrecognized message received: ");
                    sb.append(valueOf);
                    Log.e("GcmTaskService", sb.toString());
                    return;
                }
                Objects.requireNonNull(GcmTaskService.this);
            } else if (Log.isLoggable("GcmTaskService", 3)) {
                String valueOf2 = String.valueOf(message);
                StringBuilder sb2 = new StringBuilder(valueOf2.length() + 45);
                sb2.append("ignoring unimplemented stop message for now: ");
                sb2.append(valueOf2);
                Log.d("GcmTaskService", sb2.toString());
            }
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        if (intent == null || !"com.google.android.gms.gcm.ACTION_TASK_READY".equals(intent.getAction())) {
            return null;
        }
        return this.zzd.getBinder();
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.zzf = FakeDrag.getInstance(this);
        this.zzc = Executors.newFixedThreadPool(2, new zzb());
        this.zzd = new Messenger(new zza(Looper.getMainLooper()));
        this.zze = new ComponentName(this, getClass());
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        List<Runnable> shutdownNow = this.zzc.shutdownNow();
        if (!shutdownNow.isEmpty()) {
            int size = shutdownNow.size();
            StringBuilder sb = new StringBuilder(79);
            sb.append("Shutting down, but not all tasks are finished executing. Remaining: ");
            sb.append(size);
            Log.e("GcmTaskService", sb.toString());
        }
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent == null) {
            return 2;
        }
        try {
            intent.setExtrasClassLoader(PendingCallback.class.getClassLoader());
            String action = intent.getAction();
            if ("com.google.android.gms.gcm.ACTION_TASK_READY".equals(action)) {
                String stringExtra = intent.getStringExtra("tag");
                Parcelable parcelableExtra = intent.getParcelableExtra("callback");
                Bundle bundleExtra = intent.getBundleExtra("extras");
                ArrayList parcelableArrayListExtra = intent.getParcelableArrayListExtra("triggered_uris");
                if (!(parcelableExtra instanceof PendingCallback)) {
                    String packageName = getPackageName();
                    StringBuilder sb = new StringBuilder(String.valueOf(packageName).length() + 47 + String.valueOf(stringExtra).length());
                    sb.append(packageName);
                    sb.append(" ");
                    sb.append(stringExtra);
                    sb.append(": Could not process request, invalid callback.");
                    Log.e("GcmTaskService", sb.toString());
                    return 2;
                } else if (zza(stringExtra)) {
                    return 2;
                } else {
                    zzb zzb2 = new zzb(stringExtra, ((PendingCallback) parcelableExtra).zza, bundleExtra, parcelableArrayListExtra);
                    try {
                        this.zzc.execute(zzb2);
                    } catch (RejectedExecutionException e) {
                        Log.e("GcmTaskService", "Executor is shutdown. onDestroy was called but main looper had an unprocessed start task message. The task will be retried with backoff delay.", e);
                        zzb2.zza(1);
                    }
                }
            } else if (!"com.google.android.gms.gcm.SERVICE_ACTION_INITIALIZE".equals(action)) {
                StringBuilder sb2 = new StringBuilder(String.valueOf(action).length() + 37);
                sb2.append("Unknown action received ");
                sb2.append(action);
                sb2.append(", terminating");
                Log.e("GcmTaskService", sb2.toString());
            }
            return 2;
        } finally {
            zza(i2);
        }
    }

    public final boolean zza(String str) {
        boolean z;
        synchronized (this.zza) {
            z = !this.zzf.zza(str, this.zze.getClassName());
            if (z) {
                String packageName = getPackageName();
                StringBuilder sb = new StringBuilder(String.valueOf(packageName).length() + 44 + String.valueOf(str).length());
                sb.append(packageName);
                sb.append(" ");
                sb.append(str);
                sb.append(": Task already running, won't start another");
                Log.w("GcmTaskService", sb.toString());
            }
        }
        return z;
    }

    public final void zza(int i) {
        synchronized (this.zza) {
            this.zzb = i;
            if (!this.zzf.zzb(this.zze.getClassName())) {
                stopSelf(this.zzb);
            }
        }
    }

    /* loaded from: classes.dex */
    public class zzb implements Runnable {
        public final String zza;
        public final List<Uri> zzc;
        public final INetworkTaskCallback zzd;
        public final Messenger zze;

        public zzb(String str, IBinder iBinder, Bundle bundle, List<Uri> list) {
            INetworkTaskCallback iNetworkTaskCallback;
            this.zza = str;
            int i = INetworkTaskCallback.Stub.$r8$clinit;
            if (iBinder == null) {
                iNetworkTaskCallback = null;
            } else {
                IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.gcm.INetworkTaskCallback");
                if (queryLocalInterface instanceof INetworkTaskCallback) {
                    iNetworkTaskCallback = (INetworkTaskCallback) queryLocalInterface;
                } else {
                    iNetworkTaskCallback = new INetworkTaskCallback.Stub.Proxy(iBinder);
                }
            }
            this.zzd = iNetworkTaskCallback;
            this.zzc = list;
            this.zze = null;
        }

        @Override // java.lang.Runnable
        public final void run() {
            DefaultGcmNetworkManagerWrapper defaultGcmNetworkManagerWrapper;
            String str = this.zza;
            BackdropRotationTask backdropRotationTask = (BackdropRotationTask) GcmTaskService.this;
            Context applicationContext = backdropRotationTask.getApplicationContext();
            DiskBasedLogger.e("BackdropRotationTask", "Task run with tag: " + str, applicationContext);
            synchronized (DefaultGcmNetworkManagerWrapper.sInstanceLock) {
                if (DefaultGcmNetworkManagerWrapper.sInstance == null) {
                    DefaultGcmNetworkManagerWrapper.sInstance = new DefaultGcmNetworkManagerWrapper(applicationContext.getApplicationContext());
                }
                defaultGcmNetworkManagerWrapper = DefaultGcmNetworkManagerWrapper.sInstance;
            }
            BackdropRotationTask.TaskResultCallable taskResultCallable = new BackdropRotationTask.TaskResultCallable(null);
            FutureTask futureTask = new FutureTask(taskResultCallable);
            ((DefaultWallpaperRefresher) InjectorProvider.getInjector().getWallpaperRefresher(applicationContext)).refresh(
            /*  JADX ERROR: Method code generation error
                jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0053: INVOKE  
                  (wrap: com.android.wallpaper.module.DefaultWallpaperRefresher : 0x0051: CHECK_CAST (r9v1 com.android.wallpaper.module.DefaultWallpaperRefresher A[REMOVE]) = (com.android.wallpaper.module.DefaultWallpaperRefresher) (wrap: com.android.wallpaper.module.WallpaperRefresher : 0x0045: INVOKE  (r9v0 com.android.wallpaper.module.WallpaperRefresher A[REMOVE]) = 
                  (wrap: com.android.wallpaper.module.Injector : 0x0041: INVOKE  (r0v4 com.android.wallpaper.module.Injector A[REMOVE]) =  type: STATIC call: com.android.wallpaper.module.InjectorProvider.getInjector():com.android.wallpaper.module.Injector)
                  (r7v0 'applicationContext' android.content.Context)
                 type: INTERFACE call: com.android.wallpaper.module.Injector.getWallpaperRefresher(android.content.Context):com.android.wallpaper.module.WallpaperRefresher))
                  (wrap: com.google.android.apps.wallpaper.backdrop.BackdropRotationTask$2 : 0x004e: CONSTRUCTOR  (r10v0 com.google.android.apps.wallpaper.backdrop.BackdropRotationTask$2 A[REMOVE]) = 
                  (r1v1 'backdropRotationTask' com.google.android.apps.wallpaper.backdrop.BackdropRotationTask)
                  (r7v0 'applicationContext' android.content.Context)
                  (r3v1 'defaultGcmNetworkManagerWrapper' com.google.android.apps.wallpaper.backdrop.DefaultGcmNetworkManagerWrapper)
                  (r4v0 'str' java.lang.String)
                  (r5v0 'taskResultCallable' com.google.android.apps.wallpaper.backdrop.BackdropRotationTask$TaskResultCallable)
                  (r8v0 'futureTask' java.util.concurrent.FutureTask)
                 call: com.google.android.apps.wallpaper.backdrop.BackdropRotationTask.2.<init>(com.google.android.apps.wallpaper.backdrop.BackdropRotationTask, android.content.Context, com.google.android.apps.wallpaper.backdrop.DefaultGcmNetworkManagerWrapper, java.lang.String, com.google.android.apps.wallpaper.backdrop.BackdropRotationTask$TaskResultCallable, java.util.concurrent.FutureTask):void type: CONSTRUCTOR)
                 type: VIRTUAL call: com.android.wallpaper.module.DefaultWallpaperRefresher.refresh(com.android.wallpaper.module.WallpaperRefresher$RefreshListener):void in method: com.google.android.gms.gcm.GcmTaskService.zzb.run():void, file: classes.dex
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:285)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:248)
                	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:105)
                	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                	at jadx.core.dex.regions.Region.generate(Region.java:35)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:261)
                	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:254)
                	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:349)
                	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:302)
                	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:271)
                	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
                	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.google.android.apps.wallpaper.backdrop.BackdropRotationTask, state: GENERATED_AND_UNLOADED
                	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:259)
                	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:675)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:393)
                	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:976)
                	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:801)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:397)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:278)
                	... 15 more
                */
            /*
                this = this;
                java.lang.String r4 = r11.zza
                com.google.android.gms.gcm.GcmTaskService r0 = com.google.android.gms.gcm.GcmTaskService.this
                r1 = r0
                com.google.android.apps.wallpaper.backdrop.BackdropRotationTask r1 = (com.google.android.apps.wallpaper.backdrop.BackdropRotationTask) r1
                android.content.Context r7 = r1.getApplicationContext()
                java.lang.String r0 = "BackdropRotationTask"
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r3 = "Task run with tag: "
                r2.append(r3)
                r2.append(r4)
                java.lang.String r2 = r2.toString()
                com.android.wallpaper.util.DiskBasedLogger.e(r0, r2, r7)
                java.lang.Object r0 = com.google.android.apps.wallpaper.backdrop.DefaultGcmNetworkManagerWrapper.sInstanceLock
                monitor-enter(r0)
                com.google.android.apps.wallpaper.backdrop.DefaultGcmNetworkManagerWrapper r2 = com.google.android.apps.wallpaper.backdrop.DefaultGcmNetworkManagerWrapper.sInstance     // Catch: all -> 0x00b1
                if (r2 != 0) goto L_0x0033
                com.google.android.apps.wallpaper.backdrop.DefaultGcmNetworkManagerWrapper r2 = new com.google.android.apps.wallpaper.backdrop.DefaultGcmNetworkManagerWrapper     // Catch: all -> 0x00b1
                android.content.Context r3 = r7.getApplicationContext()     // Catch: all -> 0x00b1
                r2.<init>(r3)     // Catch: all -> 0x00b1
                com.google.android.apps.wallpaper.backdrop.DefaultGcmNetworkManagerWrapper.sInstance = r2     // Catch: all -> 0x00b1
            L_0x0033:
                com.google.android.apps.wallpaper.backdrop.DefaultGcmNetworkManagerWrapper r3 = com.google.android.apps.wallpaper.backdrop.DefaultGcmNetworkManagerWrapper.sInstance     // Catch: all -> 0x00b1
                monitor-exit(r0)     // Catch: all -> 0x00b1
                com.google.android.apps.wallpaper.backdrop.BackdropRotationTask$TaskResultCallable r5 = new com.google.android.apps.wallpaper.backdrop.BackdropRotationTask$TaskResultCallable
                r0 = 0
                r5.<init>(r0)
                java.util.concurrent.FutureTask r8 = new java.util.concurrent.FutureTask
                r8.<init>(r5)
                com.android.wallpaper.module.Injector r0 = com.android.wallpaper.module.InjectorProvider.getInjector()
                com.android.wallpaper.module.WallpaperRefresher r9 = r0.getWallpaperRefresher(r7)
                com.google.android.apps.wallpaper.backdrop.BackdropRotationTask$2 r10 = new com.google.android.apps.wallpaper.backdrop.BackdropRotationTask$2
                r0 = r10
                r2 = r7
                r6 = r8
                r0.<init>(r1, r2, r3, r4, r5, r6)
                com.android.wallpaper.module.DefaultWallpaperRefresher r9 = (com.android.wallpaper.module.DefaultWallpaperRefresher) r9
                r9.refresh(r10)
                r0 = 2
                r2 = 1
                java.util.concurrent.TimeUnit r3 = java.util.concurrent.TimeUnit.MINUTES     // Catch: InterruptedException -> 0x0096, ExecutionException -> 0x007e, TimeoutException -> 0x0066
                java.lang.Object r0 = r8.get(r0, r3)     // Catch: InterruptedException -> 0x0096, ExecutionException -> 0x007e, TimeoutException -> 0x0066
                java.lang.Integer r0 = (java.lang.Integer) r0     // Catch: InterruptedException -> 0x0096, ExecutionException -> 0x007e, TimeoutException -> 0x0066
                int r2 = r0.intValue()     // Catch: InterruptedException -> 0x0096, ExecutionException -> 0x007e, TimeoutException -> 0x0066
                goto L_0x00ad
            L_0x0066:
                r0 = move-exception
                java.lang.String r1 = "BackdropRotationTask"
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                r3.<init>()
                java.lang.String r4 = "Timed out updating wallpaper with max timeout of 2 minutes: "
                r3.append(r4)
                r3.append(r0)
                java.lang.String r0 = r3.toString()
                com.android.wallpaper.util.DiskBasedLogger.e(r1, r0, r7)
                goto L_0x00ad
            L_0x007e:
                r0 = move-exception
                java.lang.String r1 = "BackdropRotationTask"
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                r3.<init>()
                java.lang.String r4 = "Execution error while updating wallpaper: "
                r3.append(r4)
                r3.append(r0)
                java.lang.String r0 = r3.toString()
                com.android.wallpaper.util.DiskBasedLogger.e(r1, r0, r7)
                goto L_0x00ad
            L_0x0096:
                r0 = move-exception
                java.lang.String r1 = "BackdropRotationTask"
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                r3.<init>()
                java.lang.String r4 = "Interrupted while updating wallpaper: "
                r3.append(r4)
                r3.append(r0)
                java.lang.String r0 = r3.toString()
                com.android.wallpaper.util.DiskBasedLogger.e(r1, r0, r7)
            L_0x00ad:
                r11.zza(r2)
                return
            L_0x00b1:
                r11 = move-exception
                monitor-exit(r0)     // Catch: all -> 0x00b1
                throw r11
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.gcm.GcmTaskService.zzb.run():void");
        }

        public final void zza(int i) {
            GcmTaskService gcmTaskService;
            synchronized (GcmTaskService.this.zza) {
                try {
                    gcmTaskService = GcmTaskService.this;
                } catch (RemoteException unused) {
                    String valueOf = String.valueOf(this.zza);
                    Log.e("GcmTaskService", valueOf.length() != 0 ? "Error reporting result of operation to scheduler for ".concat(valueOf) : new String("Error reporting result of operation to scheduler for "));
                    GcmTaskService gcmTaskService2 = GcmTaskService.this;
                    gcmTaskService2.zzf.zzb(this.zza, gcmTaskService2.zze.getClassName());
                    if (!zza()) {
                        GcmTaskService gcmTaskService3 = GcmTaskService.this;
                        if (!gcmTaskService3.zzf.zzb(gcmTaskService3.zze.getClassName())) {
                            GcmTaskService gcmTaskService4 = GcmTaskService.this;
                            gcmTaskService4.stopSelf(gcmTaskService4.zzb);
                        }
                    }
                }
                if (gcmTaskService.zzf.zzc(this.zza, gcmTaskService.zze.getClassName())) {
                    GcmTaskService gcmTaskService5 = GcmTaskService.this;
                    gcmTaskService5.zzf.zzb(this.zza, gcmTaskService5.zze.getClassName());
                    if (!zza()) {
                        GcmTaskService gcmTaskService6 = GcmTaskService.this;
                        if (!gcmTaskService6.zzf.zzb(gcmTaskService6.zze.getClassName())) {
                            GcmTaskService gcmTaskService7 = GcmTaskService.this;
                            gcmTaskService7.stopSelf(gcmTaskService7.zzb);
                        }
                    }
                    return;
                }
                if (zza()) {
                    Messenger messenger = this.zze;
                    Message obtain = Message.obtain();
                    obtain.what = 3;
                    obtain.arg1 = i;
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("component", GcmTaskService.this.zze);
                    bundle.putString("tag", this.zza);
                    obtain.setData(bundle);
                    messenger.send(obtain);
                } else {
                    this.zzd.taskFinished(i);
                }
                GcmTaskService gcmTaskService8 = GcmTaskService.this;
                gcmTaskService8.zzf.zzb(this.zza, gcmTaskService8.zze.getClassName());
                if (!zza()) {
                    GcmTaskService gcmTaskService9 = GcmTaskService.this;
                    if (!gcmTaskService9.zzf.zzb(gcmTaskService9.zze.getClassName())) {
                        GcmTaskService gcmTaskService10 = GcmTaskService.this;
                        gcmTaskService10.stopSelf(gcmTaskService10.zzb);
                    }
                }
            }
        }

        public zzb(String str, Messenger messenger, Bundle bundle, List<Uri> list) {
            this.zza = str;
            this.zze = messenger;
            this.zzc = list;
            this.zzd = null;
        }

        public final boolean zza() {
            return this.zze != null;
        }
    }
}
