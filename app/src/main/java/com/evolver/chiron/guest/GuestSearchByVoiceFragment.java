package com.evolver.chiron.guest;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.evolver.chiron.databinding.FragmentGuestSearchByVoiceBinding;
import com.evolver.chiron.databinding.FragmentUserSearchByVoiceBinding;
import com.evolver.chiron.user.UserAdapter;
import com.evolver.chiron.user.UserModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class GuestSearchByVoiceFragment extends Fragment {

    FragmentGuestSearchByVoiceBinding binding;
    UserAdapter adapter;
    static final int REQUEST_CODE_SPEECH_INPUT = 1;
    static final int REQUEST_CODE_SPEECH_INPUT2 = 2;

    String Camel;
    String Camel2;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public GuestSearchByVoiceFragment() {

    }

    public static GuestSearchByVoiceFragment newInstance(String param1, String param2) {
        GuestSearchByVoiceFragment fragment = new GuestSearchByVoiceFragment();
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
        binding = FragmentGuestSearchByVoiceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.stateVoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                speechRecognition1();

            }
        });
        binding.districtVoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speechRecognition2();

            }
        });
        binding.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Camel != null && Camel2 != null) {
                    binding.rec.setLayoutManager(new LinearLayoutManager(getContext()));
                    FirebaseRecyclerOptions<UserModel> options =
                            new FirebaseRecyclerOptions.Builder<UserModel>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Organization").child(Camel).child(Camel2), UserModel.class)
                                    .build();
                    adapter = new UserAdapter(options);
                    adapter.startListening();
                    binding.rec.setAdapter(adapter);

                } else {
                    Toast.makeText(getActivity(), "Please Chose State And District", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void speechRecognition1() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Sppech To Text");

        try {

            startActivityForResult(i, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {

        }


    }

    public void speechRecognition2() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Sppech To Text");

        try {

            startActivityForResult(i, REQUEST_CODE_SPEECH_INPUT2);
        } catch (Exception e) {

        }


    }

    public void onActivityResult(int requestCode, int resultCode,
                                 @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);

                Camel = Camelcase(Objects.requireNonNull(result).get(0));
                binding.tvSelectedState.setText(Camel);
            }
        } else if (requestCode == REQUEST_CODE_SPEECH_INPUT2) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);

                Camel2 = Camelcase(Objects.requireNonNull(result).get(0));
                binding.tvSelectedDistrict.setText(Camel2);
            }
        }

    }

    public String Camelcase(String s) {
        if (s == null)
            return null;

        final StringBuilder ret = new StringBuilder(s.length());

        for (final String word : s.split(" ")) {
            if (!word.isEmpty()) {
                ret.append(Character.toUpperCase(word.charAt(0)));
                ret.append(word.substring(1).toLowerCase());
            }
            if (!(ret.length() == s.length()))
                ret.append(" ");
        }

        return ret.toString();

    }
}