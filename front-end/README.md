### front-end  

Vue.js 로 프론트 개발  

* 단위테스트 - Jest
* 통합테스트 - NightWatch

#### vue-test-utils 사용 시 책과 버전이 맞지 않아 문제  
<https://stackoverflow.com/questions/63931735/vue-v-model-bind-is-not-working-in-unit-test>  
```
  it('should have form inputs bound with data model', async () => {
    const username = 'sunny'
    const emailAddress = 'sunny@taskagile.com'
    const password = 'VueJsRocks!'
                                
    // wrapper.vm.form.username = username
    // wrapper.vm.form.emailAddress = emailAddress
    // wrapper.vm.form.password = password
    await wrapper.setData({
      form: {
        username,
        emailAddress,
        password
      }
    })
    expect(fieldUsername.element.value).toEqual(username)
    expect(fieldEmailAddress.element.value).toEqual(emailAddress)
    expect(fieldPassword.element.value).toEqual(password)
  })

```

#### jest.fn()  //  registration http methods __mocks__ 디렉토리로 mock 메서드 설정  

#### createLocalVue()  
<https://stackoverflow.com/questions/62312842/why-is-createlocalvue-needed-when-testing-vue-components-with-vuex>  

#### jest.spyOn()
#### 프로미스 기반 메소드 테스트 시  
* wrapper.vm.$nextTick()  
* expect.assertions() -- assertion 호출 횟 수 검증
