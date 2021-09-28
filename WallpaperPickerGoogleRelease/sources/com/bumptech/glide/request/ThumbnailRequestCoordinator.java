package com.bumptech.glide.request;
/* loaded from: classes.dex */
public class ThumbnailRequestCoordinator implements RequestCoordinator, Request {
    public Request full;
    public boolean isRunning;
    public final RequestCoordinator parent;
    public Request thumb;

    public ThumbnailRequestCoordinator() {
        this.parent = null;
    }

    @Override // com.bumptech.glide.request.Request
    public void begin() {
        this.isRunning = true;
        if (!this.full.isComplete() && !this.thumb.isRunning()) {
            this.thumb.begin();
        }
        if (this.isRunning && !this.full.isRunning()) {
            this.full.begin();
        }
    }

    @Override // com.bumptech.glide.request.RequestCoordinator
    public boolean canNotifyCleared(Request request) {
        RequestCoordinator requestCoordinator = this.parent;
        return (requestCoordinator == null || requestCoordinator.canNotifyCleared(this)) && request.equals(this.full);
    }

    @Override // com.bumptech.glide.request.RequestCoordinator
    public boolean canNotifyStatusChanged(Request request) {
        RequestCoordinator requestCoordinator = this.parent;
        return (requestCoordinator == null || requestCoordinator.canNotifyStatusChanged(this)) && request.equals(this.full) && !isAnyResourceSet();
    }

    @Override // com.bumptech.glide.request.RequestCoordinator
    public boolean canSetImage(Request request) {
        RequestCoordinator requestCoordinator = this.parent;
        if (requestCoordinator == null || requestCoordinator.canSetImage(this)) {
            return request.equals(this.full) || !this.full.isResourceSet();
        }
        return false;
    }

    @Override // com.bumptech.glide.request.Request
    public void clear() {
        this.isRunning = false;
        this.thumb.clear();
        this.full.clear();
    }

    @Override // com.bumptech.glide.request.RequestCoordinator
    public boolean isAnyResourceSet() {
        RequestCoordinator requestCoordinator = this.parent;
        return (requestCoordinator != null && requestCoordinator.isAnyResourceSet()) || isResourceSet();
    }

    @Override // com.bumptech.glide.request.Request
    public boolean isCleared() {
        return this.full.isCleared();
    }

    @Override // com.bumptech.glide.request.Request
    public boolean isComplete() {
        return this.full.isComplete() || this.thumb.isComplete();
    }

    @Override // com.bumptech.glide.request.Request
    public boolean isEquivalentTo(Request request) {
        if (!(request instanceof ThumbnailRequestCoordinator)) {
            return false;
        }
        ThumbnailRequestCoordinator thumbnailRequestCoordinator = (ThumbnailRequestCoordinator) request;
        Request request2 = this.full;
        if (request2 == null) {
            if (thumbnailRequestCoordinator.full != null) {
                return false;
            }
        } else if (!request2.isEquivalentTo(thumbnailRequestCoordinator.full)) {
            return false;
        }
        Request request3 = this.thumb;
        if (request3 == null) {
            if (thumbnailRequestCoordinator.thumb != null) {
                return false;
            }
        } else if (!request3.isEquivalentTo(thumbnailRequestCoordinator.thumb)) {
            return false;
        }
        return true;
    }

    @Override // com.bumptech.glide.request.Request
    public boolean isResourceSet() {
        return this.full.isResourceSet() || this.thumb.isResourceSet();
    }

    @Override // com.bumptech.glide.request.Request
    public boolean isRunning() {
        return this.full.isRunning();
    }

    @Override // com.bumptech.glide.request.RequestCoordinator
    public void onRequestFailed(Request request) {
        RequestCoordinator requestCoordinator;
        if (request.equals(this.full) && (requestCoordinator = this.parent) != null) {
            requestCoordinator.onRequestFailed(this);
        }
    }

    @Override // com.bumptech.glide.request.RequestCoordinator
    public void onRequestSuccess(Request request) {
        if (!request.equals(this.thumb)) {
            RequestCoordinator requestCoordinator = this.parent;
            if (requestCoordinator != null) {
                requestCoordinator.onRequestSuccess(this);
            }
            if (!this.thumb.isComplete()) {
                this.thumb.clear();
            }
        }
    }

    @Override // com.bumptech.glide.request.Request
    public void recycle() {
        this.full.recycle();
        this.thumb.recycle();
    }

    public ThumbnailRequestCoordinator(RequestCoordinator requestCoordinator) {
        this.parent = requestCoordinator;
    }
}
