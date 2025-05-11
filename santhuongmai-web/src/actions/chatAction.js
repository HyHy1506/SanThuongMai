
export const SHOW_CHAT_MODEL = 'SHOW_CHAT_MODEL'
export const HIDE_CHAT_MODEL = 'HIDE_CHAT_MODEL';
export const SELECTED_CONVERSATION = 'SELECTED_CONVERSATION';
export const UNSELECTED_CONVERSATION = 'UNSELECTED_CONVERSATION';


export const showChatAction = () => ({
    type: SHOW_CHAT_MODEL,
    payload: true,
});
export const hideChatAction = () => ({
    type: HIDE_CHAT_MODEL,
    payload: false,
});
export const selectedConversationAction = (conservation) => ({
    type: SELECTED_CONVERSATION,
    payload: conservation,
});
export const unselectedConversationAction = () => ({
    type: UNSELECTED_CONVERSATION,
    payload: null,
});