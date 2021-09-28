package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.view.ViewGroup;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.systemui.Dependency;
import com.android.systemui.Dumpable;
import com.android.systemui.R$dimen;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.StatusIconDisplayable;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.StatusBarIconList;
import com.android.systemui.statusbar.phone.StatusBarSignalPolicy;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.tuner.TunerService;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class StatusBarIconControllerImpl extends StatusBarIconList implements TunerService.Tunable, ConfigurationController.ConfigurationListener, Dumpable, CommandQueue.Callbacks, StatusBarIconController, DemoMode {
    private Context mContext;
    private final ArrayList<StatusBarIconController.IconManager> mIconGroups = new ArrayList<>();
    private final ArraySet<String> mIconHideList = new ArraySet<>();

    private void loadDimens() {
    }

    public StatusBarIconControllerImpl(Context context, CommandQueue commandQueue, DemoModeController demoModeController) {
        super(context.getResources().getStringArray(17236092));
        ((ConfigurationController) Dependency.get(ConfigurationController.class)).addCallback(this);
        this.mContext = context;
        loadDimens();
        commandQueue.addCallback((CommandQueue.Callbacks) this);
        ((TunerService) Dependency.get(TunerService.class)).addTunable(this, "icon_blacklist");
        demoModeController.addCallback((DemoMode) this);
    }

    @Override // com.android.systemui.statusbar.phone.StatusBarIconController
    public void addIconGroup(StatusBarIconController.IconManager iconManager) {
        this.mIconGroups.add(iconManager);
        ArrayList<StatusBarIconList.Slot> slots = getSlots();
        for (int i = 0; i < slots.size(); i++) {
            StatusBarIconList.Slot slot = slots.get(i);
            List<StatusBarIconHolder> holderListInViewOrder = slot.getHolderListInViewOrder();
            boolean contains = this.mIconHideList.contains(slot.getName());
            for (StatusBarIconHolder statusBarIconHolder : holderListInViewOrder) {
                statusBarIconHolder.getTag();
                iconManager.onIconAdded(getViewIndex(getSlotIndex(slot.getName()), statusBarIconHolder.getTag()), slot.getName(), contains, statusBarIconHolder);
            }
        }
    }

    @Override // com.android.systemui.statusbar.phone.StatusBarIconController
    public void removeIconGroup(StatusBarIconController.IconManager iconManager) {
        iconManager.destroy();
        this.mIconGroups.remove(iconManager);
    }

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public void onTuningChanged(String str, String str2) {
        if ("icon_blacklist".equals(str)) {
            this.mIconHideList.clear();
            this.mIconHideList.addAll((ArraySet<? extends String>) StatusBarIconController.getIconHideList(this.mContext, str2));
            ArrayList<StatusBarIconList.Slot> slots = getSlots();
            ArrayMap arrayMap = new ArrayMap();
            for (int size = slots.size() - 1; size >= 0; size--) {
                StatusBarIconList.Slot slot = slots.get(size);
                arrayMap.put(slot, slot.getHolderList());
                removeAllIconsForSlot(slot.getName());
            }
            for (int i = 0; i < slots.size(); i++) {
                StatusBarIconList.Slot slot2 = slots.get(i);
                List<StatusBarIconHolder> list = (List) arrayMap.get(slot2);
                if (list != null) {
                    for (StatusBarIconHolder statusBarIconHolder : list) {
                        setIcon(getSlotIndex(slot2.getName()), statusBarIconHolder);
                    }
                }
            }
        }
    }

    private void addSystemIcon(int i, StatusBarIconHolder statusBarIconHolder) {
        String slotName = getSlotName(i);
        this.mIconGroups.forEach(new Consumer(getViewIndex(i, statusBarIconHolder.getTag()), slotName, this.mIconHideList.contains(slotName), statusBarIconHolder) { // from class: com.android.systemui.statusbar.phone.StatusBarIconControllerImpl$$ExternalSyntheticLambda5
            public final /* synthetic */ int f$0;
            public final /* synthetic */ String f$1;
            public final /* synthetic */ boolean f$2;
            public final /* synthetic */ StatusBarIconHolder f$3;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                StatusBarIconControllerImpl.m362$r8$lambda$b12G9OMDE_ZNq0VztAqWOQqXng(this.f$0, this.f$1, this.f$2, this.f$3, (StatusBarIconController.IconManager) obj);
            }
        });
    }

    @Override // com.android.systemui.statusbar.phone.StatusBarIconController
    public void setIcon(String str, int i, CharSequence charSequence) {
        int slotIndex = getSlotIndex(str);
        StatusBarIconHolder icon = getIcon(slotIndex, 0);
        if (icon == null) {
            setIcon(slotIndex, StatusBarIconHolder.fromIcon(new StatusBarIcon(UserHandle.SYSTEM, this.mContext.getPackageName(), Icon.createWithResource(this.mContext, i), 0, 0, charSequence)));
            return;
        }
        icon.getIcon().icon = Icon.createWithResource(this.mContext, i);
        icon.getIcon().contentDescription = charSequence;
        handleSet(slotIndex, icon);
    }

    @Override // com.android.systemui.statusbar.phone.StatusBarIconController
    public void setSignalIcon(String str, StatusBarSignalPolicy.WifiIconState wifiIconState) {
        int slotIndex = getSlotIndex(str);
        if (wifiIconState == null) {
            removeIcon(slotIndex, 0);
            return;
        }
        StatusBarIconHolder icon = getIcon(slotIndex, 0);
        if (icon == null) {
            setIcon(slotIndex, StatusBarIconHolder.fromWifiIconState(wifiIconState));
            return;
        }
        icon.setWifiState(wifiIconState);
        handleSet(slotIndex, icon);
    }

    @Override // com.android.systemui.statusbar.phone.StatusBarIconController
    public void setMobileIcons(String str, List<StatusBarSignalPolicy.MobileIconState> list) {
        StatusBarIconList.Slot slot = getSlot(str);
        int slotIndex = getSlotIndex(str);
        Collections.reverse(list);
        for (StatusBarSignalPolicy.MobileIconState mobileIconState : list) {
            StatusBarIconHolder holderForTag = slot.getHolderForTag(mobileIconState.subId);
            if (holderForTag == null) {
                setIcon(slotIndex, StatusBarIconHolder.fromMobileIconState(mobileIconState));
            } else {
                holderForTag.setMobileState(mobileIconState);
                handleSet(slotIndex, holderForTag);
            }
        }
    }

    @Override // com.android.systemui.statusbar.phone.StatusBarIconController
    public void setCallStrengthIcons(String str, List<StatusBarSignalPolicy.CallIndicatorIconState> list) {
        StatusBarIconList.Slot slot = getSlot(str);
        int slotIndex = getSlotIndex(str);
        Collections.reverse(list);
        for (StatusBarSignalPolicy.CallIndicatorIconState callIndicatorIconState : list) {
            if (!callIndicatorIconState.isNoCalling) {
                StatusBarIconHolder holderForTag = slot.getHolderForTag(callIndicatorIconState.subId);
                if (holderForTag == null) {
                    setIcon(slotIndex, StatusBarIconHolder.fromCallIndicatorState(this.mContext, callIndicatorIconState));
                } else {
                    holderForTag.setIcon(new StatusBarIcon(UserHandle.SYSTEM, this.mContext.getPackageName(), Icon.createWithResource(this.mContext, callIndicatorIconState.callStrengthResId), 0, 0, callIndicatorIconState.callStrengthDescription));
                    setIcon(slotIndex, holderForTag);
                }
            }
            setIconVisibility(str, !callIndicatorIconState.isNoCalling, callIndicatorIconState.subId);
        }
    }

    @Override // com.android.systemui.statusbar.phone.StatusBarIconController
    public void setNoCallingIcons(String str, List<StatusBarSignalPolicy.CallIndicatorIconState> list) {
        StatusBarIconList.Slot slot = getSlot(str);
        int slotIndex = getSlotIndex(str);
        Collections.reverse(list);
        for (StatusBarSignalPolicy.CallIndicatorIconState callIndicatorIconState : list) {
            if (callIndicatorIconState.isNoCalling) {
                StatusBarIconHolder holderForTag = slot.getHolderForTag(callIndicatorIconState.subId);
                if (holderForTag == null) {
                    setIcon(slotIndex, StatusBarIconHolder.fromCallIndicatorState(this.mContext, callIndicatorIconState));
                } else {
                    holderForTag.setIcon(new StatusBarIcon(UserHandle.SYSTEM, this.mContext.getPackageName(), Icon.createWithResource(this.mContext, callIndicatorIconState.noCallingResId), 0, 0, callIndicatorIconState.noCallingDescription));
                    setIcon(slotIndex, holderForTag);
                }
            }
            setIconVisibility(str, callIndicatorIconState.isNoCalling, callIndicatorIconState.subId);
        }
    }

    @Override // com.android.systemui.statusbar.phone.StatusBarIconController
    public void setExternalIcon(String str) {
        this.mIconGroups.forEach(new Consumer(getViewIndex(getSlotIndex(str), 0), this.mContext.getResources().getDimensionPixelSize(R$dimen.status_bar_icon_drawing_size)) { // from class: com.android.systemui.statusbar.phone.StatusBarIconControllerImpl$$ExternalSyntheticLambda2
            public final /* synthetic */ int f$0;
            public final /* synthetic */ int f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                StatusBarIconControllerImpl.m361$r8$lambda$SjcaqVTXkklfYAcI1KkIUEWDc(this.f$0, this.f$1, (StatusBarIconController.IconManager) obj);
            }
        });
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks, com.android.systemui.statusbar.phone.StatusBarIconController
    public void setIcon(String str, StatusBarIcon statusBarIcon) {
        setIcon(getSlotIndex(str), statusBarIcon);
    }

    private void setIcon(int i, StatusBarIcon statusBarIcon) {
        String slotName = getSlotName(i);
        if (statusBarIcon == null) {
            removeAllIconsForSlot(slotName);
        } else {
            setIcon(i, StatusBarIconHolder.fromIcon(statusBarIcon));
        }
    }

    @Override // com.android.systemui.statusbar.phone.StatusBarIconList
    public void setIcon(int i, StatusBarIconHolder statusBarIconHolder) {
        boolean z = getIcon(i, statusBarIconHolder.getTag()) == null;
        super.setIcon(i, statusBarIconHolder);
        if (z) {
            addSystemIcon(i, statusBarIconHolder);
        } else {
            handleSet(i, statusBarIconHolder);
        }
    }

    @Override // com.android.systemui.statusbar.phone.StatusBarIconController
    public void setIconVisibility(String str, boolean z) {
        setIconVisibility(str, z, 0);
    }

    public void setIconVisibility(String str, boolean z, int i) {
        int slotIndex = getSlotIndex(str);
        StatusBarIconHolder icon = getIcon(slotIndex, i);
        if (icon != null && icon.isVisible() != z) {
            icon.setVisible(z);
            handleSet(slotIndex, icon);
        }
    }

    @Override // com.android.systemui.statusbar.phone.StatusBarIconController
    public void setIconAccessibilityLiveRegion(String str, int i) {
        StatusBarIconList.Slot slot = getSlot(str);
        if (slot.hasIconsInSlot()) {
            int slotIndex = getSlotIndex(str);
            for (StatusBarIconHolder statusBarIconHolder : slot.getHolderListInViewOrder()) {
                this.mIconGroups.forEach(new Consumer(getViewIndex(slotIndex, statusBarIconHolder.getTag()), i) { // from class: com.android.systemui.statusbar.phone.StatusBarIconControllerImpl$$ExternalSyntheticLambda3
                    public final /* synthetic */ int f$0;
                    public final /* synthetic */ int f$1;

                    {
                        this.f$0 = r1;
                        this.f$1 = r2;
                    }

                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        StatusBarIconControllerImpl.$r8$lambda$wMi_p3Cej5dKTjP4GzpA4y9RMRA(this.f$0, this.f$1, (StatusBarIconController.IconManager) obj);
                    }
                });
            }
        }
    }

    public static /* synthetic */ void lambda$setIconAccessibilityLiveRegion$2(int i, int i2, StatusBarIconController.IconManager iconManager) {
        iconManager.mGroup.getChildAt(i).setAccessibilityLiveRegion(i2);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void removeIcon(String str) {
        removeAllIconsForSlot(str);
    }

    @Override // com.android.systemui.statusbar.phone.StatusBarIconController
    public void removeIcon(String str, int i) {
        removeIcon(getSlotIndex(str), i);
    }

    @Override // com.android.systemui.statusbar.phone.StatusBarIconController
    public void removeAllIconsForSlot(String str) {
        StatusBarIconList.Slot slot = getSlot(str);
        if (slot.hasIconsInSlot()) {
            int slotIndex = getSlotIndex(str);
            for (StatusBarIconHolder statusBarIconHolder : slot.getHolderListInViewOrder()) {
                int viewIndex = getViewIndex(slotIndex, statusBarIconHolder.getTag());
                slot.removeForTag(statusBarIconHolder.getTag());
                this.mIconGroups.forEach(new Consumer(viewIndex) { // from class: com.android.systemui.statusbar.phone.StatusBarIconControllerImpl$$ExternalSyntheticLambda0
                    public final /* synthetic */ int f$0;

                    {
                        this.f$0 = r1;
                    }

                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        StatusBarIconControllerImpl.$r8$lambda$EDQUSFuMBGcjW5d6bVTNmhcGgh8(this.f$0, (StatusBarIconController.IconManager) obj);
                    }
                });
            }
        }
    }

    @Override // com.android.systemui.statusbar.phone.StatusBarIconList
    public void removeIcon(int i, int i2) {
        if (getIcon(i, i2) != null) {
            super.removeIcon(i, i2);
            this.mIconGroups.forEach(new Consumer(getViewIndex(i, 0)) { // from class: com.android.systemui.statusbar.phone.StatusBarIconControllerImpl$$ExternalSyntheticLambda1
                public final /* synthetic */ int f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    StatusBarIconControllerImpl.$r8$lambda$q79MyD9WoRKXUKaySTsmNvDyju4(this.f$0, (StatusBarIconController.IconManager) obj);
                }
            });
        }
    }

    private void handleSet(int i, StatusBarIconHolder statusBarIconHolder) {
        this.mIconGroups.forEach(new Consumer(getViewIndex(i, statusBarIconHolder.getTag()), statusBarIconHolder) { // from class: com.android.systemui.statusbar.phone.StatusBarIconControllerImpl$$ExternalSyntheticLambda4
            public final /* synthetic */ int f$0;
            public final /* synthetic */ StatusBarIconHolder f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                StatusBarIconControllerImpl.$r8$lambda$Ji5N5OuDJIGJaELUsEPCXEwTP7g(this.f$0, this.f$1, (StatusBarIconController.IconManager) obj);
            }
        });
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("StatusBarIconController state:");
        Iterator<StatusBarIconController.IconManager> it = this.mIconGroups.iterator();
        while (it.hasNext()) {
            StatusBarIconController.IconManager next = it.next();
            if (next.shouldLog()) {
                ViewGroup viewGroup = next.mGroup;
                int childCount = viewGroup.getChildCount();
                printWriter.println("  icon views: " + childCount);
                for (int i = 0; i < childCount; i++) {
                    printWriter.println("    [" + i + "] icon=" + ((StatusIconDisplayable) viewGroup.getChildAt(i)));
                }
            }
        }
        super.dump(printWriter);
    }

    @Override // com.android.systemui.demomode.DemoModeCommandReceiver
    public void onDemoModeStarted() {
        Iterator<StatusBarIconController.IconManager> it = this.mIconGroups.iterator();
        while (it.hasNext()) {
            StatusBarIconController.IconManager next = it.next();
            if (next.isDemoable()) {
                next.onDemoModeStarted();
            }
        }
    }

    @Override // com.android.systemui.demomode.DemoModeCommandReceiver
    public void onDemoModeFinished() {
        Iterator<StatusBarIconController.IconManager> it = this.mIconGroups.iterator();
        while (it.hasNext()) {
            StatusBarIconController.IconManager next = it.next();
            if (next.isDemoable()) {
                next.onDemoModeFinished();
            }
        }
    }

    @Override // com.android.systemui.demomode.DemoModeCommandReceiver
    public void dispatchDemoCommand(String str, Bundle bundle) {
        Iterator<StatusBarIconController.IconManager> it = this.mIconGroups.iterator();
        while (it.hasNext()) {
            StatusBarIconController.IconManager next = it.next();
            if (next.isDemoable()) {
                next.dispatchDemoCommand(str, bundle);
            }
        }
    }

    @Override // com.android.systemui.demomode.DemoMode
    public List<String> demoCommands() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("status");
        return arrayList;
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onDensityOrFontScaleChanged() {
        loadDimens();
    }
}
