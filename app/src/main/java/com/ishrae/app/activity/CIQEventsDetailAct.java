package com.ishrae.app.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ishrae.app.R;
import com.ishrae.app.adapter.AddSpeakerAdapter;
import com.ishrae.app.adapter.ImageAdapter;
import com.ishrae.app.cmd.CmdFactory;
import com.ishrae.app.model.AddSpeakerModel;
import com.ishrae.app.model.PendingPostEventListModel;
import com.ishrae.app.model.SharedPref;
import com.ishrae.app.network.NetworkManager;
import com.ishrae.app.network.NetworkResponse;
import com.ishrae.app.utilities.AppUrls;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;
import com.ishrae.app.utilities.recycler_view_utilities.DividerItemDecorationGray;
import com.ishrae.app.utilities.recycler_view_utilities.DividerItemDecorationTransparent;
import com.ishrae.app.utilities.recycler_view_utilities.DividerItemDecorationWhite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Nss Solutions on 23-03-2017.
 */

public class CIQEventsDetailAct extends BaseAppCompactActivity implements Callback, View.OnClickListener {

    private Toolbar toolbar;
    private TextView activityTitle;
    //    private Button btnSubmitTopic;

    private EditText etNoOfDelegates;
    private EditText etNoOfMembers;
    private EditText etNoOfPresent;
    private LinearLayout llAddMore;
    private LinearLayout llUploadPhotos;
    private LinearLayout llUploadFiles;
    private Button btnCreateEvent;

    private RecyclerView rvEventUploadPhotos;
    private RecyclerView rvEventUploadFiles;
    private RecyclerView rvAddMore;

    private NetworkResponse resp;

    private int fromWhere;
    private boolean isRefreshed;

    private PendingPostEventListModel pendingPostEventListModel;
    private Bundle bundle;
    AddSpeakerAdapter addSpeakerAdapter;
    ImageAdapter imageAdapter;
    //camera alert dialog controllers
    private TextView txtFromGallery;
    private TextView txtFromCamera;
    private TextView txtCancel;

    public TextView txtPETitle;
    public EditText etSpeakerName;
    public EditText eTxTopicNameC;
    public EditText etSpeakerMobil;
    public EditText etSpeakerEmail;
    public EditText etSpeakerDescr;


    private AlertDialog cameraAlert;
    private boolean isFromCamera;
    private int requestPermissionForPhoto = 0;

    private int CAMERA = 1;
    private int GALLERY = 2;

