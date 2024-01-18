# QA API

API for questions and answers

## Version: 1.0

**Contact information:**  
Popescu Mihnea-Valentin

### /user/modify

#### PUT

##### Summary:

Modifies the logged in user

##### Responses

| Code | Description                              |
|------|------------------------------------------|
| 200  | Modified the user with the data provided |
| 400  | Invalid model data                       |
| 401  | User is not authorized                   |
| 404  | Model not found                          |

### /user/login

#### PUT

##### Summary:

Logs in the user, sending a jwt token to be used in authenticating the user

##### Responses

| Code | Description                         |
|------|-------------------------------------|
| 200  | The user has successfully logged in |
| 400  | Invalid model data                  |
| 401  | User is not authorized              |
| 404  | Model not found                     |

### /suggestion/modify/{id}

#### PUT

##### Summary:

Modifies a suggestion with the given id

##### Parameters

| Name | Located in | Description                                     | Required | Schema |
|------|------------|-------------------------------------------------|----------|--------|
| id   | path       | The id of the suggestion that is to be modified | Yes      | long   |

##### Responses

| Code | Description                      |
|------|----------------------------------|
| 200  | Successfully modified suggestion |
| 400  | Invalid model data               |
| 401  | User is not authorized           |
| 404  | Model not found                  |

### /question/modify/{id}

#### PUT

##### Summary:

Modifies a question with the given id

##### Parameters

| Name | Located in | Description                                   | Required | Schema |
|------|------------|-----------------------------------------------|----------|--------|
| id   | path       | The id of the question that is to be modified | Yes      | long   |

##### Responses

| Code | Description                                               |
|------|-----------------------------------------------------------|
| 200  | Successfully modified question with the given data and id |
| 400  | Invalid model data                                        |
| 401  | User is not authorized                                    |
| 404  | Model not found                                           |

### /user/register

#### POST

##### Summary:

Registers the user, adding him to the database, and sends a jwt token to be used in authenticating the user

##### Responses

| Code | Description                           |
|------|---------------------------------------|
| 201  | The user has been succesfully created |
| 400  | Invalid model data                    |
| 401  | User is not authorized                |
| 404  | Model not found                       |

### /suggestion/create

#### POST

##### Summary:

Creates a new suggestion for an answer

##### Responses

| Code | Description                     |
|------|---------------------------------|
| 201  | Successfully created suggestion |
| 400  | Invalid model data              |
| 401  | User is not authorized          |
| 404  | Model not found                 |

### /question/create

#### POST

##### Summary:

Creates a new question

##### Responses

| Code | Description                                       |
|------|---------------------------------------------------|
| 201  | Successfully created question with the given data |
| 400  | Invalid model data                                |
| 401  | User is not authorized                            |
| 404  | Model not found                                   |

### /answer/create

#### POST

##### Summary:

Creates a new answer

##### Responses

| Code | Description                                     |
|------|-------------------------------------------------|
| 201  | Successfully created answer with the given data |
| 400  | Invalid model data                              |
| 401  | User is not authorized                          |
| 404  | Model not found                                 |

### /answer/reset/{questionId}

#### PATCH

##### Summary:

Resets the rank of the answers of a given question, by id, to 0

##### Parameters

| Name       | Located in | Description                                                     | Required | Schema |
|------------|------------|-----------------------------------------------------------------|----------|--------|
| questionId | path       | The id of the question for which to reset all answer ranks to 0 | Yes      | long   |

##### Responses

| Code | Description                                                          |
|------|----------------------------------------------------------------------|
| 200  | Successfully reset the ranks of all the answers of the question to 0 |
| 400  | Invalid model data                                                   |
| 401  | User is not authorized                                               |
| 404  | Model not found                                                      |

### /answer/modify/{id}

#### PATCH

##### Summary:

Updates a answer with the given information

##### Parameters

| Name | Located in | Description                    | Required | Schema |
|------|------------|--------------------------------|----------|--------|
| id   | path       | The id of the answer to modify | Yes      | long   |

