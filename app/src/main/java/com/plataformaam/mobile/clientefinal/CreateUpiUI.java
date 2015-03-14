package com.plataformaam.mobile.clientefinal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.plataformaam.mobile.clientefinal.asynctasks.vcloc.MyAsyncTaskCreateUPI;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPI;


public class CreateUpiUI extends ActionBarActivity {

    public static final String SELECTED_UPI = CreateUpiUI.class.getCanonicalName();
    UPI mUpi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_upi_ui);

        //TODO Criar UPI :  Editar as Opções de Fragmentos
        if (savedInstanceState == null) {

            Bundle bundle =  getIntent().getExtras();
            mUpi = (UPI)bundle.getSerializable(SELECTED_UPI);

            UPITextFragment upiTextFragment = UPITextFragment.newInstance(mUpi);
            getSupportFragmentManager()
                    .beginTransaction()
                        .add(R.id.container, upiTextFragment)
                    .commit();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_upi_ui, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static class UPITextFragment extends Fragment implements View.OnClickListener {
        Button btnSave;
        EditText etxTitle;
        EditText etxContent;


        private static final String SELECTED_UPI = UPITextFragment.class.getCanonicalName();
        UPI mUpi;

        public static UPITextFragment newInstance(UPI upi){
            UPITextFragment fragment = new UPITextFragment();
            if( upi != null ) {
                Bundle args = new Bundle();
                args.putSerializable(SELECTED_UPI, upi);
                fragment.setArguments(args);
            }
            return fragment;

        }

        public UPITextFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                mUpi = (UPI) getArguments().getSerializable(SELECTED_UPI);
            }
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_create_upi_text_ui, container, false);
            //Salvando os campos

            etxTitle = (EditText) rootView.findViewById(R.id.etxCreateUPIText);
            etxContent = (EditText) rootView.findViewById(R.id.etxContentCreateNewUPIText);
            btnSave = (Button) rootView.findViewById(R.id.btnCreateNewUPIText);
            btnSave.setOnClickListener(this);

            if( mUpi != null ){
                etxContent.setText(mUpi.getContent());
                etxTitle.setText(mUpi.getTitle());
            }
            return rootView;

        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnCreateNewUPIText:
                    saveUPIText();
                break;
            }
        }

        public void saveUPIText(){
            //TODO Salva UPI - Impementar Bind do service -

            /*
            //MONTANTO A UPI a ser salva
            UPI upi = new UPI();
            upi.setTitle(etxTitle.getText().toString());
            upi.setContent(etxContent.getText().toString());
            upi.setUser(MyAppData.getInstance().getUser());
            upi.setUpiType(MyAppData.getInstance().getUpiType(MyAppConfiguration.UpiType_Data_Code.UPI_TEXT) );



            CreateUpiUI ui = (CreateUpiUI)  getActivity();
            ui.saveUPIText(upi);
            */

        }




    }




    public void gotoGlobalPanelUI(View view){
        Intent intent = new Intent(CreateUpiUI.this, GlobalPanelUI.class);
        startActivity(intent);
    }

    LongOperation operationCreateUPI = null;
    public void saveUPIText(UPI upi){
        operationCreateUPI =  new LongOperation();
        operationCreateUPI.execute(upi);
    }


    private class LongOperation  extends MyAsyncTaskCreateUPI {
        private ProgressDialog Dialog = new ProgressDialog(CreateUpiUI.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialog.setMessage("Conectando no Webservice: \n " + this.webService);
            Dialog.show();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            // Close progress dialog
            Dialog.dismiss();
            if (RESULT == OPERATION_SUCCESS) {
                Intent intent = new Intent(CreateUpiUI.this, GlobalPanelUI.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            } else {
                Toast.makeText(CreateUpiUI.this, getApplicationContext().getString(R.string.errorOperationNotAllwed), Toast.LENGTH_LONG).show();
            }

        }
    }
}
