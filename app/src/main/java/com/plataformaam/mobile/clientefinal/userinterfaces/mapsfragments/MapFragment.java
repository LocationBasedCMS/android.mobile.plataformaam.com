package com.plataformaam.mobile.clientefinal.userinterfaces.mapsfragments;
import de.greenrobot.event.EventBus;
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
import com.plataformaam.mobile.clientefinal.adapters.UPIArrayAdapter;
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
import java.util.Calendar;
import java.util.List;


public class MapFragment extends Fragment
        implements
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener
{
    boolean first_location = true;
    private static GoogleMap mMap;
    private static Marker mMarkerMyLocation;
    private static final String SELECTED_VCOM = MapFragment.class.getCanonicalName();
    private View rootView;
    List<VComUPIPublication> publications;
    private static List<Marker> publicationMarkers = null;


    VComComposite composite;

    private OnFragmentInteractionListener mListener;
    public static MapFragment newInstance(VComComposite param1) {
        if( param1 == null ){
            return newInstance();
        }
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putSerializable(SELECTED_VCOM, param1);
        fragment.setArguments(args);
        return fragment;
    }


    public static MapFragment newInstance() {
        return new MapFragment();
    }


    public MapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            composite = (VComComposite) getArguments().getSerializable(  SELECTED_VCOM );
            AppController.getInstance().setOnlineComposite(composite);
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_map_navigate, container, false);
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
        sendMessage(MyAppConfig.EVENT_BUS_MESSAGE.LOAD_BASE);
        sendMessage(MyAppConfig.EVENT_BUS_MESSAGE.REQUEST_LAST_POSITION);
        sendMessage(MyAppConfig.EVENT_BUS_MESSAGE.LOAD_PUBLICATIONS);

    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpMapIfNeeded(composite);
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
        EventBus.getDefault().register(MapFragment.this);
        first_location = true;


    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(MapFragment.this);
        super.onPause();
    }



    private void setUpMap(VComComposite composite) {
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMapClickListener(this);
        drawComposite(composite);
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
    private void drawComposite(VComComposite composite){
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
                drawBase(base);
            }

        }

    }

    boolean loadVComBaseFail = false;
    private void drawBase(VComBase base){
        if( base != null && !loadVComBaseFail ) {
            if (base.getVirtualSpace() == null) {
                loadVComBaseFail = true;
                sendMessage(MyAppConfig.EVENT_BUS_MESSAGE.RELOAD_BASE);
                Log.i(MyAppConfig.LOG.Activity, "drawBase(VComBase base) fail ->" + base.getName());
            } else {
                PolygonOptions rectOptions = base.getVirtualSpace()
                        .getPolygonOptionsGoogleMapsV2()
                        .fillColor( getResources().getColor(R.color.pam_base_background) )
                        .strokeColor(  getResources().getColor(R.color.pam_base_border) )
                        .strokeWidth(1)
                        ;
                Polygon polyline = mMap.addPolygon(rectOptions);
            }
            if( publications == null ){
                publications = new ArrayList<>();
            }
            List<VComUPIPublication> list = base.getPublications();
            if( list != null ){
                for(VComUPIPublication item : list) {
                    publications.add(item);
                }
            }
        }

    }

    public void drawPublications(){
        if (publications != null) {
            //Limpa os marcadores
            if( publicationMarkers == null ){
                publicationMarkers = new ArrayList<>();
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
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_text_message)
                                )
                );
                publicationMarkers.add(marker);
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


    public VComComposite isInsideOfComposite(LatLng point){
        if( composite != null ){
            if( composite.isInside(point)){
                return composite;
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
                Toast.makeText(getActivity(), "INSIDE OF COMPOSITE " + this.composite.getName() + "  -> " + point, Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getActivity(), "OUTSIDE of COMPOSITE : " + point, Toast.LENGTH_LONG).show();
        }
    }

    private void selectRule(final LatLng position,final VComBase base){
        List<UPIAggregationRuleStart> publishRules = base.getPublishRules();
        PublishRuleDialogArrayAdapter adapter = new PublishRuleDialogArrayAdapter(getActivity(),R.layout.dialog_row_publish_rule,publishRules);
        AlertDialog.Builder builder = new AlertDialog.Builder(
                getActivity())
                .setTitle(R.string.dialogSelectPublishRule)
                .setAdapter(
                        adapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectUpi(position, base, base.getPublishRules().get(which));
                            }
                        }
                )
                .setCancelable(true);
        builder.show();
    }

    private void selectUpi(final LatLng position,final VComBase base,final UPIAggregationRuleStart aPublishRule){
        final List<UPI> upis = AppController.getInstance().getOnlineUser().getUpis();
        List<UPI> filteredUpis = new ArrayList<>();
        if( AppController.getInstance().getAllPublicationRules() != null ) {
            final UPIAggregationRuleStart publishRule = AppController.getInstance().getAllPublicationRules().get(aPublishRule.getId());
            for(int i = 0; i < upis.size(); i++ ){
                UPI test = upis.get(i);
                if( test.getUpiType() != null && publishRule.getUpiType() != null && publishRule.getUpiType().getId() == test.getUpiType().getId() ){
                    filteredUpis.add(test);
                }
            }


            UPIArrayAdapter adapter = new UPIArrayAdapter(
                    getActivity(),
                    R.layout.row_upi_text_list,
                    R.layout.row_upi_image_list,
                    filteredUpis
            );
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    getActivity())
                    .setTitle("Selecione a Produção ")
                    .setAdapter(
                            adapter,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    UPI upi = upis.get(which);
                                    makePublish(position, base, publishRule, upi);
                                }
                            }
                    ).setCancelable(true);
            builder.show();
        }
    }


    private void makePublish(LatLng position,VComBase base,UPIAggregationRuleStart publishRule,UPI upi){
        User user = AppController.getInstance().getOnlineUser();
        Calendar c  = Calendar.getInstance();
        java.util.Date date = c.getTime();
        VComUPIPublication publication = new VComUPIPublication(
                upi,
                user,
                base,
                publishRule,
                position
        );
        publication.setCurrentTime(date);
        sendPublishMessage(position, base, publishRule, upi, publication);
    }



    //TODO - Mensagem de erro quando a publicação falah
    //TODO Botão para visualização de detalhes da publicação
    //TODO Botão para fechar a visualização da publicação
    //TODO Visualização da Resposta




    ////////////////////////////////////////////////////////////////////////////////////////////
    //  Event Bus : Dispara os Metodos
    ////////////////////////////////////////////////////////////////////////////////////////
    public void onEvent(MyMessage message){
        if( message.getSender().equals(MyVComService.class.getSimpleName())){
            if( message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.BASE_LOADED_SUCCESS) ){
                Log.i(MyAppConfig.LOG.Application,message.getMessage()+ " Disparando -> drawComposite(composite);  " );
                loadVComBaseFail = false;
                drawComposite(composite);
            }
            if( message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.PUBLISH_UPI_FAIL)){
                Toast.makeText(getActivity()," Falha ao publicar. ",Toast.LENGTH_SHORT ).show();
                //TODO - Falha na Publicação
            }
        }
    }

    public void onEvent(MyPositionMessage message){
        if( message.getSender().equals(MyLocationService.class.getSimpleName())){
            if( message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.LOCATION_CHANGE) ){
                if(message.getUserPosition() != null ){
                    drawAvatar(message.getUserPosition());

                }
            }

        }




    }

    public void onEvent(MyPublishMessage message){
        if( message.getSender().equals(MyVComService.class.getSimpleName())){
            if(message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.PUBLICATIONS_RELOADED)){
                if( message.getPublications() != null ){
                    publications = message.getPublications();
                    drawPublications();
                }
            }

            if( message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.BASE_LOADED_SUCCESS)){
                drawComposite(this.composite);
            }
        }
    }

    public void sendMessage(final String messageCode){
        if( messageCode.equals(MyAppConfig.EVENT_BUS_MESSAGE.REQUEST_LAST_POSITION)){
            MyPositionMessage message = new MyPositionMessage(MapFragment.class.getSimpleName(),messageCode);
            EventBus.getDefault().post(message);
        }
        if( messageCode.equals(MyAppConfig.EVENT_BUS_MESSAGE.LOAD_PUBLICATIONS)) {
            MyMessage message = new MyMessage(MapFragment.class.getSimpleName(), MyAppConfig.EVENT_BUS_MESSAGE.LOAD_PUBLICATIONS);
            EventBus.getDefault().post(message);
        }
        if( messageCode.equals(MyAppConfig.EVENT_BUS_MESSAGE.RELOAD_BASE)) {
            MyMessage message = new MyMessage(MapFragment.class.getSimpleName(), MyAppConfig.EVENT_BUS_MESSAGE.LOAD_BASE);
            EventBus.getDefault().post(message);
        }
    }

    private void sendPublishMessage(LatLng position, VComBase base, UPIAggregationRuleStart publishRule, UPI upi, VComUPIPublication publication) {
        MyPublishMessage message =  new MyPublishMessage(MapFragment.class.getSimpleName(), MyAppConfig.EVENT_BUS_MESSAGE.PUBLISH_UPI,publication);
        message.setUpi(upi);
        message.setBase(base);
        message.setPublishRule(publishRule);
        message.setPosition(position);
        EventBus.getDefault().post(message);
    }

















}