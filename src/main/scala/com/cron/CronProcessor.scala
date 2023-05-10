package com.cron

object CronProcessor {
  def main(args: Array[String]): Unit = {

    //to run in sbt change this to true and pass command as: "run */15::0::1,15::*::10::/usr/bin/find"
    val runWithSbt = false
    val separator = if(runWithSbt) "::" else " "
    validateInputArgs(args, separator)

    //unapply the input array for easy access
val Array(minute, hour, dayOfMonth, month, dayOfWeek, command) = args.head.split(separator)

    //expand each time
    val minuteStr = expand(cronTimeField = minute, inputLimit = 60)
    val hourStr = expand(cronTimeField = hour, inputLimit = 24)
    val dayOfMonthStr = expand(cronTimeField = dayOfMonth, inputLimit = 31)
    val monthStr = expand(cronTimeField = month, inputLimit = 12, isItMonthField = true)
    val dayOfWeekStr = expand(cronTimeField = dayOfWeek, inputLimit = 7)

    // construct the response as a table
    val response = s"""
      |minute        $minuteStr
      |hour          $hourStr
      |day of month  $dayOfMonthStr
      |month         $monthStr
      |day of week   $dayOfWeekStr
      |command       $command
      |""".stripMargin

    println(response)
  }

  def validateInputArgs(args: Array[String], separator: String = " "): Unit = {
    require(args.length == 1, "Invalid number of arguments, expected one")
    val cronFields = args.head.split(separator)
    require(cronFields.length == 6, "Invalid cron expression, expected: minute hour dayOfMonth month dayOfWeek command")
  }

  /**
   * expands the cron time field into a string
   * @param cronTimeField cron time field
   * @param inputLimit field expansion limit
   * @param isItMonthField is it a the month field
   * @return the expanded form of the cron time field
   */
  def expand(
                      cronTimeField: String,
                      inputLimit: Int,
                      isItMonthField: Boolean = false): String = {
    // range to be applied for time field
    val range = if(isItMonthField) 1 to inputLimit else 0 until inputLimit

    //inner function to process the time fields with cron increment(/)
    def processIncrements =
      cronTimeField.split("/") match {
        case Array("*", part2) =>
          if(isItMonthField) List.range(1, inputLimit, part2.toInt).mkString(" ") else
          List.range(0, inputLimit, part2.toInt).mkString(" ")
        case Array(part1, part2) => List.range(part1.toInt, inputLimit, part2.toInt).mkString(" ")
      }

    // process the cron field
    cronTimeField match {
      case "*" => range.mkString(" ")
      case field if field.split("-").length == 2 =>
        val Array(from, to) = field.split("-")
        (from.toInt to to.toInt).mkString(" ")
      case field if field.contains(",") => field.replaceAll(",", " ")
      case field if field.split("/").length == 2 => processIncrements
      case field if field.toIntOption.isDefined && (field.toInt <= inputLimit) => field
      case _ => "Invalid"
    }
  }
}