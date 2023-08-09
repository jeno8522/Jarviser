const WebSocket = require("ws");

const wss = new WebSocket.Server({ port: 8080 });

wss.on("connection", (ws) => {
  // 새로운 연결 시 참여자 수 전송
  wss.clients.forEach((client) => {
    if (client.readyState === WebSocket.OPEN) {
      client.send(JSON.stringify({ participants: wss.clients.size }));
    }
  });

  ws.on("close", () => {
    // 연결 해제 시 참여자 수 전송
    wss.clients.forEach((client) => {
      if (client.readyState === WebSocket.OPEN) {
        client.send(JSON.stringify({ participants: wss.clients.size }));
      }
    });
  });
  ws.on("message", (message) => {
    // 클라이언트로부터 메시지 수신 시 모든 클라이언트에게 전달
    wss.clients.forEach((client) => {
      if (client !== ws && client.readyState === WebSocket.OPEN) {
        client.send(JSON.stringify({ type: "message", content: message }));
      }
    });
  });
});

console.log("WebSocket server started on ws://localhost:8080");
