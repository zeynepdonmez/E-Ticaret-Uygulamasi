package com.example.zeynep.e_ticaret;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sha;
    SharedPreferences.Editor edi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }
/*
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.adresler) {
            return true;
        }
        if (id == R.id.ayarlar) {
            return true;
        }
        if (id == R.id.cikis) {
            edi.remove("ID");
            edi.remove("userName");
            edi.remove("userSurname");
            edi.remove("userEmail");
            edi.remove("userPhone");
            edi.commit();
            Toast.makeText(MainActivity.this, "Çıkış Başarılı", Toast.LENGTH_SHORT).show();
            Intent in=new Intent(MainActivity.this,GirisEkrani.class);
            startActivity(in);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
 */

}
