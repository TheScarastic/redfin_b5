package com.android.systemui.screenshot;

import android.graphics.Bitmap;
import android.graphics.ColorSpace;
import android.graphics.Paint;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.RenderNode;
import android.media.Image;
import java.util.Objects;
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ImageTile implements AutoCloseable {
    private static final ColorSpace COLOR_SPACE = ColorSpace.get(ColorSpace.Named.SRGB);
    private final Image mImage;
    private final Rect mLocation;
    private RenderNode mNode;

    /* access modifiers changed from: package-private */
    public ImageTile(Image image, Rect rect) {
        Objects.requireNonNull(image, "image");
        Image image2 = image;
        this.mImage = image2;
        Objects.requireNonNull(rect);
        this.mLocation = rect;
        Objects.requireNonNull(image2.getHardwareBuffer(), "image must be a hardware image");
    }

    /* access modifiers changed from: package-private */
    public synchronized RenderNode getDisplayList() {
        if (this.mNode == null) {
            this.mNode = new RenderNode("Tile{" + Integer.toHexString(this.mImage.hashCode()) + "}");
        }
        if (this.mNode.hasDisplayList()) {
            return this.mNode;
        }
        int min = Math.min(this.mImage.getWidth(), this.mLocation.width());
        int min2 = Math.min(this.mImage.getHeight(), this.mLocation.height());
        this.mNode.setPosition(0, 0, min, min2);
        RecordingCanvas beginRecording = this.mNode.beginRecording(min, min2);
        beginRecording.save();
        beginRecording.clipRect(0, 0, this.mLocation.width(), this.mLocation.height());
        beginRecording.drawBitmap(Bitmap.wrapHardwareBuffer(this.mImage.getHardwareBuffer(), COLOR_SPACE), 0.0f, 0.0f, (Paint) null);
        beginRecording.restore();
        this.mNode.endRecording();
        return this.mNode;
    }

    /* access modifiers changed from: package-private */
    public Rect getLocation() {
        return this.mLocation;
    }

    /* access modifiers changed from: package-private */
    public int getLeft() {
        return this.mLocation.left;
    }

    /* access modifiers changed from: package-private */
    public int getTop() {
        return this.mLocation.top;
    }

    @Override // java.lang.AutoCloseable
    public synchronized void close() {
        this.mImage.close();
        RenderNode renderNode = this.mNode;
        if (renderNode != null) {
            renderNode.discardDisplayList();
        }
    }

    @Override // java.lang.Object
    public String toString() {
        return "{location=" + this.mLocation + ", source=" + this.mImage + ", buffer=" + this.mImage.getHardwareBuffer() + "}";
    }
}
