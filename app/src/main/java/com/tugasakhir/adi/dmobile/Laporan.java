package com.tugasakhir.adi.dmobile;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Laporan extends AppCompatActivity {
    String[] Ketinggian;
    String[] Status;
    String[] Volume;
    String[] Tanggal;
    boolean loaddata=false;
    Spinner spTanggalAwal,spTanggalAkhir,spBulanAwal,spBulanAkhir,spTahunAwal,spTahunAkhir;
    Button btnCek,btnDownload;
    public static String urlLaporan;
    public String nama_laporan;
    String url;
    LinearLayout llLayoutBox;
    int report_length;
    WebView wvDownload;
    TextView tvLaporan;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);
        tvLaporan = (TextView) findViewById(R.id.tvLaporan);
        wvDownload = (WebView) findViewById(R.id.wvdownload);
        spTanggalAwal = (Spinner) findViewById(R.id.spTanggalAwal);
        spTanggalAkhir = (Spinner) findViewById(R.id.spTanggalAkhir);
        spBulanAwal = (Spinner) findViewById(R.id.spBulanAwal);
        spBulanAkhir = (Spinner) findViewById(R.id.spBulanAkhir);
        spTahunAwal = (Spinner) findViewById(R.id.spTahunAwal);
        spTahunAkhir = (Spinner) findViewById(R.id.spTahunAkhir);
        btnCek = (Button) findViewById(R.id.btnCek);
        btnDownload = (Button) findViewById(R.id.btnDownload);
        ArrayAdapter<CharSequence> tanggalAdapter = ArrayAdapter.createFromResource(this,R.array.tanggal, android.R.layout.simple_spinner_item);
        tanggalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTanggalAwal.setAdapter(tanggalAdapter);
        spTanggalAkhir.setAdapter(tanggalAdapter);
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
                String tglAwal = spTanggalAwal.getSelectedItem().toString();
                String tglAkhir = spTanggalAkhir.getSelectedItem().toString();
                String indexBulanAwal = spBulanAwal.getSelectedItem().toString();
                String indexBulanAkhir = spBulanAkhir.getSelectedItem().toString();
                String tahunAwal = spTahunAwal.getSelectedItem().toString();
                String tahunAkhir = spTahunAkhir.getSelectedItem().toString();
                cekFormatLaporan(tglAwal,indexBulanAwal,tahunAwal,tglAkhir,indexBulanAkhir,tahunAkhir);
            }
        });
    }

    void cekFormatLaporan(String tglAwal,String indexBulanAwal,String tahunAwal,String tglAkhir,String indexBulanAkhir,String tahunAkhir){
        urlLaporan = tahunAwal.concat("-").concat(indexBulanAwal).concat("-").concat(tglAwal).concat("/").concat(tahunAkhir).concat("-").concat(indexBulanAkhir).concat("-").concat(tglAkhir);
        nama_laporan = tahunAwal.concat("-").concat(indexBulanAwal).concat("-").concat(tglAwal).concat("_").concat(tahunAkhir).concat("-").concat(indexBulanAkhir).concat("-").concat(tglAkhir);
        int awal = Integer.parseInt(tahunAwal.concat(indexBulanAwal).concat(tglAwal));
        int akhir = Integer.parseInt(tahunAkhir.concat(indexBulanAkhir).concat(tglAkhir));
        if(akhir < awal){
            Toast.makeText(getApplicationContext(),"Pastikan tanggal awal lebih kecil/sama dengan tanggal akhir. Terima kasih",Toast.LENGTH_LONG).show();
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
                                Tanggal[x] = obj_custom_morning_report.getString("datetime");
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
                    loaddata=false;
                    Log.e(LaurensiusSystemFramework.TAG, e.getMessage());
                }
            }else{
                loaddata=false;
            }
            Log.d(LaurensiusSystemFramework.TAG, "JSON data : " + JSON_data);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (loaddata) {
                tvLaporan.setText("Laporan tersedia dan siap di download, klik tombol di bawah untuk melakukan download");
                btnDownload.setVisibility(View.VISIBLE);
                btnDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wvDownload.getSettings().setJavaScriptEnabled(true);
                        wvDownload.getSettings().setDomStorageEnabled(true);
                        wvDownload.loadUrl(getResources().getString(R.string.default_server).concat(getResources().getString(R.string.download_custom_report)).concat(urlLaporan));
                        wvDownload.setDownloadListener(new DownloadListener() {
                            @Override
                            public void onDownloadStart(String url, String userAgent,
                                                        String contentDisposition, String mimetype,
                                                        long contentLength) {
                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                                request.allowScanningByMediaScanner();
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nama_laporan+".pdf");
                                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                dm.enqueue(request);
                                Toast.makeText(getApplicationContext(), "Download laporan :" + nama_laporan + ".pdf" ,Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

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
                btnDownload.setVisibility(View.GONE);
                tvLaporan.setText("Laporan pada periode tersebut tidak tersedia!");
//                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }
        }
    }

}
