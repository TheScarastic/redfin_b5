package android.support.media;

import android.util.Log;
import com.android.systemui.shared.system.SysUiStatsLog;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
public class ExifInterface {
    public static final Charset ASCII;
    public static final ExifTag[][] EXIF_TAGS;
    public static final byte[] IDENTIFIER_EXIF_APP1;
    public final HashMap<String, ExifAttribute>[] mAttributes = new HashMap[EXIF_TAGS.length];
    public ByteOrder mExifByteOrder = ByteOrder.BIG_ENDIAN;
    public int mExifOffset;
    public final String mFilename;
    public int mMimeType;
    public int mOrfMakerNoteOffset;
    public int mOrfThumbnailLength;
    public int mOrfThumbnailOffset;
    public int mRw2JpgFromRawOffset;
    public static final List<Integer> ROTATION_ORDER = Arrays.asList(1, 6, 3, 8);
    public static final List<Integer> FLIPPED_ROTATION_ORDER = Arrays.asList(2, 7, 4, 5);
    public static final int[] BITS_PER_SAMPLE_RGB = {8, 8, 8};
    public static final int[] BITS_PER_SAMPLE_GREYSCALE_2 = {8};
    public static final byte[] JPEG_SIGNATURE = {-1, -40, -1};
    public static final byte[] ORF_MAKER_NOTE_HEADER_1 = {79, 76, 89, 77, 80, 0};
    public static final byte[] ORF_MAKER_NOTE_HEADER_2 = {79, 76, 89, 77, 80, 85, 83, 0, 73, 73};
    public static final String[] IFD_FORMAT_NAMES = {"", "BYTE", "STRING", "USHORT", "ULONG", "URATIONAL", "SBYTE", "UNDEFINED", "SSHORT", "SLONG", "SRATIONAL", "SINGLE", "DOUBLE"};
    public static final int[] IFD_FORMAT_BYTES_PER_FORMAT = {0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8, 1};
    public static final byte[] EXIF_ASCII_PREFIX = {65, 83, 67, 73, 73, 0, 0, 0};
    public static final ExifTag TAG_RAF_IMAGE_SIZE = new ExifTag("StripOffsets", 273, 3, null);
    public static final ExifTag[] EXIF_POINTER_TAGS = {new ExifTag("SubIFDPointer", 330, 4, null), new ExifTag("ExifIFDPointer", 34665, 4, null), new ExifTag("GPSInfoIFDPointer", 34853, 4, null), new ExifTag("InteroperabilityIFDPointer", 40965, 4, null), new ExifTag("CameraSettingsIFDPointer", 8224, 1, null), new ExifTag("ImageProcessingIFDPointer", 8256, 1, null)};
    public static final HashMap<Integer, ExifTag>[] sExifTagMapsForReading = new HashMap[10];
    public static final HashMap<String, ExifTag>[] sExifTagMapsForWriting = new HashMap[10];
    public static final HashSet<String> sTagSetForCompatibility = new HashSet<>(Arrays.asList("FNumber", "DigitalZoomRatio", "ExposureTime", "SubjectDistance", "GPSTimeStamp"));
    public static final HashMap<Integer, Integer> sExifPointerTagMap = new HashMap<>();

    /* loaded from: classes.dex */
    public static class Rational {
        public final long denominator;
        public final long numerator;

        public Rational(long j, long j2, AnonymousClass1 r7) {
            if (j2 == 0) {
                this.numerator = 0;
                this.denominator = 1;
                return;
            }
            this.numerator = j;
            this.denominator = j2;
        }

        public String toString() {
            return this.numerator + "/" + this.denominator;
        }
    }

