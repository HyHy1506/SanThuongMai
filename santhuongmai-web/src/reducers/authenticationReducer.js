export  const authenticationReducer=(state=null,action)=>{
    switch(action.type){
        case "LOGIN":
            localStorage.setItem('user', JSON.stringify(action.payload))
            return action.payload
        case "LOGOUT":
            localStorage.setItem('user', null)
            localStorage.setItem('cart', [])

            return null;
        default:
            return state
    }
    return state
}
