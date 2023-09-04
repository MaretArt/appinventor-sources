package com.marchtech.GLCM;

import java.io.IOException;

import java.lang.Math;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.Manifest;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.MediaUtil;
import com.google.appinventor.components.runtime.util.YailDictionary;
import com.google.appinventor.components.runtime.util.YailList;
import com.marchtech.Icon;
import com.marchtech.GLCM.helpers.Mode;

@DesignerComponent( version = 1,
                    description = "Extension to get GLCM value from images.",
                    category = ComponentCategory.EXTENSION,
                    nonVisible = true,
                    iconName = Icon.ICON)
@SimpleObject(external = true)
@UsesPermissions(permissionNames = "android.permission.READ_EXTERNAL_STORAGE")
public class GLCM extends AndroidNonvisibleComponent {
    private final ComponentContainer container;

    private Bitmap picture;

    private int totalX;
    private int totalY;
    private int maxValue = 0;
    private int sumValue = 0;
    private double[][] gsMatrix;
    private double[][] coMatrix;
    private double[][] syMatrix;
    private double[][] nmMatrix;

    private List<Mode> modes = new ArrayList<>();

    public GLCM(ComponentContainer container) {
        super(container.$form());
        this.container = container;
    }

    @SimpleEvent(description = "An event that occurs when errors has occured.")
    public void ErrorOccurred(String messages) {
        EventDispatcher.dispatchEvent(this, "ErrorOccurred", messages);
    }

    @SimpleEvent(description = "An event that occurs when GLCM has been calculated.")
    public void Finished(YailDictionary result) {
        EventDispatcher.dispatchEvent(this, "Finished", result);
    }

    @SimpleEvent(description = "An event that occurs when Step has been finished.")
    public void OnNextStep(String step) {
        EventDispatcher.dispatchEvent(this, "OnNextStep", step);
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ASSET, defaultValue = "")
    @SimpleProperty(description = "To set picture to analyze.")
    public void Picture(@Asset final String path) {
        if (MediaUtil.isExternalFile(container.$context(), path) && container.$form().isDeniedPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            container.$form().askPermission(Manifest.permission.READ_EXTERNAL_STORAGE, new PermissionResultHandler() {
                @Override
                public void HandlePermissionResponse(String permission, boolean granted) {
                    if (granted) {
                        Picture(path);
                    } else {
                        container.$form().dispatchPermissionDeniedEvent(GLCM.this, "step1", permission);
                    }
                }
            });
        }

        String picturePath = (path == null) ? "" : path;

        Drawable drawable;
        try {
            drawable = MediaUtil.getBitmapDrawable(container.$form(), picturePath);
        } catch (IOException e) {
            ErrorOccurred("Picture" + ", Unable to load " + picturePath);
            drawable = null;
        }

        BitmapDrawable bDrawable = (BitmapDrawable) drawable;
        if (bDrawable.getBitmap() != null) {
            picture = bDrawable.getBitmap();
        } else {
            ErrorOccurred("BitmapDrawable" + ", null");
            return;
        }
    }

    @SimpleFunction(description = "To set result of GLCM method.")
    public void SetMode(YailList mode) {
        Object[] mMode = mode.toArray();
        for (Object m : mMode) {
            modes.add((Mode)m);
        }
    }

    @SimpleProperty(description = "To get mode.")
    public void Mode(@Options(Mode.class) String mode) {

    }

    @SimpleFunction(description = "Start GLCM Method")
    public void Start(int distance) {
        if (!isGrayscale(picture)) picture = toGrayscale(picture);

        int pixel, R, G, B;
        totalX = picture.getWidth();
        totalY = picture.getHeight();

        gsMatrix = new double[(totalX + 1) / distance][(totalY + 1) / distance];
        
        // Get max value and grayscale tone
        for (int x = 0; x < totalX; x += distance) {
            for (int y = 0; y < totalY; y += distance) {
                pixel = picture.getPixel(x, y);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                gsMatrix[x][y] = (double)((0.299 * R) + (0.587 * G) + (0.114 * B));

                if ((int)gsMatrix[x][y] > maxValue) {
                    maxValue = (int)gsMatrix[x][y];
                }
            }
        }
/*
        // Transpose Co-Occurance Matrix
        double[][] TcoMatrix = new double[maxValue + 2][maxValue + 2];
        for (int x = 0; x <= (maxValue + 1); x++) {
            for (int y = 0; y <= (maxValue + 1); y++) {
                TcoMatrix[x][y] = coMatrix[y][x];
            }
        }
*/
        OnNextStep("Start");
    }

