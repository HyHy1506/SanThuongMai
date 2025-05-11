import { db } from "../configs/FirebaseConfig"
import { onValue, ref, set, push, off, update, get, serverTimestamp, orderByChild, limitToLast, query } from "firebase/database"

const createUser = async (user) => {
    try {


        const userRef = ref(db, "users/" + `${user.id}`);
        const snapshot = await get(userRef)
        if (snapshot.exists()) {
            return snapshot.val()
        }
        await set(userRef, {
            "id": user.id,
            "username": user.username,
            "nickname": user.nickname,
            "avatar": user.avatar,
        })
        return { success: true };
    } catch (error) {
        return {
            success: false,
            error: error.getMessages()
        };
    }
}
const createConversationId = (userId1, userId2) => {
    const idKey = [userId1, userId2].sort().join("_")
    return idKey
}
const createConversation = async (userId1, userId2) => {
    // tao conversation theo danh user1_user2 de truy xuat nhanh hon
    const idKey = createConversationId(userId1, userId2)
    const conversationRef = ref(db, "conversations/" + idKey)

    try {
        //kiem tra co ton ton conversation giua 2 user nay chua
        const snapshot = await get(conversationRef)
        if (snapshot.exists()) {
            return { success: false, error: "Conversation already exists" };
        }
        await set(conversationRef, {
            "participants": {
                "user_1": userId1,
                "user_2": userId2,
            },
            "created_at": serverTimestamp(),
            "updated_at": serverTimestamp(),
            "active": true

        })
        const user1Ref = ref(db, `users/${userId1}/conversations`)
        await update(user1Ref, {
            [`${idKey}`]: serverTimestamp()
        })
        const user2Ref = ref(db, `users/${userId2}/conversations`)
        await update(user2Ref, {
            [`${idKey}`]: serverTimestamp()
        })

        return { success: true, id: idKey };
    } catch (error) {
        return {
            success: false,
            error: error.getMessages()
        };
    }



}
const createTextMessage = async (conversationId, userIdsend, textMessage) => {
    try {
        const messageRef = push(ref(db, `messages/${conversationId}`))
        const messageId = messageRef.key
        const message = {
            "text": textMessage,
            "sender_id": userIdsend,
            "created_at": serverTimestamp(),
            "type": "text"
        }
        //tao message moi
        await set(messageRef, message)
        //update lai last message
        const conversationLastMessRef = ref(db, `conversations/${conversationId}/last_message`)
        await update(conversationLastMessRef, message)

        const conversationTimeRef = ref(db, `conversations/${conversationId}`)
        await update(conversationTimeRef, { updated_at: serverTimestamp() })
        //cai lai cuoc tro truyen de khi tao moi tin nhan thi cuoc tro chuyen se update theo
        const userConversationRef = ref(db, `users/${userIdsend}/conversations`)
        await update(userConversationRef, { [conversationId]: serverTimestamp() })
        await update(userConversationRef, { [conversationId]: serverTimestamp() })
        return {
            success: true
        };
    } catch (error) {
        return {
            success: false,
            error: error.getMessages()
        };
    }


}
const createImageMessage = async (conversationId, userIdsend, imageMessage) => {
    try {
        const messageRef = push(ref(db, `messages/${conversationId}`))
        const messageId = messageRef.key
        const message = {
            "image": imageMessage,
            "text":"Đã gửi 1 ảnh",
            "sender_id": userIdsend,
            "created_at": serverTimestamp(),
            "type": "image"
        }
        //tao message moi
        await set(messageRef, message)
        //update lai last message
        const conversationLastMessRef = ref(db, `conversations/${conversationId}/last_message`)
        await update(conversationLastMessRef, message)

        const conversationTimeRef = ref(db, `conversations/${conversationId}`)
        await update(conversationTimeRef, { updated_at: serverTimestamp() })
        //cai lai cuoc tro truyen de khi tao moi tin nhan thi cuoc tro chuyen se update theo
        const userConversationRef = ref(db, `users/${userIdsend}/conversations`)
        await update(userConversationRef, { [conversationId]: serverTimestamp() })
        await update(userConversationRef, { [conversationId]: serverTimestamp() })
        return {
            success: true
        };
    } catch (error) {
        return {
            success: false,
            error: error.getMessages()
        };
    }


}
const getMessages = (conversationId, callback) => {
    const messagesRef = ref(db, `messages/${conversationId}`)
    //lay 50 tin nhan gan nhat
    const messageQuery = query(messagesRef, orderByChild('created_at'), limitToLast(50))

    const unsubscribe = onValue(messageQuery, (snapshot) => {
        const messages = []
        snapshot.forEach((child) => {
            messages.push({
                id: child.key,
                ...child.val()
            })
        })
        callback(messages)
    })
    return () => {
        unsubscribe()
    }
}
const testGet = async () => {
   try {
        const userConversationRef = ref(db, `users/19/conversations`);
        let a = await get(userConversationRef);
        console.log("chat functions 1", a);
    } catch (error) {
        console.error("Lỗi trong testGet:", error);
        throw error; // Ném lỗi để hàm gọi biết có vấn đề
    }

}
const testFirebaseConnection = async () => {
  try {
    // Tạo một đường dẫn kiểm tra tạm thời
    const testRef = ref(db, "testConnection/test");
    
    // Thử ghi dữ liệu
    await set(testRef, {
      timestamp: Date.now(),
      message: "Kiểm tra kết nối Firebase",
    });
    console.log("Ghi dữ liệu kiểm tra thành công");

    // Thử đọc dữ liệu
    const snapshot = await get(testRef);
    if (snapshot.exists()) {
      console.log("Đọc dữ liệu thành công:", snapshot.val());
      return { success: true, data: snapshot.val() };
    } else {
      console.log("Không tìm thấy dữ liệu tại đường dẫn kiểm tra");
      return { success: false, error: "No data found" };
    }
  } catch (error) {
    console.error("Lỗi khi kiểm tra Firebase:", error);
    return {
      success: false,
      error: error.message || "Không thể kết nối với Firebase",
    };
  }
};
const getUserConversations = (userId, callback) => {
    const userConversationRef = ref(db, `users/${userId}/conversations`)
    

    const unsubscribe = onValue(userConversationRef, async (snapshot) => {
        const conversations = (snapshot.val() || {})
        //chuyen doi sang mang va sap xep tu moi nhat den cu
        const conversationArray = Object.entries(conversations);
        conversationArray.sort((a, b) => b[1] - a[1]);
        const sortedConversationIds = conversationArray.map(item => item[0]);
        const arrayConversations = []
        for (const conversationId of sortedConversationIds) {
            const detailConversationRef = ref(db, `conversations/${conversationId}`)
            const detailConversation = await get((detailConversationRef))
            if (detailConversation.exists()) {
                arrayConversations.push({
                    id: conversationId,
                    ...detailConversation.val()
                })

            }

        }
        callback(arrayConversations)
    })
    return () => {
        unsubscribe()
    }

}
const checkExistConversation = async (userId1, userId2) => {

    const idKey = createConversationId(userId1, userId2)

    const conversationRef = ref(db, "conversations/" + idKey)
    const conversation = await get(conversationRef)

    if (conversation.exists()) {
        let conversationId = conversation.key
        return {
            success: true
        }
    } else {
        return {
            success: false
        }
    }
}
export { createConversation, createTextMessage, createUser, getMessages,
     getUserConversations, checkExistConversation, createConversationId,testGet,testFirebaseConnection,createImageMessage }
