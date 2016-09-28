package com.example.zeynep.e_ticaret;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

public class Sepetim extends AppCompatActivity {
    DB db = new DB(this);
    SharedPreferences sha;
    SharedPreferences.Editor editor;
    TextView toplam;
    Button tamamla;
    Button sil;
    ArrayList<String> Siparisdetay = new ArrayList<>();
    TextView siparissil;
    ArrayList<String> Silineceksiparisler = new ArrayList<>();
    ArrayList<String> urunidler = new ArrayList<>();
    List<urunOzellik> ls = new ArrayList<>();
    ArrayList<String> siparisidler = new ArrayList<>();
    ArrayList<String> siparisfiyatlar = new ArrayList<>();
    ArrayList<String> siparisurunadi = new ArrayList<>();
    ArrayList<String> siparisurunadii = new ArrayList<>();
    ArrayList<String> siparisiverilecekler = new ArrayList<>();
    static  String siparistoplam;


    static String siparissilID = "";
    static String siparisid = "";
    static String urunid = "";
    static String urunadi = "";
    static String urunadii = "";
    static String siparisiverilecekurunler = "";
    ListView list;
    String musteriid = "";
    static String siparisfiyat = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sepetim);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sha = getSharedPreferences("ETICARETKULLANICI", MODE_PRIVATE);
        editor = sha.edit();
        tamamla = (Button) findViewById(R.id.tamamla);
        sil = (Button) findViewById(R.id.siparissil);
        musteriid = sha.getString("ID", "");
        toplam = (TextView) findViewById(R.id.toplamfiyat);
        list = (ListView) findViewById(R.id.sepetimdekilerlist);
        siparisgetir();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                siparissilID = Silineceksiparisler.get(i);
                urunid = urunidler.get(i);
                siparisid = siparisidler.get(i);
                siparisfiyat = siparisfiyatlar.get(i);
                urunadi = siparisurunadi.get(i);
                urunadii = siparisurunadi.get(i);
                siparistoplam = siparisfiyatlar.get(i);
                if (siparisurunadi.equals("")) {
                    toplam.setText("Sepetinizde Ürün Bulunmamaktadır..");
                } else
                    toplam.setText("İptal Edilecek Ürün > " + urunadi + "  \n" + "Fiyat >  " + siparistoplam + " TL");

                //siparissil.setText("Ürün Adı:" + urunadii + "  \n" + "Ürün Fiyatı:  " + siparistoplam + "TL");
                // Toast.makeText(Sepetim.this, "Ürünün fiyatı:" + siparisfiyat, Toast.LENGTH_SHORT).show();
                //Toast.makeText(Sepetim.this, urunid, Toast.LENGTH_SHORT).show();
                //Toast.makeText(Sepetim.this, "Sipariş ID:" + siparisid, Toast.LENGTH_SHORT).show();
            }
        });


        tamamla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String siparisiverilenler = "", siparisVerilenUrunler = "";
                for (int j = 0; j < urunidler.size(); j++) {
                    siparisiverilenler += urunidler.get(j).toString() + ",";
                }
                if (!siparisiverilenler.equals("")) {
                    siparisVerilenUrunler = siparisiverilenler.substring(0, siparisiverilenler.length() - 1);
                    //Log.d("urunler : ", siparisVerilenUrunler);
                    String url = "http://jsonbulut.com/json/orderForm.php?ref=7b7392076900968d8e4ad78351ad55d3&customerId=" + musteriid + "&productId=" + siparisVerilenUrunler + "&html=" + siparisVerilenUrunler;
                    new siparisTamamlama(url, Sepetim.this).execute();
                    Toast.makeText(Sepetim.this, "Siparişiniz Ulaşmıştır..", Toast.LENGTH_SHORT).show();
                }
            }
        });

