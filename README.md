# 6_mvvm_pattern (MVVM + Data Binding)

## 개요

이 브랜치는 MVVM 패턴을 Data Binding 방식으로 적용한 상태를 보여줍니다. `LoginActivity`는 View 구현체로 두고, UI 렌더링은 XML + Data Binding으로, 상태/로직은 `LoginViewModel`로 이동했습니다. 로그인 유스케이스는 `LoginModel`, 사용자 저장은 `UserStorage`, 공통 상수는 `Constants`로 분리했습니다.

## MVVM 패턴 준수 사항
- View는 UI 렌더링과 입력 전달만 담당
- View는 Model을 직접 참조하지 않음
- ViewModel은 상태/로직을 보유하고 View를 직접 참조하지 않음
- 상태 변화는 Data Binding으로 View에 전달
- View 이벤트(예: onClick)는 Data Binding을 통해 ViewModel로 전달되어 처리
- Model은 비즈니스 로직/데이터 접근을 담당

## 현재 코드 상태

### 파일 구조
- `view/LoginActivity.java`: View 구현체 (입력 전달, 화면 전환)
- `res/layout/activity_login.xml`: UI 레이아웃 + Data Binding
- `res/values/strings.xml`: UI 문자열 리소스
- `viewmodel/LoginViewModel.java`: ViewModel (상태/로직/유스케이스 호출)
- `model/LoginModel.java`: 입력 검증 + 로그인 처리
- `model/UserStorage.java`: 사용자 저장소
- `model/Constants.java`: 공통 상수

## 5_mvp_pattern 대비 개선된 점 (MVVM의 장점)

### 1. View 의존성 완전 제거 (Decoupling)
- **개선**: MVP의 Presenter는 View 인터페이스를 참조해야 했지만, ViewModel은 View를 전혀 모름
- **효과**:
  - ViewModel이 View에 종속되지 않아 독립적인 유닛 테스트가 가능
  - 디자인이 변경되어도 ViewModel 코드를 수정할 필요가 없음

### 2. UI 갱신 코드(Glue Code) 삭제
- **개선**: Data Binding이 상태 변화를 감지해 UI를 자동으로 갱신
- **효과**:
  - `view.showError()`, `setText()` 같은 반복적이고 지루한 UI 제어 코드가 사라짐
  - 코드가 선언형(Declarative)으로 바뀌어 가독성 향상

### 3. 관계 확장성 (1:N 지원)
- **개선**: MVP의 1:1 강결합과 달리, ViewModel 하나를 여러 View가 구독(Observe)할 수 있음
- **효과**:
  - 하나의 ViewModel을 Activity, Fragment, CustomView 등에서 동시에 사용 가능
  - 구조 확장이 유연해짐

### 4. 테스트 패러다임 변화 (행위 → 상태)
- **개선**: `verify(view).method()` 같은 행위 검증 대신, `assert(viewModel.state)` 방식의 상태 검증 사용
- **효과**:
  - View Mocking이 필요 없어 테스트 작성이 훨씬 간단하고 직관적임
  - 테스트 코드가 UI 구현 상세에 의존하지 않게 됨

### 5. 개발 병렬성 증대
- **개선**: ViewModel(데이터/로직)과 XML(UI/디자인)이 명확히 분리됨
- **효과**:
  - 디자이너와 개발자가 동시에 작업하기 용이함
  - 로직이 완성되지 않아도 더미 데이터를 바인딩하여 UI 테스트 가능

## 아직 남아있는 잔존 문제점 (아키텍처 관점)

### 1. Data Binding 의존
- **문제**: Data Binding 환경 및 XML 규칙에 의존
- **영향**:
  - 레이아웃 변경 시 바인딩 규칙을 함께 관리해야 함
  - 빌드 환경이 Data Binding 설정을 필요로 함

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

이 코드는 MVVM 분리 단계의 교육 목적 예제입니다. 실제 프로덕션에서는 Validation, Repository, UseCase, DI 등을 단계적으로 추가하는 것을 권장합니다.