    @SimpleFunction(description = "Next step from start.")
    public void Next1() {
        // Get Co-Occurance Matrix
        coMatrix = new double[maxValue + 2][maxValue + 2];
        for (int y = 0; y < totalY; y++) {
            for (int x = 0; x < totalX; x++) {
                int xx = (int)gsMatrix[x + 1][y];
                int yy = (int)gsMatrix[x][y];
                coMatrix[xx][yy]++;
            }
        }

        OnNextStep("Next1");
    }

    @SimpleFunction(description = "Next step from next1.")
    public void Next2() {
        // Add up Co-Occurance Matrix with it's transpose to get Symmetric Matrix
        syMatrix = new double[maxValue + 2][maxValue + 2];
        for (int x = 0; x <= (maxValue + 1); x++) {
            for (int y = 0; y <= (maxValue + 1); y++) {
                syMatrix[x][y] = coMatrix[x][y] + coMatrix[y][x];
                sumValue += syMatrix[x][y];
            }
        }

        OnNextStep("Next2");
    }

    @SimpleFunction(description = "Next step from next2.")
    public void Next3() {
        // Get Normalization Matrix
        nmMatrix = new double[maxValue + 2][maxValue + 2];
        for (int x = 0; x <= (maxValue + 1); x++) {
            for (int y = 0; y <= (maxValue + 1); y++) {
                nmMatrix[x][y] = (syMatrix[x][y] / sumValue);
            }
        }

        OnNextStep("Next3");
    }

    @SimpleFunction(description = "Next step from next3 before finished.")
    public void Next4() {
        Map<Object, Object> result = new HashMap<>();
        if (modes.contains(Mode.Contrast)) {
            // Get contrast
            double contrast = 0;
            for (int x = 0; x <= maxValue; x++) {
                for (int y = 0; y <= maxValue; y++) {
                    contrast += (nmMatrix[x][y] * Math.pow((x - y), 2));
                }
            }

            result.put("contrast", (Object)contrast);
        }

        if (modes.contains(Mode.Dissimilarity)) {
            // Get dissimilarity
            double dissimilarity = 0;
            for (int x = 0; x <= maxValue; x++) {
                for (int y = 0; y <= maxValue; y++) {
                    dissimilarity += (nmMatrix[x][y] * Math.abs((x - y)));
                }
            }

            result.put("dissimilarity", (Object)dissimilarity);
        }

        if (modes.contains(Mode.Homogeneity)) {
            // Get homogeneity
            double homogeneity = 0;
            for (int x = 0; x <= maxValue; x++) {
                for (int y = 0; y <= maxValue; y++) {
                    homogeneity += (nmMatrix[x][y] / 1 + Math.pow((x - y), 2));
                }
            }

            result.put("homogeneity", (Object)homogeneity);
        }

        double asm = 0;
        
        if (modes.contains(Mode.ASM)) {
            // Get asm (angular second moment)
            for (int x = 0; x <= maxValue; x++) {
                for (int y = 0; y <= maxValue; y++) {
                    asm += Math.pow(nmMatrix[x][y], 2);
                }
            }

            result.put("asm", (Object)asm);
        }

        if (modes.contains(Mode.Energy)) {
            // Get energy
            double energy = 0;
            if (!modes.contains(Mode.ASM)) {
                for (int x = 0; x <= maxValue; x++) {
                    for (int y = 0; y <= maxValue; y++) {
                        asm += Math.pow(nmMatrix[x][y], 2);
                    }
                }
            }
            
            energy = Math.sqrt(asm);
            result.put("energy", (Object)energy);
        }

        if (modes.contains(Mode.Correlation)) {
            // Get correlation
            double correlation = 0;
            for (int x = 0; x <= maxValue; x++) {
                for (int y = 0; y <= maxValue; y++) {
                    correlation += (y * (nmMatrix[x][y]));
                }
            }

            result.put("correlation", (Object)correlation);
        }

        Finished(YailDictionary.makeDictionary(result));
    }

    private Bitmap toGrayscale(Bitmap bitmap) {
        int width, height;
        height = bitmap.getHeight();
        width = bitmap.getWidth();

        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cMatrix);
        paint.setColorFilter(f);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return result;
    }

    private boolean isGrayscale(Bitmap bitmap) {
        int width, height, pixel, R, G, B;
        height = bitmap.getHeight();
        width = bitmap.getWidth();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pixel = bitmap.getPixel(x, y);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                if (R != G && G != B) return false;
            }
        }

        return true;
    }
}