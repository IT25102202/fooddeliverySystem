# 🍔 FoodHub - Online Food Delivery Management System
## SE1020 – Object Oriented Programming Project

---

## 👥 Team Members & Components

| Student ID    | Component                  | OOP Concepts Used                          |
|---------------|----------------------------|--------------------------------------------|
| IT251022022   | User Management            | Encapsulation, Inheritance (Customer→User), Polymorphism |
| IT25102940    | Restaurant Management      | Encapsulation, Inheritance (Veg/NonVeg→Restaurant), Polymorphism |
| IT25100897    | Food Menu Management       | Encapsulation, Inheritance (MainCourse/Dessert/Beverage→FoodItem), Polymorphism |
| IT25101831    | Order Management           | Encapsulation, Abstraction, Polymorphism   |
| IT25101967    | Payment Management         | Encapsulation, Inheritance (CardPayment/CashPayment→Payment), Polymorphism |
| IT25100159    | Feedback & Review Management | Encapsulation, Inheritance, Polymorphism  |

---

## 🛠️ Tech Stack

- **Backend:** Java 17 + Spring Boot 3.2
- **Frontend:** Thymeleaf + HTML5 + CSS3 + JavaScript
- **Data Storage:** File Read/Write (`.txt` files in `/data/` folder)
- **IDE:** IntelliJ IDEA
- **Build Tool:** Maven
- **Version Control:** GitHub

---

## 🚀 How to Run

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- IntelliJ IDEA (recommended)

### Steps

1. **Clone / Extract** the project folder `online-food-delivery/`

2. **Open in IntelliJ IDEA:**
   - File → Open → Select the `online-food-delivery` folder
   - Wait for Maven to download dependencies

3. **Run the Application:**
   - Open `src/main/java/com/fooddelivery/FoodDeliveryApplication.java`
   - Click the green ▶ Run button
   - Or run: `mvn spring-boot:run` in the terminal

4. **Open in Browser:**
   ```
   http://localhost:8080
   ```

5. **Login Credentials:**
   | Role     | Email                  | Password    |
   |----------|------------------------|-------------|
   | Admin    | admin@foodhub.lk       | admin123    |
   | Customer | kasun@gmail.com        | password123 |
   | Customer | nimasha@gmail.com      | password123 |
   | Customer | amal@gmail.com         | password123 |

---

## 📁 Project Structure

```
online-food-delivery/
├── src/
│   ├── main/
│   │   ├── java/com/fooddelivery/
│   │   │   ├── FoodDeliveryApplication.java    ← Main entry point
│   │   │   ├── model/                          ← All model classes
│   │   │   │   ├── User.java                   ← Base user class
│   │   │   │   ├── Customer.java               ← extends User
│   │   │   │   ├── Admin.java                  ← extends User
│   │   │   │   ├── Restaurant.java             ← Base restaurant class
│   │   │   │   ├── VegRestaurant.java          ← extends Restaurant
│   │   │   │   ├── NonVegRestaurant.java       ← extends Restaurant
│   │   │   │   ├── FoodItem.java               ← Base food item class
│   │   │   │   ├── MainCourse.java             ← extends FoodItem
│   │   │   │   ├── Dessert.java                ← extends FoodItem
│   │   │   │   ├── Beverage.java               ← extends FoodItem
│   │   │   │   ├── Order.java                  ← Order management
│   │   │   │   ├── Payment.java                ← Base payment class
│   │   │   │   ├── CardPayment.java            ← extends Payment
│   │   │   │   ├── CashPayment.java            ← extends Payment
│   │   │   │   └── Review.java                 ← Review management
│   │   │   ├── service/                        ← Business logic
│   │   │   │   ├── UserService.java
│   │   │   │   ├── RestaurantService.java
│   │   │   │   ├── FoodItemService.java
│   │   │   │   ├── OrderService.java
│   │   │   │   ├── PaymentService.java
│   │   │   │   └── ReviewService.java
│   │   │   ├── controller/                     ← HTTP controllers
│   │   │   │   ├── HomeController.java
│   │   │   │   ├── UserController.java
│   │   │   │   ├── RestaurantController.java
│   │   │   │   ├── FoodItemController.java
│   │   │   │   ├── OrderController.java
│   │   │   │   ├── PaymentController.java
│   │   │   │   └── ReviewController.java
│   │   │   ├── util/
│   │   │   │   └── FileUtil.java               ← File I/O utility
│   │   │   └── config/
│   │   │       └── DataInitializer.java        ← Seeds sample data
│   │   └── resources/
│   │       ├── templates/                      ← Thymeleaf HTML pages
│   │       │   ├── index.html                  ← Home page
│   │       │   ├── user/                       ← Login, Register, Profile
│   │       │   ├── restaurant/                 ← List, Detail pages
│   │       │   ├── order/                      ← Place, History, Status, Payment
│   │       │   ├── payment/                    ← Confirmation, History
│   │       │   ├── review/                     ← Submit, Edit, My Reviews
│   │       │   └── admin/                      ← Dashboard, all CRUD pages
│   │       ├── static/css/style.css            ← Global stylesheet
│   │       └── application.properties
├── data/                                       ← Data files (auto-created)
│   ├── users.txt
│   ├── restaurants.txt
│   ├── fooditems.txt
│   ├── orders.txt
│   ├── payments.txt
│   └── reviews.txt
└── pom.xml
```

