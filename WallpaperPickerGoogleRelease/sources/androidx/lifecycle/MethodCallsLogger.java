package androidx.lifecycle;

import android.app.AlarmManager;
import android.content.Context;
import com.google.android.gms.internal.zzfib;
import com.google.android.gms.internal.zzfis;
import com.google.android.gms.internal.zzfiy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class MethodCallsLogger implements zzfiy {
    public final /* synthetic */ int $r8$classId;
    public Map<String, Integer> mCalledMethods;

    /* JADX WARN: Type inference failed for: r2v2, types: [java.util.Set, java.util.Map<java.lang.String, java.lang.Integer>] */
    public MethodCallsLogger(int i) {
        this.$r8$classId = i;
        if (i != 5) {
            this.mCalledMethods = new HashMap();
        } else {
            this.mCalledMethods = Collections.newSetFromMap(new WeakHashMap());
        }
    }

    @Override // com.google.android.gms.internal.zzfiy
    public Object zza() {
        zzfis zzfis = (zzfis) this.mCalledMethods;
        Objects.requireNonNull(zzfis);
        return zzfib.zza(zzfis.zzc.getContentResolver(), zzfis.zzh);
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [android.app.AlarmManager, java.util.Map<java.lang.String, java.lang.Integer>] */
    public MethodCallsLogger(Context context) {
        this.$r8$classId = 3;
        this.mCalledMethods = (AlarmManager) context.getSystemService("alarm");
    }
}
