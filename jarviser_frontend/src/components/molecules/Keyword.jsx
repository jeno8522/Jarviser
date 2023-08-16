import React from "react";
import { Bar, Chart } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  Legend,
  BarElement,
  Tooltip,
} from "chart.js";
import styled from "styled-components";

ChartJS.register(CategoryScale, LinearScale, Legend, BarElement, Tooltip);

const Keyword = ({ staticsOfKeywords }) => {
  if (!staticsOfKeywords || staticsOfKeywords.length === 0) {
    return <p>No keyword data available.</p>;
  }

  const data = {
    labels: staticsOfKeywords.map((item) => item.keyword),
    datasets: [
      {
        label: "Keyword Usage",
        data: staticsOfKeywords.map((item) => item.percent),
        backgroundColor: staticsOfKeywords.map(() => getRandomColor()), // 랜덤 컬러 생성 함수 호출
        borderColor: "rgba(75,192,192,1)",
        borderWidth: 1,
      },
    ],
  };

  return (
    <div>
      <TextBackground>
        <span>회의 주요 키워드</span>
      </TextBackground>
      <Bar data={data} />
    </div>
  );
};

// 랜덤 컬러 생성 함수
function getRandomColor() {
  const letters = "0123456789ABCDEF";
  let color = "#";
  for (let i = 0; i < 6; i++) {
    color += letters[Math.floor(Math.random() * 16)];
  }
  return color;
}

export default Keyword;

const TextBackground = styled.div`
  span {
    background-color: #cae1fd;
    padding: 8px;
    font-size: 20px;
    font-weight: bold;
    border-radius: 10px;
  }
`;
