package com.example.bookinghotel.Fragment;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

import com.example.bookinghotel.Adapter.HotelAdapter;
import com.example.bookinghotel.R;
import com.example.bookinghotel.Screen.Home.Home;
import com.example.bookinghotel.Screen.Login.Login_Signin;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.example.bookinghotel.entity.Hotel;
import com.example.bookinghotel.entity.User;
import com.example.bookinghotel.entity.Room;
import com.example.bookinghotel.entity.Type;
import com.example.bookinghotel.entity.Rating;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText etDiemden;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private FusedLocationProviderClient client;
    LocationManager locationManager;
    LocationRequest request;

    // Hotel Adapter
    private ArrayList<Hotel> hotels;
    private HotelAdapter adapter;
    private RecyclerView recyclerView;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_home, container, false);
        String latitude, longitude;
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        etDiemden = rootView.findViewById(R.id.etDiemden);

        recyclerView = rootView.findViewById(R.id.recyclerView_hotel);
        initHotels();
        adapter = new HotelAdapter(getActivity(),hotels);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

//    Khởi tạo dữ liệu giả để test
    private void initHotels() {
        hotels = new ArrayList<>();

        for (int i = 0; i < 60; i++) {
            ArrayList<String> list = new ArrayList<String>();
            list.add("sdfsd");
            Type t = new Type(123123,123123,1231,list);
            Room r = new Room(t,t);
            Rating ra = new Rating("qweqwe","qweqwe",12312,"sdfsdf");
            ArrayList<Rating> listRating = new ArrayList<>();
            listRating.add(ra);
            hotels.add(new Hotel(123,23423,23423, "TRAN HUYNH HOTEL",r,listRating,"1231"));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        etDiemden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(getActivity(), view);

                menu.getMenu().add("Hồ Chí Minh");
                menu.getMenu().add("Hà Nội");
                menu.getMenu().add("Đà Nẵng");

                menu.show();

                //registering popup with OnMenuItemClickListener
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        etDiemden.setText(item.getTitle());
                        return true;
                    }
                });
            }
        });



//        read location
//        btnLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                    if (!hasPermission(LOCATION)) {
//                        requestLocation(view);
//                    }
//                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                        requestLocation(view);
//
//                    } else {
//                        try {
//                            getLocation();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//
//        });

    }

}