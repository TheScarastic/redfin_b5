package kotlin.jvm.internal;
/* compiled from: PackageReference.kt */
/* loaded from: classes2.dex */
public final class PackageReference implements ClassBasedDeclarationContainer {
    private final Class<?> jClass;
    private final String moduleName;

    public PackageReference(Class<?> cls, String str) {
        Intrinsics.checkNotNullParameter(cls, "jClass");
        Intrinsics.checkNotNullParameter(str, "moduleName");
        this.jClass = cls;
        this.moduleName = str;
    }

    @Override // kotlin.jvm.internal.ClassBasedDeclarationContainer
    public Class<?> getJClass() {
        return this.jClass;
    }

    public boolean equals(Object obj) {
        return (obj instanceof PackageReference) && Intrinsics.areEqual(getJClass(), ((PackageReference) obj).getJClass());
    }

    public int hashCode() {
        return getJClass().hashCode();
    }

    public String toString() {
        return getJClass().toString() + " (Kotlin reflection is not available)";
    }
}
