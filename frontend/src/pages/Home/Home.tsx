import styles from "./Home.module.css";
import Banner from "../../components/Banner/Banner.tsx";

export default function Home() {
  return (
    <div className={styles.home}>
      <Banner />
      <h1>Eligibility Tools</h1>
    </div>
  );
}
