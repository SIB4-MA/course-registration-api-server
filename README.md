# API Documentation

## DB

- name: `db_course`

## Endpoint

`http://localhost:9000/api`

## Response Template

```json
{
  "status": boolean,
  "messages": [
    string,
    string,
    .....
  ],
  "payload": [
    {
      .....
    },
    {
      .....
    }
  ]
}
```

## USER

### Register

- URL: `/register`
- Method: `POST`
- Request Body:
  ```json
  {
    "name": string,
    "username": string,
    "email": string,
    "password": string
  }
  ```
- Response
  ```json
  {
    "status": true,
    "messages": ["Akun berhasil didaftarkan"],
    "payload": [
      {
        "id": 1,
        "username": "kana",
        "password": "$2a$10$XzDN3cVVL.RPYUS2TbrI9.1CAmIGnmKA6zLESEhZb81it8zLGzjdm",
        "is_enabled": true,
        "is_account_non_locked": true,
        "created_at": "2023-06-20T06:42:31.544+00:00",
        "roles": [
          {
            "id": 1,
            "name": "user",
            "privileges": [
              {
                "id": 1,
                "name": "create"
              },
              {
                "id": 2,
                "name": "read"
              },
              {
                "id": 3,
                "name": "update"
              },
              {
                "id": 4,
                "name": "delete"
              }
            ]
          }
        ]
      }
    ]
  }
  ```

### Login

- URL: `/login`
- Method: `POST`
- Request Body:

  ```json
  {
    "username": string,
    "password": string
  }
  ```
- Response

  ```json
  {
  "status": true,
  "messages": [
    "berhasil login"
  ],
  "payload": [
    {
      "username": "kana",
      "email": "kana@gmail.com",
      "authorities": [
        "ROLE_USER",
        ....
        ....
      ]
    }
  ]
  }
  ```

### Add course (Authorized)

- Method: `POST`
- URL: `/trasaction`
- Request Body:

  ```json
  {
    "courseId": integer
  }
  ```

### Get Trasaction by Member Id session (Authorized)

- Method: `GET`
- URL: `/transaction/member-transactions`

### Get All History by Member Id session (Authorized)

- Method: `GET`
- URL `/history/member`

### Get All (course) transaction by member Id session where is_registered is true

- Method: `GET`
- URL: `/transaction/course-registered`

---

## ADMIN

### CRUD course (Authorized)

- Method Available: `GET, POST, PUT, DELETE`
- URL `GET, POST`: `/course`
- URL `GET, PUT, DELETE`: `/course/{id}`
- Request Body `POST`:

  ```json
  {
  "thumbnail" : "this url img x",
  "title": "fullstack java",
  "instructor": "rizki",
  "description": "belajar fullstack springboot java dev",
  "price": "free",
  "periode": "3 bulan",
  "start" : "typedata date"
  "end": "typedata date"
   "category": {
     "id": 1
   }
  }
  ```

## Get All Transaction (Authorized)

- Method: `GET`
- URL: `/transaction`

### Update Status Transaction (Authorized)

- Method: `PUT`
- URL: `/transaction/{id}`
- Request Body

  ```json
  {
    "statusUpdate": "success" // success, process, failed
  }
  ```

### Get All History (Authorized)

- Method: `GET`
- URL: `/history`

### Get All Members (Authorized)

- Method: `GET`
- URL:`/member`

### JMS auto sender when admin (Authorized) updates status Transaction by member email

---

## No Authorized

### Get All Available Courses

- Method: `GET`
- URL: `/course`

### Get Course by id

- Method: `GET`
- URL: `/course/{id}`
