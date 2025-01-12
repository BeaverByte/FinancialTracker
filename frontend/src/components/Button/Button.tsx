import { PropsWithChildren } from "react";
import styles from "./Button.module.css";

type ButtonProps = {
  title?: string;
  children?: React.ReactNode;
  onClick?: React.MouseEventHandler<HTMLButtonElement>;
};

// function Button({ children, onClick, type, title }) {
function Button({ children, onClick, title }: ButtonProps) {
  return (
    <button
      title={title}
      onClick={onClick}
      // className={`${styles.btn} ${styles[title]}`}
    >
      {children}
    </button>
  );
}

export default Button;
