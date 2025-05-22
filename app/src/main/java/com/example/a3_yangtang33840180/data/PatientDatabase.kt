package com.example.a3_yangtang33840180.data

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import android.util.Log

@Database(entities = [Patient::class], version = 1)
abstract class PatientDatabase : RoomDatabase() {
    abstract fun patientDao(): PatientDAO

    companion object {
        @Volatile
        private var INSTANCE: PatientDatabase? = null

        fun getDatabase(context: Context): PatientDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PatientDatabase::class.java,
                    "patient_database"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.let { database ->
                                    prepopulateDatabase(context, database.patientDao())
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun prepopulateDatabase(context: Context, patientDao: PatientDAO) {
            try {
                val inputStream = context.assets.open("data.csv")
                val reader = BufferedReader(InputStreamReader(inputStream))

                // Skip the header line
                reader.readLine()

                var line = reader.readLine()
                while (line != null) {
                    val tokens = line.split(",")
                    if (tokens.size >= 65) {
                        val patient = Patient(
                            userId = tokens[1].toInt(),
                            phoneNumber = tokens[0].toLong(),
                            name = "User${tokens[1]}", // placeholder name
                            passWord = "password${tokens[1]}", // placeholder password
                            sex = tokens[2],
                            heifaTotalScoreMale = tokens[3].toDouble(),
                            heifaTotalScoreFemale = tokens[4].toDouble(),
                            discretionaryHeifaScoreMale = tokens[5].toDouble(),
                            discretionaryHeifaScoreFemale = tokens[6].toDouble(),
                            discretionaryServeSize = tokens[7].toDouble(),
                            vegetablesHeifaScoreMale = tokens[8].toDouble(),
                            vegetablesHeifaScoreFemale = tokens[9].toDouble(),
                            vegetablesWithLegumesAllocatedServeSize = tokens[10].toDouble(),
                            legumesAllocatedVegetables = tokens[11].toDouble(),
                            vegetablesVariationsScore = tokens[12].toDouble(),
                            vegetablesCruciferous = tokens[13].toDouble(),
                            vegetablesTuberAndBulb = tokens[14].toDouble(),
                            vegetablesOther = tokens[15].toDouble(),
                            legumes = tokens[16].toDouble(),
                            vegetablesGreen = tokens[17].toDouble(),
                            vegetablesRedAndOrange = tokens[18].toDouble(),
                            fruitHeifaScoreMale = tokens[19].toDouble(),
                            fruitHeifaScoreFemale = tokens[20].toDouble(),
                            fruitServeSize = tokens[21].toDouble(),
                            fruitVariationsScore = tokens[22].toDouble(),
                            fruitPome = tokens[23].toDouble(),
                            fruitTropicalAndSubtropical = tokens[24].toDouble(),
                            fruitBerry = tokens[25].toDouble(),
                            fruitStone = tokens[26].toDouble(),
                            fruitCitrus = tokens[27].toDouble(),
                            fruitOther = tokens[28].toDouble(),
                            grainsAndCerealsHeifaScoreMale = tokens[29].toDouble(),
                            grainsAndCerealsHeifaScoreFemale = tokens[30].toDouble(),
                            grainsAndCerealsServeSize = tokens[31].toDouble(),
                            grainsAndCerealsNonWholeGrains = tokens[32].toDouble(),
                            wholeGrainsHeifaScoreMale = tokens[33].toDouble(),
                            wholeGrainsHeifaScoreFemale = tokens[34].toDouble(),
                            wholeGrainsServeSize = tokens[35].toDouble(),
                            meatAndAlternativesHeifaScoreMale = tokens[36].toDouble(),
                            meatAndAlternativesHeifaScoreFemale = tokens[37].toDouble(),
                            meatAndAlternativesWithLegumesAllocatedServeSize = tokens[38].toDouble(),
                            legumesAllocatedMeatAndAlternatives = tokens[39].toDouble(),
                            dairyAndAlternativesHeifaScoreMale = tokens[40].toDouble(),
                            dairyAndAlternativesHeifaScoreFemale = tokens[41].toDouble(),
                            dairyAndAlternativesServeSize = tokens[42].toDouble(),
                            sodiumHeifaScoreMale = tokens[43].toDouble(),
                            sodiumHeifaScoreFemale = tokens[44].toDouble(),
                            sodiumMgMilligrams = tokens[45].toDouble(),
                            alcoholHeifaScoreMale = tokens[46].toDouble(),
                            alcoholHeifaScoreFemale = tokens[47].toDouble(),
                            alcoholStandardDrinks = tokens[48].toDouble(),
                            waterHeifaScoreMale = tokens[49].toDouble(),
                            waterHeifaScoreFemale = tokens[50].toDouble(),
                            water = tokens[51].toDouble(),
                            waterTotalMl = tokens[52].toDouble(),
                            beverageTotalMl = tokens[53].toDouble(),
                            sugarHeifaScoreMale = tokens[54].toDouble(),
                            sugarHeifaScoreFemale = tokens[55].toDouble(),
                            sugar = tokens[56].toDouble(),
                            saturatedFatHeifaScoreMale = tokens[57].toDouble(),
                            saturatedFatHeifaScoreFemale = tokens[58].toDouble(),
                            saturatedFat = tokens[59].toDouble(),
                            unsaturatedFatHeifaScoreMale = tokens[60].toDouble(),
                            unsaturatedFatHeifaScoreFemale = tokens[61].toDouble(),
                            unsaturatedFatServeSize = tokens[62].toDouble()
                        )

                        patientDao.insertPatient(patient)
                    }
                    line = reader.readLine()
                }
                reader.close()
            } catch (e: Exception) {
                Log.e("PatientDatabase", "Error prepopulating database", e)
            }
        }
    }
}