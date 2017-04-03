package com.example.grocery.grocery;

import java.text.BreakIterator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Admin_button_details extends MainActivity{
	Button edit_details;
	Button add_products;
	Button update_list;
	Button update_product;
	Button remove_list;
	Button remove_product;
	Button request;
	Button logout;
	protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.admin_button_details);
	   
	      edit_details=(Button)findViewById(R.id.button1);
	      
	      edit_details.setOnClickListener(new View.OnClickListener() {

			  @Override
			  public void onClick(View v) {
				  // TODO Auto-generated method stub

				  Intent i = new Intent(Admin_button_details.this, Admin_details.class);
				  startActivity(i);
			  }
		  });
	      add_products=(Button)findViewById(R.id.button2);
	      add_products.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				
					Intent i=new Intent(Admin_button_details.this,Admin_add_products.class);
					startActivity(i);
				}
			});
	      update_list=(Button)findViewById(R.id.button3);
	      update_list.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				
					Intent i=new Intent(Admin_button_details.this,Admin_update_list.class);
					startActivity(i);
				}
			});
	
	      update_product=(Button)findViewById(R.id.button4);
	      update_product.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				
					Intent i=new Intent(Admin_button_details.this,Admin_update_product.class);
					startActivity(i);
				}
			});
	      remove_list=(Button)findViewById(R.id.button5);
	      remove_list.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i=new Intent(Admin_button_details.this,Admin_remove_list.class);
					startActivity(i);
				}
			});
	      remove_product=(Button)findViewById(R.id.button6);
	      remove_product.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i=new Intent(Admin_button_details.this,Admin_remove_product.class);
					startActivity(i);
				}
			});
		request=(Button)findViewById(R.id.button7);
		request.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Admin_button_details.this,Admin_request_box.class);
				startActivity(intent);
			}
		});
	      logout=(Button)findViewById(R.id.button8);
	      logout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i=new Intent(Admin_button_details.this,login.class);
					startActivity(i);
					
				}
			});
}
}