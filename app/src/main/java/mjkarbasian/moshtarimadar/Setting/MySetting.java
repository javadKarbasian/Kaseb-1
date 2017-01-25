package mjkarbasian.moshtarimadar.Setting;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import mjkarbasian.moshtarimadar.Helpers.Utility;
import mjkarbasian.moshtarimadar.Others.DrawerActivity;
import mjkarbasian.moshtarimadar.R;

public class MySetting extends DrawerActivity {

    //region declare values
    private static final int GALLERY_ACTIVITY_CODE = 2;
    private static final int RESULT_CROP = 3;
    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
    ImageView mCustomerAvatar;
    Bitmap photo;
    //endregion declare values

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentManager.beginTransaction().replace(R.id.container, new PreferenceHeader()).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == GALLERY_ACTIVITY_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                String picturePath = data.getStringExtra("picturePath");
//                //perform Crop on the Image Selected from Gallery
//                performCrop(picturePath);
//            }
//        }
//        if (requestCode == RESULT_CROP) {
//            if (resultCode == Activity.RESULT_OK) {
//                if (data.getExtras() != null) {
//                    photo = data.getExtras().getParcelable("data");
//                    mCustomerAvatar.setImageBitmap(photo);
//
//                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                    photo.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
//                    byte[] imagegBytes = byteArrayOutputStream.toByteArray();
//
//                    ContentValues customerValues = new ContentValues();
//                    customerValues.put(KasebContract.Customers.COLUMN_CUSTOMER_PICTURE, imagegBytes);
//
//                } else if (data.getData() != null) {
//                    Uri picUri = data.getData();
//                    BufferedInputStream bufferInputStream = null;
//                    try {
//                        URLConnection connection = new URL(picUri.toString()).openConnection();
//                        connection.connect();
//                        bufferInputStream = new BufferedInputStream(connection.getInputStream(), 8192);
//                        photo = BitmapFactory.decodeStream(bufferInputStream);
//
//                        mCustomerAvatar.setImageBitmap(photo);
//
//                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                        photo.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
//                        byte[] imagegBytes = byteArrayOutputStream.toByteArray();
//
//                        ContentValues customerValues = new ContentValues();
//                        customerValues.put(KasebContract.Customers.COLUMN_CUSTOMER_PICTURE, imagegBytes);
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    Toast toast = Toast.makeText(this, R.string.problem_in_crop_image, Toast.LENGTH_LONG);
//                    toast.show();
//                }
//            }
//        }
//    }

//    public void pic_selector_on_profile_kaseb(View view) {
//        mCustomerAvatar = (ImageView) view;
//        Intent gallery_Intent = new Intent(getBaseContext(), GalleryUtil.class);
//        startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);
//    }

    private void performCrop(String picUri) {
        try {
            //Start Crop Activity

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            File f = new File(picUri);
            Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 280);
            cropIntent.putExtra("outputY", 280);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, RESULT_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            Toast toast = Toast.makeText(getBaseContext(), R.string.does_not_support_crop_action, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (fragmentManager.findFragmentByTag("typeFragments").isResumed())
                super.onBackPressed();
            else
                Utility.activityOnBackExit(this);

        } catch (Exception e) {
            Utility.activityOnBackExit(this);
        }

    }
}
