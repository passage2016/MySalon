package com.example.mysalon.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysalon.model.remote.*
import com.example.mysalon.model.remote.data.albumList.Album
import com.example.mysalon.model.remote.data.albumList.AlbumListResponse
import com.example.mysalon.model.remote.data.albumPhotos.AlbumPhotosResponse
import com.example.mysalon.model.remote.data.albumPhotos.Photo
import com.example.mysalon.model.remote.data.alert.Alert
import com.example.mysalon.model.remote.data.book.Appointment
import com.example.mysalon.model.remote.data.book.BookResponse
import com.example.mysalon.model.remote.data.contacts.Contact
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
import com.example.mysalon.model.remote.data.product.Product
import com.example.mysalon.model.remote.data.product.ProductsResponse
import com.example.mysalon.model.remote.data.review.getReview.GetReviewResponse
import com.example.mysalon.model.remote.data.review.getReview.Review
import com.example.mysalon.model.remote.data.updateUser.UpdateUserResponse
import com.example.mysalon.model.remote.data.workingHour.Weekday
import com.example.mysalon.utils.PageTool
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.roundToInt

class MainViewModel : ViewModel() {
    val retrofit: Retrofit = ApiClient.getRetrofit()
    var compositeDisposable: CompositeDisposable = CompositeDisposable()
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
    val productsListLiveData = MutableLiveData<ArrayList<Product>>()
    val productLiveData = MutableLiveData<Product>()
    val contactsLiveData = MutableLiveData<ArrayList<Contact>>()
    val workingHourLiveData = MutableLiveData<Map<String, Weekday>>()
    val alertLiveData = MutableLiveData<ArrayList<Alert>>()
    val reviewPageLiveData = MutableLiveData<PageTool>()
    val reviewsListLiveData = MutableLiveData<ArrayList<Review>>()
    val reviewLiveData = MutableLiveData<Review>()
    val couponLiveData = MutableLiveData<ArrayList<String>>()

