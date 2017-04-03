package com.example.grocery.grocery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Admin_update_product extends MainActivity {
	EditText list_name;
	EditText product_name;
	EditText quantity;
	Button update;
	
	protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.admin_update_products);
	
	list_name=(EditText)findViewById(R.id.editText1);

	product_name=(EditText)findViewById(R.id.editText2);

	quantity=(EditText)findViewById(R.id.editText3);


    update=(Button)findViewById(R.id.button1);
    
    update.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			Intent i=new Intent(Admin_update_product.this,Admin_button_details.class);
			startActivity(i);

		}
	});



}}

