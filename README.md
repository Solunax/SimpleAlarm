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
- room, activity, lifecycle
    - room 
        - 알람 데이터 저장을 위해서 사용함
    - activity, lifecycle
        - ViewModel을 구성하기 위해서 사용함

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
- 2023. 4. 9 다수의 알람을 동시에 등록할 수 있게 수정함, 등록된 알람 목록은 Recycler View를 통해 표시(내부 DB에 저장된 알람 )
