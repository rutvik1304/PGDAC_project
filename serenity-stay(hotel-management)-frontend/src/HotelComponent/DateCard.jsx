const DateCard = ({ dateWithStatus }) => {
  const { day, status } = dateWithStatus;

  const cardClass = status === "Available" ? "bg-primary" : "bg-color";

  return (
    <div className="col">
      <div className="col d-flex justify-content-center align-items-center">
        <div
          className={`card ${cardClass}`}
          style={{ width: "30px", height: "30px" }}
        >
          <b className="text-color text-center">{day}</b>
        </div>
      </div>
    </div>
  );
};

export default DateCard;
