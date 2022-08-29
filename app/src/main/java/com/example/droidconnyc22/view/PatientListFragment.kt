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
import com.example.droidconnyc22.viewmodel.*
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

        with(viewBinding) {
            patientList.adapter = patientAdapter
            refreshLayout.setOnRefreshListener { patientViewModel.refreshList() }
            patientTabs.addOnTabSelectedListener(onTabSelectedListener())

            // Droidcon 2
            patientList.addOnScrollListener(object: PagingScrollListener(patientList.layoutManager) {
                override fun loadMoreItems() {
                    patientViewModel.loadMore()
                }
                override val isLoading: Boolean
                    get() = patientViewModel.viewState.value.isLoadingMore()
            })
        }
    }

    private fun onTabSelectedListener() = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            patientViewModel.onTabSelected(tab.position)
        }
        override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
        override fun onTabReselected(tab: TabLayout.Tab?) = Unit
    }

    private fun updateUiBy(uiState: PatientListUiState) {
        Timber.i("uiState has updated: $uiState")

        when (uiState) {
            is PatientListUiState.Error -> {
                Toast.makeText(context, R.string.general_error, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }

        with(patientAdapter) {
            submitList(uiState.patientList)
            isLoadingMore = uiState.isLoadingMore()
        }

        viewBinding.progressLayout.isVisible = uiState.isLoading()
        if (!uiState.isLoading()) {
            viewBinding.refreshLayout.isRefreshing = false
        }

        setTabs(uiState.tabs)

        uiState.emptyState?.let {
            showEmptyState(it)
        } ?: run {
            hideEmptyState()
        }
    }

    private fun hideEmptyState() {
        viewBinding.emptyState.isVisible = false
    }

    private fun showEmptyState(emptyState: EmptyState) {
        viewBinding.emptyState.isVisible = true

        viewBinding.emptyStateTitle.text = emptyState.title.value(requireContext())
        emptyState.icon?.let { viewBinding.emptyStateIcon.setImageResource(it) }
        viewBinding.emptyStateDescription.text = emptyState.description?.value(requireContext())
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