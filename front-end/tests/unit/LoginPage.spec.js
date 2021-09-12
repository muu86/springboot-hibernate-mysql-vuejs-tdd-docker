import { shallowMount } from '@vue/test-utils'
import LoginPage from '@/views/LoginPage'
import Vue from 'vue'

describe('LoginPage.vue', () => {

  it('should render correct contents', () => {
    const Constructor = Vue.extend(LoginPage)
    const vm = new Constructor().$mount()
    expect(vm.$el.querySelector('h1').textContent)
      .toEqual('TaskAgile')
  })

})
