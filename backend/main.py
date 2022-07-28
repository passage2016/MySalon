from flask import Flask, request, Blueprint
from flask_docs import ApiDoc
import json
import re

import mysqldb

app = Flask(__name__)
db = mysqldb.Mysqldb()
app.config['API_DOC_MEMBER'] = ['test', 'app_user', 'service', 'barber', 'offers', 'shop_contacts', 'albums',
                                'working_hours', 'appointment', 'alert']
ApiDoc(
    app,
    title="Barber App",
    version="3.0.1",
    description="Barber App API",
)
test = Blueprint("test", __name__)
app_user = Blueprint("app_user", __name__)
service = Blueprint("service", __name__)
barber = Blueprint("barber", __name__)
offers = Blueprint("offers", __name__)
shop_contacts = Blueprint("shop_contacts", __name__)
albums = Blueprint("albums", __name__)
working_hours = Blueprint("working_hours", __name__)
appointment = Blueprint("appointment", __name__)
alert = Blueprint("alert", __name__)


@test.route('/get', methods=['GET'])
def test_get():
    """Return params as json format

    @@@
    #### args

    > Any

    #### return
    - ##### json
    ```json
    {"status": 0, "message": "Success"}
    ```
    @@@
    """
    result = request.args.to_dict()
    result["status"] = "0"
    result["message"] = "Success"
    str_json = json.dumps(result)
    print(str_json)
    return str_json


@test.route('/get/<test_id>', methods=['GET'])
def test_get1(test_id):
    """Return params as json format

        @@@
        #### args

        > Any

        #### return
        - ##### json
        ```json
        {"status": 0, "message": "Success", "testId": "test_id"}
        ```
        @@@
    """
    result = {"status": "0", "message": "Success", "testId": test_id}
    print(json.dumps(result))
    return json.dumps(result)


@test.route('/post', methods=['POST'])
def test_post():
    """Return body as json format

        @@@
        #### args

        > Any

        #### return
        - ##### json
        ```json
        {"status": 0, "message": "body of post request" }
        ```
        @@@
    """
    print('{"status": "0", "message":"%s"}' % str(request.get_data()))
    return '{"status": "0", "message":"%s"}' % str(request.get_data())


@test.route('/header', methods=['GET', 'POST'])
def test_header():
    """Return headers as json format

            @@@
            #### args

            > Any

            #### return
            - ##### json
            ```json
            {"status": 0, "message": "Success", "headers": {request header key: request header value}}
            ```
            @@@
        """
    headers = {}
    for i in request.headers.keys():
        headers[i] = request.headers.get(i)
    result = {"status": 0, "message": "Success", "headers": headers}
    str_json = json.dumps(result)
    print(str_json)
    return str_json


@app_user.route('/signup', methods=['POST'])
def signup():
    """

        @@@
        #### args

        | args | nullable | type | remark |
        |--------|--------|--------|--------|
        |    mobileNo   |    false  |    string   |    8-13 digits                 |
        |    password   |    false  |    string   |    at least 8 characters    |
        |    fcmToken   |    false  |    string   |    fcm token                |
        - ##### json
        ```json
        {"mobileNo": 1234567890, "password": "12345678", "fcmToken": "8e2f1c6f2774a3"}
        ```

        #### return
        - ##### json
        ```json
        {"status": 0, "message": "Success", "otp": "6200", "userId": "1"}
        ```

        #### update
        - ##### 3.0.1:
        ```
        change mobileNo to 8-13 digits
        ```

        @@@


    """
    print(request.json)
    if not isinstance(request.json, dict):
        return '{"status":1,"message":"Require json body."}'
    if request.headers.get("Content-type") != "application/json":
        return '{"status":1,"message":"Failed to authenticate"}'
    if "fcmToken" not in request.json:
        return '{"status":1,"message":"Require fcmToken"}'
    if "mobileNo" not in request.json:
        return '{"status":1,"message":"Require mobileNo"}'
    phone_rule = re.compile(r'^\d{8,13}$')
    if not re.match(phone_rule, request.json["mobileNo"]):
        return '{"status":1,"message":"MobileNo should be 8-13 digits."}'
    if len(request.json["password"]) < 8:
        return '{"status":1,"message":"Password at least 8 characters."}'
    if "password" not in request.json:
        return '{"status":1,"message":"Require password"}'

    return db.signup(request.json["fcmToken"], request.json["mobileNo"], request.json["password"])


