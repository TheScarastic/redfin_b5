package com.android.systemui.controls.ui;

import android.content.ComponentName;
import android.graphics.drawable.Drawable;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsUiControllerImpl.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class SelectionItem {
    private final CharSequence appName;
    private final ComponentName componentName;
    private final Drawable icon;
    private final CharSequence structure;
    private final int uid;

    public static /* synthetic */ SelectionItem copy$default(SelectionItem selectionItem, CharSequence charSequence, CharSequence charSequence2, Drawable drawable, ComponentName componentName, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            charSequence = selectionItem.appName;
        }
        if ((i2 & 2) != 0) {
            charSequence2 = selectionItem.structure;
        }
        if ((i2 & 4) != 0) {
            drawable = selectionItem.icon;
        }
        if ((i2 & 8) != 0) {
            componentName = selectionItem.componentName;
        }
        if ((i2 & 16) != 0) {
            i = selectionItem.uid;
        }
        return selectionItem.copy(charSequence, charSequence2, drawable, componentName, i);
    }

    public final SelectionItem copy(CharSequence charSequence, CharSequence charSequence2, Drawable drawable, ComponentName componentName, int i) {
        Intrinsics.checkNotNullParameter(charSequence, "appName");
        Intrinsics.checkNotNullParameter(charSequence2, "structure");
        Intrinsics.checkNotNullParameter(drawable, "icon");
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        return new SelectionItem(charSequence, charSequence2, drawable, componentName, i);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SelectionItem)) {
            return false;
        }
        SelectionItem selectionItem = (SelectionItem) obj;
        return Intrinsics.areEqual(this.appName, selectionItem.appName) && Intrinsics.areEqual(this.structure, selectionItem.structure) && Intrinsics.areEqual(this.icon, selectionItem.icon) && Intrinsics.areEqual(this.componentName, selectionItem.componentName) && this.uid == selectionItem.uid;
    }

    public int hashCode() {
        return (((((((this.appName.hashCode() * 31) + this.structure.hashCode()) * 31) + this.icon.hashCode()) * 31) + this.componentName.hashCode()) * 31) + Integer.hashCode(this.uid);
    }

    public String toString() {
        return "SelectionItem(appName=" + ((Object) this.appName) + ", structure=" + ((Object) this.structure) + ", icon=" + this.icon + ", componentName=" + this.componentName + ", uid=" + this.uid + ')';
    }

    public SelectionItem(CharSequence charSequence, CharSequence charSequence2, Drawable drawable, ComponentName componentName, int i) {
        Intrinsics.checkNotNullParameter(charSequence, "appName");
        Intrinsics.checkNotNullParameter(charSequence2, "structure");
        Intrinsics.checkNotNullParameter(drawable, "icon");
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        this.appName = charSequence;
        this.structure = charSequence2;
        this.icon = drawable;
        this.componentName = componentName;
        this.uid = i;
    }

    public final CharSequence getStructure() {
        return this.structure;
    }

    public final Drawable getIcon() {
        return this.icon;
    }

    public final ComponentName getComponentName() {
        return this.componentName;
    }

    public final int getUid() {
        return this.uid;
    }

    public final CharSequence getTitle() {
        return this.structure.length() == 0 ? this.appName : this.structure;
    }
}
