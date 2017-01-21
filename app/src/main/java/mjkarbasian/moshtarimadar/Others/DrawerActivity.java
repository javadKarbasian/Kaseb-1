package mjkarbasian.moshtarimadar.Others;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import mjkarbasian.moshtarimadar.AboutUs.AboutUs;
import mjkarbasian.moshtarimadar.Costs.Costs;
import mjkarbasian.moshtarimadar.Customers.Customers;
import mjkarbasian.moshtarimadar.Dashboard;
import mjkarbasian.moshtarimadar.Debaters.Debaters;
import mjkarbasian.moshtarimadar.Helpers.Utility;
import mjkarbasian.moshtarimadar.Products.Products;
import mjkarbasian.moshtarimadar.R;
import mjkarbasian.moshtarimadar.Sales.Sales;
import mjkarbasian.moshtarimadar.Setting.MySetting;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        mDrawerToggle = toggle;
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //region handle sharepreference
        View header = navigationView.getHeaderView(0);
        TextView kasebProfileTextView = (TextView) header.findViewById(R.id.kaseb_name_text);
        String kasebPREFERENCES = "kasebProfile";
        SharedPreferences kasebSharedPreferences = getSharedPreferences(kasebPREFERENCES, MODE_PRIVATE);

        if (kasebSharedPreferences.getString("firstName", null) != null)
            kasebProfileTextView.setText(
                    kasebSharedPreferences.getString("firstName", null) + "  " +
                            kasebSharedPreferences.getString("lastName", null));
        //endregion handle sharepreference
    }

    @Override
    public void onBackPressed() {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawer.isDrawerOpen(GravityCompat.START))
            mDrawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.nav_dashboard) {
            // Handle the camera action
            intent = new Intent(this, Dashboard.class);
            finish();
            startActivity(intent);
            Utility.setActivityTransition(this);
        }

        if (id == R.id.nav_customers) {
            // Handle the camera action
            intent = new Intent(this, Customers.class);
            finish();
            startActivity(intent);
            Utility.setActivityTransition(this);
        } else if (id == R.id.nav_debtors) {
            intent = new Intent(this, Debaters.class);
            finish();
            startActivity(intent);
            Utility.setActivityTransition(this);
        } else if (id == R.id.nav_sales) {
            intent = new Intent(this, Sales.class);
            finish();
            startActivity(intent);
            Utility.setActivityTransition(this);
        } else if (id == R.id.nav_costs) {
            intent = new Intent(this, Costs.class);
            finish();
            startActivity(intent);
            Utility.setActivityTransition(this);
        } else if (id == R.id.nav_products) {
            intent = new Intent(this, Products.class);
            finish();
            startActivity(intent);
            Utility.setActivityTransition(this);
        } else if (id == R.id.nav_setting) {
            intent = new Intent(this, MySetting.class);
            finish();
            startActivity(intent);
            Utility.setActivityTransition(this);
        } else if (id == R.id.nav_about_us) {
            intent = new Intent(this, AboutUs.class);
            finish();
            startActivity(intent);
            Utility.setActivityTransition(this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
