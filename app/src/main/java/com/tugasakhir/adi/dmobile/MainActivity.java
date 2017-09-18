package com.tugasakhir.adi.dmobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static String server = "http://192.168.43.3/automation_system/index.php/";
    public static String directory = "adi/api/dataset/";
    public static LaurensiusDbConFramework kelolaDatabase = new LaurensiusDbConFramework();
    ImageButton btninfo, btnExit, btnbantuan, btnMonitoring,btnReporting, btnSetting;
    LinearLayout llLogin;
    EditText etUsername, etPassword;
    boolean is_open = false;
    String m,u,nl;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btninfo = (ImageButton) findViewById(R.id.info);
        btnExit = (ImageButton) findViewById(R.id.keluar);
        btnbantuan = (ImageButton) findViewById(R.id.bantuan);
        btnMonitoring = (ImageButton) findViewById(R.id.monitoring);
        btnReporting = (ImageButton)findViewById(R.id.btnReporting);
        btnSetting = (ImageButton)findViewById(R.id.btnsetting);
        llLogin = (LinearLayout)findViewById(R.id.llLogin);
        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword= (EditText)findViewById(R.id.etPassword);

        Intent getParcel = getIntent();
        m = getParcel.getStringExtra("mode");
        u = getParcel.getStringExtra("username");
        nl = getParcel.getStringExtra("nama_lengkap");

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


        btnReporting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(m.equals("ADMINISTRATOR")){
                    Toast.makeText(getApplicationContext(),"Hello, " + nl + " silahkan pilih tanggal laporan.",Toast.LENGTH_LONG).show();
                    Intent l = new Intent(getApplicationContext(), Laporan.class);
                    startActivity(l);
                }else{
                    Toast.makeText(getApplicationContext(),"Anda tidak diperkenankan mengakses fitur reporting. Terima kasih",Toast.LENGTH_LONG).show();
                }
            }
        });
        btnMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent m = new Intent(getApplicationContext(), Monitoring.class);
                startActivity(m);
            }
        });

        btninfo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Informasi.class);
                startActivity(i);
//                finish();
            }
        });

        btnbantuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent b = new Intent(getApplicationContext(), bantuan.class);
                startActivity(b);
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(getApplicationContext(), Setting.class);
                startActivity(s);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(getApplicationContext(), splashscreen.class);
                startActivity(k);
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.close();
    }

    public void close() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakah Anda Benar-Benar ingin keluar?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }
};