@app_user.route('/login', methods=['POST'])
def login():
    """
        @@@
        #### arg

        | args | nullable | type | remark |
        |--------|--------|--------|--------|
        |    mobileNo   |    false  |    string   |    8-13 digits                 |
        |    password   |    false  |    string   |    at least 8 characters    |
        ```json
        {"mobileNo":"9999999999", "password":"password"}
        ```

        #### return
        - ##### json
        ```json
        {"status": 0, "message": "Authenticated successfully", "userId": 1,
        "fullName": "", "emailId": "", "gender": "", "mobileNo": "9999999999", "password":
        "912ec803b2ce49e4a541068d495ab570", "dateOfBirth": "", "profilePic": "", "isActive": 1,
        "isMobileVerified": 0, "isEmailVerified": 0, "fcmToken": "", "ipAddress": "127.0.0.1",
        "emailVerificationCode": "2213", "evcExpiresOn": "", "apiToken": "8333404feb6243fa94217433168078cd",
        "tokenValidUpTo": "", "createdOn": "2022-07-23 16:19:32", "updatedOn": "2022-07-23 16:19:32",
        "deletedOn": "", "isDeleted": 0}
        ```

        #### update
        - ##### 3.0.1:
        ```
        change username to mobileNo
        change mobileNo to 8-13 digits
        ```
        @@@
    """
    if request.headers.get("Content-type") != "application/json":
        return '{"status":1,"message":"Require Content-type"}'
    if "mobileNo" not in request.json:
        return '{"status":1,"message":"Require mobileNo"}'
    phone_rule = re.compile(r'^\d{8,13}$')
    if not re.match(phone_rule, request.json["mobileNo"]):
        return '{"status":1,"message":"MobileNo should be 8-13 digits."}'
    if "password" not in request.json:
        return '{"status":1,"message":"Require password"}'
    print(request.json)
    if not isinstance(request.json, dict):
        return '{"status":1,"message":"Require json body."}'
    return db.login(request.json["mobileNo"], request.json["password"], request.remote_addr)


@app_user.route('/updateUser', methods=['POST'])
def update_user():
    """
        @@@
        #### arg

        | args | nullable | type | remark |
        |--------|--------|--------|--------|
        |    userId         |    false  |    string   |         |
        |    fullName       |    false  |    string   |         |
        |    emailId        |    false  |    string   |         |
        |    dateOfBirth    |    false  |    string   |         |
        |    password       |    false  |    string   |         |
        |    profilePic     |    false  |    string   |         |

        @@@
    """
    print(request.json)
    if not isinstance(request.json, dict):
        return '{"status":1,"message":"Require json body."}'
    if request.headers.get("Content-type") != "application/json":
        return '{"status":1,"message":"Require Content-type"}'
    if request.headers.get("ps_auth_token") is None:
        return '{"status":1,"message":"Failed to authenticate"}'
    if "userId" not in request.json:
        return '{"status":1,"message":"Require userId"}'
    if "fullName" not in request.json:
        return '{"status":1,"message":"Require fullName"}'
    if "emailId" not in request.json:
        return '{"status":1,"message":"Require emailId"}'
    if "dateOfBirth" not in request.json:
        return '{"status":1,"message":"Require dateOfBirth"}'
    if "password" not in request.json:
        return '{"status":1,"message":"Require password"}'
    if len(request.json["password"]) < 8:
        return '{"status":1,"message":"Password at least 8 characters."}'
    if "profilePic" not in request.json:
        return '{"status":1,"message":"Require profilePic"}'
    return db.update_user(request.headers.get("ps_auth_token"), request.json["userId"], request.json["fullName"],
                          request.json["emailId"], request.json["dateOfBirth"],
                          request.json["password"], request.json["profilePic"])


@app_user.route('/updateFcmToken', methods=['POST'])
def update_fcm_token():
    """
        @@@
        #### arg

        | args | nullable | type | remark |
        |--------|--------|--------|--------|
        |    userId         |    false  |    string   |         |
        |    fcmToken       |    false  |    string   |         |

        @@@
    """
    if request.headers.get("Content-type") != "application/json":
        return '{"status":1,"message":"Require Content-type"}'
    if request.headers.get("ps_auth_token") is None:
        return '{"status":1,"message":"Failed to authenticate"}'
    if "userId" not in request.json:
        return '{"status":1,"message":"Require userId"}'
    if "fcmToken" not in request.json:
        return '{"status":1,"message":"Require fcmToken"}'
    if not isinstance(request.json, dict):
        return '{"status":1,"message":"Require json body."}'
    return db.update_fcm_token(request.headers.get("ps_auth_token"), request.json["userId"], request.json["fcmToken"])


