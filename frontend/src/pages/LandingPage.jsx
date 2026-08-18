import { useNavigate } from "react-router-dom";

export default function LandingPage() {
    const navigate = useNavigate();

    return (
      <div className="flex flex-1 flex-col mx-auto">
        <div className="grid sm:grid-cols-1 lg:grid-cols-2 m-auto gap-5 p-6">
          <div
            onClick={() => navigate("/products/games")}
            className="bg-blue-600 text-white text-gray-100 aspect-[4/3] w-full min-w-[25vw] rounded-2xl p-6
                  flex flex-col items-center justify-center text-2xl font-bold shadow-lg
                   hover:scale-103 transition-transform duration-300 cursor-pointer border-3 border-blue-700"
          >
            <img
              src="/images/default_game_cover.png"
              alt="games"
              className="h-[clamp(80px,50%,200px)] object-cover mb-4"
            />
            <p className="text-xl md:text-2xl xl:text-4xl font-bold">Video Games</p>
          </div>
          <div className="flex flex-col gap-5 aspect-[4/3] w-full min-w-[25vw]">
            <div
              className="flex-1 bg-indigo-300 rounded-2xl flex items-center justify-center text-black/50
                text-xl md:text-2xl xl:text-4xl font-bold shadow-lg"
            >
              Coming Soon
            </div>
            <div
              className="flex-1 bg-indigo-300 rounded-2xl flex items-center justify-center text-black/50 text-2xl
                text-xl md:text-2xl xl:text-4xl font-bold shadow-lg"
            >
              Coming Soon
            </div>
          </div>
        </div>
      </div>
    );
}