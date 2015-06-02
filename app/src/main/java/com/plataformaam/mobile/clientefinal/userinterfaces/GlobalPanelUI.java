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

import com.plataformaam.mobile.clientefinal.userinterfaces.fragments.FragmentEditImageUpi;
import com.plataformaam.mobile.clientefinal.userinterfaces.fragments.FragmentUploadImage;
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
        FragmentEditImageUpi.OnFragmentInteractionListener,
        FragmentUploadImage.OnFragmentInteractionListener,
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

            case R.id.action_goToEditImageUpiUI:
                goToCreateImageUPI();
                return true;


            case R.id.action_goToEditUpiUI:
                goToCreateUPI();
                return true;

            case R.id.action_goToListUpiUI:
                goToListUPI();
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


    public void goToListUPI(){
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        android.app.Fragment frag = FragmentUpiList.newInstance();
        fragmentTransaction.replace(R.id.container,frag, null).commit();
    }

    public void goToCreateUPI(){
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        android.app.Fragment frag = FragmentEditUpi.newInstance();
        fragmentTransaction.replace(R.id.container,frag, null).commit();
    }

    public void goToCreateImageUPI(){
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        android.app.Fragment frag = FragmentEditImageUpi.newInstance();
        fragmentTransaction.replace(R.id.container,frag, null).commit();
    }








    //FUNCIONALIDADES PARA A CAMERA
    // Storage for camera image URI components
    private final static String CAPTURED_PHOTO_PATH_KEY = "mCurrentPhotoPath";
    private final static String CAPTURED_PHOTO_URI_KEY = "mCapturedImageURI";

    // Required for camera operations in order to save the image file on resume.
    private String mCurrentPhotoPath = null;
    private Uri mCapturedImageURI = null;


        @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(CAPTURED_PHOTO_PATH_KEY)) {
            mCurrentPhotoPath = savedInstanceState.getString(CAPTURED_PHOTO_PATH_KEY);
        }
        if (savedInstanceState.containsKey(CAPTURED_PHOTO_URI_KEY)) {
            mCapturedImageURI = Uri.parse(savedInstanceState.getString(CAPTURED_PHOTO_URI_KEY));
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (mCurrentPhotoPath != null) {
            savedInstanceState.putString(CAPTURED_PHOTO_PATH_KEY, mCurrentPhotoPath);
        }
        if (mCapturedImageURI != null) {
            savedInstanceState.putString(CAPTURED_PHOTO_URI_KEY, mCapturedImageURI.toString());
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public void setCurrentPhotoPath(String mCurrentPhotoPath) {
        this.mCurrentPhotoPath = mCurrentPhotoPath;
    }

    public Uri getCapturedImageURI() {
        return mCapturedImageURI;
    }

    public void setCapturedImageURI(Uri mCapturedImageURI) {
        this.mCapturedImageURI = mCapturedImageURI;
    }



}