@app_user.route('/getPhoneVerificationCode/<mobile_no>', methods=['GET'])
def get_phone_verification_code(mobile_no: str):
    """
        @@@
        #### arg

        | args | nullable | type | remark |
        |--------|--------|--------|--------|
        |    mobile_no         |    false  |    string   |   8-13 digits   |

        #### update
        - ##### 3.0.1:
        ```
        change mobile_no to 8-13 digits
        ```
        @@@
    """
    phone_rule = re.compile(r'^\d{8,13}$')
    if not re.match(phone_rule, mobile_no):
        return '{"status":1,"message":"MobileNo should be 8-13 digits."}'
    return db.get_phone_verification_code(mobile_no)


@app_user.route('/resetPassword', methods=['POST'])
def reset_password():
    """
        @@@
        #### arg

        | args | nullable | type | remark |
        |--------|--------|--------|--------|
        |    mobileNo         |    false  |    string   |   8-13 digits   |
        |    phoneVerificationCode       |    false  |    string   |         |
        |    password        |    false  |    string   |         |

        #### update
        - ##### 3.0.1:
        ```
        change mobileNo to 8-13 digits
        ```
        @@@
    """
    print(request.json)
    if not isinstance(request.json, dict):
        return '{"status":1,"message":"Require json body."}'
    if request.headers.get("Content-type") != "application/json":
        return '{"status":1,"message":"Require Content-type"}'
    if "mobileNo" not in request.json:
        return '{"status":1,"message":"Require mobileNo"}'
    phone_rule = re.compile(r'^\d{8,13}$')
    if not re.match(phone_rule, request.json["mobileNo"]):
        return '{"status":1,"message":"MobileNo should be 8-13 digits."}'
    if "phoneVerificationCode" not in request.json:
        return '{"status":1,"message":"Require phoneVerificationCode"}'
    if "password" not in request.json:
        return '{"status":1,"message":"Require password"}'
    if len(request.json["password"]) < 8:
        return '{"status":1,"message":"Password at least 8 characters."}'
    return db.reset_password(request.json["mobileNo"], request.json["phoneVerificationCode"], request.json["password"])


@app_user.route('/logout', methods=['POST'])
def logout():
    """
        @@@
        #### arg

        | args | nullable | type | remark |
        |--------|--------|--------|--------|
        |    userId         |    false  |    string   |         |

        @@@
    """
    if request.headers.get("ps_auth_token") is None:
        return '{"status":1,"message":"Failed to authenticate"}'
    if request.headers.get("Content-type") != "application/json":
        return '{"status":1,"message":"Failed to authenticate"}'
    if "userId" not in request.json:
        return '{"status":1,"message":"Require userId"}'
    if not isinstance(request.json, dict):
        return '{"status":1,"message":"Require json body."}'
    return db.logout(request.headers.get("ps_auth_token"), request.json["userId"])


@app_user.route('/addReviews', methods=['POST'])
def add_reviews():
    """
        @@@
        #### arg

        | args | nullable | type | remark |
        |--------|--------|--------|--------|
        |    userId         |    false  |    string   |         |
        |    rating       |    false  |    string   |         |
        |    comment        |    false  |    string   |         |

        @@@
    """
    if request.headers.get("ps_auth_token") is None:
        return '{"status":1,"message":"Failed to authenticate"}'
    if request.headers.get("Content-type") != "application/json":
        return '{"status":1,"message":"Failed to authenticate"}'
    if "userId" not in request.json:
        return '{"status":1,"message":"Require userId"}'
    if "rating" not in request.json:
        return '{"status":1,"message":"Require rating"}'
    if "comment" not in request.json:
        return '{"status":1,"message":"Require comment"}'
    if not isinstance(request.json, dict):
        return '{"status":1,"message":"Require json body."}'
    return db.add_reviews(request.headers.get("ps_auth_token"), request.json["userId"], request.json["rating"],
                          request.json["comment"])


