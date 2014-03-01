package hetpin.dailyphoto;

import adapter.CaldroidCustomAdapter;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import data.DBHelper;

public class CaldroidSampleCustomFragment extends CaldroidFragment {

	@Override
	public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
		// TODO Auto-generated method stub
		MyApp myApp = (MyApp) getActivity().getApplication();
		DBHelper dbHelper = new DBHelper(getActivity());
		return new CaldroidCustomAdapter(getActivity(), month, year,
				getCaldroidData(), extraData, dbHelper, myApp.getImageLoader());
	}

}
