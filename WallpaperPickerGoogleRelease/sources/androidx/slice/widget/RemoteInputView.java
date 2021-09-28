package androidx.slice.widget;

import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.slice.SliceItem;
import com.android.systemui.shared.R;
/* loaded from: classes.dex */
public class RemoteInputView extends LinearLayout implements View.OnClickListener, TextWatcher {
    public static final Object VIEW_TAG = new Object();
    public SliceItem mAction;
    public RemoteEditText mEditText;
    public ProgressBar mProgressBar;
    public RemoteInput mRemoteInput;
    public RemoteInput[] mRemoteInputs;
    public boolean mResetting;
    public ImageButton mSendButton;

    /* loaded from: classes.dex */
    public static class RemoteEditText extends EditText {
        public final Drawable mBackground = getBackground();
        public RemoteInputView mRemoteInputView;
        public boolean mShowImeOnInputConnection;

        public RemoteEditText(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public final void defocusIfNeeded() {
            if (this.mRemoteInputView != null || isTemporarilyDetached()) {
                isTemporarilyDetached();
            } else if (isFocusable() && isEnabled()) {
                setInnerFocusable(false);
                RemoteInputView remoteInputView = this.mRemoteInputView;
                if (remoteInputView != null) {
                    remoteInputView.setVisibility(4);
                }
                this.mShowImeOnInputConnection = false;
            }
        }

        @Override // android.widget.TextView, android.view.View
        public void getFocusedRect(Rect rect) {
            super.getFocusedRect(rect);
            rect.top = getScrollY();
            rect.bottom = (getBottom() - getTop()) + getScrollY();
        }

        @Override // android.widget.TextView
        public void onCommitCompletion(CompletionInfo completionInfo) {
            clearComposingText();
            setText(completionInfo.getText());
            setSelection(getText().length());
        }

        @Override // android.widget.TextView, android.view.View
        public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
            InputConnection onCreateInputConnection = super.onCreateInputConnection(editorInfo);
            if (this.mShowImeOnInputConnection && onCreateInputConnection != null) {
                Context context = getContext();
                Object obj = ContextCompat.sLock;
                final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(InputMethodManager.class);
                if (inputMethodManager != null) {
                    post(new Runnable() { // from class: androidx.slice.widget.RemoteInputView.RemoteEditText.1
                        @Override // java.lang.Runnable
                        public void run() {
                            inputMethodManager.viewClicked(RemoteEditText.this);
                            inputMethodManager.showSoftInput(RemoteEditText.this, 0);
                        }
                    });
                }
            }
            return onCreateInputConnection;
        }

        @Override // android.widget.TextView, android.view.View
        public void onFocusChanged(boolean z, int i, Rect rect) {
            super.onFocusChanged(z, i, rect);
            if (!z) {
                defocusIfNeeded();
            }
        }

        @Override // android.widget.TextView, android.view.KeyEvent.Callback, android.view.View
        public boolean onKeyDown(int i, KeyEvent keyEvent) {
            if (i == 4) {
                return true;
            }
            return super.onKeyDown(i, keyEvent);
        }

        @Override // android.widget.TextView, android.view.KeyEvent.Callback, android.view.View
        public boolean onKeyUp(int i, KeyEvent keyEvent) {
            if (i != 4) {
                return super.onKeyUp(i, keyEvent);
            }
            defocusIfNeeded();
            return true;
        }

        @Override // android.widget.TextView, android.view.View
        public void onVisibilityChanged(View view, int i) {
            super.onVisibilityChanged(view, i);
            if (!isShown()) {
                defocusIfNeeded();
            }
        }

        @Override // android.widget.TextView
        public void setCustomSelectionActionModeCallback(ActionMode.Callback callback) {
            super.setCustomSelectionActionModeCallback(callback);
        }

        public void setInnerFocusable(boolean z) {
            setFocusableInTouchMode(z);
            setFocusable(z);
            setCursorVisible(z);
            if (z) {
                requestFocus();
                setBackground(this.mBackground);
                return;
            }
            setBackground(null);
        }
    }

    public RemoteInputView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        updateSendButton();
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // android.view.View, android.view.ViewGroup
    public void dispatchFinishTemporaryDetach() {
        if (isAttachedToWindow()) {
            RemoteEditText remoteEditText = this.mEditText;
            attachViewToParent(remoteEditText, 0, remoteEditText.getLayoutParams());
        } else {
            removeDetachedView(this.mEditText, false);
        }
        super.dispatchFinishTemporaryDetach();
    }

