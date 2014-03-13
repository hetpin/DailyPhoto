package hetpin.dailyphoto;

import java.util.ArrayList;

import model.Photo;
import adapter.SliderAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Window;

public class SliderActivity extends FragmentActivity {
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
	SliderAdapter adapter;

	/**
	 * The {@link android.support.v4.view.ViewPager} that will display the
	 * object collection.
	 */
	ViewPager mViewPager;
	private ArrayList<Photo> list_photo;
	@Override
	protected void onDestroy() {
		//myApp.slider_cur_position = 0;
		//myApp.slider_list_photos = null;
		super.onDestroy();
	}
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Log.e("SliderActivity", "onCreate");
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_slider);
		myApp= (MyApp) getApplication();
		list_photo = myApp.slider_list_photos;
		if (list_photo == null || list_photo.size() == 0) {
			finish();
		}
		adapter = new SliderAdapter(getSupportFragmentManager(), list_photo);
		//set current item
		// Set up the ViewPager, attaching the adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager_slider);
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(myApp.slider_cur_position, true);
	}
}
