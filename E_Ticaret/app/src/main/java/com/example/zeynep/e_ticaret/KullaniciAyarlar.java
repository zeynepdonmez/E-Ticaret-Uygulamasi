package com.example.zeynep.e_ticaret;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

public class KullaniciAyarlar extends AppCompatActivity {
    EditText adi, soyadi, mail, telefon, sifre, sifretekrar;
    SharedPreferences sha;
    SharedPreferences.Editor editor;
    static String kid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici_ayarlar);
        adi = (EditText) findViewById(R.id.adi);
        soyadi = (EditText) findViewById(R.id.soyadi);
        mail = (EditText) findViewById(R.id.mail);
        telefon = (EditText) findViewById(R.id.telefon);
        sifre = (EditText) findViewById(R.id.sifre);
        sifretekrar = (EditText) findViewById(R.id.tekrarsifre);
        sha = getSharedPreferences("ETICARETKULLANICI", MODE_PRIVATE);
        editor = sha.edit();
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
            Intent in = new Intent(KullaniciAyarlar.this, Adresler.class);
            startActivity(in);
        }
        if (id == R.id.sepetim) {
            Intent in = new Intent(KullaniciAyarlar.this, Sepetim.class);
            startActivity(in);
        }
        if (id == R.id.ayarlar) {
        }
        if (id == R.id.cikis) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Çıkış");
            builder.setIcon(R.drawable.cikacakmisin);
            builder.setMessage("Çıkmak Üzeresiniz!");
            builder.setPositiveButton("Çıkış Yap", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Toast.makeText(KullaniciAyarlar.this, "Çıkışa Tıklandı", Toast.LENGTH_SHORT).show();
                    editor.remove("ID");
                    editor.remove("userName");
                    editor.remove("userSurname");
                    editor.remove("userEmail");
                    editor.remove("userPhone");
                    editor.commit();
                    Toast.makeText(KullaniciAyarlar.this, "Çıkış Başarılı", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(KullaniciAyarlar.this, GirisEkrani.class);
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void guncelle(View v) {
        String kadi = adi.getText().toString().trim();
        String ksoyadi = soyadi.getText().toString();
        String kmail = mail.getText().toString().trim();
        String ktelefonu = telefon.getText().toString().trim();
        String ksifre = sifre.getText().toString().trim();
        String ksifretekrar = sifretekrar.getText().toString().trim();
        kid = sha.getString("ID", "");
        // Log.d("KULLANICI İD:", kid);
        // Toast.makeText(KullaniciAyarlar.this, kid, Toast.LENGTH_SHORT).show();
        //Toast.makeText(KullaniciAyarlar.this, kmail, Toast.LENGTH_SHORT).show();

        if (kadi.equals("")) {
            Toast.makeText(KullaniciAyarlar.this, "Adınızı Giriniz ...", Toast.LENGTH_SHORT).show();
            adi.requestFocus();
        } else if (ksoyadi.equals("")) {
            Toast.makeText(KullaniciAyarlar.this, " Soyadınızı Giriniz ...", Toast.LENGTH_SHORT).show();
            soyadi.requestFocus();
        } else if (kmail.equals("")) {
            Toast.makeText(KullaniciAyarlar.this, "Mailinizi Giriniz ...", Toast.LENGTH_SHORT).show();
            mail.requestFocus();
        } else if (ktelefonu.equals("")) {
            Toast.makeText(KullaniciAyarlar.this, " Telefon Numaranızı Giriniz ...", Toast.LENGTH_SHORT).show();
            telefon.requestFocus();
        } else if (ksifre.equals("")) {
            Toast.makeText(KullaniciAyarlar.this, " Şifrenizi Giriniz ...", Toast.LENGTH_SHORT).show();
            sifre.requestFocus();
        } else if (!ksifretekrar.equals(ksifre)) {
            Toast.makeText(KullaniciAyarlar.this, "Şifreniz Uyuşmuyor", Toast.LENGTH_SHORT).show();
            sifretekrar.requestFocus();
        } else {
            String url = "http://jsonbulut.com/json/userSettings.php?ref=7b7392076900968d8e4ad78351ad55d3&userName=" + kadi + "&userSurname=" + ksoyadi + "&userMail=" + kmail + ".com&userPhone=" + ktelefonu + "&userPass=" + ksifre + "&userId=" + kid;
            new girisYap(url, this).execute();
            adi.setText("");
            soyadi.setText("");
            mail.setText("");
            telefon.setText("");
            sifre.setText("");
            sifretekrar.setText("");
            Intent in = new Intent(KullaniciAyarlar.this, AnaKategoriler.class);
            startActivity(in);

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

            try {
                JSONObject obj = new JSONObject(data);
                JSONArray arr = obj.getJSONArray("user");
                JSONObject oj = arr.getJSONObject(0);
                if (oj.getBoolean("durum")) {
                    Toast.makeText(getApplication(), oj.getString("mesaj"), Toast.LENGTH_SHORT).show();
                    editor.putString("ID", oj.getString("kullaniciId"));
                    editor.putString("userName", adi.getText().toString().trim());
                    editor.putString("userSurname", soyadi.getText().toString().trim());
                    editor.putString("userEmail", mail.getText().toString().trim());
                    editor.putString("userPhone", telefon.getText().toString().trim());
                    editor.commit();
                    // Log.d("Giriş ID : ", oj.getString("kullaniciId"));
                    Intent git = new Intent(KullaniciAyarlar.this, AnaKategoriler.class);
                    startActivity(git);
                } else {
                    Toast.makeText(getApplication(), oj.getString("mesaj"), Toast.LENGTH_SHORT).show();
                    adi.setText("");
                    soyadi.setText("");
                    mail.setText("");
                    sifretekrar.setText("");
                    sifre.setText("");
                    adi.requestFocus();
                    Intent git = new Intent(KullaniciAyarlar.this, KullaniciAyarlar.class);
                    startActivity(git);


                }
            } catch (JSONException e) {
                Log.d("Hata var:", e.toString());
            }
            Log.d("Gelen Data : ", data);
        }
    }


    public void uygulamacikis(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Çıkış");
        builder.setIcon(R.drawable.cikacakmisin);
        builder.setMessage("Çıkmak Üzeresiniz!");
        builder.setPositiveButton("Çıkış Yap", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(KullaniciAyarlar.this, "Çıkışa Tıklandı", Toast.LENGTH_SHORT).show();
                editor.remove("ID");
                editor.remove("userName");
                editor.remove("userSurname");
                editor.remove("userEmail");
                editor.remove("userPhone");
                editor.commit();
                Toast.makeText(KullaniciAyarlar.this, "Çıkış Başarılı", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(KullaniciAyarlar.this, GirisEkrani.class);
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


    }

    public void geri(View v) {
        Intent in = new Intent(KullaniciAyarlar.this, AnaKategoriler.class);
        startActivity(in);
    }
}
