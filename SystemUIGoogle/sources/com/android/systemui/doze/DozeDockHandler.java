package com.android.systemui.doze;

import android.hardware.display.AmbientDisplayConfiguration;
import android.util.Log;
import com.android.systemui.dock.DockManager;
import com.android.systemui.doze.DozeMachine;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public class DozeDockHandler implements DozeMachine.Part {
    private static final boolean DEBUG = DozeService.DEBUG;
    private final AmbientDisplayConfiguration mConfig;
    private final DockManager mDockManager;
    private DozeMachine mMachine;
    private int mDockState = 0;
    private final DockEventListener mDockEventListener = new DockEventListener(this, null);

    /* access modifiers changed from: package-private */
    public DozeDockHandler(AmbientDisplayConfiguration ambientDisplayConfiguration, DockManager dockManager) {
        this.mConfig = ambientDisplayConfiguration;
        this.mDockManager = dockManager;
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public void setDozeMachine(DozeMachine dozeMachine) {
        this.mMachine = dozeMachine;
    }

    /* renamed from: com.android.systemui.doze.DozeDockHandler$1  reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$systemui$doze$DozeMachine$State;

        static {
            int[] iArr = new int[DozeMachine.State.values().length];
            $SwitchMap$com$android$systemui$doze$DozeMachine$State = iArr;
            try {
                iArr[DozeMachine.State.INITIALIZED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.FINISH.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public void transitionTo(DozeMachine.State state, DozeMachine.State state2) {
        int i = AnonymousClass1.$SwitchMap$com$android$systemui$doze$DozeMachine$State[state2.ordinal()];
        if (i == 1) {
            this.mDockEventListener.register();
        } else if (i == 2) {
            this.mDockEventListener.unregister();
        }
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public void dump(PrintWriter printWriter) {
        printWriter.println("DozeDockHandler:");
        printWriter.println(" dockState=" + this.mDockState);
    }

    /* loaded from: classes.dex */
    private class DockEventListener implements DockManager.DockEventListener {
        private boolean mRegistered;

        private DockEventListener() {
        }

        /* synthetic */ DockEventListener(DozeDockHandler dozeDockHandler, AnonymousClass1 r2) {
            this();
        }

        @Override // com.android.systemui.dock.DockManager.DockEventListener
        public void onEvent(int i) {
            DozeMachine.State state;
            if (DozeDockHandler.DEBUG) {
                Log.d("DozeDockHandler", "dock event = " + i);
            }
            if (DozeDockHandler.this.mDockState != i) {
                DozeDockHandler.this.mDockState = i;
                if (!isPulsing()) {
                    int i2 = DozeDockHandler.this.mDockState;
                    if (i2 != 0) {
                        if (i2 == 1) {
                            state = DozeMachine.State.DOZE_AOD_DOCKED;
                        } else if (i2 == 2) {
                            state = DozeMachine.State.DOZE;
                        } else {
                            return;
                        }
                    } else if (DozeDockHandler.this.mConfig.alwaysOnEnabled(-2)) {
                        state = DozeMachine.State.DOZE_AOD;
                    } else {
                        state = DozeMachine.State.DOZE;
                    }
                    DozeDockHandler.this.mMachine.requestState(state);
                }
            }
        }

        private boolean isPulsing() {
            DozeMachine.State state = DozeDockHandler.this.mMachine.getState();
            return state == DozeMachine.State.DOZE_REQUEST_PULSE || state == DozeMachine.State.DOZE_PULSING || state == DozeMachine.State.DOZE_PULSING_BRIGHT;
        }

        void register() {
            if (!this.mRegistered) {
                if (DozeDockHandler.this.mDockManager != null) {
                    DozeDockHandler.this.mDockManager.addListener(this);
                }
                this.mRegistered = true;
            }
        }

        void unregister() {
            if (this.mRegistered) {
                if (DozeDockHandler.this.mDockManager != null) {
                    DozeDockHandler.this.mDockManager.removeListener(this);
                }
                this.mRegistered = false;
            }
        }
    }
}
