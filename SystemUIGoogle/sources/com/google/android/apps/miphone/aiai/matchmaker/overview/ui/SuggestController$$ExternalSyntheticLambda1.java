package com.google.android.apps.miphone.aiai.matchmaker.overview.ui;

import android.content.Context;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.SuggestController;
import java.util.concurrent.Executor;
/* compiled from: D8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class SuggestController$$ExternalSyntheticLambda1 implements SuggestController.Factory {
    public static final /* synthetic */ SuggestController$$ExternalSyntheticLambda1 INSTANCE = new SuggestController$$ExternalSyntheticLambda1();

    private /* synthetic */ SuggestController$$ExternalSyntheticLambda1() {
    }

    @Override // com.google.android.apps.miphone.aiai.matchmaker.overview.ui.SuggestController.Factory
    public final ContentSuggestionsServiceWrapper create(Context context, Executor executor, Executor executor2, Executor executor3) {
        return new ContentSuggestionsServiceWrapperImpl(context, executor, executor2, executor3);
    }
}
