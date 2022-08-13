import sys
import traceback

import firebase_admin
from firebase_admin import messaging
from firebase_admin import credentials


def send_notification(title, body, fcm_token, certificate_file, apt_no):
    try:
        cred = credentials.Certificate(certificate_file + ".json")
        app = firebase_admin.initialize_app(cred)

        # See documentation on defining a message payload.
        message = messaging.Message(
            notification=messaging.Notification(
                title=title,
                body=body,
            ),
            data={
                'aptNo': str(apt_no),
            },
            # This registration token comes from the client FCM SDKs.
            token=fcm_token,
        )

        # Send a message to the device corresponding to the provided
        # registration token.
        response = messaging.send(message)
        firebase_admin.delete_app(app)
        # Response is a message ID string.
        print('Successfully sent message:', response)
    except:
        traceback.print_exc()


arg_type = sys.argv[1]
arg_key = sys.argv[2]
arg_application = sys.argv[3]
arg_value = sys.argv[4]
if arg_type == "0":
    send_notification('Appointment confirmation', 'Your appointment %s is confirmed' % sys.argv[4],
                      sys.argv[2], sys.argv[3], sys.argv[4])
if arg_type == "1":
    send_notification('Verification Code', 'Your verification code is %s' % sys.argv[4],
                      sys.argv[2], sys.argv[3], sys.argv[4])