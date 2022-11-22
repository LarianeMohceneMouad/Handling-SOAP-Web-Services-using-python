from flask import Flask, render_template, request
from twilio.rest import Client as TwilioClient
from zeep import Client as WS_Client
import mysql.connector
from random import randrange
from datetime import timedelta
from datetime import datetime

import keys

pp_requests = mysql.connector.connect(
    host="localhost",
    username="root",
    password="",
    database="requests"
)
cursor = pp_requests.cursor()


def db_insert(nin_val, name_val, lastname_val, delivery_date_val, num_val):
    query = f"INSERT INTO `passport_resuests` (`NIN`, `Name`, `LastName`, `DeliveryDate`, `Number`) VALUES ('{nin_val}'," \
            f" '{name_val}', '{lastname_val}', '{delivery_date_val}', '{num_val}');"
    cursor.execute(query)
    pp_requests.commit()
    print("record inserted")


def delivery_date_gen():
    start = datetime.today()
    end = datetime.today() + timedelta(days=30)
    delta = end - start
    int_delta = (delta.days * 24 * 60 * 60)
    random_second = randrange(int_delta)
    return (start + timedelta(seconds=random_second)).date()


def get_marital_status(nin_val, name_val, lastname_val):
    marital_status = WS_Client(wsdl='http://localhost:8080/EtatCivil/civil_state?WSDL')
    return marital_status.service.checker(nin_val, name_val, lastname_val)


def get_criminal_record(nin_val):
    criminal_record = WS_Client(wsdl='http://localhost:8080/CriminalRecord/criminal_record?WSDL')
    return criminal_record.service.cr_checker(nin_val)


def get_safety_state(nin_val):
    safety_state = WS_Client(wsdl='http://localhost:8080/SafetyState/safety_state?WSDL')
    return safety_state.service.ss_checker(nin_val)


def sms_sender(result, num):
    client = TwilioClient(keys.account_sid, keys.auth_token)
    client.messages.create(body=f'{result}', from_=keys.twilio_number, to="+213"+num)
    print('SMS sent')


app = Flask(__name__)


@app.route("/", methods=["GET", "POST"])
def home():
    try:
        if request.form.get("NIN"):
            nin = int(request.form.get("NIN"))
            name = request.form.get("name")
            last_name = request.form.get("lastname")
            number = request.form.get("number")
            message = f'NIN: {nin} \nName: {name} \nLastname: {last_name}'
            if get_marital_status(nin, name, last_name) == "Correct":
                if get_safety_state(nin) == "Wanted" or get_criminal_record(nin) == "Bad":
                    message += "\nRequest rejected \nReasons :  "
                    if get_safety_state(nin) == "Wanted":
                        message += f"\n 1. Current Safety State Status : {get_safety_state(nin)}"
                    if get_criminal_record(nin) == "Bad":
                        message += f"\n 2. Criminal Record Decision result : {get_criminal_record(nin)}"
                    cursor.execute(f'select * from passport_resuests where nin = {nin}')
                    if bool(cursor.fetchall()):
                        return 'Request Already Submitted'
                    else:
                        db_insert(nin, name, last_name, 'None', number)
                        sms_sender(message, number)
                        return 'Request Submitted'
                else:
                    cursor.execute(f'select * from passport_resuests where nin = {nin}')
                    if bool(cursor.fetchall()):
                        return 'Request Already Submitted'
                    else:
                        delivery_date = delivery_date_gen()
                        message += f"\nPassport Request : Submitted \nDelivery Date {delivery_date}"
                        db_insert(nin, name, last_name, delivery_date, number)
                        sms_sender(message, number)
                        return 'Request Submitted'
        else:
            return render_template("index.html")

    except Exception:
        return render_template("index.html")


if __name__ == '__main__':
    app.run(port=5006, debug=True)
