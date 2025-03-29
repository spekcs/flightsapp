import {
    NavigationMenu,
    NavigationMenuItem,
    NavigationMenuList,
    navigationMenuTriggerStyle,
  } from "@/components/ui/navigation-menu"

import { FilterSidebar } from "@/components/ui/filter-sidebar";
import { SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar";

import { Link } from "react-router-dom";
import { useAuth } from "@/lib/useAuth";
import { Button } from "@/components/ui/button";
import { useEffect, useState } from "react";
import { FlightCard } from "@/components/ui/flight-card";
import  axios from "axios";

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

function Flights() {
    const { isLoggedIn: initialIsLoggedIn } = useAuth();
    const [isLoggedIn, setIsLoggedIn] = useState(initialIsLoggedIn);
    const [flights, setFlights] = useState<Flight[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [pageOffset] = useState(0);
    const [pageLimit] = useState(10);
  
    const handleSignout = () => {
      localStorage.removeItem("jwt");
      setIsLoggedIn(false);
    }
  
    useEffect(() => {
      const authStatus = localStorage.getItem("jwt");
      setIsLoggedIn(!!authStatus);
      fetchFlights();
    }, []);

    useEffect(() => {
        console.log("Flights updated:", flights)
    }, [flights])

    const fetchFlights = async (filters?: Record<string, any> ) => {
        setLoading(true);

        await axios.get("/api/flights", {
            params: filters || {},
        }).then(response => {
            setFlights(response.data.content);
            setError(null);
        console.log(response.data.content)
        }).catch(err => {
            const message =
            err.response?.data?.message && `External API Error: ${err.response?.data?.message}` ||
            err.message;
            setError(message);
            console.error(message);
        } );

        setLoading(false);
    };
  
    const handleApplyFilters = (values: { orderBy: string; departureAirport?: string | undefined; arrivalAirport?: string | undefined; airline?: string | undefined; departureDate?: Date | undefined; startHH?: string | undefined; startMM?: string | undefined; endHH?: string | undefined; endMM?: string | undefined; } | undefined) => {
        const params: Record<string, any> = {
            pageOffset,
            limit: pageLimit
        }
        if (values?.departureDate) {
            params.date = values.departureDate.toISOString().split("T")[0];
        }
        if (values?.airline) {
            params.airline = values.airline;
        }
        if (values?.startHH && values?.startMM) {
            params.timeStart = `${values.startHH}:${values.startMM}`;
        }
        if (values?.endHH && values?.endMM) {
            params.timeEnd = `${values.endHH}:${values.endMM}`;
        }
        if (values?.departureAirport) {
            params.departureAirport = values.departureAirport;
        }
        if (values?.arrivalAirport) {
            params.arrivalAirport = values.arrivalAirport;
        }
        
      fetchFlights(params);
    }

    return (<>
        <div className="flex justify-end pt-2 pr-2 sticky top-0 bg-background">
        <NavigationMenu>
          <NavigationMenuList className="flex space-x-1">
                {!isLoggedIn ? (<>
            <NavigationMenuItem>
              <Link to="/login" className={navigationMenuTriggerStyle()}>
                  Sign in
              </Link>
            </NavigationMenuItem>
            <NavigationMenuItem>
              <Link to="/register" className={navigationMenuTriggerStyle()}>
                  Sign up
              </Link>
            </NavigationMenuItem></>) : (<>
              <NavigationMenuItem>
                <Button onClick={handleSignout} variant="link">Sign out</Button>
              </NavigationMenuItem>
              <NavigationMenuItem>
              <Link to="/profile" className={navigationMenuTriggerStyle()}>
                  Profile
              </Link>
              </NavigationMenuItem></>
            )}
          </NavigationMenuList>
        </NavigationMenu>
        </div>
      <div className="flex pt-2">
        <div className="max-w-[18rem]">
        <SidebarProvider>
        <FilterSidebar onSubmit={handleApplyFilters} />
            <SidebarTrigger className="sticky top-2"/>
      </SidebarProvider></div>
      <div className="grid gap-4 flex-wrap grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4">
        {loading && <p>Loading flights...</p>}
        {error && <p className="text-red-600">{error}</p>}
        {!loading && !error && flights.map((flight) => (
            <FlightCard key={flight.flightId} flight={flight}/>
        ))}
      </div></div>
      </>
      )
    }

export default Flights;