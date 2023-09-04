package com.marchtech.DynamicComponents;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.errors.YailRuntimeError;
import com.google.appinventor.components.runtime.util.YailDictionary;
import com.google.appinventor.components.runtime.util.YailList;
import com.marchtech.Icon;

@DesignerComponent( version = 1,
                    description = "Extension to help you creates any component.",
                    category = ComponentCategory.EXTENSION,
                    nonVisible = true,
                    iconName = Icon.ICON)
@SimpleObject(external = true)
public class DynamicComponents extends AndroidNonvisibleComponent {
    private final String TAG = "DynamicComponents";
    private final String BASE = "com.google.appinventor.components.runtime.";

    private final HashMap<String, Component> COMPONENTS = new HashMap<String, Component>();
    private final HashMap<Component, String> COMPONENT_IDS = new HashMap<Component, String>();

    private final Util UTIL = new Util();

    private boolean onUiThread = false;

    private ArrayList<Listeners> componentListeners = new ArrayList<Listeners>();

    private Object lastUseId = "";
    private JSONArray propArray = new JSONArray();

    public DynamicComponents(ComponentContainer container) {
        super(container.$form());
    }

    interface Listeners {
        public void onCreation(Component component, String id);
    }

    class Util {
        public boolean exists(Component component) {
            return COMPONENTS.containsValue(component);
        }

        public boolean exists(String id) {
            return COMPONENTS.containsKey(id);
        }

        public String getClassName(Object componentName) {
            String regex = "[^.$@a-zA-Z0-9]";
            String nameString = componentName.toString().replaceAll(regex, "");

            if (componentName instanceof String && nameString.contains(".")) return nameString;
            else if (componentName instanceof String) return BASE + nameString;
            else if (componentName instanceof Component) return componentName.getClass().getName().replaceAll(regex, "");
            else throw new YailRuntimeError("Component is invalid", TAG);
        }

        public Method getMethod(Method[] methods, String name, int parameterCount) {
            String regex = "[^a-zA-Z0-9]";
            name = name.replaceAll(regex, "");
            for (Method method : methods) {
                int paramCount = method.getParameterTypes().length;
                if (method.getName().equals(name) && paramCount == parameterCount) return method;
            }

            return null;
        }

        public void notifyListenerOfCreation(Component component, String id) {
            for (Listeners listener : componentListeners) {
                listener.onCreation(component, id);
            }
        }

        public void newInstance(Constructor<?> constructor, String id, AndroidViewComponent input) {
            Component mComponent = null;
            try {
                mComponent = (Component) constructor.newInstance((ComponentContainer) input);
            } catch (Exception e) {
                throw new YailRuntimeError(e.getMessage(), TAG);
            } finally {
                if (!isEmptyOrNull(mComponent)) {
                    String mClassName = mComponent.getClass().getSimpleName();
                    if (mClassName == "ImageSprite" || mClassName == "Sprite") Invoke(mComponent, "Initialize", new YailList());

                    COMPONENT_IDS.put(mComponent, id);
                    COMPONENTS.put(id, mComponent);
                    this.notifyListenerOfCreation(mComponent, id);
                    ComponentCreated(mComponent, id, mClassName);
                }
            }
        }

        public void parse(String id, JSONObject json) {
            JSONObject data = new JSONObject(json.toString());
            data.remove("components");

            if (!"".equals(id)) data.put("in", id);

            propArray.put(data);

            if (json.has("components")) {
                for (int i = 0; i < json.getJSONArray("components").length(); i++) {
                    this.parse(data.optString("id", ""), json.getJSONArray("components").getJSONObject(i));
                }
            }
        }
    }

    public boolean isEmptyOrNull(Object item) {
        if (item instanceof String) {
            String mItem = item.toString();
            mItem = mItem.replace(" ", "");
            return mItem.isEmpty();
        }

        return item == null;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_CHOICES, editorArgs = { "Main", "UI" }, defaultValue = "UI")
    @SimpleProperty(userVisible = false)
    public void Thread(String thread) {
        onUiThread = (thread == "UI");
    }

