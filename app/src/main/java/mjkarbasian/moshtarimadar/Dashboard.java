package mjkarbasian.moshtarimadar;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;

import mjkarbasian.moshtarimadar.Helpers.Utility;
import mjkarbasian.moshtarimadar.Others.DrawerActivity;

public class Dashboard extends DrawerActivity {

    private KasebDashBoard kasebDashBoard = new KasebDashBoard();
    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Utility.initializer(this);
        fragmentManager.beginTransaction().replace(R.id.container, kasebDashBoard, "KasebDashBoard").commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
