package com.example.droidconnyc22.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.droidconnyc22.databinding.FragmentPatientListBinding
import com.example.droidconnyc22.model.Patient
import com.example.droidconnyc22.model.TabData
import com.example.droidconnyc22.viewmodel.PatientListUiState
import com.example.droidconnyc22.viewmodel.PatientViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class PatientListFragment : Fragment() {

    private var _binding: FragmentPatientListBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val viewBinding get() = _binding!!

    private val patientViewModel by viewModel<PatientViewModel>()
    private val patientAdapter by lazy { PatientAdapter(this::onBookmarkChanged) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatientListBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            patientViewModel.viewState.collectLatest {
                updateUiBy(it)
            }
        }

        viewBinding.patientList.adapter = patientAdapter
    }

    private fun updateUiBy(uiState: PatientListUiState) {
        when (uiState) {
            is PatientListUiState.Empty -> TODO()
            is PatientListUiState.Error -> {
                Log.e("adsd", "fdsf", uiState.error)
            }
            is PatientListUiState.Loaded -> {
                patientAdapter.submitList(uiState.patientList)
            }
            is PatientListUiState.Loading -> {}
        }

        setTabs(uiState.tabs)

    }

    private fun setTabs(tabs: List<TabData>) {
        if (!shouldRefreshTabs(tabs)) return

        viewBinding.patientTabs.removeAllTabs()
        tabs.forEach { tab ->
            viewBinding.patientTabs.apply {
                addTab(
                    newTab().apply { text = tab.title }
                )
            }
        }
    }

    private fun shouldRefreshTabs(newTabs: List<TabData>): Boolean {
        val currentTabCount = viewBinding.patientTabs.tabCount
        if (currentTabCount != newTabs.size) return true

        for (tabIndex in 0..viewBinding.patientTabs.tabCount) {
            val tabAtIndex = viewBinding.patientTabs.getTabAt(tabIndex)
            if (tabAtIndex?.text != newTabs.getOrNull(tabIndex)?.title) {
                return true
            }
        }

        return false
    }

    private fun onBookmarkChanged(patient: Patient, isChecked: Boolean) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    // TODO mock
//    val mockTabs = listOf(
//        TabData(id = "1", title = "LVO Suspected"),
//        TabData(id = "2", title = "CTP Suspected")
//    )
//
//    addTabs(mockTabs)
//
//    // TODO mock
//    val mockPatients = listOf(
//        PatientRemote(
//            patientId = "1",
//            name = "Orel Zion",
//            bookmarkCount = 12,
//            isBookmarked = false,
//            photoUrl = "https://www.brainline.org/sites/default/files/slides/mri.jpg"
//        ),
//        PatientRemote(
//            patientId = "2",
//            name = "Sara Zion",
//            bookmarkCount = 543,
//            isBookmarked = true,
//            photoUrl = null
//        ),
//        PatientRemote(
//            patientId = "3",
//            name = "Lorem Ipsum",
//            bookmarkCount = 423,
//            isBookmarked = false,
//            photoUrl = "https://oryon.co.uk/app/uploads/2019/08/Brain-MRI-e1565353833878.jpg"
//        ),
//        PatientRemote(
//            patientId = "4",
//            name = "Soro teet Hugo",
//            bookmarkCount = 756,
//            isBookmarked = false,
//            photoUrl = "https://www.esa.int/var/esa/storage/images/esa_multimedia/images/2013/06/mri_brain_scan/12915330-1-eng-GB/MRI_brain_scan_pillars.jpg"
//        ),
//        PatientRemote(
//            patientId = "5",
//            name = "Ercha joit",
//            bookmarkCount = 12,
//            isBookmarked = false,
//            photoUrl = "https://www.kenhub.com/thumbor/zoz_XVCq44UFroH2ds6eoOUvdtA=/fit-in/800x1600/filters:watermark(/images/logo_url.png,-10,-10,0):background_color(FFFFFF):format(jpeg)/images/library/13517/ff.jpg"
//        )
//    )
}