    static {
        ExifTag[] exifTagArr = {new ExifTag("NewSubfileType", 254, 4, null), new ExifTag("SubfileType", 255, 4, null), new ExifTag("ImageWidth", 256, 3, 4, null), new ExifTag("ImageLength", 257, 3, 4, null), new ExifTag("BitsPerSample", 258, 3, null), new ExifTag("Compression", 259, 3, null), new ExifTag("PhotometricInterpretation", SysUiStatsLog.LAUNCHER_SNAPSHOT, 3, null), new ExifTag("ImageDescription", 270, 2, null), new ExifTag("Make", 271, 2, null), new ExifTag("Model", 272, 2, null), new ExifTag("StripOffsets", 273, 3, 4, null), new ExifTag("Orientation", 274, 3, null), new ExifTag("SamplesPerPixel", SysUiStatsLog.MEDIAOUTPUT_OP_SWITCH_REPORTED, 3, null), new ExifTag("RowsPerStrip", 278, 3, 4, null), new ExifTag("StripByteCounts", 279, 3, 4, null), new ExifTag("XResolution", 282, 5, null), new ExifTag("YResolution", 283, 5, null), new ExifTag("PlanarConfiguration", 284, 3, null), new ExifTag("ResolutionUnit", 296, 3, null), new ExifTag("TransferFunction", 301, 3, null), new ExifTag("Software", 305, 2, null), new ExifTag("DateTime", 306, 2, null), new ExifTag("Artist", 315, 2, null), new ExifTag("WhitePoint", 318, 5, null), new ExifTag("PrimaryChromaticities", 319, 5, null), new ExifTag("SubIFDPointer", 330, 4, null), new ExifTag("JPEGInterchangeFormat", 513, 4, null), new ExifTag("JPEGInterchangeFormatLength", 514, 4, null), new ExifTag("YCbCrCoefficients", 529, 5, null), new ExifTag("YCbCrSubSampling", 530, 3, null), new ExifTag("YCbCrPositioning", 531, 3, null), new ExifTag("ReferenceBlackWhite", 532, 5, null), new ExifTag("Copyright", 33432, 2, null), new ExifTag("ExifIFDPointer", 34665, 4, null), new ExifTag("GPSInfoIFDPointer", 34853, 4, null), new ExifTag("SensorTopBorder", 4, 4, null), new ExifTag("SensorLeftBorder", 5, 4, null), new ExifTag("SensorBottomBorder", 6, 4, null), new ExifTag("SensorRightBorder", 7, 4, null), new ExifTag("ISO", 23, 3, null), new ExifTag("JpgFromRaw", 46, 7, null)};
        EXIF_TAGS = new ExifTag[][]{exifTagArr, new ExifTag[]{new ExifTag("ExposureTime", 33434, 5, null), new ExifTag("FNumber", 33437, 5, null), new ExifTag("ExposureProgram", 34850, 3, null), new ExifTag("SpectralSensitivity", 34852, 2, null), new ExifTag("PhotographicSensitivity", 34855, 3, null), new ExifTag("OECF", 34856, 7, null), new ExifTag("ExifVersion", 36864, 2, null), new ExifTag("DateTimeOriginal", 36867, 2, null), new ExifTag("DateTimeDigitized", 36868, 2, null), new ExifTag("ComponentsConfiguration", 37121, 7, null), new ExifTag("CompressedBitsPerPixel", 37122, 5, null), new ExifTag("ShutterSpeedValue", 37377, 10, null), new ExifTag("ApertureValue", 37378, 5, null), new ExifTag("BrightnessValue", 37379, 10, null), new ExifTag("ExposureBiasValue", 37380, 10, null), new ExifTag("MaxApertureValue", 37381, 5, null), new ExifTag("SubjectDistance", 37382, 5, null), new ExifTag("MeteringMode", 37383, 3, null), new ExifTag("LightSource", 37384, 3, null), new ExifTag("Flash", 37385, 3, null), new ExifTag("FocalLength", 37386, 5, null), new ExifTag("SubjectArea", 37396, 3, null), new ExifTag("MakerNote", 37500, 7, null), new ExifTag("UserComment", 37510, 7, null), new ExifTag("SubSecTime", 37520, 2, null), new ExifTag("SubSecTimeOriginal", 37521, 2, null), new ExifTag("SubSecTimeDigitized", 37522, 2, null), new ExifTag("FlashpixVersion", 40960, 7, null), new ExifTag("ColorSpace", 40961, 3, null), new ExifTag("PixelXDimension", 40962, 3, 4, null), new ExifTag("PixelYDimension", 40963, 3, 4, null), new ExifTag("RelatedSoundFile", 40964, 2, null), new ExifTag("InteroperabilityIFDPointer", 40965, 4, null), new ExifTag("FlashEnergy", 41483, 5, null), new ExifTag("SpatialFrequencyResponse", 41484, 7, null), new ExifTag("FocalPlaneXResolution", 41486, 5, null), new ExifTag("FocalPlaneYResolution", 41487, 5, null), new ExifTag("FocalPlaneResolutionUnit", 41488, 3, null), new ExifTag("SubjectLocation", 41492, 3, null), new ExifTag("ExposureIndex", 41493, 5, null), new ExifTag("SensingMethod", 41495, 3, null), new ExifTag("FileSource", 41728, 7, null), new ExifTag("SceneType", 41729, 7, null), new ExifTag("CFAPattern", 41730, 7, null), new ExifTag("CustomRendered", 41985, 3, null), new ExifTag("ExposureMode", 41986, 3, null), new ExifTag("WhiteBalance", 41987, 3, null), new ExifTag("DigitalZoomRatio", 41988, 5, null), new ExifTag("FocalLengthIn35mmFilm", 41989, 3, null), new ExifTag("SceneCaptureType", 41990, 3, null), new ExifTag("GainControl", 41991, 3, null), new ExifTag("Contrast", 41992, 3, null), new ExifTag("Saturation", 41993, 3, null), new ExifTag("Sharpness", 41994, 3, null), new ExifTag("DeviceSettingDescription", 41995, 7, null), new ExifTag("SubjectDistanceRange", 41996, 3, null), new ExifTag("ImageUniqueID", 42016, 2, null), new ExifTag("DNGVersion", 50706, 1, null), new ExifTag("DefaultCropSize", 50720, 3, 4, null)}, new ExifTag[]{new ExifTag("GPSVersionID", 0, 1, null), new ExifTag("GPSLatitudeRef", 1, 2, null), new ExifTag("GPSLatitude", 2, 5, null), new ExifTag("GPSLongitudeRef", 3, 2, null), new ExifTag("GPSLongitude", 4, 5, null), new ExifTag("GPSAltitudeRef", 5, 1, null), new ExifTag("GPSAltitude", 6, 5, null), new ExifTag("GPSTimeStamp", 7, 5, null), new ExifTag("GPSSatellites", 8, 2, null), new ExifTag("GPSStatus", 9, 2, null), new ExifTag("GPSMeasureMode", 10, 2, null), new ExifTag("GPSDOP", 11, 5, null), new ExifTag("GPSSpeedRef", 12, 2, null), new ExifTag("GPSSpeed", 13, 5, null), new ExifTag("GPSTrackRef", 14, 2, null), new ExifTag("GPSTrack", 15, 5, null), new ExifTag("GPSImgDirectionRef", 16, 2, null), new ExifTag("GPSImgDirection", 17, 5, null), new ExifTag("GPSMapDatum", 18, 2, null), new ExifTag("GPSDestLatitudeRef", 19, 2, null), new ExifTag("GPSDestLatitude", 20, 5, null), new ExifTag("GPSDestLongitudeRef", 21, 2, null), new ExifTag("GPSDestLongitude", 22, 5, null), new ExifTag("GPSDestBearingRef", 23, 2, null), new ExifTag("GPSDestBearing", 24, 5, null), new ExifTag("GPSDestDistanceRef", 25, 2, null), new ExifTag("GPSDestDistance", 26, 5, null), new ExifTag("GPSProcessingMethod", 27, 7, null), new ExifTag("GPSAreaInformation", 28, 7, null), new ExifTag("GPSDateStamp", 29, 2, null), new ExifTag("GPSDifferential", 30, 3, null)}, new ExifTag[]{new ExifTag("InteroperabilityIndex", 1, 2, null)}, new ExifTag[]{new ExifTag("NewSubfileType", 254, 4, null), new ExifTag("SubfileType", 255, 4, null), new ExifTag("ThumbnailImageWidth", 256, 3, 4, null), new ExifTag("ThumbnailImageLength", 257, 3, 4, null), new ExifTag("BitsPerSample", 258, 3, null), new ExifTag("Compression", 259, 3, null), new ExifTag("PhotometricInterpretation", SysUiStatsLog.LAUNCHER_SNAPSHOT, 3, null), new ExifTag("ImageDescription", 270, 2, null), new ExifTag("Make", 271, 2, null), new ExifTag("Model", 272, 2, null), new ExifTag("StripOffsets", 273, 3, 4, null), new ExifTag("Orientation", 274, 3, null), new ExifTag("SamplesPerPixel", SysUiStatsLog.MEDIAOUTPUT_OP_SWITCH_REPORTED, 3, null), new ExifTag("RowsPerStrip", 278, 3, 4, null), new ExifTag("StripByteCounts", 279, 3, 4, null), new ExifTag("XResolution", 282, 5, null), new ExifTag("YResolution", 283, 5, null), new ExifTag("PlanarConfiguration", 284, 3, null), new ExifTag("ResolutionUnit", 296, 3, null), new ExifTag("TransferFunction", 301, 3, null), new ExifTag("Software", 305, 2, null), new ExifTag("DateTime", 306, 2, null), new ExifTag("Artist", 315, 2, null), new ExifTag("WhitePoint", 318, 5, null), new ExifTag("PrimaryChromaticities", 319, 5, null), new ExifTag("SubIFDPointer", 330, 4, null), new ExifTag("JPEGInterchangeFormat", 513, 4, null), new ExifTag("JPEGInterchangeFormatLength", 514, 4, null), new ExifTag("YCbCrCoefficients", 529, 5, null), new ExifTag("YCbCrSubSampling", 530, 3, null), new ExifTag("YCbCrPositioning", 531, 3, null), new ExifTag("ReferenceBlackWhite", 532, 5, null), new ExifTag("Copyright", 33432, 2, null), new ExifTag("ExifIFDPointer", 34665, 4, null), new ExifTag("GPSInfoIFDPointer", 34853, 4, null), new ExifTag("DNGVersion", 50706, 1, null), new ExifTag("DefaultCropSize", 50720, 3, 4, null)}, exifTagArr, new ExifTag[]{new ExifTag("ThumbnailImage", 256, 7, null), new ExifTag("CameraSettingsIFDPointer", 8224, 4, null), new ExifTag("ImageProcessingIFDPointer", 8256, 4, null)}, new ExifTag[]{new ExifTag("PreviewImageStart", 257, 4, null), new ExifTag("PreviewImageLength", 258, 4, null)}, new ExifTag[]{new ExifTag("AspectFrame", 4371, 3, null)}, new ExifTag[]{new ExifTag("ColorSpace", 55, 3, null)}};
        Charset forName = Charset.forName("US-ASCII");
        ASCII = forName;
        IDENTIFIER_EXIF_APP1 = "Exif\u0000\u0000".getBytes(forName);
        new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").setTimeZone(TimeZone.getTimeZone("UTC"));
        int i = 0;
        while (true) {
            ExifTag[][] exifTagArr2 = EXIF_TAGS;
            if (i < exifTagArr2.length) {
                sExifTagMapsForReading[i] = new HashMap<>();
                sExifTagMapsForWriting[i] = new HashMap<>();
                ExifTag[] exifTagArr3 = exifTagArr2[i];
                for (ExifTag exifTag : exifTagArr3) {
                    sExifTagMapsForReading[i].put(Integer.valueOf(exifTag.number), exifTag);
                    sExifTagMapsForWriting[i].put(exifTag.name, exifTag);
                }
                i++;
            } else {
                HashMap<Integer, Integer> hashMap = sExifPointerTagMap;
                ExifTag[] exifTagArr4 = EXIF_POINTER_TAGS;
                hashMap.put(Integer.valueOf(exifTagArr4[0].number), 5);
                hashMap.put(Integer.valueOf(exifTagArr4[1].number), 1);
                hashMap.put(Integer.valueOf(exifTagArr4[2].number), 2);
                hashMap.put(Integer.valueOf(exifTagArr4[3].number), 3);
                hashMap.put(Integer.valueOf(exifTagArr4[4].number), 7);
                hashMap.put(Integer.valueOf(exifTagArr4[5].number), 8);
                Pattern.compile(".*[1-9].*");
                Pattern.compile("^([0-9][0-9]):([0-9][0-9]):([0-9][0-9])$");
                return;
            }
        }
    }

