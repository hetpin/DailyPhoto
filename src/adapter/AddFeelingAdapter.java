package adapter;

import hetpin.dailyphoto.DSetting;
import hetpin.dailyphoto.FeelingFragment;
import hetpin.dailyphoto.MonthFragment;
import hetpin.dailyphoto.Utility;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class AddFeelingAdapter extends FragmentStatePagerAdapter {
	private ArrayList<String> list_package;
	private Context context;

	public AddFeelingAdapter(FragmentManager fm, Context context) {
		super(fm);
		this.context = context;
		this.list_package = new ArrayList<String>();
		//this.list_package.add("recent");
		this.list_package.add("omnom");
		this.list_package.add("doremon");
		this.list_package.add("mickey");
		this.list_package.add("girl");
		this.list_package.add("line_love");
	}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = new FeelingFragment();
		Bundle args = new Bundle();
		args.putInt(MonthFragment.ARG_OBJECT, i);
		args.putString(DSetting.date_obj, list_package.get(i));
		fragment.setArguments(args);
		Utility.insert_value_to_key_int(context, DSetting.KEY_LAST_PACKAGE_INDEX, i-1);
		return fragment;
	}

	@Override
	public int getCount() {
		return list_package.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		String result = list_package.get(position);
		return result;
	}
}
