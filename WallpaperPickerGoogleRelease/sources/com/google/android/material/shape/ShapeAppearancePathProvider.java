package com.google.android.material.shape;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapePath;
import java.util.BitSet;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes.dex */
public class ShapeAppearancePathProvider {
    public final ShapePath[] cornerPaths = new ShapePath[4];
    public final Matrix[] cornerTransforms = new Matrix[4];
    public final Matrix[] edgeTransforms = new Matrix[4];
    public final PointF pointF = new PointF();
    public final Path overlappedEdgePath = new Path();
    public final Path boundsPath = new Path();
    public final ShapePath shapePath = new ShapePath();
    public final float[] scratch = new float[2];
    public final float[] scratch2 = new float[2];
    public final Path edgePath = new Path();
    public final Path cornerPath = new Path();
    public boolean edgeIntersectionCheckEnabled = true;

    /* loaded from: classes.dex */
    public static class Lazy {
        public static final ShapeAppearancePathProvider INSTANCE = new ShapeAppearancePathProvider();
    }

    /* loaded from: classes.dex */
    public interface PathListener {
    }

    public ShapeAppearancePathProvider() {
        for (int i = 0; i < 4; i++) {
            this.cornerPaths[i] = new ShapePath();
            this.cornerTransforms[i] = new Matrix();
            this.edgeTransforms[i] = new Matrix();
        }
    }

