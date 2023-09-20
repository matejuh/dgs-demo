package com.productboard.dgsdemo.plane

data class Plane(val sign: String, val type: String)

typealias PlaneSign = String

data class CreatePlane(val sign: String, val type: String)
