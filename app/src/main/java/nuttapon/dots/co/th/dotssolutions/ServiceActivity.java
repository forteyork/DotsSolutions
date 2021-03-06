package nuttapon.dots.co.th.dotssolutions;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class ServiceActivity extends AppCompatActivity {
    private String jsonUserString, nameAnSurString, phoneString;    // ประกาศ jason
    private MyConstant myConstant = new MyConstant();
    private String[] columnStrings=myConstant.getColumnTcust();
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

 //       Get Value From Preference  ดึงข้อมูล
          getValueFromPreference();

//          Create ToolBar
        createToolBar();

//        Create Menu
        createMenu();


//        Add Fragment
        addFragment(savedInstanceState);

    }  // Main Method

    private void addFragment(Bundle savedInstanceState) {
        if(savedInstanceState==null){

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.contentServiceFragment,new DassBoardFragment())
                    .commit();

        }
    }

    private void createMenu() {
        ListView listView=findViewById(R.id.ListViewMenu);
        MenuAdapter menuAdapter=new MenuAdapter(ServiceActivity.this,
                myConstant.getTitleMenuStrings(),myConstant.getIconInts());
        listView.setAdapter(menuAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                moveToFragment(position);
            }
        });

    }

    private void moveToFragment(int position) {
        switch (position){
            case 0:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentServiceFragment,new DassBoardFragment())
                        .commit();
                drawerLayout.closeDrawers();
                break;
            case 1:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentServiceFragment,new PackageFragment())
                        .commit();
                drawerLayout.closeDrawers();
                break;
            case 2:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentServiceFragment,new EbillFragment())
                        .commit();
                drawerLayout.closeDrawers();
                break;
            case 3:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentServiceFragment,new BillingCyclerFragment())
                        .commit();
                drawerLayout.closeDrawers();
                break;
            case 4:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentServiceFragment,new ServiceFragment())
                        .commit();
                drawerLayout.closeDrawers();
                break;
            case 5:
                finish();
                break;

        }
    }

    private void createToolBar() {
        Toolbar toolbar=findViewById(R.id.toolbarService);
        setSupportActionBar(toolbar);

//Setup Name
        String firstName=findValueFromJSON(columnStrings[1]);
        String lastName = findValueFromJSON(columnStrings[2]);
        nameAnSurString= firstName+ " " + lastName;
        getSupportActionBar().setSubtitle(nameAnSurString);

// Setup Phone


        phoneString=findValueFromJSON(columnStrings[3]);
        phoneString= phoneString.trim();                   // ตัดช่องว่างในประโยคออก
        if(phoneString.length() != 11 ){
            String s=phoneString;
            phoneString=s.substring(0, 11) + "\n" + s.substring( 11, s.length());   // ตัดคำตั้งแต่ตำแหน่งที่ 0-11 ส่วน 12 ให้ \n ขึ้นบันทัดใหม่
        }


        TextView textView =findViewById(R.id.txtPhone);
        textView.setText(phoneString);

        //Create Hamberger  สร้าง Hamberger
        drawerLayout =findViewById(R.id.drawerService);
        actionBarDrawerToggle=new ActionBarDrawerToggle(ServiceActivity.this,
                drawerLayout, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_hamberger);

    }// Craate Toolbar

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        actionBarDrawerToggle.syncState();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        actionBarDrawerToggle.onConfigurationChanged(newConfig);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);


    }
    private String findValueFromJSON(String columnString) {

        String result= null;

        try{
            JSONArray jsonArray=new JSONArray(jsonUserString);
            JSONObject jsonObject =jsonArray.getJSONObject(0);
            result=jsonObject.getString(columnString);
            return result;

        }catch (Exception e){
            e.printStackTrace();
            return "Non";
        }

    }

    private void getValueFromPreference() {
        SharedPreferences sharedPreferences =getSharedPreferences("MyData", MODE_PRIVATE);
        jsonUserString=sharedPreferences.getString("User","");  // ถ้าดึงข้อมูลไม่ได้ให้แสดงเป็นค่าว่างเปล่า
        Log.d("4SepV1","jsonUserString ==> " + jsonUserString);   // ดู Log


    }


}   // Main Class
