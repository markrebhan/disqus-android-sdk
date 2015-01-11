package com.mrebhan.disqus.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.mrebhan.disqus.DisqusSdkProvider;

public abstract class BaseFragment extends Fragment {

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        DisqusSdkProvider.getInstance().getObjectGraph().inject(this);
    }
}
