import { useEffect, useState } from "react";
import { ListGroup, Row, Col, Image } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { authApis, endpoints } from "../configs/Apis";
import { toast } from "react-toastify";
import { selectedConversationAction } from "../actions/chatAction";
const ConversationItem = ({ otherUserId, conv }) => {
    const [otherUser, setOtherUser] = useState(null)
    const dispatch = useDispatch()

    const loadOtherUser = async () => {
        try {
            const res = await authApis().get(endpoints["user-with-id"](otherUserId))
            setOtherUser(res.data.data)

        } catch (error) {
            console.log(error.response?.data?.error)
            // toast.error(error.response?.data?.error)
        }
    }
    useEffect(() => {
        loadOtherUser()
    }, [])

    return (
        <>
            {otherUser != null &&
                <ListGroup.Item
                    key={conv.id}
                    className="conversation-item"
                    onClick={() => dispatch(selectedConversationAction(conv))}
                    action
                >
                    <Row className="align-items-center">
                        <Col xs={3}>
                            <Image
                                src={otherUser.avatar}
                                roundedCircle
                                style={{ width: '40px', height: '40px', objectFit: 'cover' }}
                                onError={(e) => (e.target.src = "https://picsum.photos/200/300")}
                            />
                        </Col>
                        <Col xs={9}>
                            <strong>{otherUser.nickname}</strong>
                            <p className="mb-0 text-muted" style={{ fontSize: '0.9rem' }}>
                                {conv.last_message?.text || "No messages"}
                            </p>
                        </Col>
                    </Row>
                </ListGroup.Item>
                
            }

        </>

    )
}

export default ConversationItem