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
import com.final_year_project.kisaan10.ml.Detectmodel
import com.final_year_project.kisaan10.ml.Wheatmodel
import com.final_year_project.kisaan10.model.DiseasePredictionResult
import com.final_year_project.kisaan10.model.WheatDetectionResult
import kotlinx.coroutines.launch
import org.tensorflow.lite.DataType
import java.io.IOException
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

const val TAG = "WheatViewModel"
class WheatViewModel(application: Application) : AndroidViewModel(application) {

//    private val detectModelInterpreter: Interpreter
//    private val wheatModelInterpreter: Interpreter
    private val wheatModel: Wheatmodel
    private val detectModel: Detectmodel

    init {
//        detectModelInterpreter = loadModel(application, "Detectmodel.tflite")
//        wheatModelInterpreter = loadModel(application, "Wheatmodel.tflite")
        wheatModel = Wheatmodel.newInstance(application)
        detectModel = Detectmodel.newInstance(application)

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

    private val _diseasePredictionResult = MutableLiveData<DiseasePredictionResult?>()


    val wheatDetectionResult: LiveData<WheatDetectionResult> get() = _wheatDetectionResult
    val diseasePredictionResult: LiveData<DiseasePredictionResult?> get() = _diseasePredictionResult

    fun detectWheat(image: Bitmap) {
        viewModelScope.launch {
            val result = runDetectWheatModel(image)
            Log.v(TAG,"DetectWheat function runs")
            _wheatDetectionResult.postValue(result)
        }
    }

    fun predictDisease(image: Bitmap) {
        viewModelScope.launch {
            val result = runPredictDiseaseModel(image)
            Log.v(TAG,"predictDisease function runs")
            _diseasePredictionResult.postValue(result)
        }
    }

    private fun runDetectWheatModel(image: Bitmap): WheatDetectionResult {
        val bitmap = Bitmap.createScaledBitmap(image, 224, 224, true)

        val byteBuffer = convertBitmapToByteBuffer(bitmap)

        // Creates inputs for reference
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result
        val outputs = detectModel.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        // Process the output
        val probabilities = outputFeature0.floatArray
        val isWheat = probabilities[0] > probabilities[1]

        return WheatDetectionResult(isWheat)
    }


    private fun runPredictDiseaseModel(image: Bitmap): DiseasePredictionResult? {
        val wheatDetectionResult = runDetectWheatModel(image)
        if (!wheatDetectionResult.isWheat) {
            return null
        }

        val bitmap = Bitmap.createScaledBitmap(image, 224, 224, true)

        val byteBuffer = convertBitmapToByteBuffer(bitmap)
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        val outputs = wheatModel.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val probabilities = outputFeature0.floatArray
        val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: -1
        val diseaseNames = listOf("Septoria Leaf Blotch", "Loose Smut", "Healthy Wheat", "Brown Rust (Leaf Rust)", "Yellow Rust (Stripe Rust)")
        val diseaseName = if (maxIndex != -1) diseaseNames[maxIndex] else "Unknown"
        val confidence = if (maxIndex != -1) probabilities[maxIndex] else 0.0f


        Log.v(TAG,diseaseName + confidence)
        return DiseasePredictionResult(diseaseName, confidence)
    }

    fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val inputSize = 224
        val byteBuffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * 3) // 4 bytes per float, 3 channels
        byteBuffer.order(ByteOrder.nativeOrder())

        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true)

        val intValues = IntArray(inputSize * inputSize)
        scaledBitmap.getPixels(intValues, 0, scaledBitmap.width, 0, 0, scaledBitmap.width, scaledBitmap.height)

        var pixel = 0
        for (i in 0 until inputSize) {
            for (j in 0 until inputSize) {
                val pixelValue = intValues[pixel++]

                // Normalize pixel value to [0, 1] and add to ByteBuffer
                byteBuffer.putFloat((Color.red(pixelValue) - 127.5f) / 127.5f)
                byteBuffer.putFloat((Color.green(pixelValue) - 127.5f) / 127.5f)
                byteBuffer.putFloat((Color.blue(pixelValue) - 127.5f) / 127.5f)
            }
        }
        return byteBuffer
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