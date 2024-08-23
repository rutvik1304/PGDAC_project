import DateCard from "./DateCard";

const HotelRoomAvailableStatus = (props) => {
  // Accessing the availableStatus and hotelRoom props from the props object
  const { availableStatus, hotelRoom } = props;

  console.log(JSON.stringify(availableStatus));

  // Get the first month (property) from the object
  const firstMonth = Object.keys(availableStatus)[0]; // This will give you the name of the first month
  const secondMonth = Object.keys(availableStatus)[1];
  const thirdMonth = Object.keys(availableStatus)[2];

  // Get the availability status object for the first month
  const availabilityForFirstMonth = availableStatus[firstMonth];
  const availabilityForSecondMonth = availableStatus[secondMonth];
  const availabilityForThirdMonth = availableStatus[thirdMonth];

  //   if (availabilityForFirstMonth) {
  //     // Loop over the availability data object
  //     Object.entries(availabilityForFirstMonth).forEach(([day, status]) => {
  //       // Perform functionality here for each day and status
  //       //   console.log(`Day ${day} has status: ${status}`);
  //     });
  //   } else {
  //     console.log("Availability data is null or undefined.");
  //   }

  const selectedRoom = "Room " + hotelRoom;

  return (
    <div>
      <h4 className="text-left">{selectedRoom}</h4>

      <div className="row row-cols-1 row-cols-md-3 g-4">
        <div className="col">
          <div className="card h-100">
            <div className="card-body">
              <h5 className="card-title">{firstMonth}</h5>

              <div className="row row-cols-4 row-cols-md-6 g-2">
                {availabilityForFirstMonth &&
                  Object.entries(availabilityForFirstMonth).map(
                    ([day, status]) => {
                      
                      return (
                        <DateCard key={day} dateWithStatus={{ day, status }} />
                      );
                    }
                  )}
              </div>
            </div>
          </div>
        </div>
        <div className="col">
          <div className="card h-100">
            <div className="card-body">
              <h5 className="card-title">{secondMonth}</h5>
              <div className="row row-cols-4 row-cols-md-6 g-2">
                {availabilityForSecondMonth &&
                  Object.entries(availabilityForSecondMonth).map(
                    ([day, status]) => {
                      // Perform functionality here for each day and status
                      console.log(`Day ${day} has status: ${status}`);
                      return (
                        <DateCard key={day} dateWithStatus={{ day, status }} />
                      );
                    }
                  )}
              </div>
            </div>
          </div>
        </div>
        <div className="col">
          <div className="card h-100">
            <div className="card-body">
              <h5 className="card-title">{thirdMonth}</h5>
              <div className="row row-cols-4 row-cols-md-6 g-2">
                {availabilityForThirdMonth &&
                  Object.entries(availabilityForThirdMonth).map(
                    ([day, status]) => {
                      // Perform functionality here for each day and status
                      console.log(`Day ${day} has status: ${status}`);
                      return (
                        <DateCard key={day} dateWithStatus={{ day, status }} />
                      );
                    }
                  )}
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="row mt-3">
        <div className="col d-flex align-items-between">
          <div
            className="card bg-color"
            style={{ width: "30px", height: "30px", marginRight: "5px" }}
          ></div>
          <b>Booked</b>

          <div
            className="card bg-primary ms-3"
            style={{ width: "30px", height: "30px", marginRight: "5px" }}
          ></div>
          <b>Available</b>
        </div>
      </div>
    </div>
  );
};

export default HotelRoomAvailableStatus;
