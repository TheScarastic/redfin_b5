package com.android.systemui.accessibility.floatingmenu;

import android.text.Annotation;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.view.View;
import com.android.systemui.accessibility.floatingmenu.AnnotationLinkSpan;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
/* loaded from: classes.dex */
class AnnotationLinkSpan extends ClickableSpan {
    private final Optional<View.OnClickListener> mClickListener;

    private AnnotationLinkSpan(View.OnClickListener onClickListener) {
        this.mClickListener = Optional.ofNullable(onClickListener);
    }

    @Override // android.text.style.ClickableSpan
    public void onClick(View view) {
        this.mClickListener.ifPresent(new Consumer(view) { // from class: com.android.systemui.accessibility.floatingmenu.AnnotationLinkSpan$$ExternalSyntheticLambda1
            public final /* synthetic */ View f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                AnnotationLinkSpan.$r8$lambda$DcGe6neWJqAeZyQAsmezlwDV2RA(this.f$0, (View.OnClickListener) obj);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public static CharSequence linkify(CharSequence charSequence, LinkInfo... linkInfoArr) {
        SpannableString spannableString = new SpannableString(charSequence);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(spannableString);
        Arrays.asList((Annotation[]) spannableString.getSpans(0, spannableString.length(), Annotation.class)).forEach(new Consumer(linkInfoArr, spannableStringBuilder, spannableString) { // from class: com.android.systemui.accessibility.floatingmenu.AnnotationLinkSpan$$ExternalSyntheticLambda2
            public final /* synthetic */ AnnotationLinkSpan.LinkInfo[] f$0;
            public final /* synthetic */ SpannableStringBuilder f$1;
            public final /* synthetic */ SpannableString f$2;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                AnnotationLinkSpan.m57$r8$lambda$8GFR7koENoK0EjJ1h3JTjmrJEY(this.f$0, this.f$1, this.f$2, (Annotation) obj);
            }
        });
        return spannableStringBuilder;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$linkify$4(LinkInfo[] linkInfoArr, SpannableStringBuilder spannableStringBuilder, SpannableString spannableString, Annotation annotation) {
        Arrays.asList(linkInfoArr).stream().filter(new Predicate(annotation.getValue()) { // from class: com.android.systemui.accessibility.floatingmenu.AnnotationLinkSpan$$ExternalSyntheticLambda4
            public final /* synthetic */ String f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return AnnotationLinkSpan.$r8$lambda$rM3U6RLHslGRzbG8XnxHRCRwaMc(this.f$0, (AnnotationLinkSpan.LinkInfo) obj);
            }
        }).findFirst().flatMap(AnnotationLinkSpan$$ExternalSyntheticLambda3.INSTANCE).ifPresent(new Consumer(spannableStringBuilder, spannableString, annotation) { // from class: com.android.systemui.accessibility.floatingmenu.AnnotationLinkSpan$$ExternalSyntheticLambda0
            public final /* synthetic */ SpannableStringBuilder f$0;
            public final /* synthetic */ SpannableString f$1;
            public final /* synthetic */ Annotation f$2;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                AnnotationLinkSpan.$r8$lambda$BnvWY0UNioG2XHqMxIApkCrxXYQ(this.f$0, this.f$1, this.f$2, (View.OnClickListener) obj);
            }
        });
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$linkify$1(String str, LinkInfo linkInfo) {
        return linkInfo.mAnnotation.isPresent() && ((String) linkInfo.mAnnotation.get()).equals(str);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$linkify$3(SpannableStringBuilder spannableStringBuilder, SpannableString spannableString, Annotation annotation, View.OnClickListener onClickListener) {
        AnnotationLinkSpan annotationLinkSpan = new AnnotationLinkSpan(onClickListener);
        spannableStringBuilder.setSpan(annotationLinkSpan, spannableString.getSpanStart(annotation), spannableString.getSpanEnd(annotation), spannableString.getSpanFlags(annotationLinkSpan));
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class LinkInfo {
        private final Optional<String> mAnnotation;
        private final Optional<View.OnClickListener> mListener;

        /* access modifiers changed from: package-private */
        public LinkInfo(String str, View.OnClickListener onClickListener) {
            this.mAnnotation = Optional.of(str);
            this.mListener = Optional.ofNullable(onClickListener);
        }
    }
}
