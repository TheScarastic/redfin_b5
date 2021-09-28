package androidx.palette.graphics;
/* loaded from: classes.dex */
public final class Target {
    public static final Target DARK_MUTED;
    public static final Target DARK_VIBRANT;
    public static final Target LIGHT_MUTED;
    public static final Target LIGHT_VIBRANT;
    public static final Target MUTED;
    public static final Target VIBRANT;
    final float[] mLightnessTargets;
    final float[] mSaturationTargets;
    final float[] mWeights = new float[3];
    boolean mIsExclusive = true;

    static {
        Target target = new Target();
        LIGHT_VIBRANT = target;
        setDefaultLightLightnessValues(target);
        setDefaultVibrantSaturationValues(target);
        Target target2 = new Target();
        VIBRANT = target2;
        setDefaultNormalLightnessValues(target2);
        setDefaultVibrantSaturationValues(target2);
        Target target3 = new Target();
        DARK_VIBRANT = target3;
        setDefaultDarkLightnessValues(target3);
        setDefaultVibrantSaturationValues(target3);
        Target target4 = new Target();
        LIGHT_MUTED = target4;
        setDefaultLightLightnessValues(target4);
        setDefaultMutedSaturationValues(target4);
        Target target5 = new Target();
        MUTED = target5;
        setDefaultNormalLightnessValues(target5);
        setDefaultMutedSaturationValues(target5);
        Target target6 = new Target();
        DARK_MUTED = target6;
        setDefaultDarkLightnessValues(target6);
        setDefaultMutedSaturationValues(target6);
    }

    Target() {
        float[] fArr = new float[3];
        this.mSaturationTargets = fArr;
        float[] fArr2 = new float[3];
        this.mLightnessTargets = fArr2;
        setTargetDefaultValues(fArr);
        setTargetDefaultValues(fArr2);
        setDefaultWeights();
    }

    public float getMinimumSaturation() {
        return this.mSaturationTargets[0];
    }

    public float getTargetSaturation() {
        return this.mSaturationTargets[1];
    }

    public float getMaximumSaturation() {
        return this.mSaturationTargets[2];
    }

    public float getMinimumLightness() {
        return this.mLightnessTargets[0];
    }

    public float getTargetLightness() {
        return this.mLightnessTargets[1];
    }

    public float getMaximumLightness() {
        return this.mLightnessTargets[2];
    }

    public float getSaturationWeight() {
        return this.mWeights[0];
    }

    public float getLightnessWeight() {
        return this.mWeights[1];
    }

    public float getPopulationWeight() {
        return this.mWeights[2];
    }

    public boolean isExclusive() {
        return this.mIsExclusive;
    }

    private static void setTargetDefaultValues(float[] fArr) {
        fArr[0] = 0.0f;
        fArr[1] = 0.5f;
        fArr[2] = 1.0f;
    }

    private void setDefaultWeights() {
        float[] fArr = this.mWeights;
        fArr[0] = 0.24f;
        fArr[1] = 0.52f;
        fArr[2] = 0.24f;
    }

    /* access modifiers changed from: package-private */
    public void normalizeWeights() {
        int length = this.mWeights.length;
        float f = 0.0f;
        for (int i = 0; i < length; i++) {
            float f2 = this.mWeights[i];
            if (f2 > 0.0f) {
                f += f2;
            }
        }
        if (f != 0.0f) {
            int length2 = this.mWeights.length;
            for (int i2 = 0; i2 < length2; i2++) {
                float[] fArr = this.mWeights;
                if (fArr[i2] > 0.0f) {
                    fArr[i2] = fArr[i2] / f;
                }
            }
        }
    }

    private static void setDefaultDarkLightnessValues(Target target) {
        float[] fArr = target.mLightnessTargets;
        fArr[1] = 0.26f;
        fArr[2] = 0.45f;
    }

    private static void setDefaultNormalLightnessValues(Target target) {
        float[] fArr = target.mLightnessTargets;
        fArr[0] = 0.3f;
        fArr[1] = 0.5f;
        fArr[2] = 0.7f;
    }

    private static void setDefaultLightLightnessValues(Target target) {
        float[] fArr = target.mLightnessTargets;
        fArr[0] = 0.55f;
        fArr[1] = 0.74f;
    }

    private static void setDefaultVibrantSaturationValues(Target target) {
        float[] fArr = target.mSaturationTargets;
        fArr[0] = 0.35f;
        fArr[1] = 1.0f;
    }

    private static void setDefaultMutedSaturationValues(Target target) {
        float[] fArr = target.mSaturationTargets;
        fArr[1] = 0.3f;
        fArr[2] = 0.4f;
    }
}
