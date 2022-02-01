import { useContext, useEffect, useState } from "react";
import { useParams } from "react-router";
import AuthContext from "../AuthContext";
import { Client } from "@stomp/stompjs";
import MessageContainer from "./MessageContainer";

function Chat() {
    const [message, setMessage] = useState('');
    const [messages, setMessages] = useState([]);
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

            /* 
                Update methods are aware of their own state and will pass their own state into a callback function.
                This will guarantee that React has the most up-to-date state before updating, avoiding accidental
                rewriting over good state.
            */
            setMessages(messages => {
                const messageList = [...messages];
                if (newMessage && !messageList.find(msg => newMessage.id === msg.id)) {
                    messageList.push(newMessage);
                    if (messageList.length > 15) {
                        messageList.shift();
                    }
                }
                return messageList;
            });
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

        // return a cleanup function so the client can deactivate itself.  When the component is unmounted this will fire.
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
            <MessageContainer messages={messages} username={auth.user.username} />
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