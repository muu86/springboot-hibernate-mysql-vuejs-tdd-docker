### 스프링, Vue.js 모던 웹 애플리케이션 개발 책 공부

* vscode
* springboot
* mysql 8
* jpa
* docker
* vue.js

[front-end](front-end)  

#### TDD 학습
#### 헥사고날 아키텍쳐로 실습
* 계층형 아키텍처
    * 프레젠테이션(애플리케이션) -> 비지니스(서비스) -> DAO
    * 패키지 구성(web, service, domain, dao)
    * 빈약한 도메인 모델
    * 데이터베이스 CRUD 중심 애플리케이션에 적합
* 헥사고날 아키텍처 hexagonal
    * 도메인 주도 설계 적합
    * 애플리케이션 중심부에 도메인 모델 위치
    * 풍부한 행동의 도메인 모델
    * 어댑터 (요청 -> 애플리케이션)
    * 어댑터 (애플리케이션 -> 데이터베이스 or 엘라스틱서치)
    * 어댑터 (애플리케이션 -> 이메일 기타 메세지, 캐시)
    * 다양한 유형의 클라이언트 확장 가능 (어댑터 추가)

#### 도메인 중심 패키징 스타일
하나의 도메인에 속하는 모든 클래스를 같은 패키지로~
* domain
    * application      -- controller 가 의존
        * UserService
        * Commands
            * RegistrationCommand -- UserService 가 받는 VO  
    * model
        * user
            * User
            * UserRepository
            * events.UserRegisteredEvent
            * RegistraionManagement
            * AuthenticationManagement
* infrastructure -- db
* utils --JsonUtils
* web
    * apis -- api Controller
    * pages -- Controller
    * payload --Request Json body to payload
    * results --Response body

#### Junit5, Mockito
* Junit4 -> @RunWith(SpringRunner.class)
* Jupiter -> @ExtendWith(~Extension.class), @SpringBootTest 가 포함하고 있음
* @WebMvcTest + @MockBean
    * @Controller 관련 Bean 만 스캔
    * not @Component, @Service or @Repository beans
    * @MockBean 과 함께 사용 -- 스캔하지 않는 Bean 로드
    * @SpringBootTest 어노테이션보다 가볍게
    * @ExtendWith(SpringExtension.class) 어노테이션 포함하므로 @SpringBootTest와 함께 사용 X
* @SpringBootTest + @AutoConfigureMockMvc
    * application context 로드
    * db 설정도 불러오는데 profile을 설정하지 않아 에러가 발생하여 애 먹음.. @ActiveProfiles 테스트 클래스에
    * MockMvc 사용하려면 @AutoConfigureMockMvc 추가
* 예외 예상 코드
    * junit4 - @Test(expected =".class")
    * jupiter - assertThrows (.class, () -> {})
* Mockito.mock() vs @Mock vs @MockBean
    * @Mock 은 테스트 클래스에 적용가능 -- @ExtendWith(MockitoExtension.class)

#### mysql docker container
* docker 로 mysql 설치
* wsl2 ubuntu terminal 에서 mysql 컨테이너를 만들 때 권한 문제 발생  
<https://stackoverflow.com/questions/6865538/solving-a-communications-link-failure-with-jdbc-and-mysql>  
윈도우 cmd 창에서 docker run 시 작동함  
* maven profile 설정으로 spring.profiles.active 관리  
* 노트북에선 docker 돌리기 버거워 h2 사용 --maven profile(home)
