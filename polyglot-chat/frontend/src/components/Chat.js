import React, { useState, useEffect, useRef } from 'react';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import './Chat.css';

function Chat({ user, onLogout }) {
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const [targetLanguage, setTargetLanguage] = useState('en');
  const [isTyping, setIsTyping] = useState(false);
  const [typingUser, setTypingUser] = useState('');
  const stompClient = useRef(null);
  const messagesEndRef = useRef(null);

  const languages = [
    { code: 'en', name: 'English' },
    { code: 'es', name: 'Spanish' },
    { code: 'fr', name: 'French' },
    { code: 'de', name: 'German' },
    { code: 'it', name: 'Italian' },
    { code: 'pt', name: 'Portuguese' },
    { code: 'ru', name: 'Russian' },
    { code: 'ja', name: 'Japanese' },
    { code: 'ko', name: 'Korean' },
    { code: 'zh', name: 'Chinese' },
    { code: 'ar', name: 'Arabic' },
    { code: 'hi', name: 'Hindi' },
    { code: 'tr', name: 'Turkish' },
    { code: 'pl', name: 'Polish' },
    { code: 'nl', name: 'Dutch' },
    { code: 'sv', name: 'Swedish' }
  ];

  useEffect(() => {
    connect();
    return () => {
      if (stompClient.current) {
        stompClient.current.disconnect();
      }
    };
  }, []);

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const connect = () => {
    const socket = new SockJS('/ws');
    stompClient.current = Stomp.over(socket);

    stompClient.current.connect({}, () => {
      stompClient.current.subscribe('/topic/messages', (message) => {
        const receivedMessage = JSON.parse(message.body);
        setMessages(prevMessages => [...prevMessages, receivedMessage]);
      });

      stompClient.current.subscribe('/topic/typing', (message) => {
        const typingMessage = message.body;
        setTypingUser(typingMessage);
        setIsTyping(true);
        setTimeout(() => setIsTyping(false), 3000);
      });
    });
  };

  const sendMessage = () => {
    if (newMessage.trim() && stompClient.current) {
      const message = {
        sender: user.username,
        content: newMessage,
        language: 'en', // Assuming English input
        targetLanguage: targetLanguage
      };

      stompClient.current.send('/app/send', {}, JSON.stringify(message));
      setNewMessage('');
    }
  };

  const handleTyping = () => {
    if (stompClient.current) {
      stompClient.current.send('/app/typing', {}, user.username);
    }
  };

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      sendMessage();
    }
  };

  return (
    <div className="chat-container">
      <div className="chat-header">
        <h2>Polyglot Chat</h2>
        <div className="user-info">
          <span>Welcome, {user.username}!</span>
          <button onClick={onLogout} className="logout-btn">Logout</button>
        </div>
      </div>

      <div className="language-selector">
        <label>Translate to:</label>
        <select value={targetLanguage} onChange={(e) => setTargetLanguage(e.target.value)}>
          {languages.map(lang => (
            <option key={lang.code} value={lang.code}>{lang.name}</option>
          ))}
        </select>
      </div>

      <div className="messages-container">
        {messages.map((msg, index) => (
          <div key={index} className={`message ${msg.sender === user.username ? 'own' : 'other'}`}>
            <div className="message-header">
              <strong>{msg.sender}</strong>
            </div>
            <div className="message-content">
              {msg.content}
            </div>
            {msg.translatedContent && (
              <div className="translated-content">
                <small>Translated: {msg.translatedContent}</small>
              </div>
            )}
          </div>
        ))}
        {isTyping && <div className="typing-indicator">{typingUser}</div>}
        <div ref={messagesEndRef} />
      </div>

      <div className="message-input">
        <input
          type="text"
          value={newMessage}
          onChange={(e) => setNewMessage(e.target.value)}
          onKeyPress={handleKeyPress}
          onKeyUp={handleTyping}
          placeholder="Type a message..."
        />
        <button onClick={sendMessage} disabled={!newMessage.trim()}>Send</button>
      </div>
    </div>
  );
}

export default Chat;
