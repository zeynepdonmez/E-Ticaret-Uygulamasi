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
import android.view.PointerIcon;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;

public class UrunDetay extends AppCompatActivity {
    SharedPreferences sha;
    SharedPreferences.Editor edit;
    ArrayList<String> urun = new ArrayList<>();
    ArrayList<String> urunresim = new ArrayList<>();
    ListView urundetaylstele;
    static ArrayList<urunOzellik> ozls = new ArrayList<>();
    static int secilen = 0;
    static ArrayList<String> ProductID = new ArrayList<>();
    static ArrayList<String> productnames = new ArrayList<>();
    static ArrayList<String> productprices = new ArrayList<>();
    ArrayList<String> anafotoarray = new ArrayList<>();
    static String productId = "";
    static String urunAdi = "";
    static String productprice = "";
    static String anafoto = "";
    TextView detay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_detay);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sha = getSharedPreferences("ETICARETKULLANICI", MODE_PRIVATE);
        edit = sha.edit();
        String altid = getIntent().getExtras().getString("detayiistenenurundetayi");
        urundetaylstele = (ListView) findViewById(R.id.urundetaylistele);
        detay = (TextView) findViewById(R.id.detay);
        String url = "http://jsonbulut.com/json/product.php?ref=7b7392076900968d8e4ad78351ad55d3&start=0&count=100&categoryId=" + altid + "";
        new anakategori(url, this).execute();


        urundetaylstele.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                productId = ProductID.get(i);
                productprice = productprices.get(i);
                urunAdi = productnames.get(i);
                anafoto = anafotoarray.get(i);
                Intent in = new Intent(UrunDetay.this, urunGoster.class);
                in.putExtra("urunid", productId);
                in.putExtra("urunadi", urunAdi);
                in.putExtra("urunfiyat", productprice);
                startActivity(in);
            }
        });

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
            Intent in = new Intent(UrunDetay.this, Adresler.class);
            startActivity(in);
        }
        if (id == R.id.sepet) {
            Intent in = new Intent(UrunDetay.this, Sepetim.class);
            startActivity(in);
        }
        if (id == R.id.ayarlar) {
            Intent in = new Intent(UrunDetay.this, KullaniciAyarlar.class);
            startActivity(in);
        }

        if (id == R.id.cikis) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Çıkış");
            builder.setIcon(R.drawable.cikacakmisin);
            builder.setMessage("Çıkmak Üzeresiniz!");
            builder.setPositiveButton("Çıkış Yap", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(UrunDetay.this, "Çıkışa Tıklandı", Toast.LENGTH_SHORT).show();
                    edit.remove("ID");
                    edit.remove("userName");
                    edit.remove("userSurname");
                    edit.remove("userEmail");
                    edit.remove("userPhone");
                    edit.commit();
                    Toast.makeText(UrunDetay.this, "Çıkış Başarılı", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(UrunDetay.this, GirisEkrani.class);
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


            try {
                ozls.clear();
                JSONObject obj = new JSONObject(data);
                JSONArray urunler = obj.getJSONArray("Products");
                JSONObject uurnler = urunler.getJSONObject(0);
                JSONArray bilgiler = uurnler.getJSONArray("bilgiler");

                for (int j = 0; j < bilgiler.length(); j++) {
                    JSONObject veri = bilgiler.getJSONObject(j);
                    String productname = veri.getString("productName");
                    Log.d("ürün başlık", productname);
                    urun.add(productname);
                    /*
                    String description = veri.getString("description");
                    String productId = veri.getString("productId");
                    String musteriid = veri.getString("musterilerID");
                    String price = veri.getString("price");
                        */

                    urunOzellik uoz = new urunOzellik();
                    uoz.setProductId(veri.getString("productId"));
                    productId = veri.getString("productId");
                    ProductID.add(productId);
                    uoz.setProductName(veri.getString("productName"));
                    urunAdi = veri.getString("productName");
                    productnames.add(productname);

                    uoz.setBrief(veri.getString("brief"));
                    uoz.setDescription(veri.getString("description"));
                    uoz.setPrice(veri.getString("price"));
                    productprice = veri.getString("price");

                    productprices.add(productprice);

                    //  String a = veri.getString("productName") + "\n " + veri.getString("description") + "\n" + veri.getString("price");
                    // urun.add((veri.getString("description")));
                    //urun.add(a);


                    if (veri.getBoolean("image")) {
                        String rurl = veri.getJSONArray("images").getJSONObject(0).getString("normal");

                        uoz.setResim(rurl);
                        anafotoarray.add(rurl);

                    } else {
                        uoz.setResim("http://www.resimkursu.com.tr/wp-content/uploads/2016/03/resim-kursu.jpg");
                    }

                    ozls.add(uoz);

                }
                urunadapter adp = new urunadapter(UrunDetay.this, ozls);
                urundetaylstele.setAdapter(adp);
                // ArrayAdapter<String> adp = new ArrayAdapter<>(UrunDetay.this, R.layout.urundetayrow, R.id.urundetaytxt, urun);
                //urundetaylstele.setAdapter(adp);


            } catch (JSONException e) {
                e.printStackTrace();
                if (e.toString().equals("org.json.JSONException: Value null at bilgiler of type org.json.JSONObject$1 cannot be converted to JSONArray")) {
                    detay.setText("Gösterilecek Ürün Bulunamadı!");

                }
            }
        }


    }
}
