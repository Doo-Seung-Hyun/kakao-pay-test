# kakao-pay-test
워크플랫폼팀 개발자 지원 사전과제 전형
## 과제개요
고객이 문의하면 상담사가 접수-답변할 수 있는 응대-문의 시스템 구현
## 기술구성
    * Back-End
      Spring-Boot 2.6.8
      Spring-Security
      JPA(Hibernate)
      JAVA 11
    
    * DB
      H2 (In-Memory DB)
    
    * Front-End
      react 18.1.0
      react-router-dom 6.3.0


## 프로젝트 구조
   * 인증/데이터 처리와 관련된 API는 Back-End(Spring)로 구성
   * 사용자 UI 화면은 Front-End(react)로 구성
   * 사전과제유형임을 감안하여 DB는 간편한 In-Memory 선택

## 주요 구현이슈 및 방안
   * (인증) 로그인 인증은 Spring-Security를 적용하여 구현
   * (데이터관리) 프로젝트가 간결하고 직관적일 수 있도록 JPA(Hibernate) 적용
   * (상담 충돌) 상담 접수처리가 충돌될 경우를 대비하여, JPA의 Tranaciton을 기반으로 영속성 전이 전 데이터 존재유무 확인
      * UI의 10초 자동갱신 기능으로 충돌 발생 가능성 사전 예방
