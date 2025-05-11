const ChatIcon = ({ onClick }) => {
  return (
    <button className="chat-icon" onClick={onClick}>
      <i className="fas fa-comment-dots fa-2x"></i>
    </button>
  );
};

export default ChatIcon;