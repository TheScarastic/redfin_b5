package kotlinx.coroutines.channels;

import kotlinx.coroutines.internal.SystemPropsKt;
/* compiled from: Channel.kt */
/* loaded from: classes2.dex */
public interface Channel<E> extends SendChannel<E>, ReceiveChannel<E> {
    public static final Factory Factory = Factory.$$INSTANCE;

    /* compiled from: Channel.kt */
    /* loaded from: classes2.dex */
    public static final class Factory {
        static final /* synthetic */ Factory $$INSTANCE = new Factory();
        private static final int CHANNEL_DEFAULT_CAPACITY = SystemPropsKt.systemProp("kotlinx.coroutines.channels.defaultBuffer", 64, 1, 2147483646);

        private Factory() {
        }

        public final int getCHANNEL_DEFAULT_CAPACITY$kotlinx_coroutines_core() {
            return CHANNEL_DEFAULT_CAPACITY;
        }
    }
}
