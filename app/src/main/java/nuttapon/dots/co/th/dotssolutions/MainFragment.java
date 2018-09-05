package nuttapon.dots.co.th.dotssolutions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class MainFragment extends Fragment{

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Login Controller
        loginController();

        // QR Controller
        QRController();


    }// Main Method


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String qrString = data.getStringExtra("QRcode");
        Log.d("5SepV3", "QRcode ==>" + qrString);

        checkAuthentication(qrString);

    }

    private void QRController() {
        Button button = getView().findViewById(R.id.btnQR);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), QRActivity.class);
                startActivityForResult(intent, 100);


            }
        });
    }

    private void loginController() {
        Button button=getView().findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText=getView().findViewById(R.id.edtIdCustomer);
                String idCustomerString=editText.getText().toString().trim();
                MyAlert myAlert=new MyAlert(getActivity());
                MyConstant myConstant =new MyConstant();
                String[] columnString=myConstant.getColumnTcust();  // เอาตัวแปล Array มาอยู่ที่ String ตัวนี้
                String[] userLoginString=new String[columnString.length]; //



                if (idCustomerString.isEmpty()) { /// ความว่างเปล่า
               //     Have Space

                    myAlert.normalDialog("Have Space",
                            "Please Fill ID Customer");

                } else if (idCustomerString.length()!=11) {  ////  นับจำนวนคำว่าเท่ากับ 11 หรือไม่
                    myAlert.normalDialog("ID Customer False",
                            "Please Fill ID Customer only 11 Digi");
                } else {

                    checkAuthentication(idCustomerString);



                }  //if


            }
        });
    }

    private void checkAuthentication(String idCustomerString) {
        MyAlert myAlert1=new MyAlert(getActivity());
        MyConstant myConstant1 =new MyConstant();

        try {



        GetUserWhereIdCustomer getUserWhereIdCustomer=new GetUserWhereIdCustomer(getActivity());
        getUserWhereIdCustomer.execute(idCustomerString,myConstant1.getUrlGetUserWhereCustID());
        String jsonString = getUserWhereIdCustomer.get();
            Log.d("3SepV1","json ==> " + jsonString);

            if (jsonString.equals("null")) {
                myAlert1.normalDialog("No ID Customer",
                        "No "+ idCustomerString + " in my Database");
            } else {

                SharedPreferences sharedPreferences =getActivity()
                        .getSharedPreferences("MyData", Context.MODE_PRIVATE);//  SharedPreferences จะสร้าง Text File ขึ้นมาแล้วเอาค่าที่ Json ดึงออกมาทั้งหมดมาเก็บไว้
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("User",jsonString);
                editor.commit();

                startActivity(new Intent(getActivity(),ServiceActivity.class));
                getActivity().finish();
            }

        }catch (Exception e){
            e.printStackTrace();

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }
}