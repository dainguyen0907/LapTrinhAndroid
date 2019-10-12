package com.example.danhba;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class AdapterFragment extends FragmentStatePagerAdapter {
    private String listTab[]={"Lịch sử","Liên lạc","Yêu thích"};
    private FirstFragment firstFragment;
    private  SecondFragment secondFragment;
    private  ThirdFragment thirdFragment;

    public AdapterFragment(@NonNull FragmentManager fm) {
        super(fm);
        firstFragment=new FirstFragment();
        secondFragment=new SecondFragment();
        thirdFragment=new ThirdFragment();
    }

    public AdapterFragment(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0)
        {
            return firstFragment;
        }else if(position==1)
        {
            return  secondFragment;
        }else if(position==2)
        {
            return  thirdFragment;
        }
        else
        {

        }
        return null;
    }

    @Override
    public int getCount() {
        return listTab.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listTab[position];
    }
}
