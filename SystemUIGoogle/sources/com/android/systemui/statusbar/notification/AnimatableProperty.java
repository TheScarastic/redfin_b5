package com.android.systemui.statusbar.notification;

import android.util.FloatProperty;
import android.util.Property;
import android.view.View;
import com.android.systemui.R$id;
import java.util.function.BiConsumer;
import java.util.function.Function;
/* loaded from: classes.dex */
public abstract class AnimatableProperty {
    public static final AnimatableProperty TRANSLATION_X;
    public static final AnimatableProperty X;
    public static final AnimatableProperty Y = from(View.Y, R$id.y_animator_tag, R$id.y_animator_tag_start_value, R$id.y_animator_tag_end_value);
    public static final AnimatableProperty SCALE_X = from(View.SCALE_X, R$id.scale_x_animator_tag, R$id.scale_x_animator_start_value_tag, R$id.scale_x_animator_end_value_tag);
    public static final AnimatableProperty SCALE_Y = from(View.SCALE_Y, R$id.scale_y_animator_tag, R$id.scale_y_animator_start_value_tag, R$id.scale_y_animator_end_value_tag);
    public static final AnimatableProperty ABSOLUTE_X = from(new FloatProperty<View>("ViewAbsoluteX") { // from class: com.android.systemui.statusbar.notification.AnimatableProperty.1
        public void setValue(View view, float f) {
            view.setTag(R$id.absolute_x_current_value, Float.valueOf(f));
            View.X.set(view, Float.valueOf(f));
        }

        public Float get(View view) {
            Object tag = view.getTag(R$id.absolute_x_current_value);
            if (tag instanceof Float) {
                return (Float) tag;
            }
            return (Float) View.X.get(view);
        }
    }, R$id.absolute_x_animator_tag, R$id.absolute_x_animator_start_tag, R$id.absolute_x_animator_end_tag);
    public static final AnimatableProperty ABSOLUTE_Y = from(new FloatProperty<View>("ViewAbsoluteY") { // from class: com.android.systemui.statusbar.notification.AnimatableProperty.2
        public void setValue(View view, float f) {
            view.setTag(R$id.absolute_y_current_value, Float.valueOf(f));
            View.Y.set(view, Float.valueOf(f));
        }

        public Float get(View view) {
            Object tag = view.getTag(R$id.absolute_y_current_value);
            if (tag instanceof Float) {
                return (Float) tag;
            }
            return (Float) View.Y.get(view);
        }
    }, R$id.absolute_y_animator_tag, R$id.absolute_y_animator_start_tag, R$id.absolute_y_animator_end_tag);
    public static final AnimatableProperty WIDTH = from(new FloatProperty<View>("ViewWidth") { // from class: com.android.systemui.statusbar.notification.AnimatableProperty.3
        public void setValue(View view, float f) {
            view.setTag(R$id.view_width_current_value, Float.valueOf(f));
            view.setRight((int) (((float) view.getLeft()) + f));
        }

        public Float get(View view) {
            Object tag = view.getTag(R$id.view_width_current_value);
            if (tag instanceof Float) {
                return (Float) tag;
            }
            return Float.valueOf((float) view.getWidth());
        }
    }, R$id.view_width_animator_tag, R$id.view_width_animator_start_tag, R$id.view_width_animator_end_tag);
    public static final AnimatableProperty HEIGHT = from(new FloatProperty<View>("ViewHeight") { // from class: com.android.systemui.statusbar.notification.AnimatableProperty.4
        public void setValue(View view, float f) {
            view.setTag(R$id.view_height_current_value, Float.valueOf(f));
            view.setBottom((int) (((float) view.getTop()) + f));
        }

        public Float get(View view) {
            Object tag = view.getTag(R$id.view_height_current_value);
            if (tag instanceof Float) {
                return (Float) tag;
            }
            return Float.valueOf((float) view.getHeight());
        }
    }, R$id.view_height_animator_tag, R$id.view_height_animator_start_tag, R$id.view_height_animator_end_tag);

    public abstract int getAnimationEndTag();

    public abstract int getAnimationStartTag();

    public abstract int getAnimatorTag();

    public abstract Property getProperty();

    static {
        Property property = View.X;
        int i = R$id.x_animator_tag;
        int i2 = R$id.x_animator_tag_start_value;
        int i3 = R$id.x_animator_tag_end_value;
        X = from(property, i, i2, i3);
        TRANSLATION_X = from(View.TRANSLATION_X, i, i2, i3);
    }

    public static <T extends View> AnimatableProperty from(String str, final BiConsumer<T, Float> biConsumer, final Function<T, Float> function, final int i, final int i2, final int i3) {
        final AnonymousClass5 r0 = new FloatProperty<T>(str) { // from class: com.android.systemui.statusbar.notification.AnimatableProperty.5
            /* JADX WARN: Incorrect types in method signature: (TT;)Ljava/lang/Float; */
            public Float get(View view) {
                return (Float) function.apply(view);
            }

            /* JADX WARN: Incorrect types in method signature: (TT;F)V */
            public void setValue(View view, float f) {
                biConsumer.accept(view, Float.valueOf(f));
            }
        };
        return new AnimatableProperty() { // from class: com.android.systemui.statusbar.notification.AnimatableProperty.6
            @Override // com.android.systemui.statusbar.notification.AnimatableProperty
            public int getAnimationStartTag() {
                return i2;
            }

            @Override // com.android.systemui.statusbar.notification.AnimatableProperty
            public int getAnimationEndTag() {
                return i3;
            }

            @Override // com.android.systemui.statusbar.notification.AnimatableProperty
            public int getAnimatorTag() {
                return i;
            }

            @Override // com.android.systemui.statusbar.notification.AnimatableProperty
            public Property getProperty() {
                return r0;
            }
        };
    }

    public static <T extends View> AnimatableProperty from(final Property<T, Float> property, final int i, final int i2, final int i3) {
        return new AnimatableProperty() { // from class: com.android.systemui.statusbar.notification.AnimatableProperty.7
            @Override // com.android.systemui.statusbar.notification.AnimatableProperty
            public int getAnimationStartTag() {
                return i2;
            }

            @Override // com.android.systemui.statusbar.notification.AnimatableProperty
            public int getAnimationEndTag() {
                return i3;
            }

            @Override // com.android.systemui.statusbar.notification.AnimatableProperty
            public int getAnimatorTag() {
                return i;
            }

            @Override // com.android.systemui.statusbar.notification.AnimatableProperty
            public Property getProperty() {
                return property;
            }
        };
    }
}
