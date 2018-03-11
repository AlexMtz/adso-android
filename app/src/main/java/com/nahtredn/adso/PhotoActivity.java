package com.nahtredn.adso;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PhotoActivity extends AppCompatActivity {
    private Button btnTakePhoto;
    private Button btnSelectPhoto;
    private InterstitialAd mInterstitialAd;
    private ImageView photo;

    private static final int TAKE_PHOTO_REQUEST_CODE = 1;
    private static final int SELECT_PHOTO_REQUEST_CODE = 2;
    private static final int CROP_PHOTO_REQUEST_CODE = 3;
    private boolean isCameraLaunched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        btnTakePhoto = findViewById(R.id.btn_take_picture_photo);
        btnTakePhoto.setEnabled(false);
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCameraLaunched = true;
                showInterstitial();
            }
        });

        btnSelectPhoto = findViewById(R.id.btn_select_picture_photo);
        btnSelectPhoto.setEnabled(false);
        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCameraLaunched = false;
                showInterstitial();
            }
        });

        mInterstitialAd = newInterstitialAd();
        loadInterstitial();

        photo = findViewById(R.id.image_photo);
        loadImage();
    }

    private void loadImage(){
        SharedPreferences prefs =
                getSharedPreferences("ProfilePreferences",Context.MODE_PRIVATE);

        String pathPhoto = prefs.getString("photo", null);
        if (pathPhoto != null){
            File imgFile = new  File(pathPhoto);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                photo.setImageBitmap(myBitmap);
            }
        } else {
            photo.setImageDrawable(getDrawable(R.drawable.ic_profile));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                btnTakePhoto.setEnabled(true);
                btnSelectPhoto.setEnabled(true);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                btnTakePhoto.setEnabled(true);
                btnSelectPhoto.setEnabled(true);
                launchActivityForUpdatePictureProfile();
            }

            @Override
            public void onAdClosed() {
                goToNextLevel();
                launchActivityForUpdatePictureProfile();
            }
        });
        return interstitialAd;
    }

    private void showInterstitial() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            goToNextLevel();
        }
    }

    private void loadInterstitial() {
        btnTakePhoto.setEnabled(false);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void goToNextLevel() {
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
    }

    private void launchActivityForUpdatePictureProfile() {
        if (isCameraLaunched) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST_CODE);
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, SELECT_PHOTO_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (TAKE_PHOTO_REQUEST_CODE):
                if (resultCode != RESULT_OK) {
                    return;
                }
                startCrop(data.getData());
                break;
            case (SELECT_PHOTO_REQUEST_CODE):
                if (resultCode != RESULT_OK) {
                    return;
                }
                startCrop(data.getData());
                break;
            case (CROP_PHOTO_REQUEST_CODE):
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                photo.setImageBitmap(imageBitmap);
                String fileName = saveImageFile(imageBitmap);
                saveImageUrl(fileName);
                break;
            default:
                break;
        }
    }

    /**
     * start Crop
     *
     * @param uri image uri
     */
    private void startCrop(Uri uri){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 4);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("scale", "true");
        intent.putExtra("return-data", true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intent, CROP_PHOTO_REQUEST_CODE);
    }

    public String saveImageFile(Bitmap bitmap) {
        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filename;
    }

    private String getFilename() {
        File file = new File(getFilesDir()
                .getPath(), "content");
        file.mkdirs();
        String uriSting = (file.getAbsolutePath() + "/"
                + "photo_profile.jpg");
        return uriSting;
    }

    private void saveImageUrl(String pathPhoto){
        SharedPreferences prefs =
                getSharedPreferences("ProfilePreferences", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("photo", pathPhoto);
        editor.apply();
    }
}
