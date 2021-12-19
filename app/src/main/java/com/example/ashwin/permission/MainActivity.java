package com.example.ashwin.permission;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {
    private static final int CONTACTS_PERMISSION_CODE = 25;
    private static final int SETTINGS_REQUEST_CODE = 1000;

    SharedPreferencesManager mSharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferencesManager = new SharedPreferencesManager(this);
    }

    public void requestDialog(View v) {
        // Check if already has the permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {
            // Already has permission
            Toast.makeText(MainActivity.this,"You already have the permission",Toast.LENGTH_LONG).show();
            mSharedPreferencesManager.setNeverAskForContactsPermission(false);

            // Do things that required the permission
        } else {
            // Ask for permission
            requestContactsPermission();
        }
    }

    // requesting permission
    private void requestContactsPermission(){
        if (!mSharedPreferencesManager.getNeverAskForContactsPermission()) {
            // Here you can explain why you need this permission

            // Note: this dialog is only for marshmallow and above users
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // Set dialog title
            TextView title = new TextView(this);
            title.setText("Permission Request Title");
            title.setTextColor(Color.BLACK);
            title.setGravity(Gravity.CENTER);
            title.setPadding(10, 50, 10, 50);
            title.setTextSize(15);

            alertDialogBuilder.setCustomTitle(title);

            // Set dialog message
            TextView message = new TextView(this);
            message.setText("This is just a test app, we are not reading anything from your device. But be careful before you grant any permission to any app because they mostly transmit your data to some third party analytics. Make sure that your information will never be misused.");
            message.setTextColor(Color.BLACK);
            message.setPadding(25, 0, 25, 25);  //left, top, right, bottom
            message.setLineSpacing(0, 1.5f);    //sum, multiplier
            message.setTextSize(13);

            alertDialogBuilder.setView(message);
            alertDialogBuilder.setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.GET_ACCOUNTS}, CONTACTS_PERMISSION_CODE);
                }
            });
            alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#888888"));
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#888888"));
        } else {
            // Note: do not use this line as this might irritate the user every time after he/she has selected never ask again
            Toast.makeText(this, "You have selected to never ask again. But you can grant permission by clicking on Go To Settings > Permissions", Toast.LENGTH_LONG).show();
        }
    }

    // This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // checking the request is code of our request
        if (requestCode == CONTACTS_PERMISSION_CODE) {

            // if permission is granted else denied
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission is granted
                Toast.makeText(this,"Yippie permission granted",Toast.LENGTH_LONG).show();

                // do things that required the permission
            } else {
                // permission denied
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.GET_ACCOUNTS)) {
                    // permission denied with never ask again
                    Toast.makeText(this, "You have selected to never ask again. But you can grant permission by clicking on Go To Settings > Permissions", Toast.LENGTH_LONG).show();
                    mSharedPreferencesManager.setNeverAskForContactsPermission(true);
                } else {
                    Toast.makeText(this, "Oops permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    // Go to settings
    public void goToSettings(View v) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, SETTINGS_REQUEST_CODE);
    }

    // After returning from settings
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST_CODE) {
            Toast.makeText(this, "You returned from settings", Toast.LENGTH_SHORT).show();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {
                mSharedPreferencesManager.setNeverAskForContactsPermission(false);
            }
        }
    }
}
