# ☕ Just Cafe — Spring Boot Web Application

A full-stack coffee shop website built with **Spring Boot**, **H2 (in-memory database)**, **Thymeleaf**, and a hand-crafted HTML/CSS/JS frontend.

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+

### Run the Application

```bash
# Clone / extract the project
cd justcafe

# Build and run
mvn spring-boot:run
```

Open your browser at: **http://localhost:8080**

---

## 🗄️ H2 Database Console

Access the in-memory database at: **http://localhost:8080/h2-console**

| Field    | Value                                         |
|----------|-----------------------------------------------|
| JDBC URL | `jdbc:h2:mem:justcafedb`                      |
| Username | `sa`                                          |
| Password | *(leave blank)*                               |

The database is seeded automatically on startup with:
- **29 menu items** (drinks + food)
- **42 customization options** (size, milk, sweetness, syrup, extras, food add-ons)
- **6 cafe locations** across Metro Manila
- **5 sample orders**

---

## 📁 Project Structure

```
justcafe/
├── src/
│   ├── main/
│   │   ├── java/com/justcafe/
│   │   │   ├── JustCafeApplication.java        # Main entry point
│   │   │   ├── DataInitializer.java            # Seeds H2 with test data
│   │   │   ├── controller/
│   │   │   │   ├── HomeController.java         # Serves the main page
│   │   │   │   ├── MenuController.java         # REST: /api/menu
│   │   │   │   ├── OrderController.java        # REST: /api/orders
│   │   │   │   └── LocationController.java     # REST: /api/locations
│   │   │   ├── model/
│   │   │   │   ├── MenuItem.java
│   │   │   │   ├── CustomizationOption.java
│   │   │   │   ├── CafeLocation.java
│   │   │   │   └── Order.java
│   │   │   ├── repository/                     # JPA Repositories
│   │   │   └── service/                        # Business Logic
│   │   └── resources/
│   │       ├── templates/index.html            # Thymeleaf main page
│   │       ├── static/css/style.css            # All styles
│   │       └── static/js/
│   │           ├── main.js                     # UI logic, cart, ordering
│   │           └── coffee-canvas.js            # Scroll coffee-pour animation
│   └── test/
│       └── java/com/justcafe/
│           └── JustCafeApplicationTests.java
└── pom.xml
```

---

## 🌐 REST API Endpoints

| Method | Path                           | Description                   |
|--------|--------------------------------|-------------------------------|
| GET    | `/api/menu/drinks`             | All available drinks          |
| GET    | `/api/menu/foods`              | All available food items      |
| GET    | `/api/menu/all`                | All menu items                |
| GET    | `/api/menu/{id}`               | Single menu item              |
| GET    | `/api/menu/customizations/drink` | Drink customization options |
| GET    | `/api/menu/customizations/food`  | Food customization options  |
| POST   | `/api/orders`                  | Place an order                |
| GET    | `/api/orders/{id}`             | Get order by ID               |
| GET    | `/api/locations`               | All cafe locations            |

---

## ✨ Features

- **Coffee-pour scroll animation** — canvas-based animation that draws liquid being poured as you scroll
- **Full menu** with 29 items across hot drinks, cold drinks, pastries, and meals
- **Drink & food customizer** — mix and match size, milk, sweetness, syrups, extras
- **Live cart** — add/remove items, see running total
- **Order placement** — submits to Spring Boot REST API, stored in H2
- **About Us** section with stats and brand values
- **6 Metro Manila locations** with hours and phone numbers
- Fully responsive (mobile-friendly)

---

## 🧪 Running Tests

```bash
mvn test
```

---

## 🛠 Tech Stack

| Layer     | Technology                        |
|-----------|-----------------------------------|
| Backend   | Java 17, Spring Boot 3.2          |
| Database  | H2 (in-memory), Spring Data JPA   |
| Templates | Thymeleaf                         |
| Frontend  | HTML5, CSS3, Vanilla JavaScript   |
| Fonts     | Playfair Display, DM Sans (Google)|
| Build     | Maven                             |
