package data;

public class DB {

	// DB NAME AND VERSION
	public static final String DATABASE_NAME = "dailyphoto";
	public static final int DATABASE_VERSION = 1;

	//TABLE table_daily_photo
	public static final String TABLE_DAILY_PHOTO = "table_daily_photo";
	public static final String KEY_PHOTO_ID = "photo_id";
	public static final String KEY_PHOTO_PATH = "photo_path";
	public static final String KEY_PHOTO_TITLE = "photo_title";
	public static final String KEY_PHOTO_LOCATION = "photo_location";
	public static final String KEY_PHOTO_TIME_MILIS = "time_mili_second";
	public static final String KEY_PHOTO_DATE = "photo_date";// equal cover photo date key
	//TABLE cover
	public static final String TABLE_COVER = "table_cover";
	public static final String KEY_COVER_ID = "cover_id";
	public static final String KEY_COVER_PHOTO_ID = "cover_photo_id";
	
	public static final String CREATE_TABLE_DAILY_PHOTO = "CREATE TABLE " 
	+ TABLE_DAILY_PHOTO+
	"("
	+ KEY_PHOTO_ID + " INTEGER PRIMARY KEY,"//
	+ KEY_PHOTO_TITLE + " TEXT, "//
	+ KEY_PHOTO_LOCATION + " TEXT, "//
	+ KEY_PHOTO_TIME_MILIS + " TEXT, "//
	+ KEY_PHOTO_DATE + " TEXT, "//
	+ KEY_PHOTO_PATH+ " TEXT"//
	+")";
	public static final String CREATE_TABLE_COVER = "CREATE TABLE " 
	+ TABLE_COVER+
	"("
	+ KEY_COVER_ID + " INTEGER PRIMARY KEY,"//
	+ KEY_PHOTO_DATE + " TEXT, "//
	+ KEY_COVER_PHOTO_ID+ " INTEGER"//
	+")";

}
