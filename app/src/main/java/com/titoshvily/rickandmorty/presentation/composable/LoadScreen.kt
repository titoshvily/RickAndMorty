package com.titoshvily.rickandmorty.presentation.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.titoshvily.rickandmorty.R

@Composable
fun LoadScreen() {
    val composition by rememberLottieComposition(LottieCompositionSpec
        .RawRes(R.raw.loading))
    LottieAnimation(composition = composition, iterations = LottieConstants.IterateForever, modifier = Modifier.fillMaxWidth())

}