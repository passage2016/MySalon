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
    if "mobileNo" not in request.json:
        return '{"status":1,"message":"Failed to authenticate"}'
    if "password" not in request.json:
        return '{"status":1,"message":"Failed to authenticate"}'
    return db.signup(request.json["mobileNo"], request.json["password"])


@app.route('/AppUser/login', methods=['POST'])
def login():
    if request.headers.get("Content-type") != "application/json":
        return '{"status":1,"message":"Failed to authenticate"}'
    if "username" not in request.json:
        return '{"status":1,"message":"Failed to authenticate"}'
    if "password" not in request.json:
        return '{"status":1,"message":"Failed to authenticate"}'
    return db.login(request.json["username"], request.json["password"], request.remote_addr)


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
    return db.update_user(request.headers.get("ps_auth_token"), request.json["userId"], request.json["fullName"],
                          request.json["emailId"], request.json["dateOfBirth"],
                          request.json["password"], request.json["profilePic"])


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
    return db.book(request.headers.get("ps_auth_token"), request.json["userId"], request.json["barberId"],
                   request.json["services"], request.json["aptDate"],
                   request.json["timeFrom"], request.json["timeTo"],
                   request.json["totalDuration"], request.json["totalCost"],
                   request.json["couponCode"], request.json["sendSms"])


@app.route('/User/logout', methods=['POST'])
def logout():
    if request.headers.get("ps_auth_token") is None:
        return '{"status":1,"message":"Failed to authenticate"}'
    if request.headers.get("Content-type") != "application/json":
        return '{"status":1,"message":"Failed to authenticate"}'
    if "userId" not in request.json:
        return '{"status":1,"message":"Require userId"}'
    return db.logout(request.headers.get("ps_auth_token"), request.json["userId"])


@app.route('/Service/getServices', methods=['GET'])
def get_services():
    return db.get_services()


@app.route('/ShopContacts/getList', methods=['GET'])
def get_contacts():
    return db.get_contacts()


@app.route('/WorkingHours/getList', methods=['GET'])
def get_working_hours():
    return db.get_working_hours()


@app.route('/Appointment/currentAppointments/<barber_id>', methods=['GET'])
def get_current_appointments(barber_id):
    return db.get_current_appointments(barber_id)


if __name__ == '__main__':
    app.run(host="0.0.0.0", debug=True, port=2333)
