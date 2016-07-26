package com.ran.delta.utils;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class FileUtils {

    private FileUtils() {
    }

    public static boolean deleteFile(String path) {
        if (path == null || path.isEmpty()) {
            return true;
        }

        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }

    public static String getInternalFolderPath(Context context, String folderName) {
        if (folderName == null || folderName.isEmpty()) {
            return context.getFilesDir().getAbsolutePath() + File.separator;
        }

        File mFolder = new File(context.getFilesDir() + File.separator + folderName);
        if (!mFolder.exists()) {
            mFolder.mkdirs();
        }

        return mFolder.getAbsolutePath() + File.separator;
    }

    public static void unzip(String targetDir, String zipFile) throws IOException {
        File zip = new File(targetDir, zipFile);

        if (!zip.exists()) {
            return;
        }

        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zip)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];

            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDir, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();

                if (!dir.isDirectory() && !dir.mkdirs()) {
                    throw new FileNotFoundException("Failed to ensure directory: " + dir.getAbsolutePath());
                }

                if (ze.isDirectory()) {
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1) {
                        fout.write(buffer, 0, count);
                    }
                } finally {
                    fout.close();
                }
            }
        } finally {
            zis.close();
        }
    }

    public static JSONObject readJsonFile(String dir, String fileName) {
        FileInputStream stream = null;

        File file = new File(dir, fileName);

        try {
            stream = new FileInputStream(file);
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            String jsonStr = Charset.defaultCharset().decode(bb).toString();

            return new JSONObject(jsonStr);
        } catch (Exception e) {
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public void writeToFile(File file, String fileContent) {
        if (!file.exists()) {
            try {
                FileWriter writer = new FileWriter(file);
                writer.write(fileContent);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String readFileContent(File file) {
        StringBuilder fileContentBuilder = new StringBuilder();
        if (file.exists()) {
            String stringLine;
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                while ((stringLine = bufferedReader.readLine()) != null) {
                    fileContentBuilder.append(stringLine + "\n");
                }
                bufferedReader.close();
                fileReader.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileContentBuilder.toString();
    }

    public static void CopyAssets(Context context, String assetDir,String dir) {
        String[] files;
        try {
            files = context.getResources().getAssets().list(assetDir);
        } catch (IOException e1) {
            return;
        }
        File mWorkingPath = new File(dir);
        if (!mWorkingPath.exists()) {
            if (!mWorkingPath.mkdirs()) {
                return;
            }
        }

        for (int i = 0; i < files.length; i++) {
            try {
                String fileName = files[i];
                if (!fileName.contains(".")) {
                    if (0 == assetDir.length()) {
                        CopyAssets(context, fileName, dir + fileName + File.separator);
                    } else {
                        CopyAssets(context, assetDir + File.separator + fileName, dir + fileName + File.separator);
                    }
                    continue;
                }

                File outFile = new File(mWorkingPath, fileName);
                if (outFile.exists()) {
                    outFile.delete();
                }

                InputStream in;
                if (0 != assetDir.length()) {
                    in = context.getAssets().open(assetDir + File.separator + fileName);
                } else {
                    in = context.getAssets().open(fileName);
                }
                OutputStream out = new FileOutputStream(outFile);

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
