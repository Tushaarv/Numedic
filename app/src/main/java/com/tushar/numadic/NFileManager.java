package com.tushar.numadic;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by Tushar Vengurlekar
 * Created on 01/07/17.
 */

public class NFileManager {

    private static final String FILE_PREFIX_HEALTH = "HEALTH_";
    private static final String FILE_PREFIX_LOCATION = "LOCATION_";
    private static final String FILE_EXTENSION = ".txt";

    private static final String FOLDER_NAME = "Numadic";

    private enum FileType {
        HEALTH,
        LOCATION
    }

    /**
     * Gives a file for specified file type
     * <p>
     * FILE_PREFIX_HEALTH = Used for files used to save Health Data
     * FILE_PREFIX_LOCATION = Used for files used to save LocationData Data
     * File Suffix is going to be UTC Time Value
     *
     * @param context
     * @param fileType file Type
     * @return
     */
    private File getFile(Context context, FileType fileType) {
        File file;

        String filePrefix = fileType == FileType.HEALTH ? FILE_PREFIX_HEALTH : FILE_PREFIX_LOCATION;

        if (new DataManager(context).fileExists(filePrefix)) {
            // if file exists

            // An Active file is the one which is being used for saving values at that instant
            String fileSuffix = new DataManager(context).getActiveFileSuffix(filePrefix);
            file = new File(context.getExternalFilesDir(FOLDER_NAME), filePrefix + fileSuffix + FILE_EXTENSION);

            int fileSize = Integer.parseInt(String.valueOf(file.length() / 1024));

            // File size is more than 1 MB then create a new file else use the same file
            if (fileSize > 1024) {
                // If file size is more than 1MB
                // Create new file with current time Stamp as suffix
                file = makeNewFile(context, filePrefix);
            }

        } else {
            // Else if file Does not exist;
            file = makeNewFile(context, filePrefix);
        }

        return file;
    }

    /**
     * Creates a new file with specified details
     *
     * @param context
     * @param filePrefix prefix to use for file name , suffix with be UTC time stamp
     * @return
     */
    private File makeNewFile(Context context, String filePrefix) {

        String fileSuffix = TimeManager.getCurrentUTCTime();

        File file = new File(context.getExternalFilesDir(FOLDER_NAME), filePrefix + fileSuffix + FILE_EXTENSION);
        try {
            if (file.createNewFile()) {
                new DataManager(context).setActiveFileSuffix(filePrefix, fileSuffix);
                return file;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gives a file for a Health Data
     *
     * @param context
     * @return
     */
    public File getHealthFile(Context context) {
        return getFile(context, FileType.HEALTH);
    }

    /**
     * Gives a file for a LocationData Data
     *
     * @param context
     * @return
     */
    public File getLocationFile(Context context) {
        return getFile(context, FileType.LOCATION);
    }

    /**
     * Write String values to a file
     *
     * @param file   file to write
     * @param string data to write
     */
    public void writeToAFile(File file, String string) {

        String separator = "\n\r";
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file.getAbsoluteFile(),true);


            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.append(string);
            outputStreamWriter.append(separator);
            outputStreamWriter.flush();
            outputStreamWriter.close();

            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
