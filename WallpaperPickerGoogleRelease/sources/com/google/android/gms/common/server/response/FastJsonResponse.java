package com.google.android.gms.common.server.response;

import android.os.Parcel;
import androidx.appcompat.R$bool;
import androidx.appcompat.R$dimen;
import androidx.core.R$id;
import androidx.preference.R$layout;
import com.google.android.gms.common.util.zzn;
import com.google.android.gms.internal.zzbkv;
import com.google.android.gms.internal.zzbly;
import com.google.android.gms.internal.zzbma;
import com.google.android.gms.internal.zzfit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public abstract class FastJsonResponse {

    /* loaded from: classes.dex */
    public static class Field<I, O> extends zzbkv {
        public static final FieldCreator CREATOR = new FieldCreator();
        public final Class<? extends FastJsonResponse> mConcreteType;
        public final String mConcreteTypeName;
        public final String mOutputFieldName;
        public final int mSafeParcelableFieldId;
        public final int mTypeIn;
        public final boolean mTypeInArray;
        public final int mTypeOut;
        public final boolean mTypeOutArray;
        public final int zza;
        public FieldMappingDictionary zzb;
        public FieldConverter<I, O> zzc;

        public Field(int i, int i2, boolean z, int i3, boolean z2, String str, int i4, String str2, zzbly zzbly) {
            this.zza = i;
            this.mTypeIn = i2;
            this.mTypeInArray = z;
            this.mTypeOut = i3;
            this.mTypeOutArray = z2;
            this.mOutputFieldName = str;
            this.mSafeParcelableFieldId = i4;
            if (str2 == null) {
                this.mConcreteType = null;
                this.mConcreteTypeName = null;
            } else {
                this.mConcreteType = zzl.class;
                this.mConcreteTypeName = str2;
            }
            if (zzbly == null) {
                this.zzc = null;
                return;
            }
            zzbma zzbma = zzbly.zzb;
            if (zzbma != null) {
                this.zzc = zzbma;
                return;
            }
            throw new IllegalStateException("There was no converter wrapped in this ConverterWrapper.");
        }

        @Override // java.lang.Object
        public String toString() {
            zzfit zza = R$id.zza(this);
            zza.zza("versionCode", Integer.valueOf(this.zza));
            zza.zza("typeIn", Integer.valueOf(this.mTypeIn));
            zza.zza("typeInArray", Boolean.valueOf(this.mTypeInArray));
            zza.zza("typeOut", Integer.valueOf(this.mTypeOut));
            zza.zza("typeOutArray", Boolean.valueOf(this.mTypeOutArray));
            zza.zza("outputFieldName", this.mOutputFieldName);
            zza.zza("safeParcelFieldId", Integer.valueOf(this.mSafeParcelableFieldId));
            String str = this.mConcreteTypeName;
            if (str == null) {
                str = null;
            }
            zza.zza("concreteTypeName", str);
            Class<? extends FastJsonResponse> cls = this.mConcreteType;
            if (cls != null) {
                zza.zza("concreteType.class", cls.getCanonicalName());
            }
            FieldConverter<I, O> fieldConverter = this.zzc;
            if (fieldConverter != null) {
                zza.zza("converterName", fieldConverter.getClass().getCanonicalName());
            }
            return zza.toString();
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            int zzb = androidx.slice.view.R$id.zzb(parcel, 20293);
            int i2 = this.zza;
            androidx.slice.view.R$id.zzb(parcel, 1, 4);
            parcel.writeInt(i2);
            int i3 = this.mTypeIn;
            androidx.slice.view.R$id.zzb(parcel, 2, 4);
            parcel.writeInt(i3);
            boolean z = this.mTypeInArray;
            androidx.slice.view.R$id.zzb(parcel, 3, 4);
            parcel.writeInt(z ? 1 : 0);
            int i4 = this.mTypeOut;
            androidx.slice.view.R$id.zzb(parcel, 4, 4);
            parcel.writeInt(i4);
            boolean z2 = this.mTypeOutArray;
            androidx.slice.view.R$id.zzb(parcel, 5, 4);
            parcel.writeInt(z2 ? 1 : 0);
            androidx.slice.view.R$id.zza(parcel, 6, this.mOutputFieldName, false);
            int i5 = this.mSafeParcelableFieldId;
            androidx.slice.view.R$id.zzb(parcel, 7, 4);
            parcel.writeInt(i5);
            String str = this.mConcreteTypeName;
            zzbly zzbly = null;
            if (str == null) {
                str = null;
            }
            androidx.slice.view.R$id.zza(parcel, 8, str, false);
            FieldConverter<I, O> fieldConverter = this.zzc;
            if (fieldConverter != null) {
                if (fieldConverter instanceof zzbma) {
                    zzbly = new zzbly((zzbma) fieldConverter);
                } else {
                    throw new IllegalArgumentException("Unsupported safe parcelable field converter class.");
                }
            }
            androidx.slice.view.R$id.zza(parcel, 9, zzbly, i, false);
            androidx.slice.view.R$id.zzc(parcel, zzb);
        }
    }

    /* loaded from: classes.dex */
    public interface FieldConverter<I, O> {
    }

    public static void zza(StringBuilder sb, Field field, Object obj) {
        int i = field.mTypeIn;
        if (i == 11) {
            sb.append(((FastJsonResponse) field.mConcreteType.cast(obj)).toString());
        } else if (i == 7) {
            sb.append("\"");
            sb.append(zzn.zzb((String) obj));
            sb.append("\"");
        } else {
            sb.append(obj);
        }
    }

    public abstract Map<String, Field<?, ?>> getFieldMappings();

    public Object getFieldValue(Field field) {
        String str = field.mOutputFieldName;
        if (field.mConcreteType == null) {
            return getValueObject(str);
        }
        R$layout.zza(getValueObject(str) == null, "Concrete field shouldn't be value object: %s", field.mOutputFieldName);
        try {
            char upperCase = Character.toUpperCase(str.charAt(0));
            String substring = str.substring(1);
            StringBuilder sb = new StringBuilder(String.valueOf(substring).length() + 4);
            sb.append("get");
            sb.append(upperCase);
            sb.append(substring);
            return getClass().getMethod(sb.toString(), new Class[0]).invoke(this, new Object[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: java.lang.Object */
    /* JADX WARN: Multi-variable type inference failed */
    public <O, I> I getOriginalValue(Field<I, O> field, Object obj) {
        FieldConverter<I, O> fieldConverter = field.zzc;
        if (fieldConverter == null) {
            return obj;
        }
        zzbma zzbma = (zzbma) fieldConverter;
        I i = (I) zzbma.zzc.get(((Integer) obj).intValue());
        return (i != null || !zzbma.zzb.containsKey("gms_unknown")) ? i : "gms_unknown";
    }

    public abstract Object getValueObject(String str);

    public boolean isFieldSet(Field field) {
        if (field.mTypeOut != 11) {
            return isPrimitiveFieldSet(field.mOutputFieldName);
        }
        if (field.mTypeOutArray) {
            throw new UnsupportedOperationException("Concrete type arrays not supported");
        }
        throw new UnsupportedOperationException("Concrete types not supported");
    }

    public abstract boolean isPrimitiveFieldSet(String str);

    @Override // java.lang.Object
    public String toString() {
        Map<String, Field<?, ?>> fieldMappings = getFieldMappings();
        StringBuilder sb = new StringBuilder(100);
        for (String str : fieldMappings.keySet()) {
            Field<?, ?> field = fieldMappings.get(str);
            if (isFieldSet(field)) {
                Object originalValue = getOriginalValue(field, getFieldValue(field));
                if (sb.length() == 0) {
                    sb.append("{");
                } else {
                    sb.append(",");
                }
                sb.append("\"");
                sb.append(str);
                sb.append("\":");
                if (originalValue != null) {
                    switch (field.mTypeOut) {
                        case 8:
                            sb.append("\"");
                            sb.append(R$bool.zza((byte[]) originalValue));
                            sb.append("\"");
                            continue;
                        case 9:
                            sb.append("\"");
                            sb.append(R$bool.zzb((byte[]) originalValue));
                            sb.append("\"");
                            continue;
                        case 10:
                            R$dimen.zza(sb, (HashMap) originalValue);
                            continue;
                        default:
                            if (!field.mTypeInArray) {
                                zza(sb, field, originalValue);
                                break;
                            } else {
                                ArrayList arrayList = (ArrayList) originalValue;
                                sb.append("[");
                                int size = arrayList.size();
                                for (int i = 0; i < size; i++) {
                                    if (i > 0) {
                                        sb.append(",");
                                    }
                                    Object obj = arrayList.get(i);
                                    if (obj != null) {
                                        zza(sb, field, obj);
                                    }
                                }
                                sb.append("]");
                                continue;
                            }
                    }
                } else {
                    sb.append("null");
                }
            }
        }
        if (sb.length() > 0) {
            sb.append("}");
        } else {
            sb.append("{}");
        }
        return sb.toString();
    }
}
