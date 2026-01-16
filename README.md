# 6_mvvm_pattern (MVVM + Observer 패턴)

## 개요

이 브랜치는 MVVM 패턴을 Observer 방식으로 적용한 상태를 보여줍니다. `LoginActivity`는 View 구현체로 두고, UI 렌더링은 `LoginView`, 상태/로직은 `LoginViewModel`로 이동했습니다. 로그인 유스케이스는 `LoginModel`, 사용자 저장은 `UserStorage`, 공통 상수는 `Constants`로 분리했습니다.

## MVVM 패턴 준수 사항
- View는 UI 렌더링과 입력 전달만 담당
- ViewModel은 상태/로직을 보유하고 View를 직접 참조하지 않음
- 상태 변화는 Observer(또는 Data Binding)로 View에 전달
- View 이벤트(예: onClick)는 ViewModel로 전달되어 처리
- Model은 비즈니스 로직/데이터 접근을 담당

## 현재 코드 상태

### 파일 구조
- `view/LoginActivity.java`: View 구현체 (입력 전달, 화면 전환)
- `view/LoginView.java`: View 구성 요소 (UI 구성, 입력/표시)
- `view/LoginDataBinding.java`: View-ViewModel 바인딩
- `viewmodel/LoginViewModel.java`: ViewModel (상태/로직/유스케이스 호출)
- `viewmodel/ObservableValue.java`: Observer 유틸
- `model/LoginModel.java`: Model (로그인 요청, 결과 판정)
- `model/UserStorage.java`: 사용자 저장소
- `model/Constants.java`: 공통 상수

### 코드 특징
- **View와 ViewModel 분리**
  - View는 입력 전달/표시에 집중
  - ViewModel이 상태와 로직을 주도
- **Observer 기반 바인딩**
  - `ObservableValue`로 상태 변경을 전달
- **수동 Data Binding 계층**
  - `LoginDataBinding`에서 상태-UI 연결을 담당
- **ViewModel에서 Model 호출**
  - 로그인 기능은 `model/LoginModel`
- **상수 분리**
  - UI 문자열/검증 기준 등을 `Constants`로 분리
- **간결한 View 흐름**
  - `setupUI()` → `bindViewModel()` → `viewModel.onLoginClicked()` 순서로 읽힘

## 5_mvp_pattern 대비 개선된 점

### 1. ViewModel 중심 구조
- **개선**: 흐름 제어와 검증을 `LoginViewModel`로 이동
- **효과**:
  - View는 수동 렌더링에 집중
  - 상태/로직이 ViewModel에 집중됨

### 2. Observer 기반 상태 전달
- **개선**: `ObservableValue`로 상태 변화를 전달
- **효과**:
  - View가 상태를 구독하는 구조가 명확해짐
  - 상태 흐름이 명시적으로 드러남

### 3. 수동 Data Binding 도입
- **개선**: 바인딩 코드를 `LoginDataBinding`으로 분리
- **효과**:
  - View가 상태 구독 로직에서 분리됨
  - 바인딩 책임이 한 곳에 모임

### 4. 테스트 구조 단순화
- **개선**: ViewModel이 `LoginUseCase`, `UserStorage`에만 의존
- **효과**:
  - Fake/Mock 주입이 간단해짐
  - Activity 없이도 로그인 흐름과 UI 반응을 검증 가능

### 5. 역할의 명확화
- **개선**: View/ViewModel/Model 책임이 분리됨
- **효과**:
  - 상태 관리와 UI 렌더링의 경계가 명확해짐

## 아직 남아있는 잔존 문제점 (아키텍처 관점)

### 1. ViewModel의 비즈니스 집중
- **문제**: 입력 검증과 에러 메시지 결정이 ViewModel에 집중됨
- **영향**:
  - ViewModel이 비대해질 가능성
  - 추가 규칙이 늘면 분리 필요 (Validator/UseCase)

### 2. 수동 바인딩 보일러플레이트
- **문제**: `LoginDataBinding`을 수동으로 구성/해제해야 함
- **영향**:
  - 바인딩 코드가 View에 누적됨
  - 데이터 바인딩 도입 전까지 중복이 발생

### 3. 데이터/도메인 레이어 부재
- **문제**: `LoginModel`과 `UserStorage`는 분리됐지만
  Repository/UseCase 같은 경계가 없음
- **영향**:
  - 변경 지점이 분산되어도 구조적 보호가 약함
  - 향후 레이어 확장이 필요

### 4. 동시성/수명주기 대응 부족
- **문제**: Thread 기반 비동기 처리, 취소/중복 요청 제어 없음
- **영향**:
  - 화면 종료 시 콜백 처리 위험
  - 다중 로그인 요청 제어가 어려움

### 5. 의존성 주입 부재
- **문제**: 네트워크 URL, 저장소 구현이 하드코딩됨
- **영향**:
  - 테스트 대체가 어렵고 확장에 취약

## 참고

이 코드는 MVVM 분리 단계의 교육 목적 예제입니다. 실제 프로덕션에서는 Validation, Repository, UseCase, DI 등을 단계적으로 추가하는 것을 권장합니다.
