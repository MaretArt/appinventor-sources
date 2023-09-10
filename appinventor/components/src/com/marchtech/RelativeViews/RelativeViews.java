package com.marchtech.RelativeViews;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.concurrent.FutureTask;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.AsynchUtil;
import com.marchtech.Icon;

@DesignerComponent(version = 1, description = "Extension to help you work with relative layout.", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = Icon.ICON)
@SimpleObject(external = true)
public class RelativeViews extends AndroidNonvisibleComponent {
    private final Activity activity;
    private final Form form;

    private RelativeLayout rootLayout;
    private RelativeLayout.LayoutParams layoutParams;

    private ViewGroup rootParent = null;
    private ViewGroup.LayoutParams rootParams;

    private int alignmentH;
    private int alignmentV;

    private FutureTask<Void> lastTask = null;

    public RelativeViews(ComponentContainer container) {
        super(container.$form());
        activity = container.$context();
        form = container.$form();

        rootLayout = new RelativeLayout(container.$context());
        rootLayout.setId(View.generateViewId());

        rootParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        rootLayout.setLayoutParams(rootParams);
    }

    @SimpleEvent
    public void Test(Object value1, Object value2) {
        EventDispatcher.dispatchEvent(this, "Test", value1, value2);
    }

    @SimpleFunction(description = "To initialize component.")
    public void Initialize(final AndroidViewComponent layout) {
        alignmentH = form.AlignHorizontal();
        alignmentV = form.AlignVertical();

        if (layout.getView().getWidth() == ViewGroup.LayoutParams.MATCH_PARENT
                && layout.getView().getHeight() == ViewGroup.LayoutParams.MATCH_PARENT)
            layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
        else
            layoutParams = new RelativeLayout.LayoutParams(layout.getView().getLayoutParams().width,
                    layout.getView().getLayoutParams().height);

        if (layout instanceof HVArrangement || layout instanceof TableArrangement) {
            switch (alignmentH) {
                case 1:
                    switch (alignmentV) {
                        case 1:
                            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                            break;

                        case 2:
                            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                            break;

                        case 3:
                            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                            break;
                    }
                    break;

                case 2:
                    switch (alignmentV) {
                        case 1:
                            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                            break;

                        case 2:
                            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                            break;

                        case 3:
                            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                            break;
                    }
                    break;

                case 3:
                    switch (alignmentV) {
                        case 1:
                            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                            break;

                        case 2:
                            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                            break;

                        case 3:
                            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                            break;
                    }
                    break;
            }
        }

        lastTask = new FutureTask<Void>(new Runnable() {
            @Override
            public void run() {
                initialize(layout);
            }
        }, null);

        AsynchUtil.runAsynchronously(lastTask);
    }

    private void initialize(final AndroidViewComponent layout) {
        final ViewGroup parent = (ViewGroup) layout.getView().getParent();
        if (parent != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (layout.getView().getId() < 1)
                        layout.getView().setId(View.generateViewId());

                    if (rootParent == null) {
                        final ViewGroup prevParent = parent;
                        prevParent.removeView(layout.getView());
                        parent.removeAllViews();
                        layout.getView().setLayoutParams(layoutParams);
                        rootLayout.addView(prevParent, 0, layoutParams);
                        rootLayout.addView(layout.getView(), prevParent.getChildCount(), layoutParams);

                        final ViewGroup prevRootParent = (ViewGroup) rootLayout.getParent();
                        if (prevRootParent != null)
                            prevRootParent.removeView(rootLayout);

                        parent.addView(rootLayout, 0, rootParams);
                        rootParent = parent;
                    } else {
                        /*
                         * parent.removeView(layout.getView());
                         * layout.getView().setLayoutParams(layoutParams);
                         * rootLayout.addView(layout.getView(), layoutParams);
                         */
                    }

                    rootLayout.invalidate();
                    rootLayout.requestLayout();
                    rootParent.invalidate();
                    rootParent.requestLayout();
                }
            });
        }
    }
}
