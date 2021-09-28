package com.android.systemui.shortcut;

import android.content.Context;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ShortcutKeyDispatcher_Factory implements Factory<ShortcutKeyDispatcher> {
    private final Provider<Context> contextProvider;
    private final Provider<Optional<LegacySplitScreen>> splitScreenOptionalProvider;

    public ShortcutKeyDispatcher_Factory(Provider<Context> provider, Provider<Optional<LegacySplitScreen>> provider2) {
        this.contextProvider = provider;
        this.splitScreenOptionalProvider = provider2;
    }

    @Override // javax.inject.Provider
    public ShortcutKeyDispatcher get() {
        return newInstance(this.contextProvider.get(), this.splitScreenOptionalProvider.get());
    }

    public static ShortcutKeyDispatcher_Factory create(Provider<Context> provider, Provider<Optional<LegacySplitScreen>> provider2) {
        return new ShortcutKeyDispatcher_Factory(provider, provider2);
    }

    public static ShortcutKeyDispatcher newInstance(Context context, Optional<LegacySplitScreen> optional) {
        return new ShortcutKeyDispatcher(context, optional);
    }
}
