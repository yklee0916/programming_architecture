# 7_clean_architecture_mvvm

## 개요

이 브랜치는 **Clean Architecture**와 **MVVM** 패턴을 결합한 구조입니다. 앱을 3개의 계층(Layer)으로 명확히 분리하여 의존성 규칙을 철저히 지킵니다.

## 아키텍처 구조 (Layer)

### 1. View Layer (`view`)
- **역할**: UI 렌더링 및 사용자 입력 처리
- **구성**: Activity, XML
- **의존성**: ViewModel Layer를 알고 있음

### 2. ViewModel Layer (`viewmodel`)
- **역할**: UI 상태 관리 및 비즈니스 로직 호출
- **구성**: ViewModel
- **의존성**: Model Layer를 알고 있음 (UseCase 호출)

### 3. Model Layer (`model`)
- **역할**: 비즈니스 로직 및 데이터 관리
- **구성**:
  - `UseCase`: 비즈니스 로직 수행 (`LoginUseCase`)
  - `Repository Interface`: 데이터 접근 추상화 (`LoginRepository`)
  - `RepositoryImpl`: Repository 인터페이스 구현체 (`LoginRepositoryImpl`)
  - `DataSource`: 서버(Remote)나 DB(Local) 접근 (`LoginRemoteDataSource`, `UserLocalDataSource`)
  - `Model`: 순수 데이터 모델 (`UserDto`, `UserVo`, `UserDo`)
- **특징**: Domain 부분은 안드로이드 의존성이 전혀 없음 (Pure Java/Kotlin)

## 의존성 규칙 (Dependency Rule)
- **방향**: View → ViewModel → Model
- **핵심**: Model Layer 내부에서 UseCase는 Repository Interface에만 의존하고, 구현체는 Data Source를 조합하여 제공합니다.

## 파일 구조

```
├── view/                 (View Layer - UI)
│   └── LoginActivity.java
├── viewmodel/            (ViewModel Layer - 상태 관리)
│   ├── LoginViewModel.java
│   ├── NavigationViewModel.java
│   └── ViewModelFactory.java
├── model/                (Model Layer - 비즈니스 로직 및 데이터)
│   └── login/
│       ├── LoginUseCase.java              (UseCase)
│       ├── LoginRepository.java           (Repository Interface)
│       ├── UserDto.java                   (Domain Model)
│       ├── LoginException.java
│       ├── LoginErrorMessages.java
│       └── src/                            (구현체)
│           ├── LoginRepositoryImpl.java
│           ├── LoginRemoteDataSource.java
│           ├── UserLocalDataSource.java
│           ├── UserVo.java                (Data Model - API)
│           └── UserDo.java                (Data Model - Local)
├── tests/                (테스트 코드)
│   ├── mock/                            (Mock 객체)
│   │   └── FakeLoginUseCase.java
│   ├── model/                           (Model Layer 테스트)
│   │   └── login/
│   │       ├── LoginUseCaseTest.java
│   │       ├── LoginExceptionTest.java
│   │       ├── UserDtoTest.java
│   │       └── src/
│   │           ├── LoginRepositoryImplTest.java
│   │           └── UserVoTest.java
│   └── viewmodel/                       (ViewModel Layer 테스트)
│       └── LoginViewModelTest.java
└── res/                  (리소스)
```

## 6_mvvm_pattern 대비 개선된 점

### 1. Repository 인터페이스와 구현체 분리 (의존성 역전 원칙)

**이전 (6_mvvm_pattern):**
- `UserStorage` 인터페이스는 있었지만, Repository 패턴이 없음
- `LoginModel`이 `HttpURLConnection`을 직접 사용하여 네트워크 로직이 UseCase 구현체에 포함
- `LoginUseCase`와 `UserStorage`가 분리되어 있어 데이터 흐름이 복잡
- Remote/Local 데이터 소스 구분이 없음

**개선 (7_clean_architecture_mvvm):**
- `LoginRepository` 인터페이스를 Model Layer에 정의
- `LoginRepositoryImpl`이 Model Layer에서 구현
- UseCase는 인터페이스에만 의존하므로 구현체 변경에 영향받지 않음
- **장점**: 데이터 소스 교체 시 (Room → Realm, REST → GraphQL) View/ViewModel Layer 코드 수정 불필요

