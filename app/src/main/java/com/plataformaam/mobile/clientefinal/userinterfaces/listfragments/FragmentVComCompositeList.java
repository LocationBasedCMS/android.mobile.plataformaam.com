package com.plataformaam.mobile.clientefinal.userinterfaces.listfragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.plataformaam.mobile.clientefinal.AppController;
import com.plataformaam.mobile.clientefinal.adapters.VComUserRoleArrayAdapter;
import com.plataformaam.mobile.clientefinal.models.User;
import com.plataformaam.mobile.clientefinal.models.vcloc.roles.VComUserRole;
import com.plataformaam.mobile.clientefinal.userinterfaces.mapsfragments.GlobalNavigateFragment;
import com.plataformaam.mobile.clientefinal.R;
import com.plataformaam.mobile.clientefinal.adapters.VComCompositeArrayAdapter;
import com.plataformaam.mobile.clientefinal.configurations.MyAppConfig;
import com.plataformaam.mobile.clientefinal.helpers.eventbus.MyMessage;
import com.plataformaam.mobile.clientefinal.models.vcloc.VComComposite;
import com.plataformaam.mobile.clientefinal.services.MyVComService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class FragmentVComCompositeList extends Fragment implements AbsListView.OnItemClickListener {
    public static class MODE{
        public static int MY_VCOM = 0;
        public static int ALL_VCOM = 1;
    }

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "MODE";
    int mode;
    private OnFragmentInteractionListener mListener;
    private AbsListView mListView;
    private VComCompositeArrayAdapter mAdapter;

    private List<VComComposite> composites = new ArrayList<VComComposite>();
    private Map<Integer,VComComposite> myComposites = new HashMap<Integer,VComComposite>();

    View view;
    ListView vcom_list;

    public static FragmentVComCompositeList newInstance(int mode) {
        FragmentVComCompositeList fragment = new FragmentVComCompositeList();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, mode);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentVComCompositeList() {
        mode=0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(FragmentVComCompositeList.this);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getInt(ARG_PARAM);
        }


    }

    @Override
    public void onResume() {
        if(mode == MODE.MY_VCOM){
            composites = new ArrayList<VComComposite>(AppController.getInstance().getMyComposite().values());
        }else{
            composites = AppController.getInstance().getAllComposites();
        }
        myComposites = AppController.getInstance().getMyComposite();
        buildList(composites,myComposites);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(FragmentVComCompositeList.this);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_vcomcomposite, container, false);
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
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        if (null != mListener) {
            VComComposite composite = (VComComposite) parent.getItemAtPosition(position);
            User user = AppController.getInstance().getOnlineUser();
            if( user != null && composite.hasRole(user) ){
                goToNavigateComposite(composite);
            }else{
                if( composite.getUserRoles()  != null ) {
                    final List<VComUserRole> roles = new ArrayList<VComUserRole>();
                    for (VComUserRole role : composite.getUserRoles() ) {
                        if ( role.isClientSelectable() == 1 ){
                            role.setvComComposite(composite);
                            roles.add(role);
                        }
                    }
                    //CREATE A  ADAPTER
                    VComUserRoleArrayAdapter adapter = new VComUserRoleArrayAdapter(getActivity(),R.layout.row_vcomuserrole_list,roles);
                    //CREATE A DIALOG
                    //TODO - strings.xml
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            getActivity())
                            .setTitle("Papeis disponíveis no cadastro!")
                            .setAdapter(
                                    adapter,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            confirmSubscribe(roles.get(which));
                                        }
                                    }
                            )
                            .setCancelable(true);
                    builder.show();


                } else {
                    //TODO - VCom sem papeis disponíveis  - Exibir mensagem .
                    Toast.makeText(getActivity(),"Usuário Sem Papel - implemenatr" , Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void confirmSubscribe(final VComUserRole role){
        //todo strings.xml
        AlertDialog.Builder builder = new AlertDialog.Builder(
                getActivity())
                .setTitle(" Confirmar cadastro ? ")
                .setMessage( role.getName() )
                .setPositiveButton("SIM",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MyMessage message = new MyMessage(FragmentVComCompositeList.class.getSimpleName(), MyAppConfig.EVENT_BUS_MESSAGE.SUBSCRIBE_COMPOSITE);
                                message.setRole(role);
                                EventBus.getDefault().post(message);
                            }
                        }

                ).setNegativeButton("Não",null)
                .setCancelable(true);
        builder.show();
    }


    private void goToNavigateComposite(VComComposite vComComposite) {
        mListener.onFragmentInteraction( vComComposite);
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment frag  = GlobalNavigateFragment.newInstance(vComComposite);
        fragmentTransaction.replace(R.id.container,frag, null).commit();
    }

    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(VComComposite composite);
    }




    public void buildList( List<VComComposite> composites,Map<Integer,VComComposite> myComposites){
        mAdapter = new VComCompositeArrayAdapter(
                getActivity(),
                R.layout.row_vcomcomposite_list,
                composites,
               myComposites
        );

        vcom_list = (ListView) view.findViewById(R.id.vcom_list);
        registerForContextMenu(vcom_list);
        vcom_list.setOnItemClickListener(this);
        vcom_list.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();


    }

    public void refreshList(){
        if( AppController.getInstance().getAllComposites() != null ){
            composites = AppController.getInstance().getAllComposites();
            if( AppController.getInstance().getMyComposite() != null ){
                myComposites = AppController.getInstance().getMyComposite();
                mAdapter.setMyComposites(myComposites);
            }
            mAdapter.clear();
            mAdapter.addAll(composites);
            mAdapter.notifyDataSetChanged();
        }


    }

    public void onEvent(MyMessage message){
        if(message.getSender().equals(MyVComService.class.getSimpleName()) ){
            if( message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.LOAD_COMPOSITE)) {
                Log.i(MyAppConfig.LOG.Activity,"FragmentVComCompositeList.onEvent("+message.getMessage()+")");
                refreshList();
            }
        }
    }




}
