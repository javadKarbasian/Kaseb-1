package mjkarbasian.moshtarimadar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import mjkarbasian.moshtarimadar.R;

/**
 * Created by family on 11/2/2016.
 */
public class ChooseTourAdapter extends BaseAdapter {

    private final Context mContext;
    private final LayoutInflater mInflater;
    ArrayList<Integer> mTourIcon;
    ArrayList<String> mTourTitle;

    public ChooseTourAdapter(Context context, ArrayList<Integer> tourIcon, ArrayList<String> tourTitle) {
        mContext = context;
        mTourIcon = tourIcon;
        mTourTitle = tourTitle;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        int size = mTourIcon.size();
        return size;
    }

    @Override
    public String getItem(int position) {
        return mTourTitle.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mTourIcon.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        view = mInflater.inflate(R.layout.list_item_tours, parent, false);
        ImageView mTourIconImageView = (ImageView) view.findViewById(R.id.item_list_tour_image);
        TextView mTourTitleTextView = (TextView) view.findViewById(R.id.item_list_tour_name);

        mTourIconImageView.setImageResource(mTourIcon.get(position));
        mTourTitleTextView.setText(mTourTitle.get(position));

        return view;
    }
}
