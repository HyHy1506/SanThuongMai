import { combineReducers } from "redux";
import { authenticationReducer } from "./authenticationReducer";
import cartReducer from "./cartReducer";
const rootReducer= combineReducers({
  authentication:authenticationReducer,
  cart: cartReducer,
});
export default rootReducer;