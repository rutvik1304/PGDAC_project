import { useState, useEffect } from "react";
import { ToastContainer, toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { useParams } from "react-router-dom";

const AddHotelFacilities = () => {
  const { hotelId } = useParams();
  const [hoteFacilities, setHoteFacilities] = useState([]); // hotel facilities
  const [facilityId, setFacilityId] = useState([]);

  const [allFacilities, setAllFacilities] = useState([]);

  let navigate = useNavigate();

  const retrieveHotelFacilities = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/facility/hotel?hotelId=" + hotelId
    );
    return response.data;
  };

  useEffect(() => {
    const getHotelFacilities = async () => {
      const allHotelFacilities = await retrieveHotelFacilities();
      if (allHotelFacilities) {
        setHoteFacilities(allHotelFacilities.facilities);
      }
    };

    getHotelFacilities();
  }, []);

  const retrieveAllFacilities = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/facility/fetch"
    );
    return response.data;
  };

  useEffect(() => {
    const getAllFacilities = async () => {
      const allFacilities = await retrieveAllFacilities();
      if (allFacilities) {
        setAllFacilities(allFacilities.facilities);
      }
    };

    getAllFacilities();
  }, []);

  const saveHotelFacility = (e) => {
    e.preventDefault();
    let data = { hotelId, facilityId };

    fetch("http://localhost:8080/api/facility/hotel/add", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    }).then((result) => {
      result.json().then((res) => {
        console.log(res);

        console.log(res.responseMessage);
      });
    });

    window.location.reload(true);
  };

  const deleteHotelFacility = (f) => {
    let index = hoteFacilities.indexOf(f);
    hoteFacilities.slice(index);
    setHoteFacilities(hoteFacilities);
  };

  return (
    <div>
      <div className="mt-2 d-flex aligns-items-center justify-content-center">
        <div
          className="card form-card border-color custom-bg"
          style={{ width: "45rem" }}
        >
          <div className="card-header bg-color text-center custom-bg-text">
            <h3 className="card-title">Add Facility</h3>
          </div>
          <div className="card-body text-color">
            <form onSubmit={saveHotelFacility}>
              <div className="mb-3">
                <label className="form-label">
                  <b>Facility</b>
                </label>

                <select
                  name="facilityId"
                  onChange={(e) => {
                    setFacilityId(e.target.value);
                  }}
                  className="form-control"
                >
                  <option value="">Select Facility</option>

                  {allFacilities.map((facility) => {
                    return (
                      <option value={facility.id}> {facility.name} </option>
                    );
                  })}
                </select>
              </div>

              <div className="d-flex justify-content-center mt-3">
                <input
                  type="submit"
                  className="btn bg-color custom-bg-text"
                  value="Add Facility"
                />
              </div>
            </form>

            <div className="mt-5 text-center">
              <h3>Selected Hotel Facilities</h3>
            </div>

            <div className="table-responsive mt-2">
              <table className="table table-hover custom-bg-text text-center">
                <thead className="bg-color table-bordered border-color">
                  <tr>
                    <th scope="col">Facility Name</th>
                    <th scope="col">Facility Description</th>
                    <th scope="col">Action</th>
                  </tr>
                </thead>
                <tbody className="text-color">
                  {hoteFacilities.map((f) => {
                    return (
                      <tr>
                        <td>
                          <b>{f.name}</b>
                        </td>
                        <td>
                          <b>{f.description}</b>
                        </td>

                        <td>
                          <button
                            className="btn bg-color custom-bg btn-sm"
                            onClick={() => deleteHotelFacility(f)}
                          >
                            Delete
                          </button>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AddHotelFacilities;
