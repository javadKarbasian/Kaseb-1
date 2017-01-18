package mjkarbasian.moshtarimadar.Customers;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import mjkarbasian.moshtarimadar.Adapters.CustomerAdapter;
import mjkarbasian.moshtarimadar.Data.KasebContract;
import mjkarbasian.moshtarimadar.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class CustomersLists extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    final static int FRAGMENT_CUSTOMER_LOADER = 2;
    private final String LOG_TAG = CustomersLists.class.getSimpleName();
    String searchQuery;
    FloatingActionButton fab;
    CustomerAdapter mCustomerAdapter = null;
    ListView mListView;
    String[] mProjection;
    private String sortOrder = null;


    public CustomersLists() {
        super();
    }

    public void onCreate(Bundle savedInstanceState) {
        mProjection = new String[]{
                KasebContract.Customers._ID,
                KasebContract.Customers.COLUMN_FIRST_NAME,
                KasebContract.Customers.COLUMN_LAST_NAME,
                KasebContract.Customers.COLUMN_STATE_ID,
                KasebContract.Customers.COLUMN_CUSTOMER_PICTURE};

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) searchQuery = getArguments().getString("query");
        mCustomerAdapter = new CustomerAdapter(
                getActivity(),
                null,
                0);

        View rootView = inflater.inflate(R.layout.fragment_customers, container, false);
        //hide fab to show it as animation
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab_customers);
        fab.hide();
        final Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_on_click);
        fab.setAnimation(hyperspaceJumpAnimation);
        fab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (hyperspaceJumpAnimation.getRepeatCount() < 2)
                    v.startAnimation(hyperspaceJumpAnimation);
                return false;
            }
        });
        mListView = (ListView) rootView.findViewById(R.id.list_view_customers);
        mListView.setAdapter(mCustomerAdapter);

        //region mListView setOnItemClickListener
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //animation bundle
                View sharedView = view.findViewById(R.id.item_list_customer_avatar);
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity(), sharedView, sharedView.getTransitionName()).toBundle();
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    Intent intent = new Intent(getActivity(), DetailCustomer.class)
                            .setData(KasebContract.Customers.buildCustomerUri(
                                    Long.parseLong(cursor.getString(cursor.getColumnIndex(KasebContract.Customers._ID)))
                            ));
                    startActivity(intent, bundle);
                }
            }
        });
        //endregion mListView setOnItemClickListener

        //region mListView setOnItemLongClickListener
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    final long customerId = cursor.getLong(cursor.getColumnIndex(KasebContract.Customers._ID));

                    int numberUseOfThisCustomer = getContext().getContentResolver().query(
                            KasebContract.Sales.customerSales(customerId),
                            new String[]{
                                    KasebContract.Sales._ID},
                            null, null, null).getCount();

                    if (numberUseOfThisCustomer > 0)
                        Toast.makeText(getContext(),
                                getActivity().getResources().getString(R.string.confirm_delete_customer_list_it_has)
                                        + numberUseOfThisCustomer
                                        + getActivity().getResources().getString(R.string.confirm_delete_customer_list_factors), Toast.LENGTH_LONG).show();
                    else {
                        new AlertDialog.Builder(getActivity())
                                .setTitle(getActivity().getResources().getString(R.string.confirm_title))
                                .setMessage(getActivity().getResources().getString(R.string.confirm_want_delete_customer_info) +
                                        cursor.getString(cursor.getColumnIndex(KasebContract.Customers.COLUMN_FIRST_NAME)) + "  " +
                                        cursor.getString(cursor.getColumnIndex(KasebContract.Customers.COLUMN_LAST_NAME)))
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        getActivity().getContentResolver().delete(
                                                KasebContract.Customers.CONTENT_URI,
                                                KasebContract.Customers._ID + " = ? ",
                                                new String[]{String.valueOf(customerId)}
                                        );

                                        //just a message to show everything are under control
                                        Toast.makeText(getContext(),
                                                getContext().getResources().getString(R.string.msg_delete_succeed), Toast.LENGTH_LONG).show();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                }).show();
                    }
                }
                return true;
            }
        });
        //endregion mListView setOnItemLongClickListener
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_customers, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void getSearchQuery(String query) {
        searchQuery = query;
        updateList();
    }

    public void getSortOrder(int id) {
        switch (id) {
            case R.id.menu_sort_newest:
                sortOrder = KasebContract.Customers.COLUMN_FIRST_NAME + " ASC," + KasebContract.Customers.COLUMN_LAST_NAME + " ASC";
                break;
            case R.id.menu_sort_rating:
                sortOrder = KasebContract.Customers.COLUMN_STATE_ID + " ASC," + KasebContract.Customers.COLUMN_FIRST_NAME + " ASC";
                break;
        }
        updateList();
    }

    @Override
    public void onStart() {
        Log.d(LOG_TAG, "onStart");
        super.onStart();
        updateList();
    }

    @Override
    public void onResume() {
        super.onResume();
        //define animation for scaling fab
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up);
        fab.startAnimation(hyperspaceJumpAnimation);
        fab.show();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(FRAGMENT_CUSTOMER_LOADER, null, this);
    }

    private void updateList() {
        getLoaderManager().restartLoader(FRAGMENT_CUSTOMER_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");
        String whereClause;
        String[] selectArg = null;
        if (searchQuery != null) {
            whereClause = mProjection[1] + " LIKE ? " + " OR " + mProjection[2] + " LIKE ? ";
            selectArg = new String[2];
            selectArg[0] = "%" + searchQuery + "%";
            selectArg[1] = "%" + searchQuery + "%";
        } else {
            whereClause = null;
        }
        return new CursorLoader(getActivity(), KasebContract.Customers.CONTENT_URI, mProjection, whereClause, selectArg, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(LOG_TAG, "onLoadFinished");
        mCustomerAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "onLoadReset");
        mCustomerAdapter.swapCursor(null);
    }


}
