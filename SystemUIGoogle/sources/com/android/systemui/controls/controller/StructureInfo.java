package com.android.systemui.controls.controller;

import android.content.ComponentName;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: StructureInfo.kt */
/* loaded from: classes.dex */
public final class StructureInfo {
    private final ComponentName componentName;
    private final List<ControlInfo> controls;
    private final CharSequence structure;

    /* JADX DEBUG: Multi-variable search result rejected for r0v0, resolved type: com.android.systemui.controls.controller.StructureInfo */
    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ StructureInfo copy$default(StructureInfo structureInfo, ComponentName componentName, CharSequence charSequence, List list, int i, Object obj) {
        if ((i & 1) != 0) {
            componentName = structureInfo.componentName;
        }
        if ((i & 2) != 0) {
            charSequence = structureInfo.structure;
        }
        if ((i & 4) != 0) {
            list = structureInfo.controls;
        }
        return structureInfo.copy(componentName, charSequence, list);
    }

    public final StructureInfo copy(ComponentName componentName, CharSequence charSequence, List<ControlInfo> list) {
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        Intrinsics.checkNotNullParameter(charSequence, "structure");
        Intrinsics.checkNotNullParameter(list, "controls");
        return new StructureInfo(componentName, charSequence, list);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StructureInfo)) {
            return false;
        }
        StructureInfo structureInfo = (StructureInfo) obj;
        return Intrinsics.areEqual(this.componentName, structureInfo.componentName) && Intrinsics.areEqual(this.structure, structureInfo.structure) && Intrinsics.areEqual(this.controls, structureInfo.controls);
    }

    public int hashCode() {
        return (((this.componentName.hashCode() * 31) + this.structure.hashCode()) * 31) + this.controls.hashCode();
    }

    public String toString() {
        return "StructureInfo(componentName=" + this.componentName + ", structure=" + ((Object) this.structure) + ", controls=" + this.controls + ')';
    }

    public StructureInfo(ComponentName componentName, CharSequence charSequence, List<ControlInfo> list) {
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        Intrinsics.checkNotNullParameter(charSequence, "structure");
        Intrinsics.checkNotNullParameter(list, "controls");
        this.componentName = componentName;
        this.structure = charSequence;
        this.controls = list;
    }

    public final ComponentName getComponentName() {
        return this.componentName;
    }

    public final CharSequence getStructure() {
        return this.structure;
    }

    public final List<ControlInfo> getControls() {
        return this.controls;
    }
}
