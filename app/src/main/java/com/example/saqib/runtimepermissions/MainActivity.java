package com.example.saqib.runtimepermissions;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static final Integer CALL = 0x1;
    static final Integer WRITE_EXST = 0x2;
    static final Integer READ_EXST = 0x3;
    static final Integer CAMERA = 0x4;
    static final Integer ACCOUNTS = 0x5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void askForPermission(String permission, Integer requestCode){

        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED){
            // should we show n explanation

            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)){
                // this is called if user has denied the permission before
                // in this case i am just asking the permission again

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission},requestCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission},requestCode);
            }


        } else {
            Toast.makeText(this,"" + permission + " is already granted",Toast.LENGTH_LONG).show();
        }
    }

    public void ask(View v){
        switch (v.getId()){
            case R.id.call:
                askForPermission(Manifest.permission.CALL_PHONE,CALL);
                break;
            case R.id.write:
                askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE_EXST);
                break;
            case R.id.read:
                askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE,READ_EXST);
                break;
            case R.id.camera:
                askForPermission(Manifest.permission.CAMERA,CAMERA);
                break;
            case R.id.accounts:
                askForPermission(Manifest.permission.GET_ACCOUNTS,ACCOUNTS);
                break;
            default:
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {

                //Call
                case 1:
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + "{This is a telephone number}"));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        startActivity(callIntent);
                    }
                    break;
                //Write external Storage
                case 2:
                    break;
                //Read External Storage
                case 3:
                    Intent imageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(imageIntent, 11);
                    break;
                //Camera
                case 4:
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, 12);
                    }
                    break;
                //Accounts
                case 5:
                    AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
                    Account[] list = manager.getAccounts();
                    Toast.makeText(this,""+list[0].name,Toast.LENGTH_SHORT).show();
                    for(int i=0; i<list.length;i++){
                        Log.e("Account "+i,""+list[i].name);
                    }
            }

            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

}
