package filters

import akka.stream.Materializer
import javax.inject._
import play.api.libs.streams.Accumulator
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

/**
  * This is a simple filter that adds a header to all requests. It's
  * added to the application's list of filters by the
  * [[Filters]] class.
  *
  * @param ec This class is needed to execute code asynchronously.
  * It is used below by the `map` method.
  */
@Singleton
class TestFilter @Inject()(implicit mat: Materializer,ec: ExecutionContext) extends EssentialFilter {
  override def apply(next: EssentialAction) = EssentialAction { request =>
    Accumulator.flatten(Future { "Test" }.map { t =>
      next(request).map { result =>
        result.withHeaders("X-TestFilter" -> s"foo $t")
      }
    })
  }
}