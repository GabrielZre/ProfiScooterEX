
package com.example.profiscooterex.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.profiscooterex.data.userDB.TripDetails
import com.example.profiscooterex.data.userDB.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class DataViewModel @Inject constructor(
) : ViewModel() {

    private val _sendTripFlow = MutableStateFlow<Resource<Boolean>?>(null)
    val sendTripFlow: StateFlow<Resource<Boolean>?> = _sendTripFlow

    val userDataState  = mutableStateOf(User())
    val tripsDataState  = mutableStateOf<List<TripDetails>>(emptyList())

    init {
        getUserData()
        getTripData()
    }

    private fun getUserData() {

        viewModelScope.launch {
            userDataState.value = getUserDataFromDB()
        }

    }

    private fun getTripData() {
        viewModelScope.launch {
            tripsDataState.value = getTripDataFromDB()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendTripData(trip: TripDetails) {
        viewModelScope.launch {
            sendTripDataToDB(trip, _sendTripFlow)
        }
    }

}

suspend fun getUserDataFromDB() : User {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val reference = FirebaseDatabase.getInstance()
        .getReference("Users")
    var userData = User()

    try {
        val snapshot = reference.child(currentUser?.uid!!).get().await()
        userData = snapshot.getValue(User::class.java)!!

    } catch (e: Exception) {
        Log.d("error", "getUserDataFromDB: $e")
    }

    Log.d("UserData", "Name: ${userData.nick}, Email: ${userData.email}, Age: ${userData.age}")

    return userData
}


suspend fun getTripDataFromDB() : ArrayList<TripDetails> {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val reference = FirebaseDatabase.getInstance()
        .getReference("Users")

    var tripData = TripDetails()
    var tripsListData : ArrayList<TripDetails> = ArrayList()

    val snapshot = reference.child(currentUser?.uid!!).child("trips").get().await()

    for(dataSnapshot : DataSnapshot in snapshot.children) {
        try {
            tripData = dataSnapshot.getValue(TripDetails::class.java)!!
            tripsListData.add(tripData)
        } catch (e: Exception) {
            Log.d("error", "getTripDataFromDB: $e")
        }
    }

    for (trip in tripsListData) {
        Log.d("TripData", "DateTime: ${trip.dateTime}")
        Log.d("TripData", "TripName: ${trip.tripName}")
        Log.d("TripData", "TotalDistance: ${trip.totalDistance}")
        Log.d("TripData", "DistanceTime: ${trip.distanceTime}")
        Log.d("TripData", "AverageSpeed: ${trip.averageSpeed}")
        Log.d("TripData", "BatteryDrain: ${trip.batteryDrain}")
    }

    return tripsListData
}

@RequiresApi(Build.VERSION_CODES.O)
suspend fun sendTripDataToDB(trip: TripDetails, sendTripFlow: MutableStateFlow<Resource<Boolean>?>)  {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val reference = FirebaseDatabase.getInstance()
        .getReference("Users")

    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd ' ' HH:mm")
    val date = LocalDate.now().format(formatter)


    sendTripFlow.value = Resource.Loading
    reference.child(currentUser?.uid!!).child("trips")
        .child(date)
        .setValue(trip)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                sendTripFlow.value = Resource.Success(true)
            } else {
                sendTripFlow.value = Resource.Failure(Exception(task.exception?.message ?: "Błąd"))
            }
        }
}


