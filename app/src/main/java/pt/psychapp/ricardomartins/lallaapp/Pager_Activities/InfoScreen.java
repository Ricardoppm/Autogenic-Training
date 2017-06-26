package pt.psychapp.ricardomartins.lallaapp.Pager_Activities;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluejamesbond.text.DocumentView;
import com.example.ricardomartins.lallaapp.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class InfoScreen extends FragmentActivity {


    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    ViewPager mViewPager;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_screen);

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.Info_Pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                default:
                    // The other sections of the app are dummy placeholders.
                    Fragment fragment = new InfoSection();
                    Bundle args = new Bundle();
                    args.putInt(InfoSection.ARG_SECTION_NUMBER, i);
                    fragment.setArguments(args);
                    return fragment;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Section " + (position + 1);
        }
    }


    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class InfoSection extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_whatis, container, false);
            Bundle args = getArguments();
            int index = args.getInt(ARG_SECTION_NUMBER);

            String [] headlines  = getResources().getStringArray(R.array.Info_Headline);
            String[] texts = getResources().getStringArray(R.array.Info_Text);
            ((TextView) rootView.findViewById(R.id.Page_Headline)).setText( headlines[index] );
            ((DocumentView) rootView.findViewById(R.id.Page_text)).setText( texts[index]);
            return rootView;
        }
    }
}