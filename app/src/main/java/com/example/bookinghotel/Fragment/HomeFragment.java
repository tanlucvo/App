package com.example.bookinghotel.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bookinghotel.Adapter.HotelAdapter;
import com.example.bookinghotel.R;
import com.example.bookinghotel.Screen.Home.Home;
import com.example.bookinghotel.Screen.Login.Login_Signin;
import com.example.bookinghotel.entity.Booked;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Collectors;

import com.example.bookinghotel.entity.Hotel;
import com.example.bookinghotel.entity.User;
import com.example.bookinghotel.entity.Room;
import com.example.bookinghotel.entity.Type;
import com.example.bookinghotel.entity.Rating;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
    ProgressBar progressBar_cyclic;
    Button btnFind;
    EditText etDiemden;
    TextView tvRoomSize,tvDate;
    LinearLayout llDate;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private SharedPreferences pref;
    private FusedLocationProviderClient client;
    LocationManager locationManager;
    LocationRequest request;

    // Hotel Adapter
    private ArrayList<Hotel> hotels = new ArrayList<Hotel>();
    private HotelAdapter adapter;
    private RecyclerView recyclerView;
    Long startDate = 1606867200000L;
    Long endDate = 1607126400000L;
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

//        RoundedBitmapDrawable img = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
//        img.setCornerRadius(radius);
//
//        imageView.setImageDrawable(img);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        String latitude, longitude;
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        etDiemden = rootView.findViewById(R.id.etDiemden);
        btnFind = rootView.findViewById(R.id.btnFind);
        recyclerView = rootView.findViewById(R.id.recyclerView_hotel);
        progressBar_cyclic = rootView.findViewById(R.id.progressBar_cyclic);
        tvRoomSize = rootView.findViewById(R.id.tvRoomSize);
        llDate =rootView.findViewById(R.id.llDate);
        tvDate = rootView.findViewById(R.id.tvDate);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        pref = this.getActivity().getSharedPreferences("HotelPref", Context.MODE_PRIVATE);
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = pref.edit();
        String city = etDiemden.getText().toString();
        String room = tvRoomSize.getText().toString();
        String date = tvDate.getText().toString();

        editor.putString("city", city);
        editor.putString("date", date);
        editor.putString("room", room);
        Gson gson = new Gson();
        String myJson = gson.toJson(hotels);
        editor.putString("hotel", myJson);
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        String city = pref.getString("city","Hồ Chí Minh");
        String room = pref.getString("room","Medium");
        String date = pref.getString("date","2/12-5/12");
        String hotel_pref = pref.getString("hotel",null);
        if (hotel_pref !=null){
            Gson gson = new Gson();
            hotels = gson.fromJson(hotel_pref, new TypeToken<List<Hotel>>(){}.getType());
            adapter = new HotelAdapter(getActivity(), hotels);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        etDiemden.setText(city);
        tvRoomSize.setText(room);
        tvDate.setText(date);
        if(hotel_pref!= null){
            boolean connected = false;
            ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
            }
            else
                connected = false;
//            if(connected){
//                btnFind.performClick();
//
//            }
        }


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Hotel/HoChiMinh");
        llDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateFormat date = new SimpleDateFormat("dd/MM");
//                String localTime = date.format(currentLocalTime);
                MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
                MaterialDatePicker<Pair<Long, Long>> picker = builder.build();
                picker.show(getFragmentManager(), picker.toString());
                picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override public void onPositiveButtonClick(Pair<Long,Long> selection) {
                        startDate = selection.first;
                        endDate = selection.second;
                        tvDate.setText(date.format(startDate)+"-"+date.format(endDate));
                    }
                });

            }
        });
        tvRoomSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), v);

                popupMenu.inflate(R.menu.menu_typeroom);
                Menu menu = popupMenu.getMenu();
                popupMenu.show();

                //registering popup with OnMenuItemClickListener
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        tvRoomSize.setText(item.getTitle());

                        return true;
                    }
                });

            }
        });
        etDiemden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);

                popupMenu.inflate(R.menu.menu_city);
                Menu menu = popupMenu.getMenu();
                popupMenu.show();

                //registering popup with OnMenuItemClickListener
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        etDiemden.setText(item.getTitle());

                        return true;
                    }
                });

            }
        });
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mDatabase = null;
                progressBar_cyclic.setVisibility(View.VISIBLE);
                adapter = new HotelAdapter(getActivity(), hotels);
                recyclerView.setAdapter(adapter);
                String city = etDiemden.getText().toString();
//                Log.e("test", startDate+" "+endDate);
                hotels.clear();
                if (city.trim().equals("")) {
                    return;
                } else {
                    if (city.equals("Hồ Chí Minh")) {
                        mDatabase = FirebaseDatabase.getInstance().getReference("Hotel/HoChiMinh");
                    } else if (city.equals("Đà Nẵng")) {
                        mDatabase = FirebaseDatabase.getInstance().getReference("Hotel/DaNang");
                    }else if (city.equals("Hà Nội")) {
                        mDatabase = FirebaseDatabase.getInstance().getReference("Hotel/HaNoi");
                    }


                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                                Hotel hotel = postSnapshot.getValue(Hotel.class);
                                hotel.setPath(postSnapshot.getRef().toString().replace("https://hotelbooking-5a74a.firebaseio.com/",""));
                                String typeRoom = tvRoomSize.getText().toString();

//                                ArrayList<Booked> bookedsFilter = (ArrayList<Booked>) bookeds.stream().filter(p->p.getTypeRoom().equals(typeRoom)).collect(Collectors.toList());

                                hotels.add(hotel);
                                //filter


                            }
                            adapter.notifyDataSetChanged();
                            progressBar_cyclic.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }
    String readDate(Long a){
        DateFormat date = new SimpleDateFormat("dd/MM");
        return date.format(a);
    }
}