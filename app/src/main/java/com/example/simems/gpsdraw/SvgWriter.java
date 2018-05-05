package com.example.simems.gpsdraw;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SvgWriter extends ContextWrapper {

    public SvgWriter(Context context) {
        super(context);
    }

    public boolean writeToFile(String fileName, String fileContents) {
        if (!isExternalStorageWritable()) {
            return false;
        }

        try {
            File file = getPrivateDocumentsDir(fileName);
            FileWriter writer = new FileWriter(file);
            writer.write(fileContents);
            writer.close();
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public File getPrivateDocumentsDir(String documentName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(getExternalFilesDir(
                Environment.DIRECTORY_DOCUMENTS), documentName);
        return file;
    }

    /* Checks if external storage is available for read and write */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
