package data;

import hetpin.dailyphoto.DSetting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import model.Photo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	private Context context;

	// SimpleDateFormat formatter_db = new
	// SimpleDateFormat(DSetting.date_format_db);
	// SimpleDateFormat formatter_display = new
	// SimpleDateFormat(DSetting.date_format_db);
	public DBHelper(Context context) {
		super(context, DB.DATABASE_NAME, null, DB.DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DB.CREATE_TABLE_DAILY_PHOTO);
		db.execSQL(DB.CREATE_TABLE_COVER);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Drop all older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + DB.TABLE_DAILY_PHOTO);
		db.execSQL("DROP TABLE IF EXISTS " + DB.TABLE_COVER);
		// Create tables again
		onCreate(db);
	}

	public ArrayList<Photo> getALlPhoto() {
		SQLiteDatabase db = this.getReadableDatabase();
		String cols = "*";
		String query = "SELECT " + cols + " FROM " + DB.TABLE_DAILY_PHOTO;
		Cursor cursor = db.rawQuery(query, null);
		Log.e("count of images = ", "" + cursor.getCount());
		ArrayList<Photo> list = new ArrayList<Photo>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Photo item = new Photo(cursor);
			list.add(item);
			cursor.moveToNext();
		}
		db.close();
		return list;
	}
	public HashMap<Integer, String> getHashByMonthYear(int month, int year) {
		SQLiteDatabase db = this.getReadableDatabase();
		String date_str = String.format("%02d-%04d", month, year);
		// Query dailyphoto table
		String cols = "*";
		String query = "SELECT " + cols + " FROM " + DB.TABLE_DAILY_PHOTO
				+ " WHERE " + DB.KEY_PHOTO_DATE + " LIKE '%" + date_str
				+ "' ORDER BY " + DB.KEY_PHOTO_ID + " ASC";
		Cursor cursor = db.rawQuery(query, null);
		Log.e("query", query);
		Log.e("count of photo on " + date_str, "" + cursor.getCount());
		HashMap<Integer, String> hash = new HashMap<Integer, String>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Photo item = new Photo(cursor);
			hash.put(item.dayOfMonth, item.getImageLoaderPath());
			cursor.moveToNext();
		}
		return hash;

	}

	public ArrayList<Photo> getPhotoByDate(String date_str) {
		SQLiteDatabase db = this.getReadableDatabase();
		// Query dailyphoto table
		String cols = "*";
		String query = "SELECT " + cols + " FROM " + DB.TABLE_DAILY_PHOTO
				+ " WHERE " + DB.KEY_PHOTO_DATE + " = '" + date_str
				+ "' ORDER BY " + DB.KEY_PHOTO_ID + " ASC";
		Cursor cursor = db.rawQuery(query, null);
		Log.e("query", query);
		Log.e("count of photo on " + date_str, "" + cursor.getCount());
		ArrayList<Photo> list = new ArrayList<Photo>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Photo item = new Photo(cursor);
			list.add(item);
			cursor.moveToNext();
		}
		int size = list.size();
		if (size == 0) {
			return list;
		}// Ensure list have at least one image
			// Query cover table to find cover photo id if exist
		query = "SELECT " + cols + " FROM " + DB.TABLE_COVER + " WHERE "
				+ DB.KEY_PHOTO_DATE + " = '" + date_str + "'";
		Cursor c = db.rawQuery(query, null);
		if (c.getCount() > 0) {
			// Exist one cover photo, find this photo in list
			c.moveToFirst();
			int cover_photo_id = c.getInt(c
					.getColumnIndex(DB.KEY_COVER_PHOTO_ID));
			Log.e("cover", "photo_id = " + cover_photo_id);
			for (int i = 0; i < size; i++) {
				if (list.get(i).id == cover_photo_id) {
					list.get(i).is_cover = true;
					break;
				}
			}
		} else {
			// No cover until now, set the last image as cover and insert to
			// cover table
			Photo photo = list.get(size - 1);
			photo.is_cover = true;
			insertCover(photo.date, photo.id);
			Log.e("cover", "no cover, just insert " + photo.id + " as cover");
		}
		db.close();
		return list;
	}

	public ArrayList<String> getDateContainPhotos() {
		SQLiteDatabase db = this.getReadableDatabase();
		// Query dailyphoto table
		String cols = " DISTINCT " + DB.KEY_PHOTO_DATE;
		String query = "SELECT " + cols + " FROM " + DB.TABLE_DAILY_PHOTO
				+ " ORDER  by datetime(" + DB.KEY_PHOTO_DATE + ") ASC";
		Cursor cursor = db.rawQuery(query, null);
		Log.e("query", query);
		Log.e("count of date with photos ", "" + cursor.getCount());
		ArrayList<String> list = new ArrayList<String>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String item = cursor.getString(cursor
					.getColumnIndex(DB.KEY_PHOTO_DATE));
			list.add(item);
			Log.e("added date", item);
			cursor.moveToNext();
		}
		db.close();
		return list;
	}

	// public boolean isImageExist(String des, String author) {
	// SQLiteDatabase db = this.getReadableDatabase();
	// String cols = "*";
	// String query = "" + "SELECT " + cols + " FROM "
	// + DB.TABLE_DAILY_PHOTO + " WHERE " + DB.KEY_PHOTO_TITLE
	// + " = '" + des + "' AND " + DB.KEY_PHOTO_LOCATION + " = '" + author +"'";
	// Cursor cursor = db.rawQuery(query, null);
	// Log.e("count of images exist= ", "" + cursor.getCount());
	// if (cursor.getCount() > 0) {
	// return true;
	// } else {
	// return false;
	// }
	// }

	// Insert an image
	public void insertPhoto(String path, String title, String location,
			String milis, String date) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DB.KEY_PHOTO_PATH, path);
		values.put(DB.KEY_PHOTO_TITLE, title);
		values.put(DB.KEY_PHOTO_LOCATION, location);
		values.put(DB.KEY_PHOTO_TIME_MILIS, milis);
		values.put(DB.KEY_PHOTO_DATE, date);
		// Inserting Row
		long result_id = db.insert(DB.TABLE_DAILY_PHOTO, null, values);
		// Update Cover table
		if (result_id != -1) {
			//insert photo success
			if (this.getCoverIdByDate(date) >= 0) {
				// Update current cover by new photo
				this.updateCoverForDate(date, (int)result_id);
			} else {
				// Insert photo as cover for the date
				this.insertCover(date, (int) result_id);
			}
		}
		// Closing database connection
		db.close();
		Log.e("inserted " + result_id, path + " " + date);
	}

	// Insert an image
	public void insertCover(String date, int photo_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DB.KEY_PHOTO_DATE, date);
		values.put(DB.KEY_COVER_PHOTO_ID, photo_id);
		// Inserting Row
		long result = db.insert(DB.TABLE_COVER, null, values);
		// Closing database connection
		db.close();
		Log.e("inser cover " + result, "photo_id = " + photo_id + " for "
				+ date);
	}
	// Insert an image
	public void updateCoverForDate(String date, int new_photo_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DB.KEY_PHOTO_DATE, date);
		values.put(DB.KEY_COVER_PHOTO_ID, new_photo_id);
		// Inserting Row
		long result = db.update(DB.TABLE_COVER, values, DB.KEY_PHOTO_DATE +" = '" + date + "'", null);
		// Closing database connection
		db.close();
		Log.e("update cover " + result, "new_photo_id = " + new_photo_id + " for "
				+ date);
	}

	public int getCoverIdByDate(String date_str) {
		int result = -1;
		SQLiteDatabase db = this.getReadableDatabase();
		String cols = "*";
		String query = "SELECT " + cols + " FROM " + DB.TABLE_COVER + " WHERE "
				+ DB.KEY_PHOTO_DATE + " = '" + date_str + "'";
		Cursor c = db.rawQuery(query, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			result = c.getInt(c.getColumnIndex(DB.KEY_COVER_PHOTO_ID));
		}
		db.close();
		return result;
	}

}
