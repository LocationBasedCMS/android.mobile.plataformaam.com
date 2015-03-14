package com.plataformaam.mobile.clientefinal.userinterfaces;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.plataformaam.mobile.clientefinal.R;
import com.plataformaam.mobile.clientefinal.asynctasks.MyAsyncTaskCreateUser;
import com.plataformaam.mobile.clientefinal.models.User;


public class RegisterUserUI extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user_ui);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_user_ui, menu);
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

    public void goToLogin(View v){
        Intent intent = new Intent(RegisterUserUI.this,UserLoginUI.class);
        startActivity(intent);
    }

    public void saveNewUser(View v){
        ///OBTENDO CAMPOS DOS FORMULÀRIOS
        EditText etxName = (EditText) findViewById(R.id.etxNameRegisterUserUI);
        EditText etxLogin = (EditText) findViewById(R.id.etxLoginRegisterUser);
        EditText etxPassword = (EditText) findViewById(R.id.etxPasswordRegisterUserUI);
        EditText etxEmail = (EditText) findViewById(R.id.etxEmailRegisterUser);

        //MONTANDO O USUÁRIO
        User user = new User();
        user.setLogin(etxLogin.getText().toString());
        user.setPassword(etxPassword.getText().toString());
        user.setEmail(etxEmail.getText().toString());
        user.setName(etxName.getText().toString());
        user.setId(0);


        //EXECUTANDO A TAREFA ASSINCRONA
        new LongOperation().execute(user);

    }

    private class LongOperation  extends MyAsyncTaskCreateUser {
        private ProgressDialog Dialog = new ProgressDialog(RegisterUserUI.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialog.setMessage("Conectando no Webservice: \n "+ this.webService );
            Dialog.show();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            // Close progress dialog
            Dialog.dismiss();
            if(  RESULT == OPERATION_SUCCESS ){
                Intent intent = new Intent(RegisterUserUI.this, UserLoginUI.class );
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            } else {
                Toast.makeText(RegisterUserUI.this, getApplicationContext().getString(R.string.errorOperationNotAllwed), Toast.LENGTH_LONG).show();
            }

        }



    }


}
