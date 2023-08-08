# SimpleAlarm

## 기능
- 사용자가 원하는 시간에 알람을 맞출 수 있음
- 현재 시간보다 이전 시간을 입력하면, 알람을 다음날의 선택한 시간에 울리게 설정함
- 사용자가 원하는 다른 국가를 선택하여, 해당 국가의 현재 시간을 알 수 있음
- 간단한 스톱워치 기능

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
### 최초 실행화면

![m1](https://github.com/Solunax/SimpleAlarm/assets/97011241/135b0ee2-61e0-453c-93e1-132d06d39d3e)

    - Tab Layout 및 View Pager로 activity를 구성하여 스와이프 혹은 tab 클릭으로 화면을 전환할 수 있음
  
<br>

### 알람 설정 및 동작

![a1](https://github.com/Solunax/SimpleAlarm/assets/97011241/029f3742-6d33-403d-8452-16949fa9b995)

    - 하단의 알람추가 버튼을 통해 원하는 시각에 알람이 울리도록 설정할 수 있음

![a3](https://github.com/Solunax/SimpleAlarm/assets/97011241/cb3a47d2-bc0f-409a-842d-b0356e6f63b5)

    - 추가를 완료하면 최초에 비활성화 상태로 알람이 추가됨
    - 시간 우측 switch로 활성화 및 비활성화 가능, 쓰레기통 버튼을 사용해 알람 삭제 가능

![a4](https://github.com/Solunax/SimpleAlarm/assets/97011241/b49acec8-f63b-4822-a0f6-0824f1bc5f1d)
![a5](https://github.com/Solunax/SimpleAlarm/assets/97011241/06b7756f-c021-43b5-8184-7bd67d40ca4e)

    - 사용자가 지정한 시간이 되면 Notification이 발생하여 사용자에게 시간이 되었음을 알려줌

![a6](https://github.com/Solunax/SimpleAlarm/assets/97011241/7670f276-e642-4d6b-9227-8549e46a063a)

    - 알람이 울림과 동시에 활성화 된 알람은 비활성화 상태로 변경됨
    - 알람의 정보는 내부 DB에 저장되며, 이는 BroadcastReceiver를 통해 알람이 울리면 정보가 갱신되게 함


<br>

### 세계시간 설정

![t1](https://github.com/Solunax/SimpleAlarm/assets/97011241/70874981-927e-4dc7-b964-f0bd9ce677b5)

    - 세계시간 tab을 선택했을 때 보게되는 화면
    
![t2](https://github.com/Solunax/SimpleAlarm/assets/97011241/42fcbc25-2362-4bc1-989c-4b76b8dc11df)

    - 하단 버튼을 통해 원하는 국가의 현재 시간을 추가할 수 있음
    
![t3](https://github.com/Solunax/SimpleAlarm/assets/97011241/788a12dc-203e-47d8-bbba-fa9ec29538f8)

    - 국가는 원하는 만큼, 추가한 순서대로 배치되며 우측의 쓰레기통 버튼을 통해 삭제할 수 있음
    - SharedPreference를 활용하여 사용자가 국가를 추가 및 삭제할 때 마다 갱신되며, 앱을 껏다 켜도 해당 정보가 유지됨



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
- Layout 수정
- 스톱워치 기능 추가
- 스톱워치 랩타임 저장 기능 추가
- 스톱워치 구현(0.01초 단위, 기존 chronometer 대신 timer 사용)
