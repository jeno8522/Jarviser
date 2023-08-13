import React, {useState, useEffect} from "react";
import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";
import {DndProvider, useDrag, useDrop} from "react-dnd";
import {HTML5Backend} from "react-dnd-html5-backend";
import "./WebSocketComponent.css";

const ItemType = {
  MESSAGE: "message",
};

const DraggableMessage = ({message, index, moveMessage}) => {
  const [, ref] = useDrag({
    type: ItemType.MESSAGE,
    item: {index},
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
      className={`chat-message ${index % 2 === 0 ? "sent" : "received"}`}
    >
      {message}
    </div>
  );
};

class WebSocketComponent extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      messages: [],
    };
  }

  componentDidMount() {
    const that = this;
    const socket = new SockJS("http://localhost:8081/ws");
    const stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
      stompClient.subscribe("/topic/meeting/" + 3, function (messageOutput) {
        let message = JSON.parse(messageOutput.body);
        let type = message.type;

        that.setState((prevState) => ({
          messages: [...prevState.messages, messageOutput.body],
        }));
      });
    });
  }

  moveMessage = (fromIndex, toIndex) => {
    const messages = [...this.state.messages];
    const [movedMessage] = messages.splice(fromIndex, 1);
    messages.splice(toIndex, 0, movedMessage);

    this.setState({
      messages,
    });
  };

  render() {
    return (
      <DndProvider backend={HTML5Backend}>
        <div className="chat-container">
          {this.state.messages.map((message, index) => (
            <DraggableMessage
              key={index}
              index={index}
              message={message}
              moveMessage={this.moveMessage}
            />
          ))}
        </div>
      </DndProvider>
    );
  }
}

export default WebSocketComponent;
