package com.example.zeynep.e_ticaret;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;

public class Adresler extends AppCompatActivity {
    SharedPreferences sha;
    SharedPreferences.Editor editor;
    EditText il, ilce, mahalle, adres, kapino, notbilgi;
    ListView adreslistesi;
    ArrayList<String> ADRESID = new ArrayList<>();
    String silinecekAdresID = "";
    String kisiID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adresler);

        sha = getSharedPreferences("ETICARETKULLANICI", MODE_PRIVATE);
        editor = sha.edit();
        String kisiID = sha.getString("ID", "");
        // Log.d("kişi id:", kisiID);
        il = (EditText) findViewById(R.id.il);
        ilce = (EditText) findViewById(R.id.ilce);
        mahalle = (EditText) findViewById(R.id.mahalle);
        adres = (EditText) findViewById(R.id.adres);
        kapino = (EditText) findViewById(R.id.kapino);
        notbilgi = (EditText) findViewById(R.id.notbilgi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.adresler) {

        }
        if (id == R.id.sepet) {
            Intent in = new Intent(Adresler.this, Sepetim.class);
            startActivity(in);


        }
        if (id == R.id.ayarlar) {
            Intent in = new Intent(Adresler.this, KullaniciAyarlar.class);
            startActivity(in);
            return true;

        }

        if (id == R.id.cikis) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Çıkış");
            builder.setIcon(R.drawable.cikacakmisin);
            builder.setMessage("Çıkmak Üzeresiniz!");
            builder.setPositiveButton("Çıkış Yap", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editor.remove("ID");
                    editor.remove("userName");
                    editor.remove("userSurname");
                    editor.remove("userEmail");
                    editor.remove("userPhone");
                    editor.commit();
                    Toast.makeText(Adresler.this, "Çıkış Başarılı", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(Adresler.this, GirisEkrani.class);
                    startActivity(in);
                    finish();

                }
            });
            builder.setNegativeButton("Sitede Kal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();

            switch (item.getItemId()) {
                case android.R.id.home:
                    if (getParentActivityIntent() == null) {
                        onBackPressed();
                    }
            }
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getParentActivityIntent() == null) {
                    onBackPressed();
                }
        }
        return super.onOptionsItemSelected(item);

    }


    class girisYap extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pro;
        String data = "";
        String url = "";

        public girisYap(String url, Activity ac) {
            this.url = url;
            pro = new ProgressDialog(ac);
            pro.setMessage("Lütfen Bekleyiniz ...");
            pro.show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... param) {
            try {
                data = Jsoup.connect(url).ignoreContentType(true).execute().body();
            } catch (Exception ex) {
                Log.d("Json hatası : ", ex.toString());
            } finally {
                pro.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void o) {
            super.onPostExecute(o);

            try {
                JSONObject obj = new JSONObject(data);


            } catch (JSONException e) {
                Log.d("Giriş hatası : ", e.toString());
            }

            Log.d("Gelen Data : ", data);
        }

    }

    public void adresEkle(View v) {
        String kisiID = sha.getString("ID", "");
        Log.d("kişi ıd:", kisiID);
        String kil = il.getText().toString().trim();
        String kilce = ilce.getText().toString().trim();
        String kmahalle = mahalle.getText().toString().trim();
        String kadres = adres.getText().toString().trim();
        String kkapino = kapino.getText().toString().trim();
        String knotbilgi = notbilgi.getText().toString().trim();
        // Toast.makeText(Adresler.this, kisiID, Toast.LENGTH_SHORT).show();
        if (kil.equals("") || kilce.equals("") || kmahalle.equals("") || kadres.equals("") || kapino.equals("") || knotbilgi.equals("")) {
            Toast.makeText(Adresler.this, "Tüm Alanları doldurunuz..", Toast.LENGTH_SHORT).show();
        } else {
            String url = "http://jsonbulut.com/json/addressAdd.php?ref=7b7392076900968d8e4ad78351ad55d3&musterilerID=" + kisiID + "&il=" + kil + "&ilce=" + kilce + "&Mahalle=" + kmahalle + "&adres=" + kadres + "&kapiNo=" + kkapino + "&notBilgi=" + knotbilgi + "";
            new girisYap(url, this).execute();
            Toast.makeText(Adresler.this, "Adres Eklendi..", Toast.LENGTH_SHORT).show();
            il.setText(null);
            ilce.setText(null);
            mahalle.setText(null);
            adres.setText(null);
            kapino.setText(null);
            notbilgi.setText(null);

        }
    }

    public void adreslistele(View v) {
        Intent in = new Intent(Adresler.this, ListeleAdres.class);
        startActivity(in);

    }


}