    @SimpleEvent(description = "Occurs when component has been created.")
    public void ComponentCreated(final Component component, final String id, final String type) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                EventDispatcher.dispatchEvent(DynamicComponents.this, "ComponentCreated", component, id, type);
            }
        });
    }

    @SimpleEvent(description = "Occurs when schema has/mostly finished component creation.")
    public void SchemaCreated(final String name, final YailList parameters) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                EventDispatcher.dispatchEvent(DynamicComponents.this, "SchemaCreated", name, parameters);
            }
        });
    }

    @SimpleEvent(description = "Occurs when all components has been removed.")
    public void AllRemoved() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                EventDispatcher.dispatchEvent(DynamicComponents.this, "AllRemoved");
            }
        });
    }

    @SimpleFunction(description = "Assign a new ID to a previously created dynamic component.")
    public void ChangeId(String id, String newId) {
        if (UTIL.exists(id) && !UTIL.exists(newId)) {
            for (String mId : GetAllIds().toStringArray()) {
                if (mId.contains(id)) {
                    Component mComponent = (Component) GetComponent(mId);
                    String mReplacement = mId.replace(id, newId);
                    COMPONENT_IDS.remove(mComponent);
                    COMPONENTS.put(mReplacement, COMPONENTS.remove(mId));
                    COMPONENT_IDS.put(mComponent, mReplacement);
                }
            }
        } else throw new YailRuntimeError("The id you used is either not a dynamic component, or the id you've to replace the old ID is already taken.", TAG);
    }

    @SimpleFunction(description = "Create a new dynamic component.")
    public void Create(final AndroidViewComponent in, Object component, final String id) throws Exception {
        if (!COMPONENTS.containsKey(id)) {
            lastUseId = id;

            String mClassName = UTIL.getClassName(component);
            Class<?> mClass = Class.forName(mClassName);
            final Constructor<?> mConstructor = mClass.getConstructor(ComponentContainer.class);

            if (onUiThread) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        UTIL.newInstance(mConstructor, id, in);
                    }
                });
            } else UTIL.newInstance(mConstructor, id, in);
        } else throw new YailRuntimeError("Expected a unique id, got '" + id + "'.", TAG);
    }

    @SimpleFunction(description = "Generate a random id to create a component.")
    public String GenerateID() {
        String id = "";
        do {
            id = UUID.randomUUID().toString();
        } while (UTIL.exists(id));

        return id;
    }

    @SimpleFunction(description = "Returns the component associated with the specified ID.")
    public Object GetComponent(String id) {
        return COMPONENTS.get(id);
    }

    @SimpleFunction(description = "Get meta data about the specified component.")
    public YailDictionary GetComponentMeta(Component component) {
        Class<?> mClass = component.getClass();
        DesignerComponent mDesigner = mClass.getAnnotation(DesignerComponent.class);
        boolean mHasDesigner = !isEmptyOrNull(mDesigner);
        SimpleObject mObject = mClass.getAnnotation(SimpleObject.class);
        boolean mHasObject = !isEmptyOrNull(mObject);
        YailDictionary meta = new YailDictionary();

        if (mHasDesigner && mHasObject) {
            meta.put("androidMinSdk", mDesigner.androidMinSdk());
            meta.put("category", mDesigner.category());
            meta.put("dateBuilt", mDesigner.dateBuilt());
            meta.put("description", mDesigner.description());
            meta.put("designerHelpDescription", mDesigner.designerHelpDescription());
            meta.put("external", mObject.external());
            meta.put("helpUrl", mDesigner.helpUrl());
            meta.put("iconName", mDesigner.iconName());
            meta.put("nonVisible", mDesigner.nonVisible());
            meta.put("package", mClass.getName());
            meta.put("showOnPalette", mDesigner.showOnPalette());
            meta.put("type", mClass.getSimpleName());
            meta.put("version", mDesigner.version());
            meta.put("versionName", mDesigner.versionName());
        } else if (!mHasDesigner && mHasObject) {
            meta.put("external", mObject.external());
            meta.put("package", mClass.getName());
            meta.put("type", mClass.getSimpleName());
        } else {
            meta.put("package", mClass.getName());
            meta.put("type", mClass.getSimpleName());
        }

        return meta;
    }

    @SimpleFunction(description = "Get meta data about events for the specified component.")
    public YailDictionary GetEventMeta(Component component) {
        Method[] methods = component.getClass().getMethods();
        YailDictionary meta = new YailDictionary();

        for (Method method : methods) {
            SimpleEvent event = method.getAnnotation(SimpleEvent.class);
            boolean isDeprecated = !isEmptyOrNull(method.getAnnotation(Deprecated.class));
            String name = method.getName();
            YailDictionary metaEvent = new YailDictionary();

            if (!isEmptyOrNull(event)) {
                metaEvent.put("description", event.description());
                metaEvent.put("isDeprecated", isDeprecated);
                metaEvent.put("userVisible", event.userVisible());
            } else metaEvent.put("isDeprecated", isDeprecated);

            meta.put(name, metaEvent);
        }

        return meta;
    }

    @SimpleFunction(description = "Get meta data about functions for the specified component.")
    public YailDictionary GetFunctionMeta(Component component) {
        Method[] methods = component.getClass().getMethods();
        YailDictionary meta = new YailDictionary();

        for (Method method : methods) {
            SimpleFunction function = method.getAnnotation(SimpleFunction.class);
            boolean isDeprecated = !isEmptyOrNull(method.getAnnotation(Deprecated.class));
            String name = method.getName();
            YailDictionary metaFunction = new YailDictionary();

            if (!isEmptyOrNull(function)) {
                metaFunction.put("description", function.description());
                metaFunction.put("isDeprecated", isDeprecated);
                metaFunction.put("userVisible", function.userVisible());
            } else metaFunction.put("isDeprecated", isDeprecated);

            meta.put(name, metaFunction);
        }

        return meta;
    }

    @SimpleFunction(description = "Get meta data about properties for the specified component.")
    public YailDictionary GetPropertyMeta(Component component) {
        Method[] methods = component.getClass().getMethods();
        YailDictionary meta = new YailDictionary();

        for (Method method : methods) {
            DesignerProperty designer = method.getAnnotation(DesignerProperty.class);
            boolean hasDesigner = !isEmptyOrNull(designer);
            SimpleProperty property = method.getAnnotation(SimpleProperty.class);
            boolean hasProperty = !isEmptyOrNull(property);
            boolean isDeprecated = !isEmptyOrNull(method.getAnnotation(Deprecated.class));
            String name = method.getName();
            Object value = Invoke(component, name, new YailList());
            YailDictionary metaProperty = new YailDictionary();

            if (hasProperty) {
                metaProperty.put("description", property.description());
                metaProperty.put("category", property.category());

                if (hasDesigner) {
                    YailDictionary metaDesigner = new YailDictionary();
                    metaDesigner.put("defaultValue", designer.defaultValue());
                    metaDesigner.put("editorArgs", designer.editorArgs());
                    metaDesigner.put("editorType", designer.editorType());
                    
                    metaProperty.put("designer", metaDesigner);
                }

                metaProperty.put("isDeprecated", isDeprecated);
                metaProperty.put("isDesignerProperty", hasDesigner);
                metaProperty.put("userVisible", property.userVisible());
                metaProperty.put("value", value);

                meta.put(name, metaProperty);
            }
        }

        return meta;
    }

    @SimpleFunction(description = "Return id of the specified component.")
    public String GetId(Component component) {
        if (!isEmptyOrNull(component) || COMPONENT_IDS.containsKey(component)) return COMPONENT_IDS.get(component);

        return "";
    }

    @SimpleFunction(description = "Returns all IDs of created components.")
    public YailList GetAllIds() {
        Set<String> mKeys = COMPONENTS.keySet();
        return YailList.makeList(mKeys);
    }

    @SimpleFunction(description = "Return the position of the specified component according to its parent view. Index begins at one.")
    public int GetOrder(AndroidViewComponent component) {
        View mComponent = (View) component.getView();
        int index = 0;
        ViewGroup mParent = (!isEmptyOrNull(mComponent) ? (ViewGroup) mComponent.getParent() : null);

        if (!isEmptyOrNull(mComponent) && !isEmptyOrNull(mParent)) index = mParent.indexOfChild(mComponent) + 1;
        return index;
    }

    @SimpleFunction(description = "Get properties value.")
    public Object GetProperty(Component component, String name) {
        return Invoke(component, name, YailList.makeEmptyList());
    }

    @SimpleFunction(description = "Invokes a method with parameters.")
    public Object Invoke(Component component, String name, YailList parameters) {
        if (!isEmptyOrNull(component)) {
            Object invokeMethod = null;
            Method[] methods = component.getClass().getMethods();

            try {
                Object[] params = parameters.toArray();
                Method method = UTIL.getMethod(methods, name, params.length);

                Class<?>[] requestedMethodParams = method.getParameterTypes();
                ArrayList<Object> paramsArray = new ArrayList<Object>();
                for (int i = 0; i < requestedMethodParams.length; i++) {
                    if ("int".equals(requestedMethodParams[i].getName())) paramsArray.add(Integer.parseInt(params[i].toString()));
                    else if ("float".equals(requestedMethodParams[i].getName())) paramsArray.add(Float.parseFloat(params[i].toString()));
                    else if ("double".equals(requestedMethodParams[i].getName())) paramsArray.add(Double.parseDouble(params[i].toString()));
                    else if ("java.lang.String".equals(requestedMethodParams[i].getName())) paramsArray.add(params[i].toString());
                    else if ("boolean".equals(requestedMethodParams[i].getName())) paramsArray.add(Boolean.parseBoolean(params[i].toString()));
                    else paramsArray.add(params[i]);
                }

                invokeMethod = method.invoke(component, paramsArray.toArray());
            } catch (Exception e) {
                throw new YailRuntimeError(e.getMessage(), TAG);
            } finally {
                if (!isEmptyOrNull(invokeMethod)) return invokeMethod;
                else return "";
            }
        } else throw new YailRuntimeError("Component can't be null.", TAG);
    }

    @SimpleFunction(description = "Return if specified component was created by DynamicComponents.")
    public boolean isDynamic(Component component) {
        return COMPONENTS.containsKey(component);
    }

    @SimpleFunction(description = "Return last used id.")
    public Object LastUsedId() {
        return lastUseId;
    }

    @SimpleFunction(description = "Moves the specified component to specified view.")
    public void Move(AndroidViewComponent layout, AndroidViewComponent component) {
        View mComponent = (View) component.getView();
        ViewGroup mParent = (!isEmptyOrNull(mComponent) ? (ViewGroup) mComponent.getParent() : null);

        mParent.removeView(mComponent);

        ViewGroup mLayout = (ViewGroup) layout.getView();
        ViewGroup target = (ViewGroup) mLayout.getChildAt(0);

        target.addView(mComponent);
    }

    @SimpleFunction(description = "Remove the component with the specified id from layout/screen.")
    public void Remove(String id) {
        Object component = COMPONENTS.get(id);

        if (!isEmptyOrNull(component)) {
            try {
                Method method = component.getClass().getMethod("getView");
                final View mComponent = (View) method.invoke(component);
                final ViewGroup mParent = (ViewGroup) mComponent.getParent();

                if (onUiThread) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            mParent.removeView(mComponent);
                        }
                    });
                } else mParent.removeView(mComponent);
            } catch (Exception e) {
                e.printStackTrace();
            }

            COMPONENTS.remove(id);
            COMPONENT_IDS.remove(component);
        }
    }

    @SimpleFunction(description = "Remove all component from layout/screen.")
    public void RemoveAll() {
        Object[] list = GetAllIds().toArray();

        for (Object id : list) {
            Remove(id.toString());
        }

        AllRemoved();
    }

    @SimpleFunction(description = "Sets the order of the specified component according to its parent view.")
    public void SetOrder(AndroidViewComponent component, int index) {
        index = index - 1;
        View mComponent = (View) component.getView();
        ViewGroup mParent = (ViewGroup) mComponent.getParent();

        mParent.removeView(mComponent);

        int childCount = mParent.getChildCount();
        int mIndex = (index > childCount ? childCount : index);

        mParent.addView(mComponent, index);
    }

    @SimpleFunction(description = "Set a property of the specified component, including those only available from the designer.")
    public void SetProperty(String id, String name, Object value) {
        Component component = COMPONENTS.get(id);
        Invoke(component, name, YailList.makeList(new Object[] { value }));
    }

    @SimpleFunction(description = "Set nultiple properties of the specified component, including those only available from the designer.")
    public void SetProperties(String id, YailDictionary properties) {
        Component component = COMPONENTS.get(id);
        JSONObject mProperties = new JSONObject(properties.toString());
        JSONArray mPropertyNames = mProperties.names();

        for (int i = 0; i < mProperties.length(); i++) {
            String name = mPropertyNames.getString(i);
            Object value = mProperties.get(name);
            Invoke(component, name, YailList.makeList(new Object[] { value }));
        }
    }

    @SimpleFunction(description = "Use a JSON Object to create dynamic components.")
    public void SchemaCreate(AndroidViewComponent in, final String template, final YailList parameters) throws Exception {
        JSONObject mScheme = new JSONObject(template);
        String newTemplate = template;

        if (!isEmptyOrNull(template) && mScheme.has("components")) {
            propArray = new JSONArray();

            JSONArray mKeys = (mScheme.has("keys") ? mScheme.getJSONArray("keys") : null);

            if (!isEmptyOrNull(mKeys) && mKeys.length() == parameters.length() - 1) {
                for (int i = 0; i < mKeys.length(); i++) {
                    String keyPercent = "%" + mKeys.getString(i);
                    String keyBracket = "{" + mKeys.getString(i) + "}";
                    String value = parameters.getString(i).replace("\"", "");
                    newTemplate = newTemplate.replace(keyPercent, value);
                    newTemplate = newTemplate.replace(keyBracket, value);
                }
            }

            mScheme = new JSONObject(newTemplate);
            UTIL.parse("", mScheme);
            propArray.remove(0);

            for (int i = 0; i < propArray.length(); i++) {
                if (!propArray.getJSONObject(i).has("id")) throw new YailRuntimeError("One or multiple components don't have a specified id in the template.", TAG);

                final JSONObject mJson = propArray.getJSONObject(i);
                final String mId = mJson.getString("id");
                final String mType = mJson.getString("type");
                AndroidViewComponent mRoot = (!mJson.has("in") ? in : (AndroidViewComponent) GetComponent(mJson.getString("in")));

                Listeners listener = new Listeners() {
                    @Override
                    public void onCreation(Component component, String id) {
                        if (id == mId && mJson.has("properties")) {
                            JSONObject mProp = mJson.getJSONObject("properties");
                            JSONArray keys = mProp.names();

                            for (int k = 0; k < keys.length(); k++) {
                                Invoke((Component) GetComponent(mId), keys.getString(k), YailList.makeList(new Object[] { mProp.get(keys.getString(k)) }));
                            }

                            componentListeners.remove(this);
                        }
                    }
                };

                componentListeners.add(listener);
                Create(mRoot, mType, mId);
            }

            SchemaCreated(mScheme.getString("name"), parameters);
        } else throw new YailRuntimeError("The template is empty or is doesn't have any components.", TAG);
    }
}
