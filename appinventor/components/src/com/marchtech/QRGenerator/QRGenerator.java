package com.marchtech.QRGenerator;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Hashtable;
import java.util.Map;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.AsynchUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.datamatrix.encoder.SymbolShapeHint;
import com.marchtech.Icon;
import com.marchtech.QRGenerator.helpers.BarFormat;
import com.marchtech.QRGenerator.helpers.Charset;
import com.marchtech.QRGenerator.helpers.FileFormat;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

@DesignerComponent(version = 1, description = "Extension to generate qr code.", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = Icon.ICON)
@SimpleObject(external = true)
@UsesPermissions(permissionNames = READ_EXTERNAL_STORAGE + ", " + WRITE_EXTERNAL_STORAGE)
@UsesLibraries(libraries = "zxing-3.4.jar")
public class QRGenerator extends AndroidNonvisibleComponent {
    private Activity activity;
    private SymbolShapeHint shapeHint = SymbolShapeHint.FORCE_NONE;

    private boolean useAddDecoders = false;

    private String CONTENT = "QRGenerator";

    private int WIDTH = 300;
    private int HEIGHT = 300;
    private int MARGIN = 0;
    private int backgroundColor;
    private int qrColor;

    public QRGenerator(ComponentContainer container) {
        super(container.$form());
        activity = container.$context();

        QRColor(Component.COLOR_BLACK);
        BackgroundColor(Component.COLOR_WHITE);
    }

    @SimpleEvent(description = "An event that occurrs when generated/decoded barcode failed.")
    public void ErrorOccurred(String functionName, String messages) {
        EventDispatcher.dispatchEvent(this, "ErrorOccurred", messages);
    }

    @SimpleEvent(description = "An event that occurrs when barcode has been genarated.")
    public void Generated(String filePath) {
        EventDispatcher.dispatchEvent(this, "Generated", filePath);
    }

    @SimpleEvent(description = "An event that occurrs when barcode has been decoded.")
    public void Decoded(BarFormat barFormat, String result) {
        EventDispatcher.dispatchEvent(this, "Decoded", barFormat, result);
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "False")
    @SimpleProperty(description = "Specifies whether decoders should use additional hints.")
    public void UseAdditionalDecoders(boolean use) {
        useAddDecoders = use;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_CHOICES, editorArgs = { "NONE", "RECTANGLE",
            "SQUARE" }, defaultValue = "NONE")
    @SimpleProperty(description = "Specifies barcode shape.<br> Note: Normally you wouldn't need to use this.")
    public void Shape(String shape) {
        switch (shape) {
            case "NONE":
                shapeHint = SymbolShapeHint.FORCE_NONE;
                break;

            case "RECTANGLE":
                shapeHint = SymbolShapeHint.FORCE_RECTANGLE;
                break;

            case "SQUARE":
                shapeHint = SymbolShapeHint.FORCE_SQUARE;
                break;
        }
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "QRGenerator")
    @SimpleProperty(description = "Set text of qr code.", category = PropertyCategory.APPEARANCE)
    public void Content(String text) {
        this.CONTENT = text;
    }

    @SimpleProperty
    public String Content() {
        return CONTENT;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER, defaultValue = "300")
    @SimpleProperty(description = "Set width of qr code.", category = PropertyCategory.APPEARANCE)
    public void Width(int width) {
        this.WIDTH = width;
    }

    @SimpleProperty
    public int Width() {
        return WIDTH;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER, defaultValue = "300")
    @SimpleProperty(description = "Set height of qr code.", category = PropertyCategory.APPEARANCE)
    public void Height(int height) {
        this.HEIGHT = height;
    }

    @SimpleProperty
    public int Height() {
        return HEIGHT;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR, defaultValue = Component.DEFAULT_VALUE_COLOR_BLACK)
    @SimpleProperty(description = "Set color of qr code.", category = PropertyCategory.APPEARANCE)
    public void QRColor(int argb) {
        qrColor = argb;
    }

    @SimpleProperty
    public int QRColor() {
        return qrColor;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR, defaultValue = Component.DEFAULT_VALUE_COLOR_WHITE)
    @SimpleProperty(description = "Set background color of qr code.", category = PropertyCategory.APPEARANCE)
    public void BackgroundColor(int argb) {
        backgroundColor = argb;
    }

    @SimpleProperty
    public int BackgroundColor() {
        return backgroundColor;
    }

