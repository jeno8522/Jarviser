import React, { useState, useEffect } from 'react';

const WebSocketComponent = () => {
  // 웹소켓 객체와 메시지 목록을 state로 관리합니다.
  const [ws, setWs] = useState(null);
  const [messages, setMessages] = useState([]);
  const [messageInput, setMessageInput] = useState('');
  const [participants, setParticipants] = useState(0);

  // 컴포넌트 마운트 시 웹소켓 연결을 설정합니다.
  useEffect(() => {
    const topic = "testTopic";
    const websocket = new WebSocket('ws://localhost:8080/'+topic);
    setWs(websocket);

    // 메시지 수신 시 처리
    websocket.onmessage = (event) => {
      const data = JSON.parse(event.data);
      if (data.participants !== undefined) {
        setParticipants(data.participants);
      }
      setMessages((prevMessages) => [...prevMessages, `Received: ${event.data}`]);
    };

    // 연결 해제 시 정리
    return () => {
      websocket.close();
    };
  }, []);

  // 메시지 전송 함수
  const sendMessage = () => {
    if (ws && ws.readyState === WebSocket.OPEN) {
      ws.send(messageInput);
      setMessages((prevMessages) => [...prevMessages, `Sent: ${messageInput}`]);
      setMessageInput('');
    }
  };

  return (
    <div>
      <div>Participants: {participants}</div>
      <div>
        <input
          type="text"
          value={messageInput}
          onChange={(e) => setMessageInput(e.target.value)}
        />
        <button onClick={sendMessage}>Send</button>
      </div>
      <ul>
        {messages.map((msg, index) => (
          <li key={index}>{msg}</li>
        ))}
      </ul>
    </div>
  );
};

export default WebSocketComponent;
