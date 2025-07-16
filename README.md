# E-COMMERCE-API

A list of available products should be able to be fetched by users via the API. Shopping cart management (adding, updating, and removing items) should be possible for users. An order should be able to be created from their cart. User authentication and two roles (customer, admin) should be implemented. JWT (JSON Web Tokens) should be used for securing routes. Products can only be viewed, added to the cart, and orders placed by the customer. Products can also be managed (added, updated, deleted) by the admin.

Spring Boot Application Running at ---> http://localhost:8080.

GO TO POSTMAN
1. User Registration
First, let's register a CUSTOMER and an ADMIN user.

Endpoint: POST http://localhost:8080/api/auth/register

For a CUSTOMER:

{
    "username": "customeruser",
    "password": "password123",
    "role": "CUSTOMER"
}

For an ADMIN:

{
    "username": "adminuser",
    "password": "adminpassword",
    "role": "ADMIN"
}

Expected Response: 201 Created with a success message.
![image alt](https://github.com/YashasAP/E-COMMERCE-API/blob/2edcb7d3fb0759a1f629a4a2f5dd1a99f47de9ca/postman%20Images/customer_registration.png)
![image alt]([https://github.com/YashasAP/E-COMMERCE-API/blob/222289bc8040ce1906b275af4af7ab6ab3b7a93a/postman%20Images/admin_registration.png )

2. User Login and Get JWT Token
After registration, log in to get a JWT token.

Endpoint: POST http://localhost:8080/api/auth/login

Headers:

Content-Type: application/json

Body (raw, JSON):

For Customer:

{
    "username": "customeruser",
    "password": "password123"
}

For Admin:

{
    "username": "adminuser",
    "password": "adminpassword"
}

Expected Response: 200 OK with a JSON body containing the token, username, and role.

![image alt](image_url)

{
    "token": "eyJhbGciOiJIUzI1NiJ9...", // <-- Copy this token!
    "username": "customeruser",
    "role": "CUSTOMER"
}
3. Product Management (Admin Role Required)
You need to use the JWT token obtained from logging in as adminuser for these requests.

Authorization: In Postman, go to the "Authorization" tab, select "Type" as Bearer Token, and paste your admin JWT token into the "Token" field.

  3.1. Get All Products (Public)
  Endpoint: GET http://localhost:8080/api/products

  Headers: (No specific headers needed, public access)

  Expected Response: 200 OK with a list of products (initially empty).


  ![image alt](image_url)

  3.2. Create a Product (Admin Only)
  Endpoint: POST http://localhost:8080/api/products

  Headers:

  Content-Type: application/json

  Authorization: Bearer <ADMIN_JWT_TOKEN>

  Body (raw, JSON):

  {
    "name": "Laptop Pro",
    "description": "High performance laptop for professionals.",
    "price": 1200.00,
    "stock": 50
  }

  Expected Response: 201 Created with the created product details.

  ![image alt](image_url)



  3.3. Get Product by ID (Public)
  Endpoint: GET http://localhost:8080/api/products/{productId} (Replace {productId} with an actual product ID, e.g., http://localhost:8080/api/products/1)

   Expected Response: 200 OK with the product details, or 404 Not Found if the ID doesn't exist.

   ![image alt](image_url)

  3.4. Update a Product (Admin Only)
  Endpoint: PUT http://localhost:8080/api/products/{productId} (e.g., http://localhost:8080/api/products/1)

 Headers:

 Content-Type: application/json
 Authorization: Bearer <ADMIN_JWT_TOKEN>

 Body (raw, JSON):

 {
    "name": "Laptop Pro Max",
    "description": "Ultra-high performance laptop with 32GB RAM.",
    "price": 1500.00,
    "stock": 45
 }

 Expected Response: 200 OK with the updated product details.

 ![image alt](image_url)

 3.5. Delete a Product (Admin Only)
 Endpoint: DELETE http://localhost:8080/api/products/{productId} (e.g., http://localhost:8080/api/products/1)

 Headers:

 Authorization: Bearer <ADMIN_JWT_TOKEN>

 Expected Response: 204 No Content.
 ![image alt](image_url)

4. Cart Management (Customer Role Required)
  You need to use the JWT token obtained from logging in as customeruser for these requests.
  Authorization: In Postman, go to the "Authorization" tab, select "Type" as Bearer Token, and paste your customer JWT token into the "Token" field.

 4.1. Get User's Cart
 Endpoint: GET http://localhost:8080/api/cart

 Headers:

 Authorization: Bearer <CUSTOMER_JWT_TOKEN>

 Expected Response: 200 OK with the current cart details.

4.2. Add Product to Cart
Endpoint: POST http://localhost:8080/api/cart/add

Headers:
Content-Type: application/json
Authorization: Bearer <CUSTOMER_JWT_TOKEN>

Body (raw, JSON):

{
    "productId": 1,  
    "quantity": 2
}

Expected Response: 200 OK with the updated cart details.
![image alt](image_url)

4.3. Update Cart Item Quantity
Endpoint: PUT http://localhost:8080/api/cart/update

Headers:
Content-Type: application/json
Authorization: Bearer <CUSTOMER_JWT_TOKEN>
Body (raw, JSON):

{
    "productId": 1,
    "quantity": 5 // New quantity for the product
}

Expected Response: 200 OK with the updated cart details.

4.4. Remove Product from Cart
Endpoint: DELETE http://localhost:8080/api/cart/remove/{productId} (e.g: http://localhost:8080/api/cart/remove/1)

Headers:
Authorization: Bearer <CUSTOMER_JWT_TOKEN>

Expected Response: 200 OK with the updated cart details (after removal).
![image alt](image_url)


5. Order Management
  5.1. Create Order from Cart (Customer Only)
  Endpoint: POST http://localhost:8080/api/orders

  Headers:
  Authorization: Bearer <CUSTOMER_JWT_TOKEN>
  Body: (No body needed, order is created from the authenticated user's cart)

  Expected Response: 201 Created with the newly created order details. The cart will be emptied.

5.2. Get User's Orders (Customer Only)
Endpoint: GET http://localhost:8080/api/orders

Headers:
Authorization: Bearer <CUSTOMER_JWT_TOKEN>

Expected Response: 200 OK with a list of orders for the authenticated customer.

5.3. Get All Orders (Admin Only)
Endpoint: GET http://localhost:8080/api/orders/all

Headers:

Authorization: Bearer <ADMIN_JWT_TOKEN>

Expected Response: 200 OK with a list of all orders in the system.

5.4. Get Specific Order by ID (Customer/Admin)
Endpoint: GET http://localhost:8080/api/orders/{orderId} (e.g., http://localhost:8080/api/orders/1)

Headers:

Authorization: Bearer <CUSTOMER_JWT_TOKEN> (if customer, must be their order)

OR Authorization: Bearer <ADMIN_JWT_TOKEN> (admin can view any order)

Expected Response: 200 OK with the order details, 404 Not Found, or 403 Forbidden if a customer tries to view another user's order.


