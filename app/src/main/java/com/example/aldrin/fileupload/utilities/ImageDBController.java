package com.example.aldrin.fileupload.utilities;

import com.example.aldrin.fileupload.database.LocalImage;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * Created by aldrin on 1/11/16.
 */

public class ImageDBController {

    /**
     * To get all images in the DatabaseClass.
     * @return
     */
    public static List<LocalImage> getAllImages() {
        return SQLite.select()
                .from(LocalImage.class)
                .queryList();
    }
}
