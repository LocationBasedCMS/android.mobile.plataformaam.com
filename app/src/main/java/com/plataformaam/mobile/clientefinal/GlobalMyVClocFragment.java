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

import com.plataformaam.mobile.clientefinal.adapters.VComCompositeArrayAdapter;
import com.plataformaam.mobile.clientefinal.configurations.MyAppData;
import com.plataformaam.mobile.clientefinal.models.vcloc.VComComposite;
import com.plataformaam.mobile.clientefinal.services.VCLocClientService;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GlobalMyVClocFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GlobalMyVClocFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GlobalMyVClocFragment extends Fragment
        implements AbsListView.OnItemClickListener{

    private OnFragmentInteractionListener mListener;
    private AbsListView mListView;

    VComCompositeArrayAdapter mAdapter;
    int mode;

    public static class MODE{
        public static int MY_UPI = 0;
        public static int ALL_UPI = 1;
    }
    public static GlobalMyVClocFragment newInstance(int mode) {
        GlobalMyVClocFragment fragment = new GlobalMyVClocFragment();
        fragment.mode = mode;
        return fragment;
    }

    public GlobalMyVClocFragment() {
        mode = 0;
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
        view = inflater.inflate(R.layout.fragment_global_my_vcloc, container, false);
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




    public void onButtonPressed(String uri) {
        Log.v(GlobalMyVClocFragment.class.getSimpleName(), "onButtonPressed(String " + uri + ")");
        if (mListener != null) {
            mListener.onFragmentInteraction(null);

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

                VComComposite vComComposite = (VComComposite) parent.getItemAtPosition(position);
                mListener.onFragmentInteraction( vComComposite);

                android.app.FragmentManager fragmentManager = getFragmentManager();
                android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                android.app.Fragment frag  = GlobalNavigateFragment.newInstance( vComComposite );
                fragmentTransaction.replace(R.id.container,frag, null).commit();


            }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(VComComposite composite);
    }


    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();
        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    //SERVICE BIND
    VCLocClientService mService;
    boolean mBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,IBinder service) {
            VCLocClientService.MyBinder binder = (VCLocClientService.MyBinder) service;
            mService = binder.getService();
            if( mService != null ) {
                mBound = true;
                refreshList();


            }
        }

        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    //BIND SERVICE
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



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.popup_menu_my_vcloc_list , menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.popup_item_navigate:
                Toast.makeText(getActivity().getApplicationContext(), "Navegar", Toast.LENGTH_LONG).show();
                return true;
            case R.id.popup_item_subscribe:
                Toast.makeText(getActivity().getApplicationContext(), "Inscrevet", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }




    public void refreshList(){
        Map<Integer,VComComposite> composites;
        Map<Integer,VComComposite> myComposites = mService.getUserController().getMyVComComposite( MyAppData.getAllVComComposite() );

        if( mode== MODE.MY_UPI ) {
            composites = mService.getUserController().getMyVComComposite( MyAppData.getAllVComComposite() );
        }else {
            composites = MyAppData.getAllVComComposite() ;
        }

        List<VComComposite> values = new ArrayList<VComComposite>(composites.values());
        buildList(values,myComposites);
    }

    public void buildList( List<VComComposite> composites,Map<Integer,VComComposite> myComposites){
        mAdapter = new VComCompositeArrayAdapter(
                getActivity(),
                R.layout.row_vcomcomposite_list ,
                composites,
                myComposites
        );

        ListView list = (ListView) view.findViewById(R.id.list_all_vcom_composite);
        registerForContextMenu(list);
        list.setOnItemClickListener(this);
        list.setAdapter(mAdapter);


    }




}
