import { useAuth } from "@/lib/useAuth";
import { useNavigate, Link } from "react-router-dom";
import { useEffect } from "react";
import { Button } from "@/components/ui/button"
import { useState } from "react";
import axios from "axios"
import {
    NavigationMenu,
    NavigationMenuItem,
    NavigationMenuList,
    navigationMenuTriggerStyle,
  } from "@/components/ui/navigation-menu"
import { Flight } from "./Flights";
import { Seat } from "./Seats";
import { Separator } from "@/components/ui/separator";

type Booking = {
    flight: Flight,
    seats: Seat[]
}

function Profile() {
    const navigate = useNavigate();
    const { token } = useAuth();
    const [bookings, setBookings] = useState<Booking[]>([]);
    const [error, setError] = useState('');

    const handleSignout = () => {
        localStorage.removeItem("jwt");
        navigate("/")
    }

    const fetchBookings = () => {
        axios.get("/api/user/bookings", {
            headers: {
                Authorization: `Bearer ${token}`
            }
        }).then((response) => {
            setBookings(response.data)
            console.log(response.data)
            setError('')
        }).catch((error) => {
            setError(error?.response?.data?.message)
        });
    }

    useEffect(() => {
        fetchBookings();
    }, [])

    return (<>
        <div className="flex justify-end pt-2 pr-2 sticky top-0 bg-background z-15">
        <NavigationMenu>
          <NavigationMenuList className="flex space-x-1">
                <NavigationMenuItem>
                    <Link to="/" className={navigationMenuTriggerStyle()}>Back</Link>
                </NavigationMenuItem>
              <NavigationMenuItem>
                <Button onClick={handleSignout} variant="link">Sign out</Button>
              </NavigationMenuItem>
          </NavigationMenuList>
        </NavigationMenu>
        </div>
        <div className="pl-6">
        {error && (<p className="text-red-500">{error}</p>)}
        <h1 className="text-2xl font-bold">Bookings</h1>
        <ul>
        {bookings.map((booking) => (
            <li>
                <div className="grid align-top gap-2 text-sm">
                    <p>Departure: {booking.flight.departureAirport} {booking.flight.departureTime}</p>
                    <p>Arrival: {booking.flight.arrivalAirport} {booking.flight.arrivalTime}</p>
                    <p>Airline: {booking.flight.airline}</p>
                    <p>Seats: {booking.seats?.map((seat) => (`${seat.seatCode} `))}</p>
        
                    <Separator/>
                </div>
            </li>
        ))}
        </ul>
        </div>
        </>);
}

export default Profile