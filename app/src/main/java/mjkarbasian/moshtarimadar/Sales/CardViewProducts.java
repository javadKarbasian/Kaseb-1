package mjkarbasian.moshtarimadar.Sales;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

import mjkarbasian.moshtarimadar.Adapters.ProductAdapter;
import mjkarbasian.moshtarimadar.Helpers.Utility;
import mjkarbasian.moshtarimadar.R;

/**
 * Created by Unique on 20/12/2016.
 */
public class CardViewProducts extends Fragment {
    ListView productListView;
    ProductAdapter mChosenProductAdapter;
    ArrayList<Map<String, String>> mProductListHashMap;
    View view;
    Map<String, String> cursor;
    EditText howManyEditTextForEdit;
    TextInputLayout textInputLayouthowManyEditTextForEdit;
    int index;

    String _id;
    String _nameOfProduct;
    String activity;

    Long differneceOfBuy_Sale;
    Long mDetailSaleId = 0l;

    AlertDialog.Builder builder;
    AlertDialog dialogView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_card_view_products, container, false);
        activity = getArguments().getString("activity").toString();
        mChosenProductAdapter = new ProductAdapter(getActivity(), mProductListHashMap);
        productListView = (ListView) view.findViewById(R.id.list_view_fragment_card_view_products);
        productListView.setAdapter(mChosenProductAdapter);

        try {
            mDetailSaleId = getArguments().getLong("detailSaleId");
        } catch (Exception e) {
            Log.i(CardViewProducts.class.getName(),
                    "DetailSaleView does not put DetailSaleId in Bundle of CardViewProducts Fragments.");
        }

        //region ProductListView OnItemClickListener
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                cursor = (Map<String, String>) parent.getItemAtPosition(position);

                //region Create AlertDialog
                builder = new AlertDialog.Builder(getActivity())
                        .setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_edit_chosen_product_for_sale, null))
                        .setNegativeButton(R.string.discard_button, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialogView.dismiss();
                            }
                        }).setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .setTitle(R.string.edit_quantity)
                        .setMessage(R.string.less_than_stock_explain_text);

                dialogView = builder.create();
                dialogView.show();

                dialogView.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean wantToCloseDialog = false;

                        try {
                            Long num = Long.parseLong(howManyEditTextForEdit.getText().toString());

                            if (differneceOfBuy_Sale >= num) {
                                mProductListHashMap.set(index,
                                        Utility.setValueWithIndexInKeyOfMapRow(
                                                cursor,
                                                "quantity",
                                                String.valueOf(num)
                                        ));

                                productListView.setAdapter(mChosenProductAdapter);

                                if (activity.equals("insert"))
                                    ((DetailSaleInsert) getActivity()).setValuesOfFactor();
                                else if (activity.equals("view"))
                                    ((DetailSaleView) getActivity()).setValuesOfFactor();

                                Utility.setHeightOfListView(productListView);

                                wantToCloseDialog = true;
                            } else
                                textInputLayouthowManyEditTextForEdit.setError(getResources().getString(R.string.not_enough_stock));

                            if (wantToCloseDialog)
                                dialogView.dismiss();
                        } catch (Exception e) {
                            textInputLayouthowManyEditTextForEdit.setError(getResources().getString(R.string.example_quantity));
                            howManyEditTextForEdit.setSelectAllOnFocus(true);
                            howManyEditTextForEdit.selectAll();
                            howManyEditTextForEdit.requestFocus();
                        }
                    }
                });
                //endregion Create AlertDialog

                //region Set Views To Dialog
                if (cursor != null) {
                    _id = cursor.get("id");

                    index = Utility.indexOfRowsInMap(mProductListHashMap, "id", _id);

                    if (activity.equals("insert"))
                        //in this line does not matter post 0l to second parameter of function because not use in this case
                        differneceOfBuy_Sale = Utility.checkNumberOfProductsForDetailSale(getActivity(),
                                0l, "SaleInsert", Long.parseLong(_id));
                    else if (activity.equals("view"))
                        differneceOfBuy_Sale = Utility.checkNumberOfProductsForDetailSale(getActivity(),
                                mDetailSaleId, "SaleView", Long.parseLong(_id));

                    howManyEditTextForEdit = (EditText) dialogView
                            .findViewById(R.id.edit_chosen_product_for_sale_number);

                    textInputLayouthowManyEditTextForEdit = (TextInputLayout) dialogView
                            .findViewById(R.id.text_input_layout_edit_chosen_product_for_sale_number);

                    howManyEditTextForEdit.setHint(getString(R.string.stock_product) + differneceOfBuy_Sale);
                }
                //endregion Set Views To Dialog
            }
        });
        //endregion ProductListView OnItemClickListener

        //region ProductListView OnItemLongClickListener
        productListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                Map<String, String> cursor = (Map<String, String>) parent.getItemAtPosition(position);

                if (cursor != null) {
                    _id = cursor.get("id");
                    _nameOfProduct = cursor.get("name");

                    new AlertDialog.Builder(getActivity())
                            .setTitle(getActivity().getResources().getString(R.string.confirm_title))
                            .setMessage(getActivity().getResources().getString(R.string.confirm_delete_product_list) + _nameOfProduct)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    mProductListHashMap.remove(Utility.indexOfRowsInMap(mProductListHashMap, "id", _id));
                                    productListView.setAdapter(mChosenProductAdapter);

                                    if (activity.equals("insert"))
                                        ((DetailSaleInsert) getActivity()).setValuesOfFactor();
                                    else if (activity.equals("view"))
                                        ((DetailSaleView) getActivity()).setValuesOfFactor();

                                    Utility.setHeightOfListView(productListView);
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
        //endregion ProductListView OnItemLongClickListener

        mProductListHashMap = null;
        return view;
    }

    public void getChosenProductAdapter(ArrayList<Map<String, String>> list) {
        mProductListHashMap = list;
        mChosenProductAdapter = new ProductAdapter(getActivity(), mProductListHashMap);
        productListView.setAdapter(mChosenProductAdapter);

        if (activity.equals("insert"))
            ((DetailSaleInsert) getActivity()).setValuesOfFactor();
        else if (activity.equals("view"))
            ((DetailSaleView) getActivity()).setValuesOfFactor();

        Utility.setHeightOfListView(productListView);
    }
}
