# **Assist Doc: Bridging the Gap in Digital Healthcare Communication and Management**

![WhatsApp Image 2024-12-24 at 12 08 33](https://github.com/user-attachments/assets/5fe41324-f6d0-4e2e-9801-c009b9079841)

Assist Doc is an advanced medical application developed to enhance communication between patients and
healthcare professionals, thereby improving health management and accessibility. The app provides patients
with features such as medication scheduling with reminders, symptom tracking for disease suggestions, browser
redirection for detailed health information, direct calls to healthcare providers, and locating nearby hospitals. For
healthcare professionals, the application offers tools to manage patient interactions through an integrated chat
system, organize and track appointments, and monitor patient medication adherence.

## 📚**Table of Contents**
- [🛠️ Software Architecture](#-software-architecture)
- [🐳 Docker Image](#-docker-image)
- [🎨 Frontend](#-frontend)
  - [📱 Mobile Features](#-mobile-features)
  - [💻 Web Technologies](#-web-technologies)
- [⚙️ Backend](#-backend)
  - [📱 Mobile](#-mobile)
  - [💻 Web](#-web)
- [🚀 Getting Started](#-getting-started)
  - [📱 Mobile Setup](#mobile-setup)
  - [💻 Web Setup](#web-setup)
- [🎥 Video Demonstration](#-video-demonstration)
- [🤝 Contributors](#-contributors)

---

## **🛠️ Software Architecture**
![diagram-export-25-12-2024-22_31_57](https://github.com/user-attachments/assets/9869cc95-157d-46df-a89c-b9499177dc4f)

---

## **🐳 Docker Image**

```yaml
services:
  backend:
    build:
      context: ./AssistDoc_AppointmentsWeb
    ports:
      - "8090:8090"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/doctor
      - SPRING_DATASOURCE_USERNAME=root
      - SPRING_DATASOURCE_PASSWORD=
    depends_on:
      - db
    networks:
      - app-network

  db:
    image: mysql:8
    environment:
      MYSQL_ROOT_PASSWORD:
      MYSQL_ALLOW_EMPTY_PASSWORD: "yes"
      MYSQL_DATABASE: doctor
    ports:
      - "3308:3306"
    networks:
      - app-network

networks:
  app-network:
    driver: bridge
```


## **🎨 Frontend**

## 📱 Mobile Features
- Built with Java.
- Includes features such as:
     - Appointment Making with patient.📅
     - Checking Disease according to Symptoms.💊
     - Assistance with a ChatBot.🌐
     - Locating nearby hospitals 🏨
     - Emergy call 📞
     - Integrated chat system for patient interactions 💬

## 💻 Web Technologies
- HTML
- CSS
- JavaScript
- Bootstrap

## **⚙️ Backend**

## 📱 Mobile
- Firebase

## 💻 Web
- Springboot
- MySQL

## **🚀 Getting Started**
**Prerequisites**

- Install Git

- XAMPP

Download and install XAMPP from apachefriends.org.

Start the Apache and MySQL servers.

Ensure MySQL is configured to use port 3306.

## 📱 Mobile Setup
- Clone the Project
  ```
  git clone <repository_url>
  cd <project_folder>
  ```
- Add your API Key for AI Service.
- You can login using the credentials:
   - docteur :  miskaraminaa@gmail.com
   - Patient :  marietekola@gmail.com


## 💻 Web Setup

- Clone the Project
  ```
  git clone <repository_url>
  cd <project_folder>
  ```

- Install Backend Dependencies
  ```
  mvn clean install
  ```
  
- Run Backend

Start Apache and MySQL servers in XAMPP.

Run the Spring Boot application. The database will be created automatically.

Verify the backend is running at http://localhost:8000.

## **🎥 Video Demonstration**
Videooo

## **🤝 Contributors**

MISKAR Amina  [github profile](https://github.com/miskaraminaa)

EL ABIDI Aya  [github profile](https://github.com/yaelaya)
