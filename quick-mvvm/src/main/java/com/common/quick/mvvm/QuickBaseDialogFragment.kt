package com.common.module.quick.mvvm

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.common.quick.R
import com.common.quick.utils.reflect.Generic
import com.common.quick.mvvm.QuickBaseFragment

open class QuickBaseDialogFragment<F : QuickBaseFragment<*, *>> : DialogFragment() {

    lateinit var fragment: F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStyle(STYLE_NO_TITLE, R.style.MyDialogFragment)
    }

    open fun defaultDialogHeight(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        window?.let {
            val params = window.attributes
            params.gravity = Gravity.BOTTOM
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = defaultDialogHeight()
            window.attributes = params
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.quick_base_dialog_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addFragment()
    }

    private fun addFragment() {
        val transaction = childFragmentManager.beginTransaction();
        fragment = createFragment(Generic.getParamTypeForSuper(javaClass, 0))
        fragment?.let {
            transaction.replace(R.id.fragment_container, it)
            transaction.commitAllowingStateLoss()
        }
    }

    private fun createFragment(t: Class<F>): F {
        try {
            val fragment = t.getConstructor().newInstance()
            fragment.arguments = this.arguments
            return fragment
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

}
