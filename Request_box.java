package com.example.grocery.grocery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Request_box extends Activity {
    String mail,s1;
    Cursor rs;
    SQLiteDatabase db;
    EditText name,discription,required;
    Button request;
    String name1,discription1,required1;
    InputStream is=null;
    String result;
    String line=null;
    int code;
    public ProgressDialog pDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_box);
        name=(EditText)findViewById(R.id.editText1);
        discription=(EditText)findViewById(R.id.editText2);
        required=(EditText)findViewById(R.id.editText3);

        request=(Button)findViewById(R.id.button1);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name1=name.getText().toString();
                discription1=discription.getText().toString();
                required1=required.getText().toString();
                //Toast.makeText(getApplicationContext(), location, Toast.LENGTH_LONG).show();
                new insertTask().execute("");

            }
        });

        db=openOrCreateDatabase("Grocery", MODE_PRIVATE, null);
        rs=db.rawQuery("select email from lists", null);
        while(rs.moveToNext()) {
            s1 = rs.getString(rs.getColumnIndex(DBHelper.EMAIL));
        }
        Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();

    }
    class insertTask extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Request_box.this);
            pDialog.setMessage("Creating User..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected String doInBackground(String... params) {
            insert();
            return null;
        }
        public void insert() {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("name", name1));
            nameValuePairs.add(new BasicNameValuePair("discription", discription1));
            nameValuePairs.add(new BasicNameValuePair("required", required1));
            try{
            }catch(Exception e){
            }
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://192.168.43.51/grocery/request_box.php/");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "connection success ");
            } catch (Exception e) {
                Log.e("Fail 1", e.toString());
                Toast.makeText(getApplicationContext(), "Invalid IP Address", Toast.LENGTH_LONG).show();
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
                Log.e("pass 2", "connection success ");
            } catch (Exception e) {
                Log.e("Fail 2", e.toString());
            }
            try {
                JSONObject json_data = new JSONObject(result);
                code = (json_data.getInt("code"));
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    if (code == 1) {
                        name.setText("");
                        discription.setText("");
                        required.setText("");
                        Toast.makeText(getApplicationContext(), "Product Requested Success", Toast.LENGTH_LONG);
                    } else {
                        Toast.makeText(getApplicationContext(), "User creation Failed",Toast.LENGTH_LONG);
                    }

                }
            });


        }
        public void onPostExecute(String result) {
            pDialog.dismiss();


        }}
}
