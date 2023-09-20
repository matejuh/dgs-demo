package com.productboard.dgsdemo

data class Page<T>(val nodes: List<T>, val hasNextPage: Boolean = false, val hasPreviousPage: Boolean = false)
