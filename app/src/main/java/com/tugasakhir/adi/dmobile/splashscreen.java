package com.tugasakhir.adi.dmobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class splashscreen extends Activity {

    public static String server = "";
    public static String directory = "";
    public static LaurensiusDbConFramework kelolaDatabase = new LaurensiusDbConFramework();

    LinearLayout llLogin,llButtonSelector;
    Button btnUser,btnAdmin,btnBatal,btnLogin;
    private ProgressDialog pDialog;
    EditText etUsername,etPassword;
    List<NameValuePair> data_login = new ArrayList<NameValuePair>(7);
    Boolean loaddata;
    String url;
    String JO;
    String status_cek;
    String message;
    String username;
    String nama_lengkap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);
        llLogin = (LinearLayout)findViewById(R.id.llLogin);
        llButtonSelector= (LinearLayout)findViewById(R.id.llButtonSelector);
        llButtonSelector.setVisibility(View.VISIBLE);
        llLogin.setVisibility(View.GONE);
        btnUser = (Button)findViewById(R.id.btnUser);
        btnAdmin= (Button)findViewById(R.id.btnAdmin);
        btnBatal= (Button)findViewById(R.id.btnBatal);
        btnLogin= (Button)findViewById(R.id.btnLogin);
        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);


        //        DATABaSE
        Log.d(LaurensiusSystemFramework.TAG,LaurensiusSystemFramework.BUAT_DATABASE);
        if(kelolaDatabase.buatDatabase(getResources().getString(R.string.nama_database))== LaurensiusSystemFramework.BUAT_DATABASE_SUCCESS) { //Buat database success
            Log.d(LaurensiusSystemFramework.TAG,LaurensiusSystemFramework.BUAT_DATABASE_SUCCESS);
            if (kelolaDatabase.cekTabelConfig() == LaurensiusSystemFramework.CEK_TABEL_AVAILABLE) { // Tabel Config Tersedia
                Log.d(LaurensiusSystemFramework.TAG,LaurensiusSystemFramework.CEK_TABEL_AVAILABLE);
                if (kelolaDatabase.cekIsiTabelConfig() == LaurensiusSystemFramework.CEK_ISI_TABEL_CONTAIN) { // Tabel Config Terisi
                    Log.d(LaurensiusSystemFramework.TAG,LaurensiusSystemFramework.CEK_ISI_TABEL_CONTAIN);
                    String data_config = kelolaDatabase.loadTabelConfig();
                    String[] splited = new String[data_config.split(LaurensiusSystemFramework.SEPARATOR.toString()).length];
                    splited = data_config.split(LaurensiusSystemFramework.SEPARATOR.toString());
                    server = splited[0];
                    directory = splited[1];
                    Toast.makeText(getApplicationContext(),"Server : " + server + " directory : " + directory ,Toast.LENGTH_LONG).show();
                } else if (kelolaDatabase.cekIsiTabelConfig() == LaurensiusSystemFramework.CEK_ISI_TABEL_EMPTY) { // Tabel Config Kosong
                    Log.d(LaurensiusSystemFramework.TAG,LaurensiusSystemFramework.CEK_ISI_TABEL_EMPTY);
                    server = getResources().getString(R.string.default_server).toString();
                    directory = getResources().getString(R.string.default_directory).toString();
                }
            } else
            if (kelolaDatabase.cekTabelConfig() == LaurensiusSystemFramework.CEK_TABEL_UNAVAILABLE) { // Tabel Config Tidak Tersedia
                Log.d(LaurensiusSystemFramework.TAG,LaurensiusSystemFramework.CEK_TABEL_UNAVAILABLE);
                if (kelolaDatabase.buatTabelConfig() == LaurensiusSystemFramework.BUAT_TABEL_CONFIG_SUCCESS) { //Buat Tabel Config Success
                    Log.d(LaurensiusSystemFramework.TAG,LaurensiusSystemFramework.BUAT_TABEL_CONFIG_SUCCESS);
                    kelolaDatabase.inputTabelConfig(getResources().getString(R.string.default_server).toString(),getResources().getString(R.string.default_directory).toString());
                } else if (kelolaDatabase.buatTabelConfig() == LaurensiusSystemFramework.BUAT_TABEL_CONFIG_FAILED) { //Buat Tavel Config Gagal
                    Log.d(LaurensiusSystemFramework.TAG,LaurensiusSystemFramework.BUAT_TABEL_CONFIG_FAILED);
                    finish();
                }
            }
        }else
        if(kelolaDatabase.buatDatabase(getResources().getString(R.string.nama_database))== LaurensiusSystemFramework.BUAT_DATABASE_FAILED){ //Buat Database failed
            Log.d(LaurensiusSystemFramework.TAG,LaurensiusSystemFramework.BUAT_DATABASE_FAILED);
            finish();
        }
