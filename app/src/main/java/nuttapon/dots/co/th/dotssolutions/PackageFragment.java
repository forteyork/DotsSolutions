package nuttapon.dots.co.th.dotssolutions;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class PackageFragment extends Fragment {

    private MyConstant myConstant = new MyConstant();
    private MyAlert myAlert;
    private String displayNameString;


    public PackageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myAlert = new MyAlert(getActivity());
//        upload controller
        uploadController();

    } // Main Method

    private void uploadController() {
        Button button = getView().findViewById(R.id.btnUpload);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Get Value From EditText

                EditText editText = getView().findViewById(R.id.editDisplayName);
                displayNameString = editText.getText().toString().trim();

                if (displayNameString.isEmpty()){
                    myAlert.normalDialog(getString(R.string.title_have_space),
                            getString(R.string.message_have_space));

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
