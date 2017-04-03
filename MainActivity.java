package com.example.grocery.grocery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	//ArrayList<String> listItems=new ArrayList<>();
	ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    Spinner sp;
    String type;
    String name;
    private String url = "http://192.168.43.51/grocery/productlist.php/";
    public ProgressDialog pDialog;
    private String jsonResult;
    String str1[]=new String[1000];
    Button search;
    String mail,s1;
    ListView listView;
    SQLiteDatabase db;
    DBHelper ob1, ob;
    String solu;
    Cursor rs;
    Button cart,request,userdetails;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_main);
        cart=(Button)findViewById(R.id.button2);
        request=(Button)findViewById(R.id.button3);
        userdetails=(Button)findViewById(R.id.button4);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, cart.class);
                startActivity(intent);
            }
        });
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Request_box.class);
                startActivity(intent);
            }
        });
        userdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,userdetails_display.class);
                startActivity(intent);
            }
        });

        db=openOrCreateDatabase("Grocery", MODE_PRIVATE, null);
        rs=db.rawQuery("select email from lists", null);
        while(rs.moveToNext()) {
            s1 = rs.getString(rs.getColumnIndex(DBHelper.EMAIL));
        }
        Toast.makeText(getApplicationContext(),s1, Toast.LENGTH_LONG).show();
        sp=(Spinner)findViewById(R.id.spinner1);
     search=(Button)findViewById(R.id.button1);
     listView=(ListView)findViewById(R.id.listView1);
     adapter=new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.txt,listItems);
     sp.setAdapter(adapter);
     sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             type = str1[position];

         }

         @Override
         public void onNothingSelected(AdapterView<?> parent) {
             // TODO Auto-generated method stub
         }
     });
     listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // int itemPosition = position;
             String product = ((TextView) view).getText().toString();
             Intent in = new Intent(MainActivity.this, product_disc.class);
             in.putExtra("product", product);
             startActivity(in);
         }
     });

     search.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             // TODO Auto-generated method stub
             Toast.makeText(getApplicationContext(),
                     type, Toast.LENGTH_LONG).show();

             new JsonReadTask().execute("");
         }
     });
    }
  public void onStart(){
     super.onStart();
     BackTask bt=new BackTask();
     bt.execute();
  }
  public class BackTask extends AsyncTask<Void,Void,Void> {
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
	           str1[i]=out;  
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
  private class JsonReadTask extends AsyncTask<String, Void, String> {
      @Override
      protected String doInBackground(String... params) {

          ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
          nameValuePairs.add(new BasicNameValuePair("type",type));
          try {
              HttpClient httpclient = new DefaultHttpClient();
              HttpPost httppost = new HttpPost("http://192.168.43.51/grocery/productlist.php/");
              httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
              HttpResponse response = httpclient.execute(httppost);
              jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
              Log.e("Result",jsonResult);
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
      List<Map<String, String>> employeeList = new ArrayList<Map<String, String>>();
      try {
          JSONArray jArray = new JSONArray(jsonResult);
          for (int i = 0; i < jArray.length() - 1; i++) {
              JSONObject json_data = jArray.getJSONObject(i);
                  name = String.valueOf(json_data.getString("name"));
                  String outPut = name;
                  employeeList.add(createEmployee("employees", outPut));
             
              }
      }catch (JSONException e) {
          Log.e("Failed",e.toString());
          Toast.makeText(getApplicationContext(),jsonResult, Toast.LENGTH_SHORT).show();
          Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_SHORT).show();
      }


      SimpleAdapter simpleAdapter = new SimpleAdapter(this, employeeList, android.R.layout.simple_list_item_1, new String[] { "employees" }, new int[] {android.R.id.text1});
      listView.setAdapter(simpleAdapter);
  }
  private HashMap<String, String> createEmployee(String name, String number) {
      HashMap<String, String> employeeNameNo = new HashMap<String, String>();
      employeeNameNo.put(name, number);
      return employeeNameNo;
  }




}
