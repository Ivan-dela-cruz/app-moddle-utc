package co.desofsi.souriapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import co.desofsi.souriapp.content.HomeActivity;
import co.desofsi.souriapp.init.AuthActivity;
import co.desofsi.souriapp.init.OnBoardActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences userPref = getApplicationContext().getSharedPreferences("user",Context.MODE_PRIVATE);
                boolean isLoggedIn = userPref.getBoolean("isLoggedIn",false);
                if (isLoggedIn){
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                }else {
                    isFirstTime();
                }

            }
        }, 1550);

    }



    private void isFirstTime() {
        SharedPreferences preferences = getApplication().getSharedPreferences("onBoard", Context.MODE_PRIVATE);
        boolean isFirstTime = preferences.getBoolean("isFirstTime", true);
        if (isFirstTime) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirstTime", false);
            editor.apply();
            startActivity(new Intent(MainActivity.this, OnBoardActivity.class));
        } else {
            startActivity(new Intent(MainActivity.this, AuthActivity.class));
            finish();
        }
    }




}
