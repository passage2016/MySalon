package com.example.mysalon.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mysalon.R
import com.example.mysalon.databinding.ViewAppointmentBinding
import com.example.mysalon.model.remote.data.getAppointments.AppointmentInfo
import com.example.mysalon.view.AppointmentsFragmentDirections
import com.example.mysalon.viewModel.MainViewModel


class AppointmentAdapter(
    private val fragment: Fragment,
    val infoArrayList: ArrayList<AppointmentInfo>
) :
    RecyclerView.Adapter<AppointmentAdapter.AppointmentHolder>() {
    lateinit var mainViewModel: MainViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentHolder {
        val binding =
            ViewAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        mainViewModel =
            ViewModelProvider(fragment.requireActivity())[MainViewModel::class.java]
        return AppointmentHolder(binding)
    }

    override fun onBindViewHolder(holder: AppointmentHolder, position: Int) {
        holder.apply {
            val info = infoArrayList.get(position)
            holder.bind(info)
        }
    }

    override fun getItemCount(): Int {
        return infoArrayList.size
    }


    inner class AppointmentHolder(val binding: ViewAppointmentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(appointmentInfo: AppointmentInfo) {
            binding.tvAptDate.text = appointmentInfo.aptDate
            binding.tvAptTime.text =
                "${appointmentInfo.timeFrom} to ${appointmentInfo.timeTo} (${appointmentInfo.totalDuration} Minutes)"
            binding.tvStatus.text = appointmentInfo.aptStatus
            if (appointmentInfo.aptStatus == "Confirmed") {
                binding.ivStatus.setImageResource(R.drawable.ic_baseline_check_circle_outline_24)
            } else {
                binding.ivStatus.setImageResource(R.drawable.ic_baseline_close_24)
            }
            binding.root.setOnClickListener {
                val action = AppointmentsFragmentDirections.appointmentDetailAction(appointmentInfo.aptNo)
                binding.root.findNavController().navigate(action)

            }


        }
    }
}
