package com.example.zeynep.e_ticaret;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListeleAdres extends AppCompatActivity {
    SharedPreferences sha;
    SharedPreferences.Editor edi;
    ListView list;
    TextView siladres;
    static ArrayList<String> KisininListelenmisAdresleri = new ArrayList<String>();
    static ArrayList<String> Adress = new ArrayList<String>();
    String silinececekAdresID = "";
    ArrayList<String> adresId = new ArrayList<String>();
    static String top = "";
    String silinecekadres = "";
    TextView alticizili;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listele_adres);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        siladres = (TextView) findViewById(R.id.siladres);
        alticizili = (TextView) findViewById(R.id.alticizili);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adresgetir();
        list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                silinececekAdresID = adresId.get(i);

                silinecekadres = Adress.get(i);


                //Log.d("TIKLANAN..", silinececekAdresID);
                // Log.d("SİLİNECEKADRES", silinecekadres);
                siladres.setText(silinecekadres);
                alticizili.setText("Silinecek Adresiniz");
                alticizili.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

                //Toast.makeText(ListeleAdres.this, silinececekAdresID, Toast.LENGTH_SHORT).show();
            }
        });
        sha = getSharedPreferences("ETICARETKULLANICI", MODE_PRIVATE);
        edi = sha.edit();

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
            Intent in = new Intent(ListeleAdres.this, Adresler.class);
            startActivity(in);
            return true;
        }
        if (id == R.id.ayarlar) {
            Intent in = new Intent(ListeleAdres.this, KullaniciAyarlar.class);
            startActivity(in);
            return true;
        }
        if (id == R.id.sepet) {
            Intent in = new Intent(ListeleAdres.this, Sepetim.class);
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
                    edi.remove("ID");
                    edi.remove("userName");
                    edi.remove("userSurname");
                    edi.remove("userEmail");
                    edi.remove("userPhone");
                    edi.commit();
                    Toast.makeText(ListeleAdres.this, "Çıkış Başarılı", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(ListeleAdres.this, GirisEkrani.class);
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
                JSONArray adresler = obj.getJSONArray("announcements").getJSONArray(0);
                for (int j = 0; j < adresler.length(); j++) {
                    JSONObject veri = adresler.getJSONObject(j);
                    String musteriid = veri.getString("musterilerID");
                    Log.d("HAY", musteriid);
                    String il = veri.getString("il");
                    String ilce = veri.getString("ilce");
                    String mahalle = veri.getString("Mahalle");
                    String adres = veri.getString("adres");
                    String kapiNo = veri.getString("kapiNo");
                    String not = veri.getString("not");
                    String tarih = veri.getString("tarih");
                    String adresid = veri.getString("id");
                    Log.d("Adres ID:", adresid);
                    adresId.add(adresid);
                    Log.d("ZEYNEP", adres);
                    Log.d("HAYDEEEE: ", il);
                    Log.d("hobaaa", mahalle);
                    top = il + " " + ilce + " " + mahalle + " " + adres + " " + kapiNo + " " + not + " " + tarih;
                    // Log.d("Adres : ", top);
                    String asd = sha.getString("ID", "");
                    if (musteriid.equals(asd)) {
                        Adress.add(top);
                        Log.d("zeynep:", top);
                        KisininListelenmisAdresleri.add(top);

                    }
                }
                ArrayAdapter<String> edepter = new ArrayAdapter<String>(ListeleAdres.this, R.layout.listelenmisadreslerrow, R.id.listelenmisadreslertxt, KisininListelenmisAdresleri);
                list.setAdapter(edepter);


            } catch (JSONException e) {
                if (e.toString().equals("org.json.JSONException: Value false at 0 of type java.lang.Boolean cannot be converted to JSONArray")) {
                    //Toast.makeText(ListeleAdres.this, "adres yok", Toast.LENGTH_SHORT).show();
                    // Log.d("Adres yok", e.toString());
                    siladres.setText("Kayıtlı Adresiniz Bulunmamaktadır!");

                    e.printStackTrace();
                }
            }
        }
    }

    public void adresSil(View v) {
        sha = getSharedPreferences("ETICARETKULLANICI", MODE_PRIVATE);
        final String kisiid = sha.getString("ID", "");
        if (silinececekAdresID.equals("")) {

            Toast.makeText(ListeleAdres.this, "Lütfen Silinecek veriyi seçiniz ...", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Adres Sil");
            builder.setIcon(R.drawable.sil);
            builder.setMessage("Adresinizi Silmek Üzeresiniz!");
            builder.setPositiveButton("Sil", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        String url = "http://jsonbulut.com/json/addressDelete.php?ref=7b7392076900968d8e4ad78351ad55d3&musterilerID=" + kisiid + "&adresID=" + silinececekAdresID;
                        Toast.makeText(ListeleAdres.this, "Adres Başarıyla Silindi...", Toast.LENGTH_SHORT).show();
                        silinececekAdresID = "";
                        new girisYap(url, ListeleAdres.this).execute();
                        finish();
                        Intent in = new Intent(ListeleAdres.this, ListeleAdres.class);
                        startActivity(in);
                    } catch (Exception ex) {
                        Log.d("Silme Hatası : ", ex.toString());
                    }
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    silinececekAdresID = "";
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void adresgetir() {
        KisininListelenmisAdresleri.clear();
        sha = getSharedPreferences("ETICARETKULLANICI", MODE_PRIVATE);
        String musteriid = sha.getString("ID", "");

        String url = "http://jsonbulut.com/json/addressList.php?ref=7b7392076900968d8e4ad78351ad55d3&musterilerID=" + musteriid;
        new girisYap(url, this).execute();


    }
}
