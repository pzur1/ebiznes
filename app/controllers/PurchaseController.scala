package controllers

import play.api.data.Form
import play.api.data.Forms.{mapping, number}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, MessagesAbstractController, MessagesControllerComponents}
import repositories.{PaymentRepository, PurchaseRepository}

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class PurchaseController @Inject()(purchaseRepo:PurchaseRepository, cc: MessagesControllerComponents)
                                 (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val purchaseForm: Form[CreatePurchaseForm] = Form {
    mapping(

      "type0f" -> number,
      "customerId"->number,

    )(CreatePurchaseForm.apply)(CreatePurchaseForm.unapply)
  }

  val updatePurchaseForm: Form[UpdatePurchaseForm] = Form {
    mapping(
      "id" -> number,
      "type0f" -> number,
      "customerId"->number,
    )(UpdatePurchaseForm.apply)(UpdatePurchaseForm.unapply)
  }

  def getPurchases: Action[AnyContent] = Action.async { implicit request =>
    val purchases = purchaseRepo.list()
    purchases.map( purchases => Ok(views.html.purchases(purchases)))
  }

  def getPurchase(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val purchase = purchaseRepo.getByIdOption(id)
    purchase.map(purchase => purchase match {
      case Some(p) => Ok(views.html.purchases(Seq(p)))
      case None => Redirect(routes.HomeController.index)
    })
  }

  def delete(id: Int): Action[AnyContent] = Action {
    purchaseRepo.delete(id)
    Redirect("/c")
  }

  def getPurchasesJson: Action[AnyContent] = Action.async { implicit request =>
    val purchases = purchaseRepo.list()
    purchases.map( purchases => Ok(Json.toJson(purchases)))
  }

  def getPurchaseJson(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val purchase = purchaseRepo.getByIdOption(id)
    purchase.map(purchase => purchase match {
      case Some(p) => Ok(Json.toJson(p))
      case None => Ok(Json.toJson(id))
    })
  }

  def deleteJson(id: Int): Action[AnyContent] = Action {
    purchaseRepo.delete(id)
    Redirect("/c")
  }



  def index = Action {
    Ok(views.html.index("GET."))
  }
  def delete = Action {
    Ok(views.html.index("DELETE."))
  }
  def patch = Action {
    Ok(views.html.index("PATCH."))
  }
  def add = Action {
    Ok(views.html.index("POST"))
  }
}
case class CreatePurchaseForm(orderId:Int,customerId:Int)
case class UpdatePurchaseForm(id: Int,orderId:Int,customerId:Int)