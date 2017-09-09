package com.tugasakhir.adi.dmobile;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class LaurensiusDbConFramework {

    private SQLiteDatabase db;

    public String buatDatabase(String nama_database){
        String nama_direktori = "/data/data/com.tugasakhir.adi.dmobile/";
        try{
            db = SQLiteDatabase.openDatabase(nama_direktori.concat(nama_database), null, SQLiteDatabase.CREATE_IF_NECESSARY);
            return LaurensiusSystemFramework.BUAT_DATABASE_SUCCESS;
        }catch (SQLiteException ex){
            return LaurensiusSystemFramework.BUAT_DATABASE_FAILED;
        }
    }

    public String cekTabelConfig(){
        String query = "Select name from sqlite_master where type ='table' and name='t_config_monitoring'";
        int numRow = db.rawQuery(query, null).getCount();
        if(numRow == 0){
            return LaurensiusSystemFramework.CEK_TABEL_UNAVAILABLE;
        }else{
            return LaurensiusSystemFramework.CEK_TABEL_AVAILABLE;
        }
    }

    public String cekIsiTabelConfig(){
        String sql = "select * from t_config_monitoring";
        int numRow = db.rawQuery(sql, null).getCount();
        if(numRow==0){
            return LaurensiusSystemFramework.CEK_ISI_TABEL_EMPTY;
        }else{
            return LaurensiusSystemFramework.CEK_ISI_TABEL_CONTAIN;
        }
    }

    public String buatTabelConfig(){
        String retVal;
        String sql = "create table t_config_monitoring" +
                "(server_url text," +
                "directory_url text);";
        try{
            db.execSQL(sql);
            retVal = LaurensiusSystemFramework.BUAT_TABEL_CONFIG_SUCCESS;
        }catch(SQLiteException e){
            retVal = LaurensiusSystemFramework.BUAT_TABEL_CONFIG_FAILED;
        }
        return retVal;
    }

    public String inputTabelConfig(String server_url,String directory_url){
        String retVal;
        String sql = "insert into t_config_monitoring " +
                "(server_url,directory_url) " +
                " values('"+ server_url +"','"+ directory_url +"');";
        try{
            db.execSQL(sql);
            retVal = LaurensiusSystemFramework.INSERT_DATA_CONFIG_SUCCESS;
        }catch(SQLiteException ex){
            retVal = LaurensiusSystemFramework.INSERT_DATA_CONFIG_FAILED;
        }
        return retVal;
    }

    public String updateTabelConfig(String server_url,String directory_url){
        String retVal;
        String sql = "update t_config_monitoring set " +
                "server_url = '"+server_url+"', " +
                "directory_url = '"+directory_url+"'";
        try{
            db.execSQL(sql);
            retVal = LaurensiusSystemFramework.UPDATE_DATA_CONFIG_SUCCESS;
        }catch(SQLiteException ex){
            retVal = LaurensiusSystemFramework.UPDATE_DATA_CONFIG_FAILED;
        }
        return retVal;
    }

    public String loadTabelConfig(){
        String sql = "select * from t_config_monitoring";
        Cursor c = db.rawQuery(sql, null);
        int jml_data = c.getCount();
        if(jml_data == 0){
            return null;
        }else{
            String data = null;
            try{
                int server_url = c.getColumnIndex("server_url");
                int directory_url  = c.getColumnIndex("directory_url");
                while(c.moveToNext()){
                    Log.d("Debug server", c.getString(server_url));
                    Log.d("Debug directory", c.getString(directory_url));
                    data = c.getString(server_url)
                            .concat(LaurensiusSystemFramework.SEPARATOR)
                            .concat(c.getString(directory_url));
                }
                return data;
            }catch(SQLiteException e){
                Log.e("Error","Errornya : " + e.getMessage());
                return data;
            }
        }
    }

    public void tutupKoneksi() {
        if (db.isOpen()) {
            db.close();
        }
    }
}
