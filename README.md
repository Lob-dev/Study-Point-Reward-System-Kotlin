# Study-Point-Reward-System-Kotlin
22-07-23 ~ 24일 간 Point Reward System 공부 및 기능 구현 해보기

### 주저리 주저리
- 일부 쿼리를 JOOQ로 작성하면 Lock 범위도 조절 가능할 것 같다. (연관 데이터 정합성을 위해 Lock 범위를 크게 유지하는게 나을지 고민)
- named lock을 user 단위로 설정하는게 괜찮은 선택일지 고민 (앞단에서 동일 요청에 대해 동일 키를 던져준다면 이렇게 안하고 멱등 처리만 해도 될 것 같은데)
