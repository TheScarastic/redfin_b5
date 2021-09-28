package com.adobe.xmp.options;

import androidx.recyclerview.widget.RecyclerView;
import com.adobe.xmp.XMPException;
import com.android.systemui.shared.system.QuickStepContract;
/* loaded from: classes.dex */
public final class PropertyOptions extends Options {
    public PropertyOptions() {
    }

    @Override // com.adobe.xmp.options.Options
    public void assertConsistency(int i) throws XMPException {
        if ((i & 256) > 0 && (i & QuickStepContract.SYSUI_STATE_STATUS_BAR_KEYGUARD_SHOWING_OCCLUDED) > 0) {
            throw new XMPException("IsStruct and IsArray options are mutually exclusive", 103);
        } else if ((i & 2) > 0 && (i & 768) > 0) {
            throw new XMPException("Structs and arrays can't have \"value\" options", 103);
        }
    }

    public boolean getHasLanguage() {
        return getOption(64);
    }

    @Override // com.adobe.xmp.options.Options
    public int getValidOptions() {
        return -2147475470;
    }

    public boolean isArray() {
        return getOption(QuickStepContract.SYSUI_STATE_STATUS_BAR_KEYGUARD_SHOWING_OCCLUDED);
    }

    public boolean isArrayAltText() {
        return getOption(QuickStepContract.SYSUI_STATE_TRACING_ENABLED);
    }

    public boolean isArrayAlternate() {
        return getOption(QuickStepContract.SYSUI_STATE_QUICK_SETTINGS_EXPANDED);
    }

    public boolean isCompositeProperty() {
        return (this.options & 768) > 0;
    }

    public boolean isSchemaNode() {
        return getOption(RecyclerView.UNDEFINED_DURATION);
    }

    public boolean isStruct() {
        return getOption(256);
    }

    public void mergeWith(PropertyOptions propertyOptions) throws XMPException {
        if (propertyOptions != null) {
            int i = propertyOptions.options | this.options;
            assertOptionsValid(i);
            this.options = i;
        }
    }

    public PropertyOptions(int i) throws XMPException {
        super(i);
    }
}
