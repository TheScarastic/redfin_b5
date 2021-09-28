package com.android.systemui.people.widget;

import android.app.people.ConversationChannel;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
/* loaded from: classes.dex */
public final /* synthetic */ class PeopleSpaceWidgetManager$TileConversationListener$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ PeopleSpaceWidgetManager.TileConversationListener f$0;
    public final /* synthetic */ ConversationChannel f$1;

    public /* synthetic */ PeopleSpaceWidgetManager$TileConversationListener$$ExternalSyntheticLambda0(PeopleSpaceWidgetManager.TileConversationListener tileConversationListener, ConversationChannel conversationChannel) {
        this.f$0 = tileConversationListener;
        this.f$1 = conversationChannel;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onConversationUpdate$0(this.f$1);
    }
}
