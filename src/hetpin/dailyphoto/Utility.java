package hetpin.dailyphoto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

public class Utility {
	private static final String DIRECTORY_PICTURES = "Pictures";

	/**
	 * @param fileName
	 *            is the name of the file.
	 * @param bitmap
	 *            is the bitmap to be updated.
	 * @param context
	 *            is the context of the app.
	 * @param recycle_this_bitmap
	 *            Free the bitmap after saved or not
	 * @return the file object.
	 * @throws IOException
	 *             is the exception input-output.
	 */
	public static File storeFile(String fileName, Bitmap bitmap,
			Context context, boolean recycle_this_bitmap) throws IOException {
		File file = null;
		String filePath = getOutputMediaFilePath("image", context, fileName
				+ ".jpg");
		if (filePath == null) {
			// toast error message not enough memory.
			Toast.makeText(context, "Not enough memory", Toast.LENGTH_SHORT)
					.show();
			return null;
		} else {
			file = new File(filePath);
		}
		FileOutputStream out = new FileOutputStream(file);
		//long fileSize = bitmap.getHeight() * bitmap.getRowBytes();
		int quality = 100;
		// if (fileSize > 50000) {
		// quality = (int) (100 * 50000 / fileSize);
		// }
		bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
		out.flush();
		out.close();
		if (recycle_this_bitmap) {
			bitmap.recycle();
		}
		return file;
	}

	/** Create a file Uri for saving an image or video */
	/**
	 * @param fileType
	 *            type of media.
	 * @return the uri for the image.
	 */
	public static String getOutputMediaFilePath(String fileType,
			Context context, String fileName) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.
		File mediaStorageDir = getMediaStorageDirectory(fileType, context);
		File mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ fileName);
		return mediaFile.getAbsolutePath();
	}

	public static File getMediaStorageDirectory(String fileType, Context context) {
		File mediaStorageDir;
		boolean isExternalStorageAvailable = Environment.MEDIA_MOUNTED
				.equals(Environment.getExternalStorageState());

		if (hasFroyo() && isExternalStorageAvailable) {
			String mediaFolder;
			mediaFolder = Environment.DIRECTORY_PICTURES;
			if (fileType.equals("image")) {
				mediaFolder = Environment.DIRECTORY_PICTURES;
			}
			mediaStorageDir = new File(
					Environment.getExternalStoragePublicDirectory(mediaFolder),
					"dailyphoto");

		} else {
			String mediaFolder = DIRECTORY_PICTURES;
			if (fileType.equals("image")) {
				mediaFolder = DIRECTORY_PICTURES;

			}
			if (isExternalStorageAvailable) {
				mediaStorageDir = new File(
						Environment.getExternalStorageDirectory(), mediaFolder);
			} else {
				mediaStorageDir = new File(context.getFilesDir(), mediaFolder);
			}
		}
		// This location works best if you want the created images to be
		// shared
		// between applications and persist after your app has been
		// uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.e("MyCameraApp", "failed to create directory:"
						+ mediaStorageDir.getAbsolutePath());
				return null;
			}
		}
		return mediaStorageDir;
	}

	public static boolean hasFroyo() {
		// Can use static final constants like FROYO, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed
		// behavior.
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public static ArrayList<String> get_images_from_dir(Context context) {
		ArrayList<String> list = new ArrayList<String>();
		// File representing the folder that you select using a FileChooser
		final File dir = getMediaStorageDirectory("image", context);
		Log.e("get_images_from_dir", dir.getAbsolutePath());

		// array of supported extensions (use a List if you prefer)
		final String[] EXTENSIONS = new String[] { "gif", "png", "bmp", "jpg",
				"jpeg" // and other formats you need
		};
		// filter to identify images based on their extensions
		final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

			@Override
			public boolean accept(final File dir, final String name) {
				for (final String ext : EXTENSIONS) {
					if (name.endsWith("." + ext)) {
						return (true);
					}
				}
				return (false);
			}
		};
		if (dir.isDirectory()) { // make sure it's a directory
			for (final File f : dir.listFiles(IMAGE_FILTER)) {
				// you probably want something more involved here
				// to display in your UI
				Log.e("get_images_from_dir",
						f.getName() + " size " + f.length());
			}
		}
		return list;
	}
}
