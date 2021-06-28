package com.ejemplo.appdocente.Util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by JC on 21/2/2018.
 */

public class CheckPermission extends AppCompatActivity {

    private static final int READ_SMS = 100;
    private static final int RECEIVE_SMS = 101;
    private Context cont;
    private Activity act;

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode, Context context, Activity activity) {
        cont = context;
        act = activity;
        if (ContextCompat.checkSelfPermission(cont, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(act,
                    new String[]{permission},
                    requestCode);
        } else {
            Toast.makeText(act,
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        {
            super
                    .onRequestPermissionsResult(requestCode,
                            permissions,
                            grantResults);

            if (requestCode == READ_SMS) {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(cont,
                            "Camera Permission Granted",
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Camera Permission Denied",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            } else if (requestCode == RECEIVE_SMS) {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(cont,
                            "Storage Permission Granted",
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(cont,
                            "Storage Permission Denied",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }

    }
}
