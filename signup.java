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
import android.widget.TextView;
import android.widget.Toast;

public class signup extends Activity {
	private static final String TAG = "Signup";
	EditText name;
	EditText email;
	EditText password,address,mobile;
	Button signup,admin;
	TextView loginlink; 
	SQLiteDatabase db;
    DBHelper ob1,ob;
    Cursor rs;
    InputStream is=null;
    String result;
    String line=null;
    int code;
    public ProgressDialog pDialog;
    String solu;
    int value;
    String name1,email1,password1,address1,mobile1;
	
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ob1 = new DBHelper(this);
        value = ob1.numberOfRows();
        if (value < 1) {
        setContentView(R.layout.signup);
        name= (EditText)findViewById(R.id.editText1);
        email =(EditText)findViewById(R.id.editText2);
        password=(EditText)findViewById(R.id.editText3);
		address=(EditText)findViewById(R.id.editText4);
		mobile=(EditText)findViewById(R.id.editText5);
        loginlink=(TextView)findViewById(R.id.textView1);
        signup=(Button)findViewById(R.id.button1);
        admin=(Button)findViewById(R.id.button2);
        ob = new DBHelper(this);
        signup.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 name1=name.getText().toString();
				 password1=password.getText().toString();
				 email1=email.getText().toString();
				address1=address.getText().toString();
				mobile1=mobile.getText().toString();
				 ob.insert1(name1,password1,email1);
				 
				 new insertTask().execute("");
			}
		});
        loginlink.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),login.class);
                startActivity(intent);
                finish();
			}
		});
        admin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(signup.this,Admin_login.class);
			startActivity(intent);
			}
		});
	}
    else{
        Intent intent = new Intent(signup.this,MainActivity.class);
        startActivity(intent);
    }
	}
	 class insertTask extends AsyncTask<String, String, String> {
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(signup.this);
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
	            nameValuePairs.add(new BasicNameValuePair("email", email1));
	            nameValuePairs.add(new BasicNameValuePair("password",password1));
				nameValuePairs.add(new BasicNameValuePair("address",address1));
				nameValuePairs.add(new BasicNameValuePair("mobile",mobile1));
	            try{
	            }catch(Exception e){
	            }
	            try {
	                HttpClient httpclient = new DefaultHttpClient();
	                HttpPost httppost = new HttpPost("http://192.168.43.51/grocery/users.php/");
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
				          Toast.makeText(getApplicationContext(), "User created",Toast.LENGTH_LONG);
			     	 Intent i= new Intent(signup.this,MainActivity.class);
			     	 startActivity(i);
          
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
