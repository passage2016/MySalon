from flask import Flask, request
import json

import mysqldb

app = Flask(__name__)
db = mysqldb.Mysqldb()


@app.route('/test/get', methods=['GET'])
def test_get():
    str_json = json.dumps(request.args)
    return str_json


@app.route('/test/get/<test_id>', methods=['GET'])
def test_get1(test_id):
    return test_id


@app.route('/test/post', methods=['POST'])
def test_post():
    return '{"msg":"%s"}' % str(request.get_data())


@app.route('/test/header', methods=['GET', 'POST'])
def test_header():
    headers = {}
    for i in request.headers.keys():
        headers[i] = request.headers.get(i)
    str_json = json.dumps(headers)
    return str_json


@app.route('/AppUser/signup', methods=['POST'])
def signup():
    if request.headers.get("Content-type") != "application/json":
        return '{"status":1,"message":"Failed to authenticate"}'
    if "fcmToken" not in request.json:
        return '{"status":1,"message":"Require fcmToken"}'
    if "mobileNo" not in request.json:
        return '{"status":1,"message":"Require mobileNo"}'
    if "password" not in request.json:
        return '{"status":1,"message":"Require password"}'
    json_body = json.loads(request.json)
    return db.signup(json_body["fcmToken"], json_body["mobileNo"], json_body["password"])


@app.route('/AppUser/login', methods=['POST'])
def login():
    if request.headers.get("Content-type") != "application/json":
        return '{"status":1,"message":"Require Content-type"}'
    if "username" not in request.json:
        return '{"status":1,"message":"Require username"}'
    if "password" not in request.json:
        return '{"status":1,"message":"Require password"}'
    json_body = json.loads(request.json)
    return db.login(json_body["username"], json_body["password"], request.remote_addr)


@app.route('/User/logout', methods=['POST'])
def logout():
    if request.headers.get("ps_auth_token") is None:
        return '{"status":1,"message":"Failed to authenticate"}'
    if request.headers.get("Content-type") != "application/json":
        return '{"status":1,"message":"Failed to authenticate"}'
    if "userId" not in request.json:
        return '{"status":1,"message":"Require userId"}'
    json_body = json.loads(request.json)
    return db.logout(request.headers.get("ps_auth_token"), json_body["userId"])


@app.route('/User/updateUser', methods=['POST'])
def update_user():
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
    if "profilePic" not in request.json:
        return '{"status":1,"message":"Require profilePic"}'
    json_body = json.loads(request.json)
    return db.update_user(request.headers.get("ps_auth_token"), json_body["userId"], json_body["fullName"],
                          json_body["emailId"], json_body["dateOfBirth"],
                          json_body["password"], json_body["profilePic"])


@app.route('/User/updateFcmToken', methods=['POST'])
def update_fcm_token():
    if request.headers.get("ps_auth_token") is None:
        return '{"status":1,"message":"Failed to authenticate"}'
    if "userId" not in request.json:
        return '{"status":1,"message":"Require userId"}'
    if "fcmToken" not in request.json:
        return '{"status":1,"message":"Require fcmToken"}'
    json_body = json.loads(request.json)
    return db.update_fcm_token(request.headers.get("ps_auth_token"), json_body["userId"], json_body["fcmToken"])


@app.route('/Appointment/book', methods=['POST'])
def book():
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
    json_body = json.loads(request.json)
    return db.book(request.headers.get("ps_auth_token"), json_body["userId"], json_body["barberId"],
                   json_body["services"], json_body["aptDate"],
                   json_body["timeFrom"], json_body["timeTo"],
                   json_body["totalDuration"], json_body["totalCost"],
                   json_body["couponCode"], json_body["sendSms"])


@app.route('/Appointment/myAppointments/<user_id>', methods=['GET'])
def get_appointments(user_id):
    if request.headers.get("ps_auth_token") is None:
        return '{"status":1,"message":"Failed to authenticate"}'
    return db.get_appointments(request.headers.get("ps_auth_token"), user_id)


@app.route('/Appointment/cancelAppointment/<appointment_id>', methods=['GET'])
def cancel_appointment(appointment_id):
    return db.cancel_appointment(appointment_id)


@app.route('/Appointment/reschedule', methods=['POST'])
def reschedule_appointment():
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
    json_body = json.loads(request.json)
    return db.reschedule_appointment(request.headers.get("ps_auth_token"), json_body["aptNo"],
                                     json_body["timeFrom"], json_body["timeTo"], json_body["aptDate"])


@app.route('/Service/getServices', methods=['GET'])
def get_services():
    return db.get_services()


@app.route('/Offers/getCoupons', methods=['GET'])
def get_coupons():
    return db.get_coupons()


@app.route('/ShopContacts/getList', methods=['GET'])
def get_contacts():
    return db.get_contacts()


@app.route('/WorkingHours/getList', methods=['GET'])
def get_working_hours():
    return db.get_working_hours()


@app.route('/Appointment/currentAppointments/<barber_id>', methods=['GET'])
def get_current_appointments(barber_id):
    return db.get_current_appointments(barber_id)


@app.route('/User/addReviews', methods=['POST'])
def add_reviews():
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
    json_body = json.loads(request.json)
    return db.add_reviews(request.headers.get("ps_auth_token"), json_body["userId"], json_body["rating"],
                          json_body["comment"])


@app.route('/User/reviews', methods=['POST'])
def get_reviews():
    if request.headers.get("Content-type") != "application/json":
        return '{"status":1,"message":"Failed to authenticate"}'
    if "pageSize" not in request.json:
        return '{"status":1,"message":"Require pageSize"}'
    if "pageNo" not in request.json:
        return '{"status":1,"message":"Require pageNo"}'
    json_body = json.loads(request.json)
    return db.get_reviews(json_body["pageSize"], json_body["pageNo"])


@app.route('/Albums/getList', methods=['GET'])
def get_albums():
    return db.get_albums()


@app.route('/Albums/photos/<album_id>', methods=['GET'])
def get_album_photos(album_id):
    return db.get_album_photos(album_id)


@app.route('/AppUser/dashboard', methods=['GET'])
def dashboard():
    return '{"banners":[{"photoName":"Gold Facial","photoUrl":"/uploads/images/Albums/photos/photo11.jpg"},' \
           '{"photoName":"Gold Makeup","photoUrl":"/uploads/images/Albums/photos/photo14.jpg"}],' \
           '"isShopOpened":"Now open","alertMessage":"Some message if created today otherwise empty"}'


if __name__ == '__main__':
    app.run(host="0.0.0.0", debug=True, port=2333)
