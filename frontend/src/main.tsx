import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
// import ReactDOM from "react-dom";
import "./index.css";
import App from "./App.tsx";
import { API_ROUTES } from "./utility/API_ROUTES.ts";
// import React from "react";

async function enableMocking() {
  console.log(`API Route is ${API_ROUTES.TRANSACTIONS.ENDPOINT}`);
  console.log(`Currently in "${process.env.NODE_ENV}" environment`);

  if (process.env.NODE_ENV !== "development") {
    console.log("App not in NODE development mode");
    return;
  }
  if (!import.meta.env.VITE_ENABLE_MSW) {
    console.log("App is not in testing, MSW disabled");
    return;
  }

  const { worker } = await import("./mocks/browser");

  // `worker.start()` returns a Promise that resolves
  // once the Service Worker is up and ready to intercept requests.
  return worker.start();
}

// enableMocking().then(() => {
//   ReactDOM.createRoot(document.getElementById("root")!).render(
//     <React.StrictMode>
//       <App />
//     </React.StrictMode>
//   );
// });

const container = document.getElementById("root");

if (container) {
  const root = createRoot(container);

  enableMocking().then(() => {
    root.render(
      <StrictMode>
        <App />
      </StrictMode>
    );
  });
} else {
  throw new Error(
    "Root element with ID 'root' was not found in the document. Ensure there is a corresponding HTML element with the ID 'root' in your HTML file."
  );
}
