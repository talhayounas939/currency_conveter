package com.example.currencyconverter1.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.currencyconverter1.ui.components.HowItWorks
import com.example.currencyconverter1.ui.components.OnboardingFooter
import com.example.currencyconverter1.ui.components.WhyChoose
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onFinish: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> HowItWorks()
                1 -> WhyChoose()
            }
        }

        OnboardingFooter(
            currentPage = pagerState.currentPage,
            totalPages = 2,
            onSkip = onFinish,
            onNext = {
                scope.launch {
                    if (pagerState.currentPage < 1) {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    } else {
                        onFinish()
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )
    }
}