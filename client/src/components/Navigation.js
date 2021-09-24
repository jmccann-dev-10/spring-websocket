import { useContext } from "react";
import { NavLink, Link } from "react-router-dom";
import AuthContext from "../AuthContext";

function NavBar() {
    const auth = useContext(AuthContext);

    return (
        <>
            <nav className="navbar navbar-expand-lg navbar-dark bg-info">
                <span className="navbar-brand">Navbar</span>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarNav">
                    <ul className="navbar-nav mr-auto">
                        <li className="nav-item"><NavLink exact className="nav-link" activeClassName="active" to="/" >Home</NavLink></li>
                        {!auth.user && (
                            <>
                                <li className="nav-item"><NavLink className="nav-link" activeClassName="active" to="/login">Login</NavLink></li>
                                <li className="nav-item"><NavLink className="nav-link" activeClassName="active" to="/register">Register</NavLink></li>
                            </>
                        )}
                    </ul>
                    {auth.user && (
                        // <span className="navbar-text">
                            <ul className="navbar-nav">
                                <li className="nav-item nav-link disabled" style={{color:'rgba(255,255,255,.75)'}} >Hello {auth.user.username}!</li>
                                <li className="nav-item" onClick={auth.logout}><Link to="/" className="nav-link">Logout</Link></li>
                            </ul>
                        // </span>
                                
                    )}
                </div>
            </nav>
        </>
    )
}

export default NavBar;