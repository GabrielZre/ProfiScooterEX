package com.example.profiscooterex.ui.home
/*
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.profiscooterex.data.userDB.Trip
import com.example.profiscooterex.ui.auth.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authViewModel: AuthViewModel
) : ViewModel() {





    /*private val _historyItems = mutableStateListOf<Trip>()
    val historyItems: List<Trip> get() = _historyItems

    init {
        // Pobierz dane z bazy danych (np. Firebase) i zaktualizuj listę historyItems
        fetchHistoryItems()
    }

    private fun fetchHistoryItems() {
        val userId = authViewModel.currentUser?.uid ?: ""
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)

        databaseReference.child("trips").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val trips = mutableListOf<Trip>()
                for (snapshot in dataSnapshot.children) {
                    val trip = snapshot.getValue(Trip::class.java)
                    trip?.let {
                        trips.add(it)
                    }
                }
                _historyItems.clear()
                _historyItems.addAll(trips)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Obsłuż błąd w przypadku niepowodzenia pobierania danych
            }
        })
    }*/

}
*/