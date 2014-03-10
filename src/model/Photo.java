package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hetpin.dailyphoto.DSetting;
import android.database.Cursor;
import android.util.Log;
import data.DB;

public class Photo {
	public int id;
	public String path;
	public String title;
	public String location;
	public String time_mili_second;// Unique for insertion
	public String date;
	SimpleDateFormat formatter_db = new SimpleDateFormat(
			DSetting.date_format_db);
	private SimpleDateFormat dfDateDisplay = new SimpleDateFormat(
			DSetting.date_format_display);
	// Marked as cover photo
	public boolean is_cover = false;
	// Use to add to hash map
	public int dayOfMonth;

	public Photo(Cursor c) {
		try {
			this.id = c.getInt(c.getColumnIndex(DB.KEY_PHOTO_ID));
			this.path = c.getString(c.getColumnIndex(DB.KEY_PHOTO_PATH));
			this.title = c.getString(c.getColumnIndex(DB.KEY_PHOTO_TITLE));
			this.location = c
					.getString(c.getColumnIndex(DB.KEY_PHOTO_LOCATION));
			this.time_mili_second = c.getString(c
					.getColumnIndex(DB.KEY_PHOTO_TIME_MILIS));
			this.date = c.getString(c.getColumnIndex(DB.KEY_PHOTO_DATE));
			this.dayOfMonth = Integer.parseInt(this.date.substring(0, 2));
			Log.e("added", this.toString());
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	public String getImageLoaderPath() {
		return DSetting.file_path_prefix + this.path;
	}

	public String toString() {
		return this.id + " " + this.date + " " + this.path + " "
				+ this.time_mili_second;
	}

	public String getDisplayDate() {
		try {
			
			Date date = formatter_db.parse(this.date);
			return dfDateDisplay.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return this.date;
	}
}
