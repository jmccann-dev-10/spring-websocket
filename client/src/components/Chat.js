import { useContext, useEffect, useState } from "react";
import { useHistory, useParams } from "react-router";
import AuthContext from "../AuthContext";
import { Client } from "@stomp/stompjs";
import MessageContainer from "./MessageContainer";

function Chat() {

    const [message, setMessage] = useState('');
    const [payload, setPayload] = useState();
    const [messages, setMessages] = useState([]);
    const [client, setClient] = useState();
    const [subscription, setSubscription] = useState();

    const { topic } = useParams();
    const auth = useContext(AuthContext);
    const history = useHistory();

    useEffect(()=>{
        const newClient = new Client({
            brokerURL: 'ws://localhost:8080/ws',
            connectHeaders: {"Authorization": `Bearer ${auth.user.token}`},
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000
        });

        newClient.onConnect = () => {
            const sub = newClient.subscribe(`/topic/${topic}`, onMessageReceived);
            setSubscription(sub);
            newClient.publish({destination: `/app/${topic}/chat.addUser`, body: JSON.stringify({type: 'JOIN'})});
        }
    
        newClient.onStompError = (error) => {
            console.log(error);
        }

        newClient.activate();

        setClient(newClient);
    },[auth.user.token, topic])

    useEffect(()=>{
        const messagesCopy = [...messages];
        if (payload && !messagesCopy.find(msg => payload.id === msg.id)) {
            messagesCopy.push(payload);
            if (messagesCopy.length > 15) {
                messagesCopy.shift();
            }
            setMessages(messagesCopy);
        }
    }, [payload, messages]);

    const onMessageReceived = (payload) => {
        const newMessage = JSON.parse(payload.body);
        setPayload(newMessage);
    }

    const sendMessage = (event) => {
        event.preventDefault();
        client.publish({destination: `/app/${topic}/chat.sendMessage`, body: JSON.stringify({content: message, type: 'CHAT'})});
        setMessage('');
    }

    history.listen(() => {
        if (client && subscription) {
            client.deactivate();
            setSubscription(null);
            setClient(null);
        }
    });

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