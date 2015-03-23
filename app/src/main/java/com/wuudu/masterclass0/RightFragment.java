package com.wuudu.masterclass0;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class RightFragment extends Fragment {

	private ListView mLV;
	private Context mC;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.fragment2, container, false);
		mC = mView.getContext();
		mLV = (ListView) mView.findViewById(R.id.lvMoves);
		return mView;
	}	
	
	public void setLevel(final int level) {
		mLV.setAdapter(new MoveAdaper(mC, R.layout.name_item, MainActivity2.DLList.get(level).Moves));
		mLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startActivity(new Intent(getActivity(), ItemActivity2.class)
				.putExtra("level", level)
				.putExtra("move", position));
			}
		});
	}
	
}
