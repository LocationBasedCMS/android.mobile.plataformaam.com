package com.plataformaam.mobile.clientefinal.userinterfaces.mapsfragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.plataformaam.mobile.clientefinal.AppController;
import com.plataformaam.mobile.clientefinal.R;
import com.plataformaam.mobile.clientefinal.adapters.PublishRuleDialogArrayAdapter;
import com.plataformaam.mobile.clientefinal.adapters.UPITextArrayAdapter;
import com.plataformaam.mobile.clientefinal.configurations.MyAppConfig;
import com.plataformaam.mobile.clientefinal.helpers.eventbus.MyMessage;
import com.plataformaam.mobile.clientefinal.helpers.eventbus.MyPositionMessage;
import com.plataformaam.mobile.clientefinal.helpers.eventbus.MyPublishMessage;
import com.plataformaam.mobile.clientefinal.models.User;
import com.plataformaam.mobile.clientefinal.models.location.UserPosition;
import com.plataformaam.mobile.clientefinal.models.vcloc.VComBase;
import com.plataformaam.mobile.clientefinal.models.vcloc.VComComposite;
import com.plataformaam.mobile.clientefinal.models.vcloc.rules.UPIAggregationRuleStart;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPI;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.VComUPIPublication;
import com.plataformaam.mobile.clientefinal.services.MyLocationService;
import com.plataformaam.mobile.clientefinal.services.MyVComService;
import com.plataformaam.mobile.clientefinal.userinterfaces.GlobalPanelUI;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class GlobalNavigateFragment extends Fragment
        implements
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener
{
    boolean first_location = true;
    private static GoogleMap mMap;
    private static Marker mMarkerMyLocation;
    private static final String SELECTED_VCOM = GlobalNavigateFragment.class.getCanonicalName();
    private View rootView;


    VComComposite mVcomComposite;
    int mVcomCompositePosition;



    private OnFragmentInteractionListener mListener;
    public static GlobalNavigateFragment newInstance(VComComposite param1) {
        if( param1 == null ){
            return newInstance();
        }
        GlobalNavigateFragment fragment = new GlobalNavigateFragment();
        Bundle args = new Bundle();
        args.putSerializable(SELECTED_VCOM, param1);
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
            mVcomComposite = (VComComposite) getArguments().getSerializable(  SELECTED_VCOM );
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_global_navigate, container, false);
        return rootView;
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
        void onFragmentInteraction(Uri uri);
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
            android.support.v4.app.FragmentManager manager =GlobalPanelUI.fragmentManager;
            if( manager != null ) {
                manager.beginTransaction().remove(GlobalPanelUI.fragmentManager.findFragmentById(R.id.navigate_map)).commit();
            }
            mMap = null;
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(GlobalNavigateFragment.this);
        first_location = true;
        MyPublishMessage message = new MyPublishMessage(GlobalNavigateFragment.class.getSimpleName(), MyAppConfig.EVENT_BUS_MESSAGE.RELOAD_PUBLICATIONS);

    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(GlobalNavigateFragment.this);
        super.onPause();
    }



    private void setUpMap(VComComposite composite) {

        //mMap.setMyLocationEnabled(true);
        drawVComComposite(composite);
        mMap.setOnMapLongClickListener( this );
        mMap.setOnMapClickListener(this);

    }


    private static List<Marker> publicationMarkers = null;
    public void drawPublication(List<VComUPIPublication> publications){
        //Limpa os marcadores
        if( publicationMarkers == null ){
            publicationMarkers = new ArrayList<Marker>();
        }else{
            for(Marker marker : publicationMarkers){
                marker.remove();
            }
        }
        for(VComUPIPublication publication : publications){
            Marker marker = mMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(publication.getLatitude(), publication.getLongitude()))
                            .title(publication.getUpi().getTitle())
                            .snippet(publication.getUpi().getContent())
                            .icon( BitmapDescriptorFactory.fromResource(R.mipmap.ic_text_message )
                    )
            );
            publicationMarkers.add(marker);
        }
    }


    private void drawAvatar(UserPosition userPosition){
        if( userPosition != null ) {
            if (first_location) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userPosition.getLatitude(), userPosition.getLongitude()), 15.0f));
            }
            first_location = (mMarkerMyLocation == null);
            if (!first_location) {
                mMarkerMyLocation.remove();
            }
            mMarkerMyLocation = mMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(userPosition.getLatitude(), userPosition.getLongitude()))
                            .title("Eu")
                            .snippet("Eu estou Aqui ! ")
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)
                            )
            );
        }
    }

    Polygon polylineComposite = null;
    private void drawVComComposite(VComComposite composite){
        if( composite != null         ) {
            if( polylineComposite != null  ){
                polylineComposite.remove();
            }
            PolygonOptions rectOptions = composite.getVirtualSpace()
                    .getPolygonOptionsGoogleMapsV2()
                    .fillColor(getResources().getColor(R.color.pam_composite_background))
                    .strokeColor(getResources().getColor(R.color.pam_composite_border))
                    .strokeWidth(1)
                    ;
            polylineComposite = mMap.addPolygon(rectOptions);
            for(VComBase base : composite.getvComBases()){
                drawVComBase(base);
            }
        }


    }

    boolean loadVComBaseFail = false;
    private void drawVComBase(VComBase base){
        if( base != null && !loadVComBaseFail ) {
            if (base.getVirtualSpace() == null) {
                loadVComBaseFail = true;
                MyMessage message = new MyMessage(GlobalNavigateFragment.class.getSimpleName(), MyAppConfig.EVENT_BUS_MESSAGE.RELOAD_BASE);
                EventBus.getDefault().post(message);
                Log.i(MyAppConfig.LOG.Activity, "drawVComBase(VComBase base) fail ->" + base.getName());
                return;
            } else {
                PolygonOptions rectOptions = base.getVirtualSpace()
                        .getPolygonOptionsGoogleMapsV2()
                        .fillColor( getResources().getColor(R.color.pam_base_background) )
                        .strokeColor(  getResources().getColor(R.color.pam_base_border) )
                        .strokeWidth(1)
                        ;
                Polygon polyline = mMap.addPolygon(rectOptions);

            }
        }

    }


    @Override
    public void onMapLongClick(LatLng point) {
        publishEvent(point);
    }

    @Override
    public void onMapClick(LatLng point) {
        Toast.makeText(getActivity(),"Clicked in"+point,Toast.LENGTH_LONG).show();
    }


    public void onEvent(MyMessage message){
        return;
    }

    public void onEvent(MyPositionMessage message){
        if( message.getSender().equals(MyLocationService.class.getSimpleName())){
            if( message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.LOCATION_CHANGE) ){
                if(message.getUserPosition() != null ){
                    drawAvatar(message.getUserPosition());

                }
            }

        }

        if( message.getSender().equals(MyVComService.class.getSimpleName())){
            if( message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.LOAD_BASE) ){
                Log.i(MyAppConfig.LOG.Application,message.getMessage()+ " Disparando -> drawVComComposite(mVcomComposite);  " );
                loadVComBaseFail = false;
                drawVComComposite(mVcomComposite);
            }
        }


    }

    public void onEvent(MyPublishMessage message){
        if( message.getSender().equals(MyVComService.class.getSimpleName())){
            if(message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.RELOAD_PUBLICATIONS)){
                if( message.getPublications() != null ){
                    drawPublication(message.getPublications());
                }
            }
        }
    }


    public VComComposite isInsideOfComposite(LatLng point){
        if( mVcomComposite != null ){
            if( mVcomComposite.isInside(point)){
                return mVcomComposite;
            }else{
                return null;
            }

        }else {
            return null;
        }
    }


    public VComBase isInsideOfBase( LatLng point,List<VComBase> bases ){
        VComBase insideBase = null;
        for(VComBase base: bases ){
            if( base.isInside(point)){
                insideBase = base;
            }
        }
        return insideBase;
    }



    public void publishEvent(LatLng point){
        VComComposite composite =isInsideOfComposite(point);
        if( composite  != null ){
            VComBase insideBase = isInsideOfBase(point,composite.getvComBases());
            if( insideBase != null){
                Toast.makeText(getActivity(), "INSIDE OF BASE " + insideBase.getName() + "  -> " + point, Toast.LENGTH_LONG).show();
                selectRule(point, insideBase);
            }else {
                Toast.makeText(getActivity(), "INSIDE OF COMPOSITE " + mVcomComposite.getName() + "  -> " + point, Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getActivity(), "OUTSIDE of COMPOSITE : " + point, Toast.LENGTH_LONG).show();
        }
    }

    private void selectRule(final LatLng position,final VComBase base){
        List<UPIAggregationRuleStart> publishRules = base.getPublishRules();
        PublishRuleDialogArrayAdapter adapter = new PublishRuleDialogArrayAdapter(getActivity(),R.layout.dialog_row_publish_rule,publishRules);

        //TODO xml
        AlertDialog.Builder builder = new AlertDialog.Builder(
                getActivity())
                .setTitle("Selecione a Regra de Publicação")
                .setAdapter(
                        adapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectUpi(position,base,base.getPublishRules().get(which) );
                            }
                        }
                )
                .setCancelable(true);
        builder.show();
    }

    private void selectUpi(final LatLng position,final VComBase base,final UPIAggregationRuleStart publishRule){
        final List<UPI> upis = AppController.getInstance().getOnlineUser().getUpis();
        UPITextArrayAdapter adapter = new UPITextArrayAdapter(getActivity(),R.layout.row_upitext_list,upis);
        AlertDialog.Builder builder = new AlertDialog.Builder(
                getActivity())
                .setTitle("Selecione a Produção ")
                .setAdapter(
                        adapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UPI upi = upis.get(which);
                                makePublish(position,base,publishRule,upi);
                            }
                        }
                )
                .setCancelable(true);
        builder.show();
    }


    private void makePublish(LatLng position,VComBase base,UPIAggregationRuleStart publishRule,UPI upi){
        User user = AppController.getInstance().getOnlineUser();
        VComUPIPublication publication = new VComUPIPublication(
                upi,
                user,
                base,
                publishRule,
                position
        );
        MyPublishMessage message =  new MyPublishMessage(GlobalNavigateFragment.class.getSimpleName(), MyAppConfig.EVENT_BUS_MESSAGE.PUBLISH_UPI,publication);
        message.setUpi(upi);
        message.setBase(base);
        message.setPublishRule(publishRule);
        message.setPosition(position);
        EventBus.getDefault().post(message);
    }



}