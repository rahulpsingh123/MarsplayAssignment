package com.l.marsplayassignment.viewModel

import io.reactivex.disposables.CompositeDisposable

open class BaseRepo {
    val subscriptions = CompositeDisposable()

    open fun clear(){
        subscriptions.dispose()
    }
}