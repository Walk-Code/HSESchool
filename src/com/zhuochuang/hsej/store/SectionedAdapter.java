package com.zhuochuang.hsej.store;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.model.ImageLoaderConfigs;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.ContentAdapter;
import com.zhuochuang.hsej.R;


public class SectionedAdapter extends SectionedBaseAdapter {
	
	private Context mContext;
	private String[] leftStr;
	private Map<String,Object> map;
	public SectionedAdapter(Context context, String[] leftStr,Map<String,Object> map){
		this.mContext = context;
		this.leftStr = leftStr;
		this.map = map;
	}

    @Override
    public Object getItem(int section, int position) {
        return ((JSONArray)map.get(leftStr[section])).optJSONObject(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        return leftStr.length == 0 ? 0 : leftStr.length;
    }

    @Override
    public int getCountForSection(int section) { 
    	System.out.println("====sec " + section + "  ,, ");
    	if(((JSONArray)map.get(leftStr[section])).length() == 0){
    		return 0;
    	}/*else if(1 < ((JSONArray)map.get(leftStr[section])).length()){
    		System.out.println("====secc " + (((JSONArray)map.get(leftStr[section])).length()-1));
    		return ((JSONArray)map.get(leftStr[section])).length()-1;
    	}else{
    		System.out.println("====secc " + ((JSONArray)map.get(leftStr[section])).length());
    		return  ((JSONArray)map.get(leftStr[section])).length();
    	}*/ 
    	else{
    		return 1;
    	}
    }

    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
    	ViewHolder holder;
    	if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.store_category_right, null);
            holder = new ViewHolder();
            holder.gridView = (GridViewForListView) convertView.findViewById(R.id.gridView);
            
            holder.gridAdapter = new GridViewAdapter();
            holder.gridView.setAdapter(holder.gridAdapter);
			
            convertView.setTag(holder);
        }else{
			holder = (ViewHolder) convertView.getTag();
		}
        
        holder.gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mContext,StorePersonActivity.class);
				intent.putExtra("shopId", ((JSONArray)map.get(leftStr[section])).optJSONObject(position).optString("id"));
				mContext.startActivity(intent);
			}			
		});	
        
        final JSONArray dataArr = ((JSONArray)map.get(leftStr[section]));       
        holder.gridAdapter.setData(dataArr);
		holder.gridAdapter.notifyDataSetChanged();
		
        return convertView;
    }
    
    class GridViewAdapter extends ContentAdapter{
    	JSONArray arr;
    	
    	public void setData(JSONArray data){
    		this.arr = data;
    	}
    	
		@Override
		public int getCount() {					
			return arr == null ? 0 : arr.length();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = LayoutInflater.from(mContext).inflate(R.layout.store_category_grid_item, null);
			}
			
			JSONObject obj = arr.optJSONObject(position);			
			ImageLoader.getInstance().displayImage(obj.optString("logo"), ((ImageView)convertView.findViewById(R.id.store_category_typeicon)),ImageLoaderConfigs.displayImageOptionsBg);
			((TextView)convertView.findViewById(R.id.store_category_typename)).setText(obj.optString("name"));					
			return convertView;
		};
    }
    
    class ViewHolder{
    	GridViewForListView gridView;
    	GridViewAdapter gridAdapter;
	}

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.header_item, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        layout.setClickable(false);
        ((TextView) layout.findViewById(R.id.toptype)).setText(leftStr[section]);
        return layout;
    }

}
