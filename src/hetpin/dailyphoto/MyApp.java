package hetpin.dailyphoto;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MyApp extends Application {
	public File cropped_file = null;
	public Bitmap cropped_bitmap;
	private ImageLoader imageLoader;
	public Date cur_date;
	SimpleDateFormat formatter_db = new SimpleDateFormat(
			DSetting.date_format_db);
	SimpleDateFormat formatter_display = new SimpleDateFormat(
			DSetting.date_format_display);

	public void onCreate() {
		super.onCreate();
		initImageLoader(getApplicationContext());
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	public String getCurDateFormatDb() {
		if (formatter_db == null) {
			formatter_db = new SimpleDateFormat(
					DSetting.date_format_db);
		}
		return formatter_db.format(cur_date);
	}

	public String getCurDateFormatDisplay() {
		if (formatter_display == null) {
			formatter_display = new SimpleDateFormat(
					DSetting.date_format_display);
		}
		return formatter_display.format(cur_date);
	}

	public void clear() {
		cropped_file = null;
		cropped_bitmap = null;
		cur_date = null;
	}

	public ImageLoader getImageLoader() {
		if (imageLoader == null) {
			imageLoader = ImageLoader.getInstance();
		}
		return imageLoader;

	}
}
