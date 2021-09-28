package kotlinx.coroutines;

import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class InactiveNodeList implements Incomplete {
    @NotNull
    public final NodeList list;

    public InactiveNodeList(@NotNull NodeList nodeList) {
        this.list = nodeList;
    }

    @Override // kotlinx.coroutines.Incomplete
    @NotNull
    public NodeList getList() {
        return this.list;
    }

    @Override // kotlinx.coroutines.Incomplete
    public boolean isActive() {
        return false;
    }

    @NotNull
    public String toString() {
        return DebugKt.DEBUG ? this.list.getString("New") : super.toString();
    }
}
