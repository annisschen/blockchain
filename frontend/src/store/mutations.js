export default {
  /**
   * state: application state from vuex
   * profile: payload passed into the function
   *
   * mutate or update the state
   */
  register: (state, profile) => {
    state[profile.category].push(profile)
  }
}
