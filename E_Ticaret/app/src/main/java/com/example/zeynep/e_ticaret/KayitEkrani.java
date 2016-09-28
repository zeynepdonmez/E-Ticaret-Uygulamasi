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

public class KayitEkrani extends AppCompatActivity {
    EditText adi, soyadi, tel, mail, sifre;
    SharedPreferences sha;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ekrani);
        adi = (EditText) findViewById(R.id.adi);
        soyadi = (EditText) findViewById(R.id.soyadi);
        tel = (EditText) findViewById(R.id.tel);
        mail = (EditText) findViewById(R.id.mail);
        sifre = (EditText) findViewById(R.id.sifre);
        sha = getSharedPreferences("ETICARETKULLANICI", MODE_PRIVATE);
        editor = sha.edit();
    }

    public void kayitOl(View v) {
        String kadi = adi.getText().toString().trim();
        String ksoyadi = soyadi.getText().toString().trim();
        String ktelefonu = tel.getText().toString().trim();
        String kmail = mail.getText().toString().trim();
        String ksifre = sifre.getText().toString().trim();
        if (adi.equals("")) {
            Toast.makeText(KayitEkrani.this, "Adınızı Giriniz ...", Toast.LENGTH_SHORT).show();
            adi.requestFocus();
        } else if (soyadi.equals("")) {
            Toast.makeText(KayitEkrani.this, " Soyadınızı Giriniz ...", Toast.LENGTH_SHORT).show();
            soyadi.requestFocus();
        } else if (tel.equals("")) {
            Toast.makeText(KayitEkrani.this, " Telefon Numaranızı Giriniz ...", Toast.LENGTH_SHORT).show();
            tel.requestFocus();
        } else if (mail.equals("")) {
            Toast.makeText(KayitEkrani.this, "Mailinizi Giriniz ...", Toast.LENGTH_SHORT).show();
            mail.requestFocus();
        } else if (sifre.equals("")) {
            Toast.makeText(KayitEkrani.this, " Şifrenizi Giriniz ...", Toast.LENGTH_SHORT).show();
            sifre.requestFocus();
        } else {
            String url = "http://jsonbulut.com/json/userRegister.php?ref=7b7392076900968d8e4ad78351ad55d3&userName=" + kadi + "&userSurname=" + ksoyadi + "&userPhone=" + ktelefonu + "&userMail=" + kmail + "&userPass=" + ksifre + "";
            new girisYap(url, this).execute();
        }

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
            // data çözümleme
            try {
                JSONObject obj = new JSONObject(data);
                JSONArray arr = obj.getJSONArray("user");
                JSONObject oj = arr.getJSONObject(0);
                //denetim yapılıyor
                if (oj.getBoolean("durum")) {
                    Toast.makeText(getApplication(), oj.getString("mesaj"), Toast.LENGTH_SHORT).show();
                    //kayıt başarılı aşağıdaki işlemleri yap
                    editor.putString("ID", oj.getString("kullaniciId"));
                    editor.putString("userName", adi.getText().toString().trim());
                    editor.putString("userSurname", soyadi.getText().toString().trim());
                    editor.putString("userEmail", mail.getText().toString().trim());
                    editor.putString("userPhone", tel.getText().toString().trim());
                    editor.commit();
                    finish();
                    Intent git = new Intent(KayitEkrani.this, AnaKategoriler.class);
                    startActivity(git);
                } else {
                    Toast.makeText(getApplication(), oj.getString("mesaj"), Toast.LENGTH_SHORT).show();
                    adi.setText("");
                    soyadi.setText("");
                    sifre.setText("");
                    tel.setText("");
                    mail.setText("");
                    adi.requestFocus();
                }
            } catch (JSONException e) {
                Log.d("Hata var:", e.toString());
            }
            Log.d("Gelen Data : ", data);
        }
    }

    public void kayitlikullanici(View v) {
        Intent in = new Intent(KayitEkrani.this, GirisEkrani.class);
        startActivity(in);
    }

}
