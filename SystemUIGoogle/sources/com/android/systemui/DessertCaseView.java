package com.android.systemui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import java.util.HashSet;
import java.util.Set;
/* loaded from: classes.dex */
public class DessertCaseView extends FrameLayout {
    private static final int NUM_PASTRIES;
    private static final int[] PASTRIES;
    private static final int[] RARE_PASTRIES;
    private static final int[] XRARE_PASTRIES;
    private static final int[] XXRARE_PASTRIES;
    float[] hsv;
    private int mCellSize;
    private View[] mCells;
    private int mColumns;
    private SparseArray<Drawable> mDrawables;
    private final Set<Point> mFreeList;
    private final Handler mHandler;
    private int mHeight;
    private final Runnable mJuggle;
    private int mRows;
    private boolean mStarted;
    private int mWidth;
    private final HashSet<View> tmpSet;
    private static final String TAG = DessertCaseView.class.getSimpleName();
    private static final float[] MASK = {0.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 0.0f, 255.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    private static final float[] ALPHA_MASK = {0.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
    private static final float[] WHITE_MASK = {0.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 0.0f, 255.0f, -1.0f, 0.0f, 0.0f, 0.0f, 255.0f};

    static {
        int[] iArr = {R$drawable.dessert_kitkat, R$drawable.dessert_android};
        PASTRIES = iArr;
        int[] iArr2 = {R$drawable.dessert_cupcake, R$drawable.dessert_donut, R$drawable.dessert_eclair, R$drawable.dessert_froyo, R$drawable.dessert_gingerbread, R$drawable.dessert_honeycomb, R$drawable.dessert_ics, R$drawable.dessert_jellybean};
        RARE_PASTRIES = iArr2;
        int[] iArr3 = {R$drawable.dessert_petitfour, R$drawable.dessert_donutburger, R$drawable.dessert_flan, R$drawable.dessert_keylimepie};
        XRARE_PASTRIES = iArr3;
        int[] iArr4 = {R$drawable.dessert_zombiegingerbread, R$drawable.dessert_dandroid, R$drawable.dessert_jandycane};
        XXRARE_PASTRIES = iArr4;
        NUM_PASTRIES = iArr.length + iArr2.length + iArr3.length + iArr4.length;
    }

    public DessertCaseView(Context context) {
        this(context, null);
    }

    public DessertCaseView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DessertCaseView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDrawables = new SparseArray<>(NUM_PASTRIES);
        this.mFreeList = new HashSet();
        this.mHandler = new Handler();
        this.mJuggle = new Runnable() { // from class: com.android.systemui.DessertCaseView.1
            @Override // java.lang.Runnable
            public void run() {
                int childCount = DessertCaseView.this.getChildCount();
                for (int i2 = 0; i2 < 1; i2++) {
                    DessertCaseView.this.place(DessertCaseView.this.getChildAt((int) (Math.random() * ((double) childCount))), true);
                }
                DessertCaseView.this.fillFreeList();
                if (DessertCaseView.this.mStarted) {
                    DessertCaseView.this.mHandler.postDelayed(DessertCaseView.this.mJuggle, 2000);
                }
            }
        };
        this.hsv = new float[]{0.0f, 1.0f, 0.85f};
        this.tmpSet = new HashSet<>();
        Resources resources = getResources();
        this.mStarted = false;
        this.mCellSize = resources.getDimensionPixelSize(R$dimen.dessert_case_cell_size);
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (this.mCellSize < 512) {
            options.inSampleSize = 2;
        }
        options.inMutable = true;
        Bitmap bitmap = null;
        int[][] iArr = {PASTRIES, RARE_PASTRIES, XRARE_PASTRIES, XXRARE_PASTRIES};
        for (int i2 = 0; i2 < 4; i2++) {
            int[] iArr2 = iArr[i2];
            for (int i3 : iArr2) {
                options.inBitmap = bitmap;
                bitmap = BitmapFactory.decodeResource(resources, i3, options);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(resources, convertToAlphaMask(bitmap));
                bitmapDrawable.setColorFilter(new ColorMatrixColorFilter(ALPHA_MASK));
                int i4 = this.mCellSize;
                bitmapDrawable.setBounds(0, 0, i4, i4);
                this.mDrawables.append(i3, bitmapDrawable);
            }
        }
    }

    private static Bitmap convertToAlphaMask(Bitmap bitmap) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ALPHA_8);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(MASK));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return createBitmap;
    }

    public void start() {
        if (!this.mStarted) {
            this.mStarted = true;
            fillFreeList(2000);
        }
        this.mHandler.postDelayed(this.mJuggle, 5000);
    }

    public void stop() {
        this.mStarted = false;
        this.mHandler.removeCallbacks(this.mJuggle);
    }

    int pick(int[] iArr) {
        return iArr[(int) (Math.random() * ((double) iArr.length))];
    }

    int random_color() {
        this.hsv[0] = ((float) irand(0, 12)) * 30.0f;
        return Color.HSVToColor(this.hsv);
    }

    @Override // android.view.View
    protected synchronized void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (!(this.mWidth == i && this.mHeight == i2)) {
            boolean z = this.mStarted;
            if (z) {
                stop();
            }
            this.mWidth = i;
            this.mHeight = i2;
            this.mCells = null;
            removeAllViewsInLayout();
            this.mFreeList.clear();
            int i5 = this.mHeight;
            int i6 = this.mCellSize;
            int i7 = i5 / i6;
            this.mRows = i7;
            int i8 = this.mWidth / i6;
            this.mColumns = i8;
            this.mCells = new View[i7 * i8];
            setScaleX(0.25f);
            setScaleY(0.25f);
            setTranslationX(((float) (this.mWidth - (this.mCellSize * this.mColumns))) * 0.5f * 0.25f);
            setTranslationY(((float) (this.mHeight - (this.mCellSize * this.mRows))) * 0.5f * 0.25f);
            for (int i9 = 0; i9 < this.mRows; i9++) {
                for (int i10 = 0; i10 < this.mColumns; i10++) {
                    this.mFreeList.add(new Point(i10, i9));
                }
            }
            if (z) {
                start();
            }
        }
    }

    public void fillFreeList() {
        fillFreeList(500);
    }

    public synchronized void fillFreeList(int i) {
        Drawable drawable;
        Context context = getContext();
        int i2 = this.mCellSize;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(i2, i2);
        while (!this.mFreeList.isEmpty()) {
            Point next = this.mFreeList.iterator().next();
            this.mFreeList.remove(next);
            if (this.mCells[(next.y * this.mColumns) + next.x] == null) {
                final ImageView imageView = new ImageView(context);
                imageView.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.DessertCaseView.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        DessertCaseView.this.place(imageView, true);
                        DessertCaseView.this.postDelayed(new Runnable() { // from class: com.android.systemui.DessertCaseView.2.1
                            @Override // java.lang.Runnable
                            public void run() {
                                DessertCaseView.this.fillFreeList();
                            }
                        }, 250);
                    }
                });
                imageView.setBackgroundColor(random_color());
                float frand = frand();
                if (frand < 5.0E-4f) {
                    drawable = this.mDrawables.get(pick(XXRARE_PASTRIES));
                } else if (frand < 0.005f) {
                    drawable = this.mDrawables.get(pick(XRARE_PASTRIES));
                } else if (frand < 0.5f) {
                    drawable = this.mDrawables.get(pick(RARE_PASTRIES));
                } else {
                    drawable = frand < 0.7f ? this.mDrawables.get(pick(PASTRIES)) : null;
                }
                if (drawable != null) {
                    imageView.getOverlay().add(drawable);
                }
                int i3 = this.mCellSize;
                layoutParams.height = i3;
                layoutParams.width = i3;
                addView(imageView, layoutParams);
                place(imageView, next, false);
                if (i > 0) {
                    float intValue = (float) ((Integer) imageView.getTag(33554434)).intValue();
                    float f = 0.5f * intValue;
                    imageView.setScaleX(f);
                    imageView.setScaleY(f);
                    imageView.setAlpha(0.0f);
                    imageView.animate().withLayer().scaleX(intValue).scaleY(intValue).alpha(1.0f).setDuration((long) i);
                }
            }
        }
    }

    public void place(View view, boolean z) {
        place(view, new Point(irand(0, this.mColumns), irand(0, this.mRows)), z);
    }

    private final Animator.AnimatorListener makeHardwareLayerListener(final View view) {
        return new AnimatorListenerAdapter() { // from class: com.android.systemui.DessertCaseView.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                view.setLayerType(2, null);
                view.buildLayer();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                view.setLayerType(0, null);
            }
        };
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x008e A[Catch: all -> 0x0200, TryCatch #0 {, blocks: (B:4:0x0007, B:6:0x0019, B:8:0x0021, B:11:0x0041, B:13:0x0046, B:18:0x0054, B:20:0x0059, B:25:0x0067, B:27:0x006c, B:31:0x0074, B:33:0x008e, B:35:0x009e, B:36:0x00a3, B:37:0x00a9, B:38:0x00af, B:40:0x00b5, B:42:0x00c3, B:44:0x00de, B:46:0x00e6, B:47:0x0119, B:49:0x0121, B:51:0x0125, B:52:0x013b, B:54:0x0147, B:55:0x01dc), top: B:60:0x0007 }] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00b5 A[Catch: all -> 0x0200, TryCatch #0 {, blocks: (B:4:0x0007, B:6:0x0019, B:8:0x0021, B:11:0x0041, B:13:0x0046, B:18:0x0054, B:20:0x0059, B:25:0x0067, B:27:0x006c, B:31:0x0074, B:33:0x008e, B:35:0x009e, B:36:0x00a3, B:37:0x00a9, B:38:0x00af, B:40:0x00b5, B:42:0x00c3, B:44:0x00de, B:46:0x00e6, B:47:0x0119, B:49:0x0121, B:51:0x0125, B:52:0x013b, B:54:0x0147, B:55:0x01dc), top: B:60:0x0007 }] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0125 A[Catch: all -> 0x0200, LOOP:4: B:50:0x0123->B:51:0x0125, LOOP_END, TryCatch #0 {, blocks: (B:4:0x0007, B:6:0x0019, B:8:0x0021, B:11:0x0041, B:13:0x0046, B:18:0x0054, B:20:0x0059, B:25:0x0067, B:27:0x006c, B:31:0x0074, B:33:0x008e, B:35:0x009e, B:36:0x00a3, B:37:0x00a9, B:38:0x00af, B:40:0x00b5, B:42:0x00c3, B:44:0x00de, B:46:0x00e6, B:47:0x0119, B:49:0x0121, B:51:0x0125, B:52:0x013b, B:54:0x0147, B:55:0x01dc), top: B:60:0x0007 }] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0147 A[Catch: all -> 0x0200, TryCatch #0 {, blocks: (B:4:0x0007, B:6:0x0019, B:8:0x0021, B:11:0x0041, B:13:0x0046, B:18:0x0054, B:20:0x0059, B:25:0x0067, B:27:0x006c, B:31:0x0074, B:33:0x008e, B:35:0x009e, B:36:0x00a3, B:37:0x00a9, B:38:0x00af, B:40:0x00b5, B:42:0x00c3, B:44:0x00de, B:46:0x00e6, B:47:0x0119, B:49:0x0121, B:51:0x0125, B:52:0x013b, B:54:0x0147, B:55:0x01dc), top: B:60:0x0007 }] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x01dc A[Catch: all -> 0x0200, TRY_LEAVE, TryCatch #0 {, blocks: (B:4:0x0007, B:6:0x0019, B:8:0x0021, B:11:0x0041, B:13:0x0046, B:18:0x0054, B:20:0x0059, B:25:0x0067, B:27:0x006c, B:31:0x0074, B:33:0x008e, B:35:0x009e, B:36:0x00a3, B:37:0x00a9, B:38:0x00af, B:40:0x00b5, B:42:0x00c3, B:44:0x00de, B:46:0x00e6, B:47:0x0119, B:49:0x0121, B:51:0x0125, B:52:0x013b, B:54:0x0147, B:55:0x01dc), top: B:60:0x0007 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void place(android.view.View r17, android.graphics.Point r18, boolean r19) {
        /*
        // Method dump skipped, instructions count: 515
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.DessertCaseView.place(android.view.View, android.graphics.Point, boolean):void");
    }

    private Point[] getOccupied(View view) {
        int intValue = ((Integer) view.getTag(33554434)).intValue();
        Point point = (Point) view.getTag(33554433);
        if (point == null || intValue == 0) {
            return new Point[0];
        }
        Point[] pointArr = new Point[intValue * intValue];
        int i = 0;
        for (int i2 = 0; i2 < intValue; i2++) {
            int i3 = 0;
            while (i3 < intValue) {
                pointArr[i] = new Point(point.x + i2, point.y + i3);
                i3++;
                i++;
            }
        }
        return pointArr;
    }

    static float frand() {
        return (float) Math.random();
    }

    static float frand(float f, float f2) {
        return (frand() * (f2 - f)) + f;
    }

    static int irand(int i, int i2) {
        return (int) frand((float) i, (float) i2);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /* loaded from: classes.dex */
    public static class RescalingContainer extends FrameLayout {
        private DessertCaseView mView;

        public RescalingContainer(Context context) {
            super(context);
            setSystemUiVisibility(5638);
        }

        public void setView(DessertCaseView dessertCaseView) {
            addView(dessertCaseView);
            this.mView = dessertCaseView;
        }

        @Override // android.widget.FrameLayout, android.view.View, android.view.ViewGroup
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            float f = (float) (i3 - i);
            float f2 = (float) (i4 - i2);
            int i5 = (int) ((f / 0.25f) / 2.0f);
            int i6 = (int) ((f2 / 0.25f) / 2.0f);
            int i7 = (int) (((float) i) + (f * 0.5f));
            int i8 = (int) (((float) i2) + (f2 * 0.5f));
            this.mView.layout(i7 - i5, i8 - i6, i7 + i5, i8 + i6);
        }
    }
}
