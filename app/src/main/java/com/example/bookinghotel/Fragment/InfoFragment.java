package com.example.bookinghotel.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookinghotel.Adapter.HotelAdapter;
import com.example.bookinghotel.Adapter.ReviewAdapter;
import com.example.bookinghotel.DialogFragment.AboutMeDialog;
import com.example.bookinghotel.DialogFragment.ContactDialog;
import com.example.bookinghotel.DialogFragment.ImageDialog;
import com.example.bookinghotel.DialogFragment.JobDetailDialog;
import com.example.bookinghotel.R;
import com.example.bookinghotel.Screen.Login.Login_Signin;
import com.example.bookinghotel.entity.Hotel;
import com.example.bookinghotel.entity.Review;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.bookinghotel.entity.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {

    private static final String TAG = "InfoFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    ProgressBar progressBar_cyclic;

    LinearLayout personalinfo, booking_details, review;
    TextView personalinfobtn, booking_details_btn, reviewbtn;

    TextView name_user, des_user, phone_user, email_user, address_user, sex_user, job_user, job_user_main, workplace_user;

    TextView tv_edit_contact, tv_edit_about_me, tv_edit_job;

    public TextView percent_completed_user;

    ImageView btnLogoout, topAppBar, show_img_info;

    User user;

    private ReviewAdapter adapter;
    private ArrayList<Review> reviews = new ArrayList<>();
    private RecyclerView rc_hotel_review;

    private FragmentActivity myContext;

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

        View rootView = inflater.inflate(R.layout.fragment_info, container, false);

        personalinfo = rootView.findViewById(R.id.personalinfo);
        booking_details = rootView.findViewById(R.id.booking_details);
        review = rootView.findViewById(R.id.review);
        personalinfobtn = rootView.findViewById(R.id.personalinfobtn);
        booking_details_btn = rootView.findViewById(R.id.booking_details_btn);
        reviewbtn = rootView.findViewById(R.id.reviewbtn);
        /*making personal info visible*/
        personalinfo.setVisibility(View.VISIBLE);
        booking_details.setVisibility(View.GONE);
        review.setVisibility(View.GONE);

        name_user = rootView.findViewById(R.id.name_user);
        phone_user = rootView.findViewById(R.id.phone_user);
        des_user = rootView.findViewById(R.id.des_user);
        email_user = rootView.findViewById(R.id.email_user);
        address_user = rootView.findViewById(R.id.address_user);
        sex_user = rootView.findViewById(R.id.sex_user);
        job_user = rootView.findViewById(R.id.job_user);
        job_user_main = rootView.findViewById(R.id.job_user_main);
        workplace_user = rootView.findViewById(R.id.workplace_user);

        tv_edit_contact = rootView.findViewById(R.id.tv_edit_contact);
        tv_edit_about_me = rootView.findViewById(R.id.tv_edit_about_me);
        tv_edit_job = rootView.findViewById(R.id.tv_edit_job);

        progressBar_cyclic = rootView.findViewById(R.id.progressBar_cyclic);

        percent_completed_user = rootView.findViewById(R.id.percent_completed_user);

        btnLogoout = rootView.findViewById(R.id.btnLogout);
        topAppBar = rootView.findViewById(R.id.topAppBar);

        show_img_info = rootView.findViewById(R.id.show_img_info);

        rc_hotel_review = rootView.findViewById(R.id.rc_review_hotel);

        rc_hotel_review.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rc_hotel_review.setHasFixedSize(true);
        rc_hotel_review.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        // Dialog
        tv_edit_contact.setOnClickListener((View v) -> {
            ContactDialog contact_dialog = new ContactDialog();
            contact_dialog.show(getFragmentManager(),"ContactDialog");
        });

        tv_edit_about_me.setOnClickListener((View v) -> {
            AboutMeDialog about_dialog = new AboutMeDialog();
            about_dialog.show(getFragmentManager(),"AboutMeDialog");
        });

        tv_edit_job.setOnClickListener((View v) -> {
            JobDetailDialog job_dialog = new JobDetailDialog();
            job_dialog.show(getFragmentManager(),"JobDetailDialog");
        });

        show_img_info.setOnClickListener((View v) -> {
            ImageDialog img_dialog = new ImageDialog();
            img_dialog.show(getFragmentManager(),"ImageDialog");
        });

        personalinfobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                personalinfo.setVisibility(View.VISIBLE);
                booking_details.setVisibility(View.GONE);
                review.setVisibility(View.GONE);
                personalinfobtn.setTextColor(getResources().getColor(R.color.blue));
                booking_details_btn.setTextColor(getResources().getColor(R.color.grey));
                reviewbtn.setTextColor(getResources().getColor(R.color.grey));

            }
        });

        booking_details_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                personalinfo.setVisibility(View.GONE);
                booking_details.setVisibility(View.VISIBLE);
                review.setVisibility(View.GONE);
                personalinfobtn.setTextColor(getResources().getColor(R.color.grey));
                booking_details_btn.setTextColor(getResources().getColor(R.color.blue));
                reviewbtn.setTextColor(getResources().getColor(R.color.grey));

            }
        });

        reviewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                personalinfo.setVisibility(View.GONE);
                booking_details.setVisibility(View.GONE);
                review.setVisibility(View.VISIBLE);
                personalinfobtn.setTextColor(getResources().getColor(R.color.grey));
                booking_details_btn.setTextColor(getResources().getColor(R.color.grey));
                reviewbtn.setTextColor(getResources().getColor(R.color.blue));

            }
        });

        btnLogoout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getActivity(), Login_Signin.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        topAppBar.setOnClickListener((View v) -> {
        });

        progressBar_cyclic.setVisibility(View.VISIBLE);
        DatabaseReference UserData = FirebaseDatabase.getInstance().getReference("Users");

//      Load user from id
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        UserData.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                user = dataSnapshot.getValue(User.class);
//              set data in TextView
                if (!user.getLove().equals("")) {
                    percent_completed_user.setText("100%");
                }
                else {
                    percent_completed_user.setText("97%");
                }
                name_user.setText(user.getName());
                des_user.setText(user.getLove());
                email_user.setText(user.getEmail());
                phone_user.setText(user.getPhone());
                sex_user.setText(user.getSex());
                address_user.setText(user.getAddress());
                job_user.setText(user.getJob());
                job_user_main.setText(user.getJob());
                workplace_user.setText(user.getWorkplace());

                if (user.getReview().size() != 0) {
                    reviews.addAll(user.getReview());
                }
                adapter = new ReviewAdapter(getActivity(), reviews);
                adapter.notifyDataSetChanged();
                rc_hotel_review.setAdapter(adapter);

                // Convert base 64 string to bitmap
                byte[] decodedString = Base64.decode(user.getImage(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(decodedByte, 800, 1300, true);
                show_img_info.setImageBitmap(bMapScaled);

                progressBar_cyclic.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("test", "Failed to read value.");
            }
        });

    }
}