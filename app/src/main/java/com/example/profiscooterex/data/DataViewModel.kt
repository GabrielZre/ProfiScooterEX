
package com.example.profiscooterex.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.profiscooterex.data.userDB.Scooter
import com.example.profiscooterex.data.userDB.TripDetails
import com.example.profiscooterex.data.userDB.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class DataViewModel @Inject constructor(
) : ViewModel() {

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val reference = FirebaseDatabase.getInstance()
        .getReference("Users")

    private val _sendTripFlow = MutableStateFlow<Resource<Boolean>?>(null)
    val sendTripFlow: StateFlow<Resource<Boolean>?> = _sendTripFlow

    val userDataState  = mutableStateOf(User())
    val tripsDataState  = mutableStateOf<List<TripDetails>>(emptyList())
    val scooterDataState = mutableStateOf(Scooter())

    init {
        getUserData()
        getTripData()
        getScooterData()
    }

    private fun getUserData() {

        viewModelScope.launch {
            userDataState.value = getUserDataFromDB()
        }

    }

    private fun getTripData() {
        viewModelScope.launch {
            setupTripDataListener()
        }

    }

    private fun getScooterData() {
        viewModelScope.launch {
            getScooterDataFromDB()
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun sendTripData(trip: TripDetails) {
        viewModelScope.launch {
            sendTripDataToDB(trip, _sendTripFlow)
        }
    }
    private suspend fun getUserDataFromDB() : User {
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
        reference.child(currentUser?.uid!!).child("scooter")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                        try {
                            scooterDataState.value = dataSnapshot.getValue(Scooter::class.java)!!
                        } catch (e: Exception) {
                            Log.e("error", "Failed to parse scooter data", e)
                        }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("tag", "Scooter data retrieval cancelled: ${databaseError.toException()}")
                }
            })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendTripDataToDB(trip: TripDetails, sendTripFlow: MutableStateFlow<Resource<Boolean>?>)  {
        val formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss:n")
        val time = LocalDateTime.now().format(formatter)

        sendTripFlow.value = Resource.Loading
        reference.child(currentUser?.uid!!).child("trips")
            .child(time)
            .setValue(trip)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    sendTripFlow.value = Resource.Success(true)
                } else {
                    sendTripFlow.value = Resource.Failure(Exception(task.exception?.message ?: "Błąd"))
                }
            }
    }

    private fun setupTripDataListener() {
        reference.child(currentUser?.uid!!).child("trips")
            .addValueEventListener(object : ValueEventListener {
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
                    Log.e("error", "Trip data retrieval cancelled: ${databaseError.toException()}")
                }
            })
    }
}


