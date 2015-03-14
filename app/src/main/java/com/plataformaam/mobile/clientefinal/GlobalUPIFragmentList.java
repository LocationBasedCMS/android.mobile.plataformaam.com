package com.plataformaam.mobile.clientefinal;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
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

import com.plataformaam.mobile.clientefinal.adapters.UPITextArrayAdapter;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPI;
import com.plataformaam.mobile.clientefinal.services.VCLocClientService;

import java.util.ArrayList;
import java.util.List;


public class GlobalUPIFragmentList extends Fragment
        implements AbsListView.OnItemClickListener
{
    private OnFragmentInteractionListener mListener;
    UPITextArrayAdapter  mAdapter;
    UPI selected_upi = null;
    AbsListView listView;

    public static GlobalUPIFragmentList newInstance(String param1, String param2) {
        GlobalUPIFragmentList fragment = new GlobalUPIFragmentList();
        return fragment;
    }

    public GlobalUPIFragmentList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        doBindService();
        super.onResume();
    }

    @Override
    public void onPause() {
        doUnbindService();
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_globalupi_list, container, false);
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            selected_upi = (UPI) parent.getItemAtPosition(position);
            mListener.onFragmentInteraction(selected_upi);
            //TODO : Implementarir para edição de UPIS - Aqui que abre o formulário
        }
    }

    public void setEmptyText(CharSequence emptyText) {
        View emptyView = listView.getEmptyView();
        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(UPI upi);
    }


    void doBindService(){
        Intent intent = new Intent( getActivity() , VCLocClientService.class);
        getActivity().bindService(
                intent,
                mConnection,
                Context.BIND_AUTO_CREATE);
    }

    void doUnbindService() {
        if (mBound) {
            mBound = false;
            getActivity().unbindService(mConnection);
            mService = null;

        }
    }

    //SERVICE BIND
    VCLocClientService mService;
    boolean mBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,IBinder service) {
            VCLocClientService.MyBinder binder = (VCLocClientService.MyBinder) service;
            mService = binder.getService();

            if( mService != null ){
                mBound = true;

                if( mService.getUser() != null  ) {
                    buildList(mService.getUser().getUpis());
                    //refreshUpiList(mService.getUser().getUpis());
                } else {
                    Log.e(GlobalUPIFragmentList.class.getSimpleName(),"Serviço retornou usuário null.");
                }

            }

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };



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

            case R.id.popup_item_edit_upi:
                goToEditUpiUI(selected_upi);
                return true;


            case R.id.popup_item_delete_upi:
                Toast.makeText(getActivity().getApplicationContext(), "Deletar", Toast.LENGTH_LONG).show();

                return true;

            case R.id.popup_item_publish_upi:
                Toast.makeText(getActivity().getApplicationContext(), "Publicar", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    public void goToCreateUpiUI(){
        Intent intent = new Intent(getActivity(),CreateUpiUI.class);
        startActivity(intent);
    }

    public void goToEditUpiUI(UPI upi){
        Intent intent = new Intent(getActivity(),CreateUpiUI.class);
        if( selected_upi != null ){
            Bundle bundle = new Bundle();
            bundle.putSerializable(CreateUpiUI.SELECTED_UPI, null);
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    //REFRESH LIST VIEW
    void refreshUpiList( List<UPI> upis){
        //mAdapter.addAll(upis);
        mAdapter.clear();
        mAdapter.addAll(upis);
        //mAdapter.getData().addAll(upis);
        mAdapter.notifyDataSetChanged();
    }


    private void buildList(List<UPI> upis){
        mAdapter = new UPITextArrayAdapter(
                getActivity(),
                R.layout.row_upitext_list ,
                upis
        );


        ListView list = (ListView) view.findViewById(R.id.grid_upi);
        registerForContextMenu(list);
        list.setOnItemClickListener(this);
        list.setAdapter(mAdapter);

    }
}
