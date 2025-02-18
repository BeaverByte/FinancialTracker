type ButtonProps = {
  type: React.ButtonHTMLAttributes<HTMLButtonElement>["type"];
  children?: React.ReactNode;
  onClick?: React.MouseEventHandler<HTMLButtonElement>;
};

function Button({ children, onClick, type }: Readonly<ButtonProps>) {
  return (
    <button type={type} onClick={onClick}>
      {children}
    </button>
  );
}

export default Button;
