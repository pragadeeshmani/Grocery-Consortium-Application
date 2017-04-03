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

public class Admin_login extends Activity{
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
	String email1,password1,name1,result;
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	        setContentView(R.layout.admin_login);
	        login=(Button) findViewById(R.id.button1);
	        email=(EditText)findViewById(R.id.editText1);
	        password=(EditText)findViewById(R.id.editText2);
	        login.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog = ProgressDialog.show(Admin_login.this, "", "Validating user...", true);
	                new Thread(new Runnable() {
	                    public void run() {
	                        login();
	                    }
	                }).start();
	            }
			});
	 }
	    void login(){
	        try{

	            httpclient=new DefaultHttpClient();
	            httppost= new HttpPost("http://192.168.43.51/grocery/admin_login.php/");
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
	            	startActivity(new Intent(Admin_login.this, Admin_button_details.class));
	            }else{
	                showAlert();
	            }

	        }catch(Exception e){
	            dialog.dismiss();
	            System.out.println("Exception : " + e.getMessage());
	        }
	    }
	    public void showAlert(){
	        Admin_login.this.runOnUiThread(new Runnable() {
	            public void run() {
	                AlertDialog.Builder builder = new AlertDialog.Builder(Admin_login.this);
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
}