package com.bumptech.glide.request.transition;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.request.transition.Transition;
/* loaded from: classes.dex */
public class NoTransition<R> implements Transition<R> {
    public static final NoTransition<?> NO_ANIMATION = new NoTransition<>();
    public static final TransitionFactory<?> NO_ANIMATION_FACTORY = new NoAnimationFactory();

    /* loaded from: classes.dex */
    public static class NoAnimationFactory<R> implements TransitionFactory<R> {
        @Override // com.bumptech.glide.request.transition.TransitionFactory
        public Transition<R> build(DataSource dataSource, boolean z) {
            return NoTransition.NO_ANIMATION;
        }
    }

    @Override // com.bumptech.glide.request.transition.Transition
    public boolean transition(Object obj, Transition.ViewAdapter viewAdapter) {
        return false;
    }
}
