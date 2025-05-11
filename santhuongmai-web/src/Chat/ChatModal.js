import { Modal } from "react-bootstrap";
import MessageView from "./MessageView";
import ConversationList from "./ConversationList";
import { useEffect, useRef, useState } from "react";
import { Button, Col, Form, Image, Row } from "react-bootstrap";
import { toast } from "react-toastify";
import { getUserConversations, testFirebaseConnection, testGet } from "../utils/ChatFunctions";
const ChatModal = ({ show, onHide, user, users }) => {
    const [conversations, setConversations] = useState([]);
    const [selectedConversation, setSelectedConversation] = useState(null);
  
    useEffect(() => {

        if (user && show) {


            const unsubscribe = getUserConversations(user.id, (arrayConversations) => {
                console.log(arrayConversations)
                setConversations(arrayConversations)
            });
            return unsubscribe;
        }
    }, [user, show]);

    const handleSelectConversation = (conv) => {
        setSelectedConversation(conv);
    };

    const handleBack = () => {
        setSelectedConversation(null);
    };

    return (
        <Modal
            show={show}
            onHide={onHide}
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
                        conversation={selectedConversation}
                        currentUser={user}
                        users={users}
                        onBack={handleBack}
                    />
                ) : (
                    <ConversationList
                        conversations={conversations}
                        onSelectConversation={handleSelectConversation}
                    />
                )}
            </Modal.Body>
        </Modal>
    );
};

export default ChatModal;