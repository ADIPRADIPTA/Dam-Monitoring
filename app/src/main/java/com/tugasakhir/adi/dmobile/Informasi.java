package com.tugasakhir.adi.dmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;


public class Informasi extends AppCompatActivity {

    ImageButton btnprofile, btngmail;
    LinearLayout IProfile;
    boolean is_open = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi);

        btnprofile = (ImageButton) findViewById(R.id.btnprofile);
        btngmail = (ImageButton) findViewById(R.id.btngmail);
        IProfile = (LinearLayout) findViewById(R.id.IProfile);
        IProfile.setVisibility(View.GONE);

        btnprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(IProfile.getVisibility()==View.VISIBLE){
                    IProfile.setVisibility(View.GONE);
                }else
                if(IProfile.getVisibility()==View.GONE){
                    IProfile.setVisibility(View.VISIBLE);
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
    }
}