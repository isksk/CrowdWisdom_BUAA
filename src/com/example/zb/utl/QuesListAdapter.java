package com.example.zb.utl;


import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.beihangQA_test.R;

public class QuesListAdapter extends BaseAdapter {  
	  
    private class MyViewHolder {  
        TextView tag;  
        TextView title;
        TextView info;
        TextView credit;
       
    }  
  
    private List<Map<String, Object>> mAppList;  
    private LayoutInflater mInflater;  

    public QuesListAdapter(Context c, List<Map<String, Object>> contentList) {  
        mAppList = contentList;  
        mInflater = (LayoutInflater) c  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
    }  
      
    public void clear(){  
        if(mAppList!=null){  
            mAppList.clear();  
        }  
    }  
  
    public int getCount() {  
        return mAppList.size();  
    }  
  
    @Override  
    public Object getItem(int position) {  
        return mAppList.get(position);  
    }  
  
    @Override  
    public long getItemId(int position) {  
        // TODO Auto-generated method stub  
        return position;  
    }  
    
    public void refresh(List<Map<String, Object>> list) {
		mAppList = list;
		notifyDataSetChanged();
	}
  
    public View getView(int position, View convertView, ViewGroup parent) {  
        MyViewHolder holder;  
        if (convertView == null) {  
            convertView = mInflater.inflate(R.layout.list_layout, null);  
            holder = new MyViewHolder();  
            holder.tag = (TextView) convertView.findViewById(R.id.listTag);  
            holder.title = (TextView) convertView.findViewById(R.id.listTitle);  
            holder.info = (TextView) convertView.findViewById(R.id.listInfo);  
            holder.credit = (TextView) convertView.findViewById(R.id.listCredit);
            convertView.setTag(holder);  
        } else {  
            holder = (MyViewHolder) convertView.getTag();  
        }  
        Map<String, Object> item = mAppList.get(position);
        
        holder.tag.setText(item.get("tag").toString());
        holder.title.setText(item.get("title").toString());
        holder.info.setText(item.get("info").toString());
        holder.credit.setText(item.get("credit").toString());
        
        return convertView;  
    }  
      
    public void remove(int position){  
        mAppList.remove(position);  
        this.notifyDataSetChanged();  
    }  
  
}  