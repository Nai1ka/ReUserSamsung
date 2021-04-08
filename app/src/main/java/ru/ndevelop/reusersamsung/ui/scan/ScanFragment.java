package ru.ndevelop.reusersamsung.ui.scan;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import ru.ndevelop.reusersamsung.R;

public class ScanFragment extends Fragment{

    private Context mContext;
    private TextView infoText;
    private ImageView ivNFC;
    private Button faqBtn;
    private Drawable d;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = requireContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_scan, container, false);
        initViews(root);
        return root;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    void initViews(View root){
        infoText = root.findViewById(R.id.tv_start_scan);
        ivNFC = root.findViewById(R.id.iv_nfc);
        faqBtn = root.findViewById(R.id.btn_faq);
        faqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(requireContext(), FaqActivity::class.java)
               // startActivity(i);
            }
        });
        d = ivNFC.getDrawable();
        if ((d instanceof AnimatedVectorDrawable) && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
                ((android.graphics.drawable.AnimatedVectorDrawable)d).registerAnimationCallback(new Animatable2.AnimationCallback() {
                    @Override
                    public void onAnimationEnd(Drawable drawable) {
                        super.onAnimationEnd(drawable);
                        ((AnimatedVectorDrawable)d).start();
                    }
                });

            ((AnimatedVectorDrawable)d).start();
        }

    }



}