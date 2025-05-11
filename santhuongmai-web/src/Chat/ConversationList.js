import { ListGroup, Row, Col, Image } from "react-bootstrap";
import { useSelector } from "react-redux";
import ConversationItem from "./ConversationItem";
import { authApis, endpoints } from "../configs/Apis";
import { useState } from "react";
const ConversationList = ({ conversations }) => {

    const user = useSelector((state) => state.authentication);

    return (
        <ListGroup variant="flush">
            {conversations.length === 0 ? (
                <ListGroup.Item className="text-center text-muted">
                    No conversations yet.
                </ListGroup.Item>
            ) : (
                conversations.map((conv) => {
                    const otherUserId = conv.participants.user_1 === user.id ? conv.participants.user_2 : conv.participants.user_1;
                    return (
                       <ConversationItem otherUserId={otherUserId}conv={conv} key={otherUserId}/>
                    );
                }))}
        </ListGroup>
    );
}


export default ConversationList;