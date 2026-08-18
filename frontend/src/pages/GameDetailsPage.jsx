import { useEffect, useState, useContext } from "react";
import { useParams, useNavigate, useOutletContext } from "react-router-dom";
import { AuthContext } from "../auth/AuthContext";
import api from "../api";
import DescriptionSection from "../components/DescriptionSection.jsx";
import Alert from "../components/Alert";
import GameDetailsSkeleton from "./skeletons/GameDetailsSkeleton";

export default function GameDetailsPage() {
  const { id } = useParams();
  const { accessToken } = useContext(AuthContext);
  const showAlert = useOutletContext();
  const navigate = useNavigate();

  const [game, setGame] = useState(null);
  const [availability, setAvailability] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadGameDetails() {
      setLoading(true);

      const gameResponse = await api.get(`/products/games/${id}`);
      const gameData = gameResponse.data;

      const availResponse = await api.get(`/inventory/${id}/availability`);
      const availData = availResponse.data;

      setGame(gameData);
      setAvailability(availData.availability);

     setLoading(false);
    }

    loadGameDetails();
  }, [id]);

  async function handleAddToCart() {
    if (!accessToken) {
      navigate(`/login?redirect=/products/games/details/${id}`);
      return;
    }

    try {
      const response = await api.post("/cart/items", {
        productId: id,
        quantity: 1,
      });
    } 
    catch (error) {
      console.error("Error adding to cart:", error);
      
      showAlert({
        message: "Failed to add product to cart",
        type: "error",
      });
    }

    showAlert({
      message: "Product added to cart!",
      type: "success",
    });
  }

  if (loading) {
    return (<GameDetailsSkeleton />);
  }

  return (
    <div className="flex flex-1 flex-col mx-auto bg-gray-200/70">
      <div className="flex flex-wrap gap-8 mx-10 mt-10">
        <div className="flex items-center justify-center w-[300px] h-[350px]">
          <img
            src={`${import.meta.env.VITE_GATEWAY_URL}/images/${game.coverImageUrl}`}
            alt={game.title}
            onError={(e) => {
              e.target.src = "/images/default_game_cover.png";
              e.target.className = "w-[300px] h-[300px] opacity-10";
            }}
            className="w-[300px] h-[350px] object-cover rounded-lg"
          />
        </div>
        <div className="flex flex-col gap-2">
          <h1 className="text-2xl font-bold mb-2">{game.title}</h1>
          <hr className="border-t border-gray-400 mb-4" />
          <p>
            <strong>Platform:</strong> {game.platform}
          </p>
          <p>
            <strong>Price:</strong> €{game.price?.toFixed(2)}
          </p>
          <hr className="border-t border-gray-400 mt-2 mb-2" />
          <p>
            <strong>Availability:</strong>{" "}
            <span
              className={
                availability === "IN_STOCK"
                  ? "text-green-600 font-semibold"
                  : availability === "LOW_STOCK"
                    ? "text-yellow-600 font-semibold"
                    : "text-red-600 font-semibold"
              }
            >
              {availability === "IN_STOCK" && "Available"}
              {availability === "LOW_STOCK" && "Low Stock"}
              {availability === "OUT_OF_STOCK" && "Out of Stock"}
            </span>
          </p>
          <button
            onClick={handleAddToCart}
            disabled={availability === "OUT_OF_STOCK"}
            className="mt-8 px-4 py-2 w-[150px] rounded-md text-white shadow-md
              font-semibold cursor-pointer bg-blue-600 hover:bg-blue-700 transition
              disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:bg-blue-600"
          >
            Add to Cart
          </button>
        </div>
      </div>
      <hr className="border-t border-gray-500 m-6" />
      <DescriptionSection description={game.description} />
      <hr className="border-t border-gray-500 m-6" />
      <div className="mb-6 bg-blue-100 border-2 border-gray-600 rounded-lg mx-10 p-10 min-h-[300px]">
        Comments (PlaceHolder)
      </div>
    </div>
  );
}