@app_user.route('/reviews', methods=['POST'])
def get_reviews():
    """
        @@@
        #### arg

        | args | nullable | type | remark |
        |--------|--------|--------|--------|
        |    pageSize         |    false  |    string   |         |
        |    pageNo       |    false  |    string   |         |

        @@@
    """
    if not isinstance(request.json, dict):
        return '{"status":1,"message":"Require json body."}'
    if request.headers.get("Content-type") != "application/json":
        return '{"status":1,"message":"Failed to authenticate"}'
    if "pageSize" not in request.json:
        return '{"status":1,"message":"Require pageSize"}'
    if not request.json["pageSize"].isdigit():
        return '{"status":1,"message":"The pageSize must be digit."}'
    if int(request.json["pageSize"]) < 1:
        return '{"status":1,"message":"The pageSize at least 1."}'
    if "pageNo" not in request.json:
        return '{"status":1,"message":"Require pageNo"}'
    if not request.json["pageNo"].isdigit():
        return '{"status":1,"message":"The pageNo must be digit."}'
    if int(request.json["pageSize"]) < 1:
        return '{"status":1,"message":"The pageNo at least 1."}'

    return db.get_reviews(request.json["pageSize"], request.json["pageNo"])


@app_user.route('/dashboard', methods=['GET'])
def dashboard():
    if db.get_shop_status():
        is_shop_opened = "Now open"
    else:
        is_shop_opened = "Now close"
    return '{"banners":[{"photoName":"Gold Facial","photoUrl":"/uploads/images/Albums/photos/photo11.jpg"},' \
           '{"photoName":"Gold Makeup","photoUrl":"/uploads/images/Albums/photos/photo14.jpg"}],' \
           '"isShopOpened":"%s","alertMessage":"Some message if created today otherwise empty"}' % is_shop_opened


@service.route('/getServices', methods=['GET'])
def get_services():
    return db.get_services()


@barber.route('/addBarber', methods=['POST'])
def add_barber():
    """

            @@@
            #### args

            | args | nullable | type | remark |
            |--------|--------|--------|--------|
            |    barberName   |    false  |    string   |    9 digits                 |
            |    isAdmin   |    false  |    int   |    0: false 1: true    |
            |    isBarber   |    false  |    int   |    0: false 1: true                |
            |    mobileNo   |    false  |    string   |    8-13 digits                |
            |    profilePic   |    false  |    string   |                    |
            |    gender   |    false  |    string   |    M/F                |
            |    breakTimeFrom   |    false  |    string   |    hh:mm                |
            |    breakTimeTo   |    false  |    string   |    hh:mm                |
            |    hasDefaultServices   |    false  |    int   |    1                |
            |    holiday   |    false  |    string   |   Monday,Wednesday     |
            |    userRating   |    false  |    double   |                    |
            |    password   |    false  |    string   |    at least 8 characters                |
            |    type   |    false  |    string   |                    |
            |    payment   |    false  |    double   |                    |


            #### return
            - ##### json
            ```json
            {"status": 0, "message": "Success", "barberId": "1"}
            ```
            @@@
        """
    print(request.json)
    if not isinstance(request.json, dict):
        return '{"status":1,"message":"Require json body."}'
    if request.headers.get("Content-type") != "application/json":
        return '{"status":1,"message":"Failed to authenticate"}'
    if "barberName" not in request.json:
        return '{"status":1,"message":"Require barberName"}'
    if "isAdmin" not in request.json:
        return '{"status":1,"message":"Require isAdmin"}'
    if not request.json["isAdmin"].isdigit():
        return '{"status":1,"message":"The isAdmin must be digit."}'
    if "isBarber" not in request.json:
        return '{"status":1,"message":"Require isBarber"}'
    if not request.json["isBarber"].isdigit():
        return '{"status":1,"message":"The isBarber must be digit."}'
    if "mobileNo" not in request.json:
        return '{"status":1,"message":"Require mobileNo"}'
    phone_rule = re.compile(r'^\d{8,13}$')
    if not re.match(phone_rule, request.json["mobileNo"]):
        return '{"status":1,"message":"MobileNo should be 8-13 digits."}'
    if "profilePic" not in request.json:
        return '{"status":1,"message":"Require profilePic"}'
    if "gender" not in request.json:
        return '{"status":1,"message":"Require gender"}'
    if "breakTimeFrom" not in request.json:
        return '{"status":1,"message":"Require breakTimeFrom"}'
    time_rule = re.compile(r'^\d{2}:\d{2}$')
    if not re.match(time_rule, request.json["breakTimeFrom"]):
        return '{"status":1,"message":"breakTimeFrom should be hh:mm."}'
    if "breakTimeTo" not in request.json:
        return '{"status":1,"message":"Require breakTimeTo"}'
    if not re.match(time_rule, request.json["breakTimeTo"]):
        return '{"status":1,"message":"breakTimeTo should be hh:mm."}'
    if "hasDefaultServices" not in request.json:
        return '{"status":1,"message":"Require hasDefaultServices"}'
    if not request.json["hasDefaultServices"].isdigit():
        return '{"status":1,"message":"The hasDefaultServices must be digit."}'
    if "holiday" not in request.json:
        return '{"status":1,"message":"Require holiday"}'
    if "userRating" not in request.json:
        return '{"status":1,"message":"Require userRating"}'
    if "password" not in request.json:
        return '{"status":1,"message":"Require password"}'
    if len(request.json["password"]) < 8:
        return '{"status":1,"message":"Password at least 8 characters."}'
    if "type" not in request.json:
        return '{"status":1,"message":"Require type"}'
    if "payment" not in request.json:
        return '{"status":1,"message":"Require payment"}'

    return db.add_barber(request.json["barberName"], request.json["isAdmin"], request.json["isBarber"],
                         request.json["mobileNo"], request.json["profilePic"], request.json["gender"],
                         request.json["breakTimeFrom"], request.json["breakTimeTo"], request.json["hasDefaultServices"],
                         request.json["holiday"], request.json["userRating"], request.json["password"],
                         request.json["type"], request.json["payment"])


