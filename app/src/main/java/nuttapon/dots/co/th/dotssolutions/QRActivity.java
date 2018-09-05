package nuttapon.dots.co.th.dotssolutions;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class QRActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        if (savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.contentQRFragment, new QRFragment())
                    .commit();
        }

    }
}
