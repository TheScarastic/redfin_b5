package kotlinx.coroutines.channels;
/* compiled from: Channel.kt */
/* loaded from: classes2.dex */
public final class ChannelKt {
    public static /* synthetic */ Channel Channel$default(int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = 0;
        }
        return Channel(i);
    }

    public static final <E> Channel<E> Channel(int i) {
        if (i == -2) {
            return new ArrayChannel(Channel.Factory.getCHANNEL_DEFAULT_CAPACITY$kotlinx_coroutines_core());
        }
        if (i == -1) {
            return new ConflatedChannel();
        }
        if (i == 0) {
            return new RendezvousChannel();
        }
        if (i != Integer.MAX_VALUE) {
            return new ArrayChannel(i);
        }
        return new LinkedListChannel();
    }
}
