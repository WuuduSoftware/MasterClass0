package com.wuudu.masterclass0;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MoveAdaper extends ArrayAdapter<DMove> {

	private Context mContext;
	private int mResource;
	
	public MoveAdaper(Context context, int resource, List<DMove> objects) {
		super(context, resource, objects);
		mContext = context;
		mResource = resource;
	}

	private class LevelNameHolder {
		public TextView tvName;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LevelNameHolder lh;
		DMove dato = getItem(position);
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(mResource, parent, false);
			lh = new LevelNameHolder();
			lh.tvName = (TextView) convertView.findViewById(R.id.tvName);
			convertView.setTag(lh);
		}
		else
			lh = (LevelNameHolder) convertView.getTag();
		lh.tvName.setText(dato.title);
		return convertView;
	}
	
}
