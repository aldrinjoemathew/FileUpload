package com.example.aldrin.fileupload.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.aldrin.fileupload.R;
import com.example.aldrin.fileupload.adapters.AdapterImageDisplay;
import com.example.aldrin.fileupload.database.LocalImage;
import com.example.aldrin.fileupload.utilities.Constants;
import com.example.aldrin.fileupload.utilities.FilePath;
import com.example.aldrin.fileupload.utilities.ImageDBController;
import com.example.aldrin.fileupload.utilities.UtilityMethods;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

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

    private void setRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mImages = ImageDBController.getAllImages();
        dislplayDatabase();
        mAdapter = new AdapterImageDisplay(this, mImages);
        rvImageDisplay.setLayoutManager(layoutManager);
        rvImageDisplay.setAdapter(mAdapter);
    }

    private void dislplayDatabase() {
        for (int i=0; i<mImages.size(); i++) {
            Log.d("INFO", String.valueOf(mImages.get(i).getId()));
            if (mImages.get(i).getImage_name()==null) break;
            Log.d("INFO", mImages.get(i).getImage_name());
            Log.d("INFO", mImages.get(i).getImage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.UPLOAD_IMAGE)  {
                String filepath = FilePath.getPath(this, data.getData());
                LocalImage newImage = new LocalImage();
                newImage.setImage(filepath);
                newImage.setImage_name(filepath);
                newImage.save();
                mImages.add(newImage);
                mAdapter.notifyDataSetChanged();
                dislplayDatabase();
            } else if (requestCode == Constants.REQUEST_CAMERA){
                File image = onCaptureImageResult(data);
                FileInputStream fileInputStream;
                try {
                    if (image != null) {
                        fileInputStream = new FileInputStream(image.getPath());
                        byte[] imageBytes = new byte[fileInputStream.available()];
                        LocalImage newImage = new LocalImage();
                        newImage.setImage(image.getAbsolutePath());
                        newImage.setImage_name(image.getName());
                        newImage.save();
                        mImages.add(newImage);
                        mAdapter.notifyDataSetChanged();
                        dislplayDatabase();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mUserChoosenTask.equals("Take Photo"))
                        takePhoto();
                    else if(mUserChoosenTask.equals("Choose from Library"))
                        uploadPhoto();
                } else {
                    //code for deny
                }
                break;
        }
    }

    @OnClick(R.id.btn_start_camera)
    void takePhoto() {
        mUserChoosenTask = "Take Photo";
        boolean readPermission= UtilityMethods.checkPermissionStorage(MainActivity.this);
        if (!readPermission)
            return;
        mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(mIntent, Constants.REQUEST_CAMERA);
    }

    @OnClick(R.id.btn_upload_image)
    void uploadPhoto() {
        mUserChoosenTask = "Choose from Library";
        boolean readPermission= UtilityMethods.checkPermissionStorage(MainActivity.this);
        if (!readPermission)
            return;
        mIntent = new Intent();
        mIntent.setType("image/*");
        mIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(mIntent, "Select image"), Constants.UPLOAD_IMAGE);
    }
}
