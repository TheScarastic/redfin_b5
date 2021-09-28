package com.android.systemui.controls.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.service.controls.actions.BooleanAction;
import android.service.controls.actions.CommandAction;
import android.service.controls.actions.ControlAction;
import android.service.controls.actions.FloatAction;
import android.service.controls.actions.ModeAction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ChallengeDialogs.kt */
/* loaded from: classes.dex */
public final class ChallengeDialogs {
    public static final ChallengeDialogs INSTANCE = new ChallengeDialogs();

    private ChallengeDialogs() {
    }

    public final Dialog createPinDialog(ControlViewHolder controlViewHolder, boolean z, boolean z2, Function0<Unit> function0) {
        Pair pair;
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        Intrinsics.checkNotNullParameter(function0, "onCancel");
        ControlAction lastAction = controlViewHolder.getLastAction();
        if (lastAction == null) {
            Log.e("ControlsUiController", "PIN Dialog attempted but no last action is set. Will not show");
            return null;
        }
        Resources resources = controlViewHolder.getContext().getResources();
        if (z2) {
            pair = new Pair(resources.getString(R$string.controls_pin_wrong), Integer.valueOf(R$string.controls_pin_instructions_retry));
        } else {
            pair = new Pair(resources.getString(R$string.controls_pin_verify, controlViewHolder.getTitle().getText()), Integer.valueOf(R$string.controls_pin_instructions));
        }
        int intValue = ((Number) pair.component2()).intValue();
        ChallengeDialogs$createPinDialog$1 challengeDialogs$createPinDialog$1 = new ChallengeDialogs$createPinDialog$1(controlViewHolder.getContext());
        challengeDialogs$createPinDialog$1.setTitle((String) pair.component1());
        challengeDialogs$createPinDialog$1.setView(LayoutInflater.from(challengeDialogs$createPinDialog$1.getContext()).inflate(R$layout.controls_dialog_pin, (ViewGroup) null));
        challengeDialogs$createPinDialog$1.setButton(-1, challengeDialogs$createPinDialog$1.getContext().getText(17039370), new DialogInterface.OnClickListener(controlViewHolder, lastAction) { // from class: com.android.systemui.controls.ui.ChallengeDialogs$createPinDialog$2$1
            final /* synthetic */ ControlViewHolder $cvh;
            final /* synthetic */ ControlAction $lastAction;

            /* access modifiers changed from: package-private */
            {
                this.$cvh = r1;
                this.$lastAction = r2;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface instanceof Dialog) {
                    Dialog dialog = (Dialog) dialogInterface;
                    int i2 = R$id.controls_pin_input;
                    dialog.requireViewById(i2);
                    this.$cvh.action(ChallengeDialogs.access$addChallengeValue(ChallengeDialogs.INSTANCE, this.$lastAction, ((EditText) dialog.requireViewById(i2)).getText().toString()));
                    dialogInterface.dismiss();
                }
            }
        });
        challengeDialogs$createPinDialog$1.setButton(-2, challengeDialogs$createPinDialog$1.getContext().getText(17039360), new DialogInterface.OnClickListener(function0) { // from class: com.android.systemui.controls.ui.ChallengeDialogs$createPinDialog$2$2
            final /* synthetic */ Function0<Unit> $onCancel;

            /* access modifiers changed from: package-private */
            {
                this.$onCancel = r1;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                this.$onCancel.invoke();
                dialogInterface.cancel();
            }
        });
        Window window = challengeDialogs$createPinDialog$1.getWindow();
        window.setType(2020);
        window.setSoftInputMode(4);
        challengeDialogs$createPinDialog$1.setOnShowListener(new DialogInterface.OnShowListener(challengeDialogs$createPinDialog$1, intValue, z) { // from class: com.android.systemui.controls.ui.ChallengeDialogs$createPinDialog$2$4
            final /* synthetic */ int $instructions;
            final /* synthetic */ ChallengeDialogs$createPinDialog$1 $this_apply;
            final /* synthetic */ boolean $useAlphaNumeric;

            /* access modifiers changed from: package-private */
            {
                this.$this_apply = r1;
                this.$instructions = r2;
                this.$useAlphaNumeric = r3;
            }

            @Override // android.content.DialogInterface.OnShowListener
            public final void onShow(DialogInterface dialogInterface) {
                final EditText editText = (EditText) this.$this_apply.requireViewById(R$id.controls_pin_input);
                editText.setHint(this.$instructions);
                ChallengeDialogs$createPinDialog$1 challengeDialogs$createPinDialog$12 = this.$this_apply;
                int i = R$id.controls_pin_use_alpha;
                final CheckBox checkBox = (CheckBox) challengeDialogs$createPinDialog$12.requireViewById(i);
                checkBox.setChecked(this.$useAlphaNumeric);
                ChallengeDialogs challengeDialogs = ChallengeDialogs.INSTANCE;
                Intrinsics.checkNotNullExpressionValue(editText, "editText");
                ChallengeDialogs.access$setInputType(challengeDialogs, editText, checkBox.isChecked());
                ((CheckBox) this.$this_apply.requireViewById(i)).setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.ui.ChallengeDialogs$createPinDialog$2$4.1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        ChallengeDialogs challengeDialogs2 = ChallengeDialogs.INSTANCE;
                        EditText editText2 = editText;
                        Intrinsics.checkNotNullExpressionValue(editText2, "editText");
                        ChallengeDialogs.access$setInputType(challengeDialogs2, editText2, checkBox.isChecked());
                    }
                });
                editText.requestFocus();
            }
        });
        return challengeDialogs$createPinDialog$1;
    }

    public final Dialog createConfirmationDialog(ControlViewHolder controlViewHolder, Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        Intrinsics.checkNotNullParameter(function0, "onCancel");
        ControlAction lastAction = controlViewHolder.getLastAction();
        if (lastAction == null) {
            Log.e("ControlsUiController", "Confirmation Dialog attempted but no last action is set. Will not show");
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(controlViewHolder.getContext(), 16974545);
        builder.setTitle(controlViewHolder.getContext().getResources().getString(R$string.controls_confirmation_message, controlViewHolder.getTitle().getText()));
        builder.setPositiveButton(17039370, new DialogInterface.OnClickListener(controlViewHolder, lastAction) { // from class: com.android.systemui.controls.ui.ChallengeDialogs$createConfirmationDialog$builder$1$1
            final /* synthetic */ ControlViewHolder $cvh;
            final /* synthetic */ ControlAction $lastAction;

            /* access modifiers changed from: package-private */
            {
                this.$cvh = r1;
                this.$lastAction = r2;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                this.$cvh.action(ChallengeDialogs.access$addChallengeValue(ChallengeDialogs.INSTANCE, this.$lastAction, "true"));
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(17039360, new DialogInterface.OnClickListener(function0) { // from class: com.android.systemui.controls.ui.ChallengeDialogs$createConfirmationDialog$builder$1$2
            final /* synthetic */ Function0<Unit> $onCancel;

            /* access modifiers changed from: package-private */
            {
                this.$onCancel = r1;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                this.$onCancel.invoke();
                dialogInterface.cancel();
            }
        });
        AlertDialog create = builder.create();
        create.getWindow().setType(2020);
        return create;
    }

    /* access modifiers changed from: private */
    public final void setInputType(EditText editText, boolean z) {
        if (z) {
            editText.setInputType(129);
        } else {
            editText.setInputType(18);
        }
    }

    /* access modifiers changed from: private */
    public final ControlAction addChallengeValue(ControlAction controlAction, String str) {
        String templateId = controlAction.getTemplateId();
        if (controlAction instanceof BooleanAction) {
            return new BooleanAction(templateId, ((BooleanAction) controlAction).getNewState(), str);
        }
        if (controlAction instanceof FloatAction) {
            return new FloatAction(templateId, ((FloatAction) controlAction).getNewValue(), str);
        }
        if (controlAction instanceof CommandAction) {
            return new CommandAction(templateId, str);
        }
        if (controlAction instanceof ModeAction) {
            return new ModeAction(templateId, ((ModeAction) controlAction).getNewMode(), str);
        }
        throw new IllegalStateException(Intrinsics.stringPlus("'action' is not a known type: ", controlAction));
    }
}
