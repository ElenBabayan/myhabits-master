## User Endpoints

### Get User
- **Endpoint:** GET `/user`
  ```json
  {
    "id": 1,
    "name": "John Doe",
    "email": "johndoe@example.com",
    "role": "USER"
  }
  ```

### Get User by ID
- **Endpoint:** GET `/users/{id}`
  ```json
  {
    "id": 1,
    "name": "John Doe",
    "email": "johndoe@example.com",
    "role": "USER"
  }
  ```

### Get User by Email
- **Endpoint:** GET `/email/{email}`
  ```json
  {
    "id": 1,
    "name": "John Doe",
    "email": "johndoe@example.com",
    "role": "USER"
  }
  ```

### Get All Users
- **Endpoint:** GET `/users`
- **Description:** Returns a list of all users in the system.

### Update User
- **Endpoint:** PUT `/users/{id}`
- **Example Request Body:**
  ```json
  {
    "name": "Updated Name",
    "email": "updated@example.com",
    "role": "USER"
  }
  ```


### Delete User
- **Endpoint:** DELETE `/users/{id}`
- **Description:** Deletes a user from the system.


------------------------------------------------------------------------------------------------------------------------
## Authentication Endpoints

### Signup
- **Endpoint:** POST `/signup`
  ```json
  {
    "name": "John Doe",
    "email": "johndoe@example.com",
    "password": "password123"
  }
  ```

### Login
- **Endpoint:** POST `/login`
  ```json
  {
    "email": "johndoe@example.com",
    "password": "password123"
  }
  ```
- **Note:** The JWT token should be included in the `Authorization` header for subsequent authenticated requests.


------------------------------------------------------------------------------------------------------------------------
## Habit Endpoints

### Create Habit
- **Endpoint:** POST `/habits`
  ```json
  {
    "name": "Read a book",
    "dueDate": "2023-12-04T12:00:00",
    "description": "Read a book for 30 minutes every day",
    "user_id": 2
  }
  ```

### Get Habit by ID
- **Endpoint:** GET `/habits/{id}`


### Get Habits by User
- **Endpoint:** GET `/habits/user`
- **Description:** Retrieves all habits associated with the authenticated user.


### Get All Habits
- **Endpoint:** GET `/habits`
- **Description:** Retrieves all habits from the system.

### Update Habit
- **Endpoint:** PUT `/habits/{id}`
  ```json
  {
      "name": "Read a paper",
      "description": "Read a paper for 30 minutes every day",
      "user_id": 2
  }
  ```

### Delete Habit
- **Endpoint:** DELETE `/habit/{id}`

------------------------------------------------------------------------------------------------------------------------
## Progress Endpoints

### Save Progress
- **Endpoint:** POST `/progress`
- **Example Request Body:**
  ```json
  {
    "totalCount": 10,
    "currentStreak": 5,
    "longestStreak": 7,
    "user": {
      "id": 1
    },
    "habit": {
      "id": 1
    }
  }

### Get Progress by ID
- **Endpoint:** GET `/progress/{id}`
- **Description:** Retrieves progress by its ID.
- **Path Parameter:** The ID of the progress to retrieve.
- **Response:** Returns the progress object if found, or throws a `ResourceNotFoundException` if the progress is not found.

### Get Progress by User and Habit
- **Endpoint:** GET `/progress/user/{userId}/habit/{habitId}`
- **Description:** Retrieves progress by user ID and habit ID.
- **Path Parameters:**
  - `userId`: The ID of the user.
  - `habitId`: The ID of the habit.
- **Response:** Returns the progress object if found, or throws a `ResourceNotFoundException` if the progress is not found.


### Get All Progress
- **Endpoint:** GET `/progress/all`
- **Description:** Retrieves all progress from the system.
- **Response:** Returns a list of progress objects if found, or returns a no content response if no progress is found.
