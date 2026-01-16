# 5_mvp_pattern (MVP 패턴)

## 개요

이 브랜치는 MVP 패턴을 적용하여 View와 Presenter를 분리한 상태를 보여줍니다. `LoginActivity`는 View 구현체로 두고, UI 렌더링은 `LoginView`, 흐름 제어는 `LoginPresenter`로 분리했습니다. 로그인 유스케이스는 `LoginModel`, 사용자 저장은 `UserStorage`, 공통 상수는 `Constants`로 분리했습니다.

## 현재 코드 상태

### 파일 구조
- `view/LoginActivity.java`: View 구현체 (입력 전달, 화면 전환)
- `view/LoginView.java`: View 구성 요소 (UI 구성, 입력/표시)
- `presenter/LoginContract.java`: View/Presenter 계약
- `presenter/LoginPresenter.java`: Presenter (흐름 제어, 검증, Model 호출)
- `model/LoginModel.java`: Model (로그인 요청, 결과 판정)
- `model/UserStorage.java`: 사용자 저장소
- `model/Constants.java`: 공통 상수

### 코드 특징
- **View와 Presenter 분리**
  - View는 입력 전달/표시에 집중
  - Presenter가 흐름과 로직을 주도
- **View 인터페이스 도입**
  - Presenter가 View 구현에 직접 의존하지 않음
- **Presenter에서 Model 호출**
  - 로그인 기능은 `model/LoginModel`
- **상수 분리**
  - UI 문자열/검증 기준 등을 `Constants`로 분리
- **간결한 Presenter 흐름**
  - `setupUI()` → `setupLoginButton()` → `presenter.onLoginClicked()` 순서로 읽힘

## 4_mvc_pattern 대비 개선된 점

### 1. Presenter 도입
- **개선**: 흐름 제어와 검증을 `LoginPresenter`로 이동
- **효과**:
  - View는 수동 렌더링에 집중
  - 로직이 Presenter로 집중되어 테스트 범위가 명확해짐

### 2. 계약 기반 분리
- **개선**: `LoginContract`로 View/Presenter 계약 정의
- **효과**:
  - Presenter가 View 구현에 직접 의존하지 않음
  - 교체 가능성이 높아짐
  - 구조가 규칙을 강제하여 품질 편차를 줄임

### 3. Fake/Mock 가능성 향상
- **개선**: Presenter가 `LoginUseCase`, `UserStorage`, `LoginContract.View`에만 의존
- **효과**:
  - 테스트에서 Fake/Mock으로 쉽게 교체 가능
  - Presenter 단위 테스트 작성이 쉬워짐
  - Activity 없이도 로그인 흐름과 UI 반응을 검증 가능

### 4. 역할의 명확화
- **개선**: View/Presenter/Model 책임이 분리됨
- **효과**:
  - 흐름 제어와 UI 렌더링의 경계가 명확해짐

### 5. 상수 중앙화
- **개선**: 상수를 `Constants`로 분리
- **효과**:
  - 하드코딩이 줄고 변경 지점이 단일화됨
  - UI/검증 기준을 일괄 관리 가능

## 아직 남아있는 잔존 문제점 (아키텍처 관점)

### 1. Presenter의 비즈니스 집중
- **문제**: 입력 검증과 에러 메시지 결정이 Presenter에 집중됨
- **영향**:
  - Presenter가 비대해질 가능성
  - 추가 규칙이 늘면 분리 필요 (Validator/UseCase)

### 2. 데이터/도메인 레이어 부재
- **문제**: `LoginModel`과 `UserStorage`는 분리됐지만
  Repository/UseCase 같은 경계가 없음
- **영향**:
  - 변경 지점이 분산되어도 구조적 보호가 약함
  - 향후 레이어 확장이 필요

### 3. 동시성/수명주기 대응 부족
- **문제**: Thread 기반 비동기 처리, 취소/중복 요청 제어 없음
- **영향**:
  - 화면 종료 시 콜백 처리 위험
  - 다중 로그인 요청 제어가 어려움

### 4. 의존성 주입 부재
- **문제**: 네트워크 URL, 저장소 구현이 하드코딩됨
- **영향**:
  - 테스트 대체가 어렵고 확장에 취약

## 참고

이 코드는 MVP 분리 단계의 교육 목적 예제입니다. 실제 프로덕션에서는 Validation, Repository, UseCase, DI 등을 단계적으로 추가하는 것을 권장합니다.
