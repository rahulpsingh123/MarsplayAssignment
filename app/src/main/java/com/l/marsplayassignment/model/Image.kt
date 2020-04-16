package com.l.marsplayassignment.model

class Image {

    var name: String? = null
    var imageUrl: String? = null
    var key: String? = null

    constructor() {
    }

    constructor(name: String, imageUrl: String?) {
        this.name = name
        this.imageUrl = imageUrl
    }

}