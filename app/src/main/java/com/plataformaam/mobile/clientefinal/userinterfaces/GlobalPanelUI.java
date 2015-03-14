package com.plataformaam.mobile.clientefinal.userinterfaces;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.plataformaam.mobile.clientefinal.userinterfaces.mapsfragments.GlobalNavigateFragment;
import com.plataformaam.mobile.clientefinal.R;
import com.plataformaam.mobile.clientefinal.models.vcloc.VComComposite;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPI;
import com.plataformaam.mobile.clientefinal.userinterfaces.fragments.FragmentDeleteUpiConfirmation;
import com.plataformaam.mobile.clientefinal.userinterfaces.fragments.FragmentEditUpi;
import com.plataformaam.mobile.clientefinal.userinterfaces.listfragments.FragmentUpiList;
import com.plataformaam.mobile.clientefinal.userinterfaces.listfragments.FragmentVComCompositeList;
import com.plataformaam.mobile.clientefinal.userinterfaces.fragments.NavigationDrawerFragment;


public class GlobalPanelUI extends ActionBarActivity
        implements
        NavigationDrawerFragment.NavigationDrawerCallbacks,
        FragmentUpiList.OnFragmentInteractionListener,
        GlobalNavigateFragment.OnFragmentInteractionListener,
        FragmentVComCompositeList.OnFragmentInteractionListener,
        FragmentEditUpi.OnFragmentInteractionListener,
        FragmentDeleteUpiConfirmation.OnFragmentInteractionListener
    {


    public static FragmentManager fragmentManager;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_global_panel_ui);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        android.app.Fragment frag;

        switch (position){
            case 0:
                frag= FragmentUpiList.newInstance();
                break;
            case 1:
                frag= FragmentEditUpi.newInstance();
                break;
            case 2:
                frag= FragmentVComCompositeList.newInstance(FragmentVComCompositeList.MODE.ALL_VCOM);
                break;
            case 3:
                frag=  FragmentVComCompositeList.newInstance(FragmentVComCompositeList.MODE.MY_VCOM);
                break;
            default:
                frag=   FragmentVComCompositeList.newInstance(FragmentVComCompositeList.MODE.MY_VCOM);
                break;
        }
        fragmentTransaction.replace(R.id.container,frag, null).commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.sidemenu_upi);
                break;
            case 2:
                mTitle = getString(R.string.sidemenu_vcloc);
                break;
            case 3:
                mTitle = getString(R.string.sidemenu_navigate);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.global_panel_ui, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_goToLogoutUI:
                goToLogout();
                return true;
            case R.id.action_goToEditUpiUI:
                goToCreateUPI();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(VComComposite composite) {
        if( composite!= null ){
            mTitle = composite.getName();
            restoreActionBar();
        }
    }

        @Override
    public void onFragmentInteraction(UPI upi) {
        if (upi != null) {
            mTitle = upi.getTitle();
            restoreActionBar();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        /*
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.popup_menu_upi_list , menu);
        */
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {

            default:
                return super.onContextItemSelected(item);
        }
    }

    public void goToLogout(){
        Intent intent = new Intent(GlobalPanelUI.this,UserLogoutUI.class);
        startActivity(intent);
    }

    public void goToCreateUPI(){
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        android.app.Fragment frag = FragmentEditUpi.newInstance();
        fragmentTransaction.replace(R.id.container,frag, null).commit();
    }


    public void goToListUPI(){
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        android.app.Fragment frag = FragmentUpiList.newInstance();
        fragmentTransaction.replace(R.id.container,frag, null).commit();
    }

}
