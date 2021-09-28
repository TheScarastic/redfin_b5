package androidx.appcompat.widget;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.DragEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputContentInfo;
import android.view.textclassifier.TextClassifier;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.view.ContentInfoCompat;
import androidx.core.view.OnReceiveContentViewBehavior;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.inputmethod.InputConnectionCompat$OnCommitContentListener;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import androidx.core.widget.TextViewOnReceiveContentListener;
import androidx.slice.view.R$layout;
import com.android.systemui.shared.R;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class AppCompatEditText extends EditText implements OnReceiveContentViewBehavior {
    public final AppCompatBackgroundHelper mBackgroundTintHelper;
    public final TextViewOnReceiveContentListener mDefaultOnReceiveContentListener;
    public final AppCompatEditor mEditor;
    public final AppCompatTextHelper mTextHelper;

    public AppCompatEditText(Context context) {
        this(context, null);
    }

    @Override // android.widget.TextView, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.applySupportBackgroundTint();
        }
        AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
        if (appCompatTextHelper != null) {
            appCompatTextHelper.applyCompoundDrawablesTints();
        }
    }

    @Override // android.widget.TextView
    public TextClassifier getTextClassifier() {
        return super.getTextClassifier();
    }

    @Override // android.widget.TextView, android.view.View
    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        InputConnection onCreateInputConnection = super.onCreateInputConnection(editorInfo);
        Objects.requireNonNull(this.mTextHelper);
        R$layout.onCreateInputConnection(onCreateInputConnection, editorInfo, this);
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        String[] strArr = (String[]) getTag(R.id.tag_on_receive_content_mime_types);
        if (onCreateInputConnection == null || strArr == null) {
            return onCreateInputConnection;
        }
        editorInfo.contentMimeTypes = strArr;
        return new InputConnectionWrapper(onCreateInputConnection, false, new InputConnectionCompat$OnCommitContentListener() { // from class: androidx.appcompat.widget.AppCompatEditText.1
            public boolean onCommitContent(InputContentInfoCompat inputContentInfoCompat, int i, Bundle bundle) {
                if ((i & 1) != 0) {
                    try {
                        ((InputContentInfoCompat.InputContentInfoCompatApi25Impl) inputContentInfoCompat.mImpl).requestPermission();
                    } catch (Exception e) {
                        Log.w("AppCompatEditText", "Can't insert content from IME; requestPermission() failed", e);
                        return false;
                    }
                }
                ContentInfoCompat.Builder builder = new ContentInfoCompat.Builder(new ClipData(((InputContentInfoCompat.InputContentInfoCompatApi25Impl) inputContentInfoCompat.mImpl).mObject.getDescription(), new ClipData.Item(((InputContentInfoCompat.InputContentInfoCompatApi25Impl) inputContentInfoCompat.mImpl).mObject.getContentUri())), 2);
                builder.mLinkUri = ((InputContentInfoCompat.InputContentInfoCompatApi25Impl) inputContentInfoCompat.mImpl).mObject.getLinkUri();
                builder.mExtras = bundle;
                return ViewCompat.performReceiveContent(this, new ContentInfoCompat(builder)) == null;
            }
        }) { // from class: androidx.core.view.inputmethod.InputConnectionCompat$1
            public final /* synthetic */ InputConnectionCompat$OnCommitContentListener val$listener;

            {
                this.val$listener = r3;
            }

            @Override // android.view.inputmethod.InputConnectionWrapper, android.view.inputmethod.InputConnection
            public boolean commitContent(InputContentInfo inputContentInfo, int i, Bundle bundle) {
                InputContentInfoCompat inputContentInfoCompat;
                InputConnectionCompat$OnCommitContentListener inputConnectionCompat$OnCommitContentListener = this.val$listener;
                if (inputContentInfo == null) {
                    inputContentInfoCompat = null;
                } else {
                    inputContentInfoCompat = new InputContentInfoCompat(new InputContentInfoCompat.InputContentInfoCompatApi25Impl(inputContentInfo));
                }
                if (((AppCompatEditText.AnonymousClass1) inputConnectionCompat$OnCommitContentListener).onCommitContent(inputContentInfoCompat, i, bundle)) {
                    return true;
                }
                return super.commitContent(inputContentInfo, i, bundle);
            }
        };
    }

    /* JADX INFO: finally extract failed */
    @Override // android.widget.TextView, android.view.View
    public boolean onDragEvent(DragEvent dragEvent) {
        Activity activity;
        AppCompatEditor appCompatEditor = this.mEditor;
        Objects.requireNonNull(appCompatEditor);
        boolean z = false;
        if (dragEvent.getAction() == 3 && dragEvent.getLocalState() == null && ViewCompat.getOnReceiveContentMimeTypes(appCompatEditor.mTextView) != null) {
            Context context = appCompatEditor.mTextView.getContext();
            while (true) {
                if (!(context instanceof ContextWrapper)) {
                    activity = null;
                    break;
                } else if (context instanceof Activity) {
                    activity = (Activity) context;
                    break;
                } else {
                    context = ((ContextWrapper) context).getBaseContext();
                }
            }
            if (activity == null) {
                StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("No activity so not calling performReceiveContent: ");
                m.append(appCompatEditor.mTextView);
                Log.i("AppCompatEditor", m.toString());
            } else {
                TextView textView = appCompatEditor.mTextView;
                int offsetForPosition = textView.getOffsetForPosition(dragEvent.getX(), dragEvent.getY());
                activity.requestDragAndDropPermissions(dragEvent);
                textView.beginBatchEdit();
                try {
                    Selection.setSelection((Spannable) textView.getText(), offsetForPosition);
                    ViewCompat.performReceiveContent(textView, new ContentInfoCompat(new ContentInfoCompat.Builder(dragEvent.getClipData(), 3)));
                    textView.endBatchEdit();
                    z = true;
                } catch (Throwable th) {
                    textView.endBatchEdit();
                    throw th;
                }
            }
        }
        if (z) {
            return true;
        }
        return super.onDragEvent(dragEvent);
    }

    @Override // androidx.core.view.OnReceiveContentViewBehavior
    public ContentInfoCompat onReceiveContent(ContentInfoCompat contentInfoCompat) {
        return this.mDefaultOnReceiveContentListener.onReceiveContent(this, contentInfoCompat);
    }

    @Override // android.widget.TextView
    public boolean onTextContextMenuItem(int i) {
        ClipData clipData;
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        if (((String[]) getTag(R.id.tag_on_receive_content_mime_types)) == null || (i != 16908322 && i != 16908337)) {
            return super.onTextContextMenuItem(i);
        }
        ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService("clipboard");
        if (clipboardManager == null) {
            clipData = null;
        } else {
            clipData = clipboardManager.getPrimaryClip();
        }
        if (clipData != null) {
            ContentInfoCompat.Builder builder = new ContentInfoCompat.Builder(clipData, 1);
            builder.mFlags = i == 16908322 ? 0 : 1;
            ViewCompat.performReceiveContent(this, new ContentInfoCompat(builder));
        }
        return true;
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.onSetBackgroundDrawable();
        }
    }

    @Override // android.view.View
    public void setBackgroundResource(int i) {
        super.setBackgroundResource(i);
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.onSetBackgroundResource(i);
        }
    }

    @Override // android.widget.TextView
    public void setCustomSelectionActionModeCallback(ActionMode.Callback callback) {
        super.setCustomSelectionActionModeCallback(callback);
    }

    @Override // android.widget.TextView
    public void setTextAppearance(Context context, int i) {
        super.setTextAppearance(context, i);
        AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
        if (appCompatTextHelper != null) {
            appCompatTextHelper.onSetTextAppearance(context, i);
        }
    }

    @Override // android.widget.TextView
    public void setTextClassifier(TextClassifier textClassifier) {
        super.setTextClassifier(textClassifier);
    }

    public AppCompatEditText(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.editTextStyle);
    }

    @Override // android.widget.EditText, android.widget.TextView
    public Editable getText() {
        return super.getText();
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public AppCompatEditText(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TintContextWrapper.wrap(context);
        ThemeUtils.checkAppCompatTheme(this, getContext());
        AppCompatBackgroundHelper appCompatBackgroundHelper = new AppCompatBackgroundHelper(this);
        this.mBackgroundTintHelper = appCompatBackgroundHelper;
        appCompatBackgroundHelper.loadFromAttributes(attributeSet, i);
        AppCompatTextHelper appCompatTextHelper = new AppCompatTextHelper(this);
        this.mTextHelper = appCompatTextHelper;
        appCompatTextHelper.loadFromAttributes(attributeSet, i);
        appCompatTextHelper.applyCompoundDrawablesTints();
        this.mDefaultOnReceiveContentListener = new TextViewOnReceiveContentListener();
        this.mEditor = new AppCompatEditor(this);
    }
}
