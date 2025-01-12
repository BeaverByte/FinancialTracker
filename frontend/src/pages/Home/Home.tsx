import styles from "./Home.module.css";
import Banner from "../../components/Banner/Banner.tsx";

export default function Home() {
  //   const { resetQuestionnaire } = UseQuestions();
  // const navigate = useNavigate();

  return (
    <div className={styles.home}>
      <Banner />
      <h1>Eligibility Tools</h1>
    </div>
  );
}
