package com.plataformaam.mobile.clientefinal;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.plataformaam.mobile.clientefinal.configurations.MyAppConfig;
import com.plataformaam.mobile.clientefinal.helpers.volley.LruBitmapCache;
import com.plataformaam.mobile.clientefinal.models.User;
import com.plataformaam.mobile.clientefinal.models.vcloc.VComComposite;

import java.util.List;
import java.util.Map;


/**
 * Created by bernard on 02/03/2015.
 */
public class AppController extends Application {
    private static User onlineUser;
    private static List<VComComposite> allComposites;
    private static Map<Integer,VComComposite> myComposite;


    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
        Log.i(MyAppConfig.LOG.Application,"addToRequestQueue"+req.toString());
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void setOnlineUser(User u){
        onlineUser = u;
        if( onlineUser!= null ) {
            Log.i(MyAppConfig.LOG.Application, "setOnlineUser(User " + u.getLogin() + ")");
        }
    }

    public User getOnlineUser() {
        return onlineUser;
    }

    public  List<VComComposite> getAllComposites() {
        return allComposites;
    }

    public  void setAllComposites(List<VComComposite> allComposites) {
        AppController.allComposites = allComposites;
        if( allComposites != null ) {
            Log.i(MyAppConfig.LOG.Application, "setAllComposites(" + allComposites.size() + ")");
        }
    }

    public  Map<Integer, VComComposite> getMyComposite() {
        return myComposite;
    }

    public  void setMyComposite(Map<Integer, VComComposite> myComposite) {
        AppController.myComposite = myComposite;
        if( myComposite != null ) {
            Log.i(MyAppConfig.LOG.Application, "setMyComposite(Map<Integer, VComComposite> " + myComposite.size() + ")");
        }
    }
}
