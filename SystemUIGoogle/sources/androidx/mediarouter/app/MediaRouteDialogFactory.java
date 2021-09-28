package androidx.mediarouter.app;
/* loaded from: classes.dex */
public class MediaRouteDialogFactory {
    private static final MediaRouteDialogFactory sDefault = new MediaRouteDialogFactory();

    public static MediaRouteDialogFactory getDefault() {
        return sDefault;
    }

    public MediaRouteChooserDialogFragment onCreateChooserDialogFragment() {
        return new MediaRouteChooserDialogFragment();
    }

    public MediaRouteControllerDialogFragment onCreateControllerDialogFragment() {
        return new MediaRouteControllerDialogFragment();
    }
}
