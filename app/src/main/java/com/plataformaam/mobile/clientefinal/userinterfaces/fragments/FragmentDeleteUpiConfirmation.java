package com.plataformaam.mobile.clientefinal.userinterfaces.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.plataformaam.mobile.clientefinal.userinterfaces.listfragments.FragmentUpiList;
import com.plataformaam.mobile.clientefinal.R;
import com.plataformaam.mobile.clientefinal.configurations.MyAppConfig;
import com.plataformaam.mobile.clientefinal.helpers.eventbus.MyMessage;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPI;
import com.plataformaam.mobile.clientefinal.services.MyService;

import de.greenrobot.event.EventBus;


public class FragmentDeleteUpiConfirmation extends Fragment {
    private static final String ARG_UPI = "ARG_UPI";

    private UPI upi;
    private TextView txtUpiTitle;
    private TextView txtUpiContent;
    private View rootView;
    private Button btnBack;
    private Button btnDelete;


    private OnFragmentInteractionListener mListener;
    public static FragmentDeleteUpiConfirmation newInstance(UPI param1) {
        FragmentDeleteUpiConfirmation fragment = new FragmentDeleteUpiConfirmation();
        Bundle args = new Bundle();
        args.putSerializable(ARG_UPI, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentDeleteUpiConfirmation() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            upi = (UPI) getArguments().getSerializable(ARG_UPI);
        }
        EventBus.getDefault().register(FragmentDeleteUpiConfirmation.this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(FragmentDeleteUpiConfirmation.this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_fragment_delete_upi_confirmation, container, false);

        txtUpiTitle = (TextView) rootView.findViewById(R.id.txUpiTitleConfirm);
        txtUpiTitle.setText(upi.getTitle());

        txtUpiContent = (TextView) rootView.findViewById(R.id.txUpiContentConfirm);
        txtUpiContent.setText(upi.getContent());

        btnBack = (Button) rootView.findViewById(R.id.btnCancelUpiDelete);
        btnBack.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                            goBack();
                                       }
                                   }
        );

        btnDelete = (Button) rootView.findViewById(R.id.btnConfirmUpiDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUpi();
            }
        });


        return rootView;
    }


    public void onButtonPressed(UPI upi) {
        if (mListener != null) {
            mListener.onFragmentInteraction(upi);
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
        void onFragmentInteraction(UPI upi);
    }



    public void onEvent(MyMessage message){
        if( message.getSender().equals(MyService.class.getSimpleName())){
            if( message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_SUCCESS)){
                Toast.makeText(getActivity(),"UPI deletada com sucesso",Toast.LENGTH_SHORT).show();
                goBack();
            }
            if( message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_FAIL)){
                Toast.makeText(getActivity(),"Falha na exclusão da UPI",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void goBack(){
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        android.app.Fragment frag  = FragmentUpiList.newInstance();
        fragmentTransaction.replace(R.id.container,frag, null).commit();
    }

    public void deleteUpi(){
        MyMessage message = new MyMessage(FragmentDeleteUpiConfirmation.class.getSimpleName(), MyAppConfig.EVENT_BUS_MESSAGE.DELETE_UPI);
        message.setUpi(upi);
        EventBus.getDefault().post(message);
        btnDelete.setEnabled(false);
    }


}
