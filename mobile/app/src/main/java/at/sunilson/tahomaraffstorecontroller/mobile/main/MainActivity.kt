package at.sunilson.tahomaraffstorecontroller.mobile.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.AuthenticationActivity
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.AuthenticationState
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.presentation.add.AddGroupDestination
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.presentation.overview.GroupsOverviewDestination
import at.sunilson.tahomaraffstorecontroller.mobile.features.raffstores.presentation.overview.RaffstoresOverviewDestination
import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.presentation.add.AddScheduleDestination
import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.presentation.overview.SchedulesOverviewDestination
import at.sunilson.tahomaraffstorecontroller.mobile.features.services.presentation.ServicesOverviewDestination
import at.sunilson.tahomaraffstorecontroller.mobile.features.widget.presentation.FavouritesWidgetWorker
import at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.TahomaRaffstoreTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.time.Duration

class MainActivity : ComponentActivity() {

    private val workManager: WorkManager
        get() = WorkManager.getInstance(this)

    private val viewModel: MainViewModel by viewModel()

    override fun onResume() {
        super.onResume()
        viewModel.onViewResumed()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onViewPaused()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onNewIntent(intent)
        setupCompose()
        enqueueFavouritesWidgetWorker()

        lifecycleScope.launch {
            AuthenticationState.loggedIn.collect { loggedIn ->
                Timber.d("New authentication state, logged in: $loggedIn")
                if (!loggedIn) {
                    startActivity(Intent(this@MainActivity, AuthenticationActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun enqueueFavouritesWidgetWorker() {
        workManager.enqueueUniquePeriodicWork(
            FavouritesWidgetWorker.TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            PeriodicWorkRequestBuilder<FavouritesWidgetWorker>(Duration.ofMinutes(15))
                .addTag(FavouritesWidgetWorker.TAG)
                .setConstraints(Constraints(requiredNetworkType = NetworkType.CONNECTED))
                .build()
        )
    }

    private fun setupCompose() {
        setContent {
            TahomaRaffstoreTheme {
                val navController = rememberNavController()
                Scaffold(bottomBar = { MainBottomNavigationBar(navController) }) {
                    NavHost(
                        navController = navController,
                        startDestination = "raffstores",
                        modifier = Modifier.padding(it)
                    ) {
                        navigation(startDestination = "services/overview", route = "services") {
                            composable("services/overview") { ServicesOverviewDestination(navController) }
                        }
                        navigation(startDestination = "schedules/overview", route = "schedules") {
                            composable("schedules/overview") { SchedulesOverviewDestination(navigator = navController) }
                            composable("schedules/add?scheduleId={scheduleId}", arguments = listOf(navArgument("scheduleId") {
                                type = NavType.StringType
                                nullable = true
                                defaultValue = null
                            })) { AddScheduleDestination(navigator = navController) }                        }
                        navigation(startDestination = "groups/overview", route = "groups") {
                            composable("groups/overview") { GroupsOverviewDestination(navigator = navController) }
                            composable("groups/add?groupId={groupId}", arguments = listOf(navArgument("groupId") {
                                type = NavType.StringType
                                nullable = true
                                defaultValue = null
                            })) { AddGroupDestination(navigator = navController) }
                        }
                        navigation(startDestination = "raffstores/overview", route = "raffstores") {
                            composable("raffstores/overview") { RaffstoresOverviewDestination() }
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        viewModel.onNewIntent(intent ?: return)
    }
}