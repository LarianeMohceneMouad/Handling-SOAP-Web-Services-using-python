# Handling-SOAP-Web-Services-using-python
Developing SOAP Web Services and handling their WSDL using python ``zeep`` library for Passport Requests System

## Envirement settings:
With the following command line : ``pip install -r requirements.txt``

## Setting up MySQL DataBase:
### 1. Install [XAMPP](https://www.apachefriends.org/download.html)
### 2. Start Apache & MySQL Servers
### 3. Import ``DataBase.sql``

## Setting up SMS API using Twilio:
### 1. Create [Twilio Account](https://www.twilio.com)
### 2. Copy and past your **account sid**, **auth_token**, **twilio_number** into ``keys.py``

## Web Services SOAP:
### 1. Web Services development in [OpenESB](https://open-esb.net/) using java
### 2. Web Services deployment using ``Glassfish Server``

## System Design:
SOAP Web Services:
- ``EtatCivil``: Returns Whether the entered Informations are True or False
- ``CriminalRecord``: Returns the criminal record of the person (Good/Bad)
- ``SafetyState``: Returns the Safety state of the person (Wanted/Unwanted)

> ``main.py`` Flask application, GET the form information and calls the 3 previous APIs to handle the passport request, and send SMS to the person using Twilio API.

## How to run it:
### 1. Build & Deploy ``EtatCivil`` ``CriminalRecord`` ``SafetyState`` SOAP Web Services
> Using Glassfish, or any other deployment server
### 2. Get Their WSDL (if different) and replace it in the ``main.py`` to call and handle Web services using ``zeep`` library
### 3. Start ``main.py`` (Flask App)
