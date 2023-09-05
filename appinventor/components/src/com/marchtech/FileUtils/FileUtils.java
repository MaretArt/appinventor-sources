package com.marchtech.FileUtils;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import java.io.File;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.FileUtil;
import com.google.appinventor.components.runtime.util.YailList;

import com.marchtech.Icon;
import com.marchtech.FileUtils.helpers.Size;

import android.annotation.SuppressLint;
import android.os.Environment;

@DesignerComponent(version = 1, description = "Extension to help you working with file.", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = Icon.ICON)
@SimpleObject(external = true)
@SuppressLint({ "InlinedApi", "SdCardPath" })
public class FileUtils extends AndroidNonvisibleComponent {

    private boolean clearCache = false;

    public FileUtils(ComponentContainer container) {
        super(container.$form());
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "False")
    @SimpleProperty(description = "To enable read access to file storage outside of the app-specific directories.")
    @UsesPermissions(READ_EXTERNAL_STORAGE)
    public void ReadPermission(boolean required) {
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "False")
    @SimpleProperty(description = "To enable write access to file storage outside of the app-specific directories.")
    @UsesPermissions(WRITE_EXTERNAL_STORAGE)
    public void WritePermission(boolean required) {
    }

    @SimpleEvent(description = "An event that occurrs when directory has been created.")
    public void DirectoryCreated(String path, String directoryName) {
        EventDispatcher.dispatchEvent(this, "DirectoryCreated", path, directoryName);
    }

    @SimpleEvent(description = "An event that occurrs when directory has been removed.")
    public void DirectoryRemoved(String path, String directoryName) {
        EventDispatcher.dispatchEvent(this, "DirectoryRemoved", path, directoryName);
    }

    @SimpleEvent(description = "An event that occurrs when cache has been cleared.")
    public void CacheCleared(String path, String directoryName) {
        EventDispatcher.dispatchEvent(this, "CacheCleared", path, directoryName);
    }

    @SimpleEvent(description = "An event that occurrs when got list of file and directory.")
    public void GotLists(YailList list) {
        EventDispatcher.dispatchEvent(this, "DirectoryRemoved", list);
    }

    @SimpleEvent(description = "An event that occurrs when file is moved.")
    public void Moved(Object fileName) {
        EventDispatcher.dispatchEvent(this, "Moved", fileName);
    }

    @SimpleEvent(description = "An event that occurrs when error occurred.")
    public void ErrorOccurred(Object messages) {
        EventDispatcher.dispatchEvent(this, "ErrorOccurred", messages);
    }

    @SimpleFunction(description = "Create a new directory for storing files.")
    public void MakeDirectory(FileScope scope, String directoryName) {
        File file = new File(MakeFullPath(scope, directoryName));
        try {
            if (Exists(scope, directoryName))
                DirectoryCreated(MakeFullPath(scope, directoryName), directoryName);
            else
                file.mkdirs();
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        if (clearCache) {
            clearCache = false;
            return;
        }

        if (Exists(scope, directoryName))
            DirectoryCreated(MakeFullPath(scope, directoryName), directoryName);
    }

    @SimpleFunction(description = "Remove a directory from the file system.")
    public void RemoveDirectory(FileScope scope, String directoryName, boolean recursive) {
        if (recursive)
            deleteRecursive(new File(MakeFullPath(scope, directoryName)));
        else {
            File mFile = new File(MakeFullPath(scope, directoryName));
            mFile.delete();
        }

        if (clearCache) {
            clearCache = false;
            return;
        }
        if (!Exists(scope, directoryName))
            DirectoryRemoved(MakeFullPath(scope, directoryName), directoryName);
    }

    @SimpleFunction(description = "Tests whether the path exists in the given scope.")
    public boolean Exists(FileScope scope, String path) {
        File mFile = new File(MakeFullPath(scope, path));
        return mFile.exists();
    }

    @SimpleFunction(description = "Get a list of files and directories in the given directory.")
    public void ListFileAndDirectory(FileScope scope, String directoryName) {
        File mFile = new File(MakeFullPath(scope, directoryName));
        Object[] files = mFile.list();

        GotLists(YailList.makeList(files));
    }

    @SimpleFunction(description = "Converts the scope and path into a single string for other components.")
    public String MakeFullPath(FileScope scope, String path) {
        return FileUtil.resolveFileName(form, path, scope).replaceAll("file://", "");
    }

    @SimpleFunction(description = "To get path of app.")
    public String GetApplicationPath() {
        return MakeFullPath(FileScope.App, "");
    }

    @SimpleFunction(description = "To get external storage path.")
    public String GetExternalStoragePath() {
        return MakeFullPath(FileScope.Shared, "");
    }

    @SimpleFunction(description = "To clear cache directory.")
    public void ClearCache(FileScope scope, String directoryName) {
        clearCache = true;
        if (directoryName == null || directoryName == "") {
            File mFile = new File(MakeFullPath(FileScope.Cache, ""));
            String[] files = mFile.list();
            for (String path : files) {
                RemoveDirectory(FileScope.Cache, path, true);
            }

            CacheCleared(MakeFullPath(FileScope.Cache, ""), "cache");
        } else {
            RemoveDirectory(scope, directoryName, true);
            MakeDirectory(scope, directoryName);

            CacheCleared(MakeFullPath(scope, directoryName), directoryName);
        }
    }

    @SimpleFunction(description = "To get size of file or directory.")
    public double GetSize(Size sizeIn, FileScope scope, String path) {
        File file = new File(MakeFullPath(scope, path));
        long size = getFolderSize(file);
        if (sizeIn == Size.Byte)
            return (double) size;
        else if (sizeIn == Size.KiloByte)
            return (double) size / 1024.0;
        else if (sizeIn == Size.MegaByte)
            return (double) size / (Math.pow(1024.0, 2));
        else if (sizeIn == Size.GigaByte)
            return (double) size / (Math.pow(1024.0, 3));
        else
            return -1;
    }

    @SimpleFunction(description = "To get total file and directory.")
    public long GetTotalFile(FileScope scope, String path, boolean recursive) {
        File file = new File(MakeFullPath(scope, path));
        return getTotalFile(file, recursive);
    }

    @SimpleFunction(description = "To get name of file or directory.")
    public String GetName(String path) {
        File file = new File(path);
        return file.getName();
    }

    @SimpleFunction(description = "To move file into Pictures directory.")
    public void MoveIntoPictures(String fromFileName, String toFileName) {
        File from = new File(fromFileName);
        File to = new File(Environment.DIRECTORY_PICTURES + toFileName);
        try {
            from.renameTo(to);
        } catch (SecurityException e) {
            ErrorOccurred(e.getMessage());
        }

        Moved(toFileName);
    }

    private void deleteRecursive(File folder) {
        if (folder.isDirectory()) {
            for (File mFile : folder.listFiles()) {
                deleteRecursive(mFile);
            }
        }

        folder.delete();
    }

    private long getFolderSize(File folder) {
        long length = 0;
        File[] files = folder.listFiles();

        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile())
                length += files[i].length();
            else
                length += getFolderSize(files[i]);
        }

        return length;
    }

    private long getTotalFile(File folder, boolean recursive) {
        File[] files = folder.listFiles();
        long total = files.length;

        if (recursive) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory())
                    total += getTotalFile(files[i], true);
            }
        }

        return total;
    }
}
