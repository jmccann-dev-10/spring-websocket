import { useContext } from "react";
import { BrowserRouter as Router, Switch, Route, Redirect } from "react-router-dom";

import AuthContext from "./AuthContext";
import InternalError from "./components/InternalError";
import Login from "./components/Login";
import NotFound from "./components/NotFound";
import Register from "./components/Register";
import NavBar from "./components/Navigation";
import Home from "./components/Home";
import Chat from "./components/Chat";

function AppRouter() {
    const auth = useContext(AuthContext);
    
    return (
        <Router>
            <NavBar/>
            <div  className="container">
            <Switch>
                <Route exact path="/">
                    <Home/>
                </Route>
                <Route path="/login">
                    <Login/>
                </Route>
                <Route path="/register">
                    <Register/>
                </Route>
                <Route path="/error">
                    <InternalError/>
                </Route>
                <Route path="/chat/:topic">
                    {auth.user ?
                        (<Chat/>):
                        (<Redirect to="/"/>)}
                </Route>
                <Route path="*">
                    <NotFound/>
                </Route>
            </Switch>
            </div>
        </Router>
    )
}

export default AppRouter;