    @Override // android.view.View, android.view.ViewGroup
    public void dispatchStartTemporaryDetach() {
        super.dispatchStartTemporaryDetach();
        detachViewFromParent(this.mEditText);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view == this.mSendButton) {
            sendRemoteInput();
        }
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mProgressBar = (ProgressBar) findViewById(R.id.remote_input_progress);
        ImageButton imageButton = (ImageButton) findViewById(R.id.remote_input_send);
        this.mSendButton = imageButton;
        imageButton.setOnClickListener(this);
        RemoteEditText remoteEditText = (RemoteEditText) getChildAt(0);
        this.mEditText = remoteEditText;
        remoteEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: androidx.slice.widget.RemoteInputView.1
            /* JADX WARNING: Removed duplicated region for block: B:33:0x0046  */
            @Override // android.widget.TextView.OnEditorActionListener
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public boolean onEditorAction(android.widget.TextView r4, int r5, android.view.KeyEvent r6) {
                /*
                    r3 = this;
                    r4 = 1
                    r0 = 0
                    if (r6 != 0) goto L_0x000f
                    r1 = 6
                    if (r5 == r1) goto L_0x000d
                    r1 = 5
                    if (r5 == r1) goto L_0x000d
                    r1 = 4
                    if (r5 != r1) goto L_0x000f
                L_0x000d:
                    r5 = r4
                    goto L_0x0010
                L_0x000f:
                    r5 = r0
                L_0x0010:
                    if (r6 == 0) goto L_0x0035
                    int r1 = r6.getKeyCode()
                    java.lang.Object r2 = androidx.slice.widget.RemoteInputView.VIEW_TAG
                    r2 = 23
                    if (r1 == r2) goto L_0x002a
                    r2 = 62
                    if (r1 == r2) goto L_0x002a
                    r2 = 66
                    if (r1 == r2) goto L_0x002a
                    r2 = 160(0xa0, float:2.24E-43)
                    if (r1 == r2) goto L_0x002a
                    r1 = r0
                    goto L_0x002b
                L_0x002a:
                    r1 = r4
                L_0x002b:
                    if (r1 == 0) goto L_0x0035
                    int r6 = r6.getAction()
                    if (r6 != 0) goto L_0x0035
                    r6 = r4
                    goto L_0x0036
                L_0x0035:
                    r6 = r0
                L_0x0036:
                    if (r5 != 0) goto L_0x003c
                    if (r6 == 0) goto L_0x003b
                    goto L_0x003c
                L_0x003b:
                    return r0
                L_0x003c:
                    androidx.slice.widget.RemoteInputView r5 = androidx.slice.widget.RemoteInputView.this
                    androidx.slice.widget.RemoteInputView$RemoteEditText r5 = r5.mEditText
                    int r5 = r5.length()
                    if (r5 <= 0) goto L_0x004b
                    androidx.slice.widget.RemoteInputView r3 = androidx.slice.widget.RemoteInputView.this
                    r3.sendRemoteInput()
                L_0x004b:
                    return r4
                */
                throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.RemoteInputView.AnonymousClass1.onEditorAction(android.widget.TextView, int, android.view.KeyEvent):boolean");
            }
        });
        this.mEditText.addTextChangedListener(this);
        this.mEditText.setInnerFocusable(false);
        this.mEditText.mRemoteInputView = this;
    }

    @Override // android.view.ViewGroup
    public boolean onRequestSendAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        if (!this.mResetting || view != this.mEditText) {
            return super.onRequestSendAccessibilityEvent(view, accessibilityEvent);
        }
        return false;
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        return true;
    }

    public final void reset() {
        this.mResetting = true;
        this.mEditText.getText().clear();
        this.mEditText.setEnabled(true);
        this.mSendButton.setVisibility(0);
        this.mProgressBar.setVisibility(4);
        updateSendButton();
        setVisibility(4);
        this.mResetting = false;
    }

    public void sendRemoteInput() {
        Bundle bundle = new Bundle();
        bundle.putString(this.mRemoteInput.getResultKey(), this.mEditText.getText().toString());
        Intent addFlags = new Intent().addFlags(268435456);
        RemoteInput.addResultsToIntent(this.mRemoteInputs, addFlags, bundle);
        this.mEditText.setEnabled(false);
        this.mSendButton.setVisibility(4);
        this.mProgressBar.setVisibility(0);
        this.mEditText.mShowImeOnInputConnection = false;
        try {
            this.mAction.fireActionInternal(getContext(), addFlags);
            reset();
        } catch (PendingIntent.CanceledException e) {
            Log.i("RemoteInput", "Unable to send remote input result", e);
            Toast.makeText(getContext(), "Failure sending pending intent for inline reply :(", 0).show();
            reset();
        }
    }

    public final void updateSendButton() {
        this.mSendButton.setEnabled(this.mEditText.getText().length() != 0);
    }
}
