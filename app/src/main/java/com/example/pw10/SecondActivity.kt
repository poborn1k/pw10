package com.example.pw10

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import coil.compose.AsyncImage
import com.example.pw10.ui.theme.Typography
import java.io.File
import java.time.Duration

class SecondActivity : ComponentActivity() {
    private lateinit var workManager: WorkManager
    private val viewModel by viewModels<PhotoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = applicationContext

        val file = File(context.filesDir, "new_image_1.jpg")
        val uri = Uri.fromFile(file)
        workManager = WorkManager.getInstance(context)
        viewModel.updateUncompressedUri(uri)

        val request = OneTimeWorkRequestBuilder<PhotoWorker>()
            .setInputData(
                workDataOf(
                    "KEY_CONTENT_URI" to uri.toString(),
                    "KEY_COMPRESSION_THRESHOLD" to 1024 * 20L
                )
            )
            .setInitialDelay(Duration.ofSeconds(5))
            .build()
        viewModel.updateWorkId(request.id)

        workManager.enqueue(request)

        setContent {
            val workerResult = viewModel.workId?.let { id ->
                workManager.getWorkInfoByIdLiveData(id).observeAsState().value
            }
            LaunchedEffect(key1 = workerResult?.outputData) {
                if (workerResult?.outputData != null) {
                    val filePath = workerResult.outputData.getString("KEY_RESULT_PATH")
                    filePath?.let {
                        val bitmap = BitmapFactory.decodeFile(it)
                        viewModel.updateCompBitmap(bitmap)
                    }
                }
            }

            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                viewModel.uncompressedUri?.let {
                    Text(text = "Несжатое изображение", style = Typography.titleLarge)
                    AsyncImage(modifier = Modifier.size(200.dp), model = it, contentDescription = null)
                }

                Spacer(modifier = Modifier.height(16.dp))

                viewModel.compressedBitmap?.let {
                    Text(text = "Сжатое изображение", style = Typography.titleLarge)
                    Image(modifier = Modifier.size(200.dp), bitmap = it.asImageBitmap(), contentDescription = null)
                }

                Button(
                    modifier = Modifier.padding(top = 20.dp),
                    onClick = {
                        val intent = Intent(this@SecondActivity, MainActivity::class.java)
                        startActivity(intent)
                    }) {
                    Text(text = "Назад")
                }
            }
        }
    }
}