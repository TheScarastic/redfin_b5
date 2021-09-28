package com.bumptech.glide.load.resource.bitmap;

import java.io.File;
/* loaded from: classes.dex */
public final class HardwareConfigState {
    public static final File FD_SIZE_LIST = new File("/proc/self/fd");
    public static volatile HardwareConfigState instance;
    public volatile int decodesSinceLastFdCheck;
    public volatile boolean isHardwareConfigAllowed = true;
}
