import { useDispatch, useSelector } from "react-redux";
import { hideChatAction, showChatAction } from "../actions/chatAction";

const ChatIcon = ({ onClick }) => {
  const showChat = useSelector(state => state.chat).showChat
  const dispatch = useDispatch()
  const handleToggleChat = () => {
    if (showChat){
      dispatch(hideChatAction())
    }else{
      dispatch(showChatAction())
    }
  }
  return (
    <button className="chat-icon" onClick={handleToggleChat}>
      <i className="fas fa-comment-dots fa-2x"></i>
    </button>
  );
};

export default ChatIcon;