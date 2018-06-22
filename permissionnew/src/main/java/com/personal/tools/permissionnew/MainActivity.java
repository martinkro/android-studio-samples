package com.personal.tools.permissionnew;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.List;
//import android.os.ServiceManager;

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener{
    // Remove the below line after defining your own ad unit ID.
    private static final String TOAST_TEXT = "Test ads are being shown. "
            + "To show live ads, replace the ad unit ID in res/values/strings.xml with your own ad unit ID.";
    public static final String LOG_TAG = "permissionnew";
    private Camera camera;
    private int cameraId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, getResources().getString(R.string.admob_app_id));

        // Load an ad into the AdMob banner view.
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // Toasts the test ad message on the screen. Remove this after defining your own ad unit ID.
        // Toast.makeText(this, TOAST_TEXT, Toast.LENGTH_LONG).show();

        //getApplicationContext().getUserId();
        try{
            int[] gids = getPackageManager().getPackageGids("com.personal.tools.permissionnew");
            for(int gid:gids){
                Log.e("PermissionNew","GID:" + gid);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        if (!getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
                    .show();
        } else {
            cameraId = findFrontFacingCamera();
            if (cameraId < 0) {
                Toast.makeText(this, "No front facing camera found.",
                        Toast.LENGTH_LONG).show();
            } else {
                camera = Camera.open(cameraId);
            }
        }

        findViewById(R.id.getInstalledPackages).setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Log.d(LOG_TAG, "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    @Override
    protected void onPause() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
        super.onPause();
    }

    @Override
    public  void onClick(View v){
        switch(v.getId()){
            case R.id.getInstalledPackages:
                getInstalledPackages();
                break;
            default:
                break;
        }
    }

    private void getInstalledPackages(){
        Log.d(LOG_TAG,"---getInstalledPackages start...");
        PackageManager manager = getPackageManager();
        List<PackageInfo> pkgs = manager.getInstalledPackages(PackageManager.GET_PERMISSIONS);
        for(PackageInfo pkg:pkgs){
            if((pkg.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0){
                Log.d(LOG_TAG,"package name:" + pkg.packageName);
            }
            else{
                Log.d(LOG_TAG,"system package name:" + pkg.packageName);
            }
        }


        //getSystemService(Context.PAC);
        //Log.d(LOG_TAG, "exec pm list packages");
        //String out = executeCmdDirect("pm list packages -3");
        //Log.d(LOG_TAG, "out:" + out);

      // String out = executeCmdDirect("pm uninstall com.personal.tools.permissionold");
       // Log.d(LOG_TAG, "out1:" + out);

//        Log.d(LOG_TAG, "exec pm list packages");
//        String out1= executeCmdDirect("dumpsys activity top");
//        Log.d(LOG_TAG, "out1:" + out1);

        Log.d(LOG_TAG,"---getInstalledPackages END");
    }

    private void pm(){
        try {
            Class<?> ServiceManager = Class.forName("android.os.ServiceManager");
            Method getService = ServiceManager.getMethod("getService",java.lang.String.class);
            Object remoteService = getService.invoke(null,"packages");
        }catch (Exception e){
            e.printStackTrace();;
        }
    }

    private static String executeCmdDirect(String sCmd)
    {
        try{

            Process process = Runtime.getRuntime().exec(sCmd);



            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            int read = 0;

            BufferedReader errorreader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()));


            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();


            //char[] buffer = new char[4096];
            StringBuffer err = new StringBuffer();
            while ((read = errorreader.read(buffer)) > 0) {
                err.append(buffer, 0, read);
            }
            errorreader.close();


            // Waits for the command to finish.

            process.waitFor();


            if (output!= null && output.toString().trim().length()>0)
            {
                return output.toString();
            }
            else if (err!= null && err.toString().trim().length()>0)
            {
                return err.toString();
            }
            else
            {
                return "";
            }

        }catch(Throwable e){

            e.printStackTrace();
            return "";
        }
    }


}
