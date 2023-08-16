import React, { useState } from "react";
import axios from "axios";

const AudioMessage = ({ audioMessage, onEditClick, onSaveClick, accessToken }) => {
  const [editedContent, setEditedContent] = useState(audioMessage.content);

  const handleSaveClick = async () => {
    try {
      await onSaveClick(editedContent);

      const response = await axios.post(
        "http://localhost:8081/meeting/audiomessage/update",
        {
          audioMessageId: audioMessage.audioMessageId,
          content: editedContent,
        },
        {
          headers: { Authorization: `Bearer ${accessToken}` },
        }
      );

      if (response.status === 200) {
        onSaveClick(editedContent);
      }
    } catch (error) {
      console.error("Error updating audio message:", error);
    }
  };

  return (
    <div>
      <p>Name: {audioMessage.name}</p>
      {audioMessage.isEditing ? (
        <div>
          <textarea
            value={editedContent}
            onChange={(e) => setEditedContent(e.target.value)}
          />
          <button onClick={handleSaveClick}>Save</button>
        </div>
      ) : (
        <div>
          <p>Content: {audioMessage.content}</p>
          <button onClick={onEditClick}>Edit</button>
        </div>
      )}
    </div>
  );
};

export default AudioMessage;