@barber.route('/getBarbers', methods=['GET'])
def get_barbers():
    return db.get_barbers()


@barber.route('/getBarberServices', methods=['POST'])
def get_barbers_services():
    return db.get_services()


@offers.route('/getCoupons', methods=['GET'])
def get_coupons():
    return db.get_coupons()


@offers.route('/getProducts', methods=['POST'])
def get_products():
    """
        @@@
        #### arg

        | args | nullable | type | remark |
        |--------|--------|--------|--------|
        |    pageSize         |    false  |    string   |         |
        |    pageNo       |    false  |    string   |         |

        @@@
    """
    if not isinstance(request.json, dict):
        return '{"status":1,"message":"Require json body."}'
    if request.headers.get("Content-type") != "application/json":
        return '{"status":1,"message":"Failed to authenticate"}'
    if "pageSize" not in request.json:
        return '{"status":1,"message":"Require pageSize"}'
    if not request.json["pageSize"].isdigit():
        return '{"status":1,"message":"The pageSize must be digit."}'
    if int(request.json["pageSize"]) < 1:
        return '{"status":1,"message":"The pageSize at least 1."}'
    if "pageNo" not in request.json:
        return '{"status":1,"message":"Require pageNo"}'
    if not request.json["pageNo"].isdigit():
        return '{"status":1,"message":"The pageNo must be digit."}'
    if int(request.json["pageSize"]) < 1:
        return '{"status":1,"message":"The pageNo at least 1."}'

    return db.get_products(request.json["pageSize"], request.json["pageNo"])


@offers.route('/getList', methods=['GET'])
def get_offers():
    return db.get_offers()


@shop_contacts.route('/getList', methods=['GET'])
def get_contacts():
    return db.get_contacts()


@albums.route('/getList', methods=['GET'])
def get_albums():
    return db.get_albums()


@albums.route('/photos/<album_id>', methods=['GET'])
def get_album_photos(album_id):
    return db.get_album_photos(album_id)


@working_hours.route('/getList', methods=['GET'])
def get_working_hours():
    return db.get_working_hours()


@appointment.route('/currentAppointments/<barber_id>', methods=['GET'])
def get_current_appointments(barber_id):
    return db.get_current_appointments(barber_id)


