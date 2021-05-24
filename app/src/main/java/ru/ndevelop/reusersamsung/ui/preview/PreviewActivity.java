package ru.ndevelop.reusersamsung.ui.preview;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.lang.reflect.Array;
import java.util.List;

import ru.ndevelop.reusersamsung.R;


public class PreviewActivity extends FragmentActivity implements View.OnClickListener {
    final int NUM_PAGES = 3;
    private ViewPager2 viewPager;
    private TabLayout tabDots;
    private TextView skipText;
    private TextView okText;
    private final PreviewFragment[] fragmentArray = {PreviewFragment.newInstance(1),PreviewFragment.newInstance(2),PreviewFragment.newInstance(3)};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        viewPager = findViewById(R.id.pager);
        tabDots = findViewById(R.id.tabDots);
        skipText = findViewById(R.id.tv_skip_faq);
        okText = findViewById(R.id.tv_ok_faq);
        okText.setOnClickListener(this);
        skipText.setOnClickListener(this);
        ScreenSlidePagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(tabDots, viewPager, (tab, position) -> viewPager.setCurrentItem(tab.getPosition(), true)).attach();
        tabDots.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == NUM_PAGES - 1){
                    okText.setVisibility(View.VISIBLE);
                }
                else{
                    okText.setVisibility(View.GONE);
                }

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v==skipText | v==okText){
            finish();
        }
    }

    class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        final int NUM_PAGES = 3;
        public ScreenSlidePagerAdapter(@NonNull FragmentActivity fa) {
            super(fa);
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentArray[position];
        }

        @Override
        public void onBindViewHolder(@NonNull FragmentViewHolder holder, int position, @NonNull List<Object> payloads) {
            super.onBindViewHolder(holder, position, payloads);

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
