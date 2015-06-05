package com.plataformaam.mobile.clientefinal.userinterfaces.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.plataformaam.mobile.clientefinal.AppController;
import com.plataformaam.mobile.clientefinal.R;
import com.plataformaam.mobile.clientefinal.configurations.MyAppConfig;
import com.plataformaam.mobile.clientefinal.helpers.eventbus.MyMessage;
import com.plataformaam.mobile.clientefinal.models.User;
import com.plataformaam.mobile.clientefinal.models.location.UserPosition;
import com.plataformaam.mobile.clientefinal.models.vcloc.roles.VComUPIAggregationRuleResponseOf;
import com.plataformaam.mobile.clientefinal.models.vcloc.roles.VComUPIAggregationRuleStart;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPI;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.VComUPIPublication;
import com.plataformaam.mobile.clientefinal.services.MyService;
import com.plataformaam.mobile.clientefinal.userinterfaces.listfragments.FragmentUpiList;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentEditUpi.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentEditUpi#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentEditUpi extends Fragment {
    private static final String ARG_UPI                 = "UPI";
    private static final String ARG_PUBLICATION         = "PUBLICATION";
    private static final String ARG_PUBLICATION_RULE    = "PUBLICATION_RULE";
    private static final String ARG_RESPONSE_RULE       = "RESPONSE_RULE";
    private static final String ARG_LOCATION            =  "USER_POSITION";

    private UPI upi;
    private VComUPIPublication publication;
    private VComUPIAggregationRuleStart publicationRule;
    private VComUPIAggregationRuleResponseOf responseRule;
    private UserPosition    userPosition;

    //Elementos de Interface
    EditText etxTitle;
    EditText etxContent;
    Button btnSave;
    Button btnPublish;


    private OnFragmentInteractionListener mListener;

    //CREATE UPI
    public static FragmentEditUpi newInstance(){
        FragmentEditUpi fragment = new FragmentEditUpi();
        return fragment;
    }

    //UPI EDIT
    public static FragmentEditUpi newInstance(UPI upi){
        FragmentEditUpi fragment = new FragmentEditUpi();
        Bundle args = new Bundle();
        args.putSerializable(ARG_UPI, upi);
        fragment.setArguments(args);
        return fragment;
    }

    //UPI RESPONSE
    public static FragmentEditUpi newInstance(UPI upi,VComUPIPublication publication,VComUPIAggregationRuleResponseOf responseRule) {
        FragmentEditUpi fragment = new FragmentEditUpi();
        Bundle args = new Bundle();
        args.putSerializable(ARG_UPI, upi);
        args.putSerializable(ARG_PUBLICATION, publication);
        args.putSerializable(ARG_RESPONSE_RULE, responseRule);
        fragment.setArguments(args);
        return fragment;
    }


    //UPI PUBLICATION
    public static FragmentEditUpi newInstance(UPI upi,VComUPIAggregationRuleStart publicationRule,UserPosition position) {

        FragmentEditUpi fragment = new FragmentEditUpi();
        Bundle args = new Bundle();
        args.putSerializable(ARG_UPI, upi);
        args.putSerializable(ARG_PUBLICATION_RULE, publicationRule);
        args.putSerializable(ARG_LOCATION, position);
        fragment.setArguments(args);
        return fragment;

    }




    public static FragmentEditUpi newInstance(String param1, String param2) {
        FragmentEditUpi fragment = new FragmentEditUpi();
        Bundle args = new Bundle();
        args.putString(ARG_UPI, param1);
        args.putString(ARG_PUBLICATION, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentEditUpi() {
        // Required empty public constructor
    }


    public void onButtonPressed(UPI uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_edit_upi, container, false);

        etxTitle    = (EditText)    rootView.findViewById(R.id.etxCreateUPIText);
        etxContent  = (EditText)    rootView.findViewById(R.id.etxContentCreateNewUPIText);
        btnSave     = (Button)      rootView.findViewById(R.id.btnCreateNewUPIText);
        btnPublish  = (Button)      rootView.findViewById(R.id.btnPublishUPI);
        loadUpi();

        //Criando os Listener
        btnSave.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUPI(v);
            }
        });

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishUPI(v);
            }
        });

        return rootView;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.upi                = (UPI) getArguments().getSerializable(ARG_UPI);
            this.publication        = (VComUPIPublication) getArguments().getSerializable(ARG_PUBLICATION);
            this.publicationRule    = (VComUPIAggregationRuleStart) getArguments().getSerializable(ARG_PUBLICATION_RULE);
            this.responseRule       = (VComUPIAggregationRuleResponseOf)getArguments().getSerializable(ARG_RESPONSE_RULE);
            this.userPosition       = (UserPosition) getArguments().getSerializable(ARG_LOCATION );
        }
        EventBus.getDefault().register(FragmentEditUpi.this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(FragmentEditUpi.this);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(UPI upi);
    }


    public void loadUpi(){
        //CRIA AS UPI
        if(this.upi == null ){
            this.upi = new UPI();
            this.upi.setUpiType(MyAppConfig.generateUpiType(MyAppConfig.UPI_TYPE_CODE.UPI_TEXT));
        }
        etxTitle.setText(upi.getTitle());
        etxContent.setText(upi.getContent());
    }

    public boolean recoveryUpi(){
        upi.setTitle( etxTitle.getText().toString());
        upi.setContent( etxContent.getText().toString());
        upi.setUser(AppController.getInstance().getOnlineUser());
        return !upi.getTitle().isEmpty();
    }


    public void saveUPI(View v){
        changeButtonState(false);
        Toast.makeText(getActivity(),"Salvando ... ",Toast.LENGTH_SHORT);
        recoveryUpi();
        MyMessage message = new MyMessage(FragmentEditUpi.class.getSimpleName(), MyAppConfig.EVENT_BUS_MESSAGE.SAVE_UPI);
        message.setUpi(upi);
        message.setUser(AppController.getInstance().getOnlineUser());
        EventBus.getDefault().post(message);

    }

    private void changeButtonState(boolean enable){
        this.btnSave.setEnabled(enable);
        this.btnPublish.setEnabled(enable);
    }

    public void publishUPI(View v){
        recoveryUpi();


    }

    public void onEvent(MyMessage message){
        if( message.getSender().equals(MyService.class.getSimpleName())){
            if( message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_SUCCESS)){
                Toast.makeText(getActivity(), getString(R.string.operation_upi_save_success) ,Toast.LENGTH_LONG).show();
                UPI savedUpi = message.getUpi();

                if( savedUpi != null && AppController.getInstance().getOnlineUser() != null ){
                    this.upi = savedUpi;
                    if( AppController.getInstance().getOnlineUser().getUpis() != null ) {
                        AppController.getInstance().getOnlineUser().getUpis().add(savedUpi);
                    }else{
                        List<UPI> upis = new ArrayList<>();
                        upis.add(savedUpi);
                        AppController.getInstance().getOnlineUser().setUpis(upis);
                    }
                }
                goToUpiList();

            }
            if( message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_FAIL)){
                Toast.makeText(getActivity(),getString(R.string.operation_upi_save_fail) ,Toast.LENGTH_LONG).show();
                changeButtonState(true);
            }

        }
    }


    public void goToUpiList(){
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        android.app.Fragment frag  = FragmentUpiList.newInstance();
        fragmentTransaction.replace(R.id.container,frag, null).commit();
    }


}
