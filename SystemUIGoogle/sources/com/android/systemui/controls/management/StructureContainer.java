package com.android.systemui.controls.management;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsFavoritingActivity.kt */
/* loaded from: classes.dex */
public final class StructureContainer {
    private final ControlsModel model;
    private final CharSequence structureName;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StructureContainer)) {
            return false;
        }
        StructureContainer structureContainer = (StructureContainer) obj;
        return Intrinsics.areEqual(this.structureName, structureContainer.structureName) && Intrinsics.areEqual(this.model, structureContainer.model);
    }

    public int hashCode() {
        return (this.structureName.hashCode() * 31) + this.model.hashCode();
    }

    public String toString() {
        return "StructureContainer(structureName=" + ((Object) this.structureName) + ", model=" + this.model + ')';
    }

    public StructureContainer(CharSequence charSequence, ControlsModel controlsModel) {
        Intrinsics.checkNotNullParameter(charSequence, "structureName");
        Intrinsics.checkNotNullParameter(controlsModel, "model");
        this.structureName = charSequence;
        this.model = controlsModel;
    }

    public final ControlsModel getModel() {
        return this.model;
    }

    public final CharSequence getStructureName() {
        return this.structureName;
    }
}
