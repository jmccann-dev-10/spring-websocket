import { useState, useContext } from "react";
import { useHistory, Link } from "react-router-dom";

import Errors from "./Errors";
import AuthContext from "../AuthContext";

function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errors, setErrors] = useState([]);

    const history = useHistory();
    const auth = useContext(AuthContext);

    const userNameFormOnChangeHandler = (event) => {
        setUsername(event.target.value);
    }

    const passwordFormOnChangeHandler = (event) => {
        setPassword(event.target.value);
    }

    const submitEventHandler = (event) => {
        event.preventDefault();
        const init = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username,
                password
            })
        };
        fetch('http://localhost:5000/authenticate', init)
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else if (response.status === 403){
                    return Promise.reject('The login information appears to be incorrect.');
                }
                
                return Promise.reject();
            })
            .then(data => {
                history.push('/');
                auth.login(data.jwt_token);
            })
            .catch(error => {
                if (error) {
                    setErrors([error]);
                } else {
                    history.push('/error');
                }
            });
    }

    return (
        <>
            <h2>Login</h2>
            <Errors errors={errors} />
            <form onSubmit={submitEventHandler}>
                <div className="form-group">
                    <label htmlFor="username">Username:</label>
                    <input name="username" className="form-control" type="text" id="username" onChange={userNameFormOnChangeHandler} value={username}/>
                </div>
                <div className="form-group">
                    <label htmlFor="password">Password:</label>
                    <input name="password" className="form-control" type="password" id="password" onChange={passwordFormOnChangeHandler} value={password}/>
                </div>
                <div>
                    <button className="btn btn-success" type="submit">Login</button>
                    <Link to="/" className="btn btn-warning ml-2">Cancel</Link>
                </div>
                <div>
                    <Link to="/register">I need to create an account</Link>
                </div>
            </form>
        </>
    );
}

export default Login;