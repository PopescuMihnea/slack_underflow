### 1. User Account Management

**User Story:**
As a user, I want to be able to make an account so I can keep track of my stats and not allow anyone to use the website.

**Acceptance Criteria:**

- Users can create and log in to their accounts using a username and password.
- Account information is stored in a database with hashed passwords.
- Server-side validation is implemented for user-provided information.
- Accounts have roles (e.g., user and admin).
- Additional user information, such as badges and points, is stored.
- Users can modify their account password.

---

### 2. Question Management

**User Story:**
As a user, I want to create, edit, delete, and search for questions to find answers to my problems.

**Acceptance Criteria:**

- Users can create a question by providing a title and question body.
- Questions can be modified or deleted by the user who created them.
- Timestamps are stored to indicate when the question was created and updated.
- Other users can search questions by title, ID, or get all questions by a specific username or user ID.

---

### 3. Answer Management

**User Story:**
As a user, I want to create, edit, and delete answers for questions to help others.

**Acceptance Criteria:**

- Users can provide answers to a question by navigating to it and providing an answer body.
- Answers can be modified or deleted by the user who created them.
- Timestamps indicate when the answer was created and modified.
- Users can search answers by question or get all answers by a certain user (username or user ID).

---

### 4. Suggestions to Answers

**User Story:**
As a user, I want to add, edit, and delete suggestions to answers to correct and improve them.

**Acceptance Criteria:**

- Users can add suggestions to answers by navigating to the answer and providing a text body.
- Suggestions can be modified or deleted by the user.
- Suggestions can be searched by answer or by a certain user (username or user ID).

---

### 5. Ranking Answers

**User Story:**
As a user, I want to rank answers to my questions to help others find the best answers easily.

**Acceptance Criteria:**

- Question creators can give answers a rank between zero and three.
- Points are assigned based on the rank given by the question creator.
- Duplicate ranks result in the removal of the older answer from the leaderboard.

---

### 6. Points for Ranked Answers

**User Story:**
As a user, I want to get points for my answers being ranked to feel a sense of accomplishment.

---

### 7. Badges for Points

**User Story:**
As a user, I want to have badges for having a certain amount of points to showcase my contributions.

---

### 8. Topic-based Search

**User Story:**
As a user, I want to be able to search questions by topics and add topics to the questions I create for faster answers.

---

### 9. Reset Answer Ranks

**User Story:**
As a user, I want to set all ranks of the answers to a question I asked to zero to fix any mistakes in ranking.

---

### 10. Admin Moderation

**User Story:**
As an admin, I want to see all answers and suggestions and be able to delete inappropriate content.

### Swagger Documentation

For detailed API documentation, please refer to the Swagger documentation available
in [documentation/SwaggerDocumentation.md](documentation/SwaggerDocumentation.md).

You can explore and interact with the API endpoints using Swagger to understand their functionality and usage.

Feel free to check the Swagger documentation for comprehensive information about the API.

