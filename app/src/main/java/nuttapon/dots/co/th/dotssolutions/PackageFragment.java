package nuttapon.dots.co.th.dotssolutions;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class PackageFragment extends Fragment {

    private MyConstant myConstant = new MyConstant();
    private MyAlert myAlert;
    private String displayNameString, genderString, ageString, latString, lngSting, imagePathString;
    private boolean genderABoolean = true, ageABoolean = true;
    private Uri cameraUri;





    public PackageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myAlert = new MyAlert(getActivity());

//        upload controller
        uploadController();

//        radioGroup
        RadioGroup();

//        spinner controller
        spinnerController();

//        point controller
        pointController();

//        Gallery Controller
        galleryController();

//        camera controller
        cameraController();


    } // Main Method

    private void cameraController() {
        ImageView imageView = getView().findViewById(R.id.imvCamera);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Setup Path
                String pathString = Environment.getExternalStorageDirectory() + "/" + "Dots";
                Log.d("6SepV2", "Path ==>" + pathString);

                File file = new File(pathString);
                if (!file.exists()) {
                    file.mkdirs();
                }
//                Setup name File
                Random random = new Random();
                int i = random.nextInt(1000);
                File cameraFile1 = new File(file, "dots_" + Integer.toString(i) + "jpg");

                cameraUri = Uri.fromFile(cameraFile1);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
                startActivityForResult(intent, 150);

            } // onclick
        });
    }

    private void galleryController() {
        ImageView imageView = getView().findViewById(R.id.imvGallery);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 100);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



            switch (requestCode) {
                case 50:
                    double latADouble = data.getDoubleExtra("Lat", 0);
                    double lngADouble = data.getDoubleExtra("Lng", 0);
                    Log.d("6SepV1", "Lat Receive ==>" + latADouble);
                    Log.d("6SepV1", "Lng Receive ==>" + lngADouble);

                    TextView latTextView = getView().findViewById(R.id.txtLat);
                    TextView lngTextView = getView().findViewById(R.id.txtLng);
                    latTextView.setText("Lat = " + Double.toString(latADouble));
                    lngTextView.setText("Lng = " + Double.toString(lngADouble));

                    break;
                case 100:
                    if (resultCode == getActivity().RESULT_OK) {
                        Uri uri = data.getData();
                        showPhoto(cameraUri);
                    } else {
                        myAlert.normalDialog("Not Photo", "Please Choose photo on gallery");

                    }

                    break;
                case 150:
                    if (resultCode == getActivity().RESULT_OK) {
                        showPhoto(cameraUri);

                    } else {
                        myAlert.normalDialog("No Photo", "Please take a photo");
                    }
            }








    } // onActivityResult

    private void showPhoto(Uri uri) {

        try{

            String[] strings = new String[]{MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(uri, strings, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                int i = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                imagePathString = cursor.getString(i);
            } else {
                imagePathString = uri.getPath();
            }
            Log.d("6SepV3", "imagePath ==>" + imagePathString);


            Bitmap bitmap = BitmapFactory.decodeStream(getActivity()
                    .getContentResolver()
                    .openInputStream(uri));

            Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, 800, 600, false);
            ImageView imageView = getView().findViewById(R.id.imvPhoto);
            imageView.setImageBitmap(bitmap);

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void pointController() {
        ImageView imageView = getView().findViewById(R.id.imvPoint);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivityForResult(intent, 50);

            }
        });
    }

    private void spinnerController() {
        Spinner spinner = getView().findViewById(R.id.spinnerAge);

                final String[] ageStrings = myConstant.getAgeStrings();
                ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_expandable_list_item_1, ageStrings);
                spinner.setAdapter(stringArrayAdapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        if (position != 0) {
                            ageABoolean = false;
                            ageString = ageStrings[position];
                        } else {
                            ageABoolean = true;
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        ageABoolean =true;
                    }
                });
    }

    private void RadioGroup() {
        RadioGroup radioGroup = getView().findViewById(R.id.radGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId){
                    case R.id.radMale:
                        genderString = "Male";
                        break;
                    case R.id.radFemale:
                        genderString = "Female";
                        break;
                }

                genderABoolean = false;
            }
        });
    }

    private void uploadController() {
        Button button = getView().findViewById(R.id.btnUpload);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Get Value From EditText

                EditText editText = getView().findViewById(R.id.editDisplayName);
                displayNameString = editText.getText().toString().trim();

                if (displayNameString.isEmpty()){
                    myAlert.normalDialog(getString(R.string.title_no_gender),
                            getString(R.string.message_have_space));

                } else if (genderABoolean){

                    myAlert.normalDialog(getString(R.string.title_no_gender), getString(R.string.message_no_gender));
                } else if (ageABoolean) {
                    myAlert.normalDialog("No Age",
                            "Please Choose Age");
                } else {
                    uploadPhotoToServer();
                }





            } // on click

            private void uploadPhotoToServer() {

            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_package, container, false);
    }

}
