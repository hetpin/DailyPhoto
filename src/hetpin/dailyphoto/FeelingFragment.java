package hetpin.dailyphoto;

import java.util.ArrayList;

import data.DBHelper;

import model.Photo;

import adapter.FeelingAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class FeelingFragment extends Fragment {
	private MyApp myApp;
	private DBHelper dbHelper;
	public static final String ARG_OBJECT = "object";
	private GridView grid;
	private FeelingAdapter adapter;
	private ArrayList<String> list;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_feeling, container,
				false);
		myApp = (MyApp) getActivity().getApplication();
		dbHelper = new DBHelper(getActivity());
		Bundle args = getArguments();
		int position = args.getInt(MonthFragment.ARG_OBJECT);
		String label = args.getString(DSetting.date_obj);
		grid = (GridView) rootView.findViewById(R.id.grid_feeling);
		// get list photo
		list = Utility.listFiles(getActivity(), label);

		adapter = new FeelingAdapter(getActivity(), myApp.getImageLoader(),
				list, myApp, dbHelper);
		grid.setAdapter(adapter);
		return rootView;
	}
}
