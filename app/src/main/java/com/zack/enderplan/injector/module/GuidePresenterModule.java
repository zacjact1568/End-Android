package com.zack.enderplan.injector.module;

import android.support.v4.app.FragmentManager;

import com.zack.enderplan.view.contract.GuideViewContract;

import dagger.Module;
import dagger.Provides;

@Module
public class GuidePresenterModule {

    private final GuideViewContract mGuideViewContract;
    private final FragmentManager mFragmentManager;

    public GuidePresenterModule(GuideViewContract guideViewContract, FragmentManager fragmentManager) {
        mGuideViewContract = guideViewContract;
        mFragmentManager = fragmentManager;
    }

    @Provides
    GuideViewContract provideGuideViewContract() {
        return mGuideViewContract;
    }

    @Provides
    FragmentManager provideFragmentManager() {
        return mFragmentManager;
    }
}
