package hetpin.dailyphoto;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_rate:
			SettingActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
					.parse("market://details?id=hetpin.dailyphoto")));
			//AppRater.showRateDialog(SettingActivity.this, null);
			break;

		default:
			break;
		}

	}
}
