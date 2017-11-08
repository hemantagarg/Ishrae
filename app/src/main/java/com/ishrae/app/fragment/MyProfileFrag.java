package com.ishrae.app.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.google.gson.Gson;
import com.ishrae.app.R;
import com.ishrae.app.activity.ChangePassActivity;
import com.ishrae.app.activity.DashboardActivity;
import com.ishrae.app.activity.LoginActivity;
import com.ishrae.app.activity.RenewalAct;
import com.ishrae.app.custom.CircleImageView;
import com.ishrae.app.model.SharedPref;
import com.ishrae.app.network.NetworkManager;
import com.ishrae.app.tempModel.UserDetailsTemp;
import com.ishrae.app.utilities.AppUrls;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;
import com.soundcloud.android.crop.Crop;

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

import static com.ishrae.app.R.id.txtAddress;

/**
 * Created by Nss Solutions on 22-03-2017.
 */
public class MyProfileFrag extends Fragment implements View.OnClickListener, Callback {

    private View view;

    private TextView activityTitle;

    private TextView txtSwitchBoard;
    private TextView txtUsername;
    private TextView txtFBLikesCount;
    private TextView txtMemberShipNumber;
    private View txtMemberShipNumberLine;
    private TextView txtChapterName;
    private View txtChapterNameLine;
    private TextView txtMobile;
    private TextView txtProfileEmail;
    private TextView txtProfileAddress;
    private TextView txtChangePassword;
    private TextView txtPaymentDetails;
    private View txtPaymentDetailsLine;
    private TextView txtUpgradeMembership;
    private View txtUpgradeMembershipLine;
    private TextView txtInstituteName;
    private TextView txtUpdatProfile;
    private View txtInstituteNameLine;

    private ImageView imvEditProfile;
    private CircleImageView imvUserImage;

    private LinearLayout llUpdateProf;

    private UserDetailsTemp userDetailsTemp;

    //camera alert dialog controllers
    private TextView txtFromGallery;
    private TextView txtFromCamera;
    private TextView txtCancel;
    private AlertDialog cameraAlert;
    private boolean isFromCamera;
    private int requestPermissionForPhoto = 0;

    private int CAMERA = 1;
    private int GALLERY = 2;

    private String directoryPath = "/sdcard/onBord";
    private String filePath;
    private String croppedFilePath = "";
    private String street1 = "";
    private String street2 = "";
    private String street3 = "";
    private String countryName = "";
    private String region = "";
    private String stateName = "";
    private String cityName = "";
    private String pinCode = "";
    private String address = "";

