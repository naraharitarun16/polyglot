import React, { useState, useEffect, useRef } from 'react';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import './Chat.css';

function Chat({ user, onLogout }) {
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const [targetLanguage, setTargetLanguage] = useState('en');
  const [isTyping, setIsTyping] = useState(false);
  const [typingUser, setTypingUser] = useState('');
  const [isConnected, setIsConnected] = useState(false);
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
    const socket = new SockJS('/chat');
    stompClient.current = Stomp.over(socket);
    
    // Disable debug logging
    stompClient.current.debug = () => {};
    
    // Set connection timeout to 10 seconds
    stompClient.current.connectTimeout = 10000;
    
    stompClient.current.onConnect = () => {
      console.log('STOMP connected successfully');
      setIsConnected(true);
      
      stompClient.current.subscribe('/topic/messages', (message) => {
        try {
          const receivedMessage = JSON.parse(message.body);
          setMessages(prevMessages => {
            // Check if message with same sender, content, and timestamp already exists
            const isDuplicate = prevMessages.some(m => 
              m.sender === receivedMessage.sender && 
              m.content === receivedMessage.content &&
              m.timestamp === receivedMessage.timestamp
            );
            
            if (isDuplicate) {
              // Update existing message with translation if available
              return prevMessages.map(m =>
                (m.sender === receivedMessage.sender && 
                 m.content === receivedMessage.content &&
                 m.timestamp === receivedMessage.timestamp)
                  ? receivedMessage
                  : m
              );
            }
            
            return [...prevMessages, receivedMessage];
          });
        } catch (error) {
          console.error('Error parsing message:', error);
        }
      });

      stompClient.current.subscribe('/topic/typing', (message) => {
        try {
          const typingMessage = message.body;
          setTypingUser(typingMessage);
          setIsTyping(true);
          setTimeout(() => setIsTyping(false), 3000);
        } catch (error) {
          console.error('Error handling typing:', error);
        }
      });
    };
    
    stompClient.current.onStompError = (frame) => {
      console.error('STOMP error:', frame.body || frame);
      setIsConnected(false);
      // Attempt to reconnect after 2 seconds
      setTimeout(() => {
        console.log('Attempting to reconnect...');
        try {
          connect();
        } catch (error) {
          console.error('Reconnection failed:', error);
        }
      }, 2000);
    };
    
    stompClient.current.onDisconnect = () => {
      console.log('STOMP disconnected');
      setIsConnected(false);
    };
    
    socket.onopen = () => {
      console.log('SockJS connection opened');
    };
    
    socket.onclose = () => {
      console.log('SockJS connection closed');
      setIsConnected(false);
      // Try to reconnect after connection closes
      setTimeout(() => {
        console.log('Reconnecting after close...');
        try {
          connect();
        } catch (error) {
          console.error('Reconnection after close failed:', error);
        }
      }, 2000);
    };
    
    socket.onerror = (error) => {
      console.error('SockJS error:', error);
      setIsConnected(false);
    };
    
    try {
      stompClient.current.activate();
    } catch (error) {
      console.error('Error activating STOMP client:', error);
      setIsConnected(false);
    }
  };

  const sendMessage = () => {
    if (newMessage.trim() && stompClient.current && isConnected) {
      const message = {
        sender: user.username,
        content: newMessage,
        language: 'en', // Assuming English input
        targetLanguage: targetLanguage
      };

      try {
        stompClient.current.send('/app/send', {}, JSON.stringify(message));
        setNewMessage('');
      } catch (error) {
        console.error('Error sending message:', error);
      }
    }
  };

  const handleTyping = () => {
    if (stompClient.current && isConnected) {
      try {
        stompClient.current.send('/app/typing', {}, user.username);
      } catch (error) {
        console.error('Error sending typing indicator:', error);
      }
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
          <div className="connection-status">
            <span className={`status-indicator ${isConnected ? 'connected' : 'disconnected'}`}></span>
            <span className="status-text">{isConnected ? 'Connected' : 'Connecting...'}</span>
          </div>
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
        <button onClick={sendMessage} disabled={!newMessage.trim() || !isConnected}>
          {isConnected ? 'Send' : 'Connecting...'}
        </button>
      </div>
    </div>
  );
}

export default Chat;
