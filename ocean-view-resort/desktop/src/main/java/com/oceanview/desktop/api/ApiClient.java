package com.oceanview.desktop.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.URLEncoder;
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
  HttpRequest req = HttpRequest.newBuilder()
      .uri(URI.create(BASE_URL + "/reservations/" + URLEncoder.encode(reservationNo, StandardCharsets.UTF_8)))
      .header("Authorization", "Bearer " + authToken)
      .GET()
      .build();

  HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());

  String body = res.body() == null ? "" : res.body();

  if (res.statusCode() >= 200 && res.statusCode() < 300) {
    return mapper.readValue(body, ReservationResponse.class);
  }

  // ---- SAFE error extraction ----
  String msg = "Request failed (" + res.statusCode() + ")";
  try {
    JsonNode node = mapper.readTree(body);

    // Our API format: {"error":"..."}
    if (node.hasNonNull("error")) msg = node.get("error").asText();

    // Spring default format: {"message":"..."} or sometimes {"error":"Not Found"}
    else if (node.hasNonNull("message")) msg = node.get("message").asText();
    else if (node.hasNonNull("error")) msg = node.get("error").asText();

    else msg = msg + " - " + body;
  } catch (Exception ignore) {
    msg = msg + " - " + body;
  }

  throw new RuntimeException(msg);
}

  /**
   * Generate bill
   */
  public static BillResponse generateBill(String reservationNo) throws Exception {
  HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create(BASE_URL + "/bills/" + URLEncoder.encode(reservationNo, StandardCharsets.UTF_8)))
      .header("Authorization", "Bearer " + authToken)
      .POST(HttpRequest.BodyPublishers.noBody())
      .build();

  HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
  String body = response.body() == null ? "" : response.body();

  // Accept both 200 and 201 as success
  if (response.statusCode() >= 200 && response.statusCode() < 300) {
    return mapper.readValue(body, BillResponse.class);
  }

  // Safe error extraction
  String msg = "Request failed (" + response.statusCode() + ")";
  try {
    JsonNode node = mapper.readTree(body);
    if (node.hasNonNull("error")) msg = node.get("error").asText();
    else if (node.hasNonNull("message")) msg = node.get("message").asText();
    else msg = msg + " - " + body;
  } catch (Exception ignore) {
    msg = msg + " - " + body;
  }

  throw new Exception(msg);
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

  String body = response.body() == null ? "" : response.body();

  if (response.statusCode() >= 200 && response.statusCode() < 300) {
    JsonNode node = mapper.readTree(body);

    // backend returns {"message": "..."}
    // but keep fallback in case you change backend later
    String help = node.path("message").asText(null);
    if (help == null) help = node.path("text").asText("");

    return help;
  }

  // Safe error message (no null .asText() crashes)
  String msg = "Failed to fetch help (" + response.statusCode() + ")";
  try {
    JsonNode node = mapper.readTree(body);
    if (node.hasNonNull("error")) msg = node.get("error").asText();
    else if (node.hasNonNull("message")) msg = node.get("message").asText();
    else msg = msg + " - " + body;
  } catch (Exception ignore) {
    msg = msg + " - " + body;
  }

  throw new Exception(msg);
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
