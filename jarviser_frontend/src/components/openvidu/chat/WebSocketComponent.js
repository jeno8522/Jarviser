import React, {useState, useEffect} from "react";
import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";
import "./WebSocketComponent.css";
class WebSocketComponent extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      messages: [],
    };
  }

  componentDidMount() {
    const that = this;
    var socket = new SockJS("http://localhost:8081/ws");
    var stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
      stompClient.subscribe("/topic/meeting/" + 3, function (messageOutput) {
        let message = JSON.parse(messageOutput.body);
        let type = message.type;

        // 현재의 메세지들에 새로운 메세지를 추가
        that.setState((prevState) => ({
          messages: [...prevState.messages, messageOutput.body],
        }));
      });
    });
  }

  render() {
    return (
      <div className="chat-container">
        {this.state.messages.map((message, index) => {
          // 임의로 메세지 타입을 결정 (실제 로직에서는 메세지의 소스에 따라 결정)
          const messageType = index % 2 === 0 ? "sent" : "received";
          return (
            <div key={index} className={`chat-message ${messageType}`}>
              {message}
            </div>
          );
        })}
      </div>
    );
  }
}

export default WebSocketComponent;
