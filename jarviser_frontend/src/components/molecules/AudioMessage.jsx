import React, { useState } from "react";

const AudioMessage = ({ audioMessages, setAudioMessages }) => {
  const [editedIndex, setEditedIndex] = useState(-1);
  const [editedContent, setEditedContent] = useState("");

  const handleEditClick = (index) => {
    setEditedIndex(index);
    setEditedContent(audioMessages[index].content);
  };

  const handleSaveClick = (index) => {
    // TODO: 수정된 내용을 저장하고 DB에 업데이트
    // 예시: 서버로 수정된 내용을 전송하여 DB 업데이트

    // 더미 데이터 업데이트 예시 (실제로는 서버 요청 필요)
    const newAudioMessages = [...audioMessages];
    newAudioMessages[index].content = editedContent;
    setAudioMessages(newAudioMessages);

    setEditedIndex(-1);
    setEditedContent("");
  };

  return (
    <div>
      <h2>오디오 메세지</h2>
      <ul>
        {audioMessages.map((message, index) => (
          <li key={index}>
            <p>Name: {message.name}</p>
            {editedIndex === index ? (
              <div>
                <textarea
                  value={editedContent}
                  onChange={(e) => setEditedContent(e.target.value)}
                />
                <button onClick={() => handleSaveClick(index)}>Save</button>
              </div>
            ) : (
              <>
                <p>Content: {message.content}</p>
                <button onClick={() => handleEditClick(index)}>Edit</button>
              </>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default AudioMessage;
