package com.google.protobuf;

import java.nio.charset.Charset;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
/* loaded from: classes.dex */
public final class Protobuf {
    public static final Protobuf INSTANCE = new Protobuf();
    public final ConcurrentMap<Class<?>, Schema<?>> schemaCache = new ConcurrentHashMap();
    public final SchemaFactory schemaFactory = new ManifestSchemaFactory();

    public <T> Schema<T> schemaFor(Class<T> cls) {
        Schema<T> schema;
        MessageSetSchema messageSetSchema;
        Class<?> cls2;
        Charset charset = Internal.UTF_8;
        Objects.requireNonNull(cls, "messageType");
        Schema<T> schema2 = (Schema<T>) this.schemaCache.get(cls);
        if (schema2 != null) {
            return schema2;
        }
        ManifestSchemaFactory manifestSchemaFactory = (ManifestSchemaFactory) this.schemaFactory;
        Objects.requireNonNull(manifestSchemaFactory);
        Class<?> cls3 = SchemaUtil.GENERATED_MESSAGE_CLASS;
        if (GeneratedMessageLite.class.isAssignableFrom(cls) || (cls2 = SchemaUtil.GENERATED_MESSAGE_CLASS) == null || cls2.isAssignableFrom(cls)) {
            MessageInfo messageInfoFor = manifestSchemaFactory.messageInfoFactory.messageInfoFor(cls);
            if (messageInfoFor.isMessageSetWireFormat()) {
                if (GeneratedMessageLite.class.isAssignableFrom(cls)) {
                    UnknownFieldSchema<?, ?> unknownFieldSchema = SchemaUtil.UNKNOWN_FIELD_SET_LITE_SCHEMA;
                    ExtensionSchema<?> extensionSchema = ExtensionSchemas.LITE_SCHEMA;
                    messageSetSchema = new MessageSetSchema(unknownFieldSchema, ExtensionSchemas.LITE_SCHEMA, messageInfoFor.getDefaultInstance());
                } else {
                    UnknownFieldSchema<?, ?> unknownFieldSchema2 = SchemaUtil.PROTO2_UNKNOWN_FIELD_SET_SCHEMA;
                    ExtensionSchema<?> extensionSchema2 = ExtensionSchemas.FULL_SCHEMA;
                    if (extensionSchema2 != null) {
                        messageSetSchema = new MessageSetSchema(unknownFieldSchema2, extensionSchema2, messageInfoFor.getDefaultInstance());
                    } else {
                        throw new IllegalStateException("Protobuf runtime is not correctly loaded.");
                    }
                }
                schema = messageSetSchema;
            } else {
                boolean z = false;
                if (GeneratedMessageLite.class.isAssignableFrom(cls)) {
                    if (messageInfoFor.getSyntax$enumunboxing$() == 1) {
                        z = true;
                    }
                    if (z) {
                        NewInstanceSchema newInstanceSchema = NewInstanceSchemas.LITE_SCHEMA;
                        ListFieldSchema listFieldSchema = ListFieldSchema.LITE_INSTANCE;
                        UnknownFieldSchema<?, ?> unknownFieldSchema3 = SchemaUtil.UNKNOWN_FIELD_SET_LITE_SCHEMA;
                        ExtensionSchema<?> extensionSchema3 = ExtensionSchemas.LITE_SCHEMA;
                        schema = MessageSchema.newSchema(messageInfoFor, newInstanceSchema, listFieldSchema, unknownFieldSchema3, ExtensionSchemas.LITE_SCHEMA, MapFieldSchemas.LITE_SCHEMA);
                    } else {
                        schema = MessageSchema.newSchema(messageInfoFor, NewInstanceSchemas.LITE_SCHEMA, ListFieldSchema.LITE_INSTANCE, SchemaUtil.UNKNOWN_FIELD_SET_LITE_SCHEMA, null, MapFieldSchemas.LITE_SCHEMA);
                    }
                } else {
                    if (messageInfoFor.getSyntax$enumunboxing$() == 1) {
                        z = true;
                    }
                    if (z) {
                        NewInstanceSchema newInstanceSchema2 = NewInstanceSchemas.FULL_SCHEMA;
                        ListFieldSchema listFieldSchema2 = ListFieldSchema.FULL_INSTANCE;
                        UnknownFieldSchema<?, ?> unknownFieldSchema4 = SchemaUtil.PROTO2_UNKNOWN_FIELD_SET_SCHEMA;
                        ExtensionSchema<?> extensionSchema4 = ExtensionSchemas.FULL_SCHEMA;
                        if (extensionSchema4 != null) {
                            schema = MessageSchema.newSchema(messageInfoFor, newInstanceSchema2, listFieldSchema2, unknownFieldSchema4, extensionSchema4, MapFieldSchemas.FULL_SCHEMA);
                        } else {
                            throw new IllegalStateException("Protobuf runtime is not correctly loaded.");
                        }
                    } else {
                        schema = MessageSchema.newSchema(messageInfoFor, NewInstanceSchemas.FULL_SCHEMA, ListFieldSchema.FULL_INSTANCE, SchemaUtil.PROTO3_UNKNOWN_FIELD_SET_SCHEMA, null, MapFieldSchemas.FULL_SCHEMA);
                    }
                }
            }
            Schema<T> schema3 = (Schema<T>) this.schemaCache.putIfAbsent(cls, schema);
            return schema3 != null ? schema3 : schema;
        }
        throw new IllegalArgumentException("Message classes must extend GeneratedMessage or GeneratedMessageLite");
    }

    public <T> Schema<T> schemaFor(T t) {
        return schemaFor((Class) t.getClass());
    }
}
