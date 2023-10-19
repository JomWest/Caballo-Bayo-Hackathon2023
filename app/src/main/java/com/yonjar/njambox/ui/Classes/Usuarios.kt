package com.yonjar.njambox.Classes

data class Usuarios(
    var name:String = "",
    var lastName:String = "",
    var userName:String = "",
    var email:String = "",
    var password:String = "",
    var userId:String = "",
    var profileImage:String = "https://firebasestorage.googleapis.com/v0/b/registrojambox.appspot.com/o/defaultProfile.jpeg?alt=media&token=3d2d1de6-1afa-43f7-929a-f797135264da",
    var descripcion:String = "Descripción"
)