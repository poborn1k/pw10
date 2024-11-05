package com.example.pw10

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pw10.ui.theme.Purple40
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

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
