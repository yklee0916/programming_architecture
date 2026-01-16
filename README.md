# 3_mv_pattern (MV 패턴)

## 개요

이 브랜치는 MV 패턴을 적용하여 View와 Model을 물리적으로 분리한 상태를 보여줍니다. `LoginActivity`는 View 역할로 두고, 로그인 유스케이스는 `LoginModel`에 두었습니다. 사용자 저장은 `UserStorage`, 공통 상수는 `Constants`로 분리했습니다.

## 현재 코드 상태

### 파일 구조
- `view/LoginActivity.java`: View (UI 구성, 입력 처리, 화면 전환)
- `model/LoginModel.java`: Model (로그인 요청, 결과 판정)
- `model/UserStorage.java`: 사용자 저장소
- `model/Constants.java`: 공통 상수

### 코드 특징
- **View와 Model 분리**
  - UI 로직은 `view/LoginActivity`
  - 로그인 기능은 `model/LoginModel`
- **저장소 분리**
  - 사용자 저장은 `model/UserStorage`
- **상수 분리**
  - UI 문자열/검증 기준 등을 `Constants`로 분리
- **간결한 View 흐름**
  - `setupUI()` → `setupLoginButton()` → `performLogin()` 순서로 읽힘

## 2_procedural_separation 대비 개선된 점

### 1. View/Model 분리
- **개선**: 로그인 유스케이스를 `LoginModel`로 이동
- **효과**:
  - View는 입력/표시에 집중
  - 로그인 로직 변경 영향이 View에 덜 전파됨

### 2. 파일 구조의 명확화
- **개선**: `view/`, `model/` 폴더로 분리
- **효과**:
  - 아키텍처 관점에서 레이어 경계가 보이기 시작함
  - 신규 기능 추가 시 파일 위치가 명확해짐

### 3. 저장소 분리
- **개선**: 사용자 저장을 `UserStorage`로 분리
- **효과**:
  - 저장 책임이 분리되어 View와 Model이 가벼워짐
  - 저장 방식 교체 지점이 명확해짐

### 4. 상수 중앙화
- **개선**: 상수를 `Constants`로 분리
- **효과**:
  - 하드코딩이 줄고 변경 지점이 단일화됨
  - UI/검증 기준을 일괄 관리 가능

## 아직 남아있는 잔존 문제점 (아키텍처 관점)

### 1. View에 남아있는 비즈니스 규칙
- **문제**: 입력 검증(`validateInput`)과 에러 메시지 결정이 View에 남아 있음
- **영향**:
  - View가 도메인 규칙 일부를 포함하게 됨
  - 검증 로직 테스트가 UI 레이어에 묶임

### 2. 데이터/도메인 레이어 부재
- **문제**: 네트워크는 `LoginModel`, 저장은 `UserStorage`로 분리됐지만
  레이어 경계(Repository/UseCase)와 인터페이스가 없음
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

이 코드는 MV 분리 단계의 교육 목적 예제입니다. 실제 프로덕션에서는 Validation, Repository, UseCase, DI 등을 단계적으로 추가하는 것을 권장합니다.