/*
siparissil.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (siparissilID.equals("")) {
        } else {
            sha = getSharedPreferences("ETICARETKULLANICI", MODE_PRIVATE);
            db.sil("sepet", "siparisid", siparissilID);
            siparissilID = "";
            finish();
            Intent i = new Intent(Sepetim.this, Sepetim.class);
            startActivity(i);
            Toast.makeText(Sepetim.this, "Sipariş Kaldırıldı...", Toast.LENGTH_SHORT).show();
        }

    }
});
 */




        /*
         list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                siparissilID = Silineceksiparisler.get(i);
                urunid = urunidler.get(i);
                siparisid = siparisidler.get(i);
                siparisfiyat = siparisfiyatlar.get(i);
                urunadi = siparisurunadi.get(i);
                urunadii = siparisurunadi.get(i);
                String siparistoplam = siparisfiyatlar.get(i);
                if (siparisurunadi.equals("")) {
                    toplam.setText("Sepetinizde Ürün Bulunmamaktadır..");
                } else
                    toplam.setText("Ürün Adı:" + urunadi + "  \n" + "Ürün Fiyatı:  " + siparistoplam + "TL");

                //siparissil.setText("Ürün Adı:" + urunadii + "  \n" + "Ürün Fiyatı:  " + siparistoplam + "TL");
                // Toast.makeText(Sepetim.this, "Ürünün fiyatı:" + siparisfiyat, Toast.LENGTH_SHORT).show();
                //Toast.makeText(Sepetim.this, urunid, Toast.LENGTH_SHORT).show();
                //Toast.makeText(Sepetim.this, "Sipariş ID:" + siparisid, Toast.LENGTH_SHORT).show();
            }
        });
        siparisgetir();

    }
         */
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
            Intent in = new Intent(Sepetim.this, Adresler.class);
            startActivity(in);

        }
        if (id == R.id.sepet) {


        }
        if (id == R.id.ayarlar) {
            Intent in = new Intent(Sepetim.this, KullaniciAyarlar.class);
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
                    Toast.makeText(Sepetim.this, "Çıkışa Tıklandı", Toast.LENGTH_SHORT).show();
                    editor.remove("ID");
                    editor.remove("userName");
                    editor.remove("userSurname");
                    editor.remove("userEmail");
                    editor.remove("userPhone");
                    editor.commit();
                    Toast.makeText(Sepetim.this, "Çıkış Başarılı", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(Sepetim.this, GirisEkrani.class);
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


    public void siparissil(View v) {
        if (siparissilID.equals("")) {
            Toast.makeText(Sepetim.this, "Silinecek Siparişinizi Seçiniz", Toast.LENGTH_SHORT).show();
        } else if (!siparissilID.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Sipariş İptali");
            builder.setIcon(R.drawable.cancel);
            builder.setMessage("Sipariş İptal Edilsin mi?");
            builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sha = getSharedPreferences("ETICARETKULLANICI", MODE_PRIVATE);
                    db.sil("sepet", "siparisid", siparissilID);
                    siparissilID = "";
                    finish();
                    Intent i = new Intent(Sepetim.this, Sepetim.class);
                    startActivity(i);
                    Toast.makeText(Sepetim.this, "Siparişiniz Kaldırıldı...", Toast.LENGTH_SHORT).show();


                }
            });
            builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }


/*
public void siparisonay(View v) {
        musteriid = sha.getString("ID", "");
        // Cursor cr = db.yaz().rawQuery("select * from sepet where siparismusteriid='" + musteriid + "'", null);
        //while (cr.moveToNext()) {
        //siparisiverilecekurunler = cr.getString(3);
        //  Log.d("URUNLER....", siparisiverilecekurunler);
        //  siparisiverilecekler.add(siparisiverilecekurunler);


        if (!siparisid.equals("") && !(urunid.equals(""))) {

            String url = "http://jsonbulut.com/json/orderForm.php?ref=7b7392076900968d8e4ad78351ad55d3&customerId=" + musteriid + "&productId=" + urunid + "&html=" + Siparisdetay + "";
            new girisYap(url, this).execute();
            sha = getSharedPreferences("ETICARETKULLANICI", MODE_PRIVATE);
            db.sil("sepet", "siparisid", siparisid);
            finish();
            Intent in = new Intent(Sepetim.this, Sepetim.class);
            startActivity(in);
            Toast.makeText(Sepetim.this, "Sipariş Onaylandı", Toast.LENGTH_SHORT).show();
        }
 */


    class siparisTamamlama extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pro;
        String data = "";
        String url = "";

        public siparisTamamlama(String url, Activity ac) {
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
                Log.d("data", data);
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
                int sonuc = db.sil("sepet", "siparismusteriid", musteriid);
                if (sonuc > 0) {
                    Sepetim.this.finish();
                    Intent git = new Intent(Sepetim.this, Sepetim.class);
                    Sepetim.this.startActivity(git);
                }
            } catch (Exception ex) {
                Log.d("Silme Hatası:", ex.toString());
            }
        }

    }


    public void siparisgetir() {


        DB db = new DB(this);

        try {
            sha = getSharedPreferences("ETICARETKULLANICI", MODE_PRIVATE);
            musteriid = sha.getString("ID", "");
            Cursor cr = db.yaz().rawQuery("select * from sepet where siparismusteriid='" + musteriid + "'", null);
            double siparistoplam = 0;
            while (cr.moveToNext()) {
                // String siparis = cr.getString(1) + " " + cr.getString(2) + " " + cr.getString(3) + " " + cr.getString(4);
                String siparis = "Ürün: " + cr.getString(3) + "\n" + "Fiyat: " + cr.getString(4) + "  TL";
                Siparisdetay.add(siparis);
                //  Log.d("gegegeg", siparis);
                String urunid = cr.getString(2);
                urunidler.add(urunid);
                siparisidler.add(cr.getString(0));
                String urunadi = cr.getString(3);
                siparisurunadi.add(urunadi);
                // Log.d("URUN...", urunid);
                Silineceksiparisler.add(cr.getString(0));
                siparisfiyat = cr.getString(4);
                siparisfiyatlar.add(siparisfiyat);
                siparistoplam += Double.valueOf(cr.getString(4));
            }
            toplam.setText("Toplam Tutar : " + siparistoplam + " TL");
            if (siparistoplam == 0) {
                toplam.setText("Siparişiniz Bulunmamaktadır..");

            }
            ArrayAdapter<String> adp = new ArrayAdapter<>(Sepetim.this, R.layout.sepetimdekilerrow, R.id.sepetim, Siparisdetay);
            list.setAdapter(adp);
        } catch (Exception ex) {
            Log.d("getirme hatası : ", ex.toString());
        }

    }

}