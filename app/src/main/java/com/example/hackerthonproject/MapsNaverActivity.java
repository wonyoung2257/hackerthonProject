package com.example.hackerthonproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hackerthonproject.Retrofit.RetrofitAPI;
import com.example.hackerthonproject.Retrofit.RetrofitCall;
import com.example.hackerthonproject.dto.LocationDto;
import com.example.hackerthonproject.dto.UserDto;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapsNaverActivity extends Activity implements OnMapReadyCallback, View.OnClickListener, Overlay.OnClickListener {

    MapView mapView;
    TextView test1;
    TextView mapInfo_MyPage;
    TextView txt2;

    String QR_Scan_Message = "QR 코드가 스캔되었습니다.";
    String MyPage_Message = "마이페이지 정보가 조회되었습니다.";

    IntentIntegrator integrator;

    ImageButton reits, QR_Scan, myPage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.naver_activity_maps);

        mapView = findViewById(R.id.mapView);
        mapView.getMapAsync(this);

        reits = findViewById(R.id.reits);
        myPage = findViewById(R.id.myPage);

        myPage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsNaverActivity.this, PopupActivity.class);
                startActivity(intent);
            }
        });

//         메시지 한 가운데로
//         mapInfo_MyPage.setGravity(Gravity.CENTER);

    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        Intent intent = getIntent();

        double latitude = intent.getDoubleExtra("latitude", 0);
        double longitude = intent.getDoubleExtra("longitude", 0);
        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        locationOverlay.setVisible(true);
        locationOverlay.setPosition(new LatLng(latitude, longitude));

         RetrofitCall retrofit = new RetrofitCall();

        RetrofitAPI retrofitAPI = retrofit.getRetrofit().create(RetrofitAPI.class);

        Call<List<LocationDto>> call = retrofitAPI.getLocationList();

        List<LocationDto> locationsList = new ArrayList<LocationDto>();
//
        call.enqueue(new Callback<List<LocationDto>>() {
            @Override
            public void onResponse(Call<List<LocationDto>> call, Response<List<LocationDto>> response) {
                //response 확인
                if (response.code() != 200) {
                    return;
                }
                for(int i = 0; i < response.body().size(); i++){
                    Marker marker1 = new Marker();
                    marker1.setPosition(new LatLng( Double.parseDouble(response.body().get(i).getLatitude()), Double.parseDouble(response.body().get(i).getLongitude())));
                    marker1.setMap(naverMap);
                    marker1.setOnClickListener(MapsNaverActivity.this::onClick);
                }
                Log.d("IDIDID", response.body().get(0).getAddress());
            }

            @Override
            public void onFailure(Call<List<LocationDto>> call, Throwable t) {
                Log.wtf("err123", t);
                Log.d("IDIDID", "5시작");
                System.out.println("Tlqkf1");
            }

        });

        LatLng coord = new LatLng(37.5670135, 126.9783740);

        Toast.makeText(this,
                "위도: " + coord.latitude + ", 경도: " + coord.longitude,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onClick(@NonNull Overlay overlay) {
        if (overlay instanceof Marker) {

            Toast.makeText(this.getApplicationContext(), "마커가 선택되었습니다", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    public void QR_Scan(View view){

        Intent intent = new Intent(MapsNaverActivity.this, QrScanActivity.class) ;
        startActivity(intent);

    }

    public void reits(View view){

    }

    public void myPage(View view){
//        Intent intent = new Intent(MapsNaverActivity.this, PopupActivity.class) ;
//        startActivity(intent) ;
    }

    @Override
    public void onClick(View v) {

    }
}
//myPage 메서드 안에 있던 거
// 기존의 팝업창 구현
//        // mapInfo_MyPage.setText(MyPage_Message);
//
//        Context mContext = getApplicationContext();
//        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
//
//        View layout = inflater.inflate(R.layout.activity_mypage, (ViewGroup) findViewById(R.id.popup));
//        AlertDialog.Builder dialog = new AlertDialog.Builder(MapsNaverActivity.this);
//
//        dialog.setTitle("MyPage");
//        dialog.setView(layout);
//
//        dialog.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                }
//        });
//
//        AlertDialog ad = dialog.create();
//        ad.show();

// 새 창으로 구현