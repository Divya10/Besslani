/*
 * /////////////////////////////////////////////////////////////////////////////////////////////////
 * //
 * //            © Copyright 2019 JangleTech Systems Private Limited, Thane, India
 * //
 * /////////////////////////////////////////////////////////////////////////////////////////////////
 */

package com.example.panache.utils;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static com.jangletech.mie.util.Common.throwCrashFirebase;

/**
 * Created by DeepakSingh on 1/27/2020.
 */
public class CopyAsyncTask extends AsyncTask<Void, Void, Void> {
    private final List<String> uploadsPaths;
    private final List<String> uploadList;
    private String nativePath;

    public CopyAsyncTask(List<String> uploadsPaths, List<String> uploadList, String nativePath) {
        this.uploadsPaths = uploadsPaths;
        this.uploadList = uploadList;
        this.nativePath = nativePath;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            for (int i = 0; i < uploadsPaths.size(); i++) {
                String uploadedFilePath = uploadList.get(i);
                File localFile = new File(nativePath, uploadedFilePath.substring(uploadedFilePath.lastIndexOf('/') + 1));
                if (!localFile.exists()) {
                    if (localFile.createNewFile()) {
                        FileMoving(uploadsPaths.get(i), localFile.getAbsolutePath());
                    }
                }

            }
        }
        catch (IOException e) {
            throwCrashFirebase(e);
        }
        return null;
    }

    private void FileMoving(String inputFile, String outputFile) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(inputFile);
            out = new FileOutputStream(outputFile);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            // write the output file (You have now copied the file)
            out.flush();
            out.close();
        }
        catch (IOException e) {
            throwCrashFirebase(e);
        }

    }
}
