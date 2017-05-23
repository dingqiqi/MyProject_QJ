package newsandtools.dingqiqi.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import newsandtools.dingqiqi.com.R;
import newsandtools.dingqiqi.com.config.AppConfig;
import newsandtools.dingqiqi.com.view.DoorView;

public class LoadActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_layout);

        DoorView doorView = (DoorView) findViewById(R.id.doorView);

        if (!AppConfig.getFirstInto(this)) {
            AppConfig.saveFirstInto(this);

            Intent intent = new Intent(LoadActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }

        doorView.setmCallBack(new DoorView.StatusCallBack() {
            @Override
            public void Open() {
                Intent intent = new Intent(LoadActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
