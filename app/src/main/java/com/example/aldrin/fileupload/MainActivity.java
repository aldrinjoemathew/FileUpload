package com.example.aldrin.fileupload;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.example.aldrin.fileupload.utilities.Constants;
import com.example.aldrin.fileupload.utilities.UtilityMethods;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.aldrin.fileupload.utilities.UtilityMethods.onCaptureImageResult;

/**
 * This activity is used to uplaod images and display them on screen.
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_image_display)
    RecyclerView rvImageDisplay;

    private Intent intent;
    private String userChoosenTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.UPLOAD_IMAGE)  {

            } else if (requestCode == Constants.REQUEST_CAMERA){
                onCaptureImageResult(data);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        takePhoto();
                    else if(userChoosenTask.equals("Choose from Library"))
                        uploadPhoto();
                } else {
                    //code for deny
                }
                break;
        }
    }

    @OnClick(R.id.btn_start_camera)
    void takePhoto() {
        userChoosenTask = "Take Photo";
        boolean result= UtilityMethods.checkPermission(MainActivity.this);
        if (!result)
            return;
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Constants.REQUEST_CAMERA);
    }

    @OnClick(R.id.btn_upload_image)
    void uploadPhoto() {
        userChoosenTask = "Choose from Library";
        intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"), Constants.UPLOAD_IMAGE);
    }
}
