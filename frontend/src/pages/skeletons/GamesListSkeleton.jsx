//const placeholderBg = "bg-[#bfc4c8]";
const placeholderBg = "bg-gray-800/10"

export default function GamesListSkeleton() {
  return (
    <ul className="grid gap-6 place-items-center
      grid-cols-[repeat(auto-fit,minmax(250px,1fr))] animate-pulse">
      {[1, 2, 3, 4, 5, 6].map((i) => (
        <li
          key={i}
          className="
            max-w-[250px] h-[300px] rounded-xl overflow-hidden shadow-lg
            bg-[#f2b93b]
          "
        >
          <div className="flex items-center justify-center h-[180px] w-full">
            <img
              src="/images/default_game_cover.png"
              className="h-[130px] w-auto opacity-10"
            />
          </div>
          <div className="py-2 px-6">
            <div className="flex justify-center">
              <div className={`h-5 w-40 rounded ${placeholderBg}`} />
            </div>
            <hr className="border-t border-gray-400 mb-2 mt-2" />
            <div className={`h-4 w-24 rounded mx-auto ${placeholderBg}`} />
            <div className={`h-5 w-20 rounded mx-auto mt-2 ${placeholderBg}`} />
          </div>
        </li>
      ))}
    </ul>
  );
}
