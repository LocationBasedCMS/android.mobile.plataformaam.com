package com.plataformaam.mobile.clientefinal.userinterfaces.fragments;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.plataformaam.mobile.clientefinal.AppController;
import com.plataformaam.mobile.clientefinal.R;
import com.plataformaam.mobile.clientefinal.configurations.MyAppConfig;
import com.plataformaam.mobile.clientefinal.helpers.eventbus.MyMessage;
import com.plataformaam.mobile.clientefinal.helpers.multipart.AndroidMultiPartEntity;
import com.plataformaam.mobile.clientefinal.helpers.multipart.UploadImageResponse;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPI;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPIType;
import com.plataformaam.mobile.clientefinal.services.MyService;
import com.plataformaam.mobile.clientefinal.userinterfaces.listfragments.FragmentUpiList;

import de.greenrobot.event.EventBus;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentUploadImage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentUploadImage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentUploadImage extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FILE_URI = "FILE_URI";
    private static final String UPI_TITLE = "UPI_TITLE";
    private Uri mImageUri;
    private String upiTitle;
    Bitmap bitmap;

    private ProgressBar progressBar;
    private String filePath = null;
    private TextView txtPercentage;
    long totalSize = 0;

    public static FragmentUploadImage newInstance(Uri fileUri, String upiTitle) {
        FragmentUploadImage fragment = new FragmentUploadImage();
        Bundle args = new Bundle();
        args.putString(FILE_URI, fileUri.toString());
        args.putString(UPI_TITLE, upiTitle);
        fragment.setArguments(args);
        return fragment;
    }

    private OnFragmentInteractionListener mListener;

    public FragmentUploadImage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImageUri = Uri.parse(getArguments().getString(FILE_URI));
            filePath = mImageUri.getPath();
            upiTitle = getArguments().getString(UPI_TITLE);
        }
        EventBus.getDefault().register(FragmentUploadImage.this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(FragmentUploadImage.this);
        super.onDestroy();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_upload_image, container, false);
        progressBar  = (ProgressBar) rootView.findViewById(R.id.progressBar);
        txtPercentage = (TextView) rootView.findViewById(R.id.txtPercentage);
        if( mImageUri != null ){
            try {
                bitmap = MediaStore.Images.Media.getBitmap( getActivity().getContentResolver(), mImageUri );
                new UploadFileToServer().execute();

            } catch (IOException e) {
                Log.e(MyAppConfig.LOG.Activity,mImageUri.toString()+"  "+e.getMessage());
                e.printStackTrace();
            }
        }

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




    /**
     * Uploading the file to server
     * */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }


        @Override
        protected void onPostExecute(String result) {
            Log.i(MyAppConfig.LOG.Activity, "Response from server: " + result);
            // showing the server response in an alert dialog
            showAlert(result);

            super.onPostExecute(result);
        }

        /**
         * Method to show alert dialog
         */
        private void showAlert(String message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message).setTitle("Response from Servers")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // do nothing
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost( MyAppConfig.getInstance().getUploadWebService() );
            httpPost.addHeader("HTTP_X_REST_USERNAME", AppController.getInstance().getOnlineUser().getLogin());
            httpPost.addHeader("HTTP_X_REST_PASSWORD", AppController.getInstance().getOnlineUser().getPassword());
            httpPost.addHeader("Accept", "application/json");
            String boundary = "--" + System.currentTimeMillis()+ "--";
            //httpPost.setHeader("Content-type", "multipart/form-data; charset=utf-8;");

            try {

                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });


                //Open the file
                File sourceFile = new File(filePath);
                // Adding file data to http body
                FileBody sourceFileBody = new FileBody(sourceFile);
                entity.addPart( "myFile" , sourceFileBody );
                entity.addPart( "myMessage" , new StringBody("this is my message! "));
                totalSize = entity.getContentLength();
                //Adding part to request
                httpPost.setEntity(entity);


                // Making server call
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                    Gson gson = new Gson();
                    UploadImageResponse imageResponse = gson.fromJson(responseString, UploadImageResponse.class);
                    saveUPI(imageResponse.getUrl());


                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }




            Log.d(MyAppConfig.LOG.AsyncTask, responseString);
            return responseString;
        }



    }

    public void onEvent(MyMessage message){
        if( message.getSender().equals(MyService.class.getSimpleName())){
            Log.i(MyAppConfig.LOG.Activity,"onEvent(MyMessage "+message.getSender()+"/"+message.getMessage()+")");
            if( message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_SUCCESS)){
                Toast.makeText(getActivity(), getString(R.string.operation_upi_save_success), Toast.LENGTH_LONG).show();
                UPI savedUpi = message.getUpi();
                if( savedUpi != null && AppController.getInstance().getOnlineUser() != null ){
                    AppController.getInstance().getOnlineUser().addUpi(savedUpi);
                }
                goToUpiList();

            }
            if( message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_FAIL)){
                Toast.makeText(getActivity(),getString(R.string.operation_upi_save_fail) ,Toast.LENGTH_LONG).show();
                goToUpiList();
            }

        }
    }
    //NAVEGAcAO
    public void goToUpiList(){
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        android.app.Fragment frag  = FragmentUpiList.newInstance();
        fragmentTransaction.replace(R.id.container,frag, null).commit();
    }

    public void saveUPI(String content){
        UPI upi = new UPI();
        upi.setUser(AppController.getInstance().getOnlineUser());
        upi.setTitle(upiTitle);
        upi.setContent(content);
        upi.setUpiType( new UPIType(MyAppConfig.UPI_TYPE_CODE.UPI_IMAGE,"UPI_IMAGE"));
        MyMessage message = new MyMessage(FragmentEditUpi.class.getSimpleName(), MyAppConfig.EVENT_BUS_MESSAGE.SAVE_UPI);
        message.setUpi(upi);
        message.setUser(AppController.getInstance().getOnlineUser());
        EventBus.getDefault().post(message);
    }

}


