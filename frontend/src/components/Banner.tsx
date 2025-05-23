import { useState } from "react";
import styles from "../Banner/Banner.module.css";

const Banner = () => {
  const [visible, setVisible] = useState(true);

  if (!visible) return null;

  return (
    <div className={styles.banner}>
      <span className="banner-text">Banner</span>
      <button className="banner-close" onClick={() => setVisible(false)}>
        X
      </button>
    </div>
  );
};

export default Banner;
