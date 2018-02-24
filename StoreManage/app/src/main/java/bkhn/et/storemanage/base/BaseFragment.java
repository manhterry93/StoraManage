package bkhn.et.storemanage.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import bkhn.et.storemanage.MainApp;
import bkhn.et.storemanage.di.component.DaggerFragmentComponent;
import bkhn.et.storemanage.di.component.FragmentComponent;
import bkhn.et.storemanage.di.module.FragmentModule;
import bkhn.et.storemanage.util.NetworkUtils;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static bkhn.et.storemanage.util.AppConstant.TAGG;
import static bkhn.et.storemanage.util.AppUtils.isNotNull;

/**
 * Created by PL_itto on 2/12/2018.
 */

public abstract class BaseFragment extends Fragment implements IBaseView {
    private static final String TAG = TAGG + BaseFragment.class.getSimpleName();

    private FragmentComponent mFragmentComponent;
    private Unbinder mUnbinder;

    @Override
    public void showMessage(int resId) {
        Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(@NonNull String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getContext());
    }

    public FragmentComponent getFragmentComponent() {
        if (!isNotNull(mFragmentComponent)) {
            mFragmentComponent = DaggerFragmentComponent.builder().fragmentModule(new FragmentModule(this))
                    .applicationComponent(((MainApp) getActivity().getApplication()).getApplicationComponent())
                    .build();
        }
        return mFragmentComponent;
    }

    @NonNull
    @LayoutRes
    protected abstract int getLayoutId();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        setUnbinder(ButterKnife.bind(this, view));
        setupView();
        return view;
    }

    protected abstract void setupView();

    public void setUnbinder(Unbinder unbinder) {
        mUnbinder = unbinder;
    }

    public void unbind() {
        if (isNotNull(mUnbinder))
            mUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        unbind();
        super.onDestroy();
    }
}
