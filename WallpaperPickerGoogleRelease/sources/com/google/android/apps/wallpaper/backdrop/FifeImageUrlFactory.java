package com.google.android.apps.wallpaper.backdrop;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import androidx.cardview.R$color;
import com.android.systemui.shared.R;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.util.DiskBasedLogger;
import com.android.wallpaper.util.ScreenSizeCalculator;
import com.android.wallpaper.util.WallpaperCropUtils;
import com.google.android.libraries.imageurl.FifeImageUrlUtil;
import com.google.photos.base.ImageUrlOptionsEnum;
import com.google.photos.base.ParsedImageUrlOptions$Builder;
import java.util.Objects;
/* loaded from: classes.dex */
public class FifeImageUrlFactory {
    public static FifeImageUrlFactory sInstance;
    public FifeImageUrlUtil mFifeImageUrlUtil = new FifeImageUrlUtil();

    public static FifeImageUrlFactory getInstance() {
        if (sInstance == null) {
            sInstance = new FifeImageUrlFactory();
        }
        return sInstance;
    }

    public final int calculateAndAddSize(Context context, FifeImageUrlUtil.Options options) {
        Point screenSize = ScreenSizeCalculator.getInstance().getScreenSize(((WindowManager) context.getSystemService("window")).getDefaultDisplay());
        int max = (int) (((double) ((float) Math.max(screenSize.x, screenSize.y))) * 1.5d);
        options.setSize(max, false);
        ParsedImageUrlOptions$Builder parsedImageUrlOptions$Builder = options.options;
        ImageUrlOptionsEnum imageUrlOptionsEnum = ImageUrlOptionsEnum.QUALITY_LEVEL;
        parsedImageUrlOptions$Builder.setOptionWithReadableError(imageUrlOptionsEnum, 90, "QualityLevel");
        options.options.setIsSigned(imageUrlOptionsEnum, false);
        return max;
    }

    public Uri createDesktopUri(Context context, String str) {
        Point screenSize = ScreenSizeCalculator.getInstance().getScreenSize(((WindowManager) context.getSystemService("window")).getDefaultDisplay());
        Uri parse = Uri.parse(str);
        FifeImageUrlUtil fifeImageUrlUtil = new FifeImageUrlUtil();
        if (fifeImageUrlUtil.isFifeHostedUri(parse)) {
            FifeImageUrlUtil.Options options = new FifeImageUrlUtil.Options();
            int i = screenSize.x;
            ParsedImageUrlOptions$Builder parsedImageUrlOptions$Builder = options.options;
            Integer valueOf = Integer.valueOf(i);
            ImageUrlOptionsEnum imageUrlOptionsEnum = ImageUrlOptionsEnum.WIDTH;
            parsedImageUrlOptions$Builder.setOptionWithReadableError(imageUrlOptionsEnum, valueOf, "Width");
            options.options.setIsSigned(imageUrlOptionsEnum, false);
            int i2 = screenSize.y;
            ParsedImageUrlOptions$Builder parsedImageUrlOptions$Builder2 = options.options;
            Integer valueOf2 = Integer.valueOf(i2);
            ImageUrlOptionsEnum imageUrlOptionsEnum2 = ImageUrlOptionsEnum.HEIGHT;
            parsedImageUrlOptions$Builder2.setOptionWithReadableError(imageUrlOptionsEnum2, valueOf2, "Height");
            options.options.setIsSigned(imageUrlOptionsEnum2, false);
            ParsedImageUrlOptions$Builder parsedImageUrlOptions$Builder3 = options.options;
            Boolean bool = Boolean.TRUE;
            ImageUrlOptionsEnum imageUrlOptionsEnum3 = ImageUrlOptionsEnum.CENTER_CROP;
            parsedImageUrlOptions$Builder3.setOptionWithReadableError(imageUrlOptionsEnum3, bool, "CenterCrop");
            options.options.setIsSigned(imageUrlOptionsEnum3, false);
            try {
                return fifeImageUrlUtil.mergeOptions(options, parse);
            } catch (FifeImageUrlUtil.InvalidUrlException unused) {
                DiskBasedLogger.e("FifeImageUrlFactory", "Unable to merge FIFE URL options for size " + screenSize + " on URL " + str, context);
            }
        }
        return parse;
    }

