package hetpin.dailyphoto;

import adapter.AddFeelingAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Window;

public class FeelingActivity extends FragmentActivity {
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
	AddFeelingAdapter adapter;

	/**
	 * The {@link android.support.v4.view.ViewPager} that will display the
	 * object collection.
	 */
	ViewPager mViewPager;

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_feeling);
		myApp = (MyApp) getApplication();
		adapter = new AddFeelingAdapter(getSupportFragmentManager(), FeelingActivity.this);
		mViewPager = (ViewPager) findViewById(R.id.pager_feeling);
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(Utility.get_value_by_key_int(FeelingActivity.this, DSetting.KEY_LAST_PACKAGE_INDEX));
	}
}
