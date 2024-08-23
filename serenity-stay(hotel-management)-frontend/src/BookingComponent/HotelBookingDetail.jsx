import { useLocation } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

const HotelBookingDetail = () => {
  const location = useLocation();
  var booking = location.state;

  let navigate = useNavigate();

  const payAndConfirm = (e) => {
    e.preventDefault();
    fetch(
      "http://localhost:8080/api/book/hotel/payment/confirmation?bookingId=" +
        booking.id,
      {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        //   body: JSON.stringify(updateBookingStatus),
      }
    )
      .then((result) => {
        console.log("result", result);
        result.json().then((res) => {
          if (res.responseCode === 0) {
            toast.success(res.responseMessage, {
              position: "top-center",
              autoClose: 1000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
            });
            navigate("/user/hotel/bookings");
          } else if (res.responseCode === 1) {
            toast.error(res.responseMessage, {
              position: "top-center",
              autoClose: 1000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
            });

            setTimeout(() => {
              window.location.reload(true);
            }, 1000); // Redirect after 3 seconds
          } else {
            toast.error("It seems server is down", {
              position: "top-center",
              autoClose: 1000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
            });

            setTimeout(() => {
              window.location.reload(true);
            }, 1000); // Redirect after 3 seconds
          }
        });
      })
      .catch((error) => {
        console.error(error);
        toast.error("It seems server is down", {
          position: "top-center",
          autoClose: 1000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
        });
      });
  };

  return (
    <div>
      {/* User Profile Card */}
      <div className="d-flex align-items-center justify-content-center ms-5 mt-1 me-5 mb-3">
        <div
          className="card rounded-card h-100 shadow-lg"
          style={{
            width: "900px",
          }}
        >
          <div className="card-body">
            <h3 className="card-title text-color-second text-center">
              Booking Detail
            </h3>
            <div className="d-flex align-items-center justify-content-center">
              <img
                src={"http://localhost:8080/api/hotel/" + booking.hotelImage}
                class="img-fluid"
                alt="product_pic"
                style={{
                  maxWidth: "250px",
                }}
              />
            </div>
            <div className="row mt-4">
              <div className="col-md-4">
                <p className="mb-2">
                  <b>Hotel Name:</b> {booking.hotelName}
                </p>
              </div>
              <div className="col-md-4">
                <p className="mb-2">
                  <b>Hotel Email:</b> {booking.hotelEmail}
                </p>
              </div>
              <div className="col-md-4">
                <p className="mb-2">
                  <b>Hotel Contact:</b> {booking.hotelContact}
                </p>
              </div>
            </div>

            <div className="row mt-4">
              <div className="col-md-4">
                <p className="mb-2">
                  <b>Booking Id:</b> {booking.bookingId}
                </p>
              </div>
              <div className="col-md-4">
                <p className="mb-2">
                  <b>Customer Name:</b> {booking.customerName}
                </p>
              </div>
              <div className="col-md-4">
                <p className="mb-2">
                  <b>Customer Contact:</b> {booking.customerContact}
                </p>
              </div>
            </div>

            <div className="row mt-4">
              <div className="col-md-4">
                <p className="mb-2">
                  <b>Check In:</b> {booking.checkIn}
                </p>
              </div>
              <div className="col-md-4">
                <p className="mb-2">
                  <b>Check Out:</b> {booking.checkOut}
                </p>
              </div>
              <div className="col-md-4">
                <p className="mb-2">
                  <b>Room No:</b> {booking.hotelRoomId}
                  {/* room no */}
                </p>
              </div>
            </div>
            <div className="row mt-4">
              <div className="col-md-4">
                <p className="mb-2">
                  <b>Total Day:</b> {booking.totalDay}
                </p>
              </div>
              <div className="col-md-4">
                <p className="mb-2">
                  <b>Room Price Per Day:</b> {booking.roomPrice}
                </p>
              </div>
              <div className="col-md-4">
                <p className="mb-2">
                  <b>Total Payable Amount:</b> {booking.totalAmount}
                  {/* room no */}
                </p>
              </div>
            </div>
            <div className="d-flex justify-content-center mt-4">
              <input
                type="submit"
                onClick={(e) => payAndConfirm(e)}
                class="btn custom-bg bg-color mb-3"
                value="Pay & Confirm Booking"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default HotelBookingDetail;
