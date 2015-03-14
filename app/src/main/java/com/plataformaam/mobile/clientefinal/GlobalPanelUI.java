package com.plataformaam.mobile.clientefinal;

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
import android.widget.Toast;

import com.plataformaam.mobile.clientefinal.models.vcloc.VComComposite;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPI;


public class GlobalPanelUI extends ActionBarActivity
        implements  NavigationDrawerFragment.NavigationDrawerCallbacks,
                    GlobalMyUPIFragment.OnFragmentInteractionListener,
                    GlobalMyVClocFragment.OnFragmentInteractionListener,
                    GlobalNavigateFragment.OnFragmentInteractionListener,
                    GlobalUPIFragmentList.OnFragmentInteractionListener

{


    public static FragmentManager fragmentManager;





    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;


    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_panel_ui);

        //BARRA LATERAL
        mNavigationDrawerFragment = (NavigationDrawerFragment)   getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,(DrawerLayout) findViewById(R.id.drawer_layout));
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        //BARRA LATERAL


        fragmentManager = getSupportFragmentManager();


    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        //FragmentManager fragmentManager = getSupportFragmentManager();
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //android.app.Fragment frag = new GlobalMyUPIFragment();
        android.app.Fragment frag;// = new GlobalUPIFragmentList();

        switch (position){
            case 0:
                frag= new GlobalUPIFragmentList();
                break;
            case 1:
                frag= GlobalMyVClocFragment.newInstance(GlobalMyVClocFragment.MODE.MY_UPI);
                break;
            case 2:
                frag= GlobalMyVClocFragment.newInstance(GlobalMyVClocFragment.MODE.ALL_UPI);
                break;
            case 3:
                frag = new GlobalNavigateFragment();
                break;
            default:
                frag =  GlobalMyVClocFragment.newInstance(GlobalMyVClocFragment.MODE.MY_UPI);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }



    @Override
    public void onFragmentInteraction(UPI upi) {
        if( upi != null ){
            mTitle = upi.getTitle();
            restoreActionBar();
        }
    }

    @Override
    public void onFragmentInteraction(VComComposite composite) {
        if( composite != null ) {
            mTitle = composite.getName();
            restoreActionBar();
        }
    }

    public void goToCreateNewUPI(View view){
        Bundle bundle = new Bundle();
        bundle.putSerializable(CreateUpiUI.SELECTED_UPI, null);

        Intent intent = new Intent(GlobalPanelUI.this  , CreateUpiUI.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
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
            case R.id.popup_item_delete_upi:
                Toast.makeText(getApplicationContext(), "Deletar", Toast.LENGTH_LONG).show();
                return true;

            case R.id.popup_item_publish_upi:

                Toast.makeText(getApplicationContext(), "Publicar", Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }



}
