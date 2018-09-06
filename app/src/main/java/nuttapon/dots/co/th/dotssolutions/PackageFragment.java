package nuttapon.dots.co.th.dotssolutions;


import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class PackageFragment extends Fragment {

    private MyConstant myConstant = new MyConstant();
    private MyAlert myAlert;
    private String displayNameString, genderString, ageString;
    private boolean genderABoolean = true, ageABoolean = true;




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

    } // Main Method

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
                } else if (ageABoolean){
                    myAlert.normalDialog("No Age",
                            "Please Choose Age");
                }





            } // on click
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_package, container, false);
    }

}
