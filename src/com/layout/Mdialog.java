package com.layout;

import java.util.HashMap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.model.DataLoader;
import com.model.TaskListener;
import com.model.TaskType;
import com.util.ContentAdapter;
import com.zhuochuang.hsej.R;

public class Mdialog extends Dialog implements
		android.view.View.OnClickListener, OnItemClickListener, TaskListener {
	private Context mContext;
	Button comfirm, cancle;
	private ListView listView;
	private String[] mes;
	private HashMap<String, Object> params = new HashMap<String, Object>();
	private String discussionsId;
	private ContentAdapter adaper;
	private int count;
	private static int theme = R.style.ReportDialog;
	private  Dialog dialogs;
	public Mdialog(Context context, String UserID, String discussionId) {
		super(context, theme);
		mContext = context;
		discussionsId = discussionId;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_listview);

		mes = mContext.getResources().getStringArray(R.array.contents);
		findViewById(R.id.comfire).setOnClickListener(this);
		findViewById(R.id.cancle).setOnClickListener(this);
		listView = (ListView) findViewById(R.id.dialog_listview);
		listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

		listView.setAdapter(adaper = new ContentAdapter() {
			@Override
			public int getCount() {
				if (mes != null && mes.length > 0) {
					return mes.length;
				}
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				if (convertView == null) {
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.dialog_report_item, null);
				}
				View view = convertView.findViewById(R.id.checkedView);
				if (count == position) {
					view.setBackgroundResource(R.drawable.option_yes);
				} else {
					view.setBackgroundResource(R.drawable.option_no);
				}

				((TextView) convertView.findViewById(R.id.text))
						.setText(mes[position]);

				return convertView;
			}
		});

		listView.setOnItemClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comfire:
			dialogs = new AlertDialog.Builder(mContext).create();
	        dialogs.show();
	        dialogs.getWindow().setContentView(LayoutInflater.from(mContext).inflate(R.layout.dialogprogress, null ));
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					params.put("discussionId", discussionsId);
					params.put("type", count);
					params.put("content", mes[count]);
					DataLoader.getInstance().startTaskForResult(
							TaskType.TaskOrMethod_ReportSaveReport, params,
							Mdialog.this);		
				}
			}, 500);
			break;
		case R.id.cancle:
			this.dismiss();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		 count = position;
		 adaper.notifyDataSetChanged();
	}

	@Override
	public void taskStarted(TaskType type) {
		// TODO Auto-generated method stub

	}

	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		dialogs.dismiss();
		Mdialog.this.dismiss();
		if(result == null) {
			return;
		}
		
		if(result instanceof Error) {
			Toast.makeText(mContext, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		Toast.makeText(mContext, mContext.getResources().getString(R.string.report_success), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void taskIsCanceled(TaskType type) {
		// TODO Auto-generated method stub

	}
}
