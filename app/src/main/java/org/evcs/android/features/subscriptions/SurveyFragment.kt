package org.evcs.android.features.subscriptions

import android.app.Activity
import android.text.TextUtils
import android.view.View
import androidx.core.widget.doAfterTextChanged
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentSurveyBinding
import org.evcs.android.model.SurveyItem
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.ViewUtils
import org.evcs.android.util.ViewUtils.setMargins

class SurveyFragment : ErrorFragment<SurveyPresenter>(), SurveyView {

    private lateinit var mBinding: FragmentSurveyBinding
//    private var mViewIds = HashMap<View, String>()
    private var mCheckedItems = HashSet<String>()
    private var mOtherId = ""

    override fun layout(): Int {
        return R.layout.fragment_survey
    }

    override fun createPresenter(): SurveyPresenter {
        return SurveyPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun init() {
    }

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = FragmentSurveyBinding.bind(v)
        ViewUtils.setAdjustResize(mBinding.root)
    }

    override fun populate() {
        super.populate()
        mBinding.surveyComments.editText?.hint = getString(R.string.survey_comment_hint)
        showProgressDialog()
        presenter.getSurveyQuestions()
    }

    override fun setListeners() {
        mBinding.surveyContinue.setOnClickListener {
            presenter?.sendSurveyResults(mCheckedItems, mOtherId, mBinding.surveyComments.text)
            presenter?.cancelSubscription()
        }
        mBinding.surveyCancel.setOnClickListener { requireActivity().finish() }
        mBinding.surveyComments.editText?.doAfterTextChanged {
            updateButton()
        }
    }

    override fun showQuestions(response: ArrayList<SurveyItem>) {
        hideProgressDialog()
        response.forEach { item ->
            if (item.text == "Other") {
                mOtherId = item.id!!
            } else {
                val view = ImageCheckBoxLayout(requireContext())
                view.setMargins(0, 0, 0, resources.getDimension(R.dimen.spacing_medium_extra).toInt())
                view.setDescription(item.text)
//                mViewIds[view] = item.id!!
//                view.setOnClickListener { toggle(item.id!!) }
                view.setOnCheckedClickListener { _, _ -> toggle(item.id!!) }
                mBinding.surveyLayout.addView(view)
            }
        }
    }

    private fun toggle(id: String) {
        if (mCheckedItems.contains(id)) {
            mCheckedItems.remove(id)
        } else {
            mCheckedItems.add(id)
        }
        updateButton()
    }

    private fun updateButton() {
        mBinding.surveyContinue.isEnabled = !mCheckedItems.isEmpty()
                || !TextUtils.isEmpty(mBinding.surveyComments.editText?.text)
    }

    override fun onSubscriptionCanceled() {
        requireActivity().setResult(Activity.RESULT_OK)
        requireActivity().finish()
    }
}
