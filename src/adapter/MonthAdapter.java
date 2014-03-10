package adapter;

import hetpin.dailyphoto.R;

import java.util.ArrayList;

import model.AnimateFirstDisplayListener;
import model.NormalSquareImageView;
import model.Photo;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class MonthAdapter extends BaseAdapter {
	private ImageLoader imageLoader;
	private ArrayList<Photo> list;
	private Context mContext;
	private DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	public MonthAdapter(Context mContext,
			ImageLoader imageLoader, ArrayList<Photo> list) {
		this.mContext = mContext;
		this.list = list;
		this.imageLoader = imageLoader;
		options = new DisplayImageOptions.Builder()
        .cacheInMemory(false)
        .cacheOnDisc(false)
        .build();
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
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			// Utilities.log("convert null " + position);
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_grid, null);
			holder.iv_image = (NormalSquareImageView) convertView
					.findViewById(R.id.iv_image);
			holder.text = (TextView) convertView.findViewById(R.id.tv_title);
			convertView.setTag(holder);
		} else {
			// Utilities.log("convert not null " + position);
			holder = (ViewHolder) convertView.getTag();
		}
		holder.text
				.setText(list.get(position).title);
		imageLoader.displayImage(list.get(position).getImageLoaderPath(), holder.iv_image, options);
		return convertView;
	}

}
