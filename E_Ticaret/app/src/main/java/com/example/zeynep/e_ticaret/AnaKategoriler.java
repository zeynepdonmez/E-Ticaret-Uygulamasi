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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;

public class AnaKategoriler extends AppCompatActivity {
    SharedPreferences sha;
    SharedPreferences.Editor edi;
    ListView list;
    ArrayList<String> kategoriAdi = new ArrayList<String>();
    String altkategoriID = "";
    String topcategori = "";
    String tik = "";
    String altkategoriIDD = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_kategoriler);
        list = (ListView) findViewById(R.id.ListView);
        sha = getSharedPreferences("ETICARETKULLANICI", MODE_PRIVATE);
        edi = sha.edit();
        String url = "http://jsonbulut.com/json/companyCategory.php?ref=7b7392076900968d8e4ad78351ad55d3";
        new girisYap(url, this).execute();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                tik = kategoriAdi.get(i);
                if (tik.equals("arabalar")) {
                    Intent git = new Intent(AnaKategoriler.this, altkategorilistele.class);
                    git.putExtra("altkategoriID", altkategoriID);
                    startActivity(git);
                }
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
            Intent in = new Intent(AnaKategoriler.this, Adresler.class);
            startActivity(in);
            return true;
        }
        if (id == R.id.ayarlar) {
            Intent in = new Intent(AnaKategoriler.this, KullaniciAyarlar.class);
            startActivity(in);
            return true;
        }
        if (id == R.id.sepet) {
            Intent in = new Intent(AnaKategoriler.this, Sepetim.class);
            startActivity(in);
            return true;
        }

        if (id == R.id.cikis) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Çıkış");
            builder.setMessage("Çıkmak Üzeresiniz!");
            builder.setIcon(R.drawable.cikacakmisin);
            builder.setPositiveButton("Çıkış Yap", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(AnaKategoriler.this, "Çıkışa Tıklandı", Toast.LENGTH_SHORT).show();
                    edi.remove("ID");
                    edi.remove("userName");
                    edi.remove("userSurname");
                    edi.remove("userEmail");
                    edi.remove("userPhone");
                    edi.commit();
                    Toast.makeText(AnaKategoriler.this, "Çıkış Başarılı", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(AnaKategoriler.this, GirisEkrani.class);
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

    public void girisYap(View v) {
        String url = "http://jsonbulut.com/json/companyCategory.php?ref=7b7392076900968d8e4ad78351ad55d3";
        new girisYap(url, this).execute();
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
                /*ls.clear();*/
                JSONObject obj = new JSONObject(data);
                JSONArray kategoriler = obj.getJSONArray("Kategoriler");
                JSONObject acategories = kategoriler.getJSONObject(0);
                JSONArray categories = acategories.getJSONArray("Categories");
                for (int j = 0; j < categories.length(); j++) {
                    topcategori = categories.getJSONObject(j).getString("TopCatogryId");
                    String anaKategoriAdi = categories.getJSONObject(j).getString("CatogryName");
                    //if (TopCatogryId.equals("0")) {
                    kategoriAdi.add(anaKategoriAdi);
                    if (topcategori.equals("0")) {
                        altkategoriID = categories.getJSONObject(j).getString("CatogryId");
                    } else if (topcategori.equals("56")) {
                        altkategoriIDD = categories.getJSONObject(j).getString("CatogryId");

                    }
                }

                ArrayAdapter<String> adp = new ArrayAdapter<String>(AnaKategoriler.this, R.layout.anakategorirow, R.id.anakategoritxt, kategoriAdi);
                list.setAdapter(adp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}