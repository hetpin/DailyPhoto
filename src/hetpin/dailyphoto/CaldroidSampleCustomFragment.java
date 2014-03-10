package hetpin.dailyphoto;

import model.CellListener;
import adapter.CustomCellAdapter;
import android.util.Log;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import data.DBHelper;

public class CaldroidSampleCustomFragment extends CaldroidFragment {
	private CustomCellAdapter adapter;
	private CellListener cellListener;

	@Override
	public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
		MyApp myApp = (MyApp) getActivity().getApplication();
		DBHelper dbHelper = new DBHelper(getActivity());
		adapter = new CustomCellAdapter(getActivity(), month, year,
				getCaldroidData(), extraData, dbHelper, myApp.getImageLoader());
		Log.e("getNewDatesGridAdapter", "create new adapter");
		adapter.setCellListener(cellListener);
		return adapter;
	}

	public void setCellListener(CellListener listener) {
		this.cellListener = listener;
	}
}
