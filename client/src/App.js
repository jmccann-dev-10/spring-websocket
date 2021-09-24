import jwtDecode from "jwt-decode";
import { useEffect, useState } from "react";
import AuthContext from "./AuthContext";
import AppRouter from "./AppRouter";

function App() {

  const [init, setInit] = useState(false);
  const [user, setUser] = useState(null);

  const TOKEN_KEY = 'user-api-token';

  const login = (token) => {
    localStorage.setItem(TOKEN_KEY, token);

    const {id, sub: username, roles: rolesString } = jwtDecode(token);
    const roles = rolesString.split(',');

    const user = {
      id,
      username,
      roles,
      token,
      hasRole(role) {
        return this.roles.includes(role);
      }
    };

    setUser(user);
    return user;
  };

  const logout = () => {
    localStorage.removeItem(TOKEN_KEY);
    setUser(null);
    return true;
  };

  const auth = {
    user: user ? {...user} : null,
    login,
    logout
  };

  useEffect(()=>{
    const token = localStorage.getItem(TOKEN_KEY);
    if (token) {
      login(token);
    }
    setInit(true);
  },[init]);

  return (
    <div className="App">
      {init && 
        (<AuthContext.Provider value={auth}>
          <AppRouter/>
        </AuthContext.Provider>)
      }
    </div>
  );
}

export default App;
