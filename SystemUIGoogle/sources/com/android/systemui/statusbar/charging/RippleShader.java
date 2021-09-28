package com.android.systemui.statusbar.charging;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RuntimeShader;
import android.util.MathUtils;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: RippleShader.kt */
/* loaded from: classes.dex */
public final class RippleShader extends RuntimeShader {
    public static final Companion Companion = new Companion(null);
    private float distortionStrength;
    private float progress;
    private float radius;
    private float sparkleStrength;
    private float time;
    private PointF origin = new PointF();
    private int color = 16777215;
    private float pixelDensity = 1.0f;

    /* compiled from: RippleShader.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* access modifiers changed from: private */
        public final float subProgress(float f, float f2, float f3) {
            float min = Math.min(f, f2);
            return (Math.min(Math.max(f3, min), Math.max(f, f2)) - f) / (f2 - f);
        }
    }

    public RippleShader() {
        super("uniform vec2 in_origin;\n                uniform float in_progress;\n                uniform float in_maxRadius;\n                uniform float in_time;\n                uniform float in_distort_radial;\n                uniform float in_distort_xy;\n                uniform float in_radius;\n                uniform float in_fadeSparkle;\n                uniform float in_fadeCircle;\n                uniform float in_fadeRing;\n                uniform float in_blur;\n                uniform float in_pixelDensity;\n                uniform vec4 in_color;\n                uniform float in_sparkle_strength;float triangleNoise(vec2 n) {\n                    n  = fract(n * vec2(5.3987, 5.4421));\n                    n += dot(n.yx, n.xy + vec2(21.5351, 14.3137));\n                    float xy = n.x * n.y;\n                    return fract(xy * 95.4307) + fract(xy * 75.04961) - 1.0;\n                }\n                const float PI = 3.1415926535897932384626;\n\n                float threshold(float v, float l, float h) {\n                  return step(l, v) * (1.0 - step(h, v));\n                }\n\n                float sparkles(vec2 uv, float t) {\n                  float n = triangleNoise(uv);\n                  float s = 0.0;\n                  for (float i = 0; i < 4; i += 1) {\n                    float l = i * 0.01;\n                    float h = l + 0.1;\n                    float o = smoothstep(n - l, h, n);\n                    o *= abs(sin(PI * o * (t + 0.55 * i)));\n                    s += o;\n                  }\n                  return s;\n                }\n\n                float softCircle(vec2 uv, vec2 xy, float radius, float blur) {\n                  float blurHalf = blur * 0.5;\n                  float d = distance(uv, xy);\n                  return 1. - smoothstep(1. - blurHalf, 1. + blurHalf, d / radius);\n                }\n\n                float softRing(vec2 uv, vec2 xy, float radius, float blur) {\n                  float thickness_half = radius * 0.25;\n                  float circle_outer = softCircle(uv, xy, radius + thickness_half, blur);\n                  float circle_inner = softCircle(uv, xy, radius - thickness_half, blur);\n                  return circle_outer - circle_inner;\n                }\n\n                vec2 distort(vec2 p, vec2 origin, float time,\n                    float distort_amount_radial, float distort_amount_xy) {\n                    float2 distance = origin - p;\n                    float angle = atan(distance.y, distance.x);\n                    return p + vec2(sin(angle * 8 + time * 0.003 + 1.641),\n                                    cos(angle * 5 + 2.14 + time * 0.00412)) * distort_amount_radial\n                             + vec2(sin(p.x * 0.01 + time * 0.00215 + 0.8123),\n                                    cos(p.y * 0.01 + time * 0.005931)) * distort_amount_xy;\n                }vec4 main(vec2 p) {\n                    vec2 p_distorted = distort(p, in_origin, in_time, in_distort_radial,\n                        in_distort_xy);\n\n                    // Draw shapes\n                    float sparkleRing = softRing(p_distorted, in_origin, in_radius, in_blur);\n                    float sparkle = sparkles(p - mod(p, in_pixelDensity * 0.8), in_time * 0.00175)\n                        * sparkleRing * in_fadeSparkle;\n                    float circle = softCircle(p_distorted, in_origin, in_radius * 1.2, in_blur);\n                    float rippleAlpha = max(circle * in_fadeCircle,\n                        softRing(p_distorted, in_origin, in_radius, in_blur) * in_fadeRing) * 0.45;\n                    vec4 ripple = in_color * rippleAlpha;\n                    return mix(ripple, vec4(sparkle), sparkle * in_sparkle_strength);\n                }", false);
    }

    public final void setRadius(float f) {
        this.radius = f;
        setUniform("in_maxRadius", f);
    }

    public final void setOrigin(PointF pointF) {
        Intrinsics.checkNotNullParameter(pointF, "value");
        this.origin = pointF;
        setUniform("in_origin", new float[]{pointF.x, pointF.y});
    }

    public final float getProgress() {
        return this.progress;
    }

    public final void setProgress(float f) {
        this.progress = f;
        setUniform("in_progress", f);
        float f2 = (float) 1;
        float f3 = f2 - f;
        setUniform("in_radius", (f2 - ((f3 * f3) * f3)) * this.radius);
        setUniform("in_blur", MathUtils.lerp(1.25f, 0.5f, f));
        Companion companion = Companion;
        float subProgress = companion.subProgress(0.0f, 0.1f, f);
        float subProgress2 = companion.subProgress(0.4f, 1.0f, f);
        float subProgress3 = companion.subProgress(0.3f, 1.0f, f);
        float subProgress4 = companion.subProgress(0.0f, 0.2f, f);
        setUniform("in_fadeSparkle", Math.min(subProgress, f2 - subProgress2));
        setUniform("in_fadeCircle", f2 - subProgress4);
        setUniform("in_fadeRing", Math.min(subProgress, f2 - subProgress3));
    }

    public final void setTime(float f) {
        this.time = f;
        setUniform("in_time", f);
    }

    public final int getColor() {
        return this.color;
    }

    public final void setColor(int i) {
        this.color = i;
        Color valueOf = Color.valueOf(i);
        setUniform("in_color", new float[]{valueOf.red(), valueOf.green(), valueOf.blue(), valueOf.alpha()});
    }

    public final void setSparkleStrength(float f) {
        this.sparkleStrength = f;
        setUniform("in_sparkle_strength", f);
    }

    public final void setDistortionStrength(float f) {
        this.distortionStrength = f;
        float f2 = (float) 75;
        setUniform("in_distort_radial", this.progress * f2 * f);
        setUniform("in_distort_xy", f2 * f);
    }

    public final void setPixelDensity(float f) {
        this.pixelDensity = f;
        setUniform("in_pixelDensity", f);
    }
}