```java
// Model Layer: 인터페이스 정의
public interface LoginRepository {
    UserDto login(String username, String password) throws LoginException;
    void saveUser(UserDto user);
}

// Model Layer: 구현체 제공
public class LoginRepositoryImpl implements LoginRepository {
    // Remote/Local DataSource 조합하여 구현
}
```

### 2. Domain 모델과 Data 모델 분리

**이전 (6_mvvm_pattern):**
- `String` 타입으로 사용자 정보만 전달 (`onSuccess(String username)`)
- 도메인 모델이 없어 비즈니스 로직에서 사용할 수 있는 구조화된 데이터가 없음
- API 응답 구조나 DB 스키마 변경 시 전체 코드 영향

**개선 (7_clean_architecture_mvvm):**
- **Domain 모델**: `UserDto` - 비즈니스 로직에서 사용하는 순수한 모델
- **Data 모델**: 
  - `UserVo` (Value Object) - API 응답용
  - `UserDo` (Data Object) - 로컬 저장용
- Repository에서 Data 모델을 Domain 모델로 변환
- **장점**: API/DB 구조 변경이 View/ViewModel Layer에 영향 없음, 각 레이어의 책임 명확화

```java
// Repository에서 변환 처리
UserVo userVo = remoteDataSource.login(username, password);
return new UserDto(userVo.getUsername());  // Vo → Dto 변환
```

### 3. NavigationViewModel 개선 - 재사용 가능한 구조

**이전 (6_mvvm_pattern):**
- `LoginActivity`에 `navigateToMain()` 메서드가 직접 구현되어 있음
- Navigation 로직이 Activity에 하드코딩되어 재사용 불가능
- 다른 화면으로 이동하려면 Activity마다 메서드를 추가해야 함

**개선 (7_clean_architecture_mvvm):**
- `navigate(Class<? extends Activity> targetActivity)` - 범용 메서드
- 생성자에서 target을 받지 않음
- 호출 시마다 다른 target 지정 가능
- **장점**: 하나의 NavigationViewModel로 여러 화면 전환 가능, 재사용성 극대화

```java
// 재사용 가능한 구조
navigationViewModel.navigate(MainActivity.class);
navigationViewModel.navigate(ProfileActivity.class);
```

### 4. ViewModelFactory를 통한 의존성 주입

**이전 (6_mvvm_pattern):**
- `LoginActivity`에서 직접 의존성 생성 (`new LoginModel()`, `new SharedPreferencesUserStorage()`)
- ViewModel 생성 시 `LoginUseCase`와 `UserStorage` 둘 다 주입 필요
- 테스트 시 Mock 주입을 위해 Activity 코드 수정 필요

**개선 (7_clean_architecture_mvvm):**
- `ViewModelFactory`가 모든 의존성을 생성하고 주입
- DataSource → Repository → UseCase → ViewModel 순서로 의존성 체인 구성
- **장점**: 테스트 시 Factory를 교체하여 Mock 객체 주입 가능, 의존성 관리 중앙화

```java
public static LoginViewModel createLoginViewModel(Context context) {
    UserLocalDataSource localDataSource = new UserLocalDataSource(context);
    LoginRemoteDataSource remoteDataSource = new LoginRemoteDataSource();
    LoginRepository repository = new LoginRepositoryImpl(remoteDataSource, localDataSource);
    LoginUseCase loginUseCase = new LoginUseCase(repository);
    return new LoginViewModel(loginUseCase);
}
```

### 5. 비동기 처리 개선

**이전 (6_mvvm_pattern):**
- `LoginModel` 내부에서 `new Thread()` 직접 생성하여 사용
- Executor 지원 없음
- 테스트 시 비동기 처리 제어 어려움

**개선 (7_clean_architecture_mvvm):**
- `LoginUseCase`에서 `Executor` 지원 (선택적)
- Executor가 제공되면 사용, 없으면 Thread로 처리
- **장점**: 테스트 시 동기 Executor 주입 가능, 프로덕션에서는 비동기 Executor 사용 가능

```java
public LoginUseCase(LoginRepository repository, Executor executor) {
    this.repository = repository;
    this.executor = Optional.ofNullable(executor);
}

if (executor.isPresent()) {
    executor.get().execute(loginTask);
} else {
    Thread loginThread = new Thread(loginTask, "LoginUseCase-Thread");
    loginThread.start();
}
```

### 6. 에러 처리 체계화

