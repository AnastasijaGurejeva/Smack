package com.example.smack.Utilities

const val BASE_URL = "https://chatchatappana.herokuapp.com/v1/"
//const val BASE_URL = "http://10.0.2.2:3005/v1/"

const val SOCKET_URL = "https://chatchatappana.herokuapp.com/"
const val URL_REGISTER = "${BASE_URL}account/register"
const val URL_LOGIN = "${BASE_URL}account/login"
const val URL_ADD_USER = "${BASE_URL}user/add"
const val URL_FIND_USER = "${BASE_URL}user/byEmail/"

//Broadcast constants
const val BROADCAST_USER_DATA_CHANGES = "Broadcast user data changed"