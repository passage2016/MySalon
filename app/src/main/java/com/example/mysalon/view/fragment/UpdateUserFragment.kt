package com.example.mysalon.view.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mysalon.databinding.*
import com.example.mysalon.model.remote.Constants
import com.example.mysalon.view.adapter.AboutUsAdapter
import com.example.mysalon.view.adapter.AlertAdapter
import com.example.mysalon.view.adapter.ProductAdapter
import com.example.mysalon.viewModel.MainViewModel
import java.util.*

class UpdateUserFragment : Fragment() {
    lateinit var binding: FragmentUpdateUserBinding
    lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentUpdateUserBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        binding.etImage.setText(mainViewModel.userLiveData.value!!.profilePic)
        binding.etBirth.text = mainViewModel.userLiveData.value!!.dateOfBirth
        binding.etName.setText(mainViewModel.userLiveData.value!!.fullName)
        binding.etEmailId.setText(mainViewModel.userLiveData.value!!.emailId)

        binding.etBirth.setOnClickListener {
            selectBirthday()
        }
        binding.btnUpdate.setOnClickListener {
            val map = HashMap<String, Any>()
            map["userId"] = mainViewModel.userLiveData.value!!.userId
            if (!binding.etName.text.toString().equals("")){
                map["fullName"] = binding.etName.text.toString()
            }
            if (!binding.etEmailId.text.toString().equals("")){
                map["emailId"] = binding.etEmailId.text.toString()
            }
            if (!binding.etPassword.text.toString().equals("")){
                map["password"] = binding.etPassword.text.toString()
            }
            if (!binding.etImage.text.toString().equals("")){
                map["profilePic"] = binding.etImage.text.toString()
            }
            map["dateOfBirth"] = binding.etBirth.text.toString()
            mainViewModel.updateUser(map)
            val action = UpdateUserFragmentDirections.backHomeAction()
            binding.root.findNavController().navigate(action)
        }


    }

    private fun selectBirthday() {
        val calender = Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this.requireContext(),
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                binding.etBirth.text =
                    "$selectedYear-${selectedMonth + 1}-$selectedDay"
            }, year, month, day
        )
        datePicker.show()
    }
}