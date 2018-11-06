package com.example.chiraggarg.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class WeatherForecast extends Activity {

    protected static final String ACTIVITY_NAME = "WeatherForecast";
    private ProgressBar progressBar;
    private TextView currentTemp;
    private TextView minTemp;
    private TextView maxTemp;
    private TextView windSpeedSpeed;
    private ImageView Image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        Image = (ImageView) findViewById(R.id.ImageView);
        progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
        setProgressBarVisibility(true);

        currentTemp = (TextView) findViewById(R.id.TextView1);
        minTemp = (TextView) findViewById(R.id.TextView2);
        maxTemp = (TextView) findViewById(R.id.TextView3);
        Image = (ImageView) findViewById(R.id.ImageView);
        windSpeedSpeed =(TextView)findViewById(R.id.TextView4);

        ForecastQuery forecast = new ForecastQuery();
        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";
        forecast.execute(urlString);

        Log.i(ACTIVITY_NAME,"In onCreate()");
    }

    protected static Bitmap getImage(URL url) {
        Log.i(ACTIVITY_NAME, "In getImage");
        HttpURLConnection iconConn = null;
        try {
            iconConn = (HttpURLConnection) url.openConnection();
            iconConn.connect();
            int response = iconConn.getResponseCode();
            if (response == 200) {
                return BitmapFactory.decodeStream(iconConn.getInputStream());
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (iconConn != null) {
                iconConn.disconnect();
            }
        }
    }

    public boolean fileExistence(String fileName) {
        Log.i(ACTIVITY_NAME, "In fileExistence");
        Log.i(ACTIVITY_NAME, getBaseContext().getFileStreamPath(fileName).toString());
        File file = getBaseContext().getFileStreamPath(fileName);
        return file.exists();
    }


    private class ForecastQuery extends AsyncTask <String, Integer, String> {
        private String current;
        private String min;
        private String max;
        private String windSpeed;
        private String iconname;
        private Bitmap icon;
        @Override
        protected String doInBackground(String ... args) {
            try {
                URL url = new URL(args[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                InputStream stream = conn.getInputStream();
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(stream, null);

                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    if (parser.getName().equals("temperature")) {
                        current = parser.getAttributeValue(null, "value");
                        publishProgress(25);
                        min = parser.getAttributeValue(null, "min");
                        publishProgress(50);
                        max = parser.getAttributeValue(null, "max");
                        publishProgress(75);

                    }
                    if(parser.getName().equals("speed")){
                        windSpeed = parser.getAttributeValue(null,"value");
                    }
                    if (parser.getName().equals("weather")) {
                        iconname = parser.getAttributeValue(null, "icon");
                        String iconFile = iconname+".png";
                        if (fileExistence(iconFile)) {
                            FileInputStream inputStream = null;
                            try {
                                inputStream = new FileInputStream(getBaseContext().getFileStreamPath(iconFile));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            icon = BitmapFactory.decodeStream(inputStream);
                            Log.i(ACTIVITY_NAME, "Image already exists");
                        } else {
                            URL iconUrl = new URL("http://openweathermap.org/img/w/" + iconname + ".png");
                            icon = getImage(iconUrl);
                            FileOutputStream outputStream = openFileOutput(iconname + ".png", Context.MODE_PRIVATE);
                            icon.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                            outputStream.flush();
                            outputStream.close();
                            Log.i(ACTIVITY_NAME, "Adding new image");
                        }
                        Log.i(ACTIVITY_NAME, "file name="+iconFile);
                        publishProgress(100);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            return null;

        }
        @Override
        protected void onProgressUpdate(Integer... value) {
            Log.i(ACTIVITY_NAME, "In onProgressUpdate");
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            String degree = Character.toString((char) 0x00B0);
            windSpeedSpeed.setText(windSpeedSpeed.getText()+" "+windSpeed+"m/s");
            currentTemp.setText(currentTemp.getText()+" "+current+degree+"C");
            minTemp.setText(minTemp.getText()+" "+min+degree+"C");
            maxTemp.setText(maxTemp.getText()+" "+max+degree+"C");
            Image.setImageBitmap(icon);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}