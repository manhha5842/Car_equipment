# Car Equipment Management System

## Overview
The Car Equipment Management System is a web application developed using Java and Spring Boot. It provides functionalities to manage car equipment, including user management, product catalog, orders, and reviews.

## Features
- **User Authentication and Authorization**: JWT-based security to secure API endpoints.
- **RESTful APIs**: CRUD operations for users, products, orders, and reviews.
- **Data Management**: Integration with MySQL database for data persistence.
- **CSV Processing**: Import and export data using OpenCSV.
- **Test-Driven Development**: Unit tests with JUnit and Spring Boot Test to ensure code quality.

## Technologies Used
- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Security**
- **MySQL**
- **OpenCSV**
- **Lombok**
- **Maven**

## Installation
1. **Clone the repository:**
   ```sh
   git clone https://github.com/manhha5842/Car_equipment.git
   cd Car_equipment
2. **Setup the database:** 
   Create a MySQL database named car_equipment.
   Update the src/main/resources/application.properties file with your database credentials.
3. **Build and run the application:**
   ```sh 
   ./mvnw clean install
   ./mvnw spring-boot:run
4. **API Documentation**
   For detailed API documentation and usage guidelines, please refer to the [API Documentation](https://docs.google.com/spreadsheets/d/1gTSstHwIH0K2MQ9FwXErxlP6RXu6OHRxwyhRjp2DETA/edit?usp=sharing)..
