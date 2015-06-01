package com.plataformaam.mobile.clientefinal.userinterfaces.fragments;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
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

import com.plataformaam.mobile.clientefinal.R;
import com.plataformaam.mobile.clientefinal.configurations.MyAppConfiguration;
import com.plataformaam.mobile.clientefinal.helpers.multipart.AndroidMultiPartEntity;
import com.plataformaam.mobile.clientefinal.userinterfaces.listfragments.FragmentUpiList;



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
    private Uri mImageUri;
    Bitmap bitmap;

    private ProgressBar progressBar;
    private String filePath = null;
    private TextView txtPercentage;
    long totalSize = 0;

    public static FragmentUploadImage newInstance(Uri fileUri) {
        FragmentUploadImage fragment = new FragmentUploadImage();
        Bundle args = new Bundle();
        args.putString(FILE_URI, fileUri.toString());
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

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_fragment_upload_image, container, false);
        progressBar  = (ProgressBar) rootView.findViewById(R.id.progressBar);
        txtPercentage = (TextView) rootView.findViewById(R.id.txtPercentage);
        if( mImageUri != null ){
            try {
                bitmap = MediaStore.Images.Media.getBitmap( getActivity().getContentResolver(), mImageUri );
                new UploadFileToServer().execute();

            } catch (IOException e) {
                Log.e(MyAppConfiguration.LOG.Activity,mImageUri.toString()+"  "+e.getMessage());
                e.printStackTrace();
            }
        }

        return rootView;
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


    //NAVEGAcAO
    public void goToFragmentList(){
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        android.app.Fragment frag  = FragmentUpiList.newInstance();
        fragmentTransaction.replace(R.id.container,frag, null).commit();
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
            Log.i(MyAppConfiguration.LOG.Activity, "Response from server: " + result);
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
            android.os.Debug.waitForDebugger();

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost( MyAppConfiguration.getInstance().getUploadWebService() );
            httpPost.addHeader("HTTP_X_REST_USERNAME", "bernauuudo");
            httpPost.addHeader("HTTP_X_REST_PASSWORD", "qw");
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
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }




            Log.d(MyAppConfiguration.LOG.AsyncTask, responseString);
            return responseString;
        }


//        String newUploadFile(){
//
//            String responseString = null;
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httppost = new HttpPost(url);
//
//            FileBody bin = new FileBody(new File(fileName));
//            StringBody comment = new StringBody("Filename: " + fileName);
//
//            MultipartEntity reqEntity = new MultipartEntity();
//            reqEntity.addPart("bin", bin);
//            reqEntity.addPart("comment", comment);
//            httppost.setEntity(reqEntity);
//
//            HttpResponse response = httpclient.execute(httppost);
//            HttpEntity resEntity = response.getEntity();
//
//
//            return responseString;
//        }
    }



}


