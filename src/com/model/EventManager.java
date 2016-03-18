package com.model;

import java.util.ArrayList;

import android.os.Handler;

public class EventManager {

	private static EventManager controler;
	private ArrayList<Handler> mHandlerList = new ArrayList<Handler>();
	public static final int 
				EventType_ServicesAdd = 1,
				EventType_ServicesAddDone = 2,
				EventType_Photoalbum = 3,
				EventType_AreaGroup = 4,
				EventType_SomeFriends = 5,
				EventType_RefreshAddressBook = 6,
				EventType_ClubAttention = 7,
				EventType_ReloadAlbum = 8,
				EventType_MsgChange = 9,
				EventType_SurveyChange = 10,
				EventType_LoginForRegister = 11,
				EventType_PostCommentCount = 12,
				EventType_PostReadCount = 13,
				EventType_MsgMyfriendChange = 14,
				EventType_VoteChange = 15,
				
				EventType_UpdateUI = 16,
				EventType_ShoppingCar = 17,
				EventType_UpdateAddress = 18,
				EventType_UpdateAddressAddNew = 19,
				EventType_UpdateAddressPicker = 20,
				EventType_EvaluateFinish = 21,
				EventType_StoreHomepageLoad = 22;
	
	public static EventManager getInstance(){
		if(controler == null){
			controler = new EventManager();
		}
		return controler;
	}
	
	public void setHandlerListenner(Handler handler){
		if(mHandlerList == null){
			mHandlerList = new ArrayList<Handler>();
		}
		mHandlerList.add(handler);
	}
	
	public void removeHandlerListenner(Handler handler){
		if(mHandlerList == null){
			mHandlerList = new ArrayList<Handler>();
		}
		if(handler != null){
			mHandlerList.remove(handler);
		}
	}
	
	public void sendMessage(final int messageType, final Object...objs){
		if(mHandlerList == null){
			mHandlerList = new ArrayList<Handler>();
		}
		for(Handler handler : mHandlerList){
			android.os.Message msg = handler.obtainMessage();			
			msg.what = messageType;
			msg.obj = objs;
			msg.sendToTarget();
		}
	}
	
	public void release(){
		if(mHandlerList != null){
			mHandlerList.clear();
			mHandlerList = null;
		}
		System.gc();
	}
}
