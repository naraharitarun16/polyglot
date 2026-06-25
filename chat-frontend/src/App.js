import React, { useState } from 'react';
import Login from './components/Login';
import Signup from './components/Signup';
import Chat from './components/Chat';
import './App.css';

function App() {
  const [user, setUser] = useState(null);
  const [showSignup, setShowSignup] = useState(false);

  const handleLogin = (userData) => {
    setUser(userData);
  };

  const handleSignup = (userData) => {
    setUser(userData);
  };

  const handleLogout = () => {
    setUser(null);
    localStorage.removeItem('token');
  };

  return (
    <div className="App">
      {!user ? (
        showSignup ? (
          <Signup onSignup={handleSignup} onLoginClick={() => setShowSignup(false)} />
        ) : (
          <Login onLogin={handleLogin} onSignupClick={() => setShowSignup(true)} />
        )
      ) : (
        <Chat user={user} onLogout={handleLogout} />
      )}
    </div>
  );
}

export default App;
