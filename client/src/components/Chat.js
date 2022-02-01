import { useContext, useEffect, useReducer, useState } from "react";
import { useParams } from "react-router";
import AuthContext from "../AuthContext";
import { Client } from "@stomp/stompjs";
import MessageContainer from "./MessageContainer";

const messageReducer = (currentState, action) => {
    const newMessageState = {...currentState};
    const users = newMessageState.users;

    // assign color
    if (action.type === 'LEAVE') {
        delete users[action.sender];
    } else if (!users[action.sender]) {
        users[action.sender] = `#${Math.floor(Math.random()*16777215).toString(16)}`;
    }
    action.color = users[action.sender];

    // add new messages
    const messages = newMessageState.messages;
    if (action && !messages.find(msg => action.id === msg.id)) {
        messages.push(action);
        if (messages.length > 15) {
            messages.shift();
        }
    }
    return newMessageState;
}

function Chat() {

    const [message, setMessage] = useState('');
    const [messageState, reduceState] = useReducer(messageReducer, {users: {}, messages: []});
    const [client, setClient] = useState();

    const { topic } = useParams();
    const auth = useContext(AuthContext);

    useEffect(()=>{
        const newClient = new Client({
            brokerURL: 'ws://localhost:8080/ws',
            connectHeaders: {"Authorization": `Bearer ${auth.user.token}`},
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000
        });

        const onMessageReceived = (payload) => {
            const newMessage = JSON.parse(payload.body);
    
            reduceState(newMessage);
        }

        let sub;

        newClient.onConnect = () => {
            sub = newClient.subscribe(`/topic/${topic}`, onMessageReceived);
            newClient.publish({destination: `/app/${topic}/chat.addUser`, body: JSON.stringify({type: 'JOIN'})});
        }
    
        newClient.onStompError = (error) => {
            console.log(error);
        }

        newClient.activate();
        setClient(newClient);

        // clean up the client when the component is dismissed.
        return () => {
            if (newClient && sub) {
                newClient.deactivate();
            }
        }
    },[auth.user.token, topic])

    const sendMessage = (event) => {
        event.preventDefault();
        client.publish({destination: `/app/${topic}/chat.sendMessage`, body: JSON.stringify({content: message, type: 'CHAT'})});
        setMessage('');
    }

    return (
        <>
            <h2>Chat!</h2>
            <div className="mb-1">Current Room: <span className="ml-3">{topic}</span></div>
            <MessageContainer messagesState={messageState} username={auth.user.username} />
            <div>
                <form onSubmit={sendMessage}>
                    <div className="form-group">
                        <input name="message" className="form-control" type="text" id="message" onChange={(event)=>setMessage(event.target.value)} value={message}/>
                    </div>
                    <button className="btn btn-primary" type="submit">Send</button>
                </form>
            </div>
        </>
    )
}

export default Chat;