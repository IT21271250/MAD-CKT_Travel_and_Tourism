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
                Toast.makeText(this, "You doesn't enter a Name", Toast.LENGTH_SHORT).show()
                return
            }

            if(empAmount.isEmpty()) {
                etEmpAmount.error = "Enter donate amount"
                Toast.makeText(this, "You doesn't enter Amount", Toast.LENGTH_SHORT).show()
                return
            }

            if(empComment.isEmpty()){
                etEmpComment.error = "Please Enter any comment"
                Toast.makeText(this, "You doesn't enter a Comment", Toast.LENGTH_SHORT).show()

                return
            }

            val amount: Double
            try {
                amount = empAmount.toDouble()
            } catch (e: NumberFormatException) {
                etEmpAmount.error = "Invalid amount"
                Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show()
                return
            }

            if (amount <= 0) {
                etEmpAmount.error = "Amount must be greater than 0"
                Toast.makeText(this, "Amount must be greater than 0", Toast.LENGTH_SHORT).show()
                return
            }


            val empId = dbRef.push().key!!

            val employee = EmployeeModel(empId, empName, empAmount, empComment)

            dbRef.child(empId).setValue(employee)
                .addOnCompleteListener{
                    Toast.makeText(this, "Donate Data inserted successfully", Toast.LENGTH_LONG).show()

                    etEmpName.text.clear()
                    etEmpAmount.text.clear()
                    etEmpComment.text.clear()


                }.addOnFailureListener{ err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }

        }
}