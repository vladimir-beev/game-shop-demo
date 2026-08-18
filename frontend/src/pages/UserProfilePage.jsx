import { useState, useEffect, useContext } from "react";
import { AuthContext } from "../auth/AuthContext";
import api from "../api";
import ProfileEditModal from "../components/ProfileEditModal";

export default function UserProfilePage() {
  const [email, setEmail] = useState("");
  const [username, setUsername] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");

  const [isModalOpen, setIsModalOpen] = useState(false);

  const { accessToken, user } = useContext(AuthContext);

   const firstLetter = user?.[0]?.toUpperCase() || "U";

  useEffect(() => {
    if (!accessToken) return;

    async function fetchUserProfile() {
      try {
        const response = await api.get("/user/profile");
        const data = response.data;

        setEmail(data.email);
        setUsername(user);
        setFirstName(data.firstName);
        setLastName(data.lastName);
        setPhoneNumber(data.phoneNumber);
      } 
      catch (error) {
        console.error("Failed to fetch profile data: ", error);
      }
    }

    fetchUserProfile();
  }, []);

  async function handleProfileUpdate(updatedData) {
    try {
      await api.put("/user/profile", updatedData);
      setFirstName(updatedData.firstName);
      setLastName(updatedData.lastName);
      setPhoneNumber(updatedData.phoneNumber);
    } 
    catch (error) {
      console.error("Failed to update profile data: ", error);
    }
  }

  return (
    <div className="flex flex-col items-center w-full py-10">
      <div className="bg-gray-100 shadow-xl rounded-xl p-8 w-[90%] m-auto max-w-xl">
        <h2 className="text-3xl font-bold text-gray-800 text-center mb-6">
          User Profile
        </h2>
        <div className="flex justify-center mb-6">
          <div className="relative">
            <div className="w-28 h-28 rounded-full bg-[#f8b84d] flex items-center justify-center
               font-semibold font-[Sans-Serif] text-gray-800 text-5xl border-4 border-gray-300 shadow-md">
            {firstLetter}
          </div>
          </div>
        </div>

        <hr className="border-t border-yellow-600 my-4" />

        {/* Info Section */}
        <div className="space-y-4 text-gray-700">
          <div>
            <p className="text-sm font-bold text-gray-500">Email</p>
            <p className="text-lg font-medium break-all">{email}</p>
          </div>

          <div>
            <p className="text-sm font-bold text-gray-500">Username</p>
            <p className="text-lg font-medium">{username}</p>
          </div>

          <div className="flex gap-4">
            <div className="flex-1">
              <p className="text-sm font-bold text-gray-500">First Name</p>
              <p className="text-lg font-medium">{firstName || "< N/A >"}</p>
            </div>
            <div className="flex-1">
              <p className="text-sm font-bold text-gray-500">Last Name</p>
              <p className="text-lg font-medium">{lastName || "< N/A >"}</p>
            </div>
          </div>

          <div>
            <p className="text-sm font-bold text-gray-500">Phone Number</p>
            <p className="text-lg font-medium">{phoneNumber || "< N/A >"}</p>
          </div>
        </div>

        <hr className="border-t border-yellow-600 my-4" />

        <div className="mt-8 flex justify-center">
          <button 
            className="px-6 py-2 bg-blue-600 text-white rounded-md font-semibold cursor-pointer hover:bg-blue-700 transition"
            onClick={() => setIsModalOpen(true)}
          >
            Edit Profile
          </button>
        </div>
      </div>
      <ProfileEditModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        initialData={{firstLetter, firstName, lastName, phoneNumber}}
        onSave={handleProfileUpdate}
      />
    </div>
  );
}
