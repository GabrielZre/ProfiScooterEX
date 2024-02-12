package com.example.profiscooterex.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.profiscooterex.data.userDB.GpsData
import com.example.profiscooterex.data.userDB.Scooter
import com.example.profiscooterex.data.userDB.TripDetails
import com.example.profiscooterex.data.userDB.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Suppress("NAME_SHADOWING")
@HiltViewModel
class DataViewModel @Inject constructor() : ViewModel() {

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val reference = FirebaseDatabase.getInstance().getReference("Users")

    private val _sendTripFlow = MutableStateFlow<Resource<Boolean>?>(null)

    private val _sendScooterDataFlow = MutableStateFlow<Resource<Boolean>?>(null)

    val userDataState = mutableStateOf(User())
    val tripsDataState = mutableStateOf<List<TripDetails>>(emptyList())
    val scooterDataState = mutableStateOf(Scooter())
    val gpsDataState = mutableStateOf(GpsData())

    init {
        getUserData()
        getTripData()
        getScooterData()
        getGpsData()
    }

    private fun getUserData() {
        viewModelScope.launch { userDataState.value = getUserDataFromDB() }
    }

    private fun getTripData() {
        viewModelScope.launch { setupTripDataListener() }
    }

    private fun getScooterData() {
        viewModelScope.launch { getScooterDataFromDB() }
    }

    private fun getGpsData() {
        viewModelScope.launch { getGpsDataFromDB() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendTripData(trip: TripDetails) {
        viewModelScope.launch { sendTripDataToDB(trip, _sendTripFlow) }
    }

    fun sendScooterData(scooter: Scooter) {
        viewModelScope.launch { sendScooterDataToDB(scooter, _sendScooterDataFlow) }
    }

    fun removeTripData(tripDate: String) {
        viewModelScope.launch { removeTripFromDB(tripDate, _sendScooterDataFlow) }
    }

    private suspend fun getUserDataFromDB(): User {
        var userData = User()

        try {
            val snapshot = reference.child(currentUser?.uid!!).get().await()
            userData = snapshot.getValue(User::class.java)!!
        } catch (e: Exception) {
            Log.d("error", "getUserDataFromDB: $e")
        }

        return userData
    }

    private fun getScooterDataFromDB() {
        reference
            .child(currentUser?.uid!!)
            .child("scooter")
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        try {
                            scooterDataState.value = dataSnapshot.getValue(Scooter::class.java)!!
                        } catch (e: Exception) {
                            Log.e("error", "Failed to parse scooter data", e)
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e(
                            "tag",
                            "Scooter data retrieval cancelled: ${databaseError.toException()}"
                        )
                    }
                }
            )
    }

    private fun sendScooterDataToDB(
        scooter: Scooter,
        sendScooterDataFlow: MutableStateFlow<Resource<Boolean>?>
    ) {
        sendScooterDataFlow.value = Resource.Loading
        reference
            .child(currentUser?.uid!!)
            .child("scooter")
            .setValue(scooter)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    sendScooterDataFlow.value = Resource.Success(true)
                } else {
                    sendScooterDataFlow.value =
                        Resource.Failure(Exception(task.exception?.message ?: "Error"))
                }
            }
    }

    private fun getGpsDataFromDB() {
        reference
            .child(currentUser?.uid!!)
            .child("location")
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        try {
                            gpsDataState.value = dataSnapshot.getValue(GpsData::class.java)!!
                        } catch (e: Exception) {
                            Log.e("error", "Failed to parse gps data", e)
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e("tag", "Gps data retrieval cancelled: ${databaseError.toException()}")
                    }
                }
            )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendTripDataToDB(trip: TripDetails, sendTripFlow: MutableStateFlow<Resource<Boolean>?>) {
        sendTripFlow.value = Resource.Loading
        reference
            .child(currentUser?.uid!!)
            .child("trips")
            .child(trip.dateTime)
            .setValue(trip)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    sendTripFlow.value = Resource.Success(true)
                } else {
                    sendTripFlow.value =
                        Resource.Failure(Exception(task.exception?.message ?: "Error"))
                }
            }
    }

    private fun setupTripDataListener() {
        reference
            .child(currentUser?.uid!!)
            .child("trips")
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val updatedTripsList: ArrayList<TripDetails> = ArrayList()

                        for (dataSnapshot: DataSnapshot in dataSnapshot.children) {
                            try {
                                val tripData = dataSnapshot.getValue(TripDetails::class.java)
                                tripData?.let { updatedTripsList.add(it) }
                            } catch (e: Exception) {
                                Log.e("error", "Failed to parse trip data", e)
                            }
                        }
                        tripsDataState.value = updatedTripsList
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e(
                            "error",
                            "Trip data retrieval cancelled: ${databaseError.toException()}"
                        )
                    }
                }
            )
    }

    private fun removeTripFromDB(
        tripDate: String,
        sendTripFlow: MutableStateFlow<Resource<Boolean>?>
    ) {

        sendTripFlow.value = Resource.Loading
        reference
            .child(currentUser?.uid!!)
            .child("trips")
            .child(tripDate)
            .removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    sendTripFlow.value = Resource.Success(true)
                } else {
                    sendTripFlow.value =
                        Resource.Failure(Exception(task.exception?.message ?: "Error"))
                }
            }
    }
}
