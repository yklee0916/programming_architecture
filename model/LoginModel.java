package model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginModel {
  private static final String API_LOGIN_URL = "https://api.example.com/login";
  private static final String SUCCESS_RESPONSE = "success";
  private static final String ERROR_LOGIN_FAILED = "로그인 실패";
  private static final String ERROR_NETWORK = "네트워크 오류";

  public interface LoginCallback {
    void onSuccess(String username);
    void onFailure(String errorMessage);
  }

  public LoginModel() {
  }

  public void login(String username, String password, LoginCallback callback) {
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          String response = sendLoginRequest(username, password);
          if (response.contains(SUCCESS_RESPONSE)) {
            callback.onSuccess(username);
          } else {
            callback.onFailure(ERROR_LOGIN_FAILED);
          }
        } catch (Exception e) {
          callback.onFailure(ERROR_NETWORK);
        }
      }
    }).start();
  }

  private String sendLoginRequest(String username, String password) throws Exception {
    HttpURLConnection conn = null;
    OutputStream outputStream = null;
    try {
      URL url = new URL(API_LOGIN_URL);
      conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      String jsonData = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
      outputStream = conn.getOutputStream();
      outputStream.write(jsonData.getBytes());
      outputStream.flush();
      String response;
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
        response = reader.readLine();
      }
      return response;
    } finally {
      if (outputStream != null) {
        try {
          outputStream.close();
        } catch (Exception e) {
        }
      }
      if (conn != null) {
        conn.disconnect();
      }
    }
  }

}
