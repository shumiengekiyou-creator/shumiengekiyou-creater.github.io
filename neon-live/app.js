// ===== smooth scroll button
document.getElementById("btnScroll")?.addEventListener("click", () => {
  document.getElementById("lineup")?.scrollIntoView({ behavior: "smooth" });
});

// ===== ticket modal
const modal = document.getElementById("ticketModal");
document.getElementById("btnTicket")?.addEventListener("click", () => modal?.showModal());
document.getElementById("btnClose")?.addEventListener("click", () => modal?.close());

// ===== copy phrase
const phrase = "NEON-2026-ADMIT";
document.getElementById("btnCopy")?.addEventListener("click", async () => {
  const note = document.getElementById("copyNote");
  try {
    await navigator.clipboard.writeText(phrase);
    if (note) note.textContent = `コピーしました：${phrase}`;
  } catch {
    if (note) note.textContent = "コピーできなかった…（ブラウザの権限を確認してね）";
  }
});

// ===== countdown (Japan time)
const target = new Date("2026-03-21T19:00:00+09:00").getTime();
const pad2 = (n) => String(n).padStart(2, "0");

function tick() {
  const now = Date.now();
  let diff = Math.max(0, target - now);

  const d = Math.floor(diff / (1000 * 60 * 60 * 24));
  diff -= d * 1000 * 60 * 60 * 24;

  const h = Math.floor(diff / (1000 * 60 * 60));
  diff -= h * 1000 * 60 * 60;

  const m = Math.floor(diff / (1000 * 60));
  diff -= m * 1000 * 60;

  const s = Math.floor(diff / 1000);

  const el = (id) => document.getElementById(id);
  el("cdD").textContent = pad2(d);
  el("cdH").textContent = pad2(h);
  el("cdM").textContent = pad2(m);
  el("cdS").textContent = pad2(s);
}
tick();
setInterval(tick, 1000);

// ===== setlist (search + favorite)
const songs = [
  { title: "Neon Avenue", by: "Midnight Komachi" },
  { title: "Afterglow Signals", by: "Midnight Komachi" },
  { title: "Harbor Lights", by: "Harbor Glass" },
  { title: "Glass City", by: "Harbor Glass" },
  { title: "Riverside Walk", by: "Riverside Drive" },
  { title: "Late Train", by: "Riverside Drive" },
  { title: "Choir of Neon", by: "Neon Choir" },
  { title: "Zero Gravity", by: "Neon Choir" },
];

const key = "neonlive_favs";
const favs = new Set(JSON.parse(localStorage.getItem(key) || "[]"));

function saveFavs() {
  localStorage.setItem(key, JSON.stringify([...favs]));
}

function render(filterText = "") {
  const list = document.getElementById("list");
  if (!list) return;
  list.innerHTML = "";

  const q = filterText.trim().toLowerCase();
  const items = songs.filter(s =>
    (s.title + " " + s.by).toLowerCase().includes(q)
  );

  for (const s of items) {
    const li = document.createElement("li");
    li.className = "item";

    const left = document.createElement("div");
    left.className = "left";
    left.innerHTML = `<span class="song">${s.title}</span><span class="by">${s.by}</span>`;

    const btn = document.createElement("button");
    btn.className = "fav" + (favs.has(s.title) ? " on" : "");
    btn.type = "button";
    btn.textContent = favs.has(s.title) ? "★ Fav" : "☆ Fav";
    btn.addEventListener("click", () => {
      if (favs.has(s.title)) favs.delete(s.title);
      else favs.add(s.title);
      saveFavs();
      render(document.getElementById("q")?.value || "");
    });

    li.append(left, btn);
    list.append(li);
  }
}
render();

document.getElementById("q")?.addEventListener("input", (e) => {
  render(e.target.value);
});
