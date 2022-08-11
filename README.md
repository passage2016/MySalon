# About My Salon

**About My Salon** This is a sample application that can simulate the operations required by the barber shop, and perform reservation-level query barber shop information waiting.

## Sailent Features:
- Appointment service.
- Check barber shop working hours.
- Add and view reviews.
- View goods and services.

## 📸 Screenshots

**Please click the image below to enlarge.**

<img src="https://github.com/passage2016/MySalon/Register.png" height="600" width="300" hspace="40"><img src="https://github.com/passage2016/MySalon/Login.png" height="600" width="300" hspace="40">

<img src="https://github.com/passage2016/MySalon/ForgetPassword.png" height="600" width="300" hspace="40"><img src="https://github.com/passage2016/MySalon/About.png" height="600" width="300" hspace="40">

<img src="https://github.com/passage2016/MySalon/AboutUs.png" height="600" width="300" hspace="40"><img src="https://github.com/passage2016/MySalon/AppointmentDetail.png" height="600" width="300" hspace="40">

<img src="https://github.com/passage2016/MySalon/AppointmentList.png" height="600" width="300" hspace="40"><img src="https://github.com/passage2016/MySalon/HomeScreen.png" height="600" width="300" hspace="40">

<img src="https://github.com/passage2016/MySalon/NavigateBar.png" height="600" width="300" hspace="40"><img src="https://github.com/passage2016/MySalon/Notifications.png" height="600" width="300" hspace="40">

<img src="https://github.com/passage2016/MySalon/ProductList.png" height="600" width="300" hspace="40"><img src="https://github.com/passage2016/MySalon/RateUs.png" height="600" width="300" hspace="40">

<img src="https://github.com/passage2016/MySalon/Reviews.png" height="600" width="300" hspace="40"><img src="https://github.com/passage2016/MySalon/ServiceDetail.png" height="600" width="300" hspace="40">

<img src="https://github.com/passage2016/MySalon/ShowCase.png" height="600" width="300" hspace="40"><img src="https://github.com/passage2016/MySalon/UpdateUser.png" height="600" width="300" hspace="40">

<img src="https://github.com/passage2016/MySalon/WorkingHour.png" height="600" width="300" hspace="40">


## Built With 🛠
- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - For asynchronous and more..
- [RxJava](https://github.com/ReactiveX/RxJava) - For asynchronous and more..
- [Kotlin Flow](https://kotlinlang.org/docs/flow.html) - For asynchronous and more..
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
  - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Data objects that notify views when the underlying database changes.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes. 
  - [ViewBinding](https://developer.android.com/topic/libraries/view-binding) - Generates a binding class for each XML layout file present in that module and allows you to more easily write code that interacts with views.
- [Retrofit](https://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java.
- [GSON](https://github.com/google/gson) - A Java serialization/deserialization library to convert Java Objects into JSON and back.
- [GSON Converter](https://github.com/square/retrofit/tree/master/retrofit-converters/gson) - A Converter which uses Gson for serialization to and from JSON.
- [OkHttp3](https://github.com/square/okhttp) -  For implementing interceptor, logging and mocking web server.
- [Glide](https://github.com/bumptech/glide) - An image loading and caching library for Android focused on smooth scrolling.
- [Material Components for Android](https://github.com/material-components/material-components-android) - Modular and customizable Material Design UI components for Android.


# Package Structure
    
    com.abhishek.moviefindermvvm    # Root Package
    .
    ├── data                # For data handling.
    │   ├── model           # Model classes
    │   ├── network         # Remote Data Handlers     
    |       └── api         # Retrofit API for remote end point with Coroutines, RxJava and Flow.
    |
    |
    ├── ui                  # Activity/View layer
    │   ├── main            # Main Screen Activity & ViewModel
    |   │   ├── adapter     # Adapter for RecyclerView
    |   │   ├── viewmodel   # ViewHolder for RecyclerView   
    │   └── details         # Detail Screen Activity and ViewModel
    |
    └── utils               # Utility Classes / Kotlin extensions


## Architecture
This app uses [***MVVM (Model View View-Model)***](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) architecture.

![](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)



### Contact - Let's become friend
- [Github](https://github.com/passage2016)

<p>
Don't forget to star ⭐ the repo it motivates me to share more open source
</p>

