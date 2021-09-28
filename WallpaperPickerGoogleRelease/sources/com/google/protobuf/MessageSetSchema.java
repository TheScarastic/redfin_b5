package com.google.protobuf;

import com.google.protobuf.FieldSet;
import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.LazyField;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
/* loaded from: classes.dex */
public final class MessageSetSchema<T> implements Schema<T> {
    public final MessageLite defaultInstance;
    public final ExtensionSchema<?> extensionSchema;
    public final boolean hasExtensions;
    public final UnknownFieldSchema<?, ?> unknownFieldSchema;

    public MessageSetSchema(UnknownFieldSchema<?, ?> unknownFieldSchema, ExtensionSchema<?> extensionSchema, MessageLite messageLite) {
        this.unknownFieldSchema = unknownFieldSchema;
        this.hasExtensions = extensionSchema.hasExtensions(messageLite);
        this.extensionSchema = extensionSchema;
        this.defaultInstance = messageLite;
    }

    @Override // com.google.protobuf.Schema
    public boolean equals(T t, T t2) {
        if (!this.unknownFieldSchema.getFromMessage(t).equals(this.unknownFieldSchema.getFromMessage(t2))) {
            return false;
        }
        if (this.hasExtensions) {
            return this.extensionSchema.getExtensions(t).equals(this.extensionSchema.getExtensions(t2));
        }
        return true;
    }

    @Override // com.google.protobuf.Schema
    public int getSerializedSize(T t) {
        UnknownFieldSchema<?, ?> unknownFieldSchema = this.unknownFieldSchema;
        int serializedSizeAsMessageSet = unknownFieldSchema.getSerializedSizeAsMessageSet(unknownFieldSchema.getFromMessage(t)) + 0;
        if (!this.hasExtensions) {
            return serializedSizeAsMessageSet;
        }
        FieldSet<?> extensions = this.extensionSchema.getExtensions(t);
        int i = 0;
        for (int i2 = 0; i2 < extensions.fields.getNumArrayEntries(); i2++) {
            i += extensions.getMessageSetSerializedSize(extensions.fields.getArrayEntryAt(i2));
        }
        for (Map.Entry<?, Object> entry : extensions.fields.getOverflowEntries()) {
            i += extensions.getMessageSetSerializedSize(entry);
        }
        return serializedSizeAsMessageSet + i;
    }

    @Override // com.google.protobuf.Schema
    public int hashCode(T t) {
        int hashCode = this.unknownFieldSchema.getFromMessage(t).hashCode();
        return this.hasExtensions ? (hashCode * 53) + this.extensionSchema.getExtensions(t).hashCode() : hashCode;
    }

    @Override // com.google.protobuf.Schema
    public final boolean isInitialized(T t) {
        return this.extensionSchema.getExtensions(t).isInitialized();
    }

    @Override // com.google.protobuf.Schema
    public void makeImmutable(T t) {
        this.unknownFieldSchema.makeImmutable(t);
        this.extensionSchema.makeImmutable(t);
    }

    @Override // com.google.protobuf.Schema
    public void mergeFrom(T t, T t2) {
        UnknownFieldSchema<?, ?> unknownFieldSchema = this.unknownFieldSchema;
        Class<?> cls = SchemaUtil.GENERATED_MESSAGE_CLASS;
        unknownFieldSchema.setToMessage(t, unknownFieldSchema.merge(unknownFieldSchema.getFromMessage(t), unknownFieldSchema.getFromMessage(t2)));
        if (this.hasExtensions) {
            SchemaUtil.mergeExtensions(this.extensionSchema, t, t2);
        }
    }

    @Override // com.google.protobuf.Schema
    public T newInstance() {
        return (T) ((GeneratedMessageLite.Builder) this.defaultInstance.newBuilderForType()).buildPartial();
    }

