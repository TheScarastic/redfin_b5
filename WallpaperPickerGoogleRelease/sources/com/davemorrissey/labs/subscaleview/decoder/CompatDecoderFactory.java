package com.davemorrissey.labs.subscaleview.decoder;

import java.lang.reflect.InvocationTargetException;
/* loaded from: classes.dex */
public class CompatDecoderFactory<T> implements DecoderFactory<T> {
    public final Class<? extends T> clazz;

    public CompatDecoderFactory(Class<? extends T> cls) {
        this.clazz = cls;
    }

    @Override // com.davemorrissey.labs.subscaleview.decoder.DecoderFactory
    public T make() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        return (T) this.clazz.newInstance();
    }
}