@appointment.route('/book', methods=['POST'])
def book():
    """
        @@@
        #### arg

        | args | nullable | type | remark |
        |--------|--------|--------|--------|
        |    userId         |    false  |    string   |         |
        |    barberId       |    false  |    string   |         |
        |    services        |    false  |    string   |         |
        |    aptDate    |    false  |    string   |         |
        |    timeFrom       |    false  |    string   |         |
        |    timeTo     |    false  |    string   |         |
        |    totalDuration     |    false  |    string   |         |
        |    totalCost     |    false  |    string   |         |
        |    couponCode     |    false  |    string   |         |
        |    sendSms     |    false  |    string   |         |

        @@@
    """
    if request.headers.get("Content-type") != "application/json":
        return '{"status":1,"message":"Failed to authenticate"}'
    if request.headers.get("ps_auth_token") is None:
        return '{"status":1,"message":"Failed to authenticate"}'
    if "userId" not in request.json:
        return '{"status":1,"message":"Require userId"}'
    if "barberId" not in request.json:
        return '{"status":1,"message":"Require barberId"}'
    if "services" not in request.json:
        return '{"status":1,"message":"Require services"}'
    if "aptDate" not in request.json:
        return '{"status":1,"message":"Require aptDate"}'
    if "timeFrom" not in request.json:
        return '{"status":1,"message":"Require timeFrom"}'
    if "timeTo" not in request.json:
        return '{"status":1,"message":"Require timeTo"}'
    if "totalDuration" not in request.json:
        return '{"status":1,"message":"Require totalDuration"}'
    if "totalCost" not in request.json:
        return '{"status":1,"message":"Require totalCost"}'
    if "couponCode" not in request.json:
        return '{"status":1,"message":"Require couponCode"}'
    if "sendSms" not in request.json:
        return '{"status":1,"message":"Require sendSms"}'
    if not isinstance(request.json, dict):
        return '{"status":1,"message":"Require json body."}'
    return db.book(request.headers.get("ps_auth_token"), request.json["userId"], request.json["barberId"],
                   request.json["services"], request.json["aptDate"],
                   request.json["timeFrom"], request.json["timeTo"],
                   request.json["totalDuration"], request.json["totalCost"],
                   request.json["couponCode"], request.json["sendSms"])


@appointment.route('/myAppointments/<user_id>', methods=['GET'])
def get_appointments(user_id):
    if request.headers.get("ps_auth_token") is None:
        return '{"status":1,"message":"Failed to authenticate"}'
    return db.get_appointments(request.headers.get("ps_auth_token"), user_id)


@appointment.route('/cancelAppointment/<appointment_id>', methods=['GET'])
def cancel_appointment(appointment_id):
    return db.cancel_appointment(appointment_id)


@appointment.route('/reschedule', methods=['POST'])
def reschedule_appointment():
    """
        @@@
        #### arg

        | args | nullable | type | remark |
        |--------|--------|--------|--------|
        |    aptNo         |    false  |    string   |         |
        |    timeFrom       |    false  |    string   |         |
        |    timeTo        |    false  |    string   |         |
        |    aptDate    |    false  |    string   |         |

        @@@
    """
    if request.headers.get("Content-type") != "application/json":
        return '{"status":1,"message":"Failed to authenticate"}'
    if request.headers.get("ps_auth_token") is None:
        return '{"status":1,"message":"Failed to authenticate"}'
    if "aptNo" not in request.json:
        return '{"status":1,"message":"Require aptNo"}'
    if "timeFrom" not in request.json:
        return '{"status":1,"message":"Require timeFrom"}'
    if "timeTo" not in request.json:
        return '{"status":1,"message":"Require timeTo"}'
    if "aptDate" not in request.json:
        return '{"status":1,"message":"Require aptDate"}'
    if not isinstance(request.json, dict):
        return '{"status":1,"message":"Require json body."}'
    return db.reschedule_appointment(request.headers.get("ps_auth_token"), request.json["aptNo"],
                                     request.json["timeFrom"], request.json["timeTo"], request.json["aptDate"])


@alert.route('/getList', methods=['GET'])
def get_alert():
    return db.get_alert()


app.register_blueprint(test, url_prefix="/test")
app.register_blueprint(app_user, url_prefix="/appUser")
app.register_blueprint(service, url_prefix="/service")
app.register_blueprint(barber, url_prefix="/barber")
app.register_blueprint(offers, url_prefix="/offers")
app.register_blueprint(shop_contacts, url_prefix="/shopContacts")
app.register_blueprint(albums, url_prefix="/albums")
app.register_blueprint(working_hours, url_prefix="/workingHours")
app.register_blueprint(appointment, url_prefix="/appointment")
app.register_blueprint(alert, url_prefix="/alert")

if __name__ == '__main__':
    app.run(host="0.0.0.0", debug=True, port=2333)
