package mjkarbasian.moshtarimadar.Sales;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import mjkarbasian.moshtarimadar.Adapters.PaymentAdapter;
import mjkarbasian.moshtarimadar.Helpers.Utility;
import mjkarbasian.moshtarimadar.R;


/**
 * Created by Unique on 20/12/2016.
 */
public class CardViewPayments extends Fragment {
    ListView paymentListView;
    PaymentAdapter mPaymentAdapter;
    ArrayList<Map<String, String>> mPaymentListHashMap;
    View view;
    String activity;
    String _id;
    String _amount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_card_view_payments, container, false);
        activity = getArguments().getString("activity").toString();

        mPaymentAdapter = new PaymentAdapter(getActivity(), mPaymentListHashMap);
        paymentListView = (ListView) view.findViewById(R.id.list_view_fragment_card_view_payments);
        paymentListView.setAdapter(mPaymentAdapter);
        TextView emptyText = (TextView) view.findViewById(R.id.empty_text_view);
        emptyText.setText(getActivity().getResources().getString(R.string.empty_list_text_detail_sale));
        if(paymentListView.getCount()==0)emptyText.setVisibility(View.VISIBLE);
        else emptyText.setVisibility(View.INVISIBLE);
        paymentListView.setEmptyView(emptyText);


        //region PaymentListView OnItemClickListener
        paymentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final CheckBox checkBox = (CheckBox) view.findViewById(R.id.payment_list_for_sale_is_passed_check_box);
                checkBox.setEnabled(true);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Map<String, String> selectedPayment = mPaymentListHashMap.get(position);
                        selectedPayment.put("isPass", String.valueOf(checkBox.isChecked()));
                        DetailSaleView dSActivity = (DetailSaleView) getActivity();
                        dSActivity.setPaymentMap(mPaymentListHashMap);
                    }
                });
            }
        });
        //endregion PaymentListView OnItemClickListener

        //region PaymentListView OnItemLongClickListener
        paymentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> cursor = (Map<String, String>) parent.getItemAtPosition(position);
                if (cursor != null) {
                    _id = cursor.get("id");
                    _amount = cursor.get("amount");

                    new AlertDialog.Builder(getActivity())
                            .setTitle(getActivity().getResources().getString(R.string.confirm_title))
                            .setMessage(getActivity().getString(R.string.confirm_delete_payment) + _amount)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    mPaymentListHashMap.remove(Utility.indexOfRowsInMap(mPaymentListHashMap, "id", _id));
                                    paymentListView.setAdapter(mPaymentAdapter);

                                    if (activity.equals("insert"))
                                        ((DetailSaleInsert) getActivity()).setValuesOfFactor();
                                    else if (activity.equals("view"))
                                        ((DetailSaleView) getActivity()).setValuesOfFactor();

                                    Utility.setHeightOfListView(paymentListView);
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
        //endregion PaymentListView OnItemLongClickListener

        return view;
    }

    public void getPaymentAdapter(ArrayList<Map<String, String>> list) {
        mPaymentListHashMap = list;
        mPaymentAdapter = new PaymentAdapter(getActivity(), mPaymentListHashMap);
        paymentListView.setAdapter(mPaymentAdapter);

        if (activity.equals("insert"))
            ((DetailSaleInsert) getActivity()).setValuesOfFactor();
        else if (activity.equals("view"))
            ((DetailSaleView) getActivity()).setValuesOfFactor();
        Utility.setHeightOfListView(paymentListView);
    }
}
