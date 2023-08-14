import React from "react";
import { Bar, Chart } from "react-chartjs-2";
import {Chart as ChartJS,
    CategoryScale,
    LinearScale,
    Legend,
    BarElement,
    Tooltip,
} from "chart.js";

ChartJS.register(
    CategoryScale,
    LinearScale,
    Legend,
    BarElement,
    Tooltip,
);

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
        backgroundColor: "rgba(75,192,192,0.6)",
        borderColor: "rgba(75,192,192,1)",
        borderWidth: 1,
      },
    ],
  };

  return (
    <div>
      <h2>Keyword Usage</h2>
      <Bar data={data} />
    </div>
  );
};

export default Keyword;
