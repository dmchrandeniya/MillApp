# MillApp

MillApp is a Java Swing–based desktop application developed for a Sri Lankan sawmill.
It supports two user roles—**Machine Operator** and **Helper**—to streamline daily operations.
The system can calculate **cubic feet and total cost** in real time based on the woodload details entered by the user.
Additionally, it includes features for managing **customers, total costs, salaries, and inventory records**.


---

## 📌 Features

* Java Swing–based desktop interface
* Role-based access (Machine Operator and Helper)
* Real-time calculation of cubic feet and related costs
* MySQL-based data storage

---

## 🛠️ Setup Instructions

### **1. Database Setup**

1. Import the `mill_app.sql` file into your local MySQL server.
2. Ensure that the database name and credentials match those used in the project.

---

### **2. Project Setup in NetBeans**

1. Open the project in **NetBeans IDE**.

2. Download the required libraries:

   * `mysql-connector-j<latest_version>.jar`
   * `JXDatePicker.jar`

3. Add both files to your project under:
   **Right-click Project → Properties → Libraries → Add JAR/Folder**

---

## ▶️ Running the Application

Once the libraries and database are configured, you can run the project directly from NetBeans.

---

## 📄 Notes

* Make sure your MySQL service is running before launching the app.
* Update database connection settings in the source code if needed.
