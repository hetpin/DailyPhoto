package hetpin.dailyphoto;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import model.AnimateFirstDisplayListener;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import data.DBHelper;

public class PlusActivity extends Activity implements OnClickListener {
	private MyApp myApp;
	private DBHelper dbHelper;
	private static final int DATE_PICKER_ID = 0;
	private EditText tv_title;
	private EditText tv_location;
	private EditText tv_date_time;
	private ImageView iv_image;
	private String date_db;
	private SimpleDateFormat dfDate = new SimpleDateFormat(
			DSetting.date_format_display);
	private SimpleDateFormat dfDateDB = new SimpleDateFormat(
			DSetting.date_format_db);

	private ImageLoader imageLoader;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	// http://developer.android.com/reference/java/text/SimpleDateFormat.html
	// Sunday, Nov 9, 2003
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_plus);
		setTitle("Add photo");
		myApp = (MyApp) getApplication();
		if (myApp.cropped_bitmap == null) {
			Intent intent = new Intent();
			setResult(RESULT_CANCELED, intent);
			finish();
		}
		dbHelper = new DBHelper(getApplicationContext());
		imageLoader = myApp.getImageLoader();

		tv_date_time = (EditText) findViewById(R.id.tv_date_time);
		if (myApp.cur_date != null) {
			date_db = dfDateDB.format(myApp.cur_date);
			tv_date_time.setText(dfDate.format(myApp.cur_date));
		} else {
			Calendar cal = Calendar.getInstance();
			date_db = dfDateDB.format(cal.getTime());
			tv_date_time.setText(dfDate.format(cal.getTime()));
		}

		iv_image = (ImageView) findViewById(R.id.iv_image);
		tv_title = (EditText) findViewById(R.id.tv_title);
		tv_location = (EditText) findViewById(R.id.tv_location);
		if (myApp.cropped_bitmap != null) {
			iv_image.setImageBitmap(myApp.cropped_bitmap);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_PICKER_ID:
			Calendar cal = Calendar.getInstance();
			int day, month, year;
			if (myApp.cur_date != null) {
				day = myApp.cur_date.getDay();
				month = myApp.cur_date.getMonth();
				year = myApp.cur_date.getYear();
			} else{
				year = cal.get(Calendar.YEAR);
				month = cal.get(Calendar.MONTH);
				day = cal.get(Calendar.DAY_OF_MONTH);
			}
			return new DatePickerDialog(PlusActivity.this,
					new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							Log.e("request", "" + dayOfMonth + " "
									+ monthOfYear + " " + year);
							Date date = new Date(year - 1900, monthOfYear,
									dayOfMonth);
							tv_date_time.setText(dfDate.format(date.getTime()));
							date_db = dfDateDB.format(date.getTime());
						}
					}, year, month, day);
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.plus_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_save:
			// Save
			if (myApp.cropped_bitmap == null) {
				Intent intent = new Intent();
				setResult(RESULT_CANCELED, intent);
				finish();
			}
			try {
				File file = Utility.storeFile(tv_title.getText().toString()
						+ System.currentTimeMillis(), myApp.cropped_bitmap,
						getApplicationContext(), true);
				Log.e("save file ", "path = " + file.getPath());
				myApp.cropped_file = file;
				myApp.cropped_bitmap = null;
				// Calendar cal = Calendar.getInstance();
				dbHelper.insertPhoto(file.getPath(), tv_title.getText()
						.toString(), tv_location.getText().toString(),
						System.currentTimeMillis() + "", date_db);
			} catch (IOException e) {
				Log.e("save file ", "failed");
				e.printStackTrace();
			}
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			finish();
			return true;
		case R.id.action_cancel:
			// Cancel
			myApp.cropped_bitmap.recycle();
			Intent intent2 = new Intent();
			setResult(RESULT_CANCELED, intent2);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_date_time:
			// show date_time picker
			showDialog(DATE_PICKER_ID);
			break;
		case R.id.btn_save:
			// Save
			if (myApp.cropped_bitmap == null) {
				Intent intent = new Intent();
				setResult(RESULT_CANCELED, intent);
				finish();
			}
			try {
				File file = Utility.storeFile(tv_title.getText().toString()
						+ System.currentTimeMillis(), myApp.cropped_bitmap,
						getApplicationContext(), true);
				Log.e("save file ", "path = " + file.getPath());
				myApp.cropped_file = file;
				myApp.cropped_bitmap = null;
				// Calendar cal = Calendar.getInstance();
				dbHelper.insertPhoto(file.getPath(), tv_title.getText()
						.toString(), tv_location.getText().toString(),
						System.currentTimeMillis() + "", date_db);
			} catch (IOException e) {
				Log.e("save file ", "failed");
				e.printStackTrace();
			}
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			finish();
			break;
		case R.id.btn_cancel:
			myApp.cropped_bitmap.recycle();
			Intent intent2 = new Intent();
			setResult(RESULT_CANCELED, intent2);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		myApp.cur_date = null;
		super.onDestroy();
	}
}
