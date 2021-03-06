package com.numero.material_gallery.components.list

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialSharedAxis
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.startUpdateFlowForResult
import com.numero.material_gallery.R
import com.numero.material_gallery.components.DesignComponent
import kotlinx.android.synthetic.main.fragment_component_list.*

class ComponentListFragment : Fragment(R.layout.fragment_component_list) {

    private lateinit var appUpdateManager: AppUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appUpdateManager = AppUpdateManagerFactory.create(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        initViews()
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insetsCompat ->
            componentRecyclerView.updatePadding(
                    left = 0,
                    top = 0,
                    right = 0,
                    bottom = insetsCompat.systemWindowInsetBottom
            )
            insetsCompat
        }
    }

    override fun onResume() {
        super.onResume()
        checkUpdate()
    }

    private fun checkUpdate() {
        appUpdateManager.appUpdateInfo
                .addOnSuccessListener { appUpdateInfo ->
                    when (appUpdateInfo.updateAvailability()) {
                        UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS -> {
                            doUpdate(appUpdateInfo)
                        }
                        UpdateAvailability.UPDATE_AVAILABLE -> {
                            Snackbar.make(rootLayout, R.string.update_message, Snackbar.LENGTH_LONG)
                                    .setAction(R.string.update_button) {
                                        doUpdate(appUpdateInfo)
                                    }
                                    .show()
                        }
                    }
                }
    }

    private fun doUpdate(info: AppUpdateInfo) {
        appUpdateManager.startUpdateFlowForResult(info, AppUpdateType.IMMEDIATE, this, UPDATE_REQUEST_CODE)
    }

    private fun showSettingsScreen() {
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)

        findNavController().navigate(R.id.action_ComponentList_to_Settings)
    }

    private fun initViews() {
        toolbar.apply {
            inflateMenu(R.menu.menu_main)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_settings -> {
                        showSettingsScreen()
                        true
                    }
                    else -> false
                }
            }
        }
        componentRecyclerView.apply {
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            adapter = ComponentListAdapter().apply {
                setOnItemClickListener { view, component ->
                    selectedComponent(view, component)
                }
            }
        }
    }

    private fun selectedComponent(view: View, component: DesignComponent) {
        exitTransition = Hold()
        reenterTransition = null

        val extras = FragmentNavigatorExtras(view to view.transitionName)
        findNavController().navigate(component.navigationId, null, null, extras)
    }

    private val DesignComponent.navigationId: Int
        get() = when (this) {
            DesignComponent.BOTTOM_NAVIGATION -> R.id.action_ComponentList_to_BottomNavigation
            DesignComponent.BOTTOM_APP_BAR -> R.id.action_ComponentList_to_BottomAppBar
            DesignComponent.BOTTOM_SHEET -> R.id.action_ComponentList_to_BottomSheet
            DesignComponent.CHECKBOX -> R.id.action_ComponentList_to_Checkbox
            DesignComponent.CHIPS -> R.id.action_ComponentList_to_Chip
            DesignComponent.DATE_PICKER -> R.id.action_ComponentList_to_DatePicker
            DesignComponent.EXTENDED_FAB -> R.id.action_ComponentList_to_ExtendedFab
            DesignComponent.FAB -> R.id.action_ComponentList_to_Fab
            DesignComponent.IMAGE_VIEW -> R.id.action_ComponentList_to_ShapeableImage
            DesignComponent.MATERIAL_BUTTON -> R.id.action_ComponentList_to_MaterialButton
            DesignComponent.MATERIAL_BUTTON_TOGGLE_GROUP -> R.id.action_ComponentList_to_MaterialButtonToggleGroup
            DesignComponent.MATERIAL_CARD -> R.id.action_ComponentList_to_MaterialCard
            DesignComponent.MATERIAL_DIALOG -> R.id.action_ComponentList_to_MaterialDialog
            DesignComponent.MODAL_BOTTOM_SHEET -> R.id.action_ComponentList_to_ModalBottomSheet
            DesignComponent.NAVIGATION_DRAWER -> R.id.action_ComponentList_to_NavigationDrawer
            DesignComponent.PROGRESS_INDICATOR -> R.id.action_ComponentList_to_ProgressIndicator
            DesignComponent.RADIO_BUTTON -> R.id.action_ComponentList_to_RadioButton
            DesignComponent.SLIDER -> R.id.action_ComponentList_to_Slider
            DesignComponent.SNACKBAR -> R.id.action_ComponentList_to_Snackbar
            DesignComponent.SWITCH -> R.id.action_ComponentList_to_Switch
            DesignComponent.TAB -> R.id.action_ComponentList_to_Tab
            DesignComponent.TEXT_FIELDS -> R.id.action_ComponentList_to_TextField
            DesignComponent.TOP_APP_BAR -> R.id.action_ComponentList_to_TopAppBar
        }

    companion object {
        private const val UPDATE_REQUEST_CODE = 1
    }
}
