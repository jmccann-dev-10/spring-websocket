import { useState, useContext } from "react";
import { useHistory, Link } from "react-router-dom";

import AuthContext from "../AuthContext";
import Errors from "./Errors";

function Register() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [errors, setErrors] = useState([]);

    const history = useHistory();
    const auth = useContext(AuthContext);

    const userNameFormOnChangeHandler = (event) => {
        setUsername(event.target.value);
    }

    const passwordFormOnChangeHandler = (event) => {
        setPassword(event.target.value);
    }

    const confirmPasswordFormOnChangeHandler = (event) => {
        setConfirmPassword(event.target.value);
    }

    const submitEventHandler = (event) => {
        event.preventDefault();

        if (password !== confirmPassword) {
            setErrors(['Both passwords must match.'])
            return;
        }

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

        const getUser = () => {
            return fetch('http://localhost:5000/authenticate', init)
                    .then(response => {
                        if (response.status === 200) {
                            return response.json();
                        }
                        return Promise.reject();
                    })
        }

        fetch('http://localhost:5000/create_account', init)
            .then(response => {
                if (response.status === 201 || response.status === 400) {
                    return response.json();
                }
                
                return Promise.reject();
            })
            .then(data => {
                if (data.id) {
                    return getUser();
                } else {
                    return Promise.reject(data.messages);
                }
            })
            .then(login => {
                history.push('/');
                auth.login(login.jwt_token);
            })
            .catch(errors => {
                if (errors) {
                    setErrors(errors);
                } else {
                    history.push('/error');
                }
            });
    }

    return (
        <>
            <h2>Register</h2>
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
                <div className="form-group">
                    <label htmlFor="confirm-password">Confirm Password:</label>
                    <input name="confirm-password" className="form-control" type="password" id="confirm-password" onChange={confirmPasswordFormOnChangeHandler} value={confirmPassword}/>
                </div>
                <div>
                    <button className="btn btn-success" type="submit">Register</button>
                    <Link to="/" className="btn btn-warning ml-2">Cancel</Link>
                </div>
                <div>
                    <Link to="/login">I already have an account.</Link>
                </div>
            </form>
        </>
    );
}

export default Register;