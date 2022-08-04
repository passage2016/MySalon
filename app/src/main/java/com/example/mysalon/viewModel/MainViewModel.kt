package com.example.mysalon.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mysalon.model.remote.*
import com.example.mysalon.model.remote.data.albumList.Album
import com.example.mysalon.model.remote.data.albumList.AlbumListResponse
import com.example.mysalon.model.remote.data.albumPhotos.AlbumPhotosResponse
import com.example.mysalon.model.remote.data.albumPhotos.Photo
import com.example.mysalon.model.remote.data.book.Appointment
import com.example.mysalon.model.remote.data.book.BookResponse
import com.example.mysalon.model.remote.data.currentAppointments.CurrentAppointmentsResponse
import com.example.mysalon.model.remote.data.currentAppointments.Slot
import com.example.mysalon.model.remote.data.dashboard.DashboardResponse
import com.example.mysalon.model.remote.data.getAppointments.AppointmentInfo
import com.example.mysalon.model.remote.data.getAppointments.GetAppoitmentsResponse
import com.example.mysalon.model.remote.data.getBarber.Barber
import com.example.mysalon.model.remote.data.getBarber.GetBarbersResponse
import com.example.mysalon.model.remote.data.getService.GetServiceResponse
import com.example.mysalon.model.remote.data.getService.Service
import com.example.mysalon.model.remote.data.getServiceCategory.GetServiceCategoryResopnse
import com.example.mysalon.model.remote.data.getServiceCategory.ServiceCategory
import com.example.mysalon.model.remote.data.getServicesByCategory.GetServicesByCategoryResponse
import com.example.mysalon.model.remote.data.getServicesByCategory.ServiceDetail
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

class MainViewModel : ViewModel() {
    val retrofit: Retrofit = ApiClient.getRetrofit()
    val barberApiService: BarberApiService = retrofit.create(BarberApiService::class.java)
    val appointmentApiService: AppointmentApiService =
        retrofit.create(AppointmentApiService::class.java)
    val appUserApiService: AppUserApiService = retrofit.create(AppUserApiService::class.java)
    val baseApiService: BaseApiService = retrofit.create(BaseApiService::class.java)

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
    val couponCodeLiveData = MutableLiveData<String>()
    val appointmentsLiveData = MutableLiveData<ArrayList<AppointmentInfo>>()
    val dashboardLiveData = MutableLiveData<DashboardResponse>()
    val serviceCategoriesLiveData = MutableLiveData<ArrayList<ServiceCategory>>()
    val serviceCategoryIdLiveData = MutableLiveData<Int>()
    val servicesListLiveData = MutableLiveData<ArrayList<ServiceDetail>>()
    val serviceLiveData = MutableLiveData<ServiceDetail>()
    val albumListLiveData = MutableLiveData<ArrayList<Album>>()
    val albumIdLiveData = MutableLiveData<Int>()
    val albumPhotosLiveData = MutableLiveData<ArrayList<Photo>>()
    val productPageLiveData = MutableLiveData<PageTool>()
    val reviewPageLiveData = MutableLiveData<PageTool>()

    fun loadProducts(){
        if(productPageLiveData.value == null){
            val productPage = PageTool(0, false, false, 0, 0, 10)
        }
    }

    fun setUserLiveData(loginResponse: LoginResponse) {
        userLiveData.postValue(loginResponse)
    }

