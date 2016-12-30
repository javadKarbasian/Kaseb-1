package mjkarbasian.moshtarimadar.Customers;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import mjkarbasian.moshtarimadar.Adapters.TypesSettingAdapter;
import mjkarbasian.moshtarimadar.Data.KasebContract;
import mjkarbasian.moshtarimadar.Helpers.Utility;
import mjkarbasian.moshtarimadar.R;

/**
 * Created by Unique on 10/21/2016.
 */
public class CustomerInsert extends Fragment {

    EditText firstName;
    EditText lastName;
    EditText birthDay;
    Spinner stateType;
    EditText phoneMobile;
    EditText customerDescription;
    EditText email;
    EditText phoneWork;
    EditText phoneHome;
    EditText phoneOther;
    EditText phoneFax;
    EditText addressCountry;
    EditText addressCity;
    EditText addressStreet;
    EditText addressPostalCode;
    View rootView;
    ContentValues customerValues = new ContentValues();
    private Uri insertUri;

    public CustomerInsert() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_customer_insert, container, false);

        stateType = (Spinner) rootView.findViewById(R.id.input_state_type_spinner);
        firstName = (EditText) rootView.findViewById(R.id.input_first_name);
        lastName = (EditText) rootView.findViewById(R.id.input_last_name);
        birthDay = (EditText) rootView.findViewById(R.id.input_birth_day);
        phoneMobile = (EditText) rootView.findViewById(R.id.input_phone_mobile);
        customerDescription = (EditText) rootView.findViewById(R.id.input_description);
        email = (EditText) rootView.findViewById(R.id.input_email);
        phoneWork = (EditText) rootView.findViewById(R.id.input_phone_work);
        phoneHome = (EditText) rootView.findViewById(R.id.input_phone_home);
        phoneOther = (EditText) rootView.findViewById(R.id.input_phone_other);
        phoneFax = (EditText) rootView.findViewById(R.id.input_phone_fax);
        addressCountry = (EditText) rootView.findViewById(R.id.input_address_country);
        addressCity = (EditText) rootView.findViewById(R.id.input_address_city);
        addressStreet = (EditText) rootView.findViewById(R.id.input_address_street);
        addressPostalCode = (EditText) rootView.findViewById(R.id.input_address_postal_code);

        Cursor cursor = getContext().getContentResolver().query(KasebContract.State.CONTENT_URI
                , null, null, null, null);

        int[] toViews = {
                android.R.id.text1
        };
        String[] fromColumns = {
                KasebContract.State.COLUMN_STATE_POINTER
        };

        TypesSettingAdapter cursorAdapter = new TypesSettingAdapter(getActivity(), cursor, 0, KasebContract.State.COLUMN_STATE_POINTER);
        stateType.setAdapter(cursorAdapter);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        menu.removeItem(R.id.sort_button);
        menu.removeItem(R.id.search_button);
        inflater.inflate(R.menu.fragments_for_insert, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_inputs: {

                if (CheckForValidity(
                        firstName.getText().toString(),
                        lastName.getText().toString(),
                        phoneMobile.getText().toString()
                )) {
                    customerValues.put(KasebContract.Customers.COLUMN_FIRST_NAME, firstName.getText().toString());
                    customerValues.put(KasebContract.Customers.COLUMN_LAST_NAME, lastName.getText().toString());
                    customerValues.put(KasebContract.Customers.COLUMN_BIRTHDAY, birthDay.getText().toString());
                    customerValues.put(KasebContract.Customers.COLUMN_PHONE_MOBILE, phoneMobile.getText().toString());
                    customerValues.put(KasebContract.Customers.COLUMN_PHONE_WORK, phoneWork.getText().toString());
                    customerValues.put(KasebContract.Customers.COLUMN_PHONE_HOME, phoneHome.getText().toString());
                    customerValues.put(KasebContract.Customers.COLUMN_PHONE_FAX, phoneFax.getText().toString());
                    customerValues.put(KasebContract.Customers.COLUMN_PHONE_OTHER, phoneOther.getText().toString());
                    customerValues.put(KasebContract.Customers.COLUMN_DESCRIPTION, customerDescription.getText().toString());
                    customerValues.put(KasebContract.Customers.COLUMN_EMAIL, email.getText().toString());
                    customerValues.put(KasebContract.Customers.COLUMN_ADDRESS_COUNTRY, addressCountry.getText().toString());
                    customerValues.put(KasebContract.Customers.COLUMN_ADDRESS_CITY, addressCity.getText().toString());
                    customerValues.put(KasebContract.Customers.COLUMN_ADDRESS_STREET, addressStreet.getText().toString());
                    customerValues.put(KasebContract.Customers.COLUMN_ADDRESS_POSTAL_CODE, addressPostalCode.getText().toString());

                    customerValues.put(KasebContract.Customers.COLUMN_STATE_ID, stateType.getSelectedItemPosition() + 1);
                    insertUri = getActivity().getContentResolver().insert(
                            KasebContract.Customers.CONTENT_URI,
                            customerValues
                    );

                    //region disabling edit
                    firstName.setEnabled(false);
                    lastName.setEnabled(false);
                    birthDay.setEnabled(false);
                    phoneMobile.setEnabled(false);
                    customerDescription.setEnabled(false);
                    email.setEnabled(false);
                    phoneWork.setEnabled(false);
                    phoneHome.setEnabled(false);
                    phoneOther.setEnabled(false);
                    phoneFax.setEnabled(false);
                    addressCountry.setEnabled(false);
                    addressCity.setEnabled(false);
                    addressStreet.setEnabled(false);
                    addressPostalCode.setEnabled(false);
                    stateType.setEnabled(false);

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

    // this method check the validation and correct entries. its check fill first and then check the validation rules.
    private boolean CheckForValidity(String customerFirstName, String customerLastName, String customerPhoneMobile) {
        if (customerFirstName.equals("") || customerFirstName.equals(null)) {
            Toast.makeText(getActivity(), "Choose apropriate name for CUSTOMER.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (customerLastName.equals("") || customerLastName.equals(null)) {
            Toast.makeText(getActivity(), "Choose apropriate last name for CUSTOMER.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (customerPhoneMobile.equals("") || customerPhoneMobile.equals(null)) {
            Toast.makeText(getActivity(), "Choose apropriate phone mobile for CUSTOMER.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Cursor mCursor = getContext().getContentResolver().query(
                    KasebContract.Customers.CONTENT_URI,
                    new String[]{KasebContract.Customers._ID},
                    KasebContract.Customers.COLUMN_PHONE_MOBILE + " = ? ",
                    new String[]{customerPhoneMobile},
                    null);

            if (mCursor != null)
                if (mCursor.moveToFirst())
                    if (mCursor.getCount() > 0) {
                        Toast.makeText(getActivity(), "Choose apropriate (Not Itterative) phone mobile for CUSTOMER.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
            return true;
        }
    }

    private void backToLastPage() {
        Utility.clearForm((ViewGroup) rootView);
        getFragmentManager().popBackStackImmediate();
    }
}