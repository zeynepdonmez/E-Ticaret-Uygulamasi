package com.example.zeynep.e_ticaret;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

public class GirisEkrani extends AppCompatActivity {
    SharedPreferences sha;
    SharedPreferences.Editor edi;
    EditText mail, sifre;
    static String girisID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_ekrani);
        mail = (EditText) findViewById(R.id.mail);
        sifre = (EditText) findViewById(R.id.sifre);
        sha = getSharedPreferences("ETICARETKULLANICI", MODE_PRIVATE);
        edi = sha.edit();
        String kontrol = sha.getString("ID", "");
        if (!kontrol.equals("")) {
            Intent git = new Intent(GirisEkrani.this, AnaKategoriler.class);
            startActivity(git);
        }
    }

    public void girisYap(View v) {

        String ma = mail.getText().toString().trim();
        String si = sifre.getText().toString().trim();
        if (ma.equals("")) {
            Toast.makeText(GirisEkrani.this, "Lütfen Mailinizi Giriniz ...", Toast.LENGTH_SHORT).show();

            mail.requestFocus();
        } else if (si.equals("")) {
            Toast.makeText(GirisEkrani.this, "Lütfen Şifrenizi Giriniz ...", Toast.LENGTH_SHORT).show();
            sifre.requestFocus();
        } else {
            String url = "http://jsonbulut.com/json/userLogin.php?ref=7b7392076900968d8e4ad78351ad55d3&userEmail=" + ma + "&userPass=" + si + "&face=no";
            new girisYap(url, this).execute();
        }

    }

    public void kayitOl(View v) {
        Intent git = new Intent(GirisEkrani.this, KayitEkrani.class);
        startActivity(git);
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
                JSONArray arr = obj.getJSONArray("user");
                JSONObject oj = arr.getJSONObject(0);
                if (oj.getBoolean("durum")) {
                    Toast.makeText(getApplication(), oj.getString("mesaj"), Toast.LENGTH_SHORT).show();
                    JSONObject dt = oj.getJSONObject("bilgiler");
                    edi.putString("ID", dt.getString("userId"));
                    edi.putString("userName", dt.getString("userName"));
                    edi.putString("userSurname", dt.getString("userSurname"));
                    edi.putString("userEmail", dt.getString("userEmail"));
                    edi.putString("userPhone", dt.getString("userPhone"));
                    edi.commit();
                    Log.d("Giriş ID : ", dt.getString("userId"));
                    String girisID = dt.getString("userId");

                    finish();
                    Intent git = new Intent(GirisEkrani.this, AnaKategoriler.class);
                    git.putExtra("girisID", dt.getString("userId"));
                    git.putExtra("ADI", dt.getString("userName"));
                    startActivity(git);
                } else {
                    Toast.makeText(getApplication(), oj.getString("mesaj"), Toast.LENGTH_SHORT).show();
                    mail.setText(null);
                    sifre.setText(null);
                    mail.requestFocus();
                }
            } catch (JSONException e) {
                Log.d("Giriş hatası : ", e.toString());
            }

            Log.d("Gelen Data : ", data);
        }
    }

}
