import { HIDE_CHAT_MODEL, SHOW_CHAT_MODEL, SELECTED_CONVERSATION, UNSELECTED_CONVERSATION } from "../actions/chatAction"
const initState = {
    showChat: false,
    conversation: null
}
export const chatReducer = (state = initState, action) => {
    switch (action.type) {
        case SHOW_CHAT_MODEL:
            return { ...state, showChat: action.payload }
        case HIDE_CHAT_MODEL:
            return { ...state, showChat: action.payload };
        case SELECTED_CONVERSATION:
            return { ...state, conversation: action.payload };
        case UNSELECTED_CONVERSATION:
            return { ...state, conversation: action.payload };
        default:
            return state
    }
}
