package com.example.mysalon.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mysalon.model.remote.ApiClient
import com.example.mysalon.model.remote.AppUserApiService
import com.example.mysalon.model.remote.AppointmentApiService
import com.example.mysalon.model.remote.BarberApiService
import com.example.mysalon.model.remote.data.book.Appointment
import com.example.mysalon.model.remote.data.book.BookResponse
import com.example.mysalon.model.remote.data.currentAppointments.CurrentAppointmentsResponse
import com.example.mysalon.model.remote.data.currentAppointments.Slot
import com.example.mysalon.model.remote.data.getBarber.Barber
import com.example.mysalon.model.remote.data.getBarber.GetBarbersResponse
import com.example.mysalon.model.remote.data.getBarberService.GetBarberServiceResponse
import com.example.mysalon.model.remote.data.getBarberService.Service
import com.example.mysalon.model.remote.data.login.LoginResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.math.roundToInt

class MainViewModel: ViewModel() {
    val retrofit: Retrofit = ApiClient.getRetrofit()
    val barberApiService: BarberApiService = retrofit.create(BarberApiService::class.java)
    val appointmentApiService: AppointmentApiService = retrofit.create(AppointmentApiService::class.java)
    val userLiveData = MutableLiveData<LoginResponse>()
    val barbersLiveData = MutableLiveData<ArrayList<Barber>>()
    val barberServicesIdLiveData = MutableLiveData<Int>()
    val barberServicesLiveData = MutableLiveData<ArrayList<Service>>()
    val barberServicesTypeLiveData = MutableLiveData<ArrayList<String>>()
    val barberServicesSelectLiveData = MutableLiveData<ArrayList<Int>>()
    val currentAppointmentsLiveData = MutableLiveData<ArrayList<Slot>>()
    val appointmentsSlotLiveData = MutableLiveData<Int>()
    val appointmentsTotalDurationLiveData = MutableLiveData<Double>()
    val appointmentsDateLiveData = MutableLiveData<String>()
    val appointmentsStartFromLiveData = MutableLiveData<Int>()
    val appointmentLiveData = MutableLiveData<Appointment>()
    val CouponCodeLiveData = MutableLiveData<String>()


    fun setUserLiveData(loginResponse: LoginResponse){
        userLiveData.postValue(loginResponse)
    }


    fun setBarbers(){
        if(barbersLiveData.value == null){
            val call: Call<GetBarbersResponse> = barberApiService.getBarbers()
            call.enqueue(object : Callback<GetBarbersResponse> {
                override fun onResponse(call: Call<GetBarbersResponse>, response: Response<GetBarbersResponse>) {
                    if (response.isSuccessful) {
                        if(response.body()!!.status == 0){
                            barbersLiveData.postValue(response.body()!!.barbers)
                        } else {
                            Log.e("response error", response.body()!!.message)
                        }
                    }
                }
                override fun onFailure(call: Call<GetBarbersResponse>, t: Throwable) {
                    Log.e("response.body()", t.toString())
                    t.printStackTrace()
                }
            })

        }
    }

    fun loadServicesByBarber(barberId: Int){
        if(barberServicesIdLiveData.value == null || barberId != barberServicesIdLiveData.value){
            val map = HashMap<String, String>()
            map["barber_id"] = barberId.toString()
            val reqJson: String = Gson().toJson(map)
            val body: RequestBody =
                reqJson.toRequestBody("application/json".toMediaTypeOrNull())
            val call: Call<GetBarberServiceResponse> = barberApiService.getBarberServices(body)
            call.enqueue(object : Callback<GetBarberServiceResponse> {
                override fun onResponse(call: Call<GetBarberServiceResponse>, response: Response<GetBarberServiceResponse>) {
                    if (response.isSuccessful) {
                        if(response.body()!!.status == 0){
                            barberServicesIdLiveData.postValue(barberId)
                            val serviceType = ArrayList<String>()
                            for (service in response.body()!!.services){
                                if(service.serviceType !in serviceType){
                                    serviceType.add(service.serviceType)
                                }
                            }
                            barberServicesLiveData.postValue(response.body()!!.services)
                            barberServicesSelectLiveData.postValue(ArrayList())
                            barberServicesTypeLiveData.postValue(serviceType)
                        } else {
                            Log.e("response error", response.body()!!.message)
                        }
                    }
                }
                override fun onFailure(call: Call<GetBarberServiceResponse>, t: Throwable) {
                    Log.e("response.body()", t.toString())
                    t.printStackTrace()
                }
            })

        }
    }

