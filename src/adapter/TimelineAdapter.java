package adapter;

import hetpin.dailyphoto.DSetting;
import hetpin.dailyphoto.MonthFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class TimelineAdapter extends FragmentStatePagerAdapter {
	private ArrayList<String> list_date;
	SimpleDateFormat formatter_db = new SimpleDateFormat(DSetting.date_format_db);
	SimpleDateFormat formatter_display = new SimpleDateFormat(DSetting.date_format_display);

	public TimelineAdapter(FragmentManager fm, ArrayList<String> list_date) {
		super(fm);
		this.list_date = list_date;
	}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = new MonthFragment();
		Bundle args = new Bundle();
		args.putInt(MonthFragment.ARG_OBJECT, i + 1); // Our object is just an
														// integer :-P
		args.putString(DSetting.date_obj, list_date.get(i));
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		// For this contrived example, we have a 100-object collection.
		return list_date.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		String result = list_date.get(position);
		try {
			Date date = formatter_db.parse(result);
			result = formatter_display.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
}
