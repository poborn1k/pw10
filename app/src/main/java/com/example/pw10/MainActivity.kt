package com.example.pw10

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

var number = 1

class MainActivity : ComponentActivity() {
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = applicationContext
        setContent {
            Column {
                UpperPart(context)

                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(
                        arrayOf(
                            CardInfo("hihihaha 1", R.drawable.hihihaha_rose),
                            CardInfo("hihihaha 2", R.drawable.hihihaha_rose),
                            CardInfo("hihihaha 3", R.drawable.hihihaha_rose),
                            CardInfo("hihihaha 4", R.drawable.hihihaha_rose),
                            CardInfo("hihihaha 5", R.drawable.hihihaha_rose),
                            CardInfo("hihihaha 6", R.drawable.hihihaha_rose),
                            CardInfo("hihihaha 7", R.drawable.hihihaha_rose)
                        )
                    ) { item ->
                        CardViewDisplay(cardInfo = item)
                    }
                }
            }
        }
    }
}

@Composable
fun UpperPart(context: Context) {
    var selectedImage by remember { mutableStateOf<Bitmap?>(null) }
    var text by remember { mutableStateOf("") }
    var isLoading1 by remember { mutableStateOf(false) }
    var isLoading2 by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.padding(20.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (selectedImage != null) {
            Image(
                modifier = Modifier.size(200.dp),
                bitmap = selectedImage!!.asImageBitmap(),
                contentDescription = "image"
            )
        } else {
            Image(
                modifier = Modifier.size(200.dp),
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = "image"
            )
        }

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("URL изображения") }
        )

        Button(onClick = {
            isLoading1 = true
            coroutineScope.launch {
                selectedImage = createBitmapFromURL(text)
                isLoading1 = false
            }
        }) {
            Text(if (isLoading1) "Загрузка..." else "Загрузить")
        }

        Button(onClick = {
            isLoading2 = true
            coroutineScope.launch {
                selectedImage = createBitmapFromURL(text)
            }
            coroutineScope.launch {
                val isExists = saveImage(selectedImage, context)
                if (isExists && selectedImage != null) {
                    number++
                }
            }
            isLoading2 = false
        }) {
            Text(if (isLoading2) "Загрузка..." else "Загрузить и сохранить")
        }

        Button(onClick = {
            text = ""
            selectedImage = null
        }) {
            Text("Очистить")
        }
    }
}

suspend fun createBitmapFromURL(stringURL: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        var bitmap: Bitmap? = null
        if (stringURL.isNotEmpty()) {
            try {
                val url = URL(stringURL)
                bitmap = BitmapFactory.decodeStream(url.openStream())
            } catch (e: Exception) {
                Log.e("INTERNET ERROR", "${e.message}")
            }
        }
        bitmap
    }
}

suspend fun saveImage(bitmap: Bitmap?, context: Context): Boolean {
    return withContext(Dispatchers.IO) {
        val file_name = "new_image" + "_${number}" + ".jpg"
        if (bitmap != null) {
            try {
                val file = File(context.filesDir, file_name)
                val outputStream = FileOutputStream(file)

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
            } catch (e: Exception) {
                Log.e("DISK ERROR", "${e.message}")
            }
        }
        File(context.filesDir, file_name).exists()
    }
}


@Composable
fun CardViewDisplay(cardInfo: CardInfo) {
    Card(
        modifier = Modifier.padding(20.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(200.dp),
                painter = painterResource(cardInfo.intImage),
                contentDescription = "image",
                contentScale = ContentScale.Crop
            )

            Text(modifier = Modifier.padding(top = 10.dp), text = cardInfo.name)
        }
    }
}
