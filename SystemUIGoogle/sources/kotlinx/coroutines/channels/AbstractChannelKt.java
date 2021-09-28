package kotlinx.coroutines.channels;

import kotlinx.coroutines.internal.Symbol;
/* compiled from: AbstractChannel.kt */
/* loaded from: classes2.dex */
public final class AbstractChannelKt {
    public static final Object OFFER_SUCCESS = new Symbol("OFFER_SUCCESS");
    public static final Object OFFER_FAILED = new Symbol("OFFER_FAILED");
    public static final Object POLL_FAILED = new Symbol("POLL_FAILED");
    public static final Object ENQUEUE_FAILED = new Symbol("ENQUEUE_FAILED");
    public static final Object SELECT_STARTED = new Symbol("SELECT_STARTED");
    public static final Symbol NULL_VALUE = new Symbol("NULL_VALUE");
    public static final Object CLOSE_RESUMED = new Symbol("CLOSE_RESUMED");
    public static final Object SEND_RESUMED = new Symbol("SEND_RESUMED");
    public static final Object HANDLER_INVOKED = new Symbol("ON_CLOSE_HANDLER_INVOKED");
}
