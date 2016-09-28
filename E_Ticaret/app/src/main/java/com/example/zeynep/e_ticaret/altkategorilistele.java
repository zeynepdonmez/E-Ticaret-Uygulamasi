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

public class altkategorilistele extends AppCompatActivity {
    SharedPreferences sha;
    SharedPreferences.Editor edit;
    ListView list;
    ArrayList<String> kategoriID = new ArrayList<String>();
    ArrayList<String> kategoriAdi = new ArrayList<String>();
    String altkategoriID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altkategorilistele);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list = (ListView) findViewById(R.id.altkatliste);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String altkategoriid = kategoriID.get(i);
                // Toast.makeText(altkategorilistele.this, altkategoriid, Toast.LENGTH_SHORT).show();
                Intent in = new Intent(altkategorilistele.this, UrunDetay.class);
                in.putExtra("detayiistenenurundetayi", altkategoriid);
                startActivity(in);
            }
        });
        sha = getSharedPreferences("ETICARETKULLANICI", MODE_PRIVATE);
        edit = sha.edit();

        String url = "http://jsonbulut.com/json/companyCategory.php?ref=7b7392076900968d8e4ad78351ad55d3";
        new anakategori(url, this).execute();
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
            Intent in = new Intent(altkategorilistele.this, Adresler.class);
            startActivity(in);

        }
        if (id == R.id.ayarlar) {
            Intent in = new Intent(altkategorilistele.this, KullaniciAyarlar.class);
            startActivity(in);
            return true;
        }
        if (id == R.id.sepet) {
            Intent in = new Intent(altkategorilistele.this, Sepetim.class);
            startActivity(in);
            return true;
        }

        if (id == R.id.cikis) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Çıkış");
            builder.setIcon(R.drawable.cikacakmisin);
            builder.setMessage("Çıkmak Üzeresiniz!");
            builder.setPositiveButton("Çıkış Yap", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    edit.remove("ID");
                    edit.remove("userName");
                    edit.remove("userSurname");
                    edit.remove("userEmail");
                    edit.remove("userPhone");
                    edit.commit();
                    Toast.makeText(altkategorilistele.this, "Çıkış Başarılı", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(altkategorilistele.this, GirisEkrani.class);
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
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getParentActivityIntent() == null) {
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);

    }


    class anakategori extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pro;
        String data = "";
        String url = "";

        public anakategori(String url, Activity ac) {
            this.url = url;
            pro = new ProgressDialog(ac);
            pro.setMessage("Lütfen Bekleyiniz !");
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
                Log.d("Json Hatası ", ex.toString());
            } finally {
                pro.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void o) {
            super.onPostExecute(o);
            String gelen = getIntent().getExtras().getString("altkategoriID", "");
           // Toast.makeText(altkategorilistele.this, gelen, Toast.LENGTH_SHORT).show();
            try {

                JSONObject obj = new JSONObject(data);
                JSONArray kategoriler = obj.getJSONArray("Kategoriler");
                JSONObject categoriess = kategoriler.getJSONObject(0);
                JSONArray categories = categoriess.getJSONArray("Categories");
                for (int j = 0; j < categories.length(); j++) {
                    String TopCatogryId = categories.getJSONObject(j).getString("TopCatogryId");
                    String CatogryName = categories.getJSONObject(j).getString("CatogryName");


                    if (TopCatogryId.equals(gelen)) {
                        kategoriAdi.add(CatogryName);
                        altkategoriID = categories.getJSONObject(j).getString("CatogryId");
                        kategoriID.add(altkategoriID);
                    }
                }

                ArrayAdapter<String> adp = new ArrayAdapter<String>(altkategorilistele.this, R.layout.altkategorirow, R.id.altkategoritxt, kategoriAdi);
                list.setAdapter(adp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
