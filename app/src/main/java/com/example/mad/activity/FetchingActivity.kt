package com.example.mad.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad.R
import com.example.mad.adapters.EmpAdapter
import com.example.mad.models.EmployeeModel
import com.google.firebase.database.*

class FetchingActivity: AppCompatActivity() {

    private lateinit var empRecyclerView: RecyclerView
    private lateinit var tvLoadingData : TextView
    private lateinit var empList: ArrayList<EmployeeModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)

        empRecyclerView = findViewById(R.id.rvDrugs)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        empList = arrayListOf<EmployeeModel>()

        getEmployeesData()
    }

    private fun getEmployeesData(){
        empRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Employees")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                empList.clear()
                if(snapshot.exists()){
                    for(empSnap in snapshot.children){
                        val empData = empSnap.getValue(EmployeeModel::class.java)
                        empList.add(empData!!)
                    }
                    val mAdapter = EmpAdapter(empList)
                    empRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : EmpAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent= Intent(this@FetchingActivity, EmployeeDetailsActivity::class.java)

                            //PUT EXTRA
                            intent.putExtra("empId", empList[position].empId)
                            intent.putExtra("empName", empList[position].empName)
                            intent.putExtra("empAmount", empList[position].empAmount)
                            intent.putExtra("empComment", empList[position].empComment)
                            startActivity(intent)
                        }

                    })

                    empRecyclerView.visibility  = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}