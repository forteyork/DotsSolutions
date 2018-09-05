package nuttapon.dots.co.th.dotssolutions;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class DassBoardFragment extends Fragment {

    private MyConstant myConstant=new MyConstant();
    private String[] columnTcust=myConstant.getColumnTcust();  // ประกาศตัวแปลเพื่อรับค่าจาก JSON
    private String[] valueUserStrings = new String[columnTcust.length];
    private String balanceAString, balanceBString, totalString;




    public DassBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        GetValue SharePreferrence
        getValueFromSharePreference();

//        Show Balance
        showBalance();



    }  // Main Method

    private void getValueFromSharePreference() {
        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String jsonUser = sharedPreferences.getString("User", "");

        try{
            JSONArray jsonArray = new JSONArray(jsonUser);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            for (int i=0; i<columnTcust.length; i+=1) {
                valueUserStrings[i] = jsonObject.getString(columnTcust[i]);
                Log.d("5SepV1", "valueUser[" + i + "] ==>" + valueUserStrings[i]);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showBalance() {
        TextView companyATextView=getView().findViewById(R.id.txtBalanceA);
        TextView companyBTextView=getView().findViewById(R.id.txtBalanceB);
        TextView totalTextView=getView().findViewById(R.id.txtTotal);

        try{

//            For find balance A

            GetUserWhereIdCustomer getUserWhereIdCustomer = new GetUserWhereIdCustomer(getActivity());
            getUserWhereIdCustomer.execute(valueUserStrings[0], myConstant.getUrlGetBalanceAWhereCustIDAndIsCancel());

            String resultJSON = getUserWhereIdCustomer.get();
            Log.d("5SepV1", "resultJSON ==>" + resultJSON);

            JSONArray jsonArray = new JSONArray(resultJSON);
            Log.d("5SepV2", "จำนวนของข้อมูล ==>" + jsonArray.length());

            JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length() -1);

            balanceAString = jsonObject.getString("NetTotal");
            companyATextView.setText(balanceAString + "THB.");

//            For find balance B
            balanceBString = "0";
            companyBTextView.setText(balanceBString + " THB.");

//            For find total
            int totalInt = Integer.parseInt(balanceAString.trim()) + Integer.parseInt(balanceBString.trim());
            totalString = Integer.toString(totalInt);
            totalTextView.setText(totalString + "THB.");

        } catch (Exception e){
            e.printStackTrace();
        }

    } // show balance

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dass_board, container, false);
    }

}
