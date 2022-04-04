package com.bcit.jankybattleships.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.bcit.jankybattleships.MainActivity;
import com.bcit.jankybattleships.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OptionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OptionsFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";

//    private String mParam1;

    public OptionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment OptionsFragment.
     */
    public static OptionsFragment newInstance() {
        OptionsFragment fragment = new OptionsFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_options, container, false);
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    protected void updateTheme(View view) {
        TextView title = view.findViewById(R.id.textView_options_title);
        TextView name = view.findViewById(R.id.textView_options_nametitle);
        Switch switchy = view.findViewById(R.id.switch_options);
        EditText change = view.findViewById(R.id.editText_options_namefield);

        if (MainActivity.DARK_MODE) {
            title.setTextColor(Color.WHITE);
            name.setTextColor(Color.WHITE);
            switchy.setTextColor(Color.WHITE);
            change.setTextColor(Color.WHITE);
        } else {
            title.setTextColor(Color.BLACK);
            name.setTextColor(Color.BLACK);
            switchy.setTextColor(Color.BLACK);
            change.setTextColor(Color.BLACK);
        }
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Switch theme = view.findViewById(R.id.switch_options);
        theme.setChecked(MainActivity.DARK_MODE);
        theme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    MainActivity.DARK_MODE = true;
                } else {
                    MainActivity.DARK_MODE = false;
                }
                updateTheme(view);
                ((MainActivity) requireActivity()).setLightMode(view);
                ((MainActivity) requireActivity()).updateNavTheme();
                ((MainActivity) requireActivity()).setModeMain();
            }
        });

        updateTheme(view);

        Button nameButton = view.findViewById(R.id.button_option_submit);
        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                EditText change = (EditText) view.findViewById(R.id.editText_options_namefield);
//                String name = change.getText().toString();
//                //do something with name
//                change.setText("");
            }
        });

        Button signOutButton = view.findViewById(R.id.button_options_logout);
        signOutButton.setOnClickListener(v -> {
            System.out.println("Signout");
            ((MainActivity) requireActivity()).fixButton();
            AuthUI.getInstance()
                    .signOut(getContext())
                    .addOnCompleteListener(task -> {
                        FragmentTransaction fragmentTransaction =
                                getParentFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentContainerView_main,
                                MenuFragment.newInstance());
                        fragmentTransaction.commit();
                    });
        });

    }
}