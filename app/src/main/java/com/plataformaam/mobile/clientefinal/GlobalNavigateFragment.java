package com.plataformaam.mobile.clientefinal;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.plataformaam.mobile.clientefinal.configurations.MyAppData;
import com.plataformaam.mobile.clientefinal.exceptions.NoUserPositionException;
import com.plataformaam.mobile.clientefinal.models.location.UserPosition;
import com.plataformaam.mobile.clientefinal.models.vcloc.VComComposite;
import com.plataformaam.mobile.clientefinal.services.VCLocClientService;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GlobalNavigateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GlobalNavigateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GlobalNavigateFragment extends Fragment
implements
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener
    {

    private static GoogleMap mMap;
    private static Marker mLocation;
    private static final String SELECTED_VCOM = GlobalNavigateFragment.class.getCanonicalName();
    VComComposite mVcomComposite;



    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment GlobalNavigateFragment.
     */

    public static GlobalNavigateFragment newInstance(VComComposite param1) {
        if( param1 == null ){
            return newInstance();
        }
        GlobalNavigateFragment fragment = new GlobalNavigateFragment();
        Bundle args = new Bundle();
        args.putSerializable(SELECTED_VCOM, (VComComposite) param1);
        fragment.setArguments(args);
        return fragment;
    }

    public static GlobalNavigateFragment newInstance() {
        GlobalNavigateFragment fragment = new GlobalNavigateFragment();
        return fragment;
    }

    public GlobalNavigateFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVcomComposite = (VComComposite) getArguments().getSerializable(  SELECTED_VCOM);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_global_navigate, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }



    public void setUpMapIfNeeded(VComComposite composite) {
        if (mMap == null) {
            mMap = ((SupportMapFragment) GlobalPanelUI.fragmentManager.findFragmentById(R.id.navigate_map)).getMap();
            if (mMap != null)
                setUpMap(composite);
        }
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpMapIfNeeded(mVcomComposite);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mMap != null) {
            GlobalPanelUI.fragmentManager.beginTransaction().remove(GlobalPanelUI.fragmentManager.findFragmentById(R.id.navigate_map)).commit();
            mMap = null;
        }
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


    //SERVICE BIND
    VCLocClientService mService;
    boolean mBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            VCLocClientService.MyBinder binder = (VCLocClientService.MyBinder) service;
            mService = binder.getService();
            if (mService != null) {
                mBound = true;
                callbackInterface();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    void doBindService() {
        Intent intent = new Intent( getActivity() , VCLocClientService.class);
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    void doUnbindService() {
        if (mBound) {
            mBound = false;
            getActivity().unbindService(mConnection);
            mService = null;
        }
    }


    public void callbackInterface() {
        UserPosition userPosition = mService.getUserController().getLastPosition();
        mLocation = mMap.addMarker(           new MarkerOptions()
                .position(new LatLng(userPosition.getLatitude(), userPosition.getLongitude()))
                .title("Eu")
                .snippet("Eu estou Aqui ! ")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_penguim)));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userPosition.getLatitude(), userPosition.getLongitude()), 15.0f));

    }

    private void setUpMap(VComComposite composite) {
        MyAppData myAppData = MyAppData.getInstance();
        mMap.setMyLocationEnabled(true);

        drawVComComposite(composite);

        mMap.setOnMapLongClickListener( this );
        mMap.setOnMapClickListener(this);

    }

    private void drawVComComposite(VComComposite composite){
        if( composite != null         ) {
            PolygonOptions rectOptions = composite.getVirtualSpace()
                    .getPolygonOptionsGoogleMapsV2()
                        .fillColor(0xAADDDDFF )
                        .strokeColor(0xDD3333FF)
                        .strokeWidth(1)
                    ;

            Polygon polyline = mMap.addPolygon(rectOptions);
        }
    }

        @Override
        public void onMapLongClick(LatLng point) {
            Toast.makeText(getActivity(),"Long Clicked in"+point,Toast.LENGTH_LONG).show();
            //TODO Disparar evento de publicação
        }

        @Override
        public void onMapClick(LatLng point) {
            Toast.makeText(getActivity(),"Clicked in"+point,Toast.LENGTH_LONG).show();
        }





}
