package com.example.grocery.grocery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import java.io.InputStream;
import java.util.List;

/**
 * Created by priya on 19-03-2017.
 */
public class edit_userdetails extends Activity {

    String mail,s1;
    Cursor rs;
    SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_userdetails);
        db=openOrCreateDatabase("Grocery", MODE_PRIVATE, null);
        rs=db.rawQuery("select email from lists", null);
        while(rs.moveToNext()) {
            s1 = rs.getString(rs.getColumnIndex(DBHelper.EMAIL));
        }
        Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();
    }
}
