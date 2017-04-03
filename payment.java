package com.example.grocery.grocery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 3/20/2017.
 */
public class payment extends Activity {
    String price;
int bank;
    HttpPost httppost;
    Button addproduct;

    //TextView vehtype,owner,vehno,vehcc,address,city,state,pin,salary,email,mobile;
    //String name1,description1,price1,category1,image1;
    Bitmap bi=null;
    byte[] decodedString=null;
    String name2,quantity1;
    public String jsonResult;
    InputStream is = null;
    InputStream is1 = null;
    StringBuffer buffer;
    String result="";
    JSONArray jsonMainNode;
    TextView jobs;
    String result1;
    JSONObject jsonResponse;
    HttpClient httpclient;
    Button book;
    int code=0;
    List<NameValuePair> nameValuePairs;
    public ProgressDialog pDialog;
    ImageView img;
    String images,type,number;
    SQLiteDatabase db;
    DBHelper ob1, ob;
    String s1;
    Cursor rs;
    int amount;
    private Bitmap bitmap;
    String line=null;
    EditText quantity;
    int x,y;
    int price2;
    String sent;
    //TextView name,description,price,category;
    String product;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        Button pay;
        Intent in=getIntent();
        price=in.getStringExtra("price");
        price2=Integer.parseInt(price);
        Toast.makeText(getApplicationContext(),price, Toast.LENGTH_LONG).show();
        pay=(Button)findViewById(R.id.button1);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JsonReadTask().execute("");


            }
        });



    }
    private class JsonReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("name",product));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://192.168.43.51/grocery/bank.php/");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
                Log.e("Result", jsonResult);
            }
            catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            }
            catch (IOException e) {
                Toast.makeText(getApplicationContext(),
                        "Error..." + e.toString(), Toast.LENGTH_LONG).show();
            }
            return answer;
        }
        @Override
        protected void onPostExecute(String result) {
            ListDrwaer();
        }
    }
    public void ListDrwaer() {
        try {
            JSONArray jArray = new JSONArray(jsonResult);
            for (int i = 0; i < jArray.length() - 1; i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                bank=Integer.valueOf(json_data.getInt("amount"));
            }

           // Toast.makeText(getApplicationContext(),bank, Toast.LENGTH_SHORT).show();

            amount=bank-price2;
                new Thread(new Runnable() {
                    public void run() {
                        update();
                    }
                }).start();


        }catch (JSONException e) {
           code=0;
            Log.e("Failed",e.toString());
            Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_SHORT).show();
        }
    }


    public String  update(){
        try{
            HttpClient httpclient1=new DefaultHttpClient();
            HttpPost httppost1= new HttpPost("http://192.168.43.51/grocery/updateprofile.php/");
            List<NameValuePair>  nameValuePairs1 = new ArrayList<NameValuePair>(5);
            nameValuePairs1.add(new BasicNameValuePair("amount",Integer.toString(amount)));
            httppost1.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
            HttpResponse response = httpclient1.execute(httppost1);
            HttpEntity entity = response.getEntity();
            is1 = entity.getContent();

        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection"+ e.toString());
            Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is1, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is1.close();
            result1 = sb.toString();
            Log.e("log_tag", result1);
        } catch (Exception e) {
            Log.e("fail2", result1);
            Log.e("log_tag", "Error converting result" + e.toString());
            //  Toast.makeText(getApplicationContext(), "Input reading fail", Toast.LENGTH_SHORT).show();
        }
        try {
            JSONObject json_data = new JSONObject(result1);
            code = (json_data.getInt("code"));
        } catch (Exception e) {
            Log.e("Fail 3", e.toString());
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (code == 1) {
                    Toast.makeText(getApplicationContext(), "update success", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "update failed", Toast.LENGTH_SHORT).show();

                }
            }
        });

        return null;
    }

}
