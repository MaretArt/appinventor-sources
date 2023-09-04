package com.marchtech.QRGenerator;

import android.content.Context;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;

import com.marchtech.Icon;

@DesignerComponent( version = 1,
                    description = "Extension to generate qr code.",
                    category = ComponentCategory.EXTENSION,
                    nonVisible = true,
                    iconName = Icon.ICON)
@SimpleObject(external = true)
public class QRGenerator extends AndroidNonvisibleComponent {
    private static final String CHILD_DIR = "QRGenerator";
    private static final String FILE_NAME = "qr";
    private static final String EXTENSION = "png";

    private final Context context;

    private String CONTENT = "QRGenerator";
    private int WIDTH = 300;
    private int HEIGHT = 300;
    private int backgroundColor;
    private int qrColor;

    public QRGenerator(ComponentContainer container) {
        super(container.$form());
        context = container.$context();

        Color(Component.COLOR_BLACK);
        BackgroundColor(Component.COLOR_WHITE);
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "QRGenerator")
    @SimpleProperty(description = "Set text of qr code.", category = PropertyCategory.APPEARANCE)
    public void Text(String text) {
        this.CONTENT = text;
    }

    @SimpleProperty
    public String Text() {
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
    public void Color(int argb) {
        qrColor = argb;
    }

    @SimpleProperty
    public int Color() {
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
    public void Generate(Image component) {
        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            initDir(context.getExternalCacheDir() + CHILD_DIR);

            bitMatrix = writer.encode(CONTENT, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, getMatrixConfig());
            Graphics2D g = (Graphics2D) qrImage.getGraphics();

            g.drawImage(qrImage, 0, 0, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            ImageIO.write(qrImage, EXTENSION, os);
            Files.copy(new ByteArrayInputStream(os.toByteArray()), Paths.get(context.getExternalCacheDir() + CHILD_DIR + "/" + FILE_NAME + "." + EXTENSION), StandardCopyOption.REPLACE_EXISTING);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            component.Picture(context.getExternalCacheDir() + CHILD_DIR + "/" + FILE_NAME + "." + EXTENSION);
        }
    }

    private void initDir(String directory) throws IOException {
        Files.createDirectories(Paths.get(directory));
    }

    private MatrixToImageConfig getMatrixConfig() {
        return new MatrixToImageConfig(qrColor, backgroundColor);
    }
}