//        END OF DATABaSE

        directory = getResources().getString(R.string.default_login);
        url = server.concat(directory);

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentOk = new Intent(splashscreen.this,MainActivity.class);
                intentOk.putExtra("mode","PUBLIC");
                intentOk.putExtra("username","");
                intentOk.putExtra("nama_lengkap","");
                startActivity(intentOk);
            }
        });

        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llButtonSelector.setVisibility(View.GONE);
                llLogin.setVisibility(View.VISIBLE);
            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llButtonSelector.setVisibility(View.VISIBLE);
                llLogin.setVisibility(View.GONE);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //arahkan ke async untuk post username dan password
                // void login(String username, String password);
                if(!etUsername.getText().toString().equals("") || !etPassword.getText().toString().equals("")){
                    data_login.add(new BasicNameValuePair("username", etUsername.getText().toString()));
                    data_login.add(new BasicNameValuePair("password", etPassword.getText().toString()));
                    new AsyncLogin().execute();
                }else{
                    Toast.makeText(getApplicationContext(),"Pastikan bahwa Anda sudah mengisi Username dan Password.",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private class AsyncLogin extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(splashscreen.this);
            pDialog.setMessage("Mohon menunggu...");
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d(LaurensiusSystemFramework.TAG, "Do in background");
            ServiceHandler sh = new ServiceHandler();
            String JSON_data = sh.makeServiceCall(url, ServiceHandler.POST, data_login);
            if(JSON_data!=null){
                try {
                    JSONObject jsonObj = new JSONObject(JSON_data);
                    JSONObject response = jsonObj.getJSONObject("response");
                    status_cek = response.getString("status_cek");
                    message = response.getString("message");
                    if(status_cek.equals("MATCH")){
                        JSONArray data_user = response.getJSONArray("data_user");
                        JSONObject obj_user = data_user.getJSONObject(0);
                        username = obj_user.getString("username");
                        nama_lengkap = obj_user.getString("nama_lengkap");
                        JO = JSON_data;
                    }
                } catch (final JSONException e) {
                    Log.e(LaurensiusSystemFramework.TAG, e.getMessage());
                }
                loaddata=true;
            }
            else{
                loaddata=false;
            }
            Log.d(LaurensiusSystemFramework.TAG, "JSON data : " + JSON_data);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(pDialog.isShowing()){
                pDialog.dismiss();
            }
            if(loaddata){
                etUsername.setText("");
                etPassword.setText("");
                if(status_cek.equals("MATCH")){
                    Intent intentOk = new Intent(splashscreen.this,MainActivity.class);
                    intentOk.putExtra("mode","ADMINISTRATOR");
                    intentOk.putExtra("username",username);
                    intentOk.putExtra("nama_lengkap",nama_lengkap);
                    startActivity(intentOk);
                }else{
                    Toast.makeText(getApplicationContext(),"Gagal login : " + message,Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(getApplicationContext(),"Sukses load JSON : " + JO,Toast.LENGTH_SHORT).show();
            }else{

                Toast.makeText(getApplicationContext(),"Gagal load JSON",Toast.LENGTH_SHORT).show();
            }
        }
    }
}