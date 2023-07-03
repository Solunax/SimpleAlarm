# SimpleAlarm

## 기능
- 사용자가 원하는 시간에 알람을 맞출 수 있음
- 현재 시간보다 이전 시간을 입력하면, 알람을 다음날의 선택한 시간에 울리게 설정함

## 목적
- Alarm Manager, Notification, Broadcast Receiver사용시 필요한 항목 학습
    - Alarm Manager
        - 정시에 실행해야 하는 작업이 있을 경우 사용함
    - Notification
        - 사용자에게 알림을 띄워주기 위해서 주로 사용
        - 오레오 버전 이상부터는 Notification Channel을 생성해야 함
    - Broadcast Receiver
        - 특정 상황이 되었을때 해당 상황을 트리거삼아서 원하는 동작을 수행할 수 있게끔 함
        - ex) 휴대폰 충전, 화면 켜짐/꺼짐 등등

## 사용 라이브러리
- room, activity, lifecycle, hilt
    - room 
        - 알람 데이터 저장을 위해서 사용함
    - activity, lifecycle
        - ViewModel을 구성하기 위해서 사용함
    - hilt
        - 의존성 주입(DI)를 위해서 사용함

## 구동 사진
- 최초 실행화면

![1](https://user-images.githubusercontent.com/97011241/217176985-00419c9e-90f2-41eb-96a0-1e270701f544.png)

<br>

- 시간 설정화면

![2](https://user-images.githubusercontent.com/97011241/217176995-d7837e43-7063-48d4-9f39-e1e42b89b61c.png)

<br>

- 알람 동작화면

![3](https://user-images.githubusercontent.com/97011241/217176998-ab40f171-4ed6-4991-a8f1-2571cbb4c9ed.png)


## 추가 변경 사항
- 다수의 알람을 동시에 등록할 수 있게 수정함, 등록된 알람 목록은 Recycler View를 통해 표시(내부 DB에 저장된 알람)
- Recycler View에 알람 데이터가 시간순으로 정렬되어 표시되게 함
- Hilt 라이브러리 적용
- 알람 데이터 정렬을 data observe 에서 하는게 아닌 DAO 에서 ORDER BY로 정렬하게 수정
- 알람 활성화 후 데이터 추가/삭제시 Recycler View 의 스위치 상태가 이상한 오류 수정 
- Hilt 적용시 불필요한 코드 제거
- Repository가 Interface를 구현하도록 수정
- View Model이 Interface 타입을 주입받도록 수정(Interface를 주입할 때 Module을 만들어서 인터페이스와 구현 클래스를 연결해야 함(@Module, @Binds, 추상클래스))
- 알람이 울리면 삭제되는게 아닌 비활성화 되도록 변경, 알람 활성/비활성화시 로직 수정
- 코드 일부 주석 추가, 알람 설정시 현재 시간을 초기값으로 가지게 수정
- 세계 시간 확인 기능 추가, 기능 추가에 따른 알람 Activity -> Fragment 수정
- 사용자가 보기로 지정한 세계 시간을 Shared Preference에 저장하는 기능 추가
- 세계 시간 로딩방법 변경(values/array), 세계 시간 추가시 when 구문 수정
