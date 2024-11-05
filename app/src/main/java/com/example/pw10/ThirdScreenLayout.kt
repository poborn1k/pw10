package com.example.pw10

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun LaunchThirdScreen() {
    Image(
        modifier = Modifier.fillMaxSize(),
        alignment = Alignment.Center,
        painter = painterResource(R.drawable.sad_hamster),
        contentDescription = "image"
    )
}