package com.example.grocery.grocery;
import android.app.Activity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by DELL on 3/16/2017.
 */
public class cart extends Activity {
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ListView listView;
    Spinner sp;
    private String jsonResult;
    String str1[]=new String[1000];
    String type,name;
    String mail,s1;
    int price1=0;
String price5;
    Cursor rs;
    SQLiteDatabase db;
    TextView pricedisp;
    Button pay;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
        pricedisp=(TextView)findViewById(R.id.textView1);
        pay=(Button)findViewById(R.id.button1);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(cart.this,payment.class);
               i.putExtra("price",pricedisp.getText().toString());
           startActivity(i);
            }
        });
        db=openOrCreateDatabase("Grocery", MODE_PRIVATE, null);
        rs=db.rawQuery("select email from lists", null);
        while(rs.moveToNext()) {
            s1 = rs.getString(rs.getColumnIndex(DBHelper.EMAIL));
        }
 // Toast.makeText(getApplicationContext(),s1, Toast.LENGTH_LONG).show();
        listView=(ListView)findViewById(R.id.listView1);
        new JsonReadTask().execute("");
    }
    private class JsonReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("email",s1));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://192.168.43.51/grocery/cart_disp.php/");
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
                name = "Name:" + String.valueOf(json_data.getString("name"));
                String quatity = "Quantity:" + String.valueOf(json_data.getString("quantity"));
                String price = "Price:" + String.valueOf(json_data.getString("price"));
                int price2 = Integer.valueOf(json_data.getInt("price"));
                String outPut = name + "\n" + quatity + "\n" + price;
                price1 = price1 + price2;

                employeeList.add(createEmployee("employees", outPut));
            }
            price5=Integer.toString(price1);
           pricedisp.setText(price5);


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
