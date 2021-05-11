package controllers

import play.api.data.Form
import play.api.data.Forms.{mapping, number}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, MessagesAbstractController, MessagesControllerComponents}
import repositories.{CommentRepository, InvoiceRepository}

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class CommentController @Inject()(invoiceRepo:InvoiceRepository,cc: MessagesControllerComponents)
                                 (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val invoiceForm: Form[CreateInvoiceForm] = Form {
    mapping(
      "orderId" -> number,
      "costumerId" -> number,
      "type0f" -> number,

    )(CreateInvoiceForm.apply)(CreateInvoiceForm.unapply)
  }

  val updateInvoiceForm: Form[UpdateInvoiceForm] = Form {
    mapping(
      "id" -> number,
      "orderId" -> number,
      "costumerId" -> number,
      "type0f" -> number,
    )(UpdateInvoiceForm.apply)(UpdateInvoiceForm.unapply)
  }

  def getInvoices: Action[AnyContent] = Action.async { implicit request =>
    val invoices = invoiceRepo.list()
    invoices.map( comments => Ok(views.html.invoices(invoices)))
  }

  def getInvoiuce(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val comment = invoiceRepo.getById(id)
    comment.map(comment => comment match {
      case Some(p) => Ok(views.html.index(p))
      case None => Redirect(routes.HomeController.get())
    })
  }

  def delete(id: Int): Action[AnyContent] = Action {
    invoiceRepo.delete(id)
    //Redirect("/c")
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
case class CreateInvoiceForm(orderId:Int,customerId:Int,typeOf:Int)
case class UpdateInvoiceForm(id: Int,orderId:Int,customerId:Int,typeOf:Int)