import { useEffect, useRef } from "react";

function MessageContainer({messagesState = {messages:[]}, username = ''}) {
    const messagesEndRef = useRef(null);

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({behavior: "smooth"});
    }

    useEffect(() => {
        scrollToBottom();
    }, [messagesState]);

    function Message({ m }) {
        const isFromMe = m.sender === username;
        const classes = `px-3 border-bottom py-2 text-${isFromMe ? 'right' : 'left'}`;
        return (
            <>
                {m.type === 'CHAT' && (<div className={classes}><h6 style={{backgroundColor: m.color}} className="badge rounded-pill p-2 badge-info">{m.sender}</h6><div className="mx-2">{m.content}</div></div>)}
                {m.type !== 'CHAT' && (<div className="text-center bg-secondary text-light py-3 border-bottom border-dark">{m.content}</div>)}
            </>)
    }

    return (
        <div className="mb-2 message-container-outer" >
            <div className="d-flex flex-column message-container-inner">
                    {messagesState.messages && messagesState.messages.map(msg => (<Message key={msg.id} m={msg} />))}
                <div ref={messagesEndRef} />
            </div>
        </div>
    )
}

export default MessageContainer;