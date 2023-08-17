import React, {useState, useEffect} from "react";
import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";
import {DndProvider, useDrag, useDrop} from "react-dnd";
import {HTML5Backend} from "react-dnd-html5-backend";
import "./WebSocketComponent.css";
import SttComponent from "./stt/SttComponent";
import axios from "axios";

const ItemType = {
  MESSAGE: "message",
};

//현웅 이거 만들어놓음 에러날 순 있음
const handleMoveMesseage = async (from, to) => {
  // event.preventDefault();
  console.log("from === ", from, "to === ", to);
  const accessToken = localStorage.getItem("access-token");

  const endpoint = `http://localhost:8081/meeting/현웅,값을넣어야해`;
  // 미팅을 생성하기 위해 서버에 요청을 보냅니다.
  try {
    const response = await axios.post(
      endpoint,

      {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
        data: {from: from, to: to},
      }
    );

    console.log("response === ", response);
    if (response.status === 202) {
      console.log("Move Messeage Success!!!", response.data);
      //로직 추가 해줘야해 현웅
    } else {
      console.error("Error creating meeting:", response.data);
      alert("Error !!!. Please try again.");
    }
  } catch (error) {
    console.error("Error:", error);
    alert("An error occurred !!. Please try again.");
  }
};
const DraggableMessage = ({message, index, moveMessage, userId}) => {
  const [{isDragging}, ref] = useDrag({
    type: ItemType.MESSAGE,
    item: {index},
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
    drop: (draggedItem) => {
      console.log(`Dragged from: ${draggedItem.index}, Dropped to: ${index}`);
      // handleMoveMesseage(draggedItem.index, index);  현웅 이거 실행하면 axios 보내짐
    },
  });

  return (
    <>
      <div
        ref={(node) => ref(drop(node))}
        className={`chat-message ${isDragging ? "dragging" : ""} ${
          message.userId == userId ? "sent" : "received"
        }`}
      >
        <div id="sttChatUserName">{message.userName} : </div>
        {message.content}
      </div>
    </>
  );
};

class WebSocketComponent extends React.Component {
  constructor(props) {
    super(props);
    let meetingId = this.props.meetingId;
    this.state = {
      messages: [
        '{"time": "16:32:50", "type": "connect", "userName": "1번참가자", "userId": "1", "content": "의뻘소리"}',
        '{"time": "16:32:51", "type": "connect", "userName": "2번참가자", "userId": "2", "content": "의뻘소리"}',
        '{"time": "16:32:52", "type": "connect", "userName": "3번참가자", "userId": "3", "content": "의뻘소리"}',
        '{"time": "16:32:52", "type": "connect", "userName": "3번참가자", "userId": "3", "content": "의뻘소리"}',
        '{"time": "16:32:52", "type": "connect", "userName": "3번참가자", "userId": "3", "content": "의뻘소리"}',
        '{"time": "16:32:52", "type": "connect", "userName": "3번참가자", "userId": "3", "content": "의뻘소리"}',
        '{"time": "16:32:52", "type": "connect", "userName": "3번참가자", "userId": "3", "content": "의뻘소리"}',
        '{"time": "16:32:52", "type": "connect", "userName": "3번참가자", "userId": "3", "content": "의뻘소리"}',
        '{"time": "16:32:52", "type": "connect", "userName": "3번참가자", "userId": "3", "content": "의뻘소리"}',
      ],

      meetingId: meetingId,
      draggedIndex: null, // 드래그가 시작된 인덱스를 저장할 state
      userId: null, // userId 상태
    };
    this.chatContainerRef = React.createRef();
  }

  componentDidMount() {
    const that = this;
    // 로컬 스토리지에서 토큰을 가져옵니다.
    const token = localStorage.getItem("access-token");
    if (token) {
      const parsedToken = JSON.parse(atob(token.split(".")[1]));
      if (parsedToken && parsedToken.userId) {
        this.setState({userId: parsedToken.userId});
      }
    }
    const socket = new SockJS(window.SERVER_URL+"/ws");
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
        JSON.stringify({meetingId: meetingId, Authorization: "Bearer " + token})
      );
    });
    if (this.chatContainerRef.current) {
      const scrollHeight = this.chatContainerRef.current.scrollHeight;
      this.chatContainerRef.current.scrollTop = scrollHeight;
    }
  }
  componentDidUpdate(prevProps, prevState) {
    if (prevState.messages.length !== this.state.messages.length) {
      if (this.chatContainerRef.current) {
        const scrollHeight = this.chatContainerRef.current.scrollHeight;
        this.chatContainerRef.current.scrollTop = scrollHeight;
      }
    }
  }
  moveMessage = (fromIndex, toIndex) => {
    // 드래그 시작 시 draggedIndex를 설정
    if (this.state.draggedIndex === null) {
      this.setState({draggedIndex: fromIndex});
      return; // 여기서 종료하면 아이템의 위치는 실제로 이동하지 않습니다.
    }

    const messages = [...this.state.messages];
    const [movedMessage] = messages.splice(fromIndex, 1);
    messages.splice(toIndex, 0, movedMessage);

    this.setState({
      messages,
    });

    // 끝날 때 draggedIndex와 toIndex를 함께 출력하고, draggedIndex를 다시 null로 초기화
    // this.printIndexes(this.state.draggedIndex, toIndex);
    this.setState({draggedIndex: null});
  };

  render() {
    return (
      <DndProvider backend={HTML5Backend}>
        <div className="chat-container" ref={this.chatContainerRef}>
          <div className="chat-title">STT 채팅</div>
          {this.state.messages.map((message, index) => (
            <DraggableMessage
              key={index}
              index={index}
              message={JSON.parse(message)}
              moveMessage={this.moveMessage}
              userId={this.state.userId}
              userName={this.state.userName}
            />
          ))}
        </div>
        <SttComponent meetingId={this.state.meetingId} />
      </DndProvider>
    );
  }
}

export default WebSocketComponent;
