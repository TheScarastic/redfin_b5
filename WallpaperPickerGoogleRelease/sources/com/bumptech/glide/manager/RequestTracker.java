package com.bumptech.glide.manager;

import com.adobe.xmp.XMPPathFactory$$ExternalSyntheticOutline0;
import com.bumptech.glide.request.Request;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class RequestTracker {
    public boolean isPaused;
    public final Set<Request> requests = Collections.newSetFromMap(new WeakHashMap());
    public final List<Request> pendingRequests = new ArrayList();

    public void addRequest(Request request) {
        this.requests.add(request);
    }

    public final boolean clearRemoveAndMaybeRecycle(Request request, boolean z) {
        boolean z2 = true;
        if (request == null) {
            return true;
        }
        boolean remove = this.requests.remove(request);
        if (!this.pendingRequests.remove(request) && !remove) {
            z2 = false;
        }
        if (z2) {
            request.clear();
            if (z) {
                request.recycle();
            }
        }
        return z2;
    }

    public String toString() {
        String obj = super.toString();
        int size = this.requests.size();
        boolean z = this.isPaused;
        StringBuilder sb = new StringBuilder(XMPPathFactory$$ExternalSyntheticOutline0.m(obj, 41));
        sb.append(obj);
        sb.append("{numRequests=");
        sb.append(size);
        sb.append(", isPaused=");
        sb.append(z);
        sb.append("}");
        return sb.toString();
    }
}
