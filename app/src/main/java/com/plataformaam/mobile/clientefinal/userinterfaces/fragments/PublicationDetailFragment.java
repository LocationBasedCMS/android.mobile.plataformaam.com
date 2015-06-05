package com.plataformaam.mobile.clientefinal.userinterfaces.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plataformaam.mobile.clientefinal.R;
import com.plataformaam.mobile.clientefinal.models.vcloc.rules.UPIAggregationRuleResponseOf;
import com.plataformaam.mobile.clientefinal.models.vcloc.rules.UPIAggregationRuleStart;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.VComUPIPublication;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PublicationDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PublicationDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublicationDetailFragment extends Fragment {

    private static final String PUBLICATION         = "PUBLICATION";
    private static final String PUBLICATION_RULE    = "PUBLICATION_RULE";

    private VComUPIPublication publication;
    private UPIAggregationRuleStart publicationRule;
    private List<UPIAggregationRuleResponseOf> responseRules;



    private OnFragmentInteractionListener mListener;

    public static PublicationDetailFragment newInstance(VComUPIPublication publication,UPIAggregationRuleStart publicationRule) {
        PublicationDetailFragment fragment = new PublicationDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(PUBLICATION, publication);
        args.putSerializable(PUBLICATION_RULE ,publicationRule);
        fragment.setArguments(args);
        return fragment;

    }

    public PublicationDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.publication = (VComUPIPublication) getArguments().getSerializable(PUBLICATION);
            this.publicationRule = (UPIAggregationRuleStart) getArguments().getSerializable(PUBLICATION_RULE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_publication_detail, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        public void onFragmentInteraction(Uri uri);
    }




    ////////////////////////////////////////////////////////////////////////////
    // Event Bus
    /////////////////////////////////////////////////////////////////////////

    public void sendMessage(final String messagem){

    }

    public void onEvent( ){

    }



}
