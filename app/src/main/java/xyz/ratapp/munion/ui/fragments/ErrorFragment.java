package xyz.ratapp.munion.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import xyz.ratapp.munion.ui.activities.MainActivity;
import xyz.ratapp.munion.R;
import xyz.ratapp.munion.ui.fragments.common.FragmentBase;

/**
 * <p>Date: 12.12.17</p>
 *
 * @author Simon
 */

public class ErrorFragment extends FragmentBase {

    private static final String ARG_TEXT = "arg_text";
    private static final String ARG_BUTTON = "arg_button";

    private TextView errorText;
    private Button errorButton;

    public static ErrorFragment getInstance(String text, String button){
        ErrorFragment fragment = new ErrorFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TEXT, text);
        bundle.putString(ARG_BUTTON, button);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_error, container, false);

        errorText = view.findViewById(R.id.error_text);
        errorButton = view.findViewById(R.id.error_button);

        String text =  getArguments().getString(ARG_TEXT);
        String button = getArguments().getString(ARG_BUTTON);

        errorText.setText(text);
        errorButton.setText(button);

        final MainActivity activity = (MainActivity) getActivity();

        errorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.initNoAuth();
            }
        });

        return view;
    }

    @Override
    public String getFragmentName(Context context) {
        return "Error";
    }
}
