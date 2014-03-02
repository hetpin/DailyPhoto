package adapter;

import fr.castorflex.android.flipimageview.library.FlipImageView;
import fr.castorflex.android.flipimageview.library.FlipImageView.OnFlipListener;
import hetpin.dailyphoto.DSetting;
import hetpin.dailyphoto.R;
import hirondelle.date4j.DateTime;

import java.util.HashMap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;
import com.roomorama.caldroid.SquareTextView;

import data.DBHelper;

public class CaldroidCustomAdapter extends CaldroidGridAdapter {
	private DBHelper dbHelper;
	private ImageLoader imageLoader;
//	SimpleDateFormat formatter_db = new SimpleDateFormat(DSetting.date_format_db);
//	SimpleDateFormat formatter_display = new SimpleDateFormat(DSetting.date_format_display);
	private HashMap<Integer, String> hash;
	private DecelerateInterpolator interpolator = new DecelerateInterpolator();
	public CaldroidCustomAdapter(Context context, int month, int year,
			HashMap<String, Object> caldroidData,
			HashMap<String, Object> extraData,
			DBHelper dbHelper,
			ImageLoader imageloader) {
		super(context, month, year, caldroidData, extraData);
		this.dbHelper = dbHelper;
		this.imageLoader = imageloader;
		hash = dbHelper.getHashByMonthYear(month, year);
	}
	@Override
	public void notifyDataSetChanged() {
		Log.e("notifyDataSetChanged", "called");
		hash = dbHelper.getHashByMonthYear(month, year);
		super.notifyDataSetChanged();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Log.e("adapter", "getview at" + position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View cellView = convertView;

		// For reuse
		if (convertView == null) {
			cellView = inflater.inflate(R.layout.custom_cell, null);
		}
		final ImageView iv_thumbnail = (ImageView) cellView
				.findViewById(R.id.iv_thumbnail);
		SquareTextView tv_date = (SquareTextView) cellView.findViewById(R.id.tv_date);
		// Get dateTime of this cell
		DateTime dateTime = this.datetimeList.get(position);
		Resources resources = context.getResources();
		
		if (hash.get(dateTime.getDay()) == null || dateTime.getMonth() != month) {
			//empty cell
			iv_thumbnail.setImageDrawable(null);
			tv_date.setBackgroundColor(Color.TRANSPARENT);
		} else{
			//add last image to cell
			Log.e("load cell", "" + hash.get(dateTime.getDay()));
			imageLoader.displayImage(hash.get(dateTime.getDay()), iv_thumbnail);
//			tv_date.setBackgroundColor(resources
//					.getColor(R.color.text_bg_opacity));
			tv_date.setBackgroundResource(R.drawable.gradient_blur_bg);
		}
//		iv_thumbnail.setOnFlipListener(new OnFlipListener() {
//			
//			@Override
//			public void onFlipStart(FlipImageView view) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onFlipEnd(FlipImageView view) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onClick(FlipImageView view) {
//				iv_thumbnail.setInterpolator(interpolator);
//				iv_thumbnail.setDuration(DSetting.duration);
//				iv_thumbnail.setRotationXEnabled(true);
//				iv_thumbnail.setRotationYEnabled(false);
//				// iv_thumbnail.setRotationZEnabled(mCheckBoxZ.isChecked());
//				iv_thumbnail.setRotationReversed(true);				
//			}
//		});
		//tv_date.setTextColor(Color.BLACK);

		// Set color of the dates in previous / next month
		if (dateTime.getMonth() != month) {
			tv_date.setTextColor(resources
					.getColor(com.caldroid.R.color.caldroid_lighter_gray));
			cellView.setBackgroundResource(com.caldroid.R.drawable.disable_cell);
			iv_thumbnail.setImageDrawable(null);
			tv_date.setBackgroundColor(Color.TRANSPARENT);
		}
//		// Set color of the dates in previous / next month
//		if (dateTime.getMonth() != month) {
//			tv_date.setTextColor(resources
//					.getColor(com.caldroid.R.color.caldroid_darker_gray));
//			cellView.setBackgroundResource(com.caldroid.R.drawable.disable_cell);			
//		}

		boolean shouldResetDiabledView = false;
		boolean shouldResetSelectedView = false;

		// Customize for disabled dates and date outside min/max dates
		if ((minDateTime != null && dateTime.lt(minDateTime))
				|| (maxDateTime != null && dateTime.gt(maxDateTime))
				|| (disableDates != null && disableDates.indexOf(dateTime) != -1)) {

			tv_date.setTextColor(CaldroidFragment.disabledTextColor);
			if (CaldroidFragment.disabledBackgroundDrawable == -1) {
				cellView.setBackgroundResource(com.caldroid.R.drawable.disable_cell);
			} else {
				cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
			}

			if (dateTime.equals(getToday())) {
				// NO need to highlight today in other month
				// cellView.setBackgroundResource(com.caldroid.R.drawable.red_border_gray_bg);
				// tv_date.setTextColor(Color.RED);
			}

		} else {
			shouldResetDiabledView = true;
		}

		// Customize for selected dates
		if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
			if (CaldroidFragment.selectedBackgroundDrawable != -1) {
				cellView.setBackgroundResource(CaldroidFragment.selectedBackgroundDrawable);
			} else {
				cellView.setBackgroundColor(resources
						.getColor(com.caldroid.R.color.caldroid_sky_blue));
			}

			tv_date.setTextColor(CaldroidFragment.selectedTextColor);

		} else {
			shouldResetSelectedView = true;
		}

		if (shouldResetDiabledView && shouldResetSelectedView) {
			// Customize for today
			if (dateTime.equals(getToday())) {
				//cellView.setBackgroundResource(com.caldroid.R.drawable.red_border);
				tv_date.setTextColor(Color.RED);
				if (hash.get(dateTime.getDay()) != null) {
					tv_date.setBackgroundResource(R.drawable.red_border_with_gradient);					
				} else{
					tv_date.setBackgroundResource(R.drawable.red_border);
				}
				
			} else {
				cellView.setBackgroundResource(com.caldroid.R.drawable.cell_bg);
			}
		}
		tv_date.setText("" + dateTime.getDay());
		return cellView;
	}

}
