package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: JobSupport.kt */
/* loaded from: classes2.dex */
public final class InactiveNodeList implements Incomplete {
    private final NodeList list;

    @Override // kotlinx.coroutines.Incomplete
    public boolean isActive() {
        return false;
    }

    public InactiveNodeList(NodeList nodeList) {
        Intrinsics.checkParameterIsNotNull(nodeList, "list");
        this.list = nodeList;
    }

    @Override // kotlinx.coroutines.Incomplete
    public NodeList getList() {
        return this.list;
    }

    public String toString() {
        return DebugKt.getDEBUG() ? getList().getString("New") : super.toString();
    }
}