    @Override // com.google.protobuf.Schema
    public void writeTo(T t, Writer writer) throws IOException {
        Iterator<Map.Entry<?, Object>> it = this.extensionSchema.getExtensions(t).iterator();
        while (it.hasNext()) {
            Map.Entry<?, Object> next = it.next();
            FieldSet.FieldDescriptorLite fieldDescriptorLite = (FieldSet.FieldDescriptorLite) next.getKey();
            if (fieldDescriptorLite.getLiteJavaType() != WireFormat$JavaType.MESSAGE || fieldDescriptorLite.isRepeated() || fieldDescriptorLite.isPacked()) {
                throw new IllegalStateException("Found invalid MessageSet item.");
            } else if (next instanceof LazyField.LazyEntry) {
                ((CodedOutputStreamWriter) writer).writeMessageSetItem(fieldDescriptorLite.getNumber(), ((LazyField.LazyEntry) next).entry.getValue().toByteString());
            } else {
                ((CodedOutputStreamWriter) writer).writeMessageSetItem(fieldDescriptorLite.getNumber(), next.getValue());
            }
        }
        UnknownFieldSchema<?, ?> unknownFieldSchema = this.unknownFieldSchema;
        unknownFieldSchema.writeAsMessageSetTo(unknownFieldSchema.getFromMessage(t), writer);
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x0087  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x008c A[EDGE_INSN: B:57:0x008c->B:33:0x008c ?: BREAK  , SYNTHETIC] */
    @Override // com.google.protobuf.Schema
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void mergeFrom(T r10, byte[] r11, int r12, int r13, com.google.protobuf.ArrayDecoders.Registers r14) throws java.io.IOException {
        /*
            r9 = this;
            r0 = r10
            com.google.protobuf.GeneratedMessageLite r0 = (com.google.protobuf.GeneratedMessageLite) r0
            com.google.protobuf.UnknownFieldSetLite r1 = r0.unknownFields
            com.google.protobuf.UnknownFieldSetLite r2 = com.google.protobuf.UnknownFieldSetLite.DEFAULT_INSTANCE
            if (r1 != r2) goto L_0x000f
            com.google.protobuf.UnknownFieldSetLite r1 = com.google.protobuf.UnknownFieldSetLite.newInstance()
            r0.unknownFields = r1
        L_0x000f:
            com.google.protobuf.GeneratedMessageLite$ExtendableMessage r10 = (com.google.protobuf.GeneratedMessageLite.ExtendableMessage) r10
            r10.ensureExtensionsAreMutable()
            r10 = 0
            r0 = r10
        L_0x0016:
            if (r12 >= r13) goto L_0x0096
            int r4 = com.google.protobuf.ArrayDecoders.decodeVarint32(r11, r12, r14)
            int r2 = r14.int1
            r12 = 11
            r3 = 2
            if (r2 == r12) goto L_0x0049
            r12 = r2 & 7
            if (r12 != r3) goto L_0x0044
            com.google.protobuf.ExtensionSchema<?> r12 = r9.extensionSchema
            com.google.protobuf.ExtensionRegistryLite r0 = r14.extensionRegistry
            com.google.protobuf.MessageLite r3 = r9.defaultInstance
            int r5 = r2 >>> 3
            java.lang.Object r12 = r12.findExtensionByNumber(r0, r3, r5)
            r0 = r12
            com.google.protobuf.GeneratedMessageLite$GeneratedExtension r0 = (com.google.protobuf.GeneratedMessageLite.GeneratedExtension) r0
            if (r0 != 0) goto L_0x0041
            r3 = r11
            r5 = r13
            r6 = r1
            r7 = r14
            int r12 = com.google.protobuf.ArrayDecoders.decodeUnknownField(r2, r3, r4, r5, r6, r7)
            goto L_0x0016
        L_0x0041:
            com.google.protobuf.Protobuf r9 = com.google.protobuf.Protobuf.INSTANCE
            throw r10
        L_0x0044:
            int r12 = com.google.protobuf.ArrayDecoders.skipField(r2, r11, r4, r13, r14)
            goto L_0x0016
        L_0x0049:
            r12 = 0
            r2 = r10
        L_0x004b:
            if (r4 >= r13) goto L_0x008c
            int r4 = com.google.protobuf.ArrayDecoders.decodeVarint32(r11, r4, r14)
            int r5 = r14.int1
            int r6 = r5 >>> 3
            r7 = r5 & 7
            if (r6 == r3) goto L_0x006d
            r8 = 3
            if (r6 == r8) goto L_0x005d
            goto L_0x0082
        L_0x005d:
            if (r0 != 0) goto L_0x006a
            if (r7 != r3) goto L_0x0082
            int r4 = com.google.protobuf.ArrayDecoders.decodeBytes(r11, r4, r14)
            java.lang.Object r2 = r14.object1
            com.google.protobuf.ByteString r2 = (com.google.protobuf.ByteString) r2
            goto L_0x004b
        L_0x006a:
            com.google.protobuf.Protobuf r9 = com.google.protobuf.Protobuf.INSTANCE
            throw r10
        L_0x006d:
            if (r7 != 0) goto L_0x0082
            int r4 = com.google.protobuf.ArrayDecoders.decodeVarint32(r11, r4, r14)
            int r12 = r14.int1
            com.google.protobuf.ExtensionSchema<?> r0 = r9.extensionSchema
            com.google.protobuf.ExtensionRegistryLite r5 = r14.extensionRegistry
            com.google.protobuf.MessageLite r6 = r9.defaultInstance
            java.lang.Object r0 = r0.findExtensionByNumber(r5, r6, r12)
            com.google.protobuf.GeneratedMessageLite$GeneratedExtension r0 = (com.google.protobuf.GeneratedMessageLite.GeneratedExtension) r0
            goto L_0x004b
        L_0x0082:
            r6 = 12
            if (r5 != r6) goto L_0x0087
            goto L_0x008c
        L_0x0087:
            int r4 = com.google.protobuf.ArrayDecoders.skipField(r5, r11, r4, r13, r14)
            goto L_0x004b
        L_0x008c:
            if (r2 == 0) goto L_0x0094
            int r12 = r12 << 3
            r12 = r12 | r3
            r1.storeField(r12, r2)
        L_0x0094:
            r12 = r4
            goto L_0x0016
        L_0x0096:
            if (r12 != r13) goto L_0x0099
            return
        L_0x0099:
            com.google.protobuf.InvalidProtocolBufferException r9 = com.google.protobuf.InvalidProtocolBufferException.parseFailure()
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.MessageSetSchema.mergeFrom(java.lang.Object, byte[], int, int, com.google.protobuf.ArrayDecoders$Registers):void");
    }
}
