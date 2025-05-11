import { Modal } from "react-bootstrap";
import MessageView from "./MessageView";
import ConversationList from "./ConversationList";
import { useEffect, useRef, useState } from "react";
import { Button, Col, Form, Image, Row } from "react-bootstrap";
import { toast } from "react-toastify";
import { getUserConversations, testFirebaseConnection, testGet } from "../utils/ChatFunctions";
import { useDispatch, useSelector } from "react-redux";
import { hideChatAction } from "../actions/chatAction";
const ChatModal = ({ user }) => {
    const [conversations, setConversations] = useState([]);
    const showChat = useSelector(state => state.chat).showChat
    const selectedConversation = useSelector(state => state.chat).conversation
    const dispatch = useDispatch()
    useEffect(() => {

        if (user) {


            const unsubscribe = getUserConversations(user.id, (arrayConversations) => {
                console.log(arrayConversations)
                setConversations(arrayConversations)
            });
            return unsubscribe;
        }
    }, [user]);

    return (
        <Modal
            show={showChat}
            onHide={() => dispatch(hideChatAction())}
            size="lg"
            centered
            className="chat-modal"
            style={{ maxWidth: '90vw' }}
        >
            <Modal.Header closeButton>
                <Modal.Title>{selectedConversation ? 'Tin nhắn' : 'Cuộc trò chuyện'}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {selectedConversation ? (
                    <MessageView

                    />
                ) : (
                    <ConversationList
                        conversations={conversations}

                    />
                )}
            </Modal.Body>
        </Modal>
    );
};

export default ChatModal;