# Food Delivery Application

A dual-client (Web + Swing Desktop) food delivery platform built with Jakarta EE, Tomcat, and MySQL.

## Architecture

This project uses a unified Tomcat backend and a single MySQL database to serve both the existing JSP-based web application and the new Java Swing desktop client.

Both clients communicate through the same underlying Tomcat infrastructure:
- **Web App**: Uses Servlets and JSP views.
- **Swing App**: Uses REST APIs (`/api/v1/*`) and Java `HttpClient`.
- **Real-time Sync**: Both clients connect to the `/ws/events/{userId}` WebSocket to receive live pushes (e.g. order status changes).

## Prerequisites
- **JDK**: Java 21 (or compatible Java 11+ version)
- **Database**: MySQL 8+
- **IDE**: IntelliJ IDEA (recommended) or Eclipse
- **Server**: Tomcat 10+ (for deploying the WAR)

## Database Setup
1. Ensure MySQL is running locally on port `3306`.
2. Create the target schema if it doesn't exist: `CREATE DATABASE food_delivery_db;`
3. The application uses `root` with no password by default for connections (adjust your local settings if needed).

## Running the Web Application (Backend + Frontend)
1. In IntelliJ, configure a Local Tomcat Run Configuration.
2. Add the `FoodDeliveryApp:war exploded` artifact to deployment.
3. Start the Tomcat server. The app will be available at `http://localhost:8080/FoodDeliveryApp`.

## Running the Swing Desktop Client
The Swing client is built into the same Maven project and reuses the exact same REST backend. Tomcat **must** be running first.

1. Ensure your Tomcat backend is running.
2. In IntelliJ, navigate to `src/main/java/com/foodapp/swing/MainDesktopApp.java`.
3. Right-click the file and select **Run 'MainDesktopApp.main()'**.
4. The Swing client will launch and connect to `http://localhost:8080/FoodDeliveryApp/api/v1` and the WebSocket sync layer.

## Real-Time Synchronization
The architecture guarantees no "Lost Updates" and single-source-of-truth accuracy:
- **Optimistic Locking**: Critical entities (`Order`, `Cart`, etc.) use `@Version` to prevent simultaneous collision updates.
- **WebSocket Broadcast**: Changes in Tomcat DAOs instantly push `ORDER_UPDATED`, `CART_UPDATED`, and `PAYMENT_UPDATED` payloads to connected active profiles across all platforms.
