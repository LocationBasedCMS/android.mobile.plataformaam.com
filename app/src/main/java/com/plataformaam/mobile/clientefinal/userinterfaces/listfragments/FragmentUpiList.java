package com.plataformaam.mobile.clientefinal.userinterfaces.listfragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.plataformaam.mobile.clientefinal.AppController;
import com.plataformaam.mobile.clientefinal.R;
import com.plataformaam.mobile.clientefinal.adapters.UPIArrayAdapter;
import com.plataformaam.mobile.clientefinal.configurations.MyAppConfig;
import com.plataformaam.mobile.clientefinal.helpers.eventbus.MyMessage;
import com.plataformaam.mobile.clientefinal.models.User;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPI;
import com.plataformaam.mobile.clientefinal.services.MyService;
import com.plataformaam.mobile.clientefinal.userinterfaces.fragments.FragmentDeleteUpiConfirmation;
import com.plataformaam.mobile.clientefinal.userinterfaces.fragments.FragmentEditImageUpi;
import com.plataformaam.mobile.clientefinal.userinterfaces.fragments.FragmentEditUpi;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * A fragment representing a vcom_list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class FragmentUpiList extends Fragment implements AbsListView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;
    UPIArrayAdapter mAdapter;
    UPI selected_upi = null;
    AbsListView listView;
    private AbsListView mListView;
    View view;


    public static FragmentUpiList newInstance() {
        return new FragmentUpiList();
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentUpiList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(FragmentUpiList.this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(FragmentUpiList.this);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        List<UPI> upis = null;
        if( AppController.getInstance() != null && AppController.getInstance().getOnlineUser() != null ) {
            User user = AppController.getInstance().getOnlineUser();
            upis = user.getUpis();
        }
        if (upis == null) {
            upis = new ArrayList<UPI>();
        }
        buildList(upis);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upi, container, false);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();
        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(UPI upi);
    }

    ///////////////////////////////////////////////////////////////////
    //  MENU
    /////////////////////////////////////////////////////////////////

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.popup_menu_upi_list , menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        selected_upi = mAdapter.getData().get(info.position);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {

            case R.id.popup_item_create_upi:
                goToCreateUpiUI();
                return true;

            case R.id.popup_item_create_image_upi:
                goToCreateImageUpiUI();
                return true;


            case R.id.popup_item_edit_upi:
                goToEditUpiUI(selected_upi);
                return true;


            case R.id.popup_item_delete_upi:
                deleteUpi(selected_upi);
                return true;

            case R.id.popup_item_publish_upi:
                Toast.makeText(getActivity().getApplicationContext(), "Publicar", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void goToCreateImageUpiUI(){
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        android.app.Fragment frag  = FragmentEditImageUpi.newInstance();
        fragmentTransaction.replace(R.id.container,frag, null).commit();
    }



    //REFRESH LIST VIEW
    void refreshUpiList( List<UPI> upis){
        //mAdapter.clear();
        //mAdapter.addAll(upis);
        mAdapter.notifyDataSetChanged();
    }


    private void buildList(List<UPI> upis){
        mAdapter = new UPIArrayAdapter(
                getActivity(),
                R.layout.row_upi_text_list,
                R.layout.row_upi_image_list,
                upis);


        ListView list = (ListView) view.findViewById(R.id.upi_list);
        registerForContextMenu(list);
        list.setOnItemClickListener(this);
        list.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            selected_upi = (UPI) parent.getItemAtPosition(position);
            mListener.onFragmentInteraction(selected_upi);
        }
    }

    //NAVEGACAO
    public void goToCreateUpiUI(){
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        android.app.Fragment frag  = FragmentEditUpi.newInstance();
        fragmentTransaction.replace(R.id.container,frag, null).commit();
    }




    public void goToEditUpiUI(UPI upi){
        mListener.onFragmentInteraction( upi);
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        android.app.Fragment frag  = FragmentEditUpi.newInstance(upi);
        fragmentTransaction.replace(R.id.container,frag, null).commit();
    }



    public void deleteUpi(UPI upi){
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        android.app.Fragment frag  = FragmentDeleteUpiConfirmation.newInstance(upi);
        fragmentTransaction.replace(R.id.container,frag, null).commit();
    }


    //////////////////////////////////////////////////////////////////////////////////////////////
    //  Event Bus
    ////////////////////////////////////////////////////////////////////////////////////////////

    public void onEvent(MyMessage message){
        if( message.getSender().equals(MyService.class.getSimpleName()) && message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.UPI_RELOADED_SUCCESS)   ){
            AppController app = AppController.getInstance();
            if( app != null && app.getOnlineUser() != null ) {
                List<UPI> upis = AppController.getInstance().getOnlineUser().getUpis();
                if (upis != null) {
                    refreshUpiList(upis);
                }
            }
        }
    }

}