    private String directoryPath = "/sdcard/onBord";
    private String filePath;
    private String croppedFilePath = "";
    private Uri mImageUri;
    private ArrayList<Uri> mImageUriList;
    private ArrayList<String> uploadedImageList;
    ArrayList<AddSpeakerModel> addSpeakerModelArrayList;
    private int whichAPi = 0;
    private int UPLOAD_FILE = 1;
    private int ADD_POST = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ciq_event_detail);
        initialize();
    }

    private void initialize() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        etNoOfDelegates = (EditText) findViewById(R.id.etNoOfDelegates);
        etNoOfMembers = (EditText) findViewById(R.id.etNoOfMembers);
        etNoOfPresent = (EditText) findViewById(R.id.etNoOfPresent);
        llAddMore = (LinearLayout) findViewById(R.id.llAddMore);
        llUploadPhotos = (LinearLayout) findViewById(R.id.llUploadPhotos);
        llUploadFiles = (LinearLayout) findViewById(R.id.llUploadFiles);
        btnCreateEvent = (Button) findViewById(R.id.btnCreateEvent);

        activityTitle = (TextView) findViewById(R.id.activityTitle);
        rvEventUploadPhotos = (RecyclerView) findViewById(R.id.rvEventUploadPhotos);
        rvEventUploadFiles = (RecyclerView) findViewById(R.id.rvEventUploadFiles);
        rvAddMore = (RecyclerView) findViewById(R.id.rvAddMore);

        txtPETitle = (TextView) findViewById(R.id.txtPETitle);
        etSpeakerName = (EditText) findViewById(R.id.etSpeakerName);
        eTxTopicNameC = (EditText) findViewById(R.id.eTxTopicNameC);
        etSpeakerMobil = (EditText) findViewById(R.id.etSpeakerMobil);
        etSpeakerEmail = (EditText) findViewById(R.id.etSpeakerEmail);
        etSpeakerDescr = (EditText) findViewById(R.id.etSpeakerDescr);

        llAddMore.setOnClickListener(this);
        llUploadPhotos.setOnClickListener(this);
        llUploadFiles.setOnClickListener(this);
        btnCreateEvent.setOnClickListener(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        rvAddMore.setLayoutManager(new GridLayoutManager(this, 3));
        rvAddMore.addItemDecoration(new DividerItemDecorationTransparent(this));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

        rvEventUploadPhotos.setLayoutManager(gridLayoutManager);
        rvEventUploadFiles.setLayoutManager( new GridLayoutManager(this, 3));
        rvEventUploadPhotos.addItemDecoration(new DividerItemDecorationWhite(this));
        rvEventUploadFiles.addItemDecoration(new DividerItemDecorationWhite(this));
        bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(Constants.CIQ_DATA))
            pendingPostEventListModel = (PendingPostEventListModel) bundle.getSerializable(Constants.CIQ_DATA);
        addSpeakerModelArrayList = new ArrayList<>();
        addSpeakerAdapter = new AddSpeakerAdapter(this, addSpeakerModelArrayList, this);
        rvAddMore.setAdapter(addSpeakerAdapter);
        mImageUriList = new ArrayList<>();
        uploadedImageList = new ArrayList<>();
        if(!TextUtils.isEmpty(pendingPostEventListModel.Heading))
        txtPETitle.setText(""+pendingPostEventListModel.Heading);
    }

    @Override
    public void onResume() {
        super.onResume();
        activityTitle.setText(getResources().getString(R.string.Event_Detail));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (isRefreshed)
            setResult(Constants.REFRESH);
        super.onBackPressed();
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Util.manageFailure(this, e);
    }


    @Override
    public void onResponse(Call call, Response response) throws IOException {

        Util.dismissProgressDialog();
        final JSONObject jsonObject = Util.getObjectFromResponse(response);
        try {
            if (jsonObject != null && jsonObject.getInt(Constants.FLD_RESPONSE_CODE) == 1) {
                String responseData = jsonObject.getString(Constants.FLD_RESPONSE_DATA);
                if (whichAPi == UPLOAD_FILE) {
                    uploadedImageList.add(responseData);
                } else if (responseData.length() > 0) {
                    String value = Util.decodeToken(responseData);
                    resp = new NetworkResponse();
                    resp.respStr = value;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                                Util.showCenterToast(CIQEventsDetailAct.this, Util.getMessageFromJObj(jsonObject));
                                isRefreshed = true;
                                onBackPressed();

                        }
                    });
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA && resultCode == RESULT_OK) {
            filePath = getRealPathFromURI(mImageUri);
//            beginCrop(mImageUri);
            mImageUriList.add(mImageUri);
            setImagesToRV();
            uploadPhoto();
        } else if (requestCode == GALLERY && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            filePath = getRealPathFromURI(selectedImageUri);
            mImageUriList.add(selectedImageUri);
            uploadPhoto();
            setImagesToRV();
        }


    }

    private void setImagesToRV() {
        imageAdapter = new ImageAdapter(this, mImageUriList,this);
        rvEventUploadPhotos.setAdapter(imageAdapter);
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.llAddMore) {

            addSpeakerDetailToRV();
        }
        else if (vId == R.id.llUploadFiles) {

        }
        else if (vId == R.id.llUploadPhotos) {
            if(mImageUriList.size()<5)
            selectPhotoVideoAlert();
            else
                Util.showDefaultAlert(CIQEventsDetailAct.this,getResources().getString(R.string.You_can_upload_max_5_photos),null);
        } else if (vId == R.id.btnCreateEvent) {
            addPostEvent();
        } else if (vId == R.id.txtFromGallery) {
            cameraAlert.dismiss();
            isFromCamera = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                txtFromGallery.post(new Runnable() {
                    @Override
                    public void run() {
                        if (checkAndRequestPermissions()) {
                            txtFromGallery();
                        }
                    }
                });

            } else {
                txtFromGallery();
            }

        } else if (vId == R.id.txtFromCamera) {
            cameraAlert.dismiss();
            isFromCamera = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkAndRequestPermissions()) {
                    txtFromCamera();
                }
            } else {
                txtFromCamera();
            }
        } else if (vId == R.id.txtCancel) {
            cameraAlert.dismiss();
        } else if (vId == R.id.imgRemvoeSpeaker) {
            int position = (int) v.getTag();
            addSpeakerModelArrayList.remove(position);
            addSpeakerAdapter.notifyItemRemoved(position);
        }

        else if (vId == R.id.imgRemvoePhotos) {
            int position = (int) v.getTag();
            if(uploadedImageList.size()>position);
            uploadedImageList.remove(position);
            mImageUriList.remove(position);
            imageAdapter.notifyItemRemoved(position);
        }
    }

    private void addSpeakerDetailToRV() {
        String speakerName = etSpeakerName.getText().toString().trim();

        String speakerTopickName = eTxTopicNameC.getText().toString().trim();
        String speakerMobileNumber = etSpeakerMobil.getText().toString().trim();
        String speakerEmail = etSpeakerEmail.getText().toString().trim();
        String speakerDescr = etSpeakerDescr.getText().toString().trim();

       if (!Util.isValidName(this, speakerName)) {

        } else if (TextUtils.isEmpty(speakerTopickName)) {
            Util.showDefaultAlert(this, getResources().getString(R.string.Please_enter_topic_name), null);
        } else if (!Util.isValidMobileNumber(this, speakerMobileNumber)) {

        } else if (!Util.isValidEmail(this, speakerEmail)) {

        } else {
            AddSpeakerModel addSpeakerModel = new AddSpeakerModel();
            addSpeakerModel.speakerName = speakerName;
            addSpeakerModel.speakerTopic = speakerTopickName;
            addSpeakerModel.speakerMobile = speakerMobileNumber;
            addSpeakerModel.speakerEmail = speakerEmail;
            addSpeakerModel.speakerDescr = speakerDescr;
            addSpeakerModelArrayList.add(0, addSpeakerModel);
            addSpeakerAdapter.notifyItemInserted(0);
            etSpeakerName.setText("");
            etSpeakerMobil.setText("");
            etSpeakerEmail.setText("");
            eTxTopicNameC.setText("");
//            etSpeakerDescr.setText("");
        }
    }

    private void addPostEvent() {
        if (Util.isDeviceOnline(this, true)) {

            String noOfDelegates = etNoOfDelegates.getText().toString().trim();
            String noOfNonMembers = etNoOfMembers.getText().toString().trim();
            String noOfPresente = etNoOfPresent.getText().toString().trim();
            String descr = etSpeakerDescr.getText().toString().trim();
            if (!TextUtils.isEmpty(etSpeakerName.getText().toString())) {
                onClick(llAddMore);
            }

            if (TextUtils.isEmpty(noOfDelegates)) {
                Util.showDefaultAlert(this, getResources().getString(R.string.Please_Enter_No_of_Delegates), null);
            } else if (TextUtils.isEmpty(noOfNonMembers)) {
                Util.showDefaultAlert(this, getResources().getString(R.string.Please_Enter_No_of_Non_Members), null);
            } else if (TextUtils.isEmpty(noOfPresente)) {
                Util.showDefaultAlert(this, getResources().getString(R.string.Please_Enter_No_of_Presente_technical_paper), null);
            } else if (addSpeakerModelArrayList.size() <= 0) {
                Util.showDefaultAlert(this, getResources().getString(R.string.Please_Fill_speaker_detail), null);
            } else if (TextUtils.isEmpty(descr)) {
                Util.showDefaultAlert(this, getResources().getString(R.string.Please_Enter_description), null);
            } else {
                whichAPi = ADD_POST;
                JSONArray speakerDetailArray = new JSONArray();
                for (int i = 0; i < addSpeakerModelArrayList.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(Constants.FLD_SPEAKER_NAME, addSpeakerModelArrayList.get(i).speakerName);
                        jsonObject.put(Constants.FLD_MOBLIE_NO, addSpeakerModelArrayList.get(i).speakerMobile);
                        jsonObject.put(Constants.FLD_EMAIL_CAPITAL, addSpeakerModelArrayList.get(i).speakerEmail);
                        jsonObject.put(Constants.FLD_TOPIC, addSpeakerModelArrayList.get(i).speakerTopic);
                        speakerDetailArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
//                String id = "" + 1;
                String id= ""+pendingPostEventListModel.id;
                JSONObject params = CmdFactory.addPostEventCmd(speakerDetailArray, id, noOfDelegates, noOfNonMembers, noOfPresente,  descr, uploadedImageList,"");
                NetworkManager.requestForAPI(this, this, Constants.VAL_POST, AppUrls.ADD_POST_EVENT_URL, params.toString(), true);
            }
        }
    }

    private void uploadPhoto() {
        if (Util.isDeviceOnline(this, true)) {
            whichAPi = UPLOAD_FILE;
            NetworkManager.init(this, this);
            NetworkManager.addFileForUploading(Constants.FIELD_FILE1, filePath, null, Constants.FLD_POST_EVENT_DOC);
            NetworkManager.uploadFileToServer(this, this, null, AppUrls.UPLOAD_FILE_URL, true);
        }
    }

    private void selectPhotoVideoAlert() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(false);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.profile_photo_alert, null);
        dialogBuilder.setView(dialogView);

        txtFromGallery = (TextView) dialogView.findViewById(R.id.txtFromGallery);
        txtFromCamera = (TextView) dialogView.findViewById(R.id.txtFromCamera);
        txtCancel = (TextView) dialogView.findViewById(R.id.txtCancel);

        txtFromGallery.setOnClickListener(this);
        txtFromCamera.setOnClickListener(this);
        txtCancel.setOnClickListener(this);

        cameraAlert = dialogBuilder.create();
        cameraAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Setting dialogview
        Window window = cameraAlert.getWindow();
        window.setGravity(Gravity.CENTER);
        cameraAlert.show();
    }

    private boolean checkAndRequestPermissions() {
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int writeExternalPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readExternalPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int recordAudioPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        if (writeExternalPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (readExternalPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (recordAudioPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), requestPermissionForPhoto);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == requestPermissionForPhoto) {

            Map<String, Integer> perms = new HashMap<>();
            // Initialize the map with both permissions
            if (isFromCamera) {
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
            }

            perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            // Fill with actual results from user

            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                }

                if (isFromCamera) {
                    // Check for both permissions
                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        txtFromCamera();

                    } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        showDialogOK("Camera and Storage Permission required for this app",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                checkAndRequestPermissions();
                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                // proceed with logic by disabling the related features or quit the app.
                                                break;
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Check for both permissions
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        txtFromGallery();

                    } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        showDialogOK("Storage Permission required for this app",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                checkAndRequestPermissions();
                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                // proceed with logic by disabling the related features or quit the app.
                                                break;
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private void txtFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select"), GALLERY);
    }


    public Uri setImageUri() {
        File myDir = new File(directoryPath);
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        File file = new File(directoryPath, "/image" + new Date().getTime() + ".jpg");
        Uri imgUri = Uri.fromFile(file);
        mImageUri = imgUri;
        filePath = file.getAbsolutePath();
        return imgUri;
    }

    private void txtFromCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
        this.startActivityForResult(cameraIntent, CAMERA);
    }


}
