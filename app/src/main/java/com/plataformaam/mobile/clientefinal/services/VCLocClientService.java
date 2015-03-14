package com.plataformaam.mobile.clientefinal.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.plataformaam.mobile.clientefinal.configurations.MyAppConfiguration;

import com.plataformaam.mobile.clientefinal.exceptions.InvalidServiceStateException;
import com.plataformaam.mobile.clientefinal.exceptions.UserNotAllowedException;
import com.plataformaam.mobile.clientefinal.helpers.MyFilterItem;
import com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor.model.get.GetUPIResponse;
import com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor.model.get.GetUserResponse;
import com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor.operation.WebClientGetData;
import com.plataformaam.mobile.clientefinal.models.User;
import com.plataformaam.mobile.clientefinal.models.location.UserPosition;
import com.plataformaam.mobile.clientefinal.models.vcloc.VComComposite;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPI;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.VComUPIPublication;
import com.plataformaam.mobile.clientefinal.services.broadcastsender.VClocClientServiceBroadcasterSender;
import com.plataformaam.mobile.clientefinal.services.runnables.LoginRunnable;
import com.plataformaam.mobile.clientefinal.services.runnables.LoadGlobalDataRunnable;
import com.plataformaam.mobile.clientefinal.services.runnables.MainLooperRunnable;
import com.plataformaam.mobile.clientefinal.services.runnables.SavePositionRunnable;
import com.plataformaam.mobile.clientefinal.services.statecontrol.IServiceStateControl;
import com.plataformaam.mobile.clientefinal.services.statecontrol.IServiceStateController;
import com.plataformaam.mobile.clientefinal.services.statecontrol.ServiceStateController;
import com.plataformaam.mobile.clientefinal.services.timecontrol.IServiceSpeedControl;
import com.plataformaam.mobile.clientefinal.services.timecontrol.IServiceSpeedController;
import com.plataformaam.mobile.clientefinal.services.timecontrol.SpeedController;
import com.plataformaam.mobile.clientefinal.services.usercontrol.IUserControl;
import com.plataformaam.mobile.clientefinal.services.usercontrol.IUserController;
import com.plataformaam.mobile.clientefinal.services.usercontrol.UserController;
import com.plataformaam.mobile.clientefinal.services.vcomcontrol.IVComControl;
import com.plataformaam.mobile.clientefinal.services.vcomcontrol.IVComController;
import com.plataformaam.mobile.clientefinal.services.vcomcontrol.VComController;

import java.util.List;
import java.util.Map;

