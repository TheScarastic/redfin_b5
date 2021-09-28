package com.android.systemui.media.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.graphics.drawable.IconCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$style;
import com.android.systemui.media.dialog.MediaOutputController;
import com.android.systemui.statusbar.phone.SystemUIDialog;
/* loaded from: classes.dex */
public abstract class MediaOutputBaseDialog extends SystemUIDialog implements MediaOutputController.Callback {
    MediaOutputBaseAdapter mAdapter;
    final Context mContext;
    private LinearLayout mDeviceListLayout;
    private RecyclerView mDevicesRecyclerView;
    View mDialogView;
    private Button mDoneButton;
    private ImageView mHeaderIcon;
    private TextView mHeaderSubtitle;
    private TextView mHeaderTitle;
    private final RecyclerView.LayoutManager mLayoutManager;
    private int mListMaxHeight;
    final MediaOutputController mMediaOutputController;
    private Button mStopButton;
    private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());
    private final ViewTreeObserver.OnGlobalLayoutListener mDeviceListLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.android.systemui.media.dialog.MediaOutputBaseDialog$$ExternalSyntheticLambda3
        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public final void onGlobalLayout() {
            MediaOutputBaseDialog.m127$r8$lambda$maUuQNGsMoIvHEAr1ZRNK_oqoE(MediaOutputBaseDialog.this);
        }
    };

    abstract IconCompat getHeaderIcon();

    abstract int getHeaderIconRes();

    abstract int getHeaderIconSize();

    abstract CharSequence getHeaderSubtitle();

    abstract CharSequence getHeaderText();

    abstract int getStopButtonVisibility();

    void onHeaderIconClick() {
    }

    public /* synthetic */ void lambda$new$0() {
        if (this.mDeviceListLayout.getHeight() > this.mListMaxHeight) {
            ViewGroup.LayoutParams layoutParams = this.mDeviceListLayout.getLayoutParams();
            layoutParams.height = this.mListMaxHeight;
            this.mDeviceListLayout.setLayoutParams(layoutParams);
        }
    }

    public MediaOutputBaseDialog(Context context, MediaOutputController mediaOutputController) {
        super(context, R$style.Theme_SystemUI_Dialog_MediaOutput);
        this.mContext = context;
        this.mMediaOutputController = mediaOutputController;
        this.mLayoutManager = new LinearLayoutManager(context);
        this.mListMaxHeight = context.getResources().getDimensionPixelSize(R$dimen.media_output_dialog_list_max_height);
    }

    @Override // android.app.AlertDialog, android.app.Dialog
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mDialogView = LayoutInflater.from(this.mContext).inflate(R$layout.media_output_dialog, (ViewGroup) null);
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = 80;
        attributes.setFitInsetsTypes(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
        attributes.setFitInsetsSides(WindowInsets.Side.all());
        attributes.setFitInsetsIgnoringVisibility(true);
        window.setAttributes(attributes);
        window.setContentView(this.mDialogView);
        window.setLayout(-1, -2);
        window.setWindowAnimations(R$style.Animation_MediaOutputDialog);
        this.mHeaderTitle = (TextView) this.mDialogView.requireViewById(R$id.header_title);
        this.mHeaderSubtitle = (TextView) this.mDialogView.requireViewById(R$id.header_subtitle);
        this.mHeaderIcon = (ImageView) this.mDialogView.requireViewById(R$id.header_icon);
        this.mDevicesRecyclerView = (RecyclerView) this.mDialogView.requireViewById(R$id.list_result);
        this.mDeviceListLayout = (LinearLayout) this.mDialogView.requireViewById(R$id.device_list);
        this.mDoneButton = (Button) this.mDialogView.requireViewById(R$id.done);
        this.mStopButton = (Button) this.mDialogView.requireViewById(R$id.stop);
        this.mDeviceListLayout.getViewTreeObserver().addOnGlobalLayoutListener(this.mDeviceListLayoutListener);
        this.mDevicesRecyclerView.setLayoutManager(this.mLayoutManager);
        this.mDevicesRecyclerView.setAdapter(this.mAdapter);
        this.mHeaderIcon.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.media.dialog.MediaOutputBaseDialog$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MediaOutputBaseDialog.$r8$lambda$DXWg7ZF69x5GvRYyCrcvw1mJgAo(MediaOutputBaseDialog.this, view);
            }
        });
        this.mDoneButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.media.dialog.MediaOutputBaseDialog$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MediaOutputBaseDialog.m128$r8$lambda$pqVNoF966j4Kvu_Y2Y6ktptQlM(MediaOutputBaseDialog.this, view);
            }
        });
        this.mStopButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.media.dialog.MediaOutputBaseDialog$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MediaOutputBaseDialog.$r8$lambda$r1AKjMGRlcvcmEdjOQMD0oYbOf0(MediaOutputBaseDialog.this, view);
            }
        });
    }

    public /* synthetic */ void lambda$onCreate$1(View view) {
        onHeaderIconClick();
    }

    public /* synthetic */ void lambda$onCreate$2(View view) {
        dismiss();
    }

    public /* synthetic */ void lambda$onCreate$3(View view) {
        this.mMediaOutputController.releaseSession();
        dismiss();
    }

    @Override // com.android.systemui.statusbar.phone.SystemUIDialog, android.app.Dialog
    public void onStart() {
        super.onStart();
        this.mMediaOutputController.start(this);
    }

    @Override // com.android.systemui.statusbar.phone.SystemUIDialog, android.app.Dialog
    public void onStop() {
        super.onStop();
        this.mMediaOutputController.stop();
    }

    /* renamed from: refresh */
    public void lambda$onRouteChanged$5() {
        int headerIconRes = getHeaderIconRes();
        IconCompat headerIcon = getHeaderIcon();
        if (headerIconRes != 0) {
            this.mHeaderIcon.setVisibility(0);
            this.mHeaderIcon.setImageResource(headerIconRes);
        } else if (headerIcon != null) {
            this.mHeaderIcon.setVisibility(0);
            this.mHeaderIcon.setImageIcon(headerIcon.toIcon(this.mContext));
        } else {
            this.mHeaderIcon.setVisibility(8);
        }
        if (this.mHeaderIcon.getVisibility() == 0) {
            int headerIconSize = getHeaderIconSize();
            this.mHeaderIcon.setLayoutParams(new LinearLayout.LayoutParams(this.mContext.getResources().getDimensionPixelSize(R$dimen.media_output_dialog_header_icon_padding) + headerIconSize, headerIconSize));
        }
        this.mHeaderTitle.setText(getHeaderText());
        CharSequence headerSubtitle = getHeaderSubtitle();
        if (TextUtils.isEmpty(headerSubtitle)) {
            this.mHeaderSubtitle.setVisibility(8);
            this.mHeaderTitle.setGravity(8388627);
        } else {
            this.mHeaderSubtitle.setVisibility(0);
            this.mHeaderSubtitle.setText(headerSubtitle);
            this.mHeaderTitle.setGravity(0);
        }
        if (!this.mAdapter.isDragging() && !this.mAdapter.isAnimating()) {
            int currentActivePosition = this.mAdapter.getCurrentActivePosition();
            if (currentActivePosition >= 0) {
                this.mAdapter.notifyItemChanged(currentActivePosition);
            } else {
                this.mAdapter.notifyDataSetChanged();
            }
        }
        this.mStopButton.setVisibility(getStopButtonVisibility());
    }

    @Override // com.android.systemui.media.dialog.MediaOutputController.Callback
    public void onMediaChanged() {
        this.mMainThreadHandler.post(new Runnable() { // from class: com.android.systemui.media.dialog.MediaOutputBaseDialog$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                MediaOutputBaseDialog.$r8$lambda$pIvwlkMZCANcCqYt6TmbazO3DZE(MediaOutputBaseDialog.this);
            }
        });
    }

    @Override // com.android.systemui.media.dialog.MediaOutputController.Callback
    public void onMediaStoppedOrPaused() {
        if (isShowing()) {
            dismiss();
        }
    }

    @Override // com.android.systemui.media.dialog.MediaOutputController.Callback
    public void onRouteChanged() {
        this.mMainThreadHandler.post(new Runnable() { // from class: com.android.systemui.media.dialog.MediaOutputBaseDialog$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                MediaOutputBaseDialog.m126$r8$lambda$dxtlyJqKFtBTZkqUbf4PxYqg(MediaOutputBaseDialog.this);
            }
        });
    }

    @Override // com.android.systemui.media.dialog.MediaOutputController.Callback
    public void dismissDialog() {
        dismiss();
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (!z && isShowing()) {
            dismiss();
        }
    }
}
