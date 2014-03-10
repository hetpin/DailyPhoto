package adapter;

import hetpin.dailyphoto.R;
import hirondelle.date4j.DateTime;

import java.util.Date;
import java.util.HashMap;

import model.CellListener;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.roomorama.caldroid.CaldroidGridAdapter;
import com.roomorama.caldroid.CalendarHelper;

import data.DBHelper;

public class CustomCellAdapter extends CaldroidGridAdapter {
	private DBHelper dbHelper;
	private ImageLoader imageLoader;
	// SimpleDateFormat formatter_db = new
	// SimpleDateFormat(DSetting.date_format_db);
	// SimpleDateFormat formatter_display = new
	// SimpleDateFormat(DSetting.date_format_display);
	private HashMap<Integer, String> hash;
	private CellListener cellListener;
	private DisplayImageOptions options;
	public void setCellListener(CellListener cellListener){
		this.cellListener = cellListener;
	}
	public CustomCellAdapter(Context context, int month, int year,
			HashMap<String, Object> caldroidData,
			HashMap<String, Object> extraData, DBHelper dbHelper,
			ImageLoader imageloader) {
		super(context, month, year, caldroidData, extraData);
		this.dbHelper = dbHelper;
		this.imageLoader = imageloader;
		hash = dbHelper.getHashByMonthYear(month, year);
		options = new DisplayImageOptions.Builder()
        .cacheInMemory(false)
        .cacheOnDisc(false)
        .build();
	}

	@Override
	public void notifyDataSetChanged() {
		Log.e("notifyDataSetChanged", "called");
		hash = dbHelper.getHashByMonthYear(month, year);
		super.notifyDataSetChanged();
	}
	static class ViewHolder{
		ImageButton btn_round;
		ImageView iv_thumb;
		TextView tv_date;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Log.e("adapter", "getview at" + position);
		ViewHolder viewHolder;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View cellView = convertView;

		// For reuse
		if (convertView == null) {
			cellView = inflater.inflate(R.layout.custom_round_cell, null);
			viewHolder = new ViewHolder();
			viewHolder.btn_round = (ImageButton) cellView
					.findViewById(R.id.iv_round);
			viewHolder.iv_thumb = (ImageView) cellView
					.findViewById(R.id.iv_thumb);
			viewHolder.tv_date = (TextView) cellView.findViewById(R.id.tv_date);
			cellView.setTag(viewHolder);
		} else{
			viewHolder = (ViewHolder) cellView.getTag();			
		}
		// Get dateTime of this cell
		final DateTime dateTime = this.datetimeList.get(position);
		Resources resources = context.getResources();

		if (hash.get(dateTime.getDay()) == null || dateTime.getMonth() != month) {
			// empty cell or day of other month
			viewHolder.btn_round
					.setBackgroundResource(R.drawable.selector_round_cell_transparent_blue);
			viewHolder.iv_thumb.setImageDrawable(null);
			viewHolder.tv_date.setTextColor(resources
					.getColor(R.color.caldroid_darker_gray));
		} else {
			// add last image to cell
			Log.e("load cell", "" + hash.get(dateTime.getDay()));
			viewHolder.btn_round
					.setBackgroundResource(R.drawable.selector_round_cell_opacity_light_dark);
			imageLoader.displayImage(hash.get(dateTime.getDay()), viewHolder.iv_thumb, options);
			viewHolder.tv_date.setTextColor(resources.getColor(R.color.caldroid_white));
		}

		// Set color of the dates in previous / next month
		if (dateTime.getMonth() != month) {
			// btn_round.setTextColor(resources
			// .getColor(com.caldroid.R.color.caldroid_lighter_gray));
			viewHolder.iv_thumb.setImageDrawable(null);
			viewHolder.tv_date.setTextColor(resources
					.getColor(R.color.cell_gray));
		}

		// Customize for selected dates
		if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
			cellView.setBackgroundColor(resources
					.getColor(com.caldroid.R.color.caldroid_sky_blue));
		}

		// Customize for today
		if (dateTime.equals(getToday())) {
			if (hash.get(dateTime.getDay()) != null) {
				viewHolder.tv_date.setTextColor(resources.getColor(R.color.cell_red));
				viewHolder.btn_round
						.setBackgroundResource(R.drawable.selector_round_cell_opacity_light_dark);
			} else {
				viewHolder.btn_round
						.setBackgroundResource(R.drawable.selector_round_cell_red_blue);
				viewHolder.tv_date.setTextColor(resources.getColor(R.color.caldroid_white));
			}
		} else {
			// cellView.setBackgroundResource(com.caldroid.R.drawable.cell_bg);
		}
		viewHolder.tv_date.setText("" + dateTime.getDay());
		viewHolder.btn_round.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Date date = CalendarHelper
						.convertDateTimeToDate(dateTime);
				cellListener.onSelectDate(date);
			}
		});
		viewHolder.btn_round.setOnLongClickListener(new OnLongClickListener() { 
	        @Override
	        public boolean onLongClick(View v) {
				Date date = CalendarHelper
						.convertDateTimeToDate(dateTime);
				cellListener.onLongSelectDate(date);
	        	return true;
	        }
	    });
		return cellView;
	}

}
