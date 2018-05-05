package com.stoyanivanov.tastethat.ui.base_ui;


import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by stoyan.ivanov on 5/05/2018.
 */

public abstract class BasePresenter<V extends BaseViewContract> {
    protected V view;
    private CompositeDisposable disposables;

    public BasePresenter() {
        disposables = new CompositeDisposable();
    }

    public void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }

    public void setView(V view) {
        this.view = view;
    }

    public void onViewDestroy() {
        view = null;
        disposables.clear();
    }

}