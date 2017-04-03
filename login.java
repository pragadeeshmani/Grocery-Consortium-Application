package com.example.grocery.grocery;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login extends Activity {
	private static final String TAG = "login";
	Boolean val;
	HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
    int flag=0;
    SQLiteDatabase db;
    DBHelper ob1,ob;
    Cursor rs;
    InputStream is=null;
	EditText email;
	EditText password;
	Button login;
	TextView newuser;
	String email1,password1,name1,result;
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	        setContentView(R.layout.login);
	        login=(Button) findViewById(R.id.button1);
	        email=(EditText)findViewById(R.id.editText1);
	        password=(EditText)findViewById(R.id.editText2);
	        newuser=(TextView)findViewById(R.id.textView1);
	        login.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog = ProgressDialog.show(login.this, "", "Validating user...", true);
	                new Thread(new Runnable() {
	                    public void run() {
	                        login();
	                    }
	                }).start();
	            }
			});
	        newuser.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getApplicationContext(),signup.class);
	                startActivity(intent);
	                finish();
				}
			});
	 }
	    void login(){
	        try{

	            httpclient=new DefaultHttpClient();
	            httppost= new HttpPost("http://192.168.43.51/grocery/login.php/");
	            nameValuePairs = new ArrayList<NameValuePair>(2);
	            nameValuePairs.add(new BasicNameValuePair("email",email.getText().toString().trim()));
	            nameValuePairs.add(new BasicNameValuePair("password",password.getText().toString().trim()));
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            response=httpclient.execute(httppost);
	            ResponseHandler<String> responseHandler = new BasicResponseHandler();
	            final String response = httpclient.execute(httppost, responseHandler);
	            System.out.println("Response : " + response);
	            runOnUiThread(new Runnable() {
	                public void run() {
	                    //tv.setText("Response from PHP : " + response);
	                    dialog.dismiss();
	                }
	            });

	            if(response.equalsIgnoreCase("User Found")){
	      login1();
	                //startActivity(new Intent(oldlogin.this, MainActivity.class));
	            }else{
	                showAlert();
	            }

	        }catch(Exception e){
	            dialog.dismiss();
	            System.out.println("Exception : " + e.getMessage());
	        }
	    }
	    public void showAlert(){
	        login.this.runOnUiThread(new Runnable() {
	            public void run() {
	                AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
	                builder.setTitle("Login Error.");
	                builder.setMessage("User not Found.")
	                        .setCancelable(false)
	                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	                            public void onClick(DialogInterface dialog, int id) {
	                            }
	                        });
	                AlertDialog alert = builder.create();
	                alert.show();
	            }
	        });
	    }
	    void login1(){
	        try{

	            httpclient=new DefaultHttpClient();
	            httppost= new HttpPost("http://192.168.43.51/grocery/loginfetch.php/");
	            nameValuePairs = new ArrayList<NameValuePair>(1);
	            nameValuePairs.add(new BasicNameValuePair("username",email.getText().toString().trim()));
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            HttpResponse response = httpclient.execute(httppost);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();
	            Log.e("log_tag", "connection success");

	        } catch (Exception e) {
	            Log.e("log_tag", "Error in http connection"+ e.toString());
	            Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();

	        }

	        try {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	            is.close();

	            result = sb.toString();
	            Log.e("log_tag", result);

	        } catch (Exception e) {
	            Log.e("log_tag", "Error converting result" + e.toString());
	            Toast.makeText(getApplicationContext(), "Input reading fail", Toast.LENGTH_SHORT).show();

	        }
	        //===================================
	        try {
	            JSONArray jArray = new JSONArray(result);
	            for (int i = 0; i < jArray.length() - 1; i++) {

	                JSONObject json_data = jArray.getJSONObject(i);
	               name1=String.valueOf(json_data.getString("name"));
	                email1= String.valueOf(json_data.getString("email"));
	                password1= String.valueOf(json_data.getString("password"));
	            }
	            ob = new DBHelper(this);
	            val= ob.insert1(name1,email1, password1);
	        }
	        catch (JSONException e) {
	            Log.e("log_tag", "Error parsing data" + e.toString());
	            Toast.makeText(getApplicationContext(), "JsonArray fail", Toast.LENGTH_SHORT).show();
	        }
	        if(val==true) {
	            runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(login.this, "Login Success", Toast.LENGTH_SHORT).show();
					}
				});
             runOnUiThread(new Runnable() {
				 @Override
				 public void run() {
					 Intent in = new Intent(login.this, MainActivity.class);
					 in.putExtra("email", email1);
					 startActivity(in);
				 }
			 });

	        }
	        else{
	            Toast.makeText(getApplicationContext(), "Sorry there is a problem", Toast.LENGTH_LONG).show();
	        }
	    }
}
