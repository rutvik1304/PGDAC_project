import { Link } from "react-router-dom";
import LocationNavigator from "../LocationComponent/LocationNavigator";
import HotelCarousel from "./HotelCarousel";

const HotelCard = (hotel) => {
  return (
    <div className="col">
      <div class="card border-color rounded-card card-hover product-card custom-bg h-100">
        <img
          src={"http://localhost:8080/api/hotel/" + hotel.item.image2}
          class="card-img-top rounded mx-auto d-block m-2"
          alt="img"
          style={{
            maxHeight: "270px",
            maxWidth: "100%",
            width: "auto",
          }}
        />

        {/* <HotelCarousel
      item={{
        carouselId : hotel.item.image1, 
        image1 : hotel.item.image1,
        image2 : hotel.item.image2,
        image3 : hotel.item.image3,
      }}
      /> */}

        <div class="card-body text-color">
          <h5 class="card-title d-flex justify-content-between">
            <div>
              <b>{hotel.item.name}</b>
            </div>
            <LocationNavigator
              item={{
                id: hotel.item.location.id,
                city: hotel.item.location.city,
              }}
            />
          </h5>
          <p className="card-text">{hotel.item.description}</p>
          <p class="text-color">
            <b>
              <i>Total Room :</i> {hotel.item.totalRoom}
            </b>
          </p>
        </div>
        <div class="card-footer">
          <div className="text-center text-color">
            <p>
              <span>
                <h4>Price Per Day : &#8377;{hotel.item.pricePerDay}</h4>
              </span>
            </p>
          </div>
          <div className="d-flex justify-content-center">
            <Link
              to={`/hotel/${hotel.item.id}/location/${hotel.item.location.id}`}
              className="btn bg-color custom-bg-text"
            >
              Book Now
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default HotelCard;
