package com.l.marsplayassignment.views.baseView

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.l.marsplayassignment.MarsplayApplication
import io.reactivex.disposables.CompositeDisposable


open class BaseFragment : Fragment() {

    private val STORAGE_PERMISSION_REQUEST_CODE = 91  //for video share

    val subscriptions = CompositeDisposable()
    val handler = Handler()
    val pref = MarsplayApplication.pref

    var activity: BaseActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = getActivity() as? BaseActivity
    }

    override fun onDetach() {
        activity = null
        super.onDetach()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                storagePermissionGranted()
            } else {
                //showSnack(R.string.error_storage_permission, BaseActivity.SNACK.NEUTRAL)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subscriptions.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    open fun storagePermissionGranted() {
    }

    fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_REQUEST_CODE)
        } else {
            storagePermissionGranted()
        }
    }

    /**
     * previously we are removing the fragment from backstack.
     * But activity onbackpressed provides the same functionality
     * So, we are using activity's onbackpressed
     */
    fun goToPreviousFragment() {
        activity?.onBackPressed()
    }

    fun hideKeyBoard(view: View) {
        view.let {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showKeyBoard(view: View) {
        view.let {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, 0)
        }
    }

    /**
     * Returns true if fragment context is valid and its added
     */
    fun isContextValid(): Boolean {
        return isAdded && context != null && activity != null
    }


    internal fun setDisplayFlag(on: Boolean) {
        if (on) {
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

}
