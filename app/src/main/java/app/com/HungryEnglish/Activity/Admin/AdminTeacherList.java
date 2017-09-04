package app.com.HungryEnglish.Activity.Admin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.TextView;

import app.com.HungryEnglish.Adapter.ViewPagerAdapter;
import app.com.HungryEnglish.Fragment.TeacherApprovedListFragment;
import app.com.HungryEnglish.Fragment.TeacherPendingListFragment;
import app.com.HungryEnglish.R;

/**
 * Created by Vnnovate on 7/5/2017.
 */

public class AdminTeacherList extends AppCompatActivity {
//  http://www.androidhive.info/2015/09/android-material-design-working-with-tabs/

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_main);
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        idMapping();
        setOnClick();
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void idMapping() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

    }


    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TeacherApprovedListFragment(), "APPROVED");
        adapter.addFragment(new TeacherPendingListFragment(), "PENDING");
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("APPROVED");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_aproved, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("PENDING");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_pending, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

    }

    private void setOnClick() {

    }

    private void setTabText() {

        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(AdminTeacherList.this, R.color.colorPrimaryDark));

        tabLayout.setSelectedTabIndicatorHeight(12);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
//            Toast.makeText(AdminTeacherList.this, "Back", Toast.LENGTH_LONG).show();
            onBackPressed();
            return true;
        };
        return super.onOptionsItemSelected(item);
    }


}
