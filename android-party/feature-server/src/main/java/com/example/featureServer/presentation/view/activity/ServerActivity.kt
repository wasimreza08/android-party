package com.example.featureServer.presentation.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.core.ext.exhaustive
import com.example.core.ext.viewBinding
import com.example.featureServer.databinding.ActivityServerBinding
import com.example.featureServer.presentation.viewmodel.ServerContract
import com.example.featureServer.presentation.viewmodel.ServerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ServerActivity : AppCompatActivity() {
    private val viewModel: ServerViewModel by viewModels()
    private val binding: ActivityServerBinding by viewBinding(ActivityServerBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(
            binding.fragmentContainer.id
        ) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        observeEffect()
    }

    private fun observeEffect() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is ServerContract.Effect.OnLogoutEffect -> {
                            logout()
                        }
                        else -> {
                            // do nothing
                        }
                    }.exhaustive
                }
            }
        }
    }

    override fun onBackPressed() {
        viewModel.onUiEvent(ServerContract.Event.OnLogoutClicked)
    }

    private fun logout() {
        finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}