**이전 (6_mvvm_pattern):**
- 에러 메시지가 `LoginModel`에 하드코딩 (`ERROR_LOGIN_FAILED`, `ERROR_NETWORK`)
- `LoginException` 같은 도메인 예외 클래스 없음
- 에러 메시지가 `Constants`에 일부 있으나 UseCase 구현체에도 분산

**개선 (7_clean_architecture_mvvm):**
- `LoginException`: 도메인 예외 클래스 (체인 가능)
- `LoginErrorMessages`: 에러 메시지 상수화
- **장점**: 에러 메시지 중앙 관리, 예외 체인으로 원인 추적 가능, 타입 안정성 향상

```java
// 도메인 예외
public class LoginException extends Exception {
    public LoginException(String message, Throwable cause) {
        super(message, cause);  // 원인 예외 체인
    }
}

// 에러 메시지 상수화
public class LoginErrorMessages {
    public static final String USERNAME_EMPTY = "사용자 이름을 입력해주세요";
    public static final String PASSWORD_TOO_SHORT = "비밀번호는 4자리 이상이어야 합니다";
}
```

### 7. 입력 검증 로직 UseCase로 이동

**이전 (6_mvvm_pattern):**
- 입력 검증이 `LoginModel`에 `validateInput()` 메서드로 구현되어 있음
- 검증 로직이 UseCase 구현체에 포함되어 있어 인터페이스로 추상화되지 않음

**개선 (7_clean_architecture_mvvm):**
- `LoginUseCase.validateInput()`에서 모든 검증 로직 처리
- **장점**: 비즈니스 규칙이 한 곳에 집중, 다른 View/ViewModel Layer에서도 동일한 검증 로직 재사용 가능

### 8. 프레임워크 독립성 확보

**이전 (6_mvvm_pattern):**
- `LoginModel`이 `HttpURLConnection` 등 Java 표준 라이브러리 사용 (Android 의존성은 없음)
- 하지만 `LoginUseCase` 인터페이스와 구현체가 같은 레이어에 있어 분리되지 않음
- `SharedPreferencesUserStorage`가 Android Context에 의존

**개선 (7_clean_architecture_mvvm):**
- Model Layer의 Domain 부분 (`LoginUseCase`, `LoginRepository`, `UserDto`)는 순수 Java
- Android 의존성 전혀 없음
- **장점**: 
  - 프레임워크 변경 시 비즈니스 로직 안전
  - 에뮬레이터 없이 초고속 단위 테스트 가능
  - 다른 플랫폼(서버, 데스크톱)에서도 재사용 가능

### 9. 테스트 용이성 극대화

**이전 (6_mvvm_pattern):**
- `LoginActivity`에서 직접 의존성 생성하여 테스트 시 Activity 코드 수정 필요
- Mock 객체는 `viewmodel/mock/`에 있으나, 주입을 위해 Activity 수정이 필요

**개선 (7_clean_architecture_mvvm):**
- Model Layer의 Domain 부분은 순수 Java로 JUnit으로 즉시 테스트 가능
- `tests/` 폴더에 체계적인 테스트 구조 구성
  - `tests/mock/`: Mock 객체 (`FakeLoginUseCase`)
  - `tests/model/`: Model Layer 단위 테스트
  - `tests/viewmodel/`: ViewModel Layer 테스트
- ViewModelFactory 교체로 테스트용 의존성 주입 용이
- **장점**: 빠른 단위 테스트, 높은 테스트 커버리지 달성 가능

**주요 테스트 커버리지:**
- `LoginUseCase`: 입력 검증, 로그인 성공/실패, Executor 처리
- `LoginRepositoryImpl`: Repository 구현체 동작 검증
- `LoginViewModel`: UI 상태 관리 및 UseCase 연동
- `UserDto`, `UserVo`, `LoginException`: 모델 및 예외 클래스 검증

## 종합 개선 효과

1. **유지보수성**: 비즈니스 로직이 UseCase에 집중되어 수정 범위 최소화
2. **확장성**: 새로운 기능 추가 시 기존 코드 영향 최소화
3. **테스트 용이성**: Model Layer 단위 테스트로 빠른 피드백
4. **재사용성**: UseCase와 Repository 인터페이스를 다른 View/ViewModel Layer에서도 활용 가능
5. **프레임워크 독립성**: 비즈니스 로직이 프레임워크 변경에 영향받지 않음