public class VCLocClientService extends Service implements
        LocationListener,
        IServiceSpeedControl,
        IServiceStateControl,
        IUserControl,
        IVComControl
{



    //SERVICE VARIABLES
    protected IServiceSpeedController speedController;  //CONTROLE DE VELOCIDADE DO LOOOP
    protected IServiceStateController stateController;  //CONTROLE DOS ESTADOS DO SERVIÇO
    protected IUserController userController;              //SESSÃO DE USUÁRIO DO SISTEMA
    protected IVComController vComController;


    Thread threadService;
    Runnable serviceLooper = new MainLooperRunnable(this);

    boolean serviceReady = false;       // Registro da carga Inicial de dados
    boolean serviceRunning = false;     // Registra informaçao se a thread está rodando.


    private String provider;            //Provedor de Localização
    LocalBroadcastManager broadcaster;  // Emissor de informações

    private final IBinder mBinder = new MyBinder();
    //SERVICE BINDER IMPLEMENTATION
    public class MyBinder extends Binder{
        public VCLocClientService getService(){
            return VCLocClientService.this;
        }
    }

    public VCLocClientService() {
        speedController = SpeedController.newInstance(this);
        stateController = ServiceStateController.newInstance(this);
        userController  = UserController.getInstance(this);
        vComController  = VComController.getInstance(this);

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if( ! serviceRunning ) {

                Log.w(VCLocClientService.class.getSimpleName(), "ServiceRunner - thread start");
                serviceRunning=true;
                threadService = new Thread(serviceLooper);
                threadService.start();

            }

            stateController.setState( ServiceStateController.STARTED );
        } finally {

            return super.onStartCommand(intent, flags, startId);
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        speedController.setReferenceSpeed();
        try {
            //INVOCADO TAMBÉM NO BIND DO SERVICE
            //POUCO PROVÀVEL , MAS PODE OCORRER ANTES DO START COMMAND
            if( stateController.isState(ServiceStateController.CREATED) ) {
                stateController.setState(ServiceStateController.STARTED);
            }
        } catch (InvalidServiceStateException e) {
            e.printStackTrace();
        }
        return mBinder;
    }


    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(VCLocClientService.this);
        try {
            //INVOCADO TAMBÉM NO BIND DO SERVICE
            if( stateController.isState(ServiceStateController.CREATED) ) {
                stateController.setState(ServiceStateController.STARTED);
            }
        } catch (InvalidServiceStateException e) {
            e.printStackTrace();
        }
        super.onCreate();

    }


    @Override
    public boolean onUnbind(Intent intent) {
        speedController.serviceSpeedDown();
        return super.onUnbind(intent);
    }


    @Override
    public void onRebind(Intent intent) {
        speedController.setReferenceSpeed();
        if(serviceReady){
            VClocClientServiceBroadcasterSender.sendLocalBroadCast(VCLocClientService.this,MyAppConfiguration.BroadCastMessage.SERVICE_READY);
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    //LOCATION
    @Override
    public void onLocationChanged(Location location) {
        Log.i(VCLocClientService.class.getSimpleName(),"onLocationChanged");
        if( userController.getUser() != null ) {
            this.saveUserPosition(MyAppConfiguration.UserPositionContent.NAVIGATE);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
        //TODO - Re - solicitar GPS
    }
    //LOCATION

    //CONTROLE DE VELOCIDADE DO LOOP
    @Override
    public void onServiceSpeedChanged(IServiceSpeedController iServiceSpeedController, int newSpeed) {
        Log.v(VCLocClientService.class.getSimpleName(), " Velocidade Alterada : " + newSpeed);
    }

    //CONTROLE DO ESTADO DO SERVIÇO
    @Override
    public void onStateChanged(IServiceStateController controller, int newState) {
        Log.v(VCLocClientService.class.getSimpleName(), " Estado Alterado : " + newState);
    }

    @Override
    public void onVcomLoad(Map<Integer, VComComposite> composites, IVComController ivComController) {
        Log.v(VCLocClientService.class.getSimpleName(), "onVcomLoad ");
    }

    @Override
    public void onUserVComConnect(VComComposite composite, IUserController userController, IVComController ivComController) {
        Log.v(VCLocClientService.class.getSimpleName(), "onUserVComConnect");

    }

    @Override
    public void onUserVComDisconnect(VComComposite composite, IUserController userController, IVComController ivComController) {
        Log.v(VCLocClientService.class.getSimpleName(), "onUserVComDisconnect");
    }

    @Override
    public void onUserPublish(VComUPIPublication publication, VComComposite composite, IUserController userController) {
        Log.v(VCLocClientService.class.getSimpleName(), "onUserPublish");
    }

    @Override
    public void onUserLogin(IUserController controller, User user) {
        Log.v(VCLocClientService.class.getSimpleName(),"onUserLogin:"+user.toString() );
    }

    @Override
    public void onUserLogout(IUserController controller, User user) {
        Log.v(VCLocClientService.class.getSimpleName(),"onUserLogout:"+user.toString() );
    }

    @Override
    public void onUserChanged(IUserController controller, User user, Class aClass) {
        Log.v(VCLocClientService.class.getSimpleName(),"onUserChanged:  Upadte "+aClass.getSimpleName() );
    }

    public void doAutoLogin(){
       new Thread( new LoginRunnable(this)   ).start();
    }

    @Override
    public boolean doLogin(User user) throws InvalidServiceStateException, UserNotAllowedException{
        Log.i(VCLocClientService.class.getSimpleName(),"Login");
        String login = user.getLogin();
        String password = user.getPassword();

        if( !stateController.isState( ServiceStateController.STARTED )  ){
            throw new InvalidServiceStateException(VCLocClientService.class.getSimpleName()+" -> Call doLogout() to service return to STARTED state \n" +
                    " \n\t\t Estado esperado :  "+ServiceStateController.STARTED +
                    " \n\t\t Estado encontrado: "+stateController.getState()
            );
        } else{
            user.setId(0);
            userController.setUser(user);
            WebClientGetData<User,GetUserResponse> requestLogin = new WebClientGetData<User, GetUserResponse>(User.class,GetUserResponse.class, userController);

            MyFilterItem filterLogin = new MyFilterItem("login",login,"=");
            MyFilterItem filterPassword = new MyFilterItem("password",password,"=");

            requestLogin.addFilter( filterLogin );
            //requestLogin.addFilter( filterPassword );

            GetUserResponse response =requestLogin.execute();
            if( response.isSuccess()  ){
                if( response.getData().getTotalCount()  == 1 ){

                    User u =response.getData().getUser().get(0);
                    u.setPassword(password);
                    userController.setUser(u);

                    saveUserPosition(MyAppConfiguration.UserPositionContent.LOGIN_POSITION);
                    this.loadUserData();

                    stateController.setState(ServiceStateController.LOGGED_USER);



                    return true;
                }
            }
        }
        userController.unSetUser();
        return false;
    }

    private void loadUserData(){

        //AJUSTANDO AS UPI DO USUÁRIO
        WebClientGetData<UPI, GetUPIResponse> webClientGetData = new WebClientGetData<UPI, GetUPIResponse>(UPI.class, GetUPIResponse.class, userController, true);
        GetUPIResponse upiResponse =webClientGetData.execute();
        if( upiResponse.isSuccess() && upiResponse.getData().getTotalCount() > 0  ) {
            List<UPI> upis = upiResponse.getData().getuPI();
            for (UPI u : upis) {
                u.setUser(userController.getUser()); //TROCA A CÓPIA PELO USUÁRIO
            }
            userController.updateUpis(upis);
        }

        //USER POSITION
        List<UserPosition> positions = userController.getUser().getUserPositions();
        for( UserPosition p : positions){
            p.setUser(userController.getUser());
        }
        userController.updateUserPositions(positions);


    }


    public void doLogout(){

        SharedPreferences sharedpreferences = getSharedPreferences(MyAppConfiguration.Preferences.PREFERENCE_AUTO_LOGIN, Context.MODE_PRIVATE);
        sharedpreferences.edit().clear().commit();
        userController.unSetUser();

        try {
            stateController.setState(ServiceStateController.STARTED);
        } catch (InvalidServiceStateException e) {
            e.printStackTrace();
        }
    }






    public boolean testLocationVariation(Location location,String UserPositionContent){
        if( location != null ){
            if( UserPositionContent == MyAppConfiguration.UserPositionContent.NAVIGATE  ){

                UserPosition userPosition = null;
                userPosition = userController.getLastPosition();

                if( userPosition == null ){
                    return true;
                }else {
                    //TESTA VARIAÇÂO DA DISTÂNCIA
                    Location lastLocation = new Location("lastLocation");
                    lastLocation.setLatitude(userPosition.getLatitude());
                    lastLocation.setLongitude(userPosition.getLongitude());
                    float distance = location.distanceTo(lastLocation);
                    if (distance > 30) {
                        return true;
                    }
                }
            }else{
                return true;
            }
        }
        return false;
    }






    //NOVAS INTERFACES
    @Override
    public User getUser() {
        return userController.getUser();
    }

    @Override
    public IVComController getVComController() {
        return this.vComController;
    }

    @Override
    public int getWaitTime() {
        if( speedController == null ){
            speedController = SpeedController.newInstance(this);
        }
        return this.speedController.getWaitTime();
    }

    @Override
    public IServiceSpeedController getSpeedController() {
        return this.speedController;
    }

    //Controle de estados do serviço
    @Override
    public int getState() {
        return stateController.getState();
    }
    public Location getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);
        return locationManager.getLastKnownLocation(provider);
    }

    @Override
    public IUserController getUserController() {
        return userController;
    }

    /***
     * @param upContent Reference of the saved position type.
     */
    public void saveUserPosition(final String upContent){
        new Thread(new SavePositionRunnable(this,upContent)).start();
    }

    @Override
    public void setAllVCom(Map<Integer, VComComposite> composites) {
        vComController.setAllVComComposite(composites);
    }

    @Override
    public void loadAllVCom() {
        LoadGlobalDataRunnable runnable = new LoadGlobalDataRunnable(this);
        new Thread(runnable).start();
    }

    public void selectVComComposite(VComComposite composite){
    }


}
