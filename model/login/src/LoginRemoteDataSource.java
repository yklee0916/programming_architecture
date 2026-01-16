package model.login.src;

import model.login.LoginErrorMessages;

public class LoginRemoteDataSource {
    private static final String API_LOGIN_URL = "https://api.example.com/login";
    private static final String SUCCESS_RESPONSE = "success";

    public UserVo login(String username, String password) throws Exception {
        // 실제 네트워크 요청 시뮬레이션 (기존 로직 단순화)
        // 여기서는 예제이므로 간단히 성공 처리하거나 예외 발생
        // 실제로는 HttpURLConnection 코드 등이 들어감
        Thread.sleep(1000);
        if ("error".equals(username)) {
            throw new Exception(LoginErrorMessages.LOGIN_ERROR);
        }
        return new UserVo(username);
    }
}