    fun getCoupons() {
        compositeDisposable.add(baseApiService.getCoupons()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    val coupons = ArrayList<String>()
                    it.coupons.forEach {
                        coupons.add(it.couponCode)
                    }
                    couponLiveData.postValue(coupons)
                },
                { t: Throwable? -> Log.i("Throwable", t?.message ?: "error") }
            )

        )
    }

    fun updateUser  (map: HashMap<String, Any>) {
            val ps_auth_token = userLiveData.value!!.apiToken
            val reqJson: String = Gson().toJson(map)
            val body: RequestBody =
                reqJson.toRequestBody("application/json".toMediaTypeOrNull())
        compositeDisposable.add(appUserApiService.updateUser (ps_auth_token, body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.e("", it.toString())
                    userLiveData.value!!.fullName = it.fullName
                    userLiveData.value!!.emailId = it.emailId
                    userLiveData.value!!.dateOfBirth = it.dateOfBirth
                    userLiveData.value!!.profilePic = it.profilePic
                },
                { t: Throwable? -> Log.i("Throwable", t?.message ?: "error") }
            )

        )
    }

    fun addReview(rating: Double, comment: String) {
        val ps_auth_token = userLiveData.value!!.apiToken
        val map = HashMap<String, Any>()
        map["userId"] = userLiveData.value!!.userId
        map["rating"] = rating
        map["comment"] = comment
        val reqJson: String = Gson().toJson(map)
        val body: RequestBody =
            reqJson.toRequestBody("application/json".toMediaTypeOrNull())
        compositeDisposable.add(appUserApiService.addReview(ps_auth_token, body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.e("message", it.message)
                },
                { t: Throwable? -> Log.i("Throwable", t?.message ?: "error") }
            )

        )
    }

    fun logout() {
        val ps_auth_token = userLiveData.value!!.apiToken
        val map = HashMap<String, Any>()
        map["userId"] = userLiveData.value!!.userId
        val reqJson: String = Gson().toJson(map)
        val body: RequestBody =
            reqJson.toRequestBody("application/json".toMediaTypeOrNull())
        compositeDisposable.add(appUserApiService.logout(ps_auth_token, body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {

                },
                { t: Throwable? -> Log.i("Throwable", t?.message ?: "error") }
            )

        )
    }

    fun loadReviews() {
        var isFirstPage = false
        if (reviewPageLiveData.value == null) {
            isFirstPage = true
            val reviewPage = PageTool(0, false, false, 0, 0, 10)
            reviewPageLiveData.value = reviewPage
        }
        val map = HashMap<String, Any>()
        map["pageSize"] = reviewPageLiveData.value!!.pageSize
        map["pageNo"] = reviewPageLiveData.value!!.currentPage + 1
        val reqJson: String = Gson().toJson(map)
        val body: RequestBody =
            reqJson.toRequestBody("application/json".toMediaTypeOrNull())
        val call: Call<GetReviewResponse> = appUserApiService.getReviews(body)
        call.enqueue(object : Callback<GetReviewResponse> {
            override fun onResponse(
                call: Call<GetReviewResponse>,
                response: Response<GetReviewResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 0) {
                        Log.e("loadProducts", response.body().toString())
                        var reviewList = ArrayList<Review>()
                        reviewsListLiveData.value?.let {
                            if(!isFirstPage){
                                reviewList.addAll(it)
                            }

                        }
                        reviewList.addAll(response.body()!!.reviews)
                        reviewPageLiveData.value!!.isLastPage = response.body()!!.isLastPage
                        reviewPageLiveData.value!!.totalPage = response.body()!!.totalPages
                        reviewsListLiveData.postValue(reviewList)
                        reviewPageLiveData.value!!.isLoading = false
                    } else {
                        Log.e("loadProducts error", response.body()!!.message)
                    }
                }
            }

            override fun onFailure(call: Call<GetReviewResponse>, t: Throwable) {
                Log.e("response.body()", t.toString())
                t.printStackTrace()
            }
        })
    }

    fun getAlert() {
        val url = "alert/getList/" + userLiveData.value!!.userId
        compositeDisposable.add(baseApiService.getAlert(userLiveData.value!!.apiToken, url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    alertLiveData.postValue(it.alert)
                },
                { t: Throwable? -> Log.i("Throwable", t?.message ?: "error") }
            )

        )
    }

    fun getWorkingHour() {
        compositeDisposable.add(baseApiService.getWorkingHours()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    val workingHour = it.timings.entries.sortedBy { getWeekOfDate(it.key) }.associateBy({ it.key }, { it.value })
                    workingHourLiveData.postValue(workingHour)
                },
                { t: Throwable? -> Log.i("Throwable", t?.message ?: "error") }
            )

        )
    }
    fun getWeekOfDate(day: String): Int {
        val weekDays = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        val intDay = weekDays.indexOf(day)
        val cal = Calendar.getInstance()
        var w = cal[Calendar.DAY_OF_WEEK] - 1
        println((intDay + 7 - w) % 7)
        if (w < 0) {
            w = 0
        }
        return (intDay + 7 - w) % 7
    }

    fun getContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = baseApiService.getContacts()
                if (!response.isSuccessful) {
                    return@launch
                }
                response.body()?.let {
                    val contacts: ArrayList<Contact> = it.contacts
                    contacts.sortBy { it.displayOrder }
                    contactsLiveData.postValue(contacts)
                }
            } catch (e: Exception) {
                Log.e("Exception", e.toString())
                e.printStackTrace()
            }
        }
    }

    fun loadProducts() {
        if (productPageLiveData.value == null) {
            val productPage = PageTool(0, false, false, 0, 0, 10)
            productPageLiveData.value = productPage
        }
        val map = HashMap<String, Any>()
        map["pageSize"] = productPageLiveData.value!!.pageSize
        map["pageNo"] = productPageLiveData.value!!.currentPage + 1
        val reqJson: String = Gson().toJson(map)
        val body: RequestBody =
            reqJson.toRequestBody("application/json".toMediaTypeOrNull())
        val call: Call<ProductsResponse> = baseApiService.getProducts(body)
        call.enqueue(object : Callback<ProductsResponse> {
            override fun onResponse(
                call: Call<ProductsResponse>,
                response: Response<ProductsResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 0) {
                        Log.e("loadProducts", response.body().toString())
                        var productList = ArrayList<Product>()
                        productsListLiveData.value?.let {
                            productList.addAll(it)
                        }
                        productList.addAll(response.body()!!.products)
                        productPageLiveData.value!!.isLastPage = response.body()!!.isLastPage
                        productPageLiveData.value!!.totalPage = response.body()!!.totalPages
                        productsListLiveData.postValue(productList)
                        productPageLiveData.value!!.isLoading = false
                    } else {
                        Log.e("loadProducts error", response.body()!!.message)
                    }
                }
            }

            override fun onFailure(call: Call<ProductsResponse>, t: Throwable) {
                Log.e("response.body()", t.toString())
                t.printStackTrace()
            }
        })
    }

    fun loadAlbumList() {
        if (albumListLiveData.value == null) {
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
        val call: Call<GetServicesByCategoryResponse> =
            baseApiService.getServicesByCategory(url)
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
        val call: Call<CurrentAppointmentsResponse> =
            appointmentApiService.currentAppointments(url)
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
        val call: Call<BookResponse> =
            appointmentApiService.bookAppointment(ps_auth_token, body)
        call.enqueue(object : Callback<BookResponse> {
            override fun onResponse(
                call: Call<BookResponse>,
                response: Response<BookResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 0) {
                        Log.e(
                            "loadCurrentAppointments",
                            response.body()!!.appointment.toString()
                        )
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
            override fun onResponse(
                call: Call<BookResponse>,
                response: Response<BookResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 0) {
                        Log.e(
                            "loadCurrentAppointments",
                            response.body()!!.appointment.toString()
                        )
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
        val call: Call<BookResponse> =
            appointmentApiService.cancelAppointment(url, ps_auth_token)
        call.enqueue(object : Callback<BookResponse> {
            override fun onResponse(
                call: Call<BookResponse>,
                response: Response<BookResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 0) {
                        Log.e(
                            "loadCurrentAppointments",
                            response.body()!!.appointment.toString()
                        )
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
                        Log.e(
                            "loadCurrentAppointments",
                            response.body()!!.appointments.toString()
                        )
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
            override fun onResponse(
                call: Call<BookResponse>,
                response: Response<BookResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 0) {
                        Log.e(
                            "loadCurrentAppointments",
                            response.body()!!.appointment.toString()
                        )
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
