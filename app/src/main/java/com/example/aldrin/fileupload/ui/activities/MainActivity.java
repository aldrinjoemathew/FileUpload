package com.example.aldrin.fileupload.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.aldrin.fileupload.R;
import com.example.aldrin.fileupload.adapters.AdapterImageDisplay;
import com.example.aldrin.fileupload.database.LocalImage;
import com.example.aldrin.fileupload.utilities.Constants;
import com.example.aldrin.fileupload.utilities.FilePath;
import com.example.aldrin.fileupload.utilities.ImageDBController;
import com.example.aldrin.fileupload.utilities.UtilityMethods;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.aldrin.fileupload.utilities.Constants.FROM_GALLERY;
import static com.example.aldrin.fileupload.utilities.Constants.INTENT_TYPE_IMAGE;
import static com.example.aldrin.fileupload.utilities.Constants.TAKE_PHOTO;
import static com.example.aldrin.fileupload.utilities.UtilityMethods.onCaptureImageResult;

/**
 * This activity is used to uplaod images and display them on screen.
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_image_display)
    RecyclerView rvImageDisplay;

    private Intent mIntent;
    private String mUserChoosenTask;
    private AdapterImageDisplay mAdapter;
    private List<LocalImage> mImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setRecyclerView();
    }

    /**
     * To set the recycler view when app first launches.
     */
    private void setRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mImages = ImageDBController.getAllImages();
        mAdapter = new AdapterImageDisplay(this, mImages);
        rvImageDisplay.setLayoutManager(layoutManager);
        rvImageDisplay.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.UPLOAD_IMAGE)  {
                String filepath = FilePath.getPath(this, data.getData());
                storeImageInDatabaseAndNotifyAdapter(filepath);
            } else if (requestCode == Constants.REQUEST_CAMERA){
                File image = onCaptureImageResult(data);
                if (image != null) {
                    storeImageInDatabaseAndNotifyAdapter(image.getAbsolutePath());
                }
            }
        }
    }

    /**
     * Store the path to image and notifies the adapter to update view.
     * @param filepath
     */
    private void storeImageInDatabaseAndNotifyAdapter(String filepath) {
        LocalImage newImage = new LocalImage();
        newImage.setImage(filepath);
        newImage.setImage_name(filepath);
        newImage.save();
        mImages.add(newImage);
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mUserChoosenTask.equals(TAKE_PHOTO))
                        takePhoto();
                    else if(mUserChoosenTask.equals(FROM_GALLERY))
                        uploadPhoto();
                } else {
                    finish();
                }
                break;
        }
    }

    @OnClick(R.id.btn_start_camera)
    void takePhoto() {
        mUserChoosenTask = TAKE_PHOTO;
        boolean readPermission= UtilityMethods.checkPermissionStorage(MainActivity.this);
        if (!readPermission)
            return;
        mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(mIntent, Constants.REQUEST_CAMERA);
    }

    @OnClick(R.id.btn_upload_image)
    void uploadPhoto() {
        mUserChoosenTask = FROM_GALLERY;
        boolean readPermission= UtilityMethods.checkPermissionStorage(MainActivity.this);
        if (!readPermission)
            return;
        mIntent = new Intent();
        mIntent.setType(INTENT_TYPE_IMAGE);
        mIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(mIntent, FROM_GALLERY), Constants.UPLOAD_IMAGE);
    }
}
