import React from "react";
import { PieChart } from "react-minimal-pie-chart";

const Speech = ({ speechPercentage }) => {
  // 데이터 필터링: NaN이 아닌 값만 추출하여 newData 배열에 저장
  const newData = speechPercentage.filter(item => !isNaN(item.percentage));

  // newData 배열을 기반으로 그래프 데이터 구성
  const data = newData.map(({ name, percentage }) => ({
    title: name,
    value: percentage,
    color: getRandomColor(), // 랜덤 컬러 생성 함수 호출
  }));

  return (
    <div>
      <h2>Speech Percentage</h2>
      <PieChart data={data} />
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

export default Speech;
