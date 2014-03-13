package adapter;

import hetpin.dailyphoto.DSetting;
import hetpin.dailyphoto.FeelingActivity;
import hetpin.dailyphoto.MyApp;
import hetpin.dailyphoto.R;

import java.util.ArrayList;

import model.NormalSquareImageView;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import data.DBHelper;

public class FeelingAdapter extends BaseAdapter {
	private MyApp myApp;
	private DBHelper dbHelper;
	private ImageLoader imageLoader;
	private ArrayList<String> list;
	private FeelingActivity mContext;
	private DisplayImageOptions options;

	// private ImageLoadingListener animateFirstListener = new
	// AnimateFirstDisplayListener();

	public FeelingAdapter(Context mContext, ImageLoader imageLoader,
			ArrayList<String> list, MyApp myApp, DBHelper dbHelper) {
		this.mContext = (FeelingActivity) mContext;
		this.list = list;
		this.imageLoader = imageLoader;
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).build();
		this.myApp = myApp;
		this.dbHelper = dbHelper;
	}

	@Override
	public int getCount() {
		// Because of list contain photos + one cover
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public class ViewHolder {
		public NormalSquareImageView iv_image;
		public TextView text;
		public ImageButton btn_overlay;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			// Utilities.log("convert null " + position);
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_feeling, null);
			holder.iv_image = (NormalSquareImageView) convertView
					.findViewById(R.id.iv_image);
			holder.text = (TextView) convertView.findViewById(R.id.tv_title);
			holder.btn_overlay = (ImageButton) convertView
					.findViewById(R.id.btn_overlay);
			convertView.setTag(holder);
		} else {
			// Utilities.log("convert not null " + position);
			holder = (ViewHolder) convertView.getTag();
		}
		// holder.text.setText(list.get(position).title);
		holder.btn_overlay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// myApp.slider_list_photos = list;
				// myApp.slider_cur_position = position;
				// // Start Slider activity
				// Intent intent = new Intent(mContext, SliderActivity.class);
				// mContext.startActivity(intent);
				// TODO Save image
				dbHelper.insertPhoto(
						DSetting.prefix_asset + list.get(position),
						DSetting.KEY_MEEP, "", System.currentTimeMillis() + "",
						myApp.getCurDateFormatDb());
				Log.e("add meep", myApp.getCurDateFormatDb());
				Intent intent = new Intent();
				mContext.setResult(mContext.RESULT_OK, intent);
				mContext.finish();
			}
		});
		imageLoader.displayImage(DSetting.prefix_asset + list.get(position),
				holder.iv_image, options);
		return convertView;
	}

}
