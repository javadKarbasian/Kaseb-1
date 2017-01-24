package mjkarbasian.moshtarimadar.Sales;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import mjkarbasian.moshtarimadar.Adapters.TaxAdapter;
import mjkarbasian.moshtarimadar.Helpers.Utility;
import mjkarbasian.moshtarimadar.R;

/**
 * Created by Unique on 20/12/2016.
 */
public class CardViewTaxes extends Fragment {
    ListView taxListView;
    TaxAdapter mTaxAdapter;
    ArrayList<Map<String, String>> mTaxListHashMap;
    View view;
    String activity;
    String _id;
    String _amount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_card_view_taxes, container, false);
        activity = getArguments().getString("activity").toString();

        mTaxAdapter = new TaxAdapter(getActivity(), mTaxListHashMap);
        taxListView = (ListView) view.findViewById(R.id.list_view_fragment_card_view_taxes);
        taxListView.setAdapter(mTaxAdapter);
        TextView emptyText = (TextView) view.findViewById(R.id.empty_text_view);
        emptyText.setText(getActivity().getResources().getString(R.string.empty_list_text_detail_sale));
        emptyText.setVisibility(View.VISIBLE);
        taxListView.setEmptyView(emptyText);

        //region TaxListView OnItemClickListener
        taxListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        //endregion TaxListView OnItemClickListener

        //region TaxListView OnItemLongClickListener
        taxListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> cursor = (Map<String, String>) parent.getItemAtPosition(position);
                if (cursor != null) {
                    _id = cursor.get("id");
                    _amount = cursor.get("amount");

                    new AlertDialog.Builder(getActivity())
                            .setTitle(getActivity().getResources().getString(R.string.confirm_title))
                            .setMessage(getActivity().getResources().getString(R.string.confirm_delete_tax) + _amount)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    mTaxListHashMap.remove(Utility.indexOfRowsInMap(mTaxListHashMap, "id", _id));
                                    taxListView.setAdapter(mTaxAdapter);

                                    if (activity.equals("insert"))
                                        ((DetailSaleInsert) getActivity()).setValuesOfFactor();
                                    else if (activity.equals("view"))
                                        ((DetailSaleView) getActivity()).setValuesOfFactor();

                                    Utility.setHeightOfListView(taxListView);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            }).show();
                }

                return true;
            }
        });
        //endregion TaxListView OnItemLongClickListener

        return view;
    }

    public void getTaxAdapter(ArrayList<Map<String, String>> list) {
        mTaxListHashMap = list;
        mTaxAdapter = new TaxAdapter(getActivity(), mTaxListHashMap);
        taxListView.setAdapter(mTaxAdapter);

        if (activity.equals("insert"))
            ((DetailSaleInsert) getActivity()).setValuesOfFactor();
        else if (activity.equals("view"))
            ((DetailSaleView) getActivity()).setValuesOfFactor();

        Utility.setHeightOfListView(taxListView);
    }
}
