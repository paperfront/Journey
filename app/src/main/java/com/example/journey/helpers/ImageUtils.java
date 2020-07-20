package com.example.journey.helpers;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import timber.log.Timber;

public class ImageUtils {

        public static final String TAG = "ImageUtils";


        // Returns the File for a photo stored on disk given the fileName
        public static File getPhotoFileUri(Context context, String fileName) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Timber.d("failed to create directory");
            }

            // Return the file target for the photo based on filename
            File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

            return file;
        }


}
