package mjkarbasian.moshtarimadar.Products;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import mjkarbasian.moshtarimadar.Adapters.DetailProductAdapter;
import mjkarbasian.moshtarimadar.Data.KasebContract;
import mjkarbasian.moshtarimadar.Helpers.Utility;
import mjkarbasian.moshtarimadar.R;


public class DetailProducts extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    //region Declare Values & Views
    final static int FRAGMENT_PRODUCT_HISTORY_LOADER = 5;
    private final String LOG_TAG = DetailProducts.class.getSimpleName();
    String[] mProjection;
    Cursor mCursor;
    ListView mListView;
    DetailProductAdapter mAdapter = null;
    Long productId;
    FloatingActionButton fab;

    ContentValues productHistoryValues = new ContentValues();
    ContentValues productValues = new ContentValues();

    EditText productName;
    EditText productCode;
    EditText productUnit;
    EditText productDescription;
    EditText buyPrice;
    EditText quantity;
    EditText salePrice;
    EditText buyDate;
    EditText discountAmount;
    EditText discountPercent;

    MenuItem saveItem;
    MenuItem editItem;

    AlertDialog.Builder builder;
    AlertDialog dialogView;

    private Uri insertUri;
    //endregion Declare Values & Views

    public DetailProducts() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        productId = Long.parseLong(this.getArguments().getString("productId"));

        mProjection = new String[]{
                KasebContract.ProductHistory._ID,
                KasebContract.ProductHistory.COLUMN_COST,
                KasebContract.ProductHistory.COLUMN_DATE,
                KasebContract.ProductHistory.COLUMN_QUANTITY,
                KasebContract.ProductHistory.COLUMN_SALE_PRICE};

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAdapter = new DetailProductAdapter(
                getActivity(),
                null,
                0);

        View rootView = inflater.inflate(R.layout.fragment_detail_products, container, false);
        mListView = (ListView) rootView.findViewById(R.id.listview_detail_product);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        //region Fab Add ProductHistory
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab_detail_product);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final Dialog dialog = Utility.dialogBuilder(getActivity()
//                        , R.layout.dialog_add_product_history
//                        , R.string.title_add_product_history);

                //region create alert dialog
                builder = new AlertDialog.Builder(getActivity())
                        .setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_add_product_history, null))
                        .setNegativeButton(R.string.discard_button, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialogView.dismiss();
                            }
                        }).setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .setTitle(R.string.title_add_product_history);

                dialogView = builder.create();
                dialogView.show();

                dialogView.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean wantToCloseDialog = false;

                        //region edit product
                        if (CheckForValidityInsertProductHistory()) {
                            productHistoryValues.put(KasebContract.ProductHistory.COLUMN_COST,
                                    Utility.convertFarsiNumbersToDecimal(buyPrice.getText().toString()));
                            productHistoryValues.put(KasebContract.ProductHistory.COLUMN_QUANTITY,
                                    Utility.convertFarsiNumbersToDecimal(quantity.getText().toString()));
                            productHistoryValues.put(KasebContract.ProductHistory.COLUMN_SALE_PRICE,
                                    Utility.convertFarsiNumbersToDecimal(salePrice.getText().toString()));
                            productHistoryValues.put(KasebContract.ProductHistory.COLUMN_DATE, buyDate.getText().toString());
                            productHistoryValues.put(KasebContract.ProductHistory.COLUMN_PRODUCT_ID, productId);

                            insertUri = getActivity().getContentResolver().insert(
                                    KasebContract.ProductHistory.CONTENT_URI,
                                    productHistoryValues
                            );
                            Toast.makeText(getActivity(), getResources().getString(R.string.msg_insert_succeed), Toast.LENGTH_LONG).show();

                            wantToCloseDialog = true;
                        }
                        //endregion edit product

                        if (wantToCloseDialog)
                            dialogView.dismiss();
                    }
                });
                //endregion create alert dialog

                //region Declare Views in dialog
                buyPrice = (EditText) dialogView.findViewById(R.id.add_product_history_buy_price);
                quantity = (EditText) dialogView.findViewById(R.id.add_product_history_quantity);
                salePrice = (EditText) dialogView.findViewById(R.id.add_product_history_sale_price);
                buyDate = (EditText) dialogView.findViewById(R.id.add_product_history_buy_date);
                discountAmount = (EditText) dialogView.findViewById(R.id.add_product_history_discount_amount);
                discountPercent = (EditText) dialogView.findViewById(R.id.add_product_history_discount_percent);

                buyDate.setText(Utility.preInsertDate(getActivity()));

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
                                Utility.setErrorForEditText(getActivity(), discountAmount,
                                        getResources().getString(R.string.not_more_than_sale_price));
                                discountAmount.setSelectAllOnFocus(true);
                                discountAmount.selectAll();
                            }
                        } catch (Exception e) {
                            if (salePrice.getText().toString().equals(null) || salePrice.getText().toString().equals(""))
                                Utility.setErrorForEditText(getActivity(), salePrice, "");
                            else
                                Utility.setErrorForEditText(getActivity(), discountAmount, "");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        try {
                            Float mDiscountAmount = Float.valueOf(discountAmount.getText().toString());
                            Float mSalePrice = Float.valueOf(salePrice.getText().toString());

                            buyPrice.setText(String.format("%.2f", Float.valueOf(mSalePrice - mDiscountAmount)));
                        } catch (Exception e) {
                            if (salePrice.getText().toString().equals(null) || salePrice.getText().toString().equals(""))
                                Utility.setErrorForEditText(getActivity(), salePrice, "");
                            else
                                Utility.setErrorForEditText(getActivity(), discountAmount, "");
                        }
                    }
                });

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
                                Utility.setErrorForEditText(getActivity(), discountPercent,
                                        getResources().getString(R.string.not_more_hundred));
                                discountPercent.setSelectAllOnFocus(true);
                                discountPercent.selectAll();
                            }
                        } catch (Exception e) {
                            if (salePrice.getText().toString().equals(null) || salePrice.getText().toString().equals(""))
                                Utility.setErrorForEditText(getActivity(), salePrice, "");
                            else
                                Utility.setErrorForEditText(getActivity(), discountPercent, "");
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
                            if (salePrice.getText().toString().equals(null) || salePrice.getText().toString().equals(""))
                                Utility.setErrorForEditText(getActivity(), salePrice, "");
                            else
                                Utility.setErrorForEditText(getActivity(), discountPercent, "");
                        }
                    }
                });
                //endregion discountAmount addTextChangedListener

                //endregion Declare Views in dialog
            }
        });
        //endregion Fab Add ProductHistory

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_detail_products, menu);
        saveItem = (MenuItem) menu.findItem(R.id.action_detail_product_save);
        editItem = (MenuItem) menu.findItem(R.id.action_detail_product_edit);
        saveItem.setVisible(isHidden());
        MenuItem sort = menu.findItem(R.id.sort_button);
        sort.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case R.id.action_detail_product_edit:
                saveItem.setVisible(isVisible());
                editItem.setVisible(isHidden());

                productName.setEnabled(true);
                productCode.setEnabled(true);
                productUnit.setEnabled(true);
                productDescription.setEnabled(true);

                //set fab animation and show
                Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up);
                fab.startAnimation(hyperspaceJumpAnimation);
                fab.show();

                break;
            case R.id.action_detail_product_save:
                if (Utility.checkForValidityForEditTextNullOrEmptyAndItterative(
                        getActivity(), productName, KasebContract.Products.CONTENT_URI,
                        KasebContract.Products.COLUMN_PRODUCT_NAME + " = ? and " + KasebContract.Products._ID + " != ? ",
                        KasebContract.Products._ID, new String[]{productName.getText().toString(), String.valueOf(productId)})) {
                    editItem.setVisible(isVisible());
                    saveItem.setVisible(isHidden());

                    productValues.put(KasebContract.Products.COLUMN_PRODUCT_NAME, productName.getText().toString());
                    productValues.put(KasebContract.Products.COLUMN_PRODUCT_CODE, productCode.getText().toString());
                    productValues.put(KasebContract.Products.COLUMN_UNIT, productUnit.getText().toString());
                    productValues.put(KasebContract.Products.COLUMN_DESCRIPTION, productDescription.getText().toString());

                    getActivity().getContentResolver().update(
                            KasebContract.Products.CONTENT_URI,
                            productValues,
                            KasebContract.Products._ID + " = ? ",
                            new String[]{String.valueOf(productId)}
                    );

                    //just a message to show everything are under control
                    Toast.makeText(getContext(),
                            getContext().getResources().getString(R.string.msg_update_succeed), Toast.LENGTH_LONG).show();

                    productName.setEnabled(false);
                    productCode.setEnabled(false);
                    productUnit.setEnabled(false);
                    productDescription.setEnabled(false);
                    fab.hide();
                }
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        Log.d(LOG_TAG, "onStart");

        productName = (EditText) getActivity().findViewById(R.id.detail_product_name);
        productCode = (EditText) getActivity().findViewById(R.id.detail_product_code);
        productUnit = (EditText) getActivity().findViewById(R.id.detail_product_unit);
        productDescription = (EditText) getActivity().findViewById(R.id.detail_product_description);

        mCursor = getContext().getContentResolver().query(
                KasebContract.Products.buildProductsUri(productId),
                new String[]{
                        KasebContract.Products.COLUMN_PRODUCT_NAME,
                        KasebContract.Products.COLUMN_PRODUCT_CODE,
                        KasebContract.Products.COLUMN_DESCRIPTION,
                        KasebContract.Products.COLUMN_UNIT},
                null,
                null,
                null);

        if (mCursor != null)
            if (mCursor.moveToFirst()) {
                productName.setText(mCursor.getString(mCursor.getColumnIndex(KasebContract.Products.COLUMN_PRODUCT_NAME)));
                productCode.setText(mCursor.getString(mCursor.getColumnIndex(KasebContract.Products.COLUMN_PRODUCT_CODE)));
                productUnit.setText(mCursor.getString(mCursor.getColumnIndex(KasebContract.Products.COLUMN_UNIT)));
                productDescription.setText(mCursor.getString(mCursor.getColumnIndex(KasebContract.Products.COLUMN_DESCRIPTION)));
            }

        productName.setEnabled(false);
        productCode.setEnabled(false);
        productUnit.setEnabled(false);
        productDescription.setEnabled(false);
        fab.hide();

        super.onStart();
        updateList();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(FRAGMENT_PRODUCT_HISTORY_LOADER, null, this);
    }

    private void updateList() {
        getLoaderManager().restartLoader(FRAGMENT_PRODUCT_HISTORY_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");

        return new CursorLoader(
                getActivity(),
                KasebContract.ProductHistory.aProductHistory(productId),
                mProjection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(LOG_TAG, "onLoadFinished");
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "onLoadReset");
        mAdapter.swapCursor(null);
    }

    // this method check the validation and correct entries. its check fill first and then check the validation rules.
    private boolean CheckForValidityInsertProductHistory() {
        if (!Utility.checkForValidityForEditTextNullOrEmpty(getActivity(), salePrice))
            return false;
        else if (!Utility.checkForValidityForEditTextNullOrEmpty(getActivity(), quantity))
            return false;
        else if (!Utility.checkForValidityForEditTextNullOrEmpty(getActivity(), buyPrice))
            return false;
        else if (!Utility.checkForValidityForEditTextNullOrEmpty(getActivity(), buyDate))
            return false;
        return true;
    }
}