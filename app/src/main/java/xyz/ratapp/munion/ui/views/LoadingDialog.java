package xyz.ratapp.munion.ui.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.taishi.flipprogressdialog.FlipProgressDialog;

import java.util.Arrays;
import java.util.List;

import xyz.ratapp.munion.R;

/**
 * Created by timtim on 10/01/2018.
 */

public final class LoadingDialog extends FlipProgressDialog {

    private View view;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        List<Integer> list = Arrays.asList(R.drawable.ic_timer_sand_empty,
                R.drawable.ic_timer_sand_full);
        setImageList(list);
        setCanceledOnTouchOutside(false);
        setDimAmount(0.5f);
        setBackgroundColor(getActivity().getResources().
                getColor(R.color.colorPrimaryDark));
        setBackgroundAlpha(0.2f);
        setCornerRadius(16);
        setImageSize(200);
        setImageMargin(10);
        setOrientation("rotationY");
        setDuration(600);
        setStartAngle(0.0f);
        setEndAngle(180.0f);
        setMinAlpha(0.0f);
        setMaxAlpha(1.0f);
    }

    public void addView(View view,
                        ViewGroup.LayoutParams params) {
        ((ViewGroup) view).addView(view, params);
    }

}