    public Uri createFullSizedUri(Context context, String str) {
        Uri parse = Uri.parse(str);
        if (this.mFifeImageUrlUtil.isFifeHostedUri(parse)) {
            FifeImageUrlUtil.Options options = new FifeImageUrlUtil.Options();
            int calculateAndAddSize = calculateAndAddSize(context, options);
            try {
                return this.mFifeImageUrlUtil.mergeOptions(options, parse);
            } catch (FifeImageUrlUtil.InvalidUrlException unused) {
                DiskBasedLogger.e("FifeImageUrlFactory", "Unable to merge FIFE URL options for size " + calculateAndAddSize + " on URL " + str, context);
            }
        }
        return Uri.parse(str);
    }

    public Uri createRotatingWallpaperUri(Context context, String str) {
        Point defaultCropSurfaceSize = WallpaperCropUtils.getDefaultCropSurfaceSize(context.getResources(), ((WindowManager) context.getSystemService("window")).getDefaultDisplay());
        Uri parse = Uri.parse(str);
        FifeImageUrlUtil fifeImageUrlUtil = new FifeImageUrlUtil();
        if (fifeImageUrlUtil.isFifeHostedUri(parse)) {
            FifeImageUrlUtil.Options options = new FifeImageUrlUtil.Options();
            calculateAndAddSize(context, options);
            try {
                return fifeImageUrlUtil.mergeOptions(options, parse);
            } catch (FifeImageUrlUtil.InvalidUrlException unused) {
                DiskBasedLogger.e("FifeImageUrlFactory", "Unable to merge FIFE URL options for size " + defaultCropSurfaceSize + " on URL " + str, context);
            }
        }
        return parse;
    }

    public Uri createThumbUri(Context context, String str) {
        Uri parse = Uri.parse(str);
        if (this.mFifeImageUrlUtil.isFifeHostedUri(parse)) {
            FifeImageUrlUtil.Options options = new FifeImageUrlUtil.Options();
            Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
            Point point = new Point();
            defaultDisplay.getSize(point);
            int i = point.x;
            int numColumns = R$color.getNumColumns(context, i, 3, 4);
            ScreenSizeCalculator.getInstance().getScreenSize(((WindowManager) context.getSystemService("window")).getDefaultDisplay());
            Objects.requireNonNull(InjectorProvider.getInjector().getFormFactorChecker(context));
            Resources resources = context.getResources();
            float dimensionPixelSize = ((float) (i - ((numColumns + 1) * resources.getDimensionPixelSize(R.dimen.grid_padding)))) / ((float) numColumns);
            Point point2 = new Point(Math.round(dimensionPixelSize), Math.round((dimensionPixelSize * ((float) resources.getDimensionPixelSize(R.dimen.grid_tile_aspect_height))) / ((float) resources.getDimensionPixelSize(R.dimen.grid_tile_aspect_width))));
            int max = Math.max(point2.x, point2.y);
            options.setSize(max, false);
            ParsedImageUrlOptions$Builder parsedImageUrlOptions$Builder = options.options;
            ImageUrlOptionsEnum imageUrlOptionsEnum = ImageUrlOptionsEnum.QUALITY_BUCKET;
            parsedImageUrlOptions$Builder.setOptionWithReadableError(imageUrlOptionsEnum, 1, "QualityBucket");
            options.options.setIsSigned(imageUrlOptionsEnum, false);
            try {
                return this.mFifeImageUrlUtil.mergeOptions(options, parse);
            } catch (FifeImageUrlUtil.InvalidUrlException unused) {
                DiskBasedLogger.e("FifeImageUrlFactory", "Unable to merge FIFE URL options for size " + max + " on URL " + str, context);
            }
        }
        return Uri.parse(str);
    }

    public Uri createTinyUri(String str) {
        Uri parse = Uri.parse(str);
        if (this.mFifeImageUrlUtil.isFifeHostedUri(parse)) {
            FifeImageUrlUtil.Options options = new FifeImageUrlUtil.Options();
            options.setSize(50, false);
            ParsedImageUrlOptions$Builder parsedImageUrlOptions$Builder = options.options;
            ImageUrlOptionsEnum imageUrlOptionsEnum = ImageUrlOptionsEnum.QUALITY_BUCKET;
            parsedImageUrlOptions$Builder.setOptionWithReadableError(imageUrlOptionsEnum, 1, "QualityBucket");
            options.options.setIsSigned(imageUrlOptionsEnum, false);
            try {
                return this.mFifeImageUrlUtil.mergeOptions(options, parse);
            } catch (FifeImageUrlUtil.InvalidUrlException unused) {
                Log.e("FifeImageUrlFactory", "Unable to merge FIFE URL options for size 50 on URL " + str);
            }
        }
        return Uri.parse(str);
    }
}