    private Uri mImageUri;
    int whichAPi=0;
    int UPLOAD_FILE=1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_profile, container, false);
        initialize();
        return view;
    }

    private void initialize() {

        activityTitle = (TextView) getActivity().findViewById(R.id.activityTitle);

        txtSwitchBoard = (TextView) view.findViewById(R.id.txtSwitchBoard);
        txtUsername = (TextView) view.findViewById(R.id.txtUsername);
        txtFBLikesCount = (TextView) view.findViewById(R.id.txtFBLikesCount);
        txtMemberShipNumber = (TextView) view.findViewById(R.id.txtMemberShipNumber);
        txtMemberShipNumberLine = (View) view.findViewById(R.id.txtMemberShipNumberLine);
        txtChapterName = (TextView) view.findViewById(R.id.txtChapterName);
        txtChapterNameLine = (View) view.findViewById(R.id.txtChapterNameLine);
        txtMobile = (TextView) view.findViewById(R.id.txtMobile);
        txtProfileEmail = (TextView) view.findViewById(R.id.txtProfileEmail);
        txtProfileAddress = (TextView) view.findViewById(R.id.txtProfileAddress);
        txtChangePassword = (TextView) view.findViewById(R.id.txtChangePassword);
        txtPaymentDetails = (TextView) view.findViewById(R.id.txtPaymentDetails);
        txtPaymentDetailsLine = (View) view.findViewById(R.id.txtPaymentDetailsLine);
        txtUpgradeMembership = (TextView) view.findViewById(R.id.txtUpgradeMembership);
        txtUpgradeMembershipLine = (View) view.findViewById(R.id.txtUpgradeMembershipLine);
        txtInstituteName = (TextView) view.findViewById(R.id.txtInstituteName);
        txtUpdatProfile = (TextView) view.findViewById(R.id.txtUpdatProfile);
        txtInstituteNameLine = (View) view.findViewById(R.id.txtInstituteNameLine);


        imvEditProfile = (ImageView) view.findViewById(R.id.imvEditProfile);
        imvUserImage = (CircleImageView) view.findViewById(R.id.imvUserImage);

        llUpdateProf = (LinearLayout) view.findViewById(R.id.llUpdateProf);

        imvEditProfile.setOnClickListener(this);
        txtChangePassword.setOnClickListener(this);
        txtPaymentDetails.setOnClickListener(this);
        txtUpgradeMembership.setOnClickListener(this);

        setData();
        manageProfileVisibility();
        customTextView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA && resultCode == getActivity().RESULT_OK) {
            beginCrop(mImageUri);

        } else if (requestCode == GALLERY && resultCode == getActivity().RESULT_OK) {
            Uri selectedImageUri = data.getData();
            filePath = getRealPathFromURI(selectedImageUri);
            beginCrop(selectedImageUri);

        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
            uploadPhoto();
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
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

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
        croppedFilePath = destination.getPath();
        Crop.of(source, destination).asSquare().start(getActivity());
    }

    public void handleCrop(int resultCode, Intent result) {
        if (resultCode == getActivity().RESULT_OK) {
            imvUserImage.post(new Runnable() {
                @Override
                public void run() {
                    Uri uri = Uri.fromFile(new File(croppedFilePath));
                    imvUserImage.setImageURI(uri);
                }
            });

        } else if (resultCode == Crop.RESULT_ERROR) {
            Util.showCenterToast(getActivity(), Crop.getError(result).getMessage());
        }
    }

    private void setData() {
        userDetailsTemp = (UserDetailsTemp) Util.getJsonToClassObject(SharedPref.getUserModelJSON(getActivity()), UserDetailsTemp.class);

        try {
            Glide.with(getActivity())
                    .load(userDetailsTemp.userprofile.ProfileImage)
                    .priority(Priority.IMMEDIATE).placeholder(R.mipmap.ic_default_user)
                    .error(R.mipmap.ic_default_user)
                    .fallback(R.mipmap.ic_default_user)
                    .into(imvUserImage);


            if (userDetailsTemp.userprofile.address != null) {
                if (userDetailsTemp.userprofile.address.AddressL1 != null) {
                    street1 = userDetailsTemp.userprofile.address.AddressL1;
                    address = street1;
                }

                if (userDetailsTemp.userprofile.address.AddressL2 != null) {
                    street2 = userDetailsTemp.userprofile.address.AddressL2;
                    address = address + ", " + street2;
                }

                if (userDetailsTemp.userprofile.address.AddressL3 != null) {
                    street3 = userDetailsTemp.userprofile.address.AddressL3;
                    address = address + ", " + street3;
                }

                if (userDetailsTemp.userprofile.address.Country != null) {
                    countryName = userDetailsTemp.userprofile.address.Country;
                    address = address + ", " + countryName;
                }

                if (userDetailsTemp.userprofile.address.Region != null) {
                    region = userDetailsTemp.userprofile.address.Region;
                    address = address + ", " + region;
                }

                if (userDetailsTemp.userprofile.address.State != null) {
                    stateName = userDetailsTemp.userprofile.address.State;
                    address = address + ", " + stateName;
                }

                if (userDetailsTemp.userprofile.address.City != null) {
                    cityName = userDetailsTemp.userprofile.address.City;
                    address = address + ", " + cityName;
                }

                if (userDetailsTemp.userprofile.address.Pincode != null) {
                    pinCode = userDetailsTemp.userprofile.address.Pincode;
                    address = address + ", " + pinCode;
                }

                if (address.length() == 0) {
                    txtProfileAddress.setText("");
                } else {
                    txtProfileAddress.setText("" + address);
                }
            } else {
                txtProfileAddress.setText("");
            }


            txtUsername.setText("" + userDetailsTemp.userprofile.FullName);
            txtFBLikesCount.setText("" + userDetailsTemp.userprofile.FacebookLikes + " Likes");
            txtMemberShipNumber.setText("Membership No:" + userDetailsTemp.userprofile.MembershipNumber);
            txtChapterName.setText("" + userDetailsTemp.userprofile.ChapterName);
            txtMobile.setText("" + userDetailsTemp.userprofile.MobileNumber);
            txtProfileEmail.setText("" + userDetailsTemp.userprofile.Email);
            if(userDetailsTemp.RolesTmp.size()>1&&!(userDetailsTemp.Roles.get(0).equalsIgnoreCase(Constants.ROLL_MEMBER)))
            {
                txtSwitchBoard.setVisibility(View.VISIBLE);
                txtSwitchBoard.setOnClickListener(this);
            }
            else
                txtSwitchBoard.setVisibility(View.GONE);
            if(userDetailsTemp.Roles.get(0).equalsIgnoreCase(Constants.ROLL_NON_MEMBER)||userDetailsTemp.Roles.get(0).equalsIgnoreCase(Constants.ROLL__STUDENT))
            {
                imvEditProfile.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activityTitle.setText(getResources().getString(R.string.my_profile));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imvEditProfile) {
//            Util.showCenterToast(getActivity(), "Under Development! ");
            selectPhotoVideoAlert();
        } else if (view.getId() == R.id.txtChangePassword) {
            Util.startActivity(getActivity(), ChangePassActivity.class);
        } else if (view.getId() == R.id.txtFromGallery) {
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

        } else if (view.getId() == R.id.txtFromCamera) {
            cameraAlert.dismiss();
            isFromCamera = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkAndRequestPermissions()) {
                    txtFromCamera();
                }
            } else {
                txtFromCamera();
            }
        } else if (view.getId() == R.id.txtCancel) {
            cameraAlert.dismiss();
        } else if (view.getId() == R.id.txtPaymentDetails) {
            Util.startActivity(getActivity(), RenewalAct.class);
        } else if (view.getId() == R.id.txtUpgradeMembership) {
            if (Util.isDeviceOnline(getActivity(), true)) {
                String url = "http://www.ishraehq.in/home/index";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        }

        else if(view.getId()==R.id.txtSwitchBoard)
        {
            userDetailsTemp.Roles.clear();
            userDetailsTemp.RolesTmp.clear();
            userDetailsTemp.Roles.add(Constants.ROLL_CHAPTER);
            saveUserData();
            Util.startActivityWithFinish(getActivity(), DashboardActivity.class);
        }
    }

    private void customTextView() {
        String mainText=getResources().getString(R.string.Update_your_Profile_online)+" "+AppUrls.UPDATE_PROFILE_URL;
        txtUpdatProfile.setText(mainText);
        SpannableString ss = new SpannableString(txtUpdatProfile.getText().toString());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                String url = AppUrls.UPDATE_PROFILE_HTTP_URL;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };
        ss.setSpan(clickableSpan, getResources().getString(R.string.Update_your_Profile_online).length()+1, mainText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new StyleSpan(Typeface.BOLD), getResources().getString(R.string.Update_your_Profile_online).length()+1,mainText.length(), 0);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.toolbar_bg)), getResources().getString(R.string.Update_your_Profile_online).length()+1,mainText.length(), 0);

        txtUpdatProfile.setText(ss);
        txtUpdatProfile.setMovementMethod(LinkMovementMethod.getInstance());
        txtUpdatProfile.setHighlightColor(Color.TRANSPARENT);
    }

    private void selectPhotoVideoAlert() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setCancelable(false);
        LayoutInflater inflater = getActivity().getLayoutInflater();
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
        int cameraPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        int writeExternalPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readExternalPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int recordAudioPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO);

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

                    } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)
                            || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

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
                        Toast.makeText(getActivity(), "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Check for both permissions
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        txtFromGallery();

                    } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

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
                        Toast.makeText(getActivity(), "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
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
        getActivity().startActivityForResult(Intent.createChooser(intent, "Select"), GALLERY);
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
        getActivity().startActivityForResult(cameraIntent, CAMERA);
    }


    public void manageProfileVisibility() {

        if (Util.getUserRole(getActivity()).equalsIgnoreCase(Constants.ROLL_MEMBER)) {
            llUpdateProf.setVisibility(View.VISIBLE);
        }

        if (Util.getUserRole(getActivity()).equalsIgnoreCase(Constants.ROLL_NON_MEMBER)) {
            txtMemberShipNumber.setVisibility(View.GONE);
            txtMemberShipNumberLine.setVisibility(View.GONE);
            txtChapterNameLine.setVisibility(View.GONE);
            txtChapterName.setVisibility(View.GONE);
            txtPaymentDetailsLine.setVisibility(View.GONE);
            txtPaymentDetails.setVisibility(View.GONE);
            txtChangePassword.setVisibility(View.GONE);

            txtUpgradeMembership.setVisibility(View.VISIBLE);
            txtUpgradeMembershipLine.setVisibility(View.VISIBLE);

        } else {
            if (Util.getUserRole(getActivity()).equalsIgnoreCase(Constants.ROLL__STUDENT)) {
                txtInstituteName.setVisibility(View.VISIBLE);
                txtInstituteNameLine.setVisibility(View.VISIBLE);
                txtInstituteName.setText("" + userDetailsTemp.userprofile.InstituteName);
            } else {
                txtInstituteName.setVisibility(View.GONE);
                txtInstituteNameLine.setVisibility(View.GONE);
            }
            txtMemberShipNumber.setVisibility(View.VISIBLE);
            txtMemberShipNumberLine.setVisibility(View.VISIBLE);
            txtChapterNameLine.setVisibility(View.VISIBLE);
            txtChapterName.setVisibility(View.VISIBLE);
            txtPaymentDetailsLine.setVisibility(View.VISIBLE);
            txtPaymentDetails.setVisibility(View.VISIBLE);
            txtChangePassword.setVisibility(View.VISIBLE);

            txtUpgradeMembership.setVisibility(View.GONE);
            txtUpgradeMembershipLine.setVisibility(View.GONE);
        }

    }

    @Override
    public void onFailure(Call call, IOException e) {
        Util.manageFailure(getActivity(), e);
    }


    @Override
    public void onResponse(Call call, Response response) throws IOException {

        Util.dismissProgressDialog();
        final JSONObject jsonObject = Util.getObjectFromResponse(response);
        try {
            if (jsonObject != null && jsonObject.getInt(Constants.FLD_RESPONSE_CODE) == 1) {
                String responseData = jsonObject.getString(Constants.FLD_RESPONSE_DATA);
                if (whichAPi == UPLOAD_FILE) {
                    userDetailsTemp.userprofile.ProfileImage=responseData;
                    saveUserData();
//                    Gson gson = new Gson();
//                    String jsonString =gson.toJson(userDetailsTemp);
//                    SharedPref.setUserModelJSON(getActivity(),jsonString);
//
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.showCenterToast(getActivity(), Util.getMessageFromJObj(jsonObject));
                            setData();
                        }
                    });

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void saveUserData() {
        Gson gson = new Gson();
        String jsonString =gson.toJson(userDetailsTemp);
        SharedPref.setUserModelJSON(getActivity(),jsonString);

    }

    private void uploadPhoto() {
        if (Util.isDeviceOnline(getActivity(), true)) {
            whichAPi = UPLOAD_FILE;
            NetworkManager.init(getActivity(), this);
            NetworkManager.addFileForUploading(Constants.FIELD_FILE1, filePath, null, Constants.FLD_USER_PROFILE_IMAGE);
            NetworkManager.uploadFileToServer(getActivity(), this, null, AppUrls.UPLOAD_FILE_URL, true);
        }
    }
}
