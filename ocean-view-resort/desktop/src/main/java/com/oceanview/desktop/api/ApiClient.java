package com.oceanview.desktop.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class ApiClient {
  private static final String BASE_URL = "http://localhost:8080/api";
  private static final HttpClient httpClient = HttpClient.newHttpClient();
  private static final ObjectMapper mapper = new ObjectMapper();
  
  private static String authToken = null;

  /**
   * Login endpoint - returns token
   */
  public static String login(String username, String password) throws Exception {
    String json = mapper.writeValueAsString(new LoginRequest(username, password));
    
    HttpRequest request = HttpRequest.newBuilder()
        .uri(new URI(BASE_URL + "/auth/login"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    
    if (response.statusCode() != 200) {
      JsonNode errorNode = mapper.readTree(response.body());
      throw new Exception(errorNode.get("error").asText());
    }

    JsonNode responseNode = mapper.readTree(response.body());
    authToken = responseNode.get("token").asText();
    return authToken;
  }

  /**
   * Create reservation
   */
  public static String createReservation(String guestName, String address, 
                                         String contactNumber, String roomType,
                                         String checkIn, String checkOut) throws Exception {
    String json = mapper.writeValueAsString(new ReservationCreateRequest(
        guestName, address, contactNumber, roomType, checkIn, checkOut
    ));

    HttpRequest request = HttpRequest.newBuilder()
        .uri(new URI(BASE_URL + "/reservations"))
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + authToken)
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    
    if (response.statusCode() != 201) {
      JsonNode errorNode = mapper.readTree(response.body());
      throw new Exception(errorNode.get("error").asText());
    }

    JsonNode responseNode = mapper.readTree(response.body());
    return responseNode.get("reservationNo").asText();
  }

  /**
   * Get reservation details
   */
  public static ReservationResponse getReservation(String reservationNo) throws Exception {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(new URI(BASE_URL + "/reservations/" + reservationNo))
        .header("Authorization", "Bearer " + authToken)
        .GET()
        .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    
    if (response.statusCode() != 200) {
      JsonNode errorNode = mapper.readTree(response.body());
      throw new Exception(errorNode.get("error").asText());
    }

    return mapper.readValue(response.body(), ReservationResponse.class);
  }

  /**
   * Generate bill
   */
  public static BillResponse generateBill(String reservationNo) throws Exception {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(new URI(BASE_URL + "/bills/" + reservationNo))
        .header("Authorization", "Bearer " + authToken)
        .POST(HttpRequest.BodyPublishers.noBody())
        .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    
    if (response.statusCode() != 201) {
      JsonNode errorNode = mapper.readTree(response.body());
      throw new Exception(errorNode.get("error").asText());
    }

    return mapper.readValue(response.body(), BillResponse.class);
  }

  /**
   * Get help text
   */
  public static String getHelp() throws Exception {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(new URI(BASE_URL + "/help"))
        .GET()
        .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    
    if (response.statusCode() != 200) {
      throw new Exception("Failed to fetch help");
    }

    JsonNode responseNode = mapper.readTree(response.body());
    return responseNode.get("text").asText();
  }

  public static boolean isAuthenticated() {
    return authToken != null;
  }

  public static void logout() {
    authToken = null;
  }

  // Inner DTOs
  public static class LoginRequest {
    public String username;
    public String password;

    public LoginRequest(String username, String password) {
      this.username = username;
      this.password = password;
    }
  }

  public static class ReservationCreateRequest {
    public String guestName;
    public String address;
    public String contactNumber;
    public String roomType;
    public String checkIn;
    public String checkOut;

    public ReservationCreateRequest(String guestName, String address, String contactNumber,
                                     String roomType, String checkIn, String checkOut) {
      this.guestName = guestName;
      this.address = address;
      this.contactNumber = contactNumber;
      this.roomType = roomType;
      this.checkIn = checkIn;
      this.checkOut = checkOut;
    }
  }

  public static class ReservationResponse {
    public String reservationNo;
    public String guestName;
    public String address;
    public String contactNumber;
    public String roomType;
    public String checkIn;
    public String checkOut;
  }

  public static class BillResponse {
    public String reservationNo;
    public int nights;
    public double ratePerNight;
    public double totalAmount;
  }
}
