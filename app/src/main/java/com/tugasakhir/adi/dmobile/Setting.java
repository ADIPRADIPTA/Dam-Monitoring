package com.tugasakhir.adi.dmobile;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Setting extends AppCompatActivity {

    Button btnSettingURL;
    EditText etServerURL,etDirectoryURL,etPreviewURL;
    ToggleButton btnSettingNotifikasi;
    TextView tvNotifikasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        etServerURL = (EditText)findViewById(R.id.etServerURL);
        etDirectoryURL = (EditText)findViewById(R.id.etDirectoryURL);
        etPreviewURL = (EditText)findViewById(R.id.etPreviewURL);
        tvNotifikasi = (TextView)findViewById(R.id.tvNotifikasi);
        btnSettingURL = (Button)findViewById(R.id.btnSettingURL);
        btnSettingNotifikasi = (ToggleButton) findViewById(R.id.btnSettingNotifikasi);

        if(MainActivity.server!=null && MainActivity.directory!=null){
            etServerURL.setText(MainActivity.server);
            etDirectoryURL.setText(MainActivity.directory);
            etPreviewURL.setText(etServerURL.getText().toString().concat(etDirectoryURL.getText().toString()));
        }
        etServerURL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etPreviewURL.setText("");
                etPreviewURL.setText(etServerURL.getText().toString().concat(etDirectoryURL.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        etDirectoryURL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etPreviewURL.setText("");
                etPreviewURL.setText(etServerURL.getText().toString().concat(etDirectoryURL.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        btnSettingURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String update_server = etServerURL.getText().toString();
                String update_directory = etDirectoryURL.getText().toString();
                MainActivity.kelolaDatabase.updateTabelConfig(update_server,update_directory);
                MainActivity.server = update_server;
                MainActivity.directory = update_directory;
                Toast.makeText(getApplicationContext(), "Alamat server sudah berhasil diperbaharui.",Toast.LENGTH_LONG).show();
            }
        });
        if(isMyServiceRunning(ServiceNotifikasi.class)){
            tvNotifikasi.setText("Service ON");
            btnSettingNotifikasi.setChecked(true);
        }else {
            tvNotifikasi.setText("Service OFF");
            btnSettingNotifikasi.setChecked(false);
        }

        btnSettingNotifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvNotifikasi.getText().equals("Service OFF")){
                    startService(new Intent(getBaseContext(), ServiceNotifikasi.class));
                }else{
                    stopService(new Intent(getBaseContext(), ServiceNotifikasi.class));
                }
                if(isMyServiceRunning(ServiceNotifikasi.class)){
                    tvNotifikasi.setText("Service ON");
                    btnSettingNotifikasi.setChecked(true);
                }else {
                    tvNotifikasi.setText("Service OFF");
                    btnSettingNotifikasi.setChecked(false);
                }
            }
        });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
