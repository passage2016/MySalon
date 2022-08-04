import math
import random

import pymysql
import json
import hashlib
import time
import uuid
import datetime

import utils


class Mysqldb:
    with open("./MysqlInfo.json", 'r') as load_f:
        load_dict = json.load(load_f)
        sql_host = load_dict["host"]
        sql_password = load_dict["password"]

    def signup(self, fcm_token, mobile_no, password):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')

        phone_verification_code = str(random.randint(0, 10000))
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
            dt_7 = (datetime.datetime.now() + datetime.timedelta(days=7)).strftime("%Y-%m-%d")
            sql = "INSERT INTO user(mobileNo, password, createdOn, updatedOn, fcmToken, mobileVerificationCode, " \
                  "dateOfBirth, evcExpiresOn, tokenValidUpTo, deletedOn) \
                           VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '1001-01-01', '%s', '%s', '1001-01-01 00:00:00')" \
                  % (mobile_no, md5.hexdigest(), dt, dt, fcm_token, phone_verification_code, dt_7, dt_7)
            cursor.execute(sql)
            user_id = str(db.insert_id())
            db.commit()
        except RuntimeError:
            db.rollback()
            db.close()
            return '{"status":1,"message":"Signup error."}'
        db.close()
        result = {"status": 0, "message": "Success", "otp": phone_verification_code, "userId": user_id}

        return json.dumps(result)

    def login(self, mobile_no: str, password: str, ip: str):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        sql = "SELECT * FROM user WHERE mobileNo = %s;" % mobile_no
        result = {"status": 0, "message": "Authenticated successfully"}
        user_id = ""
        token = ""
        email_verification_code = str(random.randint(0, 10000))
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
                user_id = row[0]
                token = uuid.uuid4().hex
                result["userId"] = user_id
                result["fullName"] = row[1]
                result["emailId"] = row[2]
                result["gender"] = row[3]
                result["mobileNo"] = row[4]
                result["password"] = row[5]
                date_of_birth = row[6]
                if date_of_birth is None:
                    result["dateOfBirth"] = ""
                else:
                    result["dateOfBirth"] = str(date_of_birth)
                result["profilePic"] = row[7]
                result["isActive"] = 1
                result["isMobileVerified"] = row[9]
                result["isEmailVerified"] = row[10]
                result["fcmToken"] = row[11]
                result["ipAddress"] = ip
                result["emailVerificationCode"] = email_verification_code
                evc_expires_on = row[14]
                if evc_expires_on is None:
                    result["evcExpiresOn"] = ""
                else:
                    result["evcExpiresOn"] = str(evc_expires_on)
                result["apiToken"] = token
                token_valid_up_to = row[16]
                if token_valid_up_to is None:
                    result["tokenValidUpTo"] = ""
                else:
                    result["tokenValidUpTo"] = str(token_valid_up_to)
                result["createdOn"] = str(row[17])
                result["updatedOn"] = str(row[18])
                deleted_on = row[19]
                if evc_expires_on is None:
                    result["deletedOn"] = ""
                else:
                    result["deletedOn"] = str(deleted_on)
                result["isDeleted"] = row[20]
        except RuntimeError:
            db.close()
            return '{"status":1,"message":"Login error."}'
        cursor = db.cursor()

        sql = "UPDATE user SET isActive = 1, apiToken = '%s', ipAddress = '%s', emailVerificationCode = '%s' " \
              "WHERE userId = '%s';" % (token, ip, email_verification_code, user_id)
        try:
            cursor.execute(sql)
            db.commit()
        except RuntimeError:
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
        new_password = ""
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
        except RuntimeError:
            db.close()
            return '{"status":1,"message":"Update user error."}'
        cursor = db.cursor()

        sql = "UPDATE user SET fullName = '%s', emailId = '%s', password = '%s', dateOfBirth = '%s', profilePic = '%s'\
         WHERE userId = '%s'" % (full_name, email_id, new_password, date_of_birth, profile_pic, user_id)
        try:
            cursor.execute(sql)
            db.commit()
        except RuntimeError:
            db.rollback()
            db.close()
            return '{"status":1,"message":"Update user error."}'
        db.close()
        return json.dumps(result)

    def update_fcm_token(self, ps_auth_token, user_id, fcm_token):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        sql = "SELECT * FROM user WHERE userId = %s;" % user_id
        result = {"status": 0, "message": "Success"}
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            if len(results) == 0:
                db.close()
                return '{"status":1,"message":"Account not exit."}'
            for row in results:
                if ps_auth_token != row[15]:
                    db.close()
                    return '{"status":1,"message":"Failed to authenticate."}'
        except RuntimeError:
            db.close()
            return '{"status":1,"message":"Update user error."}'
        cursor = db.cursor()
        sql = "UPDATE user SET fcmToken = '%s' WHERE userId = '%s'" % (fcm_token, user_id)
        try:
            cursor.execute(sql)
            db.commit()
        except RuntimeError:
            db.rollback()
            db.close()
            return '{"status":1,"message":"Update user error."}'
        db.close()
        return json.dumps(result)

    def get_phone_verification_code(self, mobile_no):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        phone_verification_code = str(random.randint(0, 10000))
        cursor = db.cursor()
        sql = "SELECT * FROM user WHERE mobileNo = %s;" % mobile_no
        result = {"status": 0, "message": "Success", "opt": phone_verification_code}
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            if len(results) == 0:
                db.close()
                return '{"status":1,"message":"Account not exit."}'
        except RuntimeError:
            db.close()
            return '{"status":1,"message":"Get phone verification code error."}'
        cursor = db.cursor()
        sql = "UPDATE user SET mobileVerificationCode = '%s' WHERE mobileNo = '%s'" \
              % (phone_verification_code, mobile_no)
        try:
            cursor.execute(sql)
            db.commit()
        except RuntimeError:
            db.rollback()
            db.close()
            return '{"status":1,"message":"Get phone verification code error."}'
        db.close()
        return json.dumps(result)

    def reset_password(self, mobile_no, phone_verification_code, password):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        sql = "SELECT * FROM user WHERE mobileNo = %s;" % mobile_no
        result = {"status": 0, "message": "Success"}
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            if len(results) == 0:
                db.close()
                return '{"status":1,"message":"Account not exit."}'
            for row in results:
                if phone_verification_code != row[21]:
                    db.close()
                    return '{"status":1,"message":"Phone verification code not correct."}'
        except RuntimeError:
            db.close()
            return '{"status":1,"message":"Get phone verification code error."}'
        cursor = db.cursor()
        md5 = hashlib.md5()
        md5.update(password.encode('utf-8'))
        new_password = md5.hexdigest()
        sql = "UPDATE user SET password = '%s' WHERE mobileNo = '%s'" \
              % (new_password, mobile_no)
        try:
            cursor.execute(sql)
            db.commit()
        except RuntimeError:
            db.rollback()
            db.close()
            return '{"status":1,"message":"Get phone verification code error."}'
        db.close()
        return json.dumps(result)

    # No API
    def verify_mobile(self, user_id, phone_verification_code):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        sql = "SELECT * FROM user WHERE userId = %s;" % user_id
        result = {"status": 0, "message": "Success"}
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            if len(results) == 0:
                db.close()
                return '{"status":1,"message":"Account not exit."}'
            for row in results:
                if phone_verification_code != row[21]:
                    db.close()
                    return '{"status":1,"message":"Phone verification code not correct."}'
        except RuntimeError:
            db.close()
            return '{"status":1,"message":"Verify mobile error."}'
        cursor = db.cursor()
        sql = "UPDATE user SET isMobileVerified = 1 WHERE userId = '%s'" % user_id
        try:
            cursor.execute(sql)
            db.commit()
        except RuntimeError:
            db.rollback()
            db.close()
            return '{"status":1,"message":"Verify mobile error."}'
        db.close()
        return json.dumps(result)

    # No API
    def verify_email(self, user_id, email_verification_code):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        sql = "SELECT * FROM user WHERE userId = %s;" % user_id
        result = {"status": 0, "message": "Success"}
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            if len(results) == 0:
                db.close()
                return '{"status":1,"message":"Account not exit."}'
            for row in results:
                if email_verification_code != row[13]:
                    db.close()
                    return '{"status":1,"message":"Email verification code not correct."}'
        except RuntimeError:
            db.close()
            return '{"status":1,"message":"Verify email error."}'
        cursor = db.cursor()
        sql = "UPDATE user SET isEmailVerified = 1 WHERE userId = '%s'" % user_id
        try:
            cursor.execute(sql)
            db.commit()
        except RuntimeError:
            db.rollback()
            db.close()
            return '{"status":1,"message":"Verify email error."}'
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
        except RuntimeError:
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
        result = {"status": 0, "message": "Successfully", "services": {}}
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
        except RuntimeError:
            db.close()
            return '{"status":1,"message":"Database error."}'
        db.close()
        return json.dumps(result)

    def new_get_services(self):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        sql = "SELECT * FROM services;"
        result = {"status": 0, "message": "Successfully", "services": []}
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                service = {"serviceId": row[0], "serviceName": row[1], "serviceType": row[2],
                           "duration": row[3], "cost": row[4],
                           "servicePic": row[5]}
                result["services"].append(service)
        except RuntimeError:
            db.close()
            return '{"status":1,"message":"Database error."}'
        db.close()
        return json.dumps(result)

    def get_services_category(self):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        sql = "SELECT * FROM serviceCategory;"
        result = {"status": 0, "message": "Successfully", "serviceCategories": []}
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                service = {"categoryId": row[0], "category": row[1], "categoryImage": row[2]}
                result["serviceCategories"].append(service)
        except RuntimeError:
            db.close()
            return '{"status":1,"message":"Database error."}'
        db.close()
        return json.dumps(result)

    def get_services_by_category(self, category_id):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        sql = "SELECT * FROM services WHERE categoryId = '%s';" % category_id
        result = {"status": 0, "message": "Successfully", "services": []}
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                service = {"serviceId": row[0], "serviceName": row[1], "serviceType": row[2],
                           "duration": row[3], "cost": row[4],
                           "servicePic": row[5], "description": row[7]}
                result["services"].append(service)
        except RuntimeError:
            db.close()
            return '{"status":1,"message":"Database error."}'
        db.close()
        return json.dumps(result)

    def add_barber(self, barber_name, is_admin, is_barber, mobile_no, profile_pic, gender, break_time_from,
                   break_time_to, has_default_services, holiday, user_rating, password, barber_type, payment):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')

        cursor = db.cursor()
        sql = "SELECT * FROM barbers WHERE mobileNo = %s;" % mobile_no
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            if len(results) > 0:
                return '{"status":1,"message":"Mobile No already register."}'
            md5 = hashlib.md5()
            md5.update(password.encode('utf-8'))
            sql = "INSERT INTO barbers(barberName, isAdmin, isBarber, mobileNo, profilePic, gender, breakTimeFrom, " \
                  "breakTimeTo, hasDefaultServices, holiday, userRating, password, type, payment) \
                           VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')" \
                  % (barber_name, is_admin, is_barber, mobile_no, profile_pic, gender, break_time_from,
                     break_time_to, has_default_services, holiday, user_rating, md5.hexdigest(), barber_type, payment)
            cursor.execute(sql)
            barber_id = str(db.insert_id())
            db.commit()
        except RuntimeError:
            db.rollback()
            db.close()
            return '{"status":1,"message":"Signup error."}'
        db.close()
        result = {"status": 0, "message": "Success", "barberId": barber_id}

        return json.dumps(result)

    def get_barbers(self):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        result = {"status": 0, "message": "Successfully"}
        barbers = []
        sql = "SELECT * FROM barbers;"
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                barber = {"barberId": row[0], "barberName": row[1], "isAdmin": row[2], "isBarber": row[3],
                          "mobileNo": row[4], "profilePic": row[5], "gender": row[6], "breakTimeFrom": str(row[7])[:-3],
                          "breakTimeTo": str(row[8])[:-3], "hasDefaultServices": row[9], "holiday": row[10],
                          "userRating": row[11], "password": row[12], "type": row[13], "payment": row[14]}
                barbers.append(barber)
        except RuntimeError:
            db.close()
            return '{"status":1,"message":"Database error."}'
        db.close()
        result["barbers"] = barbers
        return json.dumps(result)

    def get_coupons(self):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        sql = "SELECT * FROM coupons;"
        result = {"status": 0, "message": "Success", "coupons": []}

        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                coupon = {"couponId": row[0], "couponCode": row[1], "couponType": row[2], "fromDate": str(row[3]),
                          "toDate": str(row[4]), "terms": row[5], "minimumRqrdCost": row[6], "couponText": row[7],
                          "couponValue": row[8]}
                result["coupons"].append(coupon)
        except RuntimeError:
            db.close()
            return '{"status":1,"message":"Failed to authenticate."}'
        db.close()
        return json.dumps(result)

    def get_products(self, page_size, page_no):
        page_no = page_no - 1
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        result = {"status": 0, "message": "Success", "products": []}
        try:
            sql = "SELECT * FROM products LIMIT %d,%d;" % (page_no * page_size, page_size)
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                product = {"productId": row[0], "productName": row[1], "price": row[2], "offer": row[3],
                           "offerType": row[4], "offerValidUpTo": str(row[5]), "description": row[6],
                           "productPic": row[7]}
                result["products"].append(product)
            sql = "SELECT * FROM reviews;"
            rows = cursor.execute(sql)
            result["isFirstPage"] = page_no == 0
            result["isLastPage"] = rows <= (page_no + 1) * page_size
            result["totalPages"] = math.ceil(rows / page_size)
            # result["totalProducts"] = rows
            result["pageNo"] = page_no
            result["pageSize"] = page_size
        except RuntimeError:
            db.close()
            return '{"status":1,"message":"Database error."}'
        db.close()
        return json.dumps(result)

    def get_offers(self):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        sql = "SELECT * FROM coupons;"
        result = {"status": 0, "message": "Success", "coupons": [], "products": []}

        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                coupon = {"couponId": row[0], "couponCode": row[1], "couponType": row[2], "fromDate": str(row[3]),
                          "toDate": str(row[4]), "terms": row[5], "minimumRqrdCost": row[6], "couponText": row[7],
                          "couponValue": row[8]}
                result["coupons"].append(coupon)
            sql = "SELECT * FROM products;"
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                product = {"productId": row[0], "productName": row[1], "price": row[2], "offer": row[3],
                           "offerType": row[4], "offerValidUpTo": str(row[5]), "description": row[6],
                           "productPic": row[7]}
                result["products"].append(product)
        except RuntimeError:
            db.close()
            return '{"status":1,"message":"Database error."}'
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
        except RuntimeError:
            db.close()
            return '{"status":1,"message":"Failed to authenticate."}'
        db.close()
        return json.dumps(result)

    def get_albums(self):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        sql = "SELECT * FROM albums;"
        result = {"status": 0, "message": "Success", "albums": []}
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                album = {"albumId": row[0], "albumName": row[1], "coverPhotoUrl": row[2]}
                result["albums"].append(album)
        except RuntimeError:
            db.close()
            return '{"status":1,"message":"Database error."}'
        db.close()
        return json.dumps(result)

    def get_album_photos(self, album_id):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        sql = "SELECT * FROM albumPhotos WHERE albumId = '%s';" % album_id
        result = {"status": 0, "message": "Success", "photos": []}
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                photo = {"photoId": row[0], "photoName": row[1], "photoUrl": row[2], "albumId": row[3],
                         "albumName": row[4]}
                result["photos"].append(photo)
        except RuntimeError:
            db.close()
            return '{"status":1,"message":"Database error."}'
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
                working_hours = {"fromTime": str(row[1])[:-3], "toTime": str(row[2])[:-3]}
                result["timings"][row[0]] = working_hours
        except RuntimeError:
            db.close()
            return '{"status":1,"message":"Database error."}'
        db.close()
        return json.dumps(result)

    def get_current_appointments(self, barber_id):
        slots = {}
        working_hours = json.loads(self.get_working_hours())
        if working_hours["status"] == 1:
            return '{"status":1,"message":"Failed to get working hours."}'
        barber = self.get_barber(barber_id)
        if barber is None:
            return '{"status":1,"message":"Failed to get barber."}'
        for i in range(7):
            dt = (datetime.datetime.now() + datetime.timedelta(days=i)).strftime("%Y-%m-%d")
            int_weekday = (datetime.datetime.now() + datetime.timedelta(days=i)).weekday()
            string_weekday = utils.get_string_weekday(int_weekday)
            slots[dt] = {"day": string_weekday, "date": dt, "slots": {}}
            day_time = working_hours["timings"][string_weekday]["fromTime"]
            while utils.time_before(day_time, working_hours["timings"][string_weekday]["toTime"]):
                if i == 0 and utils.time_before(day_time, datetime.datetime.now().strftime("%H:%M:%S")):
                    slots[dt]["slots"]["%s-%s" % (day_time, utils.get_next_15min(day_time))] = True
                elif utils.time_before(day_time, barber["breakTimeTo"]) and not \
                        utils.time_before(day_time, barber["breakTimeFrom"]):
                    slots[dt]["slots"]["%s-%s" % (day_time, utils.get_next_15min(day_time))] = True
                else:
                    slots[dt]["slots"]["%s-%s" % (day_time, utils.get_next_15min(day_time))] = False
                day_time = utils.get_next_15min(day_time)
        appointments = self.get_appointments_by_time(datetime.datetime.now().strftime("%Y-%m-%d"),
                                                     (datetime.datetime.now() + datetime.timedelta(days=6))
                                                     .strftime("%Y-%m-%d"), barber_id)
        for appointment in appointments:
            if appointment["aptStatus"] == "Confirmed":
                day_time = appointment["timeFrom"]
                while utils.time_before(day_time, appointment["timeTo"]):
                    slots[appointment["aptDate"]]["slots"]["%s-%s" % (day_time, utils.get_next_15min(day_time))] = True
                    day_time = utils.get_next_15min(day_time)

        result = {"status": 0, "message": "Success", "slots": []}
        for key in slots:
            result["slots"].append(slots[key])
        return json.dumps(result)

    def book(self, ps_auth_token, user_id, barber_id, services, apt_date, time_from,
             time_to, total_duration, total_cost, coupon_code, send_sms):
        if not self.api_key_check(ps_auth_token, user_id):
            return '{"status":1,"message":"Failed to authenticate."}'
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
        if isinstance(services, list):
            services = str(services)
        services = services.replace(" ", "")[1:-1]
        if isinstance(send_sms, str):
            send_sms = send_sms.lower() == "true"
        if send_sms is True:
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
            appointment_id = db.insert_id()
            db.commit()
        except RuntimeError:
            db.rollback()
            db.close()
            return '{"status":1,"message":"Book appointment error."}'
        db.close()
        result = self.get_appointment_result(appointment_id)
        return json.dumps(result)

    def get_appointments(self, ps_auth_token, user_id):
        if not self.api_key_check(ps_auth_token, user_id):
            return '{"status":1,"message":"Failed to authenticate."}'
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        appointments = []
        sql = "SELECT * FROM appointment WHERE userId = %s AND isDeleted = 0;" % user_id
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                appointment = {"aptNo": row[0], "aptDate": str(row[1]), "timeFrom": str(row[2])[:-3],
                               "timeTo": str(row[3])[:-3], "totalDuration": row[10], "aptStatus": row[11]}
                appointments.append(appointment)
        except RuntimeError:
            db.close()
            return '{"status":1,"message":"Database error."}'
        db.close()
        result = {"status": 0, "message": "Success", "appointments": appointments}
        return json.dumps(result)

    def get_appointment_detail(self, ps_auth_token, appointment_id):
        if not self.api_key_check_without_user_id(ps_auth_token):
            return '{"status":1,"message":"Failed to authenticate."}'
        return self.get_appointment_result(appointment_id)

    def cancel_appointment(self, ps_auth_token, appointment_id):
        if not self.api_key_check_without_user_id(ps_auth_token):
            return '{"status":1,"message":"Failed to authenticate."}'
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        try:
            sql = "UPDATE appointment SET aptStatus = 'Canceled' WHERE aptNo = '%s';" % appointment_id
            cursor.execute(sql)
            db.commit()
        except RuntimeError:
            db.rollback()
            db.close()
            return '{"status":1,"message":"Database error."}'
        db.close()
        result = self.get_appointment_result(appointment_id)
        return json.dumps(result)

    def reschedule_appointment(self, ps_auth_token, appointment_id, time_from, time_to, apt_date):
        if not self.api_key_check_without_user_id(ps_auth_token):
            return '{"status":1,"message":"Failed to authenticate."}'
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        try:
            sql = "UPDATE appointment SET timeFrom = '%s', timeTo = '%s' , aptDate = '%s' WHERE aptNo = '%s';" \
                  % (time_from + ":00", time_to + ":00", apt_date, appointment_id)
            cursor.execute(sql)
            db.commit()
        except RuntimeError:
            db.rollback()
            db.close()
            return '{"status":1,"message":"Database error."}'
        db.close()
        result = self.get_appointment_result(appointment_id)
        return json.dumps(result)

    def add_reviews(self, ps_auth_token, user_id, rating, comment):
        if not self.api_key_check(ps_auth_token, user_id):
            return '{"status":1,"message":"Failed to authenticate."}'
        dt = time.strftime('%Y-%m-%d')
        user = self.get_user(user_id)
        if user is None:
            return '{"status":1,"message":"Failed to get user."}'
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        try:
            sql = "INSERT INTO reviews(comment, rating, creationDate, fullName, profilePic, userId) \
                           VALUES ('%s', '%s', '%s', '%s', '%s', '%s')" % (comment, rating, dt, user["fullName"],
                                                                           user["profilePic"], user_id)
            cursor.execute(sql)
            db.commit()
        except RuntimeError:
            db.rollback()
            db.close()
            return '{"status":1,"message":"Database error."}'
        db.close()
        return '{"status":0,"message":"Success"}'

    def get_reviews(self, page_size, page_no):
        page_no = page_no - 1
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        result = {"status": 0, "message": "Success", "reviews": []}
        try:
            sql = "SELECT * FROM reviews LIMIT %d,%d;" % (page_no * page_size, page_size)
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                review = {"reviewId": row[0], "comment": row[1], "rating": row[2], "creationDate": str(row[3]),
                          "fullName": row[4], "profilePic": row[5]}
                result["reviews"].append(review)
            sql = "SELECT * FROM reviews;"
            rows = cursor.execute(sql)
            result["isFirstPage"] = page_no == 0
            result["isLastPage"] = rows <= (page_no + 1) * page_size
            result["totalPages"] = math.ceil(rows / page_size)
            result["totalRecords"] = rows
            result["pageNo"] = page_no
            result["pageSize"] = page_size
        except RuntimeError:
            db.close()
            return '{"status":1,"message":"Database error."}'
        db.close()
        return json.dumps(result)

    def get_alert(self):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        sql = "SELECT * FROM alert;"
        result = {"status": 0, "message": "Success", "alert": []}
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                alert = {"id": row[0], "message": row[1], "type": row[2], "createdOn": str(row[3])}
                result["alert"].append(alert)
        except RuntimeError:
            db.close()
            return '{"status":1,"message":"Database error."}'
        db.close()
        return json.dumps(result)

    # Not API
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
                user["evcExpiresOn"] = str(row[14])
                user["apiToken"] = row[15]
                user["tokenValidUpTo"] = str(row[16])
                user["createdOn"] = str(row[17])
                user["updatedOn"] = str(row[18])
                user["deletedOn"] = str(row[19])
                user["isDeleted"] = row[20]
        except RuntimeError:
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
                barber["breakTimeFrom"] = str(row[7])[:-3]
                barber["breakTimeTo"] = str(row[8])[:-3]
                barber["hasDefaultServices"] = row[9]
                barber["holiday"] = row[10]
                barber["userRating"] = row[11]
                barber["password"] = row[12]
                barber["type"] = row[13]
                barber["payment"] = row[14]
        except RuntimeError:
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
        sql = "SELECT * FROM appointment WHERE aptNo = %s AND isDeleted = 0;" % apt_no
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            if len(results) == 0:
                db.close()
                return None
            for row in results:
                appointment["aptNo"] = row[0]
                appointment["aptDate"] = str(row[1])
                appointment["timeFrom"] = str(row[2])[:-3]
                appointment["timeTo"] = str(row[3])[:-3]
                appointment["userId"] = row[4]
                appointment["fullName"] = row[5]
                appointment["mobileNo"] = row[6]
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
        except RuntimeError:
            db.close()
            return None
        db.close()
        return appointment

    def get_appointments_by_time(self, start_date, end_date, barber_id):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        appointments = []

        sql = "SELECT * FROM appointment WHERE barberId = %s " \
              "AND aptDate >= '%s' AND aptDate <= '%s' AND isDeleted = 0;" % \
              (barber_id, start_date, end_date)
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                appointment = {"aptNo": row[0], "aptDate": str(row[1]), "timeFrom": str(row[2])[:-3],
                               "timeTo": str(row[3])[:-3], "userId": row[4], "fullName": row[5], "mobileNo": row[6],
                               "totalCost": row[7], "couponDiscount": row[8], "finalCost": row[9],
                               "totalDuration": row[10], "aptStatus": row[11], "couponCode": row[12],
                               "barberId": row[13], "barberName": row[14], "profilePic": row[15],
                               "userProfilePic": row[16], "services": row[17], "previousTimePhotos": row[18],
                               "sendSms": row[19]}
                appointments.append(appointment)
        except RuntimeError:
            db.close()
            return None
        db.close()
        return appointments

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

        except RuntimeError:
            db.close()
            return None
        db.close()
        return service

    def get_appointment_result(self, appointment_id):
        appointment = self.get_appointment(appointment_id)
        if appointment is None:
            return '{"status":1,"message":"Failed to get appointment."}'
        result = {"status": 0, "message": "Book appointment successfully", "appointment": {}}
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
        return result

    def get_shop_status(self):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()

        int_weekday = (datetime.datetime.now()).weekday()
        string_weekday = utils.get_string_weekday(int_weekday)
        sql = "SELECT * FROM workingHours WHERE day = '%s';" % string_weekday
        status = False
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                from_time = str(row[1])
                to_time = str(row[2])
                now = datetime.datetime.now().strftime("%H:%M:%S")
                if utils.time_before(from_time, now) and utils.time_before(now, to_time):
                    status = True

        except RuntimeError:
            db.close()
            return False
        db.close()
        return status

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
        except RuntimeError:
            db.close()
            return False
        db.close()
        return True

    def api_key_check_without_user_id(self, ps_auth_token):
        db = pymysql.connect(host=self.sql_host,
                             user='root',
                             password=self.sql_password,
                             database='barber')
        cursor = db.cursor()
        sql = "SELECT * FROM user WHERE apiToken = '%s';" % ps_auth_token
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            if len(results) == 0:
                db.close()
                return False
        except RuntimeError:
            db.close()
            return False
        db.close()
        return True
