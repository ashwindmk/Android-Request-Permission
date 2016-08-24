package com.example.ashwin.requestpermission;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Permision code that will be checked in the method onRequestPermissionsResult
    private final int CONTACTS_PERMISSION_CODE = 25;
    private final int SETTINGS_REQUEST_CODE = 1000;

    //shared preferences
    SharedPreferencesManager mSharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferencesManager = new SharedPreferencesManager(this);

    }

    public void requestDialog(View v) {

        //checking if already has the permission
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED){

            //already has permission
            Toast.makeText(MainActivity.this,"You already have the permission",Toast.LENGTH_LONG).show();
            mSharedPreferencesManager.setNeverAskForContactsPermission(false);

            //do things that required the permission

        } else {

            //ask for permission
            requestContactsPermission();

        }

    }

    //requesting permission
    private void requestContactsPermission(){

        if ( !mSharedPreferencesManager.getNeverAskForContactsPermission() ){
            //here you can explain why you need this permission

            //Note: this dialog is only for marshmallow and above users
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            //setting dialog title
            TextView title = new TextView(this);
            title.setText("Permission Request Title");
            title.setTextColor(Color.BLACK);
            title.setGravity(Gravity.CENTER);
            title.setPadding(10, 50, 10, 50);
            title.setTextSize(15);

            alertDialogBuilder.setCustomTitle(title);

            //setting dialog message
            TextView message = new TextView(this);
            message.setText("This is just a test app, we are not reading anything from your device. But be careful before you grant any permission to any app because they mostly transmit your data to some third party analytics. Make sure that your information will never be misused.");
            message.setTextColor(Color.BLACK);
            message.setPadding(25, 0, 25, 25);  //left, top, right, bottom
            message.setLineSpacing(0, 1.5f);    //sum, multiplier
            message.setTextSize(13);

            alertDialogBuilder.setView(message);

            //setting positive button
            alertDialogBuilder.setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.GET_ACCOUNTS}, CONTACTS_PERMISSION_CODE);
                }
            });

            //setting negative button
            alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();

            //showing alert dialog
            alertDialog.show();

            //changing button text colors
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#888888"));
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#888888"));

        } else {

            //Note: do not use this line as this might irritate the user every time after he/she has selected never ask again
            Toast.makeText(this, "You have selected to never ask again. But you can grant permission by clicking on Go To Settings > Permissions", Toast.LENGTH_LONG).show();

        }
    }

    //this method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //checking the request is code of our request
        if(requestCode == CONTACTS_PERMISSION_CODE){

            //if permission is granted else denied
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //permission is granted
                Toast.makeText(this,"Yippie permission granted",Toast.LENGTH_LONG).show();

                //do things that required the permission

            } else {

                //permission denied
                if( !ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.GET_ACCOUNTS) ) {
                    //permission denied with never ask again
                    Toast.makeText(this, "You have selected to never ask again. But you can grant permission by clicking on Go To Settings > Permissions", Toast.LENGTH_LONG).show();
                    mSharedPreferencesManager.setNeverAskForContactsPermission(true);
                } else {
                    Toast.makeText(this, "Oops permission denied", Toast.LENGTH_LONG).show();
                }

            }

        }

    }

    //go to settings
    public void goToSettings(View v) {

        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, SETTINGS_REQUEST_CODE);

    }

    //after returning from settings
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SETTINGS_REQUEST_CODE) {
            Toast.makeText(this, "You returned from settings", Toast.LENGTH_SHORT).show();

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED){
                mSharedPreferencesManager.setNeverAskForContactsPermission(false);
            }
        }

    }
}
