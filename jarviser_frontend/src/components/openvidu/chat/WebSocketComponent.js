import React, {useState, useEffect} from "react";
import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";
import {DndProvider, useDrag, useDrop} from "react-dnd";
import {HTML5Backend} from "react-dnd-html5-backend";
import "./WebSocketComponent.css";
import {useMicVAD} from "@ricky0123/vad-react";
const ItemType = {
  MESSAGE: "message",
};
const SttComponent = (props) => {
  const token = localStorage.getItem("access-token");
  const meetingId = props.meetingId;
  const vad = useMicVAD({
    startOnLoad: true,
    onSpeechStart: () => {
      console.log("speech start");
    },
    onSpeechEnd: (audio) => {
      console.log("speech end");
      // do something with `audio` (Float32Array of audio samples at sample rate 16000)...
      const audioBlob = _float32ArrayToWav(audio, 16000);
      _sendAudio(audioBlob);
    },
  });
  useEffect(() => {
    vad.start();
    console.log(vad.loading + "?????");
  }, []);

  const _sendAudio = async (blob) => {
    try {
      const url = "http://localhost:8081/audio/transcript";
      const formData = new FormData();
      formData.append("file", blob);
      formData.append("meetingId", meetingId);
      const response = await fetch(url, {
        method: "POST",
        body: formData,
        headers: {Authorization: "Bearer " + token},
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const data = await response.json();
      console.log(data.text);
    } catch (error) {
      console.error("Error sending audio", error);
    }
  };

  const _float32ArrayToWav = (audioData, sampleRate) => {
    const buffer = new ArrayBuffer(44 + audioData.length * 2);
    const view = new DataView(buffer);

    function writeString(view, offset, string) {
      for (let i = 0; i < string.length; i++) {
        view.setUint8(offset + i, string.charCodeAt(i));
      }
    }

    // RIFF header
    writeString(view, 0, "RIFF");
    view.setUint32(4, 32 + audioData.length * 2, true);
    writeString(view, 8, "WAVE");

    // fmt chunk
    writeString(view, 12, "fmt ");
    view.setUint32(16, 16, true);
    view.setUint16(20, 1, true); // PCM format
    view.setUint16(22, 1, true); // mono
    view.setUint32(24, sampleRate, true);
    view.setUint32(28, sampleRate * 2, true);
    view.setUint16(32, 2, true);
    view.setUint16(34, 16, true);

    // data chunk
    writeString(view, 36, "data");
    view.setUint32(40, audioData.length * 2, true);

    const volume = 1;
    let index = 44;
    for (let i = 0; i < audioData.length; i++) {
      view.setInt16(index, audioData[i] * (0x7fff * volume), true);
      index += 2;
    }
    return new Blob([view], {type: "audio/wav"});
  };

  return <div>{vad.userSpeaking && "User is speaking"}</div>;
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
      messages: [
        '{"time": "16:32:50", "type": "connect", "userName": "1번참가자", "userId": "1", "content": "1번참가자의뻘소리"}',
        '{"time": "16:32:51", "type": "connect", "userName": "2번참가자", "userId": "2", "content": "2번참가자의뻘소리"}',
        '{"time": "16:32:52", "type": "connect", "userName": "3번참가자", "userId": "3", "content": "3번참가자의뻘소리"}',
      ],

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
        this.setState({userId: parsedToken.userId});
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
        JSON.stringify({meetingId: meetingId, Authorization: "Bearer " + token})
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
    this.printIndexes(this.state.draggedIndex, toIndex);
    this.setState({draggedIndex: null});
  };

  render() {
    return (
      <DndProvider backend={HTML5Backend}>
        <div className="chat-container">
          <div className="chat-title">STT 채팅</div>
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
        <SttComponent meetingId={this.state.meetingId} />
      </DndProvider>
    );
  }
}

export default WebSocketComponent;
