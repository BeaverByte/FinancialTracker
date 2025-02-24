// src/components/Layout.jsx

import Navbar from "./Navbar/Navbar";
import Banner from "./Banner/Banner";

/**
 * Acts as template for UI elements that should be shared across pages
 *
 */
const AuthLayout = () => {
  return (
    <div>
      <Navbar />
      <Banner />
    </div>
  );
};

export default AuthLayout;
