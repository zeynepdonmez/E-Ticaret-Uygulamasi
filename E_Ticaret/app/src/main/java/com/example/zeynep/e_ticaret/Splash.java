package com.example.zeynep.e_ticaret;
//referans kodu:9541c4fbd9e82ede60537a33f372b34a
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class Splash extends AppCompatActivity {
    SharedPreferences sha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sha=getSharedPreferences("ETICARETKULLANICI",MODE_PRIVATE);

        Thread git=new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);

                    String IDkontol=sha.getString("ID","");
                    if (!IDkontol.equals("")){
                        Intent i=new Intent(Splash.this,AnaKategoriler.class);
                        startActivity(i);
                    }else if (IDkontol.equals("")){
                        Intent i=new Intent(Splash.this,GirisEkrani.class);
                        startActivity(i);

                    }

                }catch (Exception ex){
                    Log.d("Açılış Hatası : ",ex.toString());
                }finally {
                    finish();
                }
            }
        };
        git.start();




    }

}

