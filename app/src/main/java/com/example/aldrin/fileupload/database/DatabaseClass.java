package com.example.aldrin.fileupload.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by aldrin on 1/11/16.
 * DBFlow database class for storing tables.
 */

@Database(name = DatabaseClass.NAME, version = DatabaseClass.VERSION)
public class DatabaseClass {
    public static final String NAME = "MyDatabase";
    public static final int VERSION = 1;
}
