package com.tugasakhir.adi.dmobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageButton btninfo, btnExit, btnbantuan, btnMonitoring,btnReporting, btnSetting;
    LinearLayout llLogin;
    EditText etUsername, etPassword;
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

