package com.chekoff.weatherforecast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends ActionBarActivity {
    SharedPreferences sharedPref;
    private ShareActionProvider mShareActionProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();

        }

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_main_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new
                ColorDrawable(Color.parseColor("#ffffff")));
        getSupportActionBar();

        sharedPref = this.getSharedPreferences("com.chekoff.weatherforecast", Context.MODE_PRIVATE);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weather, menu);

        /*item = menu.findItem(R.id.delete_curr_location);
        int currentLocationID = sharedPref.getInt("set_current_location_id", 0);
        if (currentLocationID != 0)
            item.setVisible(false);
        else
            item.setVisible(true);*/

        item = menu.findItem(R.id.share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        mShareActionProvider.setOnShareTargetSelectedListener(new ShareActionProvider.OnShareTargetSelectedListener() {
            @Override
            public boolean onShareTargetSelected(ShareActionProvider shareActionProvider, Intent intent) {
                mShareActionProvider.setShareIntent(bitmapIntent());

                //TODO Delete Shared File
                /*File file = new File(ih.getImgPath(id));
                boolean deleted = file.delete();*/
                return true;
            }
        });


        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "#SunshineApp");

        mShareActionProvider.setShareIntent(intent);
        //mShareActionProvider.setShareIntent(bitmapIntent());

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.putExtra(intent.EXTRA_TEXT, "Settings");
                startActivity(intent);
                break;

            case R.id.add_location:
                AddLocationFragment addLocationFragment = new AddLocationFragment();
                addLocationFragment.show(getFragmentManager(), "addLocation");
                break;

            case R.id.select_location:
                SelectLocationFragment selectLocationFragment = new SelectLocationFragment();
                selectLocationFragment.show(getFragmentManager(), "selectLocationFragment");
                break;

            case R.id.delete_curr_location:
                DeleteLocationFragment deleteLocationFragment = new DeleteLocationFragment();
                deleteLocationFragment.show(getFragmentManager(), "deleteLocationFragment");
                break;

            case R.id.share:
                mShareActionProvider.setShareIntent(bitmapIntent());
                break;

            case R.id.reload_all:
                createScreenshot();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem item;
        //item = menu.findItem(R.id.delete_curr_location);

        //item = menu.findItem(R.id.delete_curr_location);
        int currentLocationID = sharedPref.getInt("set_current_location_id", 0);
        if (currentLocationID != 0) {
            menu.findItem(R.id.delete_curr_location).setVisible(true);
            menu.findItem(R.id.select_location).setVisible(true);
            //item.setVisible(true);
        } else {
            menu.findItem(R.id.delete_curr_location).setVisible(false);
            menu.findItem(R.id.select_location).setVisible(false);
            //item.setVisible(false);
        }
        return true;
    }

    //custom methods


    public Intent bitmapIntent() {


        File imagePath = new File(getApplicationContext().getCacheDir(), "screenshots");
        File newFile = new File(imagePath, createScreenshot());
        Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.chekoff.weatherforecast", newFile);

        //Uri bmpUri = Uri.parse(contentUri);

        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, contentUri);

        return intent;
    }


    //***********************
    public static Bitmap captureScreen(View v) {

        Bitmap screenshot = null;
        try {

            if (v != null) {

                screenshot = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(screenshot);
                v.draw(canvas);
            }

        } catch (Exception e) {
            Log.d("ScreenShotActivity", "Failed to capture screenshot because:" + e.getMessage());
        }

        return screenshot;
    }

    public String saveImage(Bitmap bitmap) throws IOException {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 30, bytes);
        File cacheDir = new File(this.getCacheDir(), "screenshots");
        cacheDir.mkdirs();

        String fileName = "wf_" + System.currentTimeMillis() + ".png";

        File file = new File(cacheDir, fileName);
        file.createNewFile();
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(bytes.toByteArray());
        fo.close();

        return fileName;
    }

    public String createScreenshot() {
        LinearLayout linearLayout;
        String imagePath;


        linearLayout = (LinearLayout) findViewById(R.id.main_container);
        Bitmap myBitmap;

        //take screenshot
        View rootView = linearLayout.getRootView();
        rootView.setDrawingCacheEnabled(true);
        myBitmap = rootView.getDrawingCache();

        try {
            imagePath = saveImage(myBitmap);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        return imagePath;
    }
}
