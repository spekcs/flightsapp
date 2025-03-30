import { useAuth } from "@/lib/useAuth";
import { useNavigate, useParams, Link } from "react-router-dom";
import { useEffect } from "react";
import { Button } from "@/components/ui/button"
import { SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar";
import { useState } from "react";
import axios from "axios"
import {
    NavigationMenu,
    NavigationMenuItem,
    NavigationMenuList,
    navigationMenuTriggerStyle,
  } from "@/components/ui/navigation-menu"
import { SeatsSidebar } from "@/components/ui/seats-sidebar";

const firstClassSeatsLeft = [
    "1A", "1B", "2A", "2B", "3A", "3B", "4A", "4B"
]

const firstClassSeatsRight = [
    "1C", "1D", "2C", "2D", "3C", "3D", "4C", "4D"
]

const businessClassSeatsLeft = [
    "5A", "5B", "6A", "6B", "7A", "7B"
]

const businessClassSeatsRight = [
    "5C", "5D", "6C", "6D", "7C", "7B"
]

const economyClassSeatsTopLeft = [
    "8A", "8B", "9A", "9B", "10A", "10B", "11A", "11B", "12A", "12B"
]

const economyClassSeatsTopRight = [
    "8C", "8D", "9C", "9D", "10C", "10D", "11C", "11D", "12C", "12D"
]

const middleSeatsLeft = [
    "13A", "13B"
]

const middleSeatsRight = [
    "13C", "13D"
]

const economyClassSeatsBottomLeft = [
    "14A", "14B", "15A", "15B", "16A", "16B", "17A", "17B", "18A", "18B", "19A", "19B", "20A", "20B", "21A", "21B",
    "22A", "22B", "23A", "23B", "24A", "24B", "25A", "25B", "26A", "26B", "27A", "27B", "28A", "28B", "29A", "29B",
    "30A", "30B", "31A", "31B", "32A", "32B", "33A", "33B"
]

const economyClassSeatsDottomRight = [
    "14C", "14D", "15C", "15D", "16C", "16D", "17C", "17D", "18C", "18D", "19C", "19D", "20C", "20D", "21C", "21D",
    "22C", "22D", "23C", "23D", "24C", "24D", "25C", "25D", "26C", "26D", "27C", "27D", "28C", "28D", "29C", "29D",
    "30C", "30D", "31C", "31D", "32C", "32D", "33C", "33D"
]


type Flight = {
    flightId: number,
    departureAirport: string,
    arrivalAirport: string,
    date: string,
    departureTime: string,
    arrivalTime: string,
    flightTimeMinutes: number,
    airline: string
};

type Seat = {
    seatCode: string
}

function Seats() {

    const { id } = useParams();
    const { isLoggedIn, token } = useAuth();
    const navigate = useNavigate();
    const [flight, setFlight] = useState<Flight>();
    const [error, setError] = useState('');
    const [bookedSeats, setBookedSeats] = useState<Seat[]>([]);
    const [count, setCount] = useState(1);
    const [recommendBy, setRecommendBy] = useState<"TOGETHER" | "LEGROOM" | "WINDOW" | "EXIT">("TOGETHER");
    const [selectedSeats, setSelectedSeats] = useState<string[]>([]);
    const [seatPointer, setSeatPointer] = useState(0);


    const bookedSeatSet = new Set(bookedSeats?.map((seat) => seat.seatCode));
    const checkLoggedIn = () => {
        if (!isLoggedIn) {
            navigate("/login", {state: {from: `/seats/${id}`}});
        }
    }

   const fetchFlightInfo = async () => {
        axios.get(`/api/flights/${id}`, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        }).then((response) => {
            setFlight(response.data)
            setError('');
        }).catch((error) => {
            setError(error.message)
        });
    }

    const fetchSeats = async () => {
        axios.get(`/api/seats/${id}`, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        }).then((response) => {
            setBookedSeats(response.data)
            setError('');
        }).catch((error) => {
            setError(error.message)
        });

    }

    useEffect(() => {
        console.log(bookedSeats)
    }, [bookedSeats])

    const fetchRecommendation = async () => {
        console.log("fetching")
        axios.post(`/api/seats/recommend/${id}`, { count: count, recommendBy: recommendBy},
            {
            headers: {
                Authorization: `Bearer ${token}`
            },
        })
        .then((response) => {
            setSelectedSeats(response.data?.map((seat: Seat) => seat.seatCode))
        }
        ).catch((error) =>
            console.error(error.message)
        );
    }

    useEffect(() => {
        checkLoggedIn();
        fetchFlightInfo();
        fetchSeats();
        fetchRecommendation();
    }, [])

    const handleSignout = () => {
      localStorage.removeItem("jwt");
      navigate("/")
    }

    const handleSeatClick = (index: string) => {
        if (selectedSeats.includes(index)) {
            return;
        }
        console.log(index)
        if (selectedSeats.length < count) {
            setSelectedSeats([index, ...selectedSeats])
        } else {
            setSelectedSeats(prev => {
                const newSeats = [...prev];
                newSeats[seatPointer] = index;
                setSeatPointer((seatPointer + 1) % count)
                return newSeats;
            });
        }
    }

    useEffect(() => {
        fetchRecommendation();
    }, [count, recommendBy])

    const handleBook = () => {
        axios.post(`/api/flights/book/${id}`, { seatCodes: selectedSeats },
            {
            headers: {
                Authorization: `Bearer ${token}`
            },
        })
        .then((_) => {
            navigate("/")
        }
        ).catch((error) =>
            console.error(error.message)
        );
    }

    return (<>
        <div className="flex justify-end pt-2 pr-2 sticky top-0 bg-background z-15">
            <div>
                <Link to="/" className={navigationMenuTriggerStyle()}>Back</Link>
            </div>
        <NavigationMenu>
          <NavigationMenuList className="flex space-x-1">
              <NavigationMenuItem>
                <Button onClick={handleSignout} variant="link">Sign out</Button>
              </NavigationMenuItem>
              <NavigationMenuItem>
              <Link to="/profile" className={navigationMenuTriggerStyle()}>
                  Profile
              </Link>
              </NavigationMenuItem>
          </NavigationMenuList>
        </NavigationMenu>
        </div>
      
      <div className="flex">
        <div className="max-w-[18rem] z-20">
        <SidebarProvider>
        <SeatsSidebar flight={flight} error={error} count={count} onSubmit={handleBook} selectedSeats={selectedSeats} setCount={setCount} bookedSeatCount={bookedSeatSet.size} setRecommendBy={setRecommendBy}/>
            <SidebarTrigger className="sticky top-2"/>
      </SidebarProvider>
      </div>

            <div className="absolute left-1/2 top-0 -translate-x-1/2 scale-[2] origin-top overflow-clip">
                <img src="/public/E195-seat-map.svg" alt="Seat Map" className="w-full z-10 h-auto origin-top-left" />
                {/*Left top first class*/}
                <div className="absolute gap-x-[0.3vw] gap-y-[0.5vw] top-[12.2vw] left-[20.3vw] w-[3.6vw] h-[7.5vw] grid grid-cols-2 grid-rows-4 pointer-events-none">
                    {firstClassSeatsLeft.map((seatCode, _) => (
                        <button
                        key={seatCode}
                        onClick={() => handleSeatClick(seatCode)}
                        disabled={bookedSeatSet.has(seatCode)}
                        className={`${!bookedSeatSet.has(seatCode) ? "hover:bg-red-200" : "bg-gray-400 cursor-not-allowed"}  border border-transparent pointer-events-auto`}
                        >{bookedSeatSet.has(seatCode) && (<p className="text-[0.35vw]">{seatCode}</p>)}
                        </button>
                    ))}
                </div>
                {/*Right top first class*/}
                <div className="absolute gap-x-[0.3vw] gap-y-[0.5vw] top-[12.2vw] left-[26.1vw] w-[3.6vw] h-[7.5vw] grid grid-cols-2 grid-rows-4 pointer-events-none">
                    {firstClassSeatsRight.map((seatCode, _) => (
                        <button
                        key={seatCode}
                        disabled={bookedSeatSet.has(seatCode)}
                        onClick={() => handleSeatClick(seatCode)}
                        className={`${!bookedSeatSet.has(seatCode) ? "hover:bg-red-200" : "bg-gray-400 cursor-not-allowed"}  border border-transparent pointer-events-auto`}
                        >{bookedSeatSet.has(seatCode) && (<p className="text-[0.35vw]">{seatCode}</p>)}
                        </button>
                    ))}
                </div>
                {/*Left top business class*/}
                <div className="absolute gap-x-[0.3vw] gap-y-[0.5vw] top-[20.1vw] left-[20.3vw] w-[3.6vw] h-[5.5vw] grid grid-cols-2 grid-rows-3 pointer-events-none">
                    {businessClassSeatsLeft.map((seatCode, _) => (
                        <button
                        key={seatCode}
                        disabled={bookedSeatSet.has(seatCode)}
                        onClick={() => handleSeatClick(seatCode)}
                        className={`${!bookedSeatSet.has(seatCode) ? "hover:bg-red-200" : "bg-gray-400 cursor-not-allowed"}  border border-transparent pointer-events-auto`}
                        >{bookedSeatSet.has(seatCode) && (<p className="text-[0.35vw]">{seatCode}</p>)}
                        </button>
                    ))}
                </div>
                {/*Right top business class*/}
                <div className="absolute gap-x-[0.3vw] gap-y-[0.5vw] top-[20.1vw] left-[26.1vw] w-[3.6vw] h-[5.5vw] grid grid-cols-2 grid-rows-3 pointer-events-none">
                    {businessClassSeatsRight.map((seatCode, _) => (
                        <button
                        key={seatCode}
                        disabled={bookedSeatSet.has(seatCode)}
                        onClick={() => handleSeatClick(seatCode)}
                        className={`${!bookedSeatSet.has(seatCode) ? "hover:bg-red-200" : "bg-gray-400 cursor-not-allowed"}  border border-transparent pointer-events-auto`}
                        >{bookedSeatSet.has(seatCode) && (<p className="text-[0.35vw]">{seatCode}</p>)}
                        </button>
                    ))}
                </div>
                {/*Left top economy class*/}
                <div className="absolute gap-x-[0.3vw] gap-y-[0.2vw] top-[25.8vw] left-[20.3vw] w-[3.6vw] h-[8.25vw] grid grid-cols-2 grid-rows-5 pointer-events-none">
                    {economyClassSeatsTopLeft.map((seatCode, _) => (
                        <button
                        key={seatCode}
                        disabled={bookedSeatSet.has(seatCode)}
                        onClick={() => handleSeatClick(seatCode)}
                        className={`${!bookedSeatSet.has(seatCode) ? "hover:bg-red-200" : "bg-gray-400 cursor-not-allowed"}  border border-transparent pointer-events-auto`}
                        >{bookedSeatSet.has(seatCode) && (<p className="text-[0.35vw]">{seatCode}</p>)}
                        </button>
                    ))}
                </div>
                {/*Right top economy class*/}
                <div className="absolute gap-x-[0.3vw] gap-y-[0.2vw] top-[25.8vw] left-[26.1vw] w-[3.6vw] h-[8.25vw] grid grid-cols-2 grid-rows-5 pointer-events-none">
                    {economyClassSeatsTopRight.map((seatCode, _) => (
                        <button
                        key={seatCode}
                        disabled={bookedSeatSet.has(seatCode)}
                        onClick={() => handleSeatClick(seatCode)}
                        className={`${!bookedSeatSet.has(seatCode) ? "hover:bg-red-200" : "bg-gray-400 cursor-not-allowed"}  border border-transparent pointer-events-auto`}
                        >{bookedSeatSet.has(seatCode) && (<p className="text-[0.35vw]">{seatCode}</p>)}
                        </button>
                    ))}
                </div>
                {/*Left middle seats*/}
                <div className="absolute gap-x-[0.3vw] gap-y-[0.2vw] top-[35.15vw] left-[20.3vw] w-[3.6vw] h-[1.5vw] grid grid-cols-2 grid-rows-1 pointer-events-none">
                    {middleSeatsLeft.map((seatCode, _) => (
                        <button
                        key={seatCode}
                        disabled={bookedSeatSet.has(seatCode)}
                        onClick={() => handleSeatClick(seatCode)}
                        className={`${!bookedSeatSet.has(seatCode) ? "hover:bg-red-200" : "bg-gray-400 cursor-not-allowed"}  border border-transparent pointer-events-auto`}
                        >{bookedSeatSet.has(seatCode) && (<p className="text-[0.35vw]">{seatCode}</p>)}
                        </button>
                    ))}
                </div>
                {/*Right middle seats*/}
                <div className="absolute gap-x-[0.3vw] gap-y-[0.2vw] top-[35.15vw] left-[26.1vw] w-[3.6vw] h-[1.5vw] grid grid-cols-2 grid-rows-1 pointer-events-none">
                    {middleSeatsRight.map((seatCode, _) => (
                        <button
                        key={seatCode}
                        disabled={bookedSeatSet.has(seatCode)}
                        onClick={() => handleSeatClick(seatCode)}
                        className={`${!bookedSeatSet.has(seatCode) ? "hover:bg-red-200" : "bg-gray-400 cursor-not-allowed"}  border border-transparent pointer-events-auto`}
                        >{bookedSeatSet.has(seatCode) && (<p className="text-[0.35vw]">{seatCode}</p>)}
                        </button>
                    ))}
                </div>
                {/*Left bottom seats*/}
                <div className="absolute gap-x-[0.3vw] gap-y-[0.22vw] top-[37.75vw] left-[20.3vw] w-[3.6vw] h-[34vw] grid grid-cols-2 grid-rows-20 pointer-events-none">
                    {economyClassSeatsBottomLeft.map((seatCode, _) => (
                        <button
                        key={seatCode}
                        disabled={bookedSeatSet.has(seatCode)}
                        onClick={() => handleSeatClick(seatCode)}
                        className={`${!bookedSeatSet.has(seatCode) ? "hover:bg-red-200" : "bg-gray-400 cursor-not-allowed"}  border border-transparent pointer-events-auto`}
                        >{bookedSeatSet.has(seatCode) && (<p className="text-[0.35vw]">{seatCode}</p>)}
                        </button>
                    ))}
                </div>
                {/*Right bottom seats*/}
                <div className="absolute gap-x-[0.3vw] gap-y-[0.22vw] top-[37.75vw] left-[26.1vw] w-[3.6vw] h-[34vw] grid grid-cols-2 grid-rows-20 pointer-events-none">
                    {economyClassSeatsDottomRight.map((seatCode, _) => (
                        <button
                        key={seatCode}
                        disabled={bookedSeatSet.has(seatCode)}
                        onClick={() => handleSeatClick(seatCode)}
                        className={`${!bookedSeatSet.has(seatCode) ? "hover:bg-red-200" : "bg-gray-400 cursor-not-allowed"}  border border-transparent pointer-events-auto rounded-[0.1rem]`}
                        >{bookedSeatSet.has(seatCode) && (<p className="text-[0.35vw]">{seatCode}</p>)}
                        </button>
                    ))}
                </div>
            </div>
    </div></>
        
    );
}

export default Seats;