package models

import play.api.libs.json.Json

case class Product(id: Int,name:String,description:String,productId:Int,categoryId:Int)

object Product {
  implicit val categoryFormat = Json.format[Product]
}