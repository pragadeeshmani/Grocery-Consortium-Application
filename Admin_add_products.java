package com.example.grocery.grocery;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
public class Admin_add_products extends Activity{
	EditText product_name,product_details,product_price;
	String name,details,price;
	Button add,selectimage;
	String arr[]=new String[1000];
    InputStream is=null;
    String result,image1;
    String line=null;
    int code;
    public ProgressDialog pDialog;
    String solu;
    String location;
    int value;
    private ImageView imageView;
    private Uri filePath;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    Spinner sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.admin_add_products);
	      selectimage=(Button)findViewById(R.id.button2);
	      product_name=(EditText)findViewById(R.id.editText1);
	      product_details=(EditText)findViewById(R.id.editText2);
	      product_price=(EditText)findViewById(R.id.editText3);
	      add=(Button)findViewById(R.id.button1);
	      sp=(Spinner)findViewById(R.id.spinner1);
	      imageView = (ImageView) findViewById(R.id.imageView);
	      adapter=new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.txt,listItems);
	      add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				name=product_name.getText().toString();
				details=product_details.getText().toString();
				price=product_price.getText().toString();
				//Toast.makeText(getApplicationContext(), location, Toast.LENGTH_LONG).show();
				new insertTask().execute("");
				//Intent i=new Intent(Admin_add_products.this,Admin_add_products.class);
				//startActivity(i);
			}
		});
	      selectimage.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                Intent intent = new Intent();
	                intent.setType("image/*");
	                intent.setAction(Intent.ACTION_GET_CONTENT);
	                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
	            }
	        });



	      sp.setAdapter(adapter);
	        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	            @Override
	            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
	                location = arr[position];
	            }
	            @Override
	            public void onNothingSelected(AdapterView<?> parent) {
	                // TODO Auto-generated method stub
	            }
	        });


}
	
	class insertTask extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Admin_add_products.this);
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
            nameValuePairs.add(new BasicNameValuePair("name", name));
            nameValuePairs.add(new BasicNameValuePair("details",details));
            nameValuePairs.add(new BasicNameValuePair("price",price));
            nameValuePairs.add(new BasicNameValuePair("categories",location));
            image1 = getStringImage(bitmap);

            Log.e("pass 1", image1);

            nameValuePairs.add(new BasicNameValuePair("pic", image1));
            try{
            }catch(Exception e){
            }
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://192.168.43.51/grocery/add_products.php/");
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
			        product_name.setText("");
					product_details.setText("");
					product_price.setText("");
					
						
			            } else {
			            	Toast.makeText(getApplicationContext(), "User creation Failed",Toast.LENGTH_LONG);
			            }
					
				}
			});
            
         
        }
        public void onPostExecute(String result) {
            pDialog.dismiss();


        }}
	public void onStart(){
	     super.onStart();
	     BackTask bt=new BackTask();
	     bt.execute();
	  }
	  private class BackTask extends AsyncTask<Void,Void,Void> {
	     ArrayList<String> list;
	     protected void onPreExecute(){
	       super.onPreExecute();
	       list=new ArrayList<String>();
	     }
	     protected Void doInBackground(Void...params){
	       InputStream is=null;
	       String result="";
	       try{
	          HttpClient httpclient=new DefaultHttpClient();
	          HttpPost httppost= new HttpPost("http://192.168.43.51/grocery/categories.php");
	          HttpResponse response=httpclient.execute(httppost);
	          HttpEntity entity = response.getEntity();
	          // Get our response as a String.
	          is = entity.getContent();
	       }catch(IOException e){
	          e.printStackTrace();
	       }

	       //convert response to string
	       try{
	          BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"));           
	          String line = null;
	          while ((line = reader.readLine()) != null) {
	            result+=line;
	          }
	          is.close();
	          //result=sb.toString();
	       }catch(Exception e){
	          e.printStackTrace();
	       }
	       // parse json data
	       try{
	          JSONArray jArray =new JSONArray(result);
	          for(int i=0;i<jArray.length();i++){
	             JSONObject jsonObject=jArray.getJSONObject(i);
	             // add interviewee name to arraylist
	           String out=jsonObject.getString("name");
	           arr[i]=out;  
	           list.add(out);
	          
	         }
	       }
	       catch(JSONException e){
	          e.printStackTrace();
	       }
	       return null;
	     }
	     protected void onPostExecute(Void result){
	       listItems.addAll(list);
	       adapter.notifyDataSetChanged();
	     }
  }
	  @Override
      protected void onActivityResult(int requestCode, int resultCode, Intent data) {
          super.onActivityResult(requestCode, resultCode, data);

          if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

              filePath = data.getData();
              try {
                  bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                  imageView.setImageBitmap(bitmap);
              } catch (IOException e) {
                  e.printStackTrace();
              }

          }
      }

      public String getStringImage(Bitmap bmp) {
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
          byte[] imageBytes = baos.toByteArray();
          String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
          return encodedImage;
      }


	  }