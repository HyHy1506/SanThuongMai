export const authenticationReducer = (state = null, action) => {
    switch (action.type) {
        case "LOGIN":
            localStorage.setItem('user', JSON.stringify(action.payload))
            return action.payload
        case "LOGOUT":
            localStorage.removeItem('user')
            localStorage.removeItem('cart')
            localStorage.removeItem('token')
            return null;
        default:
            return state
    }
    return state
}