##### Responses

| Code | Description                                      |
|------|--------------------------------------------------|
| 200  | Successfully modified answer with the given data |
| 400  | Invalid model data                               |
| 401  | User is not authorized                           |
| 404  | Model not found                                  |

### /answer/modify/rank/{id}

#### PATCH

##### Summary:

Updates a answer with the given rank

##### Parameters

| Name | Located in | Description                                  | Required | Schema  |
|------|------------|----------------------------------------------|----------|---------|
| id   | path       | The id of the answer of which to update rank | Yes      | long    |
| rank | query      | The new rank of the answer                   | Yes      | integer |

##### Responses

| Code | Description                                      |
|------|--------------------------------------------------|
| 200  | Successfully modified answer with the given rank |
| 400  | Invalid model data                               |
| 401  | User is not authorized                           |
| 404  | Model not found                                  |

### /user/get/{id}

#### GET

##### Summary:

Gets information about a user with the provided id

##### Parameters

| Name | Located in | Description                           | Required | Schema |
|------|------------|---------------------------------------|----------|--------|
| id   | path       | Id of the user to get information for | Yes      | long   |

##### Responses

| Code | Description                                                      |
|------|------------------------------------------------------------------|
| 200  | The user has been found and his information returned in the body |
| 400  | Invalid model data                                               |
| 401  | User is not authorized                                           |
| 404  | Model not found                                                  |

### /topic/getAll

#### GET

##### Summary:

Gets a list of all topic objects

##### Responses

| Code | Description                 |
|------|-----------------------------|
| 200  | Successfully queried topics |
| 400  | Invalid model data          |
| 401  | User is not authorized      |
| 404  | Model not found             |

### /suggestion/getAll

#### GET

##### Summary:

Gets a list of all suggestions

##### Responses

| Code | Description                      |
|------|----------------------------------|
| 200  | Successfully queried suggestions |
| 400  | Invalid model data               |
| 401  | User is not authorized           |
| 404  | Model not found                  |

### /suggestion/get/{id}

#### GET

##### Summary:

Gets a suggestion with the given id

##### Parameters

| Name | Located in | Description                              | Required | Schema |
|------|------------|------------------------------------------|----------|--------|
| id   | path       | The id of the suggestion to get info for | Yes      | long   |

##### Responses

| Code | Description                        |
|------|------------------------------------|
| 200  | Successfully found suggestion info |
| 400  | Invalid model data                 |
| 401  | User is not authorized             |
| 404  | Model not found                    |

### /suggestion/get/user/{username}

#### GET

##### Summary:

Gets all suggestions that belong to the user with the given username

##### Parameters

| Name     | Located in | Description                                                          | Required | Schema |
|----------|------------|----------------------------------------------------------------------|----------|--------|
| username | path       | The username of the user which all of the suggestions will belong to | Yes      | string |

##### Responses

| Code | Description                                |
|------|--------------------------------------------|
| 200  | Successfully found suggestions of the user |
| 400  | Invalid model data                         |
| 401  | User is not authorized                     |
| 404  | Model not found                            |

### /suggestion/get/user/{id}

#### GET

##### Summary:

Gets all suggestions created by the user with the given id

##### Parameters

| Name | Located in | Description                                                    | Required | Schema |
|------|------------|----------------------------------------------------------------|----------|--------|
| id   | path       | The id of the user which all of the suggestions will belong to | Yes      | long   |

##### Responses

| Code | Description                                |
|------|--------------------------------------------|
| 200  | Successfully found suggestions of the user |
| 400  | Invalid model data                         |
| 401  | User is not authorized                     |
| 404  | Model not found                            |

### /suggestion/get/question/{answerId}

#### GET

##### Summary:

Gets all suggestions that belong to the answer with the given id

##### Parameters

| Name     | Located in | Description                                                | Required | Schema |
|----------|------------|------------------------------------------------------------|----------|--------|
| answerId | path       | The id of the answer that the suggestions should belong to | Yes      | long   |