    public ExifInterface(String str) throws IOException {
        Throwable th;
        if (str != null) {
            FileInputStream fileInputStream = null;
            this.mFilename = str;
            try {
                FileInputStream fileInputStream2 = new FileInputStream(str);
                try {
                    loadAttributes(fileInputStream2);
                    try {
                        fileInputStream2.close();
                    } catch (RuntimeException e) {
                        throw e;
                    } catch (Exception unused) {
                    }
                } catch (Throwable th2) {
                    th = th2;
                    fileInputStream = fileInputStream2;
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (RuntimeException e2) {
                            throw e2;
                        } catch (Exception unused2) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        } else {
            throw new IllegalArgumentException("filename cannot be null");
        }
    }

    public static long[] convertToLongArray(Object obj) {
        if (obj instanceof int[]) {
            int[] iArr = (int[]) obj;
            long[] jArr = new long[iArr.length];
            for (int i = 0; i < iArr.length; i++) {
                jArr[i] = (long) iArr[i];
            }
            return jArr;
        } else if (obj instanceof long[]) {
            return (long[]) obj;
        } else {
            return null;
        }
    }

    public final void addDefaultValuesForCompatibility() {
        String attribute = getAttribute("DateTimeOriginal");
        if (attribute != null && getAttribute("DateTime") == null) {
            this.mAttributes[0].put("DateTime", ExifAttribute.createString(attribute));
        }
        if (getAttribute("ImageWidth") == null) {
            this.mAttributes[0].put("ImageWidth", ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        if (getAttribute("ImageLength") == null) {
            this.mAttributes[0].put("ImageLength", ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        if (getAttribute("Orientation") == null) {
            this.mAttributes[0].put("Orientation", ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        if (getAttribute("LightSource") == null) {
            this.mAttributes[1].put("LightSource", ExifAttribute.createULong(0, this.mExifByteOrder));
        }
    }

    public String getAttribute(String str) {
        ExifAttribute exifAttribute = getExifAttribute(str);
        if (exifAttribute != null) {
            if (!sTagSetForCompatibility.contains(str)) {
                return exifAttribute.getStringValue(this.mExifByteOrder);
            }
            if (str.equals("GPSTimeStamp")) {
                int i = exifAttribute.format;
                if (i == 5 || i == 10) {
                    Rational[] rationalArr = (Rational[]) exifAttribute.getValue(this.mExifByteOrder);
                    if (rationalArr != null && rationalArr.length == 3) {
                        return String.format("%02d:%02d:%02d", Integer.valueOf((int) (((float) rationalArr[0].numerator) / ((float) rationalArr[0].denominator))), Integer.valueOf((int) (((float) rationalArr[1].numerator) / ((float) rationalArr[1].denominator))), Integer.valueOf((int) (((float) rationalArr[2].numerator) / ((float) rationalArr[2].denominator))));
                    }
                    StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Invalid GPS Timestamp array. array=");
                    m.append(Arrays.toString(rationalArr));
                    Log.w("ExifInterface", m.toString());
                    return null;
                }
                StringBuilder m2 = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("GPS Timestamp format is not rational. format=");
                m2.append(exifAttribute.format);
                Log.w("ExifInterface", m2.toString());
                return null;
            }
            try {
                return Double.toString(exifAttribute.getDoubleValue(this.mExifByteOrder));
            } catch (NumberFormatException unused) {
            }
        }
        return null;
    }

    public final ExifAttribute getExifAttribute(String str) {
        if ("ISOSpeedRatings".equals(str)) {
            str = "PhotographicSensitivity";
        }
        for (int i = 0; i < EXIF_TAGS.length; i++) {
            ExifAttribute exifAttribute = this.mAttributes[i].get(str);
            if (exifAttribute != null) {
                return exifAttribute;
            }
        }
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:62:0x011b, code lost:
        r10.mByteOrder = r9.mExifByteOrder;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x011f, code lost:
        return;
     */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0056 A[FALL_THROUGH] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00ec  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x00fd A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void getJpegAttributes(android.support.media.ExifInterface.ByteOrderedDataInputStream r10, int r11, int r12) throws java.io.IOException {
        /*
        // Method dump skipped, instructions count: 402
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.media.ExifInterface.getJpegAttributes(android.support.media.ExifInterface$ByteOrderedDataInputStream, int, int):void");
    }

    public final int getMimeType(BufferedInputStream bufferedInputStream) throws IOException {
        boolean z;
        boolean z2;
        boolean z3;
        bufferedInputStream.mark(5000);
        byte[] bArr = new byte[5000];
        bufferedInputStream.read(bArr);
        bufferedInputStream.reset();
        int i = 0;
        while (true) {
            byte[] bArr2 = JPEG_SIGNATURE;
            z = true;
            if (i >= bArr2.length) {
                z2 = true;
                break;
            } else if (bArr[i] != bArr2[i]) {
                z2 = false;
                break;
            } else {
                i++;
            }
        }
        if (z2) {
            return 4;
        }
        byte[] bytes = "FUJIFILMCCD-RAW".getBytes(Charset.defaultCharset());
        int i2 = 0;
        while (true) {
            if (i2 >= bytes.length) {
                z3 = true;
                break;
            } else if (bArr[i2] != bytes[i2]) {
                z3 = false;
                break;
            } else {
                i2++;
            }
        }
        if (z3) {
            return 9;
        }
        ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(bArr);
        ByteOrder readByteOrder = readByteOrder(byteOrderedDataInputStream);
        this.mExifByteOrder = readByteOrder;
        byteOrderedDataInputStream.mByteOrder = readByteOrder;
        short readShort = byteOrderedDataInputStream.readShort();
        byteOrderedDataInputStream.close();
        if (readShort == 20306 || readShort == 21330) {
            return 7;
        }
        ByteOrderedDataInputStream byteOrderedDataInputStream2 = new ByteOrderedDataInputStream(bArr);
        ByteOrder readByteOrder2 = readByteOrder(byteOrderedDataInputStream2);
        this.mExifByteOrder = readByteOrder2;
        byteOrderedDataInputStream2.mByteOrder = readByteOrder2;
        short readShort2 = byteOrderedDataInputStream2.readShort();
        byteOrderedDataInputStream2.close();
        if (readShort2 != 85) {
            z = false;
        }
        if (z) {
            return 10;
        }
        return 0;
    }

    public final void getOrfAttributes(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        getRawAttributes(byteOrderedDataInputStream);
        ExifAttribute exifAttribute = this.mAttributes[1].get("MakerNote");
        if (exifAttribute != null) {
            ByteOrderedDataInputStream byteOrderedDataInputStream2 = new ByteOrderedDataInputStream(exifAttribute.bytes);
            byteOrderedDataInputStream2.mByteOrder = this.mExifByteOrder;
            byte[] bArr = ORF_MAKER_NOTE_HEADER_1;
            byte[] bArr2 = new byte[bArr.length];
            byteOrderedDataInputStream2.readFully(bArr2);
            byteOrderedDataInputStream2.seek(0);
            byte[] bArr3 = ORF_MAKER_NOTE_HEADER_2;
            byte[] bArr4 = new byte[bArr3.length];
            byteOrderedDataInputStream2.readFully(bArr4);
            if (Arrays.equals(bArr2, bArr)) {
                byteOrderedDataInputStream2.seek(8);
            } else if (Arrays.equals(bArr4, bArr3)) {
                byteOrderedDataInputStream2.seek(12);
            }
            readImageFileDirectory(byteOrderedDataInputStream2, 6);
            ExifAttribute exifAttribute2 = this.mAttributes[7].get("PreviewImageStart");
            ExifAttribute exifAttribute3 = this.mAttributes[7].get("PreviewImageLength");
            if (!(exifAttribute2 == null || exifAttribute3 == null)) {
                this.mAttributes[5].put("JPEGInterchangeFormat", exifAttribute2);
                this.mAttributes[5].put("JPEGInterchangeFormatLength", exifAttribute3);
            }
            ExifAttribute exifAttribute4 = this.mAttributes[8].get("AspectFrame");
            if (exifAttribute4 != null) {
                int[] iArr = (int[]) exifAttribute4.getValue(this.mExifByteOrder);
                if (iArr == null || iArr.length != 4) {
                    StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Invalid aspect frame values. frame=");
                    m.append(Arrays.toString(iArr));
                    Log.w("ExifInterface", m.toString());
                } else if (iArr[2] > iArr[0] && iArr[3] > iArr[1]) {
                    int i = (iArr[2] - iArr[0]) + 1;
                    int i2 = (iArr[3] - iArr[1]) + 1;
                    if (i < i2) {
                        int i3 = i + i2;
                        i2 = i3 - i2;
                        i = i3 - i2;
                    }
                    ExifAttribute createUShort = ExifAttribute.createUShort(i, this.mExifByteOrder);
                    ExifAttribute createUShort2 = ExifAttribute.createUShort(i2, this.mExifByteOrder);
                    this.mAttributes[0].put("ImageWidth", createUShort);
                    this.mAttributes[0].put("ImageLength", createUShort2);
                }
            }
        }
    }

    public final void getRafAttributes(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        byteOrderedDataInputStream.skipBytes(84);
        byte[] bArr = new byte[4];
        byte[] bArr2 = new byte[4];
        byteOrderedDataInputStream.read(bArr);
        byteOrderedDataInputStream.skipBytes(4);
        byteOrderedDataInputStream.read(bArr2);
        int i = ByteBuffer.wrap(bArr).getInt();
        int i2 = ByteBuffer.wrap(bArr2).getInt();
        getJpegAttributes(byteOrderedDataInputStream, i, 5);
        byteOrderedDataInputStream.seek((long) i2);
        byteOrderedDataInputStream.mByteOrder = ByteOrder.BIG_ENDIAN;
        int readInt = byteOrderedDataInputStream.readInt();
        for (int i3 = 0; i3 < readInt; i3++) {
            int readUnsignedShort = byteOrderedDataInputStream.readUnsignedShort();
            int readUnsignedShort2 = byteOrderedDataInputStream.readUnsignedShort();
            if (readUnsignedShort == TAG_RAF_IMAGE_SIZE.number) {
                short readShort = byteOrderedDataInputStream.readShort();
                short readShort2 = byteOrderedDataInputStream.readShort();
                ExifAttribute createUShort = ExifAttribute.createUShort(readShort, this.mExifByteOrder);
                ExifAttribute createUShort2 = ExifAttribute.createUShort(readShort2, this.mExifByteOrder);
                this.mAttributes[0].put("ImageLength", createUShort);
                this.mAttributes[0].put("ImageWidth", createUShort2);
                return;
            }
            byteOrderedDataInputStream.skipBytes(readUnsignedShort2);
        }
    }

    public final void getRawAttributes(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        ExifAttribute exifAttribute;
        parseTiffHeaders(byteOrderedDataInputStream, byteOrderedDataInputStream.available());
        readImageFileDirectory(byteOrderedDataInputStream, 0);
        updateImageSizeValues(byteOrderedDataInputStream, 0);
        updateImageSizeValues(byteOrderedDataInputStream, 5);
        updateImageSizeValues(byteOrderedDataInputStream, 4);
        swapBasedOnImageSize(0, 5);
        swapBasedOnImageSize(0, 4);
        swapBasedOnImageSize(5, 4);
        ExifAttribute exifAttribute2 = this.mAttributes[1].get("PixelXDimension");
        ExifAttribute exifAttribute3 = this.mAttributes[1].get("PixelYDimension");
        if (!(exifAttribute2 == null || exifAttribute3 == null)) {
            this.mAttributes[0].put("ImageWidth", exifAttribute2);
            this.mAttributes[0].put("ImageLength", exifAttribute3);
        }
        if (this.mAttributes[4].isEmpty() && isThumbnail(this.mAttributes[5])) {
            HashMap<String, ExifAttribute>[] hashMapArr = this.mAttributes;
            hashMapArr[4] = hashMapArr[5];
            hashMapArr[5] = new HashMap<>();
        }
        if (!isThumbnail(this.mAttributes[4])) {
            Log.d("ExifInterface", "No image meets the size requirements of a thumbnail image.");
        }
        if (this.mMimeType == 8 && (exifAttribute = this.mAttributes[1].get("MakerNote")) != null) {
            ByteOrderedDataInputStream byteOrderedDataInputStream2 = new ByteOrderedDataInputStream(exifAttribute.bytes);
            byteOrderedDataInputStream2.mByteOrder = this.mExifByteOrder;
            byteOrderedDataInputStream2.seek(6);
            readImageFileDirectory(byteOrderedDataInputStream2, 9);
            ExifAttribute exifAttribute4 = this.mAttributes[9].get("ColorSpace");
            if (exifAttribute4 != null) {
                this.mAttributes[1].put("ColorSpace", exifAttribute4);
            }
        }
    }

    public final void getRw2Attributes(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        getRawAttributes(byteOrderedDataInputStream);
        if (this.mAttributes[0].get("JpgFromRaw") != null) {
            getJpegAttributes(byteOrderedDataInputStream, this.mRw2JpgFromRawOffset, 5);
        }
        ExifAttribute exifAttribute = this.mAttributes[0].get("ISO");
        ExifAttribute exifAttribute2 = this.mAttributes[1].get("PhotographicSensitivity");
        if (exifAttribute != null && exifAttribute2 == null) {
            this.mAttributes[1].put("PhotographicSensitivity", exifAttribute);
        }
    }

    public final void handleThumbnailFromJfif(ByteOrderedDataInputStream byteOrderedDataInputStream, HashMap hashMap) throws IOException {
        int i;
        ExifAttribute exifAttribute = (ExifAttribute) hashMap.get("JPEGInterchangeFormat");
        ExifAttribute exifAttribute2 = (ExifAttribute) hashMap.get("JPEGInterchangeFormatLength");
        if (exifAttribute != null && exifAttribute2 != null) {
            int intValue = exifAttribute.getIntValue(this.mExifByteOrder);
            int min = Math.min(exifAttribute2.getIntValue(this.mExifByteOrder), byteOrderedDataInputStream.available() - intValue);
            int i2 = this.mMimeType;
            if (i2 == 4 || i2 == 9 || i2 == 10) {
                i = this.mExifOffset;
            } else {
                if (i2 == 7) {
                    i = this.mOrfMakerNoteOffset;
                }
                if (intValue > 0 && min > 0 && this.mFilename == null) {
                    byteOrderedDataInputStream.seek((long) intValue);
                    byteOrderedDataInputStream.readFully(new byte[min]);
                    return;
                }
                return;
            }
            intValue += i;
            if (intValue > 0) {
            }
        }
    }

    public final boolean isThumbnail(HashMap hashMap) throws IOException {
        ExifAttribute exifAttribute = (ExifAttribute) hashMap.get("ImageLength");
        ExifAttribute exifAttribute2 = (ExifAttribute) hashMap.get("ImageWidth");
        if (exifAttribute == null || exifAttribute2 == null) {
            return false;
        }
        return exifAttribute.getIntValue(this.mExifByteOrder) <= 512 && exifAttribute2.getIntValue(this.mExifByteOrder) <= 512;
    }

    public final void loadAttributes(InputStream inputStream) throws IOException {
        for (int i = 0; i < EXIF_TAGS.length; i++) {
            try {
                this.mAttributes[i] = new HashMap<>();
            } catch (IOException unused) {
            } catch (Throwable th) {
                addDefaultValuesForCompatibility();
                throw th;
            }
        }
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 5000);
        this.mMimeType = getMimeType(bufferedInputStream);
        ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(bufferedInputStream);
        switch (this.mMimeType) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 5:
            case 6:
            case 8:
            case 11:
                getRawAttributes(byteOrderedDataInputStream);
                break;
            case 4:
                getJpegAttributes(byteOrderedDataInputStream, 0, 0);
                break;
            case 7:
                getOrfAttributes(byteOrderedDataInputStream);
                break;
            case 9:
                getRafAttributes(byteOrderedDataInputStream);
                break;
            case 10:
                getRw2Attributes(byteOrderedDataInputStream);
                break;
        }
        setThumbnailData(byteOrderedDataInputStream);
        addDefaultValuesForCompatibility();
    }

    public final void parseTiffHeaders(ByteOrderedDataInputStream byteOrderedDataInputStream, int i) throws IOException {
        ByteOrder readByteOrder = readByteOrder(byteOrderedDataInputStream);
        this.mExifByteOrder = readByteOrder;
        byteOrderedDataInputStream.mByteOrder = readByteOrder;
        int readUnsignedShort = byteOrderedDataInputStream.readUnsignedShort();
        int i2 = this.mMimeType;
        if (i2 == 7 || i2 == 10 || readUnsignedShort == 42) {
            int readInt = byteOrderedDataInputStream.readInt();
            if (readInt < 8 || readInt >= i) {
                throw new IOException(ExifInterface$$ExternalSyntheticOutline0.m("Invalid first Ifd offset: ", readInt));
            }
            int i3 = readInt - 8;
            if (i3 > 0 && byteOrderedDataInputStream.skipBytes(i3) != i3) {
                throw new IOException(ExifInterface$$ExternalSyntheticOutline0.m("Couldn't jump to first Ifd: ", i3));
            }
            return;
        }
        StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Invalid start code: ");
        m.append(Integer.toHexString(readUnsignedShort));
        throw new IOException(m.toString());
    }

    public final ByteOrder readByteOrder(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        short readShort = byteOrderedDataInputStream.readShort();
        if (readShort == 18761) {
            return ByteOrder.LITTLE_ENDIAN;
        }
        if (readShort == 19789) {
            return ByteOrder.BIG_ENDIAN;
        }
        StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Invalid byte order: ");
        m.append(Integer.toHexString(readShort));
        throw new IOException(m.toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:56:0x0108  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0111  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void readImageFileDirectory(android.support.media.ExifInterface.ByteOrderedDataInputStream r25, int r26) throws java.io.IOException {
        /*
        // Method dump skipped, instructions count: 740
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.media.ExifInterface.readImageFileDirectory(android.support.media.ExifInterface$ByteOrderedDataInputStream, int):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x005e, code lost:
        if (java.util.Arrays.equals(r1, android.support.media.ExifInterface.BITS_PER_SAMPLE_GREYSCALE_2) != false) goto L_0x006a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0066, code lost:
        if (java.util.Arrays.equals(r1, r5) != false) goto L_0x006a;
     */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x006c  */
    /* JADX WARNING: Removed duplicated region for block: B:52:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void setThumbnailData(android.support.media.ExifInterface.ByteOrderedDataInputStream r12) throws java.io.IOException {
        /*
        // Method dump skipped, instructions count: 223
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.media.ExifInterface.setThumbnailData(android.support.media.ExifInterface$ByteOrderedDataInputStream):void");
    }

    public final void swapBasedOnImageSize(int i, int i2) throws IOException {
        if (!this.mAttributes[i].isEmpty() && !this.mAttributes[i2].isEmpty()) {
            ExifAttribute exifAttribute = this.mAttributes[i].get("ImageLength");
            ExifAttribute exifAttribute2 = this.mAttributes[i].get("ImageWidth");
            ExifAttribute exifAttribute3 = this.mAttributes[i2].get("ImageLength");
            ExifAttribute exifAttribute4 = this.mAttributes[i2].get("ImageWidth");
            if (exifAttribute != null && exifAttribute2 != null && exifAttribute3 != null && exifAttribute4 != null) {
                int intValue = exifAttribute.getIntValue(this.mExifByteOrder);
                int intValue2 = exifAttribute2.getIntValue(this.mExifByteOrder);
                int intValue3 = exifAttribute3.getIntValue(this.mExifByteOrder);
                int intValue4 = exifAttribute4.getIntValue(this.mExifByteOrder);
                if (intValue < intValue3 && intValue2 < intValue4) {
                    HashMap<String, ExifAttribute>[] hashMapArr = this.mAttributes;
                    HashMap<String, ExifAttribute> hashMap = hashMapArr[i];
                    hashMapArr[i] = hashMapArr[i2];
                    hashMapArr[i2] = hashMap;
                }
            }
        }
    }

    public final void updateImageSizeValues(ByteOrderedDataInputStream byteOrderedDataInputStream, int i) throws IOException {
        ExifAttribute exifAttribute;
        ExifAttribute exifAttribute2;
        ExifAttribute exifAttribute3;
        ExifAttribute exifAttribute4 = this.mAttributes[i].get("DefaultCropSize");
        ExifAttribute exifAttribute5 = this.mAttributes[i].get("SensorTopBorder");
        ExifAttribute exifAttribute6 = this.mAttributes[i].get("SensorLeftBorder");
        ExifAttribute exifAttribute7 = this.mAttributes[i].get("SensorBottomBorder");
        ExifAttribute exifAttribute8 = this.mAttributes[i].get("SensorRightBorder");
        if (exifAttribute4 != null) {
            if (exifAttribute4.format == 5) {
                Rational[] rationalArr = (Rational[]) exifAttribute4.getValue(this.mExifByteOrder);
                if (rationalArr == null || rationalArr.length != 2) {
                    StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Invalid crop size values. cropSize=");
                    m.append(Arrays.toString(rationalArr));
                    Log.w("ExifInterface", m.toString());
                    return;
                }
                exifAttribute3 = ExifAttribute.createURational(rationalArr[0], this.mExifByteOrder);
                exifAttribute2 = ExifAttribute.createURational(rationalArr[1], this.mExifByteOrder);
            } else {
                int[] iArr = (int[]) exifAttribute4.getValue(this.mExifByteOrder);
                if (iArr == null || iArr.length != 2) {
                    StringBuilder m2 = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Invalid crop size values. cropSize=");
                    m2.append(Arrays.toString(iArr));
                    Log.w("ExifInterface", m2.toString());
                    return;
                }
                exifAttribute3 = ExifAttribute.createUShort(iArr[0], this.mExifByteOrder);
                exifAttribute2 = ExifAttribute.createUShort(iArr[1], this.mExifByteOrder);
            }
            this.mAttributes[i].put("ImageWidth", exifAttribute3);
            this.mAttributes[i].put("ImageLength", exifAttribute2);
        } else if (exifAttribute5 == null || exifAttribute6 == null || exifAttribute7 == null || exifAttribute8 == null) {
            ExifAttribute exifAttribute9 = this.mAttributes[i].get("ImageLength");
            ExifAttribute exifAttribute10 = this.mAttributes[i].get("ImageWidth");
            if ((exifAttribute9 == null || exifAttribute10 == null) && (exifAttribute = this.mAttributes[i].get("JPEGInterchangeFormat")) != null) {
                getJpegAttributes(byteOrderedDataInputStream, exifAttribute.getIntValue(this.mExifByteOrder), i);
            }
        } else {
            int intValue = exifAttribute5.getIntValue(this.mExifByteOrder);
            int intValue2 = exifAttribute7.getIntValue(this.mExifByteOrder);
            int intValue3 = exifAttribute8.getIntValue(this.mExifByteOrder);
            int intValue4 = exifAttribute6.getIntValue(this.mExifByteOrder);
            if (intValue2 > intValue && intValue3 > intValue4) {
                ExifAttribute createUShort = ExifAttribute.createUShort(intValue2 - intValue, this.mExifByteOrder);
                ExifAttribute createUShort2 = ExifAttribute.createUShort(intValue3 - intValue4, this.mExifByteOrder);
                this.mAttributes[i].put("ImageLength", createUShort);
                this.mAttributes[i].put("ImageWidth", createUShort2);
            }
        }
    }

    /* loaded from: classes.dex */
    public static class ByteOrderedDataInputStream extends InputStream implements DataInput {
        public ByteOrder mByteOrder;
        public DataInputStream mDataInputStream;
        public final int mLength;
        public int mPosition;
        public static final ByteOrder LITTLE_ENDIAN = ByteOrder.LITTLE_ENDIAN;
        public static final ByteOrder BIG_ENDIAN = ByteOrder.BIG_ENDIAN;

        public ByteOrderedDataInputStream(InputStream inputStream) throws IOException {
            this.mByteOrder = ByteOrder.BIG_ENDIAN;
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            this.mDataInputStream = dataInputStream;
            int available = dataInputStream.available();
            this.mLength = available;
            this.mPosition = 0;
            this.mDataInputStream.mark(available);
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            return this.mDataInputStream.available();
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            this.mPosition++;
            return this.mDataInputStream.read();
        }

        @Override // java.io.DataInput
        public boolean readBoolean() throws IOException {
            this.mPosition++;
            return this.mDataInputStream.readBoolean();
        }

        @Override // java.io.DataInput
        public byte readByte() throws IOException {
            int i = this.mPosition + 1;
            this.mPosition = i;
            if (i <= this.mLength) {
                int read = this.mDataInputStream.read();
                if (read >= 0) {
                    return (byte) read;
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        @Override // java.io.DataInput
        public char readChar() throws IOException {
            this.mPosition += 2;
            return this.mDataInputStream.readChar();
        }

        @Override // java.io.DataInput
        public double readDouble() throws IOException {
            return Double.longBitsToDouble(readLong());
        }

        @Override // java.io.DataInput
        public float readFloat() throws IOException {
            return Float.intBitsToFloat(readInt());
        }

        @Override // java.io.DataInput
        public void readFully(byte[] bArr, int i, int i2) throws IOException {
            int i3 = this.mPosition + i2;
            this.mPosition = i3;
            if (i3 > this.mLength) {
                throw new EOFException();
            } else if (this.mDataInputStream.read(bArr, i, i2) != i2) {
                throw new IOException("Couldn't read up to the length of buffer");
            }
        }

        @Override // java.io.DataInput
        public int readInt() throws IOException {
            int i = this.mPosition + 4;
            this.mPosition = i;
            if (i <= this.mLength) {
                int read = this.mDataInputStream.read();
                int read2 = this.mDataInputStream.read();
                int read3 = this.mDataInputStream.read();
                int read4 = this.mDataInputStream.read();
                if ((read | read2 | read3 | read4) >= 0) {
                    ByteOrder byteOrder = this.mByteOrder;
                    if (byteOrder == LITTLE_ENDIAN) {
                        return (read4 << 24) + (read3 << 16) + (read2 << 8) + read;
                    }
                    if (byteOrder == BIG_ENDIAN) {
                        return (read << 24) + (read2 << 16) + (read3 << 8) + read4;
                    }
                    StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Invalid byte order: ");
                    m.append(this.mByteOrder);
                    throw new IOException(m.toString());
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        @Override // java.io.DataInput
        public String readLine() throws IOException {
            Log.d("ExifInterface", "Currently unsupported");
            return null;
        }

        @Override // java.io.DataInput
        public long readLong() throws IOException {
            int i = this.mPosition + 8;
            this.mPosition = i;
            if (i <= this.mLength) {
                int read = this.mDataInputStream.read();
                int read2 = this.mDataInputStream.read();
                int read3 = this.mDataInputStream.read();
                int read4 = this.mDataInputStream.read();
                int read5 = this.mDataInputStream.read();
                int read6 = this.mDataInputStream.read();
                int read7 = this.mDataInputStream.read();
                int read8 = this.mDataInputStream.read();
                if ((read | read2 | read3 | read4 | read5 | read6 | read7 | read8) >= 0) {
                    ByteOrder byteOrder = this.mByteOrder;
                    if (byteOrder == LITTLE_ENDIAN) {
                        return (((long) read8) << 56) + (((long) read7) << 48) + (((long) read6) << 40) + (((long) read5) << 32) + (((long) read4) << 24) + (((long) read3) << 16) + (((long) read2) << 8) + ((long) read);
                    }
                    if (byteOrder == BIG_ENDIAN) {
                        return (((long) read) << 56) + (((long) read2) << 48) + (((long) read3) << 40) + (((long) read4) << 32) + (((long) read5) << 24) + (((long) read6) << 16) + (((long) read7) << 8) + ((long) read8);
                    }
                    StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Invalid byte order: ");
                    m.append(this.mByteOrder);
                    throw new IOException(m.toString());
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        @Override // java.io.DataInput
        public short readShort() throws IOException {
            int i;
            int i2 = this.mPosition + 2;
            this.mPosition = i2;
            if (i2 <= this.mLength) {
                int read = this.mDataInputStream.read();
                int read2 = this.mDataInputStream.read();
                if ((read | read2) >= 0) {
                    ByteOrder byteOrder = this.mByteOrder;
                    if (byteOrder == LITTLE_ENDIAN) {
                        i = (read2 << 8) + read;
                    } else if (byteOrder == BIG_ENDIAN) {
                        i = (read << 8) + read2;
                    } else {
                        StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Invalid byte order: ");
                        m.append(this.mByteOrder);
                        throw new IOException(m.toString());
                    }
                    return (short) i;
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        @Override // java.io.DataInput
        public String readUTF() throws IOException {
            this.mPosition += 2;
            return this.mDataInputStream.readUTF();
        }

        @Override // java.io.DataInput
        public int readUnsignedByte() throws IOException {
            this.mPosition++;
            return this.mDataInputStream.readUnsignedByte();
        }

        public long readUnsignedInt() throws IOException {
            return ((long) readInt()) & 4294967295L;
        }

        @Override // java.io.DataInput
        public int readUnsignedShort() throws IOException {
            int i = this.mPosition + 2;
            this.mPosition = i;
            if (i <= this.mLength) {
                int read = this.mDataInputStream.read();
                int read2 = this.mDataInputStream.read();
                if ((read | read2) >= 0) {
                    ByteOrder byteOrder = this.mByteOrder;
                    if (byteOrder == LITTLE_ENDIAN) {
                        return (read2 << 8) + read;
                    }
                    if (byteOrder == BIG_ENDIAN) {
                        return (read << 8) + read2;
                    }
                    StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Invalid byte order: ");
                    m.append(this.mByteOrder);
                    throw new IOException(m.toString());
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        public void seek(long j) throws IOException {
            int i = this.mPosition;
            if (((long) i) > j) {
                this.mPosition = 0;
                this.mDataInputStream.reset();
                this.mDataInputStream.mark(this.mLength);
            } else {
                j -= (long) i;
            }
            int i2 = (int) j;
            if (skipBytes(i2) != i2) {
                throw new IOException("Couldn't seek up to the byteCount");
            }
        }

        @Override // java.io.DataInput
        public int skipBytes(int i) throws IOException {
            int min = Math.min(i, this.mLength - this.mPosition);
            int i2 = 0;
            while (i2 < min) {
                i2 += this.mDataInputStream.skipBytes(min - i2);
            }
            this.mPosition += i2;
            return i2;
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i, int i2) throws IOException {
            int read = this.mDataInputStream.read(bArr, i, i2);
            this.mPosition += read;
            return read;
        }

        @Override // java.io.DataInput
        public void readFully(byte[] bArr) throws IOException {
            int length = this.mPosition + bArr.length;
            this.mPosition = length;
            if (length > this.mLength) {
                throw new EOFException();
            } else if (this.mDataInputStream.read(bArr, 0, bArr.length) != bArr.length) {
                throw new IOException("Couldn't read up to the length of buffer");
            }
        }

        public ByteOrderedDataInputStream(byte[] bArr) throws IOException {
            this(new ByteArrayInputStream(bArr));
        }
    }

    /* loaded from: classes.dex */
    public static class ExifAttribute {
        public final byte[] bytes;
        public final int format;
        public final int numberOfComponents;

        public ExifAttribute(int i, int i2, byte[] bArr) {
            this.format = i;
            this.numberOfComponents = i2;
            this.bytes = bArr;
        }

        public static ExifAttribute createString(String str) {
            byte[] bytes = (str + (char) 0).getBytes(ExifInterface.ASCII);
            return new ExifAttribute(2, bytes.length, bytes);
        }

        public static ExifAttribute createULong(long j, ByteOrder byteOrder) {
            long[] jArr = {j};
            ByteBuffer wrap = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[4] * 1]);
            wrap.order(byteOrder);
            for (int i = 0; i < 1; i++) {
                wrap.putInt((int) jArr[i]);
            }
            return new ExifAttribute(4, 1, wrap.array());
        }

        public static ExifAttribute createURational(Rational rational, ByteOrder byteOrder) {
            Rational[] rationalArr = {rational};
            ByteBuffer wrap = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[5] * 1]);
            wrap.order(byteOrder);
            for (int i = 0; i < 1; i++) {
                Rational rational2 = rationalArr[i];
                wrap.putInt((int) rational2.numerator);
                wrap.putInt((int) rational2.denominator);
            }
            return new ExifAttribute(5, 1, wrap.array());
        }

        public static ExifAttribute createUShort(int i, ByteOrder byteOrder) {
            int[] iArr = {i};
            ByteBuffer wrap = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[3] * 1]);
            wrap.order(byteOrder);
            for (int i2 = 0; i2 < 1; i2++) {
                wrap.putShort((short) iArr[i2]);
            }
            return new ExifAttribute(3, 1, wrap.array());
        }

        public double getDoubleValue(ByteOrder byteOrder) {
            Object value = getValue(byteOrder);
            if (value == null) {
                throw new NumberFormatException("NULL can't be converted to a double value");
            } else if (value instanceof String) {
                return Double.parseDouble((String) value);
            } else {
                if (value instanceof long[]) {
                    long[] jArr = (long[]) value;
                    if (jArr.length == 1) {
                        return (double) jArr[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else if (value instanceof int[]) {
                    int[] iArr = (int[]) value;
                    if (iArr.length == 1) {
                        return (double) iArr[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else if (value instanceof double[]) {
                    double[] dArr = (double[]) value;
                    if (dArr.length == 1) {
                        return dArr[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else if (value instanceof Rational[]) {
                    Rational[] rationalArr = (Rational[]) value;
                    if (rationalArr.length == 1) {
                        Rational rational = rationalArr[0];
                        return ((double) rational.numerator) / ((double) rational.denominator);
                    }
                    throw new NumberFormatException("There are more than one component");
                } else {
                    throw new NumberFormatException("Couldn't find a double value");
                }
            }
        }

        public int getIntValue(ByteOrder byteOrder) {
            Object value = getValue(byteOrder);
            if (value == null) {
                throw new NumberFormatException("NULL can't be converted to a integer value");
            } else if (value instanceof String) {
                return Integer.parseInt((String) value);
            } else {
                if (value instanceof long[]) {
                    long[] jArr = (long[]) value;
                    if (jArr.length == 1) {
                        return (int) jArr[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else if (value instanceof int[]) {
                    int[] iArr = (int[]) value;
                    if (iArr.length == 1) {
                        return iArr[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else {
                    throw new NumberFormatException("Couldn't find a integer value");
                }
            }
        }

        public String getStringValue(ByteOrder byteOrder) {
            Object value = getValue(byteOrder);
            if (value == null) {
                return null;
            }
            if (value instanceof String) {
                return (String) value;
            }
            StringBuilder sb = new StringBuilder();
            int i = 0;
            if (value instanceof long[]) {
                long[] jArr = (long[]) value;
                while (i < jArr.length) {
                    sb.append(jArr[i]);
                    i++;
                    if (i != jArr.length) {
                        sb.append(",");
                    }
                }
                return sb.toString();
            } else if (value instanceof int[]) {
                int[] iArr = (int[]) value;
                while (i < iArr.length) {
                    sb.append(iArr[i]);
                    i++;
                    if (i != iArr.length) {
                        sb.append(",");
                    }
                }
                return sb.toString();
            } else if (value instanceof double[]) {
                double[] dArr = (double[]) value;
                while (i < dArr.length) {
                    sb.append(dArr[i]);
                    i++;
                    if (i != dArr.length) {
                        sb.append(",");
                    }
                }
                return sb.toString();
            } else if (!(value instanceof Rational[])) {
                return null;
            } else {
                Rational[] rationalArr = (Rational[]) value;
                while (i < rationalArr.length) {
                    sb.append(rationalArr[i].numerator);
                    sb.append('/');
                    sb.append(rationalArr[i].denominator);
                    i++;
                    if (i != rationalArr.length) {
                        sb.append(",");
                    }
                }
                return sb.toString();
            }
        }

        /*  JADX ERROR: JadxRuntimeException in pass: SSATransform
            jadx.core.utils.exceptions.JadxRuntimeException: Not initialized variable reg: 3, insn: 0x019f: MOVE  (r2 I:??[OBJECT, ARRAY]) = (r3 I:??[OBJECT, ARRAY]), block:B:137:0x019f
            	at jadx.core.dex.visitors.ssa.SSATransform.renameVarsInBlock(SSATransform.java:171)
            	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:143)
            	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:60)
            	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:41)
            */
        public final java.lang.Object getValue(
/*
[456] Method generation error in method: android.support.media.ExifInterface.ExifAttribute.getValue(java.nio.ByteOrder):java.lang.Object, file: classes.dex
        jadx.core.utils.exceptions.JadxRuntimeException: Code variable not set in r13v0 ??
        	at jadx.core.dex.instructions.args.SSAVar.getCodeVar(SSAVar.java:228)
        	at jadx.core.codegen.MethodGen.addMethodArguments(MethodGen.java:195)
        	at jadx.core.codegen.MethodGen.addDefinition(MethodGen.java:151)
        	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:344)
        	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:302)
        	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:271)
        	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
        	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
        	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
        
*/

        public String toString() {
            StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("(");
            m.append(ExifInterface.IFD_FORMAT_NAMES[this.format]);
            m.append(", data length:");
            m.append(this.bytes.length);
            m.append(")");
            return m.toString();
        }

        public ExifAttribute(int i, int i2, byte[] bArr, AnonymousClass1 r4) {
            this.format = i;
            this.numberOfComponents = i2;
            this.bytes = bArr;
        }
    }

    /* loaded from: classes.dex */
    public static class ExifTag {
        public final String name;
        public final int number;
        public final int primaryFormat;
        public final int secondaryFormat;

        public ExifTag(String str, int i, int i2, AnonymousClass1 r4) {
            this.name = str;
            this.number = i;
            this.primaryFormat = i2;
            this.secondaryFormat = -1;
        }

        public ExifTag(String str, int i, int i2, int i3, AnonymousClass1 r5) {
            this.name = str;
            this.number = i;
            this.primaryFormat = i2;
            this.secondaryFormat = i3;
        }
    }
}
