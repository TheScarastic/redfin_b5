package kotlin.sequences;

import androidx.appcompat.R$id;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public class SequencesKt___SequencesJvmKt extends R$id {
    @NotNull
    public static final <T> Sequence<T> asSequence(@NotNull Iterator<? extends T> it) {
        SequencesKt__SequencesKt$asSequence$$inlined$Sequence$1 sequencesKt__SequencesKt$asSequence$$inlined$Sequence$1 = new Sequence<T>(it) { // from class: kotlin.sequences.SequencesKt__SequencesKt$asSequence$$inlined$Sequence$1
            public final /* synthetic */ Iterator $this_asSequence$inlined;

            {
                this.$this_asSequence$inlined = r1;
            }

            @Override // kotlin.sequences.Sequence
            @NotNull
            public Iterator<T> iterator() {
                return this.$this_asSequence$inlined;
            }
        };
        return sequencesKt__SequencesKt$asSequence$$inlined$Sequence$1 instanceof ConstrainedOnceSequence ? sequencesKt__SequencesKt$asSequence$$inlined$Sequence$1 : new ConstrainedOnceSequence(sequencesKt__SequencesKt$asSequence$$inlined$Sequence$1);
    }
}
