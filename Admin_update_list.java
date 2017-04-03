package com.example.grocery.grocery;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Admin_update_list extends MainActivity {
	EditText list_name;
	Button update;
	String slist_name;
    InputStream is=null;
    String result;
    String line=null;
    int code;
    public ProgressDialog pDialog;
    String solu;
    int value;

	
	protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.admin_update_list);
	      
	      list_name=(EditText)findViewById(R.id.editText1);
	      update=(Button)findViewById(R.id.button1);

	      update.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				slist_name=list_name.getText().toString();
				new insertTask().execute("");
				
				
			//	Intent i=new Intent(Admin_update_list.this,Admin_update_list.class);
				//startActivity(i);
				
				
			}
		});
}
	
	
	class insertTask extends AsyncTask<String,String,String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Admin_update_list.this);
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
            nameValuePairs.add(new BasicNameValuePair("list_name", slist_name));
            try{
            }catch(Exception e){
            }
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://192.168.43.51/grocery/add_list.php/");
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
            }
            runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					   if (code == 1) {
			          Toast.makeText(getApplicationContext(), "Product Added",Toast.LENGTH_LONG);
			        list_name.setText("");	
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