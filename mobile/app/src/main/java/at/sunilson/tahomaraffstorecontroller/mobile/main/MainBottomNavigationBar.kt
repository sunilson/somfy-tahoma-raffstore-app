package at.sunilson.tahomaraffstorecontroller.mobile.main

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import at.sunilson.tahomaraffstorecontroller.mobile.R

@Composable
fun MainBottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        BottomBarItem.values().forEach { destination ->
            val isCurrentDestOnBackStack = currentBackStackEntry?.destination?.hierarchy?.any { it.route == destination.route } == true
            NavigationBarItem(
                selected = isCurrentDestOnBackStack,
                onClick = {
                    if (isCurrentDestOnBackStack) {
                        navController.popBackStack(destination.route, false)
                        return@NavigationBarItem
                    }

                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(destination.icon, contentDescription = stringResource(destination.label)) },
                label = { Text(stringResource(destination.label)) },
            )
        }
    }
}

enum class BottomBarItem(
    val route: String,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    Raffstores("raffstores", Icons.Default.Home, R.string.navigation_bar_raffstores),
    Groups("groups", Icons.Default.List, R.string.navigation_bar_groups),
    Schedules("schedules", Icons.Default.Timelapse, R.string.navigation_bar_schedules),
}