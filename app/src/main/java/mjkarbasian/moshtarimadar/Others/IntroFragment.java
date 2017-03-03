package mjkarbasian.moshtarimadar.Others;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import mjkarbasian.moshtarimadar.Dashboard;
import mjkarbasian.moshtarimadar.R;


public class IntroFragment extends Fragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().finish();
        Intent intent = new Intent(getActivity(), Dashboard.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
