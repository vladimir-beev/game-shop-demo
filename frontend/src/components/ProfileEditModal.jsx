import { useState, useEffect } from "react";

export default function ProfileEditModal({isOpen, onClose, initialData, onSave}) {
  const [firstName, setFirstName] = useState(initialData.firstName);
  const [lastName, setLastName] = useState(initialData.lastName);
  const [phoneNumber, setPhoneNumber] = useState(initialData.phoneNumber);

  const firstLetter = initialData?.firstLetter || "U";

  useEffect(() => {
    setFirstName(initialData?.firstName || "");
    setLastName(initialData?.lastName || "");
    setPhoneNumber(initialData?.phoneNumber || "");
  }, [initialData]);

  function handleSubmit() {
    onSave({
      firstName: firstName.trim(),
      lastName: lastName.trim(),
      phoneNumber: phoneNumber.trim(),
    });
    onClose();
  }

  if (!isOpen) {
    return null;
  }

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
      <div className="bg-gray-100 w-full max-w-lg border-4 border-yellow-500 rounded-xl shadow-xl p-8">
        <h2 className="text-2xl font-bold text-gray-800 mb-6 text-center">
          Edit Profile
        </h2>

        <div className="flex flex-col items-center mb-6">
          <div className="w-28 h-28 rounded-full bg-[#f8b84d] flex items-center justify-center
               font-semibold font-[Sans-Serif] text-gray-800 text-5xl border-4 border-gray-300 shadow-md">
            {firstLetter}
          </div>
        </div>

        <hr className="border-t border-yellow-600 my-4" />

        {/* Fields */}
        <div className="space-y-2">
          <div>
            <label className="text-sm font-semibold text-gray-600">
              First Name
            </label>
            <input
              type="text"
              className="w-full mt-1 p-2 border rounded-md"
              placeholder="Enter First Name"
              value={firstName}
              maxLength={50}
              onChange={(e) => setFirstName(e.target.value)}
            />
          </div>

          <div>
            <label className="text-sm font-semibold text-gray-600">
              Last Name
            </label>
            <input
              type="text"
              className="w-full mt-1 p-2 border rounded-md"
              placeholder="Enter Last Name"
              value={lastName}
              maxLength={50}
              onChange={(e) => setLastName(e.target.value)}
            />
          </div>

          <div>
            <label className="text-sm font-semibold text-gray-600">
              Phone Number
            </label>
            <input
              type="text"
              className="w-full mt-1 p-2 border rounded-md"
              placeholder="Enter Phone Number"
              value={phoneNumber}
              maxLength={20}
              onChange={(e) => setPhoneNumber(e.target.value)}
            />
          </div>
        </div>
        
        <div className="flex justify-end gap-3 mt-5">
          <button
            onClick={onClose}
            className="px-4 py-2 rounded-md bg-gray-300 font-semibold transition hover:bg-gray-400 cursor-pointer"
          >
            Cancel
          </button>
          <button
            onClick={handleSubmit}
            className="px-5 py-2 rounded-md bg-blue-600 text-white font-semibold transition hover:bg-blue-700 cursor-pointer"
          >
            Save Changes
          </button>
        </div>
      </div>
    </div>
  );
}
