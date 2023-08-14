import React from "react";

const AudioMessage = ({ audioMessages }) => {
  if (!audioMessages || audioMessages.length === 0) {
    return <p>No audio messages available.</p>;
  }

  return (
    <div>
      <h2>Audio Messages</h2>
      <ul>
        {audioMessages.map((message, index) => (
          <li key={index}>
            <p>Name: {message.name}</p>
            <p>Content: {message.content}</p>
            <p>Length: {message.length}</p>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default AudioMessage;
