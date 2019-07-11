export default {
  bankCount: (state) => {
    return state.bank.length
  },
  enterpriseCount: (state) => {
    return state.enterprise.length
  },
  insuranceCount: (state) => {
    return state.insurance.length
  },
  transportationCount: (state) => {
    return state.transportation.length
  },
  getAllContracts: (state) => {
    return state.contracts.length
  },
  getContractById: (state, id) => {
    var target = null
    var contracts = state.contracts
    for (let i = 0; i < state.contracts.length; i++) {
      if (contracts[i].id === id) {
        target = contracts[i]
        break
      }
    }
    return target
  }
}
