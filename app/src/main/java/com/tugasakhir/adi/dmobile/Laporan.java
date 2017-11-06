package com.tugasakhir.adi.dmobile;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Laporan extends AppCompatActivity {
    String[] Ketinggian;
    String[] Status;
    String[] Volume;
    String[] Tanggal;
    boolean loaddata=false;
    Spinner spBulanAwal,spBulanAkhir,spTahunAwal,spTahunAkhir;
    Button btnCek;
    String urlLaporan;
    String url;
    LinearLayout llLayoutBox;
    int report_length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);
        spBulanAwal = (Spinner) findViewById(R.id.spBulanAwal);
        spBulanAkhir = (Spinner) findViewById(R.id.spBulanAkhir);
        spTahunAwal = (Spinner) findViewById(R.id.spTahunAwal);
        spTahunAkhir = (Spinner) findViewById(R.id.spTahunAkhir);
        btnCek = (Button) findViewById(R.id.btnCek);
        ArrayAdapter<CharSequence> bulanAdapter = ArrayAdapter.createFromResource(this,R.array.bulan, android.R.layout.simple_spinner_item);
        bulanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBulanAwal.setAdapter(bulanAdapter);
        spBulanAkhir.setAdapter(bulanAdapter);
        ArrayAdapter<CharSequence> tahunAdapter = ArrayAdapter.createFromResource(this,R.array.tahun,android.R.layout.simple_spinner_item);
        tahunAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTahunAwal.setAdapter(tahunAdapter);
        spTahunAkhir.setAdapter(tahunAdapter);
        btnCek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String indexBulanAwal = spBulanAwal.getSelectedItem().toString();
                String indexBulanAkhir = spBulanAkhir.getSelectedItem().toString();
                String tahunAwal = spTahunAwal.getSelectedItem().toString();
                String tahunAkhir = spTahunAkhir.getSelectedItem().toString();
                cekFormatLaporan(indexBulanAwal,tahunAwal,indexBulanAkhir,tahunAkhir);
            }
        });
    }

    void cekFormatLaporan(String indexBulanAwal,String tahunAwal,String indexBulanAkhir,String tahunAkhir){
        urlLaporan = tahunAwal.concat("-").concat(indexBulanAwal).concat("/").concat(tahunAkhir).concat("-").concat(indexBulanAkhir);
        if(Integer.parseInt(tahunAwal) > Integer.parseInt(tahunAkhir)){
            Toast.makeText(getApplicationContext(),"Pastikan bulan dan tahun awal lebih kecil/sama dengan bulan tahun akhir. Terima kasih",Toast.LENGTH_LONG).show();
        }else
        if(Integer.parseInt(tahunAwal) == Integer.parseInt(tahunAkhir)){
            if(Integer.parseInt(indexBulanAwal) > Integer.parseInt(indexBulanAkhir)){
                Toast.makeText(getApplicationContext(),"Pastikan bulan dan tahun awal lebih kecil/sama dengan bulan tahun akhir. Terima kasih",Toast.LENGTH_LONG).show();
            }else{
                new GetJSONReport().execute();
            }
        }else{
            new GetJSONReport().execute();
        }
    }

    private class GetJSONReport extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d(LaurensiusSystemFramework.TAG, "Do in background");
            ServiceHandler sh = new ServiceHandler();
            url = getResources().getString(R.string.default_server).concat(getResources().getString(R.string.default_custom_report)).concat(urlLaporan);
            String JSON_data = sh.makeServiceCall(url, ServiceHandler.GET);
            if(JSON_data!=null){
                try {
                    JSONObject jsonObj = new JSONObject(JSON_data);
                    JSONObject report = jsonObj.getJSONObject("report");
                    JSONArray custom_morning_report = report.getJSONArray("custom_morning_report");
                    report_length = custom_morning_report.length();
                    if(report_length > 0){
                        Tanggal = new String[report_length];
                        Ketinggian = new String[report_length];
                        Status = new String[report_length];
                        Volume = new String[report_length];
                        try{
                            for(int x=0;x<report_length;x++){
                                JSONObject obj_custom_morning_report = custom_morning_report.getJSONObject(x);
                                Tanggal[x] = obj_custom_morning_report.getString("date");
                                Ketinggian[x] = obj_custom_morning_report.getString("ketinggian").substring(0,5);
                                Status[x] = obj_custom_morning_report.getString("status");
                                Volume[x] = obj_custom_morning_report.getString("volume").substring(0,7);
                            }
                            loaddata=true;
                        }catch (JSONException e){
                            loaddata=false;
                        }
                    }
                } catch (final JSONException e) {
                    Log.e(LaurensiusSystemFramework.TAG, e.getMessage());
                }
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
            if (loaddata) {
                llLayoutBox = (LinearLayout)findViewById(R.id.llLayoutBox);
                if(llLayoutBox.getChildCount() > 0){
                    llLayoutBox.removeAllViews();
                }
                for(int x=0;x<report_length;x++){
                    LinearLayout.LayoutParams laparItem = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    LinearLayout llItem = new LinearLayout(Laporan.this);
                    llItem.setBackgroundColor(Color.parseColor("#FAFAFA"));
                    laparItem.setMargins(0,0,0,10);
                    llItem.setOrientation(LinearLayout.HORIZONTAL);

                    //--------------kiri
                    LinearLayout.LayoutParams laparItemKiri = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    LinearLayout llItemKiri = new LinearLayout(Laporan.this);
                    llItemKiri.setOrientation(LinearLayout.VERTICAL);

                    LinearLayout.LayoutParams laparItemIndikator = new LinearLayout.LayoutParams(150,150);
                    LinearLayout llItemIndikator = new LinearLayout(Laporan.this);
                    String bg = "#000000";
                    if(Status[x].toLowerCase().equals("normal")){
                        bg = "#26C281";
                    }else
                    if(Status[x].toLowerCase().equals("tinggi")){
                        bg = "#D91E18";
                    }else
                    if(Status[x].toLowerCase().equals("rendah")){
                        bg = "#F3C200";
                    }
                    llItemIndikator.setBackgroundColor(Color.parseColor(bg));
                    llItemIndikator.setOrientation(LinearLayout.VERTICAL);
                    //------------end of kiri

                    //-----------kanan

                    LinearLayout.LayoutParams laparItemKanan = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    laparItemKanan.setMargins(10,0,0,0);
                    LinearLayout llItemKanan = new LinearLayout(Laporan.this);
                    llItemKanan.setOrientation(LinearLayout.VERTICAL);

                    LinearLayout.LayoutParams laparItemTanggal = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    TextView tvItemTanggal = new TextView(Laporan.this);
                    tvItemTanggal.setText("Tanggal Pencatatan : " + Tanggal[x]);

                    LinearLayout.LayoutParams laparItemKetinggian = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    TextView tvItemKetinggian = new TextView(Laporan.this);
                    tvItemKetinggian.setText("Ketinggian : " + Ketinggian[x]);

                    LinearLayout.LayoutParams laparItemVolume = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    TextView tvItemVolume = new TextView(Laporan.this);
                    tvItemVolume.setText("Volume : " + Volume[x] + " cm³");

                    LinearLayout.LayoutParams laparItemStatus = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    TextView tvItemStatus = new TextView(Laporan.this);
                    tvItemStatus.setText("Status : " + Status[x]);

                    llLayoutBox.addView(llItem,laparItem);

                    llItem.addView(llItemKiri,laparItemKiri);
                    llItemKiri.addView(llItemIndikator,laparItemIndikator);

                    llItem.addView(llItemKanan,laparItemKanan);
                    llItemKanan.addView(tvItemTanggal,laparItemTanggal);
                    llItemKanan.addView(tvItemKetinggian,laparItemKetinggian);
                    llItemKanan.addView(tvItemVolume,laparItemVolume);
                    llItemKanan.addView(tvItemStatus,laparItemStatus);
                }

            }else{
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }
        }
    }
}