---

## 🎯 OOP Concepts Demonstrated

### Encapsulation
- All model classes use `private` fields with public getters/setters
- Business logic is encapsulated in Service classes
- File I/O is encapsulated in `FileUtil`

### Inheritance
- `Customer` extends `User`
- `Admin` extends `User`
- `VegRestaurant` extends `Restaurant`
- `NonVegRestaurant` extends `Restaurant`
- `MainCourse`, `Dessert`, `Beverage` extend `FoodItem`
- `CardPayment`, `CashPayment` extend `Payment`

### Polymorphism
- `getDisplayInfo()` overridden in every subclass
- `validateLogin()` overridden in Customer and Admin
- `processPayment()` overridden in CardPayment and CashPayment
- `getMenuLabel()` overridden in VegRestaurant and NonVegRestaurant

### Abstraction
- `Order.getStatusDisplay()` abstracts complex status logic
- `FileUtil` abstracts all file read/write operations
- Service layer abstracts business logic from controllers

---

## 🌟 Features

### Customer Features
- ✅ Register & Login
- ✅ Browse restaurants by cuisine, type, city
- ✅ View full restaurant menu with real photos
- ✅ Add items to cart with quantity controls
- ✅ Place order with delivery address
- ✅ Pay by Card or Cash on Delivery
- ✅ Track order status in real-time
- ✅ View order history
- ✅ View payment history
- ✅ Submit, edit, delete reviews
- ✅ Update/delete profile

### Admin Features
- ✅ Dashboard with key statistics
- ✅ User management (view, search, delete)
- ✅ Restaurant management (CRUD + search)
- ✅ Food item management (CRUD + filter)
- ✅ Order management (update status, filter by status)
- ✅ Payment management (update status, delete records)
- ✅ Review moderation (hide, restore, delete)

---

## 📊 Data Storage

All data is stored in plain text files in the `data/` folder using pipe (`|`) as delimiter:

```
data/users.txt       ← User accounts
data/restaurants.txt ← Restaurant info
data/fooditems.txt   ← Food menu items
data/orders.txt      ← Customer orders
data/payments.txt    ← Payment records
data/reviews.txt     ← Customer reviews
```

Sample format (users.txt):
```
ADM001|admin|admin@foodhub.lk|admin123|0112345678|ADMIN|Management|
USR001|Kasun Perera|kasun@gmail.com|password123|0771234567|CUSTOMER|45 Galle Road|Colombo
```

---

## 🔗 GitHub Commit Structure

Each team member should commit their component:
- **IT251022022** → `model/User.java`, `model/Customer.java`, `model/Admin.java`, `service/UserService.java`, `controller/UserController.java`, `templates/user/`
- **IT25102940** → `model/Restaurant.java`, `model/VegRestaurant.java`, `model/NonVegRestaurant.java`, `service/RestaurantService.java`, `controller/RestaurantController.java`, `templates/restaurant/`
- **IT25100897** → `model/FoodItem.java`, `model/MainCourse.java`, `model/Dessert.java`, `model/Beverage.java`, `service/FoodItemService.java`, `controller/FoodItemController.java`, `templates/admin/food*`
- **IT25101831** → `model/Order.java`, `service/OrderService.java`, `controller/OrderController.java`, `templates/order/`
- **IT25101967** → `model/Payment.java`, `model/CardPayment.java`, `model/CashPayment.java`, `service/PaymentService.java`, `controller/PaymentController.java`, `templates/payment/`
- **IT25100159** → `model/Review.java`, `service/ReviewService.java`, `controller/ReviewController.java`, `templates/review/`, `templates/admin/reviews.html`

---

*SE1020 – Object Oriented Programming | FoodHub Project*
