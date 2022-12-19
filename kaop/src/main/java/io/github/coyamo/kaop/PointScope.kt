package io.github.coyamo.kaop

class PointScope {
    private val data = mutableMapOf<String, Any?>()

    operator fun get(key:String) = data[key]

    operator fun set(key:String, value:Any?){
        data[key] = value
    }

}