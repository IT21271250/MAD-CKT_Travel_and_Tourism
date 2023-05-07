package com.example.mad.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mad.models.EmployeeModel
import com.example.mad.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {

    private lateinit var etEmpName: EditText
    private lateinit var etEmpAmount: EditText
    private lateinit var etEmpComment: EditText
    private lateinit var btnDonate: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        etEmpName = findViewById(R.id.etEmpName)
        etEmpAmount = findViewById(R.id.etEmpAmount)
        etEmpComment = findViewById(R.id.etEmpComment)
        btnDonate = findViewById(R.id.btnDonate)

        dbRef = FirebaseDatabase.getInstance().getReference("Employees")

        btnDonate.setOnClickListener {
            saveEmployeeData()
        }
    }

        private fun saveEmployeeData() {

            //get values
            val empName = etEmpName.text.toString()
            val empAmount = etEmpAmount.text.toString()
            val empComment = etEmpComment.text.toString()

            if(empName.isEmpty()){
                etEmpName.error = "Please enter Name"
            }

            if(empAmount.isEmpty()) {
                etEmpName.error = "You Doesn't donate"
            }

            if(empComment.isEmpty()){
                etEmpComment.error = "Please Enter any comment"
            }

            val empId = dbRef.push().key!!

            val employee = EmployeeModel(empId, empName, empAmount, empComment)

            dbRef.child(empId).setValue(employee)
                .addOnCompleteListener{
                    Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                    etEmpName.text.clear()
                    etEmpAmount.text.clear()
                    etEmpComment.text.clear()


                }.addOnFailureListener{ err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }

        }
}