export  const authenticationReducer=(state=null,action)=>{
    switch(action.type){
        case "LOGIN":
            return action.payload
        case "LOGOUT":
            return null;
        default:
            return state
    }
    return state
}
