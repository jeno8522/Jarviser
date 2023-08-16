import React, { useState, useEffect } from "react";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import { DndProvider, useDrag, useDrop } from "react-dnd";
import { HTML5Backend } from "react-dnd-html5-backend";
import "./WebSocketComponent.css";
import SttComponent from "./stt/SttComponent";
const ItemType = {
  MESSAGE: "message",
};

const DraggableMessage = ({ message, index, moveMessage, userId }) => {
  const [{ isDragging }, ref] = useDrag({
    type: ItemType.MESSAGE,
    item: { index },
    collect: (monitor) => ({
      isDragging: !!monitor.isDragging(),
    }),
  });

  const [, drop] = useDrop({
    accept: ItemType.MESSAGE,
    hover: (draggedItem) => {
      if (draggedItem.index !== index) {
        moveMessage(draggedItem.index, index);
        draggedItem.index = index;
      }
    },
  });

  return (
    <div
      ref={(node) => ref(drop(node))}
      className={`chat-message ${
        (console.log(
          "이번 메세지의 유저아이디는" + message.userId,
          "현재 로그인한 유저아이디는" + userId
        ),
        message.userId == userId ? "sent" : "received")
      }`}
    >
      {message.content}
    </div>
  );
};

class WebSocketComponent extends React.Component {
  constructor(props) {
    super(props);
    let meetingId = this.props.meetingId;
    this.state = {
      messages: [],
      meetingId: meetingId,
      draggedIndex: null, // 드래그가 시작된 인덱스를 저장할 state
      userId: null, // userId 상태
    };
  }

  componentDidMount() {
    const that = this;
    // 로컬 스토리지에서 토큰을 가져옵니다.
    const token = localStorage.getItem("access-token");
    if (token) {
      const parsedToken = JSON.parse(atob(token.split(".")[1]));
      if (parsedToken && parsedToken.userId) {
        this.setState({ userId: parsedToken.userId });
      }
    }
    const socket = new SockJS("http://localhost:8081/ws");
    const stompClient = Stomp.over(socket);
    const meetingId = this.state.meetingId;
    stompClient.connect({}, function (frame) {
      stompClient.subscribe("/topic/" + meetingId, function (messageOutput) {
        let message = JSON.parse(messageOutput.body);
        let type = message.type;
        that.setState((prevState) => ({
          messages: [...prevState.messages, messageOutput.body],
        }));
      });
      stompClient.send(
        "/app/connect",
        {},
        JSON.stringify({ meetingId: meetingId, Authorization: "Bearer " + token })
      );
    });
  }
  // 드래그와 드랍 인덱스를 출력하는 메서드
  printIndexes = (draggedIndex, droppedIndex) => {
    console.log(`Dragged from: ${draggedIndex}, Dropped to: ${droppedIndex}`);
  };

  moveMessage = (fromIndex, toIndex) => {
    // 드래그 시작 시 draggedIndex를 설정
    if (this.state.draggedIndex === null) {
      this.setState({ draggedIndex: fromIndex });
      return; // 여기서 종료하면 아이템의 위치는 실제로 이동하지 않습니다.
    }

    const messages = [...this.state.messages];
    const [movedMessage] = messages.splice(fromIndex, 1);
    messages.splice(toIndex, 0, movedMessage);

    this.setState({
      messages,
    });

    // 끝날 때 draggedIndex와 toIndex를 함께 출력하고, draggedIndex를 다시 null로 초기화
    this.printIndexes(this.state.draggedIndex, toIndex);
    this.setState({ draggedIndex: null });
  };

  render() {
    return (
      <DndProvider backend={HTML5Backend}>
        <div className="chat-container">
          {this.state.messages.map((message, index) => (
            <DraggableMessage
              key={index}
              index={index}
              message={JSON.parse(message)}
              moveMessage={this.moveMessage}
              userId={this.state.userId}
            />
          ))}
        </div>
        <SttComponent meetingId={this.props.meetingId} />
      </DndProvider>
    );
  }
}

export default WebSocketComponent;
