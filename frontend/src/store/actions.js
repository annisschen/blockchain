export default {
  /**
   * commit: an object property from vuex, full name referred as  context.commit
   * profile: custom parameter submitted
   * register: methods in mutations.js
   */
  signup: ({ commit }, profile) => {
    commit('register', profile)
  },

}
