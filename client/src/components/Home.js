import { useContext, useState } from "react";
import { useHistory } from "react-router";
import AuthContext from "../AuthContext";

function Home() {

    const [topic, setTopic] = useState('');

    const auth = useContext(AuthContext);
    const history = useHistory();

    const topicNameCangeHandler = (event) => {
        setTopic(event.target.value);
    }

    const submitEventHandler = (event) => {
        event.preventDefault();
        history.push(`/chat/${topic}`);
    }

    return (
        <>
            <h2>Welcome</h2>
            {!auth.user && (<p>Please login to continue.</p>)}
            {auth.user && (
                <form onSubmit={submitEventHandler}>
                    <div className="form-group">
                        <label htmlFor="topic">Chat Room Name:</label>
                        <input name="topic" className="form-control" type="text" id="topic" placeholder="room name" onChange={topicNameCangeHandler} value={topic}/>
                    </div>
                    <div>
                        <button className="btn btn-success" type="submit">Go!</button>
                    </div>
                </form>
            )}
        </>
    )
}

export default Home;