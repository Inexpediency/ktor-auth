package com.example.services

open class UserServiceException(message: String = "") : Exception(message)

class UserNotFoundException(message: String = "") : UserServiceException(message)

class UserCreateException(message: String = "") : UserServiceException(message)
