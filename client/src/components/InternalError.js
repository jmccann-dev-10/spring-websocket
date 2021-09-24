import { useHistory } from "react-router-dom";

function InternalError() {

    const history = useHistory();
    
    const goBack = () => {
        history.goBack();
    }

    return (
        <div className="alert alert-warning">
            <h1>500</h1>
            <div className="text-center">
                <img src={process.env.PUBLIC_URL + '/images/Godzilla.svg'} alt="internal error"/>
            </div>
            <div className="text-center">
                <p>The server monster appears to be back and wrecking our stuff!!</p>
            </div>
            <div className="text-right">
                <button className="no-style" onClick={goBack}>Quick!!! Go back to safety!</button>
            </div>
        </div>
    )
}

export default InternalError;