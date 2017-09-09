package com.tugasakhir.adi.dmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;


public class Informasi extends AppCompatActivity {

    ImageButton btnprofile, btnandroid, btngmail, btnbantuan;
    LinearLayout IInfo, IProfile, IBantuan;
    boolean is_open = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi);

        btnprofile = (ImageButton) findViewById(R.id.btnprofile);
        btnandroid = (ImageButton) findViewById(R.id.btnandroid);
        btngmail = (ImageButton) findViewById(R.id.btngmail);
        btnbantuan = (ImageButton) findViewById(R.id.btnbantuan);
        IProfile = (LinearLayout) findViewById(R.id.IProfile);
        IInfo= (LinearLayout) findViewById(R.id.IInfo);
        IBantuan= (LinearLayout) findViewById(R.id.IBantuan);
        IProfile.setVisibility(View.GONE);
        IInfo.setVisibility(View.GONE);
        IBantuan.setVisibility(View.GONE);


        btnandroid.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(IInfo.getVisibility()==View.VISIBLE){
                    IInfo.setVisibility(View.GONE);
                }else
                if(IInfo.getVisibility()==View.GONE){
                    IInfo.setVisibility(View.VISIBLE);
                    IProfile.setVisibility(View.GONE);
                    IBantuan.setVisibility(View.GONE);
                }
            }
        });

        btnprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(IProfile.getVisibility()==View.VISIBLE){
                    IProfile.setVisibility(View.GONE);
                }else
                if(IProfile.getVisibility()==View.GONE){
                    IProfile.setVisibility(View.VISIBLE);
                    IInfo.setVisibility(View.GONE);
                    IBantuan.setVisibility(View.GONE);
                }
            }
        });
        btngmail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] gmail = new String[]{getString(R.string.gmail)};
                Intent iSend = new Intent(Intent.ACTION_SEND);
                iSend.putExtra(Intent.EXTRA_EMAIL, gmail);
                iSend.putExtra(Intent.EXTRA_SUBJECT, "");
                iSend.setType("plain/text");
                iSend.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(iSend);
            }
        });
        btnbantuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(IBantuan.getVisibility()==View.VISIBLE){
                    IBantuan.setVisibility(View.GONE);
                }else
                if(IBantuan.getVisibility()==View.GONE){
                    IBantuan.setVisibility(View.VISIBLE);
                    IInfo.setVisibility(View.GONE);
                    IProfile.setVisibility(View.GONE);
                }
            }
        });
    }
}