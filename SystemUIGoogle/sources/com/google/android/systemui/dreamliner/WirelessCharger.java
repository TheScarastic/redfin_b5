package com.google.android.systemui.dreamliner;

import android.os.Bundle;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public abstract class WirelessCharger {

    /* loaded from: classes2.dex */
    public interface AlignInfoListener {
        void onAlignInfoChanged(DockAlignInfo dockAlignInfo);
    }

    /* loaded from: classes2.dex */
    public interface ChallengeCallback {
        void onCallback(int i, ArrayList<Byte> arrayList);
    }

    /* loaded from: classes2.dex */
    public interface GetFanInformationCallback {
        void onCallback(int i, Bundle bundle);
    }

    /* loaded from: classes2.dex */
    public interface GetFanSimpleInformationCallback {
        void onCallback(int i, Bundle bundle);
    }

    /* loaded from: classes2.dex */
    public interface GetFeaturesCallback {
        void onCallback(int i, long j);
    }

    /* loaded from: classes2.dex */
    public interface GetInformationCallback {
        void onCallback(int i, DockInfo dockInfo);
    }

    /* loaded from: classes2.dex */
    public interface GetWpcAuthCertificateCallback {
        void onCallback(int i, ArrayList<Byte> arrayList);
    }

    /* loaded from: classes2.dex */
    public interface GetWpcAuthChallengeResponseCallback {
        void onCallback(int i, byte b, byte b2, byte b3, ArrayList<Byte> arrayList, ArrayList<Byte> arrayList2);
    }

    /* loaded from: classes2.dex */
    public interface GetWpcAuthDigestsCallback {
        void onCallback(int i, byte b, byte b2, ArrayList<byte[]> arrayList);
    }

    /* loaded from: classes2.dex */
    public interface IsDockPresentCallback {
        void onCallback(boolean z, byte b, byte b2, boolean z2, int i);
    }

    /* loaded from: classes2.dex */
    public interface KeyExchangeCallback {
        void onCallback(int i, byte b, ArrayList<Byte> arrayList);
    }

    /* loaded from: classes2.dex */
    public interface SetFanCallback {
        void onCallback(int i, Bundle bundle);
    }

    /* loaded from: classes2.dex */
    public interface SetFeaturesCallback {
        void onCallback(int i);
    }

    public abstract void asyncIsDockPresent(IsDockPresentCallback isDockPresentCallback);

    public abstract void challenge(byte b, byte[] bArr, ChallengeCallback challengeCallback);

    public abstract void getFanInformation(byte b, GetFanInformationCallback getFanInformationCallback);

    /* access modifiers changed from: package-private */
    public abstract int getFanLevel();

    public abstract void getFanSimpleInformation(byte b, GetFanSimpleInformationCallback getFanSimpleInformationCallback);

    public abstract void getFeatures(long j, GetFeaturesCallback getFeaturesCallback);

    public abstract void getInformation(GetInformationCallback getInformationCallback);

    public abstract void getWpcAuthCertificate(byte b, short s, short s2, GetWpcAuthCertificateCallback getWpcAuthCertificateCallback);

    public abstract void getWpcAuthChallengeResponse(byte b, byte[] bArr, GetWpcAuthChallengeResponseCallback getWpcAuthChallengeResponseCallback);

    public abstract void getWpcAuthDigests(byte b, GetWpcAuthDigestsCallback getWpcAuthDigestsCallback);

    public abstract void keyExchange(byte[] bArr, KeyExchangeCallback keyExchangeCallback);

    public abstract void registerAlignInfo(AlignInfoListener alignInfoListener);

    public abstract void setFan(byte b, byte b2, int i, SetFanCallback setFanCallback);

    public abstract void setFeatures(long j, long j2, SetFeaturesCallback setFeaturesCallback);
}
