package hetpin.dailyphoto;

import java.util.ArrayList;

import data.DB;
import data.DBHelper;
import adapter.TimelineAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Window;

public class TimelineActivity extends FragmentActivity {
	private DBHelper dbHelper;
	private MyApp myApp;
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments representing each object in a collection. We use a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter} derivative,
	 * which will destroy and re-create fragments as needed, saving and
	 * restoring their state in the process. This is important to conserve
	 * memory and is a best practice when allowing navigation between objects in
	 * a potentially large collection.
	 */
	TimelineAdapter adapter;

	/**
	 * The {@link android.support.v4.view.ViewPager} that will display the
	 * object collection.
	 */
	ViewPager mViewPager;
	private ArrayList<String> list_dates;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_timeline);
		myApp= (MyApp) getApplication();
		dbHelper = new DBHelper(getApplicationContext());
		list_dates = dbHelper.getDateContainPhotos();
		if (list_dates == null || list_dates.size() == 0) {
			finish();
		}
		// Create an adapter that when requested, will return a fragment
		// representing an object in
		// the collection.
		//
		// ViewPager and its adapters use support library fragments, so we must
		// use
		// getSupportFragmentManager.
		adapter = new TimelineAdapter(getSupportFragmentManager(), list_dates);
		//set current date
		String cur_date_str = myApp.getCurDateFormatDb();
		//Log.e("current date", cur_date_str + " |");
		if (cur_date_str == null) {
			finish();
		}
		// Set up the ViewPager, attaching the adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(adapter);
		int size = list_dates.size();
		int i = 0;
		for (i = 0; i < size; i++) {
			//Log.e("seaching", list_dates.get(i));
			if (list_dates.get(i).equals(cur_date_str)) {
				//Log.e(list_dates.get(i), cur_date_str);
				mViewPager.setCurrentItem(i, true);
				break;
			}
		}
		if (i == size) {
			//mean no date photo exist
			finish();
			
		}
	}
}
