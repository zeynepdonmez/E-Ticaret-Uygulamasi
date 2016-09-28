package com.example.zeynep.e_ticaret;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class urunGoster extends AppCompatActivity {
    private LayoutInflater inf;
    SharedPreferences sha;
    SharedPreferences.Editor edit;
    ListView list;
    List<urunOzellik> ls = new ArrayList<>();
    ArrayList<String> urun = new ArrayList<>();
    ArrayList<String> urunBaslik = new ArrayList<String>();
    ArrayList<String> urunFiyat = new ArrayList<String>();
    ArrayList<String> urunID = new ArrayList<String>();
    ArrayList<String> urunResim = new ArrayList<String>();
    TextView text;
    DB db = new DB(this);
    static ArrayList<String> Siparis = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_goster);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list = (ListView) findViewById(R.id.urunlist);
        sha = getSharedPreferences("ETICARETKULLANICI", MODE_PRIVATE);
        edit = sha.edit();
        urunOzellik oz = UrunDetay.ozls.get(UrunDetay.secilen);
        text = (TextView) findViewById(R.id.textView);
        // Log.d("getProductName", oz.getProductName());
        // Log.d("getProductId", oz.getProductId());
        urunBaslik.add(oz.getDescription());
        urunID.add(oz.getProductId());
        urunFiyat.add(oz.getPrice());
        urunResim.add(oz.getResim());

        String detay = "Ürün                  :" + oz.getProductName() + "\n" + "Detay                :" + oz.getDescription() + "\n" + "Kısa Açıklama :" + oz.getBrief() + "\n" + "Fiyat                  :" + oz.getPrice() + "  TL";
        urunOzellik ur = new urunOzellik(UrunDetay.anafoto, detay);
        urun.add(detay);
        ls.add(ur);
        urungosteradapter adp = new urungosteradapter(this, ls);
        list.setAdapter(adp);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.adresler) {
            Intent in = new Intent(urunGoster.this, Adresler.class);
            startActivity(in);

        }
        if (id == R.id.ayarlar) {
            Intent in = new Intent(urunGoster.this, KullaniciAyarlar.class);
            startActivity(in);
            return true;
        }

        if (id == R.id.sepet) {
            Intent in = new Intent(urunGoster.this, Sepetim.class);
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
                   // Toast.makeText(urunGoster.this, "Çıkışa Tıklandı", Toast.LENGTH_SHORT).show();
                    edit.remove("ID");
                    edit.remove("userName");
                    edit.remove("userSurname");
                    edit.remove("userEmail");
                    edit.remove("userPhone");
                    edit.commit();
                    Toast.makeText(urunGoster.this, "Çıkış Başarılı", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(urunGoster.this, GirisEkrani.class);
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
                    onBackPressed();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void sepetim(View v) {
        Intent in = new Intent(urunGoster.this, Sepetim.class);
        startActivity(in);


    }

    public void sepeteEkle(View v) {
        sha = getSharedPreferences("ETICARETKULLANICI", MODE_PRIVATE);
        String musteriid = sha.getString("ID", "");
        String siparisurunid = getIntent().getExtras().getString("urunid");
        String siparisurunadi = getIntent().getExtras().getString("urunadi");
        String siparisurunfiyati = getIntent().getExtras().getString("urunfiyat");
        DB db = new DB(this);
        try {
            db.yaz().execSQL("insert into sepet values(null,'" + musteriid + "','" + siparisurunid + "','" + siparisurunadi + "','" + siparisurunfiyati + "')");
            Toast.makeText(urunGoster.this, "Sepete Ürün Eklendi...", Toast.LENGTH_SHORT).show();
            finish();

        } catch (Exception ex) {
            Log.d("Ekleme Hatası : ", ex.toString());
            Toast.makeText(urunGoster.this, "Ekleme Başarısız", Toast.LENGTH_SHORT).show();
        }

    }

}
