package khalil.cointrader;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private SectionsStatePagerAdapter mSectionsStatePagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                setViewPager(0);
                                break;
                            case R.id.navigation_dashboard:
                                setViewPager(1);
                                break;
                            case R.id.navigation_notifications:
                                setViewPager(2);
                                break;
                        }
                        return true;
                    }
                });

    }

    private void setupViewPager(ViewPager viewPager){
        SectionsStatePagerAdapter sectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        sectionsStatePagerAdapter.addFragment(new Fragment1(), "Market Overview");
        sectionsStatePagerAdapter.addFragment(new Fragment2(), "Order History");

        viewPager.setAdapter(sectionsStatePagerAdapter);
    }

    public void setViewPager(int fragmentNum){
        mViewPager.setCurrentItem(fragmentNum);
    }
}
