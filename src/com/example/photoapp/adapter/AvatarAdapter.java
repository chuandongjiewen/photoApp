package com.example.photoapp.adapter;

import java.io.IOException;
import java.util.ArrayList;

import java.util.HashMap;


import com.example.photoapp.MainActivity;
import com.qzone.model.feed.User;
import com.tencent.component.widget.AsyncImageView;

import com.example.photoapp.R;

import android.R.integer;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;


public class AvatarAdapter extends BaseAdapter{

	class ViewHolder {
		AsyncImageView avatar;
	}
	
	private MainActivity mainActivity;
	private ArrayList<User> userList;
	private LayoutInflater inflater;
	private AssetManager assetManager;

	private HashMap<Integer, User> clickedMap;


	public AvatarAdapter(Context context, ArrayList<User> userList){
		this.mainActivity = (MainActivity)context;
		this.userList = userList;
		this.inflater = LayoutInflater.from(context);
		this.assetManager = mainActivity.getResources().getAssets();

		this.clickedMap = new HashMap<Integer, User>();


	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userList.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return userList.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.tpl_list_avatar, parent, false);
			holder = new ViewHolder();
			holder.avatar = (AsyncImageView)convertView.findViewById(R.id.list_avatar);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		User user = userList.get(position);
		Bitmap bitmap;
		try {
			bitmap = BitmapFactory.decodeStream(assetManager.open("avatar/" + user.getAvatar()));
			holder.avatar.setImageBitmap(bitmap);

			//保存click之后的状态
			if (clickedMap.containsKey(position)) {
				holder.avatar.setRingWidth(4);
			}else{
				holder.avatar.setRingWidth(2);
			}
		} catch (IOException e) {
			holder.avatar.setBackgroundResource(R.drawable.ic_launcher);
			e.printStackTrace();
		}
		
		bindEvent(holder, position, convertView, parent);
		return convertView;
	}


	private void bindEvent(final ViewHolder holder, final int position, View convertView, ViewGroup parent){
		

		holder.avatar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if (clickedMap.containsKey(position)) {
					clickedMap.remove(position);
					holder.avatar.setRingWidth(2);
				}else{
					clickedMap.put(position, userList.get(position));
					holder.avatar.setRingWidth(4);
				}
				
				TextView personNumView = (TextView)mainActivity.findViewById(R.id.person_num);
				personNumView.setText( clickedMap.size() + "个人被圈出"+position);

			}
		});
	}

}
