package bkhn.et.storemanage.ui.scan.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import javax.inject.Inject;

import bkhn.et.storemanage.base.BaseActivity;
import bkhn.et.storemanage.base.BaseFragment;
import bkhn.et.storemanage.di.ActivityContext;
import bkhn.et.storemanage.ui.scan.IScanContract;
import bkhn.et.storemanage.util.AppConstant;
import bkhn.et.storemanage.util.AppUtils;
import bkhn.et.storemanege.R;
import butterknife.BindView;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

import static bkhn.et.storemanage.util.AppConstant.TAGG;
import static bkhn.et.storemanage.util.AppUtils.isNotNull;

/**
 * Created by PL_itto on 3/4/2018.
 */

public class ScannerFragment extends BaseFragment implements IScanContract.IScanView, ZBarScannerView.ResultHandler {
    private static final String TAG = TAGG + ScannerFragment.class.getSimpleName();
    /*Views */
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.scanner_frame)
    FrameLayout mScannerFrame;

    /* Parameters */
    @Inject
    @ActivityContext
    Context mContext;
    @Inject
    BaseActivity mActivity;
    ZBarScannerView mScannerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentComponent().inject(this);
        setHasOptionsMenu(true);

    }

    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.frag_scanner;
    }


    @Override
    protected void setupView() {
        setupActionBar();
        mScannerView = new ZBarScannerView(mContext);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mScannerView.setLayoutParams(params);
        mScannerFrame.addView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void handleResult(Result result) {
//        Toast.makeText(getActivity(), "Contents = " + result.getContents() +
//                ", Format = " + result.getBarcodeFormat().getName(), Toast.LENGTH_SHORT).show();

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        String content = result.getContents();
        if (AppUtils.isValidProductCode(content)) {
            Intent intent = new Intent();
            intent.putExtra(AppConstant.Scanner.CODE_RESULT, result.getContents());
            mActivity.setResult(Activity.RESULT_OK, intent);
            mActivity.finish();
        } else {
            showMessage(R.string.scanner_not_valid_content);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScannerView.resumeCameraPreview(ScannerFragment.this);
                }
            }, 2000);
        }

//

    }

    @Override
    public void onPause() {
        super.onPause();
        if (isNotNull(mScannerView))
            mScannerView.stopCamera();
    }

    private void setupActionBar() {
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mActivity.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
