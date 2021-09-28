package com.android.systemui.util;

import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.graphics.drawable.InsetDrawable;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: RoundedCornerProgressDrawable.kt */
/* loaded from: classes2.dex */
public final class RoundedCornerProgressDrawable extends InsetDrawable {
    public static final Companion Companion = new Companion(null);

    public RoundedCornerProgressDrawable() {
        this(null, 1, null);
    }

    public /* synthetic */ RoundedCornerProgressDrawable(Drawable drawable, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? null : drawable);
    }

    public RoundedCornerProgressDrawable(Drawable drawable) {
        super(drawable, 0);
    }

    /* compiled from: RoundedCornerProgressDrawable.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Override // android.graphics.drawable.Drawable, android.graphics.drawable.DrawableWrapper
    public boolean onLayoutDirectionChanged(int i) {
        onLevelChange(getLevel());
        return super.onLayoutDirectionChanged(i);
    }

    @Override // android.graphics.drawable.InsetDrawable, android.graphics.drawable.Drawable, android.graphics.drawable.DrawableWrapper
    protected void onBoundsChange(Rect rect) {
        Intrinsics.checkNotNullParameter(rect, "bounds");
        super.onBoundsChange(rect);
        onLevelChange(getLevel());
    }

    @Override // android.graphics.drawable.Drawable, android.graphics.drawable.DrawableWrapper
    protected boolean onLevelChange(int i) {
        Drawable drawable = getDrawable();
        Rect bounds = drawable == null ? null : drawable.getBounds();
        Intrinsics.checkNotNull(bounds);
        int height = getBounds().height() + (((getBounds().width() - getBounds().height()) * i) / 10000);
        Drawable drawable2 = getDrawable();
        if (drawable2 != null) {
            drawable2.setBounds(getBounds().left, bounds.top, getBounds().left + height, bounds.bottom);
        }
        return super.onLevelChange(i);
    }

    @Override // android.graphics.drawable.Drawable, android.graphics.drawable.DrawableWrapper
    public Drawable.ConstantState getConstantState() {
        Drawable.ConstantState constantState = super.getConstantState();
        Intrinsics.checkNotNull(constantState);
        return new RoundedCornerState(constantState);
    }

    @Override // android.graphics.drawable.Drawable, android.graphics.drawable.DrawableWrapper
    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | 4096;
    }

    /* compiled from: RoundedCornerProgressDrawable.kt */
    /* loaded from: classes2.dex */
    private static final class RoundedCornerState extends Drawable.ConstantState {
        private final Drawable.ConstantState wrappedState;

        public RoundedCornerState(Drawable.ConstantState constantState) {
            Intrinsics.checkNotNullParameter(constantState, "wrappedState");
            this.wrappedState = constantState;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            return newDrawable(null, null);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources resources, Resources.Theme theme) {
            Drawable newDrawable = this.wrappedState.newDrawable(resources, theme);
            Objects.requireNonNull(newDrawable, "null cannot be cast to non-null type android.graphics.drawable.DrawableWrapper");
            return new RoundedCornerProgressDrawable(((DrawableWrapper) newDrawable).getDrawable());
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return this.wrappedState.getChangingConfigurations();
        }
    }
}
