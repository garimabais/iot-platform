# iot-platform

IoT Platform is a software to manage large scale deployment of IoT devices and to process big data generated by them. 
IoT platform is supported by E-Yantra (Government of India Program). This platform is being used across various 
engineering institutes including IIT Bombay.  

Architecture
=======
![60c1f12a3f754657ac33b126ef4d0bbb](https://user-images.githubusercontent.com/1313078/34651772-de7ddc08-f3fa-11e7-9aae-7e0ba8abb6fc.png)

Team
====
- Chief Architect : Akshar Prabhu Desai
- Developers 
    - Siddhesh Prabhu
    - Omkar Manjrekar 

Setup
======

1. Install mysql locally and set username password to root/root 
2. Create db named "iot"
3. In the table configset add three keys and their respective values
    - awsKey
    - awsSecret 
    - endpoint (iot endpoint unique to your AWS account)    
4. Change the CERT_ROOT under StringConstants class to point to the directory where you want to store thing certificates. 
