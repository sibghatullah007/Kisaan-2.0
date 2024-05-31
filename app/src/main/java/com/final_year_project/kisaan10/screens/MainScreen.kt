package com.final_year_project.kisaan10.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.final_year_project.kisaan10.ViewModel.BlogsViewModel
import com.final_year_project.kisaan10.ViewModel.ImageSelectionViewModel
import com.final_year_project.kisaan10.ViewModel.RecentDiseaseViewModel
import com.final_year_project.kisaan10.ViewModel.WheatViewModel
import com.final_year_project.kisaan10.auth.googleAuth.UserData
import com.final_year_project.kisaan10.navigation.BottomNavigationBar
import com.final_year_project.kisaan10.navigation.Screens
import com.final_year_project.kisaan10.utils.bottomNavigationItemsList

@Composable
fun MainScreen(recentDiseaseViewModel: RecentDiseaseViewModel,imageSelectionViewModel: ImageSelectionViewModel,blogsViewModel: BlogsViewModel, wheatViewModel: WheatViewModel,userData: UserData?, onSignOut: () -> Unit) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute by remember(navBackStackEntry) {
        derivedStateOf { navBackStackEntry?.destination?.route }
    }
    val routesWithoutBottomBar = listOf(
        "confirm_screen_route",
        "diseased_result_route",
        "blog_result_route/{blogId}",
        "appInfo",
        "help_center",
        "suggestion",
        "privacy_policy",
        "account_info",
        "edit_account_info",
        "recent_disease_result/{diseaseName}")

    Scaffold(
        bottomBar = {
            if (!routesWithoutBottomBar.contains(currentRoute)) {
                BottomNavigationBar(
                    items = bottomNavigationItemsList,
                    currentRoute = currentRoute
                ) { currentNavigationItem ->
                    navController.navigate(currentNavigationItem.route) {
                        navController.graph.startDestinationRoute?.let { startDestinationRoute ->
                            popUpTo(startDestinationRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    ) { innerPadding ->
        SetUpNavGraph(
            recentDiseaseViewModel,
            blogsViewModel,
            wheatViewModel,
            imageSelectionViewModel,
            navController = navController,
            innerPadding = innerPadding,
            userData = userData,
            onSignOut = onSignOut
        )
    }
}

@Composable
fun SetUpNavGraph(
    recentDiseaseViewModel: RecentDiseaseViewModel,
    blogsViewModel: BlogsViewModel,
    wheatViewModel: WheatViewModel,
    imageSelectionViewModel: ImageSelectionViewModel,
    navController: NavHostController,
    innerPadding: PaddingValues,
    userData: UserData?,
    onSignOut: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Screens.Home.route) {
            HomeScreen(
                onOkClick = { navController.navigate("confirm_screen_route") },
                recentDiseaseViewModel = recentDiseaseViewModel,
                imageSelectionViewModel = imageSelectionViewModel,
                navController = navController
            )
        }
        composable(Screens.Notification.route) {
            val notifications = listOf(
                Notification("Welcome to KisaanApp", "Version 1.1 is now available with new features.", "May 29, 2024"),
                // Add more notifications here
            )
            NotificationScreen(notifications)
        }
        composable(Screens.Blog.route) {
            BlogScreen(navController = navController,blogsViewModel)
        }
        composable(Screens.Setting.route) {
            SettingScreen(navController,context = LocalContext.current, userData = userData, onSignOut = onSignOut)
        }
        composable("confirm_screen_route") {
            ConfirmScreen(recentDiseaseViewModel,imageSelectionViewModel, blogsViewModel, wheatViewModel,navController)
        }
        composable("diseased_result_route") {
            DiseasedResultScreen(recentDiseaseViewModel,blogsViewModel,imageSelectionViewModel,wheatViewModel, navController)
        }
        composable("blog_result_route/{blogId}") {
            val blogId = it.arguments?.getString("blogId")
            BlogResult(blogsViewModel, blogId = blogId, navController)
        }
        composable("appInfo"){
            AppInfoScreen(navController)
        }
        composable("suggestion"){
            SuggestionScreen(navController)
        }
        composable("privacy_policy"){
            PrivacyPolicyScreen(navController)
        }
        composable("account_info"){
            AccountDetailsScreen(navController,userData)
        }
        composable("edit_account_info"){
            EditAccountDetailScreen(navController,userData)
        }
        composable("recent_disease_result/{diseaseName}"){
            val diseaseName = it.arguments?.getString("diseaseName")
            RecentDiseaseResult(navController,blogsViewModel ,diseaseName)
        }
        composable("help_center"){
            val sampleFaqs = listOf(
                FAQ("What is KisaanApp?", "KisaanApp is a mobile application designed to assist farmers with crop disease detection, providing quick and accurate diagnoses based on images of crops."),
                FAQ("How can I detect diseases in my crops using KisaanApp?", "To detect diseases in your crops, simply take a clear picture of the affected area and upload it to the Disease Detection section of the app. Our AI-powered model will analyze the image and provide a diagnosis and possible treatment options."),
                FAQ("What crops are supported by the disease detection feature?", "Currently, KisaanApp supports disease detection for various crops, including wheat, rice, maize, and more. We are continually expanding our database to include more crops."),
                FAQ("Is KisaanApp free to use?", "Yes, KisaanApp is free to use. You only need an internet connection for logging in; the rest of the application works offline."),
                FAQ("How do I update my app to the latest version?", "To update KisaanApp to the latest version, visit the Google Play Store or Apple App Store, search for KisaanApp, and click the update button if an update is available."),
                FAQ("Can I use KisaanApp offline?", "Yes, after the initial login, KisaanApp can be used offline. All features, including disease detection, are available without an internet connection."),
                FAQ("How accurate is the disease detection feature?", "Our disease detection model is trained on a large dataset and is continually being improved. While it provides highly accurate results, it is always advisable to consult with a local agricultural expert for confirmation and treatment options."),
                FAQ("How can I contact customer support?", "You can contact our customer support team through the app's Help section or by emailing kisaan002@gmail.com. We are here to help with any issues or questions you may have."),
                FAQ("What other features does KisaanApp offer?", "In addition to disease detection, KisaanApp provides tips for best farming practices. We are focused on providing the best crop disease detection to support farmers."),
                FAQ("How can I contribute to the KisaanApp community?", "You can contribute by providing feedback on the app and suggesting new features. Your input helps us improve and grow the KisaanApp community.")
            )

            HelpCenter(sampleFaqs,navController)
        }
    }
}


