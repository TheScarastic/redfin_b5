package androidx.leanback.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.leanback.R$color;
import androidx.leanback.R$dimen;
import androidx.leanback.R$id;
import androidx.leanback.R$integer;
import androidx.leanback.R$layout;
import androidx.leanback.R$raw;
import androidx.leanback.R$string;
import androidx.leanback.widget.SearchEditText;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class SearchBar extends RelativeLayout {
    static final String TAG = SearchBar.class.getSimpleName();
    boolean mAutoStartRecognition;
    private int mBackgroundAlpha;
    private int mBackgroundSpeechAlpha;
    private Drawable mBadgeDrawable;
    private ImageView mBadgeView;
    private Drawable mBarBackground;
    private int mBarHeight;
    private final Context mContext;
    final Handler mHandler;
    private String mHint;
    private final InputMethodManager mInputMethodManager;
    private boolean mListening;
    private SearchBarPermissionListener mPermissionListener;
    boolean mRecognizing;
    SearchBarListener mSearchBarListener;
    String mSearchQuery;
    SearchEditText mSearchTextEditor;
    SparseIntArray mSoundMap;
    SoundPool mSoundPool;
    SpeechOrbView mSpeechOrbView;
    private SpeechRecognitionCallback mSpeechRecognitionCallback;
    private SpeechRecognizer mSpeechRecognizer;
    private final int mTextColor;
    private final int mTextColorSpeechMode;
    private final int mTextHintColor;
    private final int mTextHintColorSpeechMode;
    private String mTitle;

    /* loaded from: classes.dex */
    public interface SearchBarListener {
        void onKeyboardDismiss(String str);

        void onSearchQueryChange(String str);

        void onSearchQuerySubmit(String str);
    }

    /* loaded from: classes.dex */
    public interface SearchBarPermissionListener {
        void requestAudioPermission();
    }

    public SearchBar(Context context) {
        this(context, null);
    }

    public SearchBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SearchBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mHandler = new Handler();
        this.mAutoStartRecognition = false;
        this.mSoundMap = new SparseIntArray();
        this.mRecognizing = false;
        this.mContext = context;
        Resources resources = getResources();
        LayoutInflater.from(getContext()).inflate(R$layout.lb_search_bar, (ViewGroup) this, true);
        this.mBarHeight = getResources().getDimensionPixelSize(R$dimen.lb_search_bar_height);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, this.mBarHeight);
        layoutParams.addRule(10, -1);
        setLayoutParams(layoutParams);
        setBackgroundColor(0);
        setClipChildren(false);
        this.mSearchQuery = "";
        this.mInputMethodManager = (InputMethodManager) context.getSystemService("input_method");
        this.mTextColorSpeechMode = resources.getColor(R$color.lb_search_bar_text_speech_mode);
        this.mTextColor = resources.getColor(R$color.lb_search_bar_text);
        this.mBackgroundSpeechAlpha = resources.getInteger(R$integer.lb_search_bar_speech_mode_background_alpha);
        this.mBackgroundAlpha = resources.getInteger(R$integer.lb_search_bar_text_mode_background_alpha);
        this.mTextHintColorSpeechMode = resources.getColor(R$color.lb_search_bar_hint_speech_mode);
        this.mTextHintColor = resources.getColor(R$color.lb_search_bar_hint);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mBarBackground = ((RelativeLayout) findViewById(R$id.lb_search_bar_items)).getBackground();
        this.mSearchTextEditor = (SearchEditText) findViewById(R$id.lb_search_text_editor);
        ImageView imageView = (ImageView) findViewById(R$id.lb_search_bar_badge);
        this.mBadgeView = imageView;
        Drawable drawable = this.mBadgeDrawable;
        if (drawable != null) {
            imageView.setImageDrawable(drawable);
        }
        this.mSearchTextEditor.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: androidx.leanback.widget.SearchBar.1
            @Override // android.view.View.OnFocusChangeListener
            public void onFocusChange(View view, boolean z) {
                if (z) {
                    SearchBar.this.showNativeKeyboard();
                } else {
                    SearchBar.this.hideNativeKeyboard();
                }
                SearchBar.this.updateUi(z);
            }
        });
        final AnonymousClass2 r0 = new Runnable() { // from class: androidx.leanback.widget.SearchBar.2
            @Override // java.lang.Runnable
            public void run() {
                SearchBar searchBar = SearchBar.this;
                searchBar.setSearchQueryInternal(searchBar.mSearchTextEditor.getText().toString());
            }
        };
        this.mSearchTextEditor.addTextChangedListener(new TextWatcher() { // from class: androidx.leanback.widget.SearchBar.3
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                SearchBar searchBar = SearchBar.this;
                if (!searchBar.mRecognizing) {
                    searchBar.mHandler.removeCallbacks(r0);
                    SearchBar.this.mHandler.post(r0);
                }
            }
        });
        this.mSearchTextEditor.setOnKeyboardDismissListener(new SearchEditText.OnKeyboardDismissListener() { // from class: androidx.leanback.widget.SearchBar.4
            @Override // androidx.leanback.widget.SearchEditText.OnKeyboardDismissListener
            public void onKeyboardDismiss() {
                SearchBar searchBar = SearchBar.this;
                SearchBarListener searchBarListener = searchBar.mSearchBarListener;
                if (searchBarListener != null) {
                    searchBarListener.onKeyboardDismiss(searchBar.mSearchQuery);
                }
            }
        });
        this.mSearchTextEditor.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: androidx.leanback.widget.SearchBar.5
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (3 == i || i == 0) {
                    SearchBar searchBar = SearchBar.this;
                    if (searchBar.mSearchBarListener != null) {
                        searchBar.hideNativeKeyboard();
                        SearchBar.this.mHandler.postDelayed(new Runnable() { // from class: androidx.leanback.widget.SearchBar.5.1
                            @Override // java.lang.Runnable
                            public void run() {
                                SearchBar.this.submitQuery();
                            }
                        }, 500);
                        return true;
                    }
                }
                if (1 == i) {
                    SearchBar searchBar2 = SearchBar.this;
                    if (searchBar2.mSearchBarListener != null) {
                        searchBar2.hideNativeKeyboard();
                        SearchBar.this.mHandler.postDelayed(new Runnable() { // from class: androidx.leanback.widget.SearchBar.5.2
                            @Override // java.lang.Runnable
                            public void run() {
                                SearchBar searchBar3 = SearchBar.this;
                                searchBar3.mSearchBarListener.onKeyboardDismiss(searchBar3.mSearchQuery);
                            }
                        }, 500);
                        return true;
                    }
                }
                if (2 != i) {
                    return false;
                }
                SearchBar.this.hideNativeKeyboard();
                SearchBar.this.mHandler.postDelayed(new Runnable() { // from class: androidx.leanback.widget.SearchBar.5.3
                    @Override // java.lang.Runnable
                    public void run() {
                        SearchBar searchBar3 = SearchBar.this;
                        searchBar3.mAutoStartRecognition = true;
                        searchBar3.mSpeechOrbView.requestFocus();
                    }
                }, 500);
                return true;
            }
        });
        this.mSearchTextEditor.setPrivateImeOptions("escapeNorth,voiceDismiss");
        SpeechOrbView speechOrbView = (SpeechOrbView) findViewById(R$id.lb_search_bar_speech_orb);
        this.mSpeechOrbView = speechOrbView;
        speechOrbView.setOnOrbClickedListener(new View.OnClickListener() { // from class: androidx.leanback.widget.SearchBar.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SearchBar.this.toggleRecognition();
            }
        });
        this.mSpeechOrbView.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: androidx.leanback.widget.SearchBar.7
            @Override // android.view.View.OnFocusChangeListener
            public void onFocusChange(View view, boolean z) {
                if (z) {
                    SearchBar.this.hideNativeKeyboard();
                    SearchBar searchBar = SearchBar.this;
                    if (searchBar.mAutoStartRecognition) {
                        searchBar.startRecognition();
                        SearchBar.this.mAutoStartRecognition = false;
                    }
                } else {
                    SearchBar.this.stopRecognition();
                }
                SearchBar.this.updateUi(z);
            }
        });
        updateUi(hasFocus());
        updateHint();
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mSoundPool = new SoundPool(2, 1, 0);
        loadSounds(this.mContext);
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onDetachedFromWindow() {
        stopRecognition();
        this.mSoundPool.release();
        super.onDetachedFromWindow();
    }

    void setSearchQueryInternal(String str) {
        if (!TextUtils.equals(this.mSearchQuery, str)) {
            this.mSearchQuery = str;
            SearchBarListener searchBarListener = this.mSearchBarListener;
            if (searchBarListener != null) {
                searchBarListener.onSearchQueryChange(str);
            }
        }
    }

    void hideNativeKeyboard() {
        this.mInputMethodManager.hideSoftInputFromWindow(this.mSearchTextEditor.getWindowToken(), 0);
    }

    void showNativeKeyboard() {
        this.mHandler.post(new Runnable() { // from class: androidx.leanback.widget.SearchBar.8
            @Override // java.lang.Runnable
            public void run() {
                SearchBar.this.mSearchTextEditor.requestFocusFromTouch();
                SearchBar.this.mSearchTextEditor.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 0, (float) SearchBar.this.mSearchTextEditor.getWidth(), (float) SearchBar.this.mSearchTextEditor.getHeight(), 0));
                SearchBar.this.mSearchTextEditor.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 1, (float) SearchBar.this.mSearchTextEditor.getWidth(), (float) SearchBar.this.mSearchTextEditor.getHeight(), 0));
            }
        });
    }

    private void updateHint() {
        String string = getResources().getString(R$string.lb_search_bar_hint);
        if (!TextUtils.isEmpty(this.mTitle)) {
            if (isVoiceMode()) {
                string = getResources().getString(R$string.lb_search_bar_hint_with_title_speech, this.mTitle);
            } else {
                string = getResources().getString(R$string.lb_search_bar_hint_with_title, this.mTitle);
            }
        } else if (isVoiceMode()) {
            string = getResources().getString(R$string.lb_search_bar_hint_speech);
        }
        this.mHint = string;
        SearchEditText searchEditText = this.mSearchTextEditor;
        if (searchEditText != null) {
            searchEditText.setHint(string);
        }
    }

    void toggleRecognition() {
        if (this.mRecognizing) {
            stopRecognition();
        } else {
            startRecognition();
        }
    }

    public void stopRecognition() {
        if (this.mRecognizing) {
            this.mSearchTextEditor.setText(this.mSearchQuery);
            this.mSearchTextEditor.setHint(this.mHint);
            this.mRecognizing = false;
            if (this.mSpeechRecognitionCallback == null && this.mSpeechRecognizer != null) {
                this.mSpeechOrbView.showNotListening();
                if (this.mListening) {
                    this.mSpeechRecognizer.cancel();
                    this.mListening = false;
                }
                this.mSpeechRecognizer.setRecognitionListener(null);
            }
        }
    }

    public void startRecognition() {
        SearchBarPermissionListener searchBarPermissionListener;
        if (!this.mRecognizing) {
            if (!hasFocus()) {
                requestFocus();
            }
            if (this.mSpeechRecognitionCallback != null) {
                this.mSearchTextEditor.setText("");
                this.mSearchTextEditor.setHint("");
                this.mSpeechRecognitionCallback.recognizeSpeech();
                this.mRecognizing = true;
            } else if (this.mSpeechRecognizer != null) {
                if (getContext().checkCallingOrSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                    this.mRecognizing = true;
                    this.mSearchTextEditor.setText("");
                    Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
                    intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
                    intent.putExtra("android.speech.extra.PARTIAL_RESULTS", true);
                    this.mSpeechRecognizer.setRecognitionListener(new RecognitionListener() { // from class: androidx.leanback.widget.SearchBar.9
                        @Override // android.speech.RecognitionListener
                        public void onBeginningOfSpeech() {
                        }

                        @Override // android.speech.RecognitionListener
                        public void onBufferReceived(byte[] bArr) {
                        }

                        @Override // android.speech.RecognitionListener
                        public void onEndOfSpeech() {
                        }

                        @Override // android.speech.RecognitionListener
                        public void onEvent(int i, Bundle bundle) {
                        }

                        @Override // android.speech.RecognitionListener
                        public void onReadyForSpeech(Bundle bundle) {
                            SearchBar.this.mSpeechOrbView.showListening();
                            SearchBar.this.playSearchOpen();
                        }

                        @Override // android.speech.RecognitionListener
                        public void onRmsChanged(float f) {
                            SearchBar.this.mSpeechOrbView.setSoundLevel(f < 0.0f ? 0 : (int) (f * 10.0f));
                        }

                        @Override // android.speech.RecognitionListener
                        public void onError(int i) {
                            switch (i) {
                                case 1:
                                    Log.w(SearchBar.TAG, "recognizer network timeout");
                                    break;
                                case 2:
                                    Log.w(SearchBar.TAG, "recognizer network error");
                                    break;
                                case 3:
                                    Log.w(SearchBar.TAG, "recognizer audio error");
                                    break;
                                case 4:
                                    Log.w(SearchBar.TAG, "recognizer server error");
                                    break;
                                case 5:
                                    Log.w(SearchBar.TAG, "recognizer client error");
                                    break;
                                case 6:
                                    Log.w(SearchBar.TAG, "recognizer speech timeout");
                                    break;
                                case 7:
                                    Log.w(SearchBar.TAG, "recognizer no match");
                                    break;
                                case 8:
                                    Log.w(SearchBar.TAG, "recognizer busy");
                                    break;
                                case 9:
                                    Log.w(SearchBar.TAG, "recognizer insufficient permissions");
                                    break;
                                default:
                                    Log.d(SearchBar.TAG, "recognizer other error");
                                    break;
                            }
                            SearchBar.this.stopRecognition();
                            SearchBar.this.playSearchFailure();
                        }

                        @Override // android.speech.RecognitionListener
                        public void onResults(Bundle bundle) {
                            ArrayList<String> stringArrayList = bundle.getStringArrayList("results_recognition");
                            if (stringArrayList != null) {
                                SearchBar.this.mSearchQuery = stringArrayList.get(0);
                                SearchBar searchBar = SearchBar.this;
                                searchBar.mSearchTextEditor.setText(searchBar.mSearchQuery);
                                SearchBar.this.submitQuery();
                            }
                            SearchBar.this.stopRecognition();
                            SearchBar.this.playSearchSuccess();
                        }

                        @Override // android.speech.RecognitionListener
                        public void onPartialResults(Bundle bundle) {
                            ArrayList<String> stringArrayList = bundle.getStringArrayList("results_recognition");
                            if (stringArrayList != null && stringArrayList.size() != 0) {
                                SearchBar.this.mSearchTextEditor.updateRecognizedText(stringArrayList.get(0), stringArrayList.size() > 1 ? stringArrayList.get(1) : null);
                            }
                        }
                    });
                    this.mListening = true;
                    this.mSpeechRecognizer.startListening(intent);
                } else if (Build.VERSION.SDK_INT < 23 || (searchBarPermissionListener = this.mPermissionListener) == null) {
                    throw new IllegalStateException("android.permission.RECORD_AUDIO required for search");
                } else {
                    searchBarPermissionListener.requestAudioPermission();
                }
            }
        }
    }

    void updateUi(boolean z) {
        if (z) {
            this.mBarBackground.setAlpha(this.mBackgroundSpeechAlpha);
            if (isVoiceMode()) {
                this.mSearchTextEditor.setTextColor(this.mTextHintColorSpeechMode);
                this.mSearchTextEditor.setHintTextColor(this.mTextHintColorSpeechMode);
            } else {
                this.mSearchTextEditor.setTextColor(this.mTextColorSpeechMode);
                this.mSearchTextEditor.setHintTextColor(this.mTextHintColorSpeechMode);
            }
        } else {
            this.mBarBackground.setAlpha(this.mBackgroundAlpha);
            this.mSearchTextEditor.setTextColor(this.mTextColor);
            this.mSearchTextEditor.setHintTextColor(this.mTextHintColor);
        }
        updateHint();
    }

    private boolean isVoiceMode() {
        return this.mSpeechOrbView.isFocused();
    }

    void submitQuery() {
        SearchBarListener searchBarListener;
        if (!TextUtils.isEmpty(this.mSearchQuery) && (searchBarListener = this.mSearchBarListener) != null) {
            searchBarListener.onSearchQuerySubmit(this.mSearchQuery);
        }
    }

    private void loadSounds(Context context) {
        int[] iArr = {R$raw.lb_voice_failure, R$raw.lb_voice_open, R$raw.lb_voice_no_input, R$raw.lb_voice_success};
        for (int i = 0; i < 4; i++) {
            int i2 = iArr[i];
            this.mSoundMap.put(i2, this.mSoundPool.load(context, i2, 1));
        }
    }

    private void play(final int i) {
        this.mHandler.post(new Runnable() { // from class: androidx.leanback.widget.SearchBar.10
            @Override // java.lang.Runnable
            public void run() {
                SearchBar.this.mSoundPool.play(SearchBar.this.mSoundMap.get(i), 1.0f, 1.0f, 1, 0, 1.0f);
            }
        });
    }

    void playSearchOpen() {
        play(R$raw.lb_voice_open);
    }

    void playSearchFailure() {
        play(R$raw.lb_voice_failure);
    }

    void playSearchSuccess() {
        play(R$raw.lb_voice_success);
    }

    @Override // android.view.View
    public void setNextFocusDownId(int i) {
        this.mSpeechOrbView.setNextFocusDownId(i);
        this.mSearchTextEditor.setNextFocusDownId(i);
    }
}
