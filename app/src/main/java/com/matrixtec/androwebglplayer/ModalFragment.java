package com.matrixtec.androwebglplayer;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModalFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button okBtn, cancelBtn;
    FrameLayout frameLayout;
    Context mContext;
    Dialog dialog;
    public ModalFragment(Context context) {
        // Required empty public constructor
        mContext = context;
    }

    public interface NoticeDialogListener {
        public void onDialogPositiveClick();
        public void onDialogNegativeClick();
    }
   public NoticeDialogListener listener;
    public static ModalFragment newInstance(String param1, String param2, Context c) {
        ModalFragment fragment = new ModalFragment(c);
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
        View view = inflater.inflate(R.layout.fragment_modal, container, false);
        okBtn = (Button) view.findViewById(R.id.okButton);
        cancelBtn = (Button) view.findViewById(R.id.cancelButton);
        listener = (NoticeDialogListener) mContext;
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDialogNegativeClick();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDialogPositiveClick();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}