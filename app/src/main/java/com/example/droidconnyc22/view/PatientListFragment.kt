package com.example.droidconnyc22.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.droidconnyc22.R
import com.example.droidconnyc22.databinding.FragmentPatientListBinding
import com.example.droidconnyc22.model.Patient
import com.example.droidconnyc22.model.TabData
import com.example.droidconnyc22.viewmodel.PatientListUiState
import com.example.droidconnyc22.viewmodel.PatientViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

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

        viewBinding.patientTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                patientViewModel.onTabSelected(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    private fun updateUiBy(uiState: PatientListUiState) {
        Timber.i("uiState has updated: $uiState")

        when (uiState) {
            is PatientListUiState.Empty -> TODO()
            is PatientListUiState.Error -> {
                Toast.makeText(context, R.string.general_error, Toast.LENGTH_SHORT).show()
            }
            is PatientListUiState.Loaded -> {}
            is PatientListUiState.Loading -> {}
        }

        patientAdapter.submitList(uiState.patientList)
        viewBinding.progressLayout.isVisible = uiState is PatientListUiState.Loading

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

    private fun onBookmarkChanged(patient: Patient) {
        patientViewModel.onUserTappedOnBookmark(ofPatient = patient)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}