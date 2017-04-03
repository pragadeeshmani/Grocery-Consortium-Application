package com.example.grocery.grocery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

public class product_disc extends Activity {

    HttpPost httppost;
    Button addproduct;
    int code;
    //TextView vehtype,owner,vehno,vehcc,address,city,state,pin,salary,email,mobile;
   String name1,description1,price1,category1,image1;
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
    JSONObject jsonResponse;
    HttpClient httpclient;
    Button book;
    List<NameValuePair> nameValuePairs;
    public ProgressDialog pDialog;
    ImageView img;
   String images,type,number;
	SQLiteDatabase db;
	DBHelper ob1, ob;
	String s1;
	Cursor rs;
	private Bitmap bitmap;
	String line=null;
	EditText quantity;
	int x,y;
	String sent;
	
	TextView name,description,price,category;
	String product;
	 protected void onCreate(Bundle savedInstanceState) {
	     super.onCreate(savedInstanceState);
	     setContentView(R.layout.product_disc);

	        Intent intent = getIntent();
		 db=openOrCreateDatabase("Grocery", MODE_PRIVATE, null);
		 rs=db.rawQuery("select email from lists", null);
		 while(rs.moveToNext()) {
			 s1 = rs.getString(rs.getColumnIndex(DBHelper.EMAIL));
		 }
		 Toast.makeText(getApplicationContext(),s1, Toast.LENGTH_LONG).show();
	       product= intent.getExtras().getString("product");
			img=(ImageView)findViewById(R.id.imageView2);
	     name=(TextView)findViewById(R.id.textView1);
		 price=(TextView)findViewById(R.id.textView2);
		 description=(TextView)findViewById(R.id.textView3);
		 category=(TextView)findViewById(R.id.textView4);
		 quantity=(EditText)findViewById(R.id.editText1);
		 addproduct= (Button) findViewById(R.id.button1);
		 addproduct.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {
				 name2=s1.toString();
				 quantity1=quantity.getText().toString();
				 price1=price.getText().toString();
				  x=(Integer.parseInt(quantity1)* Integer.parseInt(price1));
				 sent=Integer.toString(x);
				 new insertTask().execute("");

			 }
		 });
	new JsonReadTask().execute("");
	 }
	class insertTask extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(product_disc.this);
			pDialog.setMessage("Creating Products..");
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
			nameValuePairs.add(new BasicNameValuePair("email",name2));
			nameValuePairs.add(new BasicNameValuePair("quantity",quantity1));
			nameValuePairs.add(new BasicNameValuePair("price",sent));
			//nameValuePairs.add(new BasicNameValuePair("",x));


			try{
			}catch(Exception e){
			}
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("http://192.168.43.51/grocery/add_cart.php/");
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

		}



		public void onPostExecute(String result) {
			pDialog.dismiss();


		}}

	private class JsonReadTask extends AsyncTask<String, Void, String> {
	        @Override
	        protected String doInBackground(String... params) {
	            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	            nameValuePairs.add(new BasicNameValuePair("name",product));
	            try {
	                HttpClient httpclient = new DefaultHttpClient();
	                HttpPost httppost = new HttpPost("http://192.168.43.51/grocery/product_disc.php/");
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
	                images= String.valueOf(json_data.getString("image"));
	                name1=String.valueOf(json_data.getString("name"));
	                description1=String.valueOf(json_data.getString("details"));
	                price1=String.valueOf(json_data.getString("price"));
	                category1=String.valueOf(json_data.getString("categories"));
	            }
	            decodedString=null;
	            decodedString = Base64.decode(images.getBytes(), Base64.DEFAULT);
	            img.setImageBitmap(null);
	            bi=BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
	            img.setImageBitmap(bi);
	           name.setText(name1);
	            description.setText(description1);
	            price.setText(price1);
	            category.setText(category1);
	        }catch (JSONException e) {
	            Log.e("Failed",e.toString());
	            Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_SHORT).show();
	        }
	}

	 
	 
	 
}
