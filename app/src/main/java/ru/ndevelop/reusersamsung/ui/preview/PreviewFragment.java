package ru.ndevelop.reusersamsung.ui.preview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.ndevelop.reusersamsung.R;

public class PreviewFragment extends Fragment {
    public static PreviewFragment newInstance(int fragmentType) {
        PreviewFragment catFragment = new PreviewFragment();
        Bundle args = new Bundle();
        args.putInt("fragmentType", fragmentType);
        catFragment.setArguments(args);
        return catFragment;
    }
    private int fragmentType = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fragmentType = getArguments().getInt("fragmentType");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View resultFragment = inflater.inflate(R.layout.fragment_faq1, container, false);
        switch (fragmentType) {
            case 2:
                resultFragment = inflater.inflate(R.layout.fragment_faq2, container, false);
                break;
            case 3:
                resultFragment = inflater.inflate(R.layout.fragment_faq3, container, false);
                break;
            default:
                resultFragment = inflater.inflate(R.layout.fragment_faq1, container, false);
                break;
        }
        return resultFragment;
    }


}
