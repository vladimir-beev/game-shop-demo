import { useEffect, useState } from "react";

export default function Alert({ message, type, onClose }) {
  const [visible, setVisible] = useState(false);

  const styles = {
    success: "bg-green-600 text-white",
    error: "bg-red-600 text-white",
    warning: "bg-yellow-500 text-black",
  };

  useEffect(() => {
    setVisible(true);

    const timer = setTimeout(() => {
      setVisible(false);
      setTimeout(() => onClose?.(), 300);
    }, 2500);

    return () => clearTimeout(timer);
  }, []);

  return (
    <div
      className={`
        fixed top-20 right-8 z-50
        px-6 py-3 rounded-lg shadow-lg font-semibold
        transition-all duration-300
        ${styles[type]}
        ${visible ? "opacity-100 translate-x-0" : "opacity-0 translate-x-6"}
      `}
    >
      {message}
    </div>
  );
}
