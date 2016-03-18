package com.zhuochuang.hsej.store;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.model.DataLoader;
import com.model.EventManager;
import com.model.TaskType;
import com.util.Utils;
import com.zhuochuang.hsej.BaseActivity;
import com.zhuochuang.hsej.R;

public class StoreUpdateShoppingAddressActivity extends BaseActivity{
	boolean mIsDone = false;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.store_shopping_update_address);
		setTitleText(R.string.update_address);
		mMainLayout.findViewById(R.id.ico_store_update).setVisibility(View.VISIBLE);
		initView();
		
		((EditText)findViewById(R.id.address_contacter_name)).setEnabled(false);
		((EditText)findViewById(R.id.address_contacter_phone)).setEnabled(false);
		((EditText)findViewById(R.id.address_contacter_detail_address)).setEnabled(false);
		
		mMainLayout.findViewById(R.id.ico_store_update).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!mIsDone){
					mIsDone = true;
					((TextView)findViewById(R.id.address_update)).setText(R.string.done);
					((TextView)findViewById(R.id.address_update)).setTextColor(Color.parseColor("#DD514F"));
					((EditText)findViewById(R.id.address_contacter_name)).setEnabled(true);
					((EditText)findViewById(R.id.address_contacter_phone)).setEnabled(true);
					((EditText)findViewById(R.id.address_contacter_detail_address)).setEnabled(true);
				}
				else{
					String name = ((EditText)findViewById(R.id.address_contacter_name)).getText().toString();
					String phone = ((EditText)findViewById(R.id.address_contacter_phone)).getText().toString();
					String address = ((EditText)findViewById(R.id.address_contacter_detail_address)).getText().toString();
					
					if(Utils.isTextEmpty(name)){
						Toast.makeText(StoreUpdateShoppingAddressActivity.this, R.string.update_name_null, Toast.LENGTH_SHORT).show();
						return;
					}
					if(Utils.isTextEmpty(phone)){
						Toast.makeText(StoreUpdateShoppingAddressActivity.this, R.string.update_phone_null, Toast.LENGTH_SHORT).show();
						return;
					}
					if(!Utils.isMobileNO(phone)){
						Toast.makeText(StoreUpdateShoppingAddressActivity.this, R.string.login_register_phone_valid,Toast.LENGTH_SHORT).show();
						return;
					}
					if(Utils.isTextEmpty(address)){
						Toast.makeText(StoreUpdateShoppingAddressActivity.this, R.string.update_address_null, Toast.LENGTH_SHORT).show();
						return;
					}
					showDialogCustom(DIALOG_CUSTOM);
					DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserAddressUpdateUserAddress,
							DataLoader.getInstance().setUpdateUserAddressTypeParams(getIntent().getStringExtra("id"),
									name, phone, "", address, getIntent().getIntExtra("mark", 0)), StoreUpdateShoppingAddressActivity.this);
				}
			}
		});
	}

	private void initView() {
		((EditText)findViewById(R.id.address_contacter_name)).setText(getIntent().getStringExtra("Name"));
		((EditText)findViewById(R.id.address_contacter_phone)).setText(getIntent().getStringExtra("Phone"));
		//mContacter_code = (EditText) findViewById(R.id.address_contacter_code);
		((EditText)findViewById(R.id.address_contacter_detail_address)).setText(getIntent().getStringExtra("Address"));
		findViewById(R.id.delete_address).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialogCustom(DIALOG_CUSTOM);
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserAddressDelUserAddress,
						DataLoader.getInstance().setDelUserAddressTypeParams(getIntent().getStringExtra("id")),StoreUpdateShoppingAddressActivity.this);
				
			}
		});
	   findViewById(R.id.set_default_address).setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String name = ((EditText)findViewById(R.id.address_contacter_name)).getText().toString();
			String phone = ((EditText)findViewById(R.id.address_contacter_phone)).getText().toString();
			String address = ((EditText)findViewById(R.id.address_contacter_detail_address)).getText().toString();
			if(Utils.isTextEmpty(name)){
				Toast.makeText(StoreUpdateShoppingAddressActivity.this, R.string.update_name_null, Toast.LENGTH_SHORT).show();
				return;
			}
			if(Utils.isTextEmpty(phone)){
				Toast.makeText(StoreUpdateShoppingAddressActivity.this, R.string.update_phone_null, Toast.LENGTH_SHORT).show();
				return;
			}
			if(!Utils.isMobileNO(phone)){
				Toast.makeText(StoreUpdateShoppingAddressActivity.this, R.string.login_register_phone_valid,Toast.LENGTH_SHORT).show();
				return;
			}
			if(Utils.isTextEmpty(address)){
				Toast.makeText(StoreUpdateShoppingAddressActivity.this, R.string.update_address_null, Toast.LENGTH_SHORT).show();
				return;
			}
			showDialogCustom(DIALOG_CUSTOM);
			DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserAddressUpdateUserAddress,
					DataLoader.getInstance().setUpdateUserAddressTypeParams(getIntent().getStringExtra("id"),
							name, phone, "", address, 1), StoreUpdateShoppingAddressActivity.this);
		}
	 });	
	}
	
	private void notifyCheckOrder(){
		EventManager.getInstance().sendMessage(EventManager.EventType_UpdateAddressPicker, 
				getIntent().getStringExtra("id"),
				((EditText)findViewById(R.id.address_contacter_name)).getText().toString(),
				((EditText)findViewById(R.id.address_contacter_phone)).getText().toString(),
				((EditText)findViewById(R.id.address_contacter_detail_address)).getText().toString());
	}
	
	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		super.taskFinished(type, result, isHistory);
		removeDialogCustom();
		if(result == null){
			return;
		}
		if(result instanceof Error){
			Toast.makeText(StoreUpdateShoppingAddressActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		switch (type) {
		case TaskOrMethod_UserAddressUpdateUserAddress:
			notifyCheckOrder();
			Toast.makeText(StoreUpdateShoppingAddressActivity.this,getResources().getString(R.string.update_success) , Toast.LENGTH_SHORT).show();						
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					finish();
				}
			}, 800);			
			break;
		case TaskOrMethod_UserAddressDelUserAddress:
			notifyCheckOrder();
			Toast.makeText(StoreUpdateShoppingAddressActivity.this,getResources().getString(R.string.delete_success) , Toast.LENGTH_SHORT).show();
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					finish();
				}
			}, 800);
			break;
		default:
			break;
		}
	}
}