    fun loadCurrentAppointments(){
        val url = "appointment/currentAppointments/" + barberServicesIdLiveData.value
        val call: Call<CurrentAppointmentsResponse> = appointmentApiService.currentAppointments(url)
        call.enqueue(object : Callback<CurrentAppointmentsResponse> {
            override fun onResponse(call: Call<CurrentAppointmentsResponse>, response: Response<CurrentAppointmentsResponse>) {
                if (response.isSuccessful) {
                    if(response.body()!!.status == 0){
                        Log.e("loadCurrentAppointments", response.body()!!.slots.toString())
                        currentAppointmentsLiveData.postValue(response.body()!!.slots)

                    } else {
                        Log.e("response error", response.body()!!.message)
                    }
                }
            }
            override fun onFailure(call: Call<CurrentAppointmentsResponse>, t: Throwable) {
                Log.e("response.body()", t.toString())
                t.printStackTrace()
            }
        })
    }

    fun updateAppointmentsSlot(){
        var totalDuration = 0.0
        barberServicesLiveData.value!!.forEach(){
            if (it.serviceId in barberServicesSelectLiveData.value!!){
                totalDuration += it.duration
            }
        }
        appointmentsTotalDurationLiveData.postValue(totalDuration)
        appointmentsSlotLiveData.postValue((totalDuration/15 + 0.5).roundToInt())
    }

    fun setAppointmentsDate(date: String){
        appointmentsDateLiveData.postValue(date)
    }

    fun setAppointmentsStartFrom(startFrom: Int){
        appointmentsStartFromLiveData.postValue(startFrom)
    }

    fun setCouponCode(couponCode:String){
        CouponCodeLiveData.postValue(couponCode)
    }

    fun bookAppointment(map: HashMap<String, Any>){
        val reqJson: String = Gson().toJson(map)
        val body: RequestBody =
            reqJson.toRequestBody("application/json".toMediaTypeOrNull())
        val ps_auth_token = userLiveData.value!!.apiToken
        val call: Call<BookResponse> = appointmentApiService.bookAppointment(ps_auth_token, body)
        call.enqueue(object : Callback<BookResponse> {
            override fun onResponse(call: Call<BookResponse>, response: Response<BookResponse>) {
                if (response.isSuccessful) {
                    if(response.body()!!.status == 0){
                        Log.e("loadCurrentAppointments", response.body()!!.appointment.toString())
                        appointmentLiveData.postValue(response.body()!!.appointment)

                    } else {
                        Log.e("response error", response.body()!!.message)
                    }
                }
            }
            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                Log.e("response.body()", t.toString())
                t.printStackTrace()
            }
        })
    }

    fun rescheduleAppointment(map: HashMap<String, Any>){
        val reqJson: String = Gson().toJson(map)
        val body: RequestBody =
            reqJson.toRequestBody("application/json".toMediaTypeOrNull())
        val ps_auth_token = userLiveData.value!!.apiToken
        val call: Call<BookResponse> = appointmentApiService.rescheduleAppointment(ps_auth_token, body)
        call.enqueue(object : Callback<BookResponse> {
            override fun onResponse(call: Call<BookResponse>, response: Response<BookResponse>) {
                if (response.isSuccessful) {
                    if(response.body()!!.status == 0){
                        Log.e("loadCurrentAppointments", response.body()!!.appointment.toString())
                        appointmentLiveData.postValue(response.body()!!.appointment)

                    } else {
                        Log.e("response error", response.body()!!.message)
                    }
                }
            }
            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                Log.e("response.body()", t.toString())
                t.printStackTrace()
            }
        })
    }

    fun cancelAppointment(){
        val url = "appointment/cancelAppointment/" + appointmentLiveData.value!!.aptNo
        val ps_auth_token = userLiveData.value!!.apiToken
        val call: Call<BookResponse> = appointmentApiService.cancelAppointment(url, ps_auth_token)
        call.enqueue(object : Callback<BookResponse> {
            override fun onResponse(call: Call<BookResponse>, response: Response<BookResponse>) {
                if (response.isSuccessful) {
                    if(response.body()!!.status == 0){
                        Log.e("loadCurrentAppointments", response.body()!!.appointment.toString())
                        appointmentLiveData.postValue(response.body()!!.appointment)

                    } else {
                        Log.e("response error", response.body()!!.message)
                    }
                }
            }
            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                Log.e("response.body()", t.toString())
                t.printStackTrace()
            }
        })
    }


}