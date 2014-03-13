package hetpin.dailyphoto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import model.CellListener;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private static final int PICK_FROM_CAMERA = 1;
	private static final int PICK_FROM_FILE = 3;
	private static final int PLUS_ACTIVITY = 4;
	private static final int FEELING_ACTIVITY = 5;

	private MyApp myApp;
	// private DBHelper dbHelper;

	private CaldroidSampleCustomFragment caldroidFragment;
	private CaldroidFragment dialogCaldroidFragment;
	// private Resources resources;
	private Uri mImageCaptureUri;
	private File file_temp = null;

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		myApp = (MyApp) getApplication();
		AppRater.app_launched(this);
		// dbHelper = new DBHelper(getApplicationContext());
		// resources = this.getResources();
		final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
		caldroidFragment = new CaldroidSampleCustomFragment();
		// caldroidFragment = new CaldroidFragment();

		// Setup arguments
		// If Activity is created after rotation
		if (savedInstanceState != null) {
			caldroidFragment.restoreStatesFromKey(savedInstanceState,
					"CALDROID_SAVED_STATE");
		}
		// If activity is created from fresh
		else {
			Bundle args = new Bundle();
			Calendar cal = Calendar.getInstance();
			args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
			args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
			args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
			args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
			args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
					CaldroidFragment.SUNDAY); // Sunday
			caldroidFragment.setArguments(args);
		}
		// setCustomResourceForDates();
		// Attach to the activity
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.calendar, caldroidFragment);
		t.commit();

		// Setup listener
		final CaldroidListener listener = new CaldroidListener() {

			@Override
			public void onSelectDate(Date date, View view) {
				Toast.makeText(getApplicationContext(), formatter.format(date),
						Toast.LENGTH_SHORT).show();
				// Start timeline activity
				myApp.cur_date = date;
				Intent intent = new Intent(MainActivity.this,
						TimelineActivity.class);
				startActivity(intent);
			}

			@Override
			public void onChangeMonth(int month, int year) {
				// String text = "month: " + month + " year: " + year;
				// Toast.makeText(getApplicationContext(), text,
				// Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onLongClickDate(Date date, View view) {
				Toast.makeText(getApplicationContext(),
						"Long click " + formatter.format(date),
						Toast.LENGTH_SHORT).show();
				myApp.cur_date = date;
				add_photo();
			}

			@Override
			public void onCaldroidViewCreated() {
				if (caldroidFragment.getLeftArrowButton() != null) {
					// Toast.makeText(getApplicationContext(),
					// "Caldroid view is created", Toast.LENGTH_SHORT)
					// .show();
				}
			}

		};
		// Setup Caldroid
		caldroidFragment.setCaldroidListener(listener);
		CellListener cell_listener = new CellListener() {
			@Override
			public void onSelectDate(Date date) {
				// Toast.makeText(getApplicationContext(),
				// formatter.format(date),
				// Toast.LENGTH_SHORT).show();
				// Start timeline activity
				myApp.cur_date = date;
				Intent intent = new Intent(MainActivity.this,
						TimelineActivity.class);
				startActivity(intent);
			}

			@Override
			public void onLongSelectDate(Date date) {
				// Toast.makeText(getApplicationContext(),
				// "Long click " + formatter.format(date),
				// Toast.LENGTH_SHORT).show();
				myApp.cur_date = date;
				add_photo();
			}
		};
		caldroidFragment.setCellListener(cell_listener);
	}

	/**
	 * Save current states of the Caldroid here
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (caldroidFragment != null) {
			caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
		}

		if (dialogCaldroidFragment != null) {
			dialogCaldroidFragment.saveStatesToKey(outState,
					"DIALOG_CALDROID_SAVED_STATE");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (caldroidFragment == null)
			return true;
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		switch (item.getItemId()) {
		case R.id.action_add:
			myApp.cur_date = today;
			add_photo();
			return true;
		case R.id.action_today:
			caldroidFragment.moveToDate(today);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void add_photo() {
		final String[] items = new String[] { "Select from Camera",
				"Select from gallery", "Sticker" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.select_dialog_item, items);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Add photo");
		builder.setIcon(R.drawable.ic_launcher);
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) { // pick from
																	// camera
				switch (item) {
				case 0:
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					file_temp = new File(Environment
							.getExternalStorageDirectory(), "tmp_dailyphoto"
							+ String.valueOf(System.currentTimeMillis())
							+ ".jpg");
					mImageCaptureUri = Uri.fromFile(file_temp);
					intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
							mImageCaptureUri);
					try {
						intent.putExtra("return-data", true);
						startActivityForResult(intent, PICK_FROM_CAMERA);
					} catch (ActivityNotFoundException e) {
						e.printStackTrace();
					}
					break;
				case 1:
					Intent intent2 = new Intent();
					intent2.setType("image/*");
					intent2.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(Intent.createChooser(intent2,
							"Complete action using"), PICK_FROM_FILE);
					break;
				case 2:
					// Add feeling
					Intent intent3 = new Intent(MainActivity.this,
							FeelingActivity.class);
					startActivityForResult(intent3, FEELING_ACTIVITY);
					break;
				default:
					break;
				}
			}
		});
		final AlertDialog dialog = builder.create();
		dialog.show();

	}
	private void show_setting_dialog() {
		final String[] items = new String[] { "Create collage",
				"Setting","PRO version without Ad" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.select_dialog_item, items);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) { 
				switch (item) {
				case 0:
					break;
				case 1:
					Intent intent = new Intent(MainActivity.this, SettingActivity.class);
					startActivity(intent);
					break;
				case 2:
					startActivity(new Intent(Intent.ACTION_VIEW, Uri
							.parse("market://details?id=" + "hetpin.dailyphoto")));
					break;
				default:
					break;
				}
			}
		});
		final AlertDialog dialog = builder.create();
		dialog.show();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			Log.e("exit", "resultCode != RESULT_OK ");
			return;
		}
		Log.e("onActivityResult", "0");
		Intent intent = new Intent(MainActivity.this, PlusActivity.class);
		Log.e("onActivityResult", "1");
		switch (requestCode) {
		case PICK_FROM_CAMERA:
			if (mImageCaptureUri == null) {
				return;
			}
			try {
				Bitmap bitmap = Utility.getThumbnail(mImageCaptureUri,
						getApplicationContext());
				Log.e("PICK_FROM_CAMERA", "photo" + "w = " + bitmap.getWidth()
						+ " h = " + bitmap.getHeight());
				myApp.cropped_bitmap = bitmap;
				if (file_temp != null) {
					file_temp.delete();
				}
				startActivityForResult(intent, PLUS_ACTIVITY);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
		case PICK_FROM_FILE:
			Uri selectedImage = data.getData();
			try {
				myApp.cropped_bitmap = decodeUri(selectedImage);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			startActivityForResult(intent, PLUS_ACTIVITY);
			break;
		case PLUS_ACTIVITY:
			// Refresh view
			Log.e("onActivityResult", "f5 view");
			caldroidFragment.refreshView();
			break;
		case FEELING_ACTIVITY:
			// Refresh view
			Log.e("onActivityResult", "f5 view");
			caldroidFragment.refreshView();
			break;
		}
	}

	private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(
				getContentResolver().openInputStream(selectedImage), null, o);
		final int REQUIRED_SIZE = DSetting.size_image_default;
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		Log.e("ori image size", "w=" + width_tmp + " h=" + height_tmp);
		int scale = 1;
		if (width_tmp < REQUIRED_SIZE || height_tmp < REQUIRED_SIZE) {
			// w|h < default
			// Up scale
			while (true) {
				if (width_tmp > REQUIRED_SIZE && height_tmp > REQUIRED_SIZE) {
					break;
				}
				width_tmp = width_tmp * 2;
				height_tmp = height_tmp * 2;
				scale = scale / 2;
			}
		} else {
			if (width_tmp / 2 > REQUIRED_SIZE && height_tmp / 2 > REQUIRED_SIZE) {
				// Both w and h >2*default
				// Down scale
				while (true) {
					if (width_tmp / 2 < REQUIRED_SIZE
							|| height_tmp / 2 < REQUIRED_SIZE) {
						break;
					}
					width_tmp /= 2;
					height_tmp /= 2;
					scale *= 2;
				}
			}
		}

		Log.e("Scaled image size", "w=" + width_tmp + " h=" + height_tmp);
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		return BitmapFactory.decodeStream(
				getContentResolver().openInputStream(selectedImage), null, o2);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_setting:
			//Show setting dialog
			show_setting_dialog();
			break;
		default:
			break;
		}
	}

}
