package com.android.wm.shell.protolog;

import android.util.Log;
import com.android.internal.protolog.BaseProtoLogImpl;
import com.android.internal.protolog.ProtoLogViewerConfigReader;
import com.android.internal.protolog.common.IProtoLogGroup;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import org.json.JSONException;
/* loaded from: classes2.dex */
public class ShellProtoLogImpl extends BaseProtoLogImpl {
    private static final String LOG_FILENAME = new File("wm_shell_log.pb").getAbsolutePath();
    private static ShellProtoLogImpl sServiceInstance = null;

    static {
        BaseProtoLogImpl.addLogGroupEnum(ShellProtoLogGroup.values());
    }

    public static void v(IProtoLogGroup iProtoLogGroup, int i, int i2, String str, Object... objArr) {
        getSingleInstance().log(BaseProtoLogImpl.LogLevel.VERBOSE, iProtoLogGroup, i, i2, str, objArr);
    }

    public static void w(IProtoLogGroup iProtoLogGroup, int i, int i2, String str, Object... objArr) {
        getSingleInstance().log(BaseProtoLogImpl.LogLevel.WARN, iProtoLogGroup, i, i2, str, objArr);
    }

    public static void e(IProtoLogGroup iProtoLogGroup, int i, int i2, String str, Object... objArr) {
        getSingleInstance().log(BaseProtoLogImpl.LogLevel.ERROR, iProtoLogGroup, i, i2, str, objArr);
    }

    public static boolean isEnabled(IProtoLogGroup iProtoLogGroup) {
        return iProtoLogGroup.isLogToLogcat() || (iProtoLogGroup.isLogToProto() && getSingleInstance().isProtoEnabled());
    }

    public static synchronized ShellProtoLogImpl getSingleInstance() {
        ShellProtoLogImpl shellProtoLogImpl;
        synchronized (ShellProtoLogImpl.class) {
            if (sServiceInstance == null) {
                sServiceInstance = new ShellProtoLogImpl();
            }
            shellProtoLogImpl = sServiceInstance;
        }
        return shellProtoLogImpl;
    }

    public int startTextLogging(String[] strArr, PrintWriter printWriter) {
        try {
            InputStream resourceAsStream = ShellProtoLogImpl.class.getClassLoader().getResourceAsStream("wm_shell_protolog.json");
            try {
                ((BaseProtoLogImpl) this).mViewerConfig.loadViewerConfig(resourceAsStream);
                int logging = setLogging(true, true, printWriter, strArr);
                if (resourceAsStream != null) {
                    resourceAsStream.close();
                }
                return logging;
            } catch (Throwable th) {
                if (resourceAsStream != null) {
                    try {
                        resourceAsStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (IOException e) {
            Log.i("ProtoLogImpl", "Unable to load log definitions: IOException while reading wm_shell_protolog. " + e);
            return -1;
        } catch (JSONException e2) {
            Log.i("ProtoLogImpl", "Unable to load log definitions: JSON parsing exception while reading wm_shell_protolog. " + e2);
            return -1;
        }
    }

    public int stopTextLogging(String[] strArr, PrintWriter printWriter) {
        return setLogging(true, false, printWriter, strArr);
    }

    private ShellProtoLogImpl() {
        super(new File(LOG_FILENAME), (String) null, 1048576, new ProtoLogViewerConfigReader());
    }
}
