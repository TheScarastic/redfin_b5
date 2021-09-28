package com.android.volley;

import android.os.Handler;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class ExecutorDelivery implements ResponseDelivery {
    public final Executor mResponsePoster;

    /* loaded from: classes.dex */
    public static class ResponseDeliveryRunnable implements Runnable {
        public final Request mRequest;
        public final Response mResponse;
        public final Runnable mRunnable;

        public ResponseDeliveryRunnable(Request request, Response response, Runnable runnable) {
            this.mRequest = request;
            this.mResponse = response;
            this.mRunnable = runnable;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.mRequest.isCanceled();
            Response response = this.mResponse;
            VolleyError volleyError = response.error;
            if (volleyError == null) {
                this.mRequest.deliverResponse(response.result);
            } else {
                this.mRequest.deliverError(volleyError);
            }
            if (this.mResponse.intermediate) {
                this.mRequest.addMarker("intermediate-response");
            } else {
                this.mRequest.finish("done");
            }
            Runnable runnable = this.mRunnable;
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    public ExecutorDelivery(final Handler handler) {
        this.mResponsePoster = new Executor(this) { // from class: com.android.volley.ExecutorDelivery.1
            @Override // java.util.concurrent.Executor
            public void execute(Runnable runnable) {
                handler.post(runnable);
            }
        };
    }

    public void postResponse(Request<?> request, Response<?> response, Runnable runnable) {
        synchronized (request.mLock) {
            request.mResponseDelivered = true;
        }
        request.addMarker("post-response");
        this.mResponsePoster.execute(new ResponseDeliveryRunnable(request, response, runnable));
    }
}