    public void calculatePath(ShapeAppearanceModel shapeAppearanceModel, float f, RectF rectF, PathListener pathListener, Path path) {
        float f2;
        Intrinsics intrinsics;
        char c;
        CornerSize cornerSize;
        CornerTreatment cornerTreatment;
        path.rewind();
        this.overlappedEdgePath.rewind();
        this.boundsPath.rewind();
        this.boundsPath.addRect(rectF, Path.Direction.CW);
        int i = 0;
        while (i < 4) {
            if (i == 1) {
                cornerSize = shapeAppearanceModel.bottomRightCornerSize;
            } else if (i == 2) {
                cornerSize = shapeAppearanceModel.bottomLeftCornerSize;
            } else if (i != 3) {
                cornerSize = shapeAppearanceModel.topRightCornerSize;
            } else {
                cornerSize = shapeAppearanceModel.topLeftCornerSize;
            }
            if (i == 1) {
                cornerTreatment = shapeAppearanceModel.bottomRightCorner;
            } else if (i == 2) {
                cornerTreatment = shapeAppearanceModel.bottomLeftCorner;
            } else if (i != 3) {
                cornerTreatment = shapeAppearanceModel.topRightCorner;
            } else {
                cornerTreatment = shapeAppearanceModel.topLeftCorner;
            }
            ShapePath shapePath = this.cornerPaths[i];
            Objects.requireNonNull(cornerTreatment);
            cornerTreatment.getCornerPath(shapePath, 90.0f, f, cornerSize.getCornerSize(rectF));
            int i2 = i + 1;
            float f3 = (float) (i2 * 90);
            this.cornerTransforms[i].reset();
            PointF pointF = this.pointF;
            if (i == 1) {
                pointF.set(rectF.right, rectF.bottom);
            } else if (i == 2) {
                pointF.set(rectF.left, rectF.bottom);
            } else if (i != 3) {
                pointF.set(rectF.right, rectF.top);
            } else {
                pointF.set(rectF.left, rectF.top);
            }
            Matrix matrix = this.cornerTransforms[i];
            PointF pointF2 = this.pointF;
            matrix.setTranslate(pointF2.x, pointF2.y);
            this.cornerTransforms[i].preRotate(f3);
            float[] fArr = this.scratch;
            ShapePath[] shapePathArr = this.cornerPaths;
            fArr[0] = shapePathArr[i].endX;
            fArr[1] = shapePathArr[i].endY;
            this.cornerTransforms[i].mapPoints(fArr);
            this.edgeTransforms[i].reset();
            Matrix matrix2 = this.edgeTransforms[i];
            float[] fArr2 = this.scratch;
            matrix2.setTranslate(fArr2[0], fArr2[1]);
            this.edgeTransforms[i].preRotate(f3);
            i = i2;
        }
        int i3 = 0;
        while (i3 < 4) {
            float[] fArr3 = this.scratch;
            ShapePath[] shapePathArr2 = this.cornerPaths;
            fArr3[0] = shapePathArr2[i3].startX;
            fArr3[1] = shapePathArr2[i3].startY;
            this.cornerTransforms[i3].mapPoints(fArr3);
            if (i3 == 0) {
                float[] fArr4 = this.scratch;
                path.moveTo(fArr4[0], fArr4[1]);
            } else {
                float[] fArr5 = this.scratch;
                path.lineTo(fArr5[0], fArr5[1]);
            }
            this.cornerPaths[i3].applyToPath(this.cornerTransforms[i3], path);
            if (pathListener != null) {
                ShapePath shapePath2 = this.cornerPaths[i3];
                Matrix matrix3 = this.cornerTransforms[i3];
                MaterialShapeDrawable.AnonymousClass1 r13 = (MaterialShapeDrawable.AnonymousClass1) pathListener;
                BitSet bitSet = MaterialShapeDrawable.this.containsIncompatibleShadowOp;
                Objects.requireNonNull(shapePath2);
                bitSet.set(i3, false);
                ShapePath.ShadowCompatOperation[] shadowCompatOperationArr = MaterialShapeDrawable.this.cornerShadowOperation;
                shapePath2.addConnectingShadowIfNecessary(shapePath2.endShadowAngle);
                shadowCompatOperationArr[i3] = 
                /*  JADX ERROR: Method code generation error
                    jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0140: APUT  
                      (r13v25 'shadowCompatOperationArr' com.google.android.material.shape.ShapePath$ShadowCompatOperation[])
                      (r6v4 'i3' int)
                      (wrap: com.google.android.material.shape.ShapePath$1 : 0x013d: CONSTRUCTOR  (r15v17 com.google.android.material.shape.ShapePath$1 A[REMOVE]) = 
                      (r11v5 'shapePath2' com.google.android.material.shape.ShapePath)
                      (wrap: java.util.ArrayList : 0x0138: CONSTRUCTOR  (r12v18 java.util.ArrayList A[REMOVE]) = 
                      (wrap: java.util.List<com.google.android.material.shape.ShapePath$ShadowCompatOperation> : 0x0136: IGET  (r15v16 java.util.List<com.google.android.material.shape.ShapePath$ShadowCompatOperation> A[REMOVE]) = (r11v5 'shapePath2' com.google.android.material.shape.ShapePath) com.google.android.material.shape.ShapePath.shadowCompatOperations java.util.List)
                     call: java.util.ArrayList.<init>(java.util.Collection):void type: CONSTRUCTOR)
                      (wrap: android.graphics.Matrix : 0x0131: CONSTRUCTOR  (r14v30 android.graphics.Matrix A[REMOVE]) = (r12v17 'matrix3' android.graphics.Matrix) call: android.graphics.Matrix.<init>(android.graphics.Matrix):void type: CONSTRUCTOR)
                     call: com.google.android.material.shape.ShapePath.1.<init>(com.google.android.material.shape.ShapePath, java.util.List, android.graphics.Matrix):void type: CONSTRUCTOR)
                     in method: com.google.android.material.shape.ShapeAppearancePathProvider.calculatePath(com.google.android.material.shape.ShapeAppearanceModel, float, android.graphics.RectF, com.google.android.material.shape.ShapeAppearancePathProvider$PathListener, android.graphics.Path):void, file: classes.dex
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:285)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:248)
                    	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:105)
                    	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                    	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:94)
                    	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:137)
                    	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:137)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                    	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:94)
                    	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:240)
                    	at jadx.core.dex.regions.loops.LoopRegion.generate(LoopRegion.java:173)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                    	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:261)
                    	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:254)
                    	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:349)
                    	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:302)
                    	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:271)
                    	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
                    	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                    	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                    Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.google.android.material.shape.ShapePath, state: GENERATED_AND_UNLOADED
                    	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:259)
                    	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:675)
                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:393)
                    	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:450)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:278)
                    	... 27 more
                    */
                /*
                // Method dump skipped, instructions count: 669
                */
                throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.shape.ShapeAppearancePathProvider.calculatePath(com.google.android.material.shape.ShapeAppearanceModel, float, android.graphics.RectF, com.google.android.material.shape.ShapeAppearancePathProvider$PathListener, android.graphics.Path):void");
            }

            public final boolean pathOverlapsCorner(Path path, int i) {
                this.cornerPath.reset();
                this.cornerPaths[i].applyToPath(this.cornerTransforms[i], this.cornerPath);
                RectF rectF = new RectF();
                path.computeBounds(rectF, true);
                this.cornerPath.computeBounds(rectF, true);
                path.op(this.cornerPath, Path.Op.INTERSECT);
                path.computeBounds(rectF, true);
                if (!rectF.isEmpty()) {
                    return true;
                }
                if (rectF.width() <= 1.0f || rectF.height() <= 1.0f) {
                    return false;
                }
                return true;
            }
        }
