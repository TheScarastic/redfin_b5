package com.google.protobuf;

import com.android.systemui.shared.recents.ISystemUiProxy;
import com.android.systemui.shared.system.InteractionJankMonitorWrapper;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.google.protobuf.ArrayDecoders;
import com.google.protobuf.FieldSet;
import com.google.protobuf.Internal;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import sun.misc.Unsafe;
/* loaded from: classes.dex */
public final class MessageSchema<T> implements Schema<T> {
    public static final int[] EMPTY_INT_ARRAY = new int[0];
    public static final Unsafe UNSAFE;
    public final int[] buffer;
    public final int checkInitializedCount;
    public final MessageLite defaultInstance;
    public final ExtensionSchema<?> extensionSchema;
    public final boolean hasExtensions;
    public final int[] intArray;
    public final ListFieldSchema listFieldSchema;
    public final boolean lite;
    public final MapFieldSchema mapFieldSchema;
    public final int maxFieldNumber;
    public final int minFieldNumber;
    public final NewInstanceSchema newInstanceSchema;
    public final Object[] objects;
    public final boolean proto3;
    public final int repeatedFieldOffsetStart;
    public final UnknownFieldSchema<?, ?> unknownFieldSchema;
    public final boolean useCachedSizeField;

    static {
        Unsafe unsafe;
        Logger logger = UnsafeUtil.logger;
        try {
            unsafe = (Unsafe) AccessController.doPrivileged(
            /*  JADX ERROR: Method code generation error
                jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0010: CHECK_CAST (r0v7 'unsafe' sun.misc.Unsafe) = (sun.misc.Unsafe) (wrap: java.lang.Object : 0x000c: INVOKE  (r0v6 java.lang.Object A[REMOVE]) = 
                  (wrap: com.google.protobuf.UnsafeUtil$1 : 0x0009: CONSTRUCTOR  (r0v5 com.google.protobuf.UnsafeUtil$1 A[REMOVE]) =  call: com.google.protobuf.UnsafeUtil.1.<init>():void type: CONSTRUCTOR)
                 type: STATIC call: java.security.AccessController.doPrivileged(java.security.PrivilegedExceptionAction):java.lang.Object) in method: com.google.protobuf.MessageSchema.<clinit>():void, file: classes.dex
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:285)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:248)
                	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:105)
                	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                	at jadx.core.dex.regions.Region.generate(Region.java:35)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:94)
                	at jadx.core.codegen.RegionGen.makeTryCatch(RegionGen.java:314)
                	at jadx.core.dex.regions.TryCatchRegion.generate(TryCatchRegion.java:85)
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
                Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.google.protobuf.UnsafeUtil, state: GENERATED_AND_UNLOADED
                	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:259)
                	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:675)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:393)
                	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:976)
                	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:801)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:397)
                	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:320)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:278)
                	... 21 more
                */
            /*
                r0 = 0
                int[] r0 = new int[r0]
                com.google.protobuf.MessageSchema.EMPTY_INT_ARRAY = r0
                java.util.logging.Logger r0 = com.google.protobuf.UnsafeUtil.logger
                com.google.protobuf.UnsafeUtil$1 r0 = new com.google.protobuf.UnsafeUtil$1     // Catch: all -> 0x0013
                r0.<init>()     // Catch: all -> 0x0013
                java.lang.Object r0 = java.security.AccessController.doPrivileged(r0)     // Catch: all -> 0x0013
                sun.misc.Unsafe r0 = (sun.misc.Unsafe) r0     // Catch: all -> 0x0013
                goto L_0x0014
            L_0x0013:
                r0 = 0
            L_0x0014:
                com.google.protobuf.MessageSchema.UNSAFE = r0
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.MessageSchema.<clinit>():void");
        }

        public MessageSchema(int[] iArr, Object[] objArr, int i, int i2, MessageLite messageLite, boolean z, boolean z2, int[] iArr2, int i3, int i4, NewInstanceSchema newInstanceSchema, ListFieldSchema listFieldSchema, UnknownFieldSchema<?, ?> unknownFieldSchema, ExtensionSchema<?> extensionSchema, MapFieldSchema mapFieldSchema) {
            this.buffer = iArr;
            this.objects = objArr;
            this.minFieldNumber = i;
            this.maxFieldNumber = i2;
            this.lite = messageLite instanceof GeneratedMessageLite;
            this.proto3 = z;
            this.hasExtensions = extensionSchema != null && extensionSchema.hasExtensions(messageLite);
            this.useCachedSizeField = z2;
            this.intArray = iArr2;
            this.checkInitializedCount = i3;
            this.repeatedFieldOffsetStart = i4;
            this.newInstanceSchema = newInstanceSchema;
            this.listFieldSchema = listFieldSchema;
            this.unknownFieldSchema = unknownFieldSchema;
            this.extensionSchema = extensionSchema;
            this.defaultInstance = messageLite;
            this.mapFieldSchema = mapFieldSchema;
        }

        public static UnknownFieldSetLite getMutableUnknownFields(Object obj) {
            GeneratedMessageLite generatedMessageLite = (GeneratedMessageLite) obj;
            UnknownFieldSetLite unknownFieldSetLite = generatedMessageLite.unknownFields;
            if (unknownFieldSetLite != UnknownFieldSetLite.DEFAULT_INSTANCE) {
                return unknownFieldSetLite;
            }
            UnknownFieldSetLite newInstance = UnknownFieldSetLite.newInstance();
            generatedMessageLite.unknownFields = newInstance;
            return newInstance;
        }

        public static List<?> listAt(Object obj, long j) {
            return (List) UnsafeUtil.getObject(obj, j);
        }

        /* JADX WARN: Incorrect args count in method signature: <T:Ljava/lang/Object;>(Ljava/lang/Class<TT;>;Lcom/google/protobuf/MessageInfo;Lcom/google/protobuf/NewInstanceSchema;Lcom/google/protobuf/ListFieldSchema;Lcom/google/protobuf/UnknownFieldSchema<**>;Lcom/google/protobuf/ExtensionSchema<*>;Lcom/google/protobuf/MapFieldSchema;)Lcom/google/protobuf/MessageSchema<TT;>; */
        public static MessageSchema newSchema(MessageInfo messageInfo, NewInstanceSchema newInstanceSchema, ListFieldSchema listFieldSchema, UnknownFieldSchema unknownFieldSchema, ExtensionSchema extensionSchema, MapFieldSchema mapFieldSchema) {
            int i;
            int i2;
            int[] iArr;
            int i3;
            int i4;
            int i5;
            int i6;
            int i7;
            int i8;
            int i9;
            boolean z;
            int i10;
            int i11;
            int i12;
            int i13;
            RawMessageInfo rawMessageInfo;
            int i14;
            int i15;
            int[] iArr2;
            String str;
            int i16;
            int i17;
            int i18;
            int i19;
            int objectFieldOffset;
            Field field;
            char charAt;
            int i20;
            int i21;
            int i22;
            Field field2;
            Field field3;
            int i23;
            char charAt2;
            int i24;
            char charAt3;
            int i25;
            char charAt4;
            int i26;
            int i27;
            int i28;
            char charAt5;
            int i29;
            char charAt6;
            int i30;
            char charAt7;
            int i31;
            char charAt8;
            int i32;
            char charAt9;
            int i33;
            char charAt10;
            int i34;
            char charAt11;
            int i35;
            char charAt12;
            int i36;
            char charAt13;
            char charAt14;
            if (messageInfo instanceof RawMessageInfo) {
                RawMessageInfo rawMessageInfo2 = (RawMessageInfo) messageInfo;
                int i37 = 0;
                boolean z2 = (rawMessageInfo2.flags & 1) == 1 ? true : true;
                String str2 = rawMessageInfo2.info;
                int length = str2.length();
                int charAt15 = str2.charAt(0);
                if (charAt15 >= 55296) {
                    int i38 = charAt15 & 8191;
                    int i39 = 1;
                    int i40 = 13;
                    while (true) {
                        i = i39 + 1;
                        charAt14 = str2.charAt(i39);
                        if (charAt14 < 55296) {
                            break;
                        }
                        i38 |= (charAt14 & 8191) << i40;
                        i40 += 13;
                        i39 = i;
                    }
                    charAt15 = i38 | (charAt14 << i40);
                } else {
                    i = 1;
                }
                int i41 = i + 1;
                int charAt16 = str2.charAt(i);
                if (charAt16 >= 55296) {
                    int i42 = charAt16 & 8191;
                    int i43 = 13;
                    while (true) {
                        i36 = i41 + 1;
                        charAt13 = str2.charAt(i41);
                        if (charAt13 < 55296) {
                            break;
                        }
                        i42 |= (charAt13 & 8191) << i43;
                        i43 += 13;
                        i41 = i36;
                    }
                    charAt16 = i42 | (charAt13 << i43);
                    i41 = i36;
                }
                if (charAt16 == 0) {
                    iArr = EMPTY_INT_ARRAY;
                    i7 = 0;
                    i6 = 0;
                    i4 = 0;
                    i3 = 0;
                    i2 = 0;
                    i5 = 0;
                } else {
                    int i44 = i41 + 1;
                    int charAt17 = str2.charAt(i41);
                    if (charAt17 >= 55296) {
                        int i45 = charAt17 & 8191;
                        int i46 = 13;
                        while (true) {
                            i35 = i44 + 1;
                            charAt12 = str2.charAt(i44);
                            if (charAt12 < 55296) {
                                break;
                            }
                            i45 |= (charAt12 & 8191) << i46;
                            i46 += 13;
                            i44 = i35;
                        }
                        charAt17 = i45 | (charAt12 << i46);
                        i44 = i35;
                    }
                    int i47 = i44 + 1;
                    int charAt18 = str2.charAt(i44);
                    if (charAt18 >= 55296) {
                        int i48 = charAt18 & 8191;
                        int i49 = 13;
                        while (true) {
                            i34 = i47 + 1;
                            charAt11 = str2.charAt(i47);
                            if (charAt11 < 55296) {
                                break;
                            }
                            i48 |= (charAt11 & 8191) << i49;
                            i49 += 13;
                            i47 = i34;
                        }
                        charAt18 = i48 | (charAt11 << i49);
                        i47 = i34;
                    }
                    int i50 = i47 + 1;
                    int charAt19 = str2.charAt(i47);
                    if (charAt19 >= 55296) {
                        int i51 = charAt19 & 8191;
                        int i52 = 13;
                        while (true) {
                            i33 = i50 + 1;
                            charAt10 = str2.charAt(i50);
                            if (charAt10 < 55296) {
                                break;
                            }
                            i51 |= (charAt10 & 8191) << i52;
                            i52 += 13;
                            i50 = i33;
                        }
                        charAt19 = i51 | (charAt10 << i52);
                        i50 = i33;
                    }
                    int i53 = i50 + 1;
                    i3 = str2.charAt(i50);
                    if (i3 >= 55296) {
                        int i54 = i3 & 8191;
                        int i55 = 13;
                        while (true) {
                            i32 = i53 + 1;
                            charAt9 = str2.charAt(i53);
                            if (charAt9 < 55296) {
                                break;
                            }
                            i54 |= (charAt9 & 8191) << i55;
                            i55 += 13;
                            i53 = i32;
                        }
                        i3 = i54 | (charAt9 << i55);
                        i53 = i32;
                    }
                    int i56 = i53 + 1;
                    int charAt20 = str2.charAt(i53);
                    if (charAt20 >= 55296) {
                        int i57 = charAt20 & 8191;
                        int i58 = 13;
                        while (true) {
                            i31 = i56 + 1;
                            charAt8 = str2.charAt(i56);
                            if (charAt8 < 55296) {
                                break;
                            }
                            i57 |= (charAt8 & 8191) << i58;
                            i58 += 13;
                            i56 = i31;
                        }
                        charAt20 = i57 | (charAt8 << i58);
                        i56 = i31;
                    }
                    int i59 = i56 + 1;
                    int charAt21 = str2.charAt(i56);
                    if (charAt21 >= 55296) {
                        int i60 = charAt21 & 8191;
                        int i61 = i59;
                        int i62 = 13;
                        while (true) {
                            i30 = i61 + 1;
                            charAt7 = str2.charAt(i61);
                            if (charAt7 < 55296) {
                                break;
                            }
                            i60 |= (charAt7 & 8191) << i62;
                            i62 += 13;
                            i61 = i30;
                        }
                        charAt21 = i60 | (charAt7 << i62);
                        i26 = i30;
                    } else {
                        i26 = i59;
                    }
                    int i63 = i26 + 1;
                    int charAt22 = str2.charAt(i26);
                    if (charAt22 >= 55296) {
                        int i64 = charAt22 & 8191;
                        int i65 = i63;
                        int i66 = 13;
                        while (true) {
                            i29 = i65 + 1;
                            charAt6 = str2.charAt(i65);
                            if (charAt6 < 55296) {
                                break;
                            }
                            i64 |= (charAt6 & 8191) << i66;
                            i66 += 13;
                            i65 = i29;
                        }
                        charAt22 = i64 | (charAt6 << i66);
                        i27 = i29;
                    } else {
                        i27 = i63;
                    }
                    int i67 = i27 + 1;
                    int charAt23 = str2.charAt(i27);
                    if (charAt23 >= 55296) {
                        int i68 = charAt23 & 8191;
                        int i69 = i67;
                        int i70 = 13;
                        while (true) {
                            i28 = i69 + 1;
                            charAt5 = str2.charAt(i69);
                            if (charAt5 < 55296) {
                                break;
                            }
                            i68 |= (charAt5 & 8191) << i70;
                            i70 += 13;
                            i69 = i28;
                        }
                        charAt23 = i68 | (charAt5 << i70);
                        i67 = i28;
                    }
                    iArr = new int[charAt23 + charAt21 + charAt22];
                    i37 = (charAt17 * 2) + charAt18;
                    i5 = charAt20;
                    i7 = charAt17;
                    i41 = i67;
                    i2 = charAt23;
                    i6 = charAt19;
                    i4 = charAt21;
                }
                Unsafe unsafe = UNSAFE;
                Object[] objArr = rawMessageInfo2.objects;
                int i71 = i37;
                Class<?> cls = rawMessageInfo2.defaultInstance.getClass();
                int[] iArr3 = new int[i5 * 3];
                Object[] objArr2 = new Object[i5 * 2];
                int i72 = i2 + i4;
                int i73 = i2;
                int i74 = i41;
                int i75 = i72;
                int i76 = 0;
                int i77 = 0;
                while (i74 < length) {
                    int i78 = i74 + 1;
                    int charAt24 = str2.charAt(i74);
                    if (charAt24 >= 55296) {
                        int i79 = charAt24 & 8191;
                        int i80 = i78;
                        int i81 = 13;
                        while (true) {
                            i25 = i80 + 1;
                            charAt4 = str2.charAt(i80);
                            i8 = i2;
                            if (charAt4 < 55296) {
                                break;
                            }
                            i79 |= (charAt4 & 8191) << i81;
                            i81 += 13;
                            i80 = i25;
                            i2 = i8;
                        }
                        charAt24 = i79 | (charAt4 << i81);
                        i9 = i25;
                    } else {
                        i8 = i2;
                        i9 = i78;
                    }
                    int i82 = i9 + 1;
                    int charAt25 = str2.charAt(i9);
                    if (charAt25 >= 55296) {
                        int i83 = charAt25 & 8191;
                        int i84 = i82;
                        int i85 = 13;
                        while (true) {
                            i24 = i84 + 1;
                            charAt3 = str2.charAt(i84);
                            z = z2;
                            if (charAt3 < 55296) {
                                break;
                            }
                            i83 |= (charAt3 & 8191) << i85;
                            i85 += 13;
                            i84 = i24;
                            z2 = z;
                        }
                        charAt25 = i83 | (charAt3 << i85);
                        i10 = i24;
                    } else {
                        z = z2;
                        i10 = i82;
                    }
                    int i86 = charAt25 & 255;
                    if ((charAt25 & QuickStepContract.SYSUI_STATE_SEARCH_DISABLED) != 0) {
                        iArr[i76] = i77;
                        i76++;
                    }
                    if (i86 >= 51) {
                        int i87 = i10 + 1;
                        int charAt26 = str2.charAt(i10);
                        char c = 55296;
                        if (charAt26 >= 55296) {
                            int i88 = charAt26 & 8191;
                            int i89 = 13;
                            int i90 = i87;
                            while (true) {
                                i23 = i90 + 1;
                                charAt2 = str2.charAt(i90);
                                if (charAt2 < c) {
                                    break;
                                }
                                i88 |= (charAt2 & 8191) << i89;
                                i89 += 13;
                                i90 = i23;
                                c = 55296;
                            }
                            charAt26 = i88 | (charAt2 << i89);
                            i21 = i23;
                        } else {
                            i21 = i87;
                        }
                        int i91 = i86 - 51;
                        i11 = i6;
                        if (i91 == 9 || i91 == 17) {
                            i22 = 2;
                            i71++;
                            objArr2[((i77 / 3) * 2) + 1] = objArr[i71];
                        } else {
                            if (i91 == 12 && (charAt15 & 1) == 1) {
                                i71++;
                                objArr2[((i77 / 3) * 2) + 1] = objArr[i71];
                            }
                            i22 = 2;
                        }
                        int i92 = charAt26 * i22;
                        Object obj = objArr[i92];
                        if (obj instanceof Field) {
                            field2 = (Field) obj;
                        } else {
                            field2 = reflectField(cls, (String) obj);
                            objArr[i92] = field2;
                        }
                        iArr2 = iArr3;
                        int objectFieldOffset2 = (int) unsafe.objectFieldOffset(field2);
                        int i93 = i92 + 1;
                        Object obj2 = objArr[i93];
                        if (obj2 instanceof Field) {
                            field3 = (Field) obj2;
                        } else {
                            field3 = reflectField(cls, (String) obj2);
                            objArr[i93] = field3;
                        }
                        i18 = (int) unsafe.objectFieldOffset(field3);
                        str = str2;
                        i14 = charAt15;
                        i17 = i71;
                        i12 = i21;
                        i15 = 0;
                        rawMessageInfo = rawMessageInfo2;
                        i16 = objectFieldOffset2;
                        i13 = charAt25;
                    } else {
                        i11 = i6;
                        iArr2 = iArr3;
                        int i94 = i71 + 1;
                        Field reflectField = reflectField(cls, (String) objArr[i71]);
                        rawMessageInfo = rawMessageInfo2;
                        if (i86 == 9 || i86 == 17) {
                            i13 = charAt25;
                            objArr2[((i77 / 3) * 2) + 1] = reflectField.getType();
                        } else {
                            if (i86 == 27 || i86 == 49) {
                                i13 = charAt25;
                                i20 = i94 + 1;
                                objArr2[((i77 / 3) * 2) + 1] = objArr[i94];
                            } else if (i86 == 12 || i86 == 30 || i86 == 44) {
                                i13 = charAt25;
                                if ((charAt15 & 1) == 1) {
                                    i20 = i94 + 1;
                                    objArr2[((i77 / 3) * 2) + 1] = objArr[i94];
                                }
                            } else if (i86 == 50) {
                                int i95 = i73 + 1;
                                iArr[i73] = i77;
                                int i96 = (i77 / 3) * 2;
                                int i97 = i94 + 1;
                                objArr2[i96] = objArr[i94];
                                if ((charAt25 & QuickStepContract.SYSUI_STATE_QUICK_SETTINGS_EXPANDED) != 0) {
                                    objArr2[i96 + 1] = objArr[i97];
                                    i97++;
                                }
                                i73 = i95;
                                i19 = charAt15;
                                i94 = i97;
                                i13 = charAt25;
                                objectFieldOffset = (int) unsafe.objectFieldOffset(reflectField);
                                if ((i19 & 1) == 1 || i86 > 17) {
                                    i14 = i19;
                                    str = str2;
                                    i12 = i10;
                                    i18 = 0;
                                    i15 = 0;
                                } else {
                                    int i98 = i10 + 1;
                                    int charAt27 = str2.charAt(i10);
                                    if (charAt27 >= 55296) {
                                        int i99 = charAt27 & 8191;
                                        int i100 = 13;
                                        while (true) {
                                            i12 = i98 + 1;
                                            charAt = str2.charAt(i98);
                                            if (charAt < 55296) {
                                                break;
                                            }
                                            i99 |= (charAt & 8191) << i100;
                                            i100 += 13;
                                            i98 = i12;
                                        }
                                        charAt27 = i99 | (charAt << i100);
                                    } else {
                                        i12 = i98;
                                    }
                                    int i101 = (charAt27 / 32) + (i7 * 2);
                                    Object obj3 = objArr[i101];
                                    if (obj3 instanceof Field) {
                                        field = (Field) obj3;
                                    } else {
                                        field = reflectField(cls, (String) obj3);
                                        objArr[i101] = field;
                                    }
                                    i14 = i19;
                                    str = str2;
                                    i18 = (int) unsafe.objectFieldOffset(field);
                                    i15 = charAt27 % 32;
                                }
                                if (i86 >= 18 && i86 <= 49) {
                                    iArr[i75] = objectFieldOffset;
                                    i75++;
                                }
                                i17 = i94;
                                i16 = objectFieldOffset;
                            } else {
                                i13 = charAt25;
                            }
                            i94 = i20;
                        }
                        i19 = charAt15;
                        objectFieldOffset = (int) unsafe.objectFieldOffset(reflectField);
                        if ((i19 & 1) == 1) {
                        }
                        i14 = i19;
                        str = str2;
                        i12 = i10;
                        i18 = 0;
                        i15 = 0;
                        if (i86 >= 18) {
                            iArr[i75] = objectFieldOffset;
                            i75++;
                        }
                        i17 = i94;
                        i16 = objectFieldOffset;
                    }
                    int i102 = i77 + 1;
                    iArr2[i77] = charAt24;
                    int i103 = i102 + 1;
                    iArr2[i102] = ((i13 & QuickStepContract.SYSUI_STATE_STATUS_BAR_KEYGUARD_SHOWING_OCCLUDED) != 0 ? 536870912 : 0) | ((i13 & 256) != 0 ? 268435456 : 0) | (i86 << 20) | i16;
                    int i104 = i103 + 1;
                    iArr2[i103] = i18 | (i15 << 20);
                    charAt15 = i14;
                    rawMessageInfo2 = rawMessageInfo;
                    i71 = i17;
                    length = length;
                    i2 = i8;
                    z2 = z;
                    i74 = i12;
                    i6 = i11;
                    i77 = i104;
                    str2 = str;
                    iArr3 = iArr2;
                    i3 = i3;
                }
                return new MessageSchema(iArr3, objArr2, i6, i3, rawMessageInfo2.defaultInstance, z2, false, iArr, i2, i72, newInstanceSchema, listFieldSchema, unknownFieldSchema, extensionSchema, mapFieldSchema);
            }
            StructuralMessageInfo structuralMessageInfo = (StructuralMessageInfo) messageInfo;
            throw null;
        }

        public static long offset(int i) {
            return (long) (i & 1048575);
        }

        public static <T> boolean oneofBooleanAt(T t, long j) {
            return ((Boolean) UnsafeUtil.getObject(t, j)).booleanValue();
        }

        public static <T> double oneofDoubleAt(T t, long j) {
            return ((Double) UnsafeUtil.getObject(t, j)).doubleValue();
        }

        public static <T> float oneofFloatAt(T t, long j) {
            return ((Float) UnsafeUtil.getObject(t, j)).floatValue();
        }

        public static <T> int oneofIntAt(T t, long j) {
            return ((Integer) UnsafeUtil.getObject(t, j)).intValue();
        }

        public static <T> long oneofLongAt(T t, long j) {
            return ((Long) UnsafeUtil.getObject(t, j)).longValue();
        }

        public static Field reflectField(Class<?> cls, String str) {
            try {
                return cls.getDeclaredField(str);
            } catch (NoSuchFieldException unused) {
                Field[] declaredFields = cls.getDeclaredFields();
                for (Field field : declaredFields) {
                    if (str.equals(field.getName())) {
                        return field;
                    }
                }
                throw new RuntimeException("Field " + str + " for " + cls.getName() + " not found. Known fields are " + Arrays.toString(declaredFields));
            }
        }

        public static int type(int i) {
            return (i & 267386880) >>> 20;
        }

        public final boolean arePresentForEquals(T t, T t2, int i) {
            return isFieldPresent(t, i) == isFieldPresent(t2, i);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0041, code lost:
            if (com.google.protobuf.SchemaUtil.safeEquals(com.google.protobuf.UnsafeUtil.getObject(r10, r5), com.google.protobuf.UnsafeUtil.getObject(r11, r5)) != false) goto L_0x01cd;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0073, code lost:
            if (com.google.protobuf.SchemaUtil.safeEquals(com.google.protobuf.UnsafeUtil.getObject(r10, r5), com.google.protobuf.UnsafeUtil.getObject(r11, r5)) != false) goto L_0x01cd;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0087, code lost:
            if (com.google.protobuf.UnsafeUtil.getLong(r10, r5) == com.google.protobuf.UnsafeUtil.getLong(r11, r5)) goto L_0x01cd;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x0099, code lost:
            if (com.google.protobuf.UnsafeUtil.getInt(r10, r5) == com.google.protobuf.UnsafeUtil.getInt(r11, r5)) goto L_0x01cd;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x00ad, code lost:
            if (com.google.protobuf.UnsafeUtil.getLong(r10, r5) == com.google.protobuf.UnsafeUtil.getLong(r11, r5)) goto L_0x01cd;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x00bf, code lost:
            if (com.google.protobuf.UnsafeUtil.getInt(r10, r5) == com.google.protobuf.UnsafeUtil.getInt(r11, r5)) goto L_0x01cd;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x00d1, code lost:
            if (com.google.protobuf.UnsafeUtil.getInt(r10, r5) == com.google.protobuf.UnsafeUtil.getInt(r11, r5)) goto L_0x01cd;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:0x00e3, code lost:
            if (com.google.protobuf.UnsafeUtil.getInt(r10, r5) == com.google.protobuf.UnsafeUtil.getInt(r11, r5)) goto L_0x01cd;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x00f9, code lost:
            if (com.google.protobuf.SchemaUtil.safeEquals(com.google.protobuf.UnsafeUtil.getObject(r10, r5), com.google.protobuf.UnsafeUtil.getObject(r11, r5)) != false) goto L_0x01cd;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:51:0x010f, code lost:
            if (com.google.protobuf.SchemaUtil.safeEquals(com.google.protobuf.UnsafeUtil.getObject(r10, r5), com.google.protobuf.UnsafeUtil.getObject(r11, r5)) != false) goto L_0x01cd;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:0x0125, code lost:
            if (com.google.protobuf.SchemaUtil.safeEquals(com.google.protobuf.UnsafeUtil.getObject(r10, r5), com.google.protobuf.UnsafeUtil.getObject(r11, r5)) != false) goto L_0x01cd;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:59:0x0137, code lost:
            if (com.google.protobuf.UnsafeUtil.getBoolean(r10, r5) == com.google.protobuf.UnsafeUtil.getBoolean(r11, r5)) goto L_0x01cd;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:63:0x0149, code lost:
            if (com.google.protobuf.UnsafeUtil.getInt(r10, r5) == com.google.protobuf.UnsafeUtil.getInt(r11, r5)) goto L_0x01cd;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:67:0x015d, code lost:
            if (com.google.protobuf.UnsafeUtil.getLong(r10, r5) == com.google.protobuf.UnsafeUtil.getLong(r11, r5)) goto L_0x01cd;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:71:0x016f, code lost:
            if (com.google.protobuf.UnsafeUtil.getInt(r10, r5) == com.google.protobuf.UnsafeUtil.getInt(r11, r5)) goto L_0x01cd;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:75:0x0182, code lost:
            if (com.google.protobuf.UnsafeUtil.getLong(r10, r5) == com.google.protobuf.UnsafeUtil.getLong(r11, r5)) goto L_0x01cd;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:79:0x0195, code lost:
            if (com.google.protobuf.UnsafeUtil.getLong(r10, r5) == com.google.protobuf.UnsafeUtil.getLong(r11, r5)) goto L_0x01cd;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:83:0x01ae, code lost:
            if (java.lang.Float.floatToIntBits(com.google.protobuf.UnsafeUtil.getFloat(r10, r5)) == java.lang.Float.floatToIntBits(com.google.protobuf.UnsafeUtil.getFloat(r11, r5))) goto L_0x01cd;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:87:0x01c9, code lost:
            if (java.lang.Double.doubleToLongBits(com.google.protobuf.UnsafeUtil.getDouble(r10, r5)) == java.lang.Double.doubleToLongBits(com.google.protobuf.UnsafeUtil.getDouble(r11, r5))) goto L_0x01cd;
         */
        @Override // com.google.protobuf.Schema
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean equals(T r10, T r11) {
            /*
            // Method dump skipped, instructions count: 652
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.MessageSchema.equals(java.lang.Object, java.lang.Object):boolean");
        }

        public final <UT, UB> UB filterMapUnknownEnumValues(Object obj, int i, UB ub, UnknownFieldSchema<UT, UB> unknownFieldSchema) {
            int[] iArr = this.buffer;
            int i2 = iArr[i];
            Object object = UnsafeUtil.getObject(obj, offset(iArr[i + 1]));
            if (object == null) {
                return ub;
            }
            int i3 = (i / 3) * 2;
            Internal.EnumVerifier enumVerifier = (Internal.EnumVerifier) this.objects[i3 + 1];
            if (enumVerifier == null) {
                return ub;
            }
            Map<?, ?> forMutableMapData = this.mapFieldSchema.forMutableMapData(object);
            this.mapFieldSchema.forMapMetadata(this.objects[i3]);
            for (Map.Entry<?, ?> entry : forMutableMapData.entrySet()) {
                if (!enumVerifier.isInRange(((Integer) entry.getValue()).intValue())) {
                    if (ub == null) {
                        unknownFieldSchema.newBuilder();
                    }
                    entry.getKey();
                    entry.getValue();
                    Objects.requireNonNull(null);
                    throw null;
                }
            }
            return ub;
        }

        public final Internal.EnumVerifier getEnumFieldVerifier(int i) {
            return (Internal.EnumVerifier) this.objects[((i / 3) * 2) + 1];
        }

        public final Object getMapFieldDefaultEntry(int i) {
            return this.objects[(i / 3) * 2];
        }

        public final Schema getMessageFieldSchema(int i) {
            int i2 = (i / 3) * 2;
            Object[] objArr = this.objects;
            Schema schema = (Schema) objArr[i2];
            if (schema != null) {
                return schema;
            }
            Schema<T> schemaFor = Protobuf.INSTANCE.schemaFor((Class) ((Class) objArr[i2 + 1]));
            this.objects[i2] = schemaFor;
            return schemaFor;
        }

        @Override // com.google.protobuf.Schema
        public int getSerializedSize(T t) {
            int i;
            int i2;
            int i3;
            boolean z;
            int i4;
            int computeInt64Size;
            int i5;
            int i6;
            int i7;
            int computeSFixed64Size;
            int i8;
            int i9;
            int i10;
            int i11;
            int i12;
            int i13;
            int i14;
            int i15;
            int i16;
            int i17 = 1;
            int i18 = 1048575;
            if (this.proto3) {
                Unsafe unsafe = UNSAFE;
                int i19 = 0;
                for (int i20 = 0; i20 < this.buffer.length; i20 += 3) {
                    int typeAndOffsetAt = typeAndOffsetAt(i20);
                    int type = type(typeAndOffsetAt);
                    int i21 = this.buffer[i20];
                    long offset = offset(typeAndOffsetAt);
                    int i22 = (type < FieldType.DOUBLE_LIST_PACKED.id() || type > FieldType.SINT64_LIST_PACKED.id()) ? 0 : this.buffer[i20 + 2] & 1048575;
                    switch (type) {
                        case 0:
                            if (isFieldPresent(t, i20)) {
                                i13 = CodedOutputStream.computeDoubleSize(i21, 0.0d);
                                i12 = i13;
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 1:
                            if (isFieldPresent(t, i20)) {
                                i12 = CodedOutputStream.computeFloatSize(i21, 0.0f);
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 2:
                            if (isFieldPresent(t, i20)) {
                                i12 = CodedOutputStream.computeInt64Size(i21, UnsafeUtil.getLong(t, offset));
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 3:
                            if (isFieldPresent(t, i20)) {
                                i12 = CodedOutputStream.computeUInt64Size(i21, UnsafeUtil.getLong(t, offset));
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 4:
                            if (isFieldPresent(t, i20)) {
                                i12 = CodedOutputStream.computeInt32Size(i21, UnsafeUtil.getInt(t, offset));
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 5:
                            if (isFieldPresent(t, i20)) {
                                i12 = CodedOutputStream.computeFixed64Size(i21, 0);
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 6:
                            if (isFieldPresent(t, i20)) {
                                i12 = CodedOutputStream.computeFixed32Size(i21, 0);
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 7:
                            if (isFieldPresent(t, i20)) {
                                i12 = CodedOutputStream.computeBoolSize(i21, true);
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 8:
                            if (isFieldPresent(t, i20)) {
                                Object object = UnsafeUtil.getObject(t, offset);
                                if (object instanceof ByteString) {
                                    i12 = CodedOutputStream.computeBytesSize(i21, (ByteString) object);
                                } else {
                                    i12 = CodedOutputStream.computeStringSize(i21, (String) object);
                                }
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 9:
                            if (isFieldPresent(t, i20)) {
                                i12 = SchemaUtil.computeSizeMessage(i21, UnsafeUtil.getObject(t, offset), getMessageFieldSchema(i20));
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 10:
                            if (isFieldPresent(t, i20)) {
                                i12 = CodedOutputStream.computeBytesSize(i21, (ByteString) UnsafeUtil.getObject(t, offset));
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 11:
                            if (isFieldPresent(t, i20)) {
                                i12 = CodedOutputStream.computeUInt32Size(i21, UnsafeUtil.getInt(t, offset));
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 12:
                            if (isFieldPresent(t, i20)) {
                                i12 = CodedOutputStream.computeEnumSize(i21, UnsafeUtil.getInt(t, offset));
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 13:
                            if (isFieldPresent(t, i20)) {
                                i12 = CodedOutputStream.computeSFixed32Size(i21, 0);
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 14:
                            if (isFieldPresent(t, i20)) {
                                i12 = CodedOutputStream.computeSFixed64Size(i21, 0);
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 15:
                            if (isFieldPresent(t, i20)) {
                                i12 = CodedOutputStream.computeSInt32Size(i21, UnsafeUtil.getInt(t, offset));
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 16:
                            if (isFieldPresent(t, i20)) {
                                i12 = CodedOutputStream.computeSInt64Size(i21, UnsafeUtil.getLong(t, offset));
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 17:
                            if (isFieldPresent(t, i20)) {
                                i12 = CodedOutputStream.computeGroupSize(i21, (MessageLite) UnsafeUtil.getObject(t, offset), getMessageFieldSchema(i20));
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 18:
                            i12 = SchemaUtil.computeSizeFixed64List(i21, listAt(t, offset), false);
                            i19 += i12;
                            break;
                        case 19:
                            i12 = SchemaUtil.computeSizeFixed32List(i21, listAt(t, offset), false);
                            i19 += i12;
                            break;
                        case 20:
                            i12 = SchemaUtil.computeSizeInt64List(i21, listAt(t, offset), false);
                            i19 += i12;
                            break;
                        case 21:
                            i12 = SchemaUtil.computeSizeUInt64List(i21, listAt(t, offset), false);
                            i19 += i12;
                            break;
                        case 22:
                            i12 = SchemaUtil.computeSizeInt32List(i21, listAt(t, offset), false);
                            i19 += i12;
                            break;
                        case 23:
                            i12 = SchemaUtil.computeSizeFixed64List(i21, listAt(t, offset), false);
                            i19 += i12;
                            break;
                        case 24:
                            i12 = SchemaUtil.computeSizeFixed32List(i21, listAt(t, offset), false);
                            i19 += i12;
                            break;
                        case 25:
                            i12 = SchemaUtil.computeSizeBoolList(i21, listAt(t, offset), false);
                            i19 += i12;
                            break;
                        case 26:
                            i12 = SchemaUtil.computeSizeStringList(i21, listAt(t, offset));
                            i19 += i12;
                            break;
                        case InteractionJankMonitorWrapper.CUJ_APP_LAUNCH_FROM_WIDGET:
                            i12 = SchemaUtil.computeSizeMessageList(i21, listAt(t, offset), getMessageFieldSchema(i20));
                            i19 += i12;
                            break;
                        case 28:
                            i12 = SchemaUtil.computeSizeByteStringList(i21, listAt(t, offset));
                            i19 += i12;
                            break;
                        case ISystemUiProxy.Stub.TRANSACTION_handleImageBundleAsScreenshot:
                            i12 = SchemaUtil.computeSizeUInt32List(i21, listAt(t, offset), false);
                            i19 += i12;
                            break;
                        case ISystemUiProxy.Stub.TRANSACTION_expandNotificationPanel:
                            i12 = SchemaUtil.computeSizeEnumList(i21, listAt(t, offset), false);
                            i19 += i12;
                            break;
                        case 31:
                            i12 = SchemaUtil.computeSizeFixed32List(i21, listAt(t, offset), false);
                            i19 += i12;
                            break;
                        case 32:
                            i12 = SchemaUtil.computeSizeFixed64List(i21, listAt(t, offset), false);
                            i19 += i12;
                            break;
                        case 33:
                            i12 = SchemaUtil.computeSizeSInt32List(i21, listAt(t, offset), false);
                            i19 += i12;
                            break;
                        case 34:
                            i12 = SchemaUtil.computeSizeSInt64List(i21, listAt(t, offset), false);
                            i19 += i12;
                            break;
                        case 35:
                            i16 = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe.getObject(t, offset));
                            if (i16 <= 0) {
                                break;
                            } else {
                                if (this.useCachedSizeField) {
                                    unsafe.putInt(t, (long) i22, i16);
                                }
                                i15 = CodedOutputStream.computeTagSize(i21);
                                i14 = CodedOutputStream.computeUInt32SizeNoTag(i16);
                                i19 += i14 + i15 + i16;
                                break;
                            }
                        case 36:
                            i16 = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe.getObject(t, offset));
                            if (i16 <= 0) {
                                break;
                            } else {
                                if (this.useCachedSizeField) {
                                    unsafe.putInt(t, (long) i22, i16);
                                }
                                i15 = CodedOutputStream.computeTagSize(i21);
                                i14 = CodedOutputStream.computeUInt32SizeNoTag(i16);
                                i19 += i14 + i15 + i16;
                                break;
                            }
                        case 37:
                            i16 = SchemaUtil.computeSizeInt64ListNoTag((List) unsafe.getObject(t, offset));
                            if (i16 <= 0) {
                                break;
                            } else {
                                if (this.useCachedSizeField) {
                                    unsafe.putInt(t, (long) i22, i16);
                                }
                                i15 = CodedOutputStream.computeTagSize(i21);
                                i14 = CodedOutputStream.computeUInt32SizeNoTag(i16);
                                i19 += i14 + i15 + i16;
                                break;
                            }
                        case 38:
                            i16 = SchemaUtil.computeSizeUInt64ListNoTag((List) unsafe.getObject(t, offset));
                            if (i16 <= 0) {
                                break;
                            } else {
                                if (this.useCachedSizeField) {
                                    unsafe.putInt(t, (long) i22, i16);
                                }
                                i15 = CodedOutputStream.computeTagSize(i21);
                                i14 = CodedOutputStream.computeUInt32SizeNoTag(i16);
                                i19 += i14 + i15 + i16;
                                break;
                            }
                        case 39:
                            i16 = SchemaUtil.computeSizeInt32ListNoTag((List) unsafe.getObject(t, offset));
                            if (i16 <= 0) {
                                break;
                            } else {
                                if (this.useCachedSizeField) {
                                    unsafe.putInt(t, (long) i22, i16);
                                }
                                i15 = CodedOutputStream.computeTagSize(i21);
                                i14 = CodedOutputStream.computeUInt32SizeNoTag(i16);
                                i19 += i14 + i15 + i16;
                                break;
                            }
                        case 40:
                            i16 = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe.getObject(t, offset));
                            if (i16 <= 0) {
                                break;
                            } else {
                                if (this.useCachedSizeField) {
                                    unsafe.putInt(t, (long) i22, i16);
                                }
                                i15 = CodedOutputStream.computeTagSize(i21);
                                i14 = CodedOutputStream.computeUInt32SizeNoTag(i16);
                                i19 += i14 + i15 + i16;
                                break;
                            }
                        case 41:
                            i16 = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe.getObject(t, offset));
                            if (i16 <= 0) {
                                break;
                            } else {
                                if (this.useCachedSizeField) {
                                    unsafe.putInt(t, (long) i22, i16);
                                }
                                i15 = CodedOutputStream.computeTagSize(i21);
                                i14 = CodedOutputStream.computeUInt32SizeNoTag(i16);
                                i19 += i14 + i15 + i16;
                                break;
                            }
                        case 42:
                            Class<?> cls = SchemaUtil.GENERATED_MESSAGE_CLASS;
                            i16 = ((List) unsafe.getObject(t, offset)).size();
                            if (i16 <= 0) {
                                break;
                            } else {
                                if (this.useCachedSizeField) {
                                    unsafe.putInt(t, (long) i22, i16);
                                }
                                i15 = CodedOutputStream.computeTagSize(i21);
                                i14 = CodedOutputStream.computeUInt32SizeNoTag(i16);
                                i19 += i14 + i15 + i16;
                                break;
                            }
                        case 43:
                            i16 = SchemaUtil.computeSizeUInt32ListNoTag((List) unsafe.getObject(t, offset));
                            if (i16 <= 0) {
                                break;
                            } else {
                                if (this.useCachedSizeField) {
                                    unsafe.putInt(t, (long) i22, i16);
                                }
                                i15 = CodedOutputStream.computeTagSize(i21);
                                i14 = CodedOutputStream.computeUInt32SizeNoTag(i16);
                                i19 += i14 + i15 + i16;
                                break;
                            }
                        case 44:
                            i16 = SchemaUtil.computeSizeEnumListNoTag((List) unsafe.getObject(t, offset));
                            if (i16 <= 0) {
                                break;
                            } else {
                                if (this.useCachedSizeField) {
                                    unsafe.putInt(t, (long) i22, i16);
                                }
                                i15 = CodedOutputStream.computeTagSize(i21);
                                i14 = CodedOutputStream.computeUInt32SizeNoTag(i16);
                                i19 += i14 + i15 + i16;
                                break;
                            }
                        case ISystemUiProxy.Stub.TRANSACTION_onBackPressed:
                            i16 = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe.getObject(t, offset));
                            if (i16 <= 0) {
                                break;
                            } else {
                                if (this.useCachedSizeField) {
                                    unsafe.putInt(t, (long) i22, i16);
                                }
                                i15 = CodedOutputStream.computeTagSize(i21);
                                i14 = CodedOutputStream.computeUInt32SizeNoTag(i16);
                                i19 += i14 + i15 + i16;
                                break;
                            }
                        case ISystemUiProxy.Stub.TRANSACTION_setHomeRotationEnabled:
                            i16 = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe.getObject(t, offset));
                            if (i16 <= 0) {
                                break;
                            } else {
                                if (this.useCachedSizeField) {
                                    unsafe.putInt(t, (long) i22, i16);
                                }
                                i15 = CodedOutputStream.computeTagSize(i21);
                                i14 = CodedOutputStream.computeUInt32SizeNoTag(i16);
                                i19 += i14 + i15 + i16;
                                break;
                            }
                        case ISystemUiProxy.Stub.TRANSACTION_notifySwipeUpGestureStarted:
                            i16 = SchemaUtil.computeSizeSInt32ListNoTag((List) unsafe.getObject(t, offset));
                            if (i16 <= 0) {
                                break;
                            } else {
                                if (this.useCachedSizeField) {
                                    unsafe.putInt(t, (long) i22, i16);
                                }
                                i15 = CodedOutputStream.computeTagSize(i21);
                                i14 = CodedOutputStream.computeUInt32SizeNoTag(i16);
                                i19 += i14 + i15 + i16;
                                break;
                            }
                        case 48:
                            i16 = SchemaUtil.computeSizeSInt64ListNoTag((List) unsafe.getObject(t, offset));
                            if (i16 <= 0) {
                                break;
                            } else {
                                if (this.useCachedSizeField) {
                                    unsafe.putInt(t, (long) i22, i16);
                                }
                                i15 = CodedOutputStream.computeTagSize(i21);
                                i14 = CodedOutputStream.computeUInt32SizeNoTag(i16);
                                i19 += i14 + i15 + i16;
                                break;
                            }
                        case 49:
                            i12 = SchemaUtil.computeSizeGroupList(i21, listAt(t, offset), getMessageFieldSchema(i20));
                            i19 += i12;
                            break;
                        case 50:
                            i12 = this.mapFieldSchema.getSerializedSize(i21, UnsafeUtil.getObject(t, offset), getMapFieldDefaultEntry(i20));
                            i19 += i12;
                            break;
                        case 51:
                            if (isOneofPresent(t, i21, i20)) {
                                i13 = CodedOutputStream.computeDoubleSize(i21, 0.0d);
                                i12 = i13;
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 52:
                            if (isOneofPresent(t, i21, i20)) {
                                i12 = CodedOutputStream.computeFloatSize(i21, 0.0f);
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 53:
                            if (isOneofPresent(t, i21, i20)) {
                                i12 = CodedOutputStream.computeInt64Size(i21, oneofLongAt(t, offset));
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 54:
                            if (isOneofPresent(t, i21, i20)) {
                                i12 = CodedOutputStream.computeUInt64Size(i21, oneofLongAt(t, offset));
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 55:
                            if (isOneofPresent(t, i21, i20)) {
                                i12 = CodedOutputStream.computeInt32Size(i21, oneofIntAt(t, offset));
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 56:
                            if (isOneofPresent(t, i21, i20)) {
                                i12 = CodedOutputStream.computeFixed64Size(i21, 0);
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 57:
                            if (isOneofPresent(t, i21, i20)) {
                                i12 = CodedOutputStream.computeFixed32Size(i21, 0);
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 58:
                            if (isOneofPresent(t, i21, i20)) {
                                i12 = CodedOutputStream.computeBoolSize(i21, true);
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 59:
                            if (isOneofPresent(t, i21, i20)) {
                                Object object2 = UnsafeUtil.getObject(t, offset);
                                if (object2 instanceof ByteString) {
                                    i12 = CodedOutputStream.computeBytesSize(i21, (ByteString) object2);
                                } else {
                                    i12 = CodedOutputStream.computeStringSize(i21, (String) object2);
                                }
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 60:
                            if (isOneofPresent(t, i21, i20)) {
                                i12 = SchemaUtil.computeSizeMessage(i21, UnsafeUtil.getObject(t, offset), getMessageFieldSchema(i20));
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 61:
                            if (isOneofPresent(t, i21, i20)) {
                                i12 = CodedOutputStream.computeBytesSize(i21, (ByteString) UnsafeUtil.getObject(t, offset));
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case SysUiStatsLog.KEYGUARD_STATE_CHANGED:
                            if (isOneofPresent(t, i21, i20)) {
                                i12 = CodedOutputStream.computeUInt32Size(i21, oneofIntAt(t, offset));
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case SysUiStatsLog.KEYGUARD_BOUNCER_STATE_CHANGED:
                            if (isOneofPresent(t, i21, i20)) {
                                i12 = CodedOutputStream.computeEnumSize(i21, oneofIntAt(t, offset));
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 64:
                            if (isOneofPresent(t, i21, i20)) {
                                i12 = CodedOutputStream.computeSFixed32Size(i21, 0);
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 65:
                            if (isOneofPresent(t, i21, i20)) {
                                i12 = CodedOutputStream.computeSFixed64Size(i21, 0);
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 66:
                            if (isOneofPresent(t, i21, i20)) {
                                i12 = CodedOutputStream.computeSInt32Size(i21, oneofIntAt(t, offset));
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 67:
                            if (isOneofPresent(t, i21, i20)) {
                                i12 = CodedOutputStream.computeSInt64Size(i21, oneofLongAt(t, offset));
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                        case 68:
                            if (isOneofPresent(t, i21, i20)) {
                                i12 = CodedOutputStream.computeGroupSize(i21, (MessageLite) UnsafeUtil.getObject(t, offset), getMessageFieldSchema(i20));
                                i19 += i12;
                                break;
                            } else {
                                break;
                            }
                    }
                }
                UnknownFieldSchema<?, ?> unknownFieldSchema = this.unknownFieldSchema;
                return unknownFieldSchema.getSerializedSize(unknownFieldSchema.getFromMessage(t)) + i19;
            }
            Unsafe unsafe2 = UNSAFE;
            int i23 = -1;
            int i24 = 0;
            int i25 = 0;
            int i26 = 0;
            while (i24 < this.buffer.length) {
                int typeAndOffsetAt2 = typeAndOffsetAt(i24);
                int i27 = this.buffer[i24];
                int type2 = type(typeAndOffsetAt2);
                if (type2 <= 17) {
                    i3 = this.buffer[i24 + 2];
                    int i28 = i3 & i18;
                    i = i17 << (i3 >>> 20);
                    if (i28 != i23) {
                        i26 = unsafe2.getInt(t, (long) i28);
                        i23 = i28;
                        i2 = i25;
                        i3 = i3;
                    } else {
                        i2 = i25;
                    }
                } else {
                    i3 = (!this.useCachedSizeField || type2 < FieldType.DOUBLE_LIST_PACKED.id() || type2 > FieldType.SINT64_LIST_PACKED.id()) ? 0 : this.buffer[i24 + 2] & i18;
                    i2 = i25;
                    i = 0;
                }
                long offset2 = offset(typeAndOffsetAt2);
                switch (type2) {
                    case 0:
                        z = false;
                        if ((i26 & i) != 0) {
                            i4 = CodedOutputStream.computeDoubleSize(i27, 0.0d);
                            i25 = i4 + i2;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 1:
                        if ((i26 & i) != 0) {
                            z = false;
                            i4 = CodedOutputStream.computeFloatSize(i27, 0.0f);
                            i25 = i4 + i2;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        } else {
                            z = false;
                            i25 = i2;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                    case 2:
                        if ((i26 & i) != 0) {
                            computeInt64Size = CodedOutputStream.computeInt64Size(i27, unsafe2.getLong(t, offset2));
                            i4 = computeInt64Size;
                            z = false;
                            i25 = i4 + i2;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 3:
                        if ((i26 & i) != 0) {
                            computeInt64Size = CodedOutputStream.computeUInt64Size(i27, unsafe2.getLong(t, offset2));
                            i4 = computeInt64Size;
                            z = false;
                            i25 = i4 + i2;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 4:
                        if ((i26 & i) != 0) {
                            computeInt64Size = CodedOutputStream.computeInt32Size(i27, unsafe2.getInt(t, offset2));
                            i4 = computeInt64Size;
                            z = false;
                            i25 = i4 + i2;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 5:
                        if ((i26 & i) != 0) {
                            computeInt64Size = CodedOutputStream.computeFixed64Size(i27, 0);
                            i4 = computeInt64Size;
                            z = false;
                            i25 = i4 + i2;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        i25 = i2;
                        z = false;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 6:
                        if ((i26 & i) != 0) {
                            i4 = CodedOutputStream.computeFixed32Size(i27, 0);
                            z = false;
                            i25 = i4 + i2;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        } else {
                            i25 = i2;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                    case 7:
                        if ((i26 & i) != 0) {
                            i5 = CodedOutputStream.computeBoolSize(i27, true);
                            i25 = i2 + i5;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 8:
                        if ((i26 & i) != 0) {
                            Object object3 = unsafe2.getObject(t, offset2);
                            if (object3 instanceof ByteString) {
                                i6 = CodedOutputStream.computeBytesSize(i27, (ByteString) object3);
                            } else {
                                i6 = CodedOutputStream.computeStringSize(i27, (String) object3);
                            }
                            i25 = i6 + i2;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 9:
                        if ((i26 & i) != 0) {
                            i5 = SchemaUtil.computeSizeMessage(i27, unsafe2.getObject(t, offset2), getMessageFieldSchema(i24));
                            i25 = i2 + i5;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 10:
                        if ((i26 & i) != 0) {
                            i5 = CodedOutputStream.computeBytesSize(i27, (ByteString) unsafe2.getObject(t, offset2));
                            i25 = i2 + i5;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 11:
                        if ((i26 & i) != 0) {
                            i5 = CodedOutputStream.computeUInt32Size(i27, unsafe2.getInt(t, offset2));
                            i25 = i2 + i5;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 12:
                        if ((i26 & i) != 0) {
                            i5 = CodedOutputStream.computeEnumSize(i27, unsafe2.getInt(t, offset2));
                            i25 = i2 + i5;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 13:
                        if ((i26 & i) != 0) {
                            i7 = CodedOutputStream.computeSFixed32Size(i27, 0);
                            i25 = i2 + i7;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 14:
                        if ((i26 & i) != 0) {
                            computeSFixed64Size = CodedOutputStream.computeSFixed64Size(i27, 0);
                            i5 = computeSFixed64Size;
                            i25 = i2 + i5;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 15:
                        if ((i26 & i) != 0) {
                            i5 = CodedOutputStream.computeSInt32Size(i27, unsafe2.getInt(t, offset2));
                            i25 = i2 + i5;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 16:
                        if ((i26 & i) != 0) {
                            i5 = CodedOutputStream.computeSInt64Size(i27, unsafe2.getLong(t, offset2));
                            i25 = i2 + i5;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 17:
                        if ((i26 & i) != 0) {
                            i5 = CodedOutputStream.computeGroupSize(i27, (MessageLite) unsafe2.getObject(t, offset2), getMessageFieldSchema(i24));
                            i25 = i2 + i5;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 18:
                        computeSFixed64Size = SchemaUtil.computeSizeFixed64List(i27, (List) unsafe2.getObject(t, offset2), false);
                        i5 = computeSFixed64Size;
                        i25 = i2 + i5;
                        z = false;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 19:
                        i8 = SchemaUtil.computeSizeFixed32List(i27, (List) unsafe2.getObject(t, offset2), false);
                        i25 = i2 + i8;
                        z = false;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 20:
                        i8 = SchemaUtil.computeSizeInt64List(i27, (List) unsafe2.getObject(t, offset2), false);
                        i25 = i2 + i8;
                        z = false;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 21:
                        i8 = SchemaUtil.computeSizeUInt64List(i27, (List) unsafe2.getObject(t, offset2), false);
                        i25 = i2 + i8;
                        z = false;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 22:
                        i8 = SchemaUtil.computeSizeInt32List(i27, (List) unsafe2.getObject(t, offset2), false);
                        i25 = i2 + i8;
                        z = false;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 23:
                        i8 = SchemaUtil.computeSizeFixed64List(i27, (List) unsafe2.getObject(t, offset2), false);
                        i25 = i2 + i8;
                        z = false;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 24:
                        i8 = SchemaUtil.computeSizeFixed32List(i27, (List) unsafe2.getObject(t, offset2), false);
                        i25 = i2 + i8;
                        z = false;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 25:
                        i8 = SchemaUtil.computeSizeBoolList(i27, (List) unsafe2.getObject(t, offset2), false);
                        i25 = i2 + i8;
                        z = false;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 26:
                        i5 = SchemaUtil.computeSizeStringList(i27, (List) unsafe2.getObject(t, offset2));
                        i25 = i2 + i5;
                        z = false;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case InteractionJankMonitorWrapper.CUJ_APP_LAUNCH_FROM_WIDGET:
                        i5 = SchemaUtil.computeSizeMessageList(i27, (List) unsafe2.getObject(t, offset2), getMessageFieldSchema(i24));
                        i25 = i2 + i5;
                        z = false;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 28:
                        i5 = SchemaUtil.computeSizeByteStringList(i27, (List) unsafe2.getObject(t, offset2));
                        i25 = i2 + i5;
                        z = false;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case ISystemUiProxy.Stub.TRANSACTION_handleImageBundleAsScreenshot:
                        computeSFixed64Size = SchemaUtil.computeSizeUInt32List(i27, (List) unsafe2.getObject(t, offset2), false);
                        i5 = computeSFixed64Size;
                        i25 = i2 + i5;
                        z = false;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case ISystemUiProxy.Stub.TRANSACTION_expandNotificationPanel:
                        i8 = SchemaUtil.computeSizeEnumList(i27, (List) unsafe2.getObject(t, offset2), false);
                        i25 = i2 + i8;
                        z = false;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 31:
                        i8 = SchemaUtil.computeSizeFixed32List(i27, (List) unsafe2.getObject(t, offset2), false);
                        i25 = i2 + i8;
                        z = false;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 32:
                        i8 = SchemaUtil.computeSizeFixed64List(i27, (List) unsafe2.getObject(t, offset2), false);
                        i25 = i2 + i8;
                        z = false;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 33:
                        i8 = SchemaUtil.computeSizeSInt32List(i27, (List) unsafe2.getObject(t, offset2), false);
                        i25 = i2 + i8;
                        z = false;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 34:
                        i8 = SchemaUtil.computeSizeSInt64List(i27, (List) unsafe2.getObject(t, offset2), false);
                        i25 = i2 + i8;
                        z = false;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 35:
                        i9 = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe2.getObject(t, offset2));
                        if (i9 > 0) {
                            if (this.useCachedSizeField) {
                                unsafe2.putInt(t, (long) i3, i9);
                            }
                            i11 = CodedOutputStream.computeTagSize(i27);
                            i10 = CodedOutputStream.computeUInt32SizeNoTag(i9);
                            i6 = i10 + i11 + i9;
                            i25 = i6 + i2;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 36:
                        i9 = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe2.getObject(t, offset2));
                        if (i9 > 0) {
                            if (this.useCachedSizeField) {
                                unsafe2.putInt(t, (long) i3, i9);
                            }
                            i11 = CodedOutputStream.computeTagSize(i27);
                            i10 = CodedOutputStream.computeUInt32SizeNoTag(i9);
                            i6 = i10 + i11 + i9;
                            i25 = i6 + i2;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 37:
                        i9 = SchemaUtil.computeSizeInt64ListNoTag((List) unsafe2.getObject(t, offset2));
                        if (i9 > 0) {
                            if (this.useCachedSizeField) {
                                unsafe2.putInt(t, (long) i3, i9);
                            }
                            i11 = CodedOutputStream.computeTagSize(i27);
                            i10 = CodedOutputStream.computeUInt32SizeNoTag(i9);
                            i6 = i10 + i11 + i9;
                            i25 = i6 + i2;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 38:
                        i9 = SchemaUtil.computeSizeUInt64ListNoTag((List) unsafe2.getObject(t, offset2));
                        if (i9 > 0) {
                            if (this.useCachedSizeField) {
                                unsafe2.putInt(t, (long) i3, i9);
                            }
                            i11 = CodedOutputStream.computeTagSize(i27);
                            i10 = CodedOutputStream.computeUInt32SizeNoTag(i9);
                            i6 = i10 + i11 + i9;
                            i25 = i6 + i2;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 39:
                        i9 = SchemaUtil.computeSizeInt32ListNoTag((List) unsafe2.getObject(t, offset2));
                        if (i9 > 0) {
                            if (this.useCachedSizeField) {
                                unsafe2.putInt(t, (long) i3, i9);
                            }
                            i11 = CodedOutputStream.computeTagSize(i27);
                            i10 = CodedOutputStream.computeUInt32SizeNoTag(i9);
                            i6 = i10 + i11 + i9;
                            i25 = i6 + i2;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 40:
                        i9 = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe2.getObject(t, offset2));
                        if (i9 > 0) {
                            if (this.useCachedSizeField) {
                                unsafe2.putInt(t, (long) i3, i9);
                            }
                            i11 = CodedOutputStream.computeTagSize(i27);
                            i10 = CodedOutputStream.computeUInt32SizeNoTag(i9);
                            i6 = i10 + i11 + i9;
                            i25 = i6 + i2;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 41:
                        i9 = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe2.getObject(t, offset2));
                        if (i9 > 0) {
                            if (this.useCachedSizeField) {
                                unsafe2.putInt(t, (long) i3, i9);
                            }
                            i11 = CodedOutputStream.computeTagSize(i27);
                            i10 = CodedOutputStream.computeUInt32SizeNoTag(i9);
                            i6 = i10 + i11 + i9;
                            i25 = i6 + i2;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 42:
                        Class<?> cls2 = SchemaUtil.GENERATED_MESSAGE_CLASS;
                        i9 = ((List) unsafe2.getObject(t, offset2)).size();
                        if (i9 > 0) {
                            if (this.useCachedSizeField) {
                                unsafe2.putInt(t, (long) i3, i9);
                            }
                            i11 = CodedOutputStream.computeTagSize(i27);
                            i10 = CodedOutputStream.computeUInt32SizeNoTag(i9);
                            i6 = i10 + i11 + i9;
                            i25 = i6 + i2;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 43:
                        i9 = SchemaUtil.computeSizeUInt32ListNoTag((List) unsafe2.getObject(t, offset2));
                        if (i9 > 0) {
                            if (this.useCachedSizeField) {
                                unsafe2.putInt(t, (long) i3, i9);
                            }
                            i11 = CodedOutputStream.computeTagSize(i27);
                            i10 = CodedOutputStream.computeUInt32SizeNoTag(i9);
                            i6 = i10 + i11 + i9;
                            i25 = i6 + i2;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 44:
                        i9 = SchemaUtil.computeSizeEnumListNoTag((List) unsafe2.getObject(t, offset2));
                        if (i9 > 0) {
                            if (this.useCachedSizeField) {
                                unsafe2.putInt(t, (long) i3, i9);
                            }
                            i11 = CodedOutputStream.computeTagSize(i27);
                            i10 = CodedOutputStream.computeUInt32SizeNoTag(i9);
                            i6 = i10 + i11 + i9;
                            i25 = i6 + i2;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case ISystemUiProxy.Stub.TRANSACTION_onBackPressed:
                        i9 = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe2.getObject(t, offset2));
                        if (i9 > 0) {
                            if (this.useCachedSizeField) {
                                unsafe2.putInt(t, (long) i3, i9);
                            }
                            i11 = CodedOutputStream.computeTagSize(i27);
                            i10 = CodedOutputStream.computeUInt32SizeNoTag(i9);
                            i6 = i10 + i11 + i9;
                            i25 = i6 + i2;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case ISystemUiProxy.Stub.TRANSACTION_setHomeRotationEnabled:
                        i9 = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe2.getObject(t, offset2));
                        if (i9 > 0) {
                            if (this.useCachedSizeField) {
                                unsafe2.putInt(t, (long) i3, i9);
                            }
                            i11 = CodedOutputStream.computeTagSize(i27);
                            i10 = CodedOutputStream.computeUInt32SizeNoTag(i9);
                            i6 = i10 + i11 + i9;
                            i25 = i6 + i2;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case ISystemUiProxy.Stub.TRANSACTION_notifySwipeUpGestureStarted:
                        i9 = SchemaUtil.computeSizeSInt32ListNoTag((List) unsafe2.getObject(t, offset2));
                        if (i9 > 0) {
                            if (this.useCachedSizeField) {
                                unsafe2.putInt(t, (long) i3, i9);
                            }
                            i11 = CodedOutputStream.computeTagSize(i27);
                            i10 = CodedOutputStream.computeUInt32SizeNoTag(i9);
                            i6 = i10 + i11 + i9;
                            i25 = i6 + i2;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 48:
                        i9 = SchemaUtil.computeSizeSInt64ListNoTag((List) unsafe2.getObject(t, offset2));
                        if (i9 > 0) {
                            if (this.useCachedSizeField) {
                                unsafe2.putInt(t, (long) i3, i9);
                            }
                            i11 = CodedOutputStream.computeTagSize(i27);
                            i10 = CodedOutputStream.computeUInt32SizeNoTag(i9);
                            i6 = i10 + i11 + i9;
                            i25 = i6 + i2;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 49:
                        i5 = SchemaUtil.computeSizeGroupList(i27, (List) unsafe2.getObject(t, offset2), getMessageFieldSchema(i24));
                        i25 = i2 + i5;
                        z = false;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 50:
                        i5 = this.mapFieldSchema.getSerializedSize(i27, unsafe2.getObject(t, offset2), getMapFieldDefaultEntry(i24));
                        i25 = i2 + i5;
                        z = false;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 51:
                        if (isOneofPresent(t, i27, i24)) {
                            i5 = CodedOutputStream.computeDoubleSize(i27, 0.0d);
                            i25 = i2 + i5;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 52:
                        if (isOneofPresent(t, i27, i24)) {
                            i5 = CodedOutputStream.computeFloatSize(i27, 0.0f);
                            i25 = i2 + i5;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 53:
                        if (isOneofPresent(t, i27, i24)) {
                            i5 = CodedOutputStream.computeInt64Size(i27, oneofLongAt(t, offset2));
                            i25 = i2 + i5;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 54:
                        if (isOneofPresent(t, i27, i24)) {
                            i5 = CodedOutputStream.computeUInt64Size(i27, oneofLongAt(t, offset2));
                            i25 = i2 + i5;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 55:
                        if (isOneofPresent(t, i27, i24)) {
                            i5 = CodedOutputStream.computeInt32Size(i27, oneofIntAt(t, offset2));
                            i25 = i2 + i5;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 56:
                        if (isOneofPresent(t, i27, i24)) {
                            i5 = CodedOutputStream.computeFixed64Size(i27, 0);
                            i25 = i2 + i5;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 57:
                        if (isOneofPresent(t, i27, i24)) {
                            i7 = CodedOutputStream.computeFixed32Size(i27, 0);
                            i25 = i2 + i7;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 58:
                        if (isOneofPresent(t, i27, i24)) {
                            i5 = CodedOutputStream.computeBoolSize(i27, true);
                            i25 = i2 + i5;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 59:
                        if (isOneofPresent(t, i27, i24)) {
                            Object object4 = unsafe2.getObject(t, offset2);
                            if (object4 instanceof ByteString) {
                                i6 = CodedOutputStream.computeBytesSize(i27, (ByteString) object4);
                            } else {
                                i6 = CodedOutputStream.computeStringSize(i27, (String) object4);
                            }
                            i25 = i6 + i2;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 60:
                        if (isOneofPresent(t, i27, i24)) {
                            i5 = SchemaUtil.computeSizeMessage(i27, unsafe2.getObject(t, offset2), getMessageFieldSchema(i24));
                            i25 = i2 + i5;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 61:
                        if (isOneofPresent(t, i27, i24)) {
                            i5 = CodedOutputStream.computeBytesSize(i27, (ByteString) unsafe2.getObject(t, offset2));
                            i25 = i2 + i5;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case SysUiStatsLog.KEYGUARD_STATE_CHANGED:
                        if (isOneofPresent(t, i27, i24)) {
                            i5 = CodedOutputStream.computeUInt32Size(i27, oneofIntAt(t, offset2));
                            i25 = i2 + i5;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case SysUiStatsLog.KEYGUARD_BOUNCER_STATE_CHANGED:
                        if (isOneofPresent(t, i27, i24)) {
                            i5 = CodedOutputStream.computeEnumSize(i27, oneofIntAt(t, offset2));
                            i25 = i2 + i5;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 64:
                        if (isOneofPresent(t, i27, i24)) {
                            i7 = CodedOutputStream.computeSFixed32Size(i27, 0);
                            i25 = i2 + i7;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 65:
                        if (isOneofPresent(t, i27, i24)) {
                            i5 = CodedOutputStream.computeSFixed64Size(i27, 0);
                            i25 = i2 + i5;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 66:
                        if (isOneofPresent(t, i27, i24)) {
                            i5 = CodedOutputStream.computeSInt32Size(i27, oneofIntAt(t, offset2));
                            i25 = i2 + i5;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 67:
                        if (isOneofPresent(t, i27, i24)) {
                            i5 = CodedOutputStream.computeSInt64Size(i27, oneofLongAt(t, offset2));
                            i25 = i2 + i5;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    case 68:
                        if (isOneofPresent(t, i27, i24)) {
                            i5 = CodedOutputStream.computeGroupSize(i27, (MessageLite) unsafe2.getObject(t, offset2), getMessageFieldSchema(i24));
                            i25 = i2 + i5;
                            z = false;
                            i24 += 3;
                            i17 = 1;
                            i18 = 1048575;
                        }
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                    default:
                        z = false;
                        i25 = i2;
                        i24 += 3;
                        i17 = 1;
                        i18 = 1048575;
                }
            }
            UnknownFieldSchema<?, ?> unknownFieldSchema2 = this.unknownFieldSchema;
            int serializedSize = unknownFieldSchema2.getSerializedSize(unknownFieldSchema2.getFromMessage(t)) + i25;
            if (this.hasExtensions) {
                FieldSet<?> extensions = this.extensionSchema.getExtensions(t);
                int i29 = 0;
                for (int i30 = 0; i30 < extensions.fields.getNumArrayEntries(); i30++) {
                    Map.Entry<?, Object> arrayEntryAt = extensions.fields.getArrayEntryAt(i30);
                    i29 += FieldSet.computeFieldSize((FieldSet.FieldDescriptorLite) arrayEntryAt.getKey(), arrayEntryAt.getValue());
                }
                for (Map.Entry<?, Object> entry : extensions.fields.getOverflowEntries()) {
                    i29 += FieldSet.computeFieldSize((FieldSet.FieldDescriptorLite) entry.getKey(), entry.getValue());
                }
                serializedSize += i29;
            }
            return serializedSize;
        }

        @Override // com.google.protobuf.Schema
        public int hashCode(T t) {
            int i;
            int i2;
            int length = this.buffer.length;
            int i3 = 0;
            for (int i4 = 0; i4 < length; i4 += 3) {
                int typeAndOffsetAt = typeAndOffsetAt(i4);
                int i5 = this.buffer[i4];
                long offset = offset(typeAndOffsetAt);
                int i6 = 37;
                switch (type(typeAndOffsetAt)) {
                    case 0:
                        i2 = i3 * 53;
                        i = Internal.hashLong(Double.doubleToLongBits(UnsafeUtil.getDouble(t, offset)));
                        i3 = i + i2;
                        break;
                    case 1:
                        i2 = i3 * 53;
                        i = Float.floatToIntBits(UnsafeUtil.getFloat(t, offset));
                        i3 = i + i2;
                        break;
                    case 2:
                        i2 = i3 * 53;
                        i = Internal.hashLong(UnsafeUtil.getLong(t, offset));
                        i3 = i + i2;
                        break;
                    case 3:
                        i2 = i3 * 53;
                        i = Internal.hashLong(UnsafeUtil.getLong(t, offset));
                        i3 = i + i2;
                        break;
                    case 4:
                        i2 = i3 * 53;
                        i = UnsafeUtil.getInt(t, offset);
                        i3 = i + i2;
                        break;
                    case 5:
                        i2 = i3 * 53;
                        i = Internal.hashLong(UnsafeUtil.getLong(t, offset));
                        i3 = i + i2;
                        break;
                    case 6:
                        i2 = i3 * 53;
                        i = UnsafeUtil.getInt(t, offset);
                        i3 = i + i2;
                        break;
                    case 7:
                        i2 = i3 * 53;
                        i = Internal.hashBoolean(UnsafeUtil.getBoolean(t, offset));
                        i3 = i + i2;
                        break;
                    case 8:
                        i2 = i3 * 53;
                        i = ((String) UnsafeUtil.getObject(t, offset)).hashCode();
                        i3 = i + i2;
                        break;
                    case 9:
                        Object object = UnsafeUtil.getObject(t, offset);
                        if (object != null) {
                            i6 = object.hashCode();
                        }
                        i3 = (i3 * 53) + i6;
                        break;
                    case 10:
                        i2 = i3 * 53;
                        i = UnsafeUtil.getObject(t, offset).hashCode();
                        i3 = i + i2;
                        break;
                    case 11:
                        i2 = i3 * 53;
                        i = UnsafeUtil.getInt(t, offset);
                        i3 = i + i2;
                        break;
                    case 12:
                        i2 = i3 * 53;
                        i = UnsafeUtil.getInt(t, offset);
                        i3 = i + i2;
                        break;
                    case 13:
                        i2 = i3 * 53;
                        i = UnsafeUtil.getInt(t, offset);
                        i3 = i + i2;
                        break;
                    case 14:
                        i2 = i3 * 53;
                        i = Internal.hashLong(UnsafeUtil.getLong(t, offset));
                        i3 = i + i2;
                        break;
                    case 15:
                        i2 = i3 * 53;
                        i = UnsafeUtil.getInt(t, offset);
                        i3 = i + i2;
                        break;
                    case 16:
                        i2 = i3 * 53;
                        i = Internal.hashLong(UnsafeUtil.getLong(t, offset));
                        i3 = i + i2;
                        break;
                    case 17:
                        Object object2 = UnsafeUtil.getObject(t, offset);
                        if (object2 != null) {
                            i6 = object2.hashCode();
                        }
                        i3 = (i3 * 53) + i6;
                        break;
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case InteractionJankMonitorWrapper.CUJ_APP_LAUNCH_FROM_WIDGET:
                    case 28:
                    case ISystemUiProxy.Stub.TRANSACTION_handleImageBundleAsScreenshot:
                    case ISystemUiProxy.Stub.TRANSACTION_expandNotificationPanel:
                    case 31:
                    case 32:
                    case 33:
                    case 34:
                    case 35:
                    case 36:
                    case 37:
                    case 38:
                    case 39:
                    case 40:
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                    case ISystemUiProxy.Stub.TRANSACTION_onBackPressed:
                    case ISystemUiProxy.Stub.TRANSACTION_setHomeRotationEnabled:
                    case ISystemUiProxy.Stub.TRANSACTION_notifySwipeUpGestureStarted:
                    case 48:
                    case 49:
                        i2 = i3 * 53;
                        i = UnsafeUtil.getObject(t, offset).hashCode();
                        i3 = i + i2;
                        break;
                    case 50:
                        i2 = i3 * 53;
                        i = UnsafeUtil.getObject(t, offset).hashCode();
                        i3 = i + i2;
                        break;
                    case 51:
                        if (isOneofPresent(t, i5, i4)) {
                            i2 = i3 * 53;
                            i = Internal.hashLong(Double.doubleToLongBits(oneofDoubleAt(t, offset)));
                            i3 = i + i2;
                            break;
                        } else {
                            break;
                        }
                    case 52:
                        if (isOneofPresent(t, i5, i4)) {
                            i2 = i3 * 53;
                            i = Float.floatToIntBits(oneofFloatAt(t, offset));
                            i3 = i + i2;
                            break;
                        } else {
                            break;
                        }
                    case 53:
                        if (isOneofPresent(t, i5, i4)) {
                            i2 = i3 * 53;
                            i = Internal.hashLong(oneofLongAt(t, offset));
                            i3 = i + i2;
                            break;
                        } else {
                            break;
                        }
                    case 54:
                        if (isOneofPresent(t, i5, i4)) {
                            i2 = i3 * 53;
                            i = Internal.hashLong(oneofLongAt(t, offset));
                            i3 = i + i2;
                            break;
                        } else {
                            break;
                        }
                    case 55:
                        if (isOneofPresent(t, i5, i4)) {
                            i2 = i3 * 53;
                            i = oneofIntAt(t, offset);
                            i3 = i + i2;
                            break;
                        } else {
                            break;
                        }
                    case 56:
                        if (isOneofPresent(t, i5, i4)) {
                            i2 = i3 * 53;
                            i = Internal.hashLong(oneofLongAt(t, offset));
                            i3 = i + i2;
                            break;
                        } else {
                            break;
                        }
                    case 57:
                        if (isOneofPresent(t, i5, i4)) {
                            i2 = i3 * 53;
                            i = oneofIntAt(t, offset);
                            i3 = i + i2;
                            break;
                        } else {
                            break;
                        }
                    case 58:
                        if (isOneofPresent(t, i5, i4)) {
                            i2 = i3 * 53;
                            i = Internal.hashBoolean(oneofBooleanAt(t, offset));
                            i3 = i + i2;
                            break;
                        } else {
                            break;
                        }
                    case 59:
                        if (isOneofPresent(t, i5, i4)) {
                            i2 = i3 * 53;
                            i = ((String) UnsafeUtil.getObject(t, offset)).hashCode();
                            i3 = i + i2;
                            break;
                        } else {
                            break;
                        }
                    case 60:
                        if (isOneofPresent(t, i5, i4)) {
                            i2 = i3 * 53;
                            i = UnsafeUtil.getObject(t, offset).hashCode();
                            i3 = i + i2;
                            break;
                        } else {
                            break;
                        }
                    case 61:
                        if (isOneofPresent(t, i5, i4)) {
                            i2 = i3 * 53;
                            i = UnsafeUtil.getObject(t, offset).hashCode();
                            i3 = i + i2;
                            break;
                        } else {
                            break;
                        }
                    case SysUiStatsLog.KEYGUARD_STATE_CHANGED:
                        if (isOneofPresent(t, i5, i4)) {
                            i2 = i3 * 53;
                            i = oneofIntAt(t, offset);
                            i3 = i + i2;
                            break;
                        } else {
                            break;
                        }
                    case SysUiStatsLog.KEYGUARD_BOUNCER_STATE_CHANGED:
                        if (isOneofPresent(t, i5, i4)) {
                            i2 = i3 * 53;
                            i = oneofIntAt(t, offset);
                            i3 = i + i2;
                            break;
                        } else {
                            break;
                        }
                    case 64:
                        if (isOneofPresent(t, i5, i4)) {
                            i2 = i3 * 53;
                            i = oneofIntAt(t, offset);
                            i3 = i + i2;
                            break;
                        } else {
                            break;
                        }
                    case 65:
                        if (isOneofPresent(t, i5, i4)) {
                            i2 = i3 * 53;
                            i = Internal.hashLong(oneofLongAt(t, offset));
                            i3 = i + i2;
                            break;
                        } else {
                            break;
                        }
                    case 66:
                        if (isOneofPresent(t, i5, i4)) {
                            i2 = i3 * 53;
                            i = oneofIntAt(t, offset);
                            i3 = i + i2;
                            break;
                        } else {
                            break;
                        }
                    case 67:
                        if (isOneofPresent(t, i5, i4)) {
                            i2 = i3 * 53;
                            i = Internal.hashLong(oneofLongAt(t, offset));
                            i3 = i + i2;
                            break;
                        } else {
                            break;
                        }
                    case 68:
                        if (isOneofPresent(t, i5, i4)) {
                            i2 = i3 * 53;
                            i = UnsafeUtil.getObject(t, offset).hashCode();
                            i3 = i + i2;
                            break;
                        } else {
                            break;
                        }
                }
            }
            int hashCode = this.unknownFieldSchema.getFromMessage(t).hashCode() + (i3 * 53);
            return this.hasExtensions ? (hashCode * 53) + this.extensionSchema.getExtensions(t).hashCode() : hashCode;
        }

        public final boolean isFieldPresent(T t, int i, int i2, int i3) {
            if (this.proto3) {
                return isFieldPresent(t, i);
            }
            return (i2 & i3) != 0;
        }

        /* JADX DEBUG: Multi-variable search result rejected for r4v3, resolved type: com.google.protobuf.Schema */
        /* JADX DEBUG: Multi-variable search result rejected for r4v5, resolved type: com.google.protobuf.Schema */
        /* JADX DEBUG: Multi-variable search result rejected for r4v6, resolved type: com.google.protobuf.Schema */
        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.protobuf.Schema
        public final boolean isInitialized(T t) {
            int i;
            int i2 = -1;
            int i3 = 0;
            int i4 = 0;
            while (true) {
                boolean z = true;
                if (i3 >= this.checkInitializedCount) {
                    return !this.hasExtensions || this.extensionSchema.getExtensions(t).isInitialized();
                }
                int i5 = this.intArray[i3];
                int i6 = this.buffer[i5];
                int typeAndOffsetAt = typeAndOffsetAt(i5);
                if (!this.proto3) {
                    int i7 = this.buffer[i5 + 2];
                    int i8 = 1048575 & i7;
                    i = 1 << (i7 >>> 20);
                    if (i8 != i2) {
                        i4 = UNSAFE.getInt(t, (long) i8);
                        i2 = i8;
                    }
                } else {
                    i = 0;
                }
                if (((268435456 & typeAndOffsetAt) != 0) && !isFieldPresent(t, i5, i4, i)) {
                    return false;
                }
                int type = type(typeAndOffsetAt);
                if (type != 9 && type != 17) {
                    if (type != 27) {
                        if (type == 60 || type == 68) {
                            if (isOneofPresent(t, i6, i5) && !getMessageFieldSchema(i5).isInitialized(UnsafeUtil.getObject(t, offset(typeAndOffsetAt)))) {
                                return false;
                            }
                        } else if (type != 49) {
                            if (type == 50 && !this.mapFieldSchema.forMapData(UnsafeUtil.getObject(t, offset(typeAndOffsetAt))).isEmpty()) {
                                this.mapFieldSchema.forMapMetadata(this.objects[(i5 / 3) * 2]);
                                Objects.requireNonNull(null);
                                throw null;
                            }
                        }
                    }
                    List list = (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt));
                    if (!list.isEmpty()) {
                        Schema messageFieldSchema = getMessageFieldSchema(i5);
                        int i9 = 0;
                        while (true) {
                            if (i9 >= list.size()) {
                                break;
                            } else if (!messageFieldSchema.isInitialized(list.get(i9))) {
                                z = false;
                                break;
                            } else {
                                i9++;
                            }
                        }
                    }
                    if (!z) {
                        return false;
                    }
                } else if (isFieldPresent(t, i5, i4, i) && !getMessageFieldSchema(i5).isInitialized(UnsafeUtil.getObject(t, offset(typeAndOffsetAt)))) {
                    return false;
                }
                i3++;
            }
        }

        public final boolean isOneofPresent(T t, int i, int i2) {
            return UnsafeUtil.getInt(t, (long) (this.buffer[i2 + 2] & 1048575)) == i;
        }

        @Override // com.google.protobuf.Schema
        public void makeImmutable(T t) {
            int i;
            int i2 = this.checkInitializedCount;
            while (true) {
                i = this.repeatedFieldOffsetStart;
                if (i2 >= i) {
                    break;
                }
                long offset = offset(typeAndOffsetAt(this.intArray[i2]));
                Object object = UnsafeUtil.getObject(t, offset);
                if (object != null) {
                    UnsafeUtil.putObject(t, offset, this.mapFieldSchema.toImmutable(object));
                }
                i2++;
            }
            int length = this.intArray.length;
            while (i < length) {
                this.listFieldSchema.makeImmutableListAt(t, (long) this.intArray[i]);
                i++;
            }
            this.unknownFieldSchema.makeImmutable(t);
            if (this.hasExtensions) {
                this.extensionSchema.makeImmutable(t);
            }
        }

        @Override // com.google.protobuf.Schema
        public void mergeFrom(T t, T t2) {
            Objects.requireNonNull(t2);
            int i = 0;
            while (true) {
                int[] iArr = this.buffer;
                if (i < iArr.length) {
                    int i2 = iArr[i + 1];
                    long offset = offset(i2);
                    int i3 = this.buffer[i];
                    switch (type(i2)) {
                        case 0:
                            if (!isFieldPresent(t2, i)) {
                                break;
                            } else {
                                UnsafeUtil.putDouble(t, offset, UnsafeUtil.getDouble(t2, offset));
                                setFieldPresent(t, i);
                                break;
                            }
                        case 1:
                            if (!isFieldPresent(t2, i)) {
                                break;
                            } else {
                                UnsafeUtil.putFloat(t, offset, UnsafeUtil.getFloat(t2, offset));
                                setFieldPresent(t, i);
                                break;
                            }
                        case 2:
                            if (!isFieldPresent(t2, i)) {
                                break;
                            } else {
                                UnsafeUtil.putLong(t, offset, UnsafeUtil.getLong(t2, offset));
                                setFieldPresent(t, i);
                                break;
                            }
                        case 3:
                            if (!isFieldPresent(t2, i)) {
                                break;
                            } else {
                                UnsafeUtil.putLong(t, offset, UnsafeUtil.getLong(t2, offset));
                                setFieldPresent(t, i);
                                break;
                            }
                        case 4:
                            if (!isFieldPresent(t2, i)) {
                                break;
                            } else {
                                UnsafeUtil.putInt(t, offset, UnsafeUtil.getInt(t2, offset));
                                setFieldPresent(t, i);
                                break;
                            }
                        case 5:
                            if (!isFieldPresent(t2, i)) {
                                break;
                            } else {
                                UnsafeUtil.putLong(t, offset, UnsafeUtil.getLong(t2, offset));
                                setFieldPresent(t, i);
                                break;
                            }
                        case 6:
                            if (!isFieldPresent(t2, i)) {
                                break;
                            } else {
                                UnsafeUtil.putInt(t, offset, UnsafeUtil.getInt(t2, offset));
                                setFieldPresent(t, i);
                                break;
                            }
                        case 7:
                            if (!isFieldPresent(t2, i)) {
                                break;
                            } else {
                                UnsafeUtil.putBoolean(t, offset, UnsafeUtil.getBoolean(t2, offset));
                                setFieldPresent(t, i);
                                break;
                            }
                        case 8:
                            if (!isFieldPresent(t2, i)) {
                                break;
                            } else {
                                UnsafeUtil.putObject(t, offset, UnsafeUtil.getObject(t2, offset));
                                setFieldPresent(t, i);
                                break;
                            }
                        case 9:
                            mergeMessage(t, t2, i);
                            break;
                        case 10:
                            if (!isFieldPresent(t2, i)) {
                                break;
                            } else {
                                UnsafeUtil.putObject(t, offset, UnsafeUtil.getObject(t2, offset));
                                setFieldPresent(t, i);
                                break;
                            }
                        case 11:
                            if (!isFieldPresent(t2, i)) {
                                break;
                            } else {
                                UnsafeUtil.putInt(t, offset, UnsafeUtil.getInt(t2, offset));
                                setFieldPresent(t, i);
                                break;
                            }
                        case 12:
                            if (!isFieldPresent(t2, i)) {
                                break;
                            } else {
                                UnsafeUtil.putInt(t, offset, UnsafeUtil.getInt(t2, offset));
                                setFieldPresent(t, i);
                                break;
                            }
                        case 13:
                            if (!isFieldPresent(t2, i)) {
                                break;
                            } else {
                                UnsafeUtil.putInt(t, offset, UnsafeUtil.getInt(t2, offset));
                                setFieldPresent(t, i);
                                break;
                            }
                        case 14:
                            if (!isFieldPresent(t2, i)) {
                                break;
                            } else {
                                UnsafeUtil.putLong(t, offset, UnsafeUtil.getLong(t2, offset));
                                setFieldPresent(t, i);
                                break;
                            }
                        case 15:
                            if (!isFieldPresent(t2, i)) {
                                break;
                            } else {
                                UnsafeUtil.putInt(t, offset, UnsafeUtil.getInt(t2, offset));
                                setFieldPresent(t, i);
                                break;
                            }
                        case 16:
                            if (!isFieldPresent(t2, i)) {
                                break;
                            } else {
                                UnsafeUtil.putLong(t, offset, UnsafeUtil.getLong(t2, offset));
                                setFieldPresent(t, i);
                                break;
                            }
                        case 17:
                            mergeMessage(t, t2, i);
                            break;
                        case 18:
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                        case 23:
                        case 24:
                        case 25:
                        case 26:
                        case InteractionJankMonitorWrapper.CUJ_APP_LAUNCH_FROM_WIDGET:
                        case 28:
                        case ISystemUiProxy.Stub.TRANSACTION_handleImageBundleAsScreenshot:
                        case ISystemUiProxy.Stub.TRANSACTION_expandNotificationPanel:
                        case 31:
                        case 32:
                        case 33:
                        case 34:
                        case 35:
                        case 36:
                        case 37:
                        case 38:
                        case 39:
                        case 40:
                        case 41:
                        case 42:
                        case 43:
                        case 44:
                        case ISystemUiProxy.Stub.TRANSACTION_onBackPressed:
                        case ISystemUiProxy.Stub.TRANSACTION_setHomeRotationEnabled:
                        case ISystemUiProxy.Stub.TRANSACTION_notifySwipeUpGestureStarted:
                        case 48:
                        case 49:
                            this.listFieldSchema.mergeListsAt(t, t2, offset);
                            break;
                        case 50:
                            MapFieldSchema mapFieldSchema = this.mapFieldSchema;
                            Class<?> cls = SchemaUtil.GENERATED_MESSAGE_CLASS;
                            UnsafeUtil.putObject(t, offset, mapFieldSchema.mergeFrom(UnsafeUtil.getObject(t, offset), UnsafeUtil.getObject(t2, offset)));
                            break;
                        case 51:
                        case 52:
                        case 53:
                        case 54:
                        case 55:
                        case 56:
                        case 57:
                        case 58:
                        case 59:
                            if (!isOneofPresent(t2, i3, i)) {
                                break;
                            } else {
                                UnsafeUtil.putObject(t, offset, UnsafeUtil.getObject(t2, offset));
                                setOneofPresent(t, i3, i);
                                break;
                            }
                        case 60:
                            mergeOneofMessage(t, t2, i);
                            break;
                        case 61:
                        case SysUiStatsLog.KEYGUARD_STATE_CHANGED:
                        case SysUiStatsLog.KEYGUARD_BOUNCER_STATE_CHANGED:
                        case 64:
                        case 65:
                        case 66:
                        case 67:
                            if (!isOneofPresent(t2, i3, i)) {
                                break;
                            } else {
                                UnsafeUtil.putObject(t, offset, UnsafeUtil.getObject(t2, offset));
                                setOneofPresent(t, i3, i);
                                break;
                            }
                        case 68:
                            mergeOneofMessage(t, t2, i);
                            break;
                    }
                    i += 3;
                } else if (!this.proto3) {
                    UnknownFieldSchema<?, ?> unknownFieldSchema = this.unknownFieldSchema;
                    Class<?> cls2 = SchemaUtil.GENERATED_MESSAGE_CLASS;
                    unknownFieldSchema.setToMessage(t, unknownFieldSchema.merge(unknownFieldSchema.getFromMessage(t), unknownFieldSchema.getFromMessage(t2)));
                    if (this.hasExtensions) {
                        SchemaUtil.mergeExtensions(this.extensionSchema, t, t2);
                        return;
                    }
                    return;
                } else {
                    return;
                }
            }
        }

        public final void mergeMessage(T t, T t2, int i) {
            long offset = offset(this.buffer[i + 1]);
            if (isFieldPresent(t2, i)) {
                Object object = UnsafeUtil.getObject(t, offset);
                Object object2 = UnsafeUtil.getObject(t2, offset);
                if (object != null && object2 != null) {
                    UnsafeUtil.putObject(t, offset, Internal.mergeMessage(object, object2));
                    setFieldPresent(t, i);
                } else if (object2 != null) {
                    UnsafeUtil.putObject(t, offset, object2);
                    setFieldPresent(t, i);
                }
            }
        }

        public final void mergeOneofMessage(T t, T t2, int i) {
            int[] iArr = this.buffer;
            int i2 = iArr[i + 1];
            int i3 = iArr[i];
            long offset = offset(i2);
            if (isOneofPresent(t2, i3, i)) {
                Object object = UnsafeUtil.getObject(t, offset);
                Object object2 = UnsafeUtil.getObject(t2, offset);
                if (object != null && object2 != null) {
                    UnsafeUtil.putObject(t, offset, Internal.mergeMessage(object, object2));
                    setOneofPresent(t, i3, i);
                } else if (object2 != null) {
                    UnsafeUtil.putObject(t, offset, object2);
                    setOneofPresent(t, i3, i);
                }
            }
        }

        @Override // com.google.protobuf.Schema
        public T newInstance() {
            return (T) this.newInstanceSchema.newInstance(this.defaultInstance);
        }

        public final <K, V> int parseMapField(T t, byte[] bArr, int i, int i2, int i3, long j, ArrayDecoders.Registers registers) throws IOException {
            Unsafe unsafe = UNSAFE;
            Object obj = this.objects[(i3 / 3) * 2];
            Object object = unsafe.getObject(t, j);
            if (this.mapFieldSchema.isImmutable(object)) {
                Object newMapField = this.mapFieldSchema.newMapField(obj);
                this.mapFieldSchema.mergeFrom(newMapField, object);
                unsafe.putObject(t, j, newMapField);
                object = newMapField;
            }
            this.mapFieldSchema.forMapMetadata(obj);
            this.mapFieldSchema.forMutableMapData(object);
            int decodeVarint32 = ArrayDecoders.decodeVarint32(bArr, i, registers);
            int i4 = registers.int1;
            if (i4 < 0 || i4 > i2 - decodeVarint32) {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
            Objects.requireNonNull(null);
            throw null;
        }

        public final int parseOneofField(T t, byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, int i7, long j, int i8, ArrayDecoders.Registers registers) throws IOException {
            Unsafe unsafe = UNSAFE;
            long j2 = (long) (this.buffer[i8 + 2] & 1048575);
            boolean z = true;
            switch (i7) {
                case 51:
                    if (i5 == 1) {
                        unsafe.putObject(t, j, Double.valueOf(Double.longBitsToDouble(ArrayDecoders.decodeFixed64(bArr, i))));
                        int i9 = i + 8;
                        unsafe.putInt(t, j2, i4);
                        return i9;
                    }
                    break;
                case 52:
                    if (i5 == 5) {
                        unsafe.putObject(t, j, Float.valueOf(Float.intBitsToFloat(ArrayDecoders.decodeFixed32(bArr, i))));
                        int i10 = i + 4;
                        unsafe.putInt(t, j2, i4);
                        return i10;
                    }
                    break;
                case 53:
                case 54:
                    if (i5 == 0) {
                        int decodeVarint64 = ArrayDecoders.decodeVarint64(bArr, i, registers);
                        unsafe.putObject(t, j, Long.valueOf(registers.long1));
                        unsafe.putInt(t, j2, i4);
                        return decodeVarint64;
                    }
                    break;
                case 55:
                case SysUiStatsLog.KEYGUARD_STATE_CHANGED:
                    if (i5 == 0) {
                        int decodeVarint32 = ArrayDecoders.decodeVarint32(bArr, i, registers);
                        unsafe.putObject(t, j, Integer.valueOf(registers.int1));
                        unsafe.putInt(t, j2, i4);
                        return decodeVarint32;
                    }
                    break;
                case 56:
                case 65:
                    if (i5 == 1) {
                        unsafe.putObject(t, j, Long.valueOf(ArrayDecoders.decodeFixed64(bArr, i)));
                        int i11 = i + 8;
                        unsafe.putInt(t, j2, i4);
                        return i11;
                    }
                    break;
                case 57:
                case 64:
                    if (i5 == 5) {
                        unsafe.putObject(t, j, Integer.valueOf(ArrayDecoders.decodeFixed32(bArr, i)));
                        int i12 = i + 4;
                        unsafe.putInt(t, j2, i4);
                        return i12;
                    }
                    break;
                case 58:
                    if (i5 == 0) {
                        int decodeVarint642 = ArrayDecoders.decodeVarint64(bArr, i, registers);
                        if (registers.long1 == 0) {
                            z = false;
                        }
                        unsafe.putObject(t, j, Boolean.valueOf(z));
                        unsafe.putInt(t, j2, i4);
                        return decodeVarint642;
                    }
                    break;
                case 59:
                    if (i5 == 2) {
                        int decodeVarint322 = ArrayDecoders.decodeVarint32(bArr, i, registers);
                        int i13 = registers.int1;
                        if (i13 == 0) {
                            unsafe.putObject(t, j, "");
                        } else if ((i6 & 536870912) == 0 || Utf8.isValidUtf8(bArr, decodeVarint322, decodeVarint322 + i13)) {
                            unsafe.putObject(t, j, new String(bArr, decodeVarint322, i13, Internal.UTF_8));
                            decodeVarint322 += i13;
                        } else {
                            throw InvalidProtocolBufferException.invalidUtf8();
                        }
                        unsafe.putInt(t, j2, i4);
                        return decodeVarint322;
                    }
                    break;
                case 60:
                    if (i5 == 2) {
                        int decodeMessageField = ArrayDecoders.decodeMessageField(getMessageFieldSchema(i8), bArr, i, i2, registers);
                        Object object = unsafe.getInt(t, j2) == i4 ? unsafe.getObject(t, j) : null;
                        if (object == null) {
                            unsafe.putObject(t, j, registers.object1);
                        } else {
                            unsafe.putObject(t, j, Internal.mergeMessage(object, registers.object1));
                        }
                        unsafe.putInt(t, j2, i4);
                        return decodeMessageField;
                    }
                    break;
                case 61:
                    if (i5 == 2) {
                        int decodeBytes = ArrayDecoders.decodeBytes(bArr, i, registers);
                        unsafe.putObject(t, j, registers.object1);
                        unsafe.putInt(t, j2, i4);
                        return decodeBytes;
                    }
                    break;
                case SysUiStatsLog.KEYGUARD_BOUNCER_STATE_CHANGED:
                    if (i5 == 0) {
                        int decodeVarint323 = ArrayDecoders.decodeVarint32(bArr, i, registers);
                        int i14 = registers.int1;
                        Internal.EnumVerifier enumVerifier = (Internal.EnumVerifier) this.objects[((i8 / 3) * 2) + 1];
                        if (enumVerifier == null || enumVerifier.isInRange(i14)) {
                            unsafe.putObject(t, j, Integer.valueOf(i14));
                            unsafe.putInt(t, j2, i4);
                        } else {
                            getMutableUnknownFields(t).storeField(i3, Long.valueOf((long) i14));
                        }
                        return decodeVarint323;
                    }
                    break;
                case 66:
                    if (i5 == 0) {
                        int decodeVarint324 = ArrayDecoders.decodeVarint32(bArr, i, registers);
                        unsafe.putObject(t, j, Integer.valueOf(CodedInputStream.decodeZigZag32(registers.int1)));
                        unsafe.putInt(t, j2, i4);
                        return decodeVarint324;
                    }
                    break;
                case 67:
                    if (i5 == 0) {
                        int decodeVarint643 = ArrayDecoders.decodeVarint64(bArr, i, registers);
                        unsafe.putObject(t, j, Long.valueOf(CodedInputStream.decodeZigZag64(registers.long1)));
                        unsafe.putInt(t, j2, i4);
                        return decodeVarint643;
                    }
                    break;
                case 68:
                    if (i5 == 3) {
                        int decodeGroupField = ArrayDecoders.decodeGroupField(getMessageFieldSchema(i8), bArr, i, i2, (i3 & -8) | 4, registers);
                        Object object2 = unsafe.getInt(t, j2) == i4 ? unsafe.getObject(t, j) : null;
                        if (object2 == null) {
                            unsafe.putObject(t, j, registers.object1);
                        } else {
                            unsafe.putObject(t, j, Internal.mergeMessage(object2, registers.object1));
                        }
                        unsafe.putInt(t, j2, i4);
                        return decodeGroupField;
                    }
                    break;
            }
            return i;
        }

        /* JADX DEBUG: Type inference failed for r6v0. Raw type applied. Possible types: com.google.protobuf.UnknownFieldSchema<?, ?>, com.google.protobuf.UnknownFieldSchema<UT, UB> */
        /* JADX WARNING: Code restructure failed: missing block: B:127:0x037d, code lost:
            if (r0 != r4) goto L_0x03cd;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:136:0x03cb, code lost:
            if (r0 != r14) goto L_0x03cd;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:137:0x03cd, code lost:
            r10 = r29;
            r13 = r30;
            r9 = r32;
            r1 = r34;
            r11 = r35;
            r12 = r17;
            r7 = r18;
            r3 = r19;
            r8 = r26;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:138:0x03e1, code lost:
            r9 = r32;
            r6 = r34;
            r2 = r0;
         */
        /* JADX WARNING: Removed duplicated region for block: B:145:0x03ff  */
        /* JADX WARNING: Removed duplicated region for block: B:153:0x043f  */
        /* JADX WARNING: Removed duplicated region for block: B:188:0x004a A[SYNTHETIC] */
        /* JADX WARNING: Removed duplicated region for block: B:20:0x0056  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int parseProto2Message(T r30, byte[] r31, int r32, int r33, int r34, com.google.protobuf.ArrayDecoders.Registers r35) throws java.io.IOException {
            /*
            // Method dump skipped, instructions count: 1224
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.MessageSchema.parseProto2Message(java.lang.Object, byte[], int, int, int, com.google.protobuf.ArrayDecoders$Registers):int");
        }

        /* JADX WARNING: Removed duplicated region for block: B:103:0x0215  */
        /* JADX WARNING: Removed duplicated region for block: B:54:0x0149  */
        /* JADX WARNING: Removed duplicated region for block: B:83:0x01c5  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final int parseRepeatedField(T r17, byte[] r18, int r19, int r20, int r21, int r22, int r23, int r24, long r25, int r27, long r28, com.google.protobuf.ArrayDecoders.Registers r30) throws java.io.IOException {
            /*
            // Method dump skipped, instructions count: 1002
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.MessageSchema.parseRepeatedField(java.lang.Object, byte[], int, int, int, int, int, int, long, int, long, com.google.protobuf.ArrayDecoders$Registers):int");
        }

        public final int positionForFieldNumber(int i) {
            if (i < this.minFieldNumber || i > this.maxFieldNumber) {
                return -1;
            }
            return slowPositionForFieldNumber(i, 0);
        }

        public final void setFieldPresent(T t, int i) {
            if (!this.proto3) {
                int i2 = this.buffer[i + 2];
                long j = (long) (i2 & 1048575);
                UnsafeUtil.putInt(t, j, UnsafeUtil.getInt(t, j) | (1 << (i2 >>> 20)));
            }
        }

        public final void setOneofPresent(T t, int i, int i2) {
            UnsafeUtil.putInt(t, (long) (this.buffer[i2 + 2] & 1048575), i);
        }

        public final int slowPositionForFieldNumber(int i, int i2) {
            int length = (this.buffer.length / 3) - 1;
            while (i2 <= length) {
                int i3 = (length + i2) >>> 1;
                int i4 = i3 * 3;
                int i5 = this.buffer[i4];
                if (i == i5) {
                    return i4;
                }
                if (i < i5) {
                    length = i3 - 1;
                } else {
                    i2 = i3 + 1;
                }
            }
            return -1;
        }

        public final int typeAndOffsetAt(int i) {
            return this.buffer[i + 1];
        }

        /* JADX WARNING: Removed duplicated region for block: B:10:0x0025  */
        /* JADX WARNING: Removed duplicated region for block: B:163:0x061b  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void writeFieldsInAscendingOrderProto3(T r13, com.google.protobuf.Writer r14) throws java.io.IOException {
            /*
            // Method dump skipped, instructions count: 1736
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.MessageSchema.writeFieldsInAscendingOrderProto3(java.lang.Object, com.google.protobuf.Writer):void");
        }

        public final <K, V> void writeMapHelper(Writer writer, int i, Object obj, int i2) throws IOException {
            if (obj != null) {
                this.mapFieldSchema.forMapMetadata(this.objects[(i2 / 3) * 2]);
                Map<?, ?> forMapData = this.mapFieldSchema.forMapData(obj);
                CodedOutputStreamWriter codedOutputStreamWriter = (CodedOutputStreamWriter) writer;
                Objects.requireNonNull(codedOutputStreamWriter.output);
                Iterator<Map.Entry<?, ?>> it = forMapData.entrySet().iterator();
                if (it.hasNext()) {
                    Map.Entry<?, ?> next = it.next();
                    codedOutputStreamWriter.output.writeTag(i, 2);
                    next.getKey();
                    next.getValue();
                    Objects.requireNonNull(null);
                    throw null;
                }
            }
        }

        public final void writeString(int i, Object obj, Writer writer) throws IOException {
            if (obj instanceof String) {
                ((CodedOutputStreamWriter) writer).output.writeString(i, (String) obj);
                return;
            }
            ((CodedOutputStreamWriter) writer).output.writeBytes(i, (ByteString) obj);
        }

        /* JADX WARNING: Removed duplicated region for block: B:13:0x0039  */
        /* JADX WARNING: Removed duplicated region for block: B:176:0x0540  */
        @Override // com.google.protobuf.Schema
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void writeTo(T r18, com.google.protobuf.Writer r19) throws java.io.IOException {
            /*
            // Method dump skipped, instructions count: 1518
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.MessageSchema.writeTo(java.lang.Object, com.google.protobuf.Writer):void");
        }

        public final boolean isFieldPresent(T t, int i) {
            if (this.proto3) {
                int i2 = this.buffer[i + 1];
                long offset = offset(i2);
                switch (type(i2)) {
                    case 0:
                        return UnsafeUtil.getDouble(t, offset) != 0.0d;
                    case 1:
                        return UnsafeUtil.getFloat(t, offset) != 0.0f;
                    case 2:
                        return UnsafeUtil.getLong(t, offset) != 0;
                    case 3:
                        return UnsafeUtil.getLong(t, offset) != 0;
                    case 4:
                        return UnsafeUtil.getInt(t, offset) != 0;
                    case 5:
                        return UnsafeUtil.getLong(t, offset) != 0;
                    case 6:
                        return UnsafeUtil.getInt(t, offset) != 0;
                    case 7:
                        return UnsafeUtil.getBoolean(t, offset);
                    case 8:
                        Object object = UnsafeUtil.getObject(t, offset);
                        if (object instanceof String) {
                            return !((String) object).isEmpty();
                        }
                        if (object instanceof ByteString) {
                            return !ByteString.EMPTY.equals(object);
                        }
                        throw new IllegalArgumentException();
                    case 9:
                        return UnsafeUtil.getObject(t, offset) != null;
                    case 10:
                        return !ByteString.EMPTY.equals(UnsafeUtil.getObject(t, offset));
                    case 11:
                        return UnsafeUtil.getInt(t, offset) != 0;
                    case 12:
                        return UnsafeUtil.getInt(t, offset) != 0;
                    case 13:
                        return UnsafeUtil.getInt(t, offset) != 0;
                    case 14:
                        return UnsafeUtil.getLong(t, offset) != 0;
                    case 15:
                        return UnsafeUtil.getInt(t, offset) != 0;
                    case 16:
                        return UnsafeUtil.getLong(t, offset) != 0;
                    case 17:
                        return UnsafeUtil.getObject(t, offset) != null;
                    default:
                        throw new IllegalArgumentException();
                }
            } else {
                int i3 = this.buffer[i + 2];
                return (UnsafeUtil.getInt(t, (long) (i3 & 1048575)) & (1 << (i3 >>> 20))) != 0;
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:105:0x0238, code lost:
            if (r0 != r15) goto L_0x024c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:106:0x023b, code lost:
            r2 = r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:96:0x01ee, code lost:
            if (r0 != r8) goto L_0x024c;
         */
        /* JADX WARNING: Removed duplicated region for block: B:124:0x004b A[SYNTHETIC] */
        /* JADX WARNING: Removed duplicated region for block: B:22:0x0056  */
        @Override // com.google.protobuf.Schema
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void mergeFrom(T r28, byte[] r29, int r30, int r31, com.google.protobuf.ArrayDecoders.Registers r32) throws java.io.IOException {
            /*
            // Method dump skipped, instructions count: 674
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.MessageSchema.mergeFrom(java.lang.Object, byte[], int, int, com.google.protobuf.ArrayDecoders$Registers):void");
        }
    }
