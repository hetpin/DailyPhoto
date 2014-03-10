package hetpin.dailyphoto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import data.DBHelper;
import model.GridViewHeader;
import model.Photo;
import adapter.MonthAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MonthFragment extends Fragment {
	private DBHelper dbHelper;
	private MyApp myApp;
	private ArrayList<Photo> list_photos;
	private GridViewHeader gridview;
	private MonthAdapter adapter;
	private ImageView iv_header;
	private TextView tv_title;
	public static final String ARG_OBJECT = "object";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_month, container,
				false);
		myApp = (MyApp) getActivity().getApplication();
		Bundle args = getArguments();
		dbHelper = new DBHelper(getActivity());
		String date_str = args.getString(DSetting.date_obj);
//		Calendar cal = Calendar.getInstance();
//		date_str = dfDateDB.format(cal.getTime());
		list_photos = dbHelper.getPhotoByDate(date_str);
		((TextView) rootView.findViewById(android.R.id.text1)).setText(Integer
				.toString(args.getInt(ARG_OBJECT)));
		gridview = (GridViewHeader) rootView.findViewById(R.id.gridview);
		View header = View.inflate(getActivity(), R.layout.item_header, null);
		tv_title = (TextView) header.findViewById(R.id.tv_title);
		iv_header = (ImageView) header.findViewById(R.id.iv_header);
		gridview.addHeaderView(header);
		int size = list_photos.size();
		if (size > 0) {
			//find and set cover photo
			for (int i = size -1; i >=0; i--) {
				if (list_photos.get(i).is_cover) {
					myApp.getImageLoader().displayImage(list_photos.get(i).getImageLoaderPath(), iv_header);
					tv_title.setText(list_photos.get(i).getDisplayDate());
					if (size %2 == 1) {
						//Odd size
						list_photos.remove(i);
					}
					break;
				}
			}
		}
		adapter = new MonthAdapter(getActivity(), myApp.getImageLoader(), list_photos);
		gridview.setAdapter(adapter);				
		return rootView;
	}
}
