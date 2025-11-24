package com.example.currencyconverter1.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.currencyconverter1.ui.screens.ConversionScreen
import com.example.currencyconverter1.ui.viewmodel.CurrencyViewModel
import com.example.currencyconverter1.ui.screens.FirstSelectionScreen
import com.example.currencyconverter1.ui.screens.OnboardingScreen
import com.example.currencyconverter1.ui.screens.SplashScreen
import com.example.currencyconverter1.ui.screens.StartScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val sharedVM: CurrencyViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = modifier
    ) {
        composable("splash") {
            SplashScreen(onTimeout = { navController.navigate("start") })
        }

        composable("start") {
            StartScreen(
                onStartClick = { navController.navigate("onboarding") }
            )
        }

        composable("onboarding") {
            OnboardingScreen(
                onFinish = { navController.navigate("first_selection") }
            )
        }

        composable("first_selection") {
            FirstSelectionScreen(
                vm = sharedVM,
                onNext = { navController.navigate("conversion") }
            )
        }

        composable("conversion") {
            ConversionScreen(vm = sharedVM)
        }
    }
}