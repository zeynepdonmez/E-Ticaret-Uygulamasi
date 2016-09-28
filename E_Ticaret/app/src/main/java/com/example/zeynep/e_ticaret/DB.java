package com.example.zeynep.e_ticaret;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

    private static String dbName = "zeynep";
    private static int versiyon = 2;


    public SQLiteDatabase oku() {
        return this.getReadableDatabase();
    }

    public SQLiteDatabase yaz() {
        return this.getWritableDatabase();
    }


    public DB(Context context) {
        super(context, dbName + ".db", null, versiyon);
    }

    public DB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbName + ".db", null, versiyon);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE `sepet` (\n" +
                "\t`siparisid`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`siparismusteriid`\tTEXT,\n" +
                "\t`siparisurunid`\tTEXT,\n" +
                "\t`siparisurunadi`\tTEXT,\n" +
                "\t`siparisurunfiyati`\tTEXT\n" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists sepet");
        onCreate(db);
    }


    // data getirme methodu
    public Cursor dataGetir(String tableName, String query) {
        return oku().rawQuery("select *from " + tableName + " " + query, null);
    }


    // data silme methodu
    public int sil(String tableName, String columnName, String val) {
        return yaz().delete(tableName, columnName + "=?", new String[]{val});
    }

}

