package com.example.merve.havadurumu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText edtSehirAdi;
    Button btnHavaDurumu;
    TextView txtNem, txtBasinc, txtSicaklik, txtMaxSicaklik, txtMinSicaklik;
    ProgressDialog progress;
    ImageView imgIcon;
    String mURL = "http://api.openweathermap.org/data/2.5/weather?q=[CITY]&APPID=8264c8d803614690939223eb232d5c05&units=metric";
    public Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtSehirAdi = (EditText) findViewById(R.id.edtSehirAdi);
        btnHavaDurumu = (Button) findViewById(R.id.btnHavaDurumu);
        imgIcon = (ImageView) findViewById(R.id.imgIcon);
        mContext = this;
        btnHavaDurumu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtSehirAdi.getText().toString() == null || edtSehirAdi.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(mContext, "Lütfen Geçerli Bir Yer Giriniz.", Toast.LENGTH_LONG).show();
                } else {
                    String sehirAdi = edtSehirAdi.getText().toString();
                    String istekURL = mURL.replace("[CITY]", sehirAdi);
                    progress = ProgressDialog.show(MainActivity.this, "Lütfen Bekleyiniz", sehirAdi
                            +" şehri için hava durumu bilgisi getiriliyor.", true);
                    new MyAsyncTask().execute(istekURL);
                }
            }
        });
    }
    public static JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        urlConnection.setDoOutput(true);
        urlConnection.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        char[] buffer = new char[1024];
        String jsonString = new String();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();
        jsonString = sb.toString();
        return new JSONObject(jsonString);
    }
    private class MyAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                JSONObject havaDurumuJSON = getJSONObjectFromURL(params[0]);
                return havaDurumuJSON.toString();
            } catch (Exception ex) {
                System.out.print(ex.getMessage());
                return null;
            }
        }
        protected void onPostExecute(String result) {
            try {
                JSONObject havaDurumuJSON = new JSONObject(result);
                String sicaklik = havaDurumuJSON.optJSONObject("main").optString("temp");
                String nemOrani = havaDurumuJSON.optJSONObject("main").optString("humidity");
                String basinc = havaDurumuJSON.optJSONObject("main").optString("pressure");
                String sicaklikMax = havaDurumuJSON.optJSONObject("main").optString("temp_max");
                String sicaklikMin = havaDurumuJSON.optJSONObject("main").optString("temp_min");
                String iconCode = havaDurumuJSON.optJSONArray("weather").optJSONObject(0).optString("icon");
                txtNem = (TextView) findViewById(R.id.txtNem);
                txtNem.setText("%" + nemOrani);
                txtBasinc = (TextView) findViewById(R.id.txtBasinc);
                txtBasinc.setText(basinc + "bar");
                txtSicaklik = (TextView) findViewById(R.id.txtSicaklik);
                txtSicaklik.setText(sicaklik + "°C");
                txtMaxSicaklik = (TextView) findViewById(R.id.txtMaxSicaklik);
                txtMaxSicaklik.setText(sicaklikMax + "°C");
                txtMinSicaklik = (TextView) findViewById(R.id.txtMinSicaklik);
                txtMinSicaklik.setText(sicaklikMin + "°C");
                setIcon(iconCode);
                progress.dismiss();
                View view = MainActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            } catch (Exception ex) {
            }
        }
        protected void onProgressUpdate(Integer... progress) {
        }
    }
    public void setIcon(String iconCode) {
        try {
            if (iconCode.equalsIgnoreCase("01d")) {
                imgIcon.setBackgroundResource(R.mipmap.d32);
            } else if (iconCode.equalsIgnoreCase("02d")) {
                imgIcon.setBackgroundResource(R.mipmap.d28);
            } else if (iconCode.equalsIgnoreCase("03d")) {
                imgIcon.setBackgroundResource(R.mipmap.nd26);
            } else if (iconCode.equalsIgnoreCase("04d")) {
                imgIcon.setBackgroundResource(R.mipmap.nd26);
            } else if (iconCode.equalsIgnoreCase("09d")) {
                imgIcon.setBackgroundResource(R.mipmap.nd40);
            } else if (iconCode.equalsIgnoreCase("10d")) {
                imgIcon.setBackgroundResource(R.mipmap.d39);
            } else if (iconCode.equalsIgnoreCase("11d")) {
                imgIcon.setBackgroundResource(R.mipmap.d17);
            } else if (iconCode.equalsIgnoreCase("13d")) {
                imgIcon.setBackgroundResource(R.mipmap.d16);
            } else if (iconCode.equalsIgnoreCase("50d")) {
                imgIcon.setBackgroundResource(R.mipmap.nd19);
            } else if (iconCode.equalsIgnoreCase("01n")) {
                imgIcon.setBackgroundResource(R.mipmap.n31);
            } else if (iconCode.equalsIgnoreCase("02n")) {
                imgIcon.setBackgroundResource(R.mipmap.n27);
            } else if (iconCode.equalsIgnoreCase("03n")) {
                imgIcon.setBackgroundResource(R.mipmap.nd26);
            } else if (iconCode.equalsIgnoreCase("04n")) {
                imgIcon.setBackgroundResource(R.mipmap.nd26);
            } else if (iconCode.equalsIgnoreCase("09n")) {
                imgIcon.setBackgroundResource(R.mipmap.nd40);
            } else if (iconCode.equalsIgnoreCase("10n")) {
                imgIcon.setBackgroundResource(R.mipmap.n45);
            } else if (iconCode.equalsIgnoreCase("11n")) {
                imgIcon.setBackgroundResource(R.mipmap.n35);
            } else if (iconCode.equalsIgnoreCase("13n")) {
                imgIcon.setBackgroundResource(R.mipmap.n46);
            } else if (iconCode.equalsIgnoreCase("50n")) {
                imgIcon.setBackgroundResource(R.mipmap.nd19);
            }
        } catch (Exception ex) {
        }
    }
}


