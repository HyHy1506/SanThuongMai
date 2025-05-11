import { combineReducers } from "redux";
import { authenticationReducer } from "./authenticationReducer";
import cartReducer from "./cartReducer";
import { chatReducer } from "./chatReducer";
const rootReducer= combineReducers({
  authentication:authenticationReducer,
  cart: cartReducer,
  chat:chatReducer,
});
export default rootReducer;