package com.android.systemui.util;

import android.content.Context;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/* loaded from: classes2.dex */
public class InjectionInflationController {
    private final ViewInstanceCreator.Factory mViewInstanceCreatorFactory;
    private final ArrayMap<String, Method> mInjectionMap = new ArrayMap<>();
    private final LayoutInflater.Factory2 mFactory = new InjectionFactory();

    /* loaded from: classes2.dex */
    public interface ViewInstanceCreator {

        /* loaded from: classes2.dex */
        public interface Factory {
            ViewInstanceCreator build(Context context, AttributeSet attributeSet);
        }

        NotificationStackScrollLayout createNotificationStackScrollLayout();
    }

    public InjectionInflationController(ViewInstanceCreator.Factory factory) {
        this.mViewInstanceCreatorFactory = factory;
        initInjectionMap();
    }

    public LayoutInflater injectable(LayoutInflater layoutInflater) {
        LayoutInflater cloneInContext = layoutInflater.cloneInContext(layoutInflater.getContext());
        cloneInContext.setPrivateFactory(this.mFactory);
        return cloneInContext;
    }

    private void initInjectionMap() {
        Method[] declaredMethods = ViewInstanceCreator.class.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (View.class.isAssignableFrom(method.getReturnType()) && (method.getModifiers() & 1) != 0) {
                this.mInjectionMap.put(method.getReturnType().getName(), method);
            }
        }
    }

    /* loaded from: classes2.dex */
    private class InjectionFactory implements LayoutInflater.Factory2 {
        private InjectionFactory() {
        }

        @Override // android.view.LayoutInflater.Factory
        public View onCreateView(String str, Context context, AttributeSet attributeSet) {
            Method method = (Method) InjectionInflationController.this.mInjectionMap.get(str);
            if (method == null) {
                return null;
            }
            try {
                return (View) method.invoke(InjectionInflationController.this.mViewInstanceCreatorFactory.build(context, attributeSet), new Object[0]);
            } catch (IllegalAccessException e) {
                throw new InflateException("Could not inflate " + str, e);
            } catch (InvocationTargetException e2) {
                throw new InflateException("Could not inflate " + str, e2);
            }
        }

        @Override // android.view.LayoutInflater.Factory2
        public View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
            return onCreateView(str, context, attributeSet);
        }
    }
}
