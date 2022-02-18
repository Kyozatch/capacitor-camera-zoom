package com.example.app;

import android.app.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class SplashScreenActivity extends Activity {

    private static final String TAG = "SplashScreenActivity";
    private final int PERMISSION_ALL = 0;

    private Thread bootErrorThread = new Thread(){
        @Override
        public void run() {
            try {
                Thread.sleep(Toast.LENGTH_LONG);
                finishAndRemoveTask();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);
        checkAndRequestAllPermissions();
    }

    private void checkAndRequestAllPermissions(){
        ArrayList<String> permissionsToValidate = new ArrayList<>();
        permissionsToValidate.add(Manifest.permission.CAMERA);
        permissionsToValidate.add(Manifest.permission.RECORD_AUDIO);

        String[] PERMISSIONS = permissionsToValidate.toArray(new String[0]);

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            startApp();
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ALL: {
                if (grantResults.length > 0) {
                    boolean allPermissionsGranted = true;
                    for(int i=0; i < grantResults.length && allPermissionsGranted; i++){
                        allPermissionsGranted = (grantResults[i] == PackageManager.PERMISSION_GRANTED);
                    }
                    if(allPermissionsGranted){
                        startApp();
                    } else {
                        Toast toast = Toast.makeText(this, getString(R.string.no_permissions), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        finishAndRemoveTask();
                    }
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void startApp(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}