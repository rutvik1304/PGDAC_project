import { Link } from "react-router-dom";

const LocationNavigator = (location) => {
  console.log(location);
  return (
    <Link
      to={`/home/hotel/location/${location.item.id}/${location.item.city}`}
      style={{
        textDecoration: "none",
      }}
      className="text-color"
    >
      <b>
        {" "}
        <i>{location.item.city}</i>
      </b>
    </Link>
  );
};

export default LocationNavigator;
