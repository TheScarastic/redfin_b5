package com.google.protobuf;
/* loaded from: classes.dex */
public final class NewInstanceSchemas {
    public static final NewInstanceSchema FULL_SCHEMA;
    public static final NewInstanceSchema LITE_SCHEMA;

    static {
        NewInstanceSchema newInstanceSchema;
        try {
            newInstanceSchema = (NewInstanceSchema) Class.forName("com.google.protobuf.NewInstanceSchemaFull").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception unused) {
            newInstanceSchema = null;
        }
        FULL_SCHEMA = newInstanceSchema;
        LITE_SCHEMA = new NewInstanceSchemaLite();
    }
}
