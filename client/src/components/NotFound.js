import { Link } from "react-router-dom";

function NotFound() {
    return (
        <div className="alert alert-light">
            <div>
                <h1>404</h1>
            </div>
            <div className="text-center">
                <img src={process.env.PUBLIC_URL + '/images/Fail.svg'} alt="Failure occurred"/>
            </div>
            <div className="text-center">
                The thing you're looking for.  Yeah, that doesn't exist.
            </div>
            <div className="text-right">
                <Link className="alert-link" to="/">Go back home.</Link>
            </div>
        </div>
    )
}

export default NotFound;