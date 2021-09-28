package com.google.android.gms.common.server.response;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$bool;
import androidx.appcompat.R$dimen;
import androidx.appcompat.R$dimen$$ExternalSyntheticOutline0;
import androidx.preference.R$layout;
import androidx.slice.view.R$id;
import com.google.android.gms.common.server.response.FastJsonResponse;
import com.google.android.gms.common.util.zzn;
import com.google.android.gms.internal.zzbkw;
import com.google.android.gms.internal.zzbkx;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes.dex */
public class zzl extends FastSafeParcelableJsonResponse {
    public static final Parcelable.Creator<zzl> CREATOR = new zzm();
    public final int zza;
    public final Parcel zzb;
    public final int zzc = 2;
    public final FieldMappingDictionary zzd;
    public final String zze;
    public int zzf;
    public int zzg;

    public zzl(int i, Parcel parcel, FieldMappingDictionary fieldMappingDictionary) {
        this.zza = i;
        Objects.requireNonNull(parcel, "null reference");
        this.zzb = parcel;
        this.zzd = fieldMappingDictionary;
        if (fieldMappingDictionary == null) {
            this.zze = null;
        } else {
            this.zze = fieldMappingDictionary.zzd;
        }
        this.zzf = 2;
    }

    private final void zza(StringBuilder sb, FastJsonResponse.Field<?, ?> field, Object obj) {
        if (field.mTypeInArray) {
            ArrayList arrayList = (ArrayList) obj;
            sb.append("[");
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                if (i != 0) {
                    sb.append(",");
                }
                zza(sb, field.mTypeIn, arrayList.get(i));
            }
            sb.append("]");
            return;
        }
        zza(sb, field.mTypeIn, obj);
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    public Map<String, FastJsonResponse.Field<?, ?>> getFieldMappings() {
        FieldMappingDictionary fieldMappingDictionary = this.zzd;
        if (fieldMappingDictionary == null) {
            return null;
        }
        return fieldMappingDictionary.getFieldMapping(this.zze);
    }

    @Override // com.google.android.gms.common.server.response.FastSafeParcelableJsonResponse, com.google.android.gms.common.server.response.FastJsonResponse
    public Object getValueObject(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    @Override // com.google.android.gms.common.server.response.FastSafeParcelableJsonResponse, com.google.android.gms.common.server.response.FastJsonResponse
    public boolean isPrimitiveFieldSet(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse, java.lang.Object
    public String toString() {
        R$layout.zza(this.zzd, "Cannot convert to JSON on client side.");
        Parcel zza = zza();
        zza.setDataPosition(0);
        StringBuilder sb = new StringBuilder(100);
        zza(sb, this.zzd.getFieldMapping(this.zze), zza);
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        FieldMappingDictionary fieldMappingDictionary;
        int zzb = R$id.zzb(parcel, 20293);
        int i2 = this.zza;
        R$id.zzb(parcel, 1, 4);
        parcel.writeInt(i2);
        Parcel zza = zza();
        if (zza != null) {
            int zzb2 = R$id.zzb(parcel, 2);
            parcel.appendFrom(zza, 0, zza.dataSize());
            R$id.zzc(parcel, zzb2);
        }
        int i3 = this.zzc;
        if (i3 == 0) {
            fieldMappingDictionary = null;
        } else if (i3 == 1) {
            fieldMappingDictionary = this.zzd;
        } else if (i3 == 2) {
            fieldMappingDictionary = this.zzd;
        } else {
            throw new IllegalStateException(R$dimen$$ExternalSyntheticOutline0.m(34, "Invalid creation type: ", this.zzc));
        }
        R$id.zza(parcel, 3, fieldMappingDictionary, i, false);
        R$id.zzc(parcel, zzb);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0005, code lost:
        if (r0 != 1) goto L_0x001a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final android.os.Parcel zza() {
        /*
            r2 = this;
            int r0 = r2.zzf
            if (r0 == 0) goto L_0x0008
            r1 = 1
            if (r0 == r1) goto L_0x0010
            goto L_0x001a
        L_0x0008:
            android.os.Parcel r0 = r2.zzb
            int r0 = androidx.slice.view.R$id.zza(r0)
            r2.zzg = r0
        L_0x0010:
            android.os.Parcel r0 = r2.zzb
            int r1 = r2.zzg
            androidx.slice.view.R$id.zzc(r0, r1)
            r0 = 2
            r2.zzf = r0
        L_0x001a:
            android.os.Parcel r2 = r2.zzb
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.server.response.zzl.zza():android.os.Parcel");
    }

    public final void zza(StringBuilder sb, Map<String, FastJsonResponse.Field<?, ?>> map, Parcel parcel) {
        Parcel[] parcelArr;
        SparseArray sparseArray = new SparseArray();
        for (Map.Entry<String, FastJsonResponse.Field<?, ?>> entry : map.entrySet()) {
            sparseArray.put(entry.getValue().mSafeParcelableFieldId, entry);
        }
        sb.append('{');
        int zza = zzbkw.zza(parcel);
        boolean z = false;
        while (parcel.dataPosition() < zza) {
            int readInt = parcel.readInt();
            Map.Entry entry2 = (Map.Entry) sparseArray.get(65535 & readInt);
            if (entry2 != null) {
                if (z) {
                    sb.append(",");
                }
                FastJsonResponse.Field<?, ?> field = (FastJsonResponse.Field) entry2.getValue();
                sb.append("\"");
                sb.append((String) entry2.getKey());
                sb.append("\":");
                if (field.zzc != null) {
                    switch (field.mTypeOut) {
                        case 0:
                            zza(sb, field, getOriginalValue(field, Integer.valueOf(zzbkw.zzg(parcel, readInt))));
                            break;
                        case 1:
                            zza(sb, field, getOriginalValue(field, zzbkw.zzk(parcel, readInt)));
                            break;
                        case 2:
                            zza(sb, field, getOriginalValue(field, Long.valueOf(zzbkw.zzi(parcel, readInt))));
                            break;
                        case 3:
                            zza(sb, field, getOriginalValue(field, Float.valueOf(zzbkw.zzl(parcel, readInt))));
                            break;
                        case 4:
                            zza(sb, field, getOriginalValue(field, Double.valueOf(zzbkw.zzn(parcel, readInt))));
                            break;
                        case 5:
                            zza(sb, field, getOriginalValue(field, zzbkw.zzp(parcel, readInt)));
                            break;
                        case 6:
                            zza(sb, field, getOriginalValue(field, Boolean.valueOf(zzbkw.zzc(parcel, readInt))));
                            break;
                        case 7:
                            zza(sb, field, getOriginalValue(field, zzbkw.zzq(parcel, readInt)));
                            break;
                        case 8:
                        case 9:
                            zza(sb, field, getOriginalValue(field, zzbkw.zzt(parcel, readInt)));
                            break;
                        case 10:
                            Bundle zzs = zzbkw.zzs(parcel, readInt);
                            HashMap hashMap = new HashMap();
                            for (String str : zzs.keySet()) {
                                hashMap.put(str, zzs.getString(str));
                            }
                            zza(sb, field, getOriginalValue(field, hashMap));
                            break;
                        case 11:
                            throw new IllegalArgumentException("Method does not accept concrete type.");
                        default:
                            throw new IllegalArgumentException(R$dimen$$ExternalSyntheticOutline0.m(36, "Unknown field out type = ", field.mTypeOut));
                    }
                } else if (field.mTypeOutArray) {
                    sb.append("[");
                    switch (field.mTypeOut) {
                        case 0:
                            int[] zzw = zzbkw.zzw(parcel, readInt);
                            int length = zzw.length;
                            for (int i = 0; i < length; i++) {
                                if (i != 0) {
                                    sb.append(",");
                                }
                                sb.append(Integer.toString(zzw[i]));
                            }
                            break;
                        case 1:
                            BigInteger[] bigIntegerArr = null;
                            int zza2 = zzbkw.zza(parcel, readInt);
                            int dataPosition = parcel.dataPosition();
                            if (zza2 != 0) {
                                int readInt2 = parcel.readInt();
                                BigInteger[] bigIntegerArr2 = new BigInteger[readInt2];
                                for (int i2 = 0; i2 < readInt2; i2++) {
                                    bigIntegerArr2[i2] = new BigInteger(parcel.createByteArray());
                                }
                                parcel.setDataPosition(dataPosition + zza2);
                                bigIntegerArr = bigIntegerArr2;
                            }
                            R$attr.zza(sb, bigIntegerArr);
                            break;
                        case 2:
                            long[] jArr = null;
                            int zza3 = zzbkw.zza(parcel, readInt);
                            int dataPosition2 = parcel.dataPosition();
                            if (zza3 != 0) {
                                jArr = parcel.createLongArray();
                                parcel.setDataPosition(dataPosition2 + zza3);
                            }
                            int length2 = jArr.length;
                            for (int i3 = 0; i3 < length2; i3++) {
                                if (i3 != 0) {
                                    sb.append(",");
                                }
                                sb.append(Long.toString(jArr[i3]));
                            }
                            break;
                        case 3:
                            float[] fArr = null;
                            int zza4 = zzbkw.zza(parcel, readInt);
                            int dataPosition3 = parcel.dataPosition();
                            if (zza4 != 0) {
                                fArr = parcel.createFloatArray();
                                parcel.setDataPosition(dataPosition3 + zza4);
                            }
                            int length3 = fArr.length;
                            for (int i4 = 0; i4 < length3; i4++) {
                                if (i4 != 0) {
                                    sb.append(",");
                                }
                                sb.append(Float.toString(fArr[i4]));
                            }
                            break;
                        case 4:
                            double[] dArr = null;
                            int zza5 = zzbkw.zza(parcel, readInt);
                            int dataPosition4 = parcel.dataPosition();
                            if (zza5 != 0) {
                                dArr = parcel.createDoubleArray();
                                parcel.setDataPosition(dataPosition4 + zza5);
                            }
                            int length4 = dArr.length;
                            for (int i5 = 0; i5 < length4; i5++) {
                                if (i5 != 0) {
                                    sb.append(",");
                                }
                                sb.append(Double.toString(dArr[i5]));
                            }
                            break;
                        case 5:
                            BigDecimal[] bigDecimalArr = null;
                            int zza6 = zzbkw.zza(parcel, readInt);
                            int dataPosition5 = parcel.dataPosition();
                            if (zza6 != 0) {
                                int readInt3 = parcel.readInt();
                                BigDecimal[] bigDecimalArr2 = new BigDecimal[readInt3];
                                for (int i6 = 0; i6 < readInt3; i6++) {
                                    bigDecimalArr2[i6] = new BigDecimal(new BigInteger(parcel.createByteArray()), parcel.readInt());
                                }
                                parcel.setDataPosition(dataPosition5 + zza6);
                                bigDecimalArr = bigDecimalArr2;
                            }
                            R$attr.zza(sb, bigDecimalArr);
                            break;
                        case 6:
                            boolean[] zArr = null;
                            int zza7 = zzbkw.zza(parcel, readInt);
                            int dataPosition6 = parcel.dataPosition();
                            if (zza7 != 0) {
                                zArr = parcel.createBooleanArray();
                                parcel.setDataPosition(dataPosition6 + zza7);
                            }
                            int length5 = zArr.length;
                            for (int i7 = 0; i7 < length5; i7++) {
                                if (i7 != 0) {
                                    sb.append(",");
                                }
                                sb.append(Boolean.toString(zArr[i7]));
                            }
                            break;
                        case 7:
                            String[] zzaa = zzbkw.zzaa(parcel, readInt);
                            int length6 = zzaa.length;
                            for (int i8 = 0; i8 < length6; i8++) {
                                if (i8 != 0) {
                                    sb.append(",");
                                }
                                sb.append("\"");
                                sb.append(zzaa[i8]);
                                sb.append("\"");
                            }
                            break;
                        case 8:
                        case 9:
                        case 10:
                            throw new UnsupportedOperationException("List of type BASE64, BASE64_URL_SAFE, or STRING_MAP is not supported");
                        case 11:
                            int zza8 = zzbkw.zza(parcel, readInt);
                            int dataPosition7 = parcel.dataPosition();
                            if (zza8 == 0) {
                                parcelArr = null;
                            } else {
                                int readInt4 = parcel.readInt();
                                Parcel[] parcelArr2 = new Parcel[readInt4];
                                for (int i9 = 0; i9 < readInt4; i9++) {
                                    int readInt5 = parcel.readInt();
                                    if (readInt5 != 0) {
                                        int dataPosition8 = parcel.dataPosition();
                                        Parcel obtain = Parcel.obtain();
                                        obtain.appendFrom(parcel, dataPosition8, readInt5);
                                        parcelArr2[i9] = obtain;
                                        parcel.setDataPosition(dataPosition8 + readInt5);
                                    } else {
                                        parcelArr2[i9] = null;
                                    }
                                }
                                parcel.setDataPosition(dataPosition7 + zza8);
                                parcelArr = parcelArr2;
                            }
                            int length7 = parcelArr.length;
                            for (int i10 = 0; i10 < length7; i10++) {
                                if (i10 > 0) {
                                    sb.append(",");
                                }
                                parcelArr[i10].setDataPosition(0);
                                Objects.requireNonNull(field.mConcreteTypeName, "null reference");
                                Objects.requireNonNull(field.zzb, "null reference");
                                zza(sb, field.zzb.getFieldMapping(field.mConcreteTypeName), parcelArr[i10]);
                            }
                            break;
                        default:
                            throw new IllegalStateException("Unknown field type out.");
                    }
                    sb.append("]");
                } else {
                    switch (field.mTypeOut) {
                        case 0:
                            sb.append(zzbkw.zzg(parcel, readInt));
                            break;
                        case 1:
                            sb.append(zzbkw.zzk(parcel, readInt));
                            break;
                        case 2:
                            sb.append(zzbkw.zzi(parcel, readInt));
                            break;
                        case 3:
                            sb.append(zzbkw.zzl(parcel, readInt));
                            break;
                        case 4:
                            sb.append(zzbkw.zzn(parcel, readInt));
                            break;
                        case 5:
                            sb.append(zzbkw.zzp(parcel, readInt));
                            break;
                        case 6:
                            sb.append(zzbkw.zzc(parcel, readInt));
                            break;
                        case 7:
                            String zzq = zzbkw.zzq(parcel, readInt);
                            sb.append("\"");
                            sb.append(zzn.zzb(zzq));
                            sb.append("\"");
                            break;
                        case 8:
                            byte[] zzt = zzbkw.zzt(parcel, readInt);
                            sb.append("\"");
                            sb.append(R$bool.zza(zzt));
                            sb.append("\"");
                            break;
                        case 9:
                            byte[] zzt2 = zzbkw.zzt(parcel, readInt);
                            sb.append("\"");
                            sb.append(R$bool.zzb(zzt2));
                            sb.append("\"");
                            break;
                        case 10:
                            Bundle zzs2 = zzbkw.zzs(parcel, readInt);
                            Set<String> keySet = zzs2.keySet();
                            keySet.size();
                            sb.append("{");
                            boolean z2 = true;
                            for (String str2 : keySet) {
                                if (!z2) {
                                    sb.append(",");
                                }
                                sb.append("\"");
                                sb.append(str2);
                                sb.append("\"");
                                sb.append(":");
                                sb.append("\"");
                                sb.append(zzn.zzb(zzs2.getString(str2)));
                                sb.append("\"");
                                z2 = false;
                            }
                            sb.append("}");
                            break;
                        case 11:
                            Parcel zzad = zzbkw.zzad(parcel, readInt);
                            zzad.setDataPosition(0);
                            Objects.requireNonNull(field.mConcreteTypeName, "null reference");
                            Objects.requireNonNull(field.zzb, "null reference");
                            zza(sb, field.zzb.getFieldMapping(field.mConcreteTypeName), zzad);
                            break;
                        default:
                            throw new IllegalStateException("Unknown field type out");
                    }
                }
                z = true;
            }
        }
        if (parcel.dataPosition() == zza) {
            sb.append('}');
            return;
        }
        throw new zzbkx(R$dimen$$ExternalSyntheticOutline0.m(37, "Overread allowed size end=", zza), parcel);
    }

    public static void zza(StringBuilder sb, int i, Object obj) {
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                sb.append(obj);
                return;
            case 7:
                sb.append("\"");
                sb.append(zzn.zzb(obj.toString()));
                sb.append("\"");
                return;
            case 8:
                sb.append("\"");
                sb.append(R$bool.zza((byte[]) obj));
                sb.append("\"");
                return;
            case 9:
                sb.append("\"");
                sb.append(R$bool.zzb((byte[]) obj));
                sb.append("\"");
                return;
            case 10:
                R$dimen.zza(sb, (HashMap) obj);
                return;
            case 11:
                throw new IllegalArgumentException("Method does not accept concrete type.");
            default:
                throw new IllegalArgumentException(R$dimen$$ExternalSyntheticOutline0.m(26, "Unknown type = ", i));
        }
    }
}
