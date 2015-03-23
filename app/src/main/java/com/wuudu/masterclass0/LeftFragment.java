package com.wuudu.masterclass0;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class LeftFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.fragment1, container, false);
		
		ListView mLV = (ListView) mView.findViewById(R.id.lvList);
		mLV.setAdapter(new LevelAdaper(mView.getContext(), R.layout.name_item, MainActivity2.DLList));
		mLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				RightFragment rf = (RightFragment) getFragmentManager().findFragmentById(R.id.right);
				rf.setLevel(position);
			}
		});
		return mView;
	}
	
}
