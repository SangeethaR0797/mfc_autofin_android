package v2.model_view.Base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable


open class BaseViewModel  //
(application: Application) : AndroidViewModel(application) {
    val disposables = CompositeDisposable()
    override fun onCleared() {
        disposables.clear()
    }
}