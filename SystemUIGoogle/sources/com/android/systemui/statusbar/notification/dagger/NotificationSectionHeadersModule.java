package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.R$string;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent;
import javax.inject.Provider;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationSectionHeadersModule.kt */
/* loaded from: classes.dex */
public final class NotificationSectionHeadersModule {
    public static final NotificationSectionHeadersModule INSTANCE = new NotificationSectionHeadersModule();

    private NotificationSectionHeadersModule() {
    }

    public static final SectionHeaderControllerSubcomponent providesIncomingHeaderSubcomponent(Provider<SectionHeaderControllerSubcomponent.Builder> provider) {
        Intrinsics.checkNotNullParameter(provider, "builder");
        return provider.get().nodeLabel("incoming header").headerText(R$string.notification_section_header_incoming).clickIntentAction("android.settings.NOTIFICATION_SETTINGS").build();
    }

    public static final SectionHeaderControllerSubcomponent providesAlertingHeaderSubcomponent(Provider<SectionHeaderControllerSubcomponent.Builder> provider) {
        Intrinsics.checkNotNullParameter(provider, "builder");
        return provider.get().nodeLabel("alerting header").headerText(R$string.notification_section_header_alerting).clickIntentAction("android.settings.NOTIFICATION_SETTINGS").build();
    }

    public static final SectionHeaderControllerSubcomponent providesPeopleHeaderSubcomponent(Provider<SectionHeaderControllerSubcomponent.Builder> provider) {
        Intrinsics.checkNotNullParameter(provider, "builder");
        return provider.get().nodeLabel("people header").headerText(R$string.notification_section_header_conversations).clickIntentAction("android.settings.CONVERSATION_SETTINGS").build();
    }

    public static final SectionHeaderControllerSubcomponent providesSilentHeaderSubcomponent(Provider<SectionHeaderControllerSubcomponent.Builder> provider) {
        Intrinsics.checkNotNullParameter(provider, "builder");
        return provider.get().nodeLabel("silent header").headerText(R$string.notification_section_header_gentle).clickIntentAction("android.settings.NOTIFICATION_SETTINGS").build();
    }

    public static final NodeController providesSilentHeaderNodeController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        Intrinsics.checkNotNullParameter(sectionHeaderControllerSubcomponent, "subcomponent");
        return sectionHeaderControllerSubcomponent.getNodeController();
    }

    public static final SectionHeaderController providesSilentHeaderController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        Intrinsics.checkNotNullParameter(sectionHeaderControllerSubcomponent, "subcomponent");
        return sectionHeaderControllerSubcomponent.getHeaderController();
    }

    public static final NodeController providesAlertingHeaderNodeController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        Intrinsics.checkNotNullParameter(sectionHeaderControllerSubcomponent, "subcomponent");
        return sectionHeaderControllerSubcomponent.getNodeController();
    }

    public static final SectionHeaderController providesAlertingHeaderController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        Intrinsics.checkNotNullParameter(sectionHeaderControllerSubcomponent, "subcomponent");
        return sectionHeaderControllerSubcomponent.getHeaderController();
    }

    public static final NodeController providesPeopleHeaderNodeController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        Intrinsics.checkNotNullParameter(sectionHeaderControllerSubcomponent, "subcomponent");
        return sectionHeaderControllerSubcomponent.getNodeController();
    }

    public static final SectionHeaderController providesPeopleHeaderController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        Intrinsics.checkNotNullParameter(sectionHeaderControllerSubcomponent, "subcomponent");
        return sectionHeaderControllerSubcomponent.getHeaderController();
    }

    public static final NodeController providesIncomingHeaderNodeController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        Intrinsics.checkNotNullParameter(sectionHeaderControllerSubcomponent, "subcomponent");
        return sectionHeaderControllerSubcomponent.getNodeController();
    }

    public static final SectionHeaderController providesIncomingHeaderController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        Intrinsics.checkNotNullParameter(sectionHeaderControllerSubcomponent, "subcomponent");
        return sectionHeaderControllerSubcomponent.getHeaderController();
    }
}
