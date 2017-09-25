package com.learn.orc;

import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class Validator {

  public boolean validateIsRandom(String value) {

    if (value.equalsIgnoreCase("TRUE") || value.equalsIgnoreCase("FALSE")) {
      return true;
    }
    return false;
  }

  public boolean validateMacID(String value) {
    // validate if mac id contains either - or :.
    String pattern = "/^(?:[[:xdigit:]]{2}([-:]))(?:[[:xdigit:]]{2}\1){4}[[:xdigit:]]{2}$/";
    return Pattern.matches(pattern, value);
  }

  public boolean validateUUID(String value) {
    String pattern =
        "/^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$/";
    return Pattern.matches(pattern, value);
  }

  public boolean validateClientType(String value) {
    if (value.equalsIgnoreCase("WIFI") || value.equalsIgnoreCase("BLE")
        || value.equalsIgnoreCase("BLE_ASSET")) {
      return true;
    }
    return false;
  }

  public boolean validateTrigger(String value) {
    if (value.equalsIgnoreCase("in") || value.equalsIgnoreCase("out")
        || value.equalsIgnoreCase("still-in")) {
      return true;
    }
    return false;
  }

  public boolean validateLevel(String value) {
    if (value.equalsIgnoreCase("zone") || value.equalsIgnoreCase("site")
        || value.equalsIgnoreCase("map") || value.equalsIgnoreCase("org")) {
      return true;
    }
    return false;
  }

  public long validateTimeStamp(String value) {
    Calendar cal = Calendar.getInstance();
    try {
      OffsetDateTime dt = OffsetDateTime.parse(value);

      cal.setTimeZone(TimeZone.getTimeZone("GMT"));
      cal.set(dt.getYear(), dt.getMonthValue() - 1, dt.getDayOfMonth(), dt.getHour(),
          dt.getMinute(), dt.getSecond());
      cal.set(Calendar.MILLISECOND, (dt.getNano() / 1000000));
    } catch (Exception e) {
      return 0;
    }
    return cal.getTimeInMillis();
  }
  
  
}
