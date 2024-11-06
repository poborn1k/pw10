package com.example.pw10

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.io.File

var number = 1

class MainActivity : ComponentActivity() {
    lateinit var context: Context

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = applicationContext
        setContent {
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val navController = rememberNavController()
            var currentTitle by remember { mutableStateOf("Home") }

            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet {
                        ModalDrawerSheet {
                            Text("Навигация", modifier = Modifier.padding(16.dp))
                            HorizontalDivider()
                            NavigationDrawerItem(
                                label = { Text(text = "Home") },
                                selected = false,
                                onClick = {
                                    navController.navigate("main_screen")
                                    currentTitle = "Home"
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text(text = "Profile") },
                                selected = false,
                                onClick = {
                                    navController.navigate("bottom_screen_1")
                                    currentTitle = "Profile"
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text(text = "Show sad hamster") },
                                selected = false,
                                onClick = {
                                    navController.navigate("bottom_screen_2")
                                    currentTitle = "Sad hamster"
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text(text = "To Second Activity") },
                                selected = false,
                                onClick = {
                                    if (File(context.filesDir, "new_image_1.jpg").exists()) {
                                        val intent =
                                            Intent(this@MainActivity, SecondActivity::class.java)
                                        startActivity(intent)
                                    }
                                }
                            )
                        }
                    }
                }
            ) {
                Scaffold(
                    modifier = Modifier.nestedScroll(TopAppBarDefaults.enterAlwaysScrollBehavior(state = rememberTopAppBarState()).nestedScrollConnection),
                    topBar = {
                        AppToolBar(currentTitle)
                    },
                    bottomBar = {
                        CustomBottomAppBar(navController = navController, onTitleChange = {
                            newTitle -> currentTitle = newTitle
                        })
                    }
                ) { paddingValues ->
                    Navigation(context = context, navController = navController, innerPadding = paddingValues)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolBar(title: String) {
    TopAppBar(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp)),
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground.copy(0.7f)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.6f)
        ),
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
            state = rememberTopAppBarState()
        )
    )
}

@Composable
fun CustomBottomAppBar(navController: NavHostController, onTitleChange: (String) -> Unit) {
    BottomAppBar{
        IconButton(onClick = {
            navController.navigate("main_screen")
            onTitleChange("Home")
        }) {
            Icon(Icons.Filled.Home, contentDescription = null)
        }
        IconButton(onClick = {
            navController.navigate("bottom_screen_1")
            onTitleChange("Profile")
        }) {
            Icon(Icons.Filled.Person, contentDescription = null)
        }
        IconButton(onClick = {
            navController.navigate("bottom_screen_2")
            onTitleChange("Sad hamster")
        }) {
            Icon(Icons.Filled.Warning, contentDescription = null)
        }
    }
}

@Composable
fun Navigation(context: Context, navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = "main_screen",
        modifier = Modifier.padding(innerPadding)
    ) {
        composable("main_screen") {
            LaunchMainContent(context)
        }
        composable("bottom_screen_1") {
            LaunchSecondScreen()
        }
        composable("bottom_screen_2") {
            LaunchThirdScreen()
        }
    }
}
