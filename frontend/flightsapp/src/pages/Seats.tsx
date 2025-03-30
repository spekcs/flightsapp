import { useAuth } from "@/lib/useAuth";
import { useNavigate, useParams, Link } from "react-router-dom";
import { useEffect } from "react";
import { Button } from "@/components/ui/button"
import { SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar";

import {
    NavigationMenu,
    NavigationMenuItem,
    NavigationMenuList,
    navigationMenuTriggerStyle,
  } from "@/components/ui/navigation-menu"
import { SeatsSidebar } from "@/components/ui/seats-sidebar";

function Seats() {
    const { id } = useParams();
    const { isLoggedIn, token } = useAuth();
    const navigate = useNavigate();

    const checkLoggedIn = () => {
        if (!isLoggedIn) {
            navigate("/login", {state: {from: `/seats/${id}`}});
        }
    }

    useEffect(() => {
        checkLoggedIn();
    }, [])

    const handleSignout = () => {
      localStorage.removeItem("jwt");
      navigate("/")
    }

    const handleSeatClick = (index) => {
        console.log(index)
    }

    const handleSubmit = () => {

    }

    return (<>
        <div className="flex justify-end pt-2 pr-2 sticky top-0 bg-background">
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
        <div className="max-w-[18rem]">
        <SidebarProvider>
        <SeatsSidebar onSubmit={handleSubmit} />
            <SidebarTrigger className="sticky z-20"/>
      </SidebarProvider>
      </div>

            <div className="absolute left-1/2 top-0 -translate-x-1/2 scale-[2] origin-top overflow-clip">
                <img src="/public/E195-seat-map.svg" alt="Seat Map" className="w-full z-10 h-auto origin-top-left" />
                {/*Left top first class*/}
                <div className="absolute gap-x-[0.3vw] gap-y-[0.5vw] top-[12.2vw] left-[20.3vw] w-[3.6vw] h-[7.5vw] grid grid-cols-2 grid-rows-4 pointer-events-none">
                    {[...Array(8)].map((_, index) => (
                        <button
                        key={index}
                        onClick={() => handleSeatClick(index)}
                        className="bg-red-100 opacity-50 hover:bg-blue-200 border border-transparent pointer-events-auto"
                        >
                        </button>
                    ))}
                </div>
                {/*Right top first class*/}
                <div className="absolute gap-x-[0.3vw] gap-y-[0.5vw] top-[12.2vw] left-[26.1vw] w-[3.6vw] h-[7.5vw] grid grid-cols-2 grid-rows-4 pointer-events-none">
                    {[...Array(8)].map((_, index) => (
                        <button
                        key={index}
                        onClick={() => handleSeatClick(index)}
                        className="bg-green-100 opacity-50 hover:bg-blue-200 border border-transparent pointer-events-auto"
                        >
                        </button>
                    ))}
                </div>
                {/*Left top business class*/}
                <div className="absolute gap-x-[0.3vw] gap-y-[0.5vw] top-[20.1vw] left-[20.3vw] w-[3.6vw] h-[5.5vw] grid grid-cols-2 grid-rows-3 pointer-events-none">
                    {[...Array(6)].map((_, index) => (
                        <button
                        key={index}
                        onClick={() => handleSeatClick(index)}
                        className="bg-red-100 opacity-50 hover:bg-blue-200 border border-transparent pointer-events-auto"
                        >
                        </button>
                    ))}
                </div>
                {/*Right top business class*/}
                <div className="absolute gap-x-[0.3vw] gap-y-[0.5vw] top-[20.1vw] left-[26.1vw] w-[3.6vw] h-[5.5vw] grid grid-cols-2 grid-rows-3 pointer-events-none">
                    {[...Array(6)].map((_, index) => (
                        <button
                        key={index}
                        onClick={() => handleSeatClick(index)}
                        className="bg-green-200 opacity-50 hover:bg-blue-200 border border-transparent pointer-events-auto"
                        >
                        </button>
                    ))}
                </div>
                {/*Left top economy class*/}
                <div className="absolute gap-x-[0.3vw] gap-y-[0.2vw] top-[25.8vw] left-[20.3vw] w-[3.6vw] h-[8.25vw] grid grid-cols-2 grid-rows-5 pointer-events-none">
                    {[...Array(10)].map((_, index) => (
                        <button
                        key={index}
                        onClick={() => handleSeatClick(index)}
                        className="bg-red-200 opacity-50 hover:bg-blue-200 border border-transparent pointer-events-auto"
                        >
                        </button>
                    ))}
                </div>
                {/*Right top economy class*/}
                <div className="absolute gap-x-[0.3vw] gap-y-[0.2vw] top-[25.8vw] left-[26.1vw] w-[3.6vw] h-[8.25vw] grid grid-cols-2 grid-rows-5 pointer-events-none">
                    {[...Array(10)].map((_, index) => (
                        <button
                        key={index}
                        onClick={() => handleSeatClick(index)}
                        className="bg-green-300 opacity-50 hover:bg-blue-200 border border-transparent pointer-events-auto"
                        >
                        </button>
                    ))}
                </div>
                {/*Left middle seats*/}
                <div className="absolute gap-x-[0.3vw] gap-y-[0.2vw] top-[35.15vw] left-[20.3vw] w-[3.6vw] h-[1.5vw] grid grid-cols-2 grid-rows-1 pointer-events-none">
                    {[...Array(2)].map((_, index) => (
                        <button
                        key={index}
                        onClick={() => handleSeatClick(index)}
                        className="bg-red-100 opacity-50 hover:bg-blue-200 border border-transparent pointer-events-auto"
                        >
                        </button>
                    ))}
                </div>
                {/*Right middle seats*/}
                <div className="absolute gap-x-[0.3vw] gap-y-[0.2vw] top-[35.15vw] left-[26.1vw] w-[3.6vw] h-[1.5vw] grid grid-cols-2 grid-rows-1 pointer-events-none">
                    {[...Array(2)].map((_, index) => (
                        <button
                        key={index}
                        onClick={() => handleSeatClick(index)}
                        className="bg-green-200 opacity-50 hover:bg-blue-200 border border-transparent pointer-events-auto"
                        >
                        </button>
                    ))}
                </div>
                {/*Left bottom seats*/}
                <div className="absolute gap-x-[0.3vw] gap-y-[0.22vw] top-[37.75vw] left-[20.3vw] w-[3.6vw] h-[34vw] grid grid-cols-2 grid-rows-20 pointer-events-none">
                    {[...Array(40)].map((_, index) => (
                        <button
                        key={index}
                        onClick={() => handleSeatClick(index)}
                        className="bg-red-100 opacity-50 hover:bg-blue-200 border border-transparent pointer-events-auto"
                        >
                        </button>
                    ))}
                </div>
                {/*Right bottom seats*/}
                <div className="absolute gap-x-[0.3vw] gap-y-[0.22vw] top-[37.75vw] left-[26.1vw] w-[3.6vw] h-[34vw] grid grid-cols-2 grid-rows-20 pointer-events-none">
                    {[...Array(40)].map((_, index) => (
                        <button
                        key={index}
                        onClick={() => handleSeatClick(index)}
                        className="bg-green-200 opacity-50 hover:bg-blue-200 border border-transparent pointer-events-auto"
                        >
                        </button>
                    ))}
                </div>
            </div>
    </div></>
        
    );
}

export default Seats;