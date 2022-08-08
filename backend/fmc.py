import firebase_admin
from firebase_admin import messaging
from firebase_admin import credentials


def send_notification(title, body, fcm_token, certificate_file):
    cred = credentials.Certificate(certificate_file + ".json")
    app = firebase_admin.initialize_app(cred)

    # See documentation on defining a message payload.
    message = messaging.Message(
        notification=messaging.Notification(
            title=title,
            body=body,
        ),
        # This registration token comes from the client FCM SDKs.
        token=fcm_token,
    )

    # Send a message to the device corresponding to the provided
    # registration token.
    response = messaging.send(message)
    firebase_admin.delete_app(app)
    # Response is a message ID string.
    print('Successfully sent message:', response)