    fun loadAlbumList(){
        if(albumListLiveData.value == null){
            val call: Call<AlbumListResponse> = baseApiService.getAlbumList()
            call.enqueue(object : Callback<AlbumListResponse> {
                override fun onResponse(
                    call: Call<AlbumListResponse>,
                    response: Response<AlbumListResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()!!.status == 0) {
                            albumListLiveData.postValue(response.body()!!.albums)
                        } else {
                            Log.e("getDashboard error", response.body()!!.message)
                        }
                    }
                }

                override fun onFailure(call: Call<AlbumListResponse>, t: Throwable) {
                    Log.e("response.body()", t.toString())
                    t.printStackTrace()
                }
            })
        }
    }
    fun getPhotosByAlbumL() {
        val url = "albums/photos/" + albumIdLiveData.value
        val call: Call<AlbumPhotosResponse> = baseApiService.getAlbumPhotos(url)
        call.enqueue(object : Callback<AlbumPhotosResponse> {
            override fun onResponse(
                call: Call<AlbumPhotosResponse>,
                response: Response<AlbumPhotosResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 0) {

                        albumPhotosLiveData.postValue(response.body()!!.photos)

                    } else {
                        Log.e("getDashboard error", response.body()!!.message)
                    }
                }
            }

            override fun onFailure(call: Call<AlbumPhotosResponse>, t: Throwable) {
                Log.e("response.body()", t.toString())
                t.printStackTrace()
            }
        })
    }

    fun getProducts() {
        val call: Call<DashboardResponse> = appUserApiService.dashboard()
        call.enqueue(object : Callback<DashboardResponse> {
            override fun onResponse(
                call: Call<DashboardResponse>,
                response: Response<DashboardResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 0) {
                        dashboardLiveData.postValue(response.body()!!)
                    } else {
                        Log.e("getDashboard error", response.body()!!.message)
                    }
                }
            }

            override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                Log.e("response.body()", t.toString())
                t.printStackTrace()
            }
        })
    }

    fun getDashboard() {
        val call: Call<DashboardResponse> = appUserApiService.dashboard()
        call.enqueue(object : Callback<DashboardResponse> {
            override fun onResponse(
                call: Call<DashboardResponse>,
                response: Response<DashboardResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 0) {
                        dashboardLiveData.postValue(response.body()!!)
                    } else {
                        Log.e("getDashboard error", response.body()!!.message)
                    }
                }
            }

            override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                Log.e("response.body()", t.toString())
                t.printStackTrace()
            }
        })
    }

    fun getServiceCategories() {
        val call: Call<GetServiceCategoryResopnse> = baseApiService.getServiceCategory()
        call.enqueue(object : Callback<GetServiceCategoryResopnse> {
            override fun onResponse(
                call: Call<GetServiceCategoryResopnse>,
                response: Response<GetServiceCategoryResopnse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 0) {

                        serviceCategoriesLiveData.postValue(response.body()!!.serviceCategories)

                    } else {
                        Log.e("getDashboard error", response.body()!!.message)
                    }
                }
            }

            override fun onFailure(call: Call<GetServiceCategoryResopnse>, t: Throwable) {
                Log.e("response.body()", t.toString())
                t.printStackTrace()
            }
        })
    }

    fun getServicesByCategory(categoryId: Int) {
        val url = "service/category/" + serviceCategoryIdLiveData.value
        val call: Call<GetServicesByCategoryResponse> = baseApiService.getServicesByCategory(url)
        call.enqueue(object : Callback<GetServicesByCategoryResponse> {
            override fun onResponse(
                call: Call<GetServicesByCategoryResponse>,
                response: Response<GetServicesByCategoryResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 0) {

                        servicesListLiveData.postValue(response.body()!!.services)

                    } else {
                        Log.e("getDashboard error", response.body()!!.message)
                    }
                }
            }

            override fun onFailure(call: Call<GetServicesByCategoryResponse>, t: Throwable) {
                Log.e("response.body()", t.toString())
                t.printStackTrace()
            }
        })
    }

    fun setBarbers() {
        if (barbersLiveData.value == null) {
            val call: Call<GetBarbersResponse> = barberApiService.getBarbers()
            call.enqueue(object : Callback<GetBarbersResponse> {
                override fun onResponse(
                    call: Call<GetBarbersResponse>,
                    response: Response<GetBarbersResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()!!.status == 0) {
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

    fun loadServicesByBarber(barberId: Int) {
        val map = HashMap<String, String>()
        map["barber_id"] = barberId.toString()
        val reqJson: String = Gson().toJson(map)
        val body: RequestBody =
            reqJson.toRequestBody("application/json".toMediaTypeOrNull())
        val call: Call<GetServiceResponse> = barberApiService.getBarberServices(body)
        call.enqueue(object : Callback<GetServiceResponse> {
            override fun onResponse(
                call: Call<GetServiceResponse>,
                response: Response<GetServiceResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 0) {
                        barberServicesIdLiveData.postValue(barberId)
                        val serviceType = ArrayList<String>()
                        for (service in response.body()!!.services) {
                            if (service.serviceType !in serviceType) {
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

            override fun onFailure(call: Call<GetServiceResponse>, t: Throwable) {
                Log.e("response.body()", t.toString())
                t.printStackTrace()
            }
        })


    }

    fun loadCurrentAppointments() {
        val url = "appointment/currentAppointments/" + barberServicesIdLiveData.value
        val call: Call<CurrentAppointmentsResponse> = appointmentApiService.currentAppointments(url)
        call.enqueue(object : Callback<CurrentAppointmentsResponse> {
            override fun onResponse(
                call: Call<CurrentAppointmentsResponse>,
                response: Response<CurrentAppointmentsResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 0) {
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

    fun updateAppointmentsSlot() {
        var totalDuration = 0.0
        barberServicesLiveData.value!!.forEach() {
            if (it.serviceId in barberServicesSelectLiveData.value!!) {
                totalDuration += it.duration
            }
        }
        appointmentsTotalDurationLiveData.postValue(totalDuration)
        appointmentsSlotLiveData.postValue((totalDuration / 15 + 0.5).roundToInt())
    }

    fun bookAppointment(map: HashMap<String, Any>) {
        val reqJson: String = Gson().toJson(map)
        val body: RequestBody =
            reqJson.toRequestBody("application/json".toMediaTypeOrNull())
        val ps_auth_token = userLiveData.value!!.apiToken
        val call: Call<BookResponse> = appointmentApiService.bookAppointment(ps_auth_token, body)
        call.enqueue(object : Callback<BookResponse> {
            override fun onResponse(call: Call<BookResponse>, response: Response<BookResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 0) {
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

    fun rescheduleAppointment(map: HashMap<String, Any>) {
        val reqJson: String = Gson().toJson(map)
        val body: RequestBody =
            reqJson.toRequestBody("application/json".toMediaTypeOrNull())
        val ps_auth_token = userLiveData.value!!.apiToken
        val call: Call<BookResponse> =
            appointmentApiService.rescheduleAppointment(ps_auth_token, body)
        call.enqueue(object : Callback<BookResponse> {
            override fun onResponse(call: Call<BookResponse>, response: Response<BookResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 0) {
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

    fun cancelAppointment(appointmentId: Int) {
        val url = "appointment/cancelAppointment/" + appointmentId
        val ps_auth_token = userLiveData.value!!.apiToken
        val call: Call<BookResponse> = appointmentApiService.cancelAppointment(url, ps_auth_token)
        call.enqueue(object : Callback<BookResponse> {
            override fun onResponse(call: Call<BookResponse>, response: Response<BookResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 0) {
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

    fun getAppointments() {
        val url = "appointment/myAppointments/" + userLiveData.value!!.userId
        val ps_auth_token = userLiveData.value!!.apiToken
        val call: Call<GetAppoitmentsResponse> =
            appointmentApiService.getAppointments(url, ps_auth_token)
        call.enqueue(object : Callback<GetAppoitmentsResponse> {
            override fun onResponse(
                call: Call<GetAppoitmentsResponse>,
                response: Response<GetAppoitmentsResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 0) {
                        Log.e("loadCurrentAppointments", response.body()!!.appointments.toString())
                        appointmentsLiveData.postValue(response.body()!!.appointments)

                    } else {
                        Log.e("response error", response.body()!!.message)
                    }
                }
            }

            override fun onFailure(call: Call<GetAppoitmentsResponse>, t: Throwable) {
                Log.e("response.body()", t.toString())
                t.printStackTrace()
            }
        })
    }

    fun getAppointmentDetail(appointmentId: Int) {
        val url = "appointment/getAppointmentDetail/" + appointmentId
        val ps_auth_token = userLiveData.value!!.apiToken
        val call: Call<BookResponse> =
            appointmentApiService.getAppointmentDetail(url, ps_auth_token)
        call.enqueue(object : Callback<BookResponse> {
            override fun onResponse(call: Call<BookResponse>, response: Response<BookResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 0) {
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