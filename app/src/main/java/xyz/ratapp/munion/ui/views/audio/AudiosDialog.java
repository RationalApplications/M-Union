package xyz.ratapp.munion.ui.views.audio;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

import xyz.ratapp.chopino.ui.adapters.AudiosAdapter;
import xyz.ratapp.munion.R;

/**
 * Created by timtim on 31/12/2017.
 */

public final class AudiosDialog extends AlertDialog.Builder {

    private List<String> data;
    private RecyclerView rv;
    private RelativeLayout rl;
    private AudiosAdapter adapter;

    public AudiosDialog(Context context) {
        super(context);
        initViews();
    }

    @Override
    public AlertDialog show() {
        setView(rl);
        setTitle(R.string.recs_of_calls);
        setOnCancelListener(dialog -> {
            adapter.stop();
        });

        return super.show();
    }

    public void setData(List<String> urls) {
        this.data = urls;
        setupAdapter();
    }

    private void setupAdapter() {
        if(rv != null) {
            adapter = new AudiosAdapter(data);
            rv.setLayoutManager(new LinearLayoutManager(getContext()));
            rv.setAdapter(adapter);
        }
    }

    private void initViews() {
        Context c = getContext();

        int height = c.getResources().
                getDimensionPixelOffset(R.dimen.dialog_audios_height);
        rl = new RelativeLayout(c);
        rl.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                height));

        rv = new RecyclerView(c);
        rv.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        rl.addView(rv);
    }
}
