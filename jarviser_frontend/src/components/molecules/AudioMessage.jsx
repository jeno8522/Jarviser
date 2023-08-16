import React, { useState } from "react";

const AudioMessage = ({ audioMessage, onEditClick, onSaveClick }) => {
  const [editedContent, setEditedContent] = useState(audioMessage.content);

  const handleSaveClick = () => {
    onSaveClick(editedContent);
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
