package androidx.leanback.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.leanback.R$id;
/* loaded from: classes.dex */
public class PlaybackTransportRowView extends LinearLayout {
    private OnUnhandledKeyListener mOnUnhandledKeyListener;

    /* loaded from: classes.dex */
    public interface OnUnhandledKeyListener {
        boolean onUnhandledKey(KeyEvent keyEvent);
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public PlaybackTransportRowView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PlaybackTransportRowView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.view.View, android.view.ViewGroup
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (super.dispatchKeyEvent(keyEvent)) {
            return true;
        }
        OnUnhandledKeyListener onUnhandledKeyListener = this.mOnUnhandledKeyListener;
        if (onUnhandledKeyListener == null || !onUnhandledKeyListener.onUnhandledKey(keyEvent)) {
            return false;
        }
        return true;
    }

    @Override // android.view.ViewGroup
    protected boolean onRequestFocusInDescendants(int i, Rect rect) {
        View findFocus = findFocus();
        if (findFocus != null && findFocus.requestFocus(i, rect)) {
            return true;
        }
        View findViewById = findViewById(R$id.playback_progress);
        if (findViewById == null || !findViewById.isFocusable() || !findViewById.requestFocus(i, rect)) {
            return super.onRequestFocusInDescendants(i, rect);
        }
        return true;
    }

    @Override // android.view.ViewParent, android.view.ViewGroup
    public View focusSearch(View view, int i) {
        View childAt;
        if (view != null) {
            if (i == 33) {
                for (int indexOfChild = indexOfChild(getFocusedChild()) - 1; indexOfChild >= 0; indexOfChild--) {
                    View childAt2 = getChildAt(indexOfChild);
                    if (childAt2.hasFocusable()) {
                        return childAt2;
                    }
                }
            } else if (i == 130) {
                int indexOfChild2 = indexOfChild(getFocusedChild());
                do {
                    indexOfChild2++;
                    if (indexOfChild2 < getChildCount()) {
                        childAt = getChildAt(indexOfChild2);
                    }
                } while (!childAt.hasFocusable());
                return childAt;
            } else if ((i == 17 || i == 66) && (getFocusedChild() instanceof ViewGroup)) {
                return FocusFinder.getInstance().findNextFocus((ViewGroup) getFocusedChild(), view, i);
            }
        }
        return super.focusSearch(view, i);
    }
}
