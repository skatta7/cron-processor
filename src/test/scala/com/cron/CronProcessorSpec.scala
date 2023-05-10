package com.cron

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CronProcessorSpec extends AnyFlatSpec with Matchers{

  "validateInputArgs" should
    "validate without any errors" in {
    CronProcessor.validateInputArgs(Array("*/15 0 1,15 * 1-5 /usr/bin/find"))
  }
  it should "throw an exception for invalid no of arguments" in {
    assertThrows[IllegalArgumentException] {
      CronProcessor.validateInputArgs(Array("*/15 0 1,15 * 1-5 /usr/bin/find extra_value"))
    }
  }
  it should "throw an exception for invalid cron expression" in {
    assertThrows[IllegalArgumentException] {
      CronProcessor.validateInputArgs(Array("*/15 0 1,15 * 1-5 /usr/bin/find", "extra_value"))
    }
  }
  "expand" should
    "return minute field to valid expansion string" in {
    val result = CronProcessor.expand("*/15", 60)
    result shouldEqual "0 15 30 45"
  }
    it should "return string 'Invalid' for invalid minute field" in {
      val result = CronProcessor.expand("***", 60)
      result shouldEqual "Invalid"
    }
  it should "return hour field to valid expansion string" in {
    val result = CronProcessor.expand("0", 24)
    result shouldEqual "0"
  }
  it should "return string 'Invalid' for invalid hour field" in {
    val result = CronProcessor.expand("25", 24)
    result shouldEqual "Invalid"
  }
  it should "return day of month field to valid expansion string" in {
    val result = CronProcessor.expand("1,15", 31)
    result shouldEqual "1 15"
  }
  it should "return string 'Invalid' for invalid day of month field" in {
    val result = CronProcessor.expand("25xx", 31)
    result shouldEqual "Invalid"
  }
  it should "return month field to valid expansion string" in {
    val result = CronProcessor.expand("*", 12, true)
    result shouldEqual "1 2 3 4 5 6 7 8 9 10 11 12"
  }
  it should "return string 'Invalid' for invalid month field" in {
    val result = CronProcessor.expand("xx", 12, true)
    result shouldEqual "Invalid"
  }
  it should "return day of week field to valid expansion string" in {
    val result = CronProcessor.expand("1-5", 7)
    result shouldEqual "1 2 3 4 5"
  }
  it should "return string 'Invalid' for invalid day of week field" in {
    val result = CronProcessor.expand("9", 7)
    result shouldEqual "Invalid"
  }

  }