##### Responses

| Code | Description                                  |
|------|----------------------------------------------|
| 200  | Successfully found suggestions of the answer |
| 400  | Invalid model data                           |
| 401  | User is not authorized                       |
| 404  | Model not found                              |

### /question/getAll

#### GET

##### Summary:

Gets a list of all questions

##### Responses

| Code | Description                    |
|------|--------------------------------|
| 200  | Successfully queried questions |
| 400  | Invalid model data             |
| 401  | User is not authorized         |
| 404  | Model not found                |

### /question/getAllByTopics

#### GET

##### Summary:

Gets a list of all the questions that include requested topics

##### Parameters

| Name   | Located in | Description | Required | Schema     |
|--------|------------|-------------|----------|------------|
| topics | query      |             | Yes      | [ string ] |

##### Responses

| Code | Description                                      |
|------|--------------------------------------------------|
| 200  | Successfully queried suggestions by given topics |
| 400  | Invalid model data                               |
| 401  | User is not authorized                           |
| 404  | Model not found                                  |

### /question/getAllByTitle

#### GET

##### Summary:

Gets a list of all the questions that include requested title string

##### Parameters

| Name  | Located in | Description | Required | Schema |
|-------|------------|-------------|----------|--------|
| title | query      |             | Yes      | string |

##### Responses

| Code | Description                                     |
|------|-------------------------------------------------|
| 200  | Successfully queried suggestions by given title |
| 400  | Invalid model data                              |
| 401  | User is not authorized                          |
| 404  | Model not found                                 |

### /question/get/{id}

#### GET

##### Summary:

Gets the question with the given id

##### Parameters

| Name | Located in | Description                                            | Required | Schema |
|------|------------|--------------------------------------------------------|----------|--------|
| id   | path       | The id of the question you want to get information for | Yes      | long   |

##### Responses

| Code | Description                                       |
|------|---------------------------------------------------|
| 200  | Successfully found the question with the given id |
| 400  | Invalid model data                                |
| 401  | User is not authorized                            |
| 404  | Model not found                                   |

### /question/get/user/{username}

#### GET

##### Summary:

Gets a list of all the questions that have the given user username

##### Parameters

| Name     | Located in | Description                                      | Required | Schema |
|----------|------------|--------------------------------------------------|----------|--------|
| username | path       | The id of the user that the question should have | Yes      | string |

##### Responses

| Code | Description                                             |
|------|---------------------------------------------------------|
| 200  | Successfully queried suggestions by given user username |
| 400  | Invalid model data                                      |
| 401  | User is not authorized                                  |
| 404  | Model not found                                         |

### /question/get/user/{id}

#### GET

##### Summary:

Gets a list of all the questions that have the given user id

##### Parameters

| Name | Located in | Description                                      | Required | Schema |
|------|------------|--------------------------------------------------|----------|--------|
| id   | path       | The id of the user that the question should have | Yes      | long   |

##### Responses

| Code | Description                                       |
|------|---------------------------------------------------|
| 200  | Successfully queried suggestions by given user id |
| 400  | Invalid model data                                |
| 401  | User is not authorized                            |
| 404  | Model not found                                   |

### /answer/getAll

#### GET

##### Summary:

Gets a list of all answers

##### Responses

| Code | Description                  |
|------|------------------------------|
| 200  | Successfully queried answers |
| 400  | Invalid model data           |
| 401  | User is not authorized       |
| 404  | Model not found              |

### /answer/get/{id}

#### GET

##### Summary:

Gets an answer with the given id

##### Parameters

| Name | Located in | Description                 | Required | Schema |
|------|------------|-----------------------------|----------|--------|
| id   | path       | The id of the answer to get | Yes      | long   |

##### Responses

| Code | Description                           |
|------|---------------------------------------|
| 200  | Successfully found answer with the id |
| 400  | Invalid model data                    |
| 401  | User is not authorized                |
| 404  | Model not found                       |

### /answer/get/user/{username}

#### GET

##### Summary:

Gets a list of all answers belonging to a user with the given username

##### Parameters

| Name     | Located in | Description                                          | Required | Schema |
|----------|------------|------------------------------------------------------|----------|--------|
| username | path       | The username of the user which the answers belong to | Yes      | string |

##### Responses

| Code | Description                                                      |
|------|------------------------------------------------------------------|
| 200  | Successfully queried answers of the user with the given username |
| 400  | Invalid model data                                               |
| 401  | User is not authorized                                           |
| 404  | Model not found                                                  |

### /answer/get/user/{id}

#### GET

##### Summary:

Gets a list of all answers belonging to a user with the given id

##### Parameters

| Name | Located in | Description                                    | Required | Schema |
|------|------------|------------------------------------------------|----------|--------|
| id   | path       | The id of the user which the answers belong to | Yes      | long   |

##### Responses

| Code | Description                                                |
|------|------------------------------------------------------------|
| 200  | Successfully queried answers of the user with the given id |
| 400  | Invalid model data                                         |
| 401  | User is not authorized                                     |
| 404  | Model not found                                            |

### /answer/get/question/{questionId}

#### GET

##### Summary:

Gets a list of all answers belonging to the question with the given id

##### Parameters

| Name       | Located in | Description                                        | Required | Schema |
|------------|------------|----------------------------------------------------|----------|--------|
| questionId | path       | The id of the question which the answers belong to | Yes      | long   |

##### Responses

| Code | Description                                                    |
|------|----------------------------------------------------------------|
| 200  | Successfully queried answers of the question with the given id |
| 400  | Invalid model data                                             |
| 401  | User is not authorized                                         |
| 404  | Model not found                                                |

### /user/delete

#### DELETE

##### Summary:

Deletes the user currently logged in

##### Responses

| Code | Description                                               |
|------|-----------------------------------------------------------|
| 200  | The user has been deleted                                 |
| 400  | User deletion failed due to faulty data or database error |
| 401  | User is not authorized                                    |
| 404  | Model not found                                           |

### /suggestion/delete/{id}

#### DELETE

##### Summary:

Deletes the suggestion, belonging to the authenticated user, with the given id

##### Parameters

| Name | Located in | Description                                                               | Required | Schema |
|------|------------|---------------------------------------------------------------------------|----------|--------|
| id   | path       | The id of the suggestion, owned by the logged in user, you want to delete | Yes      | long   |

##### Responses

| Code | Description                                                  |
|------|--------------------------------------------------------------|
| 200  | The suggestion has been deleted                              |
| 400  | Suggestion deletion failed due to wrong id or database error |
| 401  | User is not authorized                                       |
| 404  | Model not found                                              |

### /question/delete/{id}

#### DELETE

##### Summary:

Deletes the question, belonging to the authenticated user, with the given id

##### Parameters

| Name | Located in | Description                                              | Required | Schema |
|------|------------|----------------------------------------------------------|----------|--------|
| id   | path       | The id of the question, belonging to the user, to delete | Yes      | long   |

##### Responses

| Code | Description                                                |
|------|------------------------------------------------------------|
| 200  | The question has been deleted                              |
| 400  | Question deletion failed due to wrong id or database error |
| 401  | User is not authorized                                     |
| 404  | Model not found                                            |

### /answer/delete/{id}

#### DELETE

##### Summary:

Deletes the answer, belonging to the authenticated user, with the given id

##### Parameters

| Name | Located in | Description                                  | Required | Schema |
|------|------------|----------------------------------------------|----------|--------|
| id   | path       | The id of the question that is to be deleted | Yes      | long   |

##### Responses

| Code | Description                                              |
|------|----------------------------------------------------------|
| 200  | The answer has been deleted                              |
| 400  | Answer deletion failed due to wrong id or database error |
| 401  | User is not authorized                                   |
| 404  | Model not found                                          |
