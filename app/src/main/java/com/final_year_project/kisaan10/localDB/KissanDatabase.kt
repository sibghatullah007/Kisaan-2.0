package com.final_year_project.kisaan10.localDB

import android.app.WallpaperManager.getInstance
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.final_year_project.kisaan10.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Blogs::class], version = 1, exportSchema = true)
abstract class KissanDatabase : RoomDatabase() {
    abstract fun blogsDAO(): BlogsDAO

    companion object {
        @Volatile
        private var INSTANCE: KissanDatabase? = null
        fun getDatabase(context: Context): KissanDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        KissanDatabase::class.java,
                        "kisaanDB"
                    )
                    .addCallback(object : Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // insert the data on the IO Thread
                            CoroutineScope(Dispatchers.IO).launch {
                                val prepopulateData = listOf(
                                    Blogs(
                                        id = 1,
                                        name = "Septoria Leaf Blotch",
                                        pictureResId = R.drawable.septoria_wheat_, // Replace with your drawable resource
                                        symptom = "Septoria Leaf Blotch is characterized by angular or irregularly shaped lesions on the leaves, which start as small, water-soaked spots. These spots grow and turn yellow to brown, often surrounded by a yellow halo. As the disease progresses, the lesions merge and cover larger leaf areas, causing the leaves to wither and die. In severe cases, the disease can also affect stems and spikes, leading to significant yield loss.",
                                        treatment = "To manage Septoria Leaf Blotch, apply fungicides such as Azoxystrobin, Epoxiconazole, or Tebuconazole at the onset of symptoms. Ensure that the application follows the recommended dosage and timing guidelines for optimal efficacy. Rotation of fungicides with different modes of action is recommended to prevent resistance development.\n",
                                        prevention = "Preventive measures include using resistant wheat varieties, practicing crop rotation to reduce inoculum levels in the soil, and ensuring proper field sanitation by removing crop residues. Planting seeds with appropriate spacing can improve air circulation and reduce the humidity that fosters fungal growth. Monitoring weather conditions and applying fungicides preventively when conditions are favorable for disease development can also be effective."
                                    ),
                                    Blogs(
                                        id = 2,
                                        name = "Loose Smut",
                                        pictureResId = R.drawable.loose_smut_wheat, // Replace with your drawable resource
                                        symptom = "Loose Smut manifests as black, powdery spore masses replacing the kernels in the wheat heads. Infected plants may appear taller and more vigorous initially but soon show the characteristic smutty heads. The disease becomes apparent when the wheat heads emerge from the boot, and the smut spores are released and dispersed by the wind.",
                                        treatment = "Since Loose Smut is a seed-borne disease, seed treatment with systemic fungicides such as Carboxin, Thiram, or Raxil (Tebuconazole) before planting is crucial. These fungicides penetrate the seed and eliminate the fungus inside, preventing the disease from developing in the next crop cycle.",
                                        prevention = "Preventive measures include planting certified disease-free seed and using resistant wheat varieties. Crop rotation can help reduce the inoculum in the soil. Regular field inspections during the growing season can help identify and manage any early outbreaks. Ensuring proper seed treatment protocols are followed for all seeds used for planting is essential."
                                    ),
                                    Blogs(
                                        id =3,
                                        name = "Yellow Rust",
                                        pictureResId = R.drawable.yellow_rust_wheat_, // Replace with your drawable resource
                                        symptom = "Yellow Rust is identified by the presence of bright yellow pustules arranged in stripes on the leaves, leaf sheaths, and sometimes on the stems and spikes. The pustules release yellow spores, which can spread rapidly under cool, moist conditions. Infected leaves turn yellow, dry out, and die prematurely, severely impacting grain yield and quality.",
                                        treatment = "Apply fungicides such as Propiconazole, Triadimefon, or Tebuconazole at the first sign of disease. Early intervention is crucial to control the spread. Follow the recommended fungicide application schedule and dosage for effective management. Combining fungicides with different modes of action can help manage resistance.",
                                        prevention = "Use resistant wheat varieties to minimize the risk of Yellow Rust. Crop rotation and field sanitation help reduce the presence of the rust pathogen in the field. Monitor weather conditions, as the disease thrives in cool and wet environments, and apply preventive fungicides accordingly. Avoid excessive nitrogen fertilization, which can increase susceptibility to rust."
                                    ),
                                )
                                getDatabase(context).blogsDAO().insertAll(prepopulateData)
                            }
                        }
                    })
                    .build()
                }
            }
            return INSTANCE!!
        }
    }
}