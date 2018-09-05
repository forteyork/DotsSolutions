package nuttapon.dots.co.th.dotssolutions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler;


public class QRFragment extends Fragment implements ResultHandler{

    private ZXingScannerView zXingScannerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        zXingScannerView = new ZXingScannerView(getActivity());
        return zXingScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();

    }

    @Override
    public void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {

        String resultString = result.getText().toString().trim();
        Log.d("5SepV3", "QR result ==>" + resultString);

        if (!resultString.isEmpty()){

            resultString = resultString.substring(17, 28);

            Intent intent = new Intent(getActivity(), ServiceActivity.class);
            intent.putExtra("QRcode", resultString);
            getActivity().setResult(100, intent);
            getActivity().finish();

        }

    }
}
