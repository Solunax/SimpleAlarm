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

![alarm1](https://github.com/Solunax/SimpleAlarm/assets/97011241/fe9c8b92-920d-4787-8644-bcfb2874e8b3)

    - Tab Layout 및 View Pager로 activity를 구성하여 스와이프 혹은 tab 클릭으로 화면을 전환할 수 있음
  
<br>

### 알람 설정 및 동작

![main](https://github.com/Solunax/SimpleAlarm/assets/97011241/6c5c765c-6bd0-4a87-843e-b18c997cc803)

    - 하단의 알람추가 버튼을 통해 원하는 시각에 알람이 울리도록 설정할 수 있음

![alarm2](https://github.com/Solunax/SimpleAlarm/assets/97011241/7cbb870e-c99e-4104-b2b5-4b17a4ed16bc)

    - 추가를 완료하면 최초에 비활성화 상태로 알람이 추가됨
    - 시간 우측 switch로 활성화 및 비활성화 가능, 쓰레기통 버튼을 사용해 알람 삭제 가능

![alarm3-1](https://github.com/Solunax/SimpleAlarm/assets/97011241/61b0a45e-6557-463a-bf2c-0100a9eb0747)
![alarm3-2](https://github.com/Solunax/SimpleAlarm/assets/97011241/4dbd0473-9158-4001-a38c-5c72a006c459)

    - 사용자가 지정한 시간이 되면 Notification이 발생하여 사용자에게 시간이 되었음을 알려줌
    - 알람 발생시 알람 영역을 터치해 알람 앱을 실행할 수 있음

![alarm4](https://github.com/Solunax/SimpleAlarm/assets/97011241/23f05391-8cdc-48eb-beda-b0683a64b9ad)

    - 알람이 울림과 동시에 활성화 된 알람은 비활성화 상태로 변경됨
    - 알람의 정보는 내부 DB에 저장되며, 이는 BroadcastReceiver를 통해 알람이 울릴 때 내부 DB의 정보가 갱신되게 함

<br>

### 세계시간 설정

![wc1](https://github.com/Solunax/SimpleAlarm/assets/97011241/466433de-4f49-4eca-8e2f-7b4d9abef997)

    - 세계시간 tab을 선택했을 때 보게되는 화면
    
![wc2](https://github.com/Solunax/SimpleAlarm/assets/97011241/6f9fc9e4-04e1-465a-8094-d37671c3787b)

    - 하단 버튼을 통해 원하는 국가의 현재 시간을 추가할 수 있음
    
![wc3](https://github.com/Solunax/SimpleAlarm/assets/97011241/1582d4dd-5bed-4e9b-8bba-76d7ed58e64c)

    - 국가는 원하는 만큼, 추가한 순서대로 배치되며 우측의 쓰레기통 버튼을 통해 삭제할 수 있음
    - SharedPreference를 활용하여 사용자가 국가를 추가 및 삭제할 때 마다 갱신되며, 앱을 껏다 켜도 해당 정보가 유지됨

<br>

### 스톱워치 기능

![sw1](https://github.com/Solunax/SimpleAlarm/assets/97011241/f70e35a2-8f5b-4172-94ee-bf3e136c92a3)

    - 스톱워치 tab을 선택했을 때 보게되는 화면

![sw2](https://github.com/Solunax/SimpleAlarm/assets/97011241/69f1a4aa-718b-4abd-b694-496ca616292a)

    - 스톱워치 시작 후 랩 버튼을 터치하여 랩 타임을 기록할 수 있음

![sw3](https://github.com/Solunax/SimpleAlarm/assets/97011241/5344481d-9fb6-4a6f-abf0-22a063e55119)

    - 정지버튼을 누르면 랩 버튼이 리셋 버튼으로 바뀜
    - 이 때 리셋 버튼을 터치하면 랩 타임 기록과 스톱워치의 시간 정보를 초기화함
    - 스톱워치의 랩타임과 시간 정보는 리셋 전 까지 데이터가 유지됨(앱을 꺼도 유지됨, SharedPreference)
