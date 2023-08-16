import { useMicVAD } from "@ricky0123/vad-react";
import { useEffect } from "react";

const MyComponent = () => {
  const vad = useMicVAD({
    startOnLoad: true,
    onSpeechEnd: (audio) => {
      console.log("User stopped talking");
    },
  });
  useEffect(() => {
    vad.start();
    console.log(vad.loading + "?????");
    console.log(vad);
  }, []);

  return <div>{vad.userSpeaking && "User is speaking"}</div>;
};

export default MyComponent;
