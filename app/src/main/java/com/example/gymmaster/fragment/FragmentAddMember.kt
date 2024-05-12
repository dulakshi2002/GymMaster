package com.example.gymmaster.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.database.DatabaseUtils
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gymmaster.R
import com.example.gymmaster.databinding.FragmentAddMemberBinding
import com.example.gymmaster.global.DB
import com.example.gymmaster.global.MyFunction
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FragmentAddMember : Fragment() {

    var db : DB?=null
    var oneMonth:String?=""
    var threeMonth:String?=""
    var sixMonth:String?=""
    var oneYear:String?=""
    var threeYear:String?=""

    private var gender = "Male"
    private lateinit var binding: FragmentAddMemberBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddMemberBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = activity?.let { DB(it) }

        val cal = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view1, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd/mm/yyyy"
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                binding.edtJoining.setText(sdf.format(cal.time))

                binding.spMemberShip.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
                {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val value = binding.spMemberShip.selectedItem.toString().trim()

                        if (value == "Select"){
                            binding.edtExpire.setText("")
                            calculateTotal(binding.spMemberShip, binding.edtDiscount, binding.edtTotal)
                        }else{
                            if (binding.edtJoining.text.toString().trim().isNotEmpty()){
                                if (value == "1 Month"){
                                    calculateExpireDate(1,binding.edtExpire)
                                    calculateTotal(binding.spMemberShip, binding.edtDiscount, binding.edtTotal)
                                }else if (value == "3 Months"){
                                    calculateExpireDate(3,binding.edtExpire)
                                    calculateTotal(binding.spMemberShip, binding.edtDiscount, binding.edtTotal)
                                }else if (value == "6 Months"){
                                    calculateExpireDate(6,binding.edtExpire)
                                    calculateTotal(binding.spMemberShip, binding.edtDiscount, binding.edtTotal)
                                }else if (value == "1 Year"){
                                    calculateExpireDate(12,binding.edtExpire)
                                    calculateTotal(binding.spMemberShip, binding.edtDiscount, binding.edtTotal)
                                }else if (value == "3 Years"){
                                    calculateExpireDate(36,binding.edtExpire)
                                    calculateTotal(binding.spMemberShip, binding.edtDiscount, binding.edtTotal)
                                }

                            }else{
                                showToast("Select Joining date first")
                                binding.spMemberShip.setSelection(0)
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                }

            }
        binding.edtDiscount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0!=null){
                    calculateTotal(binding.spMemberShip, binding.edtDiscount, binding.edtTotal)
                }
            }

        })

        binding.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when(id){
                R.id.rdMale ->{
                    gender = "Male"
                }
                R.id.rdFemale ->{
                    gender = "Female"
                }
            }
        }

        binding.btnAddMemberSave.setOnClickListener{
            if (validate()){
                saveData()
            }
        }

        binding.imgPicDate.setOnClickListener{
            activity?.let { it1 -> DatePickerDialog(it1, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show() }
        }
        binding.imgTakeImage.setOnClickListener{

        }

        getFree()

    }

    private fun getFree(){
        try{
            val sqlQuery = "SELECT * FROM FEE WHERE ID = '1'"
            db?.fireQuery(sqlQuery)?.use {
                if (it.count>0) {
                    oneMonth = MyFunction.getValue(it, "ONE_MONTH")
                    threeMonth = MyFunction.getValue(it, "THREE_MONTH")
                    sixMonth = MyFunction.getValue(it, "SIX_MONTH")
                    oneYear = MyFunction.getValue(it, "ONE_YEAR")
                    threeYear = MyFunction.getValue(it, "THREE_YEAR")
                }
            }

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun calculateTotal(spMember: Spinner, edtDis : EditText, edtAmount : EditText){
        val month = spMember.selectedItem.toString().trim()
        var discount = edtDis.text.toString().trim()
        if(edtDis.text.toString().trim().isEmpty()){
            discount = "0"
        }

        if(month=="Select"){
            edtAmount.setText("")
        }else if(month == "1 Month"){
            if (discount.trim().isEmpty()){
                discount = "0"
            }
            if (oneMonth!!.trim().isNotEmpty()){
                val discountAmount = ((oneMonth!!.toDouble() * discount.toDouble())/100) //find out the discount amount
                val total = oneMonth!!.toDouble() - discountAmount
                edtAmount.setText(total.toString().trim())
            }
        }else if(month == "3 Months"){
            if (discount.trim().isEmpty()){
                discount = "0"
            }
            if (threeMonth!!.trim().isNotEmpty()){
                val discountAmount = ((threeMonth!!.toDouble() * discount.toDouble())/100) //find out the discount amount
                val total = threeMonth!!.toDouble() - discountAmount
                edtAmount.setText(total.toString())
            }
        }else if(month == "6 Months"){
            if (discount.trim().isEmpty()){
                discount = "0"
            }
            if (sixMonth!!.trim().isNotEmpty()){
                val discountAmount = ((sixMonth!!.toDouble() * discount.toDouble())/100) //find out the discount amount
                val total = sixMonth!!.toDouble() - discountAmount
                edtAmount.setText(total.toString())
            }
        }else if(month == "1 Year"){
            if (discount.trim().isEmpty()){
                discount = "0"
            }
            if (oneYear!!.trim().isNotEmpty()){
                val discountAmount = ((oneYear!!.toDouble() * discount.toDouble())/100) //find out the discount amount
                val total = oneYear!!.toDouble() - discountAmount
                edtAmount.setText(total.toString())
            }
        }else if(month == "3 Years"){
            if (discount.trim().isEmpty()){
                discount = "0"
            }
            if (threeYear!!.trim().isNotEmpty()){
                val discountAmount = ((threeYear!!.toDouble() * discount.toDouble())/100) //find out the discount amount
                val total = threeYear!!.toDouble() - discountAmount
                edtAmount.setText(total.toString())
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun calculateExpireDate(month: Int, edtExpiry: EditText){
        val dtStart = binding.edtJoining.text.toString().trim()
        if (dtStart.isNotEmpty()) {
            val format = SimpleDateFormat("dd/MM/yyyy")
            val date1 = format.parse(dtStart)
            val cal = Calendar.getInstance()
            cal.time = date1
            cal.add(Calendar.MONTH,month)

            val myFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            edtExpiry.setText(sdf.format(cal.time))

        }
    }

    private fun showToast(value: String){
        Toast.makeText(activity,value, Toast.LENGTH_LONG).show()
    }

    private fun validate(): Boolean{
        if (binding.edtFirstName.text.toString().trim().isEmpty()){
            showToast("Enter First Name")
            return false
        }else  if (binding.edtLastName.text.toString().trim().isEmpty()){
            showToast("Enter Last Name")
            return false
        }else if (binding.edtAge.text.toString().trim().isEmpty()){
            showToast("Enter Age")
            return false
        }else if (binding.edtAddress.text.toString().trim().isEmpty()){
            showToast("Enter Address")
            return false
        }else if (binding.edtPhone.text.toString().trim().isEmpty()){
            showToast("Enter Telephone Number")
            return false
        }

        return true
    }

    private fun saveData(){
        try {
            val sqlQuery = "INSERT OR REPLACE INTO MEMBER (ID,FIRST_NAME,LAST_NAME,GENDER,AGE," +
                    "WEIGHT,PHONE,ADDRESS,DATE_OF_JOINING,MEMBERSHIP,EXPIRE_ON,DISCOUNT,TOTAL,IMAGE_PATH,STATUS)VALUES" +
                    "('"+getIncrementedId()+"'," +DatabaseUtils.sqlEscapeString(binding.edtFirstName.text.toString().trim())+","+
                    ""+DatabaseUtils.sqlEscapeString(binding.edtLastName.text.toString().trim())+"',"+gender+"',"+binding.edtAge.text.toString().trim()+
                    "'"+binding.edtWeight.text.toString().trim()+"',"+binding.edtPhone.text.toString().trim()+
                    "',"+DatabaseUtils.sqlEscapeString(binding.edtAddress.text.toString().trim())+"',"+MyFunction.returnSQLDataFormat(binding.edtJoining.text.toString().trim())+
                    "',"+binding.spMemberShip.selectedItem.toString().trim()+"',"+MyFunction.returnSQLDataFormat(binding.edtExpire.text.toString().trim())+"',"+binding.edtDiscount.text.toString().trim()+
                    "',"+binding.edtTotal.text.toString().trim()+"','A')"
            db?.executeQuery(sqlQuery)
            showToast("Data saved successfully")
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun getIncrementedId():String{
        var incrementedId = ""

        try {
            val sqlQuery = "SELECT MAX(ID)+1, '1')AS ID FROM MEMBER"
            db?.fireQuery(sqlQuery)?.use {
                if(it.count>0){
                    incrementedId = MyFunction.getValue(it, "ID")
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        return incrementedId
    }

    /*private fun getImage(){
        val items: Array<CharSequence>
        try {
            items = arrayOf("Take Photo", "Choose Image", "Cancel")
            val builder = android.app.AlertDialog.Builder(activity)
            builder.setCancelable(false)
            builder.setTitle("Select Image")
            builder.setItems(items){dialogInterface, i ->
                if(items[i] == "Take Photo") {
                    RuntimePermission.askPermission(this).request(Manifest.permission.CAMERA)
                        .onAccepted{

                        }
                        .onDenied{
                            android.app.AlertDialog.Builder(activity)
                                .setMessage("Please accept our permission to capture image")
                                .setPositiveButton("Yes") { dialogInterface, i ->
                                    it.askAgain()
                                }
                                .setNegativeButton("No") { dialogInterface, i ->
                                    dialogInterface.dismiss()
                                }
                                .show()
                        }
                        .ask()
                }else if (items[i] == "Choose Image"){

                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }*/

}