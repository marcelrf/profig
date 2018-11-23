package spec

import io.circe.Json
import org.scalatest.{Matchers, WordSpec}
import profig.ProfigLookupPath

class ProfigLookupPathSpec extends WordSpec with Matchers {
  "ProfigLookupPath" should {
    "extract property name before first equal sign" in {
      val testProp = "prop_name = prop_value=1"
      val expectedJson = Json.fromFields(List(
        ("prop_name", Json.fromString("prop_value=1"))
      ))

      val parsedProp = ProfigLookupPath.propertiesString2Json(testProp)
      parsedProp should be(expectedJson)
    }
    "extract property name before first colon sign" in {
      val testProp = "prop_name: prop_value:1"
      val expectedJson = Json.fromFields(List(
        ("prop_name", Json.fromString("prop_value:1"))
      ))

      val parsedProp = ProfigLookupPath.propertiesString2Json(testProp)
      parsedProp should be(expectedJson)

    }
  }
}