package com.final_year_project.kisaan10.ViewModel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.final_year_project.kisaan10.model.DiseasePredictionResult
import com.final_year_project.kisaan10.model.WheatDetectionResult
import kotlinx.coroutines.launch
import java.io.IOException
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

const val TAG = "WheatViewModel"
class WheatViewModel(application: Application) : AndroidViewModel(application) {

    private val detectModelInterpreter: Interpreter
    private val wheatModelInterpreter: Interpreter

    init {
        detectModelInterpreter = loadModel(application, "Detectmodel.tflite")
        wheatModelInterpreter = loadModel(application, "Wheatmodel.tflite")
    }

    private fun loadModel(context: Context, modelName: String): Interpreter {
        return try {
            val model = loadModelFile(context, modelName)
            Log.v(TAG,"$modelName is loaded successfully")
            Interpreter(model)
        } catch (e: IOException) {
            throw RuntimeException("Error initializing TensorFlow Lite model: $modelName", e)
        }
    }

    @Throws(IOException::class)
    private fun loadModelFile(context: Context, filename: String): MappedByteBuffer {
        context.assets.openFd(filename).use { fileDescriptor ->
            FileInputStream(fileDescriptor.fileDescriptor).use { inputStream ->
                val fileChannel: FileChannel = inputStream.channel
                val startOffset = fileDescriptor.startOffset
                val declaredLength = fileDescriptor.declaredLength
                return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
            }
        }
    }

    private val _wheatDetectionResult = MutableLiveData<WheatDetectionResult>()
    val wheatDetectionResult: LiveData<WheatDetectionResult> get() = _wheatDetectionResult

    private val _diseasePredictionResult = MutableLiveData<DiseasePredictionResult?>()
    val diseasePredictionResult: LiveData<DiseasePredictionResult?> get() = _diseasePredictionResult

    fun detectWheat(image: Bitmap) {
        viewModelScope.launch {
            val result = runDetectWheatModel(image)
            _wheatDetectionResult.postValue(result)
        }
    }

    fun predictDisease(image: Bitmap) {
        viewModelScope.launch {
            val result = runPredictDiseaseModel(image)
            _diseasePredictionResult.postValue(result)
        }
    }

    private fun runDetectWheatModel(image: Bitmap): WheatDetectionResult {
        val bitmap = Bitmap.createScaledBitmap(image, 224, 224, true)
        val input = ByteBuffer.allocateDirect(224 * 224 * 3 * 4).order(ByteOrder.nativeOrder())
        for (y in 0 until 224) {
            for (x in 0 until 224) {
                val px = bitmap.getPixel(x, y)
                val r = Color.red(px)
                val g = Color.green(px)
                val b = Color.blue(px)
                input.putFloat((r - 127) / 255f)
                input.putFloat((g - 127) / 255f)
                input.putFloat((b - 127) / 255f)
            }
        }

        val bufferSize = 2 * java.lang.Float.SIZE / java.lang.Byte.SIZE
        val modelOutput = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder())
        detectModelInterpreter.run(input, modelOutput)

        modelOutput.rewind()
        val probabilities = modelOutput.asFloatBuffer()
        val isWheat = probabilities.get(0) > probabilities.get(1)
        return WheatDetectionResult(isWheat)
    }


    private fun runPredictDiseaseModel(image: Bitmap): DiseasePredictionResult? {
        val wheatDetectionResult = runDetectWheatModel(image)
        if (!wheatDetectionResult.isWheat) {
            return null
        }

        val bitmap = Bitmap.createScaledBitmap(image, 224, 224, true)
        val input = ByteBuffer.allocateDirect(224 * 224 * 3 * 4).order(ByteOrder.nativeOrder())
        for (y in 0 until 224) {
            for (x in 0 until 224) {
                val px = bitmap.getPixel(x, y)
                val r = Color.red(px)
                val g = Color.green(px)
                val b = Color.blue(px)
                input.putFloat((r - 127) / 255f)
                input.putFloat((g - 127) / 255f)
                input.putFloat((b - 127) / 255f)
            }
        }

        val bufferSize = 5 * java.lang.Float.SIZE / java.lang.Byte.SIZE
        val modelOutput = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder())
        wheatModelInterpreter.run(input, modelOutput)

        modelOutput.rewind()
        val probabilities = FloatArray(5)
        modelOutput.asFloatBuffer().get(probabilities)

        // Find the index of the maximum probability
        val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: -1

        // Map the index to the disease name
        val diseaseNames = listOf("Septoria Leaf Blotch", "Loose Smut", "Healthy Wheat", "Brown Rust (Leaf Rust)","Yellow Rust (Stripe Rust)")
        val diseaseName = if (maxIndex != -1) diseaseNames[maxIndex] else "Unknown"

        // Get the confidence score for the predicted disease
        val confidence = if (maxIndex != -1) probabilities[maxIndex] else 0.0f

        return DiseasePredictionResult(diseaseName, confidence)
    }
















//    private fun runPredictDiseaseModel(image: Bitmap): DiseasePredictionResult? {
//        val wheatDetectionResult = runDetectWheatModel(image)
//        if (!wheatDetectionResult.isWheat) {
//            return null
//        }
//
//        val bitmap = Bitmap.createScaledBitmap(image, 224, 224, true)
//        val input = ByteBuffer.allocateDirect(224 * 224 * 3 * 4).order(ByteOrder.nativeOrder())
//        for (y in 0 until 224) {
//            for (x in 0 until 224) {
//                val px = bitmap.getPixel(x, y)
//                val r = Color.red(px)
//                val g = Color.green(px)
//                val b = Color.blue(px)
//                input.putFloat((r - 127) / 255f)
//                input.putFloat((g - 127) / 255f)
//                input.putFloat((b - 127) / 255f)
//            }
//        }
//
//        val bufferSize = 5 * java.lang.Float.SIZE / java.lang.Byte.SIZE
//        val modelOutput = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder())
//        wheatModelInterpreter.run(input, modelOutput)
//
//        modelOutput.rewind()
//        val probabilities = modelOutput.asFloatBuffer()
//        val diseaseName = "Disease"  // Replace with actual disease name logic
//        val confidence = probabilities.get(0)  // Replace with actual confidence score from your model
//        return DiseasePredictionResult(diseaseName, confidence)
//    }
}