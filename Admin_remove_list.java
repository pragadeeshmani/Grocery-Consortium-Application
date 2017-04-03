package com.example.grocery.grocery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Admin_remove_list extends MainActivity{
	
	EditText product;
	Button remove;
	protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.admin_remove_list);
	      
	      product=(EditText)findViewById(R.id.editText1);
	      remove=(Button)findViewById(R.id.button1);
	      
	      remove.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				Intent i=new Intent(Admin_remove_list.this,Admin_remove_list.class);
				startActivity(i);
				
				
				
			}
		});
	      
	   

}
}