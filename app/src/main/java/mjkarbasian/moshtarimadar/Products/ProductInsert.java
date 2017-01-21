package mjkarbasian.moshtarimadar.Products;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import mjkarbasian.moshtarimadar.Costs.CostInsert;
import mjkarbasian.moshtarimadar.Data.KasebContract;
import mjkarbasian.moshtarimadar.Helpers.Utility;
import mjkarbasian.moshtarimadar.R;


/**
 * Created by Unique on 10/25/2016.
 */
public class ProductInsert extends Fragment {

    //region declare values
    private static final String LOG_TAG = CostInsert.class.getSimpleName();
    View rootView;
    ContentValues productValues = new ContentValues();
    ContentValues productHistoryValues = new ContentValues();
    EditText productName;
    EditText productCode;
    EditText unit;
    EditText productDescription;
    EditText buyPrice;
    EditText quantity;
    EditText salePrice;
    EditText buyDate;
    EditText discountAmount;
    EditText discountPercent;
    TextInputLayout productNameTextInputLayout;
    TextInputLayout salePriceTextInputLayout;
    TextInputLayout quantityTextInputLayout;
    TextInputLayout buyPriceTextInputLayout;
    TextInputLayout buyDateTextInputLayout;
    TextInputLayout productCodeTextInputLayout;
    TextInputLayout productUnitTextInputLayout;
    TextInputLayout productDescriptionTextInputLayout;
    TextInputLayout discountAmountTextInputLayout;
    TextInputLayout discountPercentTextInputLayout;
    private Uri insertUri;
    //endregion declare values

