import { useNavigate } from "react-router-dom";
import GamesListSkeleton from "../pages/skeletons/GamesListSkeleton";

export default function GamesList({ games, loading }) {

  const navigate = useNavigate();
  
  const fitText = (element) => {
    if (!element) {
      return;
    }

    element.style.display = "inline-block";
    element.style.transformOrigin = "center";
    element.style.whiteSpace = "nowrap";

    const parentWidth = element.parentElement.clientWidth;
    const textWidth = element.scrollWidth;

    const scale = Math.min(1, (parentWidth / textWidth));

    element.style.transform = `scale(${scale})`;
  };

  if (loading) {
    return (<GamesListSkeleton />);
  }

  if (!games.length) {
    return <p className="text-center text-gray-500">No games found.</p>;
  }

  return (
    <ul className="grid gap-6 place-items-center
      grid-cols-[repeat(auto-fit,minmax(250px,1fr))]"
    >
      {games.map((game) => (
        <li
          key={game.id}
          onClick={() => navigate(`/products/games/details/${game.id}`)}
          className="w-full max-w-[250px] h-[300px] border-2 border-orange-400 rounded-2xl overflow-hidden shadow-lg
          justify-center items-center text-center bg-[#f2b93b] hover:scale-105 transition-transform duration-300 cursor-pointer"
        >
          <div className="flex items-center justify-center h-[180px] w-full">
            <img
              src={`${import.meta.env.VITE_GATEWAY_URL}/images/${game.coverImageUrl}`}
              alt={game.title}
              className="h-[180px] w-auto"
              onError={(e) => {
                e.target.src = "/images/default_game_cover.png";
                e.target.className = "h-[130px] w-auto opacity-10";
              }}
            />
          </div>
          <div className="py-2 px-6">
            <div className="flex justify-center">
              <h2 ref={fitText} className="text-lg font-bold mb-1">
                {game.title}
              </h2>
            </div>
            <hr className="border-t border-yellow-600 mb-2" />
            <p>{game.platform}</p>
            <p className="text-lg font-semibold text-gray-800">
              €{game.price?.toFixed(2)}
            </p>
          </div>
        </li>
      ))}
    </ul>
  );
}
