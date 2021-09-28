package com.android.systemui.statusbar.policy;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutManager;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.BlendMode;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.UserHandle;
import android.text.Editable;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.ContentInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OnReceiveContentListener;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowInsetsAnimation;
import android.view.accessibility.AccessibilityEvent;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.internal.graphics.ColorUtils;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.util.ContrastColorUtil;
import com.android.systemui.Dependency;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.wm.shell.animation.Interpolators;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class RemoteInputView extends LinearLayout implements View.OnClickListener {
    public static final Object VIEW_TAG = new Object();
    private NotificationRemoteInputManager.BouncerChecker mBouncerChecker;
    private boolean mColorized;
    private GradientDrawable mContentBackground;
    private RemoteInputController mController;
    private ImageView mDelete;
    private ImageView mDeleteBg;
    private RemoteEditText mEditText;
    private NotificationEntry mEntry;
    private Consumer<Boolean> mOnVisibilityChangedListener;
    private PendingIntent mPendingIntent;
    private ProgressBar mProgressBar;
    private RemoteInput mRemoteInput;
    private RemoteInput[] mRemoteInputs;
    private boolean mRemoved;
    private boolean mResetting;
    private int mRevealCx;
    private int mRevealCy;
    private int mRevealR;
    private ImageButton mSendButton;
    private int mTint;
    private NotificationViewWrapper mWrapper;
    public final Object mToken = new Object();
    private final List<View.OnFocusChangeListener> mEditTextFocusChangeListeners = new ArrayList();
    private final List<OnSendRemoteInputListener> mOnSendListeners = new ArrayList();
    private final SendButtonTextWatcher mTextWatcher = new SendButtonTextWatcher();
    private final TextView.OnEditorActionListener mEditorActionHandler = new EditorActionHandler();
    private final RemoteInputQuickSettingsDisabler mRemoteInputQuickSettingsDisabler = (RemoteInputQuickSettingsDisabler) Dependency.get(RemoteInputQuickSettingsDisabler.class);
    private final UiEventLogger mUiEventLogger = (UiEventLogger) Dependency.get(UiEventLogger.class);

    /* loaded from: classes.dex */
    public interface OnSendRemoteInputListener {
        void onSendRemoteInput();

        void onSendRequestBounced();
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum NotificationRemoteInputEvent implements UiEventLogger.UiEventEnum {
        NOTIFICATION_REMOTE_INPUT_OPEN(795),
        NOTIFICATION_REMOTE_INPUT_CLOSE(796),
        NOTIFICATION_REMOTE_INPUT_SEND(797),
        NOTIFICATION_REMOTE_INPUT_FAILURE(798);
        
        private final int mId;

        NotificationRemoteInputEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    public RemoteInputView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(new int[]{16843829, 17956909});
        this.mTint = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
    }

    private ColorStateList colorStateListWithDisabledAlpha(int i, int i2) {
        return new ColorStateList(new int[][]{new int[]{-16842910}, new int[0]}, new int[]{ColorUtils.setAlphaComponent(i, i2), i});
    }

    public void setBackgroundTintColor(int i, boolean z) {
        ColorStateList colorStateList;
        int i2;
        ColorStateList colorStateList2;
        int i3;
        int i4;
        int i5;
        if (z != this.mColorized || i != this.mTint) {
            this.mColorized = z;
            this.mTint = i;
            int dimensionPixelSize = z ? ((LinearLayout) this).mContext.getResources().getDimensionPixelSize(R$dimen.remote_input_view_text_stroke) : 0;
            if (z) {
                boolean z2 = !ContrastColorUtil.isColorLight(i);
                int i6 = -1;
                i3 = z2 ? -1 : -16777216;
                if (z2) {
                    i6 = -16777216;
                }
                colorStateList = colorStateListWithDisabledAlpha(i3, 77);
                colorStateList2 = colorStateListWithDisabledAlpha(i3, 153);
                i4 = ColorUtils.setAlphaComponent(i3, 153);
                i2 = i6;
                i5 = i;
            } else {
                colorStateList = ((LinearLayout) this).mContext.getColorStateList(R$color.remote_input_send);
                colorStateList2 = ((LinearLayout) this).mContext.getColorStateList(R$color.remote_input_text);
                int color = ((LinearLayout) this).mContext.getColor(R$color.remote_input_hint);
                i2 = colorStateList2.getDefaultColor();
                TypedArray obtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(new int[]{17956911, 17956912});
                try {
                    i5 = obtainStyledAttributes.getColor(0, i);
                    int color2 = obtainStyledAttributes.getColor(1, -7829368);
                    obtainStyledAttributes.close();
                    i3 = color2;
                    i4 = color;
                } catch (Throwable th) {
                    if (obtainStyledAttributes != null) {
                        try {
                            obtainStyledAttributes.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            }
            this.mEditText.setTextColor(colorStateList2);
            this.mEditText.setHintTextColor(i4);
            this.mEditText.getTextCursorDrawable().setColorFilter(colorStateList.getDefaultColor(), PorterDuff.Mode.SRC_IN);
            this.mContentBackground.setColor(i5);
            this.mContentBackground.setStroke(dimensionPixelSize, colorStateList);
            this.mDelete.setImageTintList(ColorStateList.valueOf(i2));
            this.mDeleteBg.setImageTintList(ColorStateList.valueOf(i3));
            this.mSendButton.setImageTintList(colorStateList);
            this.mProgressBar.setProgressTintList(colorStateList);
            this.mProgressBar.setIndeterminateTintList(colorStateList);
            this.mProgressBar.setSecondaryProgressTintList(colorStateList);
            setBackgroundColor(i);
        }
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mProgressBar = (ProgressBar) findViewById(R$id.remote_input_progress);
        ImageButton imageButton = (ImageButton) findViewById(R$id.remote_input_send);
        this.mSendButton = imageButton;
        imageButton.setOnClickListener(this);
        this.mContentBackground = (GradientDrawable) ((LinearLayout) this).mContext.getDrawable(R$drawable.remote_input_view_text_bg).mutate();
        this.mDelete = (ImageView) findViewById(R$id.remote_input_delete);
        ImageView imageView = (ImageView) findViewById(R$id.remote_input_delete_bg);
        this.mDeleteBg = imageView;
        imageView.setImageTintBlendMode(BlendMode.SRC_IN);
        this.mDelete.setImageTintBlendMode(BlendMode.SRC_IN);
        this.mDelete.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.statusbar.policy.RemoteInputView$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                RemoteInputView.this.lambda$onFinishInflate$0(view);
            }
        });
        ((LinearLayout) findViewById(R$id.remote_input_content)).setBackground(this.mContentBackground);
        RemoteEditText remoteEditText = (RemoteEditText) findViewById(R$id.remote_input_text);
        this.mEditText = remoteEditText;
        remoteEditText.setInnerFocusable(false);
        this.mEditText.setWindowInsetsAnimationCallback(new WindowInsetsAnimation.Callback(0) { // from class: com.android.systemui.statusbar.policy.RemoteInputView.1
            @Override // android.view.WindowInsetsAnimation.Callback
            public WindowInsets onProgress(WindowInsets windowInsets, List<WindowInsetsAnimation> list) {
                return windowInsets;
            }

            @Override // android.view.WindowInsetsAnimation.Callback
            public void onEnd(WindowInsetsAnimation windowInsetsAnimation) {
                super.onEnd(windowInsetsAnimation);
                if (windowInsetsAnimation.getTypeMask() == WindowInsets.Type.ime()) {
                    RemoteInputView.this.mEntry.mRemoteEditImeAnimatingAway = false;
                    RemoteInputView.this.mEntry.mRemoteEditImeVisible = RemoteInputView.this.mEditText.getRootWindowInsets().isVisible(WindowInsets.Type.ime());
                    if (!RemoteInputView.this.mEntry.mRemoteEditImeVisible && !RemoteInputView.this.mEditText.mShowImeOnInputConnection) {
                        RemoteInputView.this.mController.removeRemoteInput(RemoteInputView.this.mEntry, RemoteInputView.this.mToken);
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishInflate$0(View view) {
        setAttachment(null);
    }

    /* access modifiers changed from: private */
    public void setAttachment(ContentInfo contentInfo) {
        ContentInfo contentInfo2 = this.mEntry.remoteInputAttachment;
        if (!(contentInfo2 == null || contentInfo2 == contentInfo)) {
            contentInfo2.releasePermissions();
        }
        NotificationEntry notificationEntry = this.mEntry;
        notificationEntry.remoteInputAttachment = contentInfo;
        if (contentInfo != null) {
            notificationEntry.remoteInputUri = contentInfo.getClip().getItemAt(0).getUri();
            this.mEntry.remoteInputMimeType = contentInfo.getClip().getDescription().getMimeType(0);
        }
        View findViewById = findViewById(R$id.remote_input_content_container);
        ImageView imageView = (ImageView) findViewById(R$id.remote_input_attachment_image);
        imageView.setImageDrawable(null);
        if (contentInfo == null) {
            findViewById.setVisibility(8);
            return;
        }
        imageView.setImageURI(contentInfo.getClip().getItemAt(0).getUri());
        if (imageView.getDrawable() == null) {
            findViewById.setVisibility(8);
        } else {
            findViewById.setVisibility(0);
        }
        updateSendButton();
    }

    /* access modifiers changed from: private */
    public Intent prepareRemoteInput() {
        NotificationEntry notificationEntry = this.mEntry;
        if (notificationEntry.remoteInputAttachment == null) {
            return prepareRemoteInputFromText();
        }
        return prepareRemoteInputFromData(notificationEntry.remoteInputMimeType, notificationEntry.remoteInputUri);
    }

    private Intent prepareRemoteInputFromText() {
        Bundle bundle = new Bundle();
        bundle.putString(this.mRemoteInput.getResultKey(), this.mEditText.getText().toString());
        Intent addFlags = new Intent().addFlags(268435456);
        RemoteInput.addResultsToIntent(this.mRemoteInputs, addFlags, bundle);
        this.mEntry.remoteInputText = this.mEditText.getText().toString();
        setAttachment(null);
        NotificationEntry notificationEntry = this.mEntry;
        notificationEntry.remoteInputUri = null;
        notificationEntry.remoteInputMimeType = null;
        if (notificationEntry.editedSuggestionInfo == null) {
            RemoteInput.setResultsSource(addFlags, 0);
        } else {
            RemoteInput.setResultsSource(addFlags, 1);
        }
        return addFlags;
    }

    private Intent prepareRemoteInputFromData(String str, Uri uri) {
        HashMap hashMap = new HashMap();
        hashMap.put(str, uri);
        this.mController.grantInlineReplyUriPermission(this.mEntry.getSbn(), uri);
        Intent addFlags = new Intent().addFlags(268435456);
        RemoteInput.addDataResultToIntent(this.mRemoteInput, addFlags, hashMap);
        Bundle bundle = new Bundle();
        bundle.putString(this.mRemoteInput.getResultKey(), this.mEditText.getText().toString());
        RemoteInput.addResultsToIntent(this.mRemoteInputs, addFlags, bundle);
        String label = this.mEntry.remoteInputAttachment.getClip().getDescription().getLabel();
        if (TextUtils.isEmpty(label)) {
            label = ((LinearLayout) this).mContext.getString(R$string.remote_input_image_insertion_text);
        }
        if (!TextUtils.isEmpty(this.mEditText.getText())) {
            label = "\"" + ((Object) label) + "\" " + ((Object) this.mEditText.getText());
        }
        NotificationEntry notificationEntry = this.mEntry;
        notificationEntry.remoteInputText = label;
        if (notificationEntry.editedSuggestionInfo == null) {
            RemoteInput.setResultsSource(addFlags, 0);
        } else if (notificationEntry.remoteInputAttachment == null) {
            RemoteInput.setResultsSource(addFlags, 1);
        }
        return addFlags;
    }

    /* access modifiers changed from: private */
    public void sendRemoteInput(Intent intent) {
        NotificationRemoteInputManager.BouncerChecker bouncerChecker = this.mBouncerChecker;
        if (bouncerChecker == null || !bouncerChecker.showBouncerIfNecessary()) {
            this.mEditText.setEnabled(false);
            this.mSendButton.setVisibility(4);
            this.mProgressBar.setVisibility(0);
            this.mEntry.lastRemoteInputSent = SystemClock.elapsedRealtime();
            NotificationEntry notificationEntry = this.mEntry;
            notificationEntry.mRemoteEditImeAnimatingAway = true;
            this.mController.addSpinning(notificationEntry.getKey(), this.mToken);
            this.mController.removeRemoteInput(this.mEntry, this.mToken);
            this.mEditText.mShowImeOnInputConnection = false;
            this.mController.remoteInputSent(this.mEntry);
            this.mEntry.setHasSentReply();
            for (OnSendRemoteInputListener onSendRemoteInputListener : this.mOnSendListeners) {
                onSendRemoteInputListener.onSendRemoteInput();
            }
            ((ShortcutManager) getContext().getSystemService(ShortcutManager.class)).onApplicationActive(this.mEntry.getSbn().getPackageName(), this.mEntry.getSbn().getUser().getIdentifier());
            MetricsLogger.action(((LinearLayout) this).mContext, 398, this.mEntry.getSbn().getPackageName());
            this.mUiEventLogger.logWithInstanceId(NotificationRemoteInputEvent.NOTIFICATION_REMOTE_INPUT_SEND, this.mEntry.getSbn().getUid(), this.mEntry.getSbn().getPackageName(), this.mEntry.getSbn().getInstanceId());
            try {
                this.mPendingIntent.send(((LinearLayout) this).mContext, 0, intent);
            } catch (PendingIntent.CanceledException e) {
                Log.i("RemoteInput", "Unable to send remote input result", e);
                MetricsLogger.action(((LinearLayout) this).mContext, 399, this.mEntry.getSbn().getPackageName());
                this.mUiEventLogger.logWithInstanceId(NotificationRemoteInputEvent.NOTIFICATION_REMOTE_INPUT_FAILURE, this.mEntry.getSbn().getUid(), this.mEntry.getSbn().getPackageName(), this.mEntry.getSbn().getInstanceId());
            }
            setAttachment(null);
            return;
        }
        this.mEditText.hideIme();
        for (OnSendRemoteInputListener onSendRemoteInputListener2 : this.mOnSendListeners) {
            onSendRemoteInputListener2.onSendRequestBounced();
        }
    }

    public CharSequence getText() {
        return this.mEditText.getText();
    }

    public static RemoteInputView inflate(Context context, ViewGroup viewGroup, NotificationEntry notificationEntry, RemoteInputController remoteInputController) {
        RemoteInputView remoteInputView = (RemoteInputView) LayoutInflater.from(context).inflate(R$layout.remote_input, viewGroup, false);
        remoteInputView.mController = remoteInputController;
        remoteInputView.mEntry = notificationEntry;
        UserHandle computeTextOperationUser = computeTextOperationUser(notificationEntry.getSbn().getUser());
        RemoteEditText remoteEditText = remoteInputView.mEditText;
        remoteEditText.mUser = computeTextOperationUser;
        remoteEditText.setTextOperationUser(computeTextOperationUser);
        remoteInputView.setTag(VIEW_TAG);
        return remoteInputView;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view == this.mSendButton) {
            sendRemoteInput(prepareRemoteInput());
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        return true;
    }

    /* access modifiers changed from: private */
    public void onDefocus(boolean z, boolean z2) {
        int i;
        this.mController.removeRemoteInput(this.mEntry, this.mToken);
        this.mEntry.remoteInputText = this.mEditText.getText();
        if (!this.mRemoved) {
            if (!z || (i = this.mRevealR) <= 0) {
                setVisibility(8);
                NotificationViewWrapper notificationViewWrapper = this.mWrapper;
                if (notificationViewWrapper != null) {
                    notificationViewWrapper.setRemoteInputVisible(false);
                }
            } else {
                Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(this, this.mRevealCx, this.mRevealCy, (float) i, 0.0f);
                createCircularReveal.setInterpolator(Interpolators.FAST_OUT_LINEAR_IN);
                createCircularReveal.setDuration(150);
                createCircularReveal.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.policy.RemoteInputView.2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        RemoteInputView.this.setVisibility(8);
                        if (RemoteInputView.this.mWrapper != null) {
                            RemoteInputView.this.mWrapper.setRemoteInputVisible(false);
                        }
                    }
                });
                createCircularReveal.start();
            }
        }
        this.mRemoteInputQuickSettingsDisabler.setRemoteInputActive(false);
        if (z2) {
            MetricsLogger.action(((LinearLayout) this).mContext, 400, this.mEntry.getSbn().getPackageName());
            this.mUiEventLogger.logWithInstanceId(NotificationRemoteInputEvent.NOTIFICATION_REMOTE_INPUT_CLOSE, this.mEntry.getSbn().getUid(), this.mEntry.getSbn().getPackageName(), this.mEntry.getSbn().getInstanceId());
        }
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mEditText.mRemoteInputView = this;
        this.mEditText.setOnEditorActionListener(this.mEditorActionHandler);
        this.mEditText.addTextChangedListener(this.mTextWatcher);
        if (this.mEntry.getRow().isChangingPosition() && getVisibility() == 0 && this.mEditText.isFocusable()) {
            this.mEditText.requestFocus();
        }
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mEditText.removeTextChangedListener(this.mTextWatcher);
        this.mEditText.setOnEditorActionListener(null);
        this.mEditText.mRemoteInputView = null;
        if (!this.mEntry.getRow().isChangingPosition() && !isTemporarilyDetached()) {
            this.mController.removeRemoteInput(this.mEntry, this.mToken);
            this.mController.removeSpinning(this.mEntry.getKey(), this.mToken);
        }
    }

    public void setPendingIntent(PendingIntent pendingIntent) {
        this.mPendingIntent = pendingIntent;
    }

    public void setRemoteInput(RemoteInput[] remoteInputArr, RemoteInput remoteInput, NotificationEntry.EditedSuggestionInfo editedSuggestionInfo) {
        this.mRemoteInputs = remoteInputArr;
        this.mRemoteInput = remoteInput;
        this.mEditText.setHint(remoteInput.getLabel());
        this.mEditText.setSupportedMimeTypes(remoteInput.getAllowedDataTypes());
        NotificationEntry notificationEntry = this.mEntry;
        notificationEntry.editedSuggestionInfo = editedSuggestionInfo;
        if (editedSuggestionInfo != null) {
            notificationEntry.remoteInputText = editedSuggestionInfo.originalText;
            notificationEntry.remoteInputAttachment = null;
        }
    }

    public void setEditTextContent(CharSequence charSequence) {
        this.mEditText.setText(charSequence);
    }

    public void focusAnimated() {
        if (getVisibility() != 0) {
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(this, this.mRevealCx, this.mRevealCy, 0.0f, (float) this.mRevealR);
            createCircularReveal.setDuration(360);
            createCircularReveal.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
            createCircularReveal.start();
        }
        focus();
    }

    private static UserHandle computeTextOperationUser(UserHandle userHandle) {
        return UserHandle.ALL.equals(userHandle) ? UserHandle.of(ActivityManager.getCurrentUser()) : userHandle;
    }

    public void focus() {
        MetricsLogger.action(((LinearLayout) this).mContext, 397, this.mEntry.getSbn().getPackageName());
        this.mUiEventLogger.logWithInstanceId(NotificationRemoteInputEvent.NOTIFICATION_REMOTE_INPUT_OPEN, this.mEntry.getSbn().getUid(), this.mEntry.getSbn().getPackageName(), this.mEntry.getSbn().getInstanceId());
        setVisibility(0);
        NotificationViewWrapper notificationViewWrapper = this.mWrapper;
        if (notificationViewWrapper != null) {
            notificationViewWrapper.setRemoteInputVisible(true);
        }
        this.mEditText.setInnerFocusable(true);
        RemoteEditText remoteEditText = this.mEditText;
        remoteEditText.mShowImeOnInputConnection = true;
        remoteEditText.setText(this.mEntry.remoteInputText);
        RemoteEditText remoteEditText2 = this.mEditText;
        remoteEditText2.setSelection(remoteEditText2.length());
        this.mEditText.requestFocus();
        this.mController.addRemoteInput(this.mEntry, this.mToken);
        setAttachment(this.mEntry.remoteInputAttachment);
        this.mRemoteInputQuickSettingsDisabler.setRemoteInputActive(true);
        updateSendButton();
    }

    public void onNotificationUpdateOrReset() {
        NotificationViewWrapper notificationViewWrapper;
        if (this.mProgressBar.getVisibility() == 0) {
            reset();
        }
        if (isActive() && (notificationViewWrapper = this.mWrapper) != null) {
            notificationViewWrapper.setRemoteInputVisible(true);
        }
    }

    private void reset() {
        this.mResetting = true;
        this.mEntry.remoteInputTextWhenReset = SpannedString.valueOf(this.mEditText.getText());
        this.mEditText.getText().clear();
        this.mEditText.setEnabled(true);
        this.mSendButton.setVisibility(0);
        this.mProgressBar.setVisibility(4);
        this.mController.removeSpinning(this.mEntry.getKey(), this.mToken);
        updateSendButton();
        onDefocus(false, false);
        setAttachment(null);
        this.mResetting = false;
    }

    @Override // android.view.ViewGroup
    public boolean onRequestSendAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        if (!this.mResetting || view != this.mEditText) {
            return super.onRequestSendAccessibilityEvent(view, accessibilityEvent);
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void updateSendButton() {
        this.mSendButton.setEnabled((this.mEditText.length() == 0 && this.mEntry.remoteInputAttachment == null) ? false : true);
    }

    public void close() {
        this.mEditText.defocusIfNeeded(false);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.mController.requestDisallowLongPressAndDismiss();
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    public boolean requestScrollTo() {
        this.mController.lockScrollTo(this.mEntry);
        return true;
    }

    public boolean isActive() {
        return this.mEditText.isFocused() && this.mEditText.isEnabled();
    }

    public void stealFocusFrom(RemoteInputView remoteInputView) {
        remoteInputView.close();
        setPendingIntent(remoteInputView.mPendingIntent);
        setRemoteInput(remoteInputView.mRemoteInputs, remoteInputView.mRemoteInput, this.mEntry.editedSuggestionInfo);
        setRevealParameters(remoteInputView.mRevealCx, remoteInputView.mRevealCy, remoteInputView.mRevealR);
        focus();
    }

    public boolean updatePendingIntentFromActions(Notification.Action[] actionArr) {
        Intent intent;
        PendingIntent pendingIntent = this.mPendingIntent;
        if (pendingIntent == null || actionArr == null || (intent = pendingIntent.getIntent()) == null) {
            return false;
        }
        for (Notification.Action action : actionArr) {
            RemoteInput[] remoteInputs = action.getRemoteInputs();
            PendingIntent pendingIntent2 = action.actionIntent;
            if (!(pendingIntent2 == null || remoteInputs == null || !intent.filterEquals(pendingIntent2.getIntent()))) {
                RemoteInput remoteInput = null;
                for (RemoteInput remoteInput2 : remoteInputs) {
                    if (remoteInput2.getAllowFreeFormInput()) {
                        remoteInput = remoteInput2;
                    }
                }
                if (remoteInput != null) {
                    setPendingIntent(action.actionIntent);
                    setRemoteInput(remoteInputs, remoteInput, null);
                    return true;
                }
            }
        }
        return false;
    }

    public PendingIntent getPendingIntent() {
        return this.mPendingIntent;
    }

    public void setRemoved() {
        this.mRemoved = true;
    }

    public void setRevealParameters(int i, int i2, int i3) {
        this.mRevealCx = i;
        this.mRevealCy = i2;
        this.mRevealR = i3;
    }

    @Override // android.view.View, android.view.ViewGroup
    public void dispatchStartTemporaryDetach() {
        super.dispatchStartTemporaryDetach();
        int indexOfChild = indexOfChild(this.mEditText);
        if (indexOfChild != -1) {
            detachViewFromParent(indexOfChild);
        }
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

    public void setWrapper(NotificationViewWrapper notificationViewWrapper) {
        this.mWrapper = notificationViewWrapper;
    }

    public void setOnVisibilityChangedListener(Consumer<Boolean> consumer) {
        this.mOnVisibilityChangedListener = consumer;
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View view, int i) {
        Consumer<Boolean> consumer;
        super.onVisibilityChanged(view, i);
        if (view == this && (consumer = this.mOnVisibilityChangedListener) != null) {
            consumer.accept(Boolean.valueOf(i == 0));
            if (i != 0 && !this.mEditText.isVisibleToUser() && !this.mController.isRemoteInputActive()) {
                this.mEditText.hideIme();
            }
        }
    }

    public boolean isSending() {
        return getVisibility() == 0 && this.mController.isSpinning(this.mEntry.getKey(), this.mToken);
    }

    public void setBouncerChecker(NotificationRemoteInputManager.BouncerChecker bouncerChecker) {
        this.mBouncerChecker = bouncerChecker;
    }

    public void addOnEditTextFocusChangedListener(View.OnFocusChangeListener onFocusChangeListener) {
        this.mEditTextFocusChangeListeners.add(onFocusChangeListener);
    }

    public void removeOnEditTextFocusChangedListener(View.OnFocusChangeListener onFocusChangeListener) {
        this.mEditTextFocusChangeListeners.remove(onFocusChangeListener);
    }

    public boolean editTextHasFocus() {
        RemoteEditText remoteEditText = this.mEditText;
        return remoteEditText != null && remoteEditText.hasFocus();
    }

    /* access modifiers changed from: private */
    public void onEditTextFocusChanged(RemoteEditText remoteEditText, boolean z) {
        Iterator it = new ArrayList(this.mEditTextFocusChangeListeners).iterator();
        while (it.hasNext()) {
            ((View.OnFocusChangeListener) it.next()).onFocusChange(remoteEditText, z);
        }
    }

    public void addOnSendRemoteInputListener(OnSendRemoteInputListener onSendRemoteInputListener) {
        this.mOnSendListeners.add(onSendRemoteInputListener);
    }

    public void removeOnSendRemoteInputListener(OnSendRemoteInputListener onSendRemoteInputListener) {
        this.mOnSendListeners.remove(onSendRemoteInputListener);
    }

    /* loaded from: classes.dex */
    private class EditorActionHandler implements TextView.OnEditorActionListener {
        private EditorActionHandler() {
        }

        @Override // android.widget.TextView.OnEditorActionListener
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            boolean z = keyEvent == null && (i == 6 || i == 5 || i == 4);
            boolean z2 = keyEvent != null && KeyEvent.isConfirmKey(keyEvent.getKeyCode()) && keyEvent.getAction() == 0;
            if (!z && !z2) {
                return false;
            }
            if (RemoteInputView.this.mEditText.length() > 0 || RemoteInputView.this.mEntry.remoteInputAttachment != null) {
                RemoteInputView remoteInputView = RemoteInputView.this;
                remoteInputView.sendRemoteInput(remoteInputView.prepareRemoteInput());
            }
            return true;
        }
    }

    /* loaded from: classes.dex */
    private class SendButtonTextWatcher implements TextWatcher {
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        private SendButtonTextWatcher() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            RemoteInputView.this.updateSendButton();
        }
    }

    /* loaded from: classes.dex */
    public static class RemoteEditText extends EditText {
        private InputMethodManager mInputMethodManager;
        private RemoteInputView mRemoteInputView;
        boolean mShowImeOnInputConnection;
        UserHandle mUser;
        private final OnReceiveContentListener mOnReceiveContentListener = new RemoteInputView$RemoteEditText$$ExternalSyntheticLambda0(this);
        private ArraySet<String> mSupportedMimes = new ArraySet<>();
        private LightBarController mLightBarController = (LightBarController) Dependency.get(LightBarController.class);

        public RemoteEditText(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        void setSupportedMimeTypes(Collection<String> collection) {
            OnReceiveContentListener onReceiveContentListener;
            String[] strArr = null;
            if (collection == null || collection.isEmpty()) {
                onReceiveContentListener = null;
            } else {
                strArr = (String[]) collection.toArray(new String[0]);
                onReceiveContentListener = this.mOnReceiveContentListener;
            }
            setOnReceiveContentListener(strArr, onReceiveContentListener);
            this.mSupportedMimes.clear();
            this.mSupportedMimes.addAll(collection);
        }

        /* access modifiers changed from: private */
        public void hideIme() {
            InputMethodManager inputMethodManager = this.mInputMethodManager;
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            }
        }

        /* access modifiers changed from: private */
        public void defocusIfNeeded(boolean z) {
            RemoteInputView remoteInputView;
            RemoteInputView remoteInputView2 = this.mRemoteInputView;
            if ((remoteInputView2 == null || !remoteInputView2.mEntry.getRow().isChangingPosition()) && !isTemporarilyDetached()) {
                if (isFocusable() && isEnabled()) {
                    setInnerFocusable(false);
                    RemoteInputView remoteInputView3 = this.mRemoteInputView;
                    if (remoteInputView3 != null) {
                        remoteInputView3.onDefocus(z, true);
                    }
                    this.mShowImeOnInputConnection = false;
                }
            } else if (isTemporarilyDetached() && (remoteInputView = this.mRemoteInputView) != null) {
                remoteInputView.mEntry.remoteInputText = getText();
            }
        }

        @Override // android.widget.TextView, android.view.View
        protected void onVisibilityChanged(View view, int i) {
            super.onVisibilityChanged(view, i);
            if (!isShown()) {
                defocusIfNeeded(false);
            }
        }

        @Override // android.widget.TextView, android.view.View
        protected void onFocusChanged(boolean z, int i, Rect rect) {
            super.onFocusChanged(z, i, rect);
            RemoteInputView remoteInputView = this.mRemoteInputView;
            if (remoteInputView != null) {
                remoteInputView.onEditTextFocusChanged(this, z);
            }
            if (!z) {
                defocusIfNeeded(true);
            }
            RemoteInputView remoteInputView2 = this.mRemoteInputView;
            if (remoteInputView2 != null && !remoteInputView2.mRemoved) {
                this.mLightBarController.setDirectReplying(z);
            }
        }

        @Override // android.widget.TextView, android.view.View
        public void getFocusedRect(Rect rect) {
            super.getFocusedRect(rect);
            int i = ((EditText) this).mScrollY;
            rect.top = i;
            rect.bottom = i + (((EditText) this).mBottom - ((EditText) this).mTop);
        }

        @Override // android.view.View
        public boolean requestRectangleOnScreen(Rect rect) {
            return this.mRemoteInputView.requestScrollTo();
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
            defocusIfNeeded(true);
            return true;
        }

        @Override // android.widget.TextView, android.view.View
        public boolean onKeyPreIme(int i, KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() == 4 && keyEvent.getAction() == 1) {
                defocusIfNeeded(true);
            }
            return super.onKeyPreIme(i, keyEvent);
        }

        @Override // android.widget.TextView, android.view.View
        public boolean onCheckIsTextEditor() {
            RemoteInputView remoteInputView = this.mRemoteInputView;
            return !(remoteInputView != null && remoteInputView.mRemoved) && super.onCheckIsTextEditor();
        }

        @Override // android.widget.TextView, android.view.View
        public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
            Context context;
            InputConnection onCreateInputConnection = super.onCreateInputConnection(editorInfo);
            try {
                Context context2 = ((EditText) this).mContext;
                context = context2.createPackageContextAsUser(context2.getPackageName(), 0, this.mUser);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("RemoteInput", "Unable to create user context:" + e.getMessage(), e);
                context = null;
            }
            if (this.mShowImeOnInputConnection && onCreateInputConnection != null) {
                if (context == null) {
                    context = getContext();
                }
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(InputMethodManager.class);
                this.mInputMethodManager = inputMethodManager;
                if (inputMethodManager != null) {
                    post(new Runnable() { // from class: com.android.systemui.statusbar.policy.RemoteInputView.RemoteEditText.1
                        @Override // java.lang.Runnable
                        public void run() {
                            RemoteEditText.this.mInputMethodManager.viewClicked(RemoteEditText.this);
                            RemoteEditText.this.mInputMethodManager.showSoftInput(RemoteEditText.this, 0);
                        }
                    });
                }
            }
            return onCreateInputConnection;
        }

        @Override // android.widget.TextView
        public void onCommitCompletion(CompletionInfo completionInfo) {
            clearComposingText();
            setText(completionInfo.getText());
            setSelection(getText().length());
        }

        void setInnerFocusable(boolean z) {
            setFocusableInTouchMode(z);
            setFocusable(z);
            setCursorVisible(z);
            if (z) {
                requestFocus();
            }
        }

        /* access modifiers changed from: private */
        public ContentInfo onReceiveContent(View view, ContentInfo contentInfo) {
            Pair partition = contentInfo.partition(RemoteInputView$RemoteEditText$$ExternalSyntheticLambda1.INSTANCE);
            ContentInfo contentInfo2 = (ContentInfo) partition.first;
            ContentInfo contentInfo3 = (ContentInfo) partition.second;
            if (contentInfo2 != null) {
                this.mRemoteInputView.setAttachment(contentInfo2);
            }
            return contentInfo3;
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$onReceiveContent$0(ClipData.Item item) {
            return item.getUri() != null;
        }
    }
}
