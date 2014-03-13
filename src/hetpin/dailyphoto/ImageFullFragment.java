package hetpin.dailyphoto;

import model.Photo;
import model.TouchImageView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class ImageFullFragment extends Fragment {
	private MyApp myApp;
	private TouchImageView iv_touch;
	private TextView tv_des;
	private TextView tv_date;
	private TextView tv_location;
	private RelativeLayout rl_bottom_bar;
	public static final String ARG_OBJECT = "object";
	public Button btn_share;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_image_full,
				container, false);
		myApp = (MyApp) getActivity().getApplication();
		Bundle args = getArguments();
		int position = args.getInt(MonthFragment.ARG_OBJECT);
		final Photo item = myApp.slider_list_photos.get(position);
		if (item == null) {
			getActivity().finish();
		}
		iv_touch = (TouchImageView) rootView.findViewById(R.id.iv_touch);
		tv_des = (TextView) rootView.findViewById(R.id.tv_des);
		tv_date = (TextView) rootView.findViewById(R.id.tv_date);
		tv_location = (TextView) rootView.findViewById(R.id.tv_location);
		rl_bottom_bar = (RelativeLayout) rootView
				.findViewById(R.id.rl_bottom_bar);
		rl_bottom_bar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// To disable multytouch on imageview
			}
		});
		btn_share = (Button) rootView.findViewById(R.id.btn_share);
		btn_share.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				myApp.getImageLoader().loadImage(item.getImageLoaderPath(),
						new ImageLoadingListener() {
							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {
							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								// save to temp file
								String path = Images.Media.insertImage(
										getActivity().getContentResolver(),
										loadedImage,
										item.time_mili_second + "", null);
								Uri screenshotUri = Uri.parse(path);
								// share to facebook
								Intent sharingIntent = new Intent(
										Intent.ACTION_SEND);
								sharingIntent
										.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								sharingIntent.putExtra(Intent.EXTRA_STREAM,
										screenshotUri);
								sharingIntent.putExtra(Intent.EXTRA_TEXT,
										item.title);
								sharingIntent.setType("image/png");
								getActivity().startActivity(
										Intent.createChooser(sharingIntent,
												"Share the image with"));
							}

							@Override
							public void onLoadingCancelled(String imageUri,
									View view) {
							}
						});
			}
		});
		myApp.getImageLoader()
				.displayImage(item.getImageLoaderPath(), iv_touch);
		tv_des.setText(item.title);
		tv_date.setText(item.getDisplayDate());
		tv_location.setText(item.location);

		return rootView;
	}
}
