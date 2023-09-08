package com.marchtech.SimpleMenu;

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
import com.marchtech.SimpleMenu.helpers.Placement;

@DesignerComponent(version = 1, description = "Extension to help you create menu.", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = Icon.ICON)
@SimpleObject(external = true)
public class SimpleMenu extends AndroidNonvisibleComponent {
    private final Activity activity;
    private final Form form;

    private RelativeLayout rLayout;
    private RelativeLayout.LayoutParams componentParams;
    private RelativeLayout.LayoutParams placedComponentParams;
    private RelativeLayout.LayoutParams secondComponentParams;

    private ViewGroup rParent = null;
    private ViewGroup.LayoutParams lParams;

    private int alignmentH;
    private int alignmentV;

    private FutureTask<Void> lastTask = null;

    public SimpleMenu(ComponentContainer container) {
        super(container.$form());
        activity = container.$context();
        form = container.$form();

        rLayout = new RelativeLayout(container.$context());
        rLayout.setId(999);

        lParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rLayout.setLayoutParams(lParams);
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
            componentParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
        else
            componentParams = new RelativeLayout.LayoutParams(layout.getView().getLayoutParams().width,
                    layout.getView().getLayoutParams().height);

        if (layout instanceof HVArrangement || layout instanceof TableArrangement) {
            switch (alignmentH) {
                case 1:
                    switch (alignmentV) {
                        case 1:
                            componentParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            componentParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                            break;

                        case 2:
                            componentParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            componentParams.addRule(RelativeLayout.CENTER_VERTICAL);
                            break;

                        case 3:
                            componentParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            componentParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                            break;
                    }
                    break;

                case 2:
                    switch (alignmentV) {
                        case 1:
                            componentParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                            componentParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                            break;

                        case 2:
                            componentParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                            componentParams.addRule(RelativeLayout.CENTER_VERTICAL);
                            break;

                        case 3:
                            componentParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                            componentParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                            break;
                    }
                    break;

                case 3:
                    switch (alignmentV) {
                        case 1:
                            componentParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                            componentParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                            break;

                        case 2:
                            componentParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                            break;

                        case 3:
                            componentParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                            componentParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
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

                    if (rParent == null) {
                        layout.getView().setLayoutParams(componentParams);
                        rLayout.addView(layout.getView(), 0, componentParams);

                        final ViewGroup prevRootParent = (ViewGroup) rLayout.getParent();
                        if (prevRootParent != null)
                            prevRootParent.removeView(rLayout);

                        parent.addView(rLayout, 0, componentParams);
                        rParent = parent;
                    } else {
                        /*
                         * parent.removeView(layout.getView());
                         * layout.getView().setLayoutParams(componentParams);
                         * rLayout.addView(layout.getView(), componentParams);
                         */
                    }

                    rLayout.invalidate();
                    rLayout.requestLayout();
                    rParent.invalidate();
                    rParent.requestLayout();
                }
            });
        }
    }

    @SimpleFunction(description = "To add view on parent layout.")
    public void Add(final AndroidViewComponent component, final Placement placement) {
        final ViewGroup parent = (ViewGroup) component.getView().getParent();
        if (parent != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (rParent != null) {
                        if (component.getView().getId() < 1)
                            component.getView().setId(View.generateViewId());

                        secondComponentParams = createLayoutParams(component, placement);
                        parent.removeView(component.getView());
                        component.getView().setLayoutParams(secondComponentParams);
                        rLayout.addView(component.getView(), 0, secondComponentParams);

                        final ViewGroup prevRootParent = (ViewGroup) rLayout.getParent();
                        if (prevRootParent != null)
                            prevRootParent.removeView(rLayout);

                        parent.addView(rLayout, 0, lParams);
                        rParent = parent;

                        rLayout.invalidate();
                        rLayout.requestLayout();
                        rParent.invalidate();
                        rParent.requestLayout();
                    }
                }
            });
        }
    }

    private RelativeLayout.LayoutParams createLayoutParams(AndroidViewComponent component, Placement placement) {
        if (component.getView().getWidth() == ViewGroup.LayoutParams.MATCH_PARENT
                && component.getView().getHeight() == ViewGroup.LayoutParams.MATCH_PARENT)
            placedComponentParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
        else
            placedComponentParams = new RelativeLayout.LayoutParams(component.getView().getLayoutParams().width,
                    component.getView().getLayoutParams().height);

        if (placement == Placement.LEFT_TOP) {
            placedComponentParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            placedComponentParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        } else if (placement == Placement.LEFT_CENTER) {
            placedComponentParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            placedComponentParams.addRule(RelativeLayout.CENTER_VERTICAL);
        } else if (placement == Placement.LEFT_BOTTOM) {
            placedComponentParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            placedComponentParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        } else if (placement == Placement.CENTER_TOP) {
            placedComponentParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            placedComponentParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        } else if (placement == Placement.CENTER)
            placedComponentParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        else if (placement == Placement.CENTER_BOTTOM) {
            placedComponentParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            placedComponentParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        } else if (placement == Placement.RIGHT_TOP) {
            placedComponentParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            placedComponentParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        } else if (placement == Placement.RIGHT_CENTER) {
            placedComponentParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            placedComponentParams.addRule(RelativeLayout.CENTER_VERTICAL);
        } else if (placement == Placement.RIGHT_BOTTOM) {
            placedComponentParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            placedComponentParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }

        return placedComponentParams;
    }
}
