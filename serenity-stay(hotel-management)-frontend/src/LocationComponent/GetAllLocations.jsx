import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";

const GetAllLocations = () => {
  const [locations, setLocations] = useState([]);

  const retrieveAllLocations = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/location/fetch"
    );
    return response.data;
  };

  useEffect(() => {
    const getAllLocations = async () => {
      const allLocations = await retrieveAllLocations();
      if (allLocations) {
        setLocations(allLocations.locations);
      }
    };

    getAllLocations();
  }, []);

  return (
    <div class="list-group form-card border-color">
      <Link
        to="/home/all/hotel/location"
        class="list-group-item list-group-item-action bg-color custom-bg-text"
      >
        <b>All Locations</b>
      </Link>

      {locations.map((location) => {
        return (
          <Link
            to={`/home/hotel/location/${location.id}/${location.city}`}
            class="list-group-item list-group-item-action text-color custom-bg"
          >
            <b>{location.city}</b>
          </Link>
        );
      })}
    </div>
  );
};

export default GetAllLocations;