    public ProductInsert() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_product_insert, container, false);

        productName = (EditText) rootView.findViewById(R.id.input_product_name);
        productCode = (EditText) rootView.findViewById(R.id.input_product_code);
        productCode.setText(Utility.preInsertProductCode(getActivity()));
        unit = (EditText) rootView.findViewById(R.id.input_product_unit);
        productDescription = (EditText) rootView.findViewById(R.id.input_product_description);
        buyPrice = (EditText) rootView.findViewById(R.id.input_buy_price);
        quantity = (EditText) rootView.findViewById(R.id.input_quantity);
        salePrice = (EditText) rootView.findViewById(R.id.input_sale_price);
        buyDate = (EditText) rootView.findViewById(R.id.dialog_add_payment_for_sale_input_due_date);
        discountAmount = (EditText) rootView.findViewById(R.id.input_discount_amount);
        discountPercent = (EditText) rootView.findViewById(R.id.input_discount_percent);

        productNameTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.text_input_layout_input_product_name);
        salePriceTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.text_input_layout_input_sale_price);
        quantityTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.text_input_layout_input_quantity);
        buyPriceTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.text_input_layout_input_buy_price);
        buyDateTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.text_input_layout_input_buy_date);
        productCodeTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.text_input_layout_input_product_code);
        productUnitTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.text_input_layout_input_product_unit);
        productDescriptionTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.text_input_layout_input_product_description);
        discountAmountTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.text_input_layout_input_discount_amount);
        discountPercentTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.text_input_layout_input_discount_percent);

        buyDate.setText(Utility.preInsertDate(getActivity()));

        setHelperText();

        //region discountAmount addTextChangedListener
        discountAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    Float mDiscountAmount = Float.valueOf(discountAmount.getText().toString());
                    Float mSalePrice = Float.valueOf(salePrice.getText().toString());

                    if (mDiscountAmount > mSalePrice) {
                        discountAmount.setText(salePrice.getText().toString());
                        discountAmountTextInputLayout.setError(getResources().getString(R.string.not_more_than_sale_price));
                        discountAmount.setSelectAllOnFocus(true);
                        discountAmount.selectAll();
                        discountAmount.requestFocus();
                    }
                } catch (Exception e) {
                    if (salePrice.getText().toString().equals(null) || salePrice.getText().toString().equals("")) {
                        Utility.changeColorOfHelperText(getActivity(), salePriceTextInputLayout, R.color.colorRed);
                        salePrice.requestFocus();
                    } else {
                        discountAmount.setError(getResources().getString(R.string.choose_appropriate_data));
                        Utility.changeColorOfHelperText(getActivity(), discountAmountTextInputLayout, R.color.colorPrimaryLight);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Float mDiscountAmount = Float.valueOf(discountAmount.getText().toString());
                    Float mSalePrice = Float.valueOf(salePrice.getText().toString());

                    buyPrice.setText(String.format("%.2f", Float.valueOf(mSalePrice - mDiscountAmount)));
                } catch (Exception e) {
                    if (salePrice.getText().toString().equals(null) || salePrice.getText().toString().equals("")) {
                        Utility.changeColorOfHelperText(getActivity(), salePriceTextInputLayout, R.color.colorRed);
                        salePrice.requestFocus();
                    } else {
                        discountAmountTextInputLayout.setError(getResources().getString(R.string.choose_appropriate_data));
                        Utility.changeColorOfHelperText(getActivity(), discountAmountTextInputLayout, R.color.colorPrimaryLight);
                    }
                }
            }
        });
        //endregion discountAmount addTextChangedListener

        //region discountPercent addTextChangedListener
        discountPercent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    Float mDiscountPercent = Float.valueOf(discountPercent.getText().toString());
                    Float mSalePrice = Float.valueOf(salePrice.getText().toString());

                    if (mDiscountPercent > 100) {
                        discountAmount.setText(String.format("%.2f", Float.valueOf(mDiscountPercent * mSalePrice / 100)));

                        discountPercent.setText(Utility.doubleFormatter(100));
                        discountPercentTextInputLayout.setError(getResources().getString(R.string.not_more_hundred));
                        discountPercent.setSelectAllOnFocus(true);
                        discountPercent.selectAll();
                    }
                } catch (Exception e) {
                    if (salePrice.getText().toString().equals(null) || salePrice.getText().toString().equals("")) {
                        Utility.changeColorOfHelperText(getActivity(), salePriceTextInputLayout, R.color.colorRed);
                        salePrice.requestFocus();
                    } else {
                        discountPercentTextInputLayout.setError(getResources().getString(R.string.choose_appropriate_data));
                        Utility.changeColorOfHelperText(getActivity(), discountPercentTextInputLayout, R.color.colorPrimaryLight);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Float mDiscountPercent = Float.valueOf(discountPercent.getText().toString());
                    Float mSalePrice = Float.valueOf(salePrice.getText().toString());

                    buyPrice.setText(String.format("%.2f",
                            Float.valueOf((float) ((Float.valueOf(100 - mDiscountPercent) / 100.0) * mSalePrice))));
                    discountAmount.setText(String.format("%.2f", Float.valueOf(mDiscountPercent * mSalePrice / 100)));
                } catch (Exception e) {
                    if (salePrice.getText().toString().equals(null) || salePrice.getText().toString().equals("")) {
                        Utility.changeColorOfHelperText(getActivity(), salePriceTextInputLayout, R.color.colorRed);
                        salePrice.requestFocus();
                    } else {
                        discountPercentTextInputLayout.setError(getResources().getString(R.string.choose_appropriate_data));
                        Utility.changeColorOfHelperText(getActivity(), discountPercentTextInputLayout, R.color.colorPrimaryLight);
                    }
                }
            }
        });
        //endregion discountPercent addTextChangedListener

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.removeItem(R.id.sort_button);
        menu.removeItem(R.id.search_button);
        inflater.inflate(R.menu.fragments_for_insert, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_inputs: {
                if (checkValidityWithChangeColorOfHelperText()) {

                    productValues.put(KasebContract.Products.COLUMN_PRODUCT_NAME, productName.getText().toString());
                    productValues.put(KasebContract.Products.COLUMN_PRODUCT_CODE, productCode.getText().toString());
                    productValues.put(KasebContract.Products.COLUMN_UNIT, unit.getText().toString());
                    productValues.put(KasebContract.Products.COLUMN_DESCRIPTION, productDescription.getText().toString());
                    insertUri = getActivity().getContentResolver().insert(
                            KasebContract.Products.CONTENT_URI,
                            productValues
                    );

                    productHistoryValues.put(KasebContract.ProductHistory.COLUMN_COST,
                            Utility.convertFarsiNumbersToDecimal(buyPrice.getText().toString()));
                    productHistoryValues.put(KasebContract.ProductHistory.COLUMN_QUANTITY,
                            Utility.convertFarsiNumbersToDecimal(quantity.getText().toString()));
                    productHistoryValues.put(KasebContract.ProductHistory.COLUMN_SALE_PRICE,
                            Utility.convertFarsiNumbersToDecimal(salePrice.getText().toString()));
                    productHistoryValues.put(KasebContract.ProductHistory.COLUMN_DATE, buyDate.getText().toString());
                    productHistoryValues.put(KasebContract.ProductHistory.COLUMN_PRODUCT_ID, insertUri.getLastPathSegment());

                    insertUri = getActivity().getContentResolver().insert(
                            KasebContract.ProductHistory.CONTENT_URI,
                            productHistoryValues
                    );

                    //region disabling edit
                    productName.setEnabled(false);
                    productCode.setEnabled(false);
                    unit.setEnabled(false);
                    productDescription.setEnabled(false);
                    buyPrice.setEnabled(false);
                    quantity.setEnabled(false);
                    salePrice.setEnabled(false);
                    buyDate.setEnabled(false);

                    //just a message to show everything are under control
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.msg_insert_succeed),
                            Toast.LENGTH_LONG).show();

                    backToLastPage();
                }

                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // this method back to the activity view. this must be a utility method.
    private void backToLastPage() {
        Utility.clearForm((ViewGroup) rootView);
        getFragmentManager().popBackStackImmediate();
    }

    private void setHelperText() {

        productNameTextInputLayout.setError(getResources().getString(R.string.choose_appropriate_data)
                + getResources().getString(R.string.non_repetitive));

        salePriceTextInputLayout.setError(getResources().getString(R.string.choose_appropriate_data));
        quantityTextInputLayout.setError(getResources().getString(R.string.choose_appropriate_data));

        buyPriceTextInputLayout.setError(getResources().getString(R.string.choose_appropriate_data)
                + getResources().getString(R.string.not_more_than_sale_price));

        productCodeTextInputLayout.setError(getResources().getString(R.string.choose_appropriate_data));
        productUnitTextInputLayout.setError(getResources().getString(R.string.choose_appropriate_data));
        productDescriptionTextInputLayout.setError(getResources().getString(R.string.choose_appropriate_data));

        buyDateTextInputLayout.setError(getResources().getString(R.string.choose_appropriate_data)
                + getResources().getString(R.string.date_format_error));

        discountAmountTextInputLayout.setError(getResources().getString(R.string.choose_appropriate_data)
                + getResources().getString(R.string.not_more_than_sale_price));

        discountPercentTextInputLayout.setError(getResources().getString(R.string.choose_appropriate_data)
                + getResources().getString(R.string.not_more_hundred));
    }

    // this method check the validation and correct entries. its check fill first and then check the validation rules.
    private boolean checkValidityWithChangeColorOfHelperText() {

        if (!Utility.checkForValidityForEditTextNullOrEmptyAndItterative(
                getActivity(), productName, productNameTextInputLayout, KasebContract.Products.CONTENT_URI,
                KasebContract.Products.COLUMN_PRODUCT_NAME + " = ? ",
                KasebContract.Products._ID, new String[]{productName.getText().toString()}))
            return false;

        if (!Utility.checkForValidityForEditTextNullOrEmpty(getActivity(), productCode)) {
            Utility.changeColorOfHelperText(getActivity(), productCodeTextInputLayout, Utility.mIdOfColorSetError);
            productCode.setSelectAllOnFocus(true);
            productCode.selectAll();
            productCode.requestFocus();
            return false;
        } else
            Utility.changeColorOfHelperText(getActivity(), productCodeTextInputLayout, Utility.mIdOfColorGetError);

        if (!Utility.checkForValidityForEditTextNullOrEmpty(getActivity(), salePrice)) {
            Utility.changeColorOfHelperText(getActivity(), salePriceTextInputLayout, Utility.mIdOfColorSetError);
            salePrice.setSelectAllOnFocus(true);
            salePrice.selectAll();
            salePrice.requestFocus();
            return false;
        } else
            Utility.changeColorOfHelperText(getActivity(), salePriceTextInputLayout, Utility.mIdOfColorGetError);

        if (!Utility.checkForValidityForEditTextNullOrEmpty(getActivity(), quantity)) {
            Utility.changeColorOfHelperText(getActivity(), quantityTextInputLayout, Utility.mIdOfColorSetError);
            quantity.setSelectAllOnFocus(true);
            quantity.selectAll();
            quantity.requestFocus();
            return false;
        } else
            Utility.changeColorOfHelperText(getActivity(), quantityTextInputLayout, Utility.mIdOfColorGetError);

        if (!Utility.checkForValidityForEditTextNullOrEmpty(getActivity(), buyPrice)) {
            Utility.changeColorOfHelperText(getActivity(), buyPriceTextInputLayout, Utility.mIdOfColorSetError);
            buyPrice.setSelectAllOnFocus(true);
            buyPrice.selectAll();
            buyPrice.requestFocus();
            return false;
        } else {
            Float mBuyPrice = Float.valueOf(buyPrice.getText().toString());
            Float mSalePrice = Float.valueOf(salePrice.getText().toString());

            if (mBuyPrice > mSalePrice) {
                Utility.changeColorOfHelperText(getActivity(), buyPriceTextInputLayout, Utility.mIdOfColorSetError);
                buyPrice.setSelectAllOnFocus(true);
                buyPrice.selectAll();
                buyPrice.requestFocus();
                return false;
            } else
                Utility.changeColorOfHelperText(getActivity(), buyPriceTextInputLayout, Utility.mIdOfColorGetError);
        }

        if (!Utility.checkForValidityForEditTextDate(getActivity(), buyDate)) {
            Utility.changeColorOfHelperText(getActivity(), buyDateTextInputLayout, Utility.mIdOfColorSetError);
            buyDate.setSelectAllOnFocus(true);
            buyDate.selectAll();
            buyDate.requestFocus();
            return false;
        } else
            Utility.changeColorOfHelperText(getActivity(), buyDateTextInputLayout, Utility.mIdOfColorGetError);

        return true;
    }
}