package com.l.marsplayassignment.viewModel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel: ViewModel() {

    val subscriptions = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}