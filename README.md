# My Spring Boot Security Project 


# flow of  ecommerce

<pre> ```mermaid flowchart TD A[User] --> B[Login Form] B --> C{Spring Security} C -->|Valid Credentials| D[Generate JWT Token] C -->|Invalid Credentials| E[Access Denied] D --> F[Access Protected API] F --> G[Authorization Check] G -->|Role = Admin| H[Admin Access Granted] G -->|Role = User| I[User Access Granted] G -->|Unauthorized| E[Access Denied] ``` </pre>