import { useEffect, useRef } from "react";

function MessageContainer({messages = [], username = ''}) {
    const messagesEndRef = useRef(null);

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({behavior: "smooth"});
    }

    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    const outerContainerStyle = {
        'height': '65vh',
        'maxHeight': '65vh',
        'overflowY': 'hidden',
        'borderRadius': '1em',
        'border': '1px solid rgba(0,0,0,.125)'
    }

    const innerContainerStyle ={
        'overflowY': 'auto',
        'overflowX': 'hidden',
        'border': 'none'
    }

    function Message({ m }) {
        const isFromMe = m.sender === username;
        const classes = 'px-3 border-bottom py-2 text-' + (isFromMe ? 'right' : 'left');
        return (
            <>
                {m.type === 'CHAT' && (<div className={classes}><h6 className="badge rounded-pill p-2 badge-info">{m.sender}</h6><div className="mx-2">{m.content}</div></div>)}
                {m.type !== 'CHAT' && (<div className="text-center bg-secondary text-light py-3 border-bottom border-dark">{m.content}</div>)}
            </>)
    }

    return (
        <div className="mb-2" style={outerContainerStyle}>
            <div className="d-flex flex-column" style={{...outerContainerStyle, ...innerContainerStyle}}>
                    {messages ? messages.map(msg => (<Message key={msg.id} m={msg} />)) : null}
                <div ref={messagesEndRef} />
            </div>
        </div>
    )
}

export default MessageContainer;