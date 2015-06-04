package com.plataformaam.mobile.clientefinal.userinterfaces.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nononsenseapps.filepicker.FilePickerActivity;
import com.plataformaam.mobile.clientefinal.AppController;
import com.plataformaam.mobile.clientefinal.R;
import com.plataformaam.mobile.clientefinal.configurations.MyAppConfig;
import com.plataformaam.mobile.clientefinal.helpers.eventbus.MyMessage;
import com.plataformaam.mobile.clientefinal.models.location.UserPosition;
import com.plataformaam.mobile.clientefinal.models.vcloc.roles.VComUPIAggregationRuleResponseOf;
import com.plataformaam.mobile.clientefinal.models.vcloc.roles.VComUPIAggregationRuleStart;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPI;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.VComUPIPublication;
import com.plataformaam.mobile.clientefinal.services.MyService;
import com.plataformaam.mobile.clientefinal.userinterfaces.GlobalPanelUI;
import com.plataformaam.mobile.clientefinal.userinterfaces.listfragments.FragmentUpiList;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentEditImageUpi.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentEditImageUpi#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentEditImageUpi extends Fragment {
    private static final String ARG_UPI                 = "UPI";
    private static final String ARG_PUBLICATION         = "PUBLICATION";
    private static final String ARG_PUBLICATION_RULE    = "PUBLICATION_RULE";
    private static final String ARG_RESPONSE_RULE       = "RESPONSE_RULE";
    private static final String ARG_LOCATION            =  "USER_POSITION";
    private boolean MODO_CAMERA = false;
    // Activity result key for camera
    private static final int REQUEST_TAKE_PHOTO = 11111;
    private static final int REQUEST_FILE_CODE = 1234;

    private UPI upi;
    private VComUPIPublication publication;
    private VComUPIAggregationRuleStart publicationRule;
    private VComUPIAggregationRuleResponseOf responseRule;
    private UserPosition    userPosition;

    //Elementos de Interface
    EditText etxTitle;
    ImageView imgUpiContent;
    Button btnOpenCamera;
    Button btnOpenFile;
    Button btnSave;
    //
    Uri mImageUri = null;



    private OnFragmentInteractionListener mListener;

    //CREATE UPI
    public static FragmentEditImageUpi newInstance(){
        FragmentEditImageUpi fragment = new FragmentEditImageUpi();
        return fragment;
    }

    //UPI EDIT
    public static FragmentEditImageUpi newInstance(UPI upi){
        FragmentEditImageUpi fragment = new FragmentEditImageUpi();
        Bundle args = new Bundle();
        args.putSerializable(ARG_UPI, upi);
        fragment.setArguments(args);
        return null;// fragment;
    }

    //UPI RESPONSE
    public static FragmentEditImageUpi newInstance(UPI upi,VComUPIPublication publication,VComUPIAggregationRuleResponseOf responseRule) {
        FragmentEditImageUpi fragment = new FragmentEditImageUpi();
        Bundle args = new Bundle();
        args.putSerializable(ARG_UPI, upi);
        args.putSerializable(ARG_PUBLICATION, publication);
        args.putSerializable(ARG_RESPONSE_RULE, responseRule);
        fragment.setArguments(args);
        return fragment;
    }


    //UPI PUBLICATION
    public static FragmentEditImageUpi newInstance(UPI upi,VComUPIAggregationRuleStart publicationRule,UserPosition position) {

        FragmentEditImageUpi fragment = new FragmentEditImageUpi();
        Bundle args = new Bundle();
        args.putSerializable(ARG_UPI, upi);
        args.putSerializable(ARG_PUBLICATION_RULE, publicationRule);
        args.putSerializable(ARG_LOCATION, position);
        fragment.setArguments(args);
        return fragment;

    }

    public void goToUpiList(){
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment frag  = FragmentUpiList.newInstance();
        fragmentTransaction.replace(R.id.container,frag, null).commit();
    }





//    public static FragmentEditImageUpi newInstance(String param1, String param2) {
//        FragmentEditImageUpi fragment = new FragmentEditImageUpi();
//        Bundle args = new Bundle();
//        args.putString(ARG_UPI, param1);
//        args.putString(ARG_PUBLICATION, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }


    public FragmentEditImageUpi() {
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

        View rootView = inflater.inflate(R.layout.fragment_edit_upi_image, container, false);
        //CONECTANDO OS ELEMENTOS DE INTERFACE COM OS OBJETOS
        btnOpenCamera = (Button) rootView.findViewById(R.id.btnOpenCamera);
        btnOpenFile = (Button) rootView.findViewById(R.id.btnOpenFile);
        btnSave = (Button) rootView.findViewById(R.id.btnSaveImage);
        imgUpiContent = (ImageView) rootView.findViewById(R.id.imgUpiContent);
        etxTitle    = (EditText)    rootView.findViewById(R.id.etxCreateUPIText);


        //CRIANDO LISTENERS
        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera(v);
            }
        });

        btnOpenFile.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                openFile(v);
           }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUPI(v);
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
        EventBus.getDefault().register(FragmentEditImageUpi.this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(FragmentEditImageUpi.this);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(UPI upi);
    }


    private void changeButtonState(boolean enable){
        this.btnSave.setEnabled(enable);
        this.btnOpenCamera.setEnabled(enable);
        this.btnOpenFile.setEnabled(enable);
    }

    public void onEvent(MyMessage message){
        if( message.getSender().equals(MyService.class.getSimpleName())){
            Log.i(MyAppConfig.LOG.Activity,"onEvent(MyMessage "+message.getSender()+"/"+message.getMessage()+")");
            if( message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_SUCCESS)){
                Toast.makeText(getActivity(), getString(R.string.operation_upi_save_success) ,Toast.LENGTH_LONG).show();
                UPI savedUpi = message.getUpi();
                if( savedUpi != null ){
                    this.upi = savedUpi;
                    AppController.getInstance().getOnlineUser().getUpis().add(savedUpi);
                }
                goToUpiList();

            }
            if( message.getMessage().equals(MyAppConfig.EVENT_BUS_MESSAGE.UPI_OPERATION_FAIL)){
                Toast.makeText(getActivity(),getString(R.string.operation_upi_save_fail) ,Toast.LENGTH_LONG).show();
                changeButtonState(true);
            }

        }
    }





    public void openCamera(View v){
        Context context = getActivity();
        PackageManager packageManager = context.getPackageManager();
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false){
            Toast.makeText(getActivity(), "This device does not have a camera.", Toast.LENGTH_SHORT)
                    .show();
            return;
        } else {
            //PoSSUI CAMERA
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            GlobalPanelUI activity = (GlobalPanelUI) getActivity();
            if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                // Create the File where the photo should go.
                // If you don't do this, you may get a crash in some devices.
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Toast toast = Toast.makeText(activity, "There was a problem saving the photo...", Toast.LENGTH_SHORT);
                    toast.show();
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri fileUri = Uri.fromFile(photoFile);
                    activity.setCapturedImageURI(fileUri);
                    activity.setCurrentPhotoPath(fileUri.getPath());
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            activity.getCapturedImageURI());
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        }
    }

    /**
     * The activity returns with the photo.
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            onActivityCameraResult(requestCode, resultCode, data);
        } else if(requestCode == REQUEST_FILE_CODE && resultCode == Activity.RESULT_OK ){
            onActivityOpenFileResult(requestCode, resultCode, data);
        }else{
            Toast.makeText(getActivity(), "Image Capture Failed", Toast.LENGTH_SHORT).show();
        }
    }


    public void onActivityCameraResult(int requestCode, int resultCode, Intent data) {
        mImageUri = data.getData();
        GlobalPanelUI activity = (GlobalPanelUI)getActivity();
        setFullImageFromFilePath(activity.getCurrentPhotoPath(), imgUpiContent);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onActivityOpenFileResult(int requestCode, int resultCode, Intent data) {
        if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
            Log.e(MyAppConfig.LOG.Activity,"FilePickerActivity.EXTRA_ALLOW_MULTIPLE");
        } else {
            mImageUri = data.getData();
            imgUpiContent.setImageURI(null);
            imgUpiContent.setImageURI(mImageUri);
        }
    }


    protected File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        GlobalPanelUI activity = (GlobalPanelUI)getActivity();
        activity.setCurrentPhotoPath("file:" + image.getAbsolutePath());
        return image;
    }

    private void setFullImageFromFilePath(String imagePath, ImageView imageView) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    public void openFile(View v){
        // This always works


        Intent i = new Intent(getActivity(), FilePickerActivity.class);
        // This works if you defined the intent filter
        // Intent i = new Intent(Intent.ACTION_GET_CONTENT);

        // Set these depending on your use case. These are the defaults.
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
        i.putExtra("CONTENT_TYPE", ".*jpg|.*png|.*gif|.*JPG|.*PNG|.*GIF");


        // Configure initial directory like so
        i.putExtra(FilePickerActivity.EXTRA_START_PATH, "/storage/emulated/0/");

        startActivityForResult(i, REQUEST_FILE_CODE);
    }


    public void saveUPI(View v){
       String upiTitle =  etxTitle.getText().toString();
        if( !upiTitle.isEmpty() ) {
           if (mImageUri != null) {
                changeButtonState(false);
                android.app.FragmentManager fragmentManager = getFragmentManager();
                android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                android.app.Fragment frag = FragmentUploadImage.newInstance(mImageUri,upiTitle);
                fragmentTransaction.replace(R.id.container, frag, null).commit();
            }
        } else{
            Toast.makeText(getActivity(), getString(R.string.errorUPITitleAndImage) ,Toast.LENGTH_SHORT).show();
        }
    }

}
