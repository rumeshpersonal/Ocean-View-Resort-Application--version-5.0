package com.oceanview.desktop.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class Validator {
  private static final Pattern CONTACT_PATTERN = Pattern.compile("^(0\\d{9}|\\+94\\d{9})$");

  /**
   * Validate contact number: 0771234567 or +94771234567
   */
  public static boolean isValidContact(String contact) {
    return contact != null && CONTACT_PATTERN.matcher(contact).matches();
  }

  /**
   * Validate check-out is after check-in
   */
  public static boolean isValidDateRange(String checkInStr, String checkOutStr) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      LocalDate checkIn = LocalDate.parse(checkInStr, formatter);
      LocalDate checkOut = LocalDate.parse(checkOutStr, formatter);
      return checkOut.isAfter(checkIn);
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Validate room type
   */
  public static boolean isValidRoomType(String roomType) {
    return roomType != null && (roomType.equals("STANDARD") || roomType.equals("DELUXE") || roomType.equals("SUITE"));
  }

  /**
   * Validate required field not empty
   */
  public static boolean isNotEmpty(String value) {
    return value != null && !value.trim().isEmpty();
  }

  /**
   * Calculate nights between two dates
   */
  public static int calculateNights(String checkInStr, String checkOutStr) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      LocalDate checkIn = LocalDate.parse(checkInStr, formatter);
      LocalDate checkOut = LocalDate.parse(checkOutStr, formatter);
      return (int) java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
    } catch (Exception e) {
      return 0;
    }
  }
}
