package imartinez.com.spacematerial;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import imartinez.com.spacematerial.isslocation.IssLocationFragment;

import static butterknife.ButterKnife.findById;
import static imartinez.com.spacematerial.R.id.bottom_navigation_view;

public class MainActivity extends AppCompatActivity {

    private static final int ISS_LOCATION_FRAGMENT_POSITION = 0;
    private static final int PEOPLE_IN_SPACE_FRAGMENT_POSITION = 1;
    private static final int ISS_PASS_TIMES_FRAGMENT_POSITION = 2;

    @BindView(R.id.content_view_pager)
    ViewPager contentViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((App) getApplication()).getAppComponent().inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {
        // Bottom bar
        BottomNavigationView bottomNavigationView = findById(this, bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tab_iss_location:
                        contentViewPager.setCurrentItem(ISS_LOCATION_FRAGMENT_POSITION, false);
                        break;
                    case R.id.tab_people_in_space:
                        contentViewPager.setCurrentItem(PEOPLE_IN_SPACE_FRAGMENT_POSITION, false);
                        break;
                    case R.id.tab_iss_pass_times:
                        contentViewPager.setCurrentItem(ISS_PASS_TIMES_FRAGMENT_POSITION, false);
                        break;
                }
                return true;
            }
        });

        // ViewPager
        FragmentPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        contentViewPager.setAdapter(mainPagerAdapter);
    }

    private static class MainPagerAdapter extends FragmentPagerAdapter {

        private static final int NUM_ITEMS = 3;

        private MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case ISS_LOCATION_FRAGMENT_POSITION:
                    fragment = IssLocationFragment.newInstance();
                    break;
                case PEOPLE_IN_SPACE_FRAGMENT_POSITION:
                    fragment = PeopleInSpaceFragment.newInstance();
                    break;
                case ISS_PASS_TIMES_FRAGMENT_POSITION:
                    fragment = new Fragment();
                    break;
                default:
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }
}
