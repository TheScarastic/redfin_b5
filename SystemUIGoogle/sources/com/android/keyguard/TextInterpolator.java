package com.android.keyguard;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.fonts.Font;
import android.graphics.text.PositionedGlyphs;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextShaper;
import android.util.MathUtils;
import com.android.internal.graphics.ColorUtils;
import com.android.keyguard.FontInterpolator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: TextInterpolator.kt */
/* loaded from: classes.dex */
public final class TextInterpolator {
    private final TextPaint basePaint;
    private Layout layout;
    private float progress;
    private final TextPaint targetPaint;
    private List<Line> lines = CollectionsKt__CollectionsKt.emptyList();
    private final FontInterpolator fontInterpolator = new FontInterpolator();
    private final TextPaint tmpDrawPaint = new TextPaint();
    private float[] tmpPositionArray = new float[20];

    public TextInterpolator(Layout layout) {
        Intrinsics.checkNotNullParameter(layout, "layout");
        this.basePaint = new TextPaint(layout.getPaint());
        this.targetPaint = new TextPaint(layout.getPaint());
        this.layout = layout;
        shapeText(layout);
    }

    public final TextPaint getTargetPaint() {
        return this.targetPaint;
    }

    /* compiled from: TextInterpolator.kt */
    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class FontRun {
        private Font baseFont;
        private final int end;
        private final int start;
        private Font targetFont;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof FontRun)) {
                return false;
            }
            FontRun fontRun = (FontRun) obj;
            return this.start == fontRun.start && this.end == fontRun.end && Intrinsics.areEqual(this.baseFont, fontRun.baseFont) && Intrinsics.areEqual(this.targetFont, fontRun.targetFont);
        }

        public int hashCode() {
            return (((((Integer.hashCode(this.start) * 31) + Integer.hashCode(this.end)) * 31) + this.baseFont.hashCode()) * 31) + this.targetFont.hashCode();
        }

        public String toString() {
            return "FontRun(start=" + this.start + ", end=" + this.end + ", baseFont=" + this.baseFont + ", targetFont=" + this.targetFont + ')';
        }

        public FontRun(int i, int i2, Font font, Font font2) {
            Intrinsics.checkNotNullParameter(font, "baseFont");
            Intrinsics.checkNotNullParameter(font2, "targetFont");
            this.start = i;
            this.end = i2;
            this.baseFont = font;
            this.targetFont = font2;
        }

        public final int getStart() {
            return this.start;
        }

        public final int getEnd() {
            return this.end;
        }

        public final Font getBaseFont() {
            return this.baseFont;
        }

        public final void setBaseFont(Font font) {
            Intrinsics.checkNotNullParameter(font, "<set-?>");
            this.baseFont = font;
        }

        public final Font getTargetFont() {
            return this.targetFont;
        }

        public final void setTargetFont(Font font) {
            Intrinsics.checkNotNullParameter(font, "<set-?>");
            this.targetFont = font;
        }

        public final int getLength() {
            return this.end - this.start;
        }
    }

    /* compiled from: TextInterpolator.kt */
    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class Run {
        private final float[] baseX;
        private final float[] baseY;
        private final List<FontRun> fontRuns;
        private final int[] glyphIds;
        private final float[] targetX;
        private final float[] targetY;

        public Run(int[] iArr, float[] fArr, float[] fArr2, float[] fArr3, float[] fArr4, List<FontRun> list) {
            Intrinsics.checkNotNullParameter(iArr, "glyphIds");
            Intrinsics.checkNotNullParameter(fArr, "baseX");
            Intrinsics.checkNotNullParameter(fArr2, "baseY");
            Intrinsics.checkNotNullParameter(fArr3, "targetX");
            Intrinsics.checkNotNullParameter(fArr4, "targetY");
            Intrinsics.checkNotNullParameter(list, "fontRuns");
            this.glyphIds = iArr;
            this.baseX = fArr;
            this.baseY = fArr2;
            this.targetX = fArr3;
            this.targetY = fArr4;
            this.fontRuns = list;
        }

        public final int[] getGlyphIds() {
            return this.glyphIds;
        }

        public final float[] getBaseX() {
            return this.baseX;
        }

        public final float[] getBaseY() {
            return this.baseY;
        }

        public final float[] getTargetX() {
            return this.targetX;
        }

        public final float[] getTargetY() {
            return this.targetY;
        }

        public final List<FontRun> getFontRuns() {
            return this.fontRuns;
        }
    }

    /* compiled from: TextInterpolator.kt */
    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class Line {
        private final List<Run> runs;

        public Line(List<Run> list) {
            Intrinsics.checkNotNullParameter(list, "runs");
            this.runs = list;
        }

        public final List<Run> getRuns() {
            return this.runs;
        }
    }

    public final float getProgress() {
        return this.progress;
    }

    public final void setProgress(float f) {
        this.progress = f;
    }

    public final Layout getLayout() {
        return this.layout;
    }

    public final void setLayout(Layout layout) {
        Intrinsics.checkNotNullParameter(layout, "value");
        this.layout = layout;
        shapeText(layout);
    }

    public final void onTargetPaintModified() {
        updatePositionsAndFonts(shapeText(getLayout(), this.targetPaint), false);
    }

    public final void rebase() {
        float f = this.progress;
        boolean z = true;
        if (!(f == 0.0f)) {
            if (f != 1.0f) {
                z = false;
            }
            if (z) {
                this.basePaint.set(this.targetPaint);
            } else {
                lerp(this.basePaint, this.targetPaint, f, this.tmpDrawPaint);
                this.basePaint.set(this.tmpDrawPaint);
            }
            for (Line line : this.lines) {
                for (Run run : line.getRuns()) {
                    int length = run.getBaseX().length - 1;
                    if (length >= 0) {
                        int i = 0;
                        while (true) {
                            int i2 = i + 1;
                            run.getBaseX()[i] = MathUtils.lerp(run.getBaseX()[i], run.getTargetX()[i], getProgress());
                            run.getBaseY()[i] = MathUtils.lerp(run.getBaseY()[i], run.getTargetY()[i], getProgress());
                            if (i2 > length) {
                                break;
                            }
                            i = i2;
                        }
                    }
                    for (FontRun fontRun : run.getFontRuns()) {
                        fontRun.setBaseFont(this.fontInterpolator.lerp(fontRun.getBaseFont(), fontRun.getTargetFont(), getProgress()));
                    }
                }
            }
            this.progress = 0.0f;
        }
    }

    public final void draw(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        lerp(this.basePaint, this.targetPaint, this.progress, this.tmpDrawPaint);
        int i = 0;
        for (Object obj : this.lines) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt__CollectionsKt.throwIndexOverflow();
            }
            for (Run run : ((Line) obj).getRuns()) {
                canvas.save();
                try {
                    canvas.translate(TextInterpolatorKt.getDrawOrigin(getLayout(), i), (float) getLayout().getLineBaseline(i));
                    for (FontRun fontRun : run.getFontRuns()) {
                        drawFontRun(canvas, run, fontRun, this.tmpDrawPaint);
                    }
                } finally {
                    canvas.restore();
                }
            }
            i = i2;
        }
    }

    private final void shapeText(Layout layout) {
        int[] iArr;
        float[] fArr;
        ArrayList arrayList;
        ArrayList arrayList2;
        Iterator it;
        Iterator it2;
        float[] fArr2;
        float[] fArr3;
        float[] fArr4;
        int i;
        int i2;
        int i3;
        Font font;
        PositionedGlyphs positionedGlyphs;
        TextInterpolator textInterpolator = this;
        List<List<PositionedGlyphs>> shapeText = textInterpolator.shapeText(layout, textInterpolator.basePaint);
        List<List<PositionedGlyphs>> shapeText2 = textInterpolator.shapeText(layout, textInterpolator.targetPaint);
        if (shapeText.size() == shapeText2.size()) {
            Iterator it3 = shapeText.iterator();
            Iterator it4 = shapeText2.iterator();
            int i4 = 10;
            ArrayList arrayList3 = new ArrayList(Math.min(CollectionsKt__IterablesKt.collectionSizeOrDefault(shapeText, 10), CollectionsKt__IterablesKt.collectionSizeOrDefault(shapeText2, 10)));
            int i5 = 0;
            while (it3.hasNext() && it4.hasNext()) {
                Object next = it3.next();
                List list = (List) it4.next();
                List list2 = (List) next;
                Iterator it5 = list2.iterator();
                Iterator it6 = list.iterator();
                ArrayList arrayList4 = new ArrayList(Math.min(CollectionsKt__IterablesKt.collectionSizeOrDefault(list2, i4), CollectionsKt__IterablesKt.collectionSizeOrDefault(list, i4)));
                while (it5.hasNext() && it6.hasNext()) {
                    Object next2 = it5.next();
                    PositionedGlyphs positionedGlyphs2 = (PositionedGlyphs) it6.next();
                    PositionedGlyphs positionedGlyphs3 = (PositionedGlyphs) next2;
                    if (positionedGlyphs3.glyphCount() == positionedGlyphs2.glyphCount()) {
                        int glyphCount = positionedGlyphs3.glyphCount();
                        int[] iArr2 = new int[glyphCount];
                        for (int i6 = 0; i6 < glyphCount; i6++) {
                            int glyphId = positionedGlyphs3.getGlyphId(i6);
                            if (glyphId == positionedGlyphs2.getGlyphId(i6)) {
                                Unit unit = Unit.INSTANCE;
                                iArr2[i6] = glyphId;
                            } else {
                                throw new IllegalArgumentException(("Inconsistent glyph ID at " + i6 + " in line " + textInterpolator.lines.size()).toString());
                            }
                        }
                        float[] fArr5 = new float[glyphCount];
                        for (int i7 = 0; i7 < glyphCount; i7++) {
                            fArr5[i7] = positionedGlyphs3.getGlyphX(i7);
                        }
                        float[] fArr6 = new float[glyphCount];
                        for (int i8 = 0; i8 < glyphCount; i8++) {
                            fArr6[i8] = positionedGlyphs3.getGlyphY(i8);
                        }
                        float[] fArr7 = new float[glyphCount];
                        for (int i9 = 0; i9 < glyphCount; i9++) {
                            fArr7[i9] = positionedGlyphs2.getGlyphX(i9);
                        }
                        float[] fArr8 = new float[glyphCount];
                        for (int i10 = 0; i10 < glyphCount; i10++) {
                            fArr8[i10] = positionedGlyphs2.getGlyphY(i10);
                        }
                        ArrayList arrayList5 = new ArrayList();
                        if (glyphCount != 0) {
                            Font font2 = positionedGlyphs3.getFont(0);
                            it2 = it5;
                            Font font3 = positionedGlyphs2.getFont(0);
                            FontInterpolator.Companion companion = FontInterpolator.Companion;
                            it = it6;
                            Intrinsics.checkNotNullExpressionValue(font2, "baseFont");
                            fArr4 = fArr7;
                            Intrinsics.checkNotNullExpressionValue(font3, "targetFont");
                            arrayList2 = arrayList3;
                            arrayList = arrayList4;
                            if (companion.canInterpolate(font2, font3)) {
                                if (1 < glyphCount) {
                                    fArr3 = fArr8;
                                    fArr2 = fArr6;
                                    Font font4 = font3;
                                    int i11 = 0;
                                    int i12 = 1;
                                    Font font5 = font2;
                                    i2 = i5;
                                    while (true) {
                                        fArr = fArr5;
                                        int i13 = i12 + 1;
                                        iArr = iArr2;
                                        Font font6 = positionedGlyphs3.getFont(i12);
                                        Font font7 = positionedGlyphs2.getFont(i12);
                                        if (font5 == font6) {
                                            positionedGlyphs = positionedGlyphs2;
                                            if (!(font4 == font7)) {
                                                throw new IllegalArgumentException(("Base font has not changed at " + i12 + " but target font has changed.").toString());
                                            }
                                        } else if (font4 != font7) {
                                            positionedGlyphs = positionedGlyphs2;
                                            Intrinsics.checkNotNullExpressionValue(font5, "baseFont");
                                            Intrinsics.checkNotNullExpressionValue(font4, "targetFont");
                                            arrayList5.add(new FontRun(i11, i12, font5, font4));
                                            int max = Math.max(i2, i12 - i11);
                                            FontInterpolator.Companion companion2 = FontInterpolator.Companion;
                                            Intrinsics.checkNotNullExpressionValue(font6, "baseFont");
                                            Intrinsics.checkNotNullExpressionValue(font7, "targetFont");
                                            if (companion2.canInterpolate(font6, font7)) {
                                                font4 = font7;
                                                i2 = max;
                                                i11 = i12;
                                                font5 = font6;
                                            } else {
                                                throw new IllegalArgumentException(("Cannot interpolate font at " + i12 + " (" + font6 + " vs " + font7 + ')').toString());
                                            }
                                        } else {
                                            throw new IllegalArgumentException(("Base font has changed at " + i12 + " but target font has not changed.").toString());
                                        }
                                        if (i13 >= glyphCount) {
                                            font = font5;
                                            i3 = i11;
                                            font3 = font4;
                                            break;
                                        }
                                        i12 = i13;
                                        fArr5 = fArr;
                                        iArr2 = iArr;
                                        positionedGlyphs3 = positionedGlyphs3;
                                        positionedGlyphs2 = positionedGlyphs;
                                    }
                                } else {
                                    fArr = fArr5;
                                    fArr3 = fArr8;
                                    fArr2 = fArr6;
                                    iArr = iArr2;
                                    font = font2;
                                    i2 = i5;
                                    i3 = 0;
                                }
                                Intrinsics.checkNotNullExpressionValue(font, "baseFont");
                                Intrinsics.checkNotNullExpressionValue(font3, "targetFont");
                                arrayList5.add(new FontRun(i3, glyphCount, font, font3));
                                i = Math.max(i2, glyphCount - i3);
                            } else {
                                throw new IllegalArgumentException(("Cannot interpolate font at 0 (" + font2 + " vs " + font3 + ')').toString());
                            }
                        } else {
                            fArr = fArr5;
                            fArr3 = fArr8;
                            arrayList2 = arrayList3;
                            fArr2 = fArr6;
                            it2 = it5;
                            it = it6;
                            arrayList = arrayList4;
                            fArr4 = fArr7;
                            iArr = iArr2;
                            i = i5;
                        }
                        arrayList.add(new Run(iArr, fArr, fArr2, fArr4, fArr3, arrayList5));
                        arrayList4 = arrayList;
                        it3 = it3;
                        it4 = it4;
                        it5 = it2;
                        it6 = it;
                        arrayList3 = arrayList2;
                        i5 = i;
                        textInterpolator = this;
                    } else {
                        throw new IllegalArgumentException(Intrinsics.stringPlus("Inconsistent glyph count at line ", Integer.valueOf(textInterpolator.lines.size())).toString());
                    }
                }
                arrayList3.add(new Line(arrayList4));
                arrayList3 = arrayList3;
                i5 = i5;
                it3 = it3;
                it4 = it4;
                i4 = 10;
            }
            textInterpolator.lines = arrayList3;
            int i14 = i5 * 2;
            if (textInterpolator.tmpPositionArray.length < i14) {
                textInterpolator.tmpPositionArray = new float[i14];
                return;
            }
            return;
        }
        throw new IllegalArgumentException("The new layout result has different line count.".toString());
    }

    private final void drawFontRun(Canvas canvas, Run run, FontRun fontRun, Paint paint) {
        int start = fontRun.getStart();
        int end = fontRun.getEnd();
        if (start < end) {
            int i = 0;
            while (true) {
                int i2 = start + 1;
                int i3 = i + 1;
                this.tmpPositionArray[i] = MathUtils.lerp(run.getBaseX()[start], run.getTargetX()[start], this.progress);
                int i4 = i3 + 1;
                this.tmpPositionArray[i3] = MathUtils.lerp(run.getBaseY()[start], run.getTargetY()[start], this.progress);
                if (i2 >= end) {
                    break;
                }
                start = i2;
                i = i4;
            }
        }
        canvas.drawGlyphs(run.getGlyphIds(), fontRun.getStart(), this.tmpPositionArray, 0, fontRun.getLength(), this.fontInterpolator.lerp(fontRun.getBaseFont(), fontRun.getTargetFont(), this.progress), paint);
    }

    private final void updatePositionsAndFonts(List<? extends List<PositionedGlyphs>> list, boolean z) {
        if (list.size() == this.lines.size()) {
            List<Line> list2 = this.lines;
            Iterator<T> it = list2.iterator();
            Iterator<T> it2 = list.iterator();
            int i = 10;
            ArrayList arrayList = new ArrayList(Math.min(CollectionsKt__IterablesKt.collectionSizeOrDefault(list2, 10), CollectionsKt__IterablesKt.collectionSizeOrDefault(list, 10)));
            while (it.hasNext() && it2.hasNext()) {
                Object next = it.next();
                List list3 = (List) it2.next();
                List<Run> runs = ((Line) next).getRuns();
                Iterator<T> it3 = runs.iterator();
                Iterator it4 = list3.iterator();
                ArrayList arrayList2 = new ArrayList(Math.min(CollectionsKt__IterablesKt.collectionSizeOrDefault(runs, i), CollectionsKt__IterablesKt.collectionSizeOrDefault(list3, i)));
                while (it3.hasNext() && it4.hasNext()) {
                    Object next2 = it3.next();
                    PositionedGlyphs positionedGlyphs = (PositionedGlyphs) it4.next();
                    Run run = (Run) next2;
                    if (positionedGlyphs.glyphCount() == run.getGlyphIds().length) {
                        for (FontRun fontRun : run.getFontRuns()) {
                            Font font = positionedGlyphs.getFont(fontRun.getStart());
                            int start = fontRun.getStart();
                            int end = fontRun.getEnd();
                            if (start < end) {
                                while (true) {
                                    int i2 = start + 1;
                                    if (!(positionedGlyphs.getGlyphId(fontRun.getStart()) == run.getGlyphIds()[fontRun.getStart()])) {
                                        throw new IllegalArgumentException(Intrinsics.stringPlus("The new layout has different glyph ID at ", Integer.valueOf(fontRun.getStart())).toString());
                                    } else if (!(font == positionedGlyphs.getFont(start))) {
                                        throw new IllegalArgumentException(("The new layout has different font run. " + font + " vs " + positionedGlyphs.getFont(start) + " at " + start).toString());
                                    } else if (i2 >= end) {
                                        break;
                                    } else {
                                        start = i2;
                                    }
                                }
                            }
                            FontInterpolator.Companion companion = FontInterpolator.Companion;
                            Intrinsics.checkNotNullExpressionValue(font, "newFont");
                            if (!companion.canInterpolate(font, fontRun.getBaseFont())) {
                                throw new IllegalArgumentException(("New font cannot be interpolated with existing font. " + font + ", " + fontRun.getBaseFont()).toString());
                            } else if (z) {
                                fontRun.setBaseFont(font);
                            } else {
                                fontRun.setTargetFont(font);
                            }
                        }
                        if (z) {
                            int length = run.getBaseX().length - 1;
                            if (length >= 0) {
                                int i3 = 0;
                                while (true) {
                                    int i4 = i3 + 1;
                                    run.getBaseX()[i3] = positionedGlyphs.getGlyphX(i3);
                                    run.getBaseY()[i3] = positionedGlyphs.getGlyphY(i3);
                                    if (i4 > length) {
                                        break;
                                    }
                                    i3 = i4;
                                }
                            }
                        } else {
                            int length2 = run.getBaseX().length - 1;
                            if (length2 >= 0) {
                                int i5 = 0;
                                while (true) {
                                    int i6 = i5 + 1;
                                    run.getTargetX()[i5] = positionedGlyphs.getGlyphX(i5);
                                    run.getTargetY()[i5] = positionedGlyphs.getGlyphY(i5);
                                    if (i6 > length2) {
                                        break;
                                    }
                                    i5 = i6;
                                }
                            }
                        }
                        arrayList2.add(Unit.INSTANCE);
                    } else {
                        throw new IllegalArgumentException("The new layout has different glyph count.".toString());
                    }
                }
                arrayList.add(arrayList2);
                i = 10;
            }
            return;
        }
        throw new IllegalStateException("The new layout result has different line count.".toString());
    }

    private final void lerp(Paint paint, Paint paint2, float f, Paint paint3) {
        paint3.set(paint);
        paint3.setTextSize(MathUtils.lerp(paint.getTextSize(), paint2.getTextSize(), f));
        paint3.setColor(ColorUtils.blendARGB(paint.getColor(), paint2.getColor(), f));
    }

    private final List<List<PositionedGlyphs>> shapeText(Layout layout, TextPaint textPaint) {
        ArrayList arrayList = new ArrayList();
        int lineCount = layout.getLineCount();
        if (lineCount > 0) {
            int i = 0;
            while (true) {
                int i2 = i + 1;
                int lineStart = layout.getLineStart(i);
                int lineEnd = layout.getLineEnd(i) - lineStart;
                ArrayList arrayList2 = new ArrayList();
                TextShaper.shapeText(layout.getText(), lineStart, lineEnd, layout.getTextDirectionHeuristic(), textPaint, new TextShaper.GlyphsConsumer(arrayList2) { // from class: com.android.keyguard.TextInterpolator$shapeText$3
                    final /* synthetic */ List<PositionedGlyphs> $runs;

                    /* access modifiers changed from: package-private */
                    {
                        this.$runs = r1;
                    }

                    public final void accept(int i3, int i4, PositionedGlyphs positionedGlyphs, TextPaint textPaint2) {
                        List<PositionedGlyphs> list = this.$runs;
                        Intrinsics.checkNotNullExpressionValue(positionedGlyphs, "glyphs");
                        list.add(positionedGlyphs);
                    }
                });
                arrayList.add(arrayList2);
                if (i2 >= lineCount) {
                    break;
                }
                i = i2;
            }
        }
        return arrayList;
    }
}
