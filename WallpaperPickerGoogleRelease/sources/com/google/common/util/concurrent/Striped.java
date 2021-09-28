package com.google.common.util.concurrent;
/* loaded from: classes.dex */
public abstract class Striped<L> {

    /* loaded from: classes.dex */
    public static class LargeLazyStriped<L> extends PowerOfTwoStriped<L> {
    }

    /* loaded from: classes.dex */
    public static abstract class PowerOfTwoStriped<L> extends Striped<L> {
    }

    /* loaded from: classes.dex */
    public static class SmallLazyStriped<L> extends PowerOfTwoStriped<L> {
    }

    private Striped() {
    }
}
