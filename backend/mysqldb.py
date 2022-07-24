import pymysql
import json
import hashlib
import time
import uuid


class Mysqldb:
    with open("./MysqlInfo.json", 'r') as load_f:
        load_dict = json.load(load_f)
        sql_host = load_dict["host"]
        sql_password = load_dict["password"]

    def signup(self, mobile_no, password):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        sql = "SELECT * FROM user WHERE mobileNo = %s;" % mobile_no
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            if len(results) > 0:
                return '{"status":1,"message":"Mobile No already register."}'
            md5 = hashlib.md5()
            md5.update(password.encode('utf-8'))
            dt = time.strftime('%Y-%m-%d %H:%M:%S')
            sql = "INSERT INTO user(mobileNo, password, createdOn, updatedOn) \
                           VALUES ('%s', '%s', '%s', '%s')" % (mobile_no, md5.hexdigest(), dt, dt)
            cursor.execute(sql)
            db.commit()
        except:
            db.rollback()
            db.close()
            return '{"status":1,"message":"Signup error."}'
        db.close()
        return '{"status":0,"message":"Success"}'

    def login(self, mobile_no, password, ip):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        sql = "SELECT * FROM user WHERE mobileNo = %s;" % mobile_no
        result = {"status": 0, "message": "Authenticated successfully"}
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            if len(results) == 0:
                db.close()
                return '{"status":1,"message":"Account not exit."}'
            for row in results:
                md5 = hashlib.md5()
                md5.update(password.encode('utf-8'))
                if md5.hexdigest() != row[5]:
                    db.close()
                    return '{"status":1,"message":"Wrong password."}'
                userId = row[0]
                token = uuid.uuid4().hex
                result["userId"] = userId
                result["fullName"] = row[1]
                result["emailId"] = row[2]
                result["gender"] = row[3]
                result["mobileNo"] = row[4]
                result["password"] = row[5]
                result["dateOfBirth"] = str(row[6])
                result["profilePic"] = row[7]
                result["isActive"] = 1
                result["isMobileVerified"] = row[9]
                result["isEmailVerified"] = row[10]
                result["fcmToken"] = row[11]
                result["ipAddress"] = ip
                result["emailVerificationCode"] = row[13]
                result["evcExpiresOn"] = row[14]
                result["apiToken"] = token
                result["tokenValidUpTo"] = str(row[16])
                result["createdOn"] = str(row[17])
                result["updatedOn"] = str(row[18])
                result["deletedOn"] = str(row[19])
                result["isDeleted"] = row[20]
        except:
            db.close()
            return '{"status":1,"message":"Login error."}'
        cursor = db.cursor()

        sql = "UPDATE user SET isActive = 1, apiToken = '%s' WHERE userId = '%s';" % (token, userId)
        try:
            cursor.execute(sql)
            db.commit()
        except:
            db.rollback()
            db.close()
            return '{"status":1,"message":"Login update error."}'
        db.close()
        return json.dumps(result)

    def update_user(self, ps_auth_token, user_id, full_name, email_id, date_of_birth, password, profile_pic):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        sql = "SELECT * FROM user WHERE userId = %s;" % user_id
        result = {"status": 0, "message": "Authenticated successfully"}
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            if len(results) == 0:
                db.close()
                return '{"status":1,"message":"Account not exit."}'
            for row in results:
                md5 = hashlib.md5()
                md5.update(password.encode('utf-8'))
                new_password = md5.hexdigest()
                if ps_auth_token != row[15]:
                    db.close()
                    return '{"status":1,"message":"Failed to authenticate."}'
                result["userId"] = user_id
                result["fullName"] = full_name
                result["emailId"] = email_id
                result["password"] = new_password
                result["dateOfBirth"] = date_of_birth
                result["profilePic"] = profile_pic
        except:
            db.close()
            return '{"status":1,"message":"Update user error."}'
        cursor = db.cursor()

        sql = "UPDATE user SET fullName = '%s', emailId = '%s', password = '%s', dateOfBirth = '%s', profilePic = '%s'\
         WHERE userId = '%s'" % (full_name, email_id, new_password, date_of_birth, profile_pic, user_id)
        try:
            cursor.execute(sql)
            db.commit()
        except:
            db.rollback()
            db.close()
            return '{"status":1,"message":"Update user error."}'
        db.close()
        return json.dumps(result)

    def logout(self, ps_auth_token, user_id):
        if not self.api_key_check(ps_auth_token, user_id):
            return '{"status":1,"message":"Failed to authenticate"}'
        result = {"status": 0, "message": "Logout successfully"}
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        sql = "UPDATE user SET isActive = 0 WHERE userId = '%s';" % user_id
        try:
            cursor.execute(sql)
            db.commit()
        except:
            db.rollback()
            db.close()
            return '{"status":1,"message":"Logout update error."}'
        db.close()
        return json.dumps(result)

    def get_services(self):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        sql = "SELECT * FROM services;"
        result = {"status": 0, "services": {}}

        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                service = {"serviceId": row[0], "serviceName": row[1], "duration": row[3], "cost": row[4],
                           "servicePic": row[5]}
                if row[2] in result["services"]:
                    result["services"][row[2]].append(service)
                else:
                    result["services"][row[2]] = []
                    result["services"][row[2]].append(service)
        except:
            db.close()
            return '{"status":1,"message":"Failed to authenticate."}'
        db.close()
        return json.dumps(result)

    def get_contacts(self):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        sql = "SELECT * FROM contact;"
        result = {"status": 0, "message": "Success", "contacts": []}
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                contact = {"contactId": row[0], "contactTitle": row[1], "contactType": row[2], "contactData": row[3],
                           "iconUrl": row[4], "displayOrder": row[5]}
                result["contacts"].append(contact)
        except:
            db.close()
            return '{"status":1,"message":"Failed to authenticate."}'
        db.close()
        return json.dumps(result)

    def get_working_hours(self):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        sql = "SELECT * FROM workingHours;"
        result = {"status": 0, "message": "Success", "timings": {}}
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                working_hours = {"fromTime": str(row[1]), "toTime": str(row[2])}
                result["timings"][row[0]] = working_hours
        except:
            db.close()
            return '{"status":1,"message":"Database error."}'
        db.close()
        return json.dumps(result)

    def get_current_appointments(self, barber_id):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        sql = "SELECT * FROM workingHours;"
        result = {"status": 0, "message": "Success", "timings": {}}
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                working_hours = {"fromTime": str(row[1]), "toTime": str(row[2])}
                result["timings"][row[0]] = working_hours
        except:
            db.close()
            return '{"status":1,"message":"Database error."}'
        db.close()
        return json.dumps(result)

    def book(self, ps_auth_token, user_id, barber_id, services: str, apt_date, time_from,
             time_to, total_duration, total_cost, coupon_code, send_sms: str):
        if not self.api_key_check(ps_auth_token, user_id):
            return '{"status":1,"message":"Failed to authenticate."}'
        result = {"status": 0, "message": "Book appointment successfully", "appointment": {}}
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        user = self.get_user(user_id)
        if user is None:
            return '{"status":1,"message":"Failed to get user."}'
        barber = self.get_barber(barber_id)
        if barber is None:
            return '{"status":1,"message":"Failed to get barber."}'
        services = services.replace(" ", "")[1:-1]
        send_sms = send_sms.lower()
        if send_sms == "true":
            send_sms = 1
        else:
            send_sms = 0
        sql = "INSERT INTO appointment(aptDate, timeFrom, timeTo, userId, fullName, mobileNo, totalCost, " \
              "couponDiscount, finalCost, totalDuration, aptStatus, couponCode, barberId, barberName, " \
              "profilePic, userProfilePic, services, previousTimePhotos, sendSms) \
        VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', " \
              "'%s', '%s', '%s', '%s')" \
              % (apt_date, time_from, time_to, user_id, user["fullName"], user["mobileNo"], total_cost,
                 0, 0, total_duration, "Confirmed", coupon_code, barber_id,
                 barber["barberName"], barber["profilePic"], user["profilePic"],
                 services, "", send_sms)
        try:
            cursor.execute(sql)
            db.commit()
        except:
            db.rollback()
            db.close()
            return '{"status":1,"message":"Book appointment error."}'
        appointment = self.get_appointment(db.insert_id())
        if appointment is None:
            return '{"status":1,"message":"Failed to get appointment."}'
        result["appointment"]["aptNo"] = appointment["aptNo"]
        result["appointment"]["aptDate"] = appointment["aptDate"]
        result["appointment"]["timeFrom"] = appointment["timeFrom"]
        result["appointment"]["timeTo"] = appointment["timeTo"]
        result["appointment"]["userId"] = appointment["userId"]
        result["appointment"]["fullName"] = appointment["fullName"]
        result["appointment"]["mobileNo"] = appointment["mobileNo"]
        result["appointment"]["totalCost"] = appointment["totalCost"]
        result["appointment"]["couponDiscount"] = appointment["couponDiscount"]
        result["appointment"]["finalCost"] = appointment["finalCost"]
        result["appointment"]["totalDuration"] = appointment["totalDuration"]
        result["appointment"]["aptStatus"] = appointment["aptStatus"]
        result["appointment"]["couponCode"] = appointment["couponCode"]
        result["appointment"]["barberId"] = appointment["barberId"]
        result["appointment"]["barberName"] = appointment["barberName"]
        result["appointment"]["profilePic"] = appointment["profilePic"]
        result["appointment"]["userProfilePic"] = appointment["userProfilePic"]
        result["appointment"]["services"] = []
        services_str: str = appointment["services"]
        for i in services_str.split(","):
            service = self.get_service(i)
            if service is None:
                return '{"status":1,"message":"Failed to get service."}'
            result["appointment"]["services"].append(service)
        result["appointment"]["previousTimePhotos"] = appointment["previousTimePhotos"]
        db.close()
        return json.dumps(result)

    def get_user(self, user_id):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        user = {}
        sql = "SELECT * FROM user WHERE userId = %s;" % user_id
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            if len(results) == 0:
                db.close()
                return None
            for row in results:
                user["userId"] = row[0]
                user["fullName"] = row[1]
                user["emailId"] = row[2]
                user["gender"] = row[3]
                user["mobileNo"] = row[4]
                user["password"] = row[5]
                user["dateOfBirth"] = str(row[6])
                user["profilePic"] = row[7]
                user["isActive"] = row[8]
                user["isMobileVerified"] = row[9]
                user["isEmailVerified"] = row[10]
                user["fcmToken"] = row[11]
                user["ipAddress"] = row[12]
                user["emailVerificationCode"] = row[13]
                user["evcExpiresOn"] = row[14]
                user["apiToken"] = row[15]
                user["tokenValidUpTo"] = str(row[16])
                user["createdOn"] = str(row[17])
                user["updatedOn"] = str(row[18])
                user["deletedOn"] = str(row[19])
                user["isDeleted"] = row[20]
        except:
            db.close()
            return None
        db.close()
        return user

    def get_barber(self, barber_id):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        barber = {}
        sql = "SELECT * FROM barbers WHERE barberId = %s;" % barber_id
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            if len(results) == 0:
                db.close()
                return None
            for row in results:
                barber["barberId"] = row[0]
                barber["barberName"] = row[1]
                barber["isAdmin"] = row[2]
                barber["isBarber"] = row[3]
                barber["mobileNo"] = row[4]
                barber["profilePic"] = row[5]
                barber["gender"] = row[6]
                barber["breakTimeFrom"] = str(row[7])
                barber["breakTimeTo"] = str(row[8])
                barber["hasDefaultServices"] = row[9]
                barber["holiday"] = row[10]
                barber["userRating"] = row[11]
                barber["password"] = row[12]
                barber["type"] = row[13]
                barber["payment"] = row[14]
        except:
            db.close()
            return None
        db.close()
        return barber

    def get_appointment(self, apt_no):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        appointment = {}
        sql = "SELECT * FROM appointment WHERE aptNo = %s;" % apt_no
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            if len(results) == 0:
                db.close()
                return None
            for row in results:
                appointment["aptNo"] = row[0]
                appointment["aptDate"] = str(row[1])
                appointment["timeFrom"] = str(row[2])
                appointment["timeTo"] = str(row[3])
                appointment["userId"] = row[4]
                appointment["fullName"] = row[5]
                appointment["mobileNo"] = str(row[6])
                appointment["totalCost"] = row[7]
                appointment["totalCost"] = row[7]
                appointment["couponDiscount"] = row[8]
                appointment["finalCost"] = row[9]
                appointment["totalDuration"] = row[10]
                appointment["aptStatus"] = row[11]
                appointment["couponCode"] = row[12]
                appointment["barberId"] = row[13]
                appointment["barberName"] = row[14]
                appointment["profilePic"] = row[15]
                appointment["userProfilePic"] = row[16]
                appointment["services"] = row[17]
                appointment["previousTimePhotos"] = row[18]
                appointment["sendSms"] = row[19]
        except:
            db.close()
            return None
        db.close()
        return appointment

    def get_service(self, service_id):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        sql = "SELECT * FROM services WHERE serviceId = '%s';" % service_id
        service = {}

        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                service["serviceId"] = row[0]
                service["serviceName"] = row[1]
                service["serviceType"] = row[2]
                service["duration"] = row[3]
                service["cost"] = row[4]
                service["servicePic"] = row[5]

        except:
            db.close()
            return None
        db.close()
        return service

    def api_key_check(self, ps_auth_token, user_id):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        sql = "SELECT * FROM user WHERE userId = %s;" % user_id
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            if len(results) == 0:
                db.close()
                return False
            for row in results:
                if ps_auth_token != row[15]:
                    db.close()
                    return False
        except:
            db.close()
            return False
        db.close()
        return True
