package xyz.ratapp.munion.helpers;

import android.content.Context;
import android.net.Uri;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by timtim on 02/01/2018.
 */

public class FileHelper {

    /**
     *
     * @param imageUri
     * @param context
     * @param baseFolder
     * @param fileName
     * @return path to file
     * @throws Exception
     */
    public static String saveUriToFile(@NotNull Uri imageUri,
                                         @NotNull Context context,
                                         @NotNull String baseFolder,
                                         @NotNull String fileName) throws Exception {
        File file = new File(baseFolder, fileName);
        InputStream input = context.getContentResolver().openInputStream(imageUri);

        try {
            file.createNewFile();

            OutputStream output = new FileOutputStream(file);
            try {
                byte[] buffer = new byte[4 * 1024]; // or other buffer size
                int read;

                while ((read = input.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }

                output.flush();
            } finally {
                output.close();

            }
        } finally {
            input.close();
        }

        return file.getAbsolutePath();
    }

}
