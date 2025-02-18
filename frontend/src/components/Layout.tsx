// src/components/Layout.jsx

import { Outlet } from "react-router";
import Navbar from "./Navbar/Navbar";
import Banner from "./Banner/Banner";

/**
 * Acts as template for UI elements that should be shared across pages
 *
 */
const Layout = () => {
  return (
    <div>
      <Navbar />
      <Banner />
      <main>
        <Outlet /> {/* Route content */}
      </main>
    </div>
  );
};

export default Layout;
