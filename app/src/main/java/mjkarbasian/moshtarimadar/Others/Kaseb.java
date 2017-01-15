package mjkarbasian.moshtarimadar.Others;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import mjkarbasian.moshtarimadar.R;

public class Kaseb extends AppCompatActivity {

    private IntroFragment kaserbFragment = new IntroFragment();
    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaseb);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        fragmentManager.beginTransaction().replace(R.id.container_kaseb, kaserbFragment, "KasebFragment").commit();
    }
}
