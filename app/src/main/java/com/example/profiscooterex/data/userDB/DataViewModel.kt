
package com.example.profiscooterex.data.userDB
/*
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.profiscooterex.data.AuthRepository
import com.example.profiscooterex.data.userDB.Trip
import com.example.profiscooterex.ui.auth.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(
) : ViewModel() {

    val userDataState  = mutableStateOf(User())
    val tripsDataState  = mutableStateOf<List<Trip>>(emptyList())

    init {
        getUserData()
        getTripData()
    }

    private fun getUserData() {

        */
/*viewModelScope.launch {
            userDataState.value = getUserDataFromDB()
        }*//*

    }

    private fun getTripData() {
        */
/*viewModelScope.launch {
            tripsDataState.value = getTripDataFromDB()
        }*//*

    }

}

*/
/*suspend fun getUserDataFromDB() : User {
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
}*//*


*/
/*
suspend fun getTripDataFromDB() : ArrayList<Trip> {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val reference = FirebaseDatabase.getInstance()
        .getReference("Users")

    var tripData = Trip()
    var tripsListData : ArrayList<Trip> = ArrayList()

    val snapshot = reference.child(currentUser?.uid!!).child("trips").get().await()

    for(dataSnapshot : DataSnapshot in snapshot.children) {
        try {
            tripData = dataSnapshot.getValue(Trip::class.java)!!
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
}*//*
*/

