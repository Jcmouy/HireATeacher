package com.ejemplo.appdocente;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ejemplo.appdocente.Helper.PrefManager;
import com.ejemplo.appdocente.Util.Utils;
import com.ejemplo.appdocente.databinding.ActivityWelcomeBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

public class WelcomeActivity extends AppCompatActivity {
    private ViewsSliderAdapter mAdapter;
    private TextView[] dots;
    private int[] layouts;
    private ActivityWelcomeBinding binding;
    private PrefManager pref;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = new PrefManager(this);
        //pref.clearSession();
        //pref.setFirstTimeLaunch(true);
        if (!pref.isFirstTimeLaunch()) {
            launchSMSActivity();
            finish();
        }
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(binding.getRoot());
        init();
    }

    private void launchSMSActivity() {
        //pref.setFirstTimeLaunch(false);
        startActivity(new Intent(WelcomeActivity.this, SmsActivity.class));
        finish();
    }

    private void init() {
        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.vp_slide_one,
                R.layout.vp_slide_two,
                R.layout.vp_slide_three,
                R.layout.vp_slide_four};

        mAdapter = new ViewsSliderAdapter();
        binding.viewPager.setAdapter(mAdapter);
        binding.viewPager.registerOnPageChangeCallback(pageChangeCallback);

        binding.viewPager.setPageTransformer(Utils.getTransformer(R.id.action_depth_page));

        binding.btnNext.setBackgroundColor(Color.TRANSPARENT);
        binding.btnSkip.setBackgroundColor(Color.TRANSPARENT);

        view = binding.btnSkip;

        binding.btnSkip.setOnClickListener(v -> launchSMSActivity());

        binding.btnNext.setOnClickListener(v -> {
            // checking for last page
            // if last page home screen will be launched
            int current = getItem(+1);
            if (current < layouts.length) {
                // move to next screen
                binding.viewPager.setCurrentItem(current);
            } else {
                launchSMSActivity();
            }
        });

        /*
        binding.iconMore.setOnClickListener(view -> {
            showMenu(view);
        });
         */

        // adding bottom dots
        addBottomDots(0);
    }

    /*
     * Adds bottom dots indicator
     * */
    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        binding.layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            binding.layoutDots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return binding.viewPager.getCurrentItem() + i;
    }

    private void showMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.pager_transformers, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_orientation) {
                binding.viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
            } else {
                binding.viewPager.setPageTransformer(Utils.getTransformer(item.getItemId()));
                binding.viewPager.setCurrentItem(0);
                binding.viewPager.getAdapter().notifyDataSetChanged();
            }
            return false;
        });
        popup.show();
    }

    private void launchHomeScreen() {
        Toast.makeText(this, R.string.slides_ended, Toast.LENGTH_LONG).show();
        finish();
    }

    /*
     * ViewPager page change listener
     */
    ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                binding.btnNext.setText(getString(R.string.start));
                binding.btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                binding.btnNext.setText(getString(R.string.next));
                binding.btnSkip.setVisibility(View.VISIBLE);
            }
        }
    };

    public class ViewsSliderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public ViewsSliderAdapter() {
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(viewType, parent, false);
            return new SliderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemViewType(int position) {
            return layouts[position];
        }

        @Override
        public int getItemCount() {
            return layouts.length;
        }

        public class SliderViewHolder extends RecyclerView.ViewHolder {
            public TextView title, year, genre;

            public SliderViewHolder(View view) {
                super(view);
            }
        }
    }

}