    @SimpleFunction(description = "To generate qr code.")
    public void Generate(final String content, final String outputPath, final String logoPath,
            final FileFormat fileFormat, final BarFormat barFormat, @Options(Charset.class) final String charset) {
        AsynchUtil.runAsynchronously(new Runnable() {
            @Override
            public void run() {
                File file = new File(outputPath);
                try {
                    FileOutputStream output = new FileOutputStream(file);
                    Bitmap bitmap = writer(content, BarcodeFormat.valueOf(barFormat.toString()), WIDTH, HEIGHT, MARGIN,
                            charset, qrColor, backgroundColor, logoPath);
                    boolean success = bitmap.compress(Bitmap.CompressFormat.valueOf(fileFormat.toString()), 100,
                            output);
                    output.flush();
                    output.close();

                    if (success)
                        barGenerated(file.getPath());
                    else
                        barGenerated("Unable to generate barcode.");
                } catch (Exception e) {
                    ErrorOccurred("Generate", getMessage(e));
                }
            }
        });
    }

    @SimpleFunction(description = "To decode barcode from file.")
    public void Decode(final String filePath) {
        AsynchUtil.runAsynchronously(new Runnable() {
            @Override
            public void run() {
                File file = new File(filePath);
                reader(file.getAbsoluteFile());
            }
        });
    }

    private void barGenerated(final String string) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Generated(string);
            }
        });
    }

    private void barDecoded(final String barFormat, final String result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Decoded(BarFormat.valueOf(barFormat), result);
            }
        });
    }

    private String getMessage(Exception e) {
        return e.getMessage() != null ? e.getMessage() : e.toString();
    }

    private Bitmap writer(String content, BarcodeFormat barFormat, int width, int height, int margin, String charset,
            int fColor, int bgColor, String logoPath) {
        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, charset);
            hints.put(EncodeHintType.MARGIN, margin);
            if (shapeHint != SymbolShapeHint.FORCE_NONE) {
                hints.put(EncodeHintType.DATA_MATRIX_SHAPE, shapeHint);
            }

            MultiFormatWriter codeWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = codeWriter.encode(content, barFormat, width, height, hints);
            int w = bitMatrix.getWidth();
            int h = bitMatrix.getHeight();
            int pixel = width / w;
            if (pixel > (height / h))
                pixel = height / h;

            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width * pixel;
                for (int pixelHeight = 0; pixelHeight < pixel; pixelHeight++, offset += width) {
                    for (int x = 0; x < w; x++) {
                        for (int pixelWidth = 0; pixelWidth < pixel; pixelWidth++) {
                            pixels[offset + x * pixel + pixelWidth] = bitMatrix.get(x, y) ? fColor : bgColor;
                        }
                    }
                }
            }

            Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            result.setPixels(pixels, 0, width, 0, 0, width, height);
            if (!logoPath.isEmpty())
                return mergeBitmap(
                        logoPath.startsWith("//") ? BitmapFactory.decodeStream(form.openAsset(logoPath.substring(2)))
                                : BitmapFactory.decodeFile(logoPath),
                        result);
            else
                return result;
        } catch (Exception e) {
            return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }
    }

    private void reader(File file) {
        try {
            MultiFormatReader read = new MultiFormatReader();
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            int[] array = new int[bitmap.getWidth() * bitmap.getHeight()];
            bitmap.getPixels(array, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
            LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), array);
            BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = null;
            if (useAddDecoders) {
                Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
                hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
                hints.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.allOf(BarcodeFormat.class));
                hints.put(DecodeHintType.PURE_BARCODE, Boolean.FALSE);
                result = read.decode(bBitmap, hints);
            } else
                result = read.decode(bBitmap);

            barDecoded(result.getBarcodeFormat().toString(), result.getText());
        } catch (Exception e) {
            ErrorOccurred("Decode", getMessage(e));
        }
    }

    private Bitmap mergeBitmap(Bitmap logo, Bitmap code) {
        Bitmap result = Bitmap.createBitmap(code.getWidth(), code.getHeight(), code.getConfig());
        Canvas canvas = new Canvas(result);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        canvas.drawBitmap(code, new Matrix(), null);
        Bitmap resize = Bitmap.createScaledBitmap(logo, width / 5, height / 5, true);
        int x = (width - resize.getWidth()) / 2;
        int y = (height - logo.getHeight()) / 2;
        canvas.drawBitmap(resize, x, y, null);
        return result;
    }
}
