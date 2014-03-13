package adapter;

import hetpin.dailyphoto.DSetting;
import hetpin.dailyphoto.ImageFullFragment;
import hetpin.dailyphoto.MonthFragment;

import java.util.ArrayList;

import model.Photo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class SliderAdapter extends FragmentStatePagerAdapter {
	private ArrayList<Photo> list_photo;

	public SliderAdapter(FragmentManager fm, ArrayList<Photo> list_photo) {
		super(fm);
		this.list_photo = list_photo;
	}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = new ImageFullFragment();
		Bundle args = new Bundle();
		args.putInt(MonthFragment.ARG_OBJECT, i);
		args.putString(DSetting.date_obj, list_photo.get(i)
				.getImageLoaderPath());
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		return list_photo.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		String result = list_photo.get(position).title;
		return result;
	}
}
