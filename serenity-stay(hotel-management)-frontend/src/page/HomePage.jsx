import Carousel from "./Carousel";
import GetAllLocations from "../LocationComponent/GetAllLocations";
import GetAllFacility from "../FacilityComponent/GetAllFacility";
import axios from "axios";
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import HotelCard from "../HotelComponent/HotelCard";
import Footer from "./Footer";

const HomePage = () => {
  const [hotels, setHotels] = useState([]);
  const { locationId } = useParams();

  useEffect(() => {
    const getAllHotels = async () => {
      const allHotels = await retrieveAllHotels();
      if (allHotels) {
        setHotels(allHotels.hotels);
      }
    };

    const getProductsByLocation = async () => {
      const allHotels = await retrieveProductsByLocation();
      if (allHotels) {
        setHotels(allHotels.hotels);
      }
    };

    if (locationId == null) {
      console.log("Location Id is null");
      getAllHotels();
    } else {
      console.log("Location Id is NOT null");
      getProductsByLocation();
    }
  }, [locationId]);

  const retrieveAllHotels = async () => {
    const response = await axios.get("http://localhost:8080/api/hotel/fetch");

    return response.data;
  };

  const retrieveProductsByLocation = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/hotel/location?locationId=" + locationId
    );

    return response.data;
  };

  return (
    <div className="container-fluid mb-2">
      <Carousel />
      <div className="mt-2 mb-5">
        <div className="row">
          <div className="col-md-2">
            <GetAllLocations />
          </div>
          <div className="col-md-8">
            <div className="row row-cols-1 row-cols-md-3 g-3">
              {hotels.map((hotel) => {
                return <HotelCard item={hotel} />;
              })}
            </div>
          </div>
          <div className="col-md-2">
            <GetAllFacility />
          </div>
        </div>
      </div>
      <hr />
      <Footer />
    </div>
  );
};

export default HomePage;
