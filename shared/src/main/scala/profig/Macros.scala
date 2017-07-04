package profig

import scala.language.experimental.macros
import scala.reflect.macros.whitebox

object Macros {
  def as[T](c: whitebox.Context)(path: c.Expr[String])(implicit t: c.WeakTypeTag[T]): c.Expr[T] = {
    import c.universe._

    val config = c.prefix.tree
    c.Expr[T](
      q"""
         import io.circe._
         import io.circe.generic.extras.Configuration
         import io.circe.generic.extras.auto._
         implicit val customConfig: Configuration = Configuration.default.withSnakeCaseKeys.withDefaults

         val json = $config($path)
         implicit val decoder = implicitly[Decoder[$t]]
         decoder.decodeJson(json) match {
           case Left(failure) => throw new RuntimeException(s"Failed to decoder from $$json", failure)
           case Right(value) => value
         }
       """)
  }

  def store[T](c: whitebox.Context)(value: c.Expr[T], path: c.Expr[String])(implicit t: c.WeakTypeTag[T]): c.Expr[Unit] = {
    import c.universe._

    val config = c.prefix.tree
    c.Expr[Unit](
      q"""
         import io.circe._
         import io.circe.generic.extras.Configuration
         import io.circe.generic.extras.auto._
         implicit val customConfig: Configuration = Configuration.default.withSnakeCaseKeys.withDefaults

         val encoder = implicitly[Encoder[$t]]
         val json = encoder($value)
         $config.merge(json, $path)
       """)
  }
}
