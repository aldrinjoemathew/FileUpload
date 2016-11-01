package com.example.aldrin.fileupload.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.data.Blob;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by aldrin on 1/11/16.
 * DBFlow table class for storing images.
 */

@Table(database = DatabaseClass.class)
public class LocalImage extends BaseModel {

    @PrimaryKey(autoincrement = true)
    @Column
    long id;

    @Column
    String image_name;

    @Column
    String image;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
