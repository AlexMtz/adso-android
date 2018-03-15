package com.nahtredn.adso;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PhotoActivity extends AppCompatActivity {
    private static final String photoPreferences = "photoPreferences";
    private static final String photoProperty = "photo";
    private static final String photoFileName = "profile_photo";
    private static final String photoFileExtension = "jpg";
    private static final String dirChild = "content";

    private InterstitialAd mInterstitialAd;
    private ImageView photo;

    private static final int TAKE_PHOTO_REQUEST_CODE = 1;
    private static final int SELECT_PHOTO_REQUEST_CODE = 2;
    private static final int CROP_PHOTO_REQUEST_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        AdView mAdView = findViewById(R.id.adViewPhoto);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = newInterstitialAd();
        loadInterstitial();

        photo = findViewById(R.id.image_photo);
        loadImage();
    }

    private void loadImage(){
        String pathPhoto = getImageUrl();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            this.finish();
            return true;
        }

        if (id == R.id.action_delete){
            saveImageUrl(null);
            photo.setImageDrawable(getDrawable(R.drawable.ic_profile));
            showMessage(getString(R.string.photo_profile_deleted_activity_photo));
            return true;
        }

        if (id == R.id.action_help){
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.commonAlertDialog));
            builder.setMessage(getString(R.string.help_photo_activity))
                    .setTitle(getString(R.string.help_title));
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClicButtonPhoto(View view){
        showInterstitial();
        if (view.getId() == R.id.btn_take_picture_photo){
            launchActivityForUpdatePictureProfile(true);
            return;
        }
        if (view.getId() == R.id.btn_select_picture_photo){
            launchActivityForUpdatePictureProfile(false);
            return;
        }
    }

    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
            }

            @Override
            public void onAdClosed() {
                reloadInterstitial();
            }
        });
        return interstitialAd;
    }

    private void showInterstitial() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            reloadInterstitial();
        }
    }

    private void loadInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void reloadInterstitial() {
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
    }

    private void launchActivityForUpdatePictureProfile(boolean takePicture) {
        if (takePicture) {
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
                if (resultCode != RESULT_OK){
                    return;
                }
                Bundle extras = data.getExtras();
                if (extras == null){
                    showMessage(getString(R.string.error_croped_photo));
                    return;
                }
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
        try {
            startActivityForResult(intent, CROP_PHOTO_REQUEST_CODE);
        } catch (SecurityException s){
            showMessage(getString(R.string.error_selected_photo));
        }
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
                .getPath(), dirChild);
        file.mkdirs();
        return  (file.getAbsolutePath() + "/" + photoFileName + "." + photoFileExtension);
    }

    private void saveImageUrl(String pathPhoto){
        SharedPreferences prefs = getSharedPreferences(photoPreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(photoProperty, pathPhoto);
        editor.apply();
        editor.clear();
    }

    private String getImageUrl(){
        SharedPreferences prefs = getSharedPreferences(photoPreferences,Context.MODE_PRIVATE);
        return prefs.getString(photoProperty, null);
    }

    private void showMessage(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
}
