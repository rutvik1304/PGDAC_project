import { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";

const VerifyBooking = () => {
  const [booking, setBooking] = useState([]);

  const { bookingId } = useParams();
  const [bookingStatus, setBookingStatus] = useState([]);

  const [updateBookingStatus, setUpdateBookingStatus] = useState({
    bookingId: "",
    status: "",
  });

  updateBookingStatus.bookingId = bookingId;

  const retrieveAllBookingStatus = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/book/hotel/fetch/status"
    );
    return response.data;
  };

  useEffect(() => {
    const getBooking = async () => {
      const b = await retrieveBooking();
      if (b) {
        setBooking(b);
      }
    };

    const getAllBookingStatus = async () => {
      const allBookingStatus = await retrieveAllBookingStatus();
      if (allBookingStatus) {
        setBookingStatus(allBookingStatus);
      }
    };

    getAllBookingStatus();
    getBooking();
  }, []);

  const retrieveBooking = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/book/hotel/fetch/id?bookingId=" + bookingId
    );
    console.log(response.data);
    return response.data;
  };

  const handleBookingInput = (e) => {
    setUpdateBookingStatus({
      ...updateBookingStatus,
      [e.target.name]: e.target.value,
    });
  };

  let navigate = useNavigate();

  const updateHotelBookingStatus = (e) => {
    e.preventDefault();

    if (updateBookingStatus.status === "") {
      toast.error("Please select the status!!!", {
        position: "top-center",
        autoClose: 1000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
    } else {
      fetch("http://localhost:8080/api/book/hotel/update/status", {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify(updateBookingStatus),
      })
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
              navigate("/user/hotel/bookings/all");
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
    }
  };

  return (
    <div>
      <div className="mt-2 d-flex aligns-items-center justify-content-center">
        <div
          className="card form-card border-color custom-bg"
          style={{ width: "40rem" }}
        >
          <div className="card-header bg-color custom-bg-text text-center">
            <h5 className="card-title">Booking</h5>
          </div>
          <div className="card-body text-color">
            <form className="row g-3">
              <div className="col-md-6 mb-3">
                <label htmlFor="name" className="form-label">
                  <b>Booking Id</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  value={booking.bookingId}
                  required
                  readOnly
                />
              </div>

              <div className="col-md-6 mb-3">
                <label htmlFor="name" className="form-label">
                  <b>Custome Name</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  value={booking.customerName}
                  required
                  readOnly
                />
              </div>

              <div className="col-md-6 mb-3">
                <label htmlFor="name" className="form-label">
                  <b>Custome Contact</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  value={booking.customerContact}
                  required
                  readOnly
                />
              </div>

              <div className="col-md-6 mb-3">
                <label htmlFor="name" className="form-label">
                  <b>Check In</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  value={booking.checkIn}
                  required
                  readOnly
                />
              </div>

              <div className="col-md-6 mb-3">
                <label htmlFor="name" className="form-label">
                  <b>Check Out</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  value={booking.checkOut}
                  required
                  readOnly
                />
              </div>

              <div className="col-md-6 mb-3">
                <label htmlFor="name" className="form-label">
                  <b>Room No</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  value={"Room " + booking.hotelRoomId}
                  required
                  readOnly
                />
              </div>

              <div className="col-md-6 mb-3">
                <label htmlFor="name" className="form-label">
                  <b>Total Day</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  value={booking.totalDay}
                  required
                  readOnly
                />
              </div>

              <div className="col-md-6 mb-3">
                <label htmlFor="name" className="form-label">
                  <b>Total Person</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  value={booking.totalPerson}
                  required
                  readOnly
                />
              </div>

              <div className="col-md-6 mb-3">
                <label htmlFor="name" className="form-label">
                  <b>Total Amount</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  value={"Rs " + booking.totalAmount}
                  required
                  readOnly
                />
              </div>

              <div className="col-md-6 mb-3">
                <label htmlFor="name" className="form-label">
                  <b>Booking Status</b>
                </label>
                <select
                  name="status"
                  onChange={handleBookingInput}
                  className="form-control"
                >
                  <option value="">Select Status</option>

                  {bookingStatus.map((status) => {
                    return <option value={status}> {status} </option>;
                  })}
                </select>
              </div>

              <div className="d-flex aligns-items-center justify-content-center">
                <button
                  type="submit"
                  className="btn bg-color custom-bg-text col-md-4"
                  onClick={updateHotelBookingStatus}
                >
                  Update Booking Hotel
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default VerifyBooking;
