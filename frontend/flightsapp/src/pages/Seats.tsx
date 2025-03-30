import { useParams } from "react-router-dom";

function Seats() {
    const { id } = useParams();
    return (
        <p>seats {id}</p>
    );
}

export default Seats;