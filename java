// -------------------------
// 🎄 스테이지 & 미션 데이터
// -------------------------
const missions = [
  { layers: 3, flavor: "chocolate", frost: "#FCF0A5", toppings: ["star", "tree"] },
  { layers: 2, flavor: "strawberry", frost: "#EDEDED", toppings: ["candy", "snow", "berry"] },
  { layers: 4, flavor: "vanilla", frost: "#EBA2A9", toppings: ["star", "candy"] }
];

let currentStage = 0;
let currentMission = missions[currentStage];

// -------------------------
// 🎂 플레이어 상태
// -------------------------
const layerCount = document.getElementById("layerCount");
const flavorSelect = document.getElementById("flavorSelect");
const frostColor = document.getElementById("frostColor");
const toppingSelect = document.getElementById("toppingSelect");
const addToppingBtn = document.getElementById("addTopping");

let state = {
  layers: parseInt(layerCount.value),
  flavor: flavorSelect.value,
  frost: frostColor.value,
  toppings: []
};

// -------------------------
// 🍰 캔버스 & 그리기 함수
// -------------------------
const missionCanvas = document.getElementById("missionCanvas");
const missionCtx = missionCanvas.getContext("2d");
const playerCanvas = document.getElementById("playerCanvas");
const playerCtx = playerCanvas.getContext("2d");

const flavorColors = {
  chocolate: "#7B3F00",
  strawberry: "#ff8fa2",
  vanilla: "#fff0c9",
  matcha: "#7bb661",
  cheese: "#f5d36e"
};

const toppingIcons = {
  star: "⭐",
  tree: "🎄",
  candy: "🍬",
  berry: "🍓",
  snow: "❄"
};

function drawCakeBase(ctx, layers, flavor, frost, toppings) {
  ctx.clearRect(0, 0, 300, 300);

  // 케이크 층
  for (let i = 0; i < layers; i++) {
    ctx.fillStyle = flavorColors[flavor];
    ctx.fillRect(70, 220 - i * 40, 160, 35);
  }

  // 프로스팅
  ctx.fillStyle = frost;
  ctx.fillRect(60, 220 - layers * 40 - 10, 180, 20);

  // 토핑
  ctx.font = "20px Arial";
  toppings.forEach((t, i) => {
    ctx.fillText(toppingIcons[t], 130 + i * 25, 200 - layers * 40 - 20);
  });
}

function drawMissionCake() {
  drawCakeBase(
    missionCtx,
    currentMission.layers,
    currentMission.flavor,
    currentMission.frost,
    currentMission.toppings
  );
}

function drawPlayerCake() {
  drawCakeBase(
    playerCtx,
    state.layers,
    state.flavor,
    state.frost,
    state.toppings
  );
}

// -------------------------
// 🎯 스테이지 로드
// -------------------------
function loadStage(stage) {
  currentStage = stage;
  currentMission = missions[stage];
  document.getElementById("stageText").textContent = `현재 스테이지: ${stage + 1}`;

  // 초기화
  state.toppings = [];
  state.layers = parseInt(layerCount.value);
  state.flavor = flavorSelect.value;
  state.frost = frostColor.value;

  drawMissionCake();
  drawPlayerCake();
}

// -------------------------
// 🧁 이벤트
// -------------------------
layerCount.addEventListener("input", e => { state.layers = parseInt(e.target.value); drawPlayerCake(); });
flavorSelect.addEventListener("change", e => { state.flavor = e.target.value; drawPlayerCake(); });
frostColor.addEventListener("change", e => { state.frost = e.target.value; drawPlayerCake(); });
addToppingBtn.addEventListener("click", () => { state.toppings.push(toppingSelect.value); drawPlayerCake(); });

// -------------------------
// 🧮 점수 계산
// -------------------------
function calculateScore(mission, player) {
  let score = 0;
  if (mission.layers === player.layers) score += 30;
  if (mission.flavor === player.flavor) score += 20;
  if (mission.frost === player.frost) score += 20;
  const correct = mission.toppings.filter(t => player.toppings.includes(t)).length;
  score += Math.round((correct / mission.toppings.length) * 30);
  return score;
}

// -------------------------
// 🎉 제출 버튼
// -------------------------
document.getElementById("submitBtn").addEventListener("click", () => {
  const score = calculateScore(currentMission, state);
  const scoreText = document.getElementById("scoreText");

  if (score >= 80) {
    scoreText.textContent = `점수: ${score}점 🎉 스테이지 클리어!`;
    if (currentStage < missions.length - 1) {
      setTimeout(() => { loadStage(currentStage + 1); }, 1200);
    } else {
      scoreText.textContent = "🎄✨ ALL STAGES CLEAR! 당신은 케이크 마스터! ✨🎄";
    }
  } else {
    scoreText.textContent = `점수: ${score}점 😢 다시 도전!`;
  }
});

// -------------------------
// 🎯 초기 스테이지 로드
// -------------------------
loadStage(0);