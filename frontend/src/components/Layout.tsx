// src/components/Layout.jsx

import { Outlet } from "react-router";
import Navbar from "./Navbar/Navbar";

/**
 * Acts as template for UI elements that should be shared across pages
 *
 */
const Layout = () => {
  return (
    <div>
      <Navbar />
      <main>
        <Outlet /> {/* Route content */}
      </main>
    </div>
  );
};

export default Layout;
