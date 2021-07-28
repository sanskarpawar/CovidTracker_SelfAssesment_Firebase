package com.example.covid;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class TabsAccessorAdapter extends FragmentPagerAdapter
{
    public TabsAccessorAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                IndiaFragment indiaFragment = new IndiaFragment();
                  return indiaFragment;


            case 1:
                SelfAssessmentActivity selfAssessmentActivity = new SelfAssessmentActivity();
                return selfAssessmentActivity;





            case 2:
              NewsActivity newsFragment = new NewsActivity();
                    return newsFragment;



            default:
                return null;

        }

    }
    @Override
    public int getCount()
    {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position) {
            case 0:
                return "Tracker";
            case 1:
                return "Self Assessment(Maha.)";

            case 2:
                return "News";




            default:
                return null;
        }
    }

}
