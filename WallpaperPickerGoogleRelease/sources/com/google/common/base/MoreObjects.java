package com.google.common.base;

import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MoreObjects {

    /* loaded from: classes.dex */
    public static final class ToStringHelper {
        public final String className;
        public final ValueHolder holderHead;
        public ValueHolder holderTail;

        /* loaded from: classes.dex */
        public static final class ValueHolder {
            public String name;
            public ValueHolder next;
            public Object value;

            public ValueHolder(AnonymousClass1 r1) {
            }
        }

        public ToStringHelper(String str, AnonymousClass1 r3) {
            ValueHolder valueHolder = new ValueHolder(null);
            this.holderHead = valueHolder;
            this.holderTail = valueHolder;
            this.className = str;
        }

        public ToStringHelper add(String str, int i) {
            String valueOf = String.valueOf(i);
            ValueHolder valueHolder = new ValueHolder(null);
            this.holderTail.next = valueHolder;
            this.holderTail = valueHolder;
            valueHolder.value = valueOf;
            valueHolder.name = str;
            return this;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(32);
            sb.append(this.className);
            sb.append('{');
            ValueHolder valueHolder = this.holderHead.next;
            String str = "";
            while (valueHolder != null) {
                Object obj = valueHolder.value;
                sb.append(str);
                String str2 = valueHolder.name;
                if (str2 != null) {
                    sb.append(str2);
                    sb.append('=');
                }
                if (obj == null || !obj.getClass().isArray()) {
                    sb.append(obj);
                } else {
                    String deepToString = Arrays.deepToString(new Object[]{obj});
                    sb.append((CharSequence) deepToString, 1, deepToString.length() - 1);
                }
                valueHolder = valueHolder.next;
                str = ", ";
            }
            sb.append('}');
            return sb.toString();
        }
    }

    public static <T> T firstNonNull(T t, T t2) {
        if (t != null) {
            return t;
        }
        Objects.requireNonNull(t2, "Both parameters are null");
        return t2;
    }
}
