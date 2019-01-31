package com.colin.component.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.colin.component.R;
import com.colin.component.net.ApiException;
import com.colin.component.net.RxNetLife;
import com.colin.component.net.TokenException;
import com.colin.ctravel.base.BasePresenter;
import com.colin.ctravel.base.BaseView;
import com.colin.tools.SPUtils;
import com.colin.tools.ToastUtils;
import retrofit2.HttpException;

public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements BaseView {

    protected boolean isFirst = false;
    protected View convertView;
    protected boolean hasDestroySave = true;
    private Boolean isPrepared = false;
    public P mPresenter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (setFragmentView() != 0) {
            convertView = inflater.inflate(setFragmentView(), container, false);
            return convertView;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = createPresenter();
        if (mPresenter != null)
            mPresenter.onAttach();
        initView();
    }

    //在这里取出数据
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isFirst = true;
        initPrepare();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirst) {
                initPrepare();
            } else if (isPrepared) {
                onUserVisible();
            }
        } else {
            onUserInVisible();
        }
    }

    private synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
            isFirst = false;
        } else {
            isPrepared = true;
        }
    }

    public void onFirstUserVisible() {

    }

    public void onUserVisible() {
    }

    public void onUserInVisible() {
    }


    //这里保存数据
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxNetLife.getNetLife().clear(getNetKey());
    }







    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showTipMessage(String msg) {
        ToastUtils.showLongToast(msg);
    }

    @Override
    public String getNetKey() {
        return getClass().getSimpleName();
    }

    @Override
    public void toLoginActBySessionError() {
        showTipMessage("token error");
        SPUtils.getInstance().clear(true);
        SPUtils.getInstance("loginTemp").clear(true);
       /* Intent intent = new Intent(getContext(), RegisterLoginAct.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
    }

    public abstract int setFragmentView();

    public abstract P createPresenter();

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void showNetErrorMsg(Throwable throwable) {
        dismissLoading();
        throwable.printStackTrace();
        if (throwable instanceof ApiException) {
            ApiException exception = (ApiException) throwable;
            if (exception.getCode() != 500)
                showTipMessage(exception.getMsg());
            else {
                showTipMessage(getString(R.string.http_error) + "(server 500)");
            }
        } else if (throwable instanceof HttpException) {
            showTipMessage(getString(R.string.http_error));
        } else if (throwable instanceof TokenException) {
            toLoginActBySessionError();
        } else {
            showTipMessage(getString(R.string.net_work_error));
            throwable.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.onDestroy();
        mPresenter = null;
    }


